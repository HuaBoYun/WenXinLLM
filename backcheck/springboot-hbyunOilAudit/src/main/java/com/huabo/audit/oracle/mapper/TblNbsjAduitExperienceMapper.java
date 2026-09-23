package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjAduitExperienceEntity;

public interface TblNbsjAduitExperienceMapper extends BaseMapper<TblNbsjAduitExperienceEntity>{

	@Select("SELECT * from TBL_NBSJ_AUDIT_EXPERIENCE WHERE TARGETID= #{targetId} AND TEMPID= #{tempId} ")
	List<TblNbsjAduitExperienceEntity> findByTargetId(String targetId, String tempId, Integer pageNumber, int pageSize);
	
	@Delete("DELETE from TBL_NBSJ_AUDIT_EXPERIENCE WHERE TARGETID= #{targetId} ")
	void deleteByTargetId(String targetId);
	
	@Delete("DELETE from TBL_NBSJ_AUDIT_EXPERIENCE WHERE TEMPID= #{tempId} ")
	void deleteByTempId(String tempId);
	
}
