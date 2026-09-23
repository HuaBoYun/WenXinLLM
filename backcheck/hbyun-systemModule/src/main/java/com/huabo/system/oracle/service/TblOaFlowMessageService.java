package com.huabo.system.oracle.service;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblOaFlowMessage;

public interface TblOaFlowMessageService {

	void saveOrUpdateEntity(TblOaFlowMessage tblOaFlowMessage) throws Exception;

	String selectFlowTypeById(String id) throws Exception;

	TblOaFlowMessage findEntityById(String id) throws Exception;

	void modifyFlowMessage(TblOaFlowMessage message) throws Exception;

	void setPageInfoList(PageInfo<TblOaFlowMessage> pageInfo) throws Exception;

}
