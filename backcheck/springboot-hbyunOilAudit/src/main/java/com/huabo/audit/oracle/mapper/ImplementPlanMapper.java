package com.huabo.audit.oracle.mapper;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.vo.result.FlowTaskInfo;

import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName ImplementPlanMapper
 * @Description
 * @DATE 2023/10/27
 */
public interface ImplementPlanMapper extends BaseMapper<ImplementPlanEntity> {
	
	@SelectProvider(method="selectByrwfpEntity",type=ImplementPlanMapperSqlConfig.class)
    @Results({
            @Result(column = "ID", property = "id"),
            @Result(column = "PLAN_ID", property = "planId"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "PLAN_PROJECT_ID", property = "planProjectId"),
            @Result(column = "PLAN_NAME", property = "planName"),
            @Result(column = "PLAN_PROJECT_NAME", property = "planProjectName" ),
            @Result(column = "PROJECT_TYPE", property = "projectType"),
            @Result(column = "AUDIT_ORG_ID", property = "auditOrgId"),
            @Result(column = "AUDIT_ORG_NAME", property = "auditOrgName"),
            @Result(column = "PLAN_YEAR", property = "planYear"),
            @Result(column = "PLAN_TIME", property = "planTime"),
            @Result(column = "PROJECT_SUMMARY", property = "projectSummary"),
            @Result(column = "PROJECT_ORDER_ID", property = "projectOrderId"),
            @Result(column = "PROJECT_ORDER_NAME", property = "projectOrderName"),
            @Result(column = "PLAN_STARTTIME", property = "planStarttime"),
            @Result(column = "PLAN_ENDTIME", property = "planEndtime"),
            @Result(column = "AUDIT_METHOD", property = "auditMethod"),
            @Result(column = "COST_ESTIMATION", property = "costEstimation"),
            @Result(column = "IS_WW", property = "isWw"),
            @Result(column = "TEMP_ID", property = "tempId"),
            @Result(column = "TEMP_NAME", property = "tempName"),
            @Result(column = "IMPLEMENT_TYPE", property = "implementType"),
            @Result(column = "DEPT_ID", property = "deptId"),
            @Result(column = "DEPT_NAME", property = "deptName"),
            @Result(column = "IMPLEMENT_STEPS", property = "implementSteps"),
            @Result(column = "AUDIT_REQUIREMENT", property = "auditRequirement"),
            @Result(column = "AUDIT_REASON", property = "auditReason"),
            @Result(column = "AUDIT_CONTENT", property = "auditContent"),
            @Result(column = "AUDIT_PROCESS", property = "auditProcess"),
            @Result(column = "AUDIT_RESULT_USE", property = "auditResultUse"),
            @Result(column = "OTHER_CONTENT", property = "otherContent"),
            @Result(column = "FPSTATUS", property = "fpStatus"),
            @Result(column = "UPDATESTATUS", property = "updateStatus"),
            @Result(column = "CURRENTSTATRE", property = "currentStatre"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "IMPLEMENTTIME", property = "implementtime"),
            @Result(column = "YQDATE", property = "yqdate"),
            @Result(column = "ISJY", property = "isjy"),
            @Result(column = "ISGC", property = "isgc"),
            @Result(column = "QDCODE", property = "qdcode"),
            @Result(column = "ZYKSRYIDS", property = "zyksryids"),
            @Result(column = "ZYKSTYPE", property = "zykstype"),
            @Result(column = "ZYKSRYRWNAMES", property = "zyksryrwnames"),
//            @Result(column = "XMDQID", property = "xmqd", javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblYqnsXmdqMapper.selectById")),
            @Result(column = "ID", property= "attachments", javaType = List.class, many = @Many(select= "selectAttachmentById")),
            @Result(column = "ID", property= "teams", javaType = List.class, many = @Many(select= "selectTeamsById"))
    })
    List<ImplementPlanEntity> selectByrwfpEntity( ImplementPlanEntity implementPlanEntity) ;
	
	
	
