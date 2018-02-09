package protocol.interfaces;

public interface Logger {
	public void E(String TAG, String msg);

	public void D(String TAG, String msg);

	public void W(String TAG, String msg);

	public void V(String TAG, String msg);

	public void WTF(String TAG, String msg);

	public void I(String TAG, String msg);

	public void P(String TAG, Throwable e);
}
