package com.lessu.xieshi.module.sand.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;

import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.lessu.xieshi.R;
import com.scetia.Pro.baseapp.page.BasePageListAdapter;
import com.scetia.Pro.baseapp.basepage.PageListCommonViewHolder;
import com.lessu.xieshi.module.sand.bean.TestingCompanyBean;
import com.lessu.xieshi.module.sand.viewmodel.TestingCompanyQueryListViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * created by ljs
 * on 2020/11/6
 */
public class TestingCompanyQueryListAdapter extends BasePageListAdapter<TestingCompanyBean> {
    private TestingCompanyQueryListViewModel viewModel;
    private HashMap<String, String> addedTestingCompanies;
    private List<String> delTestingCompanyIds = new ArrayList<>();
    //需要添加的销售对象
    private ArrayList<TestingCompanyBean> selectTestingCompanyBeans = new ArrayList<>();
    //需要删除的销售对象
    private ArrayList<TestingCompanyBean> delTestingCompanyBeans = new ArrayList<>();
    private static DiffUtil.ItemCallback<TestingCompanyBean> diff = new DiffUtil.ItemCallback<TestingCompanyBean>() {
        @Override
        public boolean areItemsTheSame(@NonNull TestingCompanyBean oldItem, @NonNull TestingCompanyBean newItem) {
            return oldItem.getMemberCode().equals(newItem.getMemberCode());
        }

        @Override
        public boolean areContentsTheSame(@NonNull TestingCompanyBean oldItem, @NonNull TestingCompanyBean newItem) {
            return oldItem == newItem;
        }
    };

    public TestingCompanyQueryListAdapter(TestingCompanyQueryListViewModel viewModel) {
        super(diff, R.layout.testing_company_query_result_list_item_layout);
        this.viewModel = viewModel;
    }

    public void setAddedTestingCompanies(HashMap<String, String> addedTestingCompanies) {
        this.addedTestingCompanies = addedTestingCompanies;
    }

    public void setDelTestingCompanyIds(List<String> delTestingCompanyIds) {
        this.delTestingCompanyIds = delTestingCompanyIds;
    }

    public ArrayList<TestingCompanyBean> getSelectTestingCompanyBeans() {
        return selectTestingCompanyBeans;
    }

    public void setSelectTestingCompanyBeans(ArrayList<TestingCompanyBean> selectTestingCompanyBeans) {
        this.selectTestingCompanyBeans = selectTestingCompanyBeans;
    }

    public ArrayList<TestingCompanyBean> getDelTestingCompanyBeans() {
        return delTestingCompanyBeans;
    }

    public void setDelTestingCompanyBeans(ArrayList<TestingCompanyBean> delTestingCompanyBeans) {
        this.delTestingCompanyBeans = delTestingCompanyBeans;
    }

    public HashMap<String, String> getAddedTestingCompanies() {
        return addedTestingCompanies;
    }

    public List<String> getDelTestingCompanyIds() {
        return delTestingCompanyIds;
    }

    @Override
    public void footerItemClick() {
        viewModel.retry();
    }

    @Override
    public void bindWithCommonItemView(PageListCommonViewHolder vh, TestingCompanyBean testingCompanyBean) {
        EasySwipeMenuLayout layout = vh.getView(R.id.company_manage_swipe_menLayout);
        layout.setCanLeftSwipe(false);
        ConstraintLayout all = vh.getView(R.id.company_manage_item_content);
        ImageView ivSelect = vh.getView(R.id.testing_company_manage_item_select_img);
        ivSelect.setVisibility(View.VISIBLE);

        TextView membercode = vh.getView(R.id.testing_company_manage_item_company_code);
        TextView unitName = vh.getView(R.id.testing_company_manage_item_company_name);
        TextView counties = vh.getView(R.id.testing_company_manage_item_company_area);
        TextView contactPerson = vh.getView(R.id.testing_company_manage_item_company_user);
        TextView personPhone = vh.getView(R.id.testing_company_manage_item_company_phone);
        TextView address = vh.getView(R.id.testing_company_manage_item_company_address);
        membercode.setText("会员编号:"+testingCompanyBean.getMemberCode());
        unitName.setText(testingCompanyBean.getUnitName());
        counties.setText(testingCompanyBean.getCounties());
        contactPerson.setText(testingCompanyBean.getContactPerson());
        personPhone.setText(testingCompanyBean.getContactPersonPhoneNo());
        address.setText(testingCompanyBean.getUnitAddress());
        //点击选中还是取消选中
        all.setOnClickListener(v -> {
            testingCompanyBean.setSelect(!testingCompanyBean.isSelect());
            if (testingCompanyBean.isSelect()) {
                addSelect(testingCompanyBean);
            } else {
                removeSelect(testingCompanyBean);
            }
            notifyItemChanged(vh.getAdapterPosition());
        });
        //只有初始化的时候才把已经添加的选中，其他时候按照添加和删除的逻辑
        if (addedTestingCompanies.containsKey(testingCompanyBean.getMemberCode()) && !delTestingCompanyBeans.contains(testingCompanyBean)) {
            testingCompanyBean.setSelect(true);
        }
        int resourceId = testingCompanyBean.isSelect() ? R.drawable.icon_chosen : R.drawable.icon_unchosen;
        ivSelect.setImageResource(resourceId);
    }

    public void addSelect(TestingCompanyBean testingCompanyBean) {
        delTestingCompanyBeans.remove(testingCompanyBean);
        delTestingCompanyIds.remove(addedTestingCompanies.get(testingCompanyBean.getMemberCode()));
        if (!addedTestingCompanies.containsKey(testingCompanyBean.getMemberCode())) {
            selectTestingCompanyBeans.add(testingCompanyBean);
        }
    }

    public void removeSelect(TestingCompanyBean testingCompanyBean) {
        selectTestingCompanyBeans.remove(testingCompanyBean);
        if (addedTestingCompanies.containsKey(testingCompanyBean.getMemberCode())) {
            delTestingCompanyBeans.add(testingCompanyBean);
            delTestingCompanyIds.add(addedTestingCompanies.get(testingCompanyBean.getMemberCode()));
        }
    }

    public void clearSelectData() {
        selectTestingCompanyBeans.clear();
        delTestingCompanyBeans.clear();
        delTestingCompanyIds.clear();
    }
}
