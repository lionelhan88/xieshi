package com.lessu.xieshi.bean;

import java.util.List;

/**
 * Created by fhm on 2016/10/10.
 */
public class SiteSearchProject {


    /**
     * Success : true
     * Code : 1000
     * Message :
     * Data : {"Type":1,"ListContent":[{"ProjectId":"df52b541-9c51-4a9c-8ca7-ec9d84f0d8ef","ProjectName":"西岸传媒港地下空间（停车场及配套）项目","ProjectStatus":"未完工","ProjectNature":"房建","ProjectRegion":"徐汇区","ProjectAddress":"徐汇区龙水南路41号","ProjectCoordinates":"121.469049,31.163484"},{"ProjectId":"4d664bb4-c92c-4c20-bba7-5d69ceefd2cf","ProjectName":"东方财富大厦（宛平南路88号金座）办公楼装修项目","ProjectStatus":"未完工","ProjectNature":"房建","ProjectRegion":"徐汇区","ProjectAddress":"宛平南路88号金座（3-30）名义楼层","ProjectCoordinates":"121.451155,31.202103"},{"ProjectId":"c84be499-32a1-4dd8-b2a8-8e2da41af7e2","ProjectName":"泰安路120弄3#楼结构加固工程","ProjectStatus":"未完工","ProjectNature":"房建","ProjectRegion":"长宁区","ProjectAddress":"泰安路120弄","ProjectCoordinates":"121.441268,31.21164"},{"ProjectId":"1f2b1555-ce50-40b8-8bb4-5c443f5f8c01","ProjectName":"公交电车康定路线网整治维护项目","ProjectStatus":"未完工","ProjectNature":"其他","ProjectRegion":"静安区","ProjectAddress":"康定路（万航渡路-泰兴路）路段","ProjectCoordinates":"121.450482,31.230416"},{"ProjectId":"16018918-e837-48e6-972d-554ba2edfb64","ProjectName":"市四中学崇德楼修缮项目","ProjectStatus":"已完工","ProjectNature":"房建","ProjectRegion":"徐汇区","ProjectAddress":"天钥桥路100号","ProjectCoordinates":"121.448059,31.198621"},{"ProjectId":"82f0f7b7-937c-4fa9-b3cb-5ed75a8f824d","ProjectName":"上海市公惠医院1号楼装修项目","ProjectStatus":"未完工","ProjectNature":"房建","ProjectRegion":"静安区","ProjectAddress":"石门一路315弄6号","ProjectCoordinates":"121.467638,31.235448"},{"ProjectId":"4e5d8ef0-8f24-4885-b183-9fb288f5b456","ProjectName":"世界小学修缮工程","ProjectStatus":"已完工","ProjectNature":"房建","ProjectRegion":"徐汇区","ProjectAddress":"徐汇区武康路280弄2号","ProjectCoordinates":"121.446284,31.214026"},{"ProjectId":"4487eb0a-e1da-433b-8165-27b9d8b4e943","ProjectName":"复旦大学新建枫林校区二号医学科研楼项目","ProjectStatus":"未完工","ProjectNature":"房建","ProjectRegion":"徐汇区","ProjectAddress":"复旦大学枫林校区内","ProjectCoordinates":"121.457409,31.203287"},{"ProjectId":"bff73068-8e28-4b7a-bff8-e86ebc6e5817","ProjectName":"地下管线及路面整修工程","ProjectStatus":"未完工","ProjectNature":"市政","ProjectRegion":"黄浦区","ProjectAddress":"瑞金二路197号","ProjectCoordinates":"121.472441,31.217729"},{"ProjectId":"d07aec11-ba9c-4882-927d-b99c2c1e7c4f","ProjectName":"信息管理学校广元校区教学楼加固工程（校安工程）","ProjectStatus":"未完工","ProjectNature":"房建","ProjectRegion":"徐汇区","ProjectAddress":"广元西路187号","ProjectCoordinates":"121.440457,31.202796"}],"PageCount":4,"PageSize":10,"CurrentPageNo":1}
     */

