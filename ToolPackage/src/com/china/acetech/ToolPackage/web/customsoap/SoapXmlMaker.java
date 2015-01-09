package com.china.acetech.ToolPackage.web.customsoap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoapXmlMaker {

	Map<String, String> namespace;
	
	public static final String defaultName = "http://schemas.xmlsoap.org/soap/envelope/";
	public static final String defaultPreix = "soapenv";
	
	public static final String AnotherName = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String BXCName = "http://wristband.besta.com";
	public static final String BXCPreix = "ns";
	public SoapXmlMaker(){
		namespace = new HashMap<String, String>();
		
		namespace.put(defaultName, defaultPreix);
		namespace.put(BXCName, BXCPreix);
	}
	
	
	public void addNewNameSapce(String preix, String NameSpace){
		namespace.put(NameSpace, preix);
	}
	
	public String createXML(String method, List<MySoapObject> itemList){
		MySoapObject root = new MySoapObject(defaultName, "Envelope");
		root.addAttribute(AttributeMaker(defaultPreix, defaultName));
		root.addAttribute(AttributeMaker(BXCPreix, BXCName));
		
		root.addItem(new MySoapObject(defaultName, "Header"));
		
		MySoapObject body = new MySoapObject(defaultName, "Body");
		root.addItem(body);
		
		MySoapObject method_soap = new MySoapObject(BXCName, method);
		for( MySoapObject object : itemList ){
			method_soap.addItem(object);
		}
		body.addItem(method_soap);
		
		String res = root.getXMLString(namespace);
		//System.out.println(res);
		return res;
	}
	
	
	private String AttributeMaker(String preix, String nameSpace){
		return "xmlns:" + preix + "=" + "\"" + nameSpace + "\"";
	}
}
