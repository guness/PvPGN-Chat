package protocol.RPacket;

import java.io.IOException;

import net.bnubot.util.BNetInputStream;
import protocol.ID;
import protocol.core.Engine;
import protocol.exceptions.ProtocolException;

public abstract class _rPacket {
	public byte messageID;
	protected int length;

	public _rPacket(byte messageID) {
		this.messageID = messageID;
	}

	protected abstract void readData(BNetInputStream is) throws IOException;

	private void readHeader(BNetInputStream is) throws IOException, ProtocolException {
		is.readByte();
		byte b = is.readByte();
		if (b != messageID) {
			int available = is.available();
			byte[] bytes = new byte[available];
			for (int i = 0; i < 140 && available > 0; i += available) {
				is.read(bytes, 0, available);
				String str = new String(bytes, "UTF-8");
				Engine.printLog('e', getClass().getName(), str);
			}
			throw new ProtocolException("Expected value is: " + messageID + ", we got " + b);
		}
		length = (((is.readByte() << 0) & 0x00FF) | ((is.readByte() << 8) & 0xFF00));
		Engine.printLog('i', getClass().getName(), "length: " + length);
	}

	public synchronized void readPacket(BNetInputStream is) throws IOException, ProtocolException {
		readHeader(is);
		readData(is);
		Engine.printLog('i', getClass().getName(), "length: " + length);
	}

	public synchronized void readPacketWithoutHeader(BNetInputStream is) throws IOException {
		length = (((is.readByte() << 0) & 0x00FF) | ((is.readByte() << 8) & 0xFF00));
		readData(is);
		Engine.printLog('i', getClass().getName(), "length: " + length);
	}

	public static _rPacket readFullPacket(BNetInputStream is) throws IOException, ProtocolException {
		if (is.readByte() != (byte) 0xff) {
			throw new ProtocolException("Wrong packet start.");
		}
		byte packetId = is.readByte();
		Class<? extends _rPacket> packetClass = ID.rPackets.get(packetId);
		if (packetClass == null) {
			throw new ProtocolException("Class not found.");
		}
		_rPacket rPacket = null;
		try {
			rPacket = packetClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		rPacket.readPacketWithoutHeader(is);
		return rPacket;
	}
}
