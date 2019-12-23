package com.lessu.net.page;

import com.google.gson.GsonValidate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.google.gson.EasyGson;
import com.lessu.foundation.Validate;
import com.lessu.net.ApiBase;
import com.lessu.net.ApiConnection;
import com.lessu.net.ApiError;
import com.lessu.net.ApiMethodDescription;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
  Created by lessu on 14-6-25.
 */
public class PageController {
    public String PageControllerErrorDomain = "PageControllerErrorDomain";

    /**
     * default keys
     */
    public String pageName                              = "page";
    public String stepName                              = "step";
    public int    step                                  = 10;
    public String successKey                            = "success";
    public String listKey                               = "list";
    public String messageKey                            = "message";
    public String pageInfoKey                           = "pageinfo";
    public String pageInfoTotalKey                      = "total_page";




    public String keyName = "id";


    /**
     * Status
     */
    private int currentPage = 1;
    private int totalPage;
    protected boolean isLoading;
    private boolean isRefreashing;




    /**
     * set next fields
     */
    protected PageControllerDelegate delegate;
    protected boolean shouldMerge;


    protected ApiMethodDescription apiMethod;
    protected HashMap<String,Object> apiParams;


    /**
     * field
     */
    protected List list;
    private ApiConnection nextPageConnection;
    private ApiConnection refreashConnection;

    private PageInfoAdapterInterface pageinfoAdapter;


    public enum MergeDirection
    {
        Front,
        Tail
    }

    public PageController() {
        init();
    }


    public void init() {
//        _pageName   = [kLSPageControllerPageName copy];
//        _stepName   = [kLSPageControllerStepName copy];
//        _step       = kLSPageControllerStep;
        setCurrentPage(1);
        keyName = "id";
        apiParams = new HashMap<String ,Object>();
        list      = new ArrayList<Object>();

    }
    public void nextPage(){
        if (!hasMore()) {
            if (delegate!=null) {
                ApiError error = new ApiError();
                error.errorCode = 002;
                error.errorDomain = PageControllerErrorDomain;
                error.errorMeesage = "没有更多啦";
//                delegate.onNextFailedBlock(error);
                if (delegate!=null){
                    delegate.onNextFailed(error);
                }
            }
            return ;
        }
        setCurrentPage(getCurrentPage() + 1);

        HashMap<String,Object> params = (HashMap<String, Object>) apiParams.clone();

        params.put(stepName, Validate.stringFromInt(step));
        params.put(pageName, Validate.stringFromInt(getCurrentPage()));


        nextPageConnection = ApiBase.sharedInstance().getConnectionWithApiMethod(apiMethod, params);
        nextPageConnection .setCallbacks(new ApiConnection .ApiConnectionCallback() {

            @Override
            public void onSuccessJson(JsonElement result) {
                super.onSuccessJson(result);
                nextRequestDidSuccess(result);
            }

            @Override
            public void onFailed(ApiError error) {
                super.onFailed(error);
                if (delegate!=null){
                    delegate.onNextFailed(error);
                };
            }

            @Override
            public void onFinal() {
                super.onFinal();
                isLoading = false;
            }
        });
        boolean validatePass = true;

        validatePass = delegate.beforeNextPageRequest(nextPageConnection);

        if (validatePass) {
            isLoading = true;
            nextPageConnection.startAsynchronous();
        }

    }


    public void refresh(){
        refresh(false);
    }
    public void refreshNoMerge(){
        refresh(false);
    }

    public void refresh(boolean shoudMerge){

        cancelRefreashing();
        HashMap<String ,Object> params = (HashMap<String, Object>) apiParams.clone();

        params.put(stepName,Validate.stringFromInt(step));
        params.put(pageName,Validate.stringFromInt(1));


        if (shoudMerge == false) {
            setCurrentPage(1);
        }

        refreashConnection = ApiBase.sharedInstance().getConnectionWithApiMethod(apiMethod, params);
//        refreashConnection.addCache = false;

        this.shouldMerge = shoudMerge;

        refreashConnection.setCallbacks(new ApiConnection.ApiConnectionCallback() {

            @Override
            public void onSuccessJson(JsonElement result) {
                super.onSuccessJson(result);
                refreshRequestDidSuccess(result);
                System.out.println("刷新onSuccessJson了");
            }

            @Override
            public void onFailed(ApiError error) {
                super.onFailed(error);
                if (delegate!=null){
                    delegate.onRefreshFailed(error);
                }
                System.out.println("刷新onFailed了"+error.errorMeesage);
                System.out.println("刷新onFailed了"+error.errorDomain);
                System.out.println("刷新onFailed了"+error.errorCode);


            }

            @Override
            public void onFinal() {
                super.onFinal();
                isRefreashing = false;
                System.out.println("刷新onFinal了");
            }
        });

//        [_refreashConnection loadFromCache];
        boolean validatePass = true;
        if(delegate!=null){
            validatePass = delegate.beforeRefreshRequest(refreashConnection);
        }
        if (validatePass) {
            isRefreashing = true;
            refreashConnection.startAsynchronous();
        }
    }

