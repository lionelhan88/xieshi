package com.lessu.xieshi.Utils;

/**
 * Created by fhm on 2016/11/28.
 */
public class Changezifu {
    //
        public static String Bytes2HexString( byte[] b) {
        StringBuffer sb = new StringBuffer(b.length);
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                sb.append(0);
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    public static byte[] HexString2Bytes(String sString)
    {
        byte[] result = new byte[6];
        byte[] temp = sString.getBytes();
        for(int i=0; i<6; ++i )
        {
            result[i] = uniteBytes(temp[i*2], temp[i*2+1]);
        }
        return result;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] {src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] {src1})).byteValue();
        byte result = (byte) (_b0 | _b1);
        return result;
    }


}
