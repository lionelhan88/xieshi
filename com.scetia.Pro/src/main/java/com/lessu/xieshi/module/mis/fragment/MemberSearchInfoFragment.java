package com.lessu.xieshi.module.mis.fragment;

import android.widget.TextView;

import com.lessu.navigation.NavigationBar;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.mis.bean.MisMemberSearchResultData;
import com.scetia.Pro.baseapp.fragment.BaseFragment;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * created by Lollipop
 * on 2022/1/13
 */
public class MemberSearchInfoFragment extends BaseFragment {
    private TextView tv_mishy_huiyuanhao;
    private TextView tv_mishy_name;
    private TextView tv_mishy_xingzhi;
    private TextView tv_mishy_zhuangtai;
    private TextView tv_mishy_ruhuidate;
    private TextView tv_mishy_bianhao;
    private TextView tv_mishy_daoqidate;
    private TextView tv_mishy_fuzename;
    private TextView tv_mishy_fuzephone;
    private TextView tv_mishy_danweiphone;
    private TextView tv_mishy_dizhi;
    @Override
    protected boolean isNeedDispatchKeyBack() {
        return  false;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_member_search_info;
    }
    @Override
    protected void initView() {
        tv_mishy_huiyuanhao = contentView.findViewById(R.id.tv_mishy_huiyuanhao);
        tv_mishy_name = contentView.findViewById(R.id.tv_mishy_name);
        tv_mishy_xingzhi = contentView.findViewById(R.id.tv_mishy_xingzhi);
        tv_mishy_zhuangtai = contentView.findViewById(R.id.tv_mishy_zhuangtai);
        tv_mishy_ruhuidate = contentView.findViewById(R.id.tv_mishy_ruhuidate);
        tv_mishy_bianhao = contentView.findViewById(R.id.tv_mishy_bianhao);
        tv_mishy_daoqidate = contentView.findViewById(R.id.tv_mishy_daoqidate);
        tv_mishy_fuzename = contentView.findViewById(R.id.tv_mishy_fuzename);
        tv_mishy_fuzephone = contentView.findViewById(R.id.tv_mishy_fuzephone);
        tv_mishy_danweiphone = contentView.findViewById(R.id.tv_mishy_danweiphone);
        tv_mishy_dizhi = contentView.findViewById(R.id.tv_mishy_dizhi);
        EventBusUtil.register(this);
    }

    @Override
    protected NavigationBar createTopBarView() {
        return null;
    }

    /**
     * 初始化数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void loadData(GlobalEvent<MisMemberSearchResultData.ListContentBean> event) {
        if(event.getCode() ==EventBusUtil.E){
            MisMemberSearchResultData.ListContentBean contentBean = event.getData();
            tv_mishy_huiyuanhao.setText(contentBean.getMemberId());
            tv_mishy_name.setText(contentBean.getMemberName());
            tv_mishy_xingzhi.setText(contentBean.getMemberType());
            tv_mishy_zhuangtai.setText(contentBean.getMemberStatus());
            tv_mishy_ruhuidate.setText(contentBean.getJoinDate());
            tv_mishy_bianhao.setText(contentBean.getCertificateId());
            tv_mishy_daoqidate.setText(contentBean.getCertificateExpirationDate());
            String fzr = contentBean.getFzr();
            String fzname="";
            String fzphone="";
            if(fzr.contains("/")){
                String[] split = fzr.split("/");
                fzname = split[0];
                if(split.length>=3&&!"".equals(split[2])) {
                    fzphone = split[2];
                }
            }
            tv_mishy_fuzename.setText(fzname);
            tv_mishy_fuzephone.setText(fzphone);
            tv_mishy_danweiphone.setText(contentBean.getPhoneNumber());
            tv_mishy_dizhi.setText(contentBean.getContactAddress());
        }
    }

    @Override
    public void onDestroyView() {
        EventBusUtil.unregister(this);
        super.onDestroyView();
    }
}
