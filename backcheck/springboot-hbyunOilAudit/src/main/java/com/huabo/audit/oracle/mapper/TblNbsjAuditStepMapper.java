package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.StepResult;
import com.huabo.audit.oracle.entity.TblNbsjAuditStepEntity;
import com.huabo.audit.oracle.entity.TblNbsjReformSolution;

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
	
	
	@Select("SELECT * from TBL_NBSJ_AUDITSTEP  ")
    List<TblNbsjAuditStepEntity> findByExperByExperall();
	
	
	
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
}
 