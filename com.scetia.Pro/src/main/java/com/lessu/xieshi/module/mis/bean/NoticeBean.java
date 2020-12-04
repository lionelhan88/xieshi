package com.lessu.xieshi.module.mis.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fhm on 2017/10/12.
 */

public class NoticeBean implements Serializable{

    /**
     * Success : true
     * Code : 1000
     * Message :
     * Data : [{"BT":"关于本次中秋节及国庆节放假安排的通知","NR":"2017年国庆节及中秋节放假时间为：10月1日--10月8日，共计8天。\n\n9月30日（周六）调整为工作日。\n\n10月9日（周一）正常上班。\n\n祝大家国庆、中秋双节快乐！","SJ":"2017/9/27"},{"BT":"关于2017年端午节放假时间的通知","NR":"2017年端午节放假时间为5月28-5月30日，5月27日（周六）正常上班","SJ":"2017/5/26"},{"BT":"关于2017年劳动节放假时间的通知","NR":"2017年劳动节放假时间为4月29日-5月1日，5月2日（周二）正常上班。","SJ":"2017/4/28"},{"BT":"关于2017年清明节放假时间的通知","NR":"2017年清明节放假时间为4月2日-4月4日，4月1日（周六）上班。","SJ":"2017/3/29"},{"BT":"关于2017年元旦、春节放假时间的通知","NR":"2017年元旦的放假时间为2016年12月31日-2017年1月2日。2017年春节放假时间为2017年1月27日-2月6日。预祝大家节日快乐！","SJ":"2016/12/16"}]
     */

 /*   private boolean Success;
    private int Code;
    private String Message;
    private List<DataBean> Data;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }*/

    //public static class DataBean implements Serializable{
        /**
         * BT : 关于本次中秋节及国庆节放假安排的通知
         * NR : 2017年国庆节及中秋节放假时间为：10月1日--10月8日，共计8天。

         9月30日（周六）调整为工作日。

         10月9日（周一）正常上班。

         祝大家国庆、中秋双节快乐！
         * SJ : 2017/9/27
         */

        private String BT;
        private String NR;
        private String SJ;

        public String getBT() {
            return BT;
        }

        public void setBT(String BT) {
            this.BT = BT;
        }

        public String getNR() {
            return NR;
        }

        public void setNR(String NR) {
            this.NR = NR;
        }

        public String getSJ() {
            return SJ;
        }

        public void setSJ(String SJ) {
            this.SJ = SJ;
        }
    //}
}
