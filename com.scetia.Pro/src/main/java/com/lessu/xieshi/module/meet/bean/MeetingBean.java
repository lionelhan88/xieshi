package com.lessu.xieshi.module.meet.bean;

import java.util.List;

public class MeetingBean {


    /**
     * MeetingId : a60e5988-29ef-4f80-afcf-2bec1a8a9366
     * MeetingName : 四届十一次理事会会议
     * CreatePerson : 张
     * CreatePersonPhone : 13761391131
     * MeetingCreateTime : 2020-8-7
     * MeetingStartTime : 2020-8-20 9:00
     * MeetingEndTime : 2020-8-20 11:00
     * MeetingNumbers : 67
     * MeetingNeedSign : 1
     * MeetingDetail : 工作汇报
     * MeetingConfirm : 1
     * MeetingUnitLimitNumber : 1
     * PlaceAddress : 青松城
     * MeetingPlace : 玉兰厅
     * ListUserContent : [{"UserId":"b9e0ae42-e557-4dc8-b402-e2c7d31e108e","MeetingId":"a60e5988-29ef-4f80-afcf-2bec1a8a9366","Type":"0","UnitMemberCode":"0000","CheckStatus":"0","UserFullName":"郑","UserSignImage":null,"CheckUserGift":"","SubstituteSign":"","Tel":"13812345678","SignTime":"","ConfirmNotify":"","MemberName":"协会"},{"UserId":"49376f5f-161c-45ac-a75f-38fda0ec3fad","MeetingId":"a60e5988-29ef-4f80-afcf-2bec1a8a9366","Type":"1","UnitMemberCode":"0000","CheckStatus":"0","UserFullName":"ZY","UserSignImage":null,"CheckUserGift":"","SubstituteSign":"","Tel":"13258456544","SignTime":"","ConfirmNotify":"1","MemberName":"协会"}]
     */

    private String MeetingId;
    private String MeetingName;
    private String CreatePerson;
    private String CreatePersonPhone;
    private String MeetingCreateTime;
    private String MeetingStartTime;
    private String MeetingEndTime;
    private String MeetingNumbers;
    private String MeetingNeedSign;
    private String MeetingDetail;
    private String MeetingConfirm;
    private String MeetingUnitLimitNumber;
    private String PlaceAddress;
    private String MeetingPlace;
    private String MeetingDetailPhoto;
    private List<MeetingUserBean> ListUserContent;

    public String getMeetingId() {
        return MeetingId;
    }

    public void setMeetingId(String MeetingId) {
        this.MeetingId = MeetingId;
    }

    public String getMeetingName() {
        return MeetingName;
    }

    public void setMeetingName(String MeetingName) {
        this.MeetingName = MeetingName;
    }

    public String getCreatePerson() {
        return CreatePerson;
    }

    public void setCreatePerson(String CreatePerson) {
        this.CreatePerson = CreatePerson;
    }

    public String getCreatePersonPhone() {
        return CreatePersonPhone;
    }

    public void setCreatePersonPhone(String CreatePersonPhone) {
        this.CreatePersonPhone = CreatePersonPhone;
    }

    public String getMeetingCreateTime() {
        return MeetingCreateTime;
    }

    public void setMeetingCreateTime(String MeetingCreateTime) {
        this.MeetingCreateTime = MeetingCreateTime;
    }

    public String getMeetingStartTime() {
        return MeetingStartTime;
    }

    public void setMeetingStartTime(String MeetingStartTime) {
        this.MeetingStartTime = MeetingStartTime;
    }

    public String getMeetingEndTime() {
        return MeetingEndTime;
    }

    public void setMeetingEndTime(String MeetingEndTime) {
        this.MeetingEndTime = MeetingEndTime;
    }

    public String getMeetingNumbers() {
        return MeetingNumbers;
    }

    public void setMeetingNumbers(String MeetingNumbers) {
        this.MeetingNumbers = MeetingNumbers;
    }

    public String getMeetingNeedSign() {
        return MeetingNeedSign;
    }

    public void setMeetingNeedSign(String MeetingNeedSign) {
        this.MeetingNeedSign = MeetingNeedSign;
    }

    public String getMeetingDetail() {
        return MeetingDetail;
    }

    public void setMeetingDetail(String MeetingDetail) {
        this.MeetingDetail = MeetingDetail;
    }

    public String getMeetingConfirm() {
        return MeetingConfirm;
    }

    public void setMeetingConfirm(String MeetingConfirm) {
        this.MeetingConfirm = MeetingConfirm;
    }

