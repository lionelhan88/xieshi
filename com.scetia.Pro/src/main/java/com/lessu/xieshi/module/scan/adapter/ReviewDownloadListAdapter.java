package com.lessu.xieshi.module.scan.adapter;

import androidx.lifecycle.MutableLiveData;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lessu.xieshi.R;
import com.lessu.xieshi.module.scan.bean.ReceiveSampleInfoBean;

/**
 * created by ljs
 * on 2020/12/11
 */
public class ReviewDownloadListAdapter extends BaseQuickAdapter<ReceiveSampleInfoBean, BaseViewHolder> {
    private String toastMessage="";
    private int messageState;
    private int lostMsg;
    private MutableLiveData<String> toastLiveData = new MutableLiveData<>();
    public ReviewDownloadListAdapter() {
        super(R.layout.item_shujvjiaohu);
    }

    public MutableLiveData<String> getToastLiveData() {
        return toastLiveData;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReceiveSampleInfoBean item) {
       helper.setText(R.id.tv_biaoshibianhao,item.getSample_BsId());
       helper.setText(R.id.tv_xiangmumingchen,item.getItemName());
       helper.setText(R.id.tv_yangpinmingchen,item.getSampleName());
       helper.setText(R.id.tv_hetongdengjihao,item.getContract_SignNo());
       String state = String.valueOf(item.getRetStatus());
       switch (state.substring(0,1)){
           case "0":
               helper.setText(R.id.tv_yangpinzhuangtai,"正常");
               break;
           case "1":
               helper.setText(R.id.tv_yangpinzhuangtai,"未登记");
               if(messageState==0||messageState==10){
                  messageState++;
               }
               break;
           case "2":
               helper.setText(R.id.tv_yangpinzhuangtai,"已确认");
               break;
           case "3":
               helper.setText(R.id.tv_yangpinzhuangtai,"异常");
               if(messageState==0||messageState==1){
                   messageState+=10;
               }
               break;
           case "4":
               //缺失的数量
               String lostNum =state.substring(1);
               helper.setText(R.id.tv_yangpinzhuangtai,"缺"+lostNum+"件");
               lostMsg = 1;
               break;
       }
       if(helper.getAdapterPosition()==getData().size()-1){
           toastLiveData.postValue(getToastMessage());
       }
    }

    /**
     * 弹出对应的提示信息
     * @return
     */
    private String getToastMessage() {
        switch (messageState){
            case 1:
                toastMessage = "您提交的样品中存在未登记样品，";
                break;
            case 10:
                toastMessage = "您提交的样品中存在异常样品，";
                break;
            case 11:
                toastMessage = "您提交的样品中存在未登记与异常样品，";
                break;
        }
        return "共下载"+getData().size()+"条数据\n"+toastMessage+(lostMsg > 0 ? "且部分样品缺失唯一性标识，请确认!" : "请确认！");
    }
}
