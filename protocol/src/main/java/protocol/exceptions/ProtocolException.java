package protocol.exceptions;

import protocol.core.Engine;

public class ProtocolException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4032425801546431587L;
	private static final String TAG = "ProtocolException";

	public ProtocolException(String string) {
		Engine.printLog('e', TAG, "ProtocolException: " + string);
	}
}
