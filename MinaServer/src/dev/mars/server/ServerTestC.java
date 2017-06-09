package dev.mars.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import dev.mars.net.message.RequestSendFileMessage;
import dev.mars.net.message.RequestSendFileMessage.FileInfo;
import dev.mars.net.message.SocketMessage;
import dev.mars.net.mina.BaseCodecFactory;
import dev.mars.net.mina.handler.FileMessageHandler;
import dev.mars.net.mina.handler.FileTask;
import dev.mars.net.mina.handler.SocketMessageDispatcher;
import dev.mars.util.LogUtils;

public class ServerTestC {


	public static void main(String[] args) {
		NioSocketAcceptor acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, Constants.READ_IDLE_TIMEOUT);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.WRITER_IDLE, Constants.WRITE_IDLE_TIMEOUT);
		acceptor.getSessionConfig().setWriteTimeout(2000);
		acceptor.getSessionConfig().setTcpNoDelay(true);
		
		acceptor.getFilterChain().addLast("BaseFilter", new ProtocolCodecFilter(new BaseCodecFactory()));
		// 设置连接超时检查时间

		acceptor.getFilterChain().addLast("threadpool",
				new ExecutorFilter(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1)));
		final SocketMessageDispatcher socketMessageDispatcher = new SocketMessageDispatcher();
		acceptor.setHandler(new IoHandlerAdapter() {
			@Override
			public void sessionClosed(IoSession session) throws Exception {
				super.sessionClosed(session);
				LogUtils.DT("session:" + session.getId() + " 已关闭");
			}

			@Override
			public void sessionOpened(IoSession session) throws Exception {
				// TODO Auto-generated method stub
				super.sessionOpened(session);
				LogUtils.DT("session:" + session.getId() + " 已建立");
			}

			@Override
			public void messageSent(IoSession session, Object message) throws Exception {
				super.messageSent(session, message);
				SocketMessage msg = (SocketMessage) message;
			}

			@Override
			public void messageReceived(final IoSession session, final Object message) throws Exception {
				super.messageReceived(session, message);
				socketMessageDispatcher.handleMessage(session, (SocketMessage) message);
			}

			@Override
			public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
				super.exceptionCaught(session, cause);
				cause.printStackTrace();
				session.closeNow();
			}
		});
		FileMessageHandler.setDownloadPath("G:\\server_download");
		try {
			acceptor.bind(new InetSocketAddress(Constants.PORT));
			System.out.println("已绑定 "+Constants.PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

}
