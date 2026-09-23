package com.management.accountant.controller.ss;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.ss.SsServicePortal;
import com.management.accountant.service.ss.SsServicePortalService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 服务门户Controller
 * 提供服务门户管理的REST API接口
 * 
 * @author AI Assistant
 * @since 2025-01-01
 */
@Slf4j
@RestController
@RequestMapping("/accountant/ss/servicePortal")
@Api(tags = "服务门户管理")
public class SsServicePortalController {

    @Autowired
    private SsServicePortalService servicePortalService;

    @GetMapping("/page")
    @ApiOperation("分页查询服务门户")
    public MyJsonBean getServicePortalPage(@ApiParam("查询参数") @RequestParam Map<String, Object> params) {
        try {
            IPage<SsServicePortal> page = servicePortalService.getServicePortalPage(params);
            return MyJsonBean.success(page);
        } catch (Exception e) {
            log.error("分页查询服务门户失败", e);
            return MyJsonBean.error("分页查询服务门户失败: " + e.getMessage());
        }
    }

    @GetMapping("/{portalId}")
    @ApiOperation("根据ID查询服务门户详情")
    public MyJsonBean getServicePortalById(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            SsServicePortal servicePortal = servicePortalService.getServicePortalById(portalId, tenantId);
            return MyJsonBean.success(servicePortal);
        } catch (Exception e) {
            log.error("查询服务门户详情失败", e);
            return MyJsonBean.error("查询服务门户详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/code/{portalCode}")
    @ApiOperation("根据门户编码查询服务门户")
    public MyJsonBean getServicePortalByCode(
            @ApiParam("门户编码") @PathVariable String portalCode,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            SsServicePortal servicePortal = servicePortalService.getServicePortalByCode(portalCode, tenantId);
            return MyJsonBean.success(servicePortal);
        } catch (Exception e) {
            log.error("根据编码查询服务门户失败", e);
            return MyJsonBean.error("根据编码查询服务门户失败: " + e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation("创建服务门户")
    public MyJsonBean createServicePortal(
            @ApiParam("服务门户信息") @RequestBody SsServicePortal servicePortal,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.createServicePortal(servicePortal, tenantId);
            return success ? MyJsonBean.success("创建成功") : MyJsonBean.error("创建失败");
        } catch (Exception e) {
            log.error("创建服务门户失败", e);
            return MyJsonBean.error("创建服务门户失败: " + e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation("更新服务门户")
    public MyJsonBean updateServicePortal(
            @ApiParam("服务门户信息") @RequestBody SsServicePortal servicePortal,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.updateServicePortal(servicePortal, tenantId);
            return success ? MyJsonBean.success("更新成功") : MyJsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新服务门户失败", e);
            return MyJsonBean.error("更新服务门户失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{portalId}")
    @ApiOperation("删除服务门户")
    public MyJsonBean deleteServicePortal(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.deleteServicePortal(portalId, tenantId);
            return success ? MyJsonBean.success("删除成功") : MyJsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除服务门户失败", e);
            return MyJsonBean.error("删除服务门户失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除服务门户")
    public MyJsonBean batchDeleteServicePortal(
            @ApiParam("门户ID列表") @RequestBody List<Long> portalIds,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.batchDeleteServicePortal(portalIds, tenantId);
            return success ? MyJsonBean.success("批量删除成功") : MyJsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除服务门户失败", e);
            return MyJsonBean.error("批量删除服务门户失败: " + e.getMessage());
        }
    }

    @PostMapping("/{portalId}/activate")
    @ApiOperation("激活服务门户")
    public MyJsonBean activateServicePortal(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.activateServicePortal(portalId, tenantId);
            return success ? MyJsonBean.success("激活成功") : MyJsonBean.error("激活失败");
        } catch (Exception e) {
            log.error("激活服务门户失败", e);
            return MyJsonBean.error("激活服务门户失败: " + e.getMessage());
        }
    }

    @PostMapping("/{portalId}/deactivate")
    @ApiOperation("停用服务门户")
    public MyJsonBean deactivateServicePortal(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("停用原因") @RequestParam String reason,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.deactivateServicePortal(portalId, reason, tenantId);
            return success ? MyJsonBean.success("停用成功") : MyJsonBean.error("停用失败");
        } catch (Exception e) {
            log.error("停用服务门户失败", e);
            return MyJsonBean.error("停用服务门户失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/activate")
    @ApiOperation("批量激活服务门户")
    public MyJsonBean batchActivateServicePortal(
            @ApiParam("门户ID列表") @RequestBody List<Long> portalIds,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.batchActivateServicePortal(portalIds, tenantId);
            return success ? MyJsonBean.success("批量激活成功") : MyJsonBean.error("批量激活失败");
        } catch (Exception e) {
            log.error("批量激活服务门户失败", e);
            return MyJsonBean.error("批量激活服务门户失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/deactivate")
    @ApiOperation("批量停用服务门户")
    public MyJsonBean batchDeactivateServicePortal(
            @ApiParam("门户ID列表") @RequestBody List<Long> portalIds,
            @ApiParam("停用原因") @RequestParam String reason,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.batchDeactivateServicePortal(portalIds, reason, tenantId);
            return success ? MyJsonBean.success("批量停用成功") : MyJsonBean.error("批量停用失败");
        } catch (Exception e) {
            log.error("批量停用服务门户失败", e);
            return MyJsonBean.error("批量停用服务门户失败: " + e.getMessage());
        }
    }

    @PostMapping("/{portalId}/archive")
    @ApiOperation("归档服务门户")
    public MyJsonBean archiveServicePortal(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("归档原因") @RequestParam String reason,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.archiveServicePortal(portalId, reason, tenantId);
            return success ? MyJsonBean.success("归档成功") : MyJsonBean.error("归档失败");
        } catch (Exception e) {
            log.error("归档服务门户失败", e);
            return MyJsonBean.error("归档服务门户失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/archive")
    @ApiOperation("批量归档服务门户")
    public MyJsonBean batchArchiveServicePortal(
            @ApiParam("门户ID列表") @RequestBody List<Long> portalIds,
            @ApiParam("归档原因") @RequestParam String reason,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.batchArchiveServicePortal(portalIds, reason, tenantId);
            return success ? MyJsonBean.success("批量归档成功") : MyJsonBean.error("批量归档失败");
        } catch (Exception e) {
            log.error("批量归档服务门户失败", e);
            return MyJsonBean.error("批量归档服务门户失败: " + e.getMessage());
        }
    }

    @PostMapping("/{portalId}/maintenance/start")
    @ApiOperation("启动维护模式")
    public MyJsonBean startMaintenance(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("维护原因") @RequestParam String reason,
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.startMaintenance(portalId, reason, startTime, endTime, tenantId);
            return success ? MyJsonBean.success("启动维护模式成功") : MyJsonBean.error("启动维护模式失败");
        } catch (Exception e) {
            log.error("启动维护模式失败", e);
            return MyJsonBean.error("启动维护模式失败: " + e.getMessage());
        }
    }

    @PostMapping("/{portalId}/maintenance/end")
    @ApiOperation("结束维护模式")
    public MyJsonBean endMaintenance(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("维护结果") @RequestParam String result,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.endMaintenance(portalId, result, tenantId);
            return success ? MyJsonBean.success("结束维护模式成功") : MyJsonBean.error("结束维护模式失败");
        } catch (Exception e) {
            log.error("结束维护模式失败", e);
            return MyJsonBean.error("结束维护模式失败: " + e.getMessage());
        }
    }

    @PostMapping("/{portalId}/configure")
    @ApiOperation("配置门户")
    public MyJsonBean configurePortal(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("配置类型") @RequestParam String configType,
            @ApiParam("配置数据") @RequestBody Map<String, Object> configData,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.configurePortal(portalId, configType, configData, tenantId);
            return success ? MyJsonBean.success("配置成功") : MyJsonBean.error("配置失败");
        } catch (Exception e) {
            log.error("配置门户失败", e);
            return MyJsonBean.error("配置门户失败: " + e.getMessage());
        }
    }

    @PostMapping("/{portalId}/config/reset")
    @ApiOperation("重置门户配置")
    public MyJsonBean resetPortalConfig(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("配置类型") @RequestParam String configType,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.resetPortalConfig(portalId, configType, tenantId);
            return success ? MyJsonBean.success("重置配置成功") : MyJsonBean.error("重置配置失败");
        } catch (Exception e) {
            log.error("重置门户配置失败", e);
            return MyJsonBean.error("重置门户配置失败: " + e.getMessage());
        }
    }

    @PostMapping("/{sourcePortalId}/config/copy/{targetPortalId}")
    @ApiOperation("复制门户配置")
    public MyJsonBean copyPortalConfig(
            @ApiParam("源门户ID") @PathVariable Long sourcePortalId,
            @ApiParam("目标门户ID") @PathVariable Long targetPortalId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.copyPortalConfig(sourcePortalId, targetPortalId, tenantId);
            return success ? MyJsonBean.success("复制配置成功") : MyJsonBean.error("复制配置失败");
        } catch (Exception e) {
            log.error("复制门户配置失败", e);
            return MyJsonBean.error("复制门户配置失败: " + e.getMessage());
        }
    }

    @PostMapping("/{portalId}/setDefault")
    @ApiOperation("设置默认门户")
    public MyJsonBean setDefaultPortal(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.setDefaultPortal(portalId, tenantId);
            return success ? MyJsonBean.success("设置默认门户成功") : MyJsonBean.error("设置默认门户失败");
        } catch (Exception e) {
            log.error("设置默认门户失败", e);
            return MyJsonBean.error("设置默认门户失败: " + e.getMessage());
        }
    }

