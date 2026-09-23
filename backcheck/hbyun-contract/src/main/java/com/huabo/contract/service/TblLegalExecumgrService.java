package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.Map;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalExecumgr;

public interface TblLegalExecumgrService {
	void findListByPage(PageInfo<TblLegalExecumgr> pageInfo, BigDecimal litigationid,BigDecimal arbitraid,TblLegalExecumgr tla);
	
	void addLegalExecumgr(TblLegalExecumgr tla);
	
	TblLegalExecumgr getLegalExecumgrById(BigDecimal id);
	
	void deleteLegalExecumgrById(BigDecimal id);
	
	void findListBydisputeid( BigDecimal disputeid,Map<String,Object> resultMap);
}
