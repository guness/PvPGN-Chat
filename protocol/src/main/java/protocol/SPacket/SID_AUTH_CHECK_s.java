package protocol.SPacket;

import protocol.ID;
import protocol.VersionInfo;
import protocol.types.BOOLEAN;
import protocol.types.ByteArray;
import protocol.types.Concatable;
import protocol.types.DWORD;
import protocol.types.STRING;

public class SID_AUTH_CHECK_s extends _sPacket {
    public VersionInfo mVersionInfo;

    public DWORD dClient_Token, dEXE_Version, dEXE_Hash, dNumber_of_CD_keys_in_this_packet;
    public BOOLEAN bSpawn_CD_key;

    // For Each Key:

    public DWORD[] dKey_Length, dCD_key_s_product_value, dCD_key_s_public_value, dUnknown, dHashed_Key_Data[];

    public STRING sExe_Information, sCD_Key_owner_name;

    public SID_AUTH_CHECK_s(VersionInfo versionInfo) {
        super(ID.SID_AUTH_CHECK);
        mVersionInfo = versionInfo;
        dClient_Token = new DWORD(new byte[]{0x2a, (byte) 0xff, 0x63, 0x51});
        dEXE_Version = new DWORD(mVersionInfo.exeVersion);
        dEXE_Hash = new DWORD(mVersionInfo.exeHash);

        dNumber_of_CD_keys_in_this_packet = new DWORD(new byte[]{1, 0, 0, 0});
        bSpawn_CD_key = new BOOLEAN(false);
        int num = dNumber_of_CD_keys_in_this_packet.intValue();
        dKey_Length = new DWORD[num];
        dCD_key_s_product_value = new DWORD[num];
        dCD_key_s_public_value = new DWORD[num];
        dUnknown = new DWORD[num];
        dHashed_Key_Data = new DWORD[num][5];

        dKey_Length[0] = new DWORD(new byte[]{0x30, 0x30, 0x30, 0x30});
        dCD_key_s_product_value[0] = new DWORD(new byte[]{0x30, 0x30, 0x30, 0x30});
        dCD_key_s_public_value[0] = new DWORD(new byte[]{0x30, 0x30, 0x30, 0x30});
        dUnknown[0] = new DWORD(new byte[]{0x30, 0x30, 0x30, 0x30});

        dHashed_Key_Data[0][0] = new DWORD(new byte[]{0x30, 0x30, 0x30, 0x30});
        dHashed_Key_Data[0][1] = new DWORD(new byte[]{0x30, 0x30, 0x30, 0x30});
        dHashed_Key_Data[0][2] = new DWORD(new byte[]{0x30, 0x30, 0x30, 0x30});
        dHashed_Key_Data[0][3] = new DWORD(new byte[]{0x30, 0x30, 0x30, 0x30});
        dHashed_Key_Data[0][4] = new DWORD(new byte[]{0x30, 0x30, 0x30, 0x30});

        sExe_Information = new STRING(versionInfo.exeInfo);

        sCD_Key_owner_name = new STRING("Osman");
    }

    @Override
    protected void prepareData() {
        ByteArray data = dClient_Token.getByteArray().concat(
                new Concatable[]{dEXE_Version, dEXE_Hash, dNumber_of_CD_keys_in_this_packet, bSpawn_CD_key});
        for (int i = 0; i < dNumber_of_CD_keys_in_this_packet.intValue(); i++) {
            data = data.concat(new Concatable[]{dKey_Length[i], dCD_key_s_product_value[i],
                    dCD_key_s_public_value[i], dUnknown[i]});
            data = data.concat(dHashed_Key_Data[i]);
        }
        data = data.concat(new Concatable[]{sExe_Information, sCD_Key_owner_name});
        setData(data);

    }

}
