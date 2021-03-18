package com.lessu.xieshi.module.dataexamine;

import com.google.gson.EasyGson;
import com.google.gson.GsonValidate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lessu.navigation.NavigationActivity;
import com.lessu.uikit.easy.EasyUI;
import com.lessu.xieshi.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ExamineSampleListActivity extends NavigationActivity implements OnItemClickListener {
    JsonArray list = new JsonArray();

    @Override
    protected int getLayoutId() {
        return R.layout.examine_sample_list_activity;
    }

    @Override
    protected void initView() {
        this.setTitle("报告样品");
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();

        String jsonString = bundle.getString("jsonStringSample");

        JsonElement jsonElement = EasyGson.jsonFromString(jsonString);
        if (jsonElement.isJsonArray()) {
            list = jsonElement.getAsJsonArray().get(0).getAsJsonObject().get("SampleInfo").getAsJsonArray();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View cell, int position, long id) {
    }

    protected BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(int position, View view, ViewGroup parentViewGroup) {
            if (view == null) {
                view = View.inflate(ExamineSampleListActivity.this, R.layout.examine_sample_list_item, null);
            }
            TextView textViewSampleId = view.findViewById(R.id.sampleIdTextView);
            textViewSampleId.setText(GsonValidate.getStringByKeyPath(list.get(position), "Sample_Id", ""));

            TextView textViewExamResult = (TextView) view.findViewById(R.id.sampleNameTextView);
            textViewExamResult.setText(GsonValidate.getStringByKeyPath(list.get(position), "SampleName", ""));
            view.findViewById(R.id.sampleDetailButton).setTag(position);
            EasyUI.setButtonClickMethod(view.findViewById(R.id.sampleDetailButton), ExamineSampleListActivity.this, "sampleDetailButtonDidClick");
            view.findViewById(R.id.paramsButton).setTag(position);
            EasyUI.setButtonClickMethod(view.findViewById(R.id.paramsButton), ExamineSampleListActivity.this, "paramsButtonDidClick");
            return view;
        }


        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }


    };

    public void sampleDetailButtonDidClick(View button) {
        final int position = (Integer) button.getTag();
        Intent intent = new Intent(ExamineSampleListActivity.this, ExamineSampleDetailActivity.class);
        JsonObject json = list.get(position).getAsJsonObject();
        String jsonString = json.toString();
        intent.putExtra("jsonString", jsonString);
        startActivity(intent);
    }

    public void paramsButtonDidClick(View button) {
        final int position = (Integer) button.getTag();
        Intent intent = new Intent(ExamineSampleListActivity.this, ExamineParamsActivity.class);
        String jsonString = list.get(position).getAsJsonObject().toString();
        intent.putExtra("jsonString", jsonString);
        startActivity(intent);

    }

}
