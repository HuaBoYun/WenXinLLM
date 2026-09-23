package com.huabo.contract.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.ProjectSettlement;
import com.huabo.contract.entity.SettlementReminder;
import com.huabo.contract.mapper.ProjectSettlementMapper.ProjectSettlementStatistics;
import com.huabo.contract.service.ProjectSettlementService;
import com.huabo.contract.vo.ProjectSettlementQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目结算管理控制器
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@RestController
@RequestMapping("/settlement")
@Tag(name="项目结算管理",description="项目结算管理")
@RequiredArgsConstructor
@Validated
public class ProjectSettlementController {

    private final ProjectSettlementService projectSettlementService;
    private final UserProvider userProvider;

    @PostMapping("/create")
    @Operation(summary = "创建项目结算", description = "新增项目结算记录")
    public String createSettlement(@Valid @RequestBody ProjectSettlement settlement) {
        try {
            ProjectSettlement result = projectSettlementService.createSettlement(settlement);
            return JsonBean.success("项目结算创建成功", result);
        } catch (Exception e) {
            log.error("创建项目结算失败", e);
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询项目结算", description = "分页查询项目结算列表")
    public String getSettlementPage(@Valid @RequestBody ProjectSettlementQueryParam queryParam) {
        try {
            IPage<ProjectSettlement> pageInfo = projectSettlementService.getSettlementPage(queryParam);
            return JsonBean.success(pageInfo, pageInfo.getRecords());
        } catch (Exception e) {
            log.error("查询项目结算列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @Operation(summary = "更新项目结算", description = "更新项目结算信息")
    public String updateSettlement(@Valid @RequestBody ProjectSettlement settlement) {
        try {
            if (settlement.getId() == null) {
                return JsonBean.error("结算ID不能为空");
            }
            ProjectSettlement result = projectSettlementService.updateSettlement(settlement);
            return JsonBean.success("项目结算更新成功", result);
        } catch (Exception e) {
            log.error("更新项目结算失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "删除项目结算", description = "根据ID删除项目结算")
    public String deleteSettlement(@Parameter(description = "结算ID", required = true) @PathVariable Long id) {
        try {
            boolean success = projectSettlementService.deleteSettlement(id);
            if (success) {
                return JsonBean.success("项目结算删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除项目结算失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/detail/{id}")
    @Operation(summary = "查询结算详情", description = "根据ID查询结算详细信息")
    public String getSettlementDetail(@Parameter(description = "结算ID", required = true) @PathVariable Long id) {
        try {
            ProjectSettlement settlement = projectSettlementService.getSettlementById(id);
            if (settlement != null) {
                return JsonBean.success("查询成功", settlement);
            } else {
                return JsonBean.error("结算记录不存在");
            }
        } catch (Exception e) {
            log.error("查询结算详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/review/{id}")
    @Operation(summary = "结算审核", description = "审核项目结算")
    public String reviewSettlement(
            @Parameter(description = "结算ID", required = true) @PathVariable Long id,
            @RequestBody ReviewRequest request) {
        try {
            // 审核操作

            boolean success = projectSettlementService.reviewSettlement(
                    id, request.getReviewComments(), 1L); // 默认用户ID
            
            if (success) {
                return JsonBean.success("结算审核成功");
            } else {
                return JsonBean.error("审核失败");
            }
        } catch (Exception e) {
            log.error("结算审核失败", e);
            return JsonBean.error("审核失败: " + e.getMessage());
        }
    }

    @PostMapping("/statistics/{projectId}")
    @Operation(summary = "获取结算统计", description = "获取项目结算统计信息")
    public String getSettlementStatistics(@Parameter(description = "项目ID", required = true) @PathVariable Long projectId) {
        try {
            ProjectSettlementStatistics statistics = projectSettlementService.getSettlementStatistics(projectId);
            return JsonBean.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取结算统计失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/pending-review")
    @Operation(summary = "获取待审核结算", description = "获取待审核的结算记录列表")
    public String getPendingReviewSettlements() {
        try {
            List<ProjectSettlement> settlements = projectSettlementService.getPendingReviewSettlements();
            return JsonBean.success("查询成功", settlements);
        } catch (Exception e) {
            log.error("获取待审核结算失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-review")
    @Operation(summary = "批量审核结算", description = "批量审核多个结算记录")
    public String batchReviewSettlements(@RequestBody BatchReviewRequest request) {
        try {
            // 批量审核操作

            boolean success = projectSettlementService.batchReviewSettlements(
                    request.getIds(), request.getReviewComments(), 1L); // 默认用户ID
            
            if (success) {
                return JsonBean.success("批量审核成功");
            } else {
                return JsonBean.error("部分审核失败，请检查日志");
            }
        } catch (Exception e) {
            log.error("批量审核结算失败", e);
            return JsonBean.error("批量审核失败: " + e.getMessage());
        }
    }

    @PostMapping("/generate-no")
    @Operation(summary = "生成结算编号", description = "自动生成结算编号")
    public String generateSettlementNo() {
        try {
            String settlementNo = projectSettlementService.generateSettlementNo();
            return JsonBean.success("生成成功", settlementNo);
        } catch (Exception e) {
            log.error("生成结算编号失败", e);
            return JsonBean.error("生成失败: " + e.getMessage());
        }
    }

    /**
     * 审核请求参数
     */
    public static class ReviewRequest {
        private String reviewComments;

        public String getReviewComments() { return reviewComments; }
        public void setReviewComments(String reviewComments) { this.reviewComments = reviewComments; }
    }

    // ==================== 结算提醒相关接口 ====================

    @PostMapping("/reminder/list")
    @Operation(summary = "获取结算提醒列表", description = "分页查询结算提醒列表")
    public String getSettlementReminderList(@RequestBody ProjectSettlementQueryParam queryParam) {
        try {
            log.info("获取结算提醒列表，参数: {}", queryParam);

            // TODO: 实现真实的结算提醒查询逻辑
            // 这里先返回模拟数据
            List<SettlementReminder> reminderList = new ArrayList<>();

            // 添加模拟数据
            Calendar cal = Calendar.getInstance();

            SettlementReminder reminder1 = new SettlementReminder();
            reminder1.setId(1L);
            reminder1.setProjectId(queryParam.getProjectId());
            reminder1.setSettlementProject("办公楼建设项目第一期结算");
            reminder1.setContractNumber("HT2025001");
            reminder1.setContractAmount(new BigDecimal("3000000.00"));
            reminder1.setSettledAmount(new BigDecimal("1500000.00"));
            reminder1.setPendingAmount(new BigDecimal("1500000.00"));
            reminder1.setReminderType((short) 1);
            reminder1.setReminderStatus((short) 1);
            reminder1.setPriority((short) 2);
            cal.set(2025, Calendar.JANUARY, 25);
            reminder1.setPlannedSettlementDate(cal.getTime());
            reminder1.setResponsiblePersonId(1L);
            reminder1.setResponsiblePersonName("张三");
            reminder1.setReminderContent("请及时处理第一期结算事宜");
            cal.add(Calendar.DAY_OF_MONTH, -5);
            reminder1.setCreateTime(cal.getTime());
            reminder1.setUpdateTime(new Date());
            reminderList.add(reminder1);

            SettlementReminder reminder2 = new SettlementReminder();
            reminder2.setId(2L);
            reminder2.setProjectId(queryParam.getProjectId());
            reminder2.setSettlementProject("道路改造工程完工结算");
            reminder2.setContractNumber("HT2025002");
            reminder2.setContractAmount(new BigDecimal("5000000.00"));
            reminder2.setSettledAmount(new BigDecimal("2800000.00"));
            reminder2.setPendingAmount(new BigDecimal("2200000.00"));
            reminder2.setReminderType((short) 2);
            reminder2.setReminderStatus((short) 1);
            reminder2.setPriority((short) 1);
            cal.set(2025, Calendar.FEBRUARY, 10);
            reminder2.setPlannedSettlementDate(cal.getTime());
            reminder2.setResponsiblePersonId(2L);
            reminder2.setResponsiblePersonName("李四");
            reminder2.setReminderContent("道路改造工程即将完工，请准备完工结算");
            cal.add(Calendar.DAY_OF_MONTH, -3);
            reminder2.setCreateTime(cal.getTime());
            reminder2.setUpdateTime(new Date());
            reminderList.add(reminder2);

            SettlementReminder reminder3 = new SettlementReminder();
            reminder3.setId(3L);
            reminder3.setProjectId(queryParam.getProjectId());
            reminder3.setSettlementProject("桥梁工程专项结算");
            reminder3.setContractNumber("HT2025003");
            reminder3.setContractAmount(new BigDecimal("8000000.00"));
            reminder3.setSettledAmount(new BigDecimal("6000000.00"));
            reminder3.setPendingAmount(new BigDecimal("2000000.00"));
            reminder3.setReminderType((short) 1);
            reminder3.setReminderStatus((short) 3);
            reminder3.setPriority((short) 3);
            cal.set(2025, Calendar.JANUARY, 15);
            reminder3.setPlannedSettlementDate(cal.getTime());
            reminder3.setResponsiblePersonId(3L);
            reminder3.setResponsiblePersonName("王五");
            reminder3.setReminderContent("桥梁工程专项结算已过期，请尽快处理");
            cal.add(Calendar.DAY_OF_MONTH, -10);
            reminder3.setCreateTime(cal.getTime());
            reminder3.setUpdateTime(new Date());
            reminderList.add(reminder3);

            return JsonBean.success("查询成功", reminderList);
        } catch (Exception e) {
            log.error("查询结算提醒列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/reminder/create")
    @Operation(summary = "创建结算提醒", description = "新增结算提醒记录")
    public String createSettlementReminder(@Valid @RequestBody SettlementReminder reminder) {
        try {
            // 模拟创建结算提醒
            return JsonBean.success("结算提醒创建成功");
        } catch (Exception e) {
            log.error("创建结算提醒失败", e);
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/reminder/update")
    @Operation(summary = "更新结算提醒", description = "更新结算提醒信息")
    public String updateSettlementReminder(@Valid @RequestBody SettlementReminder reminder) {
        try {
            // 模拟更新结算提醒
            return JsonBean.success("结算提醒更新成功");
        } catch (Exception e) {
            log.error("更新结算提醒失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/reminder/delete/{id}")
    @Operation(summary = "删除结算提醒", description = "根据ID删除结算提醒")
    public String deleteSettlementReminder(@Parameter(description = "提醒ID", required = true) @PathVariable Long id) {
        try {
            // 模拟删除结算提醒
            return JsonBean.success("结算提醒删除成功");
        } catch (Exception e) {
            log.error("删除结算提醒失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/reminder/process/{id}")
    @Operation(summary = "处理结算提醒", description = "处理指定的结算提醒")
    public String processSettlementReminder(
            @Parameter(description = "提醒ID", required = true) @PathVariable Long id,
            @RequestBody ProcessReminderRequest request) {
        try {
            // 模拟处理结算提醒
            return JsonBean.success("结算提醒处理成功");
        } catch (Exception e) {
            log.error("处理结算提醒失败", e);
            return JsonBean.error("处理失败: " + e.getMessage());
        }
    }

    @PostMapping("/reminder/batch-process")
    @Operation(summary = "批量处理结算提醒", description = "批量处理多个结算提醒")
    public String batchProcessReminders(@RequestBody BatchProcessReminderRequest request) {
        try {
            // 模拟批量处理结算提醒
            return JsonBean.success("批量处理成功");
        } catch (Exception e) {
            log.error("批量处理结算提醒失败", e);
            return JsonBean.error("批量处理失败: " + e.getMessage());
        }
    }

    @PostMapping("/reminder/send-notification")
    @Operation(summary = "发送结算提醒通知", description = "发送结算提醒通知给相关人员")
    public String sendSettlementNotification(@RequestBody SendNotificationRequest request) {
        try {
            // 模拟发送通知
            return JsonBean.success("通知发送成功");
        } catch (Exception e) {
            log.error("发送通知失败", e);
            return JsonBean.error("发送失败: " + e.getMessage());
        }
    }

    // ==================== 内部类定义 ====================

    /**
     * 处理提醒请求参数
     */
    public static class ProcessReminderRequest {
        private String processNotes;
        private Short status;

        public String getProcessNotes() { return processNotes; }
        public void setProcessNotes(String processNotes) { this.processNotes = processNotes; }

        public Short getStatus() { return status; }
        public void setStatus(Short status) { this.status = status; }
    }

    /**
     * 批量处理提醒请求参数
     */
    public static class BatchProcessReminderRequest {
        private List<Long> ids;
        private String processNotes;
        private Short status;

        public List<Long> getIds() { return ids; }
        public void setIds(List<Long> ids) { this.ids = ids; }

        public String getProcessNotes() { return processNotes; }
        public void setProcessNotes(String processNotes) { this.processNotes = processNotes; }

        public Short getStatus() { return status; }
        public void setStatus(Short status) { this.status = status; }
    }

    /**
     * 发送通知请求参数
     */
    public static class SendNotificationRequest {
        private List<Long> reminderIds;
        private String notificationContent;
        private List<Long> recipientIds;

        public List<Long> getReminderIds() { return reminderIds; }
        public void setReminderIds(List<Long> reminderIds) { this.reminderIds = reminderIds; }

        public String getNotificationContent() { return notificationContent; }
        public void setNotificationContent(String notificationContent) { this.notificationContent = notificationContent; }

        public List<Long> getRecipientIds() { return recipientIds; }
        public void setRecipientIds(List<Long> recipientIds) { this.recipientIds = recipientIds; }
    }

    /**
     * 批量审核请求参数
     */
    public static class BatchReviewRequest {
        private List<Long> ids;
        private String reviewComments;

        public List<Long> getIds() { return ids; }
        public void setIds(List<Long> ids) { this.ids = ids; }
        
        public String getReviewComments() { return reviewComments; }
        public void setReviewComments(String reviewComments) { this.reviewComments = reviewComments; }
    }
}
