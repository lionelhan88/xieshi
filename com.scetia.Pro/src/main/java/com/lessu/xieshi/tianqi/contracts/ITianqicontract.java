package com.lessu.xieshi.tianqi.contracts;

import com.lessu.xieshi.mis.base.BasePresenter;
import com.lessu.xieshi.tianqi.base.BaseView;
import com.lessu.xieshi.tianqi.bean.Hourbean;
import com.lessu.xieshi.tianqi.bean.Tenbean;

/**
 * Created by fhm on 2017/10/26.
 */

public interface ITianqicontract {
    interface View extends BaseView<Presenter>{
        /**
         * 获取10天天气预报的回调
         * @param issucess
         */
        void gettencall(boolean issucess, Tenbean tenbean);

        /**
         * 获取未来几小时天气预报的回调
         * @param issucess
         */

        void gethourcall(boolean issucess, Hourbean hourbean);
    }

    interface Presenter extends BasePresenter{
        /**
         * 获取10天天气预报
         * @param token
         */
        void getten(String token);


        /**
         * 获取未来几小时天气预报
         */
        void gethour(String token,String JD,String WD);


    }






}
