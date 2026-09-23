package com.management.accountant.controller;

import com.management.accountant.oracle.service.budget.BudgetDimensionService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.MyResponseFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 预算维度配置管理Controller
 *
 * @author AI Agent
 * @date 2026-01-29
 */
@Slf4j
@Api(tags = "预算维度配置管理")
@RestController
@RequestMapping("/budget/dimension")
public class BudgetDimensionManagementController {

    @Resource(name = "oracleBudgetDimensionService")
    private BudgetDimensionService budgetDimensionService;

    /**
     * getDimension
     */
    @ApiOperation("getDimension")
    @PostMapping("/page")
    public MyJsonBean getDimension(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetDimensionService.getDimension(params, companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("getDimension失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * getOrganizationStructurePage
     */
    @ApiOperation("getOrganizationStructurePage")
    @PostMapping("/save")
    public MyJsonBean getOrganizationStructurePage(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetDimensionService.getOrganizationStructurePage(params, companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("getOrganizationStructurePage失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * deleteDimensionConfiguration
     */
    @ApiOperation("deleteDimensionConfiguration")
    @PostMapping("/delete")
    public MyJsonBean deleteDimensionConfiguration(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetDimensionService.deleteDimensionConfiguration(params, companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("deleteDimensionConfiguration失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * getDimensionDetail
     */
    @ApiOperation("getDimensionDetail")
    @GetMapping("/detail")
    public MyJsonBean getDimensionDetail(

            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetDimensionService.getDimensionDetail(companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("getDimensionDetail失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * getDimensionList
     */
    @ApiOperation("getDimensionList")
    @GetMapping("/list")
    public MyJsonBean getDimensionList(

            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetDimensionService.getDimensionList(companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("getDimensionList失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * getDimensionTree
     */
    @ApiOperation("getDimensionTree")
    @GetMapping("/tree")
    public MyJsonBean getDimensionTree(
            @RequestParam(required = false) String orgId,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetDimensionService.getDimensionTree(companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("getDimensionTree失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

}
