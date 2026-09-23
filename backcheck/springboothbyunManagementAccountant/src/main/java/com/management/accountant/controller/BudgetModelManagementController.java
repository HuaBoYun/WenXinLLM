package com.management.accountant.controller;

import com.management.accountant.oracle.service.budget.BudgetModelService;
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
 * 预算模型管理Controller
 *
 * @author AI Agent
 * @date 2026-01-29
 */
@Slf4j
@Api(tags = "预算模型管理")
@RestController
@RequestMapping("/budget/model")
public class BudgetModelManagementController {

    @Resource(name = "oracleBudgetModelService")
    private BudgetModelService budgetModelService;

    /**
     * saveBudgetModel
     */
    @ApiOperation("saveBudgetModel")
    @PostMapping("/save")
    public MyJsonBean saveBudgetModel(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetModelService.saveBudgetModel(params, companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("saveBudgetModel失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * deleteBudgetModel
     */
    @ApiOperation("deleteBudgetModel")
    @PostMapping("/delete")
    public MyJsonBean deleteBudgetModel(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetModelService.deleteBudgetModel(params, companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("deleteBudgetModel失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * copyBudgetModel
     */
    @ApiOperation("copyBudgetModel")
    @PostMapping("/copy")
    public MyJsonBean copyBudgetModel(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetModelService.copyBudgetModel(params, companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("copyBudgetModel失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * getBudgetModelList
     */
    @ApiOperation("getBudgetModelList")
    @GetMapping("/list")
    public MyJsonBean getBudgetModelList(

            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetModelService.getBudgetModelList(companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("getBudgetModelList失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * getBudgetModelDetail
     */
    @ApiOperation("getBudgetModelDetail")
    @GetMapping("/detail")
    public MyJsonBean getBudgetModelDetail(

            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetModelService.getBudgetModelDetail(companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("getBudgetModelDetail失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

}
