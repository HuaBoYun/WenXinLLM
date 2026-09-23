package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.InterimAuditDetailEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName InterimAuditDetailMapper
 * @Description
 * @DATE 2024/04/15
 */
public interface InterimAuditDetailMapper extends BaseMapper<InterimAuditDetailEntity> {

    @SelectProvider(method="selectByEntity",type=InterimAuditDetailMapperSqlConfig.class)
    @Results(id="interimAuditDetailResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "ORGID", property = "orgId"),
            @Result(column = "ORGID", property = "org", javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
            @Result(column = "AUDIT_INFO", property = "auditInfo"),
            @Result(column = "AUDIT_TIME", property = "auditTime"),
            @Result(column = "UNAUDIT_MONTH", property = "unauditMonth"),
            @Result(column = "UNAUDIT_YEAR", property = "unauditYear"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "WORK_START_TIME", property = "workStartTime" ),
            @Result(column = "WORK_END_TIME", property = "workEndTime" ),
            @Result(column = "DO_AUDIT_TIME", property = "doAuditTime" ),
			@Result(column = "TEAM_LEADER_ID", property = "teamLeaderId"),
			@Result(column = "SUB_TEAM_LEADER_ID", property = "subTeamLeaderId"),
			@Result(column = "LEADER_ID", property = "leaderId"),
            @Result(column = "PERSON_IDS", property = "personIds"),
			@Result(column = "CHIEF_REVIEWER_ID", property = "chiefReviewerId"),
			@Result(column = "DEPUTY_REVIEWER_ID", property = "deputyReviewerId"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "PROJECTLX", property = "projectlx"),
            @Result(column = "REMARKS", property = "remarks"),
            @Result(column = "LDNAME", property = "ldname"),
            @Result(column = "LDZW", property = "ldzw"),
            @Result(column = "CSYM", property = "csym"),
            @Result(column = "YJEXSJ", property = "yjexsj"),
            @Result(column = "ZJPROJECTNAME", property = "zjprojectname"),
            @Result(column = "RZSJ", property = "rzsj"),
            @Result(column = "SJFWSTARTTIME", property = "sjfwstarttime"),
            @Result(column = "SJFWENDTIME", property = "sjfwendtime"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime")
    })
    List<InterimAuditDetailEntity> selectByEntity( InterimAuditDetailEntity interimAuditDetailEntity) ;


    @Select("SELECT * FROM TBL_YQNS_INTERIM_AUDIT_DETAILS WHERE ID = #{id}")
    @ResultMap(value= "interimAuditDetailResultMap")
    InterimAuditDetailEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=InterimAuditDetailMapperSqlConfig.class) 
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void updateEntity(InterimAuditDetailEntity interimAuditDetailEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=InterimAuditDetailMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(InterimAuditDetailEntity interimAuditDetailEntity);

    @DeleteProvider(method="deleteByIds", type=InterimAuditDetailMapperSqlConfig.class)
    void deleteEntity(String ids);
    
    @Select("select ORGID,ORGNAME from TBL_ORGANIZATION WHERE ORGNAME = #{orgname}")
    TblOrganization selectOrgByName(String orgname) throws Exception;

    @SelectProvider(method="getListDraftPlan",type=InterimAuditDetailMapperSqlConfig.class)
    @ResultMap(value= "interimAuditDetailResultMap")
    List<InterimAuditDetailEntity> getListDraftPlan(InterimAuditDetailEntity interimAuditDetailEntity, Integer sourceType, BigDecimal jhid);


    @Select("SELECT * FROM TBL_YQNS_INTERIM_AUDIT_DETAILS WHERE ID IN (${idStrs})")
    @ResultMap(value= "interimAuditDetailResultMap")
	List<InterimAuditDetailEntity> findByIds(@Param("idStrs")String idStrs);


    @SelectProvider(method="findListByAnalysis",type=InterimAuditDetailMapperSqlConfig.class)
    @ResultMap(value= "interimAuditDetailResultMap")
    List<InterimAuditDetailEntity> findListByAnalysis(Integer xmnd, String projectName);
    
}
