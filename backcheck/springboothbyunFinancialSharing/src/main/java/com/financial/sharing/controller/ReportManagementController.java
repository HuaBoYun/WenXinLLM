package com.financial.sharing.controller;

import com.financial.sharing.dto.AlertQueryParam;
import com.financial.sharing.dto.BudgetExecutionQueryParam;
import com.financial.sharing.dto.CostTrendQueryParam;
import com.financial.sharing.service.ReportManagementService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.financial.sharing.util.JsonMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 报表管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-31
 */
@Api(tags = "管理会计-报表管理模块")
@RestController
@RequestMapping("/reports/management")
@CrossOrigin
@Validated
@Slf4j
public class ReportManagementController {

    @Resource
    private UserProvider userProvider;

    @Resource
    private ReportManagementService reportManagementService;

    /**
     * 验证用户权限
     */
    private TblStaffUtil validateUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return null;
            }
            return loginStaff;
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return null;
        }
    }

    /**
     * 创建错误响应
     */
    private String createErrorResponse(String message) {
        JsonBean json = new JsonBean(0, message, null);
        return JsonMapper.toJson(json);
    }

    /**
     * 创建成功响应
     */
    private String createSuccessResponse(Object data) {
        JsonBean json = new JsonBean(1, "成功", data);
        return JsonMapper.toJson(json);
    }

    @ApiOperation("获取管理概览统计")
    @GetMapping("/getOverview")
    public String getOverview(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam("账套ID") @RequestParam(required = false) Integer bookId,
            @ApiParam("租户ID") @RequestParam(required = false) Integer tenantId) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            log.info("获取管理概览统计, bookId={}, tenantId={}", bookId, tenantId);

            JsonBean result = reportManagementService.getOverview(bookId, tenantId);
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("获取管理概览统计失败", e);
            return createErrorResponse("获取管理概览统计失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本趋势分析数据")
    @PostMapping("/getCostTrend")
    public String getCostTrend(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody CostTrendQueryParam param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            log.info("获取成本趋势分析数据, param={}", param);

            JsonBean result = reportManagementService.getCostTrend(param);
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("获取成本趋势分析数据失败", e);
            return createErrorResponse("获取成本趋势分析数据失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取预算执行分析数据")
    @PostMapping("/getBudgetExecution")
    public String getBudgetExecution(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody BudgetExecutionQueryParam param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            log.info("获取预算执行分析数据, param={}", param);

            JsonBean result = reportManagementService.getBudgetExecution(param);
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("获取预算执行分析数据失败", e);
            return createErrorResponse("获取预算执行分析数据失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取异常预警列表")
    @GetMapping("/getAlerts")
    public String getAlerts(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam("账套ID") @RequestParam(required = false) Integer bookId,
            @ApiParam("租户ID") @RequestParam(required = false) Integer tenantId,
            @ApiParam("预警类型") @RequestParam(required = false, defaultValue = "all") String alertType,
            @ApiParam("预警级别") @RequestParam(required = false, defaultValue = "all") String alertLevel,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @ApiParam("页大小") @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            log.info("获取异常预警列表, bookId={}, tenantId={}, alertType={}, alertLevel={}, pageNumber={}, pageSize={}",
                    bookId, tenantId, alertType, alertLevel, pageNumber, pageSize);

            AlertQueryParam param = new AlertQueryParam();
            param.setBookId(bookId);
            param.setTenantId(tenantId);
            param.setAlertType(alertType);
            param.setAlertLevel(alertLevel);
            param.setPageNumber(pageNumber);
            param.setPageSize(pageSize);

            JsonBean result = reportManagementService.getAlerts(param);
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("获取异常预警列表失败", e);
            return createErrorResponse("获取异常预警列表失败: " + e.getMessage());
        }
    }
}
