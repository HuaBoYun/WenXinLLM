package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsXmdq;
import com.huabo.audit.vo.result.AuditControlAnalysisResult;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TblYqnsXmdqMapper extends BaseMapper<TblYqnsXmdq> {
	
	
    @Delete("DELETE FROM TBL_YQNS_XMQD WHERE XMDQID = #{xmdqid}")
    void deleteoneById(BigDecimal xmdqid) throws Exception;

 

    @Select("select *  FROM TBL_YQNS_XMQD WHERE XMDQID = #{xmdqid}") 
    TblYqnsXmdq selectById(BigDecimal xmdqid) throws Exception;


    @Insert("INSERT INTO TBL_YQNS_XMQD_ATT(XMDQID,ATTID) VALUES (#{xmdqid},#{aid})")
    void insertAttInfoAtt( BigDecimal xmdqid, String aid) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_XMQD_ATT where  ATTID=#{aid}")
    void deleteAttInfoAtt( String aid) throws Exception;

    @Insert("DELETE FROM TBL_YQNS_XMQD_ATT where XMDQID=#{xmdqid}")
    void deleteAttInfoAttByxm(BigDecimal xmdqid) throws Exception;

    @SelectProvider(type = TblYqnsXmdqMapperSqlConfig.class , method = "selectAuditProjectPlanAnalysisList")
	List<TblYqnsXmdq> selectAuditProjectPlanAnalysisList(Integer xmnd);

    @SelectProvider(type = TblYqnsXmdqMapperSqlConfig.class , method = "selectPlanAnalysisXmList")
    List<TblYqnsXmdq> selectPlanAnalysisXmList(Integer xmnd, Integer dataType) ;


    @SelectProvider(type = TblYqnsXmdqMapperSqlConfig.class , method = "selectAuditControlAnalysisService")
	List<AuditControlAnalysisResult> selectAuditControlAnalysisService(Integer choiceType, BigDecimal staffId, String staffName);


	@Select("${sql}")
	void executeSql(@Param("sql")String sql) throws Exception;


	@Select("SELECT * FROM TBL_YQNS_XMQD WHERE  SITEENDTIME >= TO_DATE(SYSDATE - 3) AND SITEENDTIME <= SYSDATE AND ZSSTAFFID = #{staffid}")
	List<TblYqnsXmdq> selectTipList(BigDecimal staffid);




}
