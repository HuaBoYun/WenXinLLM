package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsIssueListEntity;
import com.huabo.audit.oracle.entity.TblYqnsIssuesRecord;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TblYqnsIssuesRecordMapper extends BaseMapper<TblYqnsIssuesRecord> {

	@Select("SELECT * FROM TBL_YQNS_ISSUESRECORD WHERE VERSION = (SELECT MAX(VERSION) FROM TBL_YQNS_ISSUESRECORD WHERE ISSUESID = #{issuesId} ) AND ISSUESID = #{issuesId} ")
	TblYqnsIssuesRecord selectMaxVersionByIssues(@Param("issuesId") BigDecimal issuesId);

	@Select("SELECT * FROM TBL_YQNS_ISSUESRECORD WHERE ISSUESID = #{issuesId} AND WTZGID = #{wtzgid} ")
	TblYqnsIssuesRecord selectByIssuesWtzgId(@Param("issuesId") BigDecimal issuesId,@Param("wtzgid") BigDecimal wtzgid) throws Exception;

	@Update("UPDATE TBL_YQNS_ISSUESRECORD SET RECTPERSON = NULL,RECTPERSONNAME = NULL WHERE RECORDID = #{recordId} ")
	void setPersonIsNull(BigDecimal recordId) throws Exception;

	@Delete("DELETE FROM TBL_YQNS_ISSUESRECORD WHERE ISSUESID = #{issuesId}")
	void deleteByIssuesId(@Param("issuesId")BigDecimal issuesId);

}
