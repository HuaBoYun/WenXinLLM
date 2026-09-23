package com.huabo.system.service;

import com.hbfk.util.JsonBean;

public interface TblSystemLanguageService {

	JsonBean conversionGetList(String token, Integer pageNumber, Integer pageSize, String infoname,
			String targetlanguage) throws Exception;

	JsonBean conversionmodify(String token, String infoname, String targetlanguage, String infoid) throws Exception;

	JsonBean getTranslateList(String token, String infoid, Integer pageNumber, Integer pageSize, String trantext,
			String menuname) throws Exception;

	JsonBean translateMenger(String token, String infoid, String configid, String trantext, String menuname) throws Exception;

	JsonBean translateClearUp(String token, String infoid, String configid) throws Exception;

	JsonBean translateRemove(String token, String infoid, String configid) throws Exception;

	JsonBean translateGetUserConfig(String token, String infoid) throws Exception;

	JsonBean conversiondetial(String token, String infoid) throws Exception;

	JsonBean conversionremove(String token, String infoid) throws Exception;


}
