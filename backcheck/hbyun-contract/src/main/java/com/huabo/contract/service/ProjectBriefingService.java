package com.huabo.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.ProjectBriefing;
import com.huabo.contract.vo.ProjectBriefingQueryParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 项目交底表 服务类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface ProjectBriefingService extends IService<ProjectBriefing> {

    /**
     * 分页查询项目交底列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<ProjectBriefing> getProjectBriefingList(ProjectBriefingQueryParam param);

    /**
     * 保存项目交底
     *
     * @param projectBriefing 项目交底
     * @return 保存结果
     */
    boolean saveProjectBriefing(ProjectBriefing projectBriefing);

    /**
     * 根据ID获取项目交底详情
     *
     * @param id 主键ID
     * @return 项目交底详情
     */
    ProjectBriefing getProjectBriefingById(Long id);

    /**
     * 根据交底编号获取项目交底
     *
     * @param briefingNo 交底编号
     * @return 项目交底
     */
    ProjectBriefing getProjectBriefingByBriefingNo(String briefingNo);

    /**
     * 删除项目交底
     *
     * @param id 主键ID
     * @return 删除结果
     */
    boolean deleteProjectBriefing(Long id);

    /**
     * 批量删除项目交底
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    boolean batchDeleteProjectBriefing(List<Long> ids);

    /**
     * 生成交底编号
     *
     * @return 交底编号
     */
    String generateBriefingNo();

    /**
     * 检查交底编号是否存在
     *
     * @param briefingNo 交底编号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsBriefingNo(String briefingNo, Long excludeId);

    /**
     * 更新交底状态
     *
     * @param id 交底ID
     * @param briefingStatus 交底状态
     * @return 更新结果
     */
    boolean updateBriefingStatus(Long id, Integer briefingStatus);

    /**
     * 批量更新交底状态
     *
     * @param ids 交底ID列表
     * @param briefingStatus 交底状态
     * @return 更新结果
     */
    boolean batchUpdateBriefingStatus(List<Long> ids, Integer briefingStatus);

    /**
     * 更新完成度
     *
     * @param id 交底ID
     * @param completionRate 完成度
     * @return 更新结果
     */
    boolean updateCompletionRate(Long id, BigDecimal completionRate);

    /**
     * 批量更新完成度
     *
     * @param ids 交底ID列表
     * @param completionRate 完成度
     * @return 更新结果
     */
    boolean batchUpdateCompletionRate(List<Long> ids, BigDecimal completionRate);

    /**
     * 进行交底
     *
     * @param id 交底ID
     * @param brieferId 交底人ID
     * @param brieferName 交底人姓名
     * @param briefingLocation 交底地点
     * @param participants 参与人员
     * @return 交底结果
     */
    boolean conductBriefing(Long id, Long brieferId, String brieferName, String briefingLocation, String participants);

    /**
     * 接收交底
     *
     * @param id 交底ID
     * @param receiverId 接收人ID
     * @param receiverName 接收人姓名
     * @return 接收结果
     */
    boolean receiveBriefing(Long id, Long receiverId, String receiverName);

    /**
     * 确认交底
     *
     * @param id 交底ID
     * @param confirmerId 确认人ID
     * @param confirmerName 确认人姓名
     * @param confirmComments 确认意见
     * @return 确认结果
     */
    boolean confirmBriefing(Long id, Long confirmerId, String confirmerName, String confirmComments);

    /**
     * 完成交底
     *
     * @param id 交底ID
     * @param executionStatus 执行情况
     * @return 完成结果
     */
    boolean completeBriefing(Long id, String executionStatus);

    /**
     * 取消交底
     *
     * @param id 交底ID
     * @return 取消结果
     */
    boolean cancelBriefing(Long id);

    /**
     * 验收交底
     *
     * @param id 交底ID
     * @param acceptorId 验收人ID
     * @param acceptorName 验收人姓名
     * @param acceptanceResult 验收结果
     * @return 验收结果
     */
    boolean acceptBriefing(Long id, Long acceptorId, String acceptorName, String acceptanceResult);

    /**
     * 获取草稿状态的交底列表
     *
     * @return 草稿状态的交底列表
     */
    List<ProjectBriefing> getDraftBriefings();

    /**
     * 获取待交底的交底列表
     *
     * @return 待交底的交底列表
     */
    List<ProjectBriefing> getPendingBriefings();

    /**
     * 获取已交底的交底列表
     *
     * @return 已交底的交底列表
     */
    List<ProjectBriefing> getBriefedBriefings();

    /**
     * 获取已接收的交底列表
     *
     * @return 已接收的交底列表
     */
    List<ProjectBriefing> getReceivedBriefings();

    /**
     * 获取已确认的交底列表
     *
     * @return 已确认的交底列表
     */
    List<ProjectBriefing> getConfirmedBriefings();

    /**
     * 获取已完成的交底列表
     *
     * @return 已完成的交底列表
     */
    List<ProjectBriefing> getCompletedBriefings();

    /**
     * 获取已取消的交底列表
     *
     * @return 已取消的交底列表
     */
    List<ProjectBriefing> getCancelledBriefings();

    /**
     * 获取紧急交底列表
     *
     * @return 紧急交底列表
     */
    List<ProjectBriefing> getUrgentBriefings();

    /**
     * 获取重要交底列表
     *
     * @return 重要交底列表
     */
    List<ProjectBriefing> getImportantBriefings();

    /**
     * 获取高优先级交底列表
     *
     * @return 高优先级交底列表
     */
    List<ProjectBriefing> getHighPriorityBriefings();

    /**
     * 获取我交底的交底列表
     *
     * @param userId 用户ID
     * @return 我交底的交底列表
     */
    List<ProjectBriefing> getMyBriefings(Long userId);

    /**
     * 获取我接收的交底列表
     *
     * @param userId 用户ID
     * @return 我接收的交底列表
     */
    List<ProjectBriefing> getMyReceivedBriefings(Long userId);

    /**
     * 获取我确认的交底列表
     *
     * @param userId 用户ID
     * @return 我确认的交底列表
     */
    List<ProjectBriefing> getMyConfirmedBriefings(Long userId);

    /**
     * 获取我验收的交底列表
     *
     * @param userId 用户ID
     * @return 我验收的交底列表
     */
    List<ProjectBriefing> getMyAcceptedBriefings(Long userId);

    /**
     * 根据项目ID查询交底列表
     *
     * @param projectId 项目ID
     * @return 交底列表
     */
    List<ProjectBriefing> getProjectBriefingByProjectId(Long projectId);

    /**
     * 统计项目交底数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> getProjectBriefingStatistics(ProjectBriefingQueryParam param);

    /**
     * 获取交底类型分布统计
     *
     * @param param 查询参数
     * @return 交底类型分布
     */
    List<Map<String, Object>> getBriefingTypeDistribution(ProjectBriefingQueryParam param);

    /**
     * 获取交底状态分布统计
     *
     * @param param 查询参数
     * @return 交底状态分布
     */
    List<Map<String, Object>> getBriefingStatusDistribution(ProjectBriefingQueryParam param);

    /**
     * 获取月度交底趋势
     *
     * @param param 查询参数
     * @return 月度交底趋势
     */
    List<Map<String, Object>> getMonthlyBriefingTrend(ProjectBriefingQueryParam param);

    /**
     * 模糊搜索项目交底
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目交底列表
     */
    List<ProjectBriefing> searchProjectBriefings(String keyword, Integer limit);

    /**
     * 获取今日交底的交底列表
     *
     * @return 今日交底的交底列表
     */
    List<ProjectBriefing> getTodayBriefings();

    /**
     * 获取本周交底的交底列表
     *
     * @return 本周交底的交底列表
     */
    List<ProjectBriefing> getThisWeekBriefings();

    /**
     * 获取本月交底的交底列表
     *
     * @return 本月交底的交底列表
     */
    List<ProjectBriefing> getThisMonthBriefings();

    /**
     * 导入项目交底
     *
     * @param briefingList 交底列表
     * @return 导入结果
     */
    Map<String, Object> importProjectBriefings(List<ProjectBriefing> briefingList);

    /**
     * 导出项目交底
     *
     * @param param 查询参数
     * @return 交底列表
     */
    List<ProjectBriefing> exportProjectBriefings(ProjectBriefingQueryParam param);

    /**
     * 发送交底提醒通知
     *
     * @return 发送数量
     */
    int sendBriefingReminders();

    /**
     * 获取交底效果评估
     *
     * @param param 查询参数
     * @return 交底效果评估
     */
    Map<String, Object> getBriefingEffectiveness(ProjectBriefingQueryParam param);
}
