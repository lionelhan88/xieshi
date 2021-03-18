package com.lessu.xieshi.module.foundationpile;

/**
 * created by Lollipop
 * on 2021/3/17
 */
public class FoundationPileBean {

    /**
     * map : false
     * x : 0.0
     * y : 0.0
     * stakeName : 0117 , 上海众合检测应用技术研究所有限公司 , 201311-3716B , D101# , 扩建富顿苑南区商品住宅项目（三期）地下车库抗压
     * stakeProjectName : 扩建富顿苑南区商品住宅项目（三期）地下车库抗压
     */

    private boolean map;
    //纬度
    private double x;
    //经度
    private double y;
    private String stakeName;
    private String stakeProjectName;

    public boolean isMap() {
        return map;
    }

    public void setMap(boolean map) {
        this.map = map;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getStakeName() {
        return stakeName;
    }

    public void setStakeName(String stakeName) {
        this.stakeName = stakeName;
    }

    public String getStakeProjectName() {
        return stakeProjectName;
    }

    public void setStakeProjectName(String stakeProjectName) {
        this.stakeProjectName = stakeProjectName;
    }
}
