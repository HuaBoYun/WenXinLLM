package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.InvestigationEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblOrganization;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName InvestigationMapper
 * @Description
 * @DATE 2023/9/30
 */
public interface InvestigationMapper extends BaseMapper<InvestigationEntity> {

    @SelectProvider(method="selectByEntity",type=InvestigationMapperSqlConfig.class)
    @Results(id="investigationResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "PLAN_NO", property = "planNo"),
            @Result(column = "NO", property = "no"),
            @Result(column = "PROJECT_NAME", property = "projectName"),
            @Result(column = "CONTRACT_NO", property = "contractNo"),
            @Result(column = "GP_NO", property = "gpNo"),
            @Result(column = "SETTLEMENT_AMOUNT", property = "settlementAmount"),
            @Result(column = "MATERIAL_AMOUNT", property = "materialAmount" ),
            @Result(column = "CONTRACT_START_TIME", property = "contractStartTime" ),
            @Result(column = "CONTRACT_END_TIME", property = "contractEndTime"),
            @Result(column = "WORK_START_TIME", property = "workStartTime"),
            @Result(column = "WORK_END_TIME", property = "workEndTime"),
            @Result(column = "CONSTRUCTION_CONTENT", property = "constructionContent"),
            @Result(column = "SETTLEMENT_PROGRESS", property = "settlementProgress"),
            @Result(column = "PROJECT_ADDRESS", property = "projectAddress"),
            @Result(column = "ID", property= "attachments", javaType = List.class, many = @Many(select= "selectAttachmentById"))
    })
    List<InvestigationEntity> selectByEntity( InvestigationEntity investigationEntity) ;

    @SelectProvider(method="selectCountByEntity",type=InvestigationMapperSqlConfig.class)
    Integer selectCountByEntity( InvestigationEntity investigationEntity) throws Exception;
 
    @Select("SELECT * FROM TBL_YQNS_INVESTIGATION WHERE ID = #{id}")
    @ResultMap(value= "investigationResultMap")
    InvestigationEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=InvestigationMapperSqlConfig.class)
    void updateEntity(InvestigationEntity investigationEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=InvestigationMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(InvestigationEntity investigationEntity);

    @DeleteProvider(method="deleteByIds", type=InvestigationMapperSqlConfig.class)
    void deleteEntity(String ids);

    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_INVESTIGATION_ATT WHERE INVESTIGATE_ID = #{id})")
    List<TblAttachment> selectAttachmentById(BigDecimal id);

    @DeleteProvider(method="deleteAttachmentByIds", type=InvestigationMapperSqlConfig.class)
    void deleteAttachmentByIds(String ids);

    @InsertProvider(method="insertAttachments", type=InvestigationMapperSqlConfig.class)
    void insertAttachmentsWidthId(BigDecimal id, String attachmentId);
}
