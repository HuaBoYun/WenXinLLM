package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.StepResult;
import com.huabo.audit.oracle.entity.TblNbsjAuditStepEntity;
import com.huabo.audit.oracle.entity.TblNbsjReformSolution;
import com.huabo.audit.oracle.entity.TblStaff;

import io.lettuce.core.dynamic.annotation.Param;

public interface TblNbsjAuditStepMapper extends tk.mybatis.mapper.common.Mapper<TblNbsjAuditStepEntity>{

	@Select("SELECT * from TBL_NBSJ_AUDITSTEP WHERE EXPERID= #{experId} ")
    List<TblNbsjAuditStepEntity> findByExper(String experId, Integer pageNumber, int pageSize);
	
	@Select("SELECT * from TBL_NBSJ_AUDITSTEP WHERE TYPEID= #{typeid} ")
    List<TblNbsjAuditStepEntity> findByExperByExperId(BigDecimal typeid);
	
	@Select("SELECT ep.*,AC.ACCTID from TBL_NBSJ_AUDITSTEP  ep LEFT JOIN TBL_ACCBOOK ac on EP.BOOKID=AC.BOOKID WHERE STEPID= #{stepId} ")
	TblNbsjAuditStepEntity findByExperBystepId(BigDecimal stepId);
	
	@Delete("DELETE from TBL_NBSJ_AUDITSTEP WHERE EXPERID= #{experId} ")
    void deleteByExperId(String experId);
	
	@Delete("DELETE from TBL_NBSJ_AUDITSTEP WHERE STEPID= #{stepId} ")
    void deletebyid(BigDecimal stepId);
	
	
//	@Select("SELECT * from TBL_NBSJ_AUDITSTEP  ")
	@SelectProvider(method="selectListallPageInfo",type=TblNbsjAuditStepMapperSqlConfig.class)
    List<TblNbsjAuditStepEntity> findByExperByExperall(BigDecimal typeid,TblNbsjAuditStepEntity step);
	
	
	
	@SelectProvider(method="selectListPageInfo",type=TblNbsjAuditStepMapperSqlConfig.class)
	List<Map<String,Object>> selectListPageInfo(PageInfo<Map<String,Object>> pageInfo, String sql) throws Exception;
	
	@SelectProvider(method="selectListPageInfocount",type=TblNbsjAuditStepMapperSqlConfig.class)
	Integer selectListPageInfocount( String sql) throws Exception; 
	
	
	
	 @InsertProvider(method="insertEntity",type=TblNbsjAuditStepMapperSqlConfig.class)
	 @Options(useGeneratedKeys=true, keyProperty="resultid", keyColumn="RESULTID")
	 void insertEntity(StepResult result) throws Exception;
	 
	 
	 @Select("SELECT SJ.*,ST.REALNAME from TBL_NBSJ_SJMXRESULT sj LEFT JOIN TBL_STAFF st on SJ.STAFFID=ST.STAFFID  where STEPID=#{stepid} ")
	 List<StepResult> sjmxReulst(BigDecimal stepid);
	  
	 
	@Select("SELECT * from TBL_NBSJ_SJMXRESULT where RESULTID=#{resultid} ")
	StepResult onesjmxReulst(BigDecimal resultid);
	
	@Select("SELECT count(*) from TBL_NBSJ_AUDITSTEP where to_char(CREATETIME,'yyyy')=#{year}  ORDER BY STEPID DESC")
	Integer findbycodecount(String year);

	
	@Select("SELECT stepno from TBL_NBSJ_AUDITSTEP where to_char(CREATETIME,'yyyy')=#{year} and ROWNUM=1 ORDER BY STEPID DESC")
	String findbycode(String year);
	
	 
	
	@Update("UPDATE TBL_NBSJ_AUDITSTEP SET QYSTATUS=#{xgstatus} WHERE STEPID = #{stepId} ")
    void xgStatus(BigDecimal stepId,Integer xgstatus);
	
	
	@Insert("INSERT INTO TBL_NBSJ_AUDITSTEP_XFRY(STEPID,STAFFID) VALUES(#{stepId},#{staffid})")
	void saveXfry(String stepId,String staffid); 
	
	@Select("SELECT * from TBL_STAFF WHERE STAFFID in (SELECT STAFFID from TBL_NBSJ_AUDITSTEP_XFRY where STEPID=#{stepId} ) ")
    List<TblStaff> findByxfList(BigDecimal stepId);
	
	@Select("SELECT * from TBL_STAFF WHERE STAFFID in (SELECT STAFFID from TBL_NBSJ_AUDITSTEP_XFRY where STEPID=#{stepId} ) and REALNAME like '%${name}%' ")
    List<TblStaff> findByNamexfList(BigDecimal stepId,String name);
	
	@Delete("DELETE from TBL_NBSJ_AUDITSTEP_XFRY WHERE STAFFID in (${staffid}) AND STEPID=#{stepId}")
	void deleteXfry(BigDecimal stepId,String staffid);
	
	@Select("SELECT * from TBL_NBSJ_AUDITSTEP WHERE STEPID in (SELECT STEPID from TBL_NBSJ_AUDITSTEP_XFRY where STAFFID=#{staffid} ) and QYSTATUS=0 ")
    List<TblNbsjAuditStepEntity> findByStaffid(BigDecimal staffid);
	
	@SelectProvider(method="selectListxfPageInfo",type=TblNbsjAuditStepMapperSqlConfig.class)
	List<TblNbsjAuditStepEntity> selectListxfPageInfo(PageInfo<TblNbsjAuditStepEntity> pageInfo, TblNbsjAuditStepEntity step,BigDecimal staffid) throws Exception;
	
	@SelectProvider(method="selectListxfPageInfocount",type=TblNbsjAuditStepMapperSqlConfig.class)
	Integer selectListxfPageInfocount( TblNbsjAuditStepEntity setp,BigDecimal staffid) throws Exception;

	@SelectProvider(method="selectListBySummary",type=TblNbsjAuditStepMapperSqlConfig.class)
	List<TblNbsjAuditStepEntity> selectListBySummary(TblNbsjAuditStepEntity step, String limitStr) throws Exception; 
}
 