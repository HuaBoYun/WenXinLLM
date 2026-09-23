package com.huabo.contract.service;

import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblContractSpnode;

import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface TblContractSpnodeService {

	Map<String, Object> findListByXdf(Integer pageNumber, Integer pageSize, String budgetid) throws Exception;

	Map<String, Object> saveOrUpdateContractSpnode(TblContractSpnode spNode, BigDecimal nodeId, BigDecimal contractId);

	JsonBean modifyCompleteSpNode(String token, BigDecimal nodeId, BigDecimal contractId) throws Exception;


    
}
