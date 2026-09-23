package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.*;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rui
 * @ClassName QualityMapper
 * @Description
 * @DATE 2023/10/10
 */
public interface QualityMapper extends BaseMapper<QualityEntity> {

    @SelectProvider(method="selectByEntity",type=QualityMapperSqlConfig.class)
    @Results(id="qualityResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "PROJECT_ID", property = "project", javaType = ImplementPlanEntity.class,one = @One(select = "com.huabo.audit.oracle.mapper.ImplementPlanMapper.selectById")),
            @Result(column = "PROJECT_ID", property = "projectId"),
            @Result(column = "START_TIME", property = "startTime"),
            @Result(column = "ORG_ID", property = "org", javaType = TblOrganization.class,one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "ORG_ID", property = "orgId"),
            @Result(column = "SCORE", property = "score"),
    })
    List<QualityEntity> selectByEntity( QualityEntity qualityEntity) ;

    @SelectProvider(method="selectCountByEntity",type=QualityMapperSqlConfig.class)
    Integer selectCountByEntity( QualityEntity qualityEntity) throws Exception;

    @Select("SELECT COUNT(*) FROM TBL_YQNS_SCORE_MANAGE WHERE ID IN ( SELECT SMID FROM TBL_YQNS_QUALITY_EVALUATE_ITEM WHERE QEID = ( SELECT ID FROM TBL_YQNS_QUALITY_EVALUATE WHERE STATUS = 1 ) ) AND STATUS = 1 AND TYPE = #{type}")
    Integer selectConfScoreManageCount(Integer type) throws Exception;

    @Select("SELECT * FROM TBL_YQNS_QUALITY WHERE ID = #{id} ")
    @Results(id="qualityWithItemResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "PROJECT_ID", property = "project", javaType = ImplementPlanEntity.class,one = @One(select = "com.huabo.audit.oracle.mapper.ImplementPlanMapper.selectById")),
            @Result(column = "PROJECT_ID", property = "projectId"),
            @Result(column = "START_TIME", property = "startTime"),
            @Result(column = "ORG_ID", property = "org", javaType = TblOrganization.class,one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "ORG_ID", property = "orgId"),
            @Result(column = "SCORE", property = "score"),
            @Result(column = "ID", property= "qualityItems", javaType = List.class, many = @Many(select= "selectQualityItemsById"))
    })
    QualityEntity selectById(String id) throws Exception;

    @UpdateProvider(method="updateEntity", type=QualityMapperSqlConfig.class)
    void updateEntity(QualityEntity qualityEntity) throws Exception;

    @InsertProvider(method="insertEntity", type=QualityMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(QualityEntity qualityEntity);

    @DeleteProvider(method="deleteByIds", type=QualityMapperSqlConfig.class)
    void deleteEntity(String ids);


    @Select("SELECT * FROM TBL_YQNS_QUALITY_ITEM WHERE QUALITY_ID = #{id}")
    @Results(id="qualityItemWidthScoreItemResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "SMITEM_ID", property = "smItem", javaType = ScoreManageItemEntity.class,one = @One(select="com.huabo.audit.oracle.mapper.ScoreManageMapper.selectScoreManageItemById")),
            @Result(column = "SCORE", property = "score"),
            @Result(column = "DEDUCT_REASON", property = "deductReason"),
    })
    List<QualityItemEntity> selectQualityItemsById(BigDecimal id);

    @DeleteProvider(method="deleteQualityItemsById", type=QualityMapperSqlConfig.class)
    void deleteQualityItemsById(String id);

    @InsertProvider(method="insertQualityItem", type=QualityMapperSqlConfig.class)
    void insertQualityItem(BigDecimal qualityId, QualityItemEntity qualityItemEntity );

    @Results(id="qualityByProjectIdResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "START_TIME", property = "startTime"),
            @Result(column = "ORG_ID", property = "org", javaType = TblOrganization.class,one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "ORG_ID", property = "orgId"),
            @Result(column = "SCORE", property = "score"),
    })
    @Select("SELECT * FROM TBL_YQNS_QUALITY WHERE PROJECT_ID = #{projectId}")
    @ResultMap("qualityWithItemResultMap")
    List<QualityEntity> selectQualityByProjectId(BigDecimal projectId);
}
