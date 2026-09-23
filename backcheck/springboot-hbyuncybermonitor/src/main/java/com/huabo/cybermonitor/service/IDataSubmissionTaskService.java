package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.DataSubmissionTask;
import com.huabo.cybermonitor.vo.DataSubmissionTaskQueryVo;

import java.util.List;
import java.util.Map;

/**
 * 数据报送任务Service接口
 *
 * @author huabo
 * @since 2024-12-12
 */
public interface IDataSubmissionTaskService extends IService<DataSubmissionTask> {

    /**
     * 分页查询数据报送任务列表
     *
     * @param page 分页参数
     * @param queryVo 查询条件
     * @return 分页结果
     */
    IPage<DataSubmissionTask> selectDataSubmissionTaskPage(Page<DataSubmissionTask> page, DataSubmissionTaskQueryVo queryVo);

    /**
     * 查询数据报送任务列表
     *
     * @param queryVo 查询条件
     * @return 数据报送任务列表
     */
    List<DataSubmissionTask> selectDataSubmissionTaskList(DataSubmissionTaskQueryVo queryVo);

    /**
     * 新增数据报送任务
     *
     * @param dataSubmissionTask 数据报送任务
     * @return 是否成功
     */
    boolean addDataSubmissionTask(DataSubmissionTask dataSubmissionTask);

    /**
     * 修改数据报送任务
     *
     * @param dataSubmissionTask 数据报送任务
     * @return 是否成功
     */
    boolean updateDataSubmissionTask(DataSubmissionTask dataSubmissionTask);

    /**
     * 删除数据报送任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean deleteDataSubmissionTask(String taskId);

    /**
     * 批量删除数据报送任务
     *
     * @param taskIds 任务ID列表
     * @return 是否成功
     */
    boolean batchDeleteDataSubmissionTasks(List<String> taskIds);

    /**
     * 根据企业ID查询报送任务统计
     *
     * @param enterpriseId 企业ID
     * @return 统计结果
     */
    Map<String, Object> getTaskStatisticsByEnterpriseId(String enterpriseId);

    /**
     * 查询即将到期的报送任务
     *
     * @param enterpriseId 企业ID
     * @param days 天数
     * @return 即将到期的任务列表
     */
    List<DataSubmissionTask> getUpcomingTasks(String enterpriseId, Integer days);

    /**
     * 查询逾期的报送任务
     *
     * @param enterpriseId 企业ID
     * @return 逾期任务列表
     */
    List<DataSubmissionTask> getOverdueTasks(String enterpriseId);

    /**
     * 根据任务状态统计数量
     *
     * @param enterpriseId 企业ID
     * @return 状态统计
     */
    List<Map<String, Object>> getTaskCountByStatus(String enterpriseId);

    /**
     * 根据优先级统计数量
     *
     * @param enterpriseId 企业ID
     * @return 优先级统计
     */
    List<Map<String, Object>> getTaskCountByPriority(String enterpriseId);

    /**
     * 根据报送类型统计数量
     *
     * @param enterpriseId 企业ID
     * @return 报送类型统计
     */
    List<Map<String, Object>> getTaskCountBySubmissionType(String enterpriseId);

    /**
     * 查询任务完成率趋势
     *
     * @param enterpriseId 企业ID
     * @param months 月份数
     * @return 完成率趋势
     */
    List<Map<String, Object>> getTaskCompletionTrend(String enterpriseId, Integer months);

    /**
     * 查询任务质量评分分布
     *
     * @param enterpriseId 企业ID
     * @return 质量评分分布
     */
    List<Map<String, Object>> getQualityScoreDistribution(String enterpriseId);

    /**
     * 批量更新任务状态
     *
     * @param taskIds 任务ID列表
     * @param status 新状态
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean batchUpdateTaskStatus(List<String> taskIds, String status, String updateBy);

    /**
     * 启动任务
     *
     * @param taskId 任务ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean startTask(String taskId, String updateBy);

    /**
     * 完成任务
     *
     * @param taskId 任务ID
     * @param completionDescription 完成情况说明
     * @param qualityScore 质量评分
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean completeTask(String taskId, String completionDescription, String qualityScore, String updateBy);

    /**
     * 暂停任务
     *
     * @param taskId 任务ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean pauseTask(String taskId, String updateBy);

    /**
     * 取消任务
     *
     * @param taskId 任务ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean cancelTask(String taskId, String updateBy);

    /**
     * 重启任务
     *
     * @param taskId 任务ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean restartTask(String taskId, String updateBy);

    /**
     * 提交反馈
     *
     * @param taskId 任务ID
     * @param feedbackInfo 反馈信息
     * @param feedbackStatus 反馈状态
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean submitFeedback(String taskId, String feedbackInfo, String feedbackStatus, String updateBy);

    /**
     * 处理反馈
     *
     * @param taskId 任务ID
     * @param rectificationPlan 整改计划
     * @param updateBy 更新人
     * @return 是否成功
     */
    boolean processFeedback(String taskId, String rectificationPlan, String updateBy);

    /**
     * 查询任务执行进度统计
     *
     * @param enterpriseId 企业ID
     * @return 进度统计
     */
    Map<String, Object> getTaskProgressStatistics(String enterpriseId);

    /**
     * 查询部门任务分布
     *
     * @param enterpriseId 企业ID
     * @return 部门任务分布
     */
    List<Map<String, Object>> getTaskDistributionByDepartment(String enterpriseId);

    /**
     * 查询负责人任务分布
     *
     * @param enterpriseId 企业ID
     * @return 负责人任务分布
     */
    List<Map<String, Object>> getTaskDistributionByPerson(String enterpriseId);

    /**
     * 查询监管部门任务分布
     *
     * @param enterpriseId 企业ID
     * @return 监管部门任务分布
     */
    List<Map<String, Object>> getTaskDistributionByRegulator(String enterpriseId);

    /**
     * 查询任务反馈处理统计
     *
     * @param enterpriseId 企业ID
     * @return 反馈处理统计
     */
    Map<String, Object> getFeedbackProcessingStatistics(String enterpriseId);

    /**
     * 查询任务时效性分析
     *
     * @param enterpriseId 企业ID
     * @return 时效性分析
     */
    Map<String, Object> getTaskTimelinessAnalysis(String enterpriseId);

    /**
     * 导出任务列表
     *
     * @param queryVo 查询条件
     * @return 导出数据
     */
    List<DataSubmissionTask> exportTaskList(DataSubmissionTaskQueryVo queryVo);

    /**
     * 生成任务报告
     *
     * @param enterpriseId 企业ID
     * @param reportType 报告类型
     * @return 报告数据
     */
    Map<String, Object> generateTaskReport(String enterpriseId, String reportType);
}
