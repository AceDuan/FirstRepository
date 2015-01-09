package com.china.acetech.ToolPackage.java;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BXC2010011 on 2015/1/6.
 * 基本的Map表的元素，仅提供基本操作
 */
class MyPairEntity<Key, Value> {
    public MyPairEntity() {
        mList = new ArrayList<Element>();
    }

    public void put(Key key, Value value){
        Element ele = new Element();
        ele.key = key;
        ele.value = value;
        mList.add(ele);
    }

    protected List<Element> mList;

    protected class Element {
        Key key;
        Value value;
    }
}
