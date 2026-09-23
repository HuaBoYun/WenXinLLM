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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.ProjectBriefing;
import com.huabo.contract.service.ProjectBriefingService;
import com.huabo.contract.vo.ProjectBriefingQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目交底管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/briefing")
@Tag(name="项目交底管理",description="项目交底管理")
@Validated
public class ProjectBriefingController {

    @Autowired
    private ProjectBriefingService projectBriefingService;

    /**
     * 分页查询项目交底列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询项目交底列表", description = "支持多条件查询和分页")
    public String getProjectBriefingList(@RequestBody ProjectBriefingQueryParam param) {
        try {
            log.info("分页查询项目交底列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNumber() == null || param.getPageNumber() <= 0) {
                param.setPageNumber(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<ProjectBriefing> pageInfo = projectBriefingService.getProjectBriefingList(param);

            return JsonBean.success("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("查询项目交底列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取项目交底详情
     *
     * @param id 主键ID
     * @return 项目交底详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取项目交底详情", description = "根据ID获取详细信息")
    public String getProjectBriefingById(@PathVariable Long id) {
        try {
            log.info("获取项目交底详情，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            ProjectBriefing projectBriefing = projectBriefingService.getProjectBriefingById(id);
            if (projectBriefing == null) {
                return JsonBean.error("项目交底不存在");
            }

            return JsonBean.success("查询成功", projectBriefing);
        } catch (Exception e) {
            log.error("获取项目交底详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存项目交底（新增或修改）
     *
     * @param projectBriefing 项目交底
     * @return 保存结果
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST, RequestMethod.PUT})
    @Operation(summary = "保存项目交底", description = "新增或修改项目交底")
    public String saveProjectBriefing(@RequestBody ProjectBriefing projectBriefing) {
        try {
            log.info("保存项目交底，交底：{}", projectBriefing);

            // 校验必填字段
            if (!StringUtils.hasText(projectBriefing.getBriefingTitle())) {
                return JsonBean.error("交底标题不能为空");
            }

            // 校验交底编号唯一性
            if (StringUtils.hasText(projectBriefing.getBriefingNo())) {
                boolean exists = projectBriefingService.existsBriefingNo(
                    projectBriefing.getBriefingNo(), projectBriefing.getId());
                if (exists) {
                    return JsonBean.error("交底编号已存在，请重新输入");
                }
            }

            boolean result = projectBriefingService.saveProjectBriefing(projectBriefing);
            if (result) {
                return JsonBean.success("保存成功");
            } else {
                return JsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存项目交底失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 更新项目交底
     *
     * @param id 主键ID
     * @param projectBriefing 项目交底
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新项目交底", description = "根据ID更新项目交底")
    public String updateProjectBriefing(@PathVariable Long id, @RequestBody ProjectBriefing projectBriefing) {
        try {
            log.info("更新项目交底，ID：{}，交底：{}", id, projectBriefing);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            // 设置ID确保更新正确的记录
            projectBriefing.setId(id);

            // 校验必填字段
            if (!StringUtils.hasText(projectBriefing.getBriefingTitle())) {
                return JsonBean.error("交底标题不能为空");
            }

            // 校验交底编号唯一性
            if (StringUtils.hasText(projectBriefing.getBriefingNo())) {
                boolean exists = projectBriefingService.existsBriefingNo(
                    projectBriefing.getBriefingNo(), projectBriefing.getId());
                if (exists) {
                    return JsonBean.error("交底编号已存在，请重新输入");
                }
            }

            boolean result = projectBriefingService.saveProjectBriefing(projectBriefing);
            if (result) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新项目交底失败", e);
            return JsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除项目交底
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除项目交底", description = "根据ID删除项目交底")
    public String deleteProjectBriefing(@PathVariable Long id) {
        try {
            log.info("删除项目交底，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            boolean result = projectBriefingService.deleteProjectBriefing(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除项目交底失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除项目交底
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除项目交底", description = "根据ID列表批量删除项目交底")
    public String batchDeleteProjectBriefing(@RequestBody List<Long> ids) {
        try {
            log.info("批量删除项目交底，ID列表：{}", ids);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("ID列表不能为空");
            }

            boolean result = projectBriefingService.batchDeleteProjectBriefing(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除项目交底失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 进行交底
     *
     * @param request 交底请求
     * @return 交底结果
     */
    @PostMapping("/conduct")
    @Operation(summary = "进行交底", description = "进行交底")
    public String conductBriefing(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            Long brieferId = Long.valueOf(request.get("brieferId").toString());
            String brieferName = (String) request.get("brieferName");
            String briefingLocation = (String) request.get("briefingLocation");
            String participants = (String) request.get("participants");

            log.info("进行交底，ID：{}，交底人：{}，交底地点：{}", id, brieferName, briefingLocation);

            if (id == null) {
                return JsonBean.error("交底ID不能为空");
            }
            if (brieferId == null) {
                return JsonBean.error("交底人ID不能为空");
            }
            if (!StringUtils.hasText(brieferName)) {
                return JsonBean.error("交底人姓名不能为空");
            }

            boolean result = projectBriefingService.conductBriefing(id, brieferId, brieferName, briefingLocation, participants);
            if (result) {
                return JsonBean.success("交底成功");
            } else {
                return JsonBean.error("交底失败");
            }
        } catch (Exception e) {
            log.error("进行交底失败", e);
            return JsonBean.error("交底失败：" + e.getMessage());
        }
    }

    /**
     * 接收交底
     *
     * @param request 接收请求
     * @return 接收结果
     */
    @PostMapping("/receive")
    @Operation(summary = "接收交底", description = "接收交底")
    public String receiveBriefing(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            Long receiverId = Long.valueOf(request.get("receiverId").toString());
            String receiverName = (String) request.get("receiverName");

            log.info("接收交底，ID：{}，接收人：{}", id, receiverName);

            if (id == null) {
                return JsonBean.error("交底ID不能为空");
            }
            if (receiverId == null) {
                return JsonBean.error("接收人ID不能为空");
            }
            if (!StringUtils.hasText(receiverName)) {
                return JsonBean.error("接收人姓名不能为空");
            }

            boolean result = projectBriefingService.receiveBriefing(id, receiverId, receiverName);
            if (result) {
                return JsonBean.success("接收成功");
            } else {
                return JsonBean.error("接收失败");
            }
        } catch (Exception e) {
            log.error("接收交底失败", e);
            return JsonBean.error("接收失败：" + e.getMessage());
        }
    }

    /**
     * 确认交底
     *
     * @param request 确认请求
     * @return 确认结果
     */
    @PostMapping("/confirm")
    @Operation(summary = "确认交底", description = "确认交底")
    public String confirmBriefing(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            Long confirmerId = Long.valueOf(request.get("confirmerId").toString());
            String confirmerName = (String) request.get("confirmerName");
            String confirmComments = (String) request.get("confirmComments");

            log.info("确认交底，ID：{}，确认人：{}", id, confirmerName);

            if (id == null) {
                return JsonBean.error("交底ID不能为空");
            }
            if (confirmerId == null) {
                return JsonBean.error("确认人ID不能为空");
            }
            if (!StringUtils.hasText(confirmerName)) {
                return JsonBean.error("确认人姓名不能为空");
            }

            boolean result = projectBriefingService.confirmBriefing(id, confirmerId, confirmerName, confirmComments);
            if (result) {
                return JsonBean.success("确认成功");
            } else {
                return JsonBean.error("确认失败");
            }
        } catch (Exception e) {
            log.error("确认交底失败", e);
            return JsonBean.error("确认失败：" + e.getMessage());
        }
    }

    /**
     * 完成交底
     *
     * @param request 完成请求
     * @return 完成结果
     */
    @PostMapping("/complete")
    @Operation(summary = "完成交底", description = "完成交底")
    public String completeBriefing(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            String executionStatus = (String) request.get("executionStatus");

            log.info("完成交底，ID：{}", id);

            if (id == null) {
                return JsonBean.error("交底ID不能为空");
            }

            boolean result = projectBriefingService.completeBriefing(id, executionStatus);
            if (result) {
                return JsonBean.success("完成交底成功");
            } else {
                return JsonBean.error("完成交底失败");
            }
        } catch (Exception e) {
            log.error("完成交底失败", e);
            return JsonBean.error("完成交底失败：" + e.getMessage());
        }
    }

    /**
     * 取消交底
     *
     * @param id 交底ID
     * @return 取消结果
     */
    @PostMapping("/cancel/{id}")
    @Operation(summary = "取消交底", description = "取消交底")
    public String cancelBriefing(@PathVariable Long id) {
        try {
            log.info("取消交底，ID：{}", id);

            if (id == null) {
                return JsonBean.error("交底ID不能为空");
            }

            boolean result = projectBriefingService.cancelBriefing(id);
            if (result) {
                return JsonBean.success("取消交底成功");
            } else {
                return JsonBean.error("取消交底失败");
            }
        } catch (Exception e) {
            log.error("取消交底失败", e);
            return JsonBean.error("取消交底失败：" + e.getMessage());
        }
    }

    /**
     * 获取草稿状态的交底列表
     *
     * @return 草稿状态的交底列表
     */
    @GetMapping("/draft")
    @Operation(summary = "获取草稿状态的交底列表", description = "获取草稿状态的交底列表")
    public String getDraftBriefings() {
        try {
            log.info("获取草稿状态的交底列表");

            List<ProjectBriefing> briefings = projectBriefingService.getDraftBriefings();
            return JsonBean.success("查询成功", briefings);
        } catch (Exception e) {
            log.error("获取草稿状态的交底列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取待交底的交底列表
     *
     * @return 待交底的交底列表
     */
    @GetMapping("/pending")
    @Operation(summary = "获取待交底的交底列表", description = "获取待交底的交底列表")
    public String getPendingBriefings() {
        try {
            log.info("获取待交底的交底列表");

            List<ProjectBriefing> briefings = projectBriefingService.getPendingBriefings();
            return JsonBean.success("查询成功", briefings);
        } catch (Exception e) {
            log.error("获取待交底的交底列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取已完成的交底列表
     *
     * @return 已完成的交底列表
     */
    @GetMapping("/completed")
    @Operation(summary = "获取已完成的交底列表", description = "获取已完成的交底列表")
    public String getCompletedBriefings() {
        try {
            log.info("获取已完成的交底列表");

            List<ProjectBriefing> briefings = projectBriefingService.getCompletedBriefings();
            return JsonBean.success("查询成功", briefings);
        } catch (Exception e) {
            log.error("获取已完成的交底列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取紧急交底列表
     *
     * @return 紧急交底列表
     */
    @GetMapping("/urgent")
    @Operation(summary = "获取紧急交底列表", description = "获取紧急交底列表")
    public String getUrgentBriefings() {
        try {
            log.info("获取紧急交底列表");

            List<ProjectBriefing> briefings = projectBriefingService.getUrgentBriefings();
            return JsonBean.success("查询成功", briefings);
        } catch (Exception e) {
            log.error("获取紧急交底列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取我交底的交底列表
     *
     * @param userId 用户ID
     * @return 我交底的交底列表
     */
    @GetMapping("/myBriefings/{userId}")
    @Operation(summary = "获取我交底的交底列表", description = "获取我交底的交底列表")
    public String getMyBriefings(@PathVariable Long userId) {
        try {
            log.info("获取我交底的交底列表，用户ID：{}", userId);

            if (userId == null) {
                return JsonBean.error("用户ID不能为空");
            }

            List<ProjectBriefing> briefings = projectBriefingService.getMyBriefings(userId);
            return JsonBean.success("查询成功", briefings);
        } catch (Exception e) {
            log.error("获取我交底的交底列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据项目ID查询交底列表
     *
     * @param projectId 项目ID
     * @return 交底列表
     */
    @GetMapping("/project/{projectId}")
    @Operation(summary = "根据项目ID查询交底列表", description = "根据项目ID查询交底列表")
    public String getProjectBriefingByProjectId(@PathVariable Long projectId) {
        try {
            log.info("根据项目ID查询交底列表，项目ID：{}", projectId);

            if (projectId == null) {
                return JsonBean.error("项目ID不能为空");
            }

            List<ProjectBriefing> briefings = projectBriefingService.getProjectBriefingByProjectId(projectId);
            return JsonBean.success("查询成功", briefings);
        } catch (Exception e) {
            log.error("根据项目ID查询交底列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 统计项目交底数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics")
    @Operation(summary = "统计项目交底数据", description = "统计项目交底数据")
    public String getProjectBriefingStatistics(@RequestBody ProjectBriefingQueryParam param) {
        try {
            log.info("统计项目交底数据，参数：{}", param);

            Map<String, Object> statistics = projectBriefingService.getProjectBriefingStatistics(param);
            return JsonBean.success("统计成功", statistics);
        } catch (Exception e) {
            log.error("统计项目交底数据失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 模糊搜索项目交底
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目交底列表
     */
    @GetMapping("/search")
    @Operation(summary = "模糊搜索项目交底", description = "模糊搜索项目交底")
    public String searchProjectBriefings(@RequestParam String keyword,
                                        @RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("模糊搜索项目交底，关键词：{}，限制数量：{}", keyword, limit);

            if (!StringUtils.hasText(keyword)) {
                return JsonBean.error("搜索关键词不能为空");
            }

            List<ProjectBriefing> briefings = projectBriefingService.searchProjectBriefings(keyword, limit);
            return JsonBean.success("搜索成功", briefings);
        } catch (Exception e) {
            log.error("模糊搜索项目交底失败", e);
            return JsonBean.error("搜索失败：" + e.getMessage());
        }
    }

    /**
     * 导出项目交底
     *
     * @param param 查询参数
     * @return 交底列表
     */
    @PostMapping("/export")
    @Operation(summary = "导出项目交底", description = "导出项目交底")
    public String exportProjectBriefings(@RequestBody ProjectBriefingQueryParam param) {
        try {
            log.info("导出项目交底，参数：{}", param);

            List<ProjectBriefing> briefings = projectBriefingService.exportProjectBriefings(param);
            return JsonBean.success("导出成功", briefings);
        } catch (Exception e) {
            log.error("导出项目交底失败", e);
            return JsonBean.error("导出失败：" + e.getMessage());
        }
    }
}
