package dev.mars.net.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import dev.mars.net.message.SocketMessage;


public class BaseDecoder extends CumulativeProtocolDecoder{
	@Override
	protected boolean doDecode(IoSession session,IoBuffer in,
							   ProtocolDecoderOutput out) throws Exception {
		if(in.remaining()>=3){
			in.mark(); // 标记当前位置，方便reset
			byte[] header = new byte[2];
			in.get(header, 0, header.length);
			if(header[0]== SocketMessage.HEADER1 && header[1]== SocketMessage.HEADER2){
				byte type=in.get();
				//System.out.println("类型 :"+type);
				if(type==SocketMessage.TYPE_HEART_BEAT){
					SocketMessage msg = new SocketMessage();
					msg.setType(SocketMessage.TYPE_HEART_BEAT);
					out.write(msg);
					if(in.remaining()>=3){
						return true;
					}
				}else{
					if(in.remaining()>=2){
						short bodyLength = in.getShort();
						if(in.remaining()>=bodyLength){
							byte[] body = new byte[bodyLength];
							in.get(body, 0, bodyLength);
							SocketMessage msg = new SocketMessage();
							msg.setType(type);
							msg.setBody(body);
							out.write(msg);
							if(in.remaining()>=3){
								//再来一遍
								return true; 
							}
						}else{
							//长度不够
							in.reset();
						}
					}else{
						//长度不够
						in.reset();
					}
				}
			}else{
				System.err.println("HEADER[0] = "+header[0]+" , HEADER[1] = "+header[1]);
				throw new IllegalArgumentException("错误的HEADER");
			}
		}
		return false;
	}


}
