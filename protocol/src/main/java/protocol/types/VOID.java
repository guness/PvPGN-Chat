package protocol.types;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;

public class VOID implements Concatable {
	private ByteArray data;

	public VOID(byte b) {
		this.data = new ByteArray(new byte[] { b });
	}

	public VOID(byte[] data) {
		this.data = new ByteArray(data);
	}

	public VOID(String text) {
		this.data = new ByteArray(text);
	}

	public VOID(byte[] data, int beginIndex, int endIndex) {
		this.data = new ByteArray(data, beginIndex, endIndex);
	}

	public static VOID readVOID(BNetInputStream bis, int length)
			throws IOException {
		byte b[] = new byte[length];
		int count = bis.read(b);
		if (length == count) {
			return new VOID(b);
		} else {
			byte bb[] = new byte[count];
			System.arraycopy(b, 0, bb, 0, count);
			System.out
					.println("Some error occured and we could not read enough data");
			return new VOID(bb);
		}
	}

	@Override
	public ByteArray getByteArray() {
		return data;
	}
}
