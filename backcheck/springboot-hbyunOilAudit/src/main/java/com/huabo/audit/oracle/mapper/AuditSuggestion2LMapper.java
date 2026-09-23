package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.AuditSuggestion2LEntity;
import com.huabo.audit.oracle.entity.LeaveAudit2LEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * @author Rui
 * @ClassName AuditSuggestion2LMapper
 * @Description
 * @DATE 2023/9/23
 */
public interface AuditSuggestion2LMapper extends BaseMapper<AuditSuggestion2LEntity> {

    @SelectProvider(method="selectByEntity",type=AuditSuggestion2LMapperSqlConfig.class)
    @Results(id="auditSuggestion2LResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "ORG", property = "org", javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "ORG", property = "orgId"),
            @Result(column = "NAME", property = "name"),
            @Result(column = "JOB", property = "job"),
            @Result(column = "LV", property = "level" ),
            @Result(column = "ENTRUSTTIME", property = "entrustTime" ),
            @Result(column = "AUDITSCOPE", property = "auditScope" ),
            @Result(column = "REMARKS", property = "remarks" ),
            @Result(column = "PROJECTTYPE", property = "projectType" ),
            @Result(column = "WORK_START_TIME", property = "workStartTime" ),
            @Result(column = "WORK_END_TIME", property = "workEndTime" ),
            @Result(column = "WORK_DURATION", property = "workDuration" ),
            @Result(column = "HAS_FINANCE_PROBLEM", property = "hasFinanceProblem" ),
            @Result(column = "HAS_ECONOMIC_PROBLEM", property = "hasEconomicProblem" ),
            @Result(column = "HAS_BEEN_COMPLAIN", property = "hasBeenComplain" ),
            @Result(column = "IS_LEAVE_NEXT_YEAR", property = "isLeaveNextYear" ),
            @Result(column = "NEED_AUDIT", property = "needAudit" ),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime")
    })
    List<AuditSuggestion2LEntity> selectByEntity( AuditSuggestion2LEntity auditSuggestion2LEntity) ;

    @SelectProvider(method="selectCountByEntity",type=AuditSuggestion2LMapperSqlConfig.class)
    Integer selectCountByEntity( AuditSuggestion2LEntity auditSuggestion2LEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_2L_AUDIT_SUGGESTION WHERE ID = #{id}")
    @ResultMap(value= "auditSuggestion2LResultMap")
    AuditSuggestion2LEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=AuditSuggestion2LMapperSqlConfig.class)
    void updateEntity(AuditSuggestion2LEntity auditSuggestion2LEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=AuditSuggestion2LMapperSqlConfig.class)
    void insertEntity(AuditSuggestion2LEntity auditSuggestion2LEntity);

    @DeleteProvider(method="deleteByIds", type=AuditSuggestion2LMapperSqlConfig.class)
    void deleteEntity(String ids);


    @SelectProvider(method="findByIds",type=AuditSuggestion2LMapperSqlConfig.class)
    @ResultMap(value= "auditSuggestion2LResultMap")
    List<AuditSuggestion2LEntity> findByIds(String ids);

    @SelectProvider(method="selectListDraftPlan",type=AuditSuggestion2LMapperSqlConfig.class)
    @ResultMap(value= "auditSuggestion2LResultMap")
    List<AuditSuggestion2LEntity> selectListDraftPlan(AuditSuggestion2LEntity auditSuggestion2LEntity);
}
