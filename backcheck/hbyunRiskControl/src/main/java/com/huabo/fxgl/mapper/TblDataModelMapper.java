package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.dto.DataModelQueryDTO;
import com.huabo.fxgl.entity.TblDataModel;
import com.huabo.fxgl.vo.DataModelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 数据模型表 Mapper 接口
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Mapper
public interface TblDataModelMapper extends BaseMapper<TblDataModel> {

    /**
     * 分页查询数据模型列表
     *
     * @param page 分页参数
     * @param queryDTO 查询条件
     * @return 数据模型列表
     */
    IPage<DataModelVO> selectDataModelPage(Page<DataModelVO> page, @Param("query") DataModelQueryDTO queryDTO);

    /**
     * 查询数据模型列表（用于PageHelper分页）
     *
     * @param queryDTO 查询条件
     * @return 数据模型列表
     */
    List<DataModelVO> selectDataModelList(@Param("query") DataModelQueryDTO queryDTO);

    /**
     * 根据模型编码查询数据模型
     * 
     * @param modelCode 模型编码
     * @return 数据模型
     */
    @Select("SELECT * FROM TBL_DATA_MODEL WHERE MODEL_CODE = #{modelCode}")
    TblDataModel selectByModelCode(@Param("modelCode") String modelCode);

    /**
     * 根据数据源ID查询模型列表
     * 
     * @param dataSourceId 数据源ID
     * @return 模型列表
     */
    @Select("SELECT * FROM TBL_DATA_MODEL WHERE DATA_SOURCE_ID = #{dataSourceId} ORDER BY CREATE_TIME DESC")
    List<TblDataModel> selectByDataSourceId(@Param("dataSourceId") String dataSourceId);

    /**
     * 更新模型执行信息
     * 
     * @param modelId 模型ID
     * @param executionResult 执行结果
     * @return 更新行数
     */
    @Update("UPDATE TBL_DATA_MODEL SET " +
            "EXECUTION_COUNT = NVL(EXECUTION_COUNT, 0) + 1, " +
            "LAST_EXECUTION_TIME = SYSDATE, " +
            "EXECUTION_RESULT = #{executionResult}, " +
            "UPDATE_TIME = SYSDATE " +
            "WHERE MODEL_ID = #{modelId}")
    int updateExecutionInfo(@Param("modelId") String modelId, @Param("executionResult") String executionResult);

    /**
     * 更新模型状态
     * 
     * @param modelId 模型ID
     * @param status 状态
     * @param updateUser 更新人
     * @return 更新行数
     */
    @Update("UPDATE TBL_DATA_MODEL SET STATUS = #{status}, UPDATE_USER = #{updateUser}, UPDATE_TIME = SYSDATE WHERE MODEL_ID = #{modelId}")
    int updateModelStatus(@Param("modelId") String modelId, @Param("status") String status, @Param("updateUser") String updateUser);

    /**
     * 更新模型启用状态
     * 
     * @param modelId 模型ID
     * @param isEnabled 是否启用
     * @param updateUser 更新人
     * @return 更新行数
     */
    @Update("UPDATE TBL_DATA_MODEL SET IS_ENABLED = #{isEnabled}, UPDATE_USER = #{updateUser}, UPDATE_TIME = SYSDATE WHERE MODEL_ID = #{modelId}")
    int updateModelEnabled(@Param("modelId") String modelId, @Param("isEnabled") String isEnabled, @Param("updateUser") String updateUser);

    /**
     * 获取已发布的模型列表
     * 
     * @return 已发布模型列表
     */
    @Select("SELECT * FROM TBL_DATA_MODEL WHERE STATUS = 'PUBLISHED' AND IS_ENABLED = 'Y' ORDER BY CREATE_TIME DESC")
    List<TblDataModel> selectPublishedModels();

    /**
     * 获取热门模型列表
     * 
     * @param limit 限制数量
     * @return 热门模型列表
     */
    @Select("SELECT * FROM TBL_DATA_MODEL WHERE STATUS = 'PUBLISHED' AND IS_ENABLED = 'Y' " +
            "ORDER BY EXECUTION_COUNT DESC, LAST_EXECUTION_TIME DESC " +
            "FETCH FIRST #{limit} ROWS ONLY")
    List<TblDataModel> selectPopularModels(@Param("limit") Integer limit);

    /**
     * 搜索数据模型
     * 
     * @param page 分页参数
     * @param keyword 关键词
     * @param modelType 模型类型
     * @return 搜索结果
     */
    IPage<DataModelVO> searchDataModels(Page<DataModelVO> page,
                                       @Param("keyword") String keyword,
                                       @Param("modelType") String modelType);

