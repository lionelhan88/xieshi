package com.lessu.xieshi.module.mis.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Lollipop
 * on 2021/4/15
 */
public class EvaluationComparisonBean implements Parcelable {



    private String PGApproveBtn;
    private List<EvaluationComparisonItem> ListContent;

    public String getPGApproveBtn() {
        return PGApproveBtn;
    }

    public void setPGApproveBtn(String PGApproveBtn) {
        this.PGApproveBtn = PGApproveBtn;
    }

    public List<EvaluationComparisonItem> getListContent() {
        return ListContent;
    }

    public void setListContent(List<EvaluationComparisonItem> ListContent) {
        this.ListContent = ListContent;
    }

    public static class EvaluationComparisonItem implements Parcelable{

        /**
         * AID : 8e2f38a8-1766-46a0-bf2d-fde69f8a3475
         * S1 : 0708
         * S2 : 0
         * S3 :
         * S4 : 2023/3/31
         * S5 :
         * S6 : 2021/4/13 0:00:00
         * S7 : 2
         * S8 : 换法人
         * S9 : 杨兰英
         * S10 : 2021/4/13 0:00:00
         * S11 : 上海博强环境技术有限公司
         * S12 :
         * S13 :
         * S14 : 1
         * S2Text : 申请中
         * S7Text : 换发
         * S14Text : 证书
         */

        private String AID;
        private String S1;
        private String S2;
        private String S3;
        private String S4;
        private String S5;
        private String S6;
        private String S7;
        private String S8;
        private String S9;
        private String S10;
        private String S11;
        private String S12;
        private String S13;
        private String S14;
        private String S2Text;
        private String S7Text;
        private String S14Text;

        public String getAID() {
            return AID;
        }

        public void setAID(String AID) {
            this.AID = AID;
        }

        public String getS1() {
            return S1;
        }

        public void setS1(String S1) {
            this.S1 = S1;
        }

        public String getS2() {
            return S2;
        }

        public void setS2(String S2) {
            this.S2 = S2;
        }

        public String getS3() {
            return S3;
        }

        public void setS3(String S3) {
            this.S3 = S3;
        }

        public String getS4() {
            return S4;
        }

        public void setS4(String S4) {
            this.S4 = S4;
        }

        public String getS5() {
            return S5;
        }

        public void setS5(String S5) {
            this.S5 = S5;
        }

        public String getS6() {
            return S6;
        }

        public void setS6(String S6) {
            this.S6 = S6;
        }

        public String getS7() {
            return S7;
        }

        public void setS7(String S7) {
            this.S7 = S7;
        }

        public String getS8() {
            return S8;
        }

        public void setS8(String S8) {
            this.S8 = S8;
        }

        public String getS9() {
            return S9;
        }

        public void setS9(String S9) {
            this.S9 = S9;
        }

        public String getS10() {
            return S10;
        }

        public void setS10(String S10) {
            this.S10 = S10;
        }

        public String getS11() {
            return S11;
        }

        public void setS11(String S11) {
            this.S11 = S11;
        }

        public String getS12() {
            return S12;
        }

        public void setS12(String S12) {
            this.S12 = S12;
        }

        public String getS13() {
            return S13;
        }

        public void setS13(String S13) {
            this.S13 = S13;
        }

        public String getS14() {
            return S14;
        }

        public void setS14(String S14) {
            this.S14 = S14;
        }

        public String getS2Text() {
            return S2Text;
        }

        public void setS2Text(String S2Text) {
            this.S2Text = S2Text;
        }

        public String getS7Text() {
            return S7Text;
        }

        public void setS7Text(String S7Text) {
            this.S7Text = S7Text;
        }

        public String getS14Text() {
            return S14Text;
        }

        public void setS14Text(String S14Text) {
            this.S14Text = S14Text;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.AID);
            dest.writeString(this.S1);
            dest.writeString(this.S2);
            dest.writeString(this.S3);
            dest.writeString(this.S4);
            dest.writeString(this.S5);
            dest.writeString(this.S6);
            dest.writeString(this.S7);
            dest.writeString(this.S8);
            dest.writeString(this.S9);
            dest.writeString(this.S10);
            dest.writeString(this.S11);
            dest.writeString(this.S12);
            dest.writeString(this.S13);
            dest.writeString(this.S14);
            dest.writeString(this.S2Text);
            dest.writeString(this.S7Text);
            dest.writeString(this.S14Text);
        }

        public EvaluationComparisonItem() {
        }

        protected EvaluationComparisonItem(Parcel in) {
            this.AID = in.readString();
            this.S1 = in.readString();
            this.S2 = in.readString();
            this.S3 = in.readString();
            this.S4 = in.readString();
            this.S5 = in.readString();
            this.S6 = in.readString();
            this.S7 = in.readString();
            this.S8 = in.readString();
            this.S9 = in.readString();
            this.S10 = in.readString();
            this.S11 = in.readString();
            this.S12 = in.readString();
            this.S13 = in.readString();
            this.S14 = in.readString();
            this.S2Text = in.readString();
            this.S7Text = in.readString();
            this.S14Text = in.readString();
        }

        public static final Creator<EvaluationComparisonItem> CREATOR = new Creator<EvaluationComparisonItem>() {
            @Override
            public EvaluationComparisonItem createFromParcel(Parcel source) {
                return new EvaluationComparisonItem(source);
            }

            @Override
            public EvaluationComparisonItem[] newArray(int size) {
                return new EvaluationComparisonItem[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.PGApproveBtn);
        dest.writeList(this.ListContent);
    }

    public EvaluationComparisonBean() {
    }

    protected EvaluationComparisonBean(Parcel in) {
        this.PGApproveBtn = in.readString();
        this.ListContent = new ArrayList<EvaluationComparisonItem>();
        in.readList(this.ListContent, EvaluationComparisonItem.class.getClassLoader());
    }

    public static final Creator<EvaluationComparisonBean> CREATOR = new Creator<EvaluationComparisonBean>() {
        @Override
        public EvaluationComparisonBean createFromParcel(Parcel source) {
            return new EvaluationComparisonBean(source);
        }

        @Override
        public EvaluationComparisonBean[] newArray(int size) {
            return new EvaluationComparisonBean[size];
        }
    };
}
