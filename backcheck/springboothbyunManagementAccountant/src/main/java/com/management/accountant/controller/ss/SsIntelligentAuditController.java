package com.management.accountant.controller.ss;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ss.SsIntelligentAudit;
import com.management.accountant.service.ss.SsIntelligentAuditService;
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
 * 智能审核控制器
 * 
 * @author AI Assistant
 * @since 2025-01-27
 */
@Slf4j
@RestController
@RequestMapping("/accountant/ss/intelligent-audit")
@Api(tags = "智能审核管理")
public class SsIntelligentAuditController {

    @Autowired
    private SsIntelligentAuditService intelligentAuditService;

    /**
     * 分页查询智能审核列表
     */
    @GetMapping("/page")
    @ApiOperation("分页查询智能审核列表")
    public MyJsonBean getAuditPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "20") Integer size,
            @ApiParam("审核标题") @RequestParam(required = false) String auditTitle,
            @ApiParam("审核类型") @RequestParam(required = false) String auditType,
            @ApiParam("审核状态") @RequestParam(required = false) String auditStatus,
            @ApiParam("风险等级") @RequestParam(required = false) String riskLevel,
            @ApiParam("审核人员ID") @RequestParam(required = false) Long auditorId,
            @ApiParam("审核部门ID") @RequestParam(required = false) Long auditDeptId,
            @ApiParam("开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Page<SsIntelligentAudit> page = new Page<>(current, size);
            IPage<SsIntelligentAudit> result = intelligentAuditService.getAuditPage(page, auditTitle, auditType, 
                                                                                   auditStatus, riskLevel, auditorId, 
                                                                                   auditDeptId, startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("分页查询智能审核列表失败", e);
            return MyJsonBean.error("分页查询智能审核列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询智能审核详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询智能审核详情")
    public MyJsonBean getAuditById(@ApiParam("审核ID") @PathVariable Long id,
                                   @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            SsIntelligentAudit audit = intelligentAuditService.getAuditById(id, tenantId);
            return MyJsonBean.success(audit);
        } catch (Exception e) {
            log.error("查询智能审核详情失败", e);
            return MyJsonBean.error("查询智能审核详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据审核编码查询
     */
    @GetMapping("/code/{auditCode}")
    @ApiOperation("根据审核编码查询")
    public MyJsonBean getByAuditCode(@ApiParam("审核编码") @PathVariable String auditCode,
                                     @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            SsIntelligentAudit audit = intelligentAuditService.getByAuditCode(auditCode, tenantId);
            return MyJsonBean.success(audit);
        } catch (Exception e) {
            log.error("根据审核编码查询失败", e);
            return MyJsonBean.error("根据审核编码查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据审核类型查询列表
     */
    @GetMapping("/type/{auditType}")
    @ApiOperation("根据审核类型查询列表")
    public MyJsonBean getByAuditType(@ApiParam("审核类型") @PathVariable String auditType,
                                     @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsIntelligentAudit> audits = intelligentAuditService.getByAuditType(auditType, tenantId);
            return MyJsonBean.success(audits);
        } catch (Exception e) {
            log.error("根据审核类型查询列表失败", e);
            return MyJsonBean.error("根据审核类型查询列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据审核状态查询列表
     */
    @GetMapping("/status/{auditStatus}")
    @ApiOperation("根据审核状态查询列表")
    public MyJsonBean getByAuditStatus(@ApiParam("审核状态") @PathVariable String auditStatus,
                                       @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsIntelligentAudit> audits = intelligentAuditService.getByAuditStatus(auditStatus, tenantId);
            return MyJsonBean.success(audits);
        } catch (Exception e) {
            log.error("根据审核状态查询列表失败", e);
            return MyJsonBean.error("根据审核状态查询列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据风险等级查询列表
     */
    @GetMapping("/risk/{riskLevel}")
    @ApiOperation("根据风险等级查询列表")
    public MyJsonBean getByRiskLevel(@ApiParam("风险等级") @PathVariable String riskLevel,
                                     @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsIntelligentAudit> audits = intelligentAuditService.getByRiskLevel(riskLevel, tenantId);
            return MyJsonBean.success(audits);
        } catch (Exception e) {
            log.error("根据风险等级查询列表失败", e);
            return MyJsonBean.error("根据风险等级查询列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据审核人员ID查询列表
     */
    @GetMapping("/auditor/{auditorId}")
    @ApiOperation("根据审核人员ID查询列表")
    public MyJsonBean getByAuditorId(@ApiParam("审核人员ID") @PathVariable Long auditorId,
                                     @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsIntelligentAudit> audits = intelligentAuditService.getByAuditorId(auditorId, tenantId);
            return MyJsonBean.success(audits);
        } catch (Exception e) {
            log.error("根据审核人员ID查询列表失败", e);
            return MyJsonBean.error("根据审核人员ID查询列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据审核部门ID查询列表
     */
    @GetMapping("/dept/{auditDeptId}")
    @ApiOperation("根据审核部门ID查询列表")
    public MyJsonBean getByAuditDeptId(@ApiParam("审核部门ID") @PathVariable Long auditDeptId,
                                       @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsIntelligentAudit> audits = intelligentAuditService.getByAuditDeptId(auditDeptId, tenantId);
            return MyJsonBean.success(audits);
        } catch (Exception e) {
            log.error("根据审核部门ID查询列表失败", e);
            return MyJsonBean.error("根据审核部门ID查询列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据目标对象查询列表
     */
    @GetMapping("/target/{targetObjectId}/{targetObjectType}")
    @ApiOperation("根据目标对象查询列表")
    public MyJsonBean getByTargetObject(@ApiParam("目标对象ID") @PathVariable Long targetObjectId,
                                        @ApiParam("目标对象类型") @PathVariable String targetObjectType,
                                        @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsIntelligentAudit> audits = intelligentAuditService.getByTargetObject(targetObjectId, targetObjectType, tenantId);
            return MyJsonBean.success(audits);
        } catch (Exception e) {
            log.error("根据目标对象查询列表失败", e);
            return MyJsonBean.error("根据目标对象查询列表失败: " + e.getMessage());
        }
    }

    /**
     * 创建智能审核
     */
    @PostMapping
    @ApiOperation("创建智能审核")
    public MyJsonBean createAudit(@ApiParam("智能审核信息") @RequestBody SsIntelligentAudit audit) {
        try {
            boolean success = intelligentAuditService.createAudit(audit);
            return success ? MyJsonBean.success("创建成功") : MyJsonBean.error("创建失败");
        } catch (Exception e) {
            log.error("创建智能审核失败", e);
            return MyJsonBean.error("创建智能审核失败: " + e.getMessage());
        }
    }

    /**
     * 更新智能审核
     */
    @PutMapping
    @ApiOperation("更新智能审核")
    public MyJsonBean updateAudit(@ApiParam("智能审核信息") @RequestBody SsIntelligentAudit audit) {
        try {
            boolean success = intelligentAuditService.updateAudit(audit);
            return success ? MyJsonBean.success("更新成功") : MyJsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新智能审核失败", e);
            return MyJsonBean.error("更新智能审核失败: " + e.getMessage());
        }
    }

    /**
     * 删除智能审核
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除智能审核")
    public MyJsonBean deleteAudit(@ApiParam("审核ID") @PathVariable Long id,
                                  @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.deleteAudit(id, tenantId);
            return success ? MyJsonBean.success("删除成功") : MyJsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除智能审核失败", e);
            return MyJsonBean.error("删除智能审核失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除智能审核
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除智能审核")
    public MyJsonBean batchDeleteAudits(@ApiParam("审核ID列表") @RequestBody List<Long> auditIds,
                                        @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.batchDeleteAudits(auditIds, tenantId);
            return success ? MyJsonBean.success("批量删除成功") : MyJsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除智能审核失败", e);
            return MyJsonBean.error("批量删除智能审核失败: " + e.getMessage());
        }
    }

    /**
     * 启动审核
     */
    @PostMapping("/{id}/start")
    @ApiOperation("启动审核")
    public MyJsonBean startAudit(@ApiParam("审核ID") @PathVariable Long id,
                                 @ApiParam("审核人员ID") @RequestParam Long auditorId,
                                 @ApiParam("审核人员姓名") @RequestParam String auditorName,
                                 @ApiParam("审核部门ID") @RequestParam Long auditDeptId,
                                 @ApiParam("审核部门名称") @RequestParam String auditDeptName,
                                 @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.startAudit(id, auditorId, auditorName, auditDeptId, auditDeptName, tenantId);
            return success ? MyJsonBean.success("启动审核成功") : MyJsonBean.error("启动审核失败");
        } catch (Exception e) {
            log.error("启动审核失败", e);
            return MyJsonBean.error("启动审核失败: " + e.getMessage());
        }
    }

    /**
     * 暂停审核
     */
    @PostMapping("/{id}/pause")
    @ApiOperation("暂停审核")
    public MyJsonBean pauseAudit(@ApiParam("审核ID") @PathVariable Long id,
                                 @ApiParam("暂停原因") @RequestParam String reason,
                                 @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.pauseAudit(id, reason, tenantId);
            return success ? MyJsonBean.success("暂停审核成功") : MyJsonBean.error("暂停审核失败");
        } catch (Exception e) {
            log.error("暂停审核失败", e);
            return MyJsonBean.error("暂停审核失败: " + e.getMessage());
        }
    }

    /**
     * 恢复审核
     */
    @PostMapping("/{id}/resume")
    @ApiOperation("恢复审核")
    public MyJsonBean resumeAudit(@ApiParam("审核ID") @PathVariable Long id,
                                  @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.resumeAudit(id, tenantId);
            return success ? MyJsonBean.success("恢复审核成功") : MyJsonBean.error("恢复审核失败");
        } catch (Exception e) {
            log.error("恢复审核失败", e);
            return MyJsonBean.error("恢复审核失败: " + e.getMessage());
        }
    }

    /**
     * 完成审核
     */
    @PostMapping("/{id}/complete")
    @ApiOperation("完成审核")
    public MyJsonBean completeAudit(@ApiParam("审核ID") @PathVariable Long id,
                                    @ApiParam("审核结果") @RequestParam String auditResult,
                                    @ApiParam("审核结论") @RequestParam String auditConclusion,
                                    @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.completeAudit(id, auditResult, auditConclusion, tenantId);
            return success ? MyJsonBean.success("完成审核成功") : MyJsonBean.error("完成审核失败");
        } catch (Exception e) {
            log.error("完成审核失败", e);
            return MyJsonBean.error("完成审核失败: " + e.getMessage());
        }
    }

    /**
     * 取消审核
     */
    @PostMapping("/{id}/cancel")
    @ApiOperation("取消审核")
    public MyJsonBean cancelAudit(@ApiParam("审核ID") @PathVariable Long id,
                                  @ApiParam("取消原因") @RequestParam String reason,
                                  @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.cancelAudit(id, reason, tenantId);
            return success ? MyJsonBean.success("取消审核成功") : MyJsonBean.error("取消审核失败");
        } catch (Exception e) {
            log.error("取消审核失败", e);
            return MyJsonBean.error("取消审核失败: " + e.getMessage());
        }
    }

    /**
     * 复核审核
     */
    @PostMapping("/{id}/review")
    @ApiOperation("复核审核")
    public MyJsonBean reviewAudit(@ApiParam("审核ID") @PathVariable Long id,
                                  @ApiParam("复核人员ID") @RequestParam Long reviewerId,
                                  @ApiParam("复核人员姓名") @RequestParam String reviewerName,
                                  @ApiParam("复核意见") @RequestParam String reviewComments,
                                  @ApiParam("复核结果") @RequestParam String reviewResult,
                                  @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.reviewAudit(id, reviewerId, reviewerName, reviewComments, reviewResult, tenantId);
            return success ? MyJsonBean.success("复核审核成功") : MyJsonBean.error("复核审核失败");
        } catch (Exception e) {
            log.error("复核审核失败", e);
            return MyJsonBean.error("复核审核失败: " + e.getMessage());
        }
    }

    /**
     * 处理审核
     */
    @PostMapping("/{id}/process")
    @ApiOperation("处理审核")
    public MyJsonBean processAudit(@ApiParam("审核ID") @PathVariable Long id,
                                   @ApiParam("处理人员ID") @RequestParam Long processorId,
                                   @ApiParam("处理人员姓名") @RequestParam String processorName,
                                   @ApiParam("处理措施") @RequestParam String processingActions,
                                   @ApiParam("处理结果") @RequestParam String processingResult,
                                   @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.processAudit(id, processorId, processorName, processingActions, processingResult, tenantId);
            return success ? MyJsonBean.success("处理审核成功") : MyJsonBean.error("处理审核失败");
        } catch (Exception e) {
            log.error("处理审核失败", e);
            return MyJsonBean.error("处理审核失败: " + e.getMessage());
        }
    }

    /**
     * 分配审核人员
     */
    @PostMapping("/{id}/assign")
    @ApiOperation("分配审核人员")
    public MyJsonBean assignAuditor(@ApiParam("审核ID") @PathVariable Long id,
                                    @ApiParam("审核人员ID") @RequestParam Long auditorId,
                                    @ApiParam("审核人员姓名") @RequestParam String auditorName,
                                    @ApiParam("审核部门ID") @RequestParam Long auditDeptId,
                                    @ApiParam("审核部门名称") @RequestParam String auditDeptName,
                                    @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.assignAuditor(id, auditorId, auditorName, auditDeptId, auditDeptName, tenantId);
            return success ? MyJsonBean.success("分配审核人员成功") : MyJsonBean.error("分配审核人员失败");
        } catch (Exception e) {
            log.error("分配审核人员失败", e);
            return MyJsonBean.error("分配审核人员失败: " + e.getMessage());
        }
    }

    /**
     * 批量分配审核人员
     */
    @PostMapping("/batch-assign")
    @ApiOperation("批量分配审核人员")
    public MyJsonBean batchAssignAuditor(@ApiParam("审核ID列表") @RequestBody List<Long> auditIds,
                                         @ApiParam("审核人员ID") @RequestParam Long auditorId,
                                         @ApiParam("审核人员姓名") @RequestParam String auditorName,
                                         @ApiParam("审核部门ID") @RequestParam Long auditDeptId,
                                         @ApiParam("审核部门名称") @RequestParam String auditDeptName,
                                         @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.batchAssignAuditor(auditIds, auditorId, auditorName, auditDeptId, auditDeptName, tenantId);
            return success ? MyJsonBean.success("批量分配审核人员成功") : MyJsonBean.error("批量分配审核人员失败");
        } catch (Exception e) {
            log.error("批量分配审核人员失败", e);
            return MyJsonBean.error("批量分配审核人员失败: " + e.getMessage());
        }
    }

    /**
     * 执行智能审核
     */
    @PostMapping("/{id}/execute")
    @ApiOperation("执行智能审核")
    public MyJsonBean executeIntelligentAudit(@ApiParam("审核ID") @PathVariable Long id,
                                              @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.executeIntelligentAudit(id, tenantId);
            return success ? MyJsonBean.success("执行智能审核成功") : MyJsonBean.error("执行智能审核失败");
        } catch (Exception e) {
            log.error("执行智能审核失败", e);
            return MyJsonBean.error("执行智能审核失败: " + e.getMessage());
        }
    }

    /**
     * 批量执行智能审核
     */
    @PostMapping("/batch-execute")
    @ApiOperation("批量执行智能审核")
    public MyJsonBean batchExecuteIntelligentAudit(@ApiParam("审核ID列表") @RequestBody List<Long> auditIds,
                                                   @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.batchExecuteIntelligentAudit(auditIds, tenantId);
            return success ? MyJsonBean.success("批量执行智能审核成功") : MyJsonBean.error("批量执行智能审核失败");
        } catch (Exception e) {
            log.error("批量执行智能审核失败", e);
            return MyJsonBean.error("批量执行智能审核失败: " + e.getMessage());
        }
    }

    /**
     * 应用机器学习模型
     */
    @PostMapping("/{id}/apply-ml-model")
    @ApiOperation("应用机器学习模型")
    public MyJsonBean applyMlModel(@ApiParam("审核ID") @PathVariable Long id,
                                   @ApiParam("机器学习模型ID") @RequestParam Long mlModelId,
                                   @ApiParam("模型版本") @RequestParam String mlModelVersion,
                                   @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.applyMlModel(id, mlModelId, mlModelVersion, tenantId);
            return success ? MyJsonBean.success("应用机器学习模型成功") : MyJsonBean.error("应用机器学习模型失败");
        } catch (Exception e) {
            log.error("应用机器学习模型失败", e);
            return MyJsonBean.error("应用机器学习模型失败: " + e.getMessage());
        }
    }

    /**
     * 异常检测
     */
    @PostMapping("/{id}/detect-anomaly")
    @ApiOperation("异常检测")
    public MyJsonBean detectAnomaly(@ApiParam("审核ID") @PathVariable Long id,
                                    @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.detectAnomaly(id, tenantId);
            return success ? MyJsonBean.success("异常检测成功") : MyJsonBean.error("异常检测失败");
        } catch (Exception e) {
            log.error("异常检测失败", e);
            return MyJsonBean.error("异常检测失败: " + e.getMessage());
        }
    }

    /**
     * 风险评估
     */
    @PostMapping("/{id}/assess-risk")
    @ApiOperation("风险评估")
    public MyJsonBean assessRisk(@ApiParam("审核ID") @PathVariable Long id,
                                 @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.assessRisk(id, tenantId);
            return success ? MyJsonBean.success("风险评估成功") : MyJsonBean.error("风险评估失败");
        } catch (Exception e) {
            log.error("风险评估失败", e);
            return MyJsonBean.error("风险评估失败: " + e.getMessage());
        }
    }

    /**
     * 生成预警
     */
    @PostMapping("/{id}/generate-warning")
    @ApiOperation("生成预警")
    public MyJsonBean generateWarning(@ApiParam("审核ID") @PathVariable Long id,
                                      @ApiParam("预警级别") @RequestParam String warningLevel,
                                      @ApiParam("预警消息") @RequestParam String warningMessage,
                                      @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.generateWarning(id, warningLevel, warningMessage, tenantId);
            return success ? MyJsonBean.success("生成预警成功") : MyJsonBean.error("生成预警失败");
        } catch (Exception e) {
            log.error("生成预警失败", e);
            return MyJsonBean.error("生成预警失败: " + e.getMessage());
        }
    }

    /**
     * 添加跟进记录
     */
    @PostMapping("/{id}/add-follow-up")
    @ApiOperation("添加跟进记录")
    public MyJsonBean addFollowUpRecord(@ApiParam("审核ID") @PathVariable Long id,
                                        @ApiParam("跟进记录") @RequestParam String followUpRecord,
                                        @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.addFollowUpRecord(id, followUpRecord, tenantId);
            return success ? MyJsonBean.success("添加跟进记录成功") : MyJsonBean.error("添加跟进记录失败");
        } catch (Exception e) {
            log.error("添加跟进记录失败", e);
            return MyJsonBean.error("添加跟进记录失败: " + e.getMessage());
        }
    }

    /**
     * 更新跟进状态
     */
    @PostMapping("/{id}/update-follow-up-status")
    @ApiOperation("更新跟进状态")
    public MyJsonBean updateFollowUpStatus(@ApiParam("审核ID") @PathVariable Long id,
                                           @ApiParam("跟进状态") @RequestParam String followUpStatus,
                                           @ApiParam("下次跟进时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime nextFollowUpTime,
                                           @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.updateFollowUpStatus(id, followUpStatus, nextFollowUpTime, tenantId);
            return success ? MyJsonBean.success("更新跟进状态成功") : MyJsonBean.error("更新跟进状态失败");
        } catch (Exception e) {
            log.error("更新跟进状态失败", e);
            return MyJsonBean.error("更新跟进状态失败: " + e.getMessage());
        }
    }

    /**
     * 查询待处理的审核列表
     */
    @GetMapping("/pending")
    @ApiOperation("查询待处理的审核列表")
    public MyJsonBean getPendingAudits(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsIntelligentAudit> audits = intelligentAuditService.getPendingAudits(tenantId);
            return MyJsonBean.success(audits);
        } catch (Exception e) {
            log.error("查询待处理的审核列表失败", e);
            return MyJsonBean.error("查询待处理的审核列表失败: " + e.getMessage());
        }
    }

    /**
     * 查询需要复核的审核列表
     */
    @GetMapping("/need-review")
    @ApiOperation("查询需要复核的审核列表")
    public MyJsonBean getNeedReview(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsIntelligentAudit> audits = intelligentAuditService.getNeedReview(tenantId);
            return MyJsonBean.success(audits);
        } catch (Exception e) {
            log.error("查询需要复核的审核列表失败", e);
            return MyJsonBean.error("查询需要复核的审核列表失败: " + e.getMessage());
        }
    }

    /**
     * 查询需要跟进的审核列表
     */
    @GetMapping("/need-follow-up")
    @ApiOperation("查询需要跟进的审核列表")
    public MyJsonBean getNeedFollowUp(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsIntelligentAudit> audits = intelligentAuditService.getNeedFollowUp(tenantId);
            return MyJsonBean.success(audits);
        } catch (Exception e) {
            log.error("查询需要跟进的审核列表失败", e);
            return MyJsonBean.error("查询需要跟进的审核列表失败: " + e.getMessage());
        }
    }

    /**
     * 查询高风险审核列表
     */
    @GetMapping("/high-risk")
    @ApiOperation("查询高风险审核列表")
    public MyJsonBean getHighRiskAudits(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsIntelligentAudit> audits = intelligentAuditService.getHighRiskAudits(tenantId);
            return MyJsonBean.success(audits);
        } catch (Exception e) {
            log.error("查询高风险审核列表失败", e);
            return MyJsonBean.error("查询高风险审核列表失败: " + e.getMessage());
        }
    }

    /**
     * 查询异常审核列表
     */
    @GetMapping("/anomaly")
    @ApiOperation("查询异常审核列表")
    public MyJsonBean getAnomalyAudits(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsIntelligentAudit> audits = intelligentAuditService.getAnomalyAudits(tenantId);
            return MyJsonBean.success(audits);
        } catch (Exception e) {
            log.error("查询异常审核列表失败", e);
            return MyJsonBean.error("查询异常审核列表失败: " + e.getMessage());
        }
    }

    /**
     * 查询超时审核列表
     */
    @GetMapping("/overdue")
    @ApiOperation("查询超时审核列表")
    public MyJsonBean getOverdueAudits(@ApiParam("当前时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime currentTime,
                                       @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsIntelligentAudit> audits = intelligentAuditService.getOverdueAudits(currentTime, tenantId);
            return MyJsonBean.success(audits);
        } catch (Exception e) {
            log.error("查询超时审核列表失败", e);
            return MyJsonBean.error("查询超时审核列表失败: " + e.getMessage());
        }
    }

    /**
     * 统计审核数据
     */
    @GetMapping("/statistics")
    @ApiOperation("统计审核数据")
    public MyJsonBean getAuditStatistics(@ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                         @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                         @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> statistics = intelligentAuditService.getAuditStatistics(startTime, endTime, tenantId);
            return MyJsonBean.success(statistics);
        } catch (Exception e) {
            log.error("统计审核数据失败", e);
            return MyJsonBean.error("统计审核数据失败: " + e.getMessage());
        }
    }

    /**
     * 统计审核状态分布
     */
    @GetMapping("/status-distribution")
    @ApiOperation("统计审核状态分布")
    public MyJsonBean getAuditStatusDistribution(@ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                                 @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                                 @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> distribution = intelligentAuditService.getAuditStatusDistribution(startTime, endTime, tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计审核状态分布失败", e);
            return MyJsonBean.error("统计审核状态分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计审核类型分布
     */
    @GetMapping("/type-distribution")
    @ApiOperation("统计审核类型分布")
    public MyJsonBean getAuditTypeDistribution(@ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                               @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                               @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> distribution = intelligentAuditService.getAuditTypeDistribution(startTime, endTime, tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计审核类型分布失败", e);
            return MyJsonBean.error("统计审核类型分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计风险等级分布
     */
    @GetMapping("/risk-distribution")
    @ApiOperation("统计风险等级分布")
    public MyJsonBean getRiskLevelDistribution(@ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                               @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                               @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> distribution = intelligentAuditService.getRiskLevelDistribution(startTime, endTime, tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计风险等级分布失败", e);
            return MyJsonBean.error("统计风险等级分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计审核趋势
     */
    @GetMapping("/trend")
    @ApiOperation("统计审核趋势")
    public MyJsonBean getAuditTrend(@ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                    @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                    @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> trend = intelligentAuditService.getAuditTrend(startTime, endTime, tenantId);
            return MyJsonBean.success(trend);
        } catch (Exception e) {
            log.error("统计审核趋势失败", e);
            return MyJsonBean.error("统计审核趋势失败: " + e.getMessage());
        }
    }

    /**
     * 统计审核效率
     */
    @GetMapping("/efficiency")
    @ApiOperation("统计审核效率")
    public MyJsonBean getAuditEfficiency(@ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                         @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                         @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> efficiency = intelligentAuditService.getAuditEfficiency(startTime, endTime, tenantId);
            return MyJsonBean.success(efficiency);
        } catch (Exception e) {
            log.error("统计审核效率失败", e);
            return MyJsonBean.error("统计审核效率失败: " + e.getMessage());
        }
    }

    /**
     * 统计审核质量
     */
    @GetMapping("/quality")
    @ApiOperation("统计审核质量")
    public MyJsonBean getAuditQuality(@ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                      @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                      @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> quality = intelligentAuditService.getAuditQuality(startTime, endTime, tenantId);
            return MyJsonBean.success(quality);
        } catch (Exception e) {
            log.error("统计审核质量失败", e);
            return MyJsonBean.error("统计审核质量失败: " + e.getMessage());
        }
    }

    /**
     * 查询审核排行榜
     */
    @GetMapping("/ranking")
    @ApiOperation("查询审核排行榜")
    public MyJsonBean getAuditRanking(@ApiParam("排行榜类型") @RequestParam String rankingType,
                                      @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                      @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                      @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit,
                                      @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> ranking = intelligentAuditService.getAuditRanking(rankingType, startTime, endTime, limit, tenantId);
            return MyJsonBean.success(ranking);
        } catch (Exception e) {
            log.error("查询审核排行榜失败", e);
            return MyJsonBean.error("查询审核排行榜失败: " + e.getMessage());
        }
    }

    /**
     * 查询审核人员工作量统计
     */
    @GetMapping("/auditor-workload")
    @ApiOperation("查询审核人员工作量统计")
    public MyJsonBean getAuditorWorkload(@ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                         @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                         @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> workload = intelligentAuditService.getAuditorWorkload(startTime, endTime, tenantId);
            return MyJsonBean.success(workload);
        } catch (Exception e) {
            log.error("查询审核人员工作量统计失败", e);
            return MyJsonBean.error("查询审核人员工作量统计失败: " + e.getMessage());
        }
    }

    /**
     * 查询部门审核统计
     */
    @GetMapping("/dept-statistics")
    @ApiOperation("查询部门审核统计")
    public MyJsonBean getDeptAuditStatistics(@ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                             @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                             @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> statistics = intelligentAuditService.getDeptAuditStatistics(startTime, endTime, tenantId);
            return MyJsonBean.success(statistics);
        } catch (Exception e) {
            log.error("查询部门审核统计失败", e);
            return MyJsonBean.error("查询部门审核统计失败: " + e.getMessage());
        }
    }

    /**
     * 计算平均审核时长
     */
    @GetMapping("/average-duration")
    @ApiOperation("计算平均审核时长")
    public MyJsonBean calculateAverageAuditDuration(@ApiParam("审核类型") @RequestParam(required = false) String auditType,
                                                    @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                                    @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                                    @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            BigDecimal averageDuration = intelligentAuditService.calculateAverageAuditDuration(auditType, startTime, endTime, tenantId);
            return MyJsonBean.success(averageDuration);
        } catch (Exception e) {
            log.error("计算平均审核时长失败", e);
            return MyJsonBean.error("计算平均审核时长失败: " + e.getMessage());
        }
    }

    /**
     * 计算审核成功率
     */
    @GetMapping("/success-rate")
    @ApiOperation("计算审核成功率")
    public MyJsonBean calculateAuditSuccessRate(@ApiParam("审核类型") @RequestParam(required = false) String auditType,
                                                @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                                @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                                @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            BigDecimal successRate = intelligentAuditService.calculateAuditSuccessRate(auditType, startTime, endTime, tenantId);
            return MyJsonBean.success(successRate);
        } catch (Exception e) {
            log.error("计算审核成功率失败", e);
            return MyJsonBean.error("计算审核成功率失败: " + e.getMessage());
        }
    }

    /**
     * 生成审核报告
     */
    @GetMapping("/report")
    @ApiOperation("生成审核报告")
    public MyJsonBean generateAuditReport(@ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                          @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                          @ApiParam("报告类型") @RequestParam String reportType,
                                          @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> report = intelligentAuditService.generateAuditReport(startTime, endTime, reportType, tenantId);
            return MyJsonBean.success(report);
        } catch (Exception e) {
            log.error("生成审核报告失败", e);
            return MyJsonBean.error("生成审核报告失败: " + e.getMessage());
        }
    }

    /**
     * 导出审核数据
     */
    @GetMapping("/export")
    @ApiOperation("导出审核数据")
    public MyJsonBean exportAuditData(@ApiParam("审核类型") @RequestParam(required = false) String auditType,
                                      @ApiParam("审核状态") @RequestParam(required = false) String auditStatus,
                                      @ApiParam("风险等级") @RequestParam(required = false) String riskLevel,
                                      @ApiParam("开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                      @ApiParam("结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                      @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsIntelligentAudit> audits = intelligentAuditService.exportAuditData(auditType, auditStatus, riskLevel, startTime, endTime, tenantId);
            return MyJsonBean.success(audits);
        } catch (Exception e) {
            log.error("导出审核数据失败", e);
            return MyJsonBean.error("导出审核数据失败: " + e.getMessage());
        }
    }

    /**
     * 智能推荐审核规则
     */
    @GetMapping("/recommend-rules")
    @ApiOperation("智能推荐审核规则")
    public MyJsonBean recommendAuditRules(@ApiParam("目标对象ID") @RequestParam Long targetObjectId,
                                          @ApiParam("目标对象类型") @RequestParam String targetObjectType,
                                          @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> recommendations = intelligentAuditService.recommendAuditRules(targetObjectId, targetObjectType, tenantId);
            return MyJsonBean.success(recommendations);
        } catch (Exception e) {
            log.error("智能推荐审核规则失败", e);
            return MyJsonBean.error("智能推荐审核规则失败: " + e.getMessage());
        }
    }

    /**
     * 预测审核风险
     */
    @GetMapping("/predict-risk")
    @ApiOperation("预测审核风险")
    public MyJsonBean predictAuditRisk(@ApiParam("目标对象ID") @RequestParam Long targetObjectId,
                                       @ApiParam("目标对象类型") @RequestParam String targetObjectType,
                                       @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> prediction = intelligentAuditService.predictAuditRisk(targetObjectId, targetObjectType, tenantId);
            return MyJsonBean.success(prediction);
        } catch (Exception e) {
            log.error("预测审核风险失败", e);
            return MyJsonBean.error("预测审核风险失败: " + e.getMessage());
        }
    }

    /**
     * 自动化审核流程
     */
    @PostMapping("/{id}/automate")
    @ApiOperation("自动化审核流程")
    public MyJsonBean automateAuditProcess(@ApiParam("审核ID") @PathVariable Long id,
                                           @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.automateAuditProcess(id, tenantId);
            return success ? MyJsonBean.success("自动化审核流程成功") : MyJsonBean.error("自动化审核流程失败");
        } catch (Exception e) {
            log.error("自动化审核流程失败", e);
            return MyJsonBean.error("自动化审核流程失败: " + e.getMessage());
        }
    }

    /**
     * 发送审核通知
     */
    @PostMapping("/{id}/notify")
    @ApiOperation("发送审核通知")
    public MyJsonBean sendAuditNotification(@ApiParam("审核ID") @PathVariable Long id,
                                            @ApiParam("通知类型") @RequestParam String notificationType,
                                            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.sendAuditNotification(id, notificationType, tenantId);
            return success ? MyJsonBean.success("发送审核通知成功") : MyJsonBean.error("发送审核通知失败");
        } catch (Exception e) {
            log.error("发送审核通知失败", e);
            return MyJsonBean.error("发送审核通知失败: " + e.getMessage());
        }
    }

    /**
     * 批量发送审核通知
     */
    @PostMapping("/batch-notify")
    @ApiOperation("批量发送审核通知")
    public MyJsonBean batchSendAuditNotifications(@ApiParam("审核ID列表") @RequestBody List<Long> auditIds,
                                                  @ApiParam("通知类型") @RequestParam String notificationType,
                                                  @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = intelligentAuditService.batchSendAuditNotifications(auditIds, notificationType, tenantId);
            return success ? MyJsonBean.success("批量发送审核通知成功") : MyJsonBean.error("批量发送审核通知失败");
        } catch (Exception e) {
            log.error("批量发送审核通知失败", e);
            return MyJsonBean.error("批量发送审核通知失败: " + e.getMessage());
        }
    }

    /**
     * 检查审核编码是否存在
     */
    @GetMapping("/check-code")
    @ApiOperation("检查审核编码是否存在")
    public MyJsonBean checkAuditCodeExists(@ApiParam("审核编码") @RequestParam String auditCode,
                                           @ApiParam("审核ID") @RequestParam(required = false) Long auditId,
                                           @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean exists = intelligentAuditService.checkAuditCodeExists(auditCode, auditId, tenantId);
            return MyJsonBean.success(exists);
        } catch (Exception e) {
            log.error("检查审核编码是否存在失败", e);
            return MyJsonBean.error("检查审核编码是否存在失败: " + e.getMessage());
        }
    }

    /**
     * 检查目标对象是否有进行中的审核
     */
    @GetMapping("/check-target-in-progress")
    @ApiOperation("检查目标对象是否有进行中的审核")
    public MyJsonBean checkTargetObjectInProgress(@ApiParam("目标对象ID") @RequestParam Long targetObjectId,
                                                  @ApiParam("目标对象类型") @RequestParam String targetObjectType,
                                                  @ApiParam("审核ID") @RequestParam(required = false) Long auditId,
                                                  @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean inProgress = intelligentAuditService.checkTargetObjectInProgress(targetObjectId, targetObjectType, auditId, tenantId);
            return MyJsonBean.success(inProgress);
        } catch (Exception e) {
            log.error("检查目标对象是否有进行中的审核失败", e);
            return MyJsonBean.error("检查目标对象是否有进行中的审核失败: " + e.getMessage());
        }
    }
}
