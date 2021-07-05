package com.scetia.app_sand.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.gyf.immersionbar.ImmersionBar;
import com.lessu.uikit.views.LSAlert;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.app_sand.R;
import com.scetia.app_sand.R2;
import com.scetia.app_sand.adapter.TestingCompanyQueryListAdapter;
import com.scetia.app_sand.base.BaseVMFragment;
import com.scetia.app_sand.bean.AddedTestingCompanyBean;
import com.scetia.app_sand.bean.TestingCompanyBean;
import com.scetia.app_sand.util.ToastUtil;
import com.scetia.app_sand.viewmodel.TestingCompanyQueryListViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/11/9
 */
public class SandTestingCompanyQueryListFragment extends BaseVMFragment<TestingCompanyQueryListViewModel> {
    @BindView(R2.id.testing_company_query_result_rv)
    RecyclerView testingCompanyQueryResultRv;
    @BindView(R2.id.testing_company_query_result_bottom_save)
    TextView testingCompanyQueryResultBottomSave;
    @BindView(R2.id.test_info_query_back)
    ImageView testInfoQueryBack;
    @BindView(R2.id.testing_info_query_search)
    SearchView testingInfoQuerySearch;
    @BindView(R2.id.testing_info_query_toolbar)
    Toolbar testingInfoQueryToolbar;
    @BindView(R2.id.testing_company_query_list_refresh)
    SwipeRefreshLayout testingCompanyQueryListRefresh;
    @BindView(R2.id.testing_company_query_list_counties)
    TextView testingCompanyQueryListCounties;
    @BindView(R2.id.ll_company_query_list_counties)
    LinearLayout llCompanyQueryListCounties;
    private TestingCompanyQueryListAdapter listAdapter;
    private String queryKey = "";
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_testing_company_query_result;
    }

    @Override
    protected Class<TestingCompanyQueryListViewModel> getViewModelClass() {
        return TestingCompanyQueryListViewModel.class;
    }

    @Override
    protected void observerData() {
        viewModel.getPageState().observe(this, loadState -> {
            testingCompanyQueryListRefresh.setRefreshing(loadState == LoadState.LOAD_INIT);
            listAdapter.setLoadState(loadState);
        });

        viewModel.getLoadState().observe(this, loadState -> {
            switch (loadState) {
                case LOADING:
                    LSAlert.showProgressHud(requireActivity(), "正在提交...");
                    break;
                case SUCCESS:
                    LSAlert.dismissProgressHud();
                    LSAlert.showAlert(requireActivity(), "提交成功");
                    for (TestingCompanyBean bean : listAdapter.getDelTestingCompanyBeans()) {
                        listAdapter.getAddedTestingCompanies().remove(bean.getMemberCode());
                    }
                    listAdapter.clearSelectData();
                    EventBusUtil.sendEvent(new GlobalEvent<>(EventBusUtil.A,true));
                    break;
                case FAILURE:
                    LSAlert.dismissProgressHud();
                    LSAlert.showAlert(requireActivity(), loadState.getMessage());
                    break;
            }
        });
    }

    @Override
    protected void initView() {
        navigationBar.setVisibility(View.GONE);
        testingInfoQuerySearch.setIconifiedByDefault(false);
        setSwipeRefresh(testingCompanyQueryListRefresh);
        setUnderLinearTransparent(testingInfoQuerySearch);
        listAdapter = new TestingCompanyQueryListAdapter(viewModel);
        testingCompanyQueryResultRv.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        testingCompanyQueryResultRv.setAdapter(listAdapter);
        //刷新操作
        testingCompanyQueryListRefresh.setOnRefreshListener(() -> {
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
    public void initImmersionBar() {
        ImmersionBar.with(this).titleBar(testingInfoQueryToolbar)
                .navigationBarColor(R.color.light_gray)
                .navigationBarDarkIcon(true)
                .init();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void initEventData(GlobalEvent<List<AddedTestingCompanyBean>> event) {
        if (event.getCode() == EventBusUtil.B) {
            HashMap<String, String> map = new HashMap<>();
            for (AddedTestingCompanyBean bean : event.getData()) {
                map.put(bean.getDetectionAgencyMemberCode(), bean.getId());
            }
            listAdapter.setAddedTestingCompanies(map);
            viewModel.getPagedListLiveData().observe(this, testingCompanyBeans ->
                    listAdapter.submitList(testingCompanyBeans));
        }

    }

    @OnClick({R2.id.test_info_query_back, R2.id.tv_top_bar_search, R2.id.testing_company_query_result_bottom_save,
            R2.id.testing_company_query_list_counties})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.test_info_query_back) {
            leftNavBarClick(view);
        } else if (id == R.id.tv_top_bar_search) {
            viewModel.setQueryKey(queryKey);
        } else if (id == R.id.testing_company_query_result_bottom_save) {
            if (listAdapter.getSelectTestingCompanyBeans().size() == 0 && listAdapter.getDelTestingCompanyBeans().size() == 0
                    && listAdapter.getAddedTestingCompanies().size() != 0) {
                ToastUtil.showShort("已经存在检测单位，不可重复添加！");
                return;
            }
            if (listAdapter.getAddedTestingCompanies().size() == 0 && listAdapter.getSelectTestingCompanyBeans().size() == 0) {
                ToastUtil.showShort("请选中需要添加的检测单位！");
                return;
            }
            viewModel.addTestingCompanies(listAdapter.getSelectTestingCompanyBeans(), listAdapter.getDelTestingCompanyIds());
        } else if (id == R.id.testing_company_query_list_counties) {
            final String[] counties = getResources().getStringArray(R.array.counties_arr);
            OptionsPickerView<String> build = new OptionsPickerBuilder(requireActivity(),
                    (options1, options2, options3, v) -> {
                        testingCompanyQueryListCounties.setText(counties[options1]);
                        viewModel.setQueryCounties(counties[options1].equals("全部") ? "" : counties[options1]);
                    }).build();
            build.setPicker(Arrays.asList(counties));
            build.show();
        }
    }

}
