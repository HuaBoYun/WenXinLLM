package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjInnerrule;
import com.huabo.audit.oracle.vo.TblNbsjInnerRuleVo;
import com.huabo.audit.util.R;

public interface TblNbsjInnerRuleService {

	JsonBean selectInnerrulePageInfo(String token, Integer pageNumber, Integer pageSize,TblNbsjInnerRuleVo tblNbsjInnerRuleVo) throws Exception ;

	public JsonBean mergeInnerruleInfo(TblNbsjInnerrule tblNbsjInnerrule, String token, String attIds,String pulishDate)  throws Exception ;

	JsonBean selectInfo(String innerid) throws Exception;

	JsonBean deleteInfo(String innerid) throws Exception ;
	
	public TblNbsjInnerrule getInfo(String innerid) throws Exception;
	
	R removeAttInfoByAttId(String token, String attId) throws Exception;

}
