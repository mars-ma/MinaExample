package dev.mars.net.message;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * Socket通信上层消息的基类
 * |0x5c|0x74|bodylength|body|
 * @author ma.xuanwei
 *
 */
public class SocketMessage implements Serializable{

	public SocketMessage(){
	}
	
	public SocketMessage(SocketMessage msg){
		setType(msg.getType());
		setBody(msg.getBody());
	}

	private static final short MAX_BODY_LENGTH = 1400;
	public static final byte HEADER1 = 0x5c;
	public static final byte HEADER2 = 0x74;
	private byte type;
	
	//定义消息类型
	public static final byte TYPE_HEART_BEAT = -1;
	public static final byte TYPE_SEND_TEXT = 0;
	public static final byte TYPE_REQUEST_SEND_FILE = 1;
	public static final byte TYPE_ACCEPT_RECEIVE_FILE = 2;
	public static final byte TYPE_REFUSE_RECEIVE_FILE = 3;
	public static final byte TYPE_SEND_FILE_PART = 4;
	public static final byte TYPE_RECEIVE_FILE_PART = 5;

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	private byte[] body;


	public byte[] getBody() {
		return body;
	}
	
	public String getJSONBody() {
		return new String(body,Charset.forName("UTF-8"));
	}

	public void setBody(byte[] bytes){
		body = bytes;
	}
	
	/**
	 * 设置消息体，一般用json解析
	 */
	public void setBody(String str) {
		this.body = str.getBytes(Charset.forName("UTF-8"));
	}
}
