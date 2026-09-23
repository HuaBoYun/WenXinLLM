package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.TblDataModelVersion;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 数据模型版本表 Mapper 接口
 *
 * @author AI Assistant
 * @since 2025-09-28
 */
@Mapper
public interface TblDataModelVersionMapper extends BaseMapper<TblDataModelVersion> {

    /**
     * 分页查询模型版本列表
     *
     * @param page 分页参数
     * @param modelId 模型ID
     * @param status 状态
     * @return 版本列表
     */
    @Select("<script>" +
            "SELECT v.*, ds.SOURCE_NAME AS DATA_SOURCE_NAME " +
            "FROM TBL_DATA_MODEL_VERSION v " +
            "LEFT JOIN TBL_DATA_SOURCE ds ON v.DATA_SOURCE_ID = ds.SOURCE_ID " +
            "WHERE v.MODEL_ID = #{modelId} " +
            "<if test='status != null and status != \"\"'>" +
            "AND v.STATUS = #{status} " +
            "</if>" +
            "ORDER BY v.CREATE_TIME DESC" +
            "</script>")
    IPage<TblDataModelVersion> selectVersionPage(Page<TblDataModelVersion> page, 
                                                 @Param("modelId") String modelId, 
                                                 @Param("status") String status);

    /**
     * 查询模型的所有版本
     *
     * @param modelId 模型ID
     * @return 版本列表
     */
    @Select("SELECT * FROM TBL_DATA_MODEL_VERSION WHERE MODEL_ID = #{modelId} ORDER BY CREATE_TIME DESC")
    List<TblDataModelVersion> selectByModelId(@Param("modelId") String modelId);

    /**
     * 查询模型的当前版本
     *
     * @param modelId 模型ID
     * @return 当前版本
     */
    @Select("SELECT * FROM TBL_DATA_MODEL_VERSION WHERE MODEL_ID = #{modelId} AND IS_CURRENT = 'Y'")
    TblDataModelVersion selectCurrentVersion(@Param("modelId") String modelId);

    /**
     * 查询模型的最新版本
     *
     * @param modelId 模型ID
     * @return 最新版本
     */
    @Select("SELECT * FROM (SELECT * FROM TBL_DATA_MODEL_VERSION WHERE MODEL_ID = #{modelId} ORDER BY CREATE_TIME DESC) WHERE ROWNUM = 1")
    TblDataModelVersion selectLatestVersion(@Param("modelId") String modelId);

    /**
     * 根据版本号查询版本
     *
     * @param modelId 模型ID
     * @param versionNo 版本号
     * @return 版本信息
     */
    @Select("SELECT * FROM TBL_DATA_MODEL_VERSION WHERE MODEL_ID = #{modelId} AND VERSION_NO = #{versionNo}")
    TblDataModelVersion selectByVersionNo(@Param("modelId") String modelId, @Param("versionNo") String versionNo);

    /**
     * 设置当前版本
     *
     * @param modelId 模型ID
     * @param versionId 版本ID
     * @return 更新行数
     */
    @Update("UPDATE TBL_DATA_MODEL_VERSION SET IS_CURRENT = CASE WHEN VERSION_ID = #{versionId} THEN 'Y' ELSE 'N' END WHERE MODEL_ID = #{modelId}")
    int updateCurrentVersion(@Param("modelId") String modelId, @Param("versionId") String versionId);

    /**
     * 更新版本状态
     *
     * @param versionId 版本ID
     * @param status 状态
     * @param updateUser 更新人
     * @return 更新行数
     */
    @Update("UPDATE TBL_DATA_MODEL_VERSION SET STATUS = #{status}, UPDATE_USER = #{updateUser}, UPDATE_TIME = SYSDATE WHERE VERSION_ID = #{versionId}")
    int updateVersionStatus(@Param("versionId") String versionId, @Param("status") String status, @Param("updateUser") String updateUser);

