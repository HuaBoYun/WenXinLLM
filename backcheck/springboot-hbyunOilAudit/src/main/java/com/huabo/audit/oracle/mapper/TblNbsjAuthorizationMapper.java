package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjAuthorizationEntity;

public interface TblNbsjAuthorizationMapper extends BaseMapper<TblNbsjAuthorizationEntity>{

	@Select("SELECT * from TBL_NBSJ_AUTHORIZATION WHERE PROJECTID= #{projectId} ") //AND
	TblNbsjAuthorizationEntity get(String projectId,String aduitProGramId);
	
	
	//==
	@Select("SELECT * from TBL_NBSJ_AUTHORIZATION TNA "
			+ "LEFT JOIN TBL_NBSJ_AUDITPROGRAM PG ON PG.PROGRAMID = TNA.PROGRAMID "
			+ "LEFT JOIN TBL_NBSJ_PROJECT PJ ON PJ.PROJECTID = TNA.PROJECTID "
			+ "where PG.programId = #{aduitProGramId} "
			+ "and PJ.projectid = #{projectId} ") //AND
	List<TblNbsjAuthorizationEntity> getByPjPg(BigDecimal projectId,Integer aduitProGramId);
	
	
	@InsertProvider(method="insertEntity",type=TblNbsjAuthorizationMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="authId", keyColumn="AUTHID")
	void insertEntity(TblNbsjAuthorizationEntity plan) throws Exception;
	
	@UpdateProvider(method="updateEntity",type=TblNbsjAuthorizationMapperSqlConfig.class)
	void updateEntity(TblNbsjAuthorizationEntity plan);
	
	@Select("SELECT * from TBL_NBSJ_AUTHORIZATION TNA "
			+ "LEFT JOIN TBL_NBSJ_PROJECT PJ ON PJ.PROJECTID = TNA.PROJECTID "
			+ "where PJ.projectid = #{projectId} ")
	List<TblNbsjAuthorizationEntity> getByProjectId(BigDecimal projectId);
	
	@Delete("DELETE FROM TBL_NBSJ_AUTHORIZATION WHERE authId = #{authId}")
    void deleteByAuthid(Integer authId) throws Exception;
	
	
	@Select("SELECT * from TBL_NBSJ_AUTHORIZATION TNA "
			+ "where TNA.projectid = #{projectId} ")
	List<TblNbsjAuthorizationEntity> getBynewProjectId(BigDecimal projectId);
	
	@InsertProvider(method="insertnewEntity",type=TblNbsjAuthorizationMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="authId", keyColumn="AUTHID")
	void insertnewEntity(TblNbsjAuthorizationEntity plan) throws Exception;
	
	
	@Select("SELECT * from TBL_NBSJ_AUTHORIZATION TNA "
			+ "LEFT JOIN TBL_NBSJ_AUDITPROGRAM PG ON PG.PROGRAMID = TNA.PROGRAMID "
			+ "where PG.programId = #{aduitProGramId} "
			+ " and TNA.PROJECTID= #{projectId} ") //AND
	List<TblNbsjAuthorizationEntity> getByPjnewPg(BigDecimal projectId,BigDecimal aduitProGramId);
	
}
