package com.lessu.xieshi.module.sand.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lessu.uikit.views.LSAlert;
import com.lessu.xieshi.R;
import com.scetia.Pro.common.Util.Constants;
import com.scetia.Pro.common.Util.DateUtil;
import com.lessu.xieshi.Utils.ToastUtil;
import com.lessu.xieshi.base.BaseVMFragment;
import com.lessu.xieshi.module.sand.bean.AddedSandSalesTargetBean;
import com.lessu.xieshi.module.sand.bean.FlowDeclarationBean;
import com.lessu.xieshi.module.sand.bean.SandItemParameterBean;
import com.lessu.xieshi.module.sand.bean.SandSampleBean;
import com.lessu.xieshi.module.sand.bean.SandSpecBean;
import com.lessu.xieshi.module.sand.bean.SandSupplierBean;
import com.lessu.xieshi.module.sand.viewmodel.SMFlowDeclarationDetailViewModel;
import com.lessu.xieshi.view.FullScreenDialog;
import com.scetia.Pro.baseapp.uitls.EventBusUtil;
import com.scetia.Pro.baseapp.uitls.GlobalEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * created by ljs
 * on 2020/10/28
 */
public class SMFlowDeclarationDetailFragment extends BaseVMFragment<SMFlowDeclarationDetailViewModel> {
    public static final String NAVIGATE_KEY = "SMFlowDeclarationDetailFragment_key";
    @BindView(R.id.flow_declaration_detail_record_number)
    TextView flowDeclarationDetailRecordNumber;
    @BindView(R.id.flow_declaration_detail_record_name)
    TextView flowDeclarationDetailRecordName;
    @BindView(R.id.flow_declaration_detail_ship_name)
    EditText flowDeclarationDetailShipName;
    @BindView(R.id.flow_declaration_detail_sand_name)
    TextView flowDeclarationDetailSandName;
    @BindView(R.id.flow_declaration_detail_sand_spec)
    TextView flowDeclarationDetailSandSpec;
    @BindView(R.id.flow_declaration_detail_wharf_name)
    EditText flowDeclarationDetailWharfName;
    @BindView(R.id.flow_declaration_detail_sale_user)
    TextView flowDeclarationDetailSaleUser;
    @BindView(R.id.flow_declaration_detail_sale_number)
    EditText flowDeclarationDetailSaleNumber;
    @BindView(R.id.bt_flow_declaration_detail_save)
    Button btFlowDeclarationDetailSave;
    @BindView(R.id.bt_flow_declaration_detail_declaration)
    Button btFlowDeclarationDetailDeclaration;
    @BindView(R.id.flow_declaration_detail_test_parameter)
    TextView flowDeclarationDetailTestParameter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sm_flow_declaration_detail;
    }

    @Override
    protected Class<SMFlowDeclarationDetailViewModel> getViewModelClass() {
        return SMFlowDeclarationDetailViewModel.class;
    }

    @Override
    protected void observerData() {
        viewModel.getLoadState().observe(this,loadState -> {
            switch (loadState) {
                case LOADING:
                    LSAlert.showProgressHud(requireActivity(), loadState.getMessage());
                    break;
                case SUCCESS:
                    LSAlert.dismissProgressHud();
                    if(loadState.getCode()==204){
                        //完成提交操作成功返回
                        LSAlert.showAlert(requireActivity(), loadState.getMessage());
                        //当前信息保存成功后，显示“委托”按钮可以让用户立即进行委托
                        //如果已经是委托状态就更新后就不能再进行委托了
                        if(!viewModel.getFlowDeclarationBean().getFlowInfoStatus()
                                .equals(Constants.FlowDeclaration.COMMISSIONED_STATE)) {
                            btFlowDeclarationDetailDeclaration.setVisibility(View.VISIBLE);
                        }
                        EventBusUtil.sendEvent(new GlobalEvent<>(EventBusUtil.D,this));
                    }
                    break;
                case FAILURE:
                    LSAlert.dismissProgressHud();
                    ToastUtil.showShort(loadState.getMessage());
                    break;
            }
        });

        //监听流向申报数据变化
        viewModel.getFlowDeclarationLiveData().observe(this, this::initBean);

        //监听检测参名称数据变化
        viewModel.getCurParameterNames().observe(this, parameterNames -> {
            flowDeclarationDetailTestParameter.setText(parameterNames);
        });

        viewModel.getCurSandSpecBean().observe(this, curBean -> {
            flowDeclarationDetailSandSpec.setText(curBean.getSpecName());
            viewModel.getFlowDeclarationBean().setSpecID(curBean.getSpecID());
        });
    }

    @Override
    protected void initView() {
        setTitle("流向申报");
        viewModel.getListAdapter().setOnItemClickListener((adapter, view, position) -> {
            SandItemParameterBean bean = (SandItemParameterBean) adapter.getItem(position);
            if (bean.getMustBeDetectFlag() == 1) {
                ToastUtil.showShort("当前选项为必检参数！");
                return;
            }
            viewModel.getListAdapter().changedItemState(bean, position);
        });
    }

    @Override
    protected void initData() {
        viewModel.loadSandItemSamples();
        viewModel.getCurParameterNames().setValue(viewModel.getCurParameterNames().getValue());
        viewModel.getCurSandSpecBean().setValue(viewModel.getCurSandSpecBean().getValue());
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    /**
     * 进入页面初始化数据展示
     *
     * @param bean
     */
    private void initBean(FlowDeclarationBean bean) {
        flowDeclarationDetailRecordNumber.setText(bean.getPutOnRecordsPassport());
        flowDeclarationDetailRecordName.setText(bean.getProductionUnitName());
        flowDeclarationDetailSandName.setText(bean.getSampleName());
        flowDeclarationDetailSaleUser.setText(bean.getCustomerUnitName());
        flowDeclarationDetailShipName.setText(bean.getShipName());
        flowDeclarationDetailWharfName.setText(bean.getTerminalName());
        flowDeclarationDetailSaleNumber.setText(bean.getSalesVolume());
        //注意 ：这里需要判断当前项是否已经委托过了，如果委托过了就不能再进行委托了
        if(bean.getId()==null||bean.getFlowInfoStatus().equals("已委托")){
            btFlowDeclarationDetailDeclaration.setVisibility(View.GONE);
        }else{
            btFlowDeclarationDetailDeclaration.setVisibility(View.VISIBLE);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void initFlowDeclarationBean(GlobalEvent<FlowDeclarationBean> event) {
        if (event.getCode() == EventBusUtil.A) {
            //这里进入页面后要移除事件的监听，防止返回当前页面后还继续收到此事件的
            EventBusUtil.removeStickyEvent(event);
            FlowDeclarationBean data = event.getData();
            viewModel.setFlowDeclarationBean(data);
            viewModel.loadInitFlowDeclaration(viewModel.getFlowDeclarationBean().getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveSelectBean(GlobalEvent<Object> event) {
        Object data = event.getData();
        if (event.getCode() == EventBusUtil.B&&data instanceof AddedSandSalesTargetBean) {
            AddedSandSalesTargetBean bean = (AddedSandSalesTargetBean)data;
            //销售对象
            viewModel.getFlowDeclarationBean().setCustomerUnitMemberCode(bean.getCustomerUnitMemberCode());
            viewModel.getFlowDeclarationBean().setCustomerUnitName(bean.getCustomerUnitName());
            flowDeclarationDetailSaleUser.setText(bean.getCustomerUnitName());
        } else if (event.getCode() == EventBusUtil.C&&data instanceof SandSupplierBean) {
            SandSupplierBean bean = (SandSupplierBean) data;
            //备案证号
            viewModel.getFlowDeclarationBean().setPutOnRecordsPassport(bean.getPutOnRecordsPassport());
            //备案证名称
            viewModel.getFlowDeclarationBean().setProductionUnitName(bean.getProductionUnitName());
            flowDeclarationDetailRecordNumber.setText(bean.getPutOnRecordsPassport());
            flowDeclarationDetailRecordName.setText(bean.getProductionUnitName());
        }else if (event.getCode()==EventBusUtil.D&&data instanceof Boolean){
            btFlowDeclarationDetailDeclaration.setVisibility(View.GONE);
        }
    }


    //记录船名
    @OnTextChanged(value = R.id.flow_declaration_detail_ship_name, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void shipNameTextChanged(CharSequence s) {
        viewModel.getFlowDeclarationBean().setShipName(s.toString());
    }

    //记录码头
    @OnTextChanged(value = R.id.flow_declaration_detail_wharf_name, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void wharfNameTextChanged(CharSequence s) {
        viewModel.getFlowDeclarationBean().setTerminalName(s.toString());
    }

    //记录销售数量
    @OnTextChanged(value = R.id.flow_declaration_detail_sale_number, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void salesNumTextChanged(CharSequence s) {
        if(TextUtils.isEmpty(s)){
            return;
        }
        try {
            int cur = Integer.parseInt(s.toString());
            if (cur > 1000) {
                cur = 1000;
                ToastUtil.showShort("最大数量为1000！");
                flowDeclarationDetailSaleNumber.setText("1000");
            } else if (cur <= 0) {
                cur = 1;
                ToastUtil.showShort("最小数量必须大于为0！");
                flowDeclarationDetailSaleNumber.setText("1");
            }
            viewModel.getFlowDeclarationBean().setSalesVolumePost(cur + "");
        }catch (Exception e){
            ToastUtil.showShort("请输入正确的数字！");
        }
    }


    @OnClick({R.id.bt_flow_declaration_detail_save, R.id.bt_flow_declaration_detail_declaration,
            R.id.flow_declaration_detail_record_number, R.id.flow_declaration_detail_sand_name,
            R.id.flow_declaration_detail_sand_spec, R.id.flow_declaration_detail_test_parameter,
            R.id.flow_declaration_detail_sale_user})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_flow_declaration_detail_save:
                if (TextUtils.isEmpty(flowDeclarationDetailRecordName.getText())) {
                    ToastUtil.showShort("请选择备案证号！");
                    return;
                }
                if (TextUtils.isEmpty(flowDeclarationDetailShipName.getText())) {
                    ToastUtil.showShort("请输入船名称！");
                    return;
                }
                if (TextUtils.isEmpty(flowDeclarationDetailSandName.getText())) {
                    ToastUtil.showShort("请选择建设用砂名称！");
                    return;
                }
                if (TextUtils.isEmpty(flowDeclarationDetailSandSpec.getText())) {
                    ToastUtil.showShort("请选择建设用砂规格！");
                    return;
                }
                if (TextUtils.isEmpty(flowDeclarationDetailWharfName.getText())) {
                    ToastUtil.showShort("请输入码头名称！");
                    return;
                }
                if (TextUtils.isEmpty(flowDeclarationDetailSaleUser.getText())) {
                    ToastUtil.showShort("请选择销售对象！");
                    return;
                }
                if (TextUtils.isEmpty(flowDeclarationDetailSaleNumber.getText())) {
                    ToastUtil.showShort("请输入销售数量！");
                    return;
                }
                //保存
                viewModel.saveFlowDeclaration();
                break;
            case R.id.bt_flow_declaration_detail_declaration:
                //界面设计要求：可以从当前流转记录直接进行委托D
                EventBusUtil.sendStickyEvent(new GlobalEvent<>(EventBusUtil.E,viewModel.getFlowDeclarationBean()));
                Navigation.findNavController(view).navigate(R.id.flowDeclarationDetailToCommissionDetail);
                break;
            case R.id.flow_declaration_detail_sand_name:
                List<SandSampleBean> value = viewModel.getSanSampleLiveData().getValue();
                if (value == null) {
                    ToastUtil.showShort("没有建设用砂样品！");
                    return;
                }
                //建设用砂样品名称列表
                DateUtil.itemMenuPicker(requireActivity(), value, (options1, options2, options3, v) -> {
                    viewModel.getFlowDeclarationBean().setParameterIDs("");
                    flowDeclarationDetailSandName.setText(value.get(options1).getSampleName());
                    viewModel.getFlowDeclarationBean().setSampleID(value.get(options1).getSampleId());
                    viewModel.getFlowDeclarationBean().setSampleName(value.get(options1).getSampleName());
                    viewModel.loadSandItemParameters(value.get(options1).getSampleId());
                });
                break;
            case R.id.flow_declaration_detail_sand_spec:
                List<SandSpecBean> sandSpecBeans = viewModel.getSandSpecLiveData().getValue();
                if (sandSpecBeans == null) {
                    ToastUtil.showShort("请先选择用砂样品名称！");
                    return;
                }
                //建设用砂规格列表
                DateUtil.itemMenuPicker(requireActivity(), sandSpecBeans, (options1, options2, options3, v) -> {
                    viewModel.getCurSandSpecBean().postValue(sandSpecBeans.get(options1));
                });
                break;
            case R.id.flow_declaration_detail_record_number:
                //备案证号
                Bundle bundle = new Bundle();
                bundle.putInt(NAVIGATE_KEY, 1);
                Navigation.findNavController(view).navigate(R.id.actionSMFlowDeclarationDetailToSupplierFragment, bundle);
                break;
            case R.id.flow_declaration_detail_sale_user:
                //选择销售对象
                Bundle bundleSaleTarget = new Bundle();
                bundleSaleTarget.putInt(NAVIGATE_KEY, 1);
                Navigation.findNavController(view).navigate(R.id.actionSMFLowDeclarationDetailToSaleTarget, bundleSaleTarget);
                break;
        }
    }

    @OnClick(R.id.flow_declaration_detail_test_parameter)
    public void parameterBtnClick() {
        List<SandItemParameterBean> parameterBeans = viewModel.getSandItemParameterLiveData().getValue();
        if (parameterBeans == null) {
            ToastUtil.showShort("请先选择用砂样品名称！");
            return;
        }
        viewModel.getListAdapter().setDefaultSelectBeans(viewModel.getFlowDeclarationBean().getParameterIDs());
        viewModel.getListAdapter().setNewData(parameterBeans);
        FullScreenDialog fullScreenDialog = new FullScreenDialog.Builder(requireActivity(), R.layout.sand_parameters_dialog_layout)
                .setTitle("检测参数")
                .build();
        fullScreenDialog.setOnBindView(rootView -> {
            RecyclerView rv = rootView.findViewById(R.id.flow_declaration_detail_parameter_rv);
            rv.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
            rv.setAdapter(viewModel.getListAdapter());
        });

        fullScreenDialog.setOkButtonClickListener(view -> {
            StringBuilder sb = new StringBuilder();
            StringBuilder sbNames = new StringBuilder();
            for (String parameterId : viewModel.getListAdapter().getParameters()) {
                sb.append(parameterId).append(";");
                sbNames.append(viewModel.getSandParameterMap().get(parameterId).getParameterName()).append(";");
            }
            viewModel.getCurParameterNames().setValue(sbNames.toString());
            viewModel.getFlowDeclarationBean().setParameterIDs(sb.subSequence(0, sb.length() - 1).toString());
            return false;
        });
        fullScreenDialog.shows();
    }

    @Override
    public void leftNavBarClick(View view) {
        if(!viewModel.isEdit()){
            //提示用户是否保存
            LSAlert.showAlert(requireActivity(), "提示", "是否保存已更改的信息", "保存", "取消",
                    false, new LSAlert.AlertCallback() {
                        @Override
                        public void onConfirm() {
                            viewModel.saveFlowDeclaration();
                        }

                        @Override
                        public void onCancel() {
                            SMFlowDeclarationDetailFragment.super.leftNavBarClick(view);
                        }
                    });
            return;
        }
        super.leftNavBarClick(view);
    }
}
