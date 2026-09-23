package com.huabo.fxgl.service;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.TblNbkzRiskEntity;
import com.huabo.fxgl.entity.TblNbsjRisktolerability;
import com.huabo.fxgl.vo.TblNbkzRiskVo;

public interface TblNbkzRiskService {
	
	//==
	JsonBean riskPageList(String token, Integer pageNumber, Integer pageSize,TblNbkzRiskVo tblNbkzRiskVo,Integer orgid) throws Exception;
	
	JsonBean riskAdd(TblNbkzRiskEntity risk, String token,String attids)throws Exception;
    
    JsonBean riskDelete(Integer riskid, String token) throws Exception;
    
    JsonBean findRiskDetail(String token, Integer riskid) throws Exception;

	/**
	 * 风险发现-附件列表
	 * @param token
	 * @param riskId
	 * @return
	 */
	JsonBean risk_file_list(String token,Integer riskId) throws Exception;

	//==
	JsonBean fxrrdAdd(String token, TblNbsjRisktolerability tnr) throws Exception;

	JsonBean fxrrdDelete(Integer toleid, String token) throws Exception;

	JsonBean fxrrdLinkList(String token, Integer pageNumber, Integer pageSize,Integer riskid) throws Exception;
	
	
	void removeAttInfoByAttId(String token, String attId) throws Exception;
	
}
