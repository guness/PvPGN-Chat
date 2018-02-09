package protocol.SPacket;

import protocol.ID;

public class SID_FRIENDSUPDATE_s extends _sPacket {

	public byte Index_on_friends_list;

	public SID_FRIENDSUPDATE_s() {
		super(ID.SID_FRIENDSUPDATE);
	}

	@Override
	protected void prepareData() {
		setData(new byte[] { Index_on_friends_list });
	}
}
