package dev.mars.net.message;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;

public class ReceiveFileFinishMessage extends SocketMessage {
	public ReceiveFileFinishMessage(SocketMessage msg) {
		super(msg);
	}

	public ReceiveFileFinishMessage(long id,boolean success) throws UnsupportedEncodingException {
		setType(TYPE_RECEIVE_FILE_PART);
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("success", success);
		setBody(json.toString());
	}
	
	public ReceiveFileInfo getReceiveFileInfo(){
		JSONObject json = new JSONObject(getJSONBody());
		ReceiveFileInfo receiveFileInfo = new ReceiveFileInfo();
		receiveFileInfo.id = json.getLong("id");
		receiveFileInfo.success = json.getBoolean("success");
		return receiveFileInfo;
	}

	public static class ReceiveFileInfo{
		public long id;
		public boolean success;
	}
}
