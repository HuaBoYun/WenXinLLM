package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.*;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zkl
 * @InterfaceName ProjectEvaluationMapper
 * @Description
 * @DATE 2024/04/13
 */
public interface ProjectEvaluationMapper extends BaseMapper<ProjectEvaluationEntity> {

    @SelectProvider(method="selectByEntity",type=ProjectEvaluationMapperSqlConfig.class)
    @Results(id="qualityResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "PROJECT_ID", property = "projectName", javaType = String.class,one = @One(select = "com.huabo.audit.oracle.mapper.ImplementPlanMapper.selectProjectNameById")),
            @Result(column = "PROJECT_ID", property = "projectId"),
            @Result(column = "START_TIME", property = "startTime"),
            @Result(column = "ORG_ID", property = "org", javaType = TblOrganization.class,one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "ORG_ID", property = "orgId")
    })
    List<ProjectEvaluationEntity> selectByEntity( ProjectEvaluationEntity entity) ;

    @Select("SELECT * FROM TBL_PROJECT_EVALUATION WHERE ID = #{id} ")
    @Results(id="qualityWithItemResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "PROJECT_ID", property = "projectName", javaType = String.class,one = @One(select = "com.huabo.audit.oracle.mapper.ImplementPlanMapper.selectProjectNameById")),
            @Result(column = "PROJECT_ID", property = "projectId"),
            @Result(column = "START_TIME", property = "startTime"),
            @Result(column = "ORG_ID", property = "org", javaType = TblOrganization.class,one= @One(select = "com.huabo.audit.oracle.mapper.TblOrganizationMapper.selectById")),
			@Result(column = "ORG_ID", property = "orgId"),
            @Result(column = "ID", property= "qualityItems", javaType = List.class, many = @Many(select= "selectQualityItemsById"))
    })
    ProjectEvaluationEntity selectById(BigDecimal id) throws Exception;

    @UpdateProvider(method="updateEntity", type=ProjectEvaluationMapperSqlConfig.class)
    void updateEntity(ProjectEvaluationEntity entity) throws Exception;

    @InsertProvider(method="insertEntity", type=ProjectEvaluationMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "ID")
    void insertEntity(ProjectEvaluationEntity entity);

    @DeleteProvider(method="deleteByIds", type=ProjectEvaluationMapperSqlConfig.class)
    void deleteEntity(String ids);


    @Select("SELECT * FROM TBL_PROJECT_EVALUATION_ITEM WHERE PROJECT_EVALUATION_ID = #{id}")
    @Results(id="qualityItemWidthScoreItemResultMap", value={
            @Result(column = "ID", property = "id"),
            @Result(column = "PROJECT_EVALUATION_ID", property = "projectEvaluationId"),
            @Result(column = "USER_ID", property = "userId"),
            @Result(column = "USER_ID", property = "userName", javaType = String.class,one= @One(select = "com.huabo.audit.oracle.mapper.TblStaffMapper.selectNameById")),
            @Result(column = "PROJECT_ROLE", property = "projectRole"),
            @Result(column = "BASE_SCORE", property = "baseScore"),
            @Result(column = "INCREASE_SCORE", property = "increaseScore"),
            @Result(column = "INCREASE_SCORE_REASON", property = "increaseScoreReason")
    })
    List<ProjectEvaluationItemEntity> selectQualityItemsById(BigDecimal id);

    @DeleteProvider(method="deleteQualityItemsById", type=ProjectEvaluationMapperSqlConfig.class)
    void deleteQualityItemsById(String id);

    @InsertProvider(method="insertQualityItem", type=ProjectEvaluationMapperSqlConfig.class)
    void insertQualityItem(BigDecimal qualityId, ProjectEvaluationItemEntity qualityItemEntity );
}
