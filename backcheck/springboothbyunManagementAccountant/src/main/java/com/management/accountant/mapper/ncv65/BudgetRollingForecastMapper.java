package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetRollingForecast;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 滚动预测数据访问接口
 * 
 * @description 滚动预测数据访问层，提供滚动预测的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetRollingForecastMapper extends BaseMapper<BudgetRollingForecast> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据预测编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_ROLLING_FORECAST WHERE FORECAST_CODE = #{forecastCode} AND IS_DELETED = 0")
    BudgetRollingForecast selectByForecastCode(@Param("forecastCode") String forecastCode);

    /**
     * 根据预测类型查询预测列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ROLLING_FORECAST WHERE FORECAST_TYPE = #{forecastType} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetRollingForecast> selectByForecastType(@Param("forecastType") String forecastType);

    /**
     * 根据预测方法查询预测列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ROLLING_FORECAST WHERE FORECAST_METHOD = #{forecastMethod} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetRollingForecast> selectByForecastMethod(@Param("forecastMethod") String forecastMethod);

    /**
     * 根据预测状态查询预测列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ROLLING_FORECAST WHERE FORECAST_STATUS = #{forecastStatus} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetRollingForecast> selectByForecastStatus(@Param("forecastStatus") String forecastStatus);

    /**
     * 根据组织ID查询预测列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ROLLING_FORECAST WHERE ORGANIZATION_ID = #{organizationId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetRollingForecast> selectByOrganizationId(@Param("organizationId") String organizationId);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询滚动预测
     */
    IPage<BudgetRollingForecast> selectBudgetRollingForecastPage(Page<BudgetRollingForecast> page, @Param("params") Map<String, Object> params);

    /**
     * 查询激活的预测列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ROLLING_FORECAST WHERE FORECAST_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetRollingForecast> selectActiveForecasts();

    /**
     * 查询自动执行的预测列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_ROLLING_FORECAST WHERE IS_AUTO_EXECUTION = 1 AND FORECAST_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY NEXT_EXECUTION_TIME")
    List<BudgetRollingForecast> selectAutoExecutionForecasts();

    /**
     * 查询需要执行的预测列表
     */
    List<BudgetRollingForecast> selectForecastsToExecute();

    // ==================== 业务操作方法 ====================

    /**
     * 激活预测
     */
    int activateForecast(@Param("forecastId") String forecastId, @Param("updateBy") String updateBy);

    /**
     * 停用预测
     */
    int deactivateForecast(@Param("forecastId") String forecastId, @Param("updateBy") String updateBy);

    /**
     * 开始执行预测
     */
    int startExecution(@Param("forecastId") String forecastId, @Param("updateBy") String updateBy);

    /**
     * 完成预测执行
     */
    int completeExecution(@Param("forecastId") String forecastId, @Param("updateBy") String updateBy);

    /**
     * 更新执行统计
     */
    int updateExecutionStatistics(@Param("forecastId") String forecastId, 
                                 @Param("executionTime") Long executionTime,
                                 @Param("isSuccess") Boolean isSuccess,
                                 @Param("accuracy") java.math.BigDecimal accuracy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计预测总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_ROLLING_FORECAST WHERE IS_DELETED = 0")
    int countTotalForecasts();

    /**
     * 按预测类型统计数量
     */
    List<Map<String, Object>> countForecastsByType();

    /**
     * 按预测方法统计数量
     */
    List<Map<String, Object>> countForecastsByMethod();

    /**
     * 按预测状态统计数量
     */
    List<Map<String, Object>> countForecastsByStatus();

    // ==================== 数据验证方法 ====================

    /**
     * 检查预测编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_ROLLING_FORECAST WHERE FORECAST_CODE = #{forecastCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkForecastCodeExists(@Param("forecastCode") String forecastCode, @Param("excludeId") String excludeId);

    /**
     * 检查预测是否可以删除
     */
    boolean checkForecastCanDelete(@Param("forecastId") String forecastId);

    /**
     * 检查预测是否可以修改
     */
    boolean checkForecastCanModify(@Param("forecastId") String forecastId);
}
