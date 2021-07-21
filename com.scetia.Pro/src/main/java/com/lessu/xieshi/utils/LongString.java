package com.lessu.xieshi.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by fhm on 2016/11/28.
 */
public class LongString {
    /**
     * 字节数据转换为十六进制字符串
     * @param b 需要转换的字节数组
     * @return
     */
    public static String bytes2HexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length);
        for (byte value : b) {
            String hex = Integer.toHexString(value & 0xFF);
            if (hex.length() == 1) {
                sb.append(0);
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 十六进制字符串转换为字节数组
     * @param hexString 需要转换的十六进制字符串
     * @return
     */
    public static byte[] hexString2Bytes(String hexString) {
        byte[] result = new byte[6];
        byte[] temp = hexString.getBytes();
        for (int i = 0; i < 6; ++i) {
            result[i] = uniteBytes(temp[i * 2], temp[i * 2 + 1]);
        }
        return result;
    }

    /**
     * 十六进制转换字符串
     * @param  hexStr Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr)
    {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++)
        {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    private static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}));
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}));
        return (byte) (_b0 | _b1);
    }

    /**
     * md5加密算法
     *
     * @param def 需要加密的字符串
     * @return
     */
    public static String md5(String def) {
        if (TextUtils.isEmpty(def)) {
            return "";
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(def.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
