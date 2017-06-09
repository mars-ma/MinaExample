package dev.mars.net.message;

import org.json.JSONObject;

public class RefuseReceiveFileMessage extends SocketMessage {
	
	public RefuseReceiveFileMessage(SocketMessage msg) {
		// TODO Auto-generated constructor stub
		super(msg);
	}
	
	public RefuseReceiveFileMessage(long id,int errorCode,String des) {
		// TODO Auto-generated constructor stub
		setType(TYPE_REFUSE_RECEIVE_FILE);
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("errorCode", errorCode);
		json.put("des", des);
		setBody(json.toString());
	}
	
	public RefuseInfo getRefuseInfo(){
		String jsonStr = getJSONBody();
		JSONObject json = new JSONObject(jsonStr);
		RefuseInfo refuseInfo = new RefuseInfo();
		refuseInfo.errorCode=json.getInt("errorCode");
		refuseInfo.id = json.getLong("id");
		refuseInfo.description = json.getString("des");
		return refuseInfo;
	}
	
	public static class RefuseInfo{
		public long id;
		public int errorCode;
		public String description;
	}
}
