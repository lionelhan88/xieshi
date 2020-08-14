package com.lessu.xieshi.mis.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.lessu.xieshi.meet.event.SendMeetingListToDetail;
import com.lessu.xieshi.mis.activitys.Content;
import com.lessu.xieshi.mis.activitys.MisMeetingActivity;
import com.lessu.xieshi.mis.adapter.CompanySignListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CompanySignFragment extends LazyFragment {
    @BindView(R.id.mis_fragment_company_sign_rv)
    RecyclerView misFragmentCompanySignRv;
    @BindView(R.id.mis_meeting_fragment_refresh)
    SmartRefreshLayout misMeetingFragmentRefresh;
    private CompanySignListAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserVisibleHint(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meeting_company_sign;
    }

    @Override
    protected void initView() {
        listAdapter = new CompanySignListAdapter(R.layout.meeting_company_sign_list_item_layout);
        misFragmentCompanySignRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        misFragmentCompanySignRv.setAdapter(listAdapter);
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
                                        //不是代签，并且是单位性质的
                                        if (meetingUserBean.getSubstituteSign().equals("0") && meetingUserBean.getType().equals("0")) {
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


}
