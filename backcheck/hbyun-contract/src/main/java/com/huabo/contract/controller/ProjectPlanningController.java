package com.huabo.contract.controller;

import java.math.BigDecimal;
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
import com.huabo.contract.entity.ProjectPlanning;
import com.huabo.contract.service.ProjectPlanningService;
import com.huabo.contract.vo.ProjectPlanningQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目策划管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/planning")
@Tag(name="项目策划管理",description="项目策划管理")
@Validated
public class ProjectPlanningController {

    @Autowired
    private ProjectPlanningService projectPlanningService;

    /**
     * 分页查询项目策划列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询项目策划列表", description = "支持多条件查询和分页")
    public String getProjectPlanningList(@RequestBody ProjectPlanningQueryParam param) {
        try {
            log.info("分页查询项目策划列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNumber() == null || param.getPageNumber() <= 0) {
                param.setPageNumber(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<ProjectPlanning> pageInfo = projectPlanningService.getProjectPlanningList(param);

            return JsonBean.success("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("查询项目策划列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取项目策划详情
     *
     * @param id 主键ID
     * @return 项目策划详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取项目策划详情", description = "根据ID获取详细信息")
    public String getProjectPlanningById(@PathVariable Long id) {
        try {
            log.info("获取项目策划详情，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            ProjectPlanning projectPlanning = projectPlanningService.getProjectPlanningById(id);
            if (projectPlanning == null) {
                return JsonBean.error("项目策划不存在");
            }

            return JsonBean.success("查询成功", projectPlanning);
        } catch (Exception e) {
            log.error("获取项目策划详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存项目策划（新增或修改）
     *
     * @param projectPlanning 项目策划
     * @return 保存结果
     */
    @PostMapping("/save")
    @Operation(summary = "保存项目策划", description = "新增或修改项目策划")
    public String saveProjectPlanning(@RequestBody ProjectPlanning projectPlanning) {
        try {
            log.info("保存项目策划，策划：{}", projectPlanning);

            // 校验必填字段
            if (!StringUtils.hasText(projectPlanning.getPlanningName())) {
                return JsonBean.error("策划名称不能为空");
            }

            // 校验策划编号唯一性
            if (StringUtils.hasText(projectPlanning.getPlanningNo())) {
                boolean exists = projectPlanningService.existsPlanningNo(
                    projectPlanning.getPlanningNo(), projectPlanning.getId());
                if (exists) {
                    return JsonBean.error("策划编号已存在，请重新输入");
                }
            }

            // 设置策划人ID（如果为空，使用当前用户ID或默认值）
            if (projectPlanning.getPlannerId() == null) {
                // TODO: 从当前登录用户获取用户ID，这里暂时使用默认值
                projectPlanning.setPlannerId(1L);
            }

            // 设置策划日期（如果为空，使用当前日期）
            if (projectPlanning.getPlanningDate() == null) {
                projectPlanning.setPlanningDate(new Date());
            }

            // 设置创建人和更新人（如果为空，使用默认值）
            if (projectPlanning.getCreateBy() == null) {
                // TODO: 从当前登录用户获取用户ID，这里暂时使用默认值
                projectPlanning.setCreateBy(1L);
            }
            if (projectPlanning.getUpdateBy() == null) {
                projectPlanning.setUpdateBy(1L);
            }

            boolean result = projectPlanningService.saveProjectPlanning(projectPlanning);
            if (result) {
                return JsonBean.success("保存成功");
            } else {
                return JsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存项目策划失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 删除项目策划
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除项目策划", description = "根据ID删除项目策划")
    public String deleteProjectPlanning(@PathVariable Long id) {
        try {
            log.info("删除项目策划，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            boolean result = projectPlanningService.deleteProjectPlanning(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除项目策划失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 项目策划审核
     *
     * @param reviewData 审核数据
     * @return 审核结果
     */
    @PostMapping("/review")
    @Operation(summary = "项目策划审核", description = "审核项目策划")
    public String reviewProjectPlanning(@RequestBody Map<String, Object> reviewData) {
        try {
            log.info("项目策划审核，审核数据：{}", reviewData);

            Long id = Long.valueOf(reviewData.get("id").toString());
            if (id == null) {
                return JsonBean.error("策划ID不能为空");
            }

            String reviewComments = (String) reviewData.get("reviewComments");
            Integer reviewStatus = (Integer) reviewData.get("reviewStatus");

            if (reviewStatus == null) {
                return JsonBean.error("审核状态不能为空");
            }

            // 转换为reviewPlanning方法的参数格式
            boolean result = projectPlanningService.reviewPlanning(id, 1L, "系统", reviewComments, reviewStatus == 1);
            if (result) {
                return JsonBean.success("审核成功");
            } else {
                return JsonBean.error("审核失败");
            }
        } catch (Exception e) {
            log.error("项目策划审核失败", e);
            return JsonBean.error("审核失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除项目策划
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除项目策划", description = "根据ID列表批量删除项目策划")
    public String batchDeleteProjectPlanning(@RequestBody List<Long> ids) {
        try {
            log.info("批量删除项目策划，ID列表：{}", ids);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("ID列表不能为空");
            }

            boolean result = projectPlanningService.batchDeleteProjectPlanning(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除项目策划失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }



    /**
     * 开始执行策划
     *
     * @param id 策划ID
     * @return 执行结果
     */
    @PostMapping("/start/{id}")
    @Operation(summary = "开始执行策划", description = "开始执行项目策划")
    public String startPlanning(@PathVariable Long id) {
        try {
            log.info("开始执行策划，ID：{}", id);

            if (id == null) {
                return JsonBean.error("策划ID不能为空");
            }

            boolean result = projectPlanningService.startPlanning(id);
            if (result) {
                return JsonBean.success("开始执行成功");
            } else {
                return JsonBean.error("开始执行失败");
            }
        } catch (Exception e) {
            log.error("开始执行策划失败", e);
            return JsonBean.error("开始执行失败：" + e.getMessage());
        }
    }

    /**
     * 完成策划
     *
     * @param id 策划ID
     * @return 完成结果
     */
    @PostMapping("/complete/{id}")
    @Operation(summary = "完成策划", description = "完成项目策划")
    public String completePlanning(@PathVariable Long id) {
        try {
            log.info("完成策划，ID：{}", id);

            if (id == null) {
                return JsonBean.error("策划ID不能为空");
            }

            boolean result = projectPlanningService.completePlanning(id);
            if (result) {
                return JsonBean.success("完成策划成功");
            } else {
                return JsonBean.error("完成策划失败");
            }
        } catch (Exception e) {
            log.error("完成策划失败", e);
            return JsonBean.error("完成策划失败：" + e.getMessage());
        }
    }

    /**
     * 暂停策划
     *
     * @param id 策划ID
     * @return 暂停结果
     */
    @PostMapping("/pause/{id}")
    @Operation(summary = "暂停策划", description = "暂停项目策划")
    public String pausePlanning(@PathVariable Long id) {
        try {
            log.info("暂停策划，ID：{}", id);

            if (id == null) {
                return JsonBean.error("策划ID不能为空");
            }

            boolean result = projectPlanningService.pausePlanning(id);
            if (result) {
                return JsonBean.success("暂停策划成功");
            } else {
                return JsonBean.error("暂停策划失败");
            }
        } catch (Exception e) {
            log.error("暂停策划失败", e);
            return JsonBean.error("暂停策划失败：" + e.getMessage());
        }
    }

    /**
     * 取消策划
     *
     * @param id 策划ID
     * @return 取消结果
     */
    @PostMapping("/cancel/{id}")
    @Operation(summary = "取消策划", description = "取消项目策划")
    public String cancelPlanning(@PathVariable Long id) {
        try {
            log.info("取消策划，ID：{}", id);

            if (id == null) {
                return JsonBean.error("策划ID不能为空");
            }

            boolean result = projectPlanningService.cancelPlanning(id);
            if (result) {
                return JsonBean.success("取消策划成功");
            } else {
                return JsonBean.error("取消策划失败");
            }
        } catch (Exception e) {
            log.error("取消策划失败", e);
            return JsonBean.error("取消策划失败：" + e.getMessage());
        }
    }

    /**
     * 更新完成度
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/updateCompletionRate")
    @Operation(summary = "更新完成度", description = "更新项目策划的完成度")
    public String updateCompletionRate(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            BigDecimal completionRate = new BigDecimal(request.get("completionRate").toString());

            log.info("更新完成度，ID：{}，完成度：{}", id, completionRate);

            if (id == null) {
                return JsonBean.error("策划ID不能为空");
            }
            if (completionRate == null) {
                return JsonBean.error("完成度不能为空");
            }
            if (completionRate.compareTo(BigDecimal.ZERO) < 0 || completionRate.compareTo(new BigDecimal("100")) > 0) {
                return JsonBean.error("完成度必须在0-100之间");
            }

            boolean result = projectPlanningService.updateCompletionRate(id, completionRate);
            if (result) {
                return JsonBean.success("完成度更新成功");
            } else {
                return JsonBean.error("完成度更新失败");
            }
        } catch (Exception e) {
            log.error("更新完成度失败", e);
            return JsonBean.error("完成度更新失败：" + e.getMessage());
        }
    }

    /**
     * 获取待审核的策划列表
     *
     * @return 待审核的策划列表
     */
    @GetMapping("/pendingReviewPlannings")
    @Operation(summary = "获取待审核的策划列表", description = "获取所有待审核的策划")
    public String getPendingReviewPlannings() {
        try {
            log.info("获取待审核的策划列表");

            List<ProjectPlanning> plannings = projectPlanningService.getPendingReviewPlannings();
            return JsonBean.success("查询成功", plannings);
        } catch (Exception e) {
            log.error("获取待审核的策划列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取执行中的策划列表
     *
     * @return 执行中的策划列表
     */
    @GetMapping("/inProgressPlannings")
    @Operation(summary = "获取执行中的策划列表", description = "获取所有执行中的策划")
    public String getInProgressPlannings() {
        try {
            log.info("获取执行中的策划列表");

            List<ProjectPlanning> plannings = projectPlanningService.getInProgressPlannings();
            return JsonBean.success("查询成功", plannings);
        } catch (Exception e) {
            log.error("获取执行中的策划列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取已完成的策划列表
     *
     * @return 已完成的策划列表
     */
    @GetMapping("/completedPlannings")
    @Operation(summary = "获取已完成的策划列表", description = "获取所有已完成的策划")
    public String getCompletedPlannings() {
        try {
            log.info("获取已完成的策划列表");

            List<ProjectPlanning> plannings = projectPlanningService.getCompletedPlannings();
            return JsonBean.success("查询成功", plannings);
        } catch (Exception e) {
            log.error("获取已完成的策划列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取延期的策划列表
     *
     * @return 延期的策划列表
     */
    @GetMapping("/delayedPlannings")
    @Operation(summary = "获取延期的策划列表", description = "获取所有延期的策划")
    public String getDelayedPlannings() {
        try {
            log.info("获取延期的策划列表");

            List<ProjectPlanning> plannings = projectPlanningService.getDelayedPlannings();
            return JsonBean.success("查询成功", plannings);
        } catch (Exception e) {
            log.error("获取延期的策划列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取即将到期的策划列表
     *
     * @param days 天数
     * @return 即将到期的策划列表
     */
    @GetMapping("/expiringSoonPlannings")
    @Operation(summary = "获取即将到期的策划列表", description = "获取即将到期的策划")
    public String getExpiringSoonPlannings(@RequestParam(defaultValue = "7") Integer days) {
        try {
            log.info("获取即将到期的策划列表，天数：{}", days);

            List<ProjectPlanning> plannings = projectPlanningService.getExpiringSoonPlannings(days);
            return JsonBean.success("查询成功", plannings);
        } catch (Exception e) {
            log.error("获取即将到期的策划列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取重点策划列表
     *
     * @return 重点策划列表
     */
    @GetMapping("/keyPlannings")
    @Operation(summary = "获取重点策划列表", description = "获取所有重点策划")
    public String getKeyPlannings() {
        try {
            log.info("获取重点策划列表");

            List<ProjectPlanning> plannings = projectPlanningService.getKeyPlannings(null);
            return JsonBean.success("查询成功", plannings);
        } catch (Exception e) {
            log.error("获取重点策划列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取我负责的策划列表
     *
     * @param userId 用户ID
     * @return 我负责的策划列表
     */
    @GetMapping("/myResponsiblePlannings/{userId}")
    @Operation(summary = "获取我负责的策划列表", description = "获取指定用户负责的策划")
    public String getMyResponsiblePlannings(@PathVariable Long userId) {
        try {
            log.info("获取我负责的策划列表，用户ID：{}", userId);

            if (userId == null) {
                return JsonBean.error("用户ID不能为空");
            }

            List<ProjectPlanning> plannings = projectPlanningService.getMyResponsiblePlannings(userId);
            return JsonBean.success("查询成功", plannings);
        } catch (Exception e) {
            log.error("获取我负责的策划列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取我参与的策划列表
     *
     * @param userId 用户ID
     * @return 我参与的策划列表
     */
    @GetMapping("/myParticipatePlannings/{userId}")
    @Operation(summary = "获取我参与的策划列表", description = "获取指定用户参与的策划")
    public String getMyParticipatePlannings(@PathVariable Long userId) {
        try {
            log.info("获取我参与的策划列表，用户ID：{}", userId);

            if (userId == null) {
                return JsonBean.error("用户ID不能为空");
            }

            List<ProjectPlanning> plannings = projectPlanningService.getMyParticipatePlannings(userId);
            return JsonBean.success("查询成功", plannings);
        } catch (Exception e) {
            log.error("获取我参与的策划列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 模糊搜索项目策划
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目策划列表
     */
    @GetMapping("/search")
    @Operation(summary = "搜索项目策划", description = "根据关键词模糊搜索项目策划")
    public String searchProjectPlannings(@RequestParam String keyword,
                                        @RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("搜索项目策划，关键词：{}，限制数量：{}", keyword, limit);

            if (!StringUtils.hasText(keyword)) {
                return JsonBean.error("搜索关键词不能为空");
            }

            List<ProjectPlanning> plannings = projectPlanningService.searchProjectPlannings(keyword, limit);
            return JsonBean.success("搜索成功", plannings);
        } catch (Exception e) {
            log.error("搜索项目策划失败", e);
            return JsonBean.error("搜索失败：" + e.getMessage());
        }
    }

    /**
     * 获取项目策划统计数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics")
    @Operation(summary = "获取项目策划统计数据", description = "获取项目策划的统计信息")
    public String getProjectPlanningStatistics(@RequestBody ProjectPlanningQueryParam param) {
        try {
            log.info("获取项目策划统计数据，参数：{}", param);

            Map<String, Object> statistics = projectPlanningService.getProjectPlanningStatistics(param);
            return JsonBean.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取项目策划统计数据失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取策划类型分布统计
     *
     * @param param 查询参数
     * @return 策划类型分布
     */
    @PostMapping("/planningTypeDistribution")
    @Operation(summary = "获取策划类型分布统计", description = "统计各策划类型的分布情况")
    public String getPlanningTypeDistribution(@RequestBody ProjectPlanningQueryParam param) {
        try {
            log.info("获取策划类型分布统计，参数：{}", param);

            List<Map<String, Object>> distribution = projectPlanningService.getPlanningTypeDistribution(param);
            return JsonBean.success("查询成功", distribution);
        } catch (Exception e) {
            log.error("获取策划类型分布统计失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取策划状态分布统计
     *
     * @param param 查询参数
     * @return 策划状态分布
     */
    @PostMapping("/planningStatusDistribution")
    @Operation(summary = "获取策划状态分布统计", description = "统计各策划状态的分布情况")
    public String getPlanningStatusDistribution(@RequestBody ProjectPlanningQueryParam param) {
        try {
            log.info("获取策划状态分布统计，参数：{}", param);

            List<Map<String, Object>> distribution = projectPlanningService.getPlanningStatusDistribution(param);
            return JsonBean.success("查询成功", distribution);
        } catch (Exception e) {
            log.error("获取策划状态分布统计失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取今日到期的策划列表
     *
     * @return 今日到期的策划列表
     */
    @GetMapping("/todayExpiringPlannings")
    @Operation(summary = "获取今日到期的策划列表", description = "获取今天到期的策划")
    public String getTodayExpiringPlannings() {
        try {
            log.info("获取今日到期的策划列表");

            List<ProjectPlanning> plannings = projectPlanningService.getTodayExpiringPlannings();
            return JsonBean.success("查询成功", plannings);
        } catch (Exception e) {
            log.error("获取今日到期的策划列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取本周到期的策划列表
     *
     * @return 本周到期的策划列表
     */
    @GetMapping("/thisWeekExpiringPlannings")
    @Operation(summary = "获取本周到期的策划列表", description = "获取本周到期的策划")
    public String getThisWeekExpiringPlannings() {
        try {
            log.info("获取本周到期的策划列表");

            List<ProjectPlanning> plannings = projectPlanningService.getThisWeekExpiringPlannings();
            return JsonBean.success("查询成功", plannings);
        } catch (Exception e) {
            log.error("获取本周到期的策划列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 生成策划编号
     *
     * @return 策划编号
     */
    @GetMapping("/generatePlanningNo")
    @Operation(summary = "生成策划编号", description = "自动生成策划编号")
    public String generatePlanningNo() {
        try {
            log.info("生成策划编号");

            String planningNo = projectPlanningService.generatePlanningNo();
            return JsonBean.success("生成成功", planningNo);
        } catch (Exception e) {
            log.error("生成策划编号失败", e);
            return JsonBean.error("生成失败：" + e.getMessage());
        }
    }
}
