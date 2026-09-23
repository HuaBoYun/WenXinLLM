package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbkzRiskEntity;
import com.huabo.audit.oracle.entity.TblNbsjRisktolerability;
import com.huabo.audit.oracle.vo.TblNbkzRiskVo;
import com.huabo.audit.util.R;

import java.math.BigDecimal;

public interface TblNbkzRiskService {
	
	//==
	JsonBean riskPageList(String token, Integer pageNumber, Integer pageSize, TblNbkzRiskVo tblNbkzRiskVo, BigDecimal orgid) throws Exception;
	
	JsonBean riskAdd(TblNbkzRiskEntity risk, String token,String attids,String rrds)throws Exception;
    
    JsonBean riskDelete(BigDecimal riskid, String token) throws Exception;
    
    JsonBean findRiskDetail(String token, BigDecimal riskid) throws Exception;

	/**
	 * 风险发现-附件列表
	 * @param token
	 * @param riskId
	 * @return
	 */
	JsonBean risk_file_list(String token,BigDecimal riskId) throws Exception;

	//==
	JsonBean fxrrdAdd(String token, TblNbsjRisktolerability tnr) throws Exception;

	JsonBean fxrrdDelete(BigDecimal toleid, String token) throws Exception;

	JsonBean fxrrdLinkList(String token, Integer pageNumber, Integer pageSize,BigDecimal riskid) throws Exception;
	
	
	R removeAttInfoByAttId(String token, String attId) throws Exception;
	
}
