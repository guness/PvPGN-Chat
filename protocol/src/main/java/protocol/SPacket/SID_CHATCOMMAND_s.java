package protocol.SPacket;

import protocol.ID;
import protocol.types.STRING;

public class SID_CHATCOMMAND_s extends _sPacket {

	public STRING sText;

	public SID_CHATCOMMAND_s() {
		super(ID.SID_CHATCOMMAND);
	}

	@Override
	protected void prepareData() {
		setData(sText.getByteArray());
	}

}
