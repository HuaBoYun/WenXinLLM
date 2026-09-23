package com.huabo.system.utils;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

public class WebServiceClient {

	public static String getLeafVal(Document xmlDoc, String xpathExpress) throws Exception {
		List<String> resultList = XmlManager.readValFromDoc(xmlDoc,xpathExpress);
		return resultList != null && resultList.size() > 0 ?(String)resultList.get(0): null;
	}

	public static List<Map<String, String>> getLeafs(Document doc, String xpathExpress) throws Exception {
		return XmlManager.readValsFromDoc(doc,xpathExpress);
	}
	
}
