package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjPlanProject;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.vo.TblNbsjPlanProjectVo;

import io.lettuce.core.dynamic.annotation.Param;

public interface TblNbsjPlanProjectMapper{

	@InsertProvider(type=TblNbsjPlanProjectMapperSqlConfig.class,method="insertEntity")
	@Options(useGeneratedKeys=true, keyProperty="planprojectid", keyColumn="PLANPROJECTID")
	void insertEntity(TblNbsjPlanProject project) throws Exception;

	@UpdateProvider(type=TblNbsjPlanProjectMapperSqlConfig.class,method="updateEntity")
	void updateEntity(TblNbsjPlanProject project) throws Exception;

	@Delete("DELETE FROM TBL_NBSJ_PLANPROJECT WHERE PLANPROJECTID = #{planprojectid}")
	void deletePlanProjectInfo(@Param("planprojectid")Integer planprojectid) throws Exception;

	@Delete("DELETE FROM TBL_NBSJ_PLANPROJECT WHERE PLANID = #{planId}")
	void deletePlanProjectByPlanId(@Param("planId")Integer planId) throws Exception;

	@Select("SELECT PLANPROJECTID,PROJECTNAME,TARGETNAME,FINISHTIME,ORGIDS,ORGIDNAMES,EXTERNALASSIG,PLANID,BSJTYPE "
			+ "FROM TBL_NBSJ_PLANPROJECT WHERE PLANID = #{planId} "
			+ " ORDER BY PLANPROJECTID ")
	@Results({
		@Result(column="PLANPROJECTID",property="planprojectid"),
		@Result(column="PROJECTNAME",property="projectname"),
		@Result(column="TARGETNAME",property="targetname"),
		@Result(column="FINISHTIME",property="finishtime"),
		@Result(column="ORGIDS",property="orgids"),
		@Result(column="ORGIDNAMES",property="orgidnames"),
		@Result(column="EXTERNALASSIG",property="externalassig"),
		@Result(column="PLANID",property="planid"),
		@Result(column="BSJTYPE",property="bsjtype"),
	})
	List<TblNbsjPlanProject> selectPlanProjectListInfoByPlanId(@Param("planId")Integer planId);

	@SelectProvider(type=TblNbsjPlanProjectMapperSqlConfig.class,method="selectPlanProjectListByPageInfo")
	@Results({
		@Result(column="PLANPROJECTID",property="planprojectid"),
		@Result(column="PROJECTNAME",property="projectname"),
		@Result(column="TARGETNAME",property="targetname"),
		@Result(column="FINISHTIME",property="finishtime"),
		@Result(column="ORGIDNAMES",property="orgidnames"),
		@Result(column="EXTERNALASSIG",property="externalassig"),
	})
	List<TblNbsjPlanProject> selectPlanProjectListByPageInfo(TblNbsjPlanProjectVo project, PageInfo<TblNbsjPlanProject> pageInfo) throws Exception;
	
	@SelectProvider(type=TblNbsjPlanProjectMapperSqlConfig.class,method="selectPlanProjectCountByPageInfo")
	Integer selectPlanProjectCountByPageInfo(TblNbsjPlanProjectVo project, PageInfo<TblNbsjPlanProject> pageInfo) throws Exception;

	
	
