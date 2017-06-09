package dev.mars.net.mina.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.core.session.IoSession;

import dev.mars.net.message.SendFilePartMessage;
import dev.mars.util.FileUtil;
import dev.mars.util.GzipUtil;

public abstract class FileMessageHandler implements MessageHandler {
	protected static ConcurrentHashMap<Long, FileTask> mFileTasks = new ConcurrentHashMap<>();
	private static ExecutorService mExecutorService = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	protected static String DOWNLOAD_PATH;

	public static void setDownloadPath(String path) {
		DOWNLOAD_PATH = path;
	}

	protected FileTask removeFileTask(long id) {
		System.out.println("移除FileTask " + id);
		return mFileTasks.remove(id);
	}

	public static ConcurrentHashMap<Long, FileTask> getFileTasks() {
		return mFileTasks;
	}

	protected void handleFileTaskException(long id, IoSession session) {
		session.closeNow();
		FileTask task = removeFileTask(id);
		if (task != null) {
			task.running = false;
			FileUtil.deleteFile(task.zippedFilePath);
		}
	}

	public static boolean zipFile(FileTask fileTask) {
		File tempFile = new File(DOWNLOAD_PATH);
		if (!tempFile.exists()) {
			if (!tempFile.mkdirs()) {
				return false;
			} else {
				System.out.println("创建临时目录:" + DOWNLOAD_PATH);
			}
		}
		// 将源文件压缩
		fileTask.zippedFilePath = DOWNLOAD_PATH + File.separatorChar
				+ System.currentTimeMillis() + "_" + fileTask.fileName;

		System.out.println("开始压缩原文件");
		long startTime = System.currentTimeMillis();
		try {
			GzipUtil.zipFile(fileTask.filePath, fileTask.zippedFilePath);
			fileTask.zippedFileSize = new File(fileTask.zippedFilePath)
					.length();
			System.out.println("压缩结束 ， 耗时:"
					+ (System.currentTimeMillis() - startTime) + " ms");
			long zippedFileSize = new File(fileTask.zippedFilePath).length();
			System.out.println("原文件大小:" + new File(fileTask.filePath).length()
					+ " 压缩后大小:" + zippedFileSize);
			fileTask.zippedFileSize = zippedFileSize;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected void sendFilePart(final FileTask fileTask, final IoSession session) {
		try {

			final int totalPart = (int) Math.ceil((double) fileTask.zippedFileSize/ fileTask.fileSegmentSize);
			int partId = 0;
			System.out.println("总计"+totalPart+"个分段");
			while (partId < totalPart) {
				if (fileTask.running) {
					mExecutorService.execute(new SendFilePartThread(partId,
							fileTask, session, totalPart));
				}
				partId++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			session.closeNow();
			handleFileTaskException(fileTask.id, session);
		}
	}

	private class SendFilePartThread implements Runnable {
		int pardIdLocal ;
		FileTask fileTask;
		IoSession session;
		int totalPart;

		public SendFilePartThread(int partId, final FileTask fileTask,
				final IoSession session, int totalPart) {
			pardIdLocal = partId;
			this.fileTask = fileTask;
			this.session = session;
			this.totalPart = totalPart;
		}

		@Override
		public void run() {
			try {
				RandomAccessFile randomAccessFile = new RandomAccessFile(
						fileTask.zippedFilePath, "rw");
				byte[] buffer = new byte[fileTask.fileSegmentSize];
				int availableSize;
				randomAccessFile.seek(pardIdLocal * fileTask.fileSegmentSize);
				availableSize = randomAccessFile.read(buffer);
				randomAccessFile.close();

				if (availableSize == buffer.length) {
					SendFilePartMessage sendFileMessage = new SendFilePartMessage(
							fileTask.id, pardIdLocal, buffer);
					session.write(sendFileMessage);
				} else {
					byte[] actualPart = new byte[availableSize];
					for (int i = 0; i < availableSize; i++) {
						actualPart[i] = buffer[i];
					}
					SendFilePartMessage sendFileMessage = new SendFilePartMessage(
							fileTask.id, pardIdLocal, actualPart);
					session.write(sendFileMessage);
				}
				if (fileTask.partId.incrementAndGet() == totalPart-1) {
					System.out.println("发送完毕");
					
					double sendTime = (System.currentTimeMillis() - fileTask.startTime2) / 1000d;
					System.out.println("发送文件用时:" + sendTime + " s");
					System.out
							.println("平均速度:"
									+ ((new File(fileTask.zippedFilePath).length() / 1024d) / sendTime)
									+ " kbps");
				}else if(fileTask.partId.intValue() % 100==0){
					System.out.println("tid:"+Thread.currentThread().getId()+" 发送进度: "+fileTask.partId.intValue()+" / "+totalPart);
				}
			} catch (Exception e) {
				e.printStackTrace();
				handleFileTaskException(fileTask.id, session);
			}
		}

	}
}
