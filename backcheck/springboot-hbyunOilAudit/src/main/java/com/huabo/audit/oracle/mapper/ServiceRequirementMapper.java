package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.ServiceRequirementEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName ServiceRequirementMapper
 * @Description
 * @DATE 2023/9/7
 */ 
public interface ServiceRequirementMapper extends BaseMapper<ServiceRequirementEntity> {

    @SelectProvider(method="selectByEntity",type=ServiceRequirementMapperSqlConfig.class)
    @Results(id="serviceRequirementResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "REQUIREMENTNO", property = "requirementNo"),
            @Result(column = "AUDIT_ITEM", property = "auditItem"),
            @Result(column = "AUDIT_PURPOSE", property = "auditPurpose"),
            @Result(column = "CONCERNS_CONTENT", property = "concernsContent"),
            @Result(column = "UNIT_ID", property = "organization", javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "UNIT_ID", property = "organizationId"),
            @Result(column = "PROJECT_TYPE", property = "projectType"),
            @Result(column = "UNIT_RANGE", property = "unitRange" ),
            @Result(column = "TIME_RANGE", property = "timeRange" ),
            @Result(column = "PERSON_IDS", property = "personIds"),
            @Result(column = "REMARK", property = "remark"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATE_USER", property = "createUser", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
            @Result(column = "CREATE_TIME", property = "createTime")
    })
    List<ServiceRequirementEntity> selectByEntity( ServiceRequirementEntity serviceRequirementEntity) ;

    @SelectProvider(method="selectCountByEntity",type=ServiceRequirementMapperSqlConfig.class)
    Integer selectCountByEntity( ServiceRequirementEntity serviceRequirementEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_SERVICE_REQUIREMENT WHERE ID = #{id}")
    @ResultMap(value= "serviceRequirementResultMap")
    ServiceRequirementEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=ServiceRequirementMapperSqlConfig.class)
    void updateEntity(ServiceRequirementEntity serviceRequirementEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=ServiceRequirementMapperSqlConfig.class)
    void insertEntity(ServiceRequirementEntity serviceRequirementEntity);

    @DeleteProvider(method="deleteByIds", type=ServiceRequirementMapperSqlConfig.class)
    void deleteEntity(String ids);

    @Select("SELECT MAX(REQUIREMENTNO) FROM TBL_YQNS_SERVICE_REQUIREMENT WHERE REQUIREMENTNO LIKE '${currentYear}%' ")
	BigDecimal selectMaxAutoNo(Integer currentYear) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_SERVICE_REQUIREMENT WHERE REQUIREMENTNO = #{requirementNo}")
    @ResultMap(value= "serviceRequirementResultMap")
	ServiceRequirementEntity selectRequirementNoEntity(BigDecimal requirementNo) throws Exception;
}
