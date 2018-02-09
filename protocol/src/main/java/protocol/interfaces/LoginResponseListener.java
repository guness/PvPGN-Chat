package protocol.interfaces;


public interface LoginResponseListener {
	public void logonSuccess();
	/**
	 * 0: InvalidPasswordException
	 * 2: AccountDoesNotExist
	 * 3: AccountClosed
	 * 8: ProtocolError
	 * @param error
	 */
	public void logonFailed(int error);
	
	public void channelsLoaded();

}
