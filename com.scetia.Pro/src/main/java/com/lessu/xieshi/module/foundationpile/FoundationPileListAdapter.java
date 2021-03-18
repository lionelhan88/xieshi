package com.lessu.xieshi.module.foundationpile;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lessu.xieshi.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by Lollipop
 * on 2021/3/17
 */
public class FoundationPileListAdapter extends BaseAdapter {
    private List<FoundationPileBean> foundationPileBeans;

    public FoundationPileListAdapter() {
        foundationPileBeans = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return foundationPileBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return foundationPileBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.project_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        FoundationPileBean bean = foundationPileBeans.get(position);
        String stakeName = bean.getStakeName();
        String[] split = stakeName.split(",");
        String memberNo = split[0];
        String testingCompanyName = split[1];
        String machineNo = split[2];
        String pileNo = split[3];
        String projectName = split[4];
        viewHolder.projectTextView.setText(testingCompanyName);
        viewHolder.tvMemberNo.setText(memberNo);
        viewHolder.tvMachineNo.setText(machineNo);
        viewHolder.tvPileNo.setText(pileNo);
        viewHolder.tvProjectName.setText(projectName);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.projectNameTextView)
        TextView projectTextView;
        @BindView(R.id.tv_huiyuanhao)
        TextView tvMemberNo;
        @BindView(R.id.tv_shebeibianhao)
        TextView tvMachineNo;
        @BindView(R.id.tv_zhaunghao)
        TextView tvPileNo;
        @BindView(R.id.tv_project_name)
        TextView tvProjectName;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this,itemView);
        }
    }

    public void setNewData(List<FoundationPileBean> datas){
        foundationPileBeans.clear();
        foundationPileBeans.addAll(datas);
        notifyDataSetChanged();
    }
    public void clearData(){
        foundationPileBeans.clear();
        notifyDataSetChanged();
    }

}
