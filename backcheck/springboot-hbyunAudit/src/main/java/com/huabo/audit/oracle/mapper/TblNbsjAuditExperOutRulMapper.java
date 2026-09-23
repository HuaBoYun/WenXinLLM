package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjAuditExperOutRulEntity;

public interface TblNbsjAuditExperOutRulMapper extends BaseMapper<TblNbsjAuditExperOutRulEntity>{

	@Select("SELECT * from TBL_NBSJ_AUDITEXPEROUTRUL WHERE EXPERID= #{experId} AND OUTID= #{outId} ")
	TblNbsjAuditExperOutRulEntity findByExperIdAndOutId(String experId,String outId);
	
	@Select("SELECT * from TBL_NBSJ_AUDITEXPEROUTRUL WHERE EXPERID= #{experId} ")
	List<TblNbsjAuditExperOutRulEntity> findByExperId(String experId,Integer pageNumber, int pageSize);
	
	@Delete("DELETE from TBL_NBSJ_AUDITEXPEROUTRUL WHERE EXPERID= #{experId} ")
	void deleteByExperId(String experId);
	
	@Delete("DELETE from TBL_NBSJ_AUDITEXPEROUTRUL WHERE OUTID= #{outid} ")
	void delete(String outid);
	
}
