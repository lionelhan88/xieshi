package com.lessu.xieshi.Utils;

/**
 * Created by fhm on 2016/12/8.
 */

public class Decrypt {
    public static String  xinpianjiemi(String MaskStr){
        String TempStr = "";
        String FirstStr = "";
        String SecendStr = "";
        String YusuStr = "";
        String Yusu1 = "";
        String Yusu2 = "";
        TempStr = MaskStr.substring(MaskStr.length() - 15, MaskStr.length());
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
    //新设备读取芯片的解码方式
    private static String  xinpianjiemi2(String MaskStr){
        String TempStr = "";
        String FirstStr = "";
        String SecendStr = "";
        String YusuStr = "";
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
    public static String padRight(String oriStr, int len, String alexin){
        int strlen = oriStr.length();
        if(strlen < len){
            for(int i=0;i<len-strlen;i++){
                oriStr = oriStr+alexin;
            }
        }
        return oriStr;
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

//        if (src.endsWith("0")&&src.length()>10)
//            return delZero(src.substring(0, src.length() - 1));
//        else
//            return src;
    }

    /**
     * 芯片解析方式
     * @param s
     * @return
     */
    public static String jiexinpian(String s){
        String xinpianjiemi;
        String no0s=delZero(s);
        if(no0s.length()>=15){
            System.out.println("no0s..."+no0s);
            if(no0s.contains("A0")){
                xinpianjiemi = xinpianjiemi2(no0s);
            }else {
                xinpianjiemi = xinpianjiemi(no0s);
            }
            if(isxinpian(xinpianjiemi)){
                return xinpianjiemi;
            }else{
                int i = no0s.lastIndexOf("55");
                if(i!=-1) {
                    System.out.println("no0s..."+no0s);
                    String qvchu55 = no0s.substring(0, i);
                    System.out.println("qvchu55..."+qvchu55);
                    if (qvchu55.length() > 15) {
                        xinpianjiemi = xinpianjiemi(qvchu55);
                        System.out.println("2222222......."+xinpianjiemi);
                        if (isxinpian(xinpianjiemi)) {
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
    public static boolean isxinpian(String s) {
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