    /**
     * 发布版本
     *
     * @param versionId 版本ID
     * @param updateUser 更新人
     * @return 更新行数
     */
    @Update("UPDATE TBL_DATA_MODEL_VERSION SET STATUS = 'PUBLISHED', IS_ENABLED = 'Y', PUBLISH_TIME = SYSDATE, UPDATE_USER = #{updateUser}, UPDATE_TIME = SYSDATE WHERE VERSION_ID = #{versionId}")
    int publishVersion(@Param("versionId") String versionId, @Param("updateUser") String updateUser);

    /**
     * 归档版本
     *
     * @param versionId 版本ID
     * @param updateUser 更新人
     * @return 更新行数
     */
    @Update("UPDATE TBL_DATA_MODEL_VERSION SET STATUS = 'ARCHIVED', IS_ENABLED = 'N', ARCHIVE_TIME = SYSDATE, UPDATE_USER = #{updateUser}, UPDATE_TIME = SYSDATE WHERE VERSION_ID = #{versionId}")
    int archiveVersion(@Param("versionId") String versionId, @Param("updateUser") String updateUser);

    /**
     * 统计模型版本数量
     *
     * @param modelId 模型ID
     * @return 版本数量
     */
    @Select("SELECT COUNT(*) FROM TBL_DATA_MODEL_VERSION WHERE MODEL_ID = #{modelId}")
    int countByModelId(@Param("modelId") String modelId);

    /**
     * 检查版本号是否存在
     *
     * @param modelId 模型ID
     * @param versionNo 版本号
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM TBL_DATA_MODEL_VERSION WHERE MODEL_ID = #{modelId} AND VERSION_NO = #{versionNo}")
    int countByVersionNo(@Param("modelId") String modelId, @Param("versionNo") String versionNo);

    /**
     * 获取下一个版本号
     *
     * @param modelId 模型ID
     * @return 下一个版本号
     */
    @Select("SELECT 'v' || LPAD(NVL(MAX(TO_NUMBER(SUBSTR(VERSION_NO, 2))), 0) + 1, 2, '0') " +
            "FROM TBL_DATA_MODEL_VERSION WHERE MODEL_ID = #{modelId} AND VERSION_NO LIKE 'v%'")
    String getNextVersionNo(@Param("modelId") String modelId);

    /**
     * 复制模型数据到版本表
     *
     * @param versionId 版本ID
     * @param modelId 模型ID
     * @param versionNo 版本号
     * @param changeDescription 变更说明
     * @param createUser 创建人
     * @return 插入行数
     */
    /**
     * 从当前版本复制数据创建新版本（优先从当前版本复制，如果没有当前版本则从主模型表复制）
     */
    @Insert("<script>" +
            "INSERT INTO TBL_DATA_MODEL_VERSION (" +
            "VERSION_ID, MODEL_ID, VERSION_NO, MODEL_CODE, MODEL_NAME, MODEL_TYPE, " +
            "BUSINESS_MEANING, CALCULATION_LOGIC, DATA_SOURCE_ID, SQL_STATEMENT, " +
            "WITH_CLAUSE, SELECT_CLAUSE, FROM_CLAUSE, WHERE_CLAUSE, GROUP_BY_CLAUSE, " +
            "HAVING_CLAUSE, ORDER_BY_CLAUSE, DRAG_CONFIG, THRESHOLD_CONFIG, WARNING_CONFIG, " +
            "TEMPLATE_ID, PARAMETER_CONFIG, TEMPLATE_TYPE, STATUS, IS_ENABLED, IS_CURRENT, " +
            "CHANGE_DESCRIPTION, CREATE_USER, CREATE_TIME" +
            ") " +
            "SELECT " +
            "#{versionId}, MODEL_ID, #{versionNo}, MODEL_CODE, MODEL_NAME, MODEL_TYPE, " +
            "BUSINESS_MEANING, CALCULATION_LOGIC, DATA_SOURCE_ID, SQL_STATEMENT, " +
            "WITH_CLAUSE, SELECT_CLAUSE, FROM_CLAUSE, WHERE_CLAUSE, GROUP_BY_CLAUSE, " +
            "HAVING_CLAUSE, ORDER_BY_CLAUSE, DRAG_CONFIG, THRESHOLD_CONFIG, WARNING_CONFIG, " +
            "TEMPLATE_ID, PARAMETER_CONFIG, TEMPLATE_TYPE, 'DRAFT', 'N', 'N', " +
            "#{changeDescription}, #{createUser}, SYSDATE " +
            "FROM TBL_DATA_MODEL " +
            "WHERE MODEL_ID = #{modelId}" +
            "</script>")
    int copyFromModel(@Param("versionId") String versionId,
                     @Param("modelId") String modelId,
                     @Param("versionNo") String versionNo,
                     @Param("changeDescription") String changeDescription,
                     @Param("createUser") String createUser);

