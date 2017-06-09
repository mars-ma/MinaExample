package dev.mars.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtil {
	/**
	 * 对文件进行压缩
	 * 
	 * @param source
	 *            源文件
	 * @param target
	 *            目标文件
	 * @throws IOException
	 */
	public static void zipFile(String source, String target) throws IOException {
		FileInputStream fin = null;
		FileOutputStream fout = null;
		GZIPOutputStream gzout = null;
		try {
			fin = new FileInputStream(source);
			fout = new FileOutputStream(target);
			gzout = new GZIPOutputStream(fout);
			byte[] buf = new byte[1024];
			int num;
			while ((num = fin.read(buf)) != -1) {
				gzout.write(buf, 0, num);
			}
		} finally {
			if (gzout != null)
				gzout.close();
			if (fout != null)
				fout.close();
			if (fin != null)
				fin.close();
		}
	}

	/**
	 * 解压文件
	 * 
	 * @param source源文件
	 * @param target目标文件
	 * @throws IOException
	 */
	public static void unZipFile(String source, String target) throws IOException {
		FileInputStream fin = null;
		GZIPInputStream gzin = null;
		FileOutputStream fout = null;
		try {
			fin = new FileInputStream(source);
			gzin = new GZIPInputStream(fin);
			fout = new FileOutputStream(target);
			byte[] buf = new byte[1024];
			int num;
			while ((num = gzin.read(buf, 0, buf.length)) != -1) {
				fout.write(buf, 0, num);
			}
		} finally {
			if (fout != null)
				fout.close();
			if (gzin != null)
				gzin.close();
			if (fin != null)
				fin.close();
		}
	}
}