    private boolean Success;
    private int Code;
    private String Message;
    /**
     * Type : 1
     * ListContent : [{"ProjectId":"df52b541-9c51-4a9c-8ca7-ec9d84f0d8ef","ProjectName":"西岸传媒港地下空间（停车场及配套）项目","ProjectStatus":"未完工","ProjectNature":"房建","ProjectRegion":"徐汇区","ProjectAddress":"徐汇区龙水南路41号","ProjectCoordinates":"121.469049,31.163484"},{"ProjectId":"4d664bb4-c92c-4c20-bba7-5d69ceefd2cf","ProjectName":"东方财富大厦（宛平南路88号金座）办公楼装修项目","ProjectStatus":"未完工","ProjectNature":"房建","ProjectRegion":"徐汇区","ProjectAddress":"宛平南路88号金座（3-30）名义楼层","ProjectCoordinates":"121.451155,31.202103"},{"ProjectId":"c84be499-32a1-4dd8-b2a8-8e2da41af7e2","ProjectName":"泰安路120弄3#楼结构加固工程","ProjectStatus":"未完工","ProjectNature":"房建","ProjectRegion":"长宁区","ProjectAddress":"泰安路120弄","ProjectCoordinates":"121.441268,31.21164"},{"ProjectId":"1f2b1555-ce50-40b8-8bb4-5c443f5f8c01","ProjectName":"公交电车康定路线网整治维护项目","ProjectStatus":"未完工","ProjectNature":"其他","ProjectRegion":"静安区","ProjectAddress":"康定路（万航渡路-泰兴路）路段","ProjectCoordinates":"121.450482,31.230416"},{"ProjectId":"16018918-e837-48e6-972d-554ba2edfb64","ProjectName":"市四中学崇德楼修缮项目","ProjectStatus":"已完工","ProjectNature":"房建","ProjectRegion":"徐汇区","ProjectAddress":"天钥桥路100号","ProjectCoordinates":"121.448059,31.198621"},{"ProjectId":"82f0f7b7-937c-4fa9-b3cb-5ed75a8f824d","ProjectName":"上海市公惠医院1号楼装修项目","ProjectStatus":"未完工","ProjectNature":"房建","ProjectRegion":"静安区","ProjectAddress":"石门一路315弄6号","ProjectCoordinates":"121.467638,31.235448"},{"ProjectId":"4e5d8ef0-8f24-4885-b183-9fb288f5b456","ProjectName":"世界小学修缮工程","ProjectStatus":"已完工","ProjectNature":"房建","ProjectRegion":"徐汇区","ProjectAddress":"徐汇区武康路280弄2号","ProjectCoordinates":"121.446284,31.214026"},{"ProjectId":"4487eb0a-e1da-433b-8165-27b9d8b4e943","ProjectName":"复旦大学新建枫林校区二号医学科研楼项目","ProjectStatus":"未完工","ProjectNature":"房建","ProjectRegion":"徐汇区","ProjectAddress":"复旦大学枫林校区内","ProjectCoordinates":"121.457409,31.203287"},{"ProjectId":"bff73068-8e28-4b7a-bff8-e86ebc6e5817","ProjectName":"地下管线及路面整修工程","ProjectStatus":"未完工","ProjectNature":"市政","ProjectRegion":"黄浦区","ProjectAddress":"瑞金二路197号","ProjectCoordinates":"121.472441,31.217729"},{"ProjectId":"d07aec11-ba9c-4882-927d-b99c2c1e7c4f","ProjectName":"信息管理学校广元校区教学楼加固工程（校安工程）","ProjectStatus":"未完工","ProjectNature":"房建","ProjectRegion":"徐汇区","ProjectAddress":"广元西路187号","ProjectCoordinates":"121.440457,31.202796"}]
     * PageCount : 4
     * PageSize : 10
     * CurrentPageNo : 1
     */

    private DataBean Data;

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

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean {
        private int Type;
        private int PageCount;
        private int PageSize;
        private int CurrentPageNo;
        /**
         * ProjectId : df52b541-9c51-4a9c-8ca7-ec9d84f0d8ef
         * ProjectName : 西岸传媒港地下空间（停车场及配套）项目
         * ProjectStatus : 未完工
         * ProjectNature : 房建
         * ProjectRegion : 徐汇区
         * ProjectAddress : 徐汇区龙水南路41号
         * ProjectCoordinates : 121.469049,31.163484
         */

        private List<ListContentBean> ListContent;

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

        public List<ListContentBean> getListContent() {
            return ListContent;
        }

        public void setListContent(List<ListContentBean> ListContent) {
            this.ListContent = ListContent;
        }

        public static class ListContentBean {
            private String ProjectId;
            private String ProjectName;
            private String ProjectStatus;
            private String ProjectNature;
            private String ProjectRegion;
            private String ProjectAddress;
            private String ProjectCoordinates;

            public String getProjectId() {
                return ProjectId;
            }

            public void setProjectId(String ProjectId) {
                this.ProjectId = ProjectId;
            }

            public String getProjectName() {
                return ProjectName;
            }

            public void setProjectName(String ProjectName) {
                this.ProjectName = ProjectName;
            }

            public String getProjectStatus() {
                return ProjectStatus;
            }

            public void setProjectStatus(String ProjectStatus) {
                this.ProjectStatus = ProjectStatus;
            }

            public String getProjectNature() {
                return ProjectNature;
            }

            public void setProjectNature(String ProjectNature) {
                this.ProjectNature = ProjectNature;
            }

            public String getProjectRegion() {
                return ProjectRegion;
            }

            public void setProjectRegion(String ProjectRegion) {
                this.ProjectRegion = ProjectRegion;
            }

            public String getProjectAddress() {
                return ProjectAddress;
            }

            public void setProjectAddress(String ProjectAddress) {
                this.ProjectAddress = ProjectAddress;
            }

            public String getProjectCoordinates() {
                return ProjectCoordinates;
            }

            public void setProjectCoordinates(String ProjectCoordinates) {
                this.ProjectCoordinates = ProjectCoordinates;
            }
        }
    }
}
