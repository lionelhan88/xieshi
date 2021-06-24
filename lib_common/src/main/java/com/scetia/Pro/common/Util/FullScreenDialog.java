package com.scetia.Pro.common.Util;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.scetia.Pro.common.R;

/**
 * created by ljs
 * on 2020/12/29
 */
public class FullScreenDialog extends BottomSheetDialogFragment {

    private FragmentActivity activity;
    private OnBindView onBindView;
    private int layoutId;
    private TextView btnCancel;
    private TextView btnOk;
    private TextView titleView;
    private String titleText;
    private String okBtnText;
    private String cancelBtnText;
    private  BottomSheetBehavior<FrameLayout> behavior;
    private OnOkButtonClickListener okButtonClickListener;
    private OnCancelButtonClickListener cancelButtonClickListener;
    public interface OnBindView {
        void bindView(View rootView);
    }

    public interface OnOkButtonClickListener{
        boolean clickListener(View view);
    }

    public interface OnCancelButtonClickListener{
        boolean clickListener(View view);
    }

    private static FullScreenDialog newInstance() {
        Bundle args = new Bundle();
        FullScreenDialog fragment = new FullScreenDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public FullScreenDialog setOkButtonClickListener(OnOkButtonClickListener okButtonClickListener) {
        this.okButtonClickListener = okButtonClickListener;
        return this;
    }

    public FullScreenDialog setCancelButtonClickListener(OnCancelButtonClickListener cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        return  this;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        //从底部弹出
        window.setGravity(Gravity.BOTTOM);
        final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet =bottomSheetDialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if(bottomSheet!=null){
            //弹窗背景透明，不然不能显示圆角背景
            bottomSheet.setBackgroundResource(android.R.color.transparent);
            behavior = BottomSheetBehavior.from(bottomSheet);
            CoordinatorLayout.LayoutParams  layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            layoutParams.height = (int) (DensityUtil.screenHeight(requireActivity()) * 0.65);
            bottomSheet.setLayoutParams(layoutParams);
            //最高展开的高度,当前默认展开就时最大高度，不允许滑动更多
            behavior.setPeekHeight((int) (DensityUtil.screenHeight(requireActivity()) * 0.65));
            //默认展开
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.base_full_screen_dialog_layout,container,false);
        btnCancel = rootView.findViewById(R.id.full_screen_dialog_cancel);
        btnOk = rootView.findViewById(R.id.full_screen_dialog_confirm);
        titleView  =rootView.findViewById(R.id.full_screen_dialog_title);
        titleView.setText(titleText==null?"":titleText);
        btnOk.setText(okBtnText==null?"确定":okBtnText);
        btnCancel.setText(cancelBtnText==null?"取消":cancelBtnText);
        btnCancel.setOnClickListener(v -> {
            if(cancelButtonClickListener==null){
                dismiss();
                return;
            }
            boolean b = cancelButtonClickListener.clickListener(v);
            if(!b){
                //默认为false不拦截事件，可以隐藏弹窗
                dismiss();
            }

        });
        btnOk.setOnClickListener(v -> {
            if(okButtonClickListener==null){
                dismiss();
                return;
            }
            boolean b = okButtonClickListener.clickListener(v);
            if(!b) {
                //默认为false不拦截事件，可以隐藏弹窗
                dismiss();
            }
        });
        FrameLayout frameLayout = rootView.findViewById(R.id.full_screen_dialog_content);
        View customerView = inflater.inflate(layoutId,null,false);
        frameLayout.addView(customerView);
        onBindView.bindView(rootView);
        return rootView;
    }

    public void shows() {
        this.show(activity.getSupportFragmentManager(), this.getClass().getName());
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public void setOnBindView(OnBindView onBindView) {
        this.onBindView = onBindView;
    }

    /**
     * 设置标题
     * @param title
     */
    public FullScreenDialog setTitle(String title){
        titleText = title;
        return this;
    }

    public FullScreenDialog setOKButtonText(String okButtonText){
        okBtnText = okButtonText;
        return this;
    }
    public FullScreenDialog setCancelButtonText(String cancelButtonText){
        cancelBtnText = cancelButtonText;
        return this;
    }

    public static class Builder {
        private FragmentActivity activity;
        private int  layoutId;
        private String titleText;
        private String okBtnText;
        private String cancelBtnText;
        public Builder(FragmentActivity activity, int layoutId) {
            this.activity = activity;
            this.layoutId = layoutId;
        }

        public Builder setTitle(String title) {
            titleText = title;
            return this;
        }

        public Builder setOkBtnText(String okText) {
            okBtnText = okText;
            return this;
        }

        public Builder setCancelBtnText(String cancelText) {
            cancelBtnText = cancelText;
            return this;
        }

        public FullScreenDialog build() {
            FullScreenDialog fullScreenDialog = newInstance();
            fullScreenDialog.setActivity(activity);
            fullScreenDialog.setLayoutId(layoutId);
            fullScreenDialog.setTitle(titleText);
            fullScreenDialog.setOKButtonText(okBtnText);
            fullScreenDialog.setCancelButtonText(cancelBtnText);
            return fullScreenDialog;
        }
    }
}


