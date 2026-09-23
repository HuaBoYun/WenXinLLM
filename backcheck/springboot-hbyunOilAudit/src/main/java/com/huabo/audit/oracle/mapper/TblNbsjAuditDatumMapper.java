package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjAuditDatumEntity;

public interface TblNbsjAuditDatumMapper extends BaseMapper<TblNbsjAuditDatumEntity>{

	@Select("SELECT * from TBL_NBSJ_AUDITDATUM WHERE EXPERID= #{experId} ")
    List<TblNbsjAuditDatumEntity> findByExperId(String experId, Integer pageNumber, int pageSize);
	
	@Delete("DELETE from TBL_NBSJ_AUDITDATUM WHERE EXPERID= #{experId} ")
    void deleteByExperId(String experId);
	
	@Delete("DELETE from TBL_NBSJ_AUDITDATUM WHERE DATUMID= #{datumId} ")
    void delete(String datumId);
}
