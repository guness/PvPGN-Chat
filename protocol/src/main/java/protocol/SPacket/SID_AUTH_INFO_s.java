package protocol.SPacket;

import protocol.ID;
import protocol.VersionInfo;
import protocol.types.Concatable;
import protocol.types.DWORD;
import protocol.types.STRING;

public class SID_AUTH_INFO_s extends _sPacket {
    public VersionInfo mVersionInfo;

    public DWORD dProtocol_ID, dPlatform_ID, dProduct_ID, dVersion_Byte, dProduct_language,
            dLocal_IP_for_NAT_compatibility, dTime_zone_bias, dLocale_ID, dLanguage_ID;
    public STRING sCountry_abreviation, sCountry;

    public SID_AUTH_INFO_s(VersionInfo versionInfo) {
        super(ID.SID_AUTH_INFO);
        mVersionInfo = versionInfo;
        dProtocol_ID = new DWORD(new byte[]{0, 0, 0, 0});
        dPlatform_ID = new DWORD(reverse("IX86".getBytes()));
        dProduct_ID = new DWORD(_sPacket.reverse(versionInfo.product.getBytes()));
        dVersion_Byte = new DWORD(new byte[]{versionInfo.versionByte, 0, 0, 0});
        dProduct_language = new DWORD(reverse("enUS".getBytes()));
        dLocal_IP_for_NAT_compatibility = new DWORD(new byte[]{0, 0, 0, 0});
        dTime_zone_bias = new DWORD(new byte[]{(byte) 0x88, (byte) 0xff, (byte) 0xff, (byte) 0xff});
        dLocale_ID = new DWORD(new byte[]{9, 4, 0, 0});
        dLanguage_ID = new DWORD(new byte[]{9, 4, 0, 0}); // http://www.science.co.il/language/locale-codes.asp
        sCountry_abreviation = new STRING("ENU"); // http://msdn.microsoft.com/en-us/library/ee825488%28v=cs.20%29.aspx
        sCountry = new STRING("United States");
    }

    @Override
    protected void prepareData() {
        setData(dProtocol_ID.getByteArray().concat(
                new Concatable[]{dPlatform_ID, dProduct_ID, dVersion_Byte, dProduct_language,
                        dLocal_IP_for_NAT_compatibility, dTime_zone_bias, dLocale_ID, dLanguage_ID,
                        sCountry_abreviation, sCountry}));
    }
}
