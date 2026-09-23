package com.global.treasurer.controller;

import com.global.treasurer.entity.TblMonitoring;
import com.global.treasurer.service.MonitoringService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/settlement/monitoring"})
@Api(tags = "结算监控管理")
public class MonitoringController {
    @Resource
    private MonitoringService monitoringService;
    @Resource
    private UserProvider userProvider;

    @GetMapping("/page")
    @ApiOperation("分页查询监控数据")
    public String getMonitoringPage(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> result = monitoringService.getMonitoringPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询监控数据")
    public String getMonitoringById(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            TblMonitoring monitoring = monitoringService.getMonitoringById(id);
            if (monitoring == null) {
                return new JsonBean(0, "监控数据不存在", null).toJson();
            }
            return JsonBean.success(monitoring);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ApiOperation("新增监控数据")
    public String addMonitoring(@FlexibleRequestBody TblMonitoring monitoring) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result = monitoringService.createMonitoring(monitoring);
            if (result > 0) {
                return JsonBean.success("创建成功");
            } else {
                return new JsonBean(0, "创建失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ApiOperation("修改监控数据")
    public String updateMonitoring(@FlexibleRequestBody TblMonitoring monitoring) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result = monitoringService.updateMonitoring(monitoring);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除监控数据")
    public String deleteMonitoring(@PathVariable String ids) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            String[] idArray = ids.split(",");
            List<Long> idList = new java.util.ArrayList<>();
            for (String id : idArray) {
                idList.add(Long.parseLong(id));
            }
            int result = monitoringService.deleteMonitoring(idList);
            if (result > 0) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/alerts")
    @ApiOperation("查询告警记录")
    public String getAlerts(@RequestParam(required = false) Long orgId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 如果前端没有传递orgId,从用户信息中获取
            if (orgId == null) {
                orgId = loginStaff.getCurrentOrg().getOrgid().longValue();
            }

            List<TblMonitoring> list = monitoringService.getAlerts(orgId);
            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/critical-alerts")
    @ApiOperation("查询严重告警")
    public String getCriticalAlerts(@RequestParam Long orgId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<TblMonitoring> list = monitoringService.getCriticalAlerts(orgId);
            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/health-status")
    @ApiOperation("查询系统健康状态")
    public String getSystemHealthStatus(@RequestParam(required = false) Long orgId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 如果前端没有传递orgId,从用户信息中获取
            if (orgId == null) {
                orgId = loginStaff.getCurrentOrg().getOrgid().longValue();
            }

            Map<String, Object> status = monitoringService.getSystemHealthStatus(orgId);
            return JsonBean.success(status);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/dashboard")
    @ApiOperation("查询监控仪表板数据")
    public String getDashboardData(@RequestParam Long orgId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> dashboard = monitoringService.getDashboardData(orgId);
            return JsonBean.success(dashboard);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/trend")
    @ApiOperation("查询监控趋势")
    public String getMonitoringTrend(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<Map<String, Object>> trend = monitoringService.getMonitoringTrend(params);
            return JsonBean.success(trend);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{id}/send-alert")
    @ApiOperation("发送告警")
    public String sendAlert(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result = monitoringService.sendAlert(id);
            if (result > 0) {
                return JsonBean.success("告警发送成功");
            } else {
                return new JsonBean(0, "告警发送失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "告警发送失败: " + e.getMessage(), null).toJson();
        }
    }
}
