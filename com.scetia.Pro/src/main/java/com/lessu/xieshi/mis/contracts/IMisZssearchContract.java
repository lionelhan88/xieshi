package com.lessu.xieshi.mis.contracts;

import com.lessu.xieshi.mis.base.BasePresenter;
import com.lessu.xieshi.mis.base.BaseView;
import com.lessu.xieshi.mis.bean.Miszhengshu;

/**
 * Created by fhm on 2017/10/11.
 */

public interface IMisZssearchContract {
    interface  View extends BaseView<Presenter>{
        void ZssearchCall(boolean isSuccess, Miszhengshu miszhengshu);
    }


    interface Presenter extends BasePresenter{
        void Zssearch(String token,String key);
    }

}
