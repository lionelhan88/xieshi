package com.lessu.xieshi.module.mis.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.module.mis.adapter.SealManageListAdapter;
import com.lessu.xieshi.module.mis.bean.SealManageBean;
import com.lessu.xieshi.module.mis.viewmodel.SealManageListViewModel;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.Util.DateUtil;
import com.scetia.Pro.common.Util.DensityUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by Lollipop
 * on 2021/3/2
 */
public class SealManageListActivity extends NavigationActivity {
    @BindView(R.id.seal_manage_list_search_view)
    SearchView sealManageListSearchView;
    @BindView(R.id.rv_seal_manage_list)
    RecyclerView rvSealManageList;
    @BindView(R.id.tv_seal_type_top)
    TextView tvSealTypeTop;
    @BindView(R.id.show_loading_error)
    TextView showLoadingError;
    @BindView(R.id.refresh_seal_manage_list)
    SmartRefreshLayout refreshSealManageList;
    @BindView(R.id.tv_matter_state)
    TextView matterState;
    @BindView(R.id.loading_error_refresh)
    TextView loadingErrorRefresh;
    @BindView(R.id.ll_seam_manage_list_error)
    LinearLayout llSeamManageListError;
    private SealManageListViewModel viewModel;
    private SealManageListAdapter listAdapter;
    private HashMap<String, Object> param = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seal_manage_list;
    }

    @Override
    protected void initView() {
        setTitle("事项列表");
        sealManageListSearchView.setIconifiedByDefault(false);
        setUnderLinearTransparent(sealManageListSearchView);
        viewModel = new ViewModelProvider(this).get(SealManageListViewModel.class);
        viewModel.getLoadState().observe(this, loadState -> {
            switch (loadState) {
                case LOADING:
                    LSAlert.showProgressHud(SealManageListActivity.this, "正在加载数据...");
                    break;
                case SUCCESS:
                    LSAlert.dismissProgressHud();
                    refreshSealManageList.finishRefresh(true);
                    llSeamManageListError.setVisibility(View.GONE);
                    break;
                case FAILURE:
                    LSAlert.dismissProgressHud();
                    refreshSealManageList.finishRefresh(false);
                    llSeamManageListError.setVisibility(loadState.getCode()==2000?View.GONE:View.VISIBLE);
                    showLoadingError.setText(loadState.getMessage());
                    break;
            }
        });

        viewModel.getSealManageList().observe(this, sealManageBeans -> {
            listAdapter.setNewData(sealManageBeans);
        });


        listAdapter = new SealManageListAdapter();
        rvSealManageList.setLayoutManager(new LinearLayoutManager(this));
        rvSealManageList.setAdapter(listAdapter);
        rvSealManageList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int div = DensityUtil.dip2px(SealManageListActivity.this, 8);
                outRect.top = div;
                outRect.left = div;
                outRect.right = div;
            }
        });
        //点击查询按钮
        sealManageListSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.getSealManagerListByQuery(param);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    param.remove(Constants.SealManage.KEY_SEAL_APPLY_CONTENT);
                } else {
                    param.put(Constants.SealManage.KEY_SEAL_APPLY_CONTENT, newText);
                }
                return false;
            }
        });
        refreshSealManageList.setEnableLoadMore(false);
        refreshSealManageList.setOnRefreshListener(refreshLayout -> {
            viewModel.getSealManagerListByQuery(param, false);
        });

        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            SealManageBean bean = (SealManageBean) adapter.getItem(position);
            Intent intent = new Intent(this, SealMatterDetailActivity.class);
            intent.putExtra(Constants.SealManage.KEY_SEAL_MANAGE_BEAN, bean);
            startActivity(intent);
        });

    }

    @Override
    protected void initData() {
        param.put(Constants.User.XS_TOKEN, Constants.User.GET_TOKEN());
        viewModel.getSealTypeList();
        viewModel.getSealManagerListByQuery(param, true);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    /**
     * 操作事项成功，刷新列表
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void operatorMatterSuccess(GlobalEvent<Boolean> globalEvent) {
        viewModel.getSealManagerListByQuery(param, false);
    }

    @OnClick({R.id.tv_seal_type_top, R.id.tv_seal_manage_top_search, R.id.tv_matter_state,R.id.loading_error_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_seal_type_top:
                List<SealManageBean.SealType> sealTypes = viewModel.getSealTypes();
                if (sealTypes.size() == 0) {
                    ToastUtil.showShort("暂无事项类型");
                    return;
                }
                DateUtil.itemMenuPicker(this, sealTypes, (options1, options2, options3, v) -> {
                    tvSealTypeTop.setText(sealTypes.get(options1).getText());
                    String value = sealTypes.get(options1).getValue();
                    if(value.equals("")){
                        param.remove(Constants.SealManage.KEY_SEAL_MATTER_TYPE);
                    }else {
                        param.put(Constants.SealManage.KEY_SEAL_MATTER_TYPE, sealTypes.get(options1).getValue());
                    }
                    viewModel.getSealManagerListByQuery(param, true);
                });
                break;
            case R.id.tv_matter_state:
                List<String> arr = Arrays.asList("全部", "申请中", "已审核", "已批准", "已盖章","审核未通过","批准未通过");
                DateUtil.itemMenuPicker(this, arr, (options1, options2, options3, v) -> {
                    matterState.setText(arr.get(options1));
                    if (options1 == 0) {
                        param.put(Constants.SealManage.KEY_SEAL_MATTER_STATE, "");
                    } else {
                        param.put(Constants.SealManage.KEY_SEAL_MATTER_STATE, options1 - 1 + "");
                    }
                    viewModel.getSealManagerListByQuery(param, true);
                });
                break;
            case R.id.tv_seal_manage_top_search:
            case R.id.loading_error_refresh:
                viewModel.getSealManagerListByQuery(param, true);
                break;
        }
    }
}
