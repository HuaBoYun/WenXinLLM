package com.financial.sharing.budgetControl.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetControl.dto.WarningConfigQueryParam;
import com.financial.sharing.budgetControl.entity.TblWarningConfig;
import com.financial.sharing.budgetControl.service.WarningConfigService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 预警配置Controller (OpenAPI)
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Api(tags = "预警配置API")
@RestController
@RequestMapping("/financialSharing/warningConfig/api")
public class WarningConfigController {

    @Autowired
    private WarningConfigService warningConfigService;

    @ApiOperation(value = "分页查询预警配置")
    @PostMapping("/queryPage")
    public MyJsonBean queryPage(@RequestBody WarningConfigQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return warningConfigService.queryPage(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "根据ID查询预警配置")
    @PostMapping("/queryById")
    public MyJsonBean queryById(@RequestBody Map<String, String> params) {
        try {
            String configId = params.get("configId");
            String orgId = UserUtils.getOrgId();
            return warningConfigService.queryById(configId, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "保存预警配置")
    @PostMapping("/saveConfig")
    public MyJsonBean saveConfig(@RequestBody TblWarningConfig config) {
        try {
            config.setOrgId(UserUtils.getOrgId());
            if (config.getConfigId() == null) {
                config.setCreateUser(UserUtils.getUserId());
            } else {
                config.setUpdateUser(UserUtils.getUserId());
            }
            return warningConfigService.saveConfig(config);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "删除预警配置")
    @PostMapping("/deleteConfig")
    public MyJsonBean deleteConfig(@RequestBody Map<String, String> params) {
        try {
            String configId = params.get("configId");
            String orgId = UserUtils.getOrgId();
            return warningConfigService.deleteConfig(configId, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "启用/禁用预警配置")
    @PostMapping("/toggleEnabled")
    public MyJsonBean toggleEnabled(@RequestBody Map<String, String> params) {
        try {
            String configId = params.get("configId");
            String isEnabled = params.get("isEnabled");
            String orgId = UserUtils.getOrgId();
            return warningConfigService.toggleEnabled(configId, isEnabled, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "测试预警配置")
    @PostMapping("/testConfig")
    public MyJsonBean testConfig(@RequestBody Map<String, String> params) {
        try {
            String configId = params.get("configId");
            String orgId = UserUtils.getOrgId();
            return warningConfigService.testConfig(configId, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("测试失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "发送预警消息")
    @PostMapping("/sendWarningMessage")
    public MyJsonBean sendWarningMessage(@RequestBody Map<String, String> params) {
        try {
            String configId = params.get("configId");
            String message = params.get("message");
            String orgId = UserUtils.getOrgId();
            return warningConfigService.sendWarningMessage(configId, message, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("发送失败：" + e.getMessage());
        }
    }
}

