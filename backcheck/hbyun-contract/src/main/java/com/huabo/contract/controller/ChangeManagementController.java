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
import com.huabo.contract.entity.ChangeManagement;
import com.huabo.contract.entity.TblProjectInfoRegister;
import com.huabo.contract.service.ChangeManagementService;
import com.huabo.contract.service.TblProjectInfoRegisterService;
import com.huabo.contract.vo.ChangeManagementQueryParam;
import com.huabo.contract.vo.ProjectInfoRegisterQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目变更管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/change")
@Tag(name="项目变更管理",description="项目变更管理")
@Validated
public class ChangeManagementController {

    @Autowired
    private ChangeManagementService changeManagementService;

    @Autowired
    private TblProjectInfoRegisterService projectInfoRegisterService;

    /**
     * 分页查询项目变更管理列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询项目变更管理列表", description = "支持多条件查询和分页")
    public String getChangeManagementList(@RequestBody ChangeManagementQueryParam param) {
        try {
            log.info("分页查询项目变更管理列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNumber() == null || param.getPageNumber() <= 0) {
                param.setPageNumber(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<ChangeManagement> pageInfo = changeManagementService.getChangeManagementList(param);

            return JsonBean.success("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("查询项目变更管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取项目变更管理详情
     *
     * @param id 主键ID
     * @return 项目变更管理详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取项目变更管理详情", description = "根据ID获取详细信息")
    public String getChangeManagementById(@PathVariable Long id) {
        try {
            log.info("获取项目变更管理详情，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            ChangeManagement changeManagement = changeManagementService.getChangeManagementById(id);
            if (changeManagement == null) {
                return JsonBean.error("项目变更管理不存在");
            }

            return JsonBean.success("查询成功", changeManagement);
        } catch (Exception e) {
            log.error("获取项目变更管理详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存项目变更管理（新增或修改）
     *
     * @param changeManagement 项目变更管理
     * @return 保存结果
     */
    @PostMapping("/save")
    @Operation(summary = "保存项目变更管理", description = "新增或修改项目变更管理")
    public String saveChangeManagement(@RequestBody ChangeManagement changeManagement) {
        try {
            log.info("保存项目变更管理，变更：{}", changeManagement);

            // 校验必填字段
            if (changeManagement.getProjectId() == null) {
                return JsonBean.error("项目ID不能为空");
            }
            if (changeManagement.getChangeType() == null) {
                return JsonBean.error("变更类型不能为空");
            }
            if (!StringUtils.hasText(changeManagement.getChangeTitle())) {
                return JsonBean.error("变更标题不能为空");
            }

            // 设置必填的审计字段
            Long currentUserId = 1L; // TODO: 从当前登录用户获取
            Date currentTime = new Date();

            if (changeManagement.getId() == null) {
                // 新增时设置创建信息
                changeManagement.setCreateBy(currentUserId);
                changeManagement.setCreateTime(currentTime);
                log.info("设置创建信息: createBy={}, createTime={}", currentUserId, currentTime);
            }
            changeManagement.setUpdateBy(currentUserId);
            changeManagement.setUpdateTime(currentTime);
            log.info("设置更新信息: updateBy={}, updateTime={}", currentUserId, currentTime);

            // 设置默认值
            if (changeManagement.getChangeStatus() == null) {
                changeManagement.setChangeStatus(1); // 默认状态：草稿
            }

            if (changeManagement.getVersion() == null) {
                changeManagement.setVersion(1);
            }

            // 生成变更编号
            if (!StringUtils.hasText(changeManagement.getChangeNo())) {
                String changeNo = "CHG" + System.currentTimeMillis();
                changeManagement.setChangeNo(changeNo);
            }

            // 校验变更编号唯一性
            if (StringUtils.hasText(changeManagement.getChangeNo())) {
                boolean exists = changeManagementService.existsChangeNo(
                    changeManagement.getChangeNo(), changeManagement.getId());
                if (exists) {
                    return JsonBean.error("变更编号已存在，请重新输入");
                }
            }

            boolean result = changeManagementService.saveChangeManagement(changeManagement);
            if (result) {
                return JsonBean.success("保存成功");
            } else {
                return JsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存项目变更管理失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 更新项目变更管理
     *
     * @param id 变更ID
     * @param changeManagement 项目变更管理
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新项目变更管理", description = "根据ID更新项目变更管理")
    public String updateChangeManagement(@PathVariable("id") String idStr, @RequestBody ChangeManagement changeManagement) {
        try {
            log.info("更新项目变更管理，ID字符串：{}，变更：{}", idStr, changeManagement);

            // 验证和转换ID
            if (idStr == null || idStr.trim().isEmpty() || "undefined".equals(idStr) || "null".equals(idStr)) {
                return JsonBean.error("变更ID不能为空或无效");
            }

            Long id;
            try {
                id = Long.valueOf(idStr);
            } catch (NumberFormatException e) {
                log.warn("无效的ID格式：{}", idStr);
                return JsonBean.error("变更ID格式无效：" + idStr);
            }

            log.info("转换后的ID：{}", id);

            // 设置ID和更新信息
            changeManagement.setId(id);
            changeManagement.setUpdateTime(new Date());
            changeManagement.setUpdateBy(1L); // 设置更新人ID，实际应该从当前登录用户获取

            log.info("设置更新信息: updateBy={}, updateTime={}", changeManagement.getUpdateBy(), changeManagement.getUpdateTime());

            boolean result = changeManagementService.saveChangeManagement(changeManagement);
            if (result) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新项目变更管理失败", e);
            return JsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除项目变更管理
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除项目变更管理", description = "根据ID删除项目变更管理")
    public String deleteChangeManagement(@PathVariable Long id) {
        try {
            log.info("删除项目变更管理，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            boolean result = changeManagementService.deleteChangeManagement(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除项目变更管理失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除项目变更管理
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除项目变更管理", description = "根据ID列表批量删除项目变更管理")
    public String batchDeleteChangeManagement(@RequestBody List<Long> ids) {
        try {
            log.info("批量删除项目变更管理，ID列表：{}", ids);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("ID列表不能为空");
            }

            boolean result = changeManagementService.batchDeleteChangeManagement(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除项目变更管理失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据项目ID查询项目变更管理列表
     *
     * @param projectId 项目ID
     * @return 项目变更管理列表
     */
    @GetMapping("/project/{projectId}")
    @Operation(summary = "根据项目ID查询项目变更管理列表", description = "根据项目ID查询项目变更管理列表")
    public String getChangeManagementByProjectId(@PathVariable Long projectId) {
        try {
            log.info("根据项目ID查询项目变更管理列表，项目ID：{}", projectId);

            if (projectId == null) {
                return JsonBean.error("项目ID不能为空");
            }

            List<ChangeManagement> changes = changeManagementService.getChangeManagementByProjectId(projectId);
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("根据项目ID查询项目变更管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据变更类型查询项目变更管理列表
     *
     * @param changeType 变更类型
     * @return 项目变更管理列表
     */
    @GetMapping("/type/{changeType}")
    @Operation(summary = "根据变更类型查询项目变更管理列表", description = "根据变更类型查询项目变更管理列表")
    public String getChangeManagementByChangeType(@PathVariable Integer changeType) {
        try {
            log.info("根据变更类型查询项目变更管理列表，变更类型：{}", changeType);

            if (changeType == null) {
                return JsonBean.error("变更类型不能为空");
            }

            List<ChangeManagement> changes = changeManagementService.getChangeManagementByChangeType(changeType);
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("根据变更类型查询项目变更管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据变更等级查询项目变更管理列表
     *
     * @param changeLevel 变更等级
     * @return 项目变更管理列表
     */
    @GetMapping("/level/{changeLevel}")
    @Operation(summary = "根据变更等级查询项目变更管理列表", description = "根据变更等级查询项目变更管理列表")
    public String getChangeManagementByChangeLevel(@PathVariable Integer changeLevel) {
        try {
            log.info("根据变更等级查询项目变更管理列表，变更等级：{}", changeLevel);

            if (changeLevel == null) {
                return JsonBean.error("变更等级不能为空");
            }

            List<ChangeManagement> changes = changeManagementService.getChangeManagementByChangeLevel(changeLevel);
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("根据变更等级查询项目变更管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据变更状态查询项目变更管理列表
     *
     * @param changeStatus 变更状态
     * @return 项目变更管理列表
     */
    @GetMapping("/status/{changeStatus}")
    @Operation(summary = "根据变更状态查询项目变更管理列表", description = "根据变更状态查询项目变更管理列表")
    public String getChangeManagementByChangeStatus(@PathVariable Integer changeStatus) {
        try {
            log.info("根据变更状态查询项目变更管理列表，变更状态：{}", changeStatus);

            if (changeStatus == null) {
                return JsonBean.error("变更状态不能为空");
            }

            List<ChangeManagement> changes = changeManagementService.getChangeManagementByChangeStatus(changeStatus);
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("根据变更状态查询项目变更管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据申请人ID查询项目变更管理列表
     *
     * @param applicantId 申请人ID
     * @return 项目变更管理列表
     */
    @GetMapping("/applicant/{applicantId}")
    @Operation(summary = "根据申请人ID查询项目变更管理列表", description = "根据申请人ID查询项目变更管理列表")
    public String getChangeManagementByApplicantId(@PathVariable Long applicantId) {
        try {
            log.info("根据申请人ID查询项目变更管理列表，申请人ID：{}", applicantId);

            if (applicantId == null) {
                return JsonBean.error("申请人ID不能为空");
            }

            List<ChangeManagement> changes = changeManagementService.getChangeManagementByApplicantId(applicantId);
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("根据申请人ID查询项目变更管理列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取草稿状态的变更列表
     *
     * @return 变更列表
     */
    @GetMapping("/draft")
    @Operation(summary = "获取草稿状态的变更列表", description = "获取草稿状态的变更列表")
    public String getDraftChanges() {
        try {
            log.info("获取草稿状态的变更列表");

            List<ChangeManagement> changes = changeManagementService.getDraftChanges();
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("获取草稿状态的变更列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取待审核的变更列表
     *
     * @return 变更列表
     */
    @GetMapping("/pendingReview")
    @Operation(summary = "获取待审核的变更列表", description = "获取待审核的变更列表")
    public String getPendingReviewChanges() {
        try {
            log.info("获取待审核的变更列表");

            List<ChangeManagement> changes = changeManagementService.getPendingReviewChanges();
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("获取待审核的变更列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取审核中的变更列表
     *
     * @return 变更列表
     */
    @GetMapping("/inReview")
    @Operation(summary = "获取审核中的变更列表", description = "获取审核中的变更列表")
    public String getInReviewChanges() {
        try {
            log.info("获取审核中的变更列表");

            List<ChangeManagement> changes = changeManagementService.getInReviewChanges();
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("获取审核中的变更列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取已批准的变更列表
     *
     * @return 变更列表
     */
    @GetMapping("/approved")
    @Operation(summary = "获取已批准的变更列表", description = "获取已批准的变更列表")
    public String getApprovedChanges() {
        try {
            log.info("获取已批准的变更列表");

            List<ChangeManagement> changes = changeManagementService.getApprovedChanges();
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("获取已批准的变更列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取已拒绝的变更列表
     *
     * @return 变更列表
     */
    @GetMapping("/rejected")
    @Operation(summary = "获取已拒绝的变更列表", description = "获取已拒绝的变更列表")
    public String getRejectedChanges() {
        try {
            log.info("获取已拒绝的变更列表");

            List<ChangeManagement> changes = changeManagementService.getRejectedChanges();
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("获取已拒绝的变更列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取实施中的变更列表
     *
     * @return 变更列表
     */
    @GetMapping("/implementing")
    @Operation(summary = "获取实施中的变更列表", description = "获取实施中的变更列表")
    public String getImplementingChanges() {
        try {
            log.info("获取实施中的变更列表");

            List<ChangeManagement> changes = changeManagementService.getImplementingChanges();
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("获取实施中的变更列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取已完成的变更列表
     *
     * @return 变更列表
     */
    @GetMapping("/completed")
    @Operation(summary = "获取已完成的变更列表", description = "获取已完成的变更列表")
    public String getCompletedChanges() {
        try {
            log.info("获取已完成的变更列表");

            List<ChangeManagement> changes = changeManagementService.getCompletedChanges();
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("获取已完成的变更列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取已取消的变更列表
     *
     * @return 变更列表
     */
    @GetMapping("/cancelled")
    @Operation(summary = "获取已取消的变更列表", description = "获取已取消的变更列表")
    public String getCancelledChanges() {
        try {
            log.info("获取已取消的变更列表");

            List<ChangeManagement> changes = changeManagementService.getCancelledChanges();
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("获取已取消的变更列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取紧急变更列表
     *
     * @return 变更列表
     */
    @GetMapping("/urgent")
    @Operation(summary = "获取紧急变更列表", description = "获取紧急变更列表")
    public String getUrgentChanges() {
        try {
            log.info("获取紧急变更列表");

            List<ChangeManagement> changes = changeManagementService.getUrgentChanges();
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("获取紧急变更列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取重要变更列表
     *
     * @return 变更列表
     */
    @GetMapping("/important")
    @Operation(summary = "获取重要变更列表", description = "获取重要变更列表")
    public String getImportantChanges() {
        try {
            log.info("获取重要变更列表");

            List<ChangeManagement> changes = changeManagementService.getImportantChanges();
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("获取重要变更列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取高优先级变更列表
     *
     * @return 变更列表
     */
    @GetMapping("/highPriority")
    @Operation(summary = "获取高优先级变更列表", description = "获取高优先级变更列表")
    public String getHighPriorityChanges() {
        try {
            log.info("获取高优先级变更列表");

            List<ChangeManagement> changes = changeManagementService.getHighPriorityChanges();
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("获取高优先级变更列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取有成本影响的变更列表
     *
     * @return 变更列表
     */
    @GetMapping("/costImpact")
    @Operation(summary = "获取有成本影响的变更列表", description = "获取有成本影响的变更列表")
    public String getCostImpactChanges() {
        try {
            log.info("获取有成本影响的变更列表");

            List<ChangeManagement> changes = changeManagementService.getCostImpactChanges();
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("获取有成本影响的变更列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取有进度影响的变更列表
     *
     * @return 变更列表
     */
    @GetMapping("/scheduleImpact")
    @Operation(summary = "获取有进度影响的变更列表", description = "获取有进度影响的变更列表")
    public String getScheduleImpactChanges() {
        try {
            log.info("获取有进度影响的变更列表");

            List<ChangeManagement> changes = changeManagementService.getScheduleImpactChanges();
            return JsonBean.success("查询成功", changes);
        } catch (Exception e) {
            log.error("获取有进度影响的变更列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 模糊搜索项目变更管理
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目变更管理列表
     */
    @GetMapping("/search")
    @Operation(summary = "模糊搜索项目变更管理", description = "模糊搜索项目变更管理")
    public String searchChangeManagement(@RequestParam String keyword,
                                        @RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("模糊搜索项目变更管理，关键词：{}，限制数量：{}", keyword, limit);

            if (!StringUtils.hasText(keyword)) {
                return JsonBean.error("搜索关键词不能为空");
            }

            List<ChangeManagement> changes = changeManagementService.searchChangeManagement(keyword, limit);
            return JsonBean.success("搜索成功", changes);
        } catch (Exception e) {
            log.error("模糊搜索项目变更管理失败", e);
            return JsonBean.error("搜索失败：" + e.getMessage());
        }
    }

    /**
     * 统计项目变更管理数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics")
    @Operation(summary = "统计项目变更管理数据", description = "统计项目变更管理数据")
    public String getChangeManagementStatistics(@RequestBody ChangeManagementQueryParam param) {
        try {
            log.info("统计项目变更管理数据，参数：{}", param);

            Map<String, Object> statistics = changeManagementService.getChangeManagementStatistics(param);
            return JsonBean.success("统计成功", statistics);
        } catch (Exception e) {
            log.error("统计项目变更管理数据失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 统计变更类型分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics/typeDistribution")
    @Operation(summary = "统计变更类型分布", description = "统计变更类型分布")
    public String getChangeTypeDistribution(@RequestBody ChangeManagementQueryParam param) {
        try {
            log.info("统计变更类型分布，参数：{}", param);

            List<Map<String, Object>> distribution = changeManagementService.getChangeTypeDistribution(param);
            return JsonBean.success("统计成功", distribution);
        } catch (Exception e) {
            log.error("统计变更类型分布失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 统计变更等级分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics/levelDistribution")
    @Operation(summary = "统计变更等级分布", description = "统计变更等级分布")
    public String getChangeLevelDistribution(@RequestBody ChangeManagementQueryParam param) {
        try {
            log.info("统计变更等级分布，参数：{}", param);

            List<Map<String, Object>> distribution = changeManagementService.getChangeLevelDistribution(param);
            return JsonBean.success("统计成功", distribution);
        } catch (Exception e) {
            log.error("统计变更等级分布失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 统计变更状态分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics/statusDistribution")
    @Operation(summary = "统计变更状态分布", description = "统计变更状态分布")
    public String getChangeStatusDistribution(@RequestBody ChangeManagementQueryParam param) {
        try {
            log.info("统计变更状态分布，参数：{}", param);

            List<Map<String, Object>> distribution = changeManagementService.getChangeStatusDistribution(param);
            return JsonBean.success("统计成功", distribution);
        } catch (Exception e) {
            log.error("统计变更状态分布失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 统计月度变更趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics/changeTrend")
    @Operation(summary = "统计月度变更趋势", description = "统计月度变更趋势")
    public String getMonthlyChangeTrend(@RequestBody ChangeManagementQueryParam param) {
        try {
            log.info("统计月度变更趋势，参数：{}", param);

            List<Map<String, Object>> trend = changeManagementService.getMonthlyChangeTrend(param);
            return JsonBean.success("统计成功", trend);
        } catch (Exception e) {
            log.error("统计月度变更趋势失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取项目登记列表（用于项目选择）
     *
     * @param param 查询参数
     * @return 项目登记列表
     */
    @PostMapping("/projects")
    @Operation(summary = "获取项目登记列表", description = "用于项目变更中的项目选择")
    public String getProjectList(@RequestBody ProjectInfoRegisterQueryParam param) {
        try {
            log.info("获取项目登记列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNumber() == null || param.getPageNumber() <= 0) {
                param.setPageNumber(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<TblProjectInfoRegister> pageInfo = projectInfoRegisterService.getProjectInfoRegisterList(param);
            return JsonBean.success("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("获取项目登记列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }
}
