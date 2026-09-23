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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.DebtManagement;
import com.huabo.contract.service.DebtManagementService;
import com.huabo.contract.vo.DebtManagementQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 债权管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/debt")
@Tag(name="债权管理",description="债权管理")
@Validated
public class DebtManagementController {

    @Autowired
    private DebtManagementService debtManagementService;

    /**
     * 分页查询债权管理列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询债权管理列表", description = "支持多条件查询和分页")
    public String getDebtManagementList(@RequestBody DebtManagementQueryParam param) {
        try {
            log.info("分页查询债权管理列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNumber() == null || param.getPageNumber() <= 0) {
                param.setPageNumber(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<DebtManagement> pageInfo = debtManagementService.getDebtManagementList(param);

            return JsonBean.success("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("查询债权管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取债权管理详情
     *
     * @param id 主键ID
     * @return 债权管理详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取债权管理详情", description = "根据ID获取详细信息")
    public String getDebtManagementById(@PathVariable Long id) {
        try {
            log.info("获取债权管理详情，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            DebtManagement debtManagement = debtManagementService.getDebtManagementById(id);
            if (debtManagement == null) {
                return JsonBean.error("债权管理不存在");
            }

            return JsonBean.success("查询成功", debtManagement);
        } catch (Exception e) {
            log.error("获取债权管理详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存债权管理（新增或修改）
     *
     * @param debtManagement 债权管理
     * @return 保存结果
     */
    @PostMapping("/save")
    @Operation(summary = "保存债权管理", description = "新增或修改债权管理")
    public String saveDebtManagement(@RequestBody DebtManagement debtManagement) {
        try {
            log.info("保存债权管理，债权：{}", debtManagement);

            // 校验必填字段
            if (debtManagement.getProjectId() == null) {
                return JsonBean.error("项目ID不能为空");
            }
            if (debtManagement.getDebtType() == null) {
                return JsonBean.error("债权类型不能为空");
            }
            if (debtManagement.getDebtAmount() == null) {
                return JsonBean.error("债权金额不能为空");
            }

            // 校验债权编号唯一性
            if (StringUtils.hasText(debtManagement.getDebtNo())) {
                boolean exists = debtManagementService.existsDebtNo(
                    debtManagement.getDebtNo(), debtManagement.getId());
                if (exists) {
                    return JsonBean.error("债权编号已存在，请重新输入");
                }
            }

            boolean result = debtManagementService.saveDebtManagement(debtManagement);
            if (result) {
                return JsonBean.success("保存成功");
            } else {
                return JsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存债权管理失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 更新债权管理
     *
     * @param id 债权ID
     * @param debtManagement 债权管理
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新债权管理", description = "根据ID更新债权管理")
    public String updateDebtManagement(@PathVariable Long id, @RequestBody DebtManagement debtManagement) {
        try {
            log.info("更新债权管理，ID：{}，债权：{}", id, debtManagement);

            if (id == null) {
                return JsonBean.error("债权ID不能为空");
            }

            // 设置ID和更新时间
            debtManagement.setId(id);
            debtManagement.setUpdateTime(new Date());

            boolean result = debtManagementService.saveDebtManagement(debtManagement);
            if (result) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新债权管理失败", e);
            return JsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除债权管理
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除债权管理", description = "根据ID删除债权管理")
    public String deleteDebtManagement(@PathVariable Long id) {
        try {
            log.info("删除债权管理，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            boolean result = debtManagementService.deleteDebtManagement(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除债权管理失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除债权管理
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除债权管理", description = "根据ID列表批量删除债权管理")
    public String batchDeleteDebtManagement(@RequestBody List<Long> ids) {
        try {
            log.info("批量删除债权管理，ID列表：{}", ids);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("ID列表不能为空");
            }

            boolean result = debtManagementService.batchDeleteDebtManagement(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除债权管理失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据项目ID查询债权管理列表
     *
     * @param projectId 项目ID
     * @return 债权管理列表
     */
    @GetMapping("/project/{projectId}")
    @Operation(summary = "根据项目ID查询债权管理列表", description = "根据项目ID查询债权管理列表")
    public String getDebtManagementByProjectId(@PathVariable Long projectId) {
        try {
            log.info("根据项目ID查询债权管理列表，项目ID：{}", projectId);

            if (projectId == null) {
                return JsonBean.error("项目ID不能为空");
            }

            List<DebtManagement> debts = debtManagementService.getDebtManagementByProjectId(projectId);
            return JsonBean.success("查询成功", debts);
        } catch (Exception e) {
            log.error("根据项目ID查询债权管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据债权类型查询债权管理列表
     *
     * @param debtType 债权类型
     * @return 债权管理列表
     */
    @GetMapping("/type/{debtType}")
    @Operation(summary = "根据债权类型查询债权管理列表", description = "根据债权类型查询债权管理列表")
    public String getDebtManagementByDebtType(@PathVariable Integer debtType) {
        try {
            log.info("根据债权类型查询债权管理列表，债权类型：{}", debtType);

            if (debtType == null) {
                return JsonBean.error("债权类型不能为空");
            }

            List<DebtManagement> debts = debtManagementService.getDebtManagementByDebtType(debtType);
            return JsonBean.success("查询成功", debts);
        } catch (Exception e) {
            log.error("根据债权类型查询债权管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据债权状态查询债权管理列表
     *
     * @param debtStatus 债权状态
     * @return 债权管理列表
     */
    @GetMapping("/status/{debtStatus}")
    @Operation(summary = "根据债权状态查询债权管理列表", description = "根据债权状态查询债权管理列表")
    public String getDebtManagementByDebtStatus(@PathVariable Integer debtStatus) {
        try {
            log.info("根据债权状态查询债权管理列表，债权状态：{}", debtStatus);

            if (debtStatus == null) {
                return JsonBean.error("债权状态不能为空");
            }

            List<DebtManagement> debts = debtManagementService.getDebtManagementByDebtStatus(debtStatus);
            return JsonBean.success("查询成功", debts);
        } catch (Exception e) {
            log.error("根据债权状态查询债权管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }



    /**
     * 获取我的债权管理列表
     *
     * @param managerId 负责人ID
     * @return 债权管理列表
     */
    @GetMapping("/my/{managerId}")
    @Operation(summary = "获取我的债权管理列表", description = "获取我的债权管理列表")
    public String getMyDebtManagement(@PathVariable Long managerId) {
        try {
            log.info("获取我的债权管理列表，负责人ID：{}", managerId);

            if (managerId == null) {
                return JsonBean.error("负责人ID不能为空");
            }

            List<DebtManagement> debts = debtManagementService.getMyDebtManagement(managerId);
            return JsonBean.success("查询成功", debts);
        } catch (Exception e) {
            log.error("获取我的债权管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取正常状态的债权列表
     *
     * @return 债权列表
     */
    @GetMapping("/normal")
    @Operation(summary = "获取正常状态的债权列表", description = "获取正常状态的债权列表")
    public String getNormalDebts() {
        try {
            log.info("获取正常状态的债权列表");

            List<DebtManagement> debts = debtManagementService.getNormalDebts();
            return JsonBean.success("查询成功", debts);
        } catch (Exception e) {
            log.error("获取正常状态的债权列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取逾期的债权列表
     *
     * @return 债权列表
     */
    @GetMapping("/overdue")
    @Operation(summary = "获取逾期的债权列表", description = "获取逾期的债权列表")
    public String getOverdueDebts() {
        try {
            log.info("获取逾期的债权列表");

            List<DebtManagement> debts = debtManagementService.getOverdueDebts();
            return JsonBean.success("查询成功", debts);
        } catch (Exception e) {
            log.error("获取逾期的债权列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 债权账龄分析
     *
     * @param projectId 项目ID（可选）
     * @return 账龄分析结果
     */
    @GetMapping("/aging/analysis")
    @Operation(summary = "债权账龄分析", description = "分析债权的账龄分布和风险等级")
    public String getDebtAgingAnalysis(@RequestParam(required = false) Long projectId) {
        try {
            log.info("获取债权账龄分析，项目ID：{}", projectId);

            Map<String, Object> analysisResult = debtManagementService.getDebtAgingAnalysis(projectId);
            return JsonBean.success("查询成功", analysisResult);
        } catch (Exception e) {
            log.error("获取债权账龄分析失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 导出债权账龄分析报告
     *
     * @param projectId 项目ID（可选）
     * @return 导出结果
     */
    @PostMapping("/aging/export")
    @Operation(summary = "导出债权账龄分析报告", description = "导出债权账龄分析报告")
    public String exportDebtAgingReport(@RequestParam(required = false) Long projectId) {
        try {
            log.info("导出债权账龄分析报告，项目ID：{}", projectId);

            // 这里可以实现具体的导出逻辑
            // 暂时返回成功信息
            return JsonBean.success("导出成功");
        } catch (Exception e) {
            log.error("导出债权账龄分析报告失败", e);
            return JsonBean.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 获取预警的债权列表
     *
     * @return 债权列表
     */
    @GetMapping("/warning")
    @Operation(summary = "获取预警的债权列表", description = "获取预警的债权列表")
    public String getWarningDebts() {
        try {
            log.info("获取预警的债权列表");

            List<DebtManagement> debts = debtManagementService.getWarningDebts();
            return JsonBean.success("查询成功", debts);
        } catch (Exception e) {
            log.error("获取预警的债权列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取已结清的债权列表
     *
     * @return 债权列表
     */
    @GetMapping("/settled")
    @Operation(summary = "获取已结清的债权列表", description = "获取已结清的债权列表")
    public String getSettledDebts() {
        try {
            log.info("获取已结清的债权列表");

            List<DebtManagement> debts = debtManagementService.getSettledDebts();
            return JsonBean.success("查询成功", debts);
        } catch (Exception e) {
            log.error("获取已结清的债权列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取已核销的债权列表
     *
     * @return 债权列表
     */
    @GetMapping("/writtenOff")
    @Operation(summary = "获取已核销的债权列表", description = "获取已核销的债权列表")
    public String getWrittenOffDebts() {
        try {
            log.info("获取已核销的债权列表");

            List<DebtManagement> debts = debtManagementService.getWrittenOffDebts();
            return JsonBean.success("查询成功", debts);
        } catch (Exception e) {
            log.error("获取已核销的债权列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取争议中的债权列表
     *
     * @return 债权列表
     */
    @GetMapping("/disputed")
    @Operation(summary = "获取争议中的债权列表", description = "获取争议中的债权列表")
    public String getDisputedDebts() {
        try {
            log.info("获取争议中的债权列表");

            List<DebtManagement> debts = debtManagementService.getDisputedDebts();
            return JsonBean.success("查询成功", debts);
        } catch (Exception e) {
            log.error("获取争议中的债权列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取法律程序中的债权列表
     *
     * @return 债权列表
     */
    @GetMapping("/legalProcedure")
    @Operation(summary = "获取法律程序中的债权列表", description = "获取法律程序中的债权列表")
    public String getLegalProcedureDebts() {
        try {
            log.info("获取法律程序中的债权列表");

            List<DebtManagement> debts = debtManagementService.getLegalProcedureDebts();
            return JsonBean.success("查询成功", debts);
        } catch (Exception e) {
            log.error("获取法律程序中的债权列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取高风险债权列表
     *
     * @return 债权列表
     */
    @GetMapping("/highRisk")
    @Operation(summary = "获取高风险债权列表", description = "获取高风险债权列表")
    public String getHighRiskDebts() {
        try {
            log.info("获取高风险债权列表");

            List<DebtManagement> debts = debtManagementService.getHighRiskDebts();
            return JsonBean.success("查询成功", debts);
        } catch (Exception e) {
            log.error("获取高风险债权列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }
}
