package com.huabo.audit.oracle.mapper;

import cn.hutool.json.JSONObject;
import com.huabo.audit.oracle.entity.TblNbsjArchiveEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.vo.*;
import com.huabo.audit.util.PageInfo;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface TblNbsjProjectMapper extends tk.mybatis.mapper.common.Mapper<TblNbsjProject> {


    @SelectProvider(method = "selectZgListByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
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
    List<TblNbsjProject> selectZgListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo project) throws Exception;

    /**
     * 整改跟踪项目列表
     * @param re
     * @return
     * @throws Exception
     */
    List<TblNbsjProject> findZgList(@Param("re") TblnbsjProjectVo re) throws Exception;

    @SelectProvider(method = "selectZgCountByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    Integer selectZgCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo project) throws Exception;


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
    List<TblNbsjProject> selectProjectgdListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, String projectStartDate, String projectEndDate, TblnbsjProjectVo project) throws Exception;


    @SelectProvider(method = "selectProjectgdListByPageCount", type = TblNbsjProjectMapperSqlConfig.class)
    Integer selectProjectgdListByPageCount(String projectStartDate, String projectEndDate, TblnbsjProjectVo project) throws Exception;


    @Update("UPDATE TBL_NBSJ_PROJECT SET IMPLEMENTTIME = TO_DATE(#{implementTime}, 'YYYY-MM-DD HH24:MI:SS') WHERE PROJECTID = #{projectId}")
    void updateImplementTime(Integer projectId, String implementTime) throws Exception;

    @SelectProvider(method = "selectAuditFileListPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    @Results({
            @Result(column = "PROJECTID", property = "projectId"),
            @Result(column = "PRJOECTNAME", property = "prjoectName"),
            @Result(column = "AUDITTYPE", property = "auditType"),
            @Result(column = "PROJECTSOURCE", property = "projectSource"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "STARTDATE", property = "startDate"),
            @Result(column = "ENDDATE", property = "endDate"),
            @Result(column = "ASSIGBEDCONTROLTIME", property = "assigbedControlTime"),
            @Result(column = "PMSTAFFID", property = "pmStaff.staffid", id = true),
            @Result(column = "PMREALNAME", property = "pmStaff.realname"),
            @Result(column = "AUORGID", property = "auditOrgInfo.orgid", id = true),
            @Result(column = "AUORGNAME", property = "auditOrgInfo.orgname"),
    })
    List<TblNbsjProject> selectAuditFileListPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, String projectName, BigDecimal orgid) throws Exception;

    @SelectProvider(method = "selectAuditFileCountByPage", type = TblNbsjProjectMapperSqlConfig.class)
    Integer selectAuditFileCountByPage(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, String projectName, BigDecimal orgid) throws Exception;

    @SelectProvider(method = "selectProjectListByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
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
    List<TblNbsjProject> selectProjectListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, String projectStartDate, String projectEndDate, TblnbsjProjectVo project) throws Exception;

    /**
     * @param projectStartDate
     * @param projectEndDate
     * @param re
     * @return
     * @throws Exception
     */
    List<TblNbsjProject> findList(@Param("projectStartDate") String projectStartDate, @Param("projectEndDate") String projectEndDate, @Param("re") TblnbsjProjectVo re) throws Exception;

    @SelectProvider(method = "selectProjectCountByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    Integer selectProjectCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, String projectStartDate, String projectEndDate, TblnbsjProjectVo project) throws Exception;

    @Select("SELECT TNP.PRJOECTNAME,TNP.PROJECTCODE,TNP.PROJECTSOURCE,TNP.STATUS,TNP.STARTDATE,TNP.ENDDATE,TS.STAFFID PMSTAFFID,TS.REALNAME PMREALNAME FROM TBL_NBSJ_PROJECT TNP LEFT JOIN TBL_STAFF TS ON TS.STAFFID = TNP.PMID WHERE PLANID = #{planid}")
    @Results({
            @Result(column = "PRJOECTNAME", property = "prjoectName"),
            @Result(column = "PROJECTCODE", property = "projectCode"),
            @Result(column = "PROJECTSOURCE", property = "projectSource"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "STARTDATE", property = "startDate"),
            @Result(column = "ENDDATE", property = "endDate"),
            @Result(column = "PMSTAFFID", property = "pmStaff.staffid", id = true),
            @Result(column = "PMREALNAME", property = "pmStaff.realname"),
    })
    List<TblNbsjProject> selectProjectListInfoByPlanId(Integer planid) throws Exception;

    @Select("SELECT PROJECTID,PRJOECTNAME,PROJECTCODE,PROJECTSOURCE,STATUS,STARTDATE,ENDDATE,COSTS,EXAMINETYPE,(ENDDATE-STARTDATE)+1 DAYS   from TBL_NBSJ_PROJECT WHERE AUDITSTAFFID=#{staffid}")
    List<TblNbsjProject> selectBystaffid(Integer staffid) throws Exception;


    @Select("SELECT * from TBL_NBSJ_PROJECT WHERE PROJECTID= #{projectId} ")
    TblNbsjProject getById(String projectId);

    @SelectProvider(method = "selectCountByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo tblnbsjProjectVo) throws Exception;

    @Select("SELECT TNA.* FROM TBL_NBSJ_PROJECT TNA  WHERE TNA.PROJECTID = #{projectId}")
    @Results({
            @Result(column = "PROJECTID", property = "projectId"),
            @Result(column = "PRJOECTNAME", property = "prjoectName"),
            @Result(column = "PLANYEAR", property = "planYear"),
            @Result(column = "PROJECTSOURCE", property = "projectSource"),
            @Result(column = "STARTDATE", property = "startDate"),
            @Result(column = "ENDDATE", property = "endDate"),
            @Result(column = "AUDITTYPE", property = "auditType"),
            @Result(column = "PROJECTCODE", property = "projectCode"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "PMID", property = "pmId"),
            @Result(column = "TEMPID", property = "tempId"),

    })
    TblNbsjProject selectById(@Param("projectId") Integer projectId) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    @Results({
            @Result(column = "PROJECTID", property = "projectId"),
            @Result(column = "PRJOECTNAME", property = "prjoectName"),
            @Result(column = "PLANYEAR", property = "planYear"),
            @Result(column = "PROJECTSOURCE", property = "projectSource"),
            @Result(column = "STARTDATE", property = "startDate"),
            @Result(column = "ENDDATE", property = "endDate"),
            @Result(column = "AUDITTYPE", property = "auditType"),
            @Result(column = "PROJECTCODE", property = "projectCode"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "PMID", property = "pmId"),
            @Result(column = "REALNAME", property = "pmStaff.realname"),
            @Result(column = "ORGNAME", property = "auditOrgInfo.orgname"),
    })
    List<TblNbsjProject> selectListByPageInfo(PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo tblnbsjProjectVo) throws Exception;


    @Select("SELECT * from TBL_NBSJ_PROJECT WHERE AUDITTYPE= #{type} and AUDITORGID=#{orgid} ")
    List<TblNbsjProject> findByNbsjLx(@Param("type") String type, @Param("orgid") String orgid);


    //档案列表-个人
    @SelectProvider(method="selectSjgdNewCountByStaffidPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
   	Integer selectSjgdNewCountByStaffidPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
    
    @SelectProvider(method="selectSjgdNewListByStaffidVo",type=TblNbsjProjectMapperSqlConfig.class)
    @Results({
            @Result(column = "PROJECTID", property = "projectId"),
            @Result(column = "PRJOECTNAME", property = "prjoectName"),
            @Result(column = "PROJECTCODE", property = "projectCode"),
            @Result(column = "PLANYEAR", property = "planYear"),
            @Result(column = "ORGNAME", property = "auditOrgInfo.orgname"),
            @Result(column = "SJORGNAME", property = "orgInfo.orgname"),
//    	orgname
    })
	List<TblNbsjProject> selectSjgdNewListByStaffidVo(TblnbsjProjectVo tblnbsjProjectVo);
    //档案列表
    @SelectProvider(method="selectSjgdNewCountByPageInfo",type=TblNbsjProjectMapperSqlConfig.class)
    Integer selectSjgdNewCountByPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) throws Exception;
    
    @SelectProvider(method="selectSjgdNewListByVo",type=TblNbsjProjectMapperSqlConfig.class)
    @Results({
            @Result(column = "PROJECTID", property = "projectId"),
            @Result(column = "PRJOECTNAME", property = "prjoectName"),
            @Result(column = "PROJECTCODE", property = "projectCode"),
            @Result(column = "PLANYEAR", property = "planYear"),
            @Result(column = "ORGNAME", property = "auditOrgInfo.orgname"),
            @Result(column = "SJORGNAME", property = "orgInfo.orgname"),
//    	orgname
    })
    List<TblNbsjProject> selectSjgdNewListByVo(TblnbsjProjectVo tblnbsjProjectVo) ;
    
    //档案借阅
    @SelectProvider(method = "selectDajyNewCountByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    Integer selectDajyNewCountByPageInfo(PageInfo<TblNbsjProject> pageInfo, BigDecimal orgid, BigDecimal staffid, TblnbsjProjectVo tblnbsjProjectVo) throws Exception;

    @SelectProvider(method = "selectDajyNewListByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    @Results({
            @Result(column = "PROJECTID", property = "projectId"),
            @Result(column = "PRJOECTNAME", property = "prjoectName"),
            @Result(column = "PROJECTCODE", property = "projectCode"),
            @Result(column = "PLANYEAR", property = "planYear"),

            @Result(column = "ORGNAME", property = "auditOrgInfo.orgname"),
            @Result(column = "SJORGNAME", property = "orgInfo.orgname"),

            @Result(column = "PSTATUS", property = "pStatus"),
//    	orgname
    })
	List<TblNbsjProject> selectDajyNewListByPageInfo(BigDecimal orgid,BigDecimal staffid,TblnbsjProjectVo tblnbsjProjectVo) ;
    
    //借阅日志
    @SelectProvider(method = "selectJyrzNewCountByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    Integer selectJyrzNewCountByPageInfo(PageInfo<TblNbsjProject> pageInfo, Integer orgid, Integer staffid, TblnbsjProjectVo tblnbsjProjectVo) throws Exception;

    @SelectProvider(method = "selectJyrzNewListByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    @Results({
            @Result(column = "PROJECTID", property = "projectId"),
            @Result(column = "PRJOECTNAME", property = "prjoectName"),
            @Result(column = "PROJECTCODE", property = "projectCode"),
            @Result(column = "PLANYEAR", property = "planYear"),
            @Result(column = "PCNT", property = "pCount"),

            @Result(column = "ORGNAME", property = "auditOrgInfo.orgname"),
            @Result(column = "SJORGNAME", property = "orgInfo.orgname"),

//    	orgname
    })
	List<TblNbsjProject> selectJyrzNewListByPageInfo(Integer orgid,Integer staffid,TblnbsjProjectVo tblnbsjProjectVo);
    
    
    
    
    //==
    @SelectProvider(method = "selectPJListByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    @Results({
            @Result(column = "PROJECTID", property = "projectId"),
            @Result(column = "PRJOECTNAME", property = "prjoectName"),
            @Result(column = "PROJECTCODE", property = "projectCode"),
            @Result(column = "PROJECTSOURCE", property = "projectSource"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "COSTS", property = "costs"),
            @Result(column = "STAFFID", property = "pmStaff.staffid", id = true),
            @Result(column = "REALNAME", property = "pmStaff.realname"),
            @Result(column = "EXAMINETYPE", property = "examineType", id = true),
            @Result(column = "DAYS", property = "days"),
    })
    List<TblNbsjProject> selectPJListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo project) throws Exception;

    @SelectProvider(method = "selectPJCountByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    Integer selectPJCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo project) throws Exception;

    @SelectProvider(method = "selectPlanCodeByOrgid", type = TblNbsjProjectMapperSqlConfig.class)
    Integer selectPlanCodeByOrgid(TblNbsjProject plan) throws Exception;


    @Delete("DELETE FROM TBL_NBSJ_PROJECT WHERE PROJECTID = #{projectid}")
    void deleteById(Integer projectid) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_PRO_TEAM WHERE PROJECTID = #{projectid}")
    void deleteLinkTeamById(Integer projectid) throws Exception;

    @InsertProvider(method = "insertEntity", type = TblNbsjProjectMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "projectId", keyColumn = "PROJECTID")
    void insertEntity(TblNbsjProject plan) throws Exception;

    @UpdateProvider(method = "updateEntity", type = TblNbsjProjectMapperSqlConfig.class)
    void updateEntity(TblNbsjProject plan) throws Exception;

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
            + " WHERE TNA.PROJECTID = #{projectId} ")
    @Results({
            @Result(column = "PROJECTID", property = "projectId"),
            @Result(column = "PRJOECTNAME", property = "prjoectName"),
            @Result(column = "PPROJECTNAME", property = "pprojectName"),
            @Result(column = "PLANYEAR", property = "planYear"),
            @Result(column = "PROJECTSOURCE", property = "projectSource"),
            @Result(column = "STARTDATE", property = "startDate"),
            @Result(column = "ENDDATE", property = "endDate"),
            @Result(column = "AUDITTYPE", property = "auditType"),
            @Result(column = "PROJECTCODE", property = "projectCode"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "PMID", property = "pmId"),
            @Result(column = "REALNAME", property = "pmStaff.realname"),
            @Result(column = "IMPLEMENTAION", property = "implementaion"),
            @Result(column = "COSPOMSORDEPARTMENT", property = "cospomsordepartment"),
            @Result(column = "IMPLEMENTAIONSTEPS", property = "implementaionsteps"),
            @Result(column = "AUDITREQUIREMENTS", property = "auditrequirements"),
            @Result(column = "PROJECTTYPE", property = "projecttype"),
            @Result(column = "demporgid", property = "cosdepartemnt.orgid"),
            @Result(column = "dempname", property = "cosdepartemnt.orgname"),
            @Result(column = "PLANID", property = "tblnbsjPlan.planid"),
            @Result(column = "PLANNAME", property = "tblnbsjPlan.planname"),
            @Result(column = "TEMPLETENAME", property = "tbltemplete.templeteName"),
            @Result(column = "TEMPLETENAMEZY", property = "tbltempletezy.templeteName"),
            @Result(column = "TEMPSJID", property = "tbltemplete.templeteId"),
            @Result(column = "TEMPZYID", property = "tbltempletezy.templeteId"),
            @Result(column = "CREATEUSERNAME", property = "createUserName"),
            @Result(column = "PRO_SJFS", property = "proSjfs"),
            @Result(column = "PRO_DESC", property = "proDesc"),
            @Result(column = "TARGETNAME", property = "targetName"),
            @Result(column = "AUDITORGNAME", property = "auditOrgName"),
            @Result(column = "AUDITSTAFFNAME", property = "auditStaffName"),
            @Result(column = "CNTTYPE", property = "cntType"),

    })
    TblNbsjProject selectPJById(@Param("projectId") Integer projectId) throws Exception;


    //项目任务分配
    @SelectProvider(method = "selectPJRwfpListByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
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
            @Result(column = "ORGNAME", property = "auditOrgInfo.orgname"),
            @Result(column = "EXAMINETYPE", property = "examineType", id = true),
    })
    List<TblNbsjProject> selectPJRwfpListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo) throws Exception;

    @SelectProvider(method = "selectPJRwfpCountByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    Integer selectPJRwfpCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo) throws Exception;


    //项目执行一览
    @SelectProvider(method = "selectPJZxylListByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    @Results({
            @Result(column = "PROJECTID", property = "projectId"),
            @Result(column = "PRJOECTNAME", property = "prjoectName"),
            @Result(column = "PROJECTCODE", property = "projectCode"),
            @Result(column = "STARTDATE", property = "startDate"),
            @Result(column = "ENDDATE", property = "endDate"),
            @Result(column = "PLANNAME", property = "planName"),
            @Result(column = "PLANCODE", property = "planCode"),


    })
    List<TblNbsjProject> selectPJZxylListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectZXYLVo tblnbsjProjectZXYLVo) throws Exception;

    @SelectProvider(method = "selectPJZxylCountByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    Integer selectPJZxylCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectZXYLVo tblnbsjProjectZXYLVo) throws Exception;


    //==查询当前实施项目
    @Select("SELECT * from TBL_NBSJ_PROJECT WHERE STATUS= 2 ")
//
    List<TblNbsjProject> getProjectBySS();

    @UpdateProvider(method = "updatePjSS", type = TblNbsjProjectMapperSqlConfig.class)
    void updatePjSS(Integer projectid) throws Exception;


    //==
    @UpdateProvider(method = "updatePjPm", type = TblNbsjProjectMapperSqlConfig.class)
    void updatePjPm(BigDecimal pmId, Integer projectid) throws Exception;

    //==
    @UpdateProvider(method = "updatePjStart", type = TblNbsjProjectMapperSqlConfig.class)
    void updatePjStart(Integer projectid) throws Exception;


    //==
    @Select("SELECT *  from TBL_NBSJ_ARCHIVE ARC WHERE ARC.PROJECTID=#{projectid} ")
//
    List<TblNbsjArchiveEntity> getTblNbsjArchiveList(Integer projectid);

    //==
    @UpdateProvider(method = "updateFpStatus", type = TblNbsjProjectMapperSqlConfig.class)
    void updateFpStatus(Integer fpStatus, Integer projectId) throws Exception;


    //==
    @SelectProvider(method = "selectAuditPlanListByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    @Results({
            @Result(column = "PROJECTID", property = "projectId"),
            @Result(column = "PRJOECTNAME", property = "prjoectName"),
            @Result(column = "PLANYEAR", property = "planYear"),
            @Result(column = "PROJECTSOURCE", property = "projectSource"),
            @Result(column = "STARTDATE", property = "startDate"),
            @Result(column = "ENDDATE", property = "endDate"),
            @Result(column = "AUDITTYPE", property = "auditType"),
            @Result(column = "PROJECTCODE", property = "projectCode"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "PMID", property = "pmId"),
            @Result(column = "REALNAME", property = "pmStaff.realname"),
            @Result(column = "ORGNAME", property = "auditOrgInfo.orgname"),
    })
    List<TblNbsjProject> selectAuditPlanListByPageInfo(PageInfo<TblNbsjProject> pageInfo, String projectname, BigDecimal orgid) throws Exception;

    @SelectProvider(method = "selectAuditPlanCountByPageInfo", type = TblNbsjProjectMapperSqlConfig.class)
    Integer selectAuditPlanCountByPageInfo(PageInfo<TblNbsjProject> pageInfo, String projectname, BigDecimal orgid) throws Exception;

    @SelectProvider(method = "getGkProjectInfoList", type = TblNbsjProjectMapperSqlConfig.class)
    List<Map<String, Object>> getGkProjectInfo(com.hbfk.util.PageInfo<Map<String, Object>> pageInfo, TblGkProjectVo project) throws Exception;

    @SelectProvider(method = "getGkProjectInfoListCount", type = TblNbsjProjectMapperSqlConfig.class)
    Integer getGkProjectInfoListCount(TblGkProjectVo project) throws Exception;

    @SelectProvider(method = "getGkQuestionInfoList", type = TblNbsjProjectMapperSqlConfig.class)
    List<Map<String, Object>> getGkQuestionInfoList(com.hbfk.util.PageInfo<Map<String, Object>> pageInfo, TblGkQuestionVo question) throws Exception;

    @SelectProvider(method = "getGkQuestionInfoListCount", type = TblNbsjProjectMapperSqlConfig.class)
    Integer getGkQuestionInfoListCount(TblGkQuestionVo question) throws Exception;

    @SelectProvider(method = "getGkZgContentInfoList", type = TblNbsjProjectMapperSqlConfig.class)
    List<Map<String, Object>> getGkZgContentInfoList(com.hbfk.util.PageInfo<Map<String, Object>> pageInfo, TblGkZgQuestionVo question) throws Exception;

    @SelectProvider(method = "getGkZgContentInfoListCount", type = TblNbsjProjectMapperSqlConfig.class)
    Integer getGkZgContentInfoListCount(TblGkZgQuestionVo question) throws Exception;


    @Delete("DELETE FROM TBL_LEGAL_PROJECT_ATT WHERE attid=#{attid}")
    void deleteFileInfoByAttId(Integer attid);


    @SelectProvider(method = "selectAuditItems", type = TblNbsjProjectMapperSqlConfig.class)
    @Results({
            @Result(column = "PROJECTID", property = "projectId"),
            @Result(column = "PRJOECTNAME", property = "prjoectName"),
            @Result(column = "PLANYEAR", property = "planYear"),
            @Result(column = "PROJECTSOURCE", property = "projectSource"),
            @Result(column = "STARTDATE", property = "startDate"),
            @Result(column = "ENDDATE", property = "endDate"),
            @Result(column = "AUDITTYPE", property = "auditType"),
            @Result(column = "PROJECTCODE", property = "projectCode"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "PMID", property = "pmId"),
            @Result(column = "REALNAME", property = "pmStaff.realname"),
            @Result(column = "ORGNAME", property = "auditOrgInfo.orgname"),
    })
    List<TblNbsjProject> selectAuditItems(BigDecimal staffid) throws Exception;


    @SelectProvider(method = "selectIfGroup", type = TblNbsjProjectMapperSqlConfig.class)
    TblNbsjProject selectIfGroup(BigDecimal staffid, Integer projectid) throws Exception;


    @InsertProvider(method = "insertEntityar", type = TblNbsjProjectMapperSqlConfig.class)
    void insertEntityar(TblNbsjArchiveEntity ar) throws Exception;


    @SelectProvider(method = "selectListBytjsj", type = TblNbsjProjectMapperSqlConfig.class)
    List<Object[]> selectListBytjsj(String starttime, String endtime) throws Exception;

    //审计分析-年度查询条件
    @Select("SELECT DISTINCT TO_CHAR(CREATETIME,'YYYY') FROM TBL_NBSJ_PROJECT ORDER BY TO_CHAR(CREATETIME,'YYYY') DESC ")
    List<Integer> findReportYearList() throws Exception;

    //审计分析-各公司审计项目数
    @Select("SELECT ORG.ORGNAME,COUNT(0) sl FROM TBL_NBSJ_PROJECT TNP LEFT JOIN TBL_ORGANIZATION ORG ON TNP.AUDITORGID = ORG.ORGID WHERE TO_CHAR(TNP.CREATETIME,'YYYY') =#{year}  GROUP BY ORG.ORGNAME ORDER BY ORG.ORGNAME ")
    @Results({
            @Result(column = "ORGNAME", property = "auditOrgName"),
            @Result(column = "sl", property = "sl"),
    })
    List<TblNbsjProject> findNbsjProjectCountByCompanyId(Integer year) throws Exception;

    //审计分析-审计项目类型
    @Select("SELECT AUDITTYPE,COUNT(0) sl FROM TBL_NBSJ_PROJECT WHERE TO_CHAR(CREATETIME,'YYYY') = #{year}  GROUP BY AUDITTYPE ")
    @Results({
            @Result(column = "AUDITTYPE", property = "auditType"),
            @Result(column = "sl", property = "sl"),
    })
    List<TblNbsjProject> findAuditTypeCount(Integer year) throws Exception;

    //审计分析-审计项目情况表
    @SelectProvider(method = "findProjectItemReport", type = TblNbsjProjectMapperSqlConfig.class)
    @Results({
            @Result(column = "ORGNAME", property = "auditOrgName"),
            @Result(column = "PRJOECTNAME", property = "prjoectName"),
            @Result(column = "PLANYEAR", property = "planYear"),
            @Result(column = "PROJECTSOURCE", property = "projectSource"),
            @Result(column = "AUDITTYPE", property = "auditType"),
            @Result(column = "REALNAME", property = "realname"),
            @Result(column = "EXAMINETYPE", property = "examineTypes"),
            @Result(column = "ORGNAME", property = "auditOrgName"),
    })
    List<TblNbsjProject> findProjectItemReport(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, Integer year) throws Exception;


    @SelectProvider(method = "selectProjectItemReportCount", type = TblNbsjProjectMapperSqlConfig.class)
    Integer selectProjectItemReportCount(Integer year) throws Exception;


    @SelectProvider(method = "getObjBySql", type = TblNbsjProjectMapperSqlConfig.class)
    com.alibaba.fastjson.JSONObject getObjBySql(String noSql) throws Exception;


    @SelectProvider(method = "getGkProjectInfoExport", type = TblNbsjProjectMapperSqlConfig.class)
    List<JSONObject> getGkProjectInfoExport(TblGkProjectVo project) throws Exception;


    @SelectProvider(method = "getGkQuestionInfoExport", type = TblNbsjProjectMapperSqlConfig.class)
    List<JSONObject> getGkQuestionInfoExport(TblGkQuestionVo question) throws Exception;

    @SelectProvider(method = "getGkZgContentInfoExport", type = TblNbsjProjectMapperSqlConfig.class)
    List<JSONObject> getGkZgContentInfoExport(TblGkZgQuestionVo question) throws Exception;


}
