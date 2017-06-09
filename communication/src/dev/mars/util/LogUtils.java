package dev.mars.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils {
	public static boolean ENABLE = true;
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	public static void DT(String log) {
		if (ENABLE)
			System.out.println(sdf.format(new Date()) + " " + log);
	}

}
