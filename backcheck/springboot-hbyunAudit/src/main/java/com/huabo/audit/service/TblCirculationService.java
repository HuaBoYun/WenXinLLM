package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.Map;

import com.huabo.audit.oracle.entity.TblCirculation;

public interface TblCirculationService {
	TblCirculation getOneBytaskid(String taskId);

    TblCirculation saveTblCirculationnew(String cytype, String cycode, String cyname, String cyurl, BigDecimal cystaffid, String buskey, String definitionId, String taskid);

//    Map<String, Object> findeByTaskId(String taskId, String contractId, String flowId, String token);

//    Map<String, Object> findByLendid(String lendid, String token);
    TblCirculation get(String cyid);

    void upateTblCirculation(TblCirculation tblCirculation);

//	TblCirculationMySql saveMySqlTblCirculationnew(String cyhwTypeJjhtjc, String contractno, String contractname,
//			String string, BigDecimal staffid, String processInstanceId, String processDefinitionKey, String string2);

//	TblCirculationMySql getMySql(String cyid);

//	void upateMySqlTblCirculation(TblCirculationMySql tblCirculation);

//	TblCirculationMySql getOneByMySqltaskid(String taskId);
}
