/**
 * This file is distributed under the GPL
 * $Id: ByteArray.java 1619 2008-09-16 16:15:45Z scotta $
 */
package protocol.types;

import java.io.UnsupportedEncodingException;

/**
 * @author scotta
 */
public final class ByteArray implements Concatable {
	private final byte[] data;
	public final int length;

	public ByteArray(byte b) {
		this.data = new byte[] { b };
		length = 1;
	}

	public ByteArray(byte[] data) {
		this.data = data.clone();
		length = data.length;
	}

	public ByteArray(String text) {
		byte[] data;
		try {
			data = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			data = text.getBytes();
		}
		this.data = data;
		length = data.length;
	}

	public ByteArray(byte[] data, int beginIndex, int endIndex) {
		this.data = new byte[endIndex - beginIndex];
		System.arraycopy(data, beginIndex, this.data, 0, this.data.length);
		length = data.length;
	}

	public final byte[] getBytes() {
		return data;
	}

	public ByteArray concat(byte[] str) {
		byte[] out = new byte[data.length + str.length];
		System.arraycopy(data, 0, out, 0, data.length);
		System.arraycopy(str, 0, out, data.length, str.length);
		return new ByteArray(out);
	}

	public ByteArray concat(ByteArray str) {
		return concat(str.data);
	}

	public ByteArray concat(Concatable str) {
		return concat(str.getByteArray());
	}

	public ByteArray concat(ByteArray[] arrays) {
		byte[] out = null;
		int length = data.length;
		for (ByteArray str : arrays) {
			length += str.length();
		}
		out = new byte[length];
		int offset = 0;
		System.arraycopy(data, 0, out, offset, data.length);
		offset = data.length;
		for (ByteArray str : arrays) {
			System.arraycopy(str.getBytes(), 0, out, offset, str.length);
			offset += str.length();
		}
		return new ByteArray(out);
	}

	public ByteArray concat(Concatable[] arrays) {
		byte[] out = null;
		int length = data.length;
		for (Concatable str : arrays) {
			length += str.getByteArray().length();
		}
		out = new byte[length];
		int offset = 0;
		System.arraycopy(data, 0, out, offset, data.length);
		offset = data.length;
		for (Concatable str : arrays) {
			System.arraycopy(str.getByteArray().getBytes(), 0, out, offset, str
					.getByteArray().length());
			offset += str.getByteArray().length();
		}
		return new ByteArray(out);
	}

	public ByteArray removeFirst() {
		return substring(1);
	}

	@Override
	public String toString() {
		try {
			return new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return new String(data);
		}
	}

	public int length() {
		return data.length;
	}

	public byte byteAt(int i) {
		return data[i];
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof byte[]) {
			byte[] x = (byte[]) obj;
			if (x.length != data.length)
				return false;
			for (int i = 0; i < x.length; i++) {
				if (x[i] != data[i])
					return false;
			}
			return true;
		}
		if (obj instanceof ByteArray)
			return equals(((ByteArray) obj).data);
		if (obj instanceof String)
			return toString().equals(obj);
		return false;
	}

	public ByteArray substring(int beginIndex) {
		return substring(beginIndex, data.length);
	}

	public ByteArray substring(int beginIndex, int endIndex) {
		return new ByteArray(data, beginIndex, endIndex);
	}

	@Override
	public ByteArray getByteArray() {
		return this;
	}
}
