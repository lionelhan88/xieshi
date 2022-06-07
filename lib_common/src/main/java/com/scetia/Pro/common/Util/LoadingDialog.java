package com.scetia.Pro.common.Util;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.scetia.Pro.common.LoadingView;
import com.scetia.Pro.common.R;

public class LoadingDialog extends DialogFragment {
    private  static LoadingDialog loadingDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.loading_dialog_layout,container,false);
        TextView loadingMessage = dialogView.findViewById(R.id.loading_dialog_message);
        LoadingView loadingImg = dialogView.findViewById(R.id.loading_dialog_img);
        loadingImg.setColor("#3598DC");
        loadingMessage.setText(getArguments().getString("loadingMessage"));
        return dialogView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getRealMetrics(displayMetrics);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = displayMetrics.widthPixels/5*3;
        attributes.height = displayMetrics.heightPixels/5;
        window.setAttributes(attributes);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static void showLoading(FragmentActivity context){
        showLoading(context,"正在加载...");
    }

    public static void dismissDialog(){
        if(loadingDialog!=null){
            loadingDialog.dismiss();
            loadingDialog = null;
        }

    }
    public static void showLoading(FragmentActivity context, String message){
        loadingDialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putString("loadingMessage",message);
        loadingDialog.setArguments(bundle);
        loadingDialog.show(context.getSupportFragmentManager(),"loadingDialog");
    }
}
