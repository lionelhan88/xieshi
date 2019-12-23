package com.lessu.xieshi.mis.contracts;

import com.lessu.xieshi.mis.base.BasePresenter;
import com.lessu.xieshi.mis.base.BaseView;
import com.lessu.xieshi.mis.bean.Tongzhibean;

/**
 * Created by fhm on 2017/10/12.
 */

public interface IMistongzhiContract {
    interface View extends BaseView<Presenter>{
        void getTongzhiCall(boolean issuccess, Tongzhibean tongzhibean);
    }

    interface Presenter extends BasePresenter{
        void getTongzhi(String token);
    }


}
