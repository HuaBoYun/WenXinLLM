package com.management.accountant.mapper.ss;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ss.SsDigitalEmployee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数字员工数据访问层
 *
 * @author AI Assistant
 * @since 2024-01-15
 */
@Mapper
public interface SsDigitalEmployeeMapper extends BaseMapper<SsDigitalEmployee> {

    /**
     * 分页查询数字员工列表
     */
    IPage<SsDigitalEmployee> selectDigitalEmployeePage(Page<SsDigitalEmployee> page, @Param("params") Map<String, Object> params);

    /**
     * 根据状态查询数字员工列表
     */
    List<SsDigitalEmployee> selectByStatus(@Param("robotStatus") String robotStatus, @Param("tenantId") Long tenantId);

    /**
     * 根据类型查询数字员工列表
     */
    List<SsDigitalEmployee> selectByType(@Param("robotType") String robotType, @Param("tenantId") Long tenantId);

    /**
     * 根据分类查询数字员工列表
     */
    List<SsDigitalEmployee> selectByCategory(@Param("robotCategory") String robotCategory, @Param("tenantId") Long tenantId);

    /**
     * 根据部门查询数字员工列表
     */
    List<SsDigitalEmployee> selectByDepartment(@Param("departmentId") Long departmentId, @Param("tenantId") Long tenantId);

    /**
     * 根据负责人查询数字员工列表
     */
    List<SsDigitalEmployee> selectByOwner(@Param("ownerId") Long ownerId, @Param("tenantId") Long tenantId);

    /**
     * 根据开发者查询数字员工列表
     */
    List<SsDigitalEmployee> selectByDeveloper(@Param("developerId") Long developerId, @Param("tenantId") Long tenantId);

