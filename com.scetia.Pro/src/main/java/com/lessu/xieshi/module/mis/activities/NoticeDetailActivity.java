package com.lessu.xieshi.module.mis.activities;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.scetia.Pro.common.Util.Constants;

import butterknife.BindView;

public class NoticeDetailActivity extends NavigationActivity {
    @BindView(R.id.tv_notice_content)
    TextView tvNoticeContent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice_detail;
    }

    @Override
    protected void initView() {
        this.setTitle("通知内容");
    }

    @Override
    protected void initData() {
        tvNoticeContent.setText( getIntent().getStringExtra(Constants.Notice.KEY_NOTICE_CONTENT));
    }
}
