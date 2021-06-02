package com.lessu.xieshi.module.mis.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.utils.SealManageUtil;
import com.lessu.xieshi.module.mis.bean.SealManageBean;
import com.scetia.Pro.common.Util.Constants;

/**
 * created by Lollipop
 * on 2021/3/2
 */
public class SealManageListAdapter extends BaseQuickAdapter<SealManageBean, BaseViewHolder> {
    public SealManageListAdapter() {
        super(R.layout.seal_manage_list_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, SealManageBean item) {
        helper.setText(R.id.tv_seal_type, item.getYzTypeText());
        helper.setText(R.id.tv_matter_state, item.getYzStateText());
        helper.setText(R.id.tv_matter_apply_content, item.getYzContent());
        helper.setText(R.id.tv_matter_man_content, item.getApplyMan());
        helper.setText(R.id.tv_matter_apply_date, SealManageUtil.formatDate(item.getApplyDate()));
        helper.setGone(R.id.need_seal_number_label,item.getIsGz()== Constants.SealManage.STAMP_OK);
        helper.setGone(R.id.need_seal_number,item.getIsGz()==Constants.SealManage.STAMP_OK);
        String gzFs = item.getGzFs();
        if(gzFs.contains("-")){
            gzFs = gzFs.split("-")[0];
        }
        helper.setText(R.id.need_seal_number, gzFs);
        int stateTextColorByState = SealManageUtil.getStateTextColorByState(mContext, item.getYzState());
        helper.setTextColor(R.id.tv_matter_state, stateTextColorByState);
    }

}
