package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjAuditExperienceTypeEntity;

public interface TblNbsjAuditExperienceTypeMapper extends BaseMapper<TblNbsjAuditExperienceTypeEntity>{
	
	@Select("SELECT * FROM TBL_NBSJ_AUDITEXPERIENCE_TYPE WHERE PARENTID is NULL")
	List<TblNbsjAuditExperienceTypeEntity> getByall() throws Exception;
	
	
	@Select("SELECT * FROM TBL_NBSJ_AUDITEXPERIENCE_TYPE WHERE PARENTID = #{nodeId}")
	List<TblNbsjAuditExperienceTypeEntity> getBynoid(BigDecimal nodeId) throws Exception;
	
	
	@Select("SELECT count(*) FROM TBL_NBSJ_AUDITEXPERIENCE_TYPE WHERE PARENTID = #{nodeId}")
	Integer getBynoidcount(BigDecimal nodeId) throws Exception;
	
	
	
	 @InsertProvider(method="insertEntity",type=TblNbsjAuditExperienceTypeMapperSqlConfig.class)
	 @Options(useGeneratedKeys=true, keyProperty="typeId", keyColumn="TYPEID")
	 void insertEntity(TblNbsjAuditExperienceTypeEntity re) throws Exception;
	 
	 
	 @UpdateProvider(method="updateEntity",type=TblNbsjAuditExperienceTypeMapperSqlConfig.class)
	 void updateEntity(TblNbsjAuditExperienceTypeEntity re) throws Exception;
	 
	 
	 @Delete("DELETE FROM TBL_NBSJ_AUDITEXPERIENCE_TYPE WHERE TYPEID = #{nodeId}")
	 void deleteEntity(BigDecimal nodeId) throws Exception;
	
	 @Select("SELECT * FROM TBL_NBSJ_AUDITEXPERIENCE_TYPE WHERE TYPEID = #{nodeId}")
	 TblNbsjAuditExperienceTypeEntity findbyid(BigDecimal nodeId) throws Exception;
	
}
