package com.lessu.xieshi.module.sand.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.lessu.BaseFragment;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.adapter.TestingCompanyQueryResultListAdapter;
import com.lessu.xieshi.module.sand.bean.TestingManageBean;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/11/9
 */
public class TestingCompanyQueryResultFragment extends BaseFragment {
    @BindView(R.id.testing_company_query_result_rv)
    RecyclerView testingCompanyQueryResultRv;
    @BindView(R.id.testing_company_query_result_bottom_save)
    TextView testingCompanyQueryResultBottomSave;
    @BindView(R.id.test_info_query_back)
    ImageView testInfoQueryBack;
    @BindView(R.id.testing_info_query_search)
    SearchView testingInfoQuerySearch;
    @BindView(R.id.testing_info_query_toolbar)
    Toolbar testingInfoQueryToolbar;
    private TestingCompanyQueryResultListAdapter listAdapter;
    private ArrayList<TestingManageBean> selectTestingManageBeans;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_testing_company_query_result;
    }

    @Override
    protected void initView() {
        navigationBar.setVisibility(View.GONE);
        testingInfoQuerySearch.setIconifiedByDefault(false);
        setUnderLinearTransparent(testingInfoQuerySearch);
        listAdapter = new TestingCompanyQueryResultListAdapter(true);
        testingCompanyQueryResultRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        testingCompanyQueryResultRv.setAdapter(listAdapter);
        listAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TestingManageBean bean = (TestingManageBean) adapter.getItem(position);
                ImageView selectImg = (ImageView) adapter.getViewByPosition(testingCompanyQueryResultRv,
                        position, R.id.testing_company_manage_item_select_img);
                if (!selectTestingManageBeans.contains(bean)) {
                    //状态置为“选中”
                    selectTestingManageBeans.add(bean);
                    bean.setSelect(true);
                    selectImg.setImageResource(R.drawable.icon_chosen);
                } else {
                    //状态置为“未选中”
                    selectTestingManageBeans.remove(bean);
                    bean.setSelect(false);
                    selectImg.setImageResource(R.drawable.icon_unchosen);
                }
                testingCompanyQueryResultBottomSave.setText("保存（" + selectTestingManageBeans.size() + "）");
            }
        });
    }

    @Override
    protected void initData() {
        selectTestingManageBeans = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            TestingManageBean bean = new TestingManageBean();
            bean.setTestingCompanyName("上海技术检测公司" + i);
            bean.setTestingCompanyArea("静安区" + i);
            bean.setTestingCompanyUser("张三" + i);
            bean.setTestingCompanyPhone("15512345" + i);
            bean.setTestingCompanyAddress("上海市中山南二路777弄" + i);
            listAdapter.addData(bean);
        }
    }

    @Override
    protected void initImmersionBar() {
        ImmersionBar.with(this).titleBar(testingInfoQueryToolbar)
                .navigationBarColor(R.color.light_gray)
                .navigationBarDarkIcon(true)
                .init();
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
        leftNavBarClick();
    }
}
