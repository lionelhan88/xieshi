package com.lessu.xieshi.module.sand.fragment;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.lessu.BaseFragment;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.DateUtil;
import com.lessu.xieshi.Utils.ImageUtil;
import com.lessu.xieshi.Utils.LogUtil;
import com.lessu.xieshi.lifcycle.BaiduMapLifecycle;
import com.lessu.xieshi.module.meet.activity.ScalePictureActivity;
import com.lessu.xieshi.module.sand.adapter.TakePhotosAdapter;

import java.io.File;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * created by ljs
 * on 2020/10/26
 */
public class SandTestingCommissionFragment extends BaseFragment {
    @BindView(R.id.testing_commission_company_name)
    TextView testingCommissionCompanyName;
    @BindView(R.id.testing_commission_testing_company)
    TextView testingCommissionTestingCompany;
    @BindView(R.id.rb_sand_manage_supplier)
    RadioButton rbSandManageSupplier;
    @BindView(R.id.rb_sand_manage_testing_company)
    RadioButton rbSandManageTestingCompany;
    @BindView(R.id.sand_manage_sampling_time)
    TextView sandManageSamplingTime;
    @BindView(R.id.sand_manage_sampling_place)
    TextView sandManageSamplingPlace;
    @BindView(R.id.sand_manage_testing_parameters)
    TextView sandManageTestingParameters;
    @BindView(R.id.sand_manage_take_photos_rv)
    RecyclerView sandManageTakePhotosRv;
    @BindView(R.id.sand_manage_sampling_company_rg)
    RadioGroup sandManageSamplingCompanyRg;
    @BindView(R.id.sand_manage_sampling_order_time)
    TextView sandManageSamplingOrderTime;
    @BindView(R.id.sand_manage_sampling_user)
    TextView sandManageSamplingUser;
    @BindView(R.id.sand_manage_sampling_user_list)
    TextView sandManageSamplingUserList;
    @BindView(R.id.sand_manage_identification)
    EditText sandManageIdentification;
    @BindView(R.id.sand_manage_sampling_number)
    TextView sandManageSamplingNumber;
    @BindView(R.id.sand_manage_commission_number)
    TextView sandManageCommissionNumber;
    @BindView(R.id.sand_manage_commission_user)
    TextView sandManageCommissionUser;
    @BindView(R.id.sand_manage_commission_date)
    TextView sandManageCommissionDate;
    @BindView(R.id.sand_manage_sampling_order_time_ll)
    LinearLayout sandManageSamplingOrderTimeLl;
    @BindView(R.id.sand_manage_sampling_process_ll)
    LinearLayout sandManageSamplingProcessLl;
    private TakePhotosAdapter takePhotosAdapter;
    private Uri imageUri;
    private String imagePath;
    private BDLocationListener locationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double x = bdLocation.getLatitude();
            double y = bdLocation.getLongitude();
            LatLng myLocation = new LatLng(x, y);
            LogUtil.showLogD(bdLocation.getAddress().address);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sand_testing_commission;

    }

    @Override
    protected void initView() {
        setTitle("检测委托");
        initRecyclerView();
        sandManageSamplingTime.setClickable(false);
        takePhotosAdapter.setOnSrcClickListener((closeImageView, photoPath) -> {
            if (closeImageView.getCloseImgVisible()) {
                //已经有照片内容了，点击时放大图片
                Intent scaleIntent = new Intent(getActivity(), ScalePictureActivity.class);
                scaleIntent.putExtra("detail_photo", photoPath);
                startActivity(scaleIntent);
                requireActivity().overridePendingTransition(R.anim.acitvity_zoom_open, 0);
            } else {
                openCamera();
            }
        });
        sandManageSamplingCompanyRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_sand_manage_supplier) {
                    //选择了供应商,需要有取样过程
                    sandManageSamplingOrderTimeLl.setVisibility(View.GONE);
                    sandManageSamplingProcessLl.setVisibility(View.VISIBLE);
                    // 不能修改取样日期
                    sandManageSamplingTime.setClickable(false);
                } else {
                    //选择了检测单位,可以预约检测日期
                    sandManageSamplingOrderTimeLl.setVisibility(View.VISIBLE);
                    sandManageSamplingProcessLl.setVisibility(View.GONE);
                    sandManageSamplingTime.setClickable(true);
                }
            }
        });
    }

    @Override
    protected void initData() {
        //预约取样时间
        sandManageSamplingOrderTime.setText(DateUtil.getDate(new Date()));
        //取样日期
        sandManageSamplingTime.setText(DateUtil.getDate(new Date()));
        sandManageCommissionDate.setText(DateUtil.getDate(new Date()));
        //开启位置监听
        BaiduMapLifecycle baiduMapLifecycle = new BaiduMapLifecycle(requireActivity());
        baiduMapLifecycle.setBdLocationListener(locationListener);
        getLifecycle().addObserver(baiduMapLifecycle);
    }

    private void initRecyclerView() {
        takePhotosAdapter = new TakePhotosAdapter();
        sandManageTakePhotosRv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        sandManageTakePhotosRv.setAdapter(takePhotosAdapter);
    }

    /**
     * 打开相机,拍摄照片保存
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String savePath = Environment.getExternalStorageDirectory().getPath() + File.separator + Environment.DIRECTORY_PICTURES
                + File.separator + "XIESHI";
        File dirFile = new File(savePath);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File saveFile = new File(savePath, System.currentTimeMillis() + ".jpg");
        imagePath = saveFile.getPath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(requireActivity(),
                    requireActivity().getPackageName() + ".fileProvider", saveFile);
        } else {
            imageUri = Uri.fromFile(saveFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                String s = ImageUtil.scaleAndCompress(getActivity(), imagePath, imagePath, 1024);
                takePhotosAdapter.addData(0, s);
                //刷新媒体库
                MediaScannerConnection.scanFile(getActivity(), new String[]{imagePath}, null, null);
                //图片保存成功后判断是否达到了最大数量，如果是，则不再添加照片
                int picCount = takePhotosAdapter.getData().size();
                if(picCount==11){
                    takePhotosAdapter.remove(picCount-1);
                }
            }
        }
    }

    @OnClick({R.id.testing_commission_testing_company, R.id.sand_manage_sampling_order_time, R.id.sand_manage_sampling_place, R.id.sand_manage_testing_parameters, R.id.sand_manage_sampling_time, R.id.sand_manage_commission_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.testing_commission_testing_company:
                break;
            case R.id.sand_manage_sampling_order_time:
                DateUtil.datePicker(getActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        sandManageSamplingOrderTime.setText(DateUtil.getDate(date));
                    }
                });
                break;
            case R.id.sand_manage_sampling_place:
                //选择取样地点
                String[] places = getResources().getStringArray(R.array.sampling_place_arr);
                LSAlert.showDialogSingleChoice(requireActivity(), "取样地点", R.drawable.icon_choice_dialog,
                        places, "取消", new LSAlert.SelectItemCallback() {
                            @Override
                            public void selectItem(int position) {
                                sandManageSamplingPlace.setText(places[position]);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                break;
            case R.id.sand_manage_testing_parameters:
                break;
            case R.id.sand_manage_sampling_time:
                DateUtil.datePicker(getActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        sandManageSamplingTime.setText(DateUtil.getDate(date));
                    }
                });
                break;
            case R.id.sand_manage_commission_date:
                DateUtil.datePicker(getActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        sandManageCommissionDate.setText(DateUtil.getDate(date));
                    }
                });
                break;
        }
    }

}
