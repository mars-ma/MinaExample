package dev.mars.net.mina.handler;

import org.apache.mina.core.session.IoSession;

import dev.mars.net.message.RefuseReceiveFileMessage;
import dev.mars.net.message.RefuseReceiveFileMessage.RefuseInfo;
import dev.mars.net.message.SocketMessage;

public class RefuseReceiveFileMessageHandler extends FileMessageHandler{

	@Override
	public void handleMessage(SocketMessage msg, IoSession session) {
		RefuseReceiveFileMessage refuseReceiveFileMessage = new RefuseReceiveFileMessage(msg);
		RefuseInfo refuseInfo = refuseReceiveFileMessage.getRefuseInfo();
		
		System.out.println("服务器拒绝接收文件 errorCode : "+refuseInfo.errorCode+" des : "+refuseInfo.description);
		removeFileTask(refuseInfo.id);
	}

}
