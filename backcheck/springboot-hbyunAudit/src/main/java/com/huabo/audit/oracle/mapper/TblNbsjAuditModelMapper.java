package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjAuditModelEntity;

public interface TblNbsjAuditModelMapper extends BaseMapper<TblNbsjAuditModelEntity>{

	@Select("SELECT * from TBL_NBSJ_AUDITMODEL WHERE EXPERID= #{experId} ")
	TblNbsjAuditModelEntity getByExperId(String experId);
	
	@Delete("DELETE from TBL_NBSJ_AUDITMODEL WHERE EXPERID= #{experId} ")
	void deleteByExperId(String experId);
	
	@Select("SELECT * from TBL_NBSJ_AUDITMODEL WHERE MODELID= #{modelId} ")
	List<TblNbsjAuditModelEntity> findAll(String modelId);
}
