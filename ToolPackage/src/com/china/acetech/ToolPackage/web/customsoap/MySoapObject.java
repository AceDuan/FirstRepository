package com.china.acetech.ToolPackage.web.customsoap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MySoapObject{
	private List<MySoapObject> childList;
	private List<String> attributeList;
	
	private String mNamesSpace;
	private String mLabelName;
	private String mValue;
	
	public MySoapObject() {
		childList = new ArrayList<MySoapObject>();
		attributeList = new ArrayList<String>();
	}
	
	public MySoapObject(String nameSpace, String labelName){
		this();
		mNamesSpace = nameSpace;
		mLabelName = labelName;
	}
	
	public void addItem(MySoapObject item){
		childList.add(item);
	}
	
	public void removeAllItem(){
		childList.clear();
	}
	
	public void addAttribute(String attribute){
		attributeList.add(attribute);
	}
	
	public List<MySoapObject> getAllItem(){
		return childList;
	}
	
	public void setValue(String value){
		mValue = value;
	}
	
	public String getXMLString(Map<String, String> nameSpaceMap){
		StringBuffer buffer = new StringBuffer();
		
		String preix = nameSpaceMap.get(mNamesSpace);
		if ( preix != null ){
			preix = preix + ":";
		}
		else{
			preix = "";
		}
		
		buffer.append(getStartLabel(preix));
		if (childList.size() != 0) {

			buffer.append("\n");
			for (MySoapObject object : childList) {
				buffer.append(object.getXMLString(nameSpaceMap));
			}
		}
		else{
			if ( mValue != null )
				buffer.append(mValue);
		}
		buffer.append(getEndLabel(preix));
		buffer.append("\n");
		return buffer.toString();
	}
	
	private String getStartLabel(String preix){
		String attribute = "";
		if ( attributeList.size() != 0 ){
			StringBuffer buffer = new StringBuffer();
			for ( String str : attributeList ){
				buffer.append(" ");
				buffer.append(str);
			}
			attribute = buffer.toString();
		}
		return "<" + preix + mLabelName + attribute + ">";
	}
	
	private String getEndLabel(String preix){
		return "</" + preix + mLabelName + ">";
	}
}