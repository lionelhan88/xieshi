package com.lessu.xieshi.module.mis.activities;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.base.BaseVMActivity;
import com.lessu.xieshi.utils.ToastUtil;
import com.lessu.xieshi.module.mis.viewmodel.MisCertificateSearchViewModel;
import com.scetia.Pro.common.Util.Constants;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Mis中的证书查询界面
 */
public class MisCertificateSearchActivity extends BaseVMActivity<MisCertificateSearchViewModel> {
    @BindView(R.id.et_mis_Certificate_no)
    EditText certificateNo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mis_zssearch;
    }

    @Override
    protected void observerData() {
        mViewModel.getCertificateBeanData().observe(this, bean -> {
            Intent intent = new Intent(MisCertificateSearchActivity.this, CertificateDetailActivity.class);
            intent.putExtra(Constants.Certificate.KEY_CERTIFICATE_BEAN, bean);
            startActivity(intent);
        });
    }

    @Override
    protected void initView() {
        this.setTitle("证书信息查询");
    }

    @OnClick(R.id.bt_miszs_tijiao)
    public void doSearch() {
        String key = certificateNo.getText().toString().trim();
        mViewModel.getCertificateSearch(Constants.User.GET_TOKEN(), key);
    }
}
