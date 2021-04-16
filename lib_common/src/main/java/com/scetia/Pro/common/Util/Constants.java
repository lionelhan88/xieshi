package com.scetia.Pro.common.Util;


import java.util.HashMap;

/**
 * Created by fhm on 2016/7/21.
 */
public class Constants {
    //用户相关的信息
    public static class User {
        public static final String KEY_USER_NAME = "USERNAME";
        public static final String KEY_PASSWORD = "PASSWORD";
        public static final String KEY_USER_POWER = "USERPOWER";
        public static final String KEY_USER_FULL_NAME = "USERFULLNAME";
        public static final String KEY_DEVICE_ID = "DEVICEID";
        public static final String PIC_NAME = "PICNAME";
        public static final String XS_TOKEN = "Token";
        public static final String XS_PHONE_NUMBER = "PhoneNumber";
        //存放jwt的值
        public static final String JWT_KEY = "jwt_key";
        //2020-06-01 存入user_id，为了扫码登录
        public static final String USER_ID = "user_id";
        //用户单位信息
        public static final String MEMBER_INFO_STR = "MemberInfoStr";

        private static HashMap<String, String> boundUnitIdMap;

        public static void setBoundUnitId(String boundUnitId) {
            if (boundUnitIdMap == null) {
                boundUnitIdMap = new HashMap<>();
            }
            boundUnitIdMap.put("boundUnitId", boundUnitId);
        }

        public static String GET_BOUND_UNIT_ID() {
            return boundUnitIdMap.get("boundUnitId");
        }

        public static String GET_TOKEN() {
            //秘书长token
            //String testToken = "69D28D31-F25B-4A3A-8981-043670CA943A";
            //郑健
            //String testToken = "DFDDF84F-699B-45DA-A625-3AF9032681AA";
            //return testToken;
            return SPUtil.getSPLSUtil(XS_TOKEN, "");
        }
    }


    //设置相关
    public static class Setting {
        public static final String EXIT = "exit";
        public static final String SERVICE = "service";
        //在扫码入口 需要识别具体的扫码动作
        public static final String SCAN_MEETING_SIGNED = "scan_meeting_signed";
        public static String SCAN_TYPE = "";
    }

    //流向信息相关
    public static class FlowDeclaration {
        public static final String COMMISSIONED_STATE = "已委托";
        public static final String UN_COMMISSION_STATE = "未委托";
    }

    //事项审批相关
    public static class SealManage {
        public static final String KEY_SEAL_APPLY_CONTENT = "s3";
        public static final String KEY_SEAL_MATTER_STATE = "s2";
        public static final String KEY_SEAL_MATTER_TYPE = "s1";
        public static final String KEY_SEAL_MANAGE_BEAN = "key_seal_manage_bean";
        public static final String KEY_YZ_FJ_BEAN = "key_yz_fj_bean";
        //申请中
        public static final int STATE_APPLYING = 0;
        //已审核
        public static final int STATE_REVIEWED = 1;
        //已批准
        public static final int STATE_APPROVED = 2;
        //已盖章
        public static final int STATE_STAMP = 3;
        //审核未通过
        public static final int STATE_UN_REVIEWED = 4;
        //批准未通过
        public static final int STATE_UN_APPROVED = 5;


        //审核通过
        public static final int OPERATE_REVIEW = 1;
        //批准通过
        public static final int OPERATE_APPROVE = 2;
        //审核未通过
        public static final int OPERATE_UN_REVIEW = 3;
        //批准未通过
        public static final int OPERATE_UN_APPROVE = 4;
        //撤销审核
        public static final int OPERATE_REVIEW_REVOKE = 5;
        //撤销批准
        public static final int OPERATE_APPROVE_REVOKE = 6;

        //是否盖章
        public static final int STAMP_OK = 1;
        public static final int STAMP_NO = 0;
        //部门领导
        public static final int DEPARTMENT_LEADER = 1;
        //秘书长
        public static final int SECRETARY_GENERAL = 2;
        //附件的服务地址
        public static final String ANNEX_HOST = "http://www.scetia.com/DownLoad_YZ/";
    }

    //工地信息相关
    public static class Site {
        public static final String KEY_PROJECT_ID = "projectid";
        public static final String KEY_PROJECT_NAME = "projectName";
        public static final String KEY_PROJECT_AREA = "projectArea";
        //材料查询
        public static final int SITE_INFO_SEARCH_BY_MATERIAL = 0;
        //工程查询
        public static final int SITE_INFO_SEARCH_BY_PROJECT = 1;
    }

    //证书打印批准打印相关
    public static class EvaluationComparison {
        public static final String REQUEST_PARAM_KEY_MEMBER_NAME_NO="s1";
        //0申请中 1已批准
        public static final String REQUEST_PARAM_KEY_STATE="s2";
        //申请中
        public static final int STATE_APPLYING = 0;
        //已批准
        public static final int STATE_APPROVED = 1;
        //全部
        public static final String STATE_ALL = "";

        public static final String KEY_APPROVE_TYPE = "key_approve_type";
        public static final String KEY_CONTENT_BEAN = "key_content_bean";

        public static final String APPROVE_DISABLE="0";
        public static final String APPROVE_ENABLE="1";
    }


}
