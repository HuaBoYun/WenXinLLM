package com.huabo.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.contract.entity.Progress;
import com.huabo.contract.vo.ProgressQueryParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 进度管理服务接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface ProgressService extends IService<Progress> {

    /**
     * 分页查询进度记录
     */
    IPage<Progress> getProgressPage(ProgressQueryParam queryParam);

    /**
     * 根据项目ID查询进度记录
     */
    List<Progress> getByProjectId(Long projectId);

    /**
     * 根据进度类型查询进度记录
     */
    List<Progress> getByProgressType(Integer progressType);

    /**
     * 根据父级进度ID查询子进度记录
     */
    List<Progress> getByParentProgressId(Long parentProgressId);

    /**
     * 根据进度状态查询进度记录
     */
    List<Progress> getByProgressStatus(Integer progressStatus);

    /**
     * 根据优先级查询进度记录
     */
    List<Progress> getByPriority(Integer priority);

    /**
     * 根据负责人ID查询进度记录
     */
    List<Progress> getByManagerId(Long managerId);

    /**
     * 根据负责人姓名查询进度记录
     */
    List<Progress> getByManagerName(String managerName);

    /**
     * 查询里程碑进度记录
     */
    List<Progress> getMilestones(Long projectId);

    /**
     * 查询关键路径进度记录
     */
    List<Progress> getCriticalPath(Long projectId);

    /**
     * 根据创建人查询进度记录
     */
    List<Progress> getByCreateBy(Long createBy);

    /**
     * 根据项目ID统计总体进度
     */
    BigDecimal calculateOverallProgress(Long projectId);

    /**
     * 根据进度状态统计数量
     */
    Integer countByProgressStatus(Integer progressStatus);

    /**
     * 根据优先级统计数量
     */
    Integer countByPriority(Integer priority);

    /**
     * 查询延期的进度记录
     */
    List<Progress> getDelayedProgress();

    /**
     * 查询提前完成的进度记录
     */
    List<Progress> getEarlyCompletedProgress();

    /**
     * 查询高优先级进度记录
     */
    List<Progress> getHighPriorityProgress();

    /**
     * 查询即将到期的进度记录
     */
    List<Progress> getUpcomingDeadlines(Integer days);

    /**
     * 批量更新进度状态
     */
    Integer batchUpdateProgressStatus(List<Long> ids, Integer progressStatus, Long updateBy);

    /**
     * 批量更新实际进度
     */
    Integer batchUpdateActualProgress(List<Long> ids, BigDecimal actualProgress, Long updateBy);

    /**
     * 根据日期范围查询进度记录
     */
    List<Progress> getByDateRange(Date startDate, Date endDate);

    /**
     * 根据进度范围查询进度记录
     */
    List<Progress> getByProgressRange(BigDecimal minProgress, BigDecimal maxProgress);

    /**
     * 查询进度统计信息
     */
    List<Progress> getProgressStatistics();

    /**
     * 根据关键词搜索进度记录
     */
    List<Progress> searchByKeyword(String keyword, Integer limit);

    /**
     * 查询项目进度树形结构
     */
    List<Progress> getProgressTree(Long projectId);

    /**
     * 查询根级进度记录
     */
    List<Progress> getRootProgress(Long projectId);

    /**
     * 更新进度
     */
    Boolean updateProgress(Long id, BigDecimal actualProgress, String completionDescription, Long updateBy);

    /**
     * 开始进度
     */
    Boolean startProgress(Long id, Date actualStartTime, Long updateBy);

    /**
     * 完成进度
     */
    Boolean completeProgress(Long id, Date actualEndTime, String completionDescription, Long updateBy);

    /**
     * 暂停进度
     */
    Boolean pauseProgress(Long id, String reason, Long updateBy);

    /**
     * 恢复进度
     */
    Boolean resumeProgress(Long id, Long updateBy);

    /**
     * 取消进度
     */
    Boolean cancelProgress(Long id, String reason, Long updateBy);

    /**
     * 生成进度编号
     */
    String generateProgressNo();

    /**
     * 验证进度信息
     */
    Boolean validateProgressInfo(Progress progress);

    /**
     * 获取项目进度报告
     */
    List<String> getProjectProgressReport(Long projectId);
}
