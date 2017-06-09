package dev.mars.net.message;

import org.json.JSONObject;

public class AcceptReceiveFileMessage extends SocketMessage{
	
	public AcceptReceiveFileMessage(SocketMessage msg) {
		super(msg);
	}
	
	public AcceptReceiveFileMessage(long id){
		setType(TYPE_ACCEPT_RECEIVE_FILE);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		setBody(jsonObject.toString());
	}
	
	public long getId(){
		JSONObject json  = new JSONObject(getJSONBody());
		return json.getLong("id");
	}
}
