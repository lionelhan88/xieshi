package com.lessu.xieshi.module.sand.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.lessu.BaseFragment;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.sand.adapter.CompanyQueryResultListAdapter;
import com.lessu.xieshi.module.sand.bean.SandSalesTargetBean;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * created by ljs
 * on 2020/11/9
 *
 * 销售对象查询结果页面
 */
public class SupplierQueryResultFragment extends BaseFragment {
    @BindView(R.id.sand_company_query_result_rv)
    RecyclerView sandCompanyQueryResultRv;
    @BindView(R.id.company_query_result_bottom_save)
    TextView companyQueryResultBottomSave;
    @BindView(R.id.test_info_query_back)
    ImageView testInfoQueryBack;
    @BindView(R.id.testing_info_query_search)
    SearchView testingInfoQuerySearch;
    @BindView(R.id.testing_info_query_toolbar)
    Toolbar testingInfoQueryToolbar;
    private CompanyQueryResultListAdapter listAdapter;
    private ArrayList<SandSalesTargetBean> selectSalesTargetBeans;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_company_query_result;
    }

    @Override
    protected void initView() {
        navigationBar.setVisibility(View.GONE);
        testingInfoQuerySearch.setIconifiedByDefault(false);
        setUnderLinearTransparent(testingInfoQuerySearch);
        listAdapter = new CompanyQueryResultListAdapter();
        sandCompanyQueryResultRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        sandCompanyQueryResultRv.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SandSalesTargetBean bean = (SandSalesTargetBean) adapter.getItem(position);
                ImageView selectImg = (ImageView) adapter.getViewByPosition(sandCompanyQueryResultRv,
                        position, R.id.company_query_result_list_item_select);
                if (!selectSalesTargetBeans.contains(bean)) {
                    //状态置为“选中”
                    selectSalesTargetBeans.add(bean);
                    bean.setSelect(true);
                    selectImg.setImageResource(R.drawable.icon_chosen);
                } else {
                    //状态置为“未选中”
                    selectSalesTargetBeans.remove(bean);
                    bean.setSelect(false);
                    selectImg.setImageResource(R.drawable.icon_unchosen);
                }
                companyQueryResultBottomSave.setText("保存（" + selectSalesTargetBeans.size() + "）");
            }
        });

    }

    @Override
    protected void initData() {
        selectSalesTargetBeans = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            SandSalesTargetBean bean = new SandSalesTargetBean();
            bean.setNum(i);
            if (i % 2 == 0) {
                bean.setGetSalesTargetNature("私营企业" + i);
            } else {
                bean.setGetSalesTargetNature("联营企业" + i);
            }
            bean.setSalesTargetName("砂石供应商" + i);
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

    @OnClick({R.id.test_info_query_back, R.id.sand_company_query_result_rv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.test_info_query_back:
                leftNavBarClick();
                break;
            case R.id.sand_company_query_result_rv:
                break;
        }
    }

}
