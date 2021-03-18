/*
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 *
 * Created by lisah0 on 2012-02-24
 */
package com.lessu.xieshi.base;


import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.construction.QrcodeDetailActivity;
import com.lessu.xieshi.view.CameraPreview;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;

/* Import ZBar Class files */
import androidx.core.content.ContextCompat;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;


public class QRCodeActivity extends NavigationActivity {
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    ImageScanner scanner;
    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.qrcode_activity;
    }

    @Override
    protected void initView() {
        this.setTitle("条形码查询");
        autoFocusHandler = new Handler();
        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        navigationBar.setBackgroundColor(ContextCompat.getColor(this,R.color.top_bar_background));
        FrameLayout cameraPreview = (FrameLayout) findViewById(R.id.cameraPreview);
        cameraPreview.setOnClickListener(v -> {
            if (barcodeScanned) {
                barcodeScanned = false;
                mCamera.setPreviewCallback(previewCb);
                mCamera.startPreview();
                previewing = true;
                mCamera.autoFocus(autoFocusCB);
            }
        });
    }

    @Override
    protected void onStart() {
        OpenCamera();
        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.removeAllViews();
        preview.addView(mPreview);
        super.onStart();
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public Camera OpenCamera() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        mCamera = c;
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    PreviewCallback previewCb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    try {
                        String stringData = sym.getData();
                        Intent intent = new Intent(QRCodeActivity.this, QrcodeDetailActivity.class);
                        String projectId = stringData;
                        Bundle bundle = new Bundle();
                        bundle.putString("CoreCode", projectId);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } catch (Exception e) {
                        // TODO: handle exception
                        LSAlert.showAlert(QRCodeActivity.this, e.getMessage());
                    }
                    barcodeScanned = true;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
}
