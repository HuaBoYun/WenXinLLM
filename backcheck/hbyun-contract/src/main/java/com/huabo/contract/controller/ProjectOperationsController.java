package com.huabo.contract.controller;

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
import com.huabo.contract.entity.ProjectOperations;
import com.huabo.contract.service.ProjectOperationsService;
import com.huabo.contract.vo.ProjectOperationsQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目经营管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/operations")
@Tag(name="项目经营管理",description="项目经营管理")
@Validated
public class ProjectOperationsController {

    @Autowired
    private ProjectOperationsService projectOperationsService;

    /**
     * 分页查询项目经营管理列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询项目经营管理列表", description = "支持多条件查询和分页")
    public String getProjectOperationsList(@RequestBody ProjectOperationsQueryParam param) {
        try {
            log.info("分页查询项目经营管理列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNumber() == null || param.getPageNumber() <= 0) {
                param.setPageNumber(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<ProjectOperations> pageInfo = projectOperationsService.getProjectOperationsList(param);

            return JsonBean.success("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("查询项目经营管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取项目经营管理详情
     *
     * @param id 主键ID
     * @return 项目经营管理详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取项目经营管理详情", description = "根据ID获取详细信息")
    public String getProjectOperationsById(@PathVariable Long id) {
        try {
            log.info("获取项目经营管理详情，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            ProjectOperations projectOperations = projectOperationsService.getProjectOperationsById(id);
            if (projectOperations == null) {
                return JsonBean.error("项目经营管理不存在");
            }

            return JsonBean.success("查询成功", projectOperations);
        } catch (Exception e) {
            log.error("获取项目经营管理详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存项目经营管理（新增或修改）
     *
     * @param projectOperations 项目经营管理
     * @return 保存结果
     */
    @PostMapping("/save")
    @Operation(summary = "保存项目经营管理", description = "新增或修改项目经营管理")
    public String saveProjectOperations(@RequestBody ProjectOperations projectOperations) {
        try {
            log.info("保存项目经营管理，经营：{}", projectOperations);

            // 校验必填字段
            if (projectOperations.getProjectId() == null) {
                return JsonBean.error("项目ID不能为空");
            }
            if (projectOperations.getOperationsType() == null) {
                return JsonBean.error("经营类型不能为空");
            }

            // 校验经营编号唯一性
            if (StringUtils.hasText(projectOperations.getOperationsNo())) {
                boolean exists = projectOperationsService.existsOperationsNo(
                    projectOperations.getOperationsNo(), projectOperations.getId());
                if (exists) {
                    return JsonBean.error("经营编号已存在，请重新输入");
                }
            }

            boolean result = projectOperationsService.saveProjectOperations(projectOperations);
            if (result) {
                return JsonBean.success("保存成功");
            } else {
                return JsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存项目经营管理失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 删除项目经营管理
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除项目经营管理", description = "根据ID删除项目经营管理")
    public String deleteProjectOperations(@PathVariable Long id) {
        try {
            log.info("删除项目经营管理，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            boolean result = projectOperationsService.deleteProjectOperations(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除项目经营管理失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除项目经营管理
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除项目经营管理", description = "根据ID列表批量删除项目经营管理")
    public String batchDeleteProjectOperations(@RequestBody List<Long> ids) {
        try {
            log.info("批量删除项目经营管理，ID列表：{}", ids);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("ID列表不能为空");
            }

            boolean result = projectOperationsService.batchDeleteProjectOperations(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除项目经营管理失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据项目ID查询项目经营管理列表
     *
     * @param projectId 项目ID
     * @return 项目经营管理列表
     */
    @GetMapping("/project/{projectId}")
    @Operation(summary = "根据项目ID查询项目经营管理列表", description = "根据项目ID查询项目经营管理列表")
    public String getProjectOperationsByProjectId(@PathVariable Long projectId) {
        try {
            log.info("根据项目ID查询项目经营管理列表，项目ID：{}", projectId);

            if (projectId == null) {
                return JsonBean.error("项目ID不能为空");
            }

            List<ProjectOperations> operations = projectOperationsService.getProjectOperationsByProjectId(projectId);
            return JsonBean.success("查询成功", operations);
        } catch (Exception e) {
            log.error("根据项目ID查询项目经营管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据经营类型查询项目经营管理列表
     *
     * @param operationsType 经营类型
     * @return 项目经营管理列表
     */
    @GetMapping("/type/{operationsType}")
    @Operation(summary = "根据经营类型查询项目经营管理列表", description = "根据经营类型查询项目经营管理列表")
    public String getProjectOperationsByOperationsType(@PathVariable Integer operationsType) {
        try {
            log.info("根据经营类型查询项目经营管理列表，经营类型：{}", operationsType);

            if (operationsType == null) {
                return JsonBean.error("经营类型不能为空");
            }

            List<ProjectOperations> operations = projectOperationsService.getProjectOperationsByOperationsType(operationsType);
            return JsonBean.success("查询成功", operations);
        } catch (Exception e) {
            log.error("根据经营类型查询项目经营管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据经营状态查询项目经营管理列表
     *
     * @param operationsStatus 经营状态
     * @return 项目经营管理列表
     */
    @GetMapping("/status/{operationsStatus}")
    @Operation(summary = "根据经营状态查询项目经营管理列表", description = "根据经营状态查询项目经营管理列表")
    public String getProjectOperationsByOperationsStatus(@PathVariable Integer operationsStatus) {
        try {
            log.info("根据经营状态查询项目经营管理列表，经营状态：{}", operationsStatus);

            if (operationsStatus == null) {
                return JsonBean.error("经营状态不能为空");
            }

            List<ProjectOperations> operations = projectOperationsService.getProjectOperationsByOperationsStatus(operationsStatus);
            return JsonBean.success("查询成功", operations);
        } catch (Exception e) {
            log.error("根据经营状态查询项目经营管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据风险等级查询项目经营管理列表
     *
     * @param riskLevel 风险等级
     * @return 项目经营管理列表
     */
    @GetMapping("/risk/{riskLevel}")
    @Operation(summary = "根据风险等级查询项目经营管理列表", description = "根据风险等级查询项目经营管理列表")
    public String getProjectOperationsByRiskLevel(@PathVariable Integer riskLevel) {
        try {
            log.info("根据风险等级查询项目经营管理列表，风险等级：{}", riskLevel);

            if (riskLevel == null) {
                return JsonBean.error("风险等级不能为空");
            }

            List<ProjectOperations> operations = projectOperationsService.getProjectOperationsByRiskLevel(riskLevel);
            return JsonBean.success("查询成功", operations);
        } catch (Exception e) {
            log.error("根据风险等级查询项目经营管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取我的项目经营管理列表
     *
     * @param managerId 负责人ID
     * @return 项目经营管理列表
     */
    @GetMapping("/my/{managerId}")
    @Operation(summary = "获取我的项目经营管理列表", description = "获取我的项目经营管理列表")
    public String getMyProjectOperations(@PathVariable Long managerId) {
        try {
            log.info("获取我的项目经营管理列表，负责人ID：{}", managerId);

            if (managerId == null) {
                return JsonBean.error("负责人ID不能为空");
            }

            List<ProjectOperations> operations = projectOperationsService.getMyProjectOperations(managerId);
            return JsonBean.success("查询成功", operations);
        } catch (Exception e) {
            log.error("获取我的项目经营管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取正常状态的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    @GetMapping("/normal")
    @Operation(summary = "获取正常状态的项目经营管理列表", description = "获取正常状态的项目经营管理列表")
    public String getNormalOperations() {
        try {
            log.info("获取正常状态的项目经营管理列表");

            List<ProjectOperations> operations = projectOperationsService.getNormalOperations();
            return JsonBean.success("查询成功", operations);
        } catch (Exception e) {
            log.error("获取正常状态的项目经营管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取预警状态的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    @GetMapping("/warning")
    @Operation(summary = "获取预警状态的项目经营管理列表", description = "获取预警状态的项目经营管理列表")
    public String getWarningOperations() {
        try {
            log.info("获取预警状态的项目经营管理列表");

            List<ProjectOperations> operations = projectOperationsService.getWarningOperations();
            return JsonBean.success("查询成功", operations);
        } catch (Exception e) {
            log.error("获取预警状态的项目经营管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取异常状态的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    @GetMapping("/abnormal")
    @Operation(summary = "获取异常状态的项目经营管理列表", description = "获取异常状态的项目经营管理列表")
    public String getAbnormalOperations() {
        try {
            log.info("获取异常状态的项目经营管理列表");

            List<ProjectOperations> operations = projectOperationsService.getAbnormalOperations();
            return JsonBean.success("查询成功", operations);
        } catch (Exception e) {
            log.error("获取异常状态的项目经营管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取高风险的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    @GetMapping("/highRisk")
    @Operation(summary = "获取高风险的项目经营管理列表", description = "获取高风险的项目经营管理列表")
    public String getHighRiskOperations() {
        try {
            log.info("获取高风险的项目经营管理列表");

            List<ProjectOperations> operations = projectOperationsService.getHighRiskOperations();
            return JsonBean.success("查询成功", operations);
        } catch (Exception e) {
            log.error("获取高风险的项目经营管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取盈利的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    @GetMapping("/profitable")
    @Operation(summary = "获取盈利的项目经营管理列表", description = "获取盈利的项目经营管理列表")
    public String getProfitableOperations() {
        try {
            log.info("获取盈利的项目经营管理列表");

            List<ProjectOperations> operations = projectOperationsService.getProfitableOperations();
            return JsonBean.success("查询成功", operations);
        } catch (Exception e) {
            log.error("获取盈利的项目经营管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取亏损的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    @GetMapping("/loss")
    @Operation(summary = "获取亏损的项目经营管理列表", description = "获取亏损的项目经营管理列表")
    public String getLossOperations() {
        try {
            log.info("获取亏损的项目经营管理列表");

            List<ProjectOperations> operations = projectOperationsService.getLossOperations();
            return JsonBean.success("查询成功", operations);
        } catch (Exception e) {
            log.error("获取亏损的项目经营管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 模糊搜索项目经营管理
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目经营管理列表
     */
    @GetMapping("/search")
    @Operation(summary = "模糊搜索项目经营管理", description = "模糊搜索项目经营管理")
    public String searchProjectOperations(@RequestParam String keyword,
                                         @RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("模糊搜索项目经营管理，关键词：{}，限制数量：{}", keyword, limit);

            if (!StringUtils.hasText(keyword)) {
                return JsonBean.error("搜索关键词不能为空");
            }

            List<ProjectOperations> operations = projectOperationsService.searchProjectOperations(keyword, limit);
            return JsonBean.success("搜索成功", operations);
        } catch (Exception e) {
            log.error("模糊搜索项目经营管理失败", e);
            return JsonBean.error("搜索失败：" + e.getMessage());
        }
    }

    /**
     * 统计项目经营管理数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics")
    @Operation(summary = "统计项目经营管理数据", description = "统计项目经营管理数据")
    public String getProjectOperationsStatistics(@RequestBody ProjectOperationsQueryParam param) {
        try {
            log.info("统计项目经营管理数据，参数：{}", param);

            Map<String, Object> statistics = projectOperationsService.getProjectOperationsStatistics(param);
            return JsonBean.success("统计成功", statistics);
        } catch (Exception e) {
            log.error("统计项目经营管理数据失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 统计经营类型分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics/typeDistribution")
    @Operation(summary = "统计经营类型分布", description = "统计经营类型分布")
    public String getOperationsTypeDistribution(@RequestBody ProjectOperationsQueryParam param) {
        try {
            log.info("统计经营类型分布，参数：{}", param);

            List<Map<String, Object>> distribution = projectOperationsService.getOperationsTypeDistribution(param);
            return JsonBean.success("统计成功", distribution);
        } catch (Exception e) {
            log.error("统计经营类型分布失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 统计经营状态分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics/statusDistribution")
    @Operation(summary = "统计经营状态分布", description = "统计经营状态分布")
    public String getOperationsStatusDistribution(@RequestBody ProjectOperationsQueryParam param) {
        try {
            log.info("统计经营状态分布，参数：{}", param);

            List<Map<String, Object>> distribution = projectOperationsService.getOperationsStatusDistribution(param);
            return JsonBean.success("统计成功", distribution);
        } catch (Exception e) {
            log.error("统计经营状态分布失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 统计风险等级分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics/riskDistribution")
    @Operation(summary = "统计风险等级分布", description = "统计风险等级分布")
    public String getRiskLevelDistribution(@RequestBody ProjectOperationsQueryParam param) {
        try {
            log.info("统计风险等级分布，参数：{}", param);

            List<Map<String, Object>> distribution = projectOperationsService.getRiskLevelDistribution(param);
            return JsonBean.success("统计成功", distribution);
        } catch (Exception e) {
            log.error("统计风险等级分布失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 统计月度收入趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics/revenueTrend")
    @Operation(summary = "统计月度收入趋势", description = "统计月度收入趋势")
    public String getMonthlyRevenueTrend(@RequestBody ProjectOperationsQueryParam param) {
        try {
            log.info("统计月度收入趋势，参数：{}", param);

            List<Map<String, Object>> trend = projectOperationsService.getMonthlyRevenueTrend(param);
            return JsonBean.success("统计成功", trend);
        } catch (Exception e) {
            log.error("统计月度收入趋势失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 统计月度成本趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics/costTrend")
    @Operation(summary = "统计月度成本趋势", description = "统计月度成本趋势")
    public String getMonthlyCostTrend(@RequestBody ProjectOperationsQueryParam param) {
        try {
            log.info("统计月度成本趋势，参数：{}", param);

            List<Map<String, Object>> trend = projectOperationsService.getMonthlyCostTrend(param);
            return JsonBean.success("统计成功", trend);
        } catch (Exception e) {
            log.error("统计月度成本趋势失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 统计月度利润趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics/profitTrend")
    @Operation(summary = "统计月度利润趋势", description = "统计月度利润趋势")
    public String getMonthlyProfitTrend(@RequestBody ProjectOperationsQueryParam param) {
        try {
            log.info("统计月度利润趋势，参数：{}", param);

            List<Map<String, Object>> trend = projectOperationsService.getMonthlyProfitTrend(param);
            return JsonBean.success("统计成功", trend);
        } catch (Exception e) {
            log.error("统计月度利润趋势失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 导出项目经营管理数据
     *
     * @param param 查询参数
     * @return 项目经营管理列表
     */
    @PostMapping("/export")
    @Operation(summary = "导出项目经营管理数据", description = "导出项目经营管理数据")
    public String exportProjectOperations(@RequestBody ProjectOperationsQueryParam param) {
        try {
            log.info("导出项目经营管理数据，参数：{}", param);

            List<ProjectOperations> operations = projectOperationsService.exportProjectOperations(param);
            return JsonBean.success("导出成功", operations);
        } catch (Exception e) {
            log.error("导出项目经营管理数据失败", e);
            return JsonBean.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新经营状态
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/batchUpdateStatus")
    @Operation(summary = "批量更新经营状态", description = "批量更新经营状态")
    public String batchUpdateOperationsStatus(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) request.get("ids");
            Integer operationsStatus = (Integer) request.get("operationsStatus");
            Long updateBy = request.get("updateBy") != null ? Long.valueOf(request.get("updateBy").toString()) : null;

            log.info("批量更新经营状态，ID列表：{}，经营状态：{}，更新人：{}", ids, operationsStatus, updateBy);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("ID列表不能为空");
            }
            if (operationsStatus == null) {
                return JsonBean.error("经营状态不能为空");
            }

            boolean result = projectOperationsService.batchUpdateOperationsStatus(ids, operationsStatus, updateBy);
            if (result) {
                return JsonBean.success("批量更新成功");
            } else {
                return JsonBean.error("批量更新失败");
            }
        } catch (Exception e) {
            log.error("批量更新经营状态失败", e);
            return JsonBean.error("批量更新失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新风险等级
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/batchUpdateRisk")
    @Operation(summary = "批量更新风险等级", description = "批量更新风险等级")
    public String batchUpdateRiskLevel(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) request.get("ids");
            Integer riskLevel = (Integer) request.get("riskLevel");
            Long updateBy = request.get("updateBy") != null ? Long.valueOf(request.get("updateBy").toString()) : null;

            log.info("批量更新风险等级，ID列表：{}，风险等级：{}，更新人：{}", ids, riskLevel, updateBy);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("ID列表不能为空");
            }
            if (riskLevel == null) {
                return JsonBean.error("风险等级不能为空");
            }

            boolean result = projectOperationsService.batchUpdateRiskLevel(ids, riskLevel, updateBy);
            if (result) {
                return JsonBean.success("批量更新成功");
            } else {
                return JsonBean.error("批量更新失败");
            }
        } catch (Exception e) {
            log.error("批量更新风险等级失败", e);
            return JsonBean.error("批量更新失败：" + e.getMessage());
        }
    }
}
