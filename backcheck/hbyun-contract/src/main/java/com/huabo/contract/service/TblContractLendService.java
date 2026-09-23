package com.huabo.contract.service;

import java.util.Map;

import com.huabo.contract.entity.TblContractLend;


public interface TblContractLendService {


	Map<String, Object> saveTblContractLead(String contractId, String lenddate,String returndate ,String token, String staffId, TblContractLend lend) throws Exception;

    Map<String, Object> findByContractId(Integer pageNumber, Integer pageSize, String contractId, String token, String staffId);


    TblContractLend findById(String lendid);

	void saveTblContractLeadEntity(TblContractLend aw);

	String getLendInfo(String token, String lendId) throws Exception;
}
