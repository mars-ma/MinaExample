package dev.mars.net.mina.handler;

import org.apache.mina.core.session.IoSession;

import dev.mars.net.message.SocketMessage;

public interface MessageHandler {
	public void handleMessage(SocketMessage msg,IoSession session);
}
