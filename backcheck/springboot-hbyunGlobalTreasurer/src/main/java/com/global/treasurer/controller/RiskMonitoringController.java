package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.RiskMonitoringDTO;
import com.global.treasurer.dto.RiskMonitoringQueryDTO;
import com.global.treasurer.entity.TblRiskMonitoring;
import com.global.treasurer.service.IRiskMonitoringService;
import com.hbfk.util.user.UserProvider;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 风险监控Controller
 *
 * @author 华博云开发团队
 * @since 2025-03-24
 */
@RestController
@RequestMapping("/risk-management/monitoring")
@Api(tags = "风险监控管理")
public class RiskMonitoringController {

    @Autowired
    private IRiskMonitoringService riskMonitoringService;

    @Autowired
    private UserProvider userProvider;

    @GetMapping("/page")
    @ApiOperation("分页查询风险监控记录")
    public String getRiskMonitoringPage(RiskMonitoringQueryDTO queryDTO, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) return JsonBean.error("用户已失效");
            PageInfo<TblRiskMonitoring> pageInfo = riskMonitoringService.selectRiskMonitoringList(queryDTO);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/{recordId}")
    @ApiOperation("根据ID查询风险监控记录")
    public String getRiskMonitoring(@PathVariable Long recordId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) return JsonBean.error("用户已失效");
            TblRiskMonitoring record = riskMonitoringService.selectRiskMonitoringById(recordId);
            if (record == null) return JsonBean.error("记录不存在");
            return JsonBean.success(record);
        } catch (Exception e) {
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation("新增风险监控记录")
    public String createRiskMonitoring(@FlexibleRequestBody RiskMonitoringDTO dto, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) return JsonBean.error("用户已失效");
            TblRiskMonitoring record = riskMonitoringService.insertRiskMonitoring(dto);
            return JsonBean.success("创建成功", record);
        } catch (Exception e) {
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation("更新风险监控记录")
    public String updateRiskMonitoring(@FlexibleRequestBody RiskMonitoringDTO dto, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) return JsonBean.error("用户已失效");
            TblRiskMonitoring record = riskMonitoringService.updateRiskMonitoring(dto);
            return JsonBean.success("更新成功", record);
        } catch (Exception e) {
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{recordId}")
    @ApiOperation("删除风险监控记录")
    public String deleteRiskMonitoring(@PathVariable Long recordId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) return JsonBean.error("用户已失效");
            boolean result = riskMonitoringService.deleteRiskMonitoring(recordId);
            return result ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (Exception e) {
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除风险监控记录")
    public String batchDeleteRiskMonitorings(@FlexibleRequestBody Map<String, List<Long>> body, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) return JsonBean.error("用户已失效");
            List<Long> ids = body.get("ids");
            if (ids == null || ids.isEmpty()) return JsonBean.error("请选择要删除的记录");
            boolean result = riskMonitoringService.batchDeleteRiskMonitorings(ids);
            return result ? JsonBean.success("批量删除成功") : JsonBean.error("批量删除失败");
        } catch (Exception e) {
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/{recordId}/trigger-alert")
    @ApiOperation("触发风险警报")
    public String triggerAlert(@PathVariable Long recordId,
                               @RequestParam(required = false, defaultValue = "手动触发警报") String alertMessage,
                               HttpServletResponse response) {
        try {
            if (userProvider.get() == null) return JsonBean.error("用户已失效");
            TblRiskMonitoring record = riskMonitoringService.triggerAlert(recordId, alertMessage);
            return JsonBean.success("警报已触发", record);
        } catch (Exception e) {
            return JsonBean.error("触发警报失败: " + e.getMessage());
        }
    }

    @PostMapping("/{recordId}/handle")
    @ApiOperation("处理风险监控")
    public String handleMonitoring(@PathVariable Long recordId,
                                   @RequestParam(required = false, defaultValue = "") String actionTaken,
                                   HttpServletResponse response) {
        try {
            if (userProvider.get() == null) return JsonBean.error("用户已失效");
            TblRiskMonitoring record = riskMonitoringService.handleMonitoring(recordId, actionTaken);
            return JsonBean.success("处理成功", record);
        } catch (Exception e) {
            return JsonBean.error("处理失败: " + e.getMessage());
        }
    }

    @GetMapping("/overview")
    @ApiOperation("获取风险监控概览统计")
    public String getOverview(@RequestParam(required = false) Long orgId, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) return JsonBean.error("用户已失效");
            Map<String, Object> overview = riskMonitoringService.getOverview(orgId);
            return JsonBean.success(overview);
        } catch (Exception e) {
            return JsonBean.error("获取概览失败: " + e.getMessage());
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出风险监控数据")
    public void exportRiskMonitoring(@FlexibleRequestBody Map<String, Object> body, HttpServletResponse response) {
        try {
            if (userProvider.get() == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("用户已失效"));
                return;
            }
            riskMonitoringService.exportRiskMonitoring(body, response);
        } catch (Exception e) {
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("导出失败: " + e.getMessage()));
            } catch (Exception ex) { /* ignore */ }
        }
    }
}

