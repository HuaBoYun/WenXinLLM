package com.management.accountant.controller.pm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.pm.PmPerformanceInterview;
import com.management.accountant.service.pm.PmPerformanceInterviewService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 绩效面谈管理控制器
 *
 * @author AI Assistant
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/pm/performance-interview")
@Api(tags = "绩效面谈管理")
public class PmPerformanceInterviewController {

    @Autowired
    private PmPerformanceInterviewService interviewService;

    /**
     * 分页查询绩效面谈
     */
    @GetMapping("/page")
    @ApiOperation("分页查询绩效面谈")
    public MyJsonBean queryInterviewPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("查询参数") @RequestParam Map<String, Object> queryParams) {
        try {
            Page<PmPerformanceInterview> page = new Page<>(current, size);
            IPage<PmPerformanceInterview> result = interviewService.queryInterviewPage(page, queryParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("分页查询绩效面谈失败", e);
            return MyJsonBean.error("分页查询绩效面谈失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询绩效面谈
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询绩效面谈")
    public MyJsonBean getInterviewById(@ApiParam("面谈ID") @PathVariable Long id) {
        try {
            PmPerformanceInterview interview = interviewService.getInterviewById(id);
            return MyJsonBean.success(interview);
        } catch (Exception e) {
            log.error("查询绩效面谈失败", e);
            return MyJsonBean.error("查询绩效面谈失败: " + e.getMessage());
        }
    }

    /**
     * 查询面谈详情（包含关联信息）
     */
    @GetMapping("/{id}/detail")
    @ApiOperation("查询面谈详情")
    public MyJsonBean getInterviewDetail(@ApiParam("面谈ID") @PathVariable Long id) {
        try {
            Map<String, Object> detail = interviewService.getInterviewDetail(id);
            return MyJsonBean.success(detail);
        } catch (Exception e) {
            log.error("查询面谈详情失败", e);
            return MyJsonBean.error("查询面谈详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建绩效面谈
     */
    @PostMapping
    @ApiOperation("创建绩效面谈")
    public MyJsonBean createInterview(@ApiParam("面谈信息") @RequestBody PmPerformanceInterview interview) {
        try {
            boolean result = interviewService.createInterview(interview);
            if (result) {
                return MyJsonBean.successData(interview,  "创建成功");
            } else {
                return MyJsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建绩效面谈失败", e);
            return MyJsonBean.error("创建绩效面谈失败: " + e.getMessage());
        }
    }

    /**
     * 更新绩效面谈
     */
    @PutMapping
    @ApiOperation("更新绩效面谈")
    public MyJsonBean updateInterview(@ApiParam("面谈信息") @RequestBody PmPerformanceInterview interview) {
        try {
            boolean result = interviewService.updateInterview(interview);
            if (result) {
                return MyJsonBean.successData(interview,  "更新成功");
            } else {
                return MyJsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新绩效面谈失败", e);
            return MyJsonBean.error("更新绩效面谈失败: " + e.getMessage());
        }
    }

    /**
     * 删除绩效面谈
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除绩效面谈")
    public MyJsonBean deleteInterview(@ApiParam("面谈ID") @PathVariable Long id) {
        try {
            boolean result = interviewService.deleteInterview(id);
            if (result) {
                return MyJsonBean.success("删除成功");
            } else {
                return MyJsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除绩效面谈失败", e);
            return MyJsonBean.error("删除绩效面谈失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除绩效面谈
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除绩效面谈")
    public MyJsonBean batchDeleteInterviews(@ApiParam("面谈ID列表") @RequestBody List<Long> interviewIds) {
        try {
            boolean result = interviewService.batchDeleteInterviews(interviewIds);
            if (result) {
                return MyJsonBean.success("批量删除成功");
            } else {
                return MyJsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除绩效面谈失败", e);
            return MyJsonBean.error("批量删除绩效面谈失败: " + e.getMessage());
        }
    }

    /**
     * 安排面谈
     */
    @PostMapping("/{id}/schedule")
    @ApiOperation("安排面谈")
    public MyJsonBean scheduleInterview(
            @ApiParam("面谈ID") @PathVariable Long id,
            @ApiParam("安排参数") @RequestBody Map<String, Object> scheduleParams) {
        try {
            boolean result = interviewService.scheduleInterview(id, scheduleParams);
            if (result) {
                return MyJsonBean.success("安排成功");
            } else {
                return MyJsonBean.error("安排失败");
            }
        } catch (Exception e) {
            log.error("安排面谈失败", e);
            return MyJsonBean.error("安排面谈失败: " + e.getMessage());
        }
    }

    /**
     * 开始面谈
     */
    @PostMapping("/{id}/start")
    @ApiOperation("开始面谈")
    public MyJsonBean startInterview(
            @ApiParam("面谈ID") @PathVariable Long id,
            @ApiParam("开始参数") @RequestBody Map<String, Object> startParams) {
        try {
            boolean result = interviewService.startInterview(id, startParams);
            if (result) {
                return MyJsonBean.success("开始成功");
            } else {
                return MyJsonBean.error("开始失败");
            }
        } catch (Exception e) {
            log.error("开始面谈失败", e);
            return MyJsonBean.error("开始面谈失败: " + e.getMessage());
        }
    }

    /**
     * 完成面谈
     */
    @PostMapping("/{id}/complete")
    @ApiOperation("完成面谈")
    public MyJsonBean completeInterview(
            @ApiParam("面谈ID") @PathVariable Long id,
            @ApiParam("完成参数") @RequestBody Map<String, Object> completeParams) {
        try {
            boolean result = interviewService.completeInterview(id, completeParams);
            if (result) {
                return MyJsonBean.success("完成成功");
            } else {
                return MyJsonBean.error("完成失败");
            }
        } catch (Exception e) {
            log.error("完成面谈失败", e);
            return MyJsonBean.error("完成面谈失败: " + e.getMessage());
        }
    }

    /**
     * 取消面谈
     */
    @PostMapping("/{id}/cancel")
    @ApiOperation("取消面谈")
    public MyJsonBean cancelInterview(
            @ApiParam("面谈ID") @PathVariable Long id,
            @ApiParam("取消参数") @RequestBody Map<String, Object> cancelParams) {
        try {
            boolean result = interviewService.cancelInterview(id, cancelParams);
            if (result) {
                return MyJsonBean.success("取消成功");
            } else {
                return MyJsonBean.error("取消失败");
            }
        } catch (Exception e) {
            log.error("取消面谈失败", e);
            return MyJsonBean.error("取消面谈失败: " + e.getMessage());
        }
    }

    /**
     * 延期面谈
     */
    @PostMapping("/{id}/postpone")
    @ApiOperation("延期面谈")
    public MyJsonBean postponeInterview(
            @ApiParam("面谈ID") @PathVariable Long id,
            @ApiParam("延期参数") @RequestBody Map<String, Object> postponeParams) {
        try {
            boolean result = interviewService.postponeInterview(id, postponeParams);
            if (result) {
                return MyJsonBean.success("延期成功");
            } else {
                return MyJsonBean.error("延期失败");
            }
        } catch (Exception e) {
            log.error("延期面谈失败", e);
            return MyJsonBean.error("延期面谈失败: " + e.getMessage());
        }
    }

    /**
     * 批量更新面谈状态
     */
    @PostMapping("/batch/status")
    @ApiOperation("批量更新面谈状态")
    public MyJsonBean batchUpdateStatus(
            @ApiParam("面谈ID列表") @RequestParam List<Long> interviewIds,
            @ApiParam("状态") @RequestParam String status,
            @ApiParam("更新参数") @RequestBody Map<String, Object> updateParams) {
        try {
            boolean result = interviewService.batchUpdateStatus(interviewIds, status, updateParams);
            if (result) {
                return MyJsonBean.success("批量更新成功");
            } else {
                return MyJsonBean.error("批量更新失败");
            }
        } catch (Exception e) {
            log.error("批量更新面谈状态失败", e);
            return MyJsonBean.error("批量更新面谈状态失败: " + e.getMessage());
        }
    }

    /**
     * 保存面谈记录
     */
    @PostMapping("/{id}/record")
    @ApiOperation("保存面谈记录")
    public MyJsonBean saveInterviewRecord(
            @ApiParam("面谈ID") @PathVariable Long id,
            @ApiParam("记录参数") @RequestBody Map<String, Object> recordParams) {
        try {
            boolean result = interviewService.saveInterviewRecord(id, recordParams);
            if (result) {
                return MyJsonBean.success("保存成功");
            } else {
                return MyJsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存面谈记录失败", e);
            return MyJsonBean.error("保存面谈记录失败: " + e.getMessage());
        }
    }

    /**
     * 提交面谈反馈
     */
    @PostMapping("/{id}/feedback")
    @ApiOperation("提交面谈反馈")
    public MyJsonBean submitFeedback(
            @ApiParam("面谈ID") @PathVariable Long id,
            @ApiParam("反馈参数") @RequestBody Map<String, Object> feedbackParams) {
        try {
            boolean result = interviewService.submitFeedback(id, feedbackParams);
            if (result) {
                return MyJsonBean.success("提交成功");
            } else {
                return MyJsonBean.error("提交失败");
            }
        } catch (Exception e) {
            log.error("提交面谈反馈失败", e);
            return MyJsonBean.error("提交面谈反馈失败: " + e.getMessage());
        }
    }

    /**
     * 创建跟进计划
     */
    @PostMapping("/{id}/follow-up")
    @ApiOperation("创建跟进计划")
    public MyJsonBean createFollowUpPlan(
            @ApiParam("面谈ID") @PathVariable Long id,
            @ApiParam("跟进参数") @RequestBody Map<String, Object> followUpParams) {
        try {
            boolean result = interviewService.createFollowUpPlan(id, followUpParams);
            if (result) {
                return MyJsonBean.success("创建成功");
            } else {
                return MyJsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建跟进计划失败", e);
            return MyJsonBean.error("创建跟进计划失败: " + e.getMessage());
        }
    }

    /**
     * 更新跟进状态
     */
    @PutMapping("/{id}/follow-up/status")
    @ApiOperation("更新跟进状态")
    public MyJsonBean updateFollowUpStatus(
            @ApiParam("面谈ID") @PathVariable Long id,
            @ApiParam("跟进状态") @RequestParam String followUpStatus,
            @ApiParam("更新参数") @RequestBody Map<String, Object> updateParams) {
        try {
            boolean result = interviewService.updateFollowUpStatus(id, followUpStatus, updateParams);
            if (result) {
                return MyJsonBean.success("更新成功");
            } else {
                return MyJsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新跟进状态失败", e);
            return MyJsonBean.error("更新跟进状态失败: " + e.getMessage());
        }
    }

    /**
     * 批量更新跟进状态
     */
    @PostMapping("/batch/follow-up/status")
    @ApiOperation("批量更新跟进状态")
    public MyJsonBean batchUpdateFollowUpStatus(
            @ApiParam("面谈ID列表") @RequestParam List<Long> interviewIds,
            @ApiParam("跟进状态") @RequestParam String followUpStatus,
            @ApiParam("更新参数") @RequestBody Map<String, Object> updateParams) {
        try {
            boolean result = interviewService.batchUpdateFollowUpStatus(interviewIds, followUpStatus, updateParams);
            if (result) {
                return MyJsonBean.success("批量更新成功");
            } else {
                return MyJsonBean.error("批量更新失败");
            }
        } catch (Exception e) {
            log.error("批量更新跟进状态失败", e);
            return MyJsonBean.error("批量更新跟进状态失败: " + e.getMessage());
        }
    }

    /**
     * 根据被面谈人查询面谈列表
     */
    @GetMapping("/interviewee/{intervieweeId}")
    @ApiOperation("根据被面谈人查询面谈列表")
    public MyJsonBean getInterviewsByInterviewee(
            @ApiParam("被面谈人ID") @PathVariable Long intervieweeId,
            @ApiParam("状态") @RequestParam(required = false) String status,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<PmPerformanceInterview> interviews = interviewService.getInterviewsByInterviewee(intervieweeId, status, limit);
            return MyJsonBean.success(interviews);
        } catch (Exception e) {
            log.error("根据被面谈人查询面谈列表失败", e);
            return MyJsonBean.error("根据被面谈人查询面谈列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据面谈官查询面谈列表
     */
    @GetMapping("/interviewer/{interviewerId}")
    @ApiOperation("根据面谈官查询面谈列表")
    public MyJsonBean getInterviewsByInterviewer(
            @ApiParam("面谈官ID") @PathVariable Long interviewerId,
            @ApiParam("状态") @RequestParam(required = false) String status,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<PmPerformanceInterview> interviews = interviewService.getInterviewsByInterviewer(interviewerId, status, limit);
            return MyJsonBean.success(interviews);
        } catch (Exception e) {
            log.error("根据面谈官查询面谈列表失败", e);
            return MyJsonBean.error("根据面谈官查询面谈列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据部门查询面谈列表
     */
    @GetMapping("/department/{deptId}")
    @ApiOperation("根据部门查询面谈列表")
    public MyJsonBean getInterviewsByDept(
            @ApiParam("部门ID") @PathVariable Long deptId,
            @ApiParam("状态") @RequestParam(required = false) String status,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<PmPerformanceInterview> interviews = interviewService.getInterviewsByDept(deptId, status, limit);
            return MyJsonBean.success(interviews);
        } catch (Exception e) {
            log.error("根据部门查询面谈列表失败", e);
            return MyJsonBean.error("根据部门查询面谈列表失败: " + e.getMessage());
        }
    }

    /**
     * 查询待跟进的面谈
     */
    @GetMapping("/pending-follow-up")
    @ApiOperation("查询待跟进的面谈")
    public MyJsonBean getPendingFollowUpInterviews(
            @ApiParam("截止时间") @RequestParam(required = false) LocalDateTime deadline,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            if (deadline == null) {
                deadline = LocalDateTime.now().plusDays(7); // 默认7天内
            }
            List<PmPerformanceInterview> interviews = interviewService.getPendingFollowUpInterviews(deadline, limit);
            return MyJsonBean.success(interviews);
        } catch (Exception e) {
            log.error("查询待跟进面谈失败", e);
            return MyJsonBean.error("查询待跟进面谈失败: " + e.getMessage());
        }
    }

    /**
     * 查询即将到期的面谈
     */
    @GetMapping("/upcoming")
    @ApiOperation("查询即将到期的面谈")
    public MyJsonBean getUpcomingInterviews(
            @ApiParam("截止时间") @RequestParam(required = false) LocalDateTime deadline,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            if (deadline == null) {
                deadline = LocalDateTime.now().plusDays(3); // 默认3天内
            }
            List<PmPerformanceInterview> interviews = interviewService.getUpcomingInterviews(deadline, limit);
            return MyJsonBean.success(interviews);
        } catch (Exception e) {
            log.error("查询即将到期面谈失败", e);
            return MyJsonBean.error("查询即将到期面谈失败: " + e.getMessage());
        }
    }

    /**
     * 查询逾期的面谈
     */
    @GetMapping("/overdue")
    @ApiOperation("查询逾期的面谈")
    public MyJsonBean getOverdueInterviews(
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<PmPerformanceInterview> interviews = interviewService.getOverdueInterviews(LocalDateTime.now(), limit);
            return MyJsonBean.success(interviews);
        } catch (Exception e) {
            log.error("查询逾期面谈失败", e);
            return MyJsonBean.error("查询逾期面谈失败: " + e.getMessage());
        }
    }

    /**
     * 统计面谈数据
     */
    @GetMapping("/statistics")
    @ApiOperation("统计面谈数据")
    public MyJsonBean getInterviewStatistics(@ApiParam("统计参数") @RequestParam Map<String, Object> statisticsParams) {
        try {
            Map<String, Object> statistics = interviewService.getInterviewStatistics(statisticsParams);
            return MyJsonBean.success(statistics);
        } catch (Exception e) {
            log.error("统计面谈数据失败", e);
            return MyJsonBean.error("统计面谈数据失败: " + e.getMessage());
        }
    }

    /**
     * 统计面谈状态分布
     */
    @GetMapping("/statistics/status-distribution")
    @ApiOperation("统计面谈状态分布")
    public MyJsonBean getInterviewStatusDistribution(
            @ApiParam("年度") @RequestParam Integer year,
            @ApiParam("部门ID") @RequestParam(required = false) Long deptId) {
        try {
            List<Map<String, Object>> distribution = interviewService.getInterviewStatusDistribution(year, deptId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计面谈状态分布失败", e);
            return MyJsonBean.error("统计面谈状态分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计面谈类型分布
     */
    @GetMapping("/statistics/type-distribution")
    @ApiOperation("统计面谈类型分布")
    public MyJsonBean getInterviewTypeDistribution(
            @ApiParam("年度") @RequestParam Integer year,
            @ApiParam("部门ID") @RequestParam(required = false) Long deptId) {
        try {
            List<Map<String, Object>> distribution = interviewService.getInterviewTypeDistribution(year, deptId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计面谈类型分布失败", e);
            return MyJsonBean.error("统计面谈类型分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计面谈完成趋势
     */
    @GetMapping("/statistics/completion-trend")
    @ApiOperation("统计面谈完成趋势")
    public MyJsonBean getInterviewCompletionTrend(
            @ApiParam("开始时间") @RequestParam LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam LocalDateTime endTime,
            @ApiParam("部门ID") @RequestParam(required = false) Long deptId) {
        try {
            List<Map<String, Object>> trend = interviewService.getInterviewCompletionTrend(startTime, endTime, deptId);
            return MyJsonBean.success(trend);
        } catch (Exception e) {
            log.error("统计面谈完成趋势失败", e);
            return MyJsonBean.error("统计面谈完成趋势失败: " + e.getMessage());
        }
    }

    /**
     * 统计面谈满意度分布
     */
    @GetMapping("/statistics/satisfaction-distribution")
    @ApiOperation("统计面谈满意度分布")
    public MyJsonBean getSatisfactionDistribution(
            @ApiParam("年度") @RequestParam Integer year,
            @ApiParam("部门ID") @RequestParam(required = false) Long deptId) {
        try {
            List<Map<String, Object>> distribution = interviewService.getSatisfactionDistribution(year, deptId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计面谈满意度分布失败", e);
            return MyJsonBean.error("统计面谈满意度分布失败: " + e.getMessage());
        }
    }

    /**
     * 查询面谈排行榜
     */
    @GetMapping("/ranking")
    @ApiOperation("查询面谈排行榜")
    public MyJsonBean getInterviewRanking(
            @ApiParam("年度") @RequestParam Integer year,
            @ApiParam("排行类型") @RequestParam String rankType,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Map<String, Object>> ranking = interviewService.getInterviewRanking(year, rankType, limit);
            return MyJsonBean.success(ranking);
        } catch (Exception e) {
            log.error("查询面谈排行榜失败", e);
            return MyJsonBean.error("查询面谈排行榜失败: " + e.getMessage());
        }
    }

    /**
     * 检查面谈时间冲突
     */
    @GetMapping("/check-conflict")
    @ApiOperation("检查面谈时间冲突")
    public MyJsonBean checkTimeConflict(
            @ApiParam("面谈官ID") @RequestParam Long interviewerId,
            @ApiParam("开始时间") @RequestParam LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam LocalDateTime endTime,
            @ApiParam("排除的面谈ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean hasConflict = interviewService.checkTimeConflict(interviewerId, startTime, endTime, excludeId);
            return MyJsonBean.success(hasConflict);
        } catch (Exception e) {
            log.error("检查面谈时间冲突失败", e);
            return MyJsonBean.error("检查面谈时间冲突失败: " + e.getMessage());
        }
    }

    /**
     * 查询可用面谈官
     */
    @GetMapping("/available-interviewers")
    @ApiOperation("查询可用面谈官")
    public MyJsonBean getAvailableInterviewers(
            @ApiParam("部门ID") @RequestParam Long deptId,
            @ApiParam("面谈类型") @RequestParam String interviewType,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Map<String, Object>> interviewers = interviewService.getAvailableInterviewers(deptId, interviewType, limit);
            return MyJsonBean.success(interviewers);
        } catch (Exception e) {
            log.error("查询可用面谈官失败", e);
            return MyJsonBean.error("查询可用面谈官失败: " + e.getMessage());
        }
    }

    /**
     * 查询面谈提醒列表
     */
    @GetMapping("/reminders")
    @ApiOperation("查询面谈提醒列表")
    public MyJsonBean getInterviewReminders(
            @ApiParam("提醒时间") @RequestParam(required = false) LocalDateTime reminderTime,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            if (reminderTime == null) {
                reminderTime = LocalDateTime.now().plusHours(1); // 默认1小时内
            }
            List<PmPerformanceInterview> reminders = interviewService.getInterviewReminders(reminderTime, limit);
            return MyJsonBean.success(reminders);
        } catch (Exception e) {
            log.error("查询面谈提醒失败", e);
            return MyJsonBean.error("查询面谈提醒失败: " + e.getMessage());
        }
    }

    /**
     * 生成面谈报告
     */
    @PostMapping("/{id}/report")
    @ApiOperation("生成面谈报告")
    public MyJsonBean generateInterviewReport(
            @ApiParam("面谈ID") @PathVariable Long id,
            @ApiParam("报告参数") @RequestBody Map<String, Object> reportParams) {
        try {
            Map<String, Object> report = interviewService.generateInterviewReport(id, reportParams);
            return MyJsonBean.success(report);
        } catch (Exception e) {
            log.error("生成面谈报告失败", e);
            return MyJsonBean.error("生成面谈报告失败: " + e.getMessage());
        }
    }

    /**
     * 导出面谈数据
     */
    @GetMapping("/export")
    @ApiOperation("导出面谈数据")
    public MyJsonBean exportInterviewData(@ApiParam("导出参数") @RequestParam Map<String, Object> exportParams) {
        try {
            List<Map<String, Object>> exportData = interviewService.exportInterviewData(exportParams);
            return MyJsonBean.success(exportData);
        } catch (Exception e) {
            log.error("导出面谈数据失败", e);
            return MyJsonBean.error("导出面谈数据失败: " + e.getMessage());
        }
    }

    /**
     * 导入面谈数据
     */
    @PostMapping("/import")
    @ApiOperation("导入面谈数据")
    public MyJsonBean importInterviewData(
            @ApiParam("导入数据") @RequestBody List<Map<String, Object>> importData,
            @ApiParam("导入参数") @RequestParam Map<String, Object> importParams) {
        try {
            Map<String, Object> result = interviewService.importInterviewData(importData, importParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导入面谈数据失败", e);
            return MyJsonBean.error("导入面谈数据失败: " + e.getMessage());
        }
    }

    /**
     * 复制面谈
     */
    @PostMapping("/{id}/copy")
    @ApiOperation("复制面谈")
    public MyJsonBean copyInterview(
            @ApiParam("面谈ID") @PathVariable Long id,
            @ApiParam("复制参数") @RequestBody Map<String, Object> copyParams) {
        try {
            PmPerformanceInterview newInterview = interviewService.copyInterview(id, copyParams);
            return MyJsonBean.successData(newInterview,  "复制成功");
        } catch (Exception e) {
            log.error("复制面谈失败", e);
            return MyJsonBean.error("复制面谈失败: " + e.getMessage());
        }
    }

    /**
     * 批量创建面谈
     */
    @PostMapping("/batch")
    @ApiOperation("批量创建面谈")
    public MyJsonBean batchCreateInterviews(
            @ApiParam("面谈列表") @RequestBody List<PmPerformanceInterview> interviews,
            @ApiParam("批量参数") @RequestParam Map<String, Object> batchParams) {
        try {
            Map<String, Object> result = interviewService.batchCreateInterviews(interviews, batchParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量创建面谈失败", e);
            return MyJsonBean.error("批量创建面谈失败: " + e.getMessage());
        }
    }

    /**
     * 智能推荐面谈时间
     */
    @GetMapping("/recommend-times")
    @ApiOperation("智能推荐面谈时间")
    public MyJsonBean recommendInterviewTimes(
            @ApiParam("面谈官ID") @RequestParam Long interviewerId,
            @ApiParam("被面谈人ID") @RequestParam Long intervieweeId,
            @ApiParam("面谈时长") @RequestParam Integer duration,
            @ApiParam("偏好日期") @RequestParam List<String> preferredDates) {
        try {
            List<Map<String, Object>> recommendations = interviewService.recommendInterviewTimes(interviewerId, intervieweeId, duration, preferredDates);
            return MyJsonBean.success(recommendations);
        } catch (Exception e) {
            log.error("智能推荐面谈时间失败", e);
            return MyJsonBean.error("智能推荐面谈时间失败: " + e.getMessage());
        }
    }

    /**
     * 发送面谈通知
     */
    @PostMapping("/{id}/notification")
    @ApiOperation("发送面谈通知")
    public MyJsonBean sendInterviewNotification(
            @ApiParam("面谈ID") @PathVariable Long id,
            @ApiParam("通知参数") @RequestBody Map<String, Object> notificationParams) {
        try {
            boolean result = interviewService.sendInterviewNotification(id, notificationParams);
            if (result) {
                return MyJsonBean.success("发送成功");
            } else {
                return MyJsonBean.error("发送失败");
            }
        } catch (Exception e) {
            log.error("发送面谈通知失败", e);
            return MyJsonBean.error("发送面谈通知失败: " + e.getMessage());
        }
    }

    /**
     * 批量发送面谈通知
     */
    @PostMapping("/batch/notification")
    @ApiOperation("批量发送面谈通知")
    public MyJsonBean batchSendNotifications(
            @ApiParam("面谈ID列表") @RequestParam List<Long> interviewIds,
            @ApiParam("通知参数") @RequestBody Map<String, Object> notificationParams) {
        try {
            Map<String, Object> result = interviewService.batchSendNotifications(interviewIds, notificationParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量发送面谈通知失败", e);
            return MyJsonBean.error("批量发送面谈通知失败: " + e.getMessage());
        }
    }
}
