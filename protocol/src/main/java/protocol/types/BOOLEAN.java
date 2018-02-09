package protocol.types;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;

public class BOOLEAN implements Concatable {

	private ByteArray data;

	public BOOLEAN(boolean b) {
		if (b) {
			data = new ByteArray(new byte[] { 1, 0, 0, 0 });
		} else {
			data = new ByteArray(new byte[] { 0, 0, 0, 0 });
		}
	}

	public BOOLEAN(byte[] data) {
		if (data.length != 4) {
			throw new ArrayIndexOutOfBoundsException(data.length);
		} else {
			this.data = new ByteArray(data);
		}
	}

	@Override
	public ByteArray getByteArray() {
		return data;
	}

	public static BOOLEAN readBOOLEAN(BNetInputStream bis) throws IOException {
		byte[] data = new byte[4];
		bis.read(data);
		return new BOOLEAN(data);
	}

	public boolean boolValue() {
		return data.byteAt(0) == 1;
	}
}
