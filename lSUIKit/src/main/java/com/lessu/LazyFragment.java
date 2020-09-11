package com.lessu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonElement;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;
import com.lessu.net.EasyAPI;
import com.lessu.uikit.R;
import com.lessu.uikit.views.LSAlert;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class LazyFragment extends Fragment {

    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initImmersionBar();
    private Unbinder unbinder;
    public interface ResultResponse{
        void getResult(boolean success,JsonElement result,String errorMsg);
    }
    protected boolean isCreate = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  =inflater.inflate(getLayoutId(),container,false);
        unbinder=ButterKnife.bind(this,view);
        isCreate = true;
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initImmersionBar();
        if(getUserVisibleHint()) {
            initData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isCreate){
            return;
        }
        if(getUserVisibleHint()){
            initData();
        }else{

        }
    }

    @Override
    public void onDestroy() {
        isCreate = false;
        super.onDestroy();
        unbinder.unbind();
    }
    protected void getMeetingList(String token, String meetingID, final ResultResponse resultResponse) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("Token", token);
        /*params.put("s3", "");
        params.put("s4", "");*/
        //传入会议id
        params.put("s1", meetingID);

        EasyAPI.apiConnectionAsync(getActivity(), false, false, ApiMethodDescription.get("/ServiceMis.asmx/GetMeetingList"),
                params, new EasyAPI.ApiFastSuccessFailedCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        boolean isSuccess = result.getAsJsonObject().get("Success").getAsBoolean();
                        if(isSuccess){
                            resultResponse.getResult(true,result,"");
                        }else{
                           String message= result.getAsJsonObject().get("Message").getAsString();
                            resultResponse.getResult(false,result,message);
                        }

                    }

                    @Override
                    public String onFailed(ApiError error) {
                        resultResponse.getResult(false,null,error.errorMeesage);
                        LSAlert.showAlert(getActivity(), getActivity().getString( R.string.api_connection_failed_error ), error.errorMeesage , "确定", null);
                        return null;
                    }
                });
    }
}
