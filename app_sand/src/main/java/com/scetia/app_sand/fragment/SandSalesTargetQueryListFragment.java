package com.scetia.app_sand.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lessu.navigation.NavigationBar;
import com.lessu.uikit.views.LSAlert;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.app_sand.R;
import com.scetia.app_sand.R2;
import com.scetia.app_sand.adapter.CompanyQueryResultListAdapter;
import com.scetia.app_sand.base.BaseVMFragment;
import com.scetia.app_sand.bean.AddedSandSalesTargetBean;
import com.scetia.app_sand.bean.SandSalesTargetBean;
import com.scetia.app_sand.util.ToastUtil;
import com.scetia.app_sand.viewmodel.SanSalesQueryListViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/11/9
 * <p>
 * 销售对象查询结果页面
 */
public class SandSalesTargetQueryListFragment extends BaseVMFragment<SanSalesQueryListViewModel> {
    @BindView(R2.id.sand_company_query_result_rv)
    RecyclerView sandCompanyQueryResultRv;
    @BindView(R2.id.company_query_result_bottom_save)
    TextView companyQueryResultBottomSave;
    @BindView(R2.id.test_info_query_back)
    ImageView testInfoQueryBack;
    @BindView(R2.id.testing_info_query_search)
    SearchView testingInfoQuerySearch;
    @BindView(R2.id.testing_info_query_toolbar)
    Toolbar testingInfoQueryToolbar;
    @BindView(R2.id.supplier_query_result_rg)
    RadioGroup supplierQueryResultRg;
    @BindView(R2.id.refresh_sand_sales_query_list)
    SwipeRefreshLayout refreshSandSalesQueryList;
    //查询关键字，默认为""
    private String queryKey="";
    private CompanyQueryResultListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_company_query_result;
    }

    @Override
    protected Class<SanSalesQueryListViewModel> getViewModelClass() {
        return SanSalesQueryListViewModel.class;
    }

    @Override
    protected void observerData() {
        viewModel.getPageState().observe(this, loadState -> {
            refreshSandSalesQueryList.setRefreshing(loadState==LoadState.LOAD_INIT);
            listAdapter.setLoadState(loadState);
        });
        viewModel.getLoadState().observe(this,loadState -> {
            switch (loadState){
                case LOADING:
                    LSAlert.showProgressHud(requireActivity(),"正在提交...");
                    break;
                case SUCCESS:
                    LSAlert.dismissProgressHud();
                    LSAlert.showAlert(requireActivity(), "提交成功");
                    for (SandSalesTargetBean bean : listAdapter.getDelSalesTargetBeans()) {
                        listAdapter.getAddedSalesTargets().remove(bean.getSerialNo());
                    }
                    listAdapter.clearSelectData();
                    EventBusUtil.sendEvent(new GlobalEvent<>(EventBusUtil.A,true));
                    break;
                case FAILURE:
                    LSAlert.dismissProgressHud();
                    LSAlert.showAlert(requireActivity(),loadState.getMessage());
                    break;
            }
        });
    }

    @Override
    protected void initView() {
        testingInfoQuerySearch.setIconifiedByDefault(false);
        setUnderLinearTransparent(testingInfoQuerySearch);
        setSwipeRefresh(refreshSandSalesQueryList);
        listAdapter = new CompanyQueryResultListAdapter(viewModel);
        sandCompanyQueryResultRv.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        sandCompanyQueryResultRv.setAdapter(listAdapter);
        listAdapter.setAdapterItemClickListener((position, sandSalesTargetBean) -> {
            sandSalesTargetBean.setSelect(!sandSalesTargetBean.isSelect());
            if(sandSalesTargetBean.isSelect()){
                listAdapter.addSelect(sandSalesTargetBean);
            }else{
                listAdapter.removeSelect(sandSalesTargetBean);
            }
            listAdapter.notifyItemChanged(position);
        });

        supplierQueryResultRg.setOnCheckedChangeListener((group, checkedId) -> {
            viewModel.setQueryKey(queryKey);
            if (checkedId == R.id.supplier_query_result_rb_left) {
                viewModel.setQueryType("预拌混凝土");
            } else if (checkedId == R.id.supplier_query_result_rb_center) {
                viewModel.setQueryType("预制构件");
            } else if (checkedId == R.id.supplier_query_result_rb_right) {
                viewModel.setQueryType("预拌砂浆");
            }
        });

        //刷新操作
        refreshSandSalesQueryList.setOnRefreshListener(() -> {
           listAdapter.clearSelectData();
            viewModel.refresh();
        });

        //点击查询按钮
        testingInfoQuerySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.setQueryKey(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryKey = newText;
                return false;
            }
        });
    }

    @Override
    protected NavigationBar createTopBarView() {
        return null;
    }

    @Override
    protected View getImmersionBarNavigation() {
        return testingInfoQueryToolbar;
    }


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void initEventData(GlobalEvent<List<AddedSandSalesTargetBean>> event){
        if(event.getCode()== EventBusUtil.C){
            HashMap<String,String> map = new HashMap<>();
            for (AddedSandSalesTargetBean bean:event.getData()){
                map.put(bean.getCustomerUnitMemberCode(),bean.getId());
            }
            listAdapter.setAddedSalesTargets(map);
            viewModel.getPagedListLiveData().observe(this, sandSalesTargetBeans ->
                    listAdapter.submitList(sandSalesTargetBeans));
        }

    }

    @OnClick({R2.id.test_info_query_back, R2.id.company_query_result_bottom_save, R2.id.tv_top_bar_search})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.test_info_query_back) {
            leftNavBarClick(view);
        } else if (id == R.id.company_query_result_bottom_save) {
            if (listAdapter.getSelectSalesTargetBeans().size() == 0 && listAdapter.getDelSalesTargetBeans().size() == 0
                    && listAdapter.getAddedSalesTargets().size() != 0) {
                ToastUtil.showShort("已经存在销售对象，不可重复添加！");
                return;
            }
            if (listAdapter.getAddedSalesTargets().size() == 0 && listAdapter.getSelectSalesTargetBeans().size() == 0) {
                ToastUtil.showShort("请选中需要添加的销售对象！");
                return;
            }
            viewModel.addSandSalesTarget(listAdapter.getSelectSalesTargetBeans(), listAdapter.getDelSalesTargetIds());
        } else if (id == R.id.tv_top_bar_search) {
            viewModel.setQueryKey(queryKey);
        }
    }

}
