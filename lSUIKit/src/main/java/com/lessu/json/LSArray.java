package com.lessu.json;

import com.google.gson.JsonArray;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by lessu on 14-6-27.
 */
public class LSArray extends LSObject implements List {
    JsonArray jsonArray;
    LSArray(String jsonString) {
        super(jsonString);
        jsonArray = jsonElement.getAsJsonArray();
    }


    @Override
    public LSObject copy() {
        return null;
    }

    @Override
    public void add(int i, Object o) {

    }

    @Override
    public boolean add(Object o) {
        if(o instanceof LSObject){
            LSObject object = (LSObject) o;
            jsonArray.add(object.jsonElement);
        }else if (o instanceof String){
            LSString string = new LSString((String) o);
            jsonArray.add(string.jsonElement);
        }else if (objectIsNumber(o)){
            LSNumber number = new LSNumber(o);
            jsonArray.add(number.jsonPrimitive);
        }else {
            LSString string = new LSString(o.toString());
            jsonArray.add(string.jsonElement);
        }
        return false;
    }

    @Override
    public boolean addAll(int i, Collection collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection collection) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Object get(int i) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator listIterator() {
        return null;
    }

    @Override
    public ListIterator listIterator(int i) {
        return null;
    }

    @Override
    public Object remove(int i) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public Object set(int i, Object o) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public List subList(int i, int i2) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public Object[] toArray(Object[] objects) {
        return new Object[0];
    }

    @Override
    public boolean retainAll(Collection collection) {
        return false;
    }

    @Override
    public boolean removeAll(Collection collection) {
        return false;
    }

    @Override
    public boolean containsAll(Collection collection) {
        return false;
    }
}
