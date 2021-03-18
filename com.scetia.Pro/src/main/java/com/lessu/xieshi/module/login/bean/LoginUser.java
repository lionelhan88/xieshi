package com.lessu.xieshi.module.login.bean;

/**
 * created by ljs
 * on 2020/11/25
 */
public class LoginUser {
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
    private String Jwt;
    private String BoundToUnitID;
    //建设用砂供应商帐号持有者是否持证
    private boolean HasCertificate;
    //是否是供应商
    private boolean IsConstructionSandSupplier;
    private String UserFullName;

    public boolean isConstructionSandSupplier() {
        return IsConstructionSandSupplier;
    }

    public void setConstructionSandSupplier(boolean constructionSandSupplier) {
        IsConstructionSandSupplier = constructionSandSupplier;
    }

    public String getUserFullName() {
        return UserFullName;
    }

    public void setUserFullName(String userFullName) {
        UserFullName = userFullName;
    }

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

    public String getJwt() {
        return Jwt;
    }

    public void setJwt(String Jwt) {
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

    private static LoginUser loginUser;
    public static void setLoginUser(LoginUser user){
        loginUser = user;
    }
    public static LoginUser GET_LOGIN_USER(){
        return loginUser;
    }

}
