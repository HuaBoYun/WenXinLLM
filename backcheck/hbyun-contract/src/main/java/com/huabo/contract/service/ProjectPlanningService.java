package com.huabo.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.ProjectPlanning;
import com.huabo.contract.vo.ProjectPlanningQueryParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目策划表 服务类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface ProjectPlanningService extends IService<ProjectPlanning> {

    /**
     * 分页查询项目策划列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<ProjectPlanning> getProjectPlanningList(ProjectPlanningQueryParam param);

    /**
     * 保存项目策划（新增或修改）
     * 
     * @param projectPlanning 项目策划
     * @return 保存结果
     */
    boolean saveProjectPlanning(ProjectPlanning projectPlanning);

    /**
     * 根据ID获取项目策划详情
     * 
     * @param id 主键ID
     * @return 项目策划详情
     */
    ProjectPlanning getProjectPlanningById(Long id);

    /**
     * 根据策划编号获取项目策划
     * 
     * @param planningNo 策划编号
     * @return 项目策划
     */
    ProjectPlanning getProjectPlanningByPlanningNo(String planningNo);

    /**
     * 删除项目策划
     * 
     * @param id 主键ID
     * @return 删除结果
     */
    boolean deleteProjectPlanning(Long id);

    /**
     * 批量删除项目策划
     * 
     * @param ids 主键ID列表
     * @return 删除结果
     */
    boolean batchDeleteProjectPlanning(List<Long> ids);

    /**
     * 生成策划编号
     * 
     * @return 策划编号
     */
    String generatePlanningNo();

    /**
     * 检查策划编号是否存在
     * 
     * @param planningNo 策划编号
     * @param excludeId 排除的ID（用于修改时排除自己）
     * @return 存在返回true，不存在返回false
     */
    boolean existsPlanningNo(String planningNo, Long excludeId);

    /**
     * 更新策划状态
     * 
     * @param id 主键ID
     * @param planningStatus 策划状态
     * @return 更新结果
     */
    boolean updatePlanningStatus(Long id, Integer planningStatus);

    /**
     * 批量更新策划状态
     * 
     * @param ids 主键ID列表
     * @param planningStatus 策划状态
     * @return 更新结果
     */
    boolean batchUpdatePlanningStatus(List<Long> ids, Integer planningStatus);

    /**
     * 更新完成度
     * 
     * @param id 主键ID
     * @param completionRate 完成度
     * @return 更新结果
     */
    boolean updateCompletionRate(Long id, BigDecimal completionRate);

    /**
     * 批量更新完成度
     * 
     * @param ids 主键ID列表
     * @param completionRate 完成度
     * @return 更新结果
     */
    boolean batchUpdateCompletionRate(List<Long> ids, BigDecimal completionRate);

    /**
     * 审核策划
     * 
     * @param id 主键ID
     * @param reviewerId 审核人ID
     * @param reviewerName 审核人姓名
     * @param reviewComments 审核意见
     * @param approved 是否通过
     * @return 审核结果
     */
    boolean reviewPlanning(Long id, Long reviewerId, String reviewerName, String reviewComments, boolean approved);

    /**
     * 开始执行策划
     * 
     * @param id 主键ID
     * @return 执行结果
     */
    boolean startPlanning(Long id);

    /**
     * 完成策划
     * 
     * @param id 主键ID
     * @return 完成结果
     */
    boolean completePlanning(Long id);

    /**
     * 暂停策划
     * 
     * @param id 主键ID
     * @return 暂停结果
     */
    boolean pausePlanning(Long id);

    /**
     * 取消策划
     * 
     * @param id 主键ID
     * @return 取消结果
     */
    boolean cancelPlanning(Long id);

    /**
     * 获取待审核的策划列表
     * 
     * @return 待审核的策划列表
     */
    List<ProjectPlanning> getPendingReviewPlannings();

    /**
     * 获取执行中的策划列表
     * 
     * @return 执行中的策划列表
     */
    List<ProjectPlanning> getInProgressPlannings();

    /**
     * 获取已完成的策划列表
     * 
     * @return 已完成的策划列表
     */
    List<ProjectPlanning> getCompletedPlannings();

    /**
     * 获取延期的策划列表
     * 
     * @return 延期的策划列表
     */
    List<ProjectPlanning> getDelayedPlannings();

    /**
     * 获取即将到期的策划列表
     * 
     * @param days 天数
     * @return 即将到期的策划列表
     */
    List<ProjectPlanning> getExpiringSoonPlannings(Integer days);

    /**
     * 获取重点策划列表
     * 
     * @param minBudgetAmount 最小预算金额
     * @return 重点策划列表
     */
    List<ProjectPlanning> getKeyPlannings(BigDecimal minBudgetAmount);

    /**
     * 获取我负责的策划列表
     * 
     * @param userId 用户ID
     * @return 我负责的策划列表
     */
    List<ProjectPlanning> getMyResponsiblePlannings(Long userId);

    /**
     * 获取我参与的策划列表
     * 
     * @param userId 用户ID
     * @return 我参与的策划列表
     */
    List<ProjectPlanning> getMyParticipatePlannings(Long userId);

    /**
     * 根据项目ID查询策划列表
     * 
     * @param projectId 项目ID
     * @return 策划列表
     */
    List<ProjectPlanning> getProjectPlanningByProjectId(Long projectId);

    /**
     * 统计项目策划数据
     * 
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> getProjectPlanningStatistics(ProjectPlanningQueryParam param);

    /**
     * 获取策划类型分布统计
     * 
     * @param param 查询参数
     * @return 策划类型分布
     */
    List<Map<String, Object>> getPlanningTypeDistribution(ProjectPlanningQueryParam param);

    /**
     * 获取策划阶段分布统计
     * 
     * @param param 查询参数
     * @return 策划阶段分布
     */
    List<Map<String, Object>> getPlanningStageDistribution(ProjectPlanningQueryParam param);

    /**
     * 获取策划状态分布统计
     * 
     * @param param 查询参数
     * @return 策划状态分布
     */
    List<Map<String, Object>> getPlanningStatusDistribution(ProjectPlanningQueryParam param);

    /**
     * 获取月度策划趋势
     * 
     * @param param 查询参数
     * @return 月度策划趋势
     */
    List<Map<String, Object>> getMonthlyPlanningTrend(ProjectPlanningQueryParam param);

    /**
     * 获取预算金额分布统计
     * 
     * @param param 查询参数
     * @return 预算金额分布
     */
    List<Map<String, Object>> getBudgetAmountDistribution(ProjectPlanningQueryParam param);

    /**
     * 获取完成度分布统计
     * 
     * @param param 查询参数
     * @return 完成度分布
     */
    List<Map<String, Object>> getCompletionRateDistribution(ProjectPlanningQueryParam param);

    /**
     * 获取负责人分布统计
     * 
     * @param param 查询参数
     * @return 负责人分布
     */
    List<Map<String, Object>> getResponsiblePersonDistribution(ProjectPlanningQueryParam param);

    /**
     * 模糊搜索项目策划
     * 
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目策划列表
     */
    List<ProjectPlanning> searchProjectPlannings(String keyword, Integer limit);

    /**
     * 获取今日到期的策划列表
     * 
     * @return 今日到期的策划列表
     */
    List<ProjectPlanning> getTodayExpiringPlannings();

    /**
     * 获取本周到期的策划列表
     * 
     * @return 本周到期的策划列表
     */
    List<ProjectPlanning> getThisWeekExpiringPlannings();

    /**
     * 导入项目策划
     * 
     * @param planningList 策划列表
     * @return 导入结果
     */
    Map<String, Object> importProjectPlannings(List<ProjectPlanning> planningList);

    /**
     * 导出项目策划
     * 
     * @param param 查询参数
     * @return 导出数据
     */
    List<ProjectPlanning> exportProjectPlannings(ProjectPlanningQueryParam param);

    /**
     * 自动更新过期策划状态
     * 
     * @return 更新数量
     */
    int autoUpdateExpiredPlannings();

    /**
     * 发送到期提醒
     * 
     * @param days 提前天数
     * @return 提醒数量
     */
    int sendExpiryReminders(Integer days);
}
