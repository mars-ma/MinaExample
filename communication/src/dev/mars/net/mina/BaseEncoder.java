package dev.mars.net.mina;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import dev.mars.net.message.SocketMessage;

public class BaseEncoder extends ProtocolEncoderAdapter{

	@Override
	public void encode(IoSession session, Object obj, ProtocolEncoderOutput output)
			throws Exception {
		SocketMessage msg = (SocketMessage) obj;
		IoBuffer buffer = IoBuffer.allocate(100).setAutoExpand(true);
		buffer.order(ByteOrder.BIG_ENDIAN);
		buffer.put(SocketMessage.HEADER1);
		buffer.put(SocketMessage.HEADER2);
		buffer.put(msg.getType());
		if(msg.getType()!=SocketMessage.TYPE_HEART_BEAT){
			byte[] body = msg.getBody();
			short bodyLength = (short) body.length;
			buffer.putShort(bodyLength);
			buffer.put(body);
		}
		buffer.flip(); 		
		output.write(buffer); 
	}
}
