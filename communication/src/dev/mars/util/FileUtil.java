package dev.mars.util;

import java.io.File;

public class FileUtil {

	public static boolean checkAvailableSize(File dir, long size) {
		return dir.getUsableSpace() >= size;
	}

	public static boolean deleteFile(String path){
		File f = new File(path);
		return f.exists()?f.delete():true;
	}
}
