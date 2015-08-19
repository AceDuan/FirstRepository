package com.china.acetech.ToolPackage.web.customsoap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.china.acetech.ToolPackage.debug.DebugTool;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;



public class SoapXmlParser {

	public Node Parser(String xml){
		
//		try {
//			OMElement element;
//			element = new StAXOMBuilder(new ByteArrayInputStream(xml.getBytes("UTF-8"))).getDocumentElement();
//			System.out.println(element.toString());
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (XMLStreamException e) {
//			e.printStackTrace();
//		}
		
		DocumentBuilderFactory docfactory = null;
		DocumentBuilder builder = null;
		Document document = null;
		
		docfactory = DocumentBuilderFactory.newInstance();
		try {
			builder = docfactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		try {
			document = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//System.out.println(xml);
		Element root = document.getDocumentElement();
		//System.out.println(root.getNodeName());
		Node resultNode; 
		resultNode = root.getElementsByTagName("ns:return").item(0);

		return resultNode;
		
	}
	
	public Element ParserForRoot(String xml){
		
//		try {
//			OMElement element;
//			element = new StAXOMBuilder(new ByteArrayInputStream(xml.getBytes("UTF-8"))).getDocumentElement();
//			System.out.println(element.toString());
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (XMLStreamException e) {
//			e.printStackTrace();
//		}
		
		DocumentBuilderFactory docfactory = null;
		DocumentBuilder builder = null;
		Document document = null;
		
		docfactory = DocumentBuilderFactory.newInstance();
		try {
			builder = docfactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		try {
			document = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//System.out.println(xml);
		Element root = null;
		if ( document != null )
			root = document.getDocumentElement();
		//System.out.println(root.getNodeName());

		return root;
		
	}
	
	public static String GetElementsValueByTagName(String TagName, Element element)
	{
		String RetValue;
		RetValue = element.getElementsByTagName(TagName).item(0).getFirstChild().getNodeValue();
		
		return RetValue;
	}
	
	/**
	 * @param tagNameWithPath  e.g.  /Besta/SYConst/Row must start with"/"
	 * @return null if the path is error
	 */
	public static String getDeepElementsValueByTagName(String tagNameWithPath, Element element){
		String RetValue;
		try{
			tagNameWithPath = tagNameWithPath.substring(1);
			String[] array = tagNameWithPath.split("/");
			
			Element temp = element;
			for ( String name : array ){
				temp = (Element)temp.getElementsByTagName(name).item(0);
			}
			RetValue = temp.getFirstChild().getNodeValue();
		}
		catch (Exception e){
			//出現異常返回空字串，代替空指針
			DebugTool.show("Element value read error");
			RetValue = "";
		}
		
		return RetValue;
	}
	
	public static Element getDeepElement(String tagNameWithPath, Element element){
		Element RetValue = null;
		try{
			tagNameWithPath = tagNameWithPath.substring(1);
			String[] array = tagNameWithPath.split("/");
			
			Element temp = element;
			for ( String name : array ){
				temp = (Element)temp.getElementsByTagName(name).item(0);
			}
			RetValue = temp;
		}
		catch (Exception e){}
		
		return RetValue;
	}
	
	public static String getDeepElementsValueByTagNameAndAttribute(String tagNameWithPath, String attribute, Element element){
		String RetValue = null;
		try{
			tagNameWithPath = tagNameWithPath.substring(1);
			String[] array = tagNameWithPath.split("/");
			
			Element temp = element;
			int length = array.length;
			for ( int i =0; i < length-1; i++ ){
				String name = array[i];
				temp = (Element)temp.getElementsByTagName(name).item(0);
			}
			RetValue = temp.getFirstChild().getNodeValue();
		}
		catch (Exception e){
			System.out.println("Exception");
		}
		
		return RetValue;
	}
}
