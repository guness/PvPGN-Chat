package protocol.types;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;

public class QWORD implements Concatable {
	private ByteArray data;

	public QWORD(byte b) {
		throw new ArrayIndexOutOfBoundsException(1);
	}

	public QWORD(long quadword) {
		int low = (int) ((quadword >> 0l) & 0xFFFFFFFF);
		int high = (int) ((quadword >> 32l) & 0xFFFFFFFF);
		this.data = new ByteArray(new byte[] { (byte) (low & 0x000000FF),
				(byte) ((low & 0x0000FF00) >> 8),
				(byte) ((low & 0x00FF0000) >> 16),
				(byte) ((low & 0xFF000000) >> 24), (byte) (high & 0x000000FF),
				(byte) ((high & 0x0000FF00) >> 8),
				(byte) ((high & 0x00FF0000) >> 16),
				(byte) ((high & 0xFF000000) >> 24) });
	}

	public QWORD(byte[] data) {
		if (data.length != 8) {
			throw new ArrayIndexOutOfBoundsException(data.length);
		} else {
			this.data = new ByteArray(data);
		}
	}

	public QWORD(String text) {
		if (text.length() != 8) {
			throw new ArrayIndexOutOfBoundsException(data.length);
		} else {
			this.data = new ByteArray(text);
		}
	}

	public QWORD(byte[] data, int beginIndex, int endIndex) {
		if (endIndex - beginIndex - 1 != 8) {
			throw new ArrayIndexOutOfBoundsException(data.length);
		} else {
			this.data = new ByteArray(data, beginIndex, endIndex);
		}
	}

	public static QWORD readQWORD(BNetInputStream bis) throws IOException {
		byte[] data = new byte[8];
		bis.read(data);
		return new QWORD(data);
	}

	@Override
	public ByteArray getByteArray() {
		return data;
	}
}
