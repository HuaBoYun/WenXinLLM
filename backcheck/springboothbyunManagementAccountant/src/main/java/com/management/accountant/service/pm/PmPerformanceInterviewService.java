package com.management.accountant.service.pm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.pm.PmPerformanceInterview;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 绩效面谈管理服务接口
 *
 * @author AI Assistant
 * @since 2024-01-01
 */
public interface PmPerformanceInterviewService extends IService<PmPerformanceInterview> {

    /**
     * 分页查询绩效面谈
     *
     * @param page 分页参数
     * @param queryParams 查询参数
     * @return 分页结果
     */
    IPage<PmPerformanceInterview> queryInterviewPage(Page<PmPerformanceInterview> page, Map<String, Object> queryParams);

    /**
     * 创建绩效面谈
     *
     * @param interview 面谈信息
     * @return 创建结果
     */
    boolean createInterview(PmPerformanceInterview interview);

    /**
     * 更新绩效面谈
     *
     * @param interview 面谈信息
     * @return 更新结果
     */
    boolean updateInterview(PmPerformanceInterview interview);

    /**
     * 删除绩效面谈
     *
     * @param interviewId 面谈ID
     * @return 删除结果
     */
    boolean deleteInterview(Long interviewId);

    /**
     * 批量删除绩效面谈
     *
     * @param interviewIds 面谈ID列表
     * @return 删除结果
     */
    boolean batchDeleteInterviews(List<Long> interviewIds);

    /**
     * 根据ID查询面谈详情
     *
     * @param interviewId 面谈ID
     * @return 面谈详情
     */
    PmPerformanceInterview getInterviewById(Long interviewId);

    /**
     * 查询面谈详情（包含关联信息）
     *
     * @param interviewId 面谈ID
     * @return 面谈详情
     */
    Map<String, Object> getInterviewDetail(Long interviewId);

    /**
     * 安排面谈
     *
     * @param interviewId 面谈ID
     * @param scheduleParams 安排参数
     * @return 安排结果
     */
    boolean scheduleInterview(Long interviewId, Map<String, Object> scheduleParams);

    /**
     * 开始面谈
     *
     * @param interviewId 面谈ID
     * @param startParams 开始参数
     * @return 开始结果
     */
    boolean startInterview(Long interviewId, Map<String, Object> startParams);

    /**
     * 完成面谈
     *
     * @param interviewId 面谈ID
     * @param completeParams 完成参数
     * @return 完成结果
     */
    boolean completeInterview(Long interviewId, Map<String, Object> completeParams);

    /**
     * 取消面谈
     *
     * @param interviewId 面谈ID
     * @param cancelParams 取消参数
     * @return 取消结果
     */
    boolean cancelInterview(Long interviewId, Map<String, Object> cancelParams);

    /**
     * 延期面谈
     *
     * @param interviewId 面谈ID
     * @param postponeParams 延期参数
     * @return 延期结果
     */
    boolean postponeInterview(Long interviewId, Map<String, Object> postponeParams);

    /**
     * 批量更新面谈状态
     *
     * @param interviewIds 面谈ID列表
     * @param status 状态
     * @param updateParams 更新参数
     * @return 更新结果
     */
    boolean batchUpdateStatus(List<Long> interviewIds, String status, Map<String, Object> updateParams);

    /**
     * 保存面谈记录
     *
     * @param interviewId 面谈ID
     * @param recordParams 记录参数
     * @return 保存结果
     */
    boolean saveInterviewRecord(Long interviewId, Map<String, Object> recordParams);

    /**
     * 提交面谈反馈
     *
     * @param interviewId 面谈ID
     * @param feedbackParams 反馈参数
     * @return 提交结果
     */
    boolean submitFeedback(Long interviewId, Map<String, Object> feedbackParams);

    /**
     * 创建跟进计划
     *
     * @param interviewId 面谈ID
     * @param followUpParams 跟进参数
     * @return 创建结果
     */
    boolean createFollowUpPlan(Long interviewId, Map<String, Object> followUpParams);

    /**
     * 更新跟进状态
     *
     * @param interviewId 面谈ID
     * @param followUpStatus 跟进状态
     * @param updateParams 更新参数
     * @return 更新结果
     */
    boolean updateFollowUpStatus(Long interviewId, String followUpStatus, Map<String, Object> updateParams);

    /**
     * 批量更新跟进状态
     *
     * @param interviewIds 面谈ID列表
     * @param followUpStatus 跟进状态
     * @param updateParams 更新参数
     * @return 更新结果
     */
    boolean batchUpdateFollowUpStatus(List<Long> interviewIds, String followUpStatus, Map<String, Object> updateParams);

