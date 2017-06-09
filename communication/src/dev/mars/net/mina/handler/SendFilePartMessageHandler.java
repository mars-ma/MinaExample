package dev.mars.net.mina.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.RandomAccess;

import org.apache.mina.core.session.IoSession;

import dev.mars.net.message.ReceiveFileFinishMessage;
import dev.mars.net.message.SendFilePartMessage;
import dev.mars.net.message.SendFilePartMessage.FilePart;
import dev.mars.net.message.SocketMessage;
import dev.mars.util.FileCheck;
import dev.mars.util.FileUtil;
import dev.mars.util.GzipUtil;

public class SendFilePartMessageHandler extends FileMessageHandler {

	@Override
	public void handleMessage(SocketMessage msg, IoSession session) {
		SendFilePartMessage sendFileMessage = new SendFilePartMessage(msg);
		FilePart filePart = sendFileMessage.getFilePart();
		FileTask fileTask = mFileTasks.get(filePart.id);
		//System.out.println("tid:"+Thread.currentThread().getId()+" filePartId:"+filePart.partId);
		if (fileTask == null) {
			System.err.println("wrong file task id " + fileTask.id);
			handleFileTaskException(fileTask.id, session);
		} else if (filePart.partId != filePart.partId) {
			System.err.println("wrong file task id " + fileTask.id);
			handleFileTaskException(fileTask.id, session);
		} else {
			try {
				RandomAccessFile randomAccessFile = new RandomAccessFile(
						fileTask.zippedFilePath, "rw");
				long beginIndex = fileTask.fileSegmentSize * filePart.partId;
				randomAccessFile.seek(beginIndex);
				// System.out.println("file length = "+randomAccessFile.length()+" , beginIndex = "+beginIndex);
				randomAccessFile.write(filePart.data);
				randomAccessFile.close();

				// 不支持并发写入
				// FileOutputStream fos = new
				// FileOutputStream(fileTask.zippedFilePath, true);
				// fos.write(filePart.data);
				// fos.flush();
				// fos.close();

				int partId = fileTask.partId.incrementAndGet();
				// 最大传输4G的文件
				int totalPart = (int) Math
						.ceil((double) fileTask.zippedFileSize
								/ fileTask.fileSegmentSize);
				// System.out.println("已传输"+partId+"个分段,总计 "+totalPart);
				if (partId == totalPart) {
					System.out.println("文件 " + fileTask.zippedFilePath
							+ " 接收完毕 文件总计" + fileTask.zippedFileSize + "字节");
					System.out.println("开始解压");
					long startTime = System.currentTimeMillis();
					GzipUtil.unZipFile(fileTask.zippedFilePath,
							fileTask.filePath);
					System.out.println("解压结束 ， 耗时:"
							+ (System.currentTimeMillis() - startTime) + " ms");
					System.out.println("原文件大小:"
							+ new File(fileTask.filePath).length() + " 压缩文件大小:"
							+ new File(fileTask.zippedFilePath).length());
					System.out.println("原MD5:" + fileTask.md5);
					String md5 = new FileCheck().getFileMD5String(new File(
							fileTask.filePath));
					System.out.println("下载文件md5:" + md5);
					boolean check = fileTask.md5.equals(md5);
					if (check) {
						System.out.println("校验通过");
					} else {
						System.out.println("校验失败");
						FileUtil.deleteFile(fileTask.filePath);
					}
					FileUtil.deleteFile(fileTask.zippedFilePath);
					removeFileTask(fileTask.id);
					ReceiveFileFinishMessage receiveFileMessage = new ReceiveFileFinishMessage(
							fileTask.id,check);
					session.write(receiveFileMessage);
				} else if(partId%100==0){
					System.out.println("接收进度: " + partId + " / "
							+ totalPart);
				}

			} catch (Exception e) {
				e.printStackTrace();
				handleFileTaskException(fileTask.id, session);
			}
		}
	}

}
