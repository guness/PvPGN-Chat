package protocol.SPacket;

import protocol.ID;

public class SID_FRIENDSLIST_s extends _sPacket {

	public SID_FRIENDSLIST_s() {
		super(ID.SID_FRIENDSLIST);
	}

	@Override
	protected void prepareData() {
		setData(new byte[] {});
	}
}
