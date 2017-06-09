package dev.mars.net.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.json.JSONObject;

public class SendFilePartMessage extends SocketMessage {

	public SendFilePartMessage(long id, int partId, byte[] data) throws IOException {
		setType(TYPE_SEND_FILE_PART);
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("partId", partId);
		byte[] jsonBody = json.toString().getBytes("UTF-8");
		ByteBuffer buffer = ByteBuffer.allocate(jsonBody.length + 2 + data.length);
		buffer.putShort((short) jsonBody.length);
		buffer.put(jsonBody);
		buffer.put(data);
		setBody(buffer.array());
	}

	public SendFilePartMessage(SocketMessage msg) {
		super(msg);
	}

	public FilePart getFilePart() {
		ByteBuffer byteBuffer = ByteBuffer.wrap(getBody());
		short jsonLength = byteBuffer.getShort();
		byte[] jsonBytes = new byte[jsonLength];
		byteBuffer.get(jsonBytes);
		String jsonStr = new String(jsonBytes, Charset.forName("UTF-8"));
		byte[] data = new byte[getBody().length - jsonLength - 2];
		byteBuffer.get(data);
		JSONObject json = new JSONObject(jsonStr);
		FilePart filePart = new FilePart();
		filePart.id = json.getLong("id");
		filePart.partId = json.getInt("partId");
		filePart.data = data;
		return filePart;
	}

	public static class FilePart {
		public long id;
		public int partId;
		public byte[] data;
	}
}
