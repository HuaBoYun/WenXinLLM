package com.huabo.system.service;



import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblSystemImportLog;
import com.huabo.system.entity.TblSystemUrgentEvents;

public interface TblSystemBaseInfoService {

	JsonBean getAutoNumber(String token, String configId) throws Exception;

	JsonBean saveUrgentEvent(String token, TblSystemUrgentEvents events) throws Exception;

	JsonBean modifyUrgentEvent(String token, TblSystemUrgentEvents events) throws Exception;

	JsonBean getUrgentEvent(String token, String eventId) throws Exception;

	JsonBean recipientList(String token, TblSystemUrgentEvents events, Integer pageNumber, Integer pageSize) throws Exception;

	JsonBean initiatiorUrgentList(String token, TblSystemUrgentEvents events, Integer pageNumber, Integer pageSize) throws Exception;

	JsonBean loginRecipientList(String token) throws Exception;

	JsonBean getFtpInfo(String url, Integer port, String userName, String userp) throws Exception;

	JsonBean getRefreshInfo() throws Exception;

	JsonBean getPendingProcessingAllNum() throws Exception;

}
