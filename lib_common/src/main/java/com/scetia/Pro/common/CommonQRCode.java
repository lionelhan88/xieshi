package com.scetia.Pro.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.gyf.immersionbar.ImmersionBar;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * created by ljs
 * on 2021/1/15
 * 二维码或条形码扫描界面
 */
public class CommonQRCode extends DialogFragment implements QRCodeView.Delegate {
    private ZXingView scanView;
    private  FragmentActivity mFragment;
    private boolean isBarCode;
    public CommonQRCode(FragmentActivity mFragment) {
        this.mFragment = mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewStub stub;
        View rootView = inflater.inflate(R.layout.fragment_common_qrcode_layout, container, false);
        ConstraintLayout titleLayout = rootView.findViewById(R.id.fragment_common_qrcode_title);
        ImageView backView = rootView.findViewById(R.id.fragment_common_qrcode_back);
        backView.setOnClickListener(v -> dismiss());
        //监听物理返回按键
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode== KeyEvent.KEYCODE_BACK){
                dismiss();
                return true;
            }
            return false;
        });
        if (isBarCode) {
            stub = rootView.findViewById(R.id.view_stub_bar);
        } else {
            stub = rootView.findViewById(R.id.view_stub_qr);
        }
        stub.inflate();
        scanView = rootView.findViewById(R.id.common_qrcode_view);
        scanView.setDelegate(this);
        ImmersionBar.with(this).titleBar(titleLayout).init();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_NoTitleBar_Fullscreen);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requireActivity().checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 0x2001);
        } else {
            //打开相机
            scanView.startCamera();
            //开始扫描，并显示扫描框
            scanView.startSpotAndShowRect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.getDecorView().setPadding(0, 0, 0, 0);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        //扫描二维码或者条形码成功，返回数据
        startVibrate();
        Toast.makeText(requireContext(), result, Toast.LENGTH_LONG).show();
        scanView.stopSpot();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    /**
     * 开启震动
     */
    private void startVibrate() {
        //开始震动
        Vibrator vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x2001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限申请成功
                //打开相机
                scanView.startCamera();
                //开始扫描，并显示扫描框
                scanView.startSpotAndShowRect();
            }
        }
    }


    public static CommonQRCode with(FragmentActivity fragmentActivity) {
        return new CommonQRCode(fragmentActivity);
    }


    public CommonQRCode setIsBarCode(boolean isBarCode) {
        this.isBarCode = isBarCode;
        return this;
    }

    public void show() {
        this.show(mFragment.getSupportFragmentManager(), "commonQRCode");
    }

    @Override
    public void onDestroyView() {
        if(scanView!=null){
            scanView.stopSpot();
            scanView.stopCamera();
            scanView.onDestroy();
        }
        super.onDestroyView();
    }
}
