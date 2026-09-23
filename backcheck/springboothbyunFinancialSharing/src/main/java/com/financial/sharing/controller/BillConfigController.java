package com.financial.sharing.controller;

import com.financial.sharing.dto.TblBillConfigQueryParam;
import com.financial.sharing.dto.TblBillConfigSaveParam;
import com.financial.sharing.service.TblBillConfigService;
import com.financial.sharing.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 账单配置管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "账单配置管理")
@RestController
@RequestMapping("/bill/configs")
@CrossOrigin
public class BillConfigController {

    @Autowired
    private TblBillConfigService billConfigService;

    @ApiOperation("查询账单配置列表")
    @GetMapping
    public MyJsonBean getBillConfigList(@RequestParam(required = false) String configName,
                                      @RequestParam(required = false) String configCode,
                                      @RequestParam(required = false) String billType,
                                      @RequestParam(required = false) Integer isEnabled,
                                      @RequestParam(defaultValue = "1") Integer pageNo,
                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            TblBillConfigQueryParam param = new TblBillConfigQueryParam();
            param.setConfigName(configName);
            param.setConfigCode(configCode);
            param.setBillType(billType);
            param.setIsEnabled(isEnabled);
            param.setPageNo(pageNo);
            param.setPageSize(pageSize);

            return billConfigService.getList(param);
        } catch (Exception e) {
            log.error("查询账单配置列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存账单配置")
    @PostMapping
    public MyJsonBean saveBillConfig(@RequestBody TblBillConfigSaveParam param) {
        try {
            return billConfigService.saveOrUpdate(param);
        } catch (Exception e) {
            log.error("保存账单配置失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除账单配置")
    @DeleteMapping("/{configId}")
    public MyJsonBean deleteBillConfig(@PathVariable String configId) {
        try {
            return billConfigService.delete(configId);
        } catch (Exception e) {
            log.error("删除账单配置失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取账单配置详情")
    @GetMapping("/{configId}")
    public MyJsonBean getBillConfigDetail(@PathVariable String configId) {
        try {
            return billConfigService.getById(configId);
        } catch (Exception e) {
            log.error("获取账单配置详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取账单配置字段映射")
    @GetMapping("/{configId}/field-mapping")
    public MyJsonBean getBillConfigFieldMapping(@PathVariable String configId) {
        try {
            return billConfigService.getFieldMappings(configId);
        } catch (Exception e) {
            log.error("获取账单配置字段映射失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存账单配置字段映射")
    @PostMapping("/{configId}/field-mapping")
    public MyJsonBean saveBillConfigFieldMapping(@PathVariable String configId,
                                                @RequestBody java.util.List<com.financial.sharing.entity.TblBillFieldMapping> mappings) {
        try {
            return billConfigService.saveFieldMappings(configId, mappings);
        } catch (Exception e) {
            log.error("保存账单配置字段映射失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取账单配置稽核规则")
    @GetMapping("/{configId}/audit-rules")
    public MyJsonBean getBillConfigAuditRules(@PathVariable String configId) {
        try {
            return billConfigService.getAuditRules(configId);
        } catch (Exception e) {
            log.error("获取账单配置稽核规则失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存账单配置稽核规则")
    @PostMapping("/{configId}/audit-rules")
    public MyJsonBean saveBillConfigAuditRules(@PathVariable String configId,
                                              @RequestBody java.util.List<com.financial.sharing.entity.TblBillAuditRule> rules) {
        try {
            return billConfigService.saveAuditRules(configId, rules);
        } catch (Exception e) {
            log.error("保存账单配置稽核规则失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("测试账单配置")
    @PostMapping("/{configId}/test")
    public MyJsonBean testBillConfig(@PathVariable String configId,
                                   @RequestBody Map<String, Object> testData) {
        try {
            return billConfigService.testConfig(configId, testData);
        } catch (Exception e) {
            log.error("测试账单配置失败", e);
            return MyJsonBean.errorData("测试失败: " + e.getMessage());
        }
    }

    @ApiOperation("启用/禁用账单配置")
    @PutMapping("/{configId}/status")
    public MyJsonBean updateBillConfigStatus(@PathVariable String configId,
                                           @RequestParam Integer isEnabled) {
        try {
            return billConfigService.updateStatus(configId, isEnabled);
        } catch (Exception e) {
            log.error("更新账单配置状态失败", e);
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取账单识别日志")
    @GetMapping("/{configId}/recognition-logs")
    public MyJsonBean getBillRecognitionLogs(@PathVariable String configId) {
        try {
            return billConfigService.getRecognitionLogs(configId);
        } catch (Exception e) {
            log.error("获取账单识别日志失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}
