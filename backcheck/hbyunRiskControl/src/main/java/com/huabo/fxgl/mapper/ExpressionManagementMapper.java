package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.dto.ExpressionQueryDTO;
import com.huabo.fxgl.entity.ExpressionManagement;
import com.huabo.fxgl.vo.ExpressionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 表达式管理Mapper接口
 * 
 * @author AI Assistant
 * @since 2025-01-21
 */
@Mapper
public interface ExpressionManagementMapper extends BaseMapper<ExpressionManagement> {

    /**
     * 分页查询表达式列表
     * 
     * @param page 分页对象
     * @param queryDTO 查询条件
     * @return 表达式VO列表
     */
    IPage<ExpressionVO> selectExpressionPage(Page<ExpressionVO> page, @Param("query") ExpressionQueryDTO queryDTO);

    /**
     * 根据ID查询表达式详情
     * 
     * @param expressionId 表达式ID
     * @return 表达式VO
     */
    ExpressionVO selectExpressionById(@Param("expressionId") String expressionId);

    /**
     * 检查表达式编码是否存在
     * 
     * @param expressionCode 表达式编码
     * @param expressionId 表达式ID(更新时排除自己)
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM TBL_EXPRESSION_MANAGEMENT WHERE EXPRESSION_CODE = #{expressionCode} " +
            "AND (#{expressionId} IS NULL OR EXPRESSION_ID != #{expressionId})")
    int checkExpressionCodeExists(@Param("expressionCode") String expressionCode, 
                                  @Param("expressionId") String expressionId);

    /**
     * 获取数据源列表
     * 
     * @return 数据源列表
     */
    @Select("SELECT SOURCE_ID as dataSourceId, SOURCE_NAME as dataSourceName, SOURCE_TYPE as sourceType " +
            "FROM TBL_DATA_SOURCE WHERE STATUS = 'ACTIVE' ORDER BY CREATE_TIME DESC")
    List<Map<String, Object>> getDataSourceList();

    /**
     * 根据数据源ID获取表列表
     * 
     * @param dataSourceId 数据源ID
     * @return 表列表
     */
    @Select("SELECT DISTINCT TABLE_NAME as tableName, TABLE_COMMENT as tableComment " +
            "FROM TBL_TABLE_STRUCTURE WHERE DATA_SOURCE_ID = #{dataSourceId} " +
            "ORDER BY TABLE_NAME")
    List<Map<String, Object>> getTableList(@Param("dataSourceId") String dataSourceId);

    /**
     * 根据数据源ID和表名获取字段列表
     * 
     * @param dataSourceId 数据源ID
     * @param tableName 表名
     * @return 字段列表
     */
    @Select("SELECT COLUMN_NAME as fieldName, COLUMN_TYPE as fieldType, " +
            "COLUMN_COMMENT as fieldComment, IS_PRIMARY_KEY as isPrimaryKey, " +
            "IS_NULLABLE as isNullable, COLUMN_ORDER as fieldOrder " +
            "FROM TBL_TABLE_STRUCTURE " +
            "WHERE DATA_SOURCE_ID = #{dataSourceId} AND TABLE_NAME = #{tableName} " +
            "ORDER BY COLUMN_ORDER")
    List<Map<String, Object>> getFieldList(@Param("dataSourceId") String dataSourceId, 
                                           @Param("tableName") String tableName);

    /**
     * 更新表达式执行统计
     * 
     * @param expressionId 表达式ID
     * @param isSuccess 是否成功
     */
    void updateExecutionStats(@Param("expressionId") String expressionId, 
                             @Param("isSuccess") boolean isSuccess);

    /**
     * 获取表达式统计信息
     *
     * @return 统计信息
     */
    @Select("SELECT " +
            "COUNT(*) as totalCount, " +
            "SUM(CASE WHEN IS_ENABLED = 'Y' THEN 1 ELSE 0 END) as enabledCount, " +
            "SUM(CASE WHEN IS_TEMPLATE = 'Y' THEN 1 ELSE 0 END) as templateCount, " +
            "AVG(COMPLEXITY_LEVEL) as avgComplexity, " +
            "SUM(EXECUTION_COUNT) as totalExecutions " +
            "FROM TBL_EXPRESSION_MANAGEMENT")
    Map<String, Object> getExpressionStatistics();

    /**
     * 获取最近创建的表达式
     *
     * @param limit 限制数量
     * @return 最近创建的表达式列表
     */
    @Select("SELECT EXPRESSION_ID, EXPRESSION_NAME, CREATE_TIME, CREATE_USER " +
            "FROM TBL_EXPRESSION_MANAGEMENT " +
            "ORDER BY CREATE_TIME DESC " +
            "LIMIT #{limit}")
    List<Map<String, Object>> getRecentCreatedExpressions(@Param("limit") int limit);

    /**
     * 获取最近执行的表达式
     *
     * @param limit 限制数量
     * @return 最近执行的表达式列表
     */
    @Select("SELECT EXPRESSION_ID, EXPRESSION_NAME, LAST_EXECUTION_TIME, LAST_EXECUTION_RESULT " +
            "FROM TBL_EXPRESSION_MANAGEMENT " +
            "WHERE LAST_EXECUTION_TIME IS NOT NULL " +
            "ORDER BY LAST_EXECUTION_TIME DESC " +
            "LIMIT #{limit}")
    List<Map<String, Object>> getRecentExecutedExpressions(@Param("limit") int limit);
}
