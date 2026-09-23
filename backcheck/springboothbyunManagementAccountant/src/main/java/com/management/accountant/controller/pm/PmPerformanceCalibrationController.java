package com.management.accountant.controller.pm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.pm.PmPerformanceCalibration;
import com.management.accountant.service.pm.PmPerformanceCalibrationService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 绩效校准管理控制器
 *
 * @author AI Assistant
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/pm/performance-calibration")
@Api(tags = "绩效校准管理")
public class PmPerformanceCalibrationController {

    @Autowired
    private PmPerformanceCalibrationService calibrationService;

    // ==================== 基础CRUD操作 ====================

    /**
     * 分页查询绩效校准
     */
    @GetMapping("/page")
    @ApiOperation("分页查询绩效校准")
    public MyJsonBean queryCalibrationPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("校准编码") @RequestParam(required = false) String calibrationCode,
            @ApiParam("校准标题") @RequestParam(required = false) String calibrationTitle,
            @ApiParam("校准类型") @RequestParam(required = false) String calibrationType,
            @ApiParam("校准状态") @RequestParam(required = false) String calibrationStatus,
            @ApiParam("校准年度") @RequestParam(required = false) Integer calibrationYear,
            @ApiParam("校准季度") @RequestParam(required = false) Integer calibrationQuarter,
            @ApiParam("校准月份") @RequestParam(required = false) Integer calibrationMonth,
            @ApiParam("校准范围") @RequestParam(required = false) String calibrationScope,
            @ApiParam("目标部门ID") @RequestParam(required = false) Long targetDeptId,
            @ApiParam("校准负责人ID") @RequestParam(required = false) Long calibrationOwnerId,
            @ApiParam("校准负责人姓名") @RequestParam(required = false) String calibrationOwnerName,
            @ApiParam("优先级") @RequestParam(required = false) String priorityLevel,
            @ApiParam("是否需要跟进") @RequestParam(required = false) Integer needFollowUp,
            @ApiParam("跟进状态") @RequestParam(required = false) String followUpStatus,
            @ApiParam("开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        try {
            IPage<PmPerformanceCalibration> result = calibrationService.queryCalibrationPage(
                    current, size, calibrationCode, calibrationTitle, calibrationType,
                    calibrationStatus, calibrationYear, calibrationQuarter, calibrationMonth,
                    calibrationScope, targetDeptId, calibrationOwnerId, calibrationOwnerName,
                    priorityLevel, needFollowUp, followUpStatus, startTime, endTime
            );
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("分页查询绩效校准失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询绩效校准
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询绩效校准")
    public MyJsonBean getCalibrationById(@ApiParam("校准ID") @PathVariable Long id) {
        try {
            PmPerformanceCalibration calibration = calibrationService.getCalibrationById(id);
            if (calibration != null) {
                return MyJsonBean.success(calibration);
            } else {
                return MyJsonBean.error("校准不存在");
            }
        } catch (Exception e) {
            log.error("查询绩效校准失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询校准详情（包含关联信息）
     */
    @GetMapping("/{id}/detail")
    @ApiOperation("查询校准详情")
    public MyJsonBean getCalibrationDetail(@ApiParam("校准ID") @PathVariable Long id) {
        try {
            Map<String, Object> detail = calibrationService.getCalibrationDetail(id);
            return MyJsonBean.success(detail);
        } catch (Exception e) {
            log.error("查询校准详情失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建绩效校准
     */
    @PostMapping
    @ApiOperation("创建绩效校准")
    public MyJsonBean createCalibration(@RequestBody PmPerformanceCalibration calibration) {
        try {
            // 数据验证
            Map<String, Object> validation = calibrationService.validateCalibrationData(calibration);
            if (!(Boolean) validation.get("valid")) {
                return MyJsonBean.error("数据验证失败: " + validation.get("errors"));
            }

            boolean result = calibrationService.createCalibration(calibration);
            if (result) {
                return MyJsonBean.successData(calibration,  "创建成功");
            } else {
                return MyJsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建绩效校准失败", e);
            return MyJsonBean.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新绩效校准
     */
    @PutMapping
    @ApiOperation("更新绩效校准")
    public MyJsonBean updateCalibration(@RequestBody PmPerformanceCalibration calibration) {
        try {
            // 数据验证
            Map<String, Object> validation = calibrationService.validateCalibrationData(calibration);
            if (!(Boolean) validation.get("valid")) {
                return MyJsonBean.error("数据验证失败: " + validation.get("errors"));
            }

            boolean result = calibrationService.updateCalibration(calibration);
            if (result) {
                return MyJsonBean.success("更新成功");
            } else {
                return MyJsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新绩效校准失败", e);
            return MyJsonBean.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除绩效校准
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除绩效校准")
    public MyJsonBean deleteCalibration(@ApiParam("校准ID") @PathVariable Long id) {
        try {
            boolean result = calibrationService.deleteCalibration(id);
            if (result) {
                return MyJsonBean.success("删除成功");
            } else {
                return MyJsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除绩效校准失败", e);
            return MyJsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除绩效校准
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除绩效校准")
    public MyJsonBean batchDeleteCalibrations(@RequestBody List<Long> calibrationIds) {
        try {
            boolean result = calibrationService.batchDeleteCalibrations(calibrationIds);
            if (result) {
                return MyJsonBean.success("批量删除成功");
            } else {
                return MyJsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除绩效校准失败", e);
            return MyJsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    // ==================== 校准流程管理 ====================

    /**
     * 开始校准
     */
    @PostMapping("/{id}/start")
    @ApiOperation("开始校准")
    public MyJsonBean startCalibration(
            @ApiParam("校准ID") @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> startData
    ) {
        try {
            boolean result = calibrationService.startCalibration(id, startData);
            if (result) {
                return MyJsonBean.success("校准开始成功");
            } else {
                return MyJsonBean.error("校准开始失败");
            }
        } catch (Exception e) {
            log.error("开始校准失败", e);
            return MyJsonBean.error("开始失败: " + e.getMessage());
        }
    }

    /**
     * 完成校准
     */
    @PostMapping("/{id}/complete")
    @ApiOperation("完成校准")
    public MyJsonBean completeCalibration(
            @ApiParam("校准ID") @PathVariable Long id,
            @RequestBody Map<String, Object> completeData
    ) {
        try {
            boolean result = calibrationService.completeCalibration(id, completeData);
            if (result) {
                return MyJsonBean.success("校准完成成功");
            } else {
                return MyJsonBean.error("校准完成失败");
            }
        } catch (Exception e) {
            log.error("完成校准失败", e);
            return MyJsonBean.error("完成失败: " + e.getMessage());
        }
    }

    /**
     * 取消校准
     */
    @PostMapping("/{id}/cancel")
    @ApiOperation("取消校准")
    public MyJsonBean cancelCalibration(
            @ApiParam("校准ID") @PathVariable Long id,
            @RequestBody Map<String, Object> cancelData
    ) {
        try {
            boolean result = calibrationService.cancelCalibration(id, cancelData);
            if (result) {
                return MyJsonBean.success("校准取消成功");
            } else {
                return MyJsonBean.error("校准取消失败");
            }
        } catch (Exception e) {
            log.error("取消校准失败", e);
            return MyJsonBean.error("取消失败: " + e.getMessage());
        }
    }

    /**
     * 批量更新校准状态
     */
    @PostMapping("/batch/status")
    @ApiOperation("批量更新校准状态")
    public MyJsonBean batchUpdateStatus(
            @ApiParam("校准ID列表") @RequestParam List<Long> calibrationIds,
            @ApiParam("状态") @RequestParam String status,
            @RequestBody(required = false) Map<String, Object> updateData
    ) {
        try {
            boolean result = calibrationService.batchUpdateStatus(calibrationIds, status, updateData);
            if (result) {
                return MyJsonBean.success("批量更新成功");
            } else {
                return MyJsonBean.error("批量更新失败");
            }
        } catch (Exception e) {
            log.error("批量更新校准状态失败", e);
            return MyJsonBean.error("批量更新失败: " + e.getMessage());
        }
    }

    // ==================== 校准记录和跟进 ====================

    /**
     * 保存校准记录
     */
    @PostMapping("/{id}/record")
    @ApiOperation("保存校准记录")
    public MyJsonBean saveCalibrationRecord(
            @ApiParam("校准ID") @PathVariable Long id,
            @RequestBody Map<String, Object> recordData
    ) {
        try {
            boolean result = calibrationService.saveCalibrationRecord(id, recordData);
            if (result) {
                return MyJsonBean.success("保存记录成功");
            } else {
                return MyJsonBean.error("保存记录失败");
            }
        } catch (Exception e) {
            log.error("保存校准记录失败", e);
            return MyJsonBean.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 创建跟进计划
     */
    @PostMapping("/{id}/follow-up")
    @ApiOperation("创建跟进计划")
    public MyJsonBean createFollowUpPlan(
            @ApiParam("校准ID") @PathVariable Long id,
            @RequestBody Map<String, Object> followUpData
    ) {
        try {
            boolean result = calibrationService.createFollowUpPlan(id, followUpData);
            if (result) {
                return MyJsonBean.success("创建跟进计划成功");
            } else {
                return MyJsonBean.error("创建跟进计划失败");
            }
        } catch (Exception e) {
            log.error("创建跟进计划失败", e);
            return MyJsonBean.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新跟进状态
     */
    @PutMapping("/{id}/follow-up/status")
    @ApiOperation("更新跟进状态")
    public MyJsonBean updateFollowUpStatus(
            @ApiParam("校准ID") @PathVariable Long id,
            @ApiParam("跟进状态") @RequestParam String followUpStatus,
            @RequestBody(required = false) Map<String, Object> updateData
    ) {
        try {
            boolean result = calibrationService.updateFollowUpStatus(id, followUpStatus, updateData);
            if (result) {
                return MyJsonBean.success("更新跟进状态成功");
            } else {
                return MyJsonBean.error("更新跟进状态失败");
            }
        } catch (Exception e) {
            log.error("更新跟进状态失败", e);
            return MyJsonBean.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 批量更新跟进状态
     */
    @PostMapping("/batch/follow-up/status")
    @ApiOperation("批量更新跟进状态")
    public MyJsonBean batchUpdateFollowUpStatus(
            @ApiParam("校准ID列表") @RequestParam List<Long> calibrationIds,
            @ApiParam("跟进状态") @RequestParam String followUpStatus,
            @RequestBody(required = false) Map<String, Object> updateData
    ) {
        try {
            boolean result = calibrationService.batchUpdateFollowUpStatus(calibrationIds, followUpStatus, updateData);
            if (result) {
                return MyJsonBean.success("批量更新跟进状态成功");
            } else {
                return MyJsonBean.error("批量更新跟进状态失败");
            }
        } catch (Exception e) {
            log.error("批量更新跟进状态失败", e);
            return MyJsonBean.error("批量更新失败: " + e.getMessage());
        }
    }

    // ==================== 查询接口 ====================

    /**
     * 根据校准负责人查询校准列表
     */
    @GetMapping("/owner/{ownerId}")
    @ApiOperation("根据校准负责人查询校准列表")
    public MyJsonBean getCalibrationsByOwner(
            @ApiParam("校准负责人ID") @PathVariable Long ownerId,
            @ApiParam("状态") @RequestParam(required = false) String status,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit
    ) {
        try {
            List<PmPerformanceCalibration> calibrations = calibrationService.getCalibrationsByOwner(ownerId, status, limit);
            return MyJsonBean.success(calibrations);
        } catch (Exception e) {
            log.error("根据校准负责人查询校准列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据部门查询校准列表
     */
    @GetMapping("/department/{deptId}")
    @ApiOperation("根据部门查询校准列表")
    public MyJsonBean getCalibrationsByDept(
            @ApiParam("部门ID") @PathVariable Long deptId,
            @ApiParam("状态") @RequestParam(required = false) String status,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit
    ) {
        try {
            List<PmPerformanceCalibration> calibrations = calibrationService.getCalibrationsByDept(deptId, status, limit);
            return MyJsonBean.success(calibrations);
        } catch (Exception e) {
            log.error("根据部门查询校准列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据校准类型查询校准列表
     */
    @GetMapping("/type/{type}")
    @ApiOperation("根据校准类型查询校准列表")
    public MyJsonBean getCalibrationsByType(
            @ApiParam("校准类型") @PathVariable String type,
            @ApiParam("校准年度") @RequestParam(required = false) Integer calibrationYear,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit
    ) {
        try {
            List<PmPerformanceCalibration> calibrations = calibrationService.getCalibrationsByType(type, calibrationYear, limit);
            return MyJsonBean.success(calibrations);
        } catch (Exception e) {
            log.error("根据校准类型查询校准列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询待跟进的校准
     */
    @GetMapping("/pending-follow-up")
    @ApiOperation("查询待跟进的校准")
    public MyJsonBean getPendingFollowUpCalibrations(
            @ApiParam("截止时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime deadline,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit
    ) {
        try {
            List<PmPerformanceCalibration> calibrations = calibrationService.getPendingFollowUpCalibrations(deadline, limit);
            return MyJsonBean.success(calibrations);
        } catch (Exception e) {
            log.error("查询待跟进的校准失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询即将到期的校准
     */
    @GetMapping("/upcoming")
    @ApiOperation("查询即将到期的校准")
    public MyJsonBean getUpcomingCalibrations(
            @ApiParam("截止时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime deadline,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit
    ) {
        try {
            List<PmPerformanceCalibration> calibrations = calibrationService.getUpcomingCalibrations(deadline, limit);
            return MyJsonBean.success(calibrations);
        } catch (Exception e) {
            log.error("查询即将到期的校准失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询逾期的校准
     */
    @GetMapping("/overdue")
    @ApiOperation("查询逾期的校准")
    public MyJsonBean getOverdueCalibrations(
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit
    ) {
        try {
            List<PmPerformanceCalibration> calibrations = calibrationService.getOverdueCalibrations(limit);
            return MyJsonBean.success(calibrations);
        } catch (Exception e) {
            log.error("查询逾期的校准失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析 ====================

    /**
     * 统计校准数据
     */
    @GetMapping("/statistics")
    @ApiOperation("统计校准数据")
    public MyJsonBean getCalibrationStatistics(
            @ApiParam("校准年度") @RequestParam(required = false) Integer calibrationYear,
            @ApiParam("校准季度") @RequestParam(required = false) Integer calibrationQuarter,
            @ApiParam("校准月份") @RequestParam(required = false) Integer calibrationMonth,
            @ApiParam("目标部门ID") @RequestParam(required = false) Long targetDeptId,
            @ApiParam("校准类型") @RequestParam(required = false) String calibrationType
    ) {
        try {
            Map<String, Object> statistics = calibrationService.getCalibrationStatistics(
                    calibrationYear, calibrationQuarter, calibrationMonth, targetDeptId, calibrationType
            );
            return MyJsonBean.success(statistics);
        } catch (Exception e) {
            log.error("统计校准数据失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    /**
     * 统计校准状态分布
     */
    @GetMapping("/statistics/status-distribution")
    @ApiOperation("统计校准状态分布")
    public MyJsonBean getCalibrationStatusDistribution(
            @ApiParam("校准年度") @RequestParam Integer calibrationYear,
            @ApiParam("目标部门ID") @RequestParam(required = false) Long targetDeptId
    ) {
        try {
            List<Map<String, Object>> distribution = calibrationService.getCalibrationStatusDistribution(calibrationYear, targetDeptId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计校准状态分布失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    /**
     * 统计校准类型分布
     */
    @GetMapping("/statistics/type-distribution")
    @ApiOperation("统计校准类型分布")
    public MyJsonBean getCalibrationTypeDistribution(
            @ApiParam("校准年度") @RequestParam Integer calibrationYear,
            @ApiParam("目标部门ID") @RequestParam(required = false) Long targetDeptId
    ) {
        try {
            List<Map<String, Object>> distribution = calibrationService.getCalibrationTypeDistribution(calibrationYear, targetDeptId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计校准类型分布失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    /**
     * 统计校准完成趋势
     */
    @GetMapping("/statistics/completion-trend")
    @ApiOperation("统计校准完成趋势")
    public MyJsonBean getCalibrationCompletionTrend(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("目标部门ID") @RequestParam(required = false) Long targetDeptId
    ) {
        try {
            List<Map<String, Object>> trend = calibrationService.getCalibrationCompletionTrend(startTime, endTime, targetDeptId);
            return MyJsonBean.success(trend);
        } catch (Exception e) {
            log.error("统计校准完成趋势失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    /**
     * 统计校准效果分布
     */
    @GetMapping("/statistics/effectiveness-distribution")
    @ApiOperation("统计校准效果分布")
    public MyJsonBean getEffectivenessDistribution(
            @ApiParam("校准年度") @RequestParam Integer calibrationYear,
            @ApiParam("目标部门ID") @RequestParam(required = false) Long targetDeptId
    ) {
        try {
            List<Map<String, Object>> distribution = calibrationService.getEffectivenessDistribution(calibrationYear, targetDeptId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计校准效果分布失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    /**
     * 查询校准排行榜
     */
    @GetMapping("/ranking")
    @ApiOperation("查询校准排行榜")
    public MyJsonBean getCalibrationRanking(
            @ApiParam("校准年度") @RequestParam Integer calibrationYear,
            @ApiParam("排行类型") @RequestParam String rankType,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit
    ) {
        try {
            List<Map<String, Object>> ranking = calibrationService.getCalibrationRanking(calibrationYear, rankType, limit);
            return MyJsonBean.success(ranking);
        } catch (Exception e) {
            log.error("查询校准排行榜失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    // ==================== 辅助功能 ====================

    /**
     * 检查校准时间冲突
     */
    @GetMapping("/check-conflict")
    @ApiOperation("检查校准时间冲突")
    public MyJsonBean checkTimeConflict(
            @ApiParam("校准负责人ID") @RequestParam Long calibrationOwnerId,
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("排除的校准ID") @RequestParam(required = false) Long excludeId
    ) {
        try {
            boolean hasConflict = calibrationService.checkTimeConflict(calibrationOwnerId, startTime, endTime, excludeId);
            return MyJsonBean.success(hasConflict);
        } catch (Exception e) {
            log.error("检查校准时间冲突失败", e);
            return MyJsonBean.error("检查失败: " + e.getMessage());
        }
    }

    /**
     * 查询可用校准负责人
     */
    @GetMapping("/available-owners")
    @ApiOperation("查询可用校准负责人")
    public MyJsonBean getAvailableOwners(
            @ApiParam("目标部门ID") @RequestParam(required = false) Long targetDeptId,
            @ApiParam("校准类型") @RequestParam(required = false) String calibrationType,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit
    ) {
        try {
            List<Map<String, Object>> owners = calibrationService.getAvailableOwners(targetDeptId, calibrationType, limit);
            return MyJsonBean.success(owners);
        } catch (Exception e) {
            log.error("查询可用校准负责人失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询校准提醒列表
     */
    @GetMapping("/reminders")
    @ApiOperation("查询校准提醒列表")
    public MyJsonBean getCalibrationReminders(
            @ApiParam("提醒时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime reminderTime,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit
    ) {
        try {
            List<PmPerformanceCalibration> reminders = calibrationService.getCalibrationReminders(reminderTime, limit);
            return MyJsonBean.success(reminders);
        } catch (Exception e) {
            log.error("查询校准提醒列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 智能推荐校准时间
     */
    @GetMapping("/recommend-times")
    @ApiOperation("智能推荐校准时间")
    public MyJsonBean recommendCalibrationTimes(
            @ApiParam("校准负责人ID") @RequestParam Long calibrationOwnerId,
            @ApiParam("参与者ID列表") @RequestParam List<Long> participantIds,
            @ApiParam("校准时长(分钟)") @RequestParam(required = false) Integer duration,
            @ApiParam("偏好日期") @RequestParam(required = false) List<String> preferredDates
    ) {
        try {
            List<Map<String, Object>> recommendations = calibrationService.recommendCalibrationTimes(
                    calibrationOwnerId, participantIds, duration, preferredDates
            );
            return MyJsonBean.success(recommendations);
        } catch (Exception e) {
            log.error("智能推荐校准时间失败", e);
            return MyJsonBean.error("推荐失败: " + e.getMessage());
        }
    }

    // ==================== 高级功能 ====================

    /**
     * 生成校准报告
     */
    @PostMapping("/{id}/report")
    @ApiOperation("生成校准报告")
    public MyJsonBean generateCalibrationReport(
            @ApiParam("校准ID") @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> reportParams
    ) {
        try {
            Map<String, Object> result = calibrationService.generateCalibrationReport(id, reportParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("生成校准报告失败", e);
            return MyJsonBean.error("生成报告失败: " + e.getMessage());
        }
    }

    /**
     * 导出校准数据
     */
    @PostMapping("/export")
    @ApiOperation("导出校准数据")
    public MyJsonBean exportCalibrationData(@RequestBody Map<String, Object> exportParams) {
        try {
            Map<String, Object> result = calibrationService.exportCalibrationData(exportParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导出校准数据失败", e);
            return MyJsonBean.error("导出失败: " + e.getMessage());
        }
    }

    /**
     * 导入校准数据
     */
    @PostMapping("/import")
    @ApiOperation("导入校准数据")
    public MyJsonBean importCalibrationData(
            @RequestBody List<Map<String, Object>> importData,
            @RequestParam(required = false) Map<String, Object> importParams
    ) {
        try {
            Map<String, Object> result = calibrationService.importCalibrationData(importData, importParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导入校准数据失败", e);
            return MyJsonBean.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 复制校准
     */
    @PostMapping("/{id}/copy")
    @ApiOperation("复制校准")
    public MyJsonBean copyCalibration(
            @ApiParam("校准ID") @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> copyParams
    ) {
        try {
            boolean result = calibrationService.copyCalibration(id, copyParams);
            if (result) {
                return MyJsonBean.success("复制成功");
            } else {
                return MyJsonBean.error("复制失败");
            }
        } catch (Exception e) {
            log.error("复制校准失败", e);
            return MyJsonBean.error("复制失败: " + e.getMessage());
        }
    }

    /**
     * 批量创建校准
     */
    @PostMapping("/batch/create")
    @ApiOperation("批量创建校准")
    public MyJsonBean batchCreateCalibrations(
            @RequestBody List<PmPerformanceCalibration> calibrations,
            @RequestParam(required = false) Map<String, Object> batchParams
    ) {
        try {
            Map<String, Object> result = calibrationService.batchCreateCalibrations(calibrations, batchParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量创建校准失败", e);
            return MyJsonBean.error("批量创建失败: " + e.getMessage());
        }
    }

    /**
     * 发送校准通知
     */
    @PostMapping("/{id}/notification")
    @ApiOperation("发送校准通知")
    public MyJsonBean sendCalibrationNotification(
            @ApiParam("校准ID") @PathVariable Long id,
            @RequestBody Map<String, Object> notificationData
    ) {
        try {
            Map<String, Object> result = calibrationService.sendCalibrationNotification(id, notificationData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("发送校准通知失败", e);
            return MyJsonBean.error("发送通知失败: " + e.getMessage());
        }
    }

    /**
     * 批量发送通知
     */
    @PostMapping("/batch/notification")
    @ApiOperation("批量发送通知")
    public MyJsonBean batchSendNotifications(
            @RequestParam List<Long> calibrationIds,
            @RequestBody Map<String, Object> notificationData
    ) {
        try {
            Map<String, Object> result = calibrationService.batchSendNotifications(calibrationIds, notificationData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量发送通知失败", e);
            return MyJsonBean.error("批量发送通知失败: " + e.getMessage());
        }
    }

    /**
     * 校准流程控制
     */
    @PostMapping("/{id}/control")
    @ApiOperation("校准流程控制")
    public MyJsonBean controlCalibrationProcess(
            @ApiParam("校准ID") @PathVariable Long id,
            @ApiParam("操作类型") @RequestParam String action,
            @RequestBody(required = false) Map<String, Object> controlData
    ) {
        try {
            Map<String, Object> result = calibrationService.controlCalibrationProcess(id, action, controlData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("校准流程控制失败", e);
            return MyJsonBean.error("流程控制失败: " + e.getMessage());
        }
    }

    /**
     * 智能校准分析
     */
    @PostMapping("/{id}/intelligent-analysis")
    @ApiOperation("智能校准分析")
    public MyJsonBean intelligentCalibrationAnalysis(
            @ApiParam("校准ID") @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> analysisParams
    ) {
        try {
            Map<String, Object> result = calibrationService.intelligentCalibrationAnalysis(id, analysisParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("智能校准分析失败", e);
            return MyJsonBean.error("智能分析失败: " + e.getMessage());
        }
    }

    /**
     * 获取校准优化建议
     */
    @GetMapping("/{id}/optimization-suggestions")
    @ApiOperation("获取校准优化建议")
    public MyJsonBean getCalibrationOptimizationSuggestions(@ApiParam("校准ID") @PathVariable Long id) {
        try {
            Map<String, Object> result = calibrationService.getCalibrationOptimizationSuggestions(id);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取校准优化建议失败", e);
            return MyJsonBean.error("获取建议失败: " + e.getMessage());
        }
    }

    /**
     * 评估校准效果
     */
    @PostMapping("/{id}/effectiveness-evaluation")
    @ApiOperation("评估校准效果")
    public MyJsonBean evaluateCalibrationEffectiveness(
            @ApiParam("校准ID") @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> evaluationParams
    ) {
        try {
            Map<String, Object> result = calibrationService.evaluateCalibrationEffectiveness(id, evaluationParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("评估校准效果失败", e);
            return MyJsonBean.error("效果评估失败: " + e.getMessage());
        }
    }

    /**
     * 数据验证
     */
    @PostMapping("/validate")
    @ApiOperation("数据验证")
    public MyJsonBean validateCalibrationData(@RequestBody PmPerformanceCalibration calibration) {
        try {
            Map<String, Object> result = calibrationService.validateCalibrationData(calibration);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("数据验证失败", e);
            return MyJsonBean.error("验证失败: " + e.getMessage());
        }
    }
}
