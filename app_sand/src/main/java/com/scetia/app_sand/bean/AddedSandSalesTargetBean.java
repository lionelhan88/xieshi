package com.scetia.app_sand.bean;

/**
 * created by ljs
 * on 2020/12/21
 * 已添加的销售对象
 */
public class AddedSandSalesTargetBean {

    /**
     * id : 4f6a7a34-dfab-40ca-5e23-08d8982b3ed0
     * customerUnitMemberCode : 0014
     * customerUnitName : 上海城建物资有限公司隧鼎混凝土分公司六分站
     * customerUnitType : 预拌混凝土
     */

    private String id;
    private String customerUnitMemberCode;
    private String customerUnitName;
    private String customerUnitType;

    public AddedSandSalesTargetBean(String customerUnitMemberCode, String customerUnitName) {
        this.customerUnitMemberCode = customerUnitMemberCode;
        this.customerUnitName = customerUnitName;
    }

    public AddedSandSalesTargetBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerUnitMemberCode() {
        return customerUnitMemberCode;
    }

    public void setCustomerUnitMemberCode(String customerUnitMemberCode) {
        this.customerUnitMemberCode = customerUnitMemberCode;
    }

    public String getCustomerUnitName() {
        return customerUnitName;
    }

    public void setCustomerUnitName(String customerUnitName) {
        this.customerUnitName = customerUnitName;
    }

    public String getCustomerUnitType() {
        return customerUnitType;
    }

    public void setCustomerUnitType(String customerUnitType) {
        this.customerUnitType = customerUnitType;
    }

    public static class SandSalesTargetBatchBean{

        /**
         * id : 57e43e6d-1e81-4f11-194c-08d8a58ed43c
         * supplierUnitId : 20b39d10-3100-488d-be9b-c088c1b8aa1b
         * userId : 7aeeade3-dd47-459a-8665-af13d981a18a
         * salesTargetMemberCode : 0019
         */

        private String id;
        private String supplierUnitId;
        private String userId;
        private String salesTargetMemberCode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSupplierUnitId() {
            return supplierUnitId;
        }

        public void setSupplierUnitId(String supplierUnitId) {
            this.supplierUnitId = supplierUnitId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getSalesTargetMemberCode() {
            return salesTargetMemberCode;
        }

        public void setSalesTargetMemberCode(String salesTargetMemberCode) {
            this.salesTargetMemberCode = salesTargetMemberCode;
        }
    }

}
