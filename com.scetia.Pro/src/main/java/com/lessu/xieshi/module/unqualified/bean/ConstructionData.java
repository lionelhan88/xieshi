package com.lessu.xieshi.module.unqualified.bean;

import java.util.List;

/**
 * created by ljs
 * on 2020/12/15
 */
public class ConstructionData {

    /**
     * Type : 1
     * ListContent : [{"MemberId":"1283","ReportId":"20200272","Linkman":"姚总","Tel":"\u2014\u2014","ProjectName":"上海市世博会地区会展及其商务区A片区A02B-01,A02B-02地块。","BuildUnit":"中核（上海）企业发展有限公司","ConstructionUnit":"中国核工业华兴建设有限公司","SupervisorUnit":"中核工程咨询有限公司","SuperviserKey":"上海市建设工程安全质量监督总站","ModifyTime":"2020/12/7","IsSuperviser":"否"}]
     * PageCount : 1
     * PageSize : 10
     * CurrentPageNo : 1
     */

    private int Type;
    private int PageCount;
    private int PageSize;
    private int CurrentPageNo;
    private List<ConstructionBean> ListContent;

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int getPageCount() {
        return PageCount;
    }

    public void setPageCount(int PageCount) {
        this.PageCount = PageCount;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int PageSize) {
        this.PageSize = PageSize;
    }

    public int getCurrentPageNo() {
        return CurrentPageNo;
    }

    public void setCurrentPageNo(int CurrentPageNo) {
        this.CurrentPageNo = CurrentPageNo;
    }

    public List<ConstructionBean> getListContent() {
        return ListContent;
    }

    public void setListContent(List<ConstructionBean> ListContent) {
        this.ListContent = ListContent;
    }

    public static class ConstructionBean {
        /**
         * MemberId : 1283
         * ReportId : 20200272
         * Linkman : 姚总
         * Tel : ——
         * ProjectName : 上海市世博会地区会展及其商务区A片区A02B-01,A02B-02地块。
         * BuildUnit : 中核（上海）企业发展有限公司
         * ConstructionUnit : 中国核工业华兴建设有限公司
         * SupervisorUnit : 中核工程咨询有限公司
         * SuperviserKey : 上海市建设工程安全质量监督总站
         * ModifyTime : 2020/12/7
         * IsSuperviser : 否
         */

        private String MemberId;
        private String ReportId;
        private String Linkman;
        private String Tel;
        private String ProjectName;
        private String BuildUnit;
        private String ConstructionUnit;
        private String SupervisorUnit;
        private String SuperviserKey;
        private String ModifyTime;
        private String IsSuperviser;

        public String getMemberId() {
            return MemberId;
        }

        public void setMemberId(String MemberId) {
            this.MemberId = MemberId;
        }

        public String getReportId() {
            return ReportId;
        }

        public void setReportId(String ReportId) {
            this.ReportId = ReportId;
        }

        public String getLinkman() {
            return Linkman;
        }

        public void setLinkman(String Linkman) {
            this.Linkman = Linkman;
        }

        public String getTel() {
            return Tel;
        }

        public void setTel(String Tel) {
            this.Tel = Tel;
        }

        public String getProjectName() {
            return ProjectName;
        }

        public void setProjectName(String ProjectName) {
            this.ProjectName = ProjectName;
        }

        public String getBuildUnit() {
            return BuildUnit;
        }

        public void setBuildUnit(String BuildUnit) {
            this.BuildUnit = BuildUnit;
        }

        public String getConstructionUnit() {
            return ConstructionUnit;
        }

        public void setConstructionUnit(String ConstructionUnit) {
            this.ConstructionUnit = ConstructionUnit;
        }

        public String getSupervisorUnit() {
            return SupervisorUnit;
        }

        public void setSupervisorUnit(String SupervisorUnit) {
            this.SupervisorUnit = SupervisorUnit;
        }

        public String getSuperviserKey() {
            return SuperviserKey;
        }

        public void setSuperviserKey(String SuperviserKey) {
            this.SuperviserKey = SuperviserKey;
        }

        public String getModifyTime() {
            return ModifyTime;
        }

        public void setModifyTime(String ModifyTime) {
            this.ModifyTime = ModifyTime;
        }

        public String getIsSuperviser() {
            return IsSuperviser;
        }

        public void setIsSuperviser(String IsSuperviser) {
            this.IsSuperviser = IsSuperviser;
        }
    }
}