    /**
     * 将版本数据同步到主模型表
     *
     * @param modelName 模型名称
     * @param businessMeaning 业务含义
     * @param calculationLogic 计算逻辑
     * @param sqlStatement SQL语句
     * @param withClause WITH子句
     * @param selectClause SELECT子句
     * @param fromClause FROM子句
     * @param whereClause WHERE子句
     * @param groupByClause GROUP BY子句
     * @param havingClause HAVING子句
     * @param orderByClause ORDER BY子句
     * @param dragConfig 拖拽配置
     * @param thresholdConfig 阈值配置
     * @param warningConfig 预警配置
     * @param templateId 模板ID
     * @param parameterConfig 参数配置
     * @param updateUser 更新人
     * @param modelId 模型ID
     * @return 更新行数
     */
    @Update("UPDATE TBL_DATA_MODEL SET " +
            "MODEL_NAME = #{modelName}, " +
            "BUSINESS_MEANING = #{businessMeaning}, " +
            "CALCULATION_LOGIC = #{calculationLogic}, " +
            "SQL_STATEMENT = #{sqlStatement}, " +
            "WITH_CLAUSE = #{withClause}, " +
            "SELECT_CLAUSE = #{selectClause}, " +
            "FROM_CLAUSE = #{fromClause}, " +
            "WHERE_CLAUSE = #{whereClause}, " +
            "GROUP_BY_CLAUSE = #{groupByClause}, " +
            "HAVING_CLAUSE = #{havingClause}, " +
            "ORDER_BY_CLAUSE = #{orderByClause}, " +
            "DRAG_CONFIG = #{dragConfig}, " +
            "THRESHOLD_CONFIG = #{thresholdConfig}, " +
            "WARNING_CONFIG = #{warningConfig}, " +
            "TEMPLATE_ID = #{templateId}, " +
            "PARAMETER_CONFIG = #{parameterConfig}, " +
            "UPDATE_USER = #{updateUser}, " +
            "UPDATE_TIME = SYSDATE " +
            "WHERE MODEL_ID = #{modelId}")
    int updateModelFromVersion(@Param("modelName") String modelName,
                              @Param("businessMeaning") String businessMeaning,
                              @Param("calculationLogic") String calculationLogic,
                              @Param("sqlStatement") String sqlStatement,
                              @Param("withClause") String withClause,
                              @Param("selectClause") String selectClause,
                              @Param("fromClause") String fromClause,
                              @Param("whereClause") String whereClause,
                              @Param("groupByClause") String groupByClause,
                              @Param("havingClause") String havingClause,
                              @Param("orderByClause") String orderByClause,
                              @Param("dragConfig") String dragConfig,
                              @Param("thresholdConfig") String thresholdConfig,
                              @Param("warningConfig") String warningConfig,
                              @Param("templateId") String templateId,
                              @Param("parameterConfig") String parameterConfig,
                              @Param("updateUser") String updateUser,
                              @Param("modelId") String modelId);
}
