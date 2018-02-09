package protocol.SPacket;

import protocol.ID;

public class SID_AUTH_ACCOUNTLOGONPROOF_s extends _sPacket {

	public byte[] Client_Password_Proof = new byte[20];

	public SID_AUTH_ACCOUNTLOGONPROOF_s() {
		super(ID.SID_AUTH_ACCOUNTLOGONPROOF);
	}

	@Override
	protected void prepareData() {
		setData(Client_Password_Proof);
	}

}
