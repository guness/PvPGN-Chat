package protocol.types;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;

public class DWORD implements Concatable {
	private ByteArray data;

	public DWORD(byte b) {
		throw new ArrayIndexOutOfBoundsException(1);
	}

	public DWORD(int doubleword) {
		this.data = new ByteArray(new byte[] {
				(byte) (doubleword & 0x000000FF),
				(byte) ((doubleword & 0x0000FF00) >> 8),
				(byte) ((doubleword & 0x00FF0000) >> 16),
				(byte) ((doubleword & 0xFF000000) >> 24) });
	}

	public DWORD(byte[] data) {
		if (data.length != 4) {
			throw new ArrayIndexOutOfBoundsException(data.length);
		} else {
			this.data = new ByteArray(data);
		}
	}

	public DWORD(String text) {
		if (text.length() != 4) {
			throw new ArrayIndexOutOfBoundsException(data.length);
		} else {
			this.data = new ByteArray(text);
		}
	}

	public DWORD(byte[] data, int beginIndex, int endIndex) {
		if (endIndex - beginIndex - 1 != 4) {
			throw new ArrayIndexOutOfBoundsException(data.length);
		} else {
			this.data = new ByteArray(data, beginIndex, endIndex);
		}
	}

	public static DWORD readDWORD(BNetInputStream bis) throws IOException {
		byte[] data = new byte[4];
		bis.read(data);
		return new DWORD(data);
	}

	@Override
	public ByteArray getByteArray() {
		return data;
	}

	public int intValue() {
		byte data[] = this.data.getBytes();
		return ((data[0] << 0) & 0x000000FF) | ((data[1] << 8) & 0x0000FF00)
				| ((data[2] << 16) & 0x00FF0000)
				| ((data[3] << 24) & 0xFF000000);

	}
}
