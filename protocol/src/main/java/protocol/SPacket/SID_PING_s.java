package protocol.SPacket;

import protocol.ID;
import protocol.types.DWORD;

public class SID_PING_s extends _sPacket {

	public DWORD dPing_Value;

	public SID_PING_s() {
		super(ID.SID_PING);
		dPing_Value = new DWORD(new byte[] { (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff });
	}

	@Override
	protected void prepareData() {
		setData(dPing_Value.getByteArray());
	}

}
