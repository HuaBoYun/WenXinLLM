package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjDoubtfulpointEntity;
import com.huabo.audit.oracle.vo.TblNbsjDoubtfulpointVo;
import com.huabo.audit.util.R;

import java.math.BigDecimal;

public interface TblNbsjDoubtfulpointService {

	JsonBean dpPageList(String token, Integer pageNumber, Integer pageSize,TblNbsjDoubtfulpointVo tblNbsjDoubtfulpointVo) throws Exception;
	
	JsonBean dpAdd(TblNbsjDoubtfulpointEntity dp, String token,String attids)throws Exception;
    
    JsonBean dpDelete(BigDecimal dpointid, String token) throws Exception;
    
    JsonBean findDPDetail(String token, BigDecimal dpointid) throws Exception;
    
    R removeAttInfoByAttId(String token, String attId) throws Exception;
}
