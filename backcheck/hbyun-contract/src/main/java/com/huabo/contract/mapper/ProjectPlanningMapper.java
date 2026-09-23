package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.ProjectPlanning;
import com.huabo.contract.vo.ProjectPlanningQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 项目策划表 Mapper 接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface ProjectPlanningMapper extends BaseMapper<ProjectPlanning> {

    /**
     * 分页查询项目策划列表
     * 
     * @param param 查询参数
     * @return 项目策划列表
     */
    List<ProjectPlanning> selectProjectPlanningList(@Param("param") ProjectPlanningQueryParam param);

    /**
     * 根据策划编号查询项目策划
     * 
     * @param planningNo 策划编号
     * @return 项目策划
     */
    ProjectPlanning selectByPlanningNo(@Param("planningNo") String planningNo);

    /**
     * 根据项目ID查询项目策划列表
     * 
     * @param projectId 项目ID
     * @return 项目策划列表
     */
    List<ProjectPlanning> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 检查策划编号是否存在
     *
     * @param planningNo 策划编号
     * @param excludeId 排除的ID
     * @return 存在的数量
     */
    int existsPlanningNo(@Param("planningNo") String planningNo, @Param("excludeId") Long excludeId);

    /**
     * 查询指定前缀的最大序号
     *
     * @param prefix 编号前缀（如：PLAN20250121）
     * @return 最大序号，如果没有记录则返回0
     */
    int selectMaxSequenceByPrefix(@Param("prefix") String prefix);

    /**
     * 获取待审核的策划列表
     * 
     * @return 待审核的策划列表
     */
    List<ProjectPlanning> selectPendingReviewPlannings();

    /**
     * 获取执行中的策划列表
     * 
     * @return 执行中的策划列表
     */
    List<ProjectPlanning> selectInProgressPlannings();

    /**
     * 获取已完成的策划列表
     * 
     * @return 已完成的策划列表
     */
    List<ProjectPlanning> selectCompletedPlannings();

    /**
     * 获取延期的策划列表
     * 
     * @return 延期的策划列表
     */
    List<ProjectPlanning> selectDelayedPlannings();

    /**
     * 获取即将到期的策划列表
     * 
     * @param days 天数
     * @return 即将到期的策划列表
     */
    List<ProjectPlanning> selectExpiringSoonPlannings(@Param("days") Integer days);

    /**
     * 获取重点策划列表
     * 
     * @param minBudgetAmount 最小预算金额
     * @return 重点策划列表
     */
    List<ProjectPlanning> selectKeyPlannings(@Param("minBudgetAmount") java.math.BigDecimal minBudgetAmount);

    /**
     * 获取我负责的策划列表
     * 
     * @param userId 用户ID
     * @return 我负责的策划列表
     */
    List<ProjectPlanning> selectMyResponsiblePlannings(@Param("userId") Long userId);

    /**
     * 获取我参与的策划列表
     * 
     * @param userId 用户ID
     * @return 我参与的策划列表
     */
    List<ProjectPlanning> selectMyParticipatePlannings(@Param("userId") Long userId);

    /**
     * 批量更新策划状态
     * 
     * @param ids 策划ID列表
     * @param planningStatus 策划状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdatePlanningStatus(@Param("ids") List<Long> ids, 
                                 @Param("planningStatus") Integer planningStatus, 
                                 @Param("updateBy") Long updateBy);

    /**
     * 更新完成度
     * 
     * @param id 策划ID
     * @param completionRate 完成度
     * @return 更新数量
     */
    int updateCompletionRate(@Param("id") Long id, @Param("completionRate") java.math.BigDecimal completionRate);

    /**
     * 批量更新完成度
     * 
     * @param ids 策划ID列表
     * @param completionRate 完成度
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateCompletionRate(@Param("ids") List<Long> ids, 
                                 @Param("completionRate") java.math.BigDecimal completionRate, 
                                 @Param("updateBy") Long updateBy);

    /**
     * 统计项目策划数据
     * 
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> selectProjectPlanningStatistics(@Param("param") ProjectPlanningQueryParam param);

    /**
     * 获取策划类型分布统计
     * 
     * @param param 查询参数
     * @return 策划类型分布
     */
    List<Map<String, Object>> selectPlanningTypeDistribution(@Param("param") ProjectPlanningQueryParam param);

    /**
     * 获取策划阶段分布统计
     * 
     * @param param 查询参数
     * @return 策划阶段分布
     */
    List<Map<String, Object>> selectPlanningStageDistribution(@Param("param") ProjectPlanningQueryParam param);

    /**
     * 获取策划状态分布统计
     * 
     * @param param 查询参数
     * @return 策划状态分布
     */
    List<Map<String, Object>> selectPlanningStatusDistribution(@Param("param") ProjectPlanningQueryParam param);

    /**
     * 获取月度策划趋势
     * 
     * @param param 查询参数
     * @return 月度策划趋势
     */
    List<Map<String, Object>> selectMonthlyPlanningTrend(@Param("param") ProjectPlanningQueryParam param);

    /**
     * 获取预算金额分布统计
     * 
     * @param param 查询参数
     * @return 预算金额分布
     */
    List<Map<String, Object>> selectBudgetAmountDistribution(@Param("param") ProjectPlanningQueryParam param);

    /**
     * 获取完成度分布统计
     * 
     * @param param 查询参数
     * @return 完成度分布
     */
    List<Map<String, Object>> selectCompletionRateDistribution(@Param("param") ProjectPlanningQueryParam param);

    /**
     * 获取负责人分布统计
     * 
     * @param param 查询参数
     * @return 负责人分布
     */
    List<Map<String, Object>> selectResponsiblePersonDistribution(@Param("param") ProjectPlanningQueryParam param);

    /**
     * 模糊搜索项目策划
     * 
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目策划列表
     */
    List<ProjectPlanning> searchProjectPlannings(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 获取今日到期的策划列表
     * 
     * @return 今日到期的策划列表
     */
    List<ProjectPlanning> selectTodayExpiringPlannings();

    /**
     * 获取本周到期的策划列表
     * 
     * @return 本周到期的策划列表
     */
    List<ProjectPlanning> selectThisWeekExpiringPlannings();

    /**
     * 更新实际开始时间
     * 
     * @param id 策划ID
     * @param actualStartDate 实际开始时间
     * @return 更新数量
     */
    int updateActualStartDate(@Param("id") Long id, @Param("actualStartDate") java.util.Date actualStartDate);

    /**
     * 更新实际结束时间
     * 
     * @param id 策划ID
     * @param actualEndDate 实际结束时间
     * @return 更新数量
     */
    int updateActualEndDate(@Param("id") Long id, @Param("actualEndDate") java.util.Date actualEndDate);

    /**
     * 获取策划进度统计
     * 
     * @param param 查询参数
     * @return 进度统计
     */
    List<Map<String, Object>> selectPlanningProgressStatistics(@Param("param") ProjectPlanningQueryParam param);

    /**
     * 获取策划工期统计
     * 
     * @param param 查询参数
     * @return 工期统计
     */
    List<Map<String, Object>> selectPlanningDurationStatistics(@Param("param") ProjectPlanningQueryParam param);
}
