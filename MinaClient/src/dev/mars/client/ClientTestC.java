package dev.mars.client;

import java.beans.FeatureDescriptor;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import dev.mars.net.FileSender;
import dev.mars.net.FileSender.SendFileListener;
import dev.mars.net.message.RequestSendFileMessage;
import dev.mars.net.message.RequestSendFileMessage.FileInfo;
import dev.mars.net.message.SocketMessage;
import dev.mars.net.mina.BaseCodecFactory;
import dev.mars.net.mina.handler.FileMessageHandler;
import dev.mars.net.mina.handler.FileTask;
import dev.mars.net.mina.handler.SocketMessageDispatcher;
import dev.mars.util.FileCheck;
import dev.mars.util.GzipUtil;
import dev.mars.util.LogUtils;

public class ClientTestC {
	public static void main(String[] args) {

		NioSocketConnector connector = new NioSocketConnector(Runtime
				.getRuntime().availableProcessors() + 1);
		connector.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE,
				Constants.READ_IDLE_TIMEOUT);
		connector.getSessionConfig().setIdleTime(IdleStatus.WRITER_IDLE,
				Constants.WRITE_IDLE_TIMEOUT);
		connector.getSessionConfig().setWriteTimeout(2000);
		connector.getSessionConfig().setTcpNoDelay(true);

		connector.getFilterChain().addLast("BaseFilter",
				new ProtocolCodecFilter(new BaseCodecFactory()));
		// 设置连接超时检查时间
		connector.getFilterChain().addLast(
				"threadpool",
				new ExecutorFilter(Executors.newFixedThreadPool(Runtime
						.getRuntime().availableProcessors() + 1)));
		connector.setConnectTimeoutCheckInterval(5000);
		connector.setConnectTimeoutMillis(10000); // 10秒后超时
		final SocketMessageDispatcher socketMessageDispatcher = new SocketMessageDispatcher();
		connector.setHandler(new IoHandlerAdapter() {
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
			public void messageSent(IoSession session, Object message)
					throws Exception {
				super.messageSent(session, message);

			}

			@Override
			public void messageReceived(final IoSession session,
					final Object message) throws Exception {
				super.messageReceived(session, message);
				socketMessageDispatcher.handleMessage(session,
						(SocketMessage) message);
			}

			@Override
			public void exceptionCaught(IoSession session, Throwable cause)
					throws Exception {
				super.exceptionCaught(session, cause);
				cause.printStackTrace();
				session.closeNow();
			}
		});

		FileMessageHandler.setDownloadPath("G:\\client_download");
		ConnectFuture connectFuture = connector.connect(new InetSocketAddress(
				Constants.REMOTE_IP, Constants.PORT));
		connectFuture.addListener(new IoFutureListener<ConnectFuture>() {
			@Override
			public void operationComplete(final ConnectFuture ioFuture) {
				if (ioFuture.isDone() && ioFuture.isConnected()) {
					LogUtils.DT("已建立连接");
					String localFilePath = "G:\\test2.pdf";
					FileSender fileSender =new FileSender(ioFuture.getSession());
					fileSender.sendFile(localFilePath, new SendFileListener() {
						@Override
						public void onSucess() {
							System.out.println("发送文件成功");
						}
						
						@Override
						public void onFailed(String hint) {
							// TODO Auto-generated method stub
							System.out.println("发送文件失败:"+hint);
						}
					});
				}
			}
		});

	}

}
