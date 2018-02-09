package protocol.SPacket;

import protocol.ID;
import protocol.types.ByteArray;
import protocol.types.STRING;

public class SID_AUTH_ACCOUNTLOGON_s extends _sPacket {

	public byte[] Client_Key = new byte[32];
	public STRING sUsername;

	public SID_AUTH_ACCOUNTLOGON_s() {
		super(ID.SID_AUTH_ACCOUNTLOGON);
	}

	@Override
	protected void prepareData() {
		ByteArray data = new ByteArray(Client_Key);
		data = data.concat(sUsername);
		setData(data);
	}
}
