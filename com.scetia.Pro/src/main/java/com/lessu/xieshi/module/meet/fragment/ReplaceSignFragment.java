package com.lessu.xieshi.module.meet.fragment;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.meet.bean.MeetingBean;
import com.lessu.xieshi.module.meet.event.MisMeetingFragmentToMis;
import com.lessu.xieshi.module.meet.event.ReplaceSignAddEvent;
import com.lessu.xieshi.module.mis.activitys.Content;
import com.lessu.xieshi.module.meet.activity.ReplaceActivity;
import com.lessu.xieshi.module.meet.adapter.ReplaceSignListAdapter;
import com.scetia.Pro.baseapp.fragment.LazyFragment;
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
        misMeetingFragmentRefresh.setEnableLoadMore(false);
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
                getMeetingList(Content.getToken(), getArguments().getString("meetingID"),
                        new ResultResponse() {
                            @Override
                            public void getResult(boolean success, JsonElement result, String errorMsg) {
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
                                    /*
                                      SubstituteSign = 1有两种情况，一种是手动签到，一种是被指派参会，被指派参会人是属于单位签到
                                      这里需要判断 SubstituteSign=1但是SubstituteUser是空，说明不是被指派参会人
                                     */
                                    for (MeetingBean.MeetingUserBean meetingUserBean : meetingBeans.get(0).getListUserContent()) {
                                        //代签的
                                        if(meetingUserBean.getSubstituteSign().equals("1")&&meetingUserBean.getSubstituteUser().equals("")) {
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
    protected void stopData() {
        misMeetingFragmentRefresh.finishRefresh();
        if(getApiConnection()!=null) {
            getApiConnection().cancel(true);
        }
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