    @SelectProvider(method="selectByEntity",type=ImplementPlanMapperSqlConfig.class)
    @Results(id="implementPlanResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "NO", property = "no"),
            @Result(column = "PLAN_ID", property = "planId"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "PLAN_PROJECT_ID", property = "planProjectId"),
            @Result(column = "PLAN_NAME", property = "planName"),
            @Result(column = "PLAN_PROJECT_NAME", property = "planProjectName" ),
            @Result(column = "PROJECT_TYPE", property = "projectType"),
            @Result(column = "AUDIT_ORG_ID", property = "auditOrgId"),
            @Result(column = "AUDIT_ORG_NAME", property = "auditOrgName"),
            @Result(column = "PLAN_YEAR", property = "planYear"),
            @Result(column = "PLAN_TIME", property = "planTime"),
            @Result(column = "PROJECT_SUMMARY", property = "projectSummary"),
            @Result(column = "PROJECT_ORDER_ID", property = "projectOrderId"),
            @Result(column = "PROJECT_ORDER_NAME", property = "projectOrderName"),
            @Result(column = "PLAN_STARTTIME", property = "planStarttime"),
            @Result(column = "PLAN_ENDTIME", property = "planEndtime"),
            @Result(column = "AUDIT_METHOD", property = "auditMethod"),
            @Result(column = "COST_ESTIMATION", property = "costEstimation"),
            @Result(column = "IS_WW", property = "isWw"),
            @Result(column = "TEMP_ID", property = "tempId"),
            @Result(column = "TEMP_NAME", property = "tempName"),
            @Result(column = "IMPLEMENT_TYPE", property = "implementType"),
            @Result(column = "DEPT_ID", property = "deptId"),
            @Result(column = "DEPT_NAME", property = "deptName"),
            @Result(column = "IMPLEMENT_STEPS", property = "implementSteps"),
            @Result(column = "AUDIT_REQUIREMENT", property = "auditRequirement"),
            @Result(column = "AUDIT_REASON", property = "auditReason"),
            @Result(column = "AUDIT_CONTENT", property = "auditContent"),
            @Result(column = "AUDIT_PROCESS", property = "auditProcess"),
            @Result(column = "AUDIT_RESULT_USE", property = "auditResultUse"),
            @Result(column = "OTHER_CONTENT", property = "otherContent"),
            @Result(column = "FPSTATUS", property = "fpStatus"),
            @Result(column = "UPDATESTATUS", property = "updateStatus"),
            @Result(column = "CURRENTSTATRE", property = "currentStatre"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "QDCODE", property = "qdcode"),
            @Result(column = "IMPLEMENTTIME", property = "implementtime"),
            @Result(column = "YQDATE", property = "yqdate"),
            @Result(column = "ZYKSRYIDS", property = "zyksryids"),
            @Result(column = "ZYKSTYPE", property = "zykstype"),
            @Result(column = "ZYKSRYRWNAMES", property = "zyksryrwnames"),
    })
    List<ImplementPlanEntity> selectByEntity( ImplementPlanEntity implementPlanEntity, BigDecimal staffId, Integer xmnd, TblStaffUtil user) ;

    @SelectProvider(method="selectCountByEntity",type=ImplementPlanMapperSqlConfig.class)
    Integer selectCountByEntity( ImplementPlanEntity implementPlanEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_IMPLEMENT_PLAN WHERE ID = #{id}")
    @Results(id="implementPlanDetailResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "NO", property = "no"),
            @Result(column = "PLAN_ID", property = "planId"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "PLAN_PROJECT_ID", property = "planProjectId"),
            @Result(column = "PLAN_NAME", property = "planName"),
            @Result(column = "PLAN_PROJECT_NAME", property = "planProjectName" ),
            @Result(column = "PROJECT_TYPE", property = "projectType"),
            @Result(column = "AUDIT_ORG_ID", property = "auditOrgId"),
            @Result(column = "AUDIT_ORG_NAME", property = "auditOrgName"),
            @Result(column = "PLAN_YEAR", property = "planYear"),
            @Result(column = "PLAN_TIME", property = "planTime"),
            @Result(column = "PROJECT_SUMMARY", property = "projectSummary"),
            @Result(column = "PROJECT_ORDER_ID", property = "projectOrderId"),
            @Result(column = "PROJECT_ORDER_NAME", property = "projectOrderName"),
            @Result(column = "PLAN_STARTTIME", property = "planStarttime"),
            @Result(column = "PLAN_ENDTIME", property = "planEndtime"),
            @Result(column = "AUDIT_METHOD", property = "auditMethod"),
            @Result(column = "COST_ESTIMATION", property = "costEstimation"),
            @Result(column = "IS_WW", property = "isWw"),
            @Result(column = "TEMP_ID", property = "tempId"),
            @Result(column = "TEMP_NAME", property = "tempName"),
            @Result(column = "IMPLEMENT_TYPE", property = "implementType"),
            @Result(column = "DEPT_ID", property = "deptId"),
            @Result(column = "DEPT_NAME", property = "deptName"),
            @Result(column = "IMPLEMENT_STEPS", property = "implementSteps"),
            @Result(column = "AUDIT_REQUIREMENT", property = "auditRequirement"),
            @Result(column = "AUDIT_REASON", property = "auditReason"),
            @Result(column = "AUDIT_CONTENT", property = "auditContent"),
            @Result(column = "AUDIT_PROCESS", property = "auditProcess"),
            @Result(column = "AUDIT_RESULT_USE", property = "auditResultUse"),
            @Result(column = "OTHER_CONTENT", property = "otherContent"),
            @Result(column = "FPSTATUS", property = "fpStatus"),
            @Result(column = "UPDATESTATUS", property = "updateStatus"),
            @Result(column = "CURRENTSTATRE", property = "currentStatre"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "ZSSTAFFID", property = "zsstaffid"),
            @Result(column = "ZSNAME", property = "zsname"),
            @Result(column = "FZSTAFFID", property = "fzstaffid"),
            @Result(column = "FZNAME", property = "fzname"),
            @Result(column = "ISJY", property = "isjy"),
            @Result(column = "ISGC", property = "isgc"),
            @Result(column = "QDCODE", property = "qdcode"),
            @Result(column = "IMPLEMENTTIME", property = "implementtime"),
            @Result(column = "YQDATE", property = "yqdate"),
            @Result(column = "ZYKSRYIDS", property = "zyksryids"),
            @Result(column = "ZYKSRYRWNAMES", property = "zyksryrwnames"),
            @Result(column = "ZYKSTYPE", property = "zykstype"),
//            @Result(column = "XMDQID", property = "xmqd", javaType = TblYqnsXmdq.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblYqnsXmdqMapper.selectById")),
            @Result(column = "ID", property= "attachments", javaType = List.class, many = @Many(select= "selectAttachmentById")),
            @Result(column = "ID", property= "teams", javaType = List.class, many = @Many(select= "selectTeamsById"))
    })
    ImplementPlanEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=ImplementPlanMapperSqlConfig.class)
    void updateEntity(ImplementPlanEntity implementPlanEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=ImplementPlanMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(ImplementPlanEntity implementPlanEntity);

    @DeleteProvider(method="deleteByIds", type=ImplementPlanMapperSqlConfig.class)
    void deleteEntity(String ids);

    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_IMPLEMENT_PLAN_ATT WHERE IM_PLAN_ID = #{id})")
    List<TblAttachment> selectAttachmentById(BigDecimal id);

    @Select("SELECT * FROM TBL_YQNS_IMPLEMENT_PLAN_TEAM WHERE IM_PLAN_ID = #{id}")
    @Results(id="implementPlanTeamDetailResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "IM_PLAN_ID", property = "imPlanId"),
            @Result(column = "TEAM_NAME", property = "teamName"),
            @Result(column = "TEAM_LEADER_ID", property = "teamLeaderId"),
            @Result(column = "FZZSTAFFID", property = "fzzstaffid"),
            @Result(column = "FZZNAME", property = "fzzname"),
            @Result(column = "TEAM_MEMBERS_IDS", property = "teamMembersIds"),
            @Result(column = "TEAM_LEADER_ID", property = "teamLeader", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "TEAM_MEMBERS_IDS", property = "teamMembers", javaType = List.class,many = @Many(select = "selectTblStaffByIds")),
    })
    List<ImplementPlanTeamEntity> selectTeamsById(BigDecimal id);

    @DeleteProvider(method="deleteAttachmentByIds", type=ImplementPlanMapperSqlConfig.class)
    void deleteAttachmentByIds(String ids);

    @DeleteProvider(method="deleteImplementPlanTeamByIds", type=ImplementPlanMapperSqlConfig.class)
    void deleteImplementPlanTeamByIds(String ids);

    @InsertProvider(method="insertAttachments", type=ImplementPlanMapperSqlConfig.class)
    void insertAttachmentsWidthId(BigDecimal id, String attachmentId);

    @InsertProvider(method="insertImplementPlanTeamWidthId", type=ImplementPlanMapperSqlConfig.class)
    void insertImplementPlanTeamWidthId(BigDecimal id, ImplementPlanTeamEntity team);
    
    
    @UpdateProvider(method = "updateImplementPlanTeamWidthId", type = ImplementPlanMapperSqlConfig.class) 
    void updateImplementPlanTeamWidthId(BigDecimal id, ImplementPlanTeamEntity team) throws Exception;
    
    @Select("SELECT * FROM TBL_YQNS_IMPLEMENT_PLAN_TEAM WHERE ID = #{id}")
    ImplementPlanTeamEntity findbyteamid(BigDecimal id);

    @SelectProvider(method = "selectTblStaffByIds", type= ImplementPlanMapperSqlConfig.class)
    List<TblStaff> selectTblStaffByIds(String ids);
    
    @Select("SELECT PROJECT_NAME FROM TBL_YQNS_IMPLEMENT_PLAN WHERE ID = #{id}")
    String selectProjectNameById(BigDecimal id);
    
    
    @UpdateProvider(method = "updatePjStart", type = ImplementPlanMapperSqlConfig.class)
    void updatePjStart(BigDecimal projectid) throws Exception;
    
    @UpdateProvider(method = "updateFpStatus", type = ImplementPlanMapperSqlConfig.class) 
    void updateFpStatus(Integer fpStatus, BigDecimal projectId) throws Exception;
    
    
    @Update("UPDATE TBL_YQNS_IMPLEMENT_PLAN SET IMPLEMENTTIME = TO_DATE(#{implementTime}, 'YYYY-MM-DD HH24:MI:SS'),STATUS=2 WHERE ID = #{projectId}")
    void updateImplementTime(BigDecimal projectId, String implementTime) throws Exception;
    
    @Update("UPDATE TBL_YQNS_IMPLEMENT_PLAN SET STATUS = 1,CURRENTSTATRE=0 WHERE ID = #{projectId}")
    void updatestatus(BigDecimal projectId) throws Exception;
    
    @Update("UPDATE TBL_YQNS_IMPLEMENT_PLAN SET YQDATE = TO_DATE(#{yqdate}, 'YYYY-MM-DD') WHERE ID = #{projectId}")
    void updateyqTime(BigDecimal projectId, String yqdate) throws Exception;
    
    @SelectProvider(method="selectBysqEntity",type=ImplementPlanMapperSqlConfig.class)
    @Results({
        @Result(column = "ID", property = "id"),
        @Result(column = "NO", property = "no"),
        @Result(column = "PLAN_ID", property = "planId"),
        @Result(column = "PROJECT_NAME", property = "projectName"),
        @Result(column = "PLAN_PROJECT_ID", property = "planProjectId"),
        @Result(column = "PLAN_NAME", property = "planName"),
        @Result(column = "PLAN_PROJECT_NAME", property = "planProjectName" ),
        @Result(column = "PROJECT_TYPE", property = "projectType"),
        @Result(column = "AUDIT_ORG_ID", property = "auditOrgId"),
        @Result(column = "AUDIT_ORG_NAME", property = "auditOrgName"), 
        @Result(column = "PLAN_YEAR", property = "planYear"),
        @Result(column = "PLAN_TIME", property = "planTime"),
        @Result(column = "PROJECT_SUMMARY", property = "projectSummary"),
        @Result(column = "PROJECT_ORDER_ID", property = "projectOrderId"),
        @Result(column = "PROJECT_ORDER_NAME", property = "projectOrderName"),
        @Result(column = "PLAN_STARTTIME", property = "planStarttime"),
        @Result(column = "PLAN_ENDTIME", property = "planEndtime"),
        @Result(column = "AUDIT_METHOD", property = "auditMethod"),
        @Result(column = "COST_ESTIMATION", property = "costEstimation"),
        @Result(column = "IS_WW", property = "isWw"),
        @Result(column = "TEMP_ID", property = "tempId"),
        @Result(column = "TEMP_NAME", property = "tempName"),
        @Result(column = "IMPLEMENT_TYPE", property = "implementType"),
        @Result(column = "DEPT_ID", property = "deptId"),
        @Result(column = "DEPT_NAME", property = "deptName"),
        @Result(column = "IMPLEMENT_STEPS", property = "implementSteps"),
        @Result(column = "AUDIT_REQUIREMENT", property = "auditRequirement"),
        @Result(column = "AUDIT_REASON", property = "auditReason"),
        @Result(column = "AUDIT_CONTENT", property = "auditContent"),
        @Result(column = "AUDIT_PROCESS", property = "auditProcess"), 
        @Result(column = "AUDIT_RESULT_USE", property = "auditResultUse"),
        @Result(column = "OTHER_CONTENT", property = "otherContent"),
        @Result(column = "FPSTATUS", property = "fpStatus"),
        @Result(column = "UPDATESTATUS", property = "updateStatus"),
        @Result(column = "CURRENTSTATRE", property = "currentStatre"),
        @Result(column = "STATUS", property = "status"),
        @Result(column = "ZSSTAFFID", property = "zsstaffid"),
        @Result(column = "ZSNAME", property = "zsname"),
        @Result(column = "FZSTAFFID", property = "fzstaffid"),
        @Result(column = "FZNAME", property = "fzname"),
        @Result(column = "ZYKSRYIDS", property = "zyksryids"),
        @Result(column = "ZYKSRYRWNAMES", property = "zyksryrwnames"),
        @Result(column = "ZYKSTYPE", property = "zykstype"),
        @Result(column = "ID", property= "attachments", javaType = List.class, many = @Many(select= "selectAttachmentById")),
        @Result(column = "ID", property= "teams", javaType = List.class, many = @Many(select= "selectTeamsById")),
        @Result(column = "IMPLEMENTTIME", property = "implementtime")
})
    List<ImplementPlanEntity> selectBysqEntity(ImplementPlanEntity implementPlanEntity,BigDecimal staffId) ;
    
    
    
    @SelectProvider(method="daList",type=ImplementPlanMapperSqlConfig.class)
    @Results({
    	 @Result(column = "ID", property = "id"),
         @Result(column = "PLAN_ID", property = "planId"),
         @Result(column = "NO", property = "no"),
         @Result(column = "PROJECT_NAME", property = "projectName"),
         @Result(column = "PLAN_PROJECT_ID", property = "planProjectId"),
         @Result(column = "PLAN_NAME", property = "planName"),
         @Result(column = "PLAN_PROJECT_NAME", property = "planProjectName" ),
         @Result(column = "PROJECT_TYPE", property = "projectType"),
         @Result(column = "AUDIT_ORG_ID", property = "auditOrgId"),
         @Result(column = "AUDIT_ORG_NAME", property = "auditOrgName"),
         @Result(column = "PLAN_YEAR", property = "planYear"),
         @Result(column = "PLAN_TIME", property = "planTime"),
         @Result(column = "PROJECT_SUMMARY", property = "projectSummary"),
         @Result(column = "PROJECT_ORDER_ID", property = "projectOrderId"),
         @Result(column = "PROJECT_ORDER_NAME", property = "projectOrderName"),
         @Result(column = "PLAN_STARTTIME", property = "planStarttime"),
         @Result(column = "PLAN_ENDTIME", property = "planEndtime"),
         @Result(column = "AUDIT_METHOD", property = "auditMethod"),
         @Result(column = "COST_ESTIMATION", property = "costEstimation"),
         @Result(column = "IS_WW", property = "isWw"),
         @Result(column = "TEMP_ID", property = "tempId"),
         @Result(column = "TEMP_NAME", property = "tempName"),
         @Result(column = "IMPLEMENT_TYPE", property = "implementType"),
         @Result(column = "DEPT_ID", property = "deptId"),
         @Result(column = "DEPT_NAME", property = "deptName"),
         @Result(column = "IMPLEMENT_STEPS", property = "implementSteps"),
         @Result(column = "AUDIT_REQUIREMENT", property = "auditRequirement"),
         @Result(column = "AUDIT_REASON", property = "auditReason"),
         @Result(column = "AUDIT_CONTENT", property = "auditContent"),
         @Result(column = "AUDIT_PROCESS", property = "auditProcess"),
         @Result(column = "AUDIT_RESULT_USE", property = "auditResultUse"),
         @Result(column = "OTHER_CONTENT", property = "otherContent"),
         @Result(column = "FPSTATUS", property = "fpStatus"),
         @Result(column = "UPDATESTATUS", property = "updateStatus"),
         @Result(column = "CURRENTSTATRE", property = "currentStatre"),
         @Result(column = "STATUS", property = "status"),
         @Result(column = "IMPLEMENTTIME", property = "implementtime"),
         @Result(column = "YQDATE", property = "yqdate"),
         @Result(column = "ZYKSRYIDS", property = "zyksryids"),
         @Result(column = "ZYKSRYRWNAMES", property = "zyksryrwnames"),
         @Result(column = "ZYKSTYPE", property = "zykstype"),
    })
    List<ImplementPlanEntity> daList(PageInfo<ImplementPlanEntity> pageInfo, String projectName, String qdcode, BigDecimal staffid) ;
    
    @SelectProvider(method="jyrzList",type=ImplementPlanMapperSqlConfig.class)
    @Results({
    	 @Result(column = "ID", property = "id"),
         @Result(column = "PLAN_ID", property = "planId"),
         @Result(column = "NO", property = "no"),
         @Result(column = "PROJECT_NAME", property = "projectName"),
         @Result(column = "PLAN_PROJECT_ID", property = "planProjectId"),
         @Result(column = "PLAN_NAME", property = "planName"),
         @Result(column = "PLAN_PROJECT_NAME", property = "planProjectName" ),
         @Result(column = "PROJECT_TYPE", property = "projectType"),
         @Result(column = "AUDIT_ORG_ID", property = "auditOrgId"),
         @Result(column = "AUDIT_ORG_NAME", property = "auditOrgName"),
         @Result(column = "PLAN_YEAR", property = "planYear"),
         @Result(column = "PLAN_TIME", property = "planTime"),
         @Result(column = "PROJECT_SUMMARY", property = "projectSummary"),
         @Result(column = "PROJECT_ORDER_ID", property = "projectOrderId"),
         @Result(column = "PROJECT_ORDER_NAME", property = "projectOrderName"),
         @Result(column = "PLAN_STARTTIME", property = "planStarttime"),
         @Result(column = "PLAN_ENDTIME", property = "planEndtime"),
         @Result(column = "AUDIT_METHOD", property = "auditMethod"),
         @Result(column = "COST_ESTIMATION", property = "costEstimation"),
         @Result(column = "IS_WW", property = "isWw"),
         @Result(column = "TEMP_ID", property = "tempId"),
         @Result(column = "TEMP_NAME", property = "tempName"),
         @Result(column = "IMPLEMENT_TYPE", property = "implementType"),
         @Result(column = "DEPT_ID", property = "deptId"),
         @Result(column = "DEPT_NAME", property = "deptName"),
         @Result(column = "IMPLEMENT_STEPS", property = "implementSteps"),
         @Result(column = "AUDIT_REQUIREMENT", property = "auditRequirement"),
         @Result(column = "AUDIT_REASON", property = "auditReason"),
         @Result(column = "AUDIT_CONTENT", property = "auditContent"),
         @Result(column = "AUDIT_PROCESS", property = "auditProcess"),
         @Result(column = "AUDIT_RESULT_USE", property = "auditResultUse"),
         @Result(column = "OTHER_CONTENT", property = "otherContent"),
         @Result(column = "FPSTATUS", property = "fpStatus"),
         @Result(column = "UPDATESTATUS", property = "updateStatus"),
         @Result(column = "CURRENTSTATRE", property = "currentStatre"),
         @Result(column = "STATUS", property = "status"),
         @Result(column = "IMPLEMENTTIME", property = "implementtime"),
         @Result(column = "PCNT", property = "pcnt"),
         @Result(column = "ZYKSRYIDS", property = "zyksryids"),
         @Result(column = "ZYKSRYRWNAMES", property = "zyksryrwnames"),
         @Result(column = "ZYKSTYPE", property = "zykstype"),
    })
    List<ImplementPlanEntity> jyrzList(PageInfo<ImplementPlanEntity> pageInfo, String projectName, String qdcode, BigDecimal staffid,Integer isadmin) ;
    
    
    @SelectProvider(method="jyrzxqList",type=ImplementPlanMapperSqlConfig.class)
    @Results({
        @Result(column = "REALNAME", property = "staffname"),
    })
    List<TblNbsjBorrowRecordEntity> jyrzxqList(String id,Integer isadmin,BigDecimal staffid) throws Exception;
    
    @Select("SELECT STATUS FROM TBL_NBSJ_BORROWRECORD "
    		+ " WHERE 1=1 "
    		+ " AND BORROWID=(SELECT max(BORROWID) FROM TBL_NBSJ_BORROWRECORD WHERE RETURNDATE >=SYSDATE  AND STAFFID=#{staffid} AND PROJECTID=#{id})")
    Integer selectJyStatusByStaff(BigDecimal id,BigDecimal staffid) throws Exception;
    
    @Update("UPDATE TBL_YQNS_IMPLEMENT_PLAN SET ZYKSRYIDS = #{zyksryids},ZYKSRYRWNAMES=#{zyksryrwnames} WHERE ID = #{projectId}")
    void fpzyksry(String zyksryids,String zyksryrwnames,BigDecimal projectId) throws Exception;


    @SelectProvider(method="selectReviewStatusList",type=ImplementPlanMapperSqlConfig.class)
    @Results(value={
            @Result(column = "ID", property = "id"),
            @Result(column = "PLAN_ID", property = "planId"),
            @Result(column = "NO", property = "no"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "PLAN_PROJECT_ID", property = "planProjectId"),
            @Result(column = "PLAN_NAME", property = "planName"),
            @Result(column = "PLAN_PROJECT_NAME", property = "planProjectName" ),
            @Result(column = "PROJECT_TYPE", property = "projectType"),
            @Result(column = "AUDIT_ORG_ID", property = "auditOrgId"),
            @Result(column = "AUDIT_ORG_NAME", property = "auditOrgName"),
            @Result(column = "PLAN_YEAR", property = "planYear"),
            @Result(column = "PLAN_TIME", property = "planTime"),
            @Result(column = "PROJECT_SUMMARY", property = "projectSummary"),
            @Result(column = "PROJECT_ORDER_ID", property = "projectOrderId"),
            @Result(column = "PROJECT_ORDER_NAME", property = "projectOrderName"),
            @Result(column = "PLAN_STARTTIME", property = "planStarttime"),
            @Result(column = "PLAN_ENDTIME", property = "planEndtime"),
            @Result(column = "AUDIT_METHOD", property = "auditMethod"),
            @Result(column = "COST_ESTIMATION", property = "costEstimation"),
            @Result(column = "IS_WW", property = "isWw"),
            @Result(column = "TEMP_ID", property = "tempId"),
            @Result(column = "TEMP_NAME", property = "tempName"),
            @Result(column = "IMPLEMENT_TYPE", property = "implementType"),
            @Result(column = "DEPT_ID", property = "deptId"),
            @Result(column = "DEPT_NAME", property = "deptName"),
            @Result(column = "IMPLEMENT_STEPS", property = "implementSteps"),
            @Result(column = "AUDIT_REQUIREMENT", property = "auditRequirement"),
            @Result(column = "AUDIT_REASON", property = "auditReason"),
            @Result(column = "AUDIT_CONTENT", property = "auditContent"),
            @Result(column = "AUDIT_PROCESS", property = "auditProcess"),
            @Result(column = "AUDIT_RESULT_USE", property = "auditResultUse"),
            @Result(column = "OTHER_CONTENT", property = "otherContent"),
            @Result(column = "FPSTATUS", property = "fpStatus"),
            @Result(column = "UPDATESTATUS", property = "updateStatus"),
            @Result(column = "CURRENTSTATRE", property = "currentStatre"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "QDCODE", property = "qdcode"),
            @Result(column = "IMPLEMENTTIME", property = "implementtime"),
            @Result(column = "YQDATE", property = "yqdate"),
            @Result(column = "ZYKSRYIDS", property = "zyksryids"),
            @Result(column = "ZYKSRYRWNAMES", property = "zyksryrwnames"),
            @Result(column = "LEADERID", property = "leaderId"),
            @Result(column = "LEADERNAME", property = "leaderName"),
            @Result(column = "FZZSTAFFID", property = "fzzStafffId"),
            @Result(column = "FZZNAME", property = "fzzName"),
            @Result(column = "ZYKSTYPE", property = "zykstype"),
    })
    List<ImplementPlanEntity> selectReviewStatusList(ImplementPlanEntity implementPlanEntity);
    
    @Select("SELECT TFT.CURRENTSTAFFID,TS.REALNAME AS CURRENTSTAFFNAME , TFT.CREATETIME,TFT.COMMONT FROM TBL_FLOW_TASKINFO TFT LEFT JOIN TBL_STAFF TS ON TFT.CURRENTSTAFFID = TS.STAFFID WHERE  TFT.FLOWID IN (SELECT YMWORKFROM FROM TBL_SYSTEM_YMWORK WHERE TABLEID = #{sheetid}) AND TFT.FROMID = #{id} AND" + 
    		" TFT.PROCESSID IN (SELECT YMFORMID FROM TBL_SYSTEM_FORMFLOW WHERE FLOWID IN (SELECT YMWORKFROM FROM TBL_SYSTEM_YMWORK WHERE TABLEID = #{sheetid}) AND FORMID = #{id}) AND (TFT.CURRENTROLE LIKE #{stepName} OR TFT.CURRENTROLE LIKE #{stepNameTwo} ) ORDER BY TFT.TASKGROUPID DESC")
	List<FlowTaskInfo> selectApprovalInfoList(@Param("id")BigDecimal id,@Param("sheetid") Integer sheetid,@Param("stepName") String stepName,@Param("stepNameTwo")  String stepNameTwo);
    
    @Select("SELECT TFT.CURRENTSTAFFID,TS.REALNAME AS CURRENTSTAFFNAME , TFT.CREATETIME,TFT.COMMONT FROM TBL_FLOW_TASKINFO TFT LEFT JOIN TBL_STAFF TS ON TFT.CURRENTSTAFFID = TS.STAFFID WHERE  TFT.FLOWID IN (SELECT YMWORKFROM FROM TBL_SYSTEM_YMWORK WHERE TABLEID = #{sheetid}) AND FROMID = #{id} AND" + 
    		" TFT.PROCESSID IN (SELECT YMFORMID FROM TBL_SYSTEM_FORMFLOW WHERE FLOWID IN (SELECT YMWORKFROM FROM TBL_SYSTEM_YMWORK WHERE TABLEID = #{sheetid}) AND FORMID = #{id}) AND TFT.THISSTEPID = #{stepId} AND ROWNUM = 1 ORDER BY CREATETIME DESC")
	FlowTaskInfo selectApprovalEndInfo(@Param("id")BigDecimal id,@Param("sheetid") Integer sheetid,@Param("stepId") String stepId);


    @Select("SELECT * FROM TBL_YQNS_IMPLEMENT_PLAN WHERE SPZT = #{completed} ")
    List<ImplementPlanEntity> selectByCompletedEntity(ImplementPlanEntity implementPlanEntity, BigDecimal staffId, Integer xmnd,Integer completed);


    @Select("SELECT TFT.CURRENTSTAFFID,TS.REALNAME AS CURRENTSTAFFNAME , TFT.CREATETIME,TFT.COMMONT FROM TBL_FLOW_TASKINFO TFT LEFT JOIN TBL_STAFF TS ON TFT.CURRENTSTAFFID = TS.STAFFID WHERE  TFT.FLOWID IN (SELECT YMWORKFROM FROM TBL_SYSTEM_YMWORK WHERE TABLEID = #{sheetid}) AND FROMID = #{id} AND" + 
    		" TFT.PROCESSID IN (SELECT YMFORMID FROM TBL_SYSTEM_FORMFLOW WHERE FLOWID IN (SELECT YMWORKFROM FROM TBL_SYSTEM_YMWORK WHERE TABLEID = #{sheetid}) AND FORMID = #{id}) AND TFT.OPERATION = #{stepId} AND ROWNUM = 1 ORDER BY CREATETIME DESC")
	FlowTaskInfo selectApprovalStartInfo(@Param("id")BigDecimal id,@Param("sheetid") Integer sheetid,@Param("stepId") String stepId);
    
    
    
    

    @Select(" select * from (  SELECT * from TBL_YQNS_IMPLEMENT_PLAN WHERE AUDIT_ORG_ID=#{orgid}  ORDER BY CREATEDATE DESC)  where  ROWNUM=1")
    @Results(value={
            @Result(column = "ID", property = "id"),
            @Result(column = "NO", property = "no"),
            @Result(column = "PLAN_ID", property = "planId"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "PLAN_PROJECT_ID", property = "planProjectId"),
            @Result(column = "PLAN_NAME", property = "planName"),
            @Result(column = "PLAN_PROJECT_NAME", property = "planProjectName" ),
            @Result(column = "PROJECT_TYPE", property = "projectType"),
            @Result(column = "AUDIT_ORG_ID", property = "auditOrgId"),
            @Result(column = "AUDIT_ORG_NAME", property = "auditOrgName"),
            @Result(column = "PLAN_YEAR", property = "planYear"),
            @Result(column = "PLAN_TIME", property = "planTime"),
            @Result(column = "PROJECT_SUMMARY", property = "projectSummary"),
            @Result(column = "PROJECT_ORDER_ID", property = "projectOrderId"),
            @Result(column = "PROJECT_ORDER_NAME", property = "projectOrderName"),
            @Result(column = "PLAN_STARTTIME", property = "planStarttime"),
            @Result(column = "PLAN_ENDTIME", property = "planEndtime"),
            @Result(column = "AUDIT_METHOD", property = "auditMethod"),
            @Result(column = "COST_ESTIMATION", property = "costEstimation"),
            @Result(column = "IS_WW", property = "isWw"),
            @Result(column = "TEMP_ID", property = "tempId"),
            @Result(column = "TEMP_NAME", property = "tempName"),
            @Result(column = "IMPLEMENT_TYPE", property = "implementType"),
            @Result(column = "DEPT_ID", property = "deptId"),
            @Result(column = "DEPT_NAME", property = "deptName"),
            @Result(column = "IMPLEMENT_STEPS", property = "implementSteps"),
            @Result(column = "AUDIT_REQUIREMENT", property = "auditRequirement"),
            @Result(column = "AUDIT_REASON", property = "auditReason"),
            @Result(column = "AUDIT_CONTENT", property = "auditContent"),
            @Result(column = "AUDIT_PROCESS", property = "auditProcess"),
            @Result(column = "AUDIT_RESULT_USE", property = "auditResultUse"),
            @Result(column = "OTHER_CONTENT", property = "otherContent"),
            @Result(column = "FPSTATUS", property = "fpStatus"),
            @Result(column = "UPDATESTATUS", property = "updateStatus"),
            @Result(column = "CURRENTSTATRE", property = "currentStatre"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "ZSSTAFFID", property = "zsstaffid"),
            @Result(column = "ZSNAME", property = "zsname"),
            @Result(column = "FZSTAFFID", property = "fzstaffid"),
            @Result(column = "FZNAME", property = "fzname"),
            @Result(column = "ISJY", property = "isjy"),
            @Result(column = "ISGC", property = "isgc"),
            @Result(column = "QDCODE", property = "qdcode"),
            @Result(column = "IMPLEMENTTIME", property = "implementtime"),
            @Result(column = "YQDATE", property = "yqdate"),
            @Result(column = "ZYKSRYIDS", property = "zyksryids"),
            @Result(column = "ZYKSRYRWNAMES", property = "zyksryrwnames"),
            @Result(column = "ZYKSTYPE", property = "zykstype"),
//            @Result(column = "XMDQID", property = "xmqd", javaType = TblYqnsXmdq.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblYqnsXmdqMapper.selectById")),
            @Result(column = "ID", property= "attachments", javaType = List.class, many = @Many(select= "selectAttachmentById")),
            @Result(column = "ID", property= "teams", javaType = List.class, many = @Many(select= "selectTeamsById"))
    })
    ImplementPlanEntity findbyOrgidLast(String orgid) throws Exception;
    
    @Update("UPDATE TBL_YQNS_IMPLEMENT_PLAN SET STATUS = 5 WHERE ID = #{projectId}")
    void tzstatus(BigDecimal projectId) throws Exception;

    
    
    @Select("SELECT * from   TBL_YQNS_IMPLEMENT_PLAN WHERE XMAPBID=#{xmid} AND ROWNUM=1  ORDER BY ID DESC ")
    ImplementPlanEntity selectByxmid(String xmid);


    @SelectProvider(method="selectPageListForSjbgdg",type=ImplementPlanMapperSqlConfig.class)
    @Results(value={
            @Result(column = "ID", property = "id"),
            @Result(column = "NO", property = "no"),
            @Result(column = "PLAN_ID", property = "planId"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "PLAN_PROJECT_ID", property = "planProjectId"),
            @Result(column = "PLAN_NAME", property = "planName"),
            @Result(column = "PLAN_PROJECT_NAME", property = "planProjectName" ),
            @Result(column = "PROJECT_TYPE", property = "projectType"),
            @Result(column = "AUDIT_ORG_ID", property = "auditOrgId"),
            @Result(column = "AUDIT_ORG_NAME", property = "auditOrgName"),
            @Result(column = "PLAN_YEAR", property = "planYear"),
            @Result(column = "PLAN_TIME", property = "planTime"),
            @Result(column = "PROJECT_SUMMARY", property = "projectSummary"),
            @Result(column = "PROJECT_ORDER_ID", property = "projectOrderId"),
            @Result(column = "PROJECT_ORDER_NAME", property = "projectOrderName"),
            @Result(column = "PLAN_STARTTIME", property = "planStarttime"),
            @Result(column = "PLAN_ENDTIME", property = "planEndtime"),
            @Result(column = "AUDIT_METHOD", property = "auditMethod"),
            @Result(column = "COST_ESTIMATION", property = "costEstimation"),
            @Result(column = "IS_WW", property = "isWw"),
            @Result(column = "TEMP_ID", property = "tempId"),
            @Result(column = "TEMP_NAME", property = "tempName"),
            @Result(column = "IMPLEMENT_TYPE", property = "implementType"),
            @Result(column = "DEPT_ID", property = "deptId"),
            @Result(column = "DEPT_NAME", property = "deptName"),
            @Result(column = "IMPLEMENT_STEPS", property = "implementSteps"),
            @Result(column = "AUDIT_REQUIREMENT", property = "auditRequirement"),
            @Result(column = "AUDIT_REASON", property = "auditReason"),
            @Result(column = "AUDIT_CONTENT", property = "auditContent"),
            @Result(column = "AUDIT_PROCESS", property = "auditProcess"),
            @Result(column = "AUDIT_RESULT_USE", property = "auditResultUse"),
            @Result(column = "OTHER_CONTENT", property = "otherContent"),
            @Result(column = "FPSTATUS", property = "fpStatus"),
            @Result(column = "UPDATESTATUS", property = "updateStatus"),
            @Result(column = "CURRENTSTATRE", property = "currentStatre"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "QDCODE", property = "qdcode"),
            @Result(column = "IMPLEMENTTIME", property = "implementtime"),
            @Result(column = "YQDATE", property = "yqdate"),
            @Result(column = "ZYKSRYIDS", property = "zyksryids"),
            @Result(column = "ZYKSTYPE", property = "zykstype"),
            @Result(column = "ZYKSRYRWNAMES", property = "zyksryrwnames"),
    })
    List<ImplementPlanEntity> selectPageListForSjbgdg(ImplementPlanEntity implementPlanEntity, TblStaffUtil user);
    
    

    @Select("SELECT count(*) FROM TBL_YQNS_AUDIT_MY_MANUSCRIPT WHERE PROJECTID = #{projectid}")
    Integer selectCountBydgnei(String projectid) throws Exception;
    
    @Select("SELECT count(*) FROM TBL_YQNS_AUDIT_MY_MANUSCRIPT WHERE PROJECTID = #{projectid} and STATUS=6")
    Integer selectCountBydgwc(String projectid) throws Exception;
    
    @Select("SELECT count(*) FROM TBL_YQNS_SJBG_JHYJG WHERE PROJECT_ID = #{projectid} and STATUS=6")
    Integer selectCountBysjyjsSt(String projectid) throws Exception;
    
    @Select("SELECT count(*) FROM TBL_YQNS_SJBG_SJBGDG WHERE PROJECT_ID = #{projectid} and STATUS=6")
    Integer selectCountBysjbgdg(String projectid) throws Exception;


    
}
