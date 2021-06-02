package com.lessu.xieshi.module.mis.activities;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.utils.ToastUtil;
import com.lessu.xieshi.module.mis.viewmodel.MisCertificateSearchViewModel;
import com.scetia.Pro.common.Util.Constants;

/**
 * Mis中的证书查询界面
 */
public class MisCertificateSearchActivity extends NavigationActivity {
    private MisCertificateSearchViewModel viewModel;
    private EditText certificateNo;
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_zssearch);
        this.setTitle("证书信息查询");
        observerData();
        initView();
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mis_zssearch;
    }

    @Override
    protected void observerData() {
        viewModel =new ViewModelProvider(this).get(MisCertificateSearchViewModel.class);
        viewModel.getLoadState().observe(this, loadState -> {
            switch (loadState){
                case LOADING:
                    LSAlert.showProgressHud(MisCertificateSearchActivity.this,"正在查询...");
                    break;
                case SUCCESS:
                    LSAlert.dismissProgressHud();
                    break;
                case FAILURE:
                    LSAlert.dismissProgressHud();
                    ToastUtil.showShort(loadState.getMessage());
                    break;
            }
        });
        viewModel.getCertificateBeanData().observe(this, bean -> {
            Intent intent = new Intent(MisCertificateSearchActivity.this, CertificateDetailActivity.class);
            intent.putExtra("miszhengshu", bean);
            startActivity(intent);
        });
    }

    @Override
    protected void initView() {
        this.setTitle("证书信息查询");
        BarButtonItem handleButtonItem = new BarButtonItem(this, R.drawable.back);
        handleButtonItem.setOnClickListener(view -> finish());
        navigationBar.setLeftBarItem(handleButtonItem);
        certificateNo = findViewById(R.id.et_mis_Certificate_no);
        Button bt_miszs_tijiao = findViewById(R.id.bt_miszs_tijiao);
        bt_miszs_tijiao.setOnClickListener(view -> {
            //这里要改2
            String token = Constants.User.GET_TOKEN();
            String key = certificateNo.getText().toString().trim();
            viewModel.getCertificateSearch(token, key);
        });

    }
}
