package com.huabo.fxgl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.TblWarningConfig;
import com.huabo.fxgl.service.IWarningConfigService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 预警配置管理控制器
 *
 * @author 华博云
 * @date 2025-09-30
 */
@Slf4j
@RestController
@RequestMapping("/api/warning/config")
@Tag(name="预警配置管理",description="预警配置管理")
public class WarningConfigController {

    @Autowired
    private IWarningConfigService warningConfigService;

    @Autowired
    private UserProvider userProvider;

    @PostMapping("/list")
    @Operation(summary = "分页查询预警配置列表")
    public String getWarningConfigList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNum", description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(name = "pageSize", description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(name = "configType", description = "配置类型") @RequestParam(required = false) String configType,
            @Parameter(name = "isEnabled", description = "是否启用") @RequestParam(required = false) String isEnabled) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("查询预警配置列表，参数: pageNum={}, pageSize={}, configType={}, isEnabled={}", 
                    pageNum, pageSize, configType, isEnabled);

            JsonBean result = warningConfigService.getWarningConfigList(pageNum, pageSize, configType, isEnabled);
            return result.toString();
        } catch (Exception e) {
            log.error("查询预警配置列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    @Operation(summary = "保存预警配置")
    public String saveWarningConfig(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody TblWarningConfig warningConfig) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            String currentUser = staff.getRealname();
            JsonBean result = warningConfigService.saveWarningConfig(warningConfig, currentUser);
            return result.toString();
        } catch (Exception e) {
            log.error("保存预警配置失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @Operation(summary = "更新预警配置")
    public String updateWarningConfig(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody TblWarningConfig warningConfig) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            String currentUser = staff.getRealname();
            JsonBean result = warningConfigService.updateWarningConfig(warningConfig, currentUser);
            return result.toString();
        } catch (Exception e) {
            log.error("更新预警配置失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete/{configId}")
    @Operation(summary = "删除预警配置")
    public String deleteWarningConfig(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @PathVariable String configId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = warningConfigService.deleteWarningConfig(configId);
            return result.toString();
        } catch (Exception e) {
            log.error("删除预警配置失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/detail/{configId}")
    @Operation(summary = "获取预警配置详情")
    public String getWarningConfigDetail(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @PathVariable String configId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            TblWarningConfig config = warningConfigService.getById(configId);
            if (config == null) {
                return JsonBean.error("配置不存在");
            }

            return JsonBean.success("查询成功", config);
        } catch (Exception e) {
            log.error("获取预警配置详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateStatus")
    @Operation(summary = "更新配置状态")
    public String updateConfigStatus(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "configId", description = "配置ID") @RequestParam String configId,
            @Parameter(name = "isEnabled", description = "是否启用") @RequestParam String isEnabled) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            String currentUser = staff.getRealname();
            JsonBean result = warningConfigService.updateConfigStatus(configId, isEnabled, currentUser);
            return result.toString();
        } catch (Exception e) {
            log.error("更新配置状态失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchUpdateStatus")
    @Operation(summary = "批量更新配置状态")
    public String batchUpdateConfigStatus(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody List<String> configIds,
            @Parameter(name = "isEnabled", description = "是否启用") @RequestParam String isEnabled) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            String currentUser = staff.getRealname();
            JsonBean result = warningConfigService.batchUpdateConfigStatus(configIds, isEnabled, currentUser);
            return result.toString();
        } catch (Exception e) {
            log.error("批量更新配置状态失败", e);
            return JsonBean.error("批量更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/getByType")
    @Operation(summary = "根据类型获取配置")
    public String getConfigByType(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "configType", description = "配置类型") @RequestParam String configType) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            List<TblWarningConfig> configs = warningConfigService.getConfigByType(configType);
            return JsonBean.success("查询成功", configs);
        } catch (Exception e) {
            log.error("根据类型获取配置失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    @Operation(summary = "验证配置值格式")
    public String validateConfigValue(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "configType", description = "配置类型") @RequestParam String configType,
            @Parameter(name = "configValue", description = "配置值") @RequestParam String configValue) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = warningConfigService.validateConfigValue(configType, configValue);
            return result.toString();
        } catch (Exception e) {
            log.error("验证配置值失败", e);
            return JsonBean.error("验证失败: " + e.getMessage());
        }
    }

    @PostMapping("/template/{configType}")
    @Operation(summary = "获取配置模板")
    public String getConfigTemplate(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @PathVariable String configType) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = warningConfigService.getConfigTemplate(configType);
            return result.toString();
        } catch (Exception e) {
            log.error("获取配置模板失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/export")
    @Operation(summary = "导出配置")
    public String exportConfigs(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "configType", description = "配置类型") @RequestParam(required = false) String configType) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = warningConfigService.exportConfigs(configType);
            return result.toString();
        } catch (Exception e) {
            log.error("导出配置失败", e);
            return JsonBean.error("导出失败: " + e.getMessage());
        }
    }

    @PostMapping("/import")
    @Operation(summary = "导入配置")
    public String importConfigs(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "configData", description = "配置数据") @RequestParam String configData) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            String currentUser = staff.getRealname();
            JsonBean result = warningConfigService.importConfigs(configData, currentUser);
            return result.toString();
        } catch (Exception e) {
            log.error("导入配置失败", e);
            return JsonBean.error("导入失败: " + e.getMessage());
        }
    }
}
