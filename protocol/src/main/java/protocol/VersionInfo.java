package protocol;

/**
 * Created by guness on 6.02.2018.
 */

public enum VersionInfo {
    W3XP_126A((byte) 0x1A, "W3XP", new byte[]{1, 0, 26, 1},
            new byte[]{(byte) 0xC2, (byte) 0xCE, (byte) 0xE7, (byte) 0xF2},
            "war3.exe 02/01/12 15:10:41 471040"),
    W3XP_1285((byte) 0x1C, "W3XP", new byte[]{0, 5, 28, 1},
            new byte[]{(byte) 0xC9, (byte) 0x3F, (byte) 0x74, (byte) 0x60},
            "Warcraft III.exe 07/07/17 20:15:59 562152"),
    W2BN_202B((byte) 0x4F, "W2BN", new byte[]{1, 2, 0, 2},
            new byte[]{(byte) 0x4A, (byte) 0x4C, (byte) 0x0D, (byte) 0xFF},
            "Warcraft II BNE.exe 05/21/01 21:52:22 712704");

    VersionInfo(byte versionByte, String code, byte[] version, byte[] hash, String info) {
        this.versionByte = versionByte;
        product = code;
        exeVersion = version;
        exeHash = hash;
        exeInfo = info;
    }

    final public byte versionByte;
    final public String product;
    final public byte[] exeVersion;
    final public byte[] exeHash;
    final public String exeInfo;
}
