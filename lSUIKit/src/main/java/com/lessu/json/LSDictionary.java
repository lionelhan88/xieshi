package com.lessu.json;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lessu on 14-6-27.
 */
public class LSDictionary extends LSObject implements Map<String,Object>{

    LSDictionary(String jsonString) {
        super(jsonString);
    }

    @Override
    public LSObject copy() {
        return new LSDictionary(this.toString());
    }


    @Override
    public void clear() {

    }

    @Override
    public boolean containsKey(Object o) {
        return false;
    }

    @Override
    public boolean containsValue(Object o) {
        return false;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return null;
    }

    @Override
    public Object get(Object o) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Object put(String s, Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ?> map) {

    }

    @Override
    public Object remove(Object o) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Collection<Object> values() {
        return null;
    }
}
