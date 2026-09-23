package com.management.accountant.controller;

import com.management.accountant.oracle.service.budget.BudgetIndicatorService;
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
 * 预算指标管理Controller
 *
 * @author AI Agent
 * @date 2026-01-29
 */
@Slf4j
@Api(tags = "预算指标管理")
@RestController
@RequestMapping("/budget/indicator")
public class BudgetIndicatorManagementController {

    @Resource(name = "oracleBudgetIndicatorService")
    private BudgetIndicatorService budgetIndicatorService;

    /**
     * saveIndicator
     */
    @ApiOperation("saveIndicator")
    @PostMapping("/save")
    public MyJsonBean saveIndicator(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetIndicatorService.saveIndicator(params, companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("saveIndicator失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * deleteIndicator
     */
    @ApiOperation("deleteIndicator")
    @PostMapping("/delete")
    public MyJsonBean deleteIndicator(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetIndicatorService.deleteIndicator(params, companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("deleteIndicator失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * getIndicatorTree
     */
    @ApiOperation("getIndicatorTree")
    @GetMapping("/tree")
    public MyJsonBean getIndicatorTree(
            @RequestParam(required = false) String orgId,
            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetIndicatorService.getIndicatorTree(companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("getIndicatorTree失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * getIndicatorList
     */
    @ApiOperation("getIndicatorList")
    @GetMapping("/list")
    public MyJsonBean getIndicatorList(

            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetIndicatorService.getIndicatorList(companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("getIndicatorList失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * getIndicatorDetail
     */
    @ApiOperation("getIndicatorDetail")
    @GetMapping("/detail")
    public MyJsonBean getIndicatorDetail(

            HttpServletRequest request) {
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            String userName = request.getHeader("userName");

            // 调用Service
            Map<String, Object> result = budgetIndicatorService.getIndicatorDetail(companyId, userId);

            if ((Boolean) result.getOrDefault("success", false)) {
                return MyResponseFormat.success(result.get("data"), (String) result.getOrDefault("message", "操作成功"));
            } else {
                return MyResponseFormat.error((String) result.getOrDefault("message", "操作失败"));
            }
        } catch (Exception e) {
            log.error("getIndicatorDetail失败", e);
            return MyResponseFormat.error("操作失败: " + e.getMessage());
        }
    }

}
