package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.contract.entity.Progress;
import com.huabo.contract.mapper.ProgressMapper;
import com.huabo.contract.service.ProgressService;
import com.huabo.contract.vo.ProgressQueryParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 进度管理服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Service
public class ProgressServiceImpl extends ServiceImpl<ProgressMapper, Progress> implements ProgressService {

    @Override
    public IPage<Progress> getProgressPage(ProgressQueryParam queryParam) {
        Page<Progress> page = new Page<>(queryParam.getCurrent(), queryParam.getSize());
        return baseMapper.selectProgressPage(page, queryParam);
    }

    @Override
    public List<Progress> getByProjectId(Long projectId) {
        return baseMapper.selectByProjectId(projectId);
    }

    @Override
    public List<Progress> getByProgressType(Integer progressType) {
        return baseMapper.selectByProgressType(progressType);
    }

    @Override
    public List<Progress> getByParentProgressId(Long parentProgressId) {
        return baseMapper.selectByParentProgressId(parentProgressId);
    }

    @Override
    public List<Progress> getByProgressStatus(Integer progressStatus) {
        return baseMapper.selectByProgressStatus(progressStatus);
    }

    @Override
    public List<Progress> getByPriority(Integer priority) {
        return baseMapper.selectByPriority(priority);
    }

    @Override
    public List<Progress> getByManagerId(Long managerId) {
        return baseMapper.selectByManagerId(managerId);
    }

    @Override
    public List<Progress> getByManagerName(String managerName) {
        return baseMapper.selectByManagerName(managerName);
    }

    @Override
    public List<Progress> getMilestones(Long projectId) {
        return baseMapper.selectMilestones(projectId);
    }

    @Override
    public List<Progress> getCriticalPath(Long projectId) {
        return baseMapper.selectCriticalPath(projectId);
    }

    @Override
    public List<Progress> getByCreateBy(Long createBy) {
        return baseMapper.selectByCreateBy(createBy);
    }

    @Override
    public BigDecimal calculateOverallProgress(Long projectId) {
        return baseMapper.calculateOverallProgress(projectId);
    }

    @Override
    public Integer countByProgressStatus(Integer progressStatus) {
        return baseMapper.countByProgressStatus(progressStatus);
    }

    @Override
    public Integer countByPriority(Integer priority) {
        return baseMapper.countByPriority(priority);
    }

    @Override
    public List<Progress> getDelayedProgress() {
        return baseMapper.selectDelayedProgress();
    }

    @Override
    public List<Progress> getEarlyCompletedProgress() {
        return baseMapper.selectEarlyCompletedProgress();
    }

    @Override
    public List<Progress> getHighPriorityProgress() {
        return baseMapper.selectHighPriorityProgress();
    }

