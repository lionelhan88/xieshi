package com.lessu.xieshi.module.mis.activities;

import android.content.Intent;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.BaseVMActivity;
import com.lessu.xieshi.module.mis.bean.NoticeBean;
import com.lessu.xieshi.module.mis.viewmodel.MisNoticesViewModel;
import com.scetia.Pro.common.Util.Constants;

import butterknife.BindView;

public class MisNoticesActivity extends BaseVMActivity<MisNoticesViewModel> {
    @BindView(R.id.mis_notice_rv)
    RecyclerView misNoticeRv;
    private MisNoticeListAdapter misNoticeListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mistongzhi;
    }

    @Override
    protected void observerData() {
        mViewModel.getNoticeBeanData().observe(this, noticeBeans -> misNoticeListAdapter.addData(noticeBeans));
    }

    @Override
    protected void initView() {
        this.setTitle("信息通知");
        misNoticeListAdapter = new MisNoticeListAdapter();
        misNoticeRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        misNoticeRv.setAdapter(misNoticeListAdapter);
        misNoticeListAdapter.setOnItemClickListener((adapter, view, position) -> {
            ImageView imageLeft = (ImageView) adapter.getViewByPosition(misNoticeRv, position, R.id.iv_itemtz);
            imageLeft.setImageResource(R.drawable.xiaoxii);
            NoticeBean item = (NoticeBean) adapter.getItem(position);
            String nr = item.getNR();
            Intent intent = new Intent(MisNoticesActivity.this, NoticeDetailActivity.class);
            intent.putExtra(Constants.Notice.KEY_NOTICE_CONTENT, nr);
            startActivity(intent);
        });
    }

    @Override
    protected void initData() {
        mViewModel.getNotices();
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
