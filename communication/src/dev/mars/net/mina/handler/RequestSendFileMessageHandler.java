package dev.mars.net.mina.handler;

import java.io.File;
import java.io.IOException;

import org.apache.mina.core.session.IoSession;

import dev.mars.net.message.AcceptReceiveFileMessage;
import dev.mars.net.message.RefuseReceiveFileMessage;
import dev.mars.net.message.RequestSendFileMessage;
import dev.mars.net.message.RequestSendFileMessage.FileInfo;
import dev.mars.net.message.SocketMessage;
import dev.mars.util.FileUtil;

public class RequestSendFileMessageHandler extends FileMessageHandler {

	@Override
	public void handleMessage(SocketMessage msg, IoSession session) {
		RequestSendFileMessage requestSendFileMessage = new RequestSendFileMessage(
				msg);
		FileInfo fileInfo = requestSendFileMessage.getFileInfo();
		System.out.println("收到接收文件请求 : " + fileInfo.fileName + " id "
				+ fileInfo.id);
		long totalUsedSpace = fileInfo.fileSize+fileInfo.zippedFileSize;
		System.out.println("预计占用空间 : " + totalUsedSpace + " Bytes ");

		File dir = new File(DOWNLOAD_PATH);
		if (!dir.exists()) {
			if (dir.mkdirs())
				try {
					System.out.println("创建下载目录:" + dir.getCanonicalPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if (!dir.exists()) {
			System.out.println("无法创建下载目录");
			RefuseReceiveFileMessage refuseReceiveFileMessage = new RefuseReceiveFileMessage(
					fileInfo.id, -1, "server error");
			session.write(refuseReceiveFileMessage);
			System.out.println("拒绝接收文件");
			return;
		}
		if (FileUtil.checkAvailableSize(new File(DOWNLOAD_PATH),
				totalUsedSpace)) {
			FileTask fileTask = new FileTask();
			fileTask.startTime = System.currentTimeMillis();
			fileTask.filePath = DOWNLOAD_PATH + File.separatorChar
					+ fileTask.startTime + "_" + fileInfo.fileName;
			fileTask.md5 = fileInfo.md5;
			fileTask.id = fileInfo.id;
			fileTask.fileName = fileInfo.fileName;
			fileTask.zippedFileSize = fileInfo.zippedFileSize;
			fileTask.zippedFilePath = DOWNLOAD_PATH + File.separatorChar
					+ System.currentTimeMillis() + "_zipped_"
					+ fileInfo.fileName;
			fileTask.size = fileInfo.fileSize;
			fileTask.fileSegmentSize = fileInfo.fileSegmentSize;
			if (FileUtil.deleteFile(fileTask.zippedFilePath)
					&& FileUtil.deleteFile(fileTask.filePath)) {
				mFileTasks.put(fileInfo.id, fileTask);
				AcceptReceiveFileMessage acceptReceiveFileMessage = new AcceptReceiveFileMessage(
						fileInfo.id);
				session.write(acceptReceiveFileMessage);
				System.out.println("同意接收文件:" + fileInfo.fileName + " 临时存放路径:"
						+ fileTask.zippedFilePath + " 目标路径 "
						+ fileTask.filePath);
			} else {
				System.out.println("无法删除:"+fileTask.zippedFilePath+" 或者 "+fileTask.filePath);
				RefuseReceiveFileMessage refuseReceiveFileMessage = new RefuseReceiveFileMessage(fileInfo.id, 1, "服务器文件系统异常");
				session.write(refuseReceiveFileMessage);
				return;
			}
		} else {
			System.out.println(DOWNLOAD_PATH + " 内存不足 ");
			RefuseReceiveFileMessage refuseReceiveFileMessage = new RefuseReceiveFileMessage(
					fileInfo.id, 0, "服务器硬盘不足");
			session.write(refuseReceiveFileMessage);
			System.out.println("拒绝接收文件");
		}

	}

}
