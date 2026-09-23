package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.TblContractProject;
import com.huabo.contract.vo.TblContractProjectVo;

public interface TblContractProjectService {
	
	JsonBean cpPageList(String token, Integer pageNumber, Integer pageSize,TblContractProjectVo tblContractProjectVo) throws Exception;
	
	JsonBean cpAdd(TblContractProject cp, String token, String attIds)throws Exception;
    
    JsonBean cpDelete(BigDecimal projectid, String token) throws Exception;
    
    JsonBean findContractProjectDetail(String token, BigDecimal projectid) throws Exception;

	JsonBean removeFile(String token, String attId) throws Exception;

	JsonBean getContractProjectNo(String token) throws Exception;
	
	public JsonBean tball(String token,List<TblContractProject> list ) throws Exception;
}
