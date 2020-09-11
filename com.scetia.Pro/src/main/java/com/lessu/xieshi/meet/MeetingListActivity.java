package com.lessu.xieshi.meet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lessu.navigation.NavigationActivity;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.DateUtil;
import com.lessu.xieshi.Utils.GsonUtil;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.meet.adapter.MeetingListAdapter;
import com.lessu.xieshi.meet.bean.MeetingBean;
import com.lessu.xieshi.meet.event.SendMeetingDetailToList;
import com.lessu.xieshi.meet.event.SendMeetingListToDetail;
import com.lessu.xieshi.mis.activitys.Content;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingListActivity extends NavigationActivity {
    @BindView(R.id.meeting_recycler_view)
    RecyclerView meetingRecyclerView;
    @BindView(R.id.meeting_list_date_select_start)
    TextView meetingListDateSelectStart;
    @BindView(R.id.meeting_list_date_select_end)
    TextView meetingListDateSelectEnd;
    @BindView(R.id.meeting_list_search)
    ImageView meetingListSearch;
    @BindView(R.id.meeting_list_refresh)
    SwipeRefreshLayout meetingListRefresh;
    private MeetingListAdapter listAdapter;
    private int typeUser = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_meeting_list);
        EventBus.getDefault().register(this);
        navigationBar.setBackgroundColor(0xFF3598DC);
        setTitle("会议安排");
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        meetingListRefresh.setColorSchemeResources(R.color.danlan);
        listAdapter = new MeetingListAdapter(R.layout.meeting_list_item_layout);
        meetingRecyclerView.setAdapter(listAdapter);
        meetingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MeetingBean bean = (MeetingBean) adapter.getItem(position);

                //0检测人员会议界面
                if (typeUser == 1) {
                    EventBus.getDefault().postSticky(new SendMeetingListToDetail(bean));
                    startActivity(new Intent(MeetingListActivity.this, MeetingDetailActivity.class));
                } else if (typeUser == 0) {
                    //1 协会工作人员的会议界面
                    Intent intent = new Intent(MeetingListActivity.this, MisMeetingActivity.class);
                    intent.putExtra("meetingID", bean.getMeetingId());
                    startActivity(intent);
                }
            }
        });
        //刷新列表
        meetingListRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMeetingList(false);
            }
        });
    }

    /**
     * 初始化加载值
     */
    private void initData() {
        typeUser = getIntent().getIntExtra("type_user", -1);
        //默认日期为当前日期和延后一周
        meetingListDateSelectStart.setText(DateUtil.getDate(new Date()));
        meetingListDateSelectEnd.setText(DateUtil.getDayAgo(20));
        getMeetingList(true);
    }

    /**
     * 获取会议列表数据
     */
    private void getMeetingList(final boolean isShowLoading) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("Token", Content.gettoken());
        params.put("s2", Shref.getString(this, Common.USERID, ""));
        params.put("s3", meetingListDateSelectStart.getText());
        params.put("s4", meetingListDateSelectEnd.getText());
        params.put("s6", typeUser == 0 ? "0" : "1");
        EasyAPI.apiConnectionAsync(this, isShowLoading, false, ApiMethodDescription.get("/ServiceMis.asmx/GetMeetingList"),
                params, new EasyAPI.ApiFastSuccessFailedCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        if(meetingListRefresh.isRefreshing()){
                            meetingListRefresh.setRefreshing(false);
                        }
                        listAdapter.setNewData(new ArrayList<MeetingBean>());
                        boolean isSuccess = result.getAsJsonObject().get("Success").getAsBoolean();
                        if (isSuccess) {
                            JsonArray data = result.getAsJsonObject().get("Data").getAsJsonArray();
                            List<MeetingBean> meetingBeans = GsonUtil.JsonToList(data.toString(),MeetingBean.class);
                            listAdapter.setNewData(meetingBeans);
                        } else {
                            String message =  result.getAsJsonObject().get("Message").getAsString();
                            View emptyView = LayoutInflater.from(MeetingListActivity.this).inflate(R.layout.empty_layout,null,false);
                            TextView emptyViewText = emptyView.findViewById(R.id.empty_view_text);
                            emptyViewText.setText(message);
                            listAdapter.setEmptyView(emptyView);
                        }
                    }

                    @Override
                    public String onFailed(ApiError error) {
                        if(meetingListRefresh.isRefreshing()){
                            meetingListRefresh.setRefreshing(false);
                        }
                        if(!isShowLoading){
                            ToastUtil.showShort(error.errorMeesage);
                        }
                        return error.errorMeesage;
                    }
                });
    }

    /**
     * 会议详情页面通知此列表页面刷新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMeetingDetailRefresh(SendMeetingDetailToList event) {
        getMeetingList(true);
    }

    @OnClick({R.id.meeting_list_date_select_start, R.id.meeting_list_date_select_end, R.id.meeting_list_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.meeting_list_date_select_start:
                DateUtil.datePicker(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        meetingListDateSelectStart.setText(DateUtil.getDate(date));
                        getMeetingList(true);
                    }
                });
                break;
            case R.id.meeting_list_date_select_end:
                DateUtil.datePicker(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        meetingListDateSelectEnd.setText(DateUtil.getDate(date));
                        getMeetingList(true);
                    }
                });
                break;
            case R.id.meeting_list_search:
                getMeetingList(true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
