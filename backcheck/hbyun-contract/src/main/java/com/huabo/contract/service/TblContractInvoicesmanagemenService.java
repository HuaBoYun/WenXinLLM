package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.Map;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblContractInvoicesmanagemen;
import com.huabo.contract.entity.TblContractPayment;

public interface TblContractInvoicesmanagemenService {

	Map<String, Object> findInvoiceInfoListForCollection(Integer pageNumber, Integer pageSize, BigDecimal budgetId,
			TblContractInvoicesmanagemen invoice) throws Exception;

	void findContractInvociesManaeMenPageInfo(PageInfo<TblContractInvoicesmanagemen> pageInfo,
			TblContractInvoicesmanagemen invoice) throws Exception;

	String mengerInvoicemanageMen(BigDecimal budgetId, String startdate1, String enddate1,
			TblContractInvoicesmanagemen invoice) throws Exception;

	void modifyInvoiceStatus(BigDecimal invoiceId, Integer status) throws Exception;

	void removeInvoiceInfo(BigDecimal invoiceId) throws Exception;

	TblContractInvoicesmanagemen findInvoiceInfoByInvoiceId(BigDecimal invoiceId) throws Exception;

}
