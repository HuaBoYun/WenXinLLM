package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.Map;

import com.huabo.contract.entity.TblContractPayment;
import com.huabo.contract.entity.TblCyhwUnit;

import net.sf.json.JSONArray;

public interface TblContractPaymenService {

	Map<String, Object> findPaymentManagemenByPageInfo(Integer pageNumber, Integer pageSize, TblContractPayment payment,
			TblCyhwUnit unit) throws Exception;

	String mengerPaymentInfo(TblContractPayment payment, BigDecimal invoiceid, BigDecimal applyStaffId) throws Exception;

	void removePaymentInfo(BigDecimal paymentId) throws Exception;

	TblContractPayment findPaymentInfoByParmentId(BigDecimal paymentId) throws Exception;

	String setData(JSONArray arr,String type);

	Map<String, Object> setUpdatePayment(String token,String contract,String payAmount,String payDate,String mark,String bankName,String bank,String accountName,String accountNo,String paybank,String payaccountName,String payaccountNo,String abstracts);

	Map<String, Object> setCustomers(JSONArray inParametersArr);



}
