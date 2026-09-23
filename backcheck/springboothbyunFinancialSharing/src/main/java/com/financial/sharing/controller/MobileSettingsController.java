package com.financial.sharing.controller;

import com.financial.sharing.service.TblMobileSettingService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.vo.param.TblMobileSettingQueryParam;
import com.financial.sharing.vo.param.TblMobileSettingSaveParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 移动设置配置控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "移动设置配置")
@RestController
@RequestMapping("/mobile-settings")
@CrossOrigin
public class MobileSettingsController {

    @Autowired
    private TblMobileSettingService mobileSettingService;

    @ApiOperation("查询移动设置配置列表")
    @GetMapping
    public MyJsonBean getMobileSettingsList(TblMobileSettingQueryParam param) {
        try {
            log.info("查询移动设置列表，参数：{}", param);
            return mobileSettingService.getList(param);
        } catch (Exception e) {
            log.error("查询移动设置列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存移动设置配置")
    @PostMapping
    public MyJsonBean saveMobileSettings(@RequestBody TblMobileSettingSaveParam param) {
        try {
            log.info("保存移动设置，参数：{}", param);
            return mobileSettingService.saveOrUpdate(param);
        } catch (Exception e) {
            log.error("保存移动设置失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除移动设置配置")
    @DeleteMapping("/{settingId}")
    public MyJsonBean deleteMobileSettings(@PathVariable String settingId) {
        try {
            log.info("删除移动设置，settingId={}", settingId);
            return mobileSettingService.delete(settingId);
        } catch (Exception e) {
            log.error("删除移动设置失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取移动设置配置详情")
    @GetMapping("/{settingId}")
    public MyJsonBean getMobileSettingsDetail(@PathVariable String settingId) {
        try {
            log.info("获取移动设置详情，settingId={}", settingId);
            return mobileSettingService.getById(settingId);
        } catch (Exception e) {
            log.error("获取移动设置详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取移动设置配置分组")
    @GetMapping("/groups")
    public MyJsonBean getMobileSettingsGroups() {
        try {
            log.info("获取移动设置分组列表");
            return mobileSettingService.getGroups();
        } catch (Exception e) {
            log.error("获取移动设置分组列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取移动设置配置按分组")
    @GetMapping("/by-group/{groupId}")
    public MyJsonBean getMobileSettingsByGroup(@PathVariable String groupId) {
        try {
            log.info("按分组查询移动设置，groupId={}", groupId);
            // 这里可以通过查询参数实现分组过滤
            TblMobileSettingQueryParam param = new TblMobileSettingQueryParam();
            param.setPageNo(1);
            param.setPageSize(100);
            // 注意：需要在QueryParam中添加groupId字段支持
            return mobileSettingService.getList(param);
        } catch (Exception e) {
            log.error("按分组查询移动设置失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取移动设置配置模板")
    @GetMapping("/templates")
    public MyJsonBean getMobileSettingsTemplates(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) String settingId) {
        try {
            log.info("获取移动设置模板列表，page: {}, size: {}, settingId: {}", page, size, settingId);
            TblMobileSettingQueryParam param = new TblMobileSettingQueryParam();
            param.setPageNo(page);
            param.setPageSize(size);
            return mobileSettingService.getTemplates(param);
        } catch (Exception e) {
            log.error("获取移动设置模板列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存移动设置配置模板")
    @PostMapping("/templates")
    public MyJsonBean saveMobileSettingsTemplate(@RequestBody com.financial.sharing.vo.param.TblMobileSettingTemplateSaveParam param) {
        try {
            log.info("保存移动设置模板，参数：{}", param);
            return mobileSettingService.saveTemplate(param);
        } catch (Exception e) {
            log.error("保存移动设置模板失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除移动设置配置模板")
    @DeleteMapping("/templates/{templateId}")
    public MyJsonBean deleteMobileSettingsTemplate(@PathVariable String templateId) {
        try {
            log.info("删除移动设置模板，templateId={}", templateId);
            return mobileSettingService.deleteTemplate(templateId);
        } catch (Exception e) {
            log.error("删除移动设置模板失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("应用移动设置配置模板")
    @PostMapping("/apply-template")
    public MyJsonBean applyMobileSettingsTemplate(@RequestBody TblMobileSettingSaveParam param) {
        try {
            log.info("应用移动设置模板，参数：{}", param);
            // 应用模板逻辑：根据模板创建新的设置
            return mobileSettingService.saveOrUpdate(param);
        } catch (Exception e) {
            log.error("应用移动设置模板失败", e);
            return MyJsonBean.errorData("应用模板失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除移动设置配置")
    @DeleteMapping("/batch")
    public MyJsonBean batchDeleteMobileSettings(@RequestBody List<String> settingIds) {
        try {
            log.info("批量删除移动设置，settingIds={}", settingIds);
            int successCount = 0;
            for (String settingId : settingIds) {
                MyJsonBean result = mobileSettingService.delete(settingId);
                if (result.getCode() == 200) {
                    successCount++;
                }
            }
            return MyJsonBean.successData("批量删除成功，成功删除" + successCount + "条记录");
        } catch (Exception e) {
            log.error("批量删除移动设置失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("启用/禁用移动设置配置")
    @PutMapping("/{settingId}/status")
    public MyJsonBean updateMobileSettingsStatus(@PathVariable String settingId,
                                                @RequestParam Integer isEnabled) {
        try {
            log.info("更新移动设置状态，settingId={}, isEnabled={}", settingId, isEnabled);
            return mobileSettingService.updateStatus(settingId, isEnabled);
        } catch (Exception e) {
            log.error("更新移动设置状态失败", e);
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("重置移动设置配置为默认值")
    @PutMapping("/{settingId}/reset")
    public MyJsonBean resetMobileSettings(@PathVariable String settingId) {
        try {
            log.info("重置移动设置，settingId={}", settingId);
            return mobileSettingService.resetToDefault(settingId);
        } catch (Exception e) {
            log.error("重置移动设置失败", e);
            return MyJsonBean.errorData("重置失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量重置移动设置配置")
    @PutMapping("/batch-reset")
    public MyJsonBean batchResetMobileSettings(@RequestBody List<String> settingIds) {
        try {
            log.info("批量重置移动设置，settingIds={}", settingIds);
            int successCount = 0;
            for (String settingId : settingIds) {
                MyJsonBean result = mobileSettingService.resetToDefault(settingId);
                if (result.getCode() == 200) {
                    successCount++;
                }
            }
            return MyJsonBean.successData("批量重置成功，成功重置" + successCount + "条记录");
        } catch (Exception e) {
            log.error("批量重置移动设置失败", e);
            return MyJsonBean.errorData("批量重置失败: " + e.getMessage());
        }
    }

    @ApiOperation("同步移动设置配置到移动端")
    @PostMapping("/sync")
    public MyJsonBean syncMobileSettings(@RequestBody java.util.Map<String, Object> syncData) {
        try {
            log.info("同步移动设置到移动端，syncData={}", syncData);

            // 获取settingIds数组
            @SuppressWarnings("unchecked")
            List<String> settingIds = (List<String>) syncData.get("settingIds");

            if (settingIds == null || settingIds.isEmpty()) {
                return MyJsonBean.errorData("设置ID列表不能为空");
            }

            return mobileSettingService.syncToMobile(settingIds);
        } catch (Exception e) {
            log.error("同步移动设置失败", e);
            return MyJsonBean.errorData("同步失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取移动设置配置使用统计")
    @GetMapping("/{settingId}/usage-statistics")
    public MyJsonBean getMobileSettingsUsageStatistics(@PathVariable String settingId) {
        try {
            log.info("获取移动设置使用统计，settingId={}", settingId);
            return mobileSettingService.getStatistics();
        } catch (Exception e) {
            log.error("获取移动设置使用统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("测试移动设置配置")
    @PostMapping("/{settingId}/test")
    public MyJsonBean testMobileSettings(@PathVariable String settingId, @RequestBody java.util.Map<String, Object> testData) {
        try {
            log.info("测试移动设置，settingId={}，testData={}", settingId, testData);
            return mobileSettingService.test(settingId, testData);
        } catch (Exception e) {
            log.error("测试移动设置失败", e);
            return MyJsonBean.errorData("测试失败: " + e.getMessage());
        }
    }
}
