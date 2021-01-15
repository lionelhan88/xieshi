package com.lessu.xieshi.module.sand.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.base.BaseVMFragment;
import com.lessu.xieshi.module.meet.activity.ScalePictureActivity;
import com.lessu.xieshi.module.sand.adapter.TakePhotosAdapter;
import com.lessu.xieshi.module.sand.bean.AddedTestingCompanyBean;
import com.lessu.xieshi.module.sand.bean.FlowDeclarationBean;
import com.lessu.xieshi.module.sand.bean.SandSamplerBean;
import com.lessu.xieshi.module.sand.bean.TestingCommissionBean;
import com.lessu.xieshi.module.sand.viewmodel.SandTestingCommissionDetailViewModel;
import com.lessu.xieshi.module.sand.viewmodel.TestingCommissionModelFactory;
import com.lessu.xieshi.photo.XXPhotoUtil;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;
import com.scetia.Pro.baseapp.uitls.LoadState;
import com.scetia.Pro.common.Util.Common;
import com.scetia.Pro.common.Util.DateUtil;
import com.scetia.Pro.common.Util.SPUtil;

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
    @BindView(R.id.ll_sand_manage_sampling_time)
    LinearLayout llSandManageSamplingTime;
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
        viewModel.getLoadDataState().observe(this, loadState -> {
            if (loadState == LoadState.LOADING) {
                LSAlert.showProgressHud(requireActivity(), "正在加载...");
            } else {
                if (loadState == LoadState.SUCCESS) {
                    if (viewModel.getTestingCommissionBean().getSamplingMethod() == SUPPLIER_INIT) {
                        rbSandManageSupplier.setChecked(true);
                    } else {
                        rbSandManageTestingCompany.setChecked(true);
                    }
                }
                LSAlert.dismissProgressHud();
            }
        });

        viewModel.getPostDataState().observe(this, loadState -> {
            switch (loadState) {
                case LOADING:
                    LSAlert.showProgressHud(requireActivity(), "正在提交...");
                    break;
                case SUCCESS:
                    LSAlert.dismissProgressHud();
                    LSAlert.showAlert(requireContext(), "提交成功！");
                    //TODO:刷新列表页面
                    EventBusUtil.sendEvent(new GlobalEvent<>(EventBusUtil.D,true));
                    break;
                case FAILURE:
                    LSAlert.dismissProgressHud();
                    break;
            }
        });
        viewModel.getThrowable().observe(this, responseThrowable -> {
            ToastUtil.showShort(responseThrowable.message);
        });
        //委托单位
        viewModel.getCurCommission().observe(this, bean -> {
            testingCommissionCompanyName.setText(bean.getProductionUnitName());
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
        //唯一性标识号
        viewModel.getCcNoLiveData().observe(this, ccNo -> sandManageIdentification.setText(ccNo));
        //取样单位类型
        viewModel.getSampleCompanyType().observe(this, integer -> {
            List<SandSamplerBean> samplerBeans = viewModel.getSamplerBeans();
            samplerBeans.clear();
            //当前的取样单位类型
            if (integer == SUPPLIER_INIT) {
                //选择了供应商,需要有取样过程
                sandManageSamplingOrderTimeLl.setVisibility(View.GONE);
                sandManageSamplingProcessLl.setVisibility(View.VISIBLE);
                llSandManageSamplingTime.setVisibility(View.VISIBLE);
                // 不能修改取样日期
                sandManageSamplingTime.setClickable(false);
                viewModel.getSamplers(null);
            } else {
                //选择了检测单位,可以预约检测日期
                sandManageSamplingOrderTimeLl.setVisibility(View.VISIBLE);
                sandManageSamplingProcessLl.setVisibility(View.GONE);
                llSandManageSamplingTime.setVisibility(View.GONE);
                sandManageSamplingTime.setClickable(true);
                String detectionAgencyMemberCode = viewModel.getTestingCommissionBean().getDetectionAgencyMemberCode();
                if (detectionAgencyMemberCode != null) {
                    viewModel.getSamplers(detectionAgencyMemberCode);
                }
            }
        });
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

        sandManageSamplingCompanyRg.setOnCheckedChangeListener((group, checkedId) -> {
            //这里注意，只有当用户手动点击切换是才会清空已经选中的取样人员
            if (rbSandManageTestingCompany.isPressed() || rbSandManageSupplier.isPressed()) {
                viewModel.setCurSampler(new SandSamplerBean());
            }
            if (checkedId == R.id.rb_sand_manage_supplier) {
                viewModel.setWhichSampleCompany(SUPPLIER_INIT);
            } else {
                viewModel.setWhichSampleCompany(TESTING_INIT);
            }
        });
    }

    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        if(arguments!=null){
            String id = arguments.getString("id");
            String flowInfoId = arguments.getString("flowInfoId");
            viewModel.getTestingCommissionBean().setId(id);
            viewModel.getTestingCommissionBean().setFlowInfoId(flowInfoId);
            viewModel.loadSingleInfo(id);
        }else {
            //加载取样地点数据
            viewModel.loadSampleLocations(-1);
            //委托人
            viewModel.setCommissionUser(SPUtil.getSPConfig(Common.USER_FULL_NAME, ""));
        }
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
                FlowDeclarationBean data1 = (FlowDeclarationBean) event.getData();
                viewModel.getCommissionInfo(data1.getId());
                break;
            case EventBusUtil.B:
                //选择新的检测单位，取样人员也要清空重新选择
                viewModel.setCurSampler(new SandSamplerBean());
                viewModel.setTestingCompany((AddedTestingCompanyBean) event.getData());
                if (viewModel.getSampleCompanyType().getValue() == TESTING_INIT) {
                    AddedTestingCompanyBean data = (AddedTestingCompanyBean) event.getData();
                    String detectionAgencyMemberCode = data.getDetectionAgencyMemberCode();
                    viewModel.getSamplers(detectionAgencyMemberCode);
                }
                break;
        }
    }

    //从委托列表页面点击某一项进入详情页面
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void loadSingleInfo(GlobalEvent<TestingCommissionBean> event) {
        if (event.getCode() == EventBusUtil.C) {
            EventBusUtil.removeStickyEvent(event);
            viewModel.setTestingCommissionBean(event.getData());
            viewModel.loadSingleInfo(event.getData().getId());
        }
    }

    /**
     * 从流向信息记录详情跳转过来进行委托
     */
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void receiveFlowToThis(GlobalEvent<FlowDeclarationBean> event){
        if(EventBusUtil.E==event.getCode()){
            //禁止选择委托单位
            testingCommissionCompanyName.setClickable(false);
            viewModel.setCurCommission(event.getData());
        }
    }

    /**
     * 打开相机,拍摄照片保存
     */
    private void openCamera() {
        XXPhotoUtil.with(requireActivity()).setCompress(true).setListener((photoPath, photoUri) -> {
            viewModel.setSamplingProcess("");
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
                TimePickerView timePickerView = new TimePickerBuilder(requireActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        viewModel.setOrderSampleDate(sdf.format(date));
                    }
                }).setType(new boolean[]{true, true, true, true, true, false})
                        .setLabel("年", "月", "日", "时", "分", "秒")
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
                    if (viewModel.getSampleCompanyType().getValue() == TESTING_INIT && TextUtils.isEmpty(testingCommissionTestingCompany.getText())) {
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
    public void ccNoText(CharSequence s) {
        viewModel.setCCNo(s.toString());
    }

    @OnClick(R.id.sand_testing_commission_detail_ok)
    public void onViewClicked() {
        if (TextUtils.isEmpty(testingCommissionCompanyName.getText())) {
            ToastUtil.showShort("请选择委托单位!");
            return;
        }
        if (TextUtils.isEmpty(testingCommissionTestingCompany.getText())) {
            ToastUtil.showShort("请选择检测单位!");
            return;
        }
        if (TextUtils.isEmpty(sandManageSamplingPlace.getText())) {
            ToastUtil.showShort("请选择取样地点!");
            return;
        }
        if (TextUtils.isEmpty(sandManageSamplingUser.getText())) {
            ToastUtil.showShort("请选择取样人员!");
            return;
        }
        if (TextUtils.isEmpty(sandManageIdentification.getText())) {
            ToastUtil.showShort("请选择唯一性标识号!");
            return;
        }
        if (viewModel.getTestingCommissionBean().getSamplingMethod() == 1 && TextUtils.isEmpty(sandManageSamplingOrderTime.getText())) {
            //如果是检测单位就需要填写预约取样时间
            ToastUtil.showShort("请预约取样时间!");
            return;
        }
        if (viewModel.getTestingCommissionBean().getSamplingMethod() == 2 && viewModel.getTestingCommissionBean().getSamplingProcess() == null) {
            ToastUtil.showShort("请记录取样过程!");
            return;
        }
        if (viewModel.getTestingCommissionBean().getId() != null) {
            //更新信息
            viewModel.updateTestingCommission();
        } else {
            //新增信息
            viewModel.saveTestingCommission();
        }
    }
}
