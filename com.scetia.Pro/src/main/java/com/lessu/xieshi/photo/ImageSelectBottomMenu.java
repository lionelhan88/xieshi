package com.lessu.xieshi.photo;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.scetia.Pro.common.Util.DensityUtil;
import com.lessu.xieshi.R;
import com.scetia.Pro.common.Util.Common;
import com.scetia.Pro.common.photo.ImageUtil;
import com.scetia.Pro.common.photo.UriUtils;
import com.scetia.Pro.common.Util.SPUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class ImageSelectBottomMenu extends DialogFragment {
    private String photoName;
    private String photoSavePath;
    private String photoPath;
    private Uri photoUri;
    private boolean isCompress = false;
    public static final String PHOTO_NAME_KEY = "photo_name";
    public static final String PHOTO_SAVE_PATH_KEY = "photo_save_path_key";
    public static final int REQUEST_PHOTO_PICTURE = 1000;
    public static final int REQUEST_TAKE_PHOTO=10001;
    @BindView(R.id.image_select_menu_take_photo)
    TextView imageSelectMenuTakePhoto;
    @BindView(R.id.image_select_menu_photo_pictures)
    TextView imageSelectMenuPhotoPictures;
    @BindView(R.id.dialog_meeting_confirm_cancel)
    TextView dialogMeetingConfirmCancel;
    private Unbinder bind;

    //默认时开启 图片和相机菜单选择
    private XXPhotoUtil.TakePhotoOperation operation = XXPhotoUtil.TakePhotoOperation.ALL;


    public interface ImageSelectListener {
        void takePhoto(String photoPath,Uri photoUri);
    }

    private ImageSelectListener imageSelectListener;

    public void setCompress(boolean compress) {
        isCompress = compress;
    }

    public void setImageSelectListener(ImageSelectListener imageSelectListener) {
        this.imageSelectListener = imageSelectListener;
    }
    private ImageSelectBottomMenu(XXPhotoUtil.TakePhotoOperation operation){
        this.operation = operation;
    }
    public static ImageSelectBottomMenu newInstance(String photoName, String photoSavePath, XXPhotoUtil.TakePhotoOperation operation) {
        ImageSelectBottomMenu menu = new ImageSelectBottomMenu(operation);
        Bundle bundle = new Bundle();
        bundle.putString(PHOTO_NAME_KEY,photoName);
        bundle.putString(PHOTO_SAVE_PATH_KEY,photoSavePath);
        menu.setArguments(bundle);
        return menu;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        photoName = arguments.getString(PHOTO_NAME_KEY);
        photoSavePath = arguments.getString(PHOTO_SAVE_PATH_KEY);
        switch (operation){
            case ONLY_CAMERA:
                openCamera();
                break;
            case ONLY_PICTURES:
                openPictures();
                break;
        }
        if(operation!= XXPhotoUtil.TakePhotoOperation.ALL){
            return super.onCreateView(inflater,container,savedInstanceState);
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.image_select_bottom_menu_layout, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }
    @OnClick({R.id.image_select_menu_take_photo, R.id.image_select_menu_photo_pictures, R.id.dialog_meeting_confirm_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_select_menu_take_photo:
                openCamera();
                break;
            case R.id.image_select_menu_photo_pictures:
                openPictures();
                break;
            case R.id.dialog_meeting_confirm_cancel:
                dismiss();
                break;
        }
    }

    /**
     * 打开相机拍照
     */
    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoSaveFile = new File(photoSavePath);
        //TODO:创建文件目录
        if(!photoSaveFile.exists()){
            photoSaveFile.mkdirs();
        }
        File saveFile = new File(photoSavePath,photoName+".jpg");
        //如果有重名的文件，则删除之前的文件
        if(saveFile.exists()) saveFile.delete();
        //保存文件的目录
        photoPath = saveFile.getPath();
        /*
         *android10以后的插入图片的方式有所不同，这里需要判断
         */
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            photoUri = createUriQ(photoName);
        }else {
            photoUri = createUri(requireActivity(), saveFile, intent);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
        startActivityForResult(intent,REQUEST_TAKE_PHOTO);
    }

    /**
     * 打开相册选择招牌你
     */
    private void openPictures(){
        Intent imgIntent = new Intent(Intent.ACTION_PICK,null);
        imgIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        imgIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imgIntent,REQUEST_PHOTO_PICTURE);
    }
    /**
     * android10以后用这种方法
     * @return
     */
    public Uri createUriQ(String photonName){
        ContentValues contentValues = new ContentValues();
        //文件名称
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,photonName);
        //文件类型
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+"/XIESHI");
        return requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }
    /**
     * 根据不同的版本返回不同的uri地址
     * @return
     */
    public Uri createUri(Context context, File file, Intent intent){
        Uri uri= null;
        //大于7.0时获取uri的方式
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else{
            uri = Uri.fromFile(file);
        }
        return uri;
    }
    @Override
    public void onStart() {
        super.onStart();
        // 设置宽度为屏宽、位置在屏幕底部
        Dialog dialog = getDialog();
        if(dialog!=null) {
            Window window = dialog.getWindow();
            if(window!=null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
                window.setLayout((int) DensityUtil.screenWidth(requireContext()), ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.BOTTOM);
            }
            //点击空白区域禁止消失
            dialog.setCanceledOnTouchOutside(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_TAKE_PHOTO){
                //拍照的返回结果
                if(isCompress) {
                    //如果需要压缩图片
                    ImageUtil.scaleAndCompress(photoPath, photoPath);
                }
                MediaScannerConnection.scanFile(requireActivity(),new String[]{photoSavePath},null,null);
                imageSelectListener.takePhoto(photoPath,photoUri);
            }else{
                Uri uri = data.getData();
                String dataColumn = UriUtils.getPath(requireActivity(), uri);
                if (!dataColumn.contains("file://")) {
                    dataColumn = "file://" + dataColumn;
                }
                SPUtil.setSPConfig(Common.PICNAME, dataColumn);
                //选相册的返回结果
                imageSelectListener.takePhoto(dataColumn,uri);
            }
        }
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(bind!=null) {
            bind.unbind();
        }
    }
}
