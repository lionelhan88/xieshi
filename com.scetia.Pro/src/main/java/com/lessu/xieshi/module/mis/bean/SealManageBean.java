package com.lessu.xieshi.module.mis.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.contrarywind.interfaces.IPickerViewData;
import com.scetia.Pro.network.response.IResultData;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Lollipop
 * on 2021/3/2
 */
public class SealManageBean implements Parcelable {
    /**
     * yzId : fb11574a-347e-4a43-b2c4-7c5a415308ed
     * yzState : 0
     * yzType : 1
     * yzContent : f
     * isGz : 1
     * gzFs : 0
     * applyDate : 2021-02-08T16:52:19.347
     * applyMan : 肖珍珠
     * shDate : 2021-02-09T14:42:28.02
     * shMan : 郑建
     * pzDate : null
     * pzMan :
     * gzDate : 2021-02-09T15:56:32.11
     * gzMan : 郑建
     * yzTypeText : 文件审批
     * isGzText : 是
     * yzStateText : 申请中
     * YzFjInfo : [{"zyId":"fb11574a-347e-4a43-b2c4-7c5a415308ed","fileName":"f","filePath":"132572479393469044_申报户口登记表.xls"}]
     */
    private String yzId;
    private int yzState;
    private String yzType;
    private String yzContent;
    private int isGz;
    private String gzFs;
    // 1 部门领导 2秘书长
    private int IsLd;
    private String applyDate;
    private String applyMan;
    private String shDate;
    private String shMan;
    private String pzDate;
    private String pzMan;
    private String gzDate;
    private String gzMan;
    private String yzTypeText;
    private String isGzText;
    private String yzStateText;
    private List<YzFjInfoBean> YzFjInfo;

    public String getYzId() {
        return yzId;
    }

    public void setYzId(String yzId) {
        this.yzId = yzId;
    }

    public int getYzState() {
        return yzState;
    }

    public void setYzState(int yzState) {
        this.yzState = yzState;
    }

    public String getYzType() {
        return yzType;
    }

    public void setYzType(String yzType) {
        this.yzType = yzType;
    }

    public String getYzContent() {
        return yzContent;
    }

    public void setYzContent(String yzContent) {
        this.yzContent = yzContent;
    }

    public int getIsGz() {
        return isGz;
    }

    public void setIsGz(int isGz) {
        this.isGz = isGz;
    }

    public String getGzFs() {
        return gzFs;
    }

    public void setGzFs(String gzFs) {
        this.gzFs = gzFs;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getApplyMan() {
        return applyMan;
    }

    public void setApplyMan(String applyMan) {
        this.applyMan = applyMan;
    }

    public String getShDate() {
        return shDate;
    }

    public void setShDate(String shDate) {
        this.shDate = shDate;
    }

    public String getShMan() {
        return shMan;
    }

    public void setShMan(String shMan) {
        this.shMan = shMan;
    }

    public String getPzDate() {
        return pzDate;
    }

    public void setPzDate(String pzDate) {
        this.pzDate = pzDate;
    }

    public String getPzMan() {
        return pzMan;
    }

    public void setPzMan(String pzMan) {
        this.pzMan = pzMan;
    }

    public String getGzDate() {
        return gzDate;
    }

    public void setGzDate(String gzDate) {
        this.gzDate = gzDate;
    }

    public String getGzMan() {
        return gzMan;
    }

    public void setGzMan(String gzMan) {
        this.gzMan = gzMan;
    }

    public String getYzTypeText() {
        return yzTypeText;
    }

    public void setYzTypeText(String yzTypeText) {
        this.yzTypeText = yzTypeText;
    }

    public String getIsGzText() {
        return isGzText;
    }

    public void setIsGzText(String isGzText) {
        this.isGzText = isGzText;
    }

    public String getYzStateText() {
        return yzStateText;
    }

    public void setYzStateText(String yzStateText) {
        this.yzStateText = yzStateText;
    }

    public List<YzFjInfoBean> getYzFjInfo() {
        return YzFjInfo;
    }

    public void setYzFjInfo(List<YzFjInfoBean> YzFjInfo) {
        this.YzFjInfo = YzFjInfo;
    }

    public int getIsLd() {
        return IsLd;
    }

    public void setIsLd(int isLd) {
        IsLd = isLd;
    }

    public static class YzFjInfoBean implements Parcelable {

        /**
         * zyId : fb11574a-347e-4a43-b2c4-7c5a415308ed
         * fileName : f
         * filePath : 132572479393469044_申报户口登记表.xls
         */

        private String zyId;
        private String fileName;
        private String filePath;

        public String getZyId() {
            return zyId;
        }

        public void setZyId(String zyId) {
            this.zyId = zyId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.zyId);
            dest.writeString(this.fileName);
            dest.writeString(this.filePath);
        }

        public YzFjInfoBean() {
        }

        protected YzFjInfoBean(Parcel in) {
            this.zyId = in.readString();
            this.fileName = in.readString();
            this.filePath = in.readString();
        }

        public static final Creator<YzFjInfoBean> CREATOR = new Creator<YzFjInfoBean>() {
            @Override
            public YzFjInfoBean createFromParcel(Parcel source) {
                return new YzFjInfoBean(source);
            }

            @Override
            public YzFjInfoBean[] newArray(int size) {
                return new YzFjInfoBean[size];
            }
        };
    }


    public static class SealType implements IPickerViewData {
        private String value;
        private String text;

        public SealType(String value, String text) {
            this.value = value;
            this.text = text;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }


        @Override
        public String getPickerViewText() {
            return text;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.yzId);
        dest.writeInt(this.yzState);
        dest.writeString(this.yzType);
        dest.writeString(this.yzContent);
        dest.writeInt(this.isGz);
        dest.writeString(this.gzFs);
        dest.writeString(this.applyDate);
        dest.writeString(this.applyMan);
        dest.writeString(this.shDate);
        dest.writeString(this.shMan);
        dest.writeString(this.pzDate);
        dest.writeString(this.pzMan);
        dest.writeString(this.gzDate);
        dest.writeString(this.gzMan);
        dest.writeString(this.yzTypeText);
        dest.writeString(this.isGzText);
        dest.writeString(this.yzStateText);
        dest.writeList(this.YzFjInfo);
        dest.writeInt(this.IsLd);
    }

    public SealManageBean() {
    }

    protected SealManageBean(Parcel in) {
        this.yzId = in.readString();
        this.yzState = in.readInt();
        this.yzType = in.readString();
        this.yzContent = in.readString();
        this.isGz = in.readInt();
        this.gzFs = in.readString();
        this.applyDate = in.readString();
        this.applyMan = in.readString();
        this.shDate = in.readString();
        this.shMan = in.readString();
        this.pzDate = in.readString();
        this.pzMan = in.readString();
        this.gzDate = in.readString();
        this.gzMan = in.readString();
        this.yzTypeText = in.readString();
        this.isGzText = in.readString();
        this.yzStateText = in.readString();
        this.YzFjInfo = new ArrayList<YzFjInfoBean>();
        in.readList(this.YzFjInfo, YzFjInfoBean.class.getClassLoader());
        this.IsLd = in.readInt();
    }

    public static final Parcelable.Creator<SealManageBean> CREATOR = new Parcelable.Creator<SealManageBean>() {
        @Override
        public SealManageBean createFromParcel(Parcel source) {
            return new SealManageBean(source);
        }

        @Override
        public SealManageBean[] newArray(int size) {
            return new SealManageBean[size];
        }
    };
}
