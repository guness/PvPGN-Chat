/**
 * This file is distributed under the GPL
 * $Id: ReverseCrypto.java 1590 2008-08-27 18:24:10Z scotta $
 */
package net.bnubot.util.crypto;

import protocol.types.ByteArray;

/**
 * @author scotta
 */
public class ReverseCrypto {
	private static boolean isSpecial(byte b) {
		if ((b >= 'a') && (b <= 'z'))
			return false;
		if ((b >= 'A') && (b <= 'Z'))
			return false;
		return true;
	}

	public static ByteArray decode(ByteArray data) {
		byte[] nonSpecial = new byte[data.length()];
		int nsPos = 0;
		for (byte b : data.getBytes())
			if (!isSpecial(b))
				nonSpecial[nsPos++] = b;
		byte[] dataOut = new byte[data.length()];
		int pos = 0;
		for (byte b : data.getBytes()) {
			if (isSpecial(b))
				dataOut[pos++] = b;
			else
				dataOut[pos++] = nonSpecial[--nsPos];
		}
		return new ByteArray(dataOut);
	}

	public static ByteArray encode(ByteArray data) {
		return decode(data);
	}
}
