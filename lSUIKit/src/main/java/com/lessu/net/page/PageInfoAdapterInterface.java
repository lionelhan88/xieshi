package com.lessu.net.page;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.List;

/**
 * Created by lessu on 14-6-26.
 */
public interface PageInfoAdapterInterface {
    public static class PageInfo{
        public boolean isSuccess;
        public List<JsonElement> listData;
        public int totalPage;
        public String errorMessage;
    }

    public PageInfo adapter(JsonElement input);
}