    /**
     * 根据时间范围查询数字员工列表
     */
    List<SsDigitalEmployee> selectByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                             @Param("endTime") LocalDateTime endTime, 
                                             @Param("tenantId") Long tenantId);

    /**
     * 根据成功率范围查询数字员工列表
     */
    List<SsDigitalEmployee> selectBySuccessRateRange(@Param("minRate") BigDecimal minRate, 
                                                    @Param("maxRate") BigDecimal maxRate, 
                                                    @Param("tenantId") Long tenantId);

    /**
     * 根据优先级查询数字员工列表
     */
    List<SsDigitalEmployee> selectByPriority(@Param("priority") Integer priority, @Param("tenantId") Long tenantId);

    /**
     * 查询活跃的数字员工列表
     */
    List<SsDigitalEmployee> selectActiveEmployees(@Param("tenantId") Long tenantId);

    /**
     * 查询非活跃的数字员工列表
     */
    List<SsDigitalEmployee> selectInactiveEmployees(@Param("tenantId") Long tenantId);

    /**
     * 查询维护中的数字员工列表
     */
    List<SsDigitalEmployee> selectMaintenanceEmployees(@Param("tenantId") Long tenantId);

    /**
     * 查询健康状态异常的数字员工列表
     */
    List<SsDigitalEmployee> selectUnhealthyEmployees(@Param("tenantId") Long tenantId);

    /**
     * 查询高性能数字员工列表
     */
    List<SsDigitalEmployee> selectHighPerformanceEmployees(@Param("minScore") BigDecimal minScore, @Param("tenantId") Long tenantId);

    /**
     * 查询低性能数字员工列表
     */
    List<SsDigitalEmployee> selectLowPerformanceEmployees(@Param("maxScore") BigDecimal maxScore, @Param("tenantId") Long tenantId);

    /**
     * 查询过载的数字员工列表
     */
    List<SsDigitalEmployee> selectOverloadedEmployees(@Param("maxQueueLength") Integer maxQueueLength, @Param("tenantId") Long tenantId);

    /**
     * 查询空闲的数字员工列表
     */
    List<SsDigitalEmployee> selectIdleEmployees(@Param("maxQueueLength") Integer maxQueueLength, @Param("tenantId") Long tenantId);

    /**
     * 更新数字员工状态
     */
    int updateRobotStatus(@Param("robotId") Long robotId, @Param("robotStatus") String robotStatus, @Param("tenantId") Long tenantId);

    /**
     * 批量更新数字员工状态
     */
    int batchUpdateRobotStatus(@Param("robotIds") List<Long> robotIds, @Param("robotStatus") String robotStatus, @Param("tenantId") Long tenantId);

    /**
     * 更新执行统计信息
     */
    int updateExecutionStats(@Param("robotId") Long robotId, 
                           @Param("executionCount") Long executionCount,
                           @Param("successCount") Long successCount,
                           @Param("failureCount") Long failureCount,
                           @Param("successRate") BigDecimal successRate,
                           @Param("tenantId") Long tenantId);

    /**
     * 更新性能指标
     */
    int updatePerformanceMetrics(@Param("robotId") Long robotId, 
                               @Param("avgExecutionTime") BigDecimal avgExecutionTime,
                               @Param("cpuUsage") BigDecimal cpuUsage,
                               @Param("memoryUsage") BigDecimal memoryUsage,
                               @Param("responseTime") BigDecimal responseTime,
                               @Param("tenantId") Long tenantId);

    /**
     * 更新健康状态
     */
    int updateHealthStatus(@Param("robotId") Long robotId, 
                         @Param("healthStatus") String healthStatus,
                         @Param("healthCheckResult") String healthCheckResult,
                         @Param("lastHealthCheckTime") LocalDateTime lastHealthCheckTime,
                         @Param("tenantId") Long tenantId);

    /**
     * 更新最后执行时间
     */
    int updateLastExecutionTime(@Param("robotId") Long robotId, 
                              @Param("lastExecutionTime") LocalDateTime lastExecutionTime, 
                              @Param("tenantId") Long tenantId);

    /**
     * 更新下次执行时间
     */
    int updateNextExecutionTime(@Param("robotId") Long robotId, 
                              @Param("nextExecutionTime") LocalDateTime nextExecutionTime, 
                              @Param("tenantId") Long tenantId);

    /**
     * 增加执行次数
     */
    int incrementExecutionCount(@Param("robotId") Long robotId, @Param("tenantId") Long tenantId);

    /**
     * 增加成功次数
     */
    int incrementSuccessCount(@Param("robotId") Long robotId, @Param("tenantId") Long tenantId);

    /**
     * 增加失败次数
     */
    int incrementFailureCount(@Param("robotId") Long robotId, @Param("tenantId") Long tenantId);

    /**
     * 重置统计信息
     */
    int resetStatistics(@Param("robotId") Long robotId, @Param("tenantId") Long tenantId);

    /**
     * 批量重置统计信息
     */
    int batchResetStatistics(@Param("robotIds") List<Long> robotIds, @Param("tenantId") Long tenantId);

    /**
     * 统计数字员工总数
     */
    Long countTotalEmployees(@Param("tenantId") Long tenantId);

    /**
     * 统计活跃数字员工数
     */
    Long countActiveEmployees(@Param("tenantId") Long tenantId);

    /**
     * 统计非活跃数字员工数
     */
    Long countInactiveEmployees(@Param("tenantId") Long tenantId);

    /**
     * 统计维护中数字员工数
     */
    Long countMaintenanceEmployees(@Param("tenantId") Long tenantId);

    /**
     * 统计各状态数字员工数量
     */
    List<Map<String, Object>> countByStatus(@Param("tenantId") Long tenantId);

    /**
     * 统计各类型数字员工数量
     */
    List<Map<String, Object>> countByType(@Param("tenantId") Long tenantId);

    /**
     * 统计各分类数字员工数量
     */
    List<Map<String, Object>> countByCategory(@Param("tenantId") Long tenantId);

    /**
     * 统计各部门数字员工数量
     */
    List<Map<String, Object>> countByDepartment(@Param("tenantId") Long tenantId);

    /**
     * 统计执行情况
     */
    Map<String, Object> selectExecutionStatistics(@Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime,
                                                 @Param("tenantId") Long tenantId);

    /**
     * 统计性能指标
     */
    Map<String, Object> selectPerformanceStatistics(@Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime,
                                                   @Param("tenantId") Long tenantId);

    /**
     * 统计健康状态分布
     */
    List<Map<String, Object>> selectHealthStatusDistribution(@Param("tenantId") Long tenantId);

    /**
     * 查询数字员工趋势数据
     */
    List<Map<String, Object>> selectEmployeeTrend(@Param("startDate") String startDate,
                                                 @Param("endDate") String endDate,
                                                 @Param("tenantId") Long tenantId);

    /**
     * 查询数字员工排行榜
     */
    List<Map<String, Object>> selectEmployeeRanking(@Param("rankingType") String rankingType,
                                                   @Param("limit") Integer limit,
                                                   @Param("tenantId") Long tenantId);

    /**
     * 查询工作负载统计
     */
    List<Map<String, Object>> selectWorkloadStatistics(@Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime,
                                                      @Param("tenantId") Long tenantId);

    /**
     * 查询资源使用统计
     */
    List<Map<String, Object>> selectResourceUsageStatistics(@Param("startTime") LocalDateTime startTime,
                                                           @Param("endTime") LocalDateTime endTime,
                                                           @Param("tenantId") Long tenantId);

    /**
     * 查询效率分析数据
     */
    List<Map<String, Object>> selectEfficiencyAnalysis(@Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime,
                                                      @Param("tenantId") Long tenantId);

    /**
     * 查询质量分析数据
     */
    List<Map<String, Object>> selectQualityAnalysis(@Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime,
                                                   @Param("tenantId") Long tenantId);

    /**
     * 查询成本分析数据
     */
    List<Map<String, Object>> selectCostAnalysis(@Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime,
                                                @Param("tenantId") Long tenantId);

    /**
     * 查询风险分析数据
     */
    List<Map<String, Object>> selectRiskAnalysis(@Param("tenantId") Long tenantId);

    /**
     * 检查编码是否存在
     */
    int checkCodeExists(@Param("robotCode") String robotCode, @Param("robotId") Long robotId, @Param("tenantId") Long tenantId);

    /**
     * 检查名称是否存在
     */
    int checkNameExists(@Param("robotName") String robotName, @Param("robotId") Long robotId, @Param("tenantId") Long tenantId);

    /**
     * 清理过期数据
     */
    int cleanExpiredData(@Param("expiredDays") Integer expiredDays, @Param("tenantId") Long tenantId);

    /**
     * 查询需要健康检查的数字员工
     */
    List<SsDigitalEmployee> selectEmployeesForHealthCheck(@Param("checkInterval") Integer checkInterval, @Param("tenantId") Long tenantId);

    /**
     * 查询需要维护的数字员工
     */
    List<SsDigitalEmployee> selectEmployeesForMaintenance(@Param("tenantId") Long tenantId);

    /**
     * 查询过期的数字员工
     */
    List<SsDigitalEmployee> selectExpiredEmployees(@Param("expiredDays") Integer expiredDays, @Param("tenantId") Long tenantId);
}
