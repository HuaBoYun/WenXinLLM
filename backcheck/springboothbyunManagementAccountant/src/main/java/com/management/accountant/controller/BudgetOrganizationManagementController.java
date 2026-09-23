package com.management.accountant.controller;

import com.management.accountant.oracle.service.budget.BudgetOrganizationService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.MyResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 预算组织架构管理Controller
 *
 * @author AI Agent
 * @date 2026-01-29
 */
@Slf4j
@Api(tags = "预算组织架构管理")
@RestController
@RequestMapping("/budget/organization")
public class BudgetOrganizationManagementController {

    @Resource(name = "oracleBudgetOrganizationService")
    private BudgetOrganizationService budgetOrganizationService;

    /**
     * createOrganizationStructure
     */
    @Operation(summary = "新增预算组织架构")
    @ApiOperation("新增预算组织架构")
    @PostMapping("/add")
    public MyJsonBean createOrganizationStructure(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetOrganizationService.createOrganizationStructure(params, companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("createOrganizationStructure失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * updateOrganizationStructure
     */
    @Operation(summary = "修改预算组织架构")
    @ApiOperation("修改预算组织架构")
    @PostMapping("/edit")
    public MyJsonBean updateOrganizationStructure(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetOrganizationService.updateOrganizationStructure(params, companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("updateOrganizationStructure失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * deleteOrganizationStructure
     */
    @Operation(summary = "删除预算组织架构")
    @ApiOperation("删除预算组织架构")
    @PostMapping("/delete")
    public MyJsonBean deleteOrganizationStructure(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetOrganizationService.deleteOrganizationStructure(params, companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("deleteOrganizationStructure失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * batchDeleteOrganizationStructures
     */
    @Operation(summary = "批量删除预算组织架构")
    @ApiOperation("批量删除预算组织架构")
    @PostMapping("/batchDelete")
    public MyJsonBean batchDeleteOrganizationStructures(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetOrganizationService.batchDeleteOrganizationStructures(params, companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("batchDeleteOrganizationStructures失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * toggleOrganizationStructure
     */
    @Operation(summary = "启用/禁用预算组织架构")
    @ApiOperation("启用/禁用预算组织架构")
    @PostMapping("/toggle")
    public MyJsonBean toggleOrganizationStructure(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetOrganizationService.toggleOrganizationStructure(params, companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("toggleOrganizationStructure失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * getOrganizationStructureList
     */
    @Operation(summary = "查询预算组织架构列表")
    @ApiOperation("查询预算组织架构列表")
    @GetMapping("/list")
    public MyJsonBean getOrganizationStructureList(

            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetOrganizationService.getOrganizationStructureList(companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("getOrganizationStructureList失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * getOrganizationTree
     */
    @Operation(summary = "查询预算组织架构树")
    @ApiOperation("查询预算组织架构树")
    @GetMapping("/tree")
    public MyJsonBean getOrganizationTree(
            @RequestParam(required = false) String orgId,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetOrganizationService.getOrganizationTree(companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("getOrganizationTree失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

}
