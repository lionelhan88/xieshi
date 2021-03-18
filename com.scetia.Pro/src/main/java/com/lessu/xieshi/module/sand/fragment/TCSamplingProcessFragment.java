package com.lessu.xieshi.module.sand.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lessu.xieshi.R;
import com.lessu.xieshi.base.BaseVMFragment;
import com.lessu.xieshi.module.meet.activity.ScalePictureActivity;
import com.lessu.xieshi.module.sand.adapter.TakePhotosAdapter;
import com.lessu.xieshi.module.sand.viewmodel.TCSamplingProcessViewModel;
import com.scetia.Pro.common.photo.XXPhotoUtil;

import butterknife.BindView;

/**
 * created by ljs
 * on 2021/1/28
 */
public class TCSamplingProcessFragment extends BaseVMFragment<TCSamplingProcessViewModel> {
    @BindView(R.id.sand_manage_take_photos_rv)
    RecyclerView sandManageTakePhotosRv;
    private TakePhotosAdapter takePhotosAdapter;
    @Override
    protected Class<TCSamplingProcessViewModel> getViewModelClass() {
        return TCSamplingProcessViewModel.class;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_testing_commission_sampling_process;
    }

    @Override
    protected void initView() {
        setTitle("取样过程");
        takePhotosAdapter = new TakePhotosAdapter();
        sandManageTakePhotosRv.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
        sandManageTakePhotosRv.setAdapter(takePhotosAdapter);

        takePhotosAdapter.setOnSrcClickListener((closeImageView, photoPath) -> {
            if (closeImageView.getCloseImgVisible()) {
                //已经有照片内容了，点击时放大图片
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Pair<View, String> pair = new Pair<>(closeImageView, "enlargePicture");
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), pair);
                    ActivityNavigator.Extras extras = new ActivityNavigator.Extras.Builder().setActivityOptions(options).build();
                    Bundle bundle = new Bundle();
                    bundle.putString("detail_photo", photoPath);
                    Navigation.findNavController(closeImageView).navigate(R.id.action_sandTestingCommissionFragment_to_scalePictureActivity,
                            bundle, null, extras);
                    return;
                }
                Intent scaleIntent = new Intent(requireActivity(), ScalePictureActivity.class);
                scaleIntent.putExtra("detail_photo", photoPath);
                startActivity(scaleIntent);
                requireActivity().overridePendingTransition(R.anim.acitvity_zoom_open, 0);
            } else {
                openCamera();
            }
        });
    }
    /**
     * 打开相机,拍摄照片保存
     */
    private void openCamera() {
        XXPhotoUtil.with(requireActivity()).setCompress(true).setListener((photoPath, photoUri) -> {
            takePhotosAdapter.addData(0, photoPath);
            //图片保存成功后判断是否达到了最大数量，如果是，则不再添加照片
            int picCount = takePhotosAdapter.getData().size();
            if (picCount == 11) {
                takePhotosAdapter.remove(picCount - 1);
            }
        }).startCamera();
    }
}