    @PostMapping("/{portalId}/permissions")
    @ApiOperation("设置门户权限")
    public MyJsonBean setPortalPermissions(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("权限配置") @RequestBody Map<String, Object> permissions,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.setPortalPermissions(portalId, permissions, tenantId);
            return success ? MyJsonBean.success("设置权限成功") : MyJsonBean.error("设置权限失败");
        } catch (Exception e) {
            log.error("设置门户权限失败", e);
            return MyJsonBean.error("设置门户权限失败: " + e.getMessage());
        }
    }

    @PostMapping("/{portalId}/access")
    @ApiOperation("更新门户访问统计")
    public MyJsonBean updateAccessStats(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("用户名称") @RequestParam String userName,
            @ApiParam("访问IP") @RequestParam String accessIp,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.updateAccessStats(portalId, userId, userName, accessIp, tenantId);
            return success ? MyJsonBean.success("更新访问统计成功") : MyJsonBean.error("更新访问统计失败");
        } catch (Exception e) {
            log.error("更新门户访问统计失败", e);
            return MyJsonBean.error("更新门户访问统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/{portalId}/healthCheck")
    @ApiOperation("门户健康检查")
    public MyJsonBean performHealthCheck(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> result = servicePortalService.performHealthCheck(portalId, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("门户健康检查失败", e);
            return MyJsonBean.error("门户健康检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/{portalId}/monitor")
    @ApiOperation("门户性能监控")
    public MyJsonBean monitorPortalPerformance(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> result = servicePortalService.monitorPortalPerformance(portalId, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("门户性能监控失败", e);
            return MyJsonBean.error("门户性能监控失败: " + e.getMessage());
        }
    }

    @PostMapping("/{portalId}/backup")
    @ApiOperation("门户备份")
    public MyJsonBean backupPortal(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("备份类型") @RequestParam String backupType,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.backupPortal(portalId, backupType, tenantId);
            return success ? MyJsonBean.success("备份成功") : MyJsonBean.error("备份失败");
        } catch (Exception e) {
            log.error("门户备份失败", e);
            return MyJsonBean.error("门户备份失败: " + e.getMessage());
        }
    }

    @PostMapping("/{portalId}/restore")
    @ApiOperation("门户恢复")
    public MyJsonBean restorePortal(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("备份ID") @RequestParam String backupId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.restorePortal(portalId, backupId, tenantId);
            return success ? MyJsonBean.success("恢复成功") : MyJsonBean.error("恢复失败");
        } catch (Exception e) {
            log.error("门户恢复失败", e);
            return MyJsonBean.error("门户恢复失败: " + e.getMessage());
        }
    }

    @PostMapping("/{portalId}/optimize")
    @ApiOperation("门户优化")
    public MyJsonBean optimizePortal(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("优化配置") @RequestBody Map<String, Object> optimizationConfig,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.optimizePortal(portalId, optimizationConfig, tenantId);
            return success ? MyJsonBean.success("优化成功") : MyJsonBean.error("优化失败");
        } catch (Exception e) {
            log.error("门户优化失败", e);
            return MyJsonBean.error("门户优化失败: " + e.getMessage());
        }
    }

    @GetMapping("/{portalId}/securityScan")
    @ApiOperation("门户安全扫描")
    public MyJsonBean performSecurityScan(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> result = servicePortalService.performSecurityScan(portalId, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("门户安全扫描失败", e);
            return MyJsonBean.error("门户安全扫描失败: " + e.getMessage());
        }
    }

    @GetMapping("/{portalId}/integrationTest")
    @ApiOperation("门户集成测试")
    public MyJsonBean performIntegrationTest(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> result = servicePortalService.performIntegrationTest(portalId, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("门户集成测试失败", e);
            return MyJsonBean.error("门户集成测试失败: " + e.getMessage());
        }
    }

    @PostMapping("/{portalId}/feedback")
    @ApiOperation("收集用户反馈")
    public MyJsonBean collectUserFeedback(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("反馈信息") @RequestBody Map<String, Object> feedback,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean success = servicePortalService.collectUserFeedback(portalId, feedback, tenantId);
            return success ? MyJsonBean.success("收集反馈成功") : MyJsonBean.error("收集反馈失败");
        } catch (Exception e) {
            log.error("收集用户反馈失败", e);
            return MyJsonBean.error("收集用户反馈失败: " + e.getMessage());
        }
    }

    @GetMapping("/{portalId}/usage/analyze")
    @ApiOperation("门户使用分析")
    public MyJsonBean analyzePortalUsage(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> result = servicePortalService.analyzePortalUsage(portalId, startTime, endTime, tenantId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("门户使用分析失败", e);
            return MyJsonBean.error("门户使用分析失败: " + e.getMessage());
        }
    }

    @GetMapping("/recommend")
    @ApiOperation("门户推荐")
    public MyJsonBean recommendPortals(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("推荐数量") @RequestParam(defaultValue = "10") Integer limit,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.recommendPortals(userId, limit, tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("门户推荐失败", e);
            return MyJsonBean.error("门户推荐失败: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    @ApiOperation("门户搜索")
    public MyJsonBean searchPortals(
            @ApiParam("搜索关键词") @RequestParam String keyword,
            @ApiParam("过滤条件") @RequestParam Map<String, Object> filters,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.searchPortals(keyword, filters, tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("门户搜索失败", e);
            return MyJsonBean.error("门户搜索失败: " + e.getMessage());
        }
    }

    @GetMapping("/type/{portalType}")
    @ApiOperation("根据门户类型查询")
    public MyJsonBean getPortalsByType(
            @ApiParam("门户类型") @PathVariable String portalType,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getPortalsByType(portalType, tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("根据门户类型查询失败", e);
            return MyJsonBean.error("根据门户类型查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/status/{portalStatus}")
    @ApiOperation("根据门户状态查询")
    public MyJsonBean getPortalsByStatus(
            @ApiParam("门户状态") @PathVariable String portalStatus,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getPortalsByStatus(portalStatus, tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("根据门户状态查询失败", e);
            return MyJsonBean.error("根据门户状态查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/owner/{ownerId}")
    @ApiOperation("根据所有者查询")
    public MyJsonBean getPortalsByOwner(
            @ApiParam("所有者ID") @PathVariable Long ownerId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getPortalsByOwner(ownerId, tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("根据所有者查询失败", e);
            return MyJsonBean.error("根据所有者查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/admin/{adminId}")
    @ApiOperation("根据管理员查询")
    public MyJsonBean getPortalsByAdmin(
            @ApiParam("管理员ID") @PathVariable Long adminId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getPortalsByAdmin(adminId, tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("根据管理员查询失败", e);
            return MyJsonBean.error("根据管理员查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/department/{departmentId}")
    @ApiOperation("根据部门查询")
    public MyJsonBean getPortalsByDepartment(
            @ApiParam("部门ID") @PathVariable Long departmentId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getPortalsByDepartment(departmentId, tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("根据部门查询失败", e);
            return MyJsonBean.error("根据部门查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/company/{companyId}")
    @ApiOperation("根据公司查询")
    public MyJsonBean getPortalsByCompany(
            @ApiParam("公司ID") @PathVariable Long companyId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getPortalsByCompany(companyId, tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("根据公司查询失败", e);
            return MyJsonBean.error("根据公司查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/active")
    @ApiOperation("查询活跃门户")
    public MyJsonBean getActivePortals(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getActivePortals(tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("查询活跃门户失败", e);
            return MyJsonBean.error("查询活跃门户失败: " + e.getMessage());
        }
    }

    @GetMapping("/inactive")
    @ApiOperation("查询非活跃门户")
    public MyJsonBean getInactivePortals(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getInactivePortals(tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("查询非活跃门户失败", e);
            return MyJsonBean.error("查询非活跃门户失败: " + e.getMessage());
        }
    }

    @GetMapping("/maintenance")
    @ApiOperation("查询维护中门户")
    public MyJsonBean getMaintenancePortals(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getMaintenancePortals(tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("查询维护中门户失败", e);
            return MyJsonBean.error("查询维护中门户失败: " + e.getMessage());
        }
    }

    @GetMapping("/archived")
    @ApiOperation("查询已归档门户")
    public MyJsonBean getArchivedPortals(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getArchivedPortals(tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("查询已归档门户失败", e);
            return MyJsonBean.error("查询已归档门户失败: " + e.getMessage());
        }
    }

    @GetMapping("/default")
    @ApiOperation("查询默认门户")
    public MyJsonBean getDefaultPortals(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getDefaultPortals(tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("查询默认门户失败", e);
            return MyJsonBean.error("查询默认门户失败: " + e.getMessage());
        }
    }

    @GetMapping("/public")
    @ApiOperation("查询公开门户")
    public MyJsonBean getPublicPortals(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getPublicPortals(tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("查询公开门户失败", e);
            return MyJsonBean.error("查询公开门户失败: " + e.getMessage());
        }
    }

    @GetMapping("/customizable")
    @ApiOperation("查询可定制门户")
    public MyJsonBean getCustomizablePortals(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getCustomizablePortals(tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("查询可定制门户失败", e);
            return MyJsonBean.error("查询可定制门户失败: " + e.getMessage());
        }
    }

    @GetMapping("/highTraffic")
    @ApiOperation("查询高访问量门户")
    public MyJsonBean getHighTrafficPortals(
            @ApiParam("数量限制") @RequestParam(defaultValue = "10") Integer limit,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getHighTrafficPortals(limit, tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("查询高访问量门户失败", e);
            return MyJsonBean.error("查询高访问量门户失败: " + e.getMessage());
        }
    }

    @GetMapping("/recentlyAccessed")
    @ApiOperation("查询最近访问门户")
    public MyJsonBean getRecentlyAccessedPortals(
            @ApiParam("数量限制") @RequestParam(defaultValue = "10") Integer limit,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getRecentlyAccessedPortals(limit, tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("查询最近访问门户失败", e);
            return MyJsonBean.error("查询最近访问门户失败: " + e.getMessage());
        }
    }

    @GetMapping("/needMaintenance")
    @ApiOperation("查询需要维护的门户")
    public MyJsonBean getPortalsNeedMaintenance(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getPortalsNeedMaintenance(tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("查询需要维护的门户失败", e);
            return MyJsonBean.error("查询需要维护的门户失败: " + e.getMessage());
        }
    }

    @GetMapping("/expired")
    @ApiOperation("查询过期门户")
    public MyJsonBean getExpiredPortals(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getExpiredPortals(tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("查询过期门户失败", e);
            return MyJsonBean.error("查询过期门户失败: " + e.getMessage());
        }
    }

    @GetMapping("/lowUsage")
    @ApiOperation("查询低使用率门户")
    public MyJsonBean getLowUsagePortals(
            @ApiParam("使用率阈值") @RequestParam Long threshold,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<SsServicePortal> portals = servicePortalService.getLowUsagePortals(threshold, tenantId);
            return MyJsonBean.success(portals);
        } catch (Exception e) {
            log.error("查询低使用率门户失败", e);
            return MyJsonBean.error("查询低使用率门户失败: " + e.getMessage());
        }
    }

    @GetMapping("/check/code")
    @ApiOperation("检查门户编码是否存在")
    public MyJsonBean checkPortalCodeExists(
            @ApiParam("门户编码") @RequestParam String portalCode,
            @ApiParam("门户ID") @RequestParam(required = false) Long portalId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean exists = servicePortalService.checkPortalCodeExists(portalCode, portalId, tenantId);
            return MyJsonBean.success(exists);
        } catch (Exception e) {
            log.error("检查门户编码是否存在失败", e);
            return MyJsonBean.error("检查门户编码是否存在失败: " + e.getMessage());
        }
    }

    @GetMapping("/check/name")
    @ApiOperation("检查门户名称是否存在")
    public MyJsonBean checkPortalNameExists(
            @ApiParam("门户名称") @RequestParam String portalName,
            @ApiParam("门户ID") @RequestParam(required = false) Long portalId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            boolean exists = servicePortalService.checkPortalNameExists(portalName, portalId, tenantId);
            return MyJsonBean.success(exists);
        } catch (Exception e) {
            log.error("检查门户名称是否存在失败", e);
            return MyJsonBean.error("检查门户名称是否存在失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("查询统计信息")
    public MyJsonBean getStatistics(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            Map<String, Object> statistics = servicePortalService.getStatistics(startTime, endTime, tenantId);
            return MyJsonBean.success(statistics);
        } catch (Exception e) {
            log.error("查询统计信息失败", e);
            return MyJsonBean.error("查询统计信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/typeDistribution")
    @ApiOperation("查询门户类型分布")
    public MyJsonBean getPortalTypeDistribution(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> distribution = servicePortalService.getPortalTypeDistribution(tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("查询门户类型分布失败", e);
            return MyJsonBean.error("查询门户类型分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/statusDistribution")
    @ApiOperation("查询门户状态分布")
    public MyJsonBean getPortalStatusDistribution(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> distribution = servicePortalService.getPortalStatusDistribution(tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("查询门户状态分布失败", e);
            return MyJsonBean.error("查询门户状态分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/categoryDistribution")
    @ApiOperation("查询门户分类分布")
    public MyJsonBean getPortalCategoryDistribution(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> distribution = servicePortalService.getPortalCategoryDistribution(tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("查询门户分类分布失败", e);
            return MyJsonBean.error("查询门户分类分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/accessRanking")
    @ApiOperation("查询门户访问量排行")
    public MyJsonBean getPortalAccessRanking(
            @ApiParam("数量限制") @RequestParam(defaultValue = "10") Integer limit,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> ranking = servicePortalService.getPortalAccessRanking(limit, tenantId);
            return MyJsonBean.success(ranking);
        } catch (Exception e) {
            log.error("查询门户访问量排行失败", e);
            return MyJsonBean.error("查询门户访问量排行失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/usageTrend")
    @ApiOperation("查询门户使用趋势")
    public MyJsonBean getPortalUsageTrend(
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> trend = servicePortalService.getPortalUsageTrend(startTime, endTime, tenantId);
            return MyJsonBean.success(trend);
        } catch (Exception e) {
            log.error("查询门户使用趋势失败", e);
            return MyJsonBean.error("查询门户使用趋势失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/performanceMetrics")
    @ApiOperation("查询门户性能指标")
    public MyJsonBean getPortalPerformanceMetrics(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> metrics = servicePortalService.getPortalPerformanceMetrics(tenantId);
            return MyJsonBean.success(metrics);
        } catch (Exception e) {
            log.error("查询门户性能指标失败", e);
            return MyJsonBean.error("查询门户性能指标失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/userFeedback")
    @ApiOperation("查询门户用户反馈")
    public MyJsonBean getPortalUserFeedback(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> feedback = servicePortalService.getPortalUserFeedback(tenantId);
            return MyJsonBean.success(feedback);
        } catch (Exception e) {
            log.error("查询门户用户反馈失败", e);
            return MyJsonBean.error("查询门户用户反馈失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/securityEvents")
    @ApiOperation("查询门户安全事件")
    public MyJsonBean getPortalSecurityEvents(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> events = servicePortalService.getPortalSecurityEvents(tenantId);
            return MyJsonBean.success(events);
        } catch (Exception e) {
            log.error("查询门户安全事件失败", e);
            return MyJsonBean.error("查询门户安全事件失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/integrationStatus")
    @ApiOperation("查询门户集成状态")
    public MyJsonBean getPortalIntegrationStatus(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> status = servicePortalService.getPortalIntegrationStatus(tenantId);
            return MyJsonBean.success(status);
        } catch (Exception e) {
            log.error("查询门户集成状态失败", e);
            return MyJsonBean.error("查询门户集成状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/backupStatus")
    @ApiOperation("查询门户备份状态")
    public MyJsonBean getPortalBackupStatus(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> status = servicePortalService.getPortalBackupStatus(tenantId);
            return MyJsonBean.success(status);
        } catch (Exception e) {
            log.error("查询门户备份状态失败", e);
            return MyJsonBean.error("查询门户备份状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/monitoringStatus")
    @ApiOperation("查询门户监控状态")
    public MyJsonBean getPortalMonitoringStatus(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> status = servicePortalService.getPortalMonitoringStatus(tenantId);
            return MyJsonBean.success(status);
        } catch (Exception e) {
            log.error("查询门户监控状态失败", e);
            return MyJsonBean.error("查询门户监控状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/{portalId}/accessLogs")
    @ApiOperation("查询门户访问日志")
    public MyJsonBean getPortalAccessLogs(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("开始时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> logs = servicePortalService.getPortalAccessLogs(portalId, startTime, endTime, tenantId);
            return MyJsonBean.success(logs);
        } catch (Exception e) {
            log.error("查询门户访问日志失败", e);
            return MyJsonBean.error("查询门户访问日志失败: " + e.getMessage());
        }
    }

    @GetMapping("/{portalId}/configHistory")
    @ApiOperation("查询门户配置历史")
    public MyJsonBean getPortalConfigHistory(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> history = servicePortalService.getPortalConfigHistory(portalId, tenantId);
            return MyJsonBean.success(history);
        } catch (Exception e) {
            log.error("查询门户配置历史失败", e);
            return MyJsonBean.error("查询门户配置历史失败: " + e.getMessage());
        }
    }

    @GetMapping("/{portalId}/maintenanceRecords")
    @ApiOperation("查询门户维护记录")
    public MyJsonBean getPortalMaintenanceRecords(
            @ApiParam("门户ID") @PathVariable Long portalId,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> records = servicePortalService.getPortalMaintenanceRecords(portalId, tenantId);
            return MyJsonBean.success(records);
        } catch (Exception e) {
            log.error("查询门户维护记录失败", e);
            return MyJsonBean.error("查询门户维护记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/alerts")
    @ApiOperation("查询门户告警信息")
    public MyJsonBean getPortalAlerts(
            @ApiParam("告警类型") @RequestParam String alertType,
            @ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> alerts = servicePortalService.getPortalAlerts(alertType, tenantId);
            return MyJsonBean.success(alerts);
        } catch (Exception e) {
            log.error("查询门户告警信息失败", e);
            return MyJsonBean.error("查询门户告警信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/pendingItems")
    @ApiOperation("查询门户待处理事项")
    public MyJsonBean getPortalPendingItems(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> items = servicePortalService.getPortalPendingItems(tenantId);
            return MyJsonBean.success(items);
        } catch (Exception e) {
            log.error("查询门户待处理事项失败", e);
            return MyJsonBean.error("查询门户待处理事项失败: " + e.getMessage());
        }
    }

    @GetMapping("/healthCheckResults")
    @ApiOperation("查询门户健康检查结果")
    public MyJsonBean getPortalHealthCheckResults(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> results = servicePortalService.getPortalHealthCheckResults(tenantId);
            return MyJsonBean.success(results);
        } catch (Exception e) {
            log.error("查询门户健康检查结果失败", e);
            return MyJsonBean.error("查询门户健康检查结果失败: " + e.getMessage());
        }
    }

    @GetMapping("/resourceUsage")
    @ApiOperation("查询门户资源使用情况")
    public MyJsonBean getPortalResourceUsage(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> usage = servicePortalService.getPortalResourceUsage(tenantId);
            return MyJsonBean.success(usage);
        } catch (Exception e) {
            log.error("查询门户资源使用情况失败", e);
            return MyJsonBean.error("查询门户资源使用情况失败: " + e.getMessage());
        }
    }

    @GetMapping("/optimizationSuggestions")
    @ApiOperation("查询门户优化建议")
    public MyJsonBean getPortalOptimizationSuggestions(@ApiParam("租户ID") @RequestParam Long tenantId) {
        try {
            List<Map<String, Object>> suggestions = servicePortalService.getPortalOptimizationSuggestions(tenantId);
            return MyJsonBean.success(suggestions);
        } catch (Exception e) {
            log.error("查询门户优化建议失败", e);
            return MyJsonBean.error("查询门户优化建议失败: " + e.getMessage());
        }
    }
}
