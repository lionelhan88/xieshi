package com.lessu.xieshi.module.meet.fragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.LazyFragment;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.meet.bean.MeetingBean;
import com.lessu.xieshi.module.meet.event.MisMeetingFragmentToMis;
import com.lessu.xieshi.module.mis.activitys.Content;
import com.lessu.xieshi.module.meet.adapter.PersonSignListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PersonSignFragment extends LazyFragment {
    @BindView(R.id.mis_fragment_company_sign_rv)
    RecyclerView misFragmentCompanySignRv;
    @BindView(R.id.mis_meeting_fragment_refresh)
    SmartRefreshLayout misMeetingFragmentRefresh;
    private PersonSignListAdapter listAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_meeting_company_sign;
    }

    @Override
    protected void initView() {
        listAdapter = new PersonSignListAdapter(R.layout.meeting_company_sign_list_item_layout);
        misFragmentCompanySignRv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        misFragmentCompanySignRv.setAdapter(listAdapter);
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
                                    for (MeetingBean.MeetingUserBean meetingUserBean : meetingBeans.get(0).getListUserContent()) {
                                        //不是代签，并且是嘉宾签的
                                        if(meetingUserBean.getSubstituteSign().equals("0")&&meetingUserBean.getType().equals("1")) {
                                            //已经签到的排在后面，未签到的排在前面
                                            if(meetingUserBean.getCheckStatus().equals("1")){
                                                listAdapter.addData(meetingUserBean);
                                            }else{
                                                listAdapter.addData(0,meetingUserBean);
                                            }
                                        }
                                    }
                                }
                            }
                        });
            }

        });
    }

    @Override
    protected void initData() {
        misMeetingFragmentRefresh.autoRefresh();
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
}
