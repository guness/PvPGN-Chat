package protocol.RPacket;

import java.io.IOException;

import protocol.ID;
import protocol.core.Engine;
import protocol.types.VOID;

import net.bnubot.util.BNetInputStream;

public class SID_unknown_r extends _rPacket {

	public SID_unknown_r(byte messageID) {
		super(messageID);
		Class<? extends _rPacket> c = ID.rPackets.get(messageID);
		Engine.printLog('w', "SID_unknown_r",
		        "This packet is not implemented: " + (c == null ? messageID + "" : c.getName()));
	}

	public VOID data;

	@Override
	protected void readData(BNetInputStream bis) throws IOException {
		data = VOID.readVOID(bis, length);
	}
}
