package com.huabo.contract.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.RiskAssessment;
import com.huabo.contract.service.RiskAssessmentDetailService;
import com.huabo.contract.service.RiskAssessmentService;
import com.huabo.contract.vo.RiskAssessmentQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 风险评估管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/riskAssessment")
@Tag(name="风险评估管理",description="风险评估管理")
@Validated
public class RiskAssessmentController {

    @Autowired
    private RiskAssessmentService riskAssessmentService;

    @Autowired
    private RiskAssessmentDetailService riskAssessmentDetailService;

    /**
     * 分页查询风险评估列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询风险评估列表", description = "支持多条件查询和分页")
    public String getRiskAssessmentList(@RequestBody RiskAssessmentQueryParam param) {
        try {
            log.info("分页查询风险评估列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNum() == null || param.getPageNum() <= 0) {
                param.setPageNum(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<RiskAssessment> pageInfo = riskAssessmentService.getRiskAssessmentList(param);

            return JsonBean.success("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("查询风险评估列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取风险评估详情
     *
     * @param id 主键ID
     * @return 风险评估详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取风险评估详情", description = "根据ID获取详细信息")
    public String getRiskAssessmentById(@PathVariable Long id) {
        try {
            log.info("获取风险评估详情，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            RiskAssessment riskAssessment = riskAssessmentService.getRiskAssessmentById(id);
            if (riskAssessment == null) {
                return JsonBean.error("风险评估不存在");
            }

            return JsonBean.success("查询成功", riskAssessment);
        } catch (Exception e) {
            log.error("获取风险评估详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存风险评估（新增或修改）
     *
     * @param riskAssessment 风险评估
     * @return 保存结果
     */
    @PostMapping("/save")
    @Operation(summary = "保存风险评估", description = "新增或修改风险评估")
    public String saveRiskAssessment(@RequestBody RiskAssessment riskAssessment) {
        try {
            log.info("保存风险评估，风险评估：{}", riskAssessment);

            // 校验必填字段
            if (!StringUtils.hasText(riskAssessment.getAssessmentName())) {
                return JsonBean.error("评估名称不能为空");
            }
//            if (riskAssessment.getCounterpartId() == null) {
//                return JsonBean.error("相对方不能为空");
//            }
            if (riskAssessment.getAssessmentType() == null) {
                return JsonBean.error("评估类型不能为空");
            }

            // 校验评估编号唯一性
            if (StringUtils.hasText(riskAssessment.getAssessmentNo())) {
                boolean exists = riskAssessmentService.existsAssessmentNo(
                    riskAssessment.getAssessmentNo(), riskAssessment.getId());
                if (exists) {
                    return JsonBean.error("评估编号已存在，请重新输入");
                }
            }
            riskAssessment.setCounterpartId((long)3);
            riskAssessment.setCreateBy((long)4);
            riskAssessment.setCreateTime(new Date());
            riskAssessment.setUpdateBy((long)5);
            riskAssessment.setUpdateTime(new Date());
            boolean result = riskAssessmentService.saveRiskAssessment(riskAssessment);
            if (result) {
                return JsonBean.success("保存成功");
            } else {
                return JsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存风险评估失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 删除风险评估
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除风险评估", description = "根据ID删除风险评估")
    public String deleteRiskAssessment(@PathVariable Long id) {
        try {
            log.info("删除风险评估，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            boolean result = riskAssessmentService.deleteRiskAssessment(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除风险评估失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除风险评估
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除风险评估", description = "根据ID列表批量删除风险评估")
    public String batchDeleteRiskAssessment(@RequestBody List<Long> ids) {
        try {
            log.info("批量删除风险评估，ID列表：{}", ids);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("ID列表不能为空");
            }

            boolean result = riskAssessmentService.batchDeleteRiskAssessment(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除风险评估失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 更新评估状态
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/updateAssessmentStatus")
    @Operation(summary = "更新评估状态", description = "更新风险评估的状态")
    public String updateAssessmentStatus(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            Integer assessmentStatus = (Integer) request.get("assessmentStatus");

            log.info("更新评估状态，ID：{}，评估状态：{}", id, assessmentStatus);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }
            if (assessmentStatus == null) {
                return JsonBean.error("评估状态不能为空");
            }

            boolean result = riskAssessmentService.updateAssessmentStatus(id, assessmentStatus);
            if (result) {
                return JsonBean.success("状态更新成功");
            } else {
                return JsonBean.error("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新评估状态失败", e);
            return JsonBean.error("状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 更新审批状态
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/updateApprovalStatus")
    @Operation(summary = "更新审批状态", description = "更新风险评估的审批状态")
    public String updateApprovalStatus(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            Integer approvalStatus = (Integer) request.get("approvalStatus");
            Long approverId = request.get("approverId") != null ? Long.valueOf(request.get("approverId").toString()) : null;

            log.info("更新审批状态，ID：{}，审批状态：{}，审批人ID：{}", id, approvalStatus, approverId);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }
            if (approvalStatus == null) {
                return JsonBean.error("审批状态不能为空");
            }

            boolean result = riskAssessmentService.updateApprovalStatus(id, approvalStatus, approverId);
            if (result) {
                return JsonBean.success("审批状态更新成功");
            } else {
                return JsonBean.error("审批状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新审批状态失败", e);
            return JsonBean.error("审批状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 获取待审批的风险评估列表
     *
     * @return 待审批的风险评估列表
     */
    @GetMapping("/pendingApprovalList")
    @Operation(summary = "获取待审批的风险评估列表", description = "获取所有待审批的风险评估")
    public String getPendingApprovalList(@RequestParam(required = false) Long approverId) {
        try {
            log.info("获取待审批的风险评估列表，审批人ID：{}", approverId);

            List<RiskAssessment> assessments = riskAssessmentService.getPendingApprovalList(approverId);
            return JsonBean.success("查询成功", assessments);
        } catch (Exception e) {
            log.error("获取待审批的风险评估列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取高风险评估列表
     *
     * @return 高风险评估列表
     */
    @GetMapping("/highRiskList")
    @Operation(summary = "获取高风险评估列表", description = "获取所有高风险评估")
    public String getHighRiskList(@RequestParam(defaultValue = "3") Integer riskLevel) {
        try {
            log.info("获取高风险评估列表，风险等级阈值：{}", riskLevel);

            List<RiskAssessment> assessments = riskAssessmentService.getHighRiskList(riskLevel);
            return JsonBean.success("查询成功", assessments);
        } catch (Exception e) {
            log.error("获取高风险评估列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 生成评估编号
     *
     * @return 评估编号
     */
    @GetMapping("/generateAssessmentNo")
    @Operation(summary = "生成评估编号", description = "自动生成唯一的评估编号")
    public String generateAssessmentNo() {
        try {
            log.info("生成评估编号");
            String assessmentNo = riskAssessmentService.generateAssessmentNo();
            return JsonBean.success("生成成功", assessmentNo);
        } catch (Exception e) {
            log.error("生成评估编号失败", e);
            return JsonBean.error("生成失败：" + e.getMessage());
        }
    }

    /**
     * 计算风险评估总分和等级
     *
     * @param id 评估ID
     * @return 计算结果
     */
    @PostMapping("/calculateRiskScoreAndLevel/{id}")
    @Operation(summary = "计算风险评估总分和等级", description = "根据明细数据计算总分和风险等级")
    public String calculateRiskScoreAndLevel(@PathVariable Long id) {
        try {
            log.info("计算风险评估总分和等级，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            boolean result = riskAssessmentService.calculateRiskScoreAndLevel(id);
            if (result) {
                return JsonBean.success("计算成功");
            } else {
                return JsonBean.error("计算失败");
            }
        } catch (Exception e) {
            log.error("计算风险评估总分和等级失败", e);
            return JsonBean.error("计算失败：" + e.getMessage());
        }
    }

    /**
     * 提交评估
     *
     * @param id 评估ID
     * @return 提交结果
     */
    @PostMapping("/submit/{id}")
    @Operation(summary = "提交评估", description = "完成评估并提交审批")
    public String submitAssessment(@PathVariable Long id) {
        try {
            log.info("提交评估，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            boolean result = riskAssessmentService.submitAssessment(id);
            if (result) {
                return JsonBean.success("提交成功");
            } else {
                return JsonBean.error("提交失败");
            }
        } catch (Exception e) {
            log.error("提交评估失败", e);
            return JsonBean.error("提交失败：" + e.getMessage());
        }
    }

    /**
     * 审批评估
     *
     * @param request 审批请求
     * @return 审批结果
     */
    @PostMapping("/approve")
    @Operation(summary = "审批评估", description = "审批风险评估")
    public String approveAssessment(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            Integer approvalStatus = (Integer) request.get("approvalStatus");
            Long approverId = Long.valueOf(request.get("approverId").toString());
            String remarks = (String) request.get("remarks");

            log.info("审批评估，ID：{}，审批状态：{}，审批人ID：{}，审批意见：{}", id, approvalStatus, approverId, remarks);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }
            if (approvalStatus == null) {
                return JsonBean.error("审批状态不能为空");
            }
            if (approverId == null) {
                return JsonBean.error("审批人不能为空");
            }

            boolean result = riskAssessmentService.approveAssessment(id, approvalStatus, approverId, remarks);
            if (result) {
                return JsonBean.success("审批成功");
            } else {
                return JsonBean.error("审批失败");
            }
        } catch (Exception e) {
            log.error("审批评估失败", e);
            return JsonBean.error("审批失败：" + e.getMessage());
        }
    }
}
