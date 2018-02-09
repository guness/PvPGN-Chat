package protocol.types;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;

public class STRING implements Concatable {
	private ByteArray data;

	public STRING(byte b) {
		throw new ArrayIndexOutOfBoundsException(1);
	}

	public STRING(byte[] data) {
		if (data[data.length - 1] == 0) {
			this.data = new ByteArray(data);
		} else {
			this.data = new ByteArray(new String(data) + (char) 0);
		}
	}

	public STRING(String text) {
		if (text.length() == 0) {
			this.data = new ByteArray((byte) 0);
		} else if (text.charAt(text.length() - 1) == 0) {
			this.data = new ByteArray(text);
		} else {
			this.data = new ByteArray(text + (char) 0);
		}
	}

	public STRING(byte[] data, int beginIndex, int endIndex) {
		if (data[endIndex] == 0) {
			this.data = new ByteArray(data);
		} else {
			this.data = new ByteArray(new String(data, beginIndex, endIndex
					- beginIndex)
					+ (char) 0);
		}
	}

	public static STRING readSTRING(BNetInputStream bis) throws IOException {
		String text = "";
		int i = 0;
		while (0 != (i = bis.read())) {
			text += (char) i;
		}
		text += (char) 0;
		return new STRING(text);
	}

	@Override
	public ByteArray getByteArray() {
		return data;
	}

	@Override
	public String toString() {
		return new String(data.getBytes(), 0, data.getBytes().length - 1);
	}
}
