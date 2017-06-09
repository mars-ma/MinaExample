package dev.mars.net;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.core.session.IoSession;

import dev.mars.net.message.RequestSendFileMessage;
import dev.mars.net.mina.handler.FileMessageHandler;
import dev.mars.net.mina.handler.FileTask;
import dev.mars.util.FileCheck;

public class FileSender {

	private static ExecutorService mExecutorService = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private IoSession ioSession;

	public FileSender(IoSession ioSession)  {
		this.ioSession = ioSession;
	}
	
	public void sendFile(final String filePath,SendFileListener sendFileListener){
		final File localFile = new File(filePath);
		if (!localFile.exists()) {
			System.out.println("本地文件不存在");
			throw new IllegalArgumentException("文件不存在");
		}
		
		mExecutorService.execute(new Runnable() {
			@Override
			public void run() {

				FileTask fileTask = new FileTask();
				fileTask.fileSegmentSize=Constants.FILE_SEGMENT_SIZE;
				fileTask.filePath = filePath;
				try {
					fileTask.md5 = new FileCheck().getFileMD5String(localFile);
					fileTask.id = System.currentTimeMillis();
					fileTask.fileName = localFile.getName();
					fileTask.startTime = fileTask.id;
					fileTask.size = localFile.length();
					
					boolean zipped = FileMessageHandler.zipFile(fileTask);
					if(zipped){
						FileMessageHandler.getFileTasks().put(fileTask.id, fileTask);
						fileTask.startTime2=System.currentTimeMillis();
						RequestSendFileMessage requestSendFileMessage = new RequestSendFileMessage(fileTask);
						ioSession.write(requestSendFileMessage);
						System.out.println("请求发送文件 " + fileTask.filePath + " id " + fileTask.id);
					}else{
						System.out.println("压缩失败");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}
	
	public interface SendFileListener{
		public void onSucess();
		
		public void onFailed(String hint);
	}
	

}
