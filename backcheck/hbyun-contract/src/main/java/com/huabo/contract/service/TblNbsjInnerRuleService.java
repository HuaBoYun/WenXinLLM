package com.huabo.contract.service;

import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.TblNbsjInnerrule;
import com.huabo.contract.vo.Result;
import com.huabo.contract.vo.TblNbsjInnerRuleVo;

public interface TblNbsjInnerRuleService {

	JsonBean selectInnerrulePageInfo(String token, Integer pageNumber, Integer pageSize,TblNbsjInnerRuleVo tblNbsjInnerRuleVo) throws Exception ;

	public JsonBean mergeInnerruleInfo(TblNbsjInnerrule tblNbsjInnerrule, String token, String attIds,String pulishDate)  throws Exception ;

	JsonBean selectInfo(String innerid) throws Exception;

	JsonBean deleteInfo(String innerid) throws Exception ;
	
	public TblNbsjInnerrule getInfo(String innerid) throws Exception;
	
	Result removeAttInfoByAttId(String token, String attId) throws Exception;

	JsonBean getAutoCodeByHtzd(String token)throws Exception;
	
}