    public void clearList(){
        list.clear();
    }

    public void cancelLoading(){
        if (isLoading) {

            nextPageConnection.cancel(true);
            isLoading = false;

        }
    }
    public void cancelRefreashing(){
        if (isRefreashing) {
            refreashConnection.cancel(true);
            isRefreashing = false;
        }
    }

    public void refreshRequestDidSuccess(JsonElement result){
        if (getPageinfoAdapter() == null){
            setPageinfoAdapter(getDefaultPageAdapter());
        }

        PageInfoAdapterInterface.PageInfo pageInfo = getPageinfoAdapter().adapter(result);

        if (pageInfo.isSuccess) {
            List newlistElement = pageInfo.listData;
            totalPage = pageInfo.totalPage;
            if (shouldMerge) {
                mergeArray(list,newlistElement,keyName,MergeDirection.Front);

            }else{
                list = newlistElement;
            }
//            if (_refreashConnection.isCacheLoading) {
//                if (_onCacheRefreashBlock) {
//                    _onCacheRefreashBlock(_list,result);
//                }
//            }
//            else
//            {
                if (delegate!=null) {
                    delegate.onRefreshSuccess(list, result);
                }
//                _refreashConnection.addCache = true;
//            }
        }else{
            ApiError error = new ApiError();
            error.errorDomain = PageControllerErrorDomain;
            error.errorCode = 001;
            error.errorMeesage = Validate.stringEmptyIfNot(pageInfo.errorMessage);

            if (delegate!=null) {
                delegate.onRefreshFailed(error);
            }

        }
    }
    public void nextRequestDidSuccess(JsonElement result){
        if (getPageinfoAdapter() == null){
            setPageinfoAdapter(getDefaultPageAdapter());
        }

        PageInfoAdapterInterface.PageInfo pageInfo = getPageinfoAdapter().adapter(result);


        if (pageInfo.isSuccess) {
            List newListElement = pageInfo.listData;
            totalPage = pageInfo.totalPage;
            mergeArray(list,newListElement,keyName,MergeDirection.Tail);

//            if (_nextPageConnection.isCacheLoading) {
//                if (_onCacheNextBlock) {
//                    _onCacheNextBlock(_list,result);
//                }
//            }else{
//                if (_onNextSuccessBlock) {
//                    _onNextSuccessBlock(_list,result);
//                }
//            }
            if (delegate!=null) {
                delegate.onNextSuccess(list, result);
            }
        }else{
            ApiError error = new ApiError();
            error.errorDomain = PageControllerErrorDomain;
            error.errorCode = 001;
            error.errorMeesage = Validate.stringEmptyIfNot(pageInfo.errorMessage);

            if (delegate!=null) {
                delegate.onNextFailed(error);
            }
        }

    }
    private void mergeArray(List<JsonElement> destination ,List<JsonElement> src ,String keyName ,MergeDirection direction){
        switch (direction) {
            case Front:{

                List<JsonElement> source = new ArrayList<JsonElement>(src);
                int count = source.size();
                int[] shouldRemoveIndexs = new int[count];
                int shouldRemoveIndexsCount = 0;

                for(int i = 0 ; i < count ;i ++){
                    JsonObject data = source.get(i).getAsJsonObject();
                    String key = GsonValidate.getStringByKeyPath(data, keyName , null);
                    if (key != null){
                        int index = 0;
                        index = indexOfObject(data,keyName,destination);
                        if (index >= 0) {
                            destination.set(index , data);
                            shouldRemoveIndexs[shouldRemoveIndexsCount] = i;
                            shouldRemoveIndexsCount++;
                        }
                    }
                }
                for (int i = shouldRemoveIndexsCount -1 ; i >=0; i--) {
                    source.remove(shouldRemoveIndexs[i]);
                }

                source.addAll(destination);
                destination.clear();
                destination.addAll(destination);

                break;
            }
            case Tail:{
                List<JsonElement> source = new ArrayList<JsonElement>(src);
                int count = source.size();
                int[] shouldRemoveIndexs =  new int[count];
                int shouldRemoveIndexsCount = 0;
                for(int i = 0 ; i < count ;i ++){
                    JsonObject data = source.get(i).getAsJsonObject();

                    String key = GsonValidate.getStringByKeyPath(data, keyName ,null);
                    if (key != null) {
                        int index = 0;
                        index = indexOfObject(data,keyName,destination);
                        if (index >= 0) {
                            destination.set(index , data);
                            shouldRemoveIndexs[shouldRemoveIndexsCount] = i;
                            shouldRemoveIndexsCount++;
                        }
                    }
                }
                for (int i = shouldRemoveIndexsCount -1 ; i >=0; i--) {
                    source.remove(shouldRemoveIndexs[i]);
                }

                destination.addAll(source);
                break;
            }
            default:
                break;
        }
    }
    public PageInfoAdapterInterface getDefaultPageAdapter(){
        return new PageInfoAdapterInterface() {
            @Override
            public PageInfo adapter(JsonElement input) {
                PageInfo pageInfo = new PageInfo();
                try {
                    JsonObject result = input.getAsJsonObject();
                    if ( result!=null ){
                        if(result.get(successKey).getAsBoolean()){
                            JsonObject pageInfoObjecy =result.get(pageInfoKey).getAsJsonObject();
                            JsonArray  resultList     = result.get(listKey).getAsJsonArray();

                            pageInfo.totalPage  = pageInfoObjecy.get(pageInfoTotalKey).getAsInt();

                            pageInfo.listData   = EasyGson.jsonArrayToList(resultList);
                            pageInfo.isSuccess = true;
                        }else {
                            pageInfo.isSuccess = false;
                            pageInfo.errorMessage = result.get(messageKey).getAsString();
                        }
                    }

                }catch (Exception e){
                    pageInfo.isSuccess = false;
                    pageInfo.errorMessage = "网络访问类型不为DH标准的分页格式";
                }
                return pageInfo;
            }
        };
    }
    public interface PageControllerDelegate{

