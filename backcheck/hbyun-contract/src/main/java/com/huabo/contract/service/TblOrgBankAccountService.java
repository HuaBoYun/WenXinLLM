package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.Map;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblOrgBankaccount;

public interface TblOrgBankAccountService {

	Map<String, Object> findListByPageInfo(Integer pageNumber, Integer pageSize, TblOrgBankaccount bank) throws Exception;

	TblOrgBankaccount findById(BigDecimal bankId) throws Exception;

	void findListByPageInfoPid(PageInfo<TblOrgBankaccount> pageInfo, BigDecimal pid, TblOrgBankaccount bank) throws Exception;

	TblOrgBankaccount findByBankId(BigDecimal bankid) throws Exception;

	void UpdateModifyBankInfo(TblOrgBankaccount oldEntity) throws Exception;

	void savebankInfo(TblOrgBankaccount bank) throws Exception;

	String removeOrgBankInfo(BigDecimal bankId) throws Exception;
}
