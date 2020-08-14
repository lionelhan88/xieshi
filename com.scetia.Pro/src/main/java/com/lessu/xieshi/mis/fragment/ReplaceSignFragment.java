package com.lessu.xieshi.mis.fragment;

import android.content.Intent;
import android.icu.text.Replaceable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.lessu.BaseFragment;
import com.lessu.LazyFragment;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.meet.bean.MeetingBean;
import com.lessu.xieshi.meet.event.MisMeetingFragmentToMis;
import com.lessu.xieshi.meet.event.ReplaceSignAddEvent;
import com.lessu.xieshi.meet.event.SendMeetingListToDetail;
import com.lessu.xieshi.mis.activitys.Content;
import com.lessu.xieshi.mis.activitys.ReplaceActivity;
import com.lessu.xieshi.mis.adapter.PersonSignListAdapter;
import com.lessu.xieshi.mis.adapter.ReplaceSignListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ReplaceSignFragment extends LazyFragment {
    @BindView(R.id.mis_fragment_company_sign_rv)
    RecyclerView misFragmentCompanySignRv;
    private ReplaceSignListAdapter listAdapter;
    @BindView(R.id.mis_meeting_fragment_refresh)
    SmartRefreshLayout misMeetingFragmentRefresh;
    @BindView(R.id.rl_replace)
    RelativeLayout rlReplace;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meeting_company_sign;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        rlReplace.setVisibility(View.VISIBLE);
        listAdapter = new ReplaceSignListAdapter(R.layout.meeting_company_sign_list_item_layout);
        misFragmentCompanySignRv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        misFragmentCompanySignRv.setAdapter(listAdapter);
        rlReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReplaceActivity.class);
                intent.putExtra("meetingID",getArguments().getString("meetingID"));
                startActivity(intent);
            }
        });
        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击列表项进入 代签记录明细
                MeetingBean.MeetingUserBean meetingUserBean = (MeetingBean.MeetingUserBean) adapter.getItem(position);
                EventBus.getDefault().postSticky(new ReplaceSignAddEvent(false,meetingUserBean,getArguments().getString("meetingID")));
                startActivity(new Intent(getActivity(),ReplaceActivity.class));
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
                                        //代签的
                                        if(meetingUserBean.getSubstituteSign().equals("1")) {
                                            listAdapter.addData(meetingUserBean);
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
