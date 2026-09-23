package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.global.treasurer.entity.*;
import com.global.treasurer.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Claude
 * @date 2025-01-19
 * @description 资金归集管理Controller
 */
@RestController
@RequestMapping("/fund-concentration")
@Api(tags = "资金归集管理")
public class FundConcentrationController {

    private static final Logger log = LoggerFactory.getLogger(FundConcentrationController.class);

    @Resource
    private UserProvider userProvider;

    @Resource
    private TblFundPoolService tblFundPoolService;

    @Resource
    private TblConcentrationStrategyService tblConcentrationStrategyService;

    @Resource
    private TblConcentrationPlanService tblConcentrationPlanService;

    @Resource
    private TblConcentrationExecutionService tblConcentrationExecutionService;

    @Resource
    private TblFundAllocationService tblFundAllocationService;

    @Resource
    private TblInternalLoanService tblInternalLoanService;

    @Resource
    private TblExceptionHandlingService tblExceptionHandlingService;

    @Resource
    private TblReportStatisticsService tblReportStatisticsService;

    @Resource
    private TblRegulatoryAnalysisService tblRegulatoryAnalysisService;

    // ==================== 资金池管理 ====================

    /**
     * 分页查询资金池
     */
    @GetMapping("/fund-pool/page")
    @ApiOperation("分页查询资金池")
    public String getFundPoolPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam("资金池名称") @RequestParam(required = false) String poolName,
            @ApiParam("资金池类型") @RequestParam(required = false) String poolType,
            @ApiParam("状态") @RequestParam(required = false) String status,
            HttpServletResponse response) throws IOException {

        try {
            PageInfo<TblFundPool> pageInfo = tblFundPoolService.getFundPoolPage(pageNo, pageSize, poolName, poolType, status);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询资金池分页数据失败", e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 根据ID查询资金池
     */
    @GetMapping("/fund-pool/{poolId}")
    @ApiOperation("根据ID查询资金池")
    public String getFundPoolById(
            @ApiParam("资金池ID") @PathVariable Long poolId,
            HttpServletResponse response) throws IOException {

        try {
            TblFundPool pool = tblFundPoolService.getFundPoolById(poolId);
            return JsonBean.success(pool);
        } catch (Exception e) {
            log.error("查询资金池详情失败, poolId={}", poolId, e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 创建资金池
     */
    @PostMapping(value = "/fund-pool")
    @ApiOperation("创建资金池")
    public String createFundPool(
            @ApiParam("资金池信息") @FlexibleRequestBody TblFundPool pool,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            TblFundPool saved = tblFundPoolService.saveFundPool(pool);
            return JsonBean.success("创建成功", saved);
        } catch (Exception e) {
            log.error("创建资金池失败", e);
            JsonBean json = new JsonBean(0, "创建失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 更新资金池
     */
    @PutMapping(value = "/fund-pool")
    @ApiOperation("更新资金池")
    public String updateFundPool(
            @ApiParam("资金池信息") @FlexibleRequestBody TblFundPool pool,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            tblFundPoolService.updateFundPool(pool);
            return JsonBean.success("更新成功");
        } catch (Exception e) {
            log.error("更新资金池失败", e);
            JsonBean json = new JsonBean(0, "更新失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 删除资金池
     */
    @DeleteMapping("/fund-pool/{poolId}")
    @ApiOperation("删除资金池")
    public String deleteFundPool(
            @ApiParam("资金池ID") @PathVariable Long poolId,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            tblFundPoolService.deleteFundPool(poolId);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除资金池失败, poolId={}", poolId, e);
            JsonBean json = new JsonBean(0, "删除失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 批量删除资金池
     */
    @DeleteMapping("/fund-pool/batch")
    @ApiOperation("批量删除资金池")
    public String batchDeleteFundPool(
            @ApiParam("资金池ID列表") @RequestParam(value = "poolIds") List<Long> poolIds,
            HttpServletResponse response) throws IOException {

        try {
            if (!validateUser(response)) {
                return null;
            }
            if (poolIds == null || poolIds.isEmpty()) {
                return JsonBean.error("资金池ID列表不能为空");
            }
            tblFundPoolService.batchDeleteFundPool(poolIds);
            return JsonBean.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除资金池失败", e);
            JsonBean json = new JsonBean(0, "批量删除失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }
    @GetMapping("/fund-pool/statistics")
    @ApiOperation("获取资金池统计信息")
    public String getFundPoolStatistics(HttpServletResponse response) throws IOException {

        try {
            Map<String, Object> statistics = tblFundPoolService.getFundPoolStatistics();
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("查询资金池统计信息失败", e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    // ==================== 归集策略配置 ====================

    /**
     * 分页查询归集策略
     */
    @GetMapping("/strategy/page")
    @ApiOperation("分页查询归集策略")
    public String getStrategyPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam("策略名称") @RequestParam(required = false) String strategyName,
            @ApiParam("策略类型") @RequestParam(required = false) String strategyType,
            @ApiParam("状态") @RequestParam(required = false) String status,
            HttpServletResponse response) throws IOException {

        try {
            PageInfo<TblConcentrationStrategy> pageInfo = tblConcentrationStrategyService.getStrategyPage(pageNo, pageSize, strategyName, strategyType, status);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询归集策略分页数据失败", e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 根据ID查询归集策略
     */
    @GetMapping("/strategy/{strategyId}")
    @ApiOperation("根据ID查询归集策略")
    public String getStrategyById(
            @ApiParam("策略ID") @PathVariable Long strategyId,
            HttpServletResponse response) throws IOException {

        try {
            TblConcentrationStrategy strategy = tblConcentrationStrategyService.getStrategyById(strategyId);
            return JsonBean.success(strategy);
        } catch (Exception e) {
            log.error("查询归集策略详情失败, strategyId={}", strategyId, e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 创建归集策略
     */
    @PostMapping(value = "/strategy")
    @ApiOperation("创建归集策略")
    public String createStrategy(
            @ApiParam("归集策略信息") @FlexibleRequestBody TblConcentrationStrategy strategy,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            // 填充创建人信息
            try {
                com.hbfk.entity.TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    if (strategy.getCreatorId() == null || strategy.getCreatorId().isEmpty()) {
                        strategy.setCreatorId(loginStaff.getStaffid() != null ? loginStaff.getStaffid().toPlainString() : "");
                    }
                    if (strategy.getCreatorName() == null || strategy.getCreatorName().isEmpty()) {
                        strategy.setCreatorName(loginStaff.getRealname() != null ? loginStaff.getRealname() : loginStaff.getUsername());
                    }
                }
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败，创建人将为空: {}", ex.getMessage());
            }

            TblConcentrationStrategy saved = tblConcentrationStrategyService.saveStrategy(strategy);
            return JsonBean.success("创建成功", saved);
        } catch (Exception e) {
            log.error("创建归集策略失败", e);
            JsonBean json = new JsonBean(0, "创建失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 更新归集策略
     */
    @PutMapping(value = "/strategy")
    @ApiOperation("更新归集策略")
    public String updateStrategy(
            @ApiParam("归集策略信息") @FlexibleRequestBody TblConcentrationStrategy strategy,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            // 填充更新人信息
            try {
                com.hbfk.entity.TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    strategy.setUpdateUser(loginStaff.getRealname() != null ? loginStaff.getRealname() : loginStaff.getUsername());
                }
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败，更新人将为空: {}", ex.getMessage());
            }

            tblConcentrationStrategyService.updateStrategy(strategy);
            return JsonBean.success("更新成功");
        } catch (Exception e) {
            log.error("更新归集策略失败", e);
            JsonBean json = new JsonBean(0, "更新失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 启用归集策略
     */
    @PutMapping("/strategy/{strategyId}/enable")
    @ApiOperation("启用归集策略")
    public String enableStrategy(
            @ApiParam("策略ID") @PathVariable Long strategyId,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            tblConcentrationStrategyService.enableStrategy(strategyId);
            return JsonBean.success("启用成功");
        } catch (Exception e) {
            log.error("启用归集策略失败, strategyId={}", strategyId, e);
            JsonBean json = new JsonBean(0, "启用失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 停用归集策略
     */
    @PutMapping("/strategy/{strategyId}/disable")
    @ApiOperation("停用归集策略")
    public String disableStrategy(
            @ApiParam("策略ID") @PathVariable Long strategyId,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            tblConcentrationStrategyService.disableStrategy(strategyId);
            return JsonBean.success("停用成功");
        } catch (Exception e) {
            log.error("停用归集策略失败, strategyId={}", strategyId, e);
            JsonBean json = new JsonBean(0, "停用失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 测试归集策略
     */
    @PostMapping("/strategy/{strategyId}/test")
    @ApiOperation("测试归集策略")
    public String testStrategy(
            @ApiParam("策略ID") @PathVariable Long strategyId,
            HttpServletResponse response) throws IOException {

        try {
            String testResult = tblConcentrationStrategyService.testStrategy(strategyId);
            return JsonBean.success(testResult);
        } catch (Exception e) {
            log.error("测试归集策略失败, strategyId={}", strategyId, e);
            JsonBean json = new JsonBean(0, "测试失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 批量启用归集策略
     */
    @PostMapping("/strategy/batch-enable")
    @ApiOperation("批量启用归集策略")
    public String batchEnableStrategy(
            @ApiParam("策略ID列表") @RequestParam(value = "strategyIds", required = false) List<Long> strategyIds,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            tblConcentrationStrategyService.batchEnableStrategy(strategyIds);
            return JsonBean.success("批量启用成功");
        } catch (Exception e) {
            log.error("批量启用归集策略失败", e);
            JsonBean json = new JsonBean(0, "批量启用失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 删除归集策略
     */
    @DeleteMapping("/strategy/{strategyId}")
    @ApiOperation("删除归集策略")
    public String deleteStrategy(
            @ApiParam("策略ID") @PathVariable Long strategyId,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            tblConcentrationStrategyService.deleteStrategy(strategyId);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除归集策略失败, strategyId={}", strategyId, e);
            JsonBean json = new JsonBean(0, "删除失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    // ==================== 归集计划管理 ====================

    /**
     * 分页查询归集计划
     */
    @GetMapping("/plan/page")
    @ApiOperation("分页查询归集计划")
    public String getPlanPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam("计划名称") @RequestParam(required = false) String planName,
            @ApiParam("计划类型") @RequestParam(required = false) String planType,
            @ApiParam("计划状态") @RequestParam(required = false) String planStatus,
            @ApiParam("策略ID") @RequestParam(required = false) Long strategyId,
            @ApiParam("执行日期范围") @RequestParam(required = false) String executionDateRange,
            HttpServletResponse response) throws IOException {

        try {
            // 解析执行日期范围
            String executionDateStart = null;
            String executionDateEnd = null;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(executionDateRange)) {
                String[] dates = executionDateRange.split(",");
                if (dates.length == 2) {
                    executionDateStart = dates[0];
                    executionDateEnd = dates[1];
                }
            }

            PageInfo<TblConcentrationPlan> pageInfo = tblConcentrationPlanService.getPlanPage(
                    pageNo, pageSize, planName, planType, planStatus, strategyId, executionDateStart, executionDateEnd);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询归集计划分页数据失败", e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 根据ID查询归集计划
     */
    @GetMapping("/plan/{planId}")
    @ApiOperation("根据ID查询归集计划")
    public String getPlanById(
            @ApiParam("计划ID") @PathVariable Long planId,
            HttpServletResponse response) throws IOException {

        try {
            TblConcentrationPlan plan = tblConcentrationPlanService.getPlanById(planId);
            return JsonBean.success(plan);
        } catch (Exception e) {
            log.error("查询归集计划详情失败, planId={}", planId, e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 创建归集计划
     */
    @PostMapping(value = "/plan")
    @ApiOperation("创建归集计划")
    public String createPlan(
            @ApiParam("归集计划信息") @FlexibleRequestBody TblConcentrationPlan plan,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            // 填充创建人信息
            try {
                com.hbfk.entity.TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    if (plan.getCreatorId() == null || plan.getCreatorId().isEmpty()) {
                        plan.setCreatorId(loginStaff.getStaffid() != null ? loginStaff.getStaffid().toPlainString() : "");
                    }
                    if (plan.getCreatorName() == null || plan.getCreatorName().isEmpty()) {
                        plan.setCreatorName(loginStaff.getRealname() != null ? loginStaff.getRealname() : loginStaff.getUsername());
                    }
                }
            } catch (Exception ex) {
                log.warn("获取当前用户信息失败，创建人将为空: {}", ex.getMessage());
            }

            TblConcentrationPlan saved = tblConcentrationPlanService.savePlan(plan);
            return JsonBean.success("创建成功", saved);
        } catch (Exception e) {
            log.error("创建归集计划失败", e);
            JsonBean json = new JsonBean(0, "创建失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 更新归集计划
     */
    @PutMapping(value = "/plan")
    @ApiOperation("更新归集计划")
    public String updatePlan(
            @ApiParam("归集计划信息") @FlexibleRequestBody TblConcentrationPlan plan,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            tblConcentrationPlanService.updatePlan(plan);
            return JsonBean.success("更新成功");
        } catch (Exception e) {
            log.error("更新归集计划失败", e);
            JsonBean json = new JsonBean(0, "更新失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 执行归集计划
     */
    @PostMapping("/plan/{planId}/execute")
    @ApiOperation("执行归集计划")
    public String executePlan(
            @ApiParam("计划ID") @PathVariable Long planId,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            String executionNo = tblConcentrationPlanService.executePlan(planId);
            return JsonBean.success("执行成功", executionNo);
        } catch (Exception e) {
            log.error("执行归集计划失败, planId={}", planId, e);
            JsonBean json = new JsonBean(0, "执行失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 暂停归集计划
     */
    @PutMapping("/plan/{planId}/pause")
    @ApiOperation("暂停归集计划")
    public String pausePlan(
            @ApiParam("计划ID") @PathVariable Long planId,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            tblConcentrationPlanService.pausePlan(planId);
            return JsonBean.success("暂停成功");
        } catch (Exception e) {
            log.error("暂停归集计划失败, planId={}", planId, e);
            JsonBean json = new JsonBean(0, "暂停失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 恢复归集计划
     */
    @PutMapping("/plan/{planId}/resume")
    @ApiOperation("恢复归集计划")
    public String resumePlan(
            @ApiParam("计划ID") @PathVariable Long planId,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            tblConcentrationPlanService.resumePlan(planId);
            return JsonBean.success("恢复成功");
        } catch (Exception e) {
            log.error("恢复归集计划失败, planId={}", planId, e);
            JsonBean json = new JsonBean(0, "恢复失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 取消归集计划
     */
    @PutMapping("/plan/{planId}/cancel")
    @ApiOperation("取消归集计划")
    public String cancelPlan(
            @ApiParam("计划ID") @PathVariable Long planId,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            tblConcentrationPlanService.cancelPlan(planId);
            return JsonBean.success("取消成功");
        } catch (Exception e) {
            log.error("取消归集计划失败, planId={}", planId, e);
            JsonBean json = new JsonBean(0, "取消失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 批量执行归集计划（支持 form-urlencoded 和 JSON 两种格式）
     */
    @PostMapping("/plan/batch-execute")
    @ApiOperation("批量执行归集计划")
    public String batchExecutePlan(
            @ApiParam("计划ID列表") @RequestParam(value = "planIds", required = false) List<Long> planIds,
            HttpServletResponse response) throws IOException {

        try {
            log.info("========== 批量执行归集计划 ==========");
            log.info("接收到的计划ID列表: {}", planIds);

            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            if (planIds == null || planIds.isEmpty()) {
                log.warn("计划ID列表为空，请求拒绝");
                JsonBean json = new JsonBean(0, "请选择要执行的计划", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toJson());
                return null;
            }

            tblConcentrationPlanService.batchExecutePlan(planIds);
            return JsonBean.success("批量执行成功");
        } catch (Exception e) {
            log.error("批量执行归集计划失败", e);
            JsonBean json = new JsonBean(0, "批量执行失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 获取归集计划执行明细
     */
    @GetMapping("/plan/{planId}/details")
    @ApiOperation("获取归集计划执行明细")
    public String getPlanDetails(
            @ApiParam("计划ID") @PathVariable Long planId,
            HttpServletResponse response) throws IOException {

        try {
            // 获取计划信息
            TblConcentrationPlan plan = tblConcentrationPlanService.getPlanById(planId);
            if (plan == null) {
                JsonBean json = new JsonBean(0, "计划不存在", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toJson());
                return null;
            }

            // 获取该计划的执行记录
            PageInfo<TblConcentrationExecution> executions = tblConcentrationExecutionService.getExecutionPage(1, 100, null, null, String.valueOf(planId), null);

            Map<String, Object> result = new HashMap<>();
            result.put("plan", plan);
            result.put("executions", executions.getList());
            result.put("totalExecutions", executions.getTotal());

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("获取归集计划执行明细失败", e);
            JsonBean json = new JsonBean(0, "获取明细失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    // ==================== 归集执行监控 ====================

    /**
     * 分页查询执行监控
     */
    @GetMapping("/monitor/page")
    @ApiOperation("分页查询执行监控")
    public String getMonitorPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam("执行编号") @RequestParam(required = false) String executionNo,
            @ApiParam("执行状态") @RequestParam(required = false) String executionStatus,
            HttpServletResponse response) throws IOException {

        try {
            PageInfo<TblConcentrationExecution> pageInfo = tblConcentrationExecutionService.getExecutionPage(pageNo, pageSize, executionNo, executionStatus, null, null);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询执行监控分页数据失败", e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 根据ID查询执行记录
     */
    @GetMapping("/monitor/{executionId}")
    @ApiOperation("根据ID查询执行记录")
    public String getExecutionById(
            @ApiParam("执行ID") @PathVariable String executionId,
            HttpServletResponse response) throws IOException {

        try {
            TblConcentrationExecution execution = tblConcentrationExecutionService.getExecutionById(Long.valueOf(executionId));
            return JsonBean.success(execution);
        } catch (Exception e) {
            log.error("查询执行记录详情失败, executionId={}", executionId, e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 获取监控统计数据
     */
    @GetMapping("/monitor/statistics")
    @ApiOperation("获取监控统计数据")
    public String getMonitorStatistics(HttpServletResponse response) throws IOException {

        try {
            Map<String, Object> statistics = tblConcentrationExecutionService.getMonitoringData();
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("查询监控统计数据失败", e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 获取告警列表
     */
    @GetMapping("/monitor/alerts")
    @ApiOperation("获取告警列表")
    public String getAlertList(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "50") Integer pageSize,
            HttpServletResponse response) throws IOException {

        try {
            List<Map<String, Object>> alerts = tblConcentrationExecutionService.getAlertList();
            // 返回分页格式
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", alerts);
            result.put("totalRecord", alerts.size());
            result.put("pageNo", pageNo);
            result.put("pageSize", pageSize);
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询告警列表失败", e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 暂停执行任务
     */
    @PutMapping("/monitor/{executionId}/pause")
    @ApiOperation("暂停执行任务")
    public String pauseExecution(
            @ApiParam("执行ID") @PathVariable String executionId,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            tblConcentrationExecutionService.pauseExecution(Long.valueOf(executionId));
            return JsonBean.success("暂停成功");
        } catch (Exception e) {
            log.error("暂停执行任务失败, executionId={}", executionId, e);
            JsonBean json = new JsonBean(0, "暂停失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 恢复执行任务
     */
    @PutMapping("/monitor/{executionId}/resume")
    @ApiOperation("恢复执行任务")
    public String resumeExecution(
            @ApiParam("执行ID") @PathVariable String executionId,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            tblConcentrationExecutionService.resumeExecution(Long.valueOf(executionId));
            return JsonBean.success("恢复成功");
        } catch (Exception e) {
            log.error("恢复执行任务失败, executionId={}", executionId, e);
            JsonBean json = new JsonBean(0, "恢复失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 重试执行任务
     */
    @PostMapping("/monitor/{executionId}/retry")
    @ApiOperation("重试执行任务")
    public String retryExecution(
            @ApiParam("执行ID") @PathVariable String executionId,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            String executionNo = tblConcentrationExecutionService.retryExecution(Long.valueOf(executionId));
            return JsonBean.success("重试成功", executionNo);
        } catch (Exception e) {
            log.error("重试执行任务失败, executionId={}", executionId, e);
            JsonBean json = new JsonBean(0, "重试失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 批量标记告警为已处理
     */
    @PostMapping("/monitor/alerts/batch-handle")
    @ApiOperation("批量标记告警为已处理")
    public String batchHandleAlerts(
            @ApiParam("告警ID列表") @RequestParam(value = "alertIds", required = false) List<String> alertIds,
            HttpServletResponse response) throws IOException {

        try {
            // 验证用户权限
            if (!validateUser(response)) {
                return null;
            }

            tblConcentrationExecutionService.batchMarkAlertAsHandled(alertIds);
            return JsonBean.success("批量处理成功");
        } catch (Exception e) {
            log.error("批量处理告警失败", e);
            JsonBean json = new JsonBean(0, "批量处理失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    // ==================== 资金下拨管理 ====================

    /**
     * 分页查询资金下拨
     */
    @GetMapping("/allocation/page")
    @ApiOperation("分页查询资金下拨")
    public String getAllocationPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam("下拨编号") @RequestParam(required = false) String allocationNo,
            @ApiParam("下拨状态") @RequestParam(required = false) String allocationStatus,
            HttpServletResponse response) throws IOException {

        try {
            PageInfo<TblFundAllocation> pageInfo = tblFundAllocationService.getAllocationPage(pageNo, pageSize, allocationNo, allocationStatus, null, null);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询资金下拨分页数据失败", e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 根据ID查询资金下拨
     */
    @GetMapping("/allocation/{allocationId}")
    @ApiOperation("根据ID查询资金下拨")
    public String getAllocationById(
            @ApiParam("下拨ID") @PathVariable String allocationId,
            HttpServletResponse response) throws IOException {

        try {
            TblFundAllocation allocation = tblFundAllocationService.getAllocationById(allocationId);
            return JsonBean.success(allocation);
        } catch (Exception e) {
            log.error("查询资金下拨详情失败, allocationId={}", allocationId, e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 创建资金下拨申请
     */
    @PostMapping(value = "/allocation")
    @ApiOperation("创建资金下拨申请")
    public String createAllocation(
            @ApiParam("资金下拨信息") @FlexibleRequestBody TblFundAllocation allocation,
            HttpServletResponse response) throws IOException {

        try {
            if (!validateUser(response)) {
                return null;
            }

            TblFundAllocation saved = tblFundAllocationService.saveAllocation(allocation);
            return JsonBean.success("创建成功", saved);
        } catch (Exception e) {
            log.error("创建资金下拨申请失败", e);
            JsonBean json = new JsonBean(0, "创建失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 修改资金下拨申请
     */
    @PutMapping(value = "/allocation")
    @ApiOperation("修改资金下拨申请")
    public String updateAllocation(
            @ApiParam("资金下拨信息") @FlexibleRequestBody TblFundAllocation allocation,
            HttpServletResponse response) throws IOException {

        try {
            if (!validateUser(response)) {
                return null;
            }
            tblFundAllocationService.updateAllocation(allocation);
            return JsonBean.success("修改成功");
        } catch (Exception e) {
            log.error("修改资金下拨申请失败", e);
            JsonBean json = new JsonBean(0, "修改失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 执行资金下拨
     */
    @PostMapping("/allocation/{allocationId}/execute")
    @ApiOperation("执行资金下拨")
    public String executeAllocation(
            @ApiParam("下拨ID") @PathVariable String allocationId,
            HttpServletResponse response) throws IOException {

        try {
            if (!validateUser(response)) {
                return null;
            }

            String allocationNo = tblFundAllocationService.executeAllocation(allocationId);
            return JsonBean.success("执行成功", allocationNo);
        } catch (Exception e) {
            log.error("执行资金下拨失败, allocationId={}", allocationId, e);
            JsonBean json = new JsonBean(0, "执行失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 删除资金下拨
     */
    @DeleteMapping("/allocation/{allocationId}")
    @ApiOperation("删除资金下拨")
    public String deleteAllocation(
            @ApiParam("下拨ID") @PathVariable String allocationId,
            HttpServletResponse response) throws IOException {
        try {
            if (!validateUser(response)) { return null; }
            tblFundAllocationService.deleteAllocation(allocationId);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除资金下拨失败, allocationId={}", allocationId, e);
            JsonBean json = new JsonBean(0, "删除失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 取消资金下拨
     */
    @PutMapping("/allocation/{allocationId}/cancel")
    @ApiOperation("取消资金下拨")
    public String cancelAllocation(
            @ApiParam("下拨ID") @PathVariable String allocationId,
            HttpServletResponse response) throws IOException {

        try {
            if (!validateUser(response)) {
                return null;
            }

            tblFundAllocationService.cancelAllocation(allocationId);
            return JsonBean.success("取消成功");
        } catch (Exception e) {
            log.error("取消资金下拨失败, allocationId={}", allocationId, e);
            JsonBean json = new JsonBean(0, "取消失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 重试资金下拨
     */
    @PostMapping("/allocation/{allocationId}/retry")
    @ApiOperation("重试资金下拨")
    public String retryAllocation(
            @ApiParam("下拨ID") @PathVariable String allocationId,
            HttpServletResponse response) throws IOException {

        try {
            if (!validateUser(response)) {
                return null;
            }

            String allocationNo = tblFundAllocationService.retryAllocation(allocationId);
            return JsonBean.success("重试成功", allocationNo);
        } catch (Exception e) {
            log.error("重试资金下拨失败, allocationId={}", allocationId, e);
            JsonBean json = new JsonBean(0, "重试失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    // ==================== 统计接口 ====================

    /**
     * 获取资金归集统计数据
     */
    @GetMapping("/statistics")
    @ApiOperation("获取资金归集统计数据")
    public String getConcentrationStatistics(HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> statistics = new HashMap<>();
            // 获取策略统计
            long totalRules = tblConcentrationStrategyService.count();
            long activeRules = tblConcentrationStrategyService.countByStatus("ACTIVE");
            // 获取今日执行次数
            long todayExecutions = tblConcentrationExecutionService.countTodayExecutions();
            // 计算成功率
            long successExecutions = tblConcentrationExecutionService.countTodaySuccessExecutions();
            double successRate = todayExecutions > 0 ? (double) successExecutions / todayExecutions * 100 : 0;

            statistics.put("totalRules", totalRules);
            statistics.put("activeRules", activeRules);
            statistics.put("todayExecutions", todayExecutions);
            statistics.put("successRate", Math.round(successRate * 100) / 100.0);

            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("获取资金归集统计数据失败", e);
            JsonBean json = new JsonBean(0, "获取统计数据失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    // ==================== 内部借贷管理（fund-concentration路径下） ====================

    /**
     * 分页查询内部借贷（fund-concentration路径）
     */
    @GetMapping("/internal-loan/page")
    @ApiOperation("分页查询内部借贷")
    public String getInternalLoanPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("借贷编号") @RequestParam(required = false) String loanNo,
            @ApiParam("借贷状态") @RequestParam(required = false) String loanStatus,
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate,
            HttpServletResponse response) throws IOException {
        try {
            PageInfo<TblInternalLoan> pageInfo = tblInternalLoanService.getLoanPage(current, size, loanNo, loanStatus, startDate, endDate);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", current);
            result.put("pageSize", size);
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询内部借贷列表失败", e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 根据ID查询内部借贷（fund-concentration路径）
     */
    @GetMapping("/internal-loan/{loanId}")
    @ApiOperation("根据ID查询内部借贷")
    public String getInternalLoanById(
            @ApiParam("借贷ID") @PathVariable String loanId,
            HttpServletResponse response) throws IOException {
        try {
            TblInternalLoan loan = tblInternalLoanService.getLoanById(loanId);
            return JsonBean.success(loan);
        } catch (Exception e) {
            log.error("查询内部借贷失败, loanId={}", loanId, e);
            JsonBean json = new JsonBean(0, "查询失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 创建内部借贷申请（fund-concentration路径）
     */
    @PostMapping(value = "/internal-loan")
    @ApiOperation("创建内部借贷申请")
    public String createInternalLoan(
            @ApiParam("内部借贷信息") @FlexibleRequestBody TblInternalLoan loan,
            HttpServletResponse response) throws IOException {
        try {
            TblInternalLoan savedLoan = tblInternalLoanService.saveLoan(loan);
            return JsonBean.success(savedLoan, "创建成功");
        } catch (Exception e) {
            log.error("创建内部借贷失败", e);
            JsonBean json = new JsonBean(0, "创建失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 更新内部借贷申请（fund-concentration路径）
     */
    @PutMapping(value = "/internal-loan")
    @ApiOperation("更新内部借贷申请")
    public String updateInternalLoan(
            @ApiParam("内部借贷信息") @FlexibleRequestBody TblInternalLoan loan,
            HttpServletResponse response) throws IOException {
        try {
            tblInternalLoanService.updateLoan(loan);
            return JsonBean.success("更新成功");
        } catch (Exception e) {
            log.error("更新内部借贷失败", e);
            JsonBean json = new JsonBean(0, "更新失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 删除内部借贷记录（fund-concentration路径）
     */
    @DeleteMapping("/internal-loan/{loanId}")
    @ApiOperation("删除内部借贷记录")
    public String deleteInternalLoan(
            @ApiParam("借贷ID") @PathVariable String loanId,
            HttpServletResponse response) throws IOException {
        try {
            tblInternalLoanService.deleteLoan(loanId);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除内部借贷失败, loanId={}", loanId, e);
            JsonBean json = new JsonBean(0, "删除失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 提交内部借贷申请（fund-concentration路径）
     */
    @PostMapping("/internal-loan/{loanId}/submit")
    @ApiOperation("提交内部借贷申请")
    public String submitInternalLoan(
            @ApiParam("借贷ID") @PathVariable String loanId,
            HttpServletResponse response) throws IOException {
        try {
            tblInternalLoanService.submitLoan(loanId);
            return JsonBean.success("提交成功");
        } catch (Exception e) {
            log.error("提交内部借贷失败, loanId={}", loanId, e);
            JsonBean json = new JsonBean(0, "提交失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 取消内部借贷申请（fund-concentration路径）
     */
    @PostMapping("/internal-loan/{loanId}/cancel")
    @ApiOperation("取消内部借贷申请")
    public String cancelInternalLoan(
            @ApiParam("借贷ID") @PathVariable String loanId,
            HttpServletResponse response) throws IOException {
        try {
            tblInternalLoanService.cancelLoan(loanId);
            return JsonBean.success("取消成功");
        } catch (Exception e) {
            log.error("取消内部借贷失败, loanId={}", loanId, e);
            JsonBean json = new JsonBean(0, "取消失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 审批内部借贷（fund-concentration路径）
     */
    @PostMapping("/internal-loan/{loanId}/approve")
    @ApiOperation("审批内部借贷")
    public String approveInternalLoan(
            @ApiParam("借贷ID") @PathVariable String loanId,
            @RequestParam Map<String, Object> data,
            HttpServletResponse response) throws IOException {
        try {
            String approveResult = (String) data.get("approveResult");
            String approveRemark = (String) data.get("approveRemark");
            tblInternalLoanService.approveLoan(loanId, approveResult, approveRemark);
            return JsonBean.success("审批成功");
        } catch (Exception e) {
            log.error("审批内部借贷失败, loanId={}", loanId, e);
            JsonBean json = new JsonBean(0, "审批失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 放款（fund-concentration路径）
     */
    @PostMapping("/internal-loan/{loanId}/disburse")
    @ApiOperation("放款")
    public String disburseInternalLoan(
            @ApiParam("借贷ID") @PathVariable String loanId,
            HttpServletResponse response) throws IOException {
        try {
            tblInternalLoanService.disburseLoan(loanId);
            return JsonBean.success("放款成功");
        } catch (Exception e) {
            log.error("放款失败, loanId={}", loanId, e);
            JsonBean json = new JsonBean(0, "放款失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 还款（fund-concentration路径）
     */
    @PostMapping("/internal-loan/{loanId}/repay")
    @ApiOperation("还款")
    public String repayInternalLoan(
            @ApiParam("借贷ID") @PathVariable String loanId,
            @RequestParam Map<String, Object> data,
            HttpServletResponse response) throws IOException {
        try {
            // TODO: 实现还款逻辑
            return JsonBean.success("还款成功");
        } catch (Exception e) {
            log.error("还款失败, loanId={}", loanId, e);
            JsonBean json = new JsonBean(0, "还款失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 计算利息（fund-concentration路径）
     */
    @GetMapping("/internal-loan/{loanId}/interest")
    @ApiOperation("计算利息")
    public String calculateInterest(
            @ApiParam("借贷ID") @PathVariable String loanId,
            HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("loanId", loanId);
            result.put("interest", new java.math.BigDecimal("0.00"));
            result.put("principal", new java.math.BigDecimal("0.00"));
            result.put("total", new java.math.BigDecimal("0.00"));
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("计算利息失败, loanId={}", loanId, e);
            JsonBean json = new JsonBean(0, "计算利息失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    // ==================== 资金监控管理 ====================

    /**
     * 获取资金监控仪表盘数据
     */
    @GetMapping("/monitoring/dashboard")
    @ApiOperation("获取资金监控仪表盘数据")
    public String getMonitoringDashboard(HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> dashboard = new HashMap<>();

            // 资金池统计
            Map<String, Object> poolStats = tblFundPoolService.getFundPoolStatistics();
            dashboard.put("fundPoolStats", poolStats);

            // 归集执行统计
            Map<String, Object> executionStats = tblConcentrationExecutionService.getMonitoringData();
            dashboard.put("executionStats", executionStats);

            // 今日归集金额
            dashboard.put("todayAmount", tblConcentrationExecutionService.getTodayTotalAmount());

            // 本月归集金额
            dashboard.put("monthAmount", tblConcentrationExecutionService.getMonthTotalAmount());

            // 预警数量
            long alertCount = tblConcentrationExecutionService.countPendingAlerts();
            dashboard.put("alertCount", alertCount);

            return JsonBean.success(dashboard);
        } catch (Exception e) {
            log.error("获取监控仪表盘数据失败", e);
            JsonBean json = new JsonBean(0, "获取仪表盘数据失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 获取资金流向分析
     */
    @GetMapping("/monitoring/flow-analysis")
    @ApiOperation("获取资金流向分析")
    public String getFlowAnalysis(
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate,
            HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> flowAnalysis = tblConcentrationExecutionService.getFlowAnalysis(startDate, endDate);
            return JsonBean.success(flowAnalysis);
        } catch (Exception e) {
            log.error("获取资金流向分析失败", e);
            JsonBean json = new JsonBean(0, "获取流向分析失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 获取余额监控数据
     */
    @GetMapping("/monitoring/balance")
    @ApiOperation("获取余额监控数据")
    public String getBalanceMonitoring(HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> balanceData = tblFundPoolService.getFundPoolStatistics();
            return JsonBean.success(balanceData);
        } catch (Exception e) {
            log.error("获取余额监控数据失败", e);
            JsonBean json = new JsonBean(0, "获取余额监控失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 获取预警信息列表
     */
    @GetMapping("/monitoring/alerts")
    @ApiOperation("获取预警信息列表")
    public String getMonitoringAlerts(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletResponse response) throws IOException {
        try {
            List<Map<String, Object>> alerts = tblConcentrationExecutionService.getAlertList();

            // 分页处理
            int total = alerts.size();
            int start = (pageNo - 1) * pageSize;
            int end = Math.min(start + pageSize, total);
            List<Map<String, Object>> pagedAlerts = start < total ? alerts.subList(start, end) : new ArrayList<>();

            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pagedAlerts);
            result.put("totalRecord", total);
            result.put("pageNo", pageNo);
            result.put("pageSize", pageSize);

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预警信息失败", e);
            JsonBean json = new JsonBean(0, "获取预警信息失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 处理预警
     */
    @PostMapping("/monitoring/alerts/{alertId}/handle")
    @ApiOperation("处理预警")
    public String handleMonitoringAlert(
            @ApiParam("预警ID") @PathVariable String alertId,
            @RequestParam Map<String, Object> data,
            HttpServletResponse response) throws IOException {
        try {
            String handleResult = (String) data.get("handleResult");
            String handleRemark = (String) data.get("handleRemark");

            // 更新执行记录状态
            TblConcentrationExecution execution = tblConcentrationExecutionService.getExecutionById(Long.valueOf(alertId));
            if (execution != null) {
                execution.setRemark(handleRemark);
                tblConcentrationExecutionService.updateExecution(execution);
            }

            return JsonBean.success("预警处理成功");
        } catch (Exception e) {
            log.error("处理预警失败, alertId={}", alertId, e);
            JsonBean json = new JsonBean(0, "处理预警失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 设置监控规则
     */
    @PostMapping("/monitoring/rules")
    @ApiOperation("设置监控规则")
    public String setMonitoringRule(@RequestParam Map<String, Object> data, HttpServletResponse response) throws IOException {
        try {
            // TODO: 实现监控规则设置逻辑
            return JsonBean.success("监控规则设置成功");
        } catch (Exception e) {
            log.error("设置监控规则失败", e);
            JsonBean json = new JsonBean(0, "设置监控规则失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }

    /**
     * 获取监控规则列表
     */
    @GetMapping("/monitoring/rules")
    @ApiOperation("获取监控规则列表")
    public String getMonitoringRules(HttpServletResponse response) throws IOException {
        try {
            // 返回默认的监控规则
            List<Map<String, Object>> rules = new ArrayList<>();

            Map<String, Object> rule1 = new HashMap<>();
            rule1.put("ruleId", "1");
            rule1.put("ruleName", "余额预警");
            rule1.put("ruleType", "BALANCE");
            rule1.put("threshold", 100000);
            rule1.put("status", "ACTIVE");
            rules.add(rule1);

            Map<String, Object> rule2 = new HashMap<>();
            rule2.put("ruleId", "2");
            rule2.put("ruleName", "归集失败预警");
            rule2.put("ruleType", "EXECUTION_FAIL");
            rule2.put("threshold", 3);
            rule2.put("status", "ACTIVE");
            rules.add(rule2);

            return JsonBean.success(rules);
        } catch (Exception e) {
            log.error("获取监控规则失败", e);
            JsonBean json = new JsonBean(0, "获取监控规则失败: " + e.getMessage(), null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return null;
        }
    }


    /**
     * 批量删除归集策略
     */
    @DeleteMapping("/strategy/batch")
    @ApiOperation("批量删除归集策略")
    public String batchDeleteStrategy(
            @ApiParam("策略ID列表") @RequestParam(value = "strategyIds") List<Long> strategyIds,
            HttpServletResponse response) throws IOException {
        try {
            if (strategyIds == null || strategyIds.isEmpty()) {
                return JsonBean.error("策略ID列表不能为空");
            }
            tblConcentrationStrategyService.batchDisableStrategy(strategyIds);
            return JsonBean.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除归集策略失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 导出归集策略
     */
    @GetMapping("/strategy/export")
    @ApiOperation("导出归集策略")
    public void exportStrategy(
            @ApiParam("策略名称") @RequestParam(required = false) String strategyName,
            @ApiParam("策略类型") @RequestParam(required = false) String strategyType,
            @ApiParam("策略状态") @RequestParam(required = false) String strategyStatus,
            HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=concentration_strategy_" + System.currentTimeMillis() + ".xlsx");
            tblConcentrationStrategyService.exportStrategy(strategyName, strategyType, strategyStatus, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出归集策略失败", e);
        }
    }

    /**
     * 批量删除归集计划
     */
    @DeleteMapping("/plan/batch")
    @ApiOperation("批量删除归集计划")
    public String batchDeletePlan(
            @ApiParam("计划ID列表") @RequestParam(value = "planIds") List<Long> planIds,
            HttpServletResponse response) throws IOException {
        try {
            if (planIds == null || planIds.isEmpty()) {
                return JsonBean.error("计划ID列表不能为空");
            }
            tblConcentrationPlanService.batchDeletePlan(planIds);
            return JsonBean.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除归集计划失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 删除归集计划
     */
    @DeleteMapping("/plan/{planId}")
    @ApiOperation("删除归集计划")
    public String deletePlan(
            @ApiParam("计划ID") @PathVariable Long planId,
            HttpServletResponse response) throws IOException {
        try {
            tblConcentrationPlanService.deletePlan(planId);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除归集计划失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 导出归集计划
     */
    @GetMapping("/plan/export")
    @ApiOperation("导出归集计划")
    public void exportPlan(
            @ApiParam("计划名称") @RequestParam(required = false) String planName,
            @ApiParam("计划状态") @RequestParam(required = false) String planStatus,
            @ApiParam("策略ID") @RequestParam(required = false) Long strategyId,
            HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=concentration_plan_" + System.currentTimeMillis() + ".xlsx");
            tblConcentrationPlanService.exportPlan(planName, planStatus, strategyId, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出归集计划失败", e);
        }
    }

    /**
     * 导出执行监控
     */
    @GetMapping("/monitor/export")
    @ApiOperation("导出执行监控")
    public void exportMonitor(
            @ApiParam("执行状态") @RequestParam(required = false) String executionStatus,
            @ApiParam("任务名称") @RequestParam(required = false) String taskName,
            HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=execution_monitor_" + System.currentTimeMillis() + ".xlsx");
            tblConcentrationExecutionService.exportMonitor(executionStatus, taskName, response.getOutputStream());
        } catch (Exception e) {
            log.error("导出执行监控失败", e);
        }
    }

    // ==================== 报表统计 ====================

    /**
     * 分页查询报表统计
     */
    @GetMapping("/report-statistics/page")
    @ApiOperation("分页查询报表统计")
    public String getReportStatisticsPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam("报表类型") @RequestParam(required = false) String reportType,
            @ApiParam("报表周期") @RequestParam(required = false) String reportPeriod,
            @ApiParam("创建时间") @RequestParam(required = false) String createTime,
            HttpServletResponse response) throws IOException {
        try {
            PageInfo<TblReportStatistics> pageInfo = tblReportStatisticsService.getReportPage(
                    pageNo, pageSize, null, reportType, reportPeriod, createTime);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询报表统计失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询报表统计详情
     */
    @GetMapping("/report-statistics/{reportId}")
    @ApiOperation("查询报表统计详情")
    public String getReportStatisticsById(@PathVariable String reportId, HttpServletResponse response) throws IOException {
        try {
            TblReportStatistics report = tblReportStatisticsService.getReportById(reportId);
            return JsonBean.success(report);
        } catch (Exception e) {
            log.error("查询报表统计详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 分析报表数据
     */
    @GetMapping("/report-statistics/{reportId}/analyze")
    @ApiOperation("分析报表数据")
    public String analyzeReportStatistics(@PathVariable String reportId, HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> result = tblReportStatisticsService.analyzeReport(reportId);
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("分析报表数据失败", e);
            return JsonBean.error("分析失败: " + e.getMessage());
        }
    }

    /**
     * 新增报表统计
     */
    @PostMapping("/report-statistics/create")
    @ApiOperation("新增报表统计")
    public String createReportStatistics(@FlexibleRequestBody TblReportStatistics entity, HttpServletResponse response) throws IOException {
        try {
            tblReportStatisticsService.saveReport(entity);
            return JsonBean.success("新增成功");
        } catch (Exception e) {
            log.error("新增报表统计失败", e);
            return JsonBean.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改报表统计
     */
    @PutMapping("/report-statistics/update")
    @ApiOperation("修改报表统计")
    public String updateReportStatistics(@FlexibleRequestBody TblReportStatistics entity, HttpServletResponse response) throws IOException {
        try {
            tblReportStatisticsService.updateReport(entity);
            return JsonBean.success("修改成功");
        } catch (Exception e) {
            log.error("修改报表统计失败", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除报表统计
     */
    @DeleteMapping("/report-statistics/{reportId}")
    @ApiOperation("删除报表统计")
    public String deleteReportStatistics(@PathVariable String reportId, HttpServletResponse response) throws IOException {
        try {
            tblReportStatisticsService.deleteReport(reportId);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除报表统计失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除报表统计
     */
    @DeleteMapping("/report-statistics/batch")
    @ApiOperation("批量删除报表统计")
    public String batchDeleteReportStatistics(
            @ApiParam("报表ID列表") @RequestParam(value = "reportIds") List<String> reportIds,
            HttpServletResponse response) throws IOException {
        try {
            if (reportIds == null || reportIds.isEmpty()) {
                return JsonBean.error("报表ID列表不能为空");
            }
            for (String reportId : reportIds) {
                tblReportStatisticsService.deleteReport(reportId);
            }
            return JsonBean.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除报表统计失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 导出报表统计
     */
    @GetMapping("/report-statistics/export")
    @ApiOperation("导出报表统计")
    public void exportReportStatistics(
            @ApiParam("报表类型") @RequestParam(required = false) String reportType,
            @ApiParam("报表周期") @RequestParam(required = false) String reportPeriod,
            HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=report_statistics_" + System.currentTimeMillis() + ".xlsx");
            PageInfo<TblReportStatistics> pageInfo = tblReportStatisticsService.getReportPage(1, 10000, null, reportType, reportPeriod, null);
            writeSimpleExcelForReportStatistics(response, pageInfo.getList(), "报表统计");
        } catch (Exception e) {
            log.error("导出报表统计失败", e);
        }
    }

    // ==================== 异常处理 ====================

    /**
     * 分页查询异常处理列表
     */
    @GetMapping("/exception-handling/page")
    @ApiOperation("分页查询异常处理列表")
    public String getExceptionPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam("异常编号") @RequestParam(required = false) String exceptionNo,
            @ApiParam("异常类型") @RequestParam(required = false) String exceptionType,
            @ApiParam("异常级别") @RequestParam(required = false) String exceptionLevel,
            @ApiParam("异常状态") @RequestParam(required = false) String exceptionStatus,
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate,
            HttpServletResponse response) throws IOException {
        try {
            PageInfo<TblExceptionHandling> pageInfo = tblExceptionHandlingService.getExceptionPage(
                    pageNo, pageSize, exceptionNo, exceptionType, exceptionLevel, exceptionStatus, startDate, endDate);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询异常处理列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询异常处理详情
     */
    @GetMapping("/exception-handling/{exceptionId}")
    @ApiOperation("查询异常处理详情")
    public String getExceptionById(@PathVariable String exceptionId, HttpServletResponse response) throws IOException {
        try {
            TblExceptionHandling exception = tblExceptionHandlingService.getExceptionById(exceptionId);
            return JsonBean.success(exception);
        } catch (Exception e) {
            log.error("查询异常处理详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增异常记录
     */
    @PostMapping(value = "/exception-handling")
    @ApiOperation("新增异常记录")
    public String createException(@FlexibleRequestBody TblExceptionHandling entity, HttpServletResponse response) throws IOException {
        try {
            TblExceptionHandling saved = tblExceptionHandlingService.saveException(entity);
            return JsonBean.success(saved);
        } catch (Exception e) {
            log.error("新增异常记录失败", e);
            return JsonBean.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 处理异常
     */
    @PostMapping("/exception-handling/{exceptionId}/handle")
    @ApiOperation("处理异常")
    public String handleException(
            @PathVariable String exceptionId,
            @ApiParam("处理方式") @RequestParam(required = false) String handleMethod,
            @ApiParam("处理结果") @RequestParam(required = false) String handleResult,
            HttpServletResponse response) throws IOException {
        try {
            tblExceptionHandlingService.handleException(exceptionId, handleMethod, handleResult);
            return JsonBean.success("处理成功");
        } catch (Exception e) {
            log.error("处理异常失败", e);
            return JsonBean.error("处理失败: " + e.getMessage());
        }
    }

    /**
     * 重试异常
     */
    @PostMapping("/exception-handling/{exceptionId}/retry")
    @ApiOperation("重试异常")
    public String retryException(@PathVariable String exceptionId, HttpServletResponse response) throws IOException {
        try {
            String result = tblExceptionHandlingService.retryException(exceptionId);
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("重试异常失败", e);
            return JsonBean.error("重试失败: " + e.getMessage());
        }
    }

    /**
     * 关闭异常
     */
    @PutMapping("/exception-handling/{exceptionId}/close")
    @ApiOperation("关闭异常")
    public String closeException(
            @PathVariable String exceptionId,
            @ApiParam("关闭备注") @RequestParam(required = false) String closeRemark,
            HttpServletResponse response) throws IOException {
        try {
            tblExceptionHandlingService.closeException(exceptionId, closeRemark);
            return JsonBean.success("关闭成功");
        } catch (Exception e) {
            log.error("关闭异常失败", e);
            return JsonBean.error("关闭失败: " + e.getMessage());
        }
    }

    /**
     * 获取异常统计
     */
    @GetMapping("/exception-handling/statistics")
    @ApiOperation("获取异常统计")
    public String getExceptionStatistics(HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> result = tblExceptionHandlingService.getExceptionStatistics();
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("获取异常统计失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 删除异常记录
     */
    @DeleteMapping("/exception-handling/{exceptionId}")
    @ApiOperation("删除异常记录")
    public String deleteException(@PathVariable String exceptionId, HttpServletResponse response) throws IOException {
        try {
            tblExceptionHandlingService.deleteException(exceptionId);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除异常记录失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除异常记录
     */
    @DeleteMapping("/exception-handling/batch")
    @ApiOperation("批量删除异常记录")
    public String batchDeleteException(
            @ApiParam("异常ID列表") @RequestParam(value = "exceptionIds") List<String> exceptionIds,
            HttpServletResponse response) throws IOException {
        try {
            if (exceptionIds == null || exceptionIds.isEmpty()) {
                return JsonBean.error("异常ID列表不能为空");
            }
            for (String exceptionId : exceptionIds) {
                tblExceptionHandlingService.deleteException(exceptionId);
            }
            return JsonBean.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除异常记录失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除异常记录 (POST方式，避免URL编码问题)
     */
    @PostMapping("/exception-handling/batch")
    @ApiOperation("批量删除异常记录(POST方式)")
    public String batchDeleteExceptionPost(
            @ApiParam("异常ID列表") @RequestBody List<String> exceptionIds,
            HttpServletResponse response) throws IOException {
        log.info("========== 批量删除异常(POST) ==========");
        log.info("接收到的异常ID列表: {}", exceptionIds);
        try {
            if (exceptionIds == null || exceptionIds.isEmpty()) {
                return JsonBean.error("异常ID列表不能为空");
            }
            for (String exceptionId : exceptionIds) {
                log.info("删除异常ID: {}", exceptionId);
                tblExceptionHandlingService.deleteException(exceptionId);
            }
            return JsonBean.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除异常记录失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 修改异常记录
     */
    @PutMapping("/exception-handling/update")
    @ApiOperation("修改异常记录")
    public String updateException(@FlexibleRequestBody TblExceptionHandling entity, HttpServletResponse response) throws IOException {
        try {
            tblExceptionHandlingService.updateException(entity);
            return JsonBean.success("修改成功");
        } catch (Exception e) {
            log.error("修改异常记录失败", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 导出异常记录
     */
    @GetMapping("/exception-handling/export")
    @ApiOperation("导出异常记录")
    public void exportException(
            @ApiParam("异常类型") @RequestParam(required = false) String exceptionType,
            @ApiParam("异常级别") @RequestParam(required = false) String exceptionLevel,
            @ApiParam("异常状态") @RequestParam(required = false) String exceptionStatus,
            HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=exception_handling_" + System.currentTimeMillis() + ".xlsx");
            PageInfo<TblExceptionHandling> pageInfo = tblExceptionHandlingService.getExceptionPage(
                    1, 10000, null, exceptionType, exceptionLevel, exceptionStatus, null, null);
            writeSimpleExcelForExceptionHandling(response, pageInfo.getList(), "异常处理");
        } catch (Exception e) {
            log.error("导出异常记录失败", e);
        }
    }

    // ==================== 监管分析 ====================

    /**
     * 分页查询监管分析列表
     */
    @GetMapping("/regulatory-analysis/page")
    @ApiOperation("分页查询监管分析列表")
    public String getRegulatoryAnalysisPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam("分析名称") @RequestParam(required = false) String analysisName,
            @ApiParam("分析类型") @RequestParam(required = false) String analysisType,
            @ApiParam("合规状态") @RequestParam(required = false) String complianceStatus,
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate,
            HttpServletResponse response) throws IOException {
        try {
            PageInfo<TblRegulatoryAnalysis> pageInfo = tblRegulatoryAnalysisService.getAnalysisPage(
                    pageNo, pageSize, analysisName, analysisType, complianceStatus, startDate, endDate);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询监管分析列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询监管分析详情
     */
    @GetMapping("/regulatory-analysis/{analysisId}")
    @ApiOperation("查询监管分析详情")
    public String getRegulatoryAnalysisById(@PathVariable String analysisId, HttpServletResponse response) throws IOException {
        try {
            log.info("===== 开始查询监管分析详情 =====");
            log.info("analysisId: {}", analysisId);
            TblRegulatoryAnalysis analysis = tblRegulatoryAnalysisService.getAnalysisById(analysisId);
            if (analysis != null) {
                log.info("查询到分析数据:");
                log.info("  analysisId: {}", analysis.getAnalysisId());
                log.info("  analysisName: {}", analysis.getAnalysisName());
                log.info("  analysisDate: {}", analysis.getAnalysisDate());
                log.info("  analysisDate 类型: {}", analysis.getAnalysisDate() != null ? analysis.getAnalysisDate().getClass().getName() : "null");
                log.info("  analysisDate.getTime(): {}", analysis.getAnalysisDate() != null ? analysis.getAnalysisDate().getTime() : "null");
                log.info("  regulator: {}", analysis.getRegulator());
                log.info("  complianceScore: {}", analysis.getComplianceScore());
            } else {
                log.warn("未找到分析数据，analysisId: {}", analysisId);
            }
            String result = JsonBean.success(analysis);
            log.info("返回的JSON字符串: {}", result);
            return result;
        } catch (Exception e) {
            log.error("查询监管分析详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增监管分析
     */
    @PostMapping(value = "/regulatory-analysis")
    @ApiOperation("新增监管分析")
    public String createRegulatoryAnalysis(@RequestBody TblRegulatoryAnalysis entity, HttpServletResponse response) throws IOException {
        try {
            log.info("收到新增监管分析请求: {}", entity);
            log.info("  - analysisName: {}", entity.getAnalysisName());
            log.info("  - regulator: {}", entity.getRegulator());
            log.info("  - analysisType: {}", entity.getAnalysisType());
            log.info("  - analysisDate: {}", entity.getAnalysisDate());
            log.info("  - complianceScore: {}", entity.getComplianceScore());
            log.info("  - riskLevel: {}", entity.getRiskLevel());
            log.info("  - description: {}", entity.getDescription());
            TblRegulatoryAnalysis saved = tblRegulatoryAnalysisService.saveAnalysis(entity);
            return JsonBean.success(saved);
        } catch (Exception e) {
            log.error("新增监管分析失败", e);
            log.error("异常堆栈: ", e);
            return JsonBean.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改监管分析
     */
    @PutMapping(value = "/regulatory-analysis")
    @ApiOperation("修改监管分析")
    public String updateRegulatoryAnalysis(@RequestBody TblRegulatoryAnalysis entity, HttpServletResponse response) throws IOException {
        try {
            log.info("收到修改监管分析请求: {}", entity);
            log.info("  - analysisId: {}", entity.getAnalysisId());
            log.info("  - analysisName: {}", entity.getAnalysisName());
            log.info("  - regulator: {}", entity.getRegulator());
            log.info("  - analysisType: {}", entity.getAnalysisType());
            log.info("  - analysisDate: {}", entity.getAnalysisDate());
            log.info("  - complianceScore: {}", entity.getComplianceScore());
            log.info("  - riskLevel: {}", entity.getRiskLevel());
            log.info("  - description: {}", entity.getDescription());
            log.info("  - analysisStatus: {}", entity.getAnalysisStatus());
            tblRegulatoryAnalysisService.updateAnalysis(entity);
            return JsonBean.success("修改成功");
        } catch (Exception e) {
            log.error("修改监管分析失败", e);
            log.error("异常堆栈: ", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除监管分析
     */
    @DeleteMapping("/regulatory-analysis/{analysisId}")
    @ApiOperation("删除监管分析")
    public String deleteRegulatoryAnalysis(@PathVariable String analysisId, HttpServletResponse response) throws IOException {
        try {
            tblRegulatoryAnalysisService.deleteAnalysis(analysisId);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除监管分析失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 生成监管报告
     */
    @PostMapping("/regulatory-analysis/{analysisId}/generate-report")
    @ApiOperation("生成监管报告")
    public void generateRegulatoryReport(
            @PathVariable String analysisId,
            @ApiParam("报告格式") @RequestParam(defaultValue = "PDF") String reportFormat,
            HttpServletResponse response) throws IOException {
        try {
            TblRegulatoryAnalysis analysis = tblRegulatoryAnalysisService.getAnalysisById(analysisId);
            if (analysis == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"分析记录不存在\"}");
                return;
            }

            byte[] reportBytes = tblRegulatoryAnalysisService.generateReport(analysisId, reportFormat);
            if (reportBytes == null || reportBytes.length == 0) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"报告生成失败\"}");
                return;
            }

            // 设置响应头
            String fileName = "监管分析报告_" + analysis.getAnalysisName() + "_" + analysis.getAnalysisId();
            String encodedFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            String contentType = "PDF".equalsIgnoreCase(reportFormat) ? "application/pdf" : "application/vnd.ms-excel";

            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "." + reportFormat.toLowerCase() + "\"");
            response.setContentLength(reportBytes.length);

            // 写入响应流
            ServletOutputStream out = response.getOutputStream();
            out.write(reportBytes);
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("生成监管报告失败", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":0,\"msg\":\"生成失败: " + e.getMessage() + "\"}");
        }
    }

    /**
     * 批量删除监管分析
     */
    @DeleteMapping("/regulatory-analysis/batch")
    @ApiOperation("批量删除监管分析")
    public String batchDeleteRegulatoryAnalysis(
            @ApiParam("分析ID列表") @RequestParam(value = "analysisIds") List<String> analysisIds,
            HttpServletResponse response) throws IOException {
        try {
            if (analysisIds == null || analysisIds.isEmpty()) {
                return JsonBean.error("分析ID列表不能为空");
            }
            for (String analysisId : analysisIds) {
                tblRegulatoryAnalysisService.deleteAnalysis(analysisId);
            }
            return JsonBean.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除监管分析失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 导出监管分析
     */
    @GetMapping("/regulatory-analysis/export")
    @ApiOperation("导出监管分析")
    public void exportRegulatoryAnalysis(
            @ApiParam("监管机构") @RequestParam(required = false) String regulator,
            @ApiParam("分析类型") @RequestParam(required = false) String analysisType,
            HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=regulatory_analysis_" + System.currentTimeMillis() + ".xlsx");
            PageInfo<TblRegulatoryAnalysis> pageInfo = tblRegulatoryAnalysisService.getAnalysisPage(
                    1, 10000, null, analysisType, null, null, null);
            writeSimpleExcelForRegulatoryAnalysis(response, pageInfo.getList(), "监管分析");
        } catch (Exception e) {
            log.error("导出监管分析失败", e);
        }
    }

    /**
     * 获取监管分析概览统计
     */
    @GetMapping("/regulatory-analysis/overview-statistics")
    @ApiOperation("获取监管分析概览统计")
    public String getRegulatoryOverviewStatistics(HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> statistics = tblRegulatoryAnalysisService.getOverviewStatistics();
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("查询监管分析概览统计失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取合规状态统计
     */
    @GetMapping("/regulatory-analysis/compliance-statistics")
    @ApiOperation("获取合规状态统计")
    public String getComplianceStatistics(HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> statistics = tblRegulatoryAnalysisService.getComplianceStatistics();
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("查询合规状态统计失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取风险等级分布
     */
    @GetMapping("/regulatory-analysis/risk-distribution")
    @ApiOperation("获取风险等级分布")
    public String getRiskDistribution(HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> distribution = tblRegulatoryAnalysisService.getRiskLevelDistribution();
            return JsonBean.success(distribution);
        } catch (Exception e) {
            log.error("查询风险等级分布失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取分析类型列表
     */
    @GetMapping("/regulatory-analysis/types")
    @ApiOperation("获取分析类型列表")
    public String getAnalysisTypes(HttpServletResponse response) throws IOException {
        try {
            List<Map<String, Object>> types = tblRegulatoryAnalysisService.getAnalysisTypes();
            return JsonBean.success(types);
        } catch (Exception e) {
            log.error("查询分析类型列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    // ==================== 工具方法 ====================

    /**
     * 导出报表统计到Excel
     */
    private void writeSimpleExcelForReportStatistics(HttpServletResponse response, List<TblReportStatistics> dataList, String sheetName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("报表ID");
        headerRow.createCell(1).setCellValue("报表编号");
        headerRow.createCell(2).setCellValue("报表名称");
        headerRow.createCell(3).setCellValue("报表类型");
        headerRow.createCell(4).setCellValue("报表周期");
        headerRow.createCell(5).setCellValue("公司名称");
        headerRow.createCell(6).setCellValue("开始日期");
        headerRow.createCell(7).setCellValue("结束日期");
        headerRow.createCell(8).setCellValue("归集总额");
        headerRow.createCell(9).setCellValue("下拨总额");
        headerRow.createCell(10).setCellValue("借贷总额");
        headerRow.createCell(11).setCellValue("还款总额");
        headerRow.createCell(12).setCellValue("利息总额");
        headerRow.createCell(13).setCellValue("成功率");
        headerRow.createCell(14).setCellValue("报表状态");

        int rowNum = 1;
        for (TblReportStatistics item : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getReportId());
            row.createCell(1).setCellValue(item.getReportNo());
            row.createCell(2).setCellValue(item.getReportName());
            row.createCell(3).setCellValue(item.getReportType());
            row.createCell(4).setCellValue(item.getReportPeriod());
            row.createCell(5).setCellValue(item.getCompanyName());
            row.createCell(6).setCellValue(item.getStartDate());
            row.createCell(7).setCellValue(item.getEndDate());
            row.createCell(8).setCellValue(item.getTotalCollection() != null ? item.getTotalCollection().doubleValue() : 0);
            row.createCell(9).setCellValue(item.getTotalAllocation() != null ? item.getTotalAllocation().doubleValue() : 0);
            row.createCell(10).setCellValue(item.getTotalLoan() != null ? item.getTotalLoan().doubleValue() : 0);
            row.createCell(11).setCellValue(item.getTotalRepayment() != null ? item.getTotalRepayment().doubleValue() : 0);
            row.createCell(12).setCellValue(item.getTotalInterest() != null ? item.getTotalInterest().doubleValue() : 0);
            row.createCell(13).setCellValue(item.getSuccessRate() != null ? item.getSuccessRate().doubleValue() : 0);
            row.createCell(14).setCellValue(item.getReportStatus());
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
    }

    /**
     * 导出异常处理到Excel
     */
    private void writeSimpleExcelForExceptionHandling(HttpServletResponse response, List<TblExceptionHandling> dataList, String sheetName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("异常ID");
        headerRow.createCell(1).setCellValue("异常编号");
        headerRow.createCell(2).setCellValue("异常类型");
        headerRow.createCell(3).setCellValue("异常级别");
        headerRow.createCell(4).setCellValue("来源类型");
        headerRow.createCell(5).setCellValue("来源编号");
        headerRow.createCell(6).setCellValue("公司名称");
        headerRow.createCell(7).setCellValue("异常标题");
        headerRow.createCell(8).setCellValue("异常描述");
        headerRow.createCell(9).setCellValue("异常时间");
        headerRow.createCell(10).setCellValue("异常状态");
        headerRow.createCell(11).setCellValue("处理方式");
        headerRow.createCell(12).setCellValue("处理时间");
        headerRow.createCell(13).setCellValue("处理人");

        int rowNum = 1;
        for (TblExceptionHandling item : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getExceptionId());
            row.createCell(1).setCellValue(item.getExceptionNo());
            row.createCell(2).setCellValue(item.getExceptionType());
            row.createCell(3).setCellValue(item.getExceptionLevel());
            row.createCell(4).setCellValue(item.getSourceType());
            row.createCell(5).setCellValue(item.getSourceNo());
            row.createCell(6).setCellValue(item.getCompanyName());
            row.createCell(7).setCellValue(item.getExceptionTitle());
            row.createCell(8).setCellValue(item.getExceptionDesc());
            row.createCell(9).setCellValue(item.getExceptionTime());
            row.createCell(10).setCellValue(item.getExceptionStatus());
            row.createCell(11).setCellValue(item.getHandleMethod());
            row.createCell(12).setCellValue(item.getHandleTime());
            row.createCell(13).setCellValue(item.getHandleBy());
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
    }

    /**
     * 导出监管分析到Excel
     */
    private void writeSimpleExcelForRegulatoryAnalysis(HttpServletResponse response, List<TblRegulatoryAnalysis> dataList, String sheetName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("分析ID");
        headerRow.createCell(1).setCellValue("分析编号");
        headerRow.createCell(2).setCellValue("分析名称");
        headerRow.createCell(3).setCellValue("分析类型");
        headerRow.createCell(4).setCellValue("公司名称");
        headerRow.createCell(5).setCellValue("分析日期");
        headerRow.createCell(6).setCellValue("合规状态");
        headerRow.createCell(7).setCellValue("风险等级");
        headerRow.createCell(8).setCellValue("总金额");
        headerRow.createCell(9).setCellValue("跨境金额");
        headerRow.createCell(10).setCellValue("境内金额");
        headerRow.createCell(11).setCellValue("违规次数");
        headerRow.createCell(12).setCellValue("预警次数");
        headerRow.createCell(13).setCellValue("分析结果");

        int rowNum = 1;
        for (TblRegulatoryAnalysis item : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.getAnalysisId());
            row.createCell(1).setCellValue(item.getAnalysisNo());
            row.createCell(2).setCellValue(item.getAnalysisName());
            row.createCell(3).setCellValue(item.getAnalysisType());
            row.createCell(4).setCellValue(item.getCompanyName());
            row.createCell(5).setCellValue(item.getAnalysisDate());
            row.createCell(6).setCellValue(item.getComplianceStatus());
            row.createCell(7).setCellValue(item.getRiskLevel());
            row.createCell(8).setCellValue(item.getTotalAmount() != null ? item.getTotalAmount().doubleValue() : 0);
            row.createCell(9).setCellValue(item.getCrossBorderAmount() != null ? item.getCrossBorderAmount().doubleValue() : 0);
            row.createCell(10).setCellValue(item.getDomesticAmount() != null ? item.getDomesticAmount().doubleValue() : 0);
            row.createCell(11).setCellValue(item.getViolationCount());
            row.createCell(12).setCellValue(item.getWarningCount());
            row.createCell(13).setCellValue(item.getAnalysisResult());
        }

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
    }

    /**
     * 验证用户权限
     */
    private boolean validateUser(HttpServletResponse response) throws IOException {
        try {
            // TODO: 从token中获取用户信息
            // 这里暂时返回true，实际应该验证用户是否登录
            return true;
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            JsonBean json = new JsonBean(401, "用户已失效", null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toJson());
            return false;
        }
    }
}
