package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.LeaveAudit2LEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName LeaveAudit2LMapper
 * @Description
 * @DATE 2023/9/14
 */
public interface LeaveAudit2LMapper extends BaseMapper<LeaveAudit2LEntity> {

    @SelectProvider(method="selectByEntity",type=LeaveAudit2LMapperSqlConfig.class)
    @Results(id="leaveAudit2LResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "LEAVENO", property = "leaveNo"),
            @Result(column = "REMARKS", property = "remarks"),
            @Result(column = "PROJECTTYPE", property = "projectType"),
            @Result(column = "AUDITSCOPE", property = "auditScope"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "AUDIT_ORG", property = "auditOrg", javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "AUDIT_ORG", property = "auditOrgId"),
            @Result(column = "ENTRUST_NO", property = "entrustNo"),
            @Result(column = "ENTRUST_TIME", property = "entrustTime"),
            @Result(column = "AUDIT_START_TIME", property = "auditStartTime" ),
            @Result(column = "AUDIT_END_TIME", property = "auditEndTime" ),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime")
    })
    List<LeaveAudit2LEntity> selectByEntity( LeaveAudit2LEntity leaveAudit2LEntity) ;

    
    @SelectProvider(method="selectListByDraftPlan",type=LeaveAudit2LMapperSqlConfig.class)
    @ResultMap(value= "leaveAudit2LResultMap")
    List<LeaveAudit2LEntity> selectListByDraftPlan(LeaveAudit2LEntity leaveAudit2LEntity, Integer sourceType, BigDecimal jhid);
    
    @SelectProvider(method="selectCountByEntity",type=LeaveAudit2LMapperSqlConfig.class)
    Integer selectCountByEntity( LeaveAudit2LEntity leaveAudit2LEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_LEAVE_AUDIT_2L WHERE ID = #{id}")
    @ResultMap(value= "leaveAudit2LResultMap")
    LeaveAudit2LEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=LeaveAudit2LMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void updateEntity(LeaveAudit2LEntity leaveAudit2LEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=LeaveAudit2LMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(LeaveAudit2LEntity leaveAudit2LEntity);

    @DeleteProvider(method="deleteByIds", type=LeaveAudit2LMapperSqlConfig.class)
    void deleteEntity(String ids);

    @SelectProvider(method="findByIds",type=LeaveAudit2LMapperSqlConfig.class)
    @Results({
            @Result(column = "ID", property = "id"),
            @Result(column = "LEAVENO", property = "leaveNo"),
            @Result(column = "REMARKS", property = "remarks"),
            @Result(column = "PROJECTTYPE", property = "projectType"),
            @Result(column = "AUDITSCOPE", property = "auditScope"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "AUDIT_ORG", property = "auditOrg", javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "AUDIT_ORG", property = "auditOrgId"),
            @Result(column = "ENTRUST_NO", property = "entrustNo"),
            @Result(column = "ENTRUST_TIME", property = "entrustTime"),
            @Result(column = "AUDIT_START_TIME", property = "auditStartTime" ),
            @Result(column = "AUDIT_END_TIME", property = "auditEndTime" ),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "ORGNAME", property = "auditOrgName"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime")
    })
    List<LeaveAudit2LEntity> findByIds(String ids);

    @Select("SELECT MAX(LEAVENO) FROM TBL_YQNS_LEAVE_AUDIT_2L WHERE LEAVENO LIKE '${currentYear}%' ")
	BigDecimal selectMaxAutoNo(Integer currentYear) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_LEAVE_AUDIT_2L WHERE LEAVENO = #{leaveNo}")
    @ResultMap(value= "leaveAudit2LResultMap")
	LeaveAudit2LEntity selectRequirementNoEntity(BigDecimal leaveNo) throws Exception;

    @SelectProvider(method="findListByAnalysis",type=LeaveAudit2LMapperSqlConfig.class)
    @ResultMap(value= "leaveAudit2LResultMap")
    List<LeaveAudit2LEntity> findListByAnalysis(Integer xmnd, String projectName);

}
