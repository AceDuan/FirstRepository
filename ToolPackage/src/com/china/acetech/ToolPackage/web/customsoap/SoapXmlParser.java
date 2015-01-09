package com.china.acetech.ToolPackage.web.customsoap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
	
	public static String GetElementsValueByTagName(String TagName, Element element)
	{
		String RetValue = "";
		RetValue = element.getElementsByTagName(TagName).item(0).getFirstChild().getNodeValue();
		
		return RetValue;
	}
}
