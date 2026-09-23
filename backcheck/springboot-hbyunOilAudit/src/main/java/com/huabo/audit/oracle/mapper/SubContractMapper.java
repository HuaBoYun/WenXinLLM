package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.SubContractEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName SubContractMapper
 * @Description
 * @DATE 2023/9/30
 */
public interface SubContractMapper extends BaseMapper<SubContractEntity> {

    @SelectProvider(method="selectByEntity",type=SubContractMapperSqlConfig.class)
    @Results(id="subContractResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "NO", property = "no"),
            @Result(column = "GENERAL_CONTRACT_NO", property = "generalContractNo"),
            @Result(column = "SUB_CONTRACT_NO", property = "subContractNo"),
            @Result(column = "SUB_ORG_ID", property = "subOrg"  , javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "SUB_ORG_ID", property = "subOrgId"),
            @Result(column = "SUB_TYPE", property = "subType"),
            @Result(column = "SUB_CONTRACT_AMOUNT", property = "subContractAmount"),
            @Result(column = "SUB_SETTLEMENT_AMOUNT", property = "subSettlementAmount"),
            @Result(column = "QUOTA", property = "quota"),
            @Result(column = "PROJECT_ADDRESS", property = "projectAddress"),
            @Result(column = "SUB_CHECK_MODE", property = "subCheckMode"),
            @Result(column = "HAS_PROCEEDINGS", property = "hasProceedings"),
            @Result(column = "BUILD_ORG_ID", property = "buildOrg", javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "BUILD_ORG_ID", property = "buildOrgId"),
            @Result(column = "GENERAL_ORG_ID", property = "generalOrg"  , javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "GENERAL_ORG_ID", property = "generalOrgId"),
            @Result(column = "AUDIT_PERSON_ID", property = "auditPerson", javaType = TblStaff.class,one = @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectById")),
			@Result(column = "AUDIT_PERSON_ID", property = "auditPersonId"),
    })
    List<SubContractEntity> selectByEntity( SubContractEntity subContractEntity) ;

    @SelectProvider(method="selectCountByEntity",type=SubContractMapperSqlConfig.class)
    Integer selectCountByEntity( SubContractEntity subContractEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_SUB_CONTRACT WHERE ID = #{id}")
    @ResultMap(value= "subContractResultMap")
    SubContractEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=SubContractMapperSqlConfig.class)
    void updateEntity(SubContractEntity subContractEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=SubContractMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(SubContractEntity subContractEntity);

    @DeleteProvider(method="deleteByIds", type=SubContractMapperSqlConfig.class)
    void deleteEntity(String ids);
}
