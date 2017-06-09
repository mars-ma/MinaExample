package dev.mars.net.message;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.json.JSONObject;

import dev.mars.net.mina.handler.FileMessageHandler;
import dev.mars.net.mina.handler.FileTask;
import dev.mars.util.FileCheck;

public class RequestSendFileMessage extends SocketMessage{
	
	
	public FileInfo getFileInfo(){
		JSONObject json = new JSONObject(getJSONBody());
		FileInfo fileInfo = new FileInfo();
		fileInfo.fileName = json.getString("fileName");
		fileInfo.fileSize = json.getLong("size");
		fileInfo.md5 = json.getString("md5");
		fileInfo.id = json.getLong("id");
		fileInfo.fileSegmentSize=json.getInt("fileSegmentSize");
		fileInfo.zippedFileSize = json.getLong("zippedFileSize");
		return fileInfo;
		
	}
	
	public RequestSendFileMessage(FileTask fileTask) throws IOException{
		setType(TYPE_REQUEST_SEND_FILE);
		JSONObject json=new JSONObject();
		json.put("fileName", fileTask.fileName);
		json.put("md5", fileTask.md5);
		json.put("size", fileTask.size);
		json.put("id", fileTask.id);
		json.put("zippedFileSize", fileTask.zippedFileSize);
		json.put("fileSegmentSize", fileTask.fileSegmentSize);
		setBody(json.toString().getBytes(Charset.forName("UTF-8")));
	}

	public RequestSendFileMessage(SocketMessage msg) {
		super(msg);
	}

	public static class FileInfo{
		public String fileName;
		public String md5;
		public long fileSize;
		public long id;
		public long zippedFileSize;
		public int fileSegmentSize;
	}
}
