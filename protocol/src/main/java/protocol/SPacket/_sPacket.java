package protocol.SPacket;

import java.io.IOException;

import protocol.core.Engine;
import protocol.types.ByteArray;



import net.bnubot.util.BNetOutputStream;

public abstract class _sPacket {
	private byte messageID;
	private byte protocolByte;
	private ByteArray data;

	public _sPacket(byte messageID) {
		this.messageID = messageID;
		protocolByte = (byte) 0xff;
	}

	protected void setData(byte[] data) {
		this.data = new ByteArray(data);
	}

	protected void setData(ByteArray data) {
		this.data = data;
	}

	/**
	 * you have to call setData() in this method
	 */
	protected abstract void prepareData();

	public synchronized void sendPacket(BNetOutputStream os) throws IOException {
		prepareData();
		Engine.printLog('i', getClass().getName(), "length: " + data.length);
		if (this instanceof SID_AUTH_INFO_s) {
			os.write(0x01);
		}
		os.write(protocolByte);
		os.write(messageID);
		os.writeWord(data.length + 4);
		os.write(data.getBytes());
		os.flush();
	}

	public static byte[] reverse(byte[] arr) {
		byte bt[] = new byte[arr.length];
		for (int i = 0; i < bt.length; i++) {
			bt[i] = arr[arr.length - i - 1];
		}
		return bt;
	}
}
