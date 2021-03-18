package com.lessu.xieshi.Utils;

/**
 * Created by fhm on 2016/12/8.
 */

public class Decrypt {
    /**
     * 读取芯片内容
     * @param MaskStr
     * @return
     */
    public static String chipDecryption(String MaskStr){
        String TempStr;
        String FirstStr;
        String SecendStr;
        String YusuStr;
        TempStr = MaskStr.substring(MaskStr.length() - 15);
        //0350F
        FirstStr = TempStr.substring(0, 5);
        //F55485
        YusuStr = TempStr.substring(5, 10);
        SecendStr = TempStr.substring(10, 15);
        if(TempStr.matches("[0-9]*")) {
            FirstStr = (Long.parseLong(FirstStr) * 7 + Long.parseLong(YusuStr.substring(3, 4))) + "";
            SecendStr = (Long.parseLong(SecendStr) * 7 + Long.parseLong(YusuStr.substring(4, 5))) + "";
            return padLeft(FirstStr, 5, "0") + padLeft(SecendStr, 5, "0");
        }else{
            return "2";
        }
    }

    /**
     *  新设备读取芯片的解码方式
     */
    private static String newChipDecryption(String MaskStr){
        String TempStr;
        String FirstStr;
        String SecendStr;
        String YusuStr;
        if(MaskStr.length()<42){
            return "2";
        }
        TempStr = MaskStr.substring(23, MaskStr.length()-4);
        FirstStr = TempStr.substring(0, 5);
        YusuStr = TempStr.substring(5, 10);
        SecendStr = TempStr.substring(10, 15);
        if(TempStr.matches("[0-9]*")) {
            FirstStr = (Long.parseLong(FirstStr) * 7 + Long.parseLong(YusuStr.substring(3, 4))) + "";
            SecendStr = (Long.parseLong(SecendStr) * 7 + Long.parseLong(YusuStr.substring(4, 5))) + "";
            return padLeft(FirstStr, 5, "0") + padLeft(SecendStr, 5, "0");
        }else{
            return "2";
        }
    }

    /**
     * 左补位，右对齐
     * @param oriStr  原字符串
     * @param len  目标字符串长度
     * @param alexin  补位字符
     * @return  目标字符串
     */
    public static String padLeft(String oriStr, int len, String alexin){
        int strlen = oriStr.length();
        if(strlen < len){
            for(int i=0;i<len-strlen;i++){
                oriStr = alexin+oriStr;
            }
        }
        return oriStr;
    }

    /**
     * 删除buffer缓冲区后面多余的0-
     * @param src
     * @return
     */
    public static String delZero(String src) {
        while (src.endsWith("0")) {
            src = (src.substring(0, src.length() - 1));
        }
        return src;
    }

    /**
     * 芯片解析方式
     * @param s
     * @return
     */
    public static String decodeChip(String s){
        String xinpianjiemi;
        String no0s=delZero(s);
        if(no0s.length()>=15){
            if(no0s.contains("A0")){
                xinpianjiemi = newChipDecryption(no0s);
            }else {
                xinpianjiemi = chipDecryption(no0s);
            }
            if(isChip(xinpianjiemi)){
                return xinpianjiemi;
            }else{
                int i = no0s.lastIndexOf("55");
                if(i!=-1) {
                    String qvchu55 = no0s.substring(0, i);
                    if (qvchu55.length() > 15) {
                        xinpianjiemi = chipDecryption(qvchu55);
                        if (isChip(xinpianjiemi)) {
                            return xinpianjiemi;
                        } else {
                            System.out.println("");
                            return null;//虽然包含55但是不符合
                        }
                    } else {
                        return null;//包含55但去除55后长度不够
                    }
                }else{
                    return null;//不包含55
                }
            }
        }else{
            return null;//小于15的长度
        }
    }

    /**
     * 是否是芯片
     * @param s
     * @return
     */
    public static boolean isChip(String s) {
        System.out.println(s.length());
        String substring = s.substring(0, 1);
        String index = "1";
        System.out.println(s.substring(0, 1) + substring.equals(index));
        if (s.length() == 10 && substring.equals(index)) {
            return true;
        } else {
            return false;
        }
    }
}
