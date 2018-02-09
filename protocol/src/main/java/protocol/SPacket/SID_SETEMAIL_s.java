package protocol.SPacket;

import protocol.ID;
import protocol.types.STRING;

public class SID_SETEMAIL_s extends _sPacket {

	public STRING sEmail_Address;

	public SID_SETEMAIL_s() {
		super(ID.SID_SETEMAIL);
	}

	@Override
	protected void prepareData() {
		setData(sEmail_Address.getByteArray());
	}

}
