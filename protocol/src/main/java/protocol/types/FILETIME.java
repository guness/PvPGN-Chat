package protocol.types;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;

public class FILETIME implements Concatable {
	private ByteArray data;

	public FILETIME(byte b) {
		throw new ArrayIndexOutOfBoundsException(1);
	}

	public FILETIME(byte[] data) {
		if (data.length != 8) {
			throw new ArrayIndexOutOfBoundsException(data.length);
		} else {
			this.data = new ByteArray(data);
		}
	}

	public FILETIME(String text) {
		if (text.length() != 8) {
			throw new ArrayIndexOutOfBoundsException(data.length);
		} else {
			this.data = new ByteArray(text);
		}
	}

	public FILETIME(byte[] data, int beginIndex, int endIndex) {
		if (endIndex - beginIndex - 1 != 8) {
			throw new ArrayIndexOutOfBoundsException(data.length);
		} else {
			this.data = new ByteArray(data, beginIndex, endIndex);
		}
	}

	public static FILETIME readFILETIME(BNetInputStream bis) throws IOException {
		byte[] data = new byte[8];
		bis.read(data);
		return new FILETIME(data);
	}

	@Override
	public ByteArray getByteArray() {
		return data;
	}
}
