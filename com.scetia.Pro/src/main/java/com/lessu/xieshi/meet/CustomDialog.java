package com.lessu.xieshi.meet;

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
import android.widget.Button;
import android.widget.TextView;

import com.lessu.xieshi.R;
import com.lessu.xieshi.SignView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CustomDialog extends DialogFragment {

    @BindView(R.id.bt_confirm_sign)
    TextView btConfirmSign;
    @BindView(R.id.bt_cancel_sign)
    TextView btCancelSign;
    @BindView(R.id.bt_reset_sign)
    TextView btResetSign;
    @BindView(R.id.signView)
    SignView signView;
    @BindView(R.id.meeting_success_dialog_hy_code)
    TextView meetingSuccessDialogHyCode;
    @BindView(R.id.meeting_success_dialog_hy_dw)
    TextView meetingSuccessDialogHyDw;
    @BindView(R.id.meeting_success_dialog_user)
    TextView meetingSuccessDialogUser;

    private Unbinder bind;


    public interface CustomDialogInterface {
        void clickOkButton(String base64Str);
    }

    private CustomDialogInterface customDialogInterface;


    public void setCustomDialogInterface(CustomDialogInterface customDialogInterface) {
        this.customDialogInterface = customDialogInterface;
    }

    public static CustomDialog newInstance(String memberCode, String memberName, String userFullName) {
        Bundle args = new Bundle();
        args.putString("memberCode", memberCode);
        args.putString("memberName", memberName);
        args.putString("userFullName", userFullName);
        CustomDialog fragment = new CustomDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.meeting_dialog_sign_layout, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String memberCode = getArguments().getString("memberCode");
        String memberName = getArguments().getString("memberName");
        String userFullName = getArguments().getString("userFullName");
        meetingSuccessDialogHyCode.setText(memberCode);
        meetingSuccessDialogHyDw.setText(memberName);
        meetingSuccessDialogUser.setText(userFullName);
    }

    @OnClick({R.id.bt_confirm_sign, R.id.bt_cancel_sign, R.id.bt_reset_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_confirm_sign:
                if (customDialogInterface != null) {
                    if(!signView.isHandSign()){
                        //没有签名动作，就返回""
                        customDialogInterface.clickOkButton("");
                        return;
                    }
                    customDialogInterface.clickOkButton(signView.saveBase64Str());
                }
                break;
            case R.id.bt_cancel_sign:
                getDialog().dismiss();
                break;
            case R.id.bt_reset_sign:
                signView.clear();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 设置宽度为屏宽、位置在屏幕底部
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.white_smoke);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getRealMetrics(displayMetrics);
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = displayMetrics.heightPixels / 3 * 2;
        window.setAttributes(wlp);
        //点击空白区域禁止消失
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
}
