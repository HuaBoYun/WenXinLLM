package com.management.accountant.controller.pm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.pm.PmIncentiveManagement;
import com.management.accountant.service.pm.PmIncentiveManagementService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 激励管理控制器
 *
 * @author AI Assistant
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/accountant/pm/incentive-management")
@Api(tags = "激励管理")
public class PmIncentiveManagementController {

    @Autowired
    private PmIncentiveManagementService incentiveManagementService;

    @GetMapping("/page")
    @ApiOperation("分页查询激励管理列表")
    public MyJsonBean getIncentiveManagementPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("激励标题") @RequestParam(required = false) String incentiveTitle,
            @ApiParam("激励类型") @RequestParam(required = false) String incentiveType,
            @ApiParam("激励状态") @RequestParam(required = false) String incentiveStatus,
            @ApiParam("激励年度") @RequestParam(required = false) Integer incentiveYear,
            @ApiParam("目标部门ID") @RequestParam(required = false) Long targetDeptId,
            @ApiParam("负责人ID") @RequestParam(required = false) Long incentiveOwnerId,
            @ApiParam("开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            Page<PmIncentiveManagement> page = new Page<>(current, size);
            IPage<PmIncentiveManagement> result = incentiveManagementService.getIncentiveManagementPage(
                    page, incentiveTitle, incentiveType, incentiveStatus, incentiveYear, 
                    targetDeptId, incentiveOwnerId, startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("分页查询激励管理列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询激励管理详情")
    public MyJsonBean getIncentiveManagementById(@ApiParam("激励ID") @PathVariable Long id,
                                                  @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            PmIncentiveManagement incentiveManagement = incentiveManagementService.getById(id);
            if (incentiveManagement == null || (tenantId != null && !incentiveManagement.getTenantId().equals(tenantId))) {
                return MyJsonBean.error("激励方案不存在");
            }
            return MyJsonBean.success(incentiveManagement);
        } catch (Exception e) {
            log.error("查询激励管理详情失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/code/{incentiveCode}")
    @ApiOperation("根据激励编码查询")
    public MyJsonBean getByIncentiveCode(@ApiParam("激励编码") @PathVariable String incentiveCode,
                                         @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            PmIncentiveManagement incentiveManagement = incentiveManagementService.getByIncentiveCode(incentiveCode, tenantId);
            return MyJsonBean.success(incentiveManagement);
        } catch (Exception e) {
            log.error("根据激励编码查询失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/type/{incentiveType}")
    @ApiOperation("根据激励类型查询列表")
    public MyJsonBean getByIncentiveType(@ApiParam("激励类型") @PathVariable String incentiveType,
                                         @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<PmIncentiveManagement> list = incentiveManagementService.getByIncentiveType(incentiveType, tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据激励类型查询失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/status/{incentiveStatus}")
    @ApiOperation("根据激励状态查询列表")
    public MyJsonBean getByIncentiveStatus(@ApiParam("激励状态") @PathVariable String incentiveStatus,
                                           @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<PmIncentiveManagement> list = incentiveManagementService.getByIncentiveStatus(incentiveStatus, tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据激励状态查询失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/dept/{targetDeptId}")
    @ApiOperation("根据部门ID查询激励列表")
    public MyJsonBean getByDeptId(@ApiParam("部门ID") @PathVariable Long targetDeptId,
                                  @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<PmIncentiveManagement> list = incentiveManagementService.getByDeptId(targetDeptId, tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据部门ID查询激励列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/owner/{incentiveOwnerId}")
    @ApiOperation("根据负责人ID查询激励列表")
    public MyJsonBean getByOwnerId(@ApiParam("负责人ID") @PathVariable Long incentiveOwnerId,
                                   @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<PmIncentiveManagement> list = incentiveManagementService.getByOwnerId(incentiveOwnerId, tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据负责人ID查询激励列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/year/{incentiveYear}")
    @ApiOperation("根据年度查询激励列表")
    public MyJsonBean getByYear(@ApiParam("激励年度") @PathVariable Integer incentiveYear,
                                @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<PmIncentiveManagement> list = incentiveManagementService.getByYear(incentiveYear, tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据年度查询激励列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/quarter/{incentiveYear}/{incentiveQuarter}")
    @ApiOperation("根据季度查询激励列表")
    public MyJsonBean getByQuarter(@ApiParam("激励年度") @PathVariable Integer incentiveYear,
                                   @ApiParam("激励季度") @PathVariable Integer incentiveQuarter,
                                   @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<PmIncentiveManagement> list = incentiveManagementService.getByQuarter(incentiveYear, incentiveQuarter, tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据季度查询激励列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/month/{incentiveYear}/{incentiveMonth}")
    @ApiOperation("根据月份查询激励列表")
    public MyJsonBean getByMonth(@ApiParam("激励年度") @PathVariable Integer incentiveYear,
                                 @ApiParam("激励月份") @PathVariable Integer incentiveMonth,
                                 @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<PmIncentiveManagement> list = incentiveManagementService.getByMonth(incentiveYear, incentiveMonth, tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("根据月份查询激励列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation("创建激励方案")
    public MyJsonBean createIncentiveScheme(@ApiParam("激励管理对象") @RequestBody PmIncentiveManagement incentiveManagement) {
        try {
            boolean result = incentiveManagementService.createIncentiveScheme(incentiveManagement);
            return result ? MyJsonBean.success("创建成功") : MyJsonBean.error("创建失败");
        } catch (Exception e) {
            log.error("创建激励方案失败", e);
            return MyJsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation("更新激励方案")
    public MyJsonBean updateIncentiveScheme(@ApiParam("激励管理对象") @RequestBody PmIncentiveManagement incentiveManagement) {
        try {
            boolean result = incentiveManagementService.updateIncentiveScheme(incentiveManagement);
            return result ? MyJsonBean.success("更新成功") : MyJsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新激励方案失败", e);
            return MyJsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除激励方案")
    public MyJsonBean deleteIncentiveScheme(@ApiParam("激励ID") @PathVariable Long id,
                                            @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.deleteIncentiveScheme(id, tenantId);
            return result ? MyJsonBean.success("删除成功") : MyJsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除激励方案失败", e);
            return MyJsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除激励方案")
    public MyJsonBean batchDeleteIncentiveSchemes(@ApiParam("激励ID列表") @RequestBody List<Long> incentiveIds,
                                                  @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.batchDeleteIncentiveSchemes(incentiveIds, tenantId);
            return result ? MyJsonBean.success("批量删除成功") : MyJsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除激励方案失败", e);
            return MyJsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/start")
    @ApiOperation("启动激励方案")
    public MyJsonBean startIncentiveScheme(@ApiParam("激励ID") @PathVariable Long id,
                                           @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.startIncentiveScheme(id, tenantId);
            return result ? MyJsonBean.success("启动成功") : MyJsonBean.error("启动失败");
        } catch (Exception e) {
            log.error("启动激励方案失败", e);
            return MyJsonBean.error("启动失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/suspend")
    @ApiOperation("暂停激励方案")
    public MyJsonBean suspendIncentiveScheme(@ApiParam("激励ID") @PathVariable Long id,
                                             @ApiParam("暂停原因") @RequestParam String reason,
                                             @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.suspendIncentiveScheme(id, reason, tenantId);
            return result ? MyJsonBean.success("暂停成功") : MyJsonBean.error("暂停失败");
        } catch (Exception e) {
            log.error("暂停激励方案失败", e);
            return MyJsonBean.error("暂停失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/complete")
    @ApiOperation("完成激励方案")
    public MyJsonBean completeIncentiveScheme(@ApiParam("激励ID") @PathVariable Long id,
                                              @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.completeIncentiveScheme(id, tenantId);
            return result ? MyJsonBean.success("完成成功") : MyJsonBean.error("完成失败");
        } catch (Exception e) {
            log.error("完成激励方案失败", e);
            return MyJsonBean.error("完成失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/cancel")
    @ApiOperation("取消激励方案")
    public MyJsonBean cancelIncentiveScheme(@ApiParam("激励ID") @PathVariable Long id,
                                            @ApiParam("取消原因") @RequestParam String reason,
                                            @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.cancelIncentiveScheme(id, reason, tenantId);
            return result ? MyJsonBean.success("取消成功") : MyJsonBean.error("取消失败");
        } catch (Exception e) {
            log.error("取消激励方案失败", e);
            return MyJsonBean.error("取消失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/approve")
    @ApiOperation("审批激励方案")
    public MyJsonBean approveIncentiveScheme(@ApiParam("激励ID") @PathVariable Long id,
                                             @ApiParam("审批意见") @RequestParam(required = false) String approvalComments,
                                             @ApiParam("审批人ID") @RequestParam Long approverId,
                                             @ApiParam("审批人姓名") @RequestParam String approverName,
                                             @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.approveIncentiveScheme(id, approvalComments, approverId, approverName, tenantId);
            return result ? MyJsonBean.success("审批成功") : MyJsonBean.error("审批失败");
        } catch (Exception e) {
            log.error("审批激励方案失败", e);
            return MyJsonBean.error("审批失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/reject")
    @ApiOperation("拒绝激励方案")
    public MyJsonBean rejectIncentiveScheme(@ApiParam("激励ID") @PathVariable Long id,
                                            @ApiParam("审批意见") @RequestParam String approvalComments,
                                            @ApiParam("审批人ID") @RequestParam Long approverId,
                                            @ApiParam("审批人姓名") @RequestParam String approverName,
                                            @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.rejectIncentiveScheme(id, approvalComments, approverId, approverName, tenantId);
            return result ? MyJsonBean.success("拒绝成功") : MyJsonBean.error("拒绝失败");
        } catch (Exception e) {
            log.error("拒绝激励方案失败", e);
            return MyJsonBean.error("拒绝失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/distribute")
    @ApiOperation("发放激励")
    public MyJsonBean distributeIncentive(@ApiParam("激励ID") @PathVariable Long id,
                                          @ApiParam("发放方式") @RequestParam String distributionMethod,
                                          @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.distributeIncentive(id, distributionMethod, tenantId);
            return result ? MyJsonBean.success("发放成功") : MyJsonBean.error("发放失败");
        } catch (Exception e) {
            log.error("发放激励失败", e);
            return MyJsonBean.error("发放失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-distribute")
    @ApiOperation("批量发放激励")
    public MyJsonBean batchDistributeIncentives(@ApiParam("激励ID列表") @RequestBody List<Long> incentiveIds,
                                                @ApiParam("发放方式") @RequestParam String distributionMethod,
                                                @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.batchDistributeIncentives(incentiveIds, distributionMethod, tenantId);
            return result ? MyJsonBean.success("批量发放成功") : MyJsonBean.error("批量发放失败");
        } catch (Exception e) {
            log.error("批量发放激励失败", e);
            return MyJsonBean.error("批量发放失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/calculate-amount")
    @ApiOperation("计算激励金额")
    public MyJsonBean calculateIncentiveAmount(@ApiParam("激励ID") @PathVariable Long id,
                                               @ApiParam("计算参数") @RequestBody Map<String, Object> parameters,
                                               @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            BigDecimal amount = incentiveManagementService.calculateIncentiveAmount(id, parameters, tenantId);
            return MyJsonBean.success(amount);
        } catch (Exception e) {
            log.error("计算激励金额失败", e);
            return MyJsonBean.error("计算失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-calculate-amounts")
    @ApiOperation("批量计算激励金额")
    public MyJsonBean batchCalculateIncentiveAmounts(@ApiParam("激励ID列表") @RequestBody List<Long> incentiveIds,
                                                     @ApiParam("计算参数") @RequestParam Map<String, Object> parameters,
                                                     @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            Map<Long, BigDecimal> amounts = incentiveManagementService.batchCalculateIncentiveAmounts(incentiveIds, parameters, tenantId);
            return MyJsonBean.success(amounts);
        } catch (Exception e) {
            log.error("批量计算激励金额失败", e);
            return MyJsonBean.error("批量计算失败: " + e.getMessage());
        }
    }

    @GetMapping("/pending-approval")
    @ApiOperation("查询待审批的激励列表")
    public MyJsonBean getPendingApproval(@ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<PmIncentiveManagement> list = incentiveManagementService.getPendingApproval(tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询待审批激励列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/pending-distribution")
    @ApiOperation("查询待发放的激励列表")
    public MyJsonBean getPendingDistribution(@ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<PmIncentiveManagement> list = incentiveManagementService.getPendingDistribution(tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询待发放激励列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/need-follow-up")
    @ApiOperation("查询需要跟进的激励列表")
    public MyJsonBean getNeedFollowUp(@ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<PmIncentiveManagement> list = incentiveManagementService.getNeedFollowUp(tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询需要跟进的激励列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/upcoming-deadline")
    @ApiOperation("查询即将到期的激励列表")
    public MyJsonBean getUpcomingDeadline(@ApiParam("截止时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime deadline,
                                          @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<PmIncentiveManagement> list = incentiveManagementService.getUpcomingDeadline(deadline, tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询即将到期的激励列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/overdue")
    @ApiOperation("查询超期的激励列表")
    public MyJsonBean getOverdue(@ApiParam("当前时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime currentTime,
                                 @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            if (currentTime == null) {
                currentTime = LocalDateTime.now();
            }
            List<PmIncentiveManagement> list = incentiveManagementService.getOverdue(currentTime, tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询超期激励列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/add-follow-up")
    @ApiOperation("添加跟进记录")
    public MyJsonBean addFollowUpRecord(@ApiParam("激励ID") @PathVariable Long id,
                                        @ApiParam("跟进记录") @RequestParam String followUpRecord,
                                        @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.addFollowUpRecord(id, followUpRecord, tenantId);
            return result ? MyJsonBean.success("添加跟进记录成功") : MyJsonBean.error("添加跟进记录失败");
        } catch (Exception e) {
            log.error("添加跟进记录失败", e);
            return MyJsonBean.error("添加跟进记录失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/update-follow-up-status")
    @ApiOperation("更新跟进状态")
    public MyJsonBean updateFollowUpStatus(@ApiParam("激励ID") @PathVariable Long id,
                                           @ApiParam("跟进状态") @RequestParam String followUpStatus,
                                           @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.updateFollowUpStatus(id, followUpStatus, tenantId);
            return result ? MyJsonBean.success("更新跟进状态成功") : MyJsonBean.error("更新跟进状态失败");
        } catch (Exception e) {
            log.error("更新跟进状态失败", e);
            return MyJsonBean.error("更新跟进状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-update-follow-up-status")
    @ApiOperation("批量更新跟进状态")
    public MyJsonBean batchUpdateFollowUpStatus(@ApiParam("激励ID列表") @RequestBody List<Long> incentiveIds,
                                                @ApiParam("跟进状态") @RequestParam String followUpStatus,
                                                @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.batchUpdateFollowUpStatus(incentiveIds, followUpStatus, tenantId);
            return result ? MyJsonBean.success("批量更新跟进状态成功") : MyJsonBean.error("批量更新跟进状态失败");
        } catch (Exception e) {
            log.error("批量更新跟进状态失败", e);
            return MyJsonBean.error("批量更新跟进状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/send-reminder")
    @ApiOperation("发送提醒通知")
    public MyJsonBean sendReminderNotification(@ApiParam("激励ID") @PathVariable Long id,
                                               @ApiParam("通知类型") @RequestParam String notificationType,
                                               @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.sendReminderNotification(id, notificationType, tenantId);
            return result ? MyJsonBean.success("发送提醒通知成功") : MyJsonBean.error("发送提醒通知失败");
        } catch (Exception e) {
            log.error("发送提醒通知失败", e);
            return MyJsonBean.error("发送提醒通知失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-send-reminders")
    @ApiOperation("批量发送提醒通知")
    public MyJsonBean batchSendReminderNotifications(@ApiParam("激励ID列表") @RequestBody List<Long> incentiveIds,
                                                     @ApiParam("通知类型") @RequestParam String notificationType,
                                                     @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.batchSendReminderNotifications(incentiveIds, notificationType, tenantId);
            return result ? MyJsonBean.success("批量发送提醒通知成功") : MyJsonBean.error("批量发送提醒通知失败");
        } catch (Exception e) {
            log.error("批量发送提醒通知失败", e);
            return MyJsonBean.error("批量发送提醒通知失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("统计激励数据")
    public MyJsonBean getIncentiveStatistics(@ApiParam("激励年度") @RequestParam Integer incentiveYear,
                                              @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            Map<String, Object> statistics = incentiveManagementService.getIncentiveStatistics(incentiveYear, tenantId);
            return MyJsonBean.success(statistics);
        } catch (Exception e) {
            log.error("统计激励数据失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/status-distribution")
    @ApiOperation("统计激励状态分布")
    public MyJsonBean getIncentiveStatusDistribution(@ApiParam("激励年度") @RequestParam Integer incentiveYear,
                                                      @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<Map<String, Object>> distribution = incentiveManagementService.getIncentiveStatusDistribution(incentiveYear, tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计激励状态分布失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/type-distribution")
    @ApiOperation("统计激励类型分布")
    public MyJsonBean getIncentiveTypeDistribution(@ApiParam("激励年度") @RequestParam Integer incentiveYear,
                                                    @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<Map<String, Object>> distribution = incentiveManagementService.getIncentiveTypeDistribution(incentiveYear, tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计激励类型分布失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/completion-trend")
    @ApiOperation("统计激励完成趋势")
    public MyJsonBean getIncentiveCompletionTrend(@ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                                   @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                                   @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<Map<String, Object>> trend = incentiveManagementService.getIncentiveCompletionTrend(startTime, endTime, tenantId);
            return MyJsonBean.success(trend);
        } catch (Exception e) {
            log.error("统计激励完成趋势失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/amount-distribution")
    @ApiOperation("统计激励金额分布")
    public MyJsonBean getIncentiveAmountDistribution(@ApiParam("激励年度") @RequestParam Integer incentiveYear,
                                                      @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<Map<String, Object>> distribution = incentiveManagementService.getIncentiveAmountDistribution(incentiveYear, tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计激励金额分布失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/effectiveness-distribution")
    @ApiOperation("统计激励效果分布")
    public MyJsonBean getIncentiveEffectivenessDistribution(@ApiParam("激励年度") @RequestParam Integer incentiveYear,
                                                             @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<Map<String, Object>> distribution = incentiveManagementService.getIncentiveEffectivenessDistribution(incentiveYear, tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计激励效果分布失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/ranking")
    @ApiOperation("查询激励排行榜")
    public MyJsonBean getIncentiveRanking(@ApiParam("激励年度") @RequestParam Integer incentiveYear,
                                          @ApiParam("排行类型") @RequestParam String rankingType,
                                          @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit,
                                          @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<Map<String, Object>> ranking = incentiveManagementService.getIncentiveRanking(incentiveYear, rankingType, limit, tenantId);
            return MyJsonBean.success(ranking);
        } catch (Exception e) {
            log.error("查询激励排行榜失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/check-code-exists")
    @ApiOperation("检查激励编码是否存在")
    public MyJsonBean checkIncentiveCodeExists(@ApiParam("激励编码") @RequestParam String incentiveCode,
                                               @ApiParam("激励ID") @RequestParam(required = false) Long incentiveId,
                                               @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean exists = incentiveManagementService.checkIncentiveCodeExists(incentiveCode, incentiveId, tenantId);
            return MyJsonBean.success(exists);
        } catch (Exception e) {
            log.error("检查激励编码是否存在失败", e);
            return MyJsonBean.error("检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/check-time-conflict")
    @ApiOperation("检查时间冲突")
    public MyJsonBean checkTimeConflict(@ApiParam("激励ID") @RequestParam(required = false) Long incentiveId,
                                        @ApiParam("目标部门ID") @RequestParam Long targetDeptId,
                                        @ApiParam("激励类型") @RequestParam String incentiveType,
                                        @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                        @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                        @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean conflict = incentiveManagementService.checkTimeConflict(incentiveId, targetDeptId, incentiveType, startTime, endTime, tenantId);
            return MyJsonBean.success(conflict);
        } catch (Exception e) {
            log.error("检查时间冲突失败", e);
            return MyJsonBean.error("检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/total-amount")
    @ApiOperation("计算激励总金额")
    public MyJsonBean calculateTotalIncentiveAmount(@ApiParam("激励年度") @RequestParam Integer incentiveYear,
                                                     @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            BigDecimal totalAmount = incentiveManagementService.calculateTotalIncentiveAmount(incentiveYear, tenantId);
            return MyJsonBean.success(totalAmount);
        } catch (Exception e) {
            log.error("计算激励总金额失败", e);
            return MyJsonBean.error("计算失败: " + e.getMessage());
        }
    }

    @GetMapping("/dept-amount")
    @ApiOperation("计算部门激励金额")
    public MyJsonBean calculateDeptIncentiveAmount(@ApiParam("目标部门ID") @RequestParam Long targetDeptId,
                                                   @ApiParam("激励年度") @RequestParam Integer incentiveYear,
                                                   @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            BigDecimal deptAmount = incentiveManagementService.calculateDeptIncentiveAmount(targetDeptId, incentiveYear, tenantId);
            return MyJsonBean.success(deptAmount);
        } catch (Exception e) {
            log.error("计算部门激励金额失败", e);
            return MyJsonBean.error("计算失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/detail-with-relations")
    @ApiOperation("查询激励详情（包含关联信息）")
    public MyJsonBean getIncentiveDetailWithRelations(@ApiParam("激励ID") @PathVariable Long id,
                                                       @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            Map<String, Object> detail = incentiveManagementService.getIncentiveDetailWithRelations(id, tenantId);
            return MyJsonBean.success(detail);
        } catch (Exception e) {
            log.error("查询激励详情失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/history")
    @ApiOperation("查询激励历史记录")
    public MyJsonBean getIncentiveHistory(@ApiParam("激励ID") @PathVariable Long id,
                                          @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<Map<String, Object>> history = incentiveManagementService.getIncentiveHistory(id, tenantId);
            return MyJsonBean.success(history);
        } catch (Exception e) {
            log.error("查询激励历史记录失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    @ApiOperation("导出激励数据")
    public MyJsonBean exportIncentiveData(@ApiParam("激励年度") @RequestParam Integer incentiveYear,
                                          @ApiParam("激励类型") @RequestParam(required = false) String incentiveType,
                                          @ApiParam("激励状态") @RequestParam(required = false) String incentiveStatus,
                                          @ApiParam("目标部门ID") @RequestParam(required = false) Long targetDeptId,
                                          @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<Map<String, Object>> exportData = incentiveManagementService.exportIncentiveData(incentiveYear, incentiveType, incentiveStatus, targetDeptId, tenantId);
            return MyJsonBean.success(exportData);
        } catch (Exception e) {
            log.error("导出激励数据失败", e);
            return MyJsonBean.error("导出失败: " + e.getMessage());
        }
    }

    @GetMapping("/recommended-schemes")
    @ApiOperation("智能推荐激励方案")
    public MyJsonBean getRecommendedIncentiveSchemes(@ApiParam("目标部门ID") @RequestParam Long targetDeptId,
                                                      @ApiParam("激励类型") @RequestParam String incentiveType,
                                                      @ApiParam("预算范围") @RequestParam BigDecimal budgetRange,
                                                      @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<Map<String, Object>> schemes = incentiveManagementService.getRecommendedIncentiveSchemes(targetDeptId, incentiveType, budgetRange, tenantId);
            return MyJsonBean.success(schemes);
        } catch (Exception e) {
            log.error("智能推荐激励方案失败", e);
            return MyJsonBean.error("推荐失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/analyze-effectiveness")
    @ApiOperation("分析激励效果")
    public MyJsonBean analyzeIncentiveEffectiveness(@ApiParam("激励ID") @PathVariable Long id,
                                                     @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            Map<String, Object> analysis = incentiveManagementService.analyzeIncentiveEffectiveness(id, tenantId);
            return MyJsonBean.success(analysis);
        } catch (Exception e) {
            log.error("分析激励效果失败", e);
            return MyJsonBean.error("分析失败: " + e.getMessage());
        }
    }

    @GetMapping("/generate-report")
    @ApiOperation("生成激励报告数据")
    public MyJsonBean generateIncentiveReportData(@ApiParam("激励年度") @RequestParam Integer incentiveYear,
                                                   @ApiParam("报告类型") @RequestParam String reportType,
                                                   @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            Map<String, Object> reportData = incentiveManagementService.generateIncentiveReportData(incentiveYear, reportType, tenantId);
            return MyJsonBean.success(reportData);
        } catch (Exception e) {
            log.error("生成激励报告数据失败", e);
            return MyJsonBean.error("生成失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/optimization-suggestions")
    @ApiOperation("查询激励优化建议")
    public MyJsonBean getIncentiveOptimizationSuggestions(@ApiParam("激励ID") @PathVariable Long id,
                                                           @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            List<Map<String, Object>> suggestions = incentiveManagementService.getIncentiveOptimizationSuggestions(id, tenantId);
            return MyJsonBean.success(suggestions);
        } catch (Exception e) {
            log.error("查询激励优化建议失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/copy")
    @ApiOperation("复制激励方案")
    public MyJsonBean copyIncentiveScheme(@ApiParam("源激励ID") @PathVariable Long id,
                                          @ApiParam("新激励标题") @RequestParam String newIncentiveTitle,
                                          @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.copyIncentiveScheme(id, newIncentiveTitle, tenantId);
            return result ? MyJsonBean.success("复制成功") : MyJsonBean.error("复制失败");
        } catch (Exception e) {
            log.error("复制激励方案失败", e);
            return MyJsonBean.error("复制失败: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/save-as-template")
    @ApiOperation("保存为模板")
    public MyJsonBean saveAsTemplate(@ApiParam("激励ID") @PathVariable Long id,
                                     @ApiParam("模板名称") @RequestParam String templateName,
                                     @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.saveAsTemplate(id, templateName, tenantId);
            return result ? MyJsonBean.success("保存为模板成功") : MyJsonBean.error("保存为模板失败");
        } catch (Exception e) {
            log.error("保存为模板失败", e);
            return MyJsonBean.error("保存为模板失败: " + e.getMessage());
        }
    }

    @PostMapping("/create-from-template")
    @ApiOperation("从模板创建激励方案")
    public MyJsonBean createFromTemplate(@ApiParam("模板ID") @RequestParam Long templateId,
                                         @ApiParam("激励标题") @RequestParam String incentiveTitle,
                                         @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            boolean result = incentiveManagementService.createFromTemplate(templateId, incentiveTitle, tenantId);
            return result ? MyJsonBean.success("从模板创建成功") : MyJsonBean.error("从模板创建失败");
        } catch (Exception e) {
            log.error("从模板创建失败", e);
            return MyJsonBean.error("从模板创建失败: " + e.getMessage());
        }
    }

    @GetMapping("/analyze-trends")
    @ApiOperation("智能分析激励趋势")
    public MyJsonBean analyzeIncentiveTrends(@ApiParam("激励年度") @RequestParam Integer incentiveYear,
                                             @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            Map<String, Object> trends = incentiveManagementService.analyzeIncentiveTrends(incentiveYear, tenantId);
            return MyJsonBean.success(trends);
        } catch (Exception e) {
            log.error("智能分析激励趋势失败", e);
            return MyJsonBean.error("分析失败: " + e.getMessage());
        }
    }

    @GetMapping("/predict-needs")
    @ApiOperation("预测激励需求")
    public MyJsonBean predictIncentiveNeeds(@ApiParam("目标部门ID") @RequestParam Long targetDeptId,
                                            @ApiParam("目标年度") @RequestParam Integer targetYear,
                                            @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            Map<String, Object> prediction = incentiveManagementService.predictIncentiveNeeds(targetDeptId, targetYear, tenantId);
            return MyJsonBean.success(prediction);
        } catch (Exception e) {
            log.error("预测激励需求失败", e);
            return MyJsonBean.error("预测失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/optimize-configuration")
    @ApiOperation("优化激励配置")
    public MyJsonBean optimizeIncentiveConfiguration(@ApiParam("激励ID") @PathVariable Long id,
                                                      @ApiParam("租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            Map<String, Object> optimization = incentiveManagementService.optimizeIncentiveConfiguration(id, tenantId);
            return MyJsonBean.success(optimization);
        } catch (Exception e) {
            log.error("优化激励配置失败", e);
            return MyJsonBean.error("优化失败: " + e.getMessage());
        }
    }
}