	@Select("SELECT TNA.*,TS.REALNAME,PLAN.PLANNAME,TEMP.TEMPLETENAME,TEMPZY.TEMPLETENAME TEMPLETENAMEZY,"
    		+ "TEMP.TEMPLETEID TEMPSJID,TEMPZY.TEMPLETEID TEMPZYID,createUser.REALNAME CREATEUSERNAME,"
    		+ "auditOrg.ORGNAME AUDITORGNAME,auditStaff.REALNAME AUDITSTAFFNAME,demp.ORGID demporgid,demp.ORGNAME dempname,TNA.PROJECTTYPE "
    		+ " FROM TBL_NBSJ_PROJECT TNA "
    		+ " LEFT JOIN TBL_NBSJ_AUDITPLAN PLAN ON PLAN.PLANID = TNA.PLANID "
    		+ " LEFT JOIN TBL_NBSJ_TEMPLETE TEMP ON TEMP.TEMPLETEID = TNA.TEMPID "
    		+ " LEFT JOIN TBL_NBSJ_TEMPLETE TEMPZY ON TEMPZY.TEMPLETEID = TNA.TEMPZYID "
			+ " LEFT JOIN TBL_STAFF TS ON TNA.PMID = TS.STAFFID "
			+ " LEFT JOIN TBL_STAFF createUser ON TNA.CREATESTAFFID = createUser.STAFFID "
			+ " LEFT JOIN TBL_ORGANIZATION auditOrg ON TNA.AUDITORGID = auditOrg.ORGID "
			+ " LEFT JOIN TBL_STAFF auditStaff ON TNA.AUDITSTAFFID = auditStaff.STAFFID "
			+ " LEFT JOIN TBL_ORGANIZATION demp ON TNA.cospomsordepartment = demp.ORGID "
    		+ " WHERE TNA.PLANID = #{planId} ")
    @Results({
    	@Result(column="PROJECTID",property="projectId"),
    	@Result(column="PRJOECTNAME",property="prjoectName"),
    	@Result(column="PPROJECTNAME",property="pprojectName"),
    	@Result(column="PLANYEAR",property="planYear"),
    	@Result(column="PROJECTSOURCE",property="projectSource"),
    	@Result(column="STARTDATE",property="startDate"),
    	@Result(column="ENDDATE",property="endDate"),
    	@Result(column="AUDITTYPE",property="auditType"),
    	@Result(column="PROJECTCODE",property="projectCode"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="PMID",property="pmId"),
    	@Result(column="REALNAME",property="pmStaff.realname"),
    	@Result(column="IMPLEMENTAION",property="implementaion"),
    	@Result(column="COSPOMSORDEPARTMENT",property="cospomsordepartment"),
    	@Result(column="IMPLEMENTAIONSTEPS",property="implementaionsteps"),
    	@Result(column="AUDITREQUIREMENTS",property="auditrequirements"),
    	@Result(column="PROJECTTYPE",property="projecttype"),
    	@Result(column="demporgid",property="cosdepartemnt.orgid"),
    	@Result(column="dempname",property="cosdepartemnt.orgname"),
    	@Result(column="PLANID",property="tblnbsjPlan.planid"),
    	@Result(column="PLANNAME",property="tblnbsjPlan.planname"),
    	@Result(column="TEMPLETENAME",property="tbltemplete.templeteName"),
    	@Result(column="TEMPLETENAMEZY",property="tbltempletezy.templeteName"),
    	@Result(column="TEMPSJID",property="tbltemplete.templeteId"),
    	@Result(column="TEMPZYID",property="tbltempletezy.templeteId"),
    	
    	@Result(column="CREATEUSERNAME",property="createUserName"),
    	@Result(column="PRO_SJFS",property="proSjfs"),
    	@Result(column="PRO_DESC",property="proDesc"),
    	
    	@Result(column="TARGETNAME",property="targetName"),
    	
    	@Result(column="AUDITORGNAME",property="auditOrgName"),
    	@Result(column="AUDITSTAFFNAME",property="auditStaffName"),
    	
    })
	List<TblNbsjProject> selectProjectListInfoByWspJhw(@Param("planId")Integer planId);
	
	
	
	@Update("UPDATE TBL_NBSJ_PROJECT SET PLANID=#{planId} "
    		+ " WHERE projecttype = '计划外' "
    		+ " AND (examineType=0 OR examineType=1) and ORGID =${orgid}")
	void updatePjPlanByWspJhw(Integer planId,BigDecimal orgid);


}