    public String getMeetingUnitLimitNumber() {
        return MeetingUnitLimitNumber;
    }

    public void setMeetingUnitLimitNumber(String MeetingUnitLimitNumber) {
        this.MeetingUnitLimitNumber = MeetingUnitLimitNumber;
    }

    public String getPlaceAddress() {
        return PlaceAddress;
    }

    public void setPlaceAddress(String PlaceAddress) {
        this.PlaceAddress = PlaceAddress;
    }

    public String getMeetingPlace() {
        return MeetingPlace;
    }

    public void setMeetingPlace(String MeetingPlace) {
        this.MeetingPlace = MeetingPlace;
    }

    public List<MeetingUserBean> getListUserContent() {
        return ListUserContent;
    }

    public void setListUserContent(List<MeetingUserBean> ListUserContent) {
        this.ListUserContent = ListUserContent;
    }

    public String getMeetingDetailPhoto() {
        return MeetingDetailPhoto;
    }

    public void setMeetingDetailPhoto(String meetingDetailPhoto) {
        MeetingDetailPhoto = meetingDetailPhoto;
    }

    public static class MeetingUserBean {
        /**
         * UserId : b9e0ae42-e557-4dc8-b402-e2c7d31e108e
         * MeetingId : a60e5988-29ef-4f80-afcf-2bec1a8a9366
         * Type : 0为单位性质，1为人员性质
         * UnitMemberCode : 0000
         * CheckStatus : 0
         * UserFullName : 郑
         * UserSignImage : null
         * CheckUserGift :
         * SubstituteSign :
         * Tel : 13812345678
         * SignTime :
         * ConfirmNotify :
         * MemberName : 协会
         */

        private String UserId;
        private String MeetingId;
        private String Type;
        private String UnitMemberCode;
        private String CheckStatus;
        private String UserFullName;
        private Object UserSignImage;
        private String CheckUserGift;
        private String SubstituteSign;
        private String Tel;
        private String SignTime;
        private String ConfirmNotify;
        private String MemberName;
        // 非本人0； 本人1
        private String IsSelf;
        //实际参会人ID；原参会人ID
        private String SubstituteUser;
        //被指定的无账号的参会人
        private String AccountNumber;

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String UserId) {
            this.UserId = UserId;
        }

        public String getMeetingId() {
            return MeetingId;
        }

        public void setMeetingId(String MeetingId) {
            this.MeetingId = MeetingId;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public String getUnitMemberCode() {
            return UnitMemberCode;
        }

        public void setUnitMemberCode(String UnitMemberCode) {
            this.UnitMemberCode = UnitMemberCode;
        }

        public String getCheckStatus() {
            return CheckStatus;
        }

        public void setCheckStatus(String CheckStatus) {
            this.CheckStatus = CheckStatus;
        }

        public String getUserFullName() {
            return UserFullName;
        }

        public void setUserFullName(String UserFullName) {
            this.UserFullName = UserFullName;
        }

        public Object getUserSignImage() {
            return UserSignImage;
        }

        public void setUserSignImage(Object UserSignImage) {
            this.UserSignImage = UserSignImage;
        }

        public String getCheckUserGift() {
            return CheckUserGift;
        }

        public void setCheckUserGift(String CheckUserGift) {
            this.CheckUserGift = CheckUserGift;
        }

        public String getSubstituteSign() {
            return SubstituteSign;
        }

        public void setSubstituteSign(String SubstituteSign) {
            this.SubstituteSign = SubstituteSign;
        }

        public String getTel() {
            return Tel;
        }

        public void setTel(String Tel) {
            this.Tel = Tel;
        }

        public String getSignTime() {
            return SignTime;
        }

        public void setSignTime(String SignTime) {
            this.SignTime = SignTime;
        }

        public String getConfirmNotify() {
            return ConfirmNotify;
        }

        public void setConfirmNotify(String ConfirmNotify) {
            this.ConfirmNotify = ConfirmNotify;
        }

        public String getMemberName() {
            return MemberName;
        }

        public void setMemberName(String MemberName) {
            this.MemberName = MemberName;
        }

        public String getIsSelf() {
            return IsSelf;
        }

        public void setIsSelf(String isSelf) {
            IsSelf = isSelf;
        }

        public String getSubstituteUser() {
            return SubstituteUser;
        }

        public void setSubstituteUser(String substituteUser) {
            SubstituteUser = substituteUser;
        }

        public String getAccountNumber() {
            return AccountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            AccountNumber = accountNumber;
        }
    }
}
