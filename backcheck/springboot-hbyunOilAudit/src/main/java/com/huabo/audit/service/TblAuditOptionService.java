package com.huabo.audit.service;

import java.util.List;

import org.apache.ibatis.annotations.Delete;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblAuditOption;
import com.huabo.audit.oracle.entity.TblCirculation;

public interface TblAuditOptionService {

	List<TblAuditOption> findOptionByRelationId(String string, Integer cyId) throws Exception;

	JsonBean saveAuditOptionInfo(TblCirculation cy, TblAuditOption opt) throws Exception;

	List<TblAuditOption> findOptionByRelationId(String id) throws Exception;
	
	
}