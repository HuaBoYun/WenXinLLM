package com.financial.sharing.controller;

import com.financial.sharing.dto.VisualizationQueryParam;
import com.financial.sharing.service.ReportVisualizationService;
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
 * 报表可视化控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-31
 */
@Api(tags = "管理会计-报表可视化模块")
@RestController
@RequestMapping("/reports/visualization")
@CrossOrigin
@Validated
@Slf4j
public class ReportVisualizationController {

    @Resource
    private UserProvider userProvider;

    @Resource
    private ReportVisualizationService reportVisualizationService;

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

    @ApiOperation("获取可视化综合数据")
    @PostMapping("/getData")
    public String getVisualizationData(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody VisualizationQueryParam param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            log.info("获取可视化综合数据, param={}", param);

            JsonBean result = reportVisualizationService.getVisualizationData(param);
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("获取可视化综合数据失败", e);
            return createErrorResponse("获取可视化综合数据失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出可视化报告")
    @PostMapping("/export")
    public String exportVisualizationReport(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody VisualizationQueryParam param,
            @ApiParam("导出类型(pdf/excel)") @RequestParam(defaultValue = "pdf") String exportType) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            log.info("导出可视化报告, exportType={}, param={}", exportType, param);

            JsonBean result = reportVisualizationService.exportVisualizationReport(param, exportType, response);
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("导出可视化报告失败", e);
            return createErrorResponse("导出可视化报告失败: " + e.getMessage());
        }
    }
}
