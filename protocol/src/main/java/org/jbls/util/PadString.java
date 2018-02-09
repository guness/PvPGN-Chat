/**
 * $Id: PadString.java 1608 2008-09-12 17:35:39Z scotta $
 */
package org.jbls.util;

/**
 * Since Java has no utility (that I could find) for converting an integer to a
 * String and padding it with the appropriate number of 0's, I wrote this class
 * that takes care of that for me.
 * 
 * @author iago
 */
public class PadString {
	// Pads a number with 0's up to the requested length.
	static public String padNumber(int number, int length) {
		return padString("" + number, length, '0');
	}

	static public String padHex(byte number, int length) {
		return padHex(number & 0xFF, length);
	}

	// Converts the number to hex, then pads it with 0's up to the requested
	// length.
	static public String padHex(int number, int length) {
		return padString(Integer.toHexString(number), length, '0');
	}

	// Pads the requested string to the requested length with the specified
	// character.
	static public String padString(String str, int length, char c) {
		while (str.length() < length)
			str = c + str;
		return str;
	}
}
