package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjWorkReportEntity;
import com.huabo.audit.oracle.vo.TblNbsjWorkReportVo;
import com.huabo.audit.util.R;

public interface TblNbsjWorkReportService {
	
	JsonBean workReportPageList(String token, Integer pageNumber, Integer pageSize,TblNbsjWorkReportVo tblNbsjWorkReportVo) throws Exception;
	
	JsonBean workReportAdd(TblNbsjWorkReportEntity wr, String token,String attids)throws Exception;
    
    JsonBean workReportDelete(Integer reportid, String token) throws Exception;
    
    JsonBean findNbsjWorkReportDetail(String token, Integer reportid) throws Exception;
    
    R removeAttInfoByAttId(String token, String attId) throws Exception;
	
}
