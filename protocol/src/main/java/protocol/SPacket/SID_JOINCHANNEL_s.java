package protocol.SPacket;

import protocol.ID;
import protocol.core.Channel;
import protocol.types.DWORD;
import protocol.types.STRING;

public class SID_JOINCHANNEL_s extends _sPacket {

	DWORD dFlags;
	STRING sChannel;

	public SID_JOINCHANNEL_s(byte channelJoinFlag, String channel) {
		super(ID.SID_JOINCHANNEL);
		dFlags = new DWORD(new byte[] { channelJoinFlag, 0, 0, 0 });
		sChannel = new STRING(channel);
		Channel.getInstance().init(channel);
	}

	@Override
	protected void prepareData() {
		setData(dFlags.getByteArray().concat(sChannel));
	}

}