    @Override
    public List<Progress> getUpcomingDeadlines(Integer days) {
        return baseMapper.selectUpcomingDeadlines(days);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateProgressStatus(List<Long> ids, Integer progressStatus, Long updateBy) {
        return baseMapper.batchUpdateProgressStatus(ids, progressStatus, updateBy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateActualProgress(List<Long> ids, BigDecimal actualProgress, Long updateBy) {
        return baseMapper.batchUpdateActualProgress(ids, actualProgress, updateBy);
    }

    @Override
    public List<Progress> getByDateRange(Date startDate, Date endDate) {
        return baseMapper.selectByDateRange(startDate, endDate);
    }

    @Override
    public List<Progress> getByProgressRange(BigDecimal minProgress, BigDecimal maxProgress) {
        return baseMapper.selectByProgressRange(minProgress, maxProgress);
    }

    @Override
    public List<Progress> getProgressStatistics() {
        return baseMapper.selectProgressStatistics();
    }

    @Override
    public List<Progress> searchByKeyword(String keyword, Integer limit) {
        return baseMapper.searchByKeyword(keyword, limit);
    }

    @Override
    public List<Progress> getProgressTree(Long projectId) {
        return baseMapper.selectProgressTree(projectId);
    }

    @Override
    public List<Progress> getRootProgress(Long projectId) {
        return baseMapper.selectRootProgress(projectId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateProgress(Long id, BigDecimal actualProgress, String completionDescription, Long updateBy) {
        Progress progress = getById(id);
        if (progress == null) {
            return false;
        }
        
        progress.setActualProgress(actualProgress);
        progress.setProgressDescription(completionDescription);
        progress.setUpdateTime(new Date());

        // 如果进度达到100%，自动设置为已完成
        if (actualProgress != null && actualProgress.compareTo(new BigDecimal("100")) >= 0) {
            progress.setProgressStatus(3); // 已完成
            if (progress.getActualEndDate() == null) {
                progress.setActualEndDate(new Date());
            }
        }
        
        return updateById(progress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean startProgress(Long id, Date actualStartTime, Long updateBy) {
        Progress progress = getById(id);
        if (progress == null) {
            return false;
        }
        
        progress.setProgressStatus(2); // 进行中
        progress.setActualStartDate(actualStartTime != null ? actualStartTime : new Date());
        progress.setUpdateTime(new Date());
        
        return updateById(progress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean completeProgress(Long id, Date actualEndTime, String completionDescription, Long updateBy) {
        Progress progress = getById(id);
        if (progress == null) {
            return false;
        }
        
        progress.setProgressStatus(3); // 已完成
        progress.setActualEndDate(actualEndTime != null ? actualEndTime : new Date());
        progress.setActualProgress(new BigDecimal("100"));
        progress.setProgressDescription(completionDescription);
        progress.setUpdateTime(new Date());

        // 计算延期天数
        if (progress.getActualStartDate() != null && progress.getPlannedEndDate() != null) {
            long diffInMillies = progress.getActualEndDate().getTime() - progress.getPlannedEndDate().getTime();
            int delayDays = (int) (diffInMillies / (24 * 60 * 60 * 1000));
            progress.setDelayDays(Math.max(0, delayDays));
        }
        
        return updateById(progress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean pauseProgress(Long id, String reason, Long updateBy) {
        Progress progress = getById(id);
        if (progress == null) {
            return false;
        }
        
        progress.setProgressStatus(4); // 延期
        progress.setDelayReason(reason);
        progress.setUpdateTime(new Date());
        
        return updateById(progress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean resumeProgress(Long id, Long updateBy) {
        Progress progress = getById(id);
        if (progress == null) {
            return false;
        }
        
        progress.setProgressStatus(2); // 进行中
        progress.setUpdateTime(new Date());
        
        return updateById(progress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelProgress(Long id, String reason, Long updateBy) {
        Progress progress = getById(id);
        if (progress == null) {
            return false;
        }
        
        progress.setProgressStatus(4); // 延期
        progress.setDelayReason(reason);
        progress.setUpdateTime(new Date());
        
        return updateById(progress);
    }

    @Override
    public String generateProgressNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        long timestamp = System.currentTimeMillis() % 10000;
        return "PG" + dateStr + String.format("%04d", timestamp);
    }

    @Override
    public Boolean validateProgressInfo(Progress progress) {
        if (progress == null) {
            System.out.println("进度记录验证失败：progress对象为null");
            return false;
        }

        // 打印调试信息
        System.out.println("验证进度记录：projectId=" + progress.getProjectId() +
                ", milestoneName=" + progress.getMilestoneName() +
                ", milestoneType=" + progress.getMilestoneType() +
                ", plannedProgress=" + progress.getPlannedProgress() +
                ", actualProgress=" + progress.getActualProgress() +
                ", progressStatus=" + progress.getProgressStatus());

        // 验证必填字段
        if (progress.getProjectId() == null) {
            System.out.println("进度记录验证失败：projectId为空");
            return false;
        }

        if (progress.getMilestoneName() == null || progress.getMilestoneName().trim().isEmpty()) {
            System.out.println("进度记录验证失败：milestoneName为空");
            return false;
        }

        if (progress.getMilestoneType() == null) {
            System.out.println("进度记录验证失败：milestoneType为空");
            return false;
        }

        // 验证进度百分比 - 使用更宽松的验证
        if (progress.getPlannedProgress() != null) {
            try {
                BigDecimal planned = progress.getPlannedProgress();
                if (planned.compareTo(BigDecimal.ZERO) < 0 || planned.compareTo(new BigDecimal("100")) > 0) {
                    System.out.println("进度记录验证失败：plannedProgress超出范围 " + planned);
                    return false;
                }
            } catch (Exception e) {
                System.out.println("进度记录验证失败：plannedProgress格式错误 " + e.getMessage());
                // 如果进度值有问题，跳过验证
            }
        }

        if (progress.getActualProgress() != null) {
            try {
                BigDecimal actual = progress.getActualProgress();
                if (actual.compareTo(BigDecimal.ZERO) < 0 || actual.compareTo(new BigDecimal("100")) > 0) {
                    System.out.println("进度记录验证失败：actualProgress超出范围 " + actual);
                    return false;
                }
            } catch (Exception e) {
                System.out.println("进度记录验证失败：actualProgress格式错误 " + e.getMessage());
                // 如果进度值有问题，跳过验证
            }
        }

        System.out.println("进度记录验证通过");
        return true;
    }

    @Override
    public List<String> getProjectProgressReport(Long projectId) {
        List<String> report = new ArrayList<>();
        
        // 查询项目进度记录
        List<Progress> progressList = getByProjectId(projectId);
        
        if (progressList.isEmpty()) {
            report.add("该项目暂无进度记录");
            return report;
        }
        
        // 统计进度情况
        int totalCount = progressList.size();
        int notStartedCount = 0;
        int inProgressCount = 0;
        int completedCount = 0;
        int pausedCount = 0;
        int cancelledCount = 0;
        int delayedCount = 0;
        int milestoneCount = 0;
        int criticalPathCount = 0;
        
        BigDecimal totalPlannedProgress = BigDecimal.ZERO;
        BigDecimal totalActualProgress = BigDecimal.ZERO;
        
        for (Progress progress : progressList) {
            switch (progress.getProgressStatus()) {
                case 1: notStartedCount++; break;
                case 2: inProgressCount++; break;
                case 3: completedCount++; break;
                case 4: pausedCount++; break;
                case 5: cancelledCount++; break;
            }
            
            if (progress.isDelayed()) {
                delayedCount++;
            }
            
            // 里程碑类型为1或3的算作里程碑
            if (progress.getMilestoneType() != null && (progress.getMilestoneType() == 1 || progress.getMilestoneType() == 3)) {
                milestoneCount++;
            }
            
            if (progress.getPlannedProgress() != null) {
                totalPlannedProgress = totalPlannedProgress.add(progress.getPlannedProgress());
            }
            
            if (progress.getActualProgress() != null) {
                totalActualProgress = totalActualProgress.add(progress.getActualProgress());
            }
        }
        
        // 计算总体进度
        BigDecimal overallProgress = calculateOverallProgress(projectId);
        
        // 生成进度报告
        report.add("=== 项目进度报告 ===");
        report.add("进度记录总数：" + totalCount + " 项");
        report.add("未开始：" + notStartedCount + " 项");
        report.add("进行中：" + inProgressCount + " 项");
        report.add("已完成：" + completedCount + " 项");
        report.add("已暂停：" + pausedCount + " 项");
        report.add("已取消：" + cancelledCount + " 项");
        report.add("延期项目：" + delayedCount + " 项");
        report.add("里程碑：" + milestoneCount + " 项");
        report.add("关键路径：" + criticalPathCount + " 项");
        
        if (overallProgress != null) {
            report.add("项目总体进度：" + overallProgress + "%");
        }
        
        // 完成率
        if (totalCount > 0) {
            BigDecimal completionRate = new BigDecimal(completedCount).divide(new BigDecimal(totalCount), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
            report.add("完成率：" + completionRate + "%");
        }
        
        return report;
    }
}
