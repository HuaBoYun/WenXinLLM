package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblComplianceWeekly;

public interface TblComplianceSendService {
	
    
    JsonBean complianceWeenlyList(String token, Integer pageNumber, Integer pageSize,TblComplianceWeekly tblComplianceWeekly) throws Exception;
    
    JsonBean complianceWeenlySave(TblComplianceWeekly tblComplianceWeekly, String token)throws Exception;
    
    JsonBean complianceWeenlyDel(String id, String token) throws Exception;
    
    JsonBean complianceWeenlyDetail(String token, String id) throws Exception;
    
}
