package dev.mars.net.mina.handler;

import org.apache.mina.core.session.IoSession;

import dev.mars.net.message.SocketMessage;

public class SocketMessageDispatcher {

	public void handleMessage(IoSession session, SocketMessage msg) {
		int type = msg.getType();
		MessageHandlerFactory.getHandler(type).handleMessage(msg, session);
	}
}
