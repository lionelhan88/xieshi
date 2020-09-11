package com.lessu.xieshi.meet.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.LazyFragment;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.meet.ReplaceActivity;
import com.lessu.xieshi.meet.bean.MeetingBean;
import com.lessu.xieshi.meet.event.CompanyListToReplace;
import com.lessu.xieshi.meet.event.MisMeetingFragmentToMis;
import com.lessu.xieshi.meet.event.ReplaceSignAddEvent;
import com.lessu.xieshi.mis.activitys.Content;
import com.lessu.xieshi.meet.adapter.CompanySignListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CompanySignFragment extends LazyFragment {
    @BindView(R.id.mis_fragment_company_sign_rv)
    RecyclerView misFragmentCompanySignRv;
    @BindView(R.id.mis_meeting_fragment_refresh)
    SmartRefreshLayout misMeetingFragmentRefresh;
    private CompanySignListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meeting_company_sign;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        listAdapter = new CompanySignListAdapter(R.layout.meeting_company_sign_list_item_layout);
        misFragmentCompanySignRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        misFragmentCompanySignRv.setAdapter(listAdapter);
        listAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.tv_meeting_company_guid_none_sign){
                    MeetingBean.MeetingUserBean bean = (MeetingBean.MeetingUserBean) adapter.getItem(position);
                    EventBus.getDefault().postSticky(new CompanyListToReplace(bean));
                    startActivity(new Intent(getActivity(), ReplaceActivity.class));
                }
            }
        });
    }

    @Override
    protected void initData() {
        misMeetingFragmentRefresh.autoRefresh();
        misMeetingFragmentRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getMeetingList(Content.gettoken(), getArguments().getString("meetingID"),
                        new ResultResponse() {
                            @Override
                            public void getResult(boolean success, JsonElement result, String errorMsg) {
                                if(misMeetingFragmentRefresh==null){
                                    return;
                                }
                                misMeetingFragmentRefresh.finishRefresh();
                                if(result==null){
                                    return;
                                }
                                if (success) {
                                    listAdapter.setNewData(new ArrayList<MeetingBean.MeetingUserBean>());
                                    JsonArray data = result.getAsJsonObject().get("Data").getAsJsonArray();
                                    List<MeetingBean> meetingBeans = GsonUtil.JsonToList(data.toString(),MeetingBean.class);
                                    if(meetingBeans.size()==0){
                                        return;
                                    }
                                    EventBus.getDefault().post(new MisMeetingFragmentToMis(meetingBeans.get(0)));
                                    for (MeetingBean.MeetingUserBean meetingUserBean : meetingBeans.get(0).getListUserContent()) {
                                        /**SubstituteSign=0就是非代替签到
                                         * SubstituteSign = 1有两种情况，一种是手动签到，一种是被指派参会，被指派参会人是属于单位签到
                                         * 需要多判断一下
                                         */
                                        if ((meetingUserBean.getSubstituteSign().equals("0")
                                                ||(meetingUserBean.getSubstituteSign().equals("1")&&!meetingUserBean.getSubstituteUser().equals("")))
                                                && meetingUserBean.getType().equals("0")) {
                                            //未签到的排在前面
                                            if (meetingUserBean.getCheckStatus().equals("1")) {
                                                //已经签到的排在后面
                                                listAdapter.addData(meetingUserBean);
                                            } else {
                                                listAdapter.addData(0, meetingUserBean);
                                            }
                                        }
                                    }
                                } else {
                                    LSAlert.showAlert(getActivity(), "没有数据了！");
                                }
                            }
                        });
            }

        });

    }
    @Override
    protected void initImmersionBar() {

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveReplaceRefresh(ReplaceSignAddEvent event){
        if(event.isRefresh()) {
            //代签成功后，返回当前页面刷新列表
            initData();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
