package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAduitProGramEntity;

public interface TblAduitProGramMapper extends tk.mybatis.mapper.common.Mapper<TblAduitProGramEntity> {
	
	@Select("SELECT * FROM  TBL_NBSJ_AUDITPROGRAM WHERE PROGRAMID = #{programId}")
	List<TblAduitProGramEntity> getByTempleteId(String programId);
	
	
	@Select("SELECT * FROM  TBL_NBSJ_AUDITPROGRAM WHERE TARGETID = #{targetId}")
	List<TblAduitProGramEntity> findByTargetId(String targetId);
	
	@Delete("DELETE FROM  TBL_NBSJ_AUDITPROGRAM WHERE TARGETID = #{targetId}")
	void deleteByTargetId(String targetId);
	
	@Delete("DELETE FROM  TBL_NBSJ_AUDITPROGRAM WHERE TEMPID = #{tempId}")
	void deleteByTempId(String tempId);
	
	@Select("SELECT * FROM  TBL_NBSJ_AUDITPROGRAM WHERE TEMPID = #{tempid}")
	List<TblAduitProGramEntity> findByALL(String tempid);
	
	
	@Delete("DELETE FROM  TBL_NBSJ_AUDITPROGRAM WHERE TEMPID = #{tempId}")
	void deleteZy(String tempId);
	
	
	
	
	//==
	@Select("SELECT * from TBL_NBSJ_AUDITPROGRAM WHERE PROGRAMID= #{programId} ")
    TblAduitProGramEntity getById(String programId);
    
    @SelectProvider(method="selectCountByPageInfo",type=TblAduitProGramMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblAduitProGramEntity> pageInfo,BigDecimal projectId,BigDecimal tempId,BigDecimal targetId) throws Exception;

    @Select("SELECT TNA.* FROM TBL_NBSJ_AUDITPROGRAM TNA  WHERE TNA.PROGRAMID = #{programId}")
    @Results({
    	@Result(column="PROGRAMID",property="programId"),
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="RISKSOURCE",property="riskSource"),
    	@Result(column="RISKPOINT",property="riskPoint"),
    	@Result(column="CONTROL",property="control"),
    	
    })
   	TblAduitProGramEntity selectById(@Param("programId") BigDecimal programId) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblAduitProGramMapperSqlConfig.class)
    @Results({
    	@Result(column="PROGRAMID",property="programId"),
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="RISKSOURCE",property="riskSource"),
    	@Result(column="RISKPOINT",property="riskPoint"),
    	@Result(column="CONTROL",property="control"),
    })
	List<TblAduitProGramEntity> selectListByPageInfo(PageInfo<TblAduitProGramEntity> pageInfo,BigDecimal projectId,BigDecimal tempId,BigDecimal targetId) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_AUDITPROGRAM WHERE PROGRAMID = #{programId}")
    void deleteById(BigDecimal programId) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblAduitProGramMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblAduitProGramEntity plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblAduitProGramMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="programId", keyColumn="PROGRAMID")
	void insertEntity(TblAduitProGramEntity plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblAduitProGramMapperSqlConfig.class)
	void updateEntity(TblAduitProGramEntity plan) throws Exception;
    
    
    
    
    
    
    
    
    
    
    
    
    //==
    @Select("SELECT * FROM  TBL_NBSJ_AUDITPROGRAM TNA "
    		+ "LEFT JOIN TBL_NBSJ_TARGETTYPE tg ON tg.targetId = TNA.targetId "
    		+ "WHERE tg.TARGETID = #{targetId}")
	List<TblAduitProGramEntity> findByTGId(Integer targetId)throws Exception;
    
    @Select("SELECT * FROM  TBL_NBSJ_AUDITPROGRAM TNA "
    		+ "LEFT JOIN TBL_NBSJ_TEMPLETE tg ON tg.templeteId = TNA.TEMPID "
    		+ "WHERE tg.templeteId = #{targetId}")
	List<TblAduitProGramEntity> findByTMId(BigDecimal tempId)throws Exception;
    
    
    @Select("SELECT * FROM TBL_NBSJ_AUDITPROGRAM where TARGETID=#{tempid}")
	List<TblAduitProGramEntity> findByALLTempid(String tempid);

    @Results({
    	@Result(column="renyuan",property="renyuan"),
    })
	List<TblAduitProGramEntity> selectListByPageInfoXml(@Param("proGramEntity") TblAduitProGramEntity proGramEntity);
}
