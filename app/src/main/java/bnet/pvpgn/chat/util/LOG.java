package bnet.pvpgn.chat.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

import protocol.interfaces.Logger;
import android.util.Log;

public class LOG implements Logger {

	private static LOG instance;

	public static LOG getInstance() {
		if (instance == null) {
			instance = new LOG();
		}
		return instance;
	}

	public void E(String TAG, String msg) {
		appendLog("E " + TAG + "\t" + msg);
		Log.e(TAG, msg);
	}

	public void D(String TAG, String msg) {
		appendLog("D " + TAG + "\t" + msg);
		Log.d(TAG, msg);
	}

	public void W(String TAG, String msg) {
		appendLog("W " + TAG + "\t" + msg);
		Log.w(TAG, msg);
	}

	public void WTF(String TAG, String msg) {
		appendLog("WTF " + TAG + "\t" + msg);
		Log.wtf(TAG, msg);
	}

	public void I(String TAG, String msg) {
		appendLog("I " + TAG + "\t" + msg);
		Log.i(TAG, msg);
	}

	public void V(String TAG, String msg) {
		appendLog("V " + TAG + "\t" + msg);
		Log.v(TAG, msg);
	}

	private static void appendLog(String text) {
		File logFile = new File(OtherUtils.getLogFile());
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				return;
			}
		}
		try {
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.append(OtherUtils.getNow() + "\t" + text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void P(String TAG, Throwable e) {
		String str = "\r\n*** START EXCEPTION *** "
				+ "\r\n"
				+ new SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.getDefault())
						.format(new Timestamp(System.currentTimeMillis()))
				+ "\r\n" + "Message: " + e.getMessage() + "\r\n" + "Cause: "
				+ e.getCause() + "\r\n" + "Name: " + e.getClass().getName();
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement ee : e.getStackTrace()) {
			sb.append(ee.toString() + "\r\n");
		}
		e.printStackTrace();
		str += sb.toString() + "\r\n" + "*** END EXCEPTION ***" + "\r\n";
		instance.E(TAG, str);
	}
}
