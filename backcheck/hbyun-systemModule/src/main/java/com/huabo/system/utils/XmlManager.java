package com.huabo.system.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlManager {
	public static final SimpleDateFormat webserviceSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
	
	private static TransformerFactory factory = TransformerFactory.newInstance();

	public static Document string2Dom(String source) throws Exception {
		if(StringUtils.isBlank(source) || !source.startsWith("<?xml") && !source.startsWith("<soap")) {
			throw new RuntimeException("返回非XML格式文本："+source);
		}else {
			Document xmldoc = null;
			StringReader sr = new StringReader(source);
			InputSource is = new InputSource(sr);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setIgnoringElementContentWhitespace(true);
			
			DocumentBuilder db = dbf.newDocumentBuilder();
			xmldoc = db.parse(is);
			return xmldoc;
		}
	}

	public static String dom2String(Document document) throws Exception {
		String result = null;
		if(document != null) {
			DOMSource source = new DOMSource(document);
			StringWriter strWtr = new StringWriter();
			StreamResult strResult = new StreamResult(strWtr);
			
			Transformer t = factory.newTransformer();
			t.setOutputProperty("encoding", "UTF-8");
			t.setOutputProperty("indent", "yes");
			t.setOutputProperty("cdata-section-elements", "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			t.transform(source, strResult);
			
			result = strWtr.getBuffer().toString();
		}
		return result;
	}

	public static String readValFromNode(Object source, String express) throws Exception {
		Node node = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		
		node = (Node) xpath.evaluate(express, source, XPathConstants.NODE);
		
		if(!node.hasChildNodes()) {
			return node.getTextContent();
		}else {
			NodeList nodeList = node.getChildNodes();
			
			for(int j = 0 ; j<nodeList.getLength();++j) {
				if(nodeList.item(j).getNodeType() == 3) {
					return nodeList.item(j).getTextContent();
				}
			}
			return null;
		}
	}

	public static List<String> readValFromDoc(Document xmlDoc, String express) throws Exception {
		Node node = null;
		List<String> list = new ArrayList<String>();
		Element root = xmlDoc.getDocumentElement();
		NodeList nodeList = selectMultiNode(root,express);
		
		for(int i = 0 ; i < nodeList.getLength() ; ++i) {
			node = nodeList.item(i);
			if(!node.hasChildNodes()) {
				list.add(node.getTextContent());
			}else if(isLeafNode(node)) {
				list.add(getLeafNodeValue(node));
			}
		}
		return list;
	}
	
	public static List<Map<String, String>> readValsFromDoc(Document xmlDoc, String express) throws Exception {
		Node node = null;
		Node node1 = null;
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Element root = xmlDoc.getDocumentElement();
		NodeList nodeList = selectMultiNode(root,express);
		
		for(int i = 0; i<nodeList.getLength();++i) {
			Map<String,String> map = new HashMap<String, String>(0);
			node = nodeList.item(i);
			if(node.hasChildNodes()) {
				NodeList nodeList1 = node.getChildNodes();
				for(int j = 0; j< nodeList1.getLength() ; ++j) {
					node1 = nodeList1.item(j);
					if(isLeafNode(node1)) {
						map.put(node1.getNodeName(), getLeafNodeValue(node1));
					}
				}
			}
			result.add(map);
		}
		return result;
	}

	public static String getLeafNodeValue(Node node) {
		String value = node.getFirstChild() == null? "": node.getFirstChild().getNodeValue();
		return value;
	}

	public static NodeList selectMultiNode(Element source, String express) throws Exception {
		NodeList resultSet = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		
		resultSet = (NodeList)xpath.evaluate(express, source, XPathConstants.NODESET);
		return resultSet;
	}
	
	public static boolean isLeafNode(Node node) {
		boolean isLeafNode = true;
		if(node.getNodeType() == 1) {
			NodeList nodeList = node.getChildNodes();
			for(int j = 0 ; j < nodeList.getLength() ; ++j) {
				if(nodeList.item(j).getNodeType() != 3) {
					isLeafNode = false;
				}
			}
		}else {
			isLeafNode = false;
		}
		return isLeafNode;
	}

}
