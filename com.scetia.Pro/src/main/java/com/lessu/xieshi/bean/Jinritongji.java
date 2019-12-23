package com.lessu.xieshi.bean;

import java.util.List;

/**
 * Created by fhm on 2017/3/29.
 */

public class Jinritongji {

    /**
     * Success : true
     * Code : 1000
     * Message :
     * Data : {"Type":0,"ListContent":[],"JsonContent":{"Day_ImportConsignCount":0,"Day_ImportSampleCount":0,"Day_FgConsign":22,"Day_FgSample":22,"Day_JdConsign":0,"Day_JdSample":0,"ItemList":[{"ItemId":1104,"ItemName":"墙体材料","ItemSampleCount":1,"ItemSampleFinishCount":1,"ItemBhgCount":0,"ItemNoExam":0},{"ItemId":1108,"ItemName":"混凝土抗压","ItemSampleCount":1,"ItemSampleFinishCount":1,"ItemBhgCount":0,"ItemNoExam":0},{"ItemId":1132,"ItemName":"预应力用钢材及锚夹具","ItemSampleCount":20,"ItemSampleFinishCount":10,"ItemBhgCount":8,"ItemNoExam":0}],"RecordTime":"2017/3/22 14:05:45"}}
     */

    private boolean Success;
    private int Code;
    private String Message;
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
        /**
         * Type : 0
         * ListContent : []
         * JsonContent : {"Day_ImportConsignCount":0,"Day_ImportSampleCount":0,"Day_FgConsign":22,"Day_FgSample":22,"Day_JdConsign":0,"Day_JdSample":0,"ItemList":[{"ItemId":1104,"ItemName":"墙体材料","ItemSampleCount":1,"ItemSampleFinishCount":1,"ItemBhgCount":0,"ItemNoExam":0},{"ItemId":1108,"ItemName":"混凝土抗压","ItemSampleCount":1,"ItemSampleFinishCount":1,"ItemBhgCount":0,"ItemNoExam":0},{"ItemId":1132,"ItemName":"预应力用钢材及锚夹具","ItemSampleCount":20,"ItemSampleFinishCount":10,"ItemBhgCount":8,"ItemNoExam":0}],"RecordTime":"2017/3/22 14:05:45"}
         */

        private int Type;
        private JsonContentBean JsonContent;
        private List<?> ListContent;

        public int getType() {
            return Type;
        }

        public void setType(int Type) {
            this.Type = Type;
        }

        public JsonContentBean getJsonContent() {
            return JsonContent;
        }

        public void setJsonContent(JsonContentBean JsonContent) {
            this.JsonContent = JsonContent;
        }

        public List<?> getListContent() {
            return ListContent;
        }

        public void setListContent(List<?> ListContent) {
            this.ListContent = ListContent;
        }

        public static class JsonContentBean {
            /**
             * Day_ImportConsignCount : 0
             * Day_ImportSampleCount : 0
             * Day_FgConsign : 22
             * Day_FgSample : 22
             * Day_JdConsign : 0
             * Day_JdSample : 0
             * ItemList : [{"ItemId":1104,"ItemName":"墙体材料","ItemSampleCount":1,"ItemSampleFinishCount":1,"ItemBhgCount":0,"ItemNoExam":0},{"ItemId":1108,"ItemName":"混凝土抗压","ItemSampleCount":1,"ItemSampleFinishCount":1,"ItemBhgCount":0,"ItemNoExam":0},{"ItemId":1132,"ItemName":"预应力用钢材及锚夹具","ItemSampleCount":20,"ItemSampleFinishCount":10,"ItemBhgCount":8,"ItemNoExam":0}]
             * RecordTime : 2017/3/22 14:05:45
             */

            private int Day_ImportConsignCount;
            private int Day_ImportSampleCount;
            private int Day_FgConsign;
            private int Day_FgSample;
            private int Day_JdConsign;
            private int Day_JdSample;
            private String RecordTime;
            private List<ItemListBean> ItemList;

            public int getDay_ImportConsignCount() {
                return Day_ImportConsignCount;
            }

            public void setDay_ImportConsignCount(int Day_ImportConsignCount) {
                this.Day_ImportConsignCount = Day_ImportConsignCount;
            }

            public int getDay_ImportSampleCount() {
                return Day_ImportSampleCount;
            }

            public void setDay_ImportSampleCount(int Day_ImportSampleCount) {
                this.Day_ImportSampleCount = Day_ImportSampleCount;
            }

            public int getDay_FgConsign() {
                return Day_FgConsign;
            }

            public void setDay_FgConsign(int Day_FgConsign) {
                this.Day_FgConsign = Day_FgConsign;
            }

            public int getDay_FgSample() {
                return Day_FgSample;
            }

            public void setDay_FgSample(int Day_FgSample) {
                this.Day_FgSample = Day_FgSample;
            }

            public int getDay_JdConsign() {
                return Day_JdConsign;
            }

            public void setDay_JdConsign(int Day_JdConsign) {
                this.Day_JdConsign = Day_JdConsign;
            }

            public int getDay_JdSample() {
                return Day_JdSample;
            }

            public void setDay_JdSample(int Day_JdSample) {
                this.Day_JdSample = Day_JdSample;
            }

            public String getRecordTime() {
                return RecordTime;
            }

            public void setRecordTime(String RecordTime) {
                this.RecordTime = RecordTime;
            }

            public List<ItemListBean> getItemList() {
                return ItemList;
            }

            public void setItemList(List<ItemListBean> ItemList) {
                this.ItemList = ItemList;
            }

            public static class ItemListBean {
                /**
                 * ItemId : 1104
                 * ItemName : 墙体材料
                 * ItemSampleCount : 1
                 * ItemSampleFinishCount : 1
                 * ItemBhgCount : 0
                 * ItemNoExam : 0
                 */

                private int ItemId;
                private String ItemName;
                private int ItemSampleCount;
                private int ItemSampleFinishCount;
                private int ItemBhgCount;
                private int ItemNoExam;

                public int getItemId() {
                    return ItemId;
                }

                public void setItemId(int ItemId) {
                    this.ItemId = ItemId;
                }

                public String getItemName() {
                    return ItemName;
                }

                public void setItemName(String ItemName) {
                    this.ItemName = ItemName;
                }

                public int getItemSampleCount() {
                    return ItemSampleCount;
                }

                public void setItemSampleCount(int ItemSampleCount) {
                    this.ItemSampleCount = ItemSampleCount;
                }

                public int getItemSampleFinishCount() {
                    return ItemSampleFinishCount;
                }

                public void setItemSampleFinishCount(int ItemSampleFinishCount) {
                    this.ItemSampleFinishCount = ItemSampleFinishCount;
                }

                public int getItemBhgCount() {
                    return ItemBhgCount;
                }

                public void setItemBhgCount(int ItemBhgCount) {
                    this.ItemBhgCount = ItemBhgCount;
                }

                public int getItemNoExam() {
                    return ItemNoExam;
                }

                public void setItemNoExam(int ItemNoExam) {
                    this.ItemNoExam = ItemNoExam;
                }
            }
        }
    }
}
