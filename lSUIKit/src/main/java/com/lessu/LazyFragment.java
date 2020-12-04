package com.lessu;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonElement;
import com.lessu.net.ApiConnection;
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
    protected abstract void stopData();
    protected abstract void initImmersionBar();
    private Unbinder unbinder;
    private ApiConnection apiConnection;

    public ApiConnection getApiConnection() {
        return apiConnection;
    }

    /**
     * 当前页面的是否可见
     */
    private boolean currentVisibleState;
    public interface ResultResponse{
        void getResult(boolean success,JsonElement result,String errorMsg);
    }
    protected boolean isViewCreated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  =inflater.inflate(getLayoutId(),container,false);
        unbinder=ButterKnife.bind(this,view);
        isViewCreated = true;
        initView();
        initImmersionBar();
        //页面第一次创建出来加载数据
        if(getUserVisibleHint()) {
            dispatchUserVisibleHint(true);
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isViewCreated){
            return;
        }
        //由不可见状态变为可见状态 需要加载数据
        if(!currentVisibleState&&isVisibleToUser){
            dispatchUserVisibleHint(true);
        }else if(currentVisibleState&&!isVisibleToUser){
            //有可见状态变为不可见状态取消加载数据
            dispatchUserVisibleHint(false);
        }
    }

    /**
     * 分发加载事件
     * @param isVisible 是否加载数据
     */
    private void dispatchUserVisibleHint(boolean isVisible){
        if(isVisible==currentVisibleState){
            return;
        }
        currentVisibleState = isVisible;
        if(isVisible){
            initData();
        }else{
            stopData();
        }
    }
    @Override
    public void onDestroy() {
        isViewCreated = false;
        super.onDestroy();
        unbinder.unbind();
    }
    protected void getMeetingList(String token, String meetingID, final ResultResponse resultResponse) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("Token", token);
        //传入会议id
        params.put("s1", meetingID);

       apiConnection= EasyAPI.apiConnectionAsync(getActivity(), false, false, ApiMethodDescription.get("/ServiceMis.asmx/GetMeetingList"),
                params, new EasyAPI.ApiFastSuccessFailedCallBack() {
                    @Override
                    public void onSuccessJson(JsonElement result) {
                        boolean isSuccess = result.getAsJsonObject().get("Success").getAsBoolean();
                        if(isSuccess){
                            resultResponse.getResult(true,result,"");
                        }else{
                            String message= result.getAsJsonObject().get("Message").getAsString();
                            resultResponse.getResult(false,result,message);
                            LSAlert.showAlert(requireActivity(),
                                   getResources().getString(R.string.api_connection_failed_failure),
                                    message, "确定", null);
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
