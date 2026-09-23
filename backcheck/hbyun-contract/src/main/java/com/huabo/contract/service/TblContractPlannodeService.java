package com.huabo.contract.service;

import com.hbfk.entity.TblAttachment;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblContractPlannode;
import com.huabo.contract.entity.TblCyhwUnit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TblContractPlannodeService {

	void findPlanNodeListForCollection(PageInfo<TblContractPlannode> pageInfo, TblContractPlannode node) throws Exception;

	void findPlanNodeListForPayment(PageInfo<TblContractPlannode> pageInfo, TblContractPlannode node) throws Exception;

	Map<String, Object> saveContractPlannode(TblContractPlannode node, BigDecimal contractid,BigDecimal jbunitid, BigDecimal jbstaffid);

	Map<String, Object> selectById(BigDecimal nodeId);

	Map<String, Object> removeContractPlannode(BigDecimal nodeId);

	Map<String, Object> sendTipEmail(TblCyhwUnit tcu, String content, String date, String token, String staffId) throws Exception;

	Map<String, Object> findPlannodeListById(BigDecimal contractId);

	Map<String, Object> findWorkableContractNodeByPageInfo(BigDecimal contractId, Integer pageNumber, Integer pageSize, String token,TblContractPlannode node);

	TblContractPlannode findWriteContractPlanNode(BigDecimal planId);

	List<TblAttachment> findeWriteContractPlanFileInfo(BigDecimal planId);

	Map<String, Object> modifyPlanNodeStatus(BigDecimal nodeId, Integer planStatus, String feedback);

	Map<String, Object> findPlannodeListByContractId(BigDecimal contractId);

}