    /**
     * 自定义插入数据模型方法，避免MyBatis解析SQL语句中的参数
     *
     * @param modelId 模型ID
     * @param modelCode 模型编码
     * @param modelName 模型名称
     * @param modelType 模型类型
     * @param businessMeaning 业务含义
     * @param calculationLogic 计算逻辑
     * @param dataSourceId 数据源ID
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
     * @param templateType 模板类型
     * @param version 版本
     * @param status 状态
     * @param isEnabled 是否启用
     * @param executionCount 执行次数
     * @param lastExecutionTime 最后执行时间
     * @param executionResult 执行结果
     * @param createUser 创建人
     * @param createTime 创建时间
     * @param updateUser 更新人
     * @param updateTime 更新时间
     * @return 插入行数
     */
    int insertDataModel(
        @Param("modelId") String modelId,
        @Param("modelCode") String modelCode,
        @Param("modelName") String modelName,
        @Param("modelType") String modelType,
        @Param("businessMeaning") String businessMeaning,
        @Param("calculationLogic") String calculationLogic,
        @Param("dataSourceId") String dataSourceId,
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
        @Param("templateType") String templateType,
        @Param("version") String version,
        @Param("status") String status,
        @Param("isEnabled") String isEnabled,
        @Param("executionCount") Integer executionCount,
        @Param("lastExecutionTime") java.time.LocalDateTime lastExecutionTime,
        @Param("executionResult") String executionResult,
        @Param("createUser") String createUser,
        @Param("createTime") java.time.LocalDateTime createTime,
        @Param("updateUser") String updateUser,
        @Param("updateTime") java.time.LocalDateTime updateTime
    );

    /**
     * 获取数据模型统计信息
     * 
     * @return 统计信息
     */
    @Select("SELECT " +
            "COUNT(*) AS total_count, " +
            "SUM(CASE WHEN STATUS = 'DRAFT' THEN 1 ELSE 0 END) AS draft_count, " +
            "SUM(CASE WHEN STATUS = 'PUBLISHED' THEN 1 ELSE 0 END) AS published_count, " +
            "SUM(CASE WHEN STATUS = 'ARCHIVED' THEN 1 ELSE 0 END) AS archived_count, " +
            "SUM(CASE WHEN IS_ENABLED = 'Y' THEN 1 ELSE 0 END) AS enabled_count, " +
            "SUM(CASE WHEN IS_ENABLED = 'N' THEN 1 ELSE 0 END) AS disabled_count, " +
            "SUM(NVL(EXECUTION_COUNT, 0)) AS total_execution_count, " +
            "AVG(NVL(EXECUTION_COUNT, 0)) AS avg_execution_count " +
            "FROM TBL_DATA_MODEL")
    DataModelStatisticsVO selectDataModelStatistics();

    /**
     * 根据模型类型统计
     * 
     * @return 类型统计
     */
    @Select("SELECT MODEL_TYPE, COUNT(*) AS count FROM TBL_DATA_MODEL GROUP BY MODEL_TYPE")
    List<Map<String, Object>> selectModelTypeStatistics();

    /**
     * 根据创建时间统计（按月）
     * 
     * @return 时间统计
     */
    @Select("SELECT " +
            "TO_CHAR(CREATE_TIME, 'YYYY-MM') AS month, " +
            "COUNT(*) AS count " +
            "FROM TBL_DATA_MODEL " +
            "WHERE CREATE_TIME >= ADD_MONTHS(SYSDATE, -12) " +
            "GROUP BY TO_CHAR(CREATE_TIME, 'YYYY-MM') " +
            "ORDER BY month")
    List<Map<String, Object>> selectModelCreateStatistics();

    /**
     * 数据模型统计信息VO
     */
    class DataModelStatisticsVO {
        private Integer totalCount;
        private Integer draftCount;
        private Integer publishedCount;
        private Integer archivedCount;
        private Integer enabledCount;
        private Integer disabledCount;
        private Long totalExecutionCount;
        private Double avgExecutionCount;

        // getters and setters
        public Integer getTotalCount() { return totalCount; }
        public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
        public Integer getDraftCount() { return draftCount; }
        public void setDraftCount(Integer draftCount) { this.draftCount = draftCount; }
        public Integer getPublishedCount() { return publishedCount; }
        public void setPublishedCount(Integer publishedCount) { this.publishedCount = publishedCount; }
        public Integer getArchivedCount() { return archivedCount; }
        public void setArchivedCount(Integer archivedCount) { this.archivedCount = archivedCount; }
        public Integer getEnabledCount() { return enabledCount; }
        public void setEnabledCount(Integer enabledCount) { this.enabledCount = enabledCount; }
        public Integer getDisabledCount() { return disabledCount; }
        public void setDisabledCount(Integer disabledCount) { this.disabledCount = disabledCount; }
        public Long getTotalExecutionCount() { return totalExecutionCount; }
        public void setTotalExecutionCount(Long totalExecutionCount) { this.totalExecutionCount = totalExecutionCount; }
        public Double getAvgExecutionCount() { return avgExecutionCount; }
        public void setAvgExecutionCount(Double avgExecutionCount) { this.avgExecutionCount = avgExecutionCount; }
    }
}
