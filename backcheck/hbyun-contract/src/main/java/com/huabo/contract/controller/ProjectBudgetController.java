package com.huabo.contract.controller;

import java.math.BigDecimal;
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
import com.huabo.contract.entity.ProjectBudget;
import com.huabo.contract.service.ProjectBudgetService;
import com.huabo.contract.vo.ProjectBudgetQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目预算管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/budget")
@Tag(name="项目预算管理",description="项目预算管理")
@Validated
public class ProjectBudgetController {

    @Autowired
    private ProjectBudgetService projectBudgetService;

    /**
     * 分页查询项目预算列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询项目预算列表", description = "支持多条件查询和分页")
    public String getProjectBudgetList(@RequestBody ProjectBudgetQueryParam param) {
        try {
            log.info("分页查询项目预算列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNumber() == null || param.getPageNumber() <= 0) {
                param.setPageNumber(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<ProjectBudget> pageInfo = projectBudgetService.getProjectBudgetList(param);

            return JsonBean.success("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("查询项目预算列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取项目预算详情
     *
     * @param id 主键ID
     * @return 项目预算详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取项目预算详情", description = "根据ID获取详细信息")
    public String getProjectBudgetById(@PathVariable Long id) {
        try {
            log.info("获取项目预算详情，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            ProjectBudget projectBudget = projectBudgetService.getProjectBudgetById(id);
            if (projectBudget == null) {
                return JsonBean.error("项目预算不存在");
            }

            return JsonBean.success("查询成功", projectBudget);
        } catch (Exception e) {
            log.error("获取项目预算详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存项目预算（新增或修改）
     *
     * @param projectBudget 项目预算
     * @return 保存结果
     */
    @PostMapping("/save")
    @Operation(summary = "保存项目预算", description = "新增或修改项目预算")
    public String saveProjectBudget(@RequestBody ProjectBudget projectBudget) {
        try {
            log.info("保存项目预算，预算：{}", projectBudget);

            // 校验必填字段
            if (!StringUtils.hasText(projectBudget.getBudgetName())) {
                return JsonBean.error("预算名称不能为空");
            }
            if (projectBudget.getTotalBudget() == null || projectBudget.getTotalBudget().compareTo(BigDecimal.ZERO) <= 0) {
                return JsonBean.error("预算总金额必须大于0");
            }

            // 校验预算编号唯一性
            if (StringUtils.hasText(projectBudget.getBudgetNo())) {
                boolean exists = projectBudgetService.existsBudgetNo(
                    projectBudget.getBudgetNo(), projectBudget.getId());
                if (exists) {
                    return JsonBean.error("预算编号已存在，请重新输入");
                }
            }

            boolean result = projectBudgetService.saveProjectBudget(projectBudget);
            if (result) {
                return JsonBean.success("保存成功");
            } else {
                return JsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存项目预算失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 删除项目预算
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除项目预算", description = "根据ID删除项目预算")
    public String deleteProjectBudget(@PathVariable Long id) {
        try {
            log.info("删除项目预算，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            boolean result = projectBudgetService.deleteProjectBudget(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除项目预算失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除项目预算
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除项目预算", description = "根据ID列表批量删除项目预算")
    public String batchDeleteProjectBudget(@RequestBody List<Long> ids) {
        try {
            log.info("批量删除项目预算，ID列表：{}", ids);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("ID列表不能为空");
            }

            boolean result = projectBudgetService.batchDeleteProjectBudget(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除项目预算失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 审核预算
     *
     * @param request 审核请求
     * @return 审核结果
     */
    @PostMapping("/review")
    @Operation(summary = "审核预算", description = "审核项目预算")
    public String reviewBudget(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            Long reviewerId = Long.valueOf(request.get("reviewerId").toString());
            String reviewerName = (String) request.get("reviewerName");
            String reviewComments = (String) request.get("reviewComments");
            Boolean approved = (Boolean) request.get("approved");

            log.info("审核预算，ID：{}，审核人：{}，是否通过：{}", id, reviewerName, approved);

            if (id == null) {
                return JsonBean.error("预算ID不能为空");
            }
            if (reviewerId == null) {
                return JsonBean.error("审核人ID不能为空");
            }
            if (!StringUtils.hasText(reviewerName)) {
                return JsonBean.error("审核人姓名不能为空");
            }
            if (approved == null) {
                return JsonBean.error("审核结果不能为空");
            }

            boolean result = projectBudgetService.reviewBudget(id, reviewerId, reviewerName, reviewComments, approved);
            if (result) {
                return JsonBean.success("审核成功");
            } else {
                return JsonBean.error("审核失败");
            }
        } catch (Exception e) {
            log.error("审核预算失败", e);
            return JsonBean.error("审核失败：" + e.getMessage());
        }
    }

    /**
     * 批准预算
     *
     * @param request 批准请求
     * @return 批准结果
     */
    @PostMapping("/approve")
    @Operation(summary = "批准预算", description = "批准项目预算")
    public String approveBudget(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            Long approverId = Long.valueOf(request.get("approverId").toString());
            String approverName = (String) request.get("approverName");
            String approveComments = (String) request.get("approveComments");
            Boolean approved = (Boolean) request.get("approved");

            log.info("批准预算，ID：{}，批准人：{}，是否通过：{}", id, approverName, approved);

            if (id == null) {
                return JsonBean.error("预算ID不能为空");
            }
            if (approverId == null) {
                return JsonBean.error("批准人ID不能为空");
            }
            if (!StringUtils.hasText(approverName)) {
                return JsonBean.error("批准人姓名不能为空");
            }
            if (approved == null) {
                return JsonBean.error("批准结果不能为空");
            }

            boolean result = projectBudgetService.approveBudget(id, approverId, approverName, approveComments, approved);
            if (result) {
                return JsonBean.success("批准成功");
            } else {
                return JsonBean.error("批准失败");
            }
        } catch (Exception e) {
            log.error("批准预算失败", e);
            return JsonBean.error("批准失败：" + e.getMessage());
        }
    }

    /**
     * 开始执行预算
     *
     * @param id 预算ID
     * @return 执行结果
     */
    @PostMapping("/start/{id}")
    @Operation(summary = "开始执行预算", description = "开始执行项目预算")
    public String startBudget(@PathVariable Long id) {
        try {
            log.info("开始执行预算，ID：{}", id);

            if (id == null) {
                return JsonBean.error("预算ID不能为空");
            }

            boolean result = projectBudgetService.startBudget(id);
            if (result) {
                return JsonBean.success("开始执行成功");
            } else {
                return JsonBean.error("开始执行失败");
            }
        } catch (Exception e) {
            log.error("开始执行预算失败", e);
            return JsonBean.error("开始执行失败：" + e.getMessage());
        }
    }

    /**
     * 完成预算
     *
     * @param id 预算ID
     * @return 完成结果
     */
    @PostMapping("/complete/{id}")
    @Operation(summary = "完成预算", description = "完成项目预算")
    public String completeBudget(@PathVariable Long id) {
        try {
            log.info("完成预算，ID：{}", id);

            if (id == null) {
                return JsonBean.error("预算ID不能为空");
            }

            boolean result = projectBudgetService.completeBudget(id);
            if (result) {
                return JsonBean.success("完成预算成功");
            } else {
                return JsonBean.error("完成预算失败");
            }
        } catch (Exception e) {
            log.error("完成预算失败", e);
            return JsonBean.error("完成预算失败：" + e.getMessage());
        }
    }

    /**
     * 作废预算
     *
     * @param id 预算ID
     * @return 作废结果
     */
    @PostMapping("/void/{id}")
    @Operation(summary = "作废预算", description = "作废项目预算")
    public String voidBudget(@PathVariable Long id) {
        try {
            log.info("作废预算，ID：{}", id);

            if (id == null) {
                return JsonBean.error("预算ID不能为空");
            }

            boolean result = projectBudgetService.voidBudget(id);
            if (result) {
                return JsonBean.success("作废预算成功");
            } else {
                return JsonBean.error("作废预算失败");
            }
        } catch (Exception e) {
            log.error("作废预算失败", e);
            return JsonBean.error("作废预算失败：" + e.getMessage());
        }
    }

    /**
     * 更新使用金额
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/updateUsedAmount")
    @Operation(summary = "更新使用金额", description = "更新项目预算的使用金额")
    public String updateUsedAmount(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            BigDecimal usedAmount = new BigDecimal(request.get("usedAmount").toString());

            log.info("更新使用金额，ID：{}，使用金额：{}", id, usedAmount);

            if (id == null) {
                return JsonBean.error("预算ID不能为空");
            }
            if (usedAmount == null) {
                return JsonBean.error("使用金额不能为空");
            }
            if (usedAmount.compareTo(BigDecimal.ZERO) < 0) {
                return JsonBean.error("使用金额不能为负数");
            }

            boolean result = projectBudgetService.updateUsedAmount(id, usedAmount);
            if (result) {
                return JsonBean.success("使用金额更新成功");
            } else {
                return JsonBean.error("使用金额更新失败");
            }
        } catch (Exception e) {
            log.error("更新使用金额失败", e);
            return JsonBean.error("使用金额更新失败：" + e.getMessage());
        }
    }

    /**
     * 获取待审核的预算列表
     *
     * @return 待审核的预算列表
     */
    @GetMapping("/pendingReviewBudgets")
    @Operation(summary = "获取待审核的预算列表", description = "获取所有待审核的预算")
    public String getPendingReviewBudgets() {
        try {
            log.info("获取待审核的预算列表");

            List<ProjectBudget> budgets = projectBudgetService.getPendingReviewBudgets();
            return JsonBean.success("查询成功", budgets);
        } catch (Exception e) {
            log.error("获取待审核的预算列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取执行中的预算列表
     *
     * @return 执行中的预算列表
     */
    @GetMapping("/inProgressBudgets")
    @Operation(summary = "获取执行中的预算列表", description = "获取所有执行中的预算")
    public String getInProgressBudgets() {
        try {
            log.info("获取执行中的预算列表");

            List<ProjectBudget> budgets = projectBudgetService.getInProgressBudgets();
            return JsonBean.success("查询成功", budgets);
        } catch (Exception e) {
            log.error("获取执行中的预算列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取已完成的预算列表
     *
     * @return 已完成的预算列表
     */
    @GetMapping("/completedBudgets")
    @Operation(summary = "获取已完成的预算列表", description = "获取所有已完成的预算")
    public String getCompletedBudgets() {
        try {
            log.info("获取已完成的预算列表");

            List<ProjectBudget> budgets = projectBudgetService.getCompletedBudgets();
            return JsonBean.success("查询成功", budgets);
        } catch (Exception e) {
            log.error("获取已完成的预算列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取超预算的预算列表
     *
     * @return 超预算的预算列表
     */
    @GetMapping("/overBudgets")
    @Operation(summary = "获取超预算的预算列表", description = "获取所有超预算的预算")
    public String getOverBudgets() {
        try {
            log.info("获取超预算的预算列表");

            List<ProjectBudget> budgets = projectBudgetService.getOverBudgets();
            return JsonBean.success("查询成功", budgets);
        } catch (Exception e) {
            log.error("获取超预算的预算列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算紧张的预算列表
     *
     * @return 预算紧张的预算列表
     */
    @GetMapping("/tightBudgets")
    @Operation(summary = "获取预算紧张的预算列表", description = "获取所有预算紧张的预算")
    public String getTightBudgets() {
        try {
            log.info("获取预算紧张的预算列表");

            List<ProjectBudget> budgets = projectBudgetService.getTightBudgets();
            return JsonBean.success("查询成功", budgets);
        } catch (Exception e) {
            log.error("获取预算紧张的预算列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取大额预算列表
     *
     * @return 大额预算列表
     */
    @GetMapping("/largeBudgets")
    @Operation(summary = "获取大额预算列表", description = "获取所有大额预算")
    public String getLargeBudgets() {
        try {
            log.info("获取大额预算列表");

            List<ProjectBudget> budgets = projectBudgetService.getLargeBudgets(null);
            return JsonBean.success("查询成功", budgets);
        } catch (Exception e) {
            log.error("获取大额预算列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取我负责的预算列表
     *
     * @param userId 用户ID
     * @return 我负责的预算列表
     */
    @GetMapping("/myResponsibleBudgets/{userId}")
    @Operation(summary = "获取我负责的预算列表", description = "获取指定用户负责的预算")
    public String getMyResponsibleBudgets(@PathVariable Long userId) {
        try {
            log.info("获取我负责的预算列表，用户ID：{}", userId);

            if (userId == null) {
                return JsonBean.error("用户ID不能为空");
            }

            List<ProjectBudget> budgets = projectBudgetService.getMyResponsibleBudgets(userId);
            return JsonBean.success("查询成功", budgets);
        } catch (Exception e) {
            log.error("获取我负责的预算列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取我参与的预算列表
     *
     * @param userId 用户ID
     * @return 我参与的预算列表
     */
    @GetMapping("/myParticipateBudgets/{userId}")
    @Operation(summary = "获取我参与的预算列表", description = "获取指定用户参与的预算")
    public String getMyParticipateBudgets(@PathVariable Long userId) {
        try {
            log.info("获取我参与的预算列表，用户ID：{}", userId);

            if (userId == null) {
                return JsonBean.error("用户ID不能为空");
            }

            List<ProjectBudget> budgets = projectBudgetService.getMyParticipateBudgets(userId);
            return JsonBean.success("查询成功", budgets);
        } catch (Exception e) {
            log.error("获取我参与的预算列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 模糊搜索项目预算
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目预算列表
     */
    @GetMapping("/search")
    @Operation(summary = "搜索项目预算", description = "根据关键词模糊搜索项目预算")
    public String searchProjectBudgets(@RequestParam String keyword,
                                      @RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("搜索项目预算，关键词：{}，限制数量：{}", keyword, limit);

            if (!StringUtils.hasText(keyword)) {
                return JsonBean.error("搜索关键词不能为空");
            }

            List<ProjectBudget> budgets = projectBudgetService.searchProjectBudgets(keyword, limit);
            return JsonBean.success("搜索成功", budgets);
        } catch (Exception e) {
            log.error("搜索项目预算失败", e);
            return JsonBean.error("搜索失败：" + e.getMessage());
        }
    }

    /**
     * 获取项目预算统计数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics")
    @Operation(summary = "获取项目预算统计数据", description = "获取项目预算的统计信息")
    public String getProjectBudgetStatistics(@RequestBody ProjectBudgetQueryParam param) {
        try {
            log.info("获取项目预算统计数据，参数：{}", param);

            Map<String, Object> statistics = projectBudgetService.getProjectBudgetStatistics(param);
            return JsonBean.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取项目预算统计数据失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算类型分布统计
     *
     * @param param 查询参数
     * @return 预算类型分布
     */
    @PostMapping("/budgetTypeDistribution")
    @Operation(summary = "获取预算类型分布统计", description = "统计各预算类型的分布情况")
    public String getBudgetTypeDistribution(@RequestBody ProjectBudgetQueryParam param) {
        try {
            log.info("获取预算类型分布统计，参数：{}", param);

            List<Map<String, Object>> distribution = projectBudgetService.getBudgetTypeDistribution(param);
            return JsonBean.success("查询成功", distribution);
        } catch (Exception e) {
            log.error("获取预算类型分布统计失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算状态分布统计
     *
     * @param param 查询参数
     * @return 预算状态分布
     */
    @PostMapping("/budgetStatusDistribution")
    @Operation(summary = "获取预算状态分布统计", description = "统计各预算状态的分布情况")
    public String getBudgetStatusDistribution(@RequestBody ProjectBudgetQueryParam param) {
        try {
            log.info("获取预算状态分布统计，参数：{}", param);

            List<Map<String, Object>> distribution = projectBudgetService.getBudgetStatusDistribution(param);
            return JsonBean.success("查询成功", distribution);
        } catch (Exception e) {
            log.error("获取预算状态分布统计失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 生成预算编号
     *
     * @return 预算编号
     */
    @GetMapping("/generateBudgetNo")
    @Operation(summary = "生成预算编号", description = "自动生成预算编号")
    public String generateBudgetNo() {
        try {
            log.info("生成预算编号");

            String budgetNo = projectBudgetService.generateBudgetNo();
            return JsonBean.success("生成成功", budgetNo);
        } catch (Exception e) {
            log.error("生成预算编号失败", e);
            return JsonBean.error("生成失败：" + e.getMessage());
        }
    }
}
