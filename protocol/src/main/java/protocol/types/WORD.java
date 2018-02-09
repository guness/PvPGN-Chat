package protocol.types;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;

public class WORD implements Concatable {
	private ByteArray data;

	public WORD(byte b) {
		throw new ArrayIndexOutOfBoundsException(1);
	}

	public WORD(byte[] data) {
		if (data.length != 2) {
			throw new ArrayIndexOutOfBoundsException(data.length);
		} else {
			this.data = new ByteArray(data);
		}
	}

	public WORD(String text) {
		if (text.length() != 2) {
			throw new ArrayIndexOutOfBoundsException(data.length);
		} else {
			this.data = new ByteArray(text);
		}
	}

	public WORD(byte[] data, int beginIndex, int endIndex) {
		if (endIndex - beginIndex - 1 != 2) {
			throw new ArrayIndexOutOfBoundsException(data.length);
		} else {
			this.data = new ByteArray(data, beginIndex, endIndex);
		}
	}

	public static WORD readWORD(BNetInputStream bis) throws IOException {
		byte[] data = new byte[2];
		bis.read(data);
		return new WORD(data);
	}

	@Override
	public ByteArray getByteArray() {
		return data;
	}

	public int intValue() {
		return (((data.byteAt(0) << 0) & 0x00FF) | ((data.byteAt(1) << 8) & 0xFF00));
	}
}
