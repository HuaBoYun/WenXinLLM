package com.management.accountant.service.pm.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.pm.PmPerformanceInterview;
import com.management.accountant.mapper.pm.PmPerformanceInterviewMapper;
import com.management.accountant.service.pm.PmPerformanceInterviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 绩效面谈管理服务实现类
 *
 * @author AI Assistant
 * @since 2024-01-01
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PmPerformanceInterviewServiceImpl extends ServiceImpl<PmPerformanceInterviewMapper, PmPerformanceInterview> implements PmPerformanceInterviewService {

    @Autowired
    private PmPerformanceInterviewMapper interviewMapper;

    @Override
    public IPage<PmPerformanceInterview> queryInterviewPage(Page<PmPerformanceInterview> page, Map<String, Object> queryParams) {
        try {
            String interviewCode = (String) queryParams.get("interviewCode");
            String interviewTitle = (String) queryParams.get("interviewTitle");
            String interviewType = (String) queryParams.get("interviewType");
            String interviewStatus = (String) queryParams.get("interviewStatus");
            Long intervieweeId = queryParams.get("intervieweeId") != null ? Long.valueOf(queryParams.get("intervieweeId").toString()) : null;
            String intervieweeName = (String) queryParams.get("intervieweeName");
            Long interviewerId = queryParams.get("interviewerId") != null ? Long.valueOf(queryParams.get("interviewerId").toString()) : null;
            String interviewerName = (String) queryParams.get("interviewerName");
            Integer interviewYear = queryParams.get("interviewYear") != null ? Integer.valueOf(queryParams.get("interviewYear").toString()) : null;
            Integer interviewQuarter = queryParams.get("interviewQuarter") != null ? Integer.valueOf(queryParams.get("interviewQuarter").toString()) : null;
            Integer interviewMonth = queryParams.get("interviewMonth") != null ? Integer.valueOf(queryParams.get("interviewMonth").toString()) : null;
            LocalDateTime startTime = (LocalDateTime) queryParams.get("startTime");
            LocalDateTime endTime = (LocalDateTime) queryParams.get("endTime");
            String priorityLevel = (String) queryParams.get("priorityLevel");
            Integer needFollowUp = queryParams.get("needFollowUp") != null ? Integer.valueOf(queryParams.get("needFollowUp").toString()) : null;
            String followUpStatus = (String) queryParams.get("followUpStatus");

            return interviewMapper.selectInterviewPage(page, interviewCode, interviewTitle, interviewType, interviewStatus,
                    intervieweeId, intervieweeName, interviewerId, interviewerName, interviewYear, interviewQuarter,
                    interviewMonth, startTime, endTime, priorityLevel, needFollowUp, followUpStatus);
        } catch (Exception e) {
            log.error("查询绩效面谈分页数据失败", e);
            throw new RuntimeException("查询绩效面谈分页数据失败: " + e.getMessage());
        }
    }

    @Override
    public boolean createInterview(PmPerformanceInterview interview) {
        try {
            // 生成面谈编码
            if (!StringUtils.hasText(interview.getInterviewCode())) {
                interview.setInterviewCode(generateInterviewCode());
            }

            // 设置默认状态
            if (!StringUtils.hasText(interview.getInterviewStatus())) {
                interview.setInterviewStatus("PLANNED");
            }

            // 检查时间冲突
            if (interview.getInterviewerId() != null && interview.getPlannedStartTime() != null && interview.getPlannedEndTime() != null) {
                if (checkTimeConflict(interview.getInterviewerId(), interview.getPlannedStartTime(), interview.getPlannedEndTime(), null)) {
                    throw new RuntimeException("面谈时间与其他面谈冲突，请重新选择时间");
                }
            }

            return save(interview);
        } catch (Exception e) {
            log.error("创建绩效面谈失败", e);
            throw new RuntimeException("创建绩效面谈失败: " + e.getMessage());
        }
    }

    @Override
    public boolean updateInterview(PmPerformanceInterview interview) {
        try {
            // 检查时间冲突
            if (interview.getInterviewerId() != null && interview.getPlannedStartTime() != null && interview.getPlannedEndTime() != null) {
                if (checkTimeConflict(interview.getInterviewerId(), interview.getPlannedStartTime(), interview.getPlannedEndTime(), interview.getInterviewId())) {
                    throw new RuntimeException("面谈时间与其他面谈冲突，请重新选择时间");
                }
            }

            return updateById(interview);
        } catch (Exception e) {
            log.error("更新绩效面谈失败", e);
            throw new RuntimeException("更新绩效面谈失败: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteInterview(Long interviewId) {
        try {
            PmPerformanceInterview interview = getById(interviewId);
            if (interview == null) {
                throw new RuntimeException("面谈不存在");
            }

            // 检查是否可以删除
            if ("ONGOING".equals(interview.getInterviewStatus()) || "COMPLETED".equals(interview.getInterviewStatus())) {
                throw new RuntimeException("进行中或已完成的面谈不能删除");
            }

            return removeById(interviewId);
        } catch (Exception e) {
            log.error("删除绩效面谈失败", e);
            throw new RuntimeException("删除绩效面谈失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchDeleteInterviews(List<Long> interviewIds) {
        try {
            // 检查每个面谈是否可以删除
            for (Long interviewId : interviewIds) {
                PmPerformanceInterview interview = getById(interviewId);
                if (interview != null && ("ONGOING".equals(interview.getInterviewStatus()) || "COMPLETED".equals(interview.getInterviewStatus()))) {
                    throw new RuntimeException("存在进行中或已完成的面谈，不能删除");
                }
            }

            return removeByIds(interviewIds);
        } catch (Exception e) {
            log.error("批量删除绩效面谈失败", e);
            throw new RuntimeException("批量删除绩效面谈失败: " + e.getMessage());
        }
    }

    @Override
    public PmPerformanceInterview getInterviewById(Long interviewId) {
        try {
            return getById(interviewId);
        } catch (Exception e) {
            log.error("查询绩效面谈详情失败", e);
            throw new RuntimeException("查询绩效面谈详情失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getInterviewDetail(Long interviewId) {
        try {
            return interviewMapper.selectInterviewDetail(interviewId);
        } catch (Exception e) {
            log.error("查询绩效面谈详情失败", e);
            throw new RuntimeException("查询绩效面谈详情失败: " + e.getMessage());
        }
    }

    @Override
    public boolean scheduleInterview(Long interviewId, Map<String, Object> scheduleParams) {
        try {
            PmPerformanceInterview interview = getById(interviewId);
            if (interview == null) {
                throw new RuntimeException("面谈不存在");
            }

            if (!"PLANNED".equals(interview.getInterviewStatus())) {
                throw new RuntimeException("只有已计划状态的面谈才能安排");
            }

            // 更新面谈信息
            LocalDateTime plannedStartTime = (LocalDateTime) scheduleParams.get("plannedStartTime");
            LocalDateTime plannedEndTime = (LocalDateTime) scheduleParams.get("plannedEndTime");
            String interviewLocation = (String) scheduleParams.get("interviewLocation");
            String interviewMethod = (String) scheduleParams.get("interviewMethod");

            // 检查时间冲突
            if (plannedStartTime != null && plannedEndTime != null) {
                if (checkTimeConflict(interview.getInterviewerId(), plannedStartTime, plannedEndTime, interviewId)) {
                    throw new RuntimeException("面谈时间与其他面谈冲突，请重新选择时间");
                }
            }

            interview.setPlannedStartTime(plannedStartTime);
            interview.setPlannedEndTime(plannedEndTime);
            interview.setInterviewLocation(interviewLocation);
            interview.setInterviewMethod(interviewMethod);
            interview.setInterviewStatus("SCHEDULED");

            return updateById(interview);
        } catch (Exception e) {
            log.error("安排绩效面谈失败", e);
            throw new RuntimeException("安排绩效面谈失败: " + e.getMessage());
        }
    }

    @Override
    public boolean startInterview(Long interviewId, Map<String, Object> startParams) {
        try {
            PmPerformanceInterview interview = getById(interviewId);
            if (interview == null) {
                throw new RuntimeException("面谈不存在");
            }

            if (!"SCHEDULED".equals(interview.getInterviewStatus())) {
                throw new RuntimeException("只有已安排状态的面谈才能开始");
            }

            interview.setActualStartTime(LocalDateTime.now());
            interview.setInterviewStatus("ONGOING");

            return updateById(interview);
        } catch (Exception e) {
            log.error("开始绩效面谈失败", e);
            throw new RuntimeException("开始绩效面谈失败: " + e.getMessage());
        }
    }

    @Override
    public boolean completeInterview(Long interviewId, Map<String, Object> completeParams) {
        try {
            PmPerformanceInterview interview = getById(interviewId);
            if (interview == null) {
                throw new RuntimeException("面谈不存在");
            }

            if (!"ONGOING".equals(interview.getInterviewStatus())) {
                throw new RuntimeException("只有进行中状态的面谈才能完成");
            }

            // 更新面谈结果
            interview.setActualEndTime(LocalDateTime.now());
            interview.setInterviewStatus("COMPLETED");

            // 保存面谈记录和反馈
            if (completeParams.containsKey("interviewNotes")) {
                interview.setInterviewNotes((String) completeParams.get("interviewNotes"));
            }
            if (completeParams.containsKey("interviewSummary")) {
                interview.setInterviewSummary((String) completeParams.get("interviewSummary"));
            }
            if (completeParams.containsKey("performanceSummary")) {
                interview.setPerformanceSummary((String) completeParams.get("performanceSummary"));
            }
            if (completeParams.containsKey("keyAchievements")) {
                interview.setKeyAchievements((String) completeParams.get("keyAchievements"));
            }
            if (completeParams.containsKey("improvementAreas")) {
                interview.setImprovementAreas((String) completeParams.get("improvementAreas"));
            }
            if (completeParams.containsKey("developmentGoals")) {
                interview.setDevelopmentGoals((String) completeParams.get("developmentGoals"));
            }
            if (completeParams.containsKey("actionPlan")) {
                interview.setActionPlan((String) completeParams.get("actionPlan"));
            }
            if (completeParams.containsKey("satisfactionRating")) {
                interview.setSatisfactionRating((Integer) completeParams.get("satisfactionRating"));
            }

            // 设置跟进计划
            Boolean needFollowUp = (Boolean) completeParams.get("needFollowUp");
            if (needFollowUp != null && needFollowUp) {
                interview.setNeedFollowUp(1);
                interview.setFollowUpStatus("PENDING");
                interview.setFollowUpDeadline((LocalDateTime) completeParams.get("followUpDeadline"));
                interview.setFollowUpPlan((String) completeParams.get("followUpPlan"));
            } else {
                interview.setNeedFollowUp(0);
            }

            return updateById(interview);
        } catch (Exception e) {
            log.error("完成绩效面谈失败", e);
            throw new RuntimeException("完成绩效面谈失败: " + e.getMessage());
        }
    }

    @Override
    public boolean cancelInterview(Long interviewId, Map<String, Object> cancelParams) {
        try {
            PmPerformanceInterview interview = getById(interviewId);
            if (interview == null) {
                throw new RuntimeException("面谈不存在");
            }

            if ("COMPLETED".equals(interview.getInterviewStatus()) || "CANCELLED".equals(interview.getInterviewStatus())) {
                throw new RuntimeException("已完成或已取消的面谈不能再次取消");
            }

            interview.setInterviewStatus("CANCELLED");
            if (cancelParams.containsKey("cancelReason")) {
                interview.setRemarks((String) cancelParams.get("cancelReason"));
            }

            return updateById(interview);
        } catch (Exception e) {
            log.error("取消绩效面谈失败", e);
            throw new RuntimeException("取消绩效面谈失败: " + e.getMessage());
        }
    }

    @Override
    public boolean postponeInterview(Long interviewId, Map<String, Object> postponeParams) {
        try {
            PmPerformanceInterview interview = getById(interviewId);
            if (interview == null) {
                throw new RuntimeException("面谈不存在");
            }

            if ("COMPLETED".equals(interview.getInterviewStatus()) || "CANCELLED".equals(interview.getInterviewStatus())) {
                throw new RuntimeException("已完成或已取消的面谈不能延期");
            }

            // 更新面谈时间
            LocalDateTime newStartTime = (LocalDateTime) postponeParams.get("newStartTime");
            LocalDateTime newEndTime = (LocalDateTime) postponeParams.get("newEndTime");

            if (newStartTime != null && newEndTime != null) {
                // 检查时间冲突
                if (checkTimeConflict(interview.getInterviewerId(), newStartTime, newEndTime, interviewId)) {
                    throw new RuntimeException("新的面谈时间与其他面谈冲突，请重新选择时间");
                }

                interview.setPlannedStartTime(newStartTime);
                interview.setPlannedEndTime(newEndTime);
            }

            interview.setInterviewStatus("POSTPONED");
            if (postponeParams.containsKey("postponeReason")) {
                interview.setRemarks((String) postponeParams.get("postponeReason"));
            }

            return updateById(interview);
        } catch (Exception e) {
            log.error("延期绩效面谈失败", e);
            throw new RuntimeException("延期绩效面谈失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchUpdateStatus(List<Long> interviewIds, String status, Map<String, Object> updateParams) {
        try {
            Long updatedBy = (Long) updateParams.get("updatedBy");
            String updatedName = (String) updateParams.get("updatedName");

            return interviewMapper.batchUpdateStatus(interviewIds, status, updatedBy, updatedName) > 0;
        } catch (Exception e) {
            log.error("批量更新面谈状态失败", e);
            throw new RuntimeException("批量更新面谈状态失败: " + e.getMessage());
        }
    }

    @Override
    public boolean saveInterviewRecord(Long interviewId, Map<String, Object> recordParams) {
        try {
            PmPerformanceInterview interview = getById(interviewId);
            if (interview == null) {
                throw new RuntimeException("面谈不存在");
            }

            // 更新面谈记录
            if (recordParams.containsKey("interviewNotes")) {
                interview.setInterviewNotes((String) recordParams.get("interviewNotes"));
            }
            if (recordParams.containsKey("performanceSummary")) {
                interview.setPerformanceSummary((String) recordParams.get("performanceSummary"));
            }
            if (recordParams.containsKey("keyAchievements")) {
                interview.setKeyAchievements((String) recordParams.get("keyAchievements"));
            }
            if (recordParams.containsKey("improvementAreas")) {
                interview.setImprovementAreas((String) recordParams.get("improvementAreas"));
            }

            return updateById(interview);
        } catch (Exception e) {
            log.error("保存面谈记录失败", e);
            throw new RuntimeException("保存面谈记录失败: " + e.getMessage());
        }
    }

    @Override
    public boolean submitFeedback(Long interviewId, Map<String, Object> feedbackParams) {
        try {
            PmPerformanceInterview interview = getById(interviewId);
            if (interview == null) {
                throw new RuntimeException("面谈不存在");
            }

            // 更新反馈信息
            if (feedbackParams.containsKey("employeeFeedback")) {
                interview.setEmployeeFeedback((String) feedbackParams.get("employeeFeedback"));
            }
            if (feedbackParams.containsKey("managerFeedback")) {
                interview.setManagerFeedback((String) feedbackParams.get("managerFeedback"));
            }
            if (feedbackParams.containsKey("satisfactionRating")) {
                interview.setSatisfactionRating((Integer) feedbackParams.get("satisfactionRating"));
            }
            if (feedbackParams.containsKey("effectivenessEvaluation")) {
                interview.setEffectivenessEvaluation((String) feedbackParams.get("effectivenessEvaluation"));
            }

            return updateById(interview);
        } catch (Exception e) {
            log.error("提交面谈反馈失败", e);
            throw new RuntimeException("提交面谈反馈失败: " + e.getMessage());
        }
    }

    @Override
    public boolean createFollowUpPlan(Long interviewId, Map<String, Object> followUpParams) {
        try {
            PmPerformanceInterview interview = getById(interviewId);
            if (interview == null) {
                throw new RuntimeException("面谈不存在");
            }

            interview.setNeedFollowUp(1);
            interview.setFollowUpStatus("PENDING");
            interview.setFollowUpPlan((String) followUpParams.get("followUpPlan"));
            interview.setFollowUpDeadline((LocalDateTime) followUpParams.get("followUpDeadline"));

            return updateById(interview);
        } catch (Exception e) {
            log.error("创建跟进计划失败", e);
            throw new RuntimeException("创建跟进计划失败: " + e.getMessage());
        }
    }

    @Override
    public boolean updateFollowUpStatus(Long interviewId, String followUpStatus, Map<String, Object> updateParams) {
        try {
            PmPerformanceInterview interview = getById(interviewId);
            if (interview == null) {
                throw new RuntimeException("面谈不存在");
            }

            interview.setFollowUpStatus(followUpStatus);

            return updateById(interview);
        } catch (Exception e) {
            log.error("更新跟进状态失败", e);
            throw new RuntimeException("更新跟进状态失败: " + e.getMessage());
        }
    }

    @Override
    public boolean batchUpdateFollowUpStatus(List<Long> interviewIds, String followUpStatus, Map<String, Object> updateParams) {
        try {
            Long updatedBy = (Long) updateParams.get("updatedBy");
            String updatedName = (String) updateParams.get("updatedName");

            return interviewMapper.batchUpdateFollowUpStatus(interviewIds, followUpStatus, updatedBy, updatedName) > 0;
        } catch (Exception e) {
            log.error("批量更新跟进状态失败", e);
            throw new RuntimeException("批量更新跟进状态失败: " + e.getMessage());
        }
    }

    @Override
    public List<PmPerformanceInterview> getInterviewsByInterviewee(Long intervieweeId, String status, Integer limit) {
        try {
            return interviewMapper.selectByIntervieweeId(intervieweeId, status, limit);
        } catch (Exception e) {
            log.error("根据被面谈人查询面谈列表失败", e);
            throw new RuntimeException("根据被面谈人查询面谈列表失败: " + e.getMessage());
        }
    }

    @Override
    public List<PmPerformanceInterview> getInterviewsByInterviewer(Long interviewerId, String status, Integer limit) {
        try {
            return interviewMapper.selectByInterviewerId(interviewerId, status, limit);
        } catch (Exception e) {
            log.error("根据面谈官查询面谈列表失败", e);
            throw new RuntimeException("根据面谈官查询面谈列表失败: " + e.getMessage());
        }
    }

    @Override
    public List<PmPerformanceInterview> getInterviewsByDept(Long deptId, String status, Integer limit) {
        try {
            return interviewMapper.selectByDeptId(deptId, status, limit);
        } catch (Exception e) {
            log.error("根据部门查询面谈列表失败", e);
            throw new RuntimeException("根据部门查询面谈列表失败: " + e.getMessage());
        }
    }

    @Override
    public List<PmPerformanceInterview> getPendingFollowUpInterviews(LocalDateTime deadline, Integer limit) {
        try {
            return interviewMapper.selectPendingFollowUp(deadline, limit);
        } catch (Exception e) {
            log.error("查询待跟进面谈失败", e);
            throw new RuntimeException("查询待跟进面谈失败: " + e.getMessage());
        }
    }

    @Override
    public List<PmPerformanceInterview> getUpcomingInterviews(LocalDateTime deadline, Integer limit) {
        try {
            return interviewMapper.selectUpcomingInterviews(deadline, limit);
        } catch (Exception e) {
            log.error("查询即将到期面谈失败", e);
            throw new RuntimeException("查询即将到期面谈失败: " + e.getMessage());
        }
    }

    @Override
    public List<PmPerformanceInterview> getOverdueInterviews(LocalDateTime currentTime, Integer limit) {
        try {
            return interviewMapper.selectOverdueInterviews(currentTime, limit);
        } catch (Exception e) {
            log.error("查询逾期面谈失败", e);
            throw new RuntimeException("查询逾期面谈失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getInterviewStatistics(Map<String, Object> statisticsParams) {
        try {
            Integer interviewYear = (Integer) statisticsParams.get("interviewYear");
            Integer interviewQuarter = (Integer) statisticsParams.get("interviewQuarter");
            Integer interviewMonth = (Integer) statisticsParams.get("interviewMonth");
            Long deptId = (Long) statisticsParams.get("deptId");
            Long interviewerId = (Long) statisticsParams.get("interviewerId");

            return interviewMapper.selectInterviewStatistics(interviewYear, interviewQuarter, interviewMonth, deptId, interviewerId);
        } catch (Exception e) {
            log.error("统计面谈数据失败", e);
            throw new RuntimeException("统计面谈数据失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getInterviewStatusDistribution(Integer year, Long deptId) {
        try {
            return interviewMapper.selectInterviewStatusDistribution(year, deptId);
        } catch (Exception e) {
            log.error("统计面谈状态分布失败", e);
            throw new RuntimeException("统计面谈状态分布失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getInterviewTypeDistribution(Integer year, Long deptId) {
        try {
            return interviewMapper.selectInterviewTypeDistribution(year, deptId);
        } catch (Exception e) {
            log.error("统计面谈类型分布失败", e);
            throw new RuntimeException("统计面谈类型分布失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getInterviewCompletionTrend(LocalDateTime startTime, LocalDateTime endTime, Long deptId) {
        try {
            return interviewMapper.selectInterviewCompletionTrend(startTime, endTime, deptId);
        } catch (Exception e) {
            log.error("统计面谈完成趋势失败", e);
            throw new RuntimeException("统计面谈完成趋势失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getSatisfactionDistribution(Integer year, Long deptId) {
        try {
            return interviewMapper.selectSatisfactionDistribution(year, deptId);
        } catch (Exception e) {
            log.error("统计面谈满意度分布失败", e);
            throw new RuntimeException("统计面谈满意度分布失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getInterviewRanking(Integer year, String rankType, Integer limit) {
        try {
            return interviewMapper.selectInterviewRanking(year, rankType, limit);
        } catch (Exception e) {
            log.error("查询面谈排行榜失败", e);
            throw new RuntimeException("查询面谈排行榜失败: " + e.getMessage());
        }
    }

    @Override
    public boolean checkTimeConflict(Long interviewerId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId) {
        try {
            int conflictCount = interviewMapper.checkTimeConflict(interviewerId, startTime, endTime, excludeId);
            return conflictCount > 0;
        } catch (Exception e) {
            log.error("检查面谈时间冲突失败", e);
            throw new RuntimeException("检查面谈时间冲突失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAvailableInterviewers(Long deptId, String interviewType, Integer limit) {
        try {
            return interviewMapper.selectAvailableInterviewers(deptId, interviewType, limit);
        } catch (Exception e) {
            log.error("查询可用面谈官失败", e);
            throw new RuntimeException("查询可用面谈官失败: " + e.getMessage());
        }
    }

    @Override
    public List<PmPerformanceInterview> getInterviewReminders(LocalDateTime reminderTime, Integer limit) {
        try {
            return interviewMapper.selectInterviewReminders(reminderTime, limit);
        } catch (Exception e) {
            log.error("查询面谈提醒失败", e);
            throw new RuntimeException("查询面谈提醒失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> generateInterviewReport(Long interviewId, Map<String, Object> reportParams) {
        try {
            PmPerformanceInterview interview = getById(interviewId);
            if (interview == null) {
                throw new RuntimeException("面谈不存在");
            }

            Map<String, Object> report = new HashMap<>();
            report.put("interview", interview);
            report.put("reportType", reportParams.get("reportType"));
            report.put("generateTime", LocalDateTime.now());

            // 这里可以添加更多报告生成逻辑
            return report;
        } catch (Exception e) {
            log.error("生成面谈报告失败", e);
            throw new RuntimeException("生成面谈报告失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> exportInterviewData(Map<String, Object> exportParams) {
        try {
            Integer interviewYear = (Integer) exportParams.get("interviewYear");
            Integer interviewQuarter = (Integer) exportParams.get("interviewQuarter");
            Integer interviewMonth = (Integer) exportParams.get("interviewMonth");
            Long deptId = (Long) exportParams.get("deptId");
            String interviewStatus = (String) exportParams.get("interviewStatus");

            return interviewMapper.selectInterviewExportData(interviewYear, interviewQuarter, interviewMonth, deptId, interviewStatus);
        } catch (Exception e) {
            log.error("导出面谈数据失败", e);
            throw new RuntimeException("导出面谈数据失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> importInterviewData(List<Map<String, Object>> importData, Map<String, Object> importParams) {
        try {
            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();

            for (Map<String, Object> data : importData) {
                try {
                    PmPerformanceInterview interview = convertMapToInterview(data);
                    if (createInterview(interview)) {
                        successCount++;
                    } else {
                        failCount++;
                        errorMessages.add("导入第" + (successCount + failCount) + "行数据失败");
                    }
                } catch (Exception e) {
                    failCount++;
                    errorMessages.add("导入第" + (successCount + failCount) + "行数据失败: " + e.getMessage());
                }
            }

            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errorMessages", errorMessages);

            return result;
        } catch (Exception e) {
            log.error("导入面谈数据失败", e);
            throw new RuntimeException("导入面谈数据失败: " + e.getMessage());
        }
    }

    @Override
    public PmPerformanceInterview copyInterview(Long interviewId, Map<String, Object> copyParams) {
        try {
            PmPerformanceInterview originalInterview = getById(interviewId);
            if (originalInterview == null) {
                throw new RuntimeException("原面谈不存在");
            }

            PmPerformanceInterview newInterview = new PmPerformanceInterview();
            // 复制基本信息
            newInterview.setInterviewTitle(originalInterview.getInterviewTitle() + "_副本");
            newInterview.setInterviewType(originalInterview.getInterviewType());
            newInterview.setIntervieweeId(originalInterview.getIntervieweeId());
            newInterview.setIntervieweeName(originalInterview.getIntervieweeName());
            newInterview.setInterviewerId(originalInterview.getInterviewerId());
            newInterview.setInterviewerName(originalInterview.getInterviewerName());
            newInterview.setInterviewPurpose(originalInterview.getInterviewPurpose());
            newInterview.setInterviewAgenda(originalInterview.getInterviewAgenda());
            newInterview.setInterviewPreparation(originalInterview.getInterviewPreparation());

            // 设置新的状态和时间
            newInterview.setInterviewStatus("PLANNED");

            if (createInterview(newInterview)) {
                return newInterview;
            } else {
                throw new RuntimeException("复制面谈失败");
            }
        } catch (Exception e) {
            log.error("复制面谈失败", e);
            throw new RuntimeException("复制面谈失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> batchCreateInterviews(List<PmPerformanceInterview> interviews, Map<String, Object> batchParams) {
        try {
            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();

            for (PmPerformanceInterview interview : interviews) {
                try {
                    if (createInterview(interview)) {
                        successCount++;
                    } else {
                        failCount++;
                        errorMessages.add("创建面谈失败: " + interview.getInterviewTitle());
                    }
                } catch (Exception e) {
                    failCount++;
                    errorMessages.add("创建面谈失败: " + interview.getInterviewTitle() + ", 错误: " + e.getMessage());
                }
            }

            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errorMessages", errorMessages);

            return result;
        } catch (Exception e) {
            log.error("批量创建面谈失败", e);
            throw new RuntimeException("批量创建面谈失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> recommendInterviewTimes(Long interviewerId, Long intervieweeId, Integer duration, List<String> preferredDates) {
        try {
            // 这里实现智能推荐面谈时间的逻辑
            List<Map<String, Object>> recommendations = new ArrayList<>();
            
            // 模拟推荐结果
            for (String date : preferredDates) {
                Map<String, Object> recommendation = new HashMap<>();
                recommendation.put("date", date);
                recommendation.put("startTime", "09:00");
                recommendation.put("endTime", "10:00");
                recommendation.put("available", true);
                recommendations.add(recommendation);
            }

            return recommendations;
        } catch (Exception e) {
            log.error("智能推荐面谈时间失败", e);
            throw new RuntimeException("智能推荐面谈时间失败: " + e.getMessage());
        }
    }

    @Override
    public boolean sendInterviewNotification(Long interviewId, Map<String, Object> notificationParams) {
        try {
            PmPerformanceInterview interview = getById(interviewId);
            if (interview == null) {
                throw new RuntimeException("面谈不存在");
            }

            // 这里实现发送通知的逻辑
            log.info("发送面谈通知: 面谈ID={}, 通知类型={}", interviewId, notificationParams.get("notificationType"));

            return true;
        } catch (Exception e) {
            log.error("发送面谈通知失败", e);
            throw new RuntimeException("发送面谈通知失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> batchSendNotifications(List<Long> interviewIds, Map<String, Object> notificationParams) {
        try {
            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failCount = 0;

            for (Long interviewId : interviewIds) {
                try {
                    if (sendInterviewNotification(interviewId, notificationParams)) {
                        successCount++;
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    failCount++;
                }
            }

            result.put("successCount", successCount);
            result.put("failCount", failCount);

            return result;
        } catch (Exception e) {
            log.error("批量发送面谈通知失败", e);
            throw new RuntimeException("批量发送面谈通知失败: " + e.getMessage());
        }
    }

    /**
     * 生成面谈编码
     */
    private String generateInterviewCode() {
        return "INT" + System.currentTimeMillis();
    }

    /**
     * 将Map转换为面谈对象
     */
    private PmPerformanceInterview convertMapToInterview(Map<String, Object> data) {
        PmPerformanceInterview interview = new PmPerformanceInterview();
        // 这里实现Map到对象的转换逻辑
        interview.setInterviewTitle((String) data.get("interviewTitle"));
        interview.setInterviewType((String) data.get("interviewType"));
        // ... 其他字段转换
        return interview;
    }
}