    /**
     * 根据被面谈人查询面谈列表
     *
     * @param intervieweeId 被面谈人ID
     * @param status 状态
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> getInterviewsByInterviewee(Long intervieweeId, String status, Integer limit);

    /**
     * 根据面谈官查询面谈列表
     *
     * @param interviewerId 面谈官ID
     * @param status 状态
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> getInterviewsByInterviewer(Long interviewerId, String status, Integer limit);

    /**
     * 根据部门查询面谈列表
     *
     * @param deptId 部门ID
     * @param status 状态
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> getInterviewsByDept(Long deptId, String status, Integer limit);

    /**
     * 查询待跟进的面谈
     *
     * @param deadline 截止时间
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> getPendingFollowUpInterviews(LocalDateTime deadline, Integer limit);

    /**
     * 查询即将到期的面谈
     *
     * @param deadline 截止时间
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> getUpcomingInterviews(LocalDateTime deadline, Integer limit);

    /**
     * 查询逾期的面谈
     *
     * @param currentTime 当前时间
     * @param limit 限制数量
     * @return 面谈列表
     */
    List<PmPerformanceInterview> getOverdueInterviews(LocalDateTime currentTime, Integer limit);

    /**
     * 统计面谈数据
     *
     * @param statisticsParams 统计参数
     * @return 统计结果
     */
    Map<String, Object> getInterviewStatistics(Map<String, Object> statisticsParams);

    /**
     * 统计面谈状态分布
     *
     * @param year 年度
     * @param deptId 部门ID
     * @return 状态分布
     */
    List<Map<String, Object>> getInterviewStatusDistribution(Integer year, Long deptId);

    /**
     * 统计面谈类型分布
     *
     * @param year 年度
     * @param deptId 部门ID
     * @return 类型分布
     */
    List<Map<String, Object>> getInterviewTypeDistribution(Integer year, Long deptId);

    /**
     * 统计面谈完成趋势
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param deptId 部门ID
     * @return 完成趋势
     */
    List<Map<String, Object>> getInterviewCompletionTrend(LocalDateTime startTime, LocalDateTime endTime, Long deptId);

    /**
     * 统计面谈满意度分布
     *
     * @param year 年度
     * @param deptId 部门ID
     * @return 满意度分布
     */
    List<Map<String, Object>> getSatisfactionDistribution(Integer year, Long deptId);

    /**
     * 查询面谈排行榜
     *
     * @param year 年度
     * @param rankType 排行类型
     * @param limit 限制数量
     * @return 排行榜
     */
    List<Map<String, Object>> getInterviewRanking(Integer year, String rankType, Integer limit);

    /**
     * 检查面谈时间冲突
     *
     * @param interviewerId 面谈官ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param excludeId 排除的面谈ID
     * @return 是否冲突
     */
    boolean checkTimeConflict(Long interviewerId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId);

    /**
     * 自动分配面谈官
     *
     * @param deptId 部门ID
     * @param interviewType 面谈类型
     * @param limit 限制数量
     * @return 面谈官列表
     */
    List<Map<String, Object>> getAvailableInterviewers(Long deptId, String interviewType, Integer limit);

    /**
     * 查询面谈提醒列表
     *
     * @param reminderTime 提醒时间
     * @param limit 限制数量
     * @return 提醒列表
     */
    List<PmPerformanceInterview> getInterviewReminders(LocalDateTime reminderTime, Integer limit);

    /**
     * 生成面谈报告
     *
     * @param interviewId 面谈ID
     * @param reportParams 报告参数
     * @return 报告结果
     */
    Map<String, Object> generateInterviewReport(Long interviewId, Map<String, Object> reportParams);

    /**
     * 导出面谈数据
     *
     * @param exportParams 导出参数
     * @return 导出数据
     */
    List<Map<String, Object>> exportInterviewData(Map<String, Object> exportParams);

    /**
     * 导入面谈数据
     *
     * @param importData 导入数据
     * @param importParams 导入参数
     * @return 导入结果
     */
    Map<String, Object> importInterviewData(List<Map<String, Object>> importData, Map<String, Object> importParams);

    /**
     * 复制面谈
     *
     * @param interviewId 面谈ID
     * @param copyParams 复制参数
     * @return 复制结果
     */
    PmPerformanceInterview copyInterview(Long interviewId, Map<String, Object> copyParams);

    /**
     * 批量创建面谈
     *
     * @param interviews 面谈列表
     * @param batchParams 批量参数
     * @return 创建结果
     */
    Map<String, Object> batchCreateInterviews(List<PmPerformanceInterview> interviews, Map<String, Object> batchParams);

    /**
     * 智能推荐面谈时间
     *
     * @param interviewerId 面谈官ID
     * @param intervieweeId 被面谈人ID
     * @param duration 面谈时长（分钟）
     * @param preferredDates 偏好日期
     * @return 推荐时间
     */
    List<Map<String, Object>> recommendInterviewTimes(Long interviewerId, Long intervieweeId, Integer duration, List<String> preferredDates);

    /**
     * 发送面谈通知
     *
     * @param interviewId 面谈ID
     * @param notificationParams 通知参数
     * @return 发送结果
     */
    boolean sendInterviewNotification(Long interviewId, Map<String, Object> notificationParams);

    /**
     * 批量发送面谈通知
     *
     * @param interviewIds 面谈ID列表
     * @param notificationParams 通知参数
     * @return 发送结果
     */
    Map<String, Object> batchSendNotifications(List<Long> interviewIds, Map<String, Object> notificationParams);
}
