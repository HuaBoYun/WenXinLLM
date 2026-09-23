package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.oracle.vo.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjArchiveEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.vo.result.QualityParam;

import cn.hutool.json.JSONObject;


public interface TblNbsjProjectMapper extends BaseMapper<TblNbsjProject> {
	
	
	
	List<TblNbsjProject> findList(@Param("tblnbsjProjectVo") TblnbsjProjectVo tblnbsjProjectVo);
	
	
	 @SelectProvider(method = "selectProjectgdListByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
	    @Results({
	            @Result(column = "PROJECTID", property = "projectId"),
	            @Result(column = "PRJOECTNAME", property = "prjoectName"),
	            @Result(column = "PROJECTCODE", property = "projectCode"),
	            @Result(column = "PROJECTSOURCE", property = "projectSource"),
	            @Result(column = "STATUS", property = "status"),
	            @Result(column = "STARTDATE", property = "startDate"),
	            @Result(column = "ENDDATE", property = "endDate"),
	            @Result(column = "COSTS", property = "costs"),
	            @Result(column = "STAFFID", property = "pmStaff.staffid", id = true),
	            @Result(column = "REALNAME", property = "pmStaff.realname"),
	            @Result(column = "EXAMINETYPE", property = "examineType", id = true),
	            @Result(column = "DAYS", property = "days"),
	    })
	List<TblNbsjProject> findgdList(TblnbsjProjectVo tblnbsjProjectVo);
	
	List<TblNbsjProject> findrwList(@Param("tblnbsjProjectRwfpVo") TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo);
	
	
	  @SelectProvider(method="selectZgListByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
		@Results({
			@Result(column="PROJECTID",property="projectId"),
			@Result(column="PRJOECTNAME",property="prjoectName"),
			@Result(column="PROJECTCODE",property="projectCode"),
			@Result(column="PROJECTSOURCE",property="projectSource"),
			@Result(column="STATUS",property="status"),
			@Result(column="STARTDATE",property="startDate"),
			@Result(column="ENDDATE",property="endDate"),
			@Result(column="COSTS",property="costs"),
			@Result(column="STAFFID",property="pmStaff.staffid",id=true),
	    	@Result(column="REALNAME",property="pmStaff.realname"),
	    	@Result(column="EXAMINETYPE",property="examineType",id=true),
	    	@Result(column="DAYS",property="days"),
		})
		List<TblNbsjProject> selectZgListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo project) throws Exception;

		@SelectProvider(method="selectZgCountByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
		Integer selectZgCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo project) throws Exception;
	
	
	
	@SelectProvider(method="selectProjectgdListByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
	@Results({
		@Result(column="PROJECTID",property="projectId"),
		@Result(column="PRJOECTNAME",property="prjoectName"),
		@Result(column="PROJECTCODE",property="projectCode"),
		@Result(column="PROJECTSOURCE",property="projectSource"),
		@Result(column="STATUS",property="status"),
		@Result(column="STARTDATE",property="startDate"),
		@Result(column="ENDDATE",property="endDate"),
		@Result(column="COSTS",property="costs"),
		@Result(column="STAFFID",property="pmStaff.staffid",id=true),
    	@Result(column="REALNAME",property="pmStaff.realname"),
    	@Result(column="EXAMINETYPE",property="examineType",id=true),
    	@Result(column="DAYS",property="days"),
	})
	List<TblNbsjProject> selectProjectgdListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo,String projectStartDate, String projectEndDate, TblnbsjProjectVo project) throws Exception;



	@SelectProvider(method="selectProjectgdListByPageCount",type=TblNbsjProjectMapperSqlConfig.class)
	Integer selectProjectgdListByPageCount(String projectStartDate,String projectEndDate, TblnbsjProjectVo project) throws Exception;
	

	@Update("UPDATE TBL_NBSJ_PROJECT SET IMPLEMENTTIME = TO_DATE(#{implementTime}, 'YYYY-MM-DD HH24:MI:SS') WHERE PROJECTID = #{projectId}")
	void updateImplementTime(BigDecimal projectId, String implementTime) throws Exception;
	
	@SelectProvider(method="selectAuditFileListPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
	@Results({
		@Result(column="PROJECTID",property="projectId"),
		@Result(column="PRJOECTNAME",property="prjoectName"),
		@Result(column="AUDITTYPE",property="auditType"),
		@Result(column="PROJECTSOURCE",property="projectSource"),
		@Result(column="STATUS",property="status"),
		@Result(column="STARTDATE",property="startDate"),
		@Result(column="ENDDATE",property="endDate"),
		@Result(column="ASSIGBEDCONTROLTIME",property="assigbedControlTime"),
		@Result(column="PMSTAFFID",property="pmStaff.staffid",id=true),
    	@Result(column="PMREALNAME",property="pmStaff.realname"),
    	@Result(column="AUORGID",property="auditOrgInfo.orgid",id=true),
    	@Result(column="AUORGNAME",property="auditOrgInfo.orgname"),
	})
	List<TblNbsjProject> selectAuditFileListPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo,String projectName, BigDecimal orgid) throws Exception;

	@SelectProvider(method="selectAuditFileCountByPage",type=TblNbsjProjectMapperSqlConfig.class)
	Integer selectAuditFileCountByPage(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, String projectName,BigDecimal orgid) throws Exception;

	@SelectProvider(method="selectProjectListByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
	@Results({
		@Result(column="PROJECTID",property="projectId"),
		@Result(column="PRJOECTNAME",property="prjoectName"),
		@Result(column="PROJECTCODE",property="projectCode"),
		@Result(column="PROJECTSOURCE",property="projectSource"),
		@Result(column="STATUS",property="status"),
		@Result(column="STARTDATE",property="startDate"),
		@Result(column="ENDDATE",property="endDate"),
		@Result(column="COSTS",property="costs"),
		@Result(column="STAFFID",property="pmStaff.staffid",id=true),
    	@Result(column="REALNAME",property="pmStaff.realname"),
    	@Result(column="EXAMINETYPE",property="examineType",id=true),
    	@Result(column="DAYS",property="days"),
	})
	List<TblNbsjProject> selectProjectListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo,String projectStartDate, String projectEndDate, TblnbsjProjectVo project) throws Exception;

	@SelectProvider(method="selectProjectCountByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
	Integer selectProjectCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, String projectStartDate,String projectEndDate, TblnbsjProjectVo project) throws Exception;
	
	@Select("SELECT TNP.SECRECTLEVELID,TNP.STAFFSCOPEIDS,TNP.STAFFSCOPENAMES,TNP.PRJOECTNAME,TNP.PROJECTCODE,TNP.PROJECTSOURCE,TNP.STATUS,TNP.STARTDATE,TNP.ENDDATE,TS.STAFFID PMSTAFFID,TS.REALNAME PMREALNAME FROM TBL_NBSJ_PROJECT TNP LEFT JOIN TBL_STAFF TS ON TS.STAFFID = TNP.PMID WHERE PLANID = #{planid}")
	@Results({
		@Result(column="PRJOECTNAME",property="prjoectName"),
		@Result(column="PROJECTCODE",property="projectCode"),
		@Result(column="PROJECTSOURCE",property="projectSource"),
		@Result(column="STATUS",property="status"),
		@Result(column="STARTDATE",property="startDate"),
		@Result(column="ENDDATE",property="endDate"),
		@Result(column="PMSTAFFID",property="pmStaff.staffid",id=true),
    	@Result(column="PMREALNAME",property="pmStaff.realname"),
	})
	List<TblNbsjProject> selectProjectListInfoByPlanId(BigDecimal planid) throws Exception;
	
	 @Select("SELECT PROJECTID,PRJOECTNAME,PROJECTCODE,PROJECTSOURCE,STATUS,STARTDATE,ENDDATE,COSTS,EXAMINETYPE,(ENDDATE-STARTDATE)+1 DAYS   from TBL_NBSJ_PROJECT WHERE AUDITSTAFFID=#{staffid}")
	 List<TblNbsjProject> selectBystaffid(BigDecimal staffid) throws Exception;
	
	
	 
	@Select("SELECT * from TBL_NBSJ_PROJECT WHERE PROJECTID= #{projectId} ")
	TblNbsjProject getById(String projectId);
    
    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;

    @Select("SELECT TNA.* FROM TBL_NBSJ_PROJECT TNA  WHERE TNA.PROJECTID = #{projectId}")
    @Results({
    	@Result(column="PROJECTID",property="projectId"),
    	@Result(column="PRJOECTNAME",property="prjoectName"),
    	@Result(column="PLANYEAR",property="planYear"),
    	@Result(column="PROJECTSOURCE",property="projectSource"),
    	@Result(column="STARTDATE",property="startDate"),
    	@Result(column="ENDDATE",property="endDate"),
    	@Result(column="AUDITTYPE",property="auditType"),
    	@Result(column="PROJECTCODE",property="projectCode"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="PMID",property="pmId"),
    	@Result(column="TEMPID",property="tempId"),
    	
    })
    TblNbsjProject selectById(BigDecimal projectId) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
    @Results({
    	@Result(column="PROJECTID",property="projectId"),
    	@Result(column="PRJOECTNAME",property="prjoectName"),
    	@Result(column="PLANYEAR",property="planYear"),
    	@Result(column="PROJECTSOURCE",property="projectSource"),
    	@Result(column="STARTDATE",property="startDate"),
    	@Result(column="ENDDATE",property="endDate"),
    	@Result(column="AUDITTYPE",property="auditType"),
    	@Result(column="PROJECTCODE",property="projectCode"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="PMID",property="pmId"),
    	@Result(column="REALNAME",property="pmStaff.realname"),
    	@Result(column="ORGNAME",property="auditOrgInfo.orgname"),
    })
	List<TblNbsjProject> selectListByPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;

    
    @Select("SELECT * from TBL_NBSJ_PROJECT WHERE AUDITTYPE= #{type} and AUDITORGID=#{orgid} ")
    List<TblNbsjProject>  findByNbsjLx(@Param("type")String type,@Param("orgid")String orgid);
    
    
    
    
    
    //档案列表-个人
    @SelectProvider(method="selectSjgdNewCountByStaffidPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
   	Integer selectSjgdNewCountByStaffidPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
    
    @SelectProvider(method="selectSjgdNewListByStaffidPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
    @Results({
    	@Result(column="PROJECTID",property="projectId"),
    	@Result(column="PRJOECTNAME",property="prjoectName"),
    	@Result(column="PROJECTCODE",property="projectCode"),
    	@Result(column="PLANYEAR",property="planYear"),
    	@Result(column="ORGIDNAMES",property="orgIdNames"),
    	@Result(column="ORGNAME",property="auditOrgInfo.orgname"),
    	@Result(column="SJORGNAME",property="orgInfo.orgname"),
//    	orgname
    })
	List<TblNbsjProject> selectSjgdNewListByStaffidPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
    //档案列表
    @SelectProvider(method="selectSjgdNewCountByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
    Integer selectSjgdNewCountByPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
    
    @SelectProvider(method="selectSjgdNewListByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
    @Results({
    	@Result(column="PROJECTID",property="projectId"),
    	@Result(column="PRJOECTNAME",property="prjoectName"),
    	@Result(column="PROJECTCODE",property="projectCode"),
    	@Result(column="PLANYEAR",property="planYear"),
    	@Result(column="ORGIDNAMES",property="orgIdNames"),
    	@Result(column="ORGNAME",property="auditOrgInfo.orgname"),
    	@Result(column="SJORGNAME",property="orgInfo.orgname"),
//    	orgname
    })
    List<TblNbsjProject> selectSjgdNewListByPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
    
    //档案借阅
    @SelectProvider(method="selectDajyNewCountByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
   	Integer selectDajyNewCountByPageInfo(PageInfo<TblNbsjProject> pageInfo,BigDecimal orgid,BigDecimal staffid,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
    
    @SelectProvider(method="selectDajyNewListByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
    @Results({
    	@Result(column="PROJECTID",property="projectId"),
    	@Result(column="PRJOECTNAME",property="prjoectName"),
    	@Result(column="PROJECTCODE",property="projectCode"),
    	@Result(column="PLANYEAR",property="planYear"),
    	
    	@Result(column="ORGNAME",property="auditOrgInfo.orgname"),
    	@Result(column="SJORGNAME",property="orgInfo.orgname"),
    	@Result(column="ORGIDNAMES",property="orgIdNames"),
    	@Result(column="PSTATUS",property="pStatus"),
//    	orgname
    })
	List<TblNbsjProject> selectDajyNewListByPageInfo(PageInfo<TblNbsjProject> pageInfo,BigDecimal orgid,BigDecimal staffid,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
    
    //借阅日志
    @SelectProvider(method="selectJyrzNewCountByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
   	Integer selectJyrzNewCountByPageInfo(PageInfo<TblNbsjProject> pageInfo,Integer orgid,Integer staffid,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
    
    @SelectProvider(method="selectJyrzNewListByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
    @Results({
    	@Result(column="PROJECTID",property="projectId"),
    	@Result(column="PRJOECTNAME",property="prjoectName"),
    	@Result(column="PROJECTCODE",property="projectCode"),
    	@Result(column="PLANYEAR",property="planYear"),
    	@Result(column="PCNT",property="pCount"),
    	@Result(column="ORGIDNAMES",property="orgIdNames"),
    	@Result(column="ORGNAME",property="auditOrgInfo.orgname"),
    	@Result(column="SJORGNAME",property="orgInfo.orgname"),
    	
//    	orgname
    })
	List<TblNbsjProject> selectJyrzNewListByPageInfo(PageInfo<TblNbsjProject> pageInfo,Integer orgid,Integer staffid,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
    
    
    
    
    @SelectProvider(method="selectPJListByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
	@Results({
		@Result(column="PROJECTID",property="projectId"),
		@Result(column="PRJOECTNAME",property="prjoectName"),
		@Result(column="PROJECTCODE",property="projectCode"),
		@Result(column="PROJECTSOURCE",property="projectSource"),
		@Result(column="STATUS",property="status"),
		@Result(column="STARTDATE",property="startDate"),
		@Result(column="ENDDATE",property="endDate"),
		@Result(column="COSTS",property="costs"),
		@Result(column="STAFFID",property="pmStaff.staffid",id=true),
    	@Result(column="REALNAME",property="pmStaff.realname"),
    	@Result(column="EXAMINETYPE",property="examineType",id=true),
    	@Result(column="DAYS",property="days"),
	})
	List<TblNbsjProject> selectPJListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo project, String sortFields, String sortFlag, TblStaffUtil loginStaff) throws Exception;

	@SelectProvider(method="selectPJCountByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
	Integer selectPJCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo project, TblStaffUtil loginStaff) throws Exception;

	/**
	 * 查询审计项目数详细数据 - 统一接口(支持按公司/审计类型/状态/月份查询)
	 */
	@SelectProvider(method="selectPlancountDetailList",type=TblNbsjProjectMapperSqlConfig.class)
	List<TblNbsjProject> selectPlancountDetailList(String username,String year, String companyName, String auditType, String status, String month) throws Exception;
	
	@SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjProjectMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblNbsjProject plan) throws Exception;
	
	
	@Delete("DELETE FROM TBL_NBSJ_PROJECT WHERE PROJECTID = #{projectid}")
    void deleteById(BigDecimal projectid) throws Exception;
	
	@Delete("DELETE FROM TBL_NBSJ_PRO_TEAM WHERE PROJECTID = #{projectid}")
    void deleteLinkTeamById(BigDecimal projectid) throws Exception;
	
    @InsertProvider(method="insertEntity",type=TblNbsjProjectMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="projectId", keyColumn="PROJECTID")
	void insertEntity(TblNbsjProject plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblNbsjProjectMapperSqlConfig.class)
	void updateEntity(TblNbsjProject plan) throws Exception;
    
    @Select("SELECT TNA.*,TS.REALNAME,PLAN.PLANNAME,TEMP.TEMPLETENAME,TEMPZY.TEMPLETENAME TEMPLETENAMEZY,"
			+ "TEMP.TEMPLETEID TEMPSJID,TEMPZY.TEMPLETEID TEMPZYID,createUser.REALNAME CREATEUSERNAME "
			+ " FROM TBL_NBSJ_PROJECT TNA "
			+ " LEFT JOIN TBL_NBSJ_AUDITPLAN PLAN ON PLAN.PLANID = TNA.PLANID "
			+ " LEFT JOIN TBL_NBSJ_TEMPLETE TEMP ON TEMP.TEMPLETEID = TNA.TEMPID "
			+ " LEFT JOIN TBL_NBSJ_TEMPLETE TEMPZY ON TEMPZY.TEMPLETEID = TNA.TEMPZYID "
			+ " LEFT JOIN TBL_STAFF TS ON TNA.PMID = TS.STAFFID "
			+ " LEFT JOIN TBL_STAFF createUser ON TNA.CREATESTAFFID = createUser.STAFFID "
			+ " WHERE TNA.PROJECTID = #{projectId} ")
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

	})
	TblNbsjProject selectPJById(@Param("projectId") BigDecimal projectId) throws Exception;
    
    
  //项目任务分配
    @SelectProvider(method="selectPJRwfpListByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
	@Results({
		@Result(column="PROJECTID",property="projectId"),
		@Result(column="PRJOECTNAME",property="prjoectName"),
		@Result(column="PROJECTCODE",property="projectCode"),
		@Result(column="PROJECTSOURCE",property="projectSource"),
		@Result(column="STATUS",property="status"),
		@Result(column="STARTDATE",property="startDate"),
		@Result(column="ENDDATE",property="endDate"),
		@Result(column="COSTS",property="costs"),
		@Result(column="STAFFID",property="pmStaff.staffid",id=true),
    	@Result(column="REALNAME",property="pmStaff.realname"),
    	@Result(column="ORGNAME",property="auditOrgInfo.orgname"),
    	@Result(column="EXAMINETYPE",property="examineType",id=true),
	})
	List<TblNbsjProject> selectPJRwfpListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo, String sortFields, String sortFlag,TblStaffUtil loginStaff) throws Exception;

	@SelectProvider(method="selectPJRwfpCountByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
	Integer selectPJRwfpCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo,TblStaffUtil loginStaff) throws Exception;
    
	
	
	//项目执行一览
    @SelectProvider(method="selectPJZxylListByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
	@Results({
		@Result(column="PROJECTID",property="projectId"),
		@Result(column="PRJOECTNAME",property="prjoectName"),
		@Result(column="PROJECTCODE",property="projectCode"),
		@Result(column="STARTDATE",property="startDate"),
		@Result(column="ENDDATE",property="endDate"),
		@Result(column="PLANNAME",property="planName"),
		@Result(column="PLANCODE",property="planCode"),
		
		
	})
	List<TblNbsjProject> selectPJZxylListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectZXYLVo tblnbsjProjectZXYLVo) throws Exception;

	@SelectProvider(method="selectPJZxylCountByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
	Integer selectPJZxylCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectZXYLVo tblnbsjProjectZXYLVo) throws Exception;
	
	
	
	//==查询当前实施项目
	@Select("SELECT * from TBL_NBSJ_PROJECT WHERE STATUS= 2 ")//
	List<TblNbsjProject> getProjectBySS();
	
	@UpdateProvider(method="updatePjSS",type=TblNbsjProjectMapperSqlConfig.class)
	void updatePjSS(Integer projectid) throws Exception;
	
	
	//==
	@UpdateProvider(method="updatePjPm",type=TblNbsjProjectMapperSqlConfig.class)
	void updatePjPm(BigDecimal pmId,BigDecimal projectid) throws Exception;
	
	//==
	@UpdateProvider(method="updatePjStart",type=TblNbsjProjectMapperSqlConfig.class)
	void updatePjStart(BigDecimal projectid) throws Exception;
	
	
	//==
	@Select("SELECT *  from TBL_NBSJ_ARCHIVE ARC WHERE ARC.PROJECTID=#{projectid} ")//
	List<TblNbsjArchiveEntity> getTblNbsjArchiveList(BigDecimal projectid);
	
	//==
	@UpdateProvider(method="updateFpStatus",type=TblNbsjProjectMapperSqlConfig.class)
	void updateFpStatus(Integer fpStatus,BigDecimal projectId) throws Exception;
	
	
	//==
	@SelectProvider(method="selectAuditPlanListByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
    @Results({
    	@Result(column="PROJECTID",property="projectId"),
    	@Result(column="PRJOECTNAME",property="prjoectName"),
    	@Result(column="PLANYEAR",property="planYear"),
    	@Result(column="PROJECTSOURCE",property="projectSource"),
    	@Result(column="STARTDATE",property="startDate"),
    	@Result(column="ENDDATE",property="endDate"),
    	@Result(column="AUDITTYPE",property="auditType"),
    	@Result(column="PROJECTCODE",property="projectCode"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="PMID",property="pmId"),
    	@Result(column="REALNAME",property="pmStaff.realname"),
    	@Result(column="ORGNAME",property="auditOrgInfo.orgname"),
    })
	List<TblNbsjProject> selectAuditPlanListByPageInfo(PageInfo<TblNbsjProject> pageInfo,String  projectname,BigDecimal orgid) throws Exception;
	
	@SelectProvider(method="selectAuditPlanCountByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
   	Integer selectAuditPlanCountByPageInfo(PageInfo<TblNbsjProject> pageInfo,String  projectname,BigDecimal orgid) throws Exception;
	
    @SelectProvider(method="getGkProjectInfoList",type=TblNbsjProjectMapperSqlConfig.class)
	List<Map<String, Object>> getGkProjectInfo(com.hbfk.util.PageInfo<Map<String, Object>> pageInfo,TblGkProjectVo project) throws Exception;
    
    @SelectProvider(method="getGkProjectInfoListCount",type=TblNbsjProjectMapperSqlConfig.class)
    Integer getGkProjectInfoListCount(TblGkProjectVo project) throws Exception;
    
    @SelectProvider(method="getGkQuestionInfoList",type=TblNbsjProjectMapperSqlConfig.class)
    List<Map<String, Object>> getGkQuestionInfoList(com.hbfk.util.PageInfo<Map<String, Object>> pageInfo,TblGkQuestionVo question) throws Exception;
    
    @SelectProvider(method="getGkQuestionInfoListCount",type=TblNbsjProjectMapperSqlConfig.class)
    Integer getGkQuestionInfoListCount(TblGkQuestionVo question) throws Exception;
    
    @SelectProvider(method="getGkZgContentInfoList",type=TblNbsjProjectMapperSqlConfig.class)
    List<Map<String, Object>> getGkZgContentInfoList(com.hbfk.util.PageInfo<Map<String, Object>> pageInfo,TblGkZgQuestionVo question) throws Exception;
    
    @SelectProvider(method="getGkZgContentInfoListCount",type=TblNbsjProjectMapperSqlConfig.class)
    Integer getGkZgContentInfoListCount(TblGkZgQuestionVo question) throws Exception;
    
    
    @Delete("DELETE FROM TBL_LEGAL_PROJECT_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(BigDecimal attid);

    
    
    
    //@SelectProvider(method="selectAuditItems",type=TblNbsjProjectMapperSqlConfig.class)
	@Select("SELECT distinct TNA.*  FROM TBL_NBSJ_PROJECT TNA    WHERE 1=1   AND (PMID=#{staffid} or (TNA.projectid in ( SELECT distinct T.PROJECTID\n" +
			" FROM TBL_NBSJ_PROJECTTEAM TNA  LEFT JOIN TBL_NBSJ_PRO_TEAM T ON TNA.TEAMID = T.TEAMID  \n" +
			" LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID  WHERE TNS.STAFFID =#{staffid}) ) )")
    @Results({
    	@Result(column="PROJECTID",property="projectId"),
    	@Result(column="PRJOECTNAME",property="prjoectName"),
    	@Result(column="PLANYEAR",property="planYear"),
    	@Result(column="PROJECTSOURCE",property="projectSource"),
    	@Result(column="STARTDATE",property="startDate"),
    	@Result(column="ENDDATE",property="endDate"),
    	@Result(column="AUDITTYPE",property="auditType"),
    	@Result(column="PROJECTCODE",property="projectCode"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="PMID",property="pmId"),
    	@Result(column="REALNAME",property="pmStaff.realname"),
    	@Result(column="ORGNAME",property="auditOrgInfo.orgname"),
    })
	List<TblNbsjProject> selectAuditItems(BigDecimal staffid) throws Exception;
    
    
    @SelectProvider(method="selectIfGroup",type=TblNbsjProjectMapperSqlConfig.class)
	TblNbsjProject selectIfGroup(BigDecimal staffid,Integer projectid) throws Exception;
    
    
    @InsertProvider(method="insertEntityar",type=TblNbsjProjectMapperSqlConfig.class)
	void insertEntityar(TblNbsjArchiveEntity ar) throws Exception;
    
    
    
    @SelectProvider(method="selectListBytjsj",type=TblNbsjProjectMapperSqlConfig.class)
	List<Object[]> selectListBytjsj(String starttime,String endtime) throws Exception;

	@Select("SELECT DISTINCT \n" +
			"    p.PROJECTID,\n" +
			"    p.ORGIDNAMES AS ORGNAME ,\n" +
			"    p.PRJOECTNAME,\n" +
			"    p.STARTDATE,\n" +
			"    p.AUDITTYPE,\n" +
			"    s.REALNAME,\n" +
			"    o.ORGNAME AS ORGIDNAMES,\n" +
			"    p.STATUS,\n" +
			"    p.PROJECTSOURCE,\n" +
			"    p.ENDDATE,\n" +
			"    SUM(CASE WHEN z.CONCLUSION IN ('已整改', '已整改到位') THEN 1 ELSE 0 END) AS yzg,\n" +
			"    SUM(CASE WHEN z.CONCLUSION IN ('未整改', '已整改未到位') THEN 1 ELSE 0 END) AS wzg,\n" +
			"    SUM(CASE WHEN z.CONCLUSION IN ('已整改', '已整改到位', '未整改', '已整改未到位') THEN 1 ELSE 0 END) AS zs\n" +
			"FROM TBL_ZGZZ_ISSUESILIST i \n" +
			"    LEFT JOIN TBL_NBSJ_PROJECT p ON i.PROJECTID = p.PROJECTID\n" +
			"    LEFT JOIN TBL_RECTIFICATION_ISSUES r ON i.ISSUESID = r.ISSUESID\n" +
			"    LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL z ON r.RELAID = z.RELAID\n" +
			"    LEFT JOIN TBL_STAFF s ON s.STAFFID = p.PMID\n" +
			"    LEFT JOIN TBL_ORGANIZATION o ON o.ORGID = p.ORGID \n" +
			"WHERE year(P.CREATETIME) = #{year}\n" +
			"GROUP BY \n" +
			"    p.PROJECTID,\n" +
			"    p.ORGIDNAMES,\n" +
			"    p.PRJOECTNAME,\n" +
			"    p.STARTDATE, \n" +
			"    p.AUDITTYPE, \n" +
			"    s.REALNAME,\n" +
			"    o.ORGNAME, \n" +
			"    p.STATUS, \n" +
			"    p.PROJECTSOURCE,\n" +
			"    p.ENDDATE")
	List<SjjscDpVo> selectSjjscDpVoByYear(Integer year) throws Exception;

	@Select("SELECT COUNT(*) AS total_count\n" +
			"FROM (\n" +
			"    SELECT \n" +
			"        p.ORGIDNAMES,\n" +
			"        p.PRJOECTNAME,\n" +
			"        p.STARTDATE,\n" +
			"        p.AUDITTYPE,\n" +
			"        s.REALNAME,\n" +
			"        o.ORGNAME,\n" +
			"        p.STATUS,\n" +
			"        p.PROJECTSOURCE,\n" +
			"        p.ENDDATE,\n" +
			"        SUM(CASE WHEN z.CONCLUSION IN ('已整改', '已整改到位') THEN 1 ELSE 0 END) AS yzg,\n" +
			"        SUM(CASE WHEN z.CONCLUSION IN ('未整改', '已整改未到位') THEN 1 ELSE 0 END) AS wzg,\n" +
			"        SUM(CASE WHEN z.CONCLUSION IN ('已整改', '已整改到位', '未整改', '已整改未到位') THEN 1 ELSE 0 END) AS zs\n" +
			"    FROM TBL_ZGZZ_ISSUESILIST i \n" +
			"        LEFT JOIN TBL_NBSJ_PROJECT p ON i.PROJECTID = p.PROJECTID\n" +
			"        LEFT JOIN TBL_RECTIFICATION_ISSUES r ON i.ISSUESID = r.ISSUESID\n" +
			"        LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL z ON r.RELAID = z.RELAID\n" +
			"        LEFT JOIN TBL_STAFF s ON s.STAFFID = p.PMID\n" +
			"        LEFT JOIN TBL_ORGANIZATION o ON o.ORGID = p.ORGID \n" +
			"    WHERE year(P.CREATETIME) = #{year}\n" +
			"    GROUP BY \n" +
			"        p.ORGIDNAMES,\n" +
			"        p.PRJOECTNAME,\n" +
			"        p.STARTDATE, \n" +
			"        p.AUDITTYPE, \n" +
			"        s.REALNAME,\n" +
			"        o.ORGNAME, \n" +
			"        p.STATUS, \n" +
			"        p.PROJECTSOURCE,\n" +
			"        p.ENDDATE\n" +
			") AS subquery")
	Integer CountSjjscDpVoByYear(Integer year) throws Exception;

	@Select("SELECT a.ORGNAME, " +
			"           SUM(CASE WHEN z.CONCLUSION IN ('已整改', '已整改到位') THEN 1 ELSE 0 END) AS yzg," +
//			"           SUM(CASE WHEN z.CONCLUSION IN ('未整改', '已整改未到位') THEN 1 ELSE 0 END) AS wzg," +
			"           count(0)-SUM(CASE WHEN z.CONCLUSION IN ('已整改', '已整改到位') THEN 1 ELSE 0 END) AS wzg," +
//			"           SUM(CASE WHEN z.CONCLUSION IN ('已整改', '已整改到位', '未整改', '已整改未到位') THEN 1 ELSE 0 END) AS zs," +
			"           count(0)  AS zs," +
			"           SUM(CASE WHEN z.ISXH='1' THEN 1 ELSE 0 END) AS yxh," +
			"           SUM(CASE WHEN z.ISXH ='0'  OR z.ISXH is null   THEN 1 ELSE 0 END) AS wxh," +
			"           SUM(CASE WHEN z.ISXH IN ('1','0') or  z.ISXH is null THEN 1 ELSE 0 END) AS xhzs" +
			"    FROM TBL_ZGZZ_ISSUESILIST i  " +
			"    LEFT JOIN TBL_RECTIFICATION_ISSUES r ON i.ISSUESID = r.ISSUESID " +
			"    LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL z ON r.RELAID = z.RELAID " +
			"    LEFT JOIN (SELECT CASE WHEN i.AUDITOBJECTTYPE =3 THEN sta.REALNAME else o.ORGNAME END ORGNAME,i.ISSUESID FROM TBL_ZGZZ_ISSUESILIST i  "+ 
			"    LEFT JOIN TBL_ORGANIZATION o ON o.ORGID = i.AUDITOBJECTID  "+ 
			"    LEFT JOIN TBL_STAFF sta on STA.STAFFID=i.AUDITOBJECTID  WHERE i.ISSUESTYPE = 1) a ON a.ISSUESID=I.ISSUESID " +
			"    WHERE year(i.CREATETIME) = #{year} AND i.ISSUESTYPE = 1  " +
			"    GROUP BY a.ORGNAME")
	List<ZgwtVo> zgwt(Integer year) throws Exception;

	@Select("SELECT COUNT(*) AS total_count " +
			"FROM (" +
			"    SELECT a.ORGNAME," +
			"           SUM(CASE WHEN z.CONCLUSION IN ('已整改', '已整改到位') THEN 1 ELSE 0 END) AS yzg," +
//			"           SUM(CASE WHEN z.CONCLUSION IN ('未整改', '已整改未到位') THEN 1 ELSE 0 END) AS wzg," +
			"           count(0)-SUM(CASE WHEN z.CONCLUSION IN ('已整改', '已整改到位') THEN 1 ELSE 0 END) AS wzg," +
//			"           SUM(CASE WHEN z.CONCLUSION IN ('已整改', '已整改到位', '未整改', '已整改未到位') THEN 1 ELSE 0 END) AS zs," +
			"           count(0)  AS zs," +
			"           SUM(CASE WHEN z.ISXH='1' THEN 1 ELSE 0 END) AS yxh," +
			"           SUM(CASE WHEN z.ISXH ='0'  OR z.ISXH is null   THEN 1 ELSE 0 END) AS wxh," +
			"           SUM(CASE WHEN z.ISXH IN ('1','0') or  z.ISXH is null THEN 1 ELSE 0 END) AS xhzs" +
			"    FROM TBL_ZGZZ_ISSUESILIST i " +
			"         LEFT JOIN TBL_RECTIFICATION_ISSUES r ON i.ISSUESID = r.ISSUESID" +
			"         LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL z ON r.RELAID = z.RELAID" +
			"    LEFT JOIN (SELECT CASE WHEN i.AUDITOBJECTTYPE =3 THEN sta.REALNAME else o.ORGNAME END ORGNAME,i.ISSUESID FROM TBL_ZGZZ_ISSUESILIST i  "+ 
			"    LEFT JOIN TBL_ORGANIZATION o ON o.ORGID = i.AUDITOBJECTID  "+ 
			"    LEFT JOIN TBL_STAFF sta on STA.STAFFID=i.AUDITOBJECTID  WHERE i.ISSUESTYPE = 1) a ON a.ISSUESID=I.ISSUESID " +
			"    WHERE year(i.CREATETIME) = #{year} AND i.ISSUESTYPE = 1 " +
			"    GROUP BY a.ORGNAME" +
			") AS sub_query")
	Integer countZgwt(@Param("year") Integer year);

	//审计分析-年度查询条件
	@Select("SELECT DISTINCT TO_CHAR(CREATETIME,'YYYY') FROM TBL_NBSJ_PROJECT ORDER BY TO_CHAR(CREATETIME,'YYYY') DESC ")
	List<Integer> findReportYearList() throws Exception;
	 
	//审计分析-各公司审计项目数
	@Select("SELECT ORG.ORGNAME,COUNT(0) sl FROM TBL_NBSJ_PROJECT TNP LEFT JOIN TBL_ORGANIZATION ORG ON TNP.AUDITORGID = ORG.ORGID WHERE TO_CHAR(TNP.CREATETIME,'YYYY') =#{year}  GROUP BY ORG.ORGNAME ORDER BY ORG.ORGNAME ") 
	 @Results({
	    	@Result(column="ORGNAME",property="auditOrgName"),
	    	@Result(column="sl",property="sl"),
	    }) 
	List<TblNbsjProject> findNbsjProjectCountByCompanyId(Integer year) throws Exception;
	 
	 //审计分析-审计项目类型
	 @Select("SELECT AUDITTYPE,COUNT(0) sl FROM TBL_NBSJ_PROJECT WHERE   PLANYEAR = #{year}  GROUP BY AUDITTYPE ") 
	 @Results({
	    	@Result(column="AUDITTYPE",property="auditType"),
	    	@Result(column="sl",property="sl"),
	    })
	 List<TblNbsjProject> findAuditTypeCount(Integer year) throws Exception;

	 //审计分析-审计项目类型详细数据
	 @SelectProvider(method="findAuditTypeDetail",type=TblNbsjProjectMapperSqlConfig.class)
	 @Results({
	    	@Result(column="projectCode",property="projectCode"),
	    	@Result(column="prjoectName",property="prjoectName"),
	    	@Result(column="orgIdNames",property="orgIdNames"),
	    	@Result(column="auditType",property="auditType"),
	    	@Result(column="planYear",property="planYear"),
	    	@Result(column="realname",property="realname"),
	    	@Result(column="examineType",property="examineType"),
	    })
	 List<TblNbsjProject> findAuditTypeDetail(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, String year, String auditType) throws Exception;

	 @SelectProvider(method="selectAuditTypeDetailCount",type=TblNbsjProjectMapperSqlConfig.class)
	 Integer selectAuditTypeDetailCount(String year, String auditType) throws Exception;

	 //审计分析-审计项目情况表
	 @SelectProvider(method="findProjectItemReport",type=TblNbsjProjectMapperSqlConfig.class)
	 @Results({
	    	@Result(column="ORGNAME",property="auditOrgName"),
	    	@Result(column="PRJOECTNAME",property="prjoectName"),
	    	@Result(column="PLANYEAR",property="planYear"),
	    	@Result(column="PROJECTSOURCE",property="projectSource"),
	    	@Result(column="AUDITTYPE",property="auditType"),
	    	@Result(column="REALNAME",property="realname"),
	    	@Result(column="EXAMINETYPE",property="examineTypes"),
	    	@Result(column="ORGNAME",property="auditOrgName"),
	    })
	 List<TblNbsjProject> findProjectItemReport(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo,Integer year) throws Exception;
	 
	 
	 @SelectProvider(method="selectProjectItemReportCount",type=TblNbsjProjectMapperSqlConfig.class)
	 Integer selectProjectItemReportCount(Integer year) throws Exception;
    
    
	@SelectProvider(method="getObjBySql",type=TblNbsjProjectMapperSqlConfig.class)
	com.alibaba.fastjson.JSONObject getObjBySql(String noSql) throws Exception;
		
		
	@SelectProvider(method="getGkProjectInfoExport",type=TblNbsjProjectMapperSqlConfig.class)
	List<JSONObject> getGkProjectInfoExport(TblGkProjectVo project) throws Exception;	
	
	
	@SelectProvider(method="getGkQuestionInfoExport",type=TblNbsjProjectMapperSqlConfig.class)
	List<JSONObject>  getGkQuestionInfoExport(TblGkQuestionVo question) throws Exception;
	
	@SelectProvider(method="getGkZgContentInfoExport",type=TblNbsjProjectMapperSqlConfig.class)
	List<JSONObject>  getGkZgContentInfoExport(TblGkZgQuestionVo question) throws Exception;
	    
	@SelectProvider(method="selectPageInfoListByRectification",type=TblNbsjProjectMapperSqlConfig.class)
    @Results({
    	@Result(column="PROJECTID",property="planId"),
    	@Result(column="PROJECTCODE",property="planCode"),
    	@Result(column="PRJOECTNAME",property="planName"),
    })
	List<TblZgzzProjectVo> selectPageInfoListByRectification(TblZgzzProjectVo project);

	List<TblNbsjProject> selectSjgdNewListByPageInfoXml(@Param("tblnbsjProjectVo") TblnbsjProjectVo tblnbsjProjectVo,@Param("loginStaff")TblStaffUtil loginStaff,@Param("mjsql")String mjsql);

	List<TblNbsjProject> selectSjgdNewListByStaffidPageInfoXml(@Param("tblnbsjProjectVo")TblnbsjProjectVo tblnbsjProjectVo,@Param("loginStaff")TblStaffUtil loginStaff,@Param("mjsql")String mjsql);

	List<TblNbsjProject> selectDajyNewListByPageInfoXml(@Param("tblnbsjProjectVo")TblnbsjProjectVo tblnbsjProjectVo,@Param("loginStaff")TblStaffUtil loginStaff,@Param("mjsql")String mjsql);
	@Results({
			@Result(column="PROJECTID",property="projectId"),
			@Result(column="PRJOECTNAME",property="prjoectName"),
			@Result(column="PROJECTCODE",property="projectCode"),
			@Result(column="PLANYEAR",property="planYear"),
			@Result(column="PCNT",property="pCount"),
			@Result(column="ORGIDNAMES",property="orgIdNames"),
			@Result(column="ORGNAME",property="auditOrgInfo.orgname"),
			@Result(column="SJORGNAME",property="orgInfo.orgname"),

//    	orgname
	})
	List<TblNbsjProject> selectJyrzNewListByPageInfoXml(@Param("tblnbsjProjectVo")TblnbsjProjectVo tblnbsjProjectVo,@Param("loginStaff")TblStaffUtil loginStaff,@Param("mjsql")String mjsql);

	@Results({
		@Result(column="DAYNUMBER",property="daynumber"),
		@Result(column="PMNAME",property="pmname"),
	})
	List<TblNbsjProject> getGkProjectInfoXml(@Param("project") TblGkProjectVo project,@Param("strs")String strs,@Param("loginStaff")TblStaffUtil loginStaff,@Param("mjsql")String mjsql);

	
	@Results({
		@Result(column="QUESTITLE",property="questitle"),
		@Result(column="AUDITDISCOVERABLE",property="auditdiscoverable"),
		@Result(column="FINDREALNAME",property="findrealname"),
		@Result(column="INTERNALTYPE",property="internalType"),
	})
	List<TblNbsjProject> getGkQuestionInfoListXml(@Param("question") TblGkQuestionVo question,@Param("strs")String strs,@Param("loginStaff")TblStaffUtil loginStaff,@Param("mjsql")String mjsql);

	List<TblNbsjProject> getGkZgContentInfoListXml(@Param("question")TblGkZgQuestionVo question,@Param("strs")String strs,@Param("loginStaff")TblStaffUtil loginStaff,@Param("mjsql")String mjsql);

	List<TblNbsjProject> selectListByPageInfoXml(@Param("tblnbsjProjectVo") TblnbsjProjectVo tblnbsjProjectVo,@Param("sql")String sql);

	List<TblNbsjProject> selectAuditPlanListByPageInfoXml(@Param("projectname") String projectname, @Param("orgid")BigDecimal orgid);
	
	
	
	/**
	 * 查询本年每月项目数量
	 * @param queryYear
	 * @return
	 * @throws Exception
	 */
	@Select("SELECT to_number(aa.yf) yf,count(0) sl from ( SELECT PROJECTID,to_char(CREATETIME,'MM') yf from "
			+ " TBL_NBSJ_PROJECT WHERE PLANYEAR=#{queryYear} AND CREATETIME is not NULL) aa  GROUP BY aa.yf ")
	List<QualityParam> selectPlanyfcount(@Param("queryYear")Integer queryYear) throws Exception;
	
	
	
	/**
	 * 查询本年项目状态数量
	 * @param queryYear
	 * @return
	 * @throws Exception
	 */
	@Select(" SELECT AA.status,count(*) sl from ( SELECT pl.PROJECTID, CASE WHEN PL.STATUS='1' THEN '已启动'  WHEN PL.STATUS='2' THEN '实施中' WHEN PL.STATUS='4' OR PL.STATUS='3'  THEN '已完成' "
			+ " else '未启动' END status FROM TBL_NBSJ_PROJECT  pl WHERE 1=1   AND PLANYEAR= #{queryYear} ) aa GROUP BY AA.status ")
	List<QualityParam> selectPlanZtcount(@Param("queryYear")Integer queryYear) throws Exception;	
	
	
	
	/**
	 * 查询本年度计划项目数
	 * @param queryYear
	 * @return
	 * @throws Exception
	 */
	@Select("SELECT AA.bsjdw,count(0) ZS,sum(AA.ywc) WCS,count(0)-sum(AA.ywc) WWCS  from ( "
			+ " SELECT PL.PROJECTID,CASE WHEN PL.AUDITSTAFFID!=null THEN STA.REALNAME WHEN PL.AUDITORGID!=null THEN ORG.ORGNAME ELSE PL.ORGIDNAMES END bsjdw,"
			+ " (SELECT count(0) from TBL_NBSJ_PROJECT zx WHERE PL.PROJECTID=PROJECTID AND (ZX.STATUS=3 or ZX.STATUS=4)) ywc "
			+ " from TBL_NBSJ_PROJECT pl  LEFT JOIN TBL_ORGANIZATION org on PL.AUDITORGID=ORG.ORGID "
			+ " LEFT JOIN TBL_STAFF sta on PL.AUDITSTAFFID=STA.STAFFID "
			+ " WHERE PL.PLANYEAR= #{queryYear} ) aa where AA.bsjdw is not NULL GROUP BY AA.bsjdw ")
	List<QualityParam> selectPlancount(@Param("queryYear")Integer queryYear) throws Exception;

	/**
	 * 查询指定公司的审计项目详细数据
	 * @param year 查询年度
	 * @param companyName 被审计单位名称
	 * @return 审计项目详细数据列表
	 * @throws Exception
	 */
	@Select("SELECT "
			+ "CASE "
			+ "WHEN PL.AUDITSTAFFID IS NOT NULL THEN STA.REALNAME "
			+ "WHEN PL.AUDITORGID IS NOT NULL THEN ORG.ORGNAME "
			+ "ELSE PL.ORGIDNAMES "
			+ "END AS bsjdw, "
			+ "PL.PRJOECTNAME AS projectname, "
			+ "CASE WHEN (PL.STATUS = 3 OR PL.STATUS = 4) THEN '1' ELSE '0' END AS wcs, "
			+ "CASE WHEN (PL.STATUS != 3 AND PL.STATUS != 4) THEN '1' ELSE '0' END AS wwcs, "
			+ "'1' AS zs "
			+ "FROM TBL_NBSJ_PROJECT PL "
			+ "LEFT JOIN TBL_ORGANIZATION ORG ON PL.AUDITORGID = ORG.ORGID "
			+ "LEFT JOIN TBL_STAFF STA ON PL.AUDITSTAFFID = STA.STAFFID "
			+ "WHERE PL.PLANYEAR = #{year} "
			+ "AND (CASE WHEN PL.AUDITSTAFFID IS NOT NULL THEN STA.REALNAME "
			+ "WHEN PL.AUDITORGID IS NOT NULL THEN ORG.ORGNAME "
			+ "ELSE PL.ORGIDNAMES END = #{companyName})")
	List<QualityParam> selectPlancountDetail(@Param("year") String year, @Param("companyName") String companyName) throws Exception;


	Object selectSjgdNewListByPageInfoXml(TblnbsjProjectVo tblnbsjProjectVo, TblStaffUtil loginStaff);

	/**
	 * 整改问题详细数据查询(支持按主管部门和查询类型筛选)
	 * @param year 年度
	 * @param orgName 主管部门名称
	 * @param projectId 项目ID
	 * @param audittype 审计类型
	 * @param queryType 查询类型: yzg(已整改) | wzg(未整改) | zs(整改总数) | yxh(已销号) | wxh(未销号) | xhzs(销号总数)
	 * @param pageInfo 分页对象
	 * @return 整改问题详细列表
	 */
	@SelectProvider(type = TblNbsjProjectMapperSqlConfig.class, method = "findZgwtDetail")
	@Results({
		@Result(column = "issueId", property = "issueId"),
		@Result(column = "issueCode", property = "issueCode"),
		@Result(column = "issueName", property = "issueName"),
		@Result(column = "issueTitle", property = "issueTitle"),
		@Result(column = "questionMemo", property = "questionMemo"),
		@Result(column = "projectId", property = "projectId"),
		@Result(column = "orgIdNames", property = "orgIdNames"),
		@Result(column = "resultStatus", property = "resultStatus"),
		@Result(column = "projectName", property = "projectName"),
		@Result(column = "startDate", property = "startDate"),
		@Result(column = "auditType", property = "auditType"),
		@Result(column = "status", property = "status"),
		@Result(column = "projectSource", property = "projectSource"),
		@Result(column = "endDate", property = "endDate"),
		@Result(column = "staffId", property = "staffId"),
		@Result(column = "realName", property = "realName"),
		@Result(column = "mobilePhone", property = "mobilePhone"),
		@Result(column = "email", property = "email"),
		@Result(column = "orgId", property = "orgId"),
		@Result(column = "orgName", property = "orgName"),
		@Result(column = "fatherOrgId", property = "fatherOrgId"),
		@Result(column = "relaId", property = "relaId"),
		@Result(column = "rectificationPlan", property = "rectificationPlan"),
		@Result(column = "rectificationMeasures", property = "rectificationMeasures"),
		@Result(column = "planDeadline", property = "planDeadline"),
		@Result(column = "implId", property = "implId"),
		@Result(column = "implRectMeasures", property = "implRectMeasures"),
		@Result(column = "situationOverview", property = "situationOverview"),
		@Result(column = "achievement", property = "achievement"),
		@Result(column = "conclusion", property = "conclusion"),
		@Result(column = "implDeadline", property = "implDeadline"),
		@Result(column = "finishTime", property = "finishTime"),
	})
	List<ZgwtDetailVo> findZgwtDetail(@Param("year") Integer year,
									   @Param("orgName") String orgName,
									   @Param("projectId") String projectId,
									   @Param("audittype") String audittype,
									   @Param("projectsource") String projectsource,
									   @Param("queryType") String queryType,
									   @Param("pageInfo") com.hbfk.util.PageInfo<ZgwtDetailVo> pageInfo) throws Exception;

	/**
	 * 统计整改问题详细数据总数
	 * @param year 年度
	 * @param orgName 主管部门名称
	 * @param projectId 项目ID
	 * @param audittype 审计类型
	 * @param projectsource 审计来源
	 * @param queryType 查询类型
	 * @return 总数
	 */
	@SelectProvider(type = TblNbsjProjectMapperSqlConfig.class, method = "countZgwtDetail")
	Integer countZgwtDetail(@Param("year") Integer year,
							@Param("orgName") String orgName,
							@Param("projectId") String projectId,
							@Param("audittype") String audittype,
							@Param("projectsource") String projectsource,
							@Param("queryType") String queryType) throws Exception;

	/**
	 * 查询大屏审计项目列表
	 * @param pageInfo 分页对象
	 * @param year 年度
	 * @return 审计项目列表
	 * @throws Exception
	 */
	List<com.huabo.audit.oracle.vo.SjfxProjectListVo> findSjfxProjectList(com.hbfk.util.PageInfo<com.huabo.audit.oracle.vo.SjfxProjectListVo> pageInfo, @Param("year") Integer year) throws Exception;

	/**
	 * 统计大屏审计项目列表总数
	 * @param year 年度
	 * @return 总数
	 * @throws Exception
	 */
	Integer countSjfxProjectList(@Param("year") Integer year) throws Exception;

	/**
	 * 项目数趋势变化查询（最近12个月）
	 * @return 项目数趋势数据列表
	 * @throws Exception
	 */
	List<Map<String, Object>> selectProjectTrend() throws Exception;

	/**
	 * 整改问题状态统计查询
	 * @return 整改问题状态统计数据列表
	 * @throws Exception
	 */
	List<Map<String, Object>> selectIssuesStatusStatistics() throws Exception;


}
