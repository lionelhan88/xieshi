package com.lessu.xieshi.module.mis.activities;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.module.mis.bean.NoticeBean;
import com.lessu.xieshi.module.mis.viewmodel.MisNoticesViewModel;

import java.util.List;

import butterknife.BindView;

public class MisNoticesActivity extends NavigationActivity {
    @BindView(R.id.mis_notice_rv)
    RecyclerView misNoticeRv;
    private MisNoticeListAdapter misNoticeListAdapter;
    private MisNoticesViewModel viewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mistongzhi;
    }

    @Override
    protected void observerData() {
        viewModel = new ViewModelProvider(this).get(MisNoticesViewModel.class);
        viewModel.getLoadState().observe(this, new Observer<LoadState>() {
            @Override
            public void onChanged(LoadState loadState) {
                switch (loadState) {
                    case LOADING:
                        LSAlert.showProgressHud(MisNoticesActivity.this, loadState.getMessage());
                        break;
                    case SUCCESS:
                        LSAlert.dismissProgressHud();
                        break;
                    case FAILURE:
                        LSAlert.dismissProgressHud();
                        LSAlert.showAlert(MisNoticesActivity.this, loadState.getMessage());
                        break;
                }
            }
        });

        viewModel.getNoticeBeanData().observe(this, new Observer<List<NoticeBean>>() {
            @Override
            public void onChanged(List<NoticeBean> noticeBeans) {
                misNoticeListAdapter.addData(noticeBeans);
            }
        });
    }

    @Override
    protected void initView() {
        this.setTitle("信息通知");
        misNoticeListAdapter = new MisNoticeListAdapter();
        misNoticeRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        misNoticeRv.setAdapter(misNoticeListAdapter);
        misNoticeListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImageView imageLeft = (ImageView) adapter.getViewByPosition(misNoticeRv, position, R.id.iv_itemtz);
                imageLeft.setImageResource(R.drawable.xiaoxii);
                NoticeBean item = (NoticeBean) adapter.getItem(position);
                String nr = item.getNR();
                Intent intent = new Intent(MisNoticesActivity.this, NoticeDetailActivity.class);
                intent.putExtra("tongzhi", nr);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        viewModel.getNotices();
    }


    static class MisNoticeListAdapter extends BaseQuickAdapter<NoticeBean, BaseViewHolder> {

        public MisNoticeListAdapter() {
            super(R.layout.item_mistongzhi);
        }

        @Override
        protected void convert(BaseViewHolder helper, NoticeBean item) {
            helper.setText(R.id.tv_itemtz_bt, item.getBT());
            helper.setText(R.id.tv_itemtz_sj, item.getSJ());
            helper.setImageResource(R.id.iv_itemtz, R.drawable.xiaoxi);
        }
    }
}
