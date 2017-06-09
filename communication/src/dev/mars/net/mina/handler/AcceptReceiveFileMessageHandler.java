package dev.mars.net.mina.handler;

import java.io.File;
import java.io.IOException;

import org.apache.mina.core.session.IoSession;

import dev.mars.net.message.AcceptReceiveFileMessage;
import dev.mars.net.message.SocketMessage;
import dev.mars.util.GzipUtil;

public class AcceptReceiveFileMessageHandler extends FileMessageHandler {

	@Override
	public void handleMessage(SocketMessage msg, IoSession session) {
		AcceptReceiveFileMessage requestSendFileMessage = new AcceptReceiveFileMessage(
				msg);
		long id = requestSendFileMessage.getId();
		FileTask fileTask = mFileTasks.get(id);
		if (fileTask == null) {
			System.err.println("wrong file task id " + id);
			session.closeOnFlush();
		} else {
			sendFilePart(fileTask, session);
		}
	}

}
