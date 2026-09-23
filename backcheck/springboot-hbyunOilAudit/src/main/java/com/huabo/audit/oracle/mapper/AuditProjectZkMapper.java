package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.*;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName AuditProjectZkMapper
 * @Description
 * @DATE 2023/10/17
 */
public interface AuditProjectZkMapper extends BaseMapper<AuditProjectZkEntity> {

    @SelectProvider(method="selectByEntity",type=AuditProjectZkMapperSqlConfig.class)
    @Results(id="auditProjectZkResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "RESULT_ID", property = "resultId"),
            @Result(column = "NO", property = "no"),
            @Result(column = "RESULT_ID", property = "result", javaType = TblYqnsResult.class, one=@One(select="com.huabo.audit.oracle.mapper.TblYqnsResultMapper.selectById")),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "AUDIT_ORG_ID", property = "auditOrg", javaType = TblOrganization.class,one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "AUDIT_ORG_ID", property = "auditOrgId"),
            @Result(column = "ZK_ORG_ID", property = "zkOrg", javaType = TblOrganization.class,one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "ZK_ORG_ID", property = "zkOrgId"),
            @Result(column = "REASON", property = "reason"),
            @Result(column = "MONEY", property = "money"),
            @Result(column = "REMARK", property = "remark"),
    })
    List<AuditProjectZkEntity> selectByEntity( AuditProjectZkEntity auditProjectZkEntity) ;

    @SelectProvider(method="selectCountByEntity",type=AuditProjectZkMapperSqlConfig.class)
    Integer selectCountByEntity( AuditProjectZkEntity auditProjectZkEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_PROJECT_ZK WHERE ID = #{id} ")
    @ResultMap("auditProjectZkResultMap")
    AuditProjectZkEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=AuditProjectZkMapperSqlConfig.class)
    void updateEntity(AuditProjectZkEntity auditProjectZkEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=AuditProjectZkMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(AuditProjectZkEntity auditProjectZkEntity);

    @DeleteProvider(method="deleteByIds", type=AuditProjectZkMapperSqlConfig.class)
    void deleteEntity(String ids);

}
