package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.BalanceProjectEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblOrganization;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName BalanceProjectMapper
 * @Description
 * @DATE 2023/9/30
 */
public interface BalanceProjectMapper extends BaseMapper<BalanceProjectEntity> {

    @SelectProvider(method="selectByEntity",type=BalanceProjectMapperSqlConfig.class)
    @Results(id="balanceProjectResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "PLAN_NO", property = "planNo"),
            @Result(column = "NO", property = "no"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "CONTRACT_NO", property = "contractNo"),
            @Result(column = "SETTLEMENT_AMOUNT", property = "settlementAmount"),
            @Result(column = "MATERIAL_AMOUNT", property = "materialAmount" ),
            @Result(column = "CONSTRUCTION_CONTENT", property = "constructionContent" ),
            @Result(column = "SURVEY_ORG_ID", property = "surveyOrg"  , javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "SURVEY_ORG_ID", property = "surveyOrgId"),
            @Result(column = "DESIGN_ORG_ID", property = "designOrg"  , javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "DESIGN_ORG_ID", property = "designOrgId"),
            @Result(column = "CONSTRUCT_ORG_ID", property = "constructOrg"  , javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "CONSTRUCT_ORG_ID", property = "constructOrgId"),
            @Result(column = "SUPERVISION_ORG_ID", property = "supervisionOrg"  , javaType = TblOrganization.class, one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "SUPERVISION_ORG_ID", property = "supervisionOrgId"),
            @Result(column = "BUILD_MANAGER", property = "buildManager"),
            @Result(column = "BUILD_MANAGER_PHONE", property = "buildManagerPhone"),
            @Result(column = "MATERIAL_MANAGER", property = "materialManager"),
            @Result(column = "MATERIAL_MANAGER_PHONE", property = "materialManagerPhone"),
            @Result(column = "CONSTRUCT_MANAGER", property = "constructManager"),
            @Result(column = "CONSTRUCT_MANAGER_PHONE", property = "constructManagerPhone"),
            @Result(column = "MANAGER", property = "manager"),
            @Result(column = "MANAGER_PHONE", property = "managerPhone"),
            @Result(column = "DESIGN_MANAGER", property = "designManager"),
            @Result(column = "DESIGN_MANAGER_PHONE", property = "designManagerPhone"),
            @Result(column = "REMARK", property = "remark"),
            @Result(column = "ID", property= "attachments", javaType = List.class, many = @Many(select= "selectAttachmentById"))
    })
    List<BalanceProjectEntity> selectByEntity( BalanceProjectEntity balanceProjectEntity) ;

    @SelectProvider(method="selectCountByEntity",type=BalanceProjectMapperSqlConfig.class)
    Integer selectCountByEntity( BalanceProjectEntity balanceProjectEntity) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_BALANCE_PROJECT WHERE ID = #{id}")
    @ResultMap(value= "balanceProjectResultMap")
    BalanceProjectEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=BalanceProjectMapperSqlConfig.class)
    void updateEntity(BalanceProjectEntity balanceProjectEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=BalanceProjectMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(BalanceProjectEntity balanceProjectEntity);

    @DeleteProvider(method="deleteByIds", type=BalanceProjectMapperSqlConfig.class)
    void deleteEntity(String ids);

    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_BALANCE_PROJECT_ATT WHERE PID = #{id})")
    List<TblAttachment> selectAttachmentById(BigDecimal id);

    @DeleteProvider(method="deleteAttachmentByIds", type=BalanceProjectMapperSqlConfig.class)
    void deleteAttachmentByIds(String ids);

    @InsertProvider(method="insertAttachments", type=BalanceProjectMapperSqlConfig.class)
    void insertAttachmentsWidthId(BigDecimal id, String attachmentId);
}
