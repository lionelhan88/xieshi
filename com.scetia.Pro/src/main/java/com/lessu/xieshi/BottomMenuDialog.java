package com.lessu.xieshi;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BottomMenuDialog extends DialogFragment {

    @BindView(R.id.user_join)
    TextView userJoin;
    @BindView(R.id.other_join)
    TextView otherJoin;
    @BindView(R.id.dialog_meeting_confirm_cancel)
    TextView dialogMeetingConfirmCancel;
    private Unbinder bind;




    public interface CustomDialogInterface {
        void selfJoin();

        void otherJoin();
    }

    private CustomDialogInterface customDialogInterface;


    public void setCustomDialogInterface(CustomDialogInterface customDialogInterface) {
        this.customDialogInterface = customDialogInterface;
    }

    public static BottomMenuDialog newInstance() {
        return new BottomMenuDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_meeting_confirm_layout, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @OnClick({R.id.user_join, R.id.other_join, R.id.dialog_meeting_confirm_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_join:
                dismiss();
                customDialogInterface.selfJoin();
                break;
            case R.id.other_join:
                dismiss();
                customDialogInterface.otherJoin();
                break;
            case R.id.dialog_meeting_confirm_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 设置宽度为屏宽、位置在屏幕底部
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.getDecorView().setPadding(20, 0, 20, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getRealMetrics(displayMetrics);
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
        //点击空白区域禁止消失
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
}
