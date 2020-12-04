package com.lessu.xieshi.login.bean;

/**
 * created by ljs
 * on 2020/11/19
 */
public class UserBean {

    /**
     * Success : true
     * Code : 1000
     * Message :
     * Data : {"IsFirstLogin":false,"Token":"98da0402-c813-406d-a648-05d2f93469df","UserPower":"0110100000000001000000","UserId":"b9e0ae42-e557-4dc8-b402-e2c7d31e108e","PhoneNumber":"13521475141","MemberInfoStr":"0000|协会","UserName":"T9990001","Jwt":null,"BoundToUnitID":"697cb41b-ff88-4e91-a407-d5d2eef4be69","HasCertificate":true,"IsConstructionSandSupplier":false}
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
         * IsFirstLogin : false
         * Token : 98da0402-c813-406d-a648-05d2f93469df
         * UserPower : 0110100000000001000000
         * UserId : b9e0ae42-e557-4dc8-b402-e2c7d31e108e
         * PhoneNumber : 13521475141
         * MemberInfoStr : 0000|协会
         * UserName : T9990001
         * Jwt : null
         * BoundToUnitID : 697cb41b-ff88-4e91-a407-d5d2eef4be69
         * HasCertificate : true
         * IsConstructionSandSupplier : false
         */

        private boolean IsFirstLogin;
        private String Token;
        private String UserPower;
        private String UserId;
        private String PhoneNumber;
        private String MemberInfoStr;
        private String UserName;
        private Object Jwt;
        private String BoundToUnitID;
        private boolean HasCertificate;
        private boolean IsConstructionSandSupplier;

        public boolean isIsFirstLogin() {
            return IsFirstLogin;
        }

        public void setIsFirstLogin(boolean IsFirstLogin) {
            this.IsFirstLogin = IsFirstLogin;
        }

        public String getToken() {
            return Token;
        }

        public void setToken(String Token) {
            this.Token = Token;
        }

        public String getUserPower() {
            return UserPower;
        }

        public void setUserPower(String UserPower) {
            this.UserPower = UserPower;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String UserId) {
            this.UserId = UserId;
        }

        public String getPhoneNumber() {
            return PhoneNumber;
        }

        public void setPhoneNumber(String PhoneNumber) {
            this.PhoneNumber = PhoneNumber;
        }

        public String getMemberInfoStr() {
            return MemberInfoStr;
        }

        public void setMemberInfoStr(String MemberInfoStr) {
            this.MemberInfoStr = MemberInfoStr;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public Object getJwt() {
            return Jwt;
        }

        public void setJwt(Object Jwt) {
            this.Jwt = Jwt;
        }

        public String getBoundToUnitID() {
            return BoundToUnitID;
        }

        public void setBoundToUnitID(String BoundToUnitID) {
            this.BoundToUnitID = BoundToUnitID;
        }

        public boolean isHasCertificate() {
            return HasCertificate;
        }

        public void setHasCertificate(boolean HasCertificate) {
            this.HasCertificate = HasCertificate;
        }

        public boolean isIsConstructionSandSupplier() {
            return IsConstructionSandSupplier;
        }

        public void setIsConstructionSandSupplier(boolean IsConstructionSandSupplier) {
            this.IsConstructionSandSupplier = IsConstructionSandSupplier;
        }
    }
}