        public boolean beforeNextPageRequest(ApiConnection nextPageConnection);
        public boolean beforeRefreshRequest(ApiConnection refreshConnection);


        public abstract void onRefreshSuccess(List list, JsonElement result);
        public abstract void onNextSuccess(List list, JsonElement result);

        public void onRefreshFailed(ApiError error);
        public void onNextFailed(ApiError error);
    }

    public int indexOfObject(JsonObject object ,String key ,List<JsonElement> src){
        if (!object.isJsonObject()) {
            return  -1;
        }
        String objectId = GsonValidate.getStringByKeyPath(object, key ,null);
        if (objectId == null){
            return -1;
        }
        for (int i =  0; i<src.size(); i ++ ) {
            JsonElement jsonElement = src.get(i);
            if (jsonElement.isJsonObject()) {
                String keyId = GsonValidate.getStringByKeyPath(jsonElement , key , null);
                if (keyId .equals(objectId)) {
                    return i;
                }
            }
        }
        return  -1;
    }



    public boolean isBusy(){
        return isLoading||isRefreashing;
    }

    public boolean hasMore(){
        return getCurrentPage()< totalPage;
    }


    /**
     * setter and getter
     * @return
     */
    public PageControllerDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(PageControllerDelegate delegate) {
        this.delegate = delegate;
    }

    public boolean isShouldMerge() {
        return shouldMerge;
    }

    public void setShouldMerge(boolean shouldMerge) {
        this.shouldMerge = shouldMerge;
    }

    public ApiMethodDescription getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(ApiMethodDescription apiMethod) {
        this.apiMethod = apiMethod;
    }

    public HashMap<String, Object> getApiParams() {
        return apiParams;
    }

    public void setApiParams(HashMap<String, Object> apiParams) {
        this.apiParams = apiParams;
    }



    public List getList() {
        return list;
    }


	public PageInfoAdapterInterface getPageinfoAdapter() {
		return pageinfoAdapter;
	}


	public void setPageinfoAdapter(PageInfoAdapterInterface pageinfoAdapter) {
		this.pageinfoAdapter = pageinfoAdapter;
	}


	public int getCurrentPage() {
		return currentPage;
	}


	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
/*


    @implementation LSPageController (DefaultSetting)
    + (void)setDefaultStepName:(String )stepName{
        kLSPageControllerStepName = stepName;
    }
    + (void)setDefaultPageName:(String )pageName{
        kLSPageControllerStepName = pageName;
    }
    + (void)setDefaultStep:(int)step{
        kLSPageControllerStep    = step;
    }

    + (void)setDefaultListKey:(String )listKey{
        kLSPageControllerListKey = listKey;
    }
    + (void)setDefaultPageInfoKey:(String )pageinfokey{
        kLSPageControllerPageInfoKey = pageinfokey;
    }
    + (void)setDefaultPageInfoTotalKey:(String )totalkey{
        kLSPageControllerPageInfoTotalKey = totalkey;
    }
    @end
}
*/