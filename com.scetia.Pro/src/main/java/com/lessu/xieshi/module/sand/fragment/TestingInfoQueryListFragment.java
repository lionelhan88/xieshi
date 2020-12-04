package com.lessu.xieshi.module.sand.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.lessu.BaseFragment;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.adapter.SMFlowDeclarationListAdapter;
import com.lessu.xieshi.module.sand.adapter.TestingInfoQueryListAdapter;
import com.lessu.xieshi.module.sand.bean.TestingQueryResultBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/10/30
 */
public class TestingInfoQueryListFragment extends BaseFragment {
    @BindView(R.id.test_info_query_back)
    ImageView testInfoQueryBack;
    @BindView(R.id.testing_info_query_search)
    SearchView testingInfoQuerySearch;
    @BindView(R.id.testing_info_query_toolbar)
    Toolbar testingInfoQueryToolbar;
    @BindView(R.id.testing_info_query_list_rv)
    RecyclerView testingInfoQueryListRv;
    @BindView(R.id.testing_info_query_list_refresh)
    SmartRefreshLayout testingInfoQueryListRefresh;

    private TestingInfoQueryListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_testing_info_query_list;
    }

    @Override
    protected void initView() {
        navigationBar.setVisibility(View.GONE);
        testingInfoQuerySearch.setIconifiedByDefault(false);
        setUnderLinearTransparent(testingInfoQuerySearch);
        initRecyclerView();
    }

    @Override
    protected void initData() {
        for (int i=0;i<=10;i++){
            TestingQueryResultBean bean = new TestingQueryResultBean();
            bean.setS1("人工砂");
            bean.setS3("2020-11-01");
            bean.setS4("上海检测技术公司");
            bean.setS5("0063");
            if(i%2==0){
                bean.setS2("在检已出报告");
                bean.setS6(0);
            }else if(i%3==0){
                bean.setS2("在检已出报告");
                bean.setS6(1);
            }else{
                bean.setS2("检测中");
                bean.setS6(2);
            }
            listAdapter.addData(bean);
        }
    }

    @Override
    protected void initImmersionBar() {
        ImmersionBar.with(this).titleBar(testingInfoQueryToolbar)
                .navigationBarColor(com.lessu.uikit.R.color.light_gray)
                .navigationBarDarkIcon(true)
                .init();
    }

    /**
     * 初始化列表刷新和显示
     */
    private void initRecyclerView() {
        listAdapter = new TestingInfoQueryListAdapter();
        testingInfoQueryListRv.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        testingInfoQueryListRv.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Navigation.findNavController(view).navigate(R.id.actionTestingQueryListToDetail);
            }
        });
    }

    /**
     * 设置SearchView下划线透明
     **/
    private void setUnderLinearTransparent(SearchView searchView) {
        try {
            Class<?> argClass = searchView.getClass();
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackgroundColor(Color.TRANSPARENT);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.test_info_query_back)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }
}
