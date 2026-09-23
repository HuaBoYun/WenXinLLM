package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblOtherarticle;
import com.huabo.audit.oracle.vo.TblOtherarticleVo;

import java.util.List;

public interface TblOtherarticleService {

	JsonBean selectOtherarticlePageInfo(String token, Integer pageNumber, Integer pageSize,
			TblOtherarticleVo tblOtherarticleVo) throws Exception ;

	JsonBean mergeOtherarticleInfo(TblOtherarticle tblOtherarticle, String token, String attIds) throws Exception ;

	JsonBean selectInfo(String othartid);

	JsonBean deleteInfo(String othartid);


}
