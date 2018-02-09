/**
 * This file is distributed under the GPL
 * $Id: UnloggedException.java 1701 2008-12-10 17:08:14Z scotta $
 */
package net.bnubot.util;

/**
 * @author scotta
 */
public class UnloggedException extends IllegalStateException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an UnloggedException with the specified detail message. A
	 * detail message is a String that describes this particular exception.
	 * 
	 * @param s
	 *            the String that contains a detailed message
	 */
	public UnloggedException(String s) {
		super(s);
	}
}
