package com.lessu.xieshi.module.mis.activities;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.mis.adapter.EvaluationComparisonListAdapter;
import com.lessu.xieshi.module.mis.bean.EvaluationComparisonBean;
import com.lessu.xieshi.module.mis.viewmodel.EvaluationComparisonPrintViewModel;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.Util.DateUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by Lollipop
 * on 2021/4/15
 */
public class EvaluationComparisonPrintActivity extends NavigationActivity {
    @BindView(R.id.evaluation_comparison_list_search_view)
    SearchView evaluationComparisonListSearchView;
    @BindView(R.id.tv_evaluation_comparison_top_search)
    TextView tvEvaluationComparisonTopSearch;
    @BindView(R.id.rv_evaluation_comparison_list)
    RecyclerView rvEvaluationComparisonList;
    @BindView(R.id.refresh_evaluation_comparison_list)
    SmartRefreshLayout refreshEvaluationComparisonList;
    @BindView(R.id.tv_matter_state)
    TextView tvEvaluationComparisonState;
    private EvaluationComparisonPrintViewModel viewModel;
    private EvaluationComparisonListAdapter listAdapter;
    private HashMap<String,Object> params;
    private String approveType;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_evaluation_comparison;
    }

    @Override
    protected void observerData() {
        viewModel = new ViewModelProvider(this).get(EvaluationComparisonPrintViewModel.class);
        viewModel.getLoadState().observe(this,loadState -> {
            switch (loadState) {
                case LOADING:
                    LSAlert.showProgressHud(this, getResources().getString(R.string.loading_data_text));
                    break;
                case SUCCESS:
                    LSAlert.dismissProgressHud();
                    refreshEvaluationComparisonList.finishRefresh(true);
                    break;
                case FAILURE:
                    LSAlert.dismissProgressHud();
                    LSAlert.showAlert(this,loadState.getMessage());
                    refreshEvaluationComparisonList.finishRefresh(false);
                    break;
            }
            evaluationComparisonListSearchView.clearFocus();
        });

        viewModel.getEvaluationComparisonLiveData().observe(this,evaluationComparisonBeans -> {
            listAdapter.setNewData(evaluationComparisonBeans);
            rvEvaluationComparisonList.scrollToPosition(0);
        });
        viewModel.getApproveState().observe(this,approveType->{
            this.approveType = approveType;
        });
    }

    @Override
    protected void initView() {
        setTitle("证书打印列表");
        evaluationComparisonListSearchView.setIconifiedByDefault(false);
        setUnderLinearTransparent(evaluationComparisonListSearchView);
        SearchView.SearchAutoComplete searchEdit = evaluationComparisonListSearchView.findViewById(R.id.search_src_text);
        searchEdit.setTextSize(15);
        refreshEvaluationComparisonList.setEnableLoadMore(false);//禁止加载更多
        rvEvaluationComparisonList.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new EvaluationComparisonListAdapter();
        rvEvaluationComparisonList.setAdapter(listAdapter);
        evaluationComparisonListSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.loadData(true,params);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                params.put(Constants.EvaluationComparison.REQUEST_PARAM_KEY_MEMBER_NAME_NO,newText);
                return false;
            }
        });
        //刷新
        refreshEvaluationComparisonList.setOnRefreshListener(refreshLayout -> {
            viewModel.loadData(false,params);
        });
        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            EvaluationComparisonBean.EvaluationComparisonItem bean = (EvaluationComparisonBean.EvaluationComparisonItem) adapter.getItem(position);
            //将对象传递到详情页面
            Intent intent = new Intent(this,EvaluationComparisonDetailActivity.class);
            intent.putExtra(Constants.EvaluationComparison.KEY_APPROVE_TYPE,approveType);
            intent.putExtra(Constants.EvaluationComparison.KEY_CONTENT_BEAN,bean);
            startActivity(intent);
        });
    }

    @Override
    protected void initData() {
        //初始化加载“申请中”的列表项
        params = new HashMap<>();
        params.put(Constants.EvaluationComparison.REQUEST_PARAM_KEY_MEMBER_NAME_NO,"");
        params.put(Constants.EvaluationComparison.REQUEST_PARAM_KEY_STATE,Constants.EvaluationComparison.STATE_APPLYING);
        viewModel.loadData(true,params);
        tvEvaluationComparisonState.setText("申请中");
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ApproveSuccess(GlobalEvent<Boolean> event){
        if(event.getCode()==EventBusUtil.E&& event.getData() != null){
            //批准成功后，返回当前页面需要刷新列表
            viewModel.loadData(false,params);
        }
    }

    @OnClick({R.id.tv_matter_state,R.id.tv_evaluation_comparison_top_search})
    public void onButtonClickDid(View view){
        switch (view.getId()){
            case R.id.tv_matter_state:
                String[] stringArray = getResources().getStringArray(R.array.annual_leave_state);
                DateUtil.itemMenuPicker(this, Arrays.asList(stringArray), (options1, options2, options3, v) -> {
                    tvEvaluationComparisonState.setText(stringArray[options1]);
                    if (options1==0){
                        params.put(Constants.EvaluationComparison.REQUEST_PARAM_KEY_STATE,Constants.EvaluationComparison.STATE_ALL);
                    }else if(options1==1){
                        params.put(Constants.EvaluationComparison.REQUEST_PARAM_KEY_STATE,Constants.EvaluationComparison.STATE_APPLYING);
                    }else if(options1==2){
                        params.put(Constants.EvaluationComparison.REQUEST_PARAM_KEY_STATE,Constants.EvaluationComparison.STATE_APPROVED);
                    }
                    viewModel.loadData(true,params);
                });
                break;
            case R.id.tv_evaluation_comparison_top_search:
                //点击搜索按钮
                //TODO:刷新数据
                viewModel.loadData(true,params);
                break;
        }
    }
}
