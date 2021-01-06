package com.lessu.xieshi.module.sand.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lessu.BaseFragment;
import com.lessu.EventBusUtil;
import com.lessu.GlobalEvent;
import com.lessu.data.LoadState;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.Common;
import com.lessu.xieshi.Utils.DateUtil;
import com.lessu.xieshi.Utils.ImageUtil;
import com.lessu.xieshi.Utils.Shref;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.base.BaseVMFragment;
import com.lessu.xieshi.module.meet.activity.ScalePictureActivity;
import com.lessu.xieshi.module.sand.adapter.TakePhotosAdapter;
import com.lessu.xieshi.module.sand.bean.AddedTestingCompanyBean;
import com.lessu.xieshi.module.sand.bean.FlowDeclarationBean;
import com.lessu.xieshi.module.sand.bean.SandSamplerBean;
import com.lessu.xieshi.module.sand.viewmodel.SandTestingCommissionDetailViewModel;
import com.lessu.xieshi.module.sand.viewmodel.TestingCommissionModelFactory;
import com.lessu.xieshi.photo.XXPhotoUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.lessu.xieshi.module.sand.viewmodel.SandTestingCommissionDetailViewModel.SUPPLIER_INIT;
import static com.lessu.xieshi.module.sand.viewmodel.SandTestingCommissionDetailViewModel.TESTING_INIT;

/**
 * created by ljs
 * on 2020/10/26
 */
