package dev.mars.net.mina.handler;

import java.util.concurrent.atomic.AtomicInteger;

public class FileTask {
	public boolean running = true;
	public long id;
	public String zippedFilePath;
	public long zippedFileSize;
	public String fileName;
	public String filePath;
	public String md5;
	public long startTime;
	public long startTime2; //网络计时
	public long size;
	public int fileSegmentSize;
	public AtomicInteger partId = new AtomicInteger(0);
}
