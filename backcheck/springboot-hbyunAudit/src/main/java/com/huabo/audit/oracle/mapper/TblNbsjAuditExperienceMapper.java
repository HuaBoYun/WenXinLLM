package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjAuditExperienceEntity;

public interface TblNbsjAuditExperienceMapper extends BaseMapper<TblNbsjAuditExperienceEntity>{

	@Select("SELECT * from TBL_NBSJ_AUDIT_EXPERIENCE WHERE TYPEID= #{typeId} AND TITLE= #{title} AND AUTOR= #{autor} ")
    List<TblNbsjAuditExperienceEntity> findByType(String typeId, String title, String autor, Integer pageNumber, int pageSize);
	
	@Delete("DELETE from TBL_NBSJ_AUDIT_EXPERIENCE WHERE EXPERID= #{experId}")
    void delete(String experId);
}