public class TestingCommissionDetailFragment extends BaseVMFragment<SandTestingCommissionDetailViewModel> {
    public static final String SAND_TESTING_COMMISSION_KEY = "testing_commission_key";
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
    @BindView(R.id.sand_testing_commission_detail_ok)
    Button sandTestingCommissionDetailOk;
    private TakePhotosAdapter takePhotosAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sand_testing_commission;
    }


    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        return new TestingCommissionModelFactory(requireActivity().getApplication(), this);
    }

    @Override
    protected Class<SandTestingCommissionDetailViewModel> getViewModelClass() {
        return SandTestingCommissionDetailViewModel.class;
    }

    @Override
    protected void observerData() {
        viewModel.getLoadMoreState().observe(this, moreState -> {
            if (moreState.loadState != LoadState.LOADING) {
                LSAlert.dismissProgressHud();
            } else if (moreState.loadType == 1) {
                LSAlert.showProgressHud(requireActivity(), "正在提交...");
            } else {
                LSAlert.showProgressHud(requireActivity(),"正在加载...");
            }
        });
        viewModel.getThrowable().observe(this,responseThrowable -> {
            ToastUtil.showShort(responseThrowable.message);
        });
        //委托单位
        viewModel.getCurCommission().observe(this, bean -> {
            testingCommissionCompanyName.setText(bean.getCustomerUnitName());
        });
        //检测单位
        viewModel.getTestingCompany().observe(this, bean -> {
            testingCommissionTestingCompany.setText(bean.getDetectionAgencyUnitName());
        });
        //取样地点
        viewModel.getCurSampleLocation().observe(this, location -> {
            sandManageSamplingPlace.setText(viewModel.getSampleLocationSparse(location));
        });
        //取样人员
        viewModel.getCurSampler().observe(this, bean -> sandManageSamplingUser.setText(bean.getSamplerName()));
        //取样日期
        viewModel.getSampleDate().observe(this, date -> sandManageSamplingTime.setText(date));
        //委托人
        viewModel.getCommissionUser().observe(this, commissionUser -> sandManageCommissionUser.setText(commissionUser));
        //委托日期
        viewModel.getCommissionDate().observe(this, date -> sandManageCommissionDate.setText(date));
        //预约取样时间
        viewModel.getOrderSampleDate().observe(this, date -> sandManageSamplingOrderTime.setText(date));
    }

    @Override
    protected void initView() {
        setTitle("检测委托");
        takePhotosAdapter = viewModel.getTakePhotosAdapter();
        sandManageTakePhotosRv.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
        sandManageTakePhotosRv.setAdapter(takePhotosAdapter);

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

        //当前的取样单位类型
        if (viewModel.getSampleCompanyType() == SUPPLIER_INIT) {
            //供应商
            rbSandManageSupplier.setChecked(true);
            viewModel.getSamplers(null, true);
        } else {
            //检测单位
            rbSandManageTestingCompany.setChecked(true);
            String detectionAgencyMemberCode = viewModel.getTestingCommissionBean().getDetectionAgencyMemberCode();
            if (detectionAgencyMemberCode != null) {
                viewModel.getSamplers(detectionAgencyMemberCode, true);
            }
        }

        sandManageSamplingCompanyRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_sand_manage_supplier) {
                //选择了供应商,需要有取样过程
                sandManageSamplingOrderTimeLl.setVisibility(View.GONE);
                sandManageSamplingProcessLl.setVisibility(View.VISIBLE);
                // 不能修改取样日期
                sandManageSamplingTime.setClickable(false);
                viewModel.setWhichSampleCompany(SUPPLIER_INIT);
                viewModel.getSamplers(null, true);
            } else {
                //选择了检测单位,可以预约检测日期
                sandManageSamplingOrderTimeLl.setVisibility(View.VISIBLE);
                sandManageSamplingProcessLl.setVisibility(View.GONE);
                sandManageSamplingTime.setClickable(true);
                viewModel.setWhichSampleCompany(TESTING_INIT);
                List<SandSamplerBean> samplerBeans = viewModel.getSamplerBeans();
                samplerBeans.clear();
                String detectionAgencyMemberCode = viewModel.getTestingCommissionBean().getDetectionAgencyMemberCode();
                if (detectionAgencyMemberCode != null) {
                    viewModel.getSamplers(detectionAgencyMemberCode, true);
                }

            }
        });
    }

    @Override
    protected void initData() {
        //加载取样地点数据
        viewModel.loadSampleLocations();
        //初始化委托单位
        viewModel.setCurCommission(viewModel.getCurCommission().getValue());
        //初始化检测单位
        viewModel.setTestingCompany(viewModel.getTestingCompany().getValue());
        //取样地点
        viewModel.setSampleLocation(viewModel.getCurSampleLocation().getValue());
        //取样人
        viewModel.setCurSampler(viewModel.getCurSampler().getValue());
        //取样日期
        viewModel.setSampleDate(viewModel.getSampleDate().getValue());
        //委托人
        viewModel.setCommissionUser(Shref.getString(requireActivity(), Common.USER_FULL_NAME, ""));
        //委托日期
        viewModel.setCommissionDate(viewModel.getCommissionDate().getValue());
        //预约取样时间
        viewModel.setOrderSampleDate(viewModel.getOrderSampleDate().getValue());
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    //获取选中的委托单位(流向记录)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addSelectCommission(GlobalEvent event) {
        switch (event.getCode()) {
            case EventBusUtil.A:
                viewModel.setCurCommission((FlowDeclarationBean) event.getData());
                break;
            case EventBusUtil.B:
                viewModel.setTestingCompany((AddedTestingCompanyBean) event.getData());
                if (viewModel.getSampleCompanyType() == TESTING_INIT) {
                    AddedTestingCompanyBean data = (AddedTestingCompanyBean) event.getData();
                    String detectionAgencyMemberCode = data.getDetectionAgencyMemberCode();
                    viewModel.getSamplers(detectionAgencyMemberCode, true);
                }
                break;
        }
    }

    /**
     * 打开相机,拍摄照片保存
     */
    private void openCamera() {
        XXPhotoUtil.with(requireActivity()).setListener((photoPath, photoUri) -> {
            takePhotosAdapter.addData(0, photoPath);
            //图片保存成功后判断是否达到了最大数量，如果是，则不再添加照片
            int picCount = takePhotosAdapter.getData().size();
            if (picCount == 11) {
                takePhotosAdapter.remove(picCount - 1);
            }
        }).startCamera();
    }

    @OnClick({R.id.testing_commission_company_name, R.id.testing_commission_testing_company, R.id.sand_manage_sampling_order_time, R.id.sand_manage_sampling_place,
            R.id.sand_manage_sampling_time, R.id.sand_manage_commission_date,
            R.id.sand_manage_sampling_user})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.testing_commission_company_name:
                //TODO:选择流向记录
                Bundle bundle = new Bundle();
                bundle.putInt(SAND_TESTING_COMMISSION_KEY, 1);
                Navigation.findNavController(view).navigate(R.id.actionSandTestingCommissionFragmentToFlowDeclarationList, bundle);
                break;
            case R.id.testing_commission_testing_company:
                //TODO:选择检测单位
                Bundle bundle2 = new Bundle();
                bundle2.putInt(SAND_TESTING_COMMISSION_KEY, 1);
                Navigation.findNavController(view).navigate(R.id.actionSandTestingCommissionFragmentToCompanyList, bundle2);
                break;
            case R.id.sand_manage_sampling_order_time:
                //只有选择“检测单位”才能预约取样时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm", Locale.CHINA);
                TimePickerView timePickerView  = new TimePickerBuilder(requireActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        viewModel.setOrderSampleDate(sdf.format(date));
                    }
                }).setType(new boolean[]{true, true, true, true, true,false})
                        .setLabel("年","月","日","时","分","秒")
                        .build();
                timePickerView.show();
                break;
            case R.id.sand_manage_sampling_place:
                List<String> sampleLocations = viewModel.getSampleLocationDatas();
                if (sampleLocations.size() == 0) {
                    ToastUtil.showShort("没有取样地点数据！");
                    return;
                }
                //选择取样地点
                DateUtil.itemMenuPicker(requireActivity(), sampleLocations,
                        (options1, options2, options3, v) -> {
                            viewModel.setSampleLocation(options1 + 1);
                        });
                break;
            case R.id.sand_manage_sampling_time:
                //取样时间暂时不能更改
                DateUtil.datePicker(getActivity(), (date, v) ->
                        sandManageSamplingTime.setText(DateUtil.getDate(date)));
                break;
            case R.id.sand_manage_commission_date:
                DateUtil.datePicker(getActivity(), (date, v) ->
                        viewModel.setCommissionDate(DateUtil.getDate(date)));
                break;
            case R.id.sand_manage_sampling_user:
                //取样人员
                List<SandSamplerBean> samplerBeans = viewModel.getSamplerBeans();
                String msg = "";
                if (samplerBeans.size() == 0) {
                    if (viewModel.getSampleCompanyType() == TESTING_INIT && TextUtils.isEmpty(testingCommissionTestingCompany.getText())) {
                        msg = "请先选择检测单位！";
                    } else {
                        msg = "没有取样人员数据！";
                    }
                    ToastUtil.showShort(msg);
                    return;
                }
                //选择取样地点
                DateUtil.itemMenuPicker(requireActivity(), samplerBeans,
                        (options1, options2, options3, v) -> {
                            viewModel.setCurSampler(samplerBeans.get(options1));
                        });
                break;
        }
    }

    @OnTextChanged(R.id.sand_manage_identification)
    public void ccNoText(CharSequence s){
        viewModel.setCCNo(s.toString());
    }

    @OnClick(R.id.sand_testing_commission_detail_ok)
    public void onViewClicked() {
        if(TextUtils.isEmpty(testingCommissionCompanyName.getText())){
            ToastUtil.showShort("请选择委托单位!");
            return;
        }
        if(TextUtils.isEmpty(testingCommissionTestingCompany.getText())){
            ToastUtil.showShort("请选择检测单位!");
            return;
        }
        if(TextUtils.isEmpty(sandManageSamplingPlace.getText())){
            ToastUtil.showShort("请选择取样地点!");
            return;
        }
        if(TextUtils.isEmpty(sandManageSamplingUser.getText())){
            ToastUtil.showShort("请选择取样人员!");
            return;
        }
        if(TextUtils.isEmpty(sandManageIdentification.getText())){
            ToastUtil.showShort("请选择唯一性标识号!");
            return;
        }
        if(viewModel.getTestingCommissionBean().getSamplingMethod()==1&&TextUtils.isEmpty(sandManageSamplingOrderTime.getText())){
            //如果是检测单位就需要填写预约取样时间
            ToastUtil.showShort("请预约取样时间!");
            return;
        }
        if(viewModel.getTestingCommissionBean().getSamplingMethod()==2&&viewModel.getTestingCommissionBean().getSamplingProcess()==null){
            ToastUtil.showShort("请记录取样过程!");
            return;
        }

        viewModel.saveTestingCommission();
    }
}
