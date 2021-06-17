package com.lessu.xieshi.module.web;

import android.os.Bundle;
import android.os.Environment;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.utils.ToastUtil;
import com.lessu.xieshi.module.mis.bean.SealManageBean;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.network.bean.ExceptionHandle;
import com.scetia.Pro.network.conversion.ResponseObserver;
import com.scetia.Pro.network.util.DownloadUtil;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

import butterknife.BindView;

/**
 * created by Lollipop
 * on 2021/3/1
 */
public class WordPreviewActivity extends NavigationActivity {
    @BindView(R.id.word_preview_frame)
    FrameLayout frameLayout;
    private TbsReaderView tbsReaderView;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_word_preview;
    }

    @Override
    protected void initView() {
        createTasView();
    }

    @Override
    protected void initData() {
        LSAlert.showProgressHud(this,"正在加载数据...");
        SealManageBean.YzFjInfoBean bean = getIntent().getParcelableExtra(Constants.SealManage.KEY_YZ_FJ_BEAN);
        String savePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();
        if(bean!=null){
            setTitle(bean.getFileName());
            DownloadUtil.download("http://www.scetia.com/DownLoad_YZ/"+bean.getFilePath(),bean.getFilePath(),savePath,
                    new ResponseObserver<File>() {

                        @Override
                        public void success(File file) {
                            LSAlert.dismissProgressHud();
                            String filePath = bean.getFilePath();
                            String fileType = filePath.substring(filePath.lastIndexOf(".")+1);
                            Bundle bundle = new Bundle();
                            bundle.putString("filePath", file.getPath());
                            bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
                            boolean result = tbsReaderView.preOpen(fileType, false);
                            if (result) {
                                tbsReaderView.openFile(bundle);
                            }
                        }

                        @Override
                        public void failure(ExceptionHandle.ResponseThrowable throwable) {
                            LSAlert.dismissProgressHud();
                            ToastUtil.showShort(throwable.message);
                        }
                    });
        }
    }
    private void createTasView() {
        tbsReaderView = new TbsReaderView(this, (integer, o, o1) -> {
        });
        frameLayout.addView(tbsReaderView, new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onDestroy() {
        tbsReaderView.onStop();
        frameLayout.removeView(tbsReaderView);
        super.onDestroy();
    }
}
