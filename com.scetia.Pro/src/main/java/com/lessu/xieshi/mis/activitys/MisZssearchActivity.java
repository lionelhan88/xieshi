package com.lessu.xieshi.mis.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lessu.navigation.BarButtonItem;
import com.lessu.navigation.NavigationActivity;
import com.lessu.xieshi.R;
import com.lessu.xieshi.mis.bean.Miszhengshu;
import com.lessu.xieshi.mis.contracts.IMisZssearchContract;
import com.lessu.xieshi.mis.presenters.MisZsPresenter;

public class MisZssearchActivity extends NavigationActivity implements IMisZssearchContract.View {

    private BarButtonItem handleButtonItem;
    private IMisZssearchContract.Presenter presenter;
    private EditText et_miszs;
    private Button bt_miszs_tijiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_zssearch);
        navigationBar.setBackgroundColor(0xFF3598DC);
        this.setTitle("证书信息查询");
        handleButtonItem = new BarButtonItem(this, R.drawable.back);
        handleButtonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navigationBar.setLeftBarItem(handleButtonItem);
        initView();
        initData();
    }

    private void initView() {
        et_miszs = (EditText) findViewById(R.id.et_miszs);
        bt_miszs_tijiao = (Button) findViewById(R.id.bt_miszs_tijiao);
        bt_miszs_tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里要改2
                String token = Content.gettoken();
                String key = et_miszs.getText().toString().trim();
                presenter.Zssearch(token, key);
            }
        });
    }

    private void initData() {
        presenter = new MisZsPresenter(this, this);
    }


    @Override
    public void ZssearchCall(boolean isSuccess, Miszhengshu miszhengshu) {
        if (isSuccess && miszhengshu.getData().getCertificateNumber() != null) {
            Intent intent = new Intent(this, ZsdetailActivity.class);
            intent.putExtra("miszhengshu", miszhengshu);
            startActivity(intent);
        } else {
            Toast.makeText(MisZssearchActivity.this, "请输入正确的证书编号", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setPresenter(IMisZssearchContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
