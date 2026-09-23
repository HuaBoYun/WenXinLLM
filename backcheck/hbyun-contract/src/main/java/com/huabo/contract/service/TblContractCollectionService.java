package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.huabo.contract.entity.TblContractCollection;

import net.sf.json.JSONArray;

public interface TblContractCollectionService {

	Map<String, Object> findCollectionListByPageInfo(String contractname, String contractno,
			TblContractCollection collection, Integer pageNumber, Integer pageSize) throws Exception;

	String mengerCollectionEntity(BigDecimal nodeid, BigDecimal contractid, BigDecimal invoiceid,
			BigDecimal invoicemoney, BigDecimal bankbankid, BigDecimal bankid, TblContractCollection collection) throws Exception;

	void removeContractCollection(BigDecimal collectionId) throws Exception;

	TblContractCollection findCollectionInfoByCollectId(BigDecimal collectionId) throws Exception;

	List<String> getSkContractIds();
	
	String setData(JSONArray arr);
   
}
