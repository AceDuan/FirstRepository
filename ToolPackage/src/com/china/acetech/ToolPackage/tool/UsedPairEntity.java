package com.china.acetech.ToolPackage.tool;

import java.util.ArrayList;
import java.util.List;

public class UsedPairEntity extends MyPairEntity<String, String>{

	
	
	public String[] getKeyArray(){
		String[] array = new String[mList.size()];
		int i = 0;
		for ( Element ele : mList ){
			array[i] = ele.key;
			i++;
		}
		return array;
	}
	
	public String getKeyByValue(String value){
		String res = "";
		for ( Element ele : mList ){
			if ( ele.value.equals(value)){
				res = ele.key;
				break;
			}
		}
		
		return res;
	}
	
	public String removeItemByValue(String value){
		String res = "";
		for ( int i =0; i < mList.size(); i++ ){
			Element ele = mList.get(i);
			if ( ele.value.equals(value)){
				res = ele.value;
				mList.remove(i);
				break;
			}
		}
		
		return res;
	}
	
	public String getValue(String key){
		String res = "";
		for ( Element ele : mList ){
			if ( ele.key.equals(key)){
				res = ele.value;
				break;
			}
		}
		
		return res;
	}
	
	public String getValue(int position){
		return mList.get(position).value;
	}
	
	public int getValuePosition(String value){
		int ret = -1;
		int i = 0;
		for ( Element ele : mList ){
			if ( ele.value.equals(value)){
				ret = i;
				break;
			}
			i++;
		}
		
		return ret;
	}
	
	
}

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
