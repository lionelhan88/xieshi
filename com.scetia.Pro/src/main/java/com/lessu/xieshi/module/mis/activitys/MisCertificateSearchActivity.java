package com.lessu.xieshi.module.mis.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.lessu.xieshi.module.mis.bean.CertificateBean;
import com.lessu.xieshi.module.mis.viewmodel.MisCertificateSearchViewModel;
import com.scetia.Pro.common.exceptionhandle.ExceptionHandle;

/**
 * Mis中的证书查询界面
 */
public class MisCertificateSearchActivity extends NavigationActivity {
    private MisCertificateSearchViewModel viewModel;
    private EditText certificateNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_zssearch);
        this.setTitle("证书信息查询");
        initDataListener();
        initView();
    }

    private void initDataListener() {
        viewModel =new ViewModelProvider(this).get(MisCertificateSearchViewModel.class);
        viewModel.getCertificateBeanData().observe(this, new Observer<CertificateBean>() {
            @Override
            public void onChanged(CertificateBean bean) {
                Intent intent = new Intent(MisCertificateSearchActivity.this, CertificateDetailActivity.class);
                intent.putExtra("miszhengshu", bean);
                startActivity(intent);
            }
        });

        viewModel.getThrowable().observe(this, new Observer<ExceptionHandle.ResponseThrowable>() {
            @Override
            public void onChanged(ExceptionHandle.ResponseThrowable throwable) {
                ToastUtil.showShort(throwable.message);
            }
        });

        viewModel.getLoadState().observe(this, new Observer<LoadState>() {
            @Override
            public void onChanged(LoadState loadState) {
                if(loadState==LoadState.LOADING){
                    LSAlert.showProgressHud(MisCertificateSearchActivity.this,"正在查询...");
                }else{
                    LSAlert.dismissProgressHud();
                }
            }
        });
    }

    private void initView() {
        BarButtonItem handleButtonItem = new BarButtonItem(this, R.drawable.back);
        handleButtonItem.setOnClickListener(view -> finish());
        navigationBar.setLeftBarItem(handleButtonItem);
        certificateNo = findViewById(R.id.et_mis_Certificate_no);
        Button bt_miszs_tijiao = findViewById(R.id.bt_miszs_tijiao);
        bt_miszs_tijiao.setOnClickListener(view -> {
            //这里要改2
            String token = Content.getToken();
            String key = certificateNo.getText().toString().trim();
            viewModel.getCertificateSearch(token, key);
        });

    }
}
