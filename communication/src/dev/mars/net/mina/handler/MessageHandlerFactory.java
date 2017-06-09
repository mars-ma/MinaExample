package dev.mars.net.mina.handler;

import dev.mars.net.message.SocketMessage;

public class MessageHandlerFactory {
	public static MessageHandler getHandler(int type){
		MessageHandler handler =null;
		switch (type) {
		case SocketMessage.TYPE_REQUEST_SEND_FILE:
			handler = new RequestSendFileMessageHandler();
			break;
		case SocketMessage.TYPE_ACCEPT_RECEIVE_FILE:
			handler = new AcceptReceiveFileMessageHandler();
			break;
		case SocketMessage.TYPE_REFUSE_RECEIVE_FILE:
			handler = new RefuseReceiveFileMessageHandler();
			break;
		case SocketMessage.TYPE_SEND_FILE_PART:
			handler = new SendFilePartMessageHandler();
			break;
		case SocketMessage.TYPE_RECEIVE_FILE_PART:
			handler = new ReceiveFileFinishMessageHandler();
			break;
		}
		return handler;
	}
}
