package bnet.pvpgn.chat.util;

import java.io.File;
import java.util.Calendar;
import android.os.Environment;

import bnet.pvpgn.chat.C;

public class OtherUtils {
	public static String getNow() {
		Calendar rightNow = Calendar.getInstance();
		String date = "";
		date += rightNow.get(Calendar.DAY_OF_MONTH) > 9 ? rightNow
				.get(Calendar.DAY_OF_MONTH) : "0"
				+ rightNow.get(Calendar.DAY_OF_MONTH);
		date += ".";
		date += rightNow.get(Calendar.MONTH) + 1 > 9 ? rightNow
				.get(Calendar.MONTH) + 1 : "0"
				+ (rightNow.get(Calendar.MONTH) + 1);
		date += ".";
		date += rightNow.get(Calendar.YEAR);
		date += " ";
		date += rightNow.get(Calendar.HOUR_OF_DAY) > 9 ? rightNow
				.get(Calendar.HOUR_OF_DAY) : "0"
				+ rightNow.get(Calendar.HOUR_OF_DAY);
		date += ":";
		date += rightNow.get(Calendar.MINUTE) > 9 ? rightNow
				.get(Calendar.MINUTE) : "0" + rightNow.get(Calendar.MINUTE);
		return date;
	}

	public static String getLogFile() {
		return Environment.getExternalStorageDirectory().getPath()
				+ File.separator + C.LOG_FILE;
	}

}
