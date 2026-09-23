package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.Map;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblCounterpartBankinfo;

public interface TblCounterpartBankInfoService {

	Map<String, Object> findAllListByBankInfo(BigDecimal budgetId, Integer pageNumber, Integer pageSize,
			TblCounterpartBankinfo bank) throws Exception;

	Map<String, Object> saveCounterPartBankInfo(BigDecimal budgetId, TblCounterpartBankinfo bank) throws Exception;

	Map<String, Object> removeBank(String bankId) throws Exception;

	Map<String, Object> modifyBankStatus(String bankId, Integer bankstatus) throws Exception;

	Map<String, Object> findListByPageInfo(PageInfo<TblCounterpartBankinfo> pageInfo, TblCounterpartBankinfo bank) throws Exception;
}
