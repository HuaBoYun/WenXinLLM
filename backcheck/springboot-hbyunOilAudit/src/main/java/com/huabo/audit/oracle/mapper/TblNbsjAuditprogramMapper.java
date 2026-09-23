package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjAuditprogramEntity;

public interface TblNbsjAuditprogramMapper extends BaseMapper<TblNbsjAuditprogramEntity>{

	@InsertProvider(method="insertEntity",type=TblNbsjAuditprogramMapperSqlConfig.class)
	@Options(useGeneratedKeys=true, keyProperty="targetId", keyColumn="TARGETID")
	void insertEntity(TblNbsjAuditprogramEntity program);
	
    @UpdateProvider(method="updateEntity",type=TblNbsjAuditprogramMapperSqlConfig.class)
	void updateEntity(TblNbsjAuditprogramEntity program);
    
    @Select("SELECT * FROM TBL_NBSJ_AUDITPROGRAM WHERE programId=#{programId}")
    TblNbsjAuditprogramEntity findbyid(String programId);
    
    @Delete("Delete from TBL_NBSJ_AUDITPROGRAM WHERE programId=#{programId}")
	void deleteInfoById(String programId);
    
    @Delete("Delete from TBL_NBSJ_AUDITPROGRAM WHERE targetid=#{targetid}")
    void deleteInfoByTargetId(String targetid);
    
    @SelectProvider(method="selectTblNbsjAuditprogramListByPageInfo",type=TblNbsjAuditprogramMapperSqlConfig.class)
	List<TblNbsjAuditprogramEntity> selectTblNbsjAuditprogramListByPageInfo(PageInfo<TblNbsjAuditprogramEntity> pageInfo, String templeteId, String targetId);
    
    @SelectProvider(method="selectTblNbsjAuditprogramCountByPageInfo",type=TblNbsjAuditprogramMapperSqlConfig.class)
	Integer selectTblNbsjAuditprogramCountByPageInfo(PageInfo<TblNbsjAuditprogramEntity> pageInfo, String templeteId,String targetId);
    @Delete("Delete from TBL_NBSJ_AUDITPROGRAM WHERE TEMPID=#{templeteId}")
	void deleteInfoByTempleteId(String templeteId);
    
    @Select("SELECT * FROM TBL_NBSJ_AUDITPROGRAM WHERE targetid=#{targetid}")
    List<TblNbsjAuditprogramEntity> selectByTargetid(BigDecimal targetid);

}
