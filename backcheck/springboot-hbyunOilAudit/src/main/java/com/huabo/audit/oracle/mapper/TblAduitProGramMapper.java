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

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAduitProGramEntity;
import com.huabo.audit.vo.result.QualityParam;

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
   	Integer selectCountByPageInfo(PageInfo<TblAduitProGramEntity> pageInfo,Integer projectId,Integer tempId,Integer targetId) throws Exception;

    @Select("SELECT TNA.* FROM TBL_NBSJ_AUDITPROGRAM TNA  WHERE TNA.PROGRAMID = #{programId}")
    @Results({
    	@Result(column="PROGRAMID",property="programId"),
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="RISKSOURCE",property="riskSource"),
    	@Result(column="RISKPOINT",property="riskPoint"),
    	@Result(column="CONTROL",property="control"),
    	
    })
   	TblAduitProGramEntity selectById(@Param("programId") String programId) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblAduitProGramMapperSqlConfig.class)
    @Results({
    	@Result(column="PROGRAMID",property="programId"),
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="RISKSOURCE",property="riskSource"),
    	@Result(column="RISKPOINT",property="riskPoint"),
    	@Result(column="CONTROL",property="control"),
    })
	List<TblAduitProGramEntity> selectListByPageInfo(PageInfo<TblAduitProGramEntity> pageInfo,Integer projectId,Integer tempId,Integer targetId) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_AUDITPROGRAM WHERE PROGRAMID = #{programId}")
    void deleteById(String programId) throws Exception;

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
    
    
    @Select("SELECT * FROM TBL_NBSJ_AUDITPROGRAM where TEMPID=#{tempid}")
  	List<TblAduitProGramEntity> findByALLbytempid(String tempid);
    
    
//    @Select("SELECT bb.pc,bb.ssdw,bb.auditorgname,bb.projectname,bb.ksfzr,bb.xmzcy,bb.xcsrarttime,bb.xcendtime,bb.sj, (REGEXP_COUNT(bb.xmzcy,',')+1) * bb.sj trzy,"
//    		+ " CASE WHEN bb.dgzs=0 then 0 else (REGEXP_COUNT(bb.xmzcy,',')+1) * bb.sj/bb.dgzs END  gxb,bb.fhdg,bb.wtdg from (  "
//    		+ " SELECT aa.pc,aa.ssdw,aa.auditorgname,aa.projectname,aa.ksfzr,aa.xmzcy,aa.xcsrarttime,aa.xcendtime,CASE WHEN aa.xcsrarttime is null then 0 ELSE  aa.sj end sj,"
//    		+ " aa.dgzs,aa.fhdg,aa.wtdg from (select DISTINCT CASE WHEN PL.ZYKSTYPE='基建' THEN tb.BATC ELSE tbf.BATC END pc,"
//    		+ " CASE WHEN PL.ZYKSTYPE='基建' THEN EP.exePhraseUnit ELSE fp.exePhraseUnit END ssdw,PL.AUDIT_ORG_NAME auditorgname,PL.PROJECT_NAME projectname,FP.APPROVER,FP.ASSISTAPPROVER,FP.XCSRARTTIME,FP.XCENDTIME, "
//    		+ " CASE WHEN PL.ZYKSTYPE='基建' THEN EP.RSYQ ELSE fp.RSYQ END RSYQ, CASE WHEN PL.ZYKSTYPE='基建' THEN round(EP.XCENDTIME -  EP.XCSRARTTIME) ELSE round(fp.XCENDTIME -  fp.XCSRARTTIME) END  sj,"
//    		+ " CASE WHEN PL.ZYKSTYPE='基建' THEN EP.XFKSRYNAMES ELSE fp.XFKSRYNAMES END ksfzr, CASE WHEN PL.ZYKSTYPE='基建'   THEN ep.approver||','||ep.assistApprover ELSE fp.approver||','||fp.assistApprover END xmzcy,"
//    		+ " (SELECT count(*) from TBL_YQNS_AUDIT_MY_MANUSCRIPT dg where DG.PROJECTID=PL.ID) dgzs, "
//    		+ " (SELECT count(*) from TBL_YQNS_AUDIT_MY_MANUSCRIPT dg where DG.PROJECTID=PL.ID and DG.PROBLEMDRAFT=0) fhdg, "
//    		+ " (SELECT count(*) from TBL_YQNS_AUDIT_MY_MANUSCRIPT dg where DG.PROJECTID=PL.ID and DG.PROBLEMDRAFT=1) wtdg"
//    		+ " from TBL_YQNS_IMPLEMENT_PLAN pl  LEFT JOIN TBL_YQNS_FUND_AUDIT_PROJECT fp ON PL.XMAPBID=fp.ID  "
//    		+ " LEFT JOIN TBL_YQNS_FUNDTB_GL fgl on fp.ID =fgl.FUNID LEFT JOIN TBL_YQNS_ENGINTB tbf on fgl.TBID=tbf.TBID "
//    		+ " LEFT JOIN TBL_YQNS_ENGIN_AUDIT_PROJECT ep on PL.XMAPBID=EP.ID  LEFT JOIN TBL_YQNS_ENGINTB_GL gl on EP.ID =gl.ENGID "
//    		+ " LEFT JOIN TBL_YQNS_ENGINTB tb on gl.TBID=tb.TBID )  aa )bb")
    @SelectProvider(method="findBytjsj",type=TblAduitProGramMapperSqlConfig.class)
  	List<QualityParam> findBytjsj(QualityParam param);
}
