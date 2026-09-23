package com.management.accountant.controller.mobile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.mobile.MobileOfflineData;
import com.management.accountant.service.mobile.MobileOfflineDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 移动离线数据控制器
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Slf4j
@RestController
@RequestMapping("/api/mobile/offline-data")
@Api(tags = "移动离线数据管理")
public class MobileOfflineDataController {

    @Autowired
    private MobileOfflineDataService mobileOfflineDataService;

    // ==================== 基础CRUD操作 ====================

    @PostMapping("/create")
    @ApiOperation("创建离线数据")
    public Map<String, Object> createOfflineData(@RequestBody MobileOfflineData offlineData) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileOfflineDataService.createOfflineData(offlineData);
            result.put("success", success);
            result.put("message", success ? "创建成功" : "创建失败");
            result.put("data", success ? offlineData : null);
        } catch (Exception e) {
            log.error("创建离线数据失败", e);
            result.put("success", false);
            result.put("message", "创建失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/update")
    @ApiOperation("更新离线数据")
    public Map<String, Object> updateOfflineData(@RequestBody MobileOfflineData offlineData) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileOfflineDataService.updateOfflineData(offlineData);
            result.put("success", success);
            result.put("message", success ? "更新成功" : "更新失败");
            result.put("data", success ? offlineData : null);
        } catch (Exception e) {
            log.error("更新离线数据失败", e);
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/delete/{offlineDataId}")
    @ApiOperation("删除离线数据")
    public Map<String, Object> deleteOfflineData(@PathVariable String offlineDataId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileOfflineDataService.deleteOfflineData(offlineDataId);
            result.put("success", success);
            result.put("message", success ? "删除成功" : "删除失败");
        } catch (Exception e) {
            log.error("删除离线数据失败", e);
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/get/{offlineDataId}")
    @ApiOperation("根据ID获取离线数据")
    public Map<String, Object> getOfflineDataById(@PathVariable String offlineDataId) {
        Map<String, Object> result = new HashMap<>();
        try {
            MobileOfflineData offlineData = mobileOfflineDataService.getOfflineDataById(offlineDataId);
            result.put("success", offlineData != null);
            result.put("message", offlineData != null ? "获取成功" : "数据不存在");
            result.put("data", offlineData);
        } catch (Exception e) {
            log.error("获取离线数据失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/get-by-code/{offlineDataCode}")
    @ApiOperation("根据编码获取离线数据")
    public Map<String, Object> getOfflineDataByCode(@PathVariable String offlineDataCode) {
        Map<String, Object> result = new HashMap<>();
        try {
            MobileOfflineData offlineData = mobileOfflineDataService.getOfflineDataByCode(offlineDataCode);
            result.put("success", offlineData != null);
            result.put("message", offlineData != null ? "获取成功" : "数据不存在");
            result.put("data", offlineData);
        } catch (Exception e) {
            log.error("根据编码获取离线数据失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 查询操作 ====================

    @GetMapping("/list-by-app-config/{appConfigId}")
    @ApiOperation("根据应用配置ID获取离线数据列表")
    public Map<String, Object> getOfflineDatasByAppConfigId(@PathVariable String appConfigId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileOfflineData> offlineDataList = mobileOfflineDataService.getOfflineDatasByAppConfigId(appConfigId);
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", offlineDataList);
            result.put("total", offlineDataList != null ? offlineDataList.size() : 0);
        } catch (Exception e) {
            log.error("根据应用配置ID获取离线数据失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-by-type/{offlineDataType}")
    @ApiOperation("根据类型获取离线数据列表")
    public Map<String, Object> getOfflineDatasByType(@PathVariable String offlineDataType) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileOfflineData> offlineDataList = mobileOfflineDataService.getOfflineDatasByType(offlineDataType);
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", offlineDataList);
            result.put("total", offlineDataList != null ? offlineDataList.size() : 0);
        } catch (Exception e) {
            log.error("根据类型获取离线数据失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-by-category/{offlineDataCategory}")
    @ApiOperation("根据分类获取离线数据列表")
    public Map<String, Object> getOfflineDatasByCategory(@PathVariable String offlineDataCategory) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileOfflineData> offlineDataList = mobileOfflineDataService.getOfflineDatasByCategory(offlineDataCategory);
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", offlineDataList);
            result.put("total", offlineDataList != null ? offlineDataList.size() : 0);
        } catch (Exception e) {
            log.error("根据分类获取离线数据失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-by-user/{userId}")
    @ApiOperation("根据用户ID获取离线数据")
    public Map<String, Object> getOfflineDatasByUserId(@PathVariable String userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileOfflineData> offlineDataList = mobileOfflineDataService.getOfflineDatasByUserId(userId);
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", offlineDataList);
            result.put("total", offlineDataList != null ? offlineDataList.size() : 0);
        } catch (Exception e) {
            log.error("根据用户ID获取离线数据失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-available")
    @ApiOperation("获取可用的离线数据")
    public Map<String, Object> getAvailableOfflineData() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileOfflineData> offlineDataList = mobileOfflineDataService.getAvailableOfflineData();
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", offlineDataList);
            result.put("total", offlineDataList != null ? offlineDataList.size() : 0);
        } catch (Exception e) {
            log.error("获取可用离线数据失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-expired")
    @ApiOperation("获取已过期的离线数据")
    public Map<String, Object> getExpiredOfflineData() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileOfflineData> offlineDataList = mobileOfflineDataService.getExpiredOfflineData();
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", offlineDataList);
            result.put("total", offlineDataList != null ? offlineDataList.size() : 0);
        } catch (Exception e) {
            log.error("获取过期离线数据失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list-need-sync")
    @ApiOperation("获取需要同步的离线数据")
    public Map<String, Object> getNeedSyncOfflineData() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileOfflineData> offlineDataList = mobileOfflineDataService.getNeedSyncOfflineData();
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", offlineDataList);
            result.put("total", offlineDataList != null ? offlineDataList.size() : 0);
        } catch (Exception e) {
            log.error("获取需要同步的离线数据失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 分页查询 ====================

    @GetMapping("/page")
    @ApiOperation("分页查询离线数据")
    public Map<String, Object> getOfflineDataPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String offlineDataName,
            @RequestParam(required = false) String offlineDataType,
            @RequestParam(required = false) String offlineDataCategory,
            @RequestParam(required = false) String dataStatus,
            @RequestParam(required = false) String syncStatus) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<MobileOfflineData> page = new Page<>(current, size);
            Map<String, Object> params = new HashMap<>();
            params.put("offlineDataName", offlineDataName);
            params.put("offlineDataType", offlineDataType);
            params.put("offlineDataCategory", offlineDataCategory);
            params.put("dataStatus", dataStatus);
            params.put("syncStatus", syncStatus);

            IPage<MobileOfflineData> pageResult = mobileOfflineDataService.getOfflineDataPage(page, params);
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", pageResult.getRecords());
            result.put("total", pageResult.getTotal());
            result.put("current", pageResult.getCurrent());
            result.put("size", pageResult.getSize());
        } catch (Exception e) {
            log.error("分页查询离线数据失败", e);
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 离线数据管理 ====================

    @PutMapping("/enable/{offlineDataId}")
    @ApiOperation("启用离线数据")
    public Map<String, Object> enableOfflineData(@PathVariable String offlineDataId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileOfflineDataService.enableOfflineData(offlineDataId);
            result.put("success", success);
            result.put("message", success ? "启用成功" : "启用失败");
        } catch (Exception e) {
            log.error("启用离线数据失败", e);
            result.put("success", false);
            result.put("message", "启用失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/disable/{offlineDataId}")
    @ApiOperation("禁用离线数据")
    public Map<String, Object> disableOfflineData(@PathVariable String offlineDataId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileOfflineDataService.disableOfflineData(offlineDataId);
            result.put("success", success);
            result.put("message", success ? "禁用成功" : "禁用失败");
        } catch (Exception e) {
            log.error("禁用离线数据失败", e);
            result.put("success", false);
            result.put("message", "禁用失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/batch-update-data-status")
    @ApiOperation("批量更新数据状态")
    public Map<String, Object> batchUpdateDataStatus(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> offlineDataIds = (List<String>) params.get("offlineDataIds");
            String dataStatus = (String) params.get("dataStatus");
            
            boolean success = mobileOfflineDataService.batchUpdateDataStatus(offlineDataIds, dataStatus);
            result.put("success", success);
            result.put("message", success ? "批量更新成功" : "批量更新失败");
        } catch (Exception e) {
            log.error("批量更新数据状态失败", e);
            result.put("success", false);
            result.put("message", "批量更新失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/batch-enable")
    @ApiOperation("批量启用离线数据")
    public Map<String, Object> batchEnableOfflineData(@RequestBody List<String> offlineDataIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileOfflineDataService.batchEnableOfflineData(offlineDataIds);
            result.put("success", success);
            result.put("message", success ? "批量启用成功" : "批量启用失败");
        } catch (Exception e) {
            log.error("批量启用离线数据失败", e);
            result.put("success", false);
            result.put("message", "批量启用失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/batch-disable")
    @ApiOperation("批量禁用离线数据")
    public Map<String, Object> batchDisableOfflineData(@RequestBody List<String> offlineDataIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileOfflineDataService.batchDisableOfflineData(offlineDataIds);
            result.put("success", success);
            result.put("message", success ? "批量禁用成功" : "批量禁用失败");
        } catch (Exception e) {
            log.error("批量禁用离线数据失败", e);
            result.put("success", false);
            result.put("message", "批量禁用失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 数据同步管理 ====================

    @PostMapping("/sync/{offlineDataId}")
    @ApiOperation("同步离线数据")
    public Map<String, Object> syncOfflineData(@PathVariable String offlineDataId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileOfflineDataService.syncOfflineData(offlineDataId);
            result.put("success", success);
            result.put("message", success ? "同步成功" : "同步失败");
        } catch (Exception e) {
            log.error("同步离线数据失败", e);
            result.put("success", false);
            result.put("message", "同步失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/batch-sync")
    @ApiOperation("批量同步离线数据")
    public Map<String, Object> batchSyncOfflineData(@RequestBody List<String> offlineDataIds) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileOfflineDataService.batchSyncOfflineData(offlineDataIds);
            result.put("success", success);
            result.put("message", success ? "批量同步成功" : "批量同步失败");
        } catch (Exception e) {
            log.error("批量同步离线数据失败", e);
            result.put("success", false);
            result.put("message", "批量同步失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/download/{offlineDataId}")
    @ApiOperation("下载离线数据")
    public Map<String, Object> downloadOfflineData(@PathVariable String offlineDataId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileOfflineDataService.downloadOfflineData(offlineDataId);
            result.put("success", success);
            result.put("message", success ? "下载成功" : "下载失败");
        } catch (Exception e) {
            log.error("下载离线数据失败", e);
            result.put("success", false);
            result.put("message", "下载失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 搜索功能 ====================

    @GetMapping("/search-by-keyword")
    @ApiOperation("根据关键词搜索离线数据")
    public Map<String, Object> searchOfflineDatasByKeyword(@RequestParam String keyword) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileOfflineData> offlineDataList = mobileOfflineDataService.searchOfflineDatasByKeyword(keyword);
            result.put("success", true);
            result.put("message", "搜索成功");
            result.put("data", offlineDataList);
            result.put("total", offlineDataList != null ? offlineDataList.size() : 0);
        } catch (Exception e) {
            log.error("根据关键词搜索离线数据失败", e);
            result.put("success", false);
            result.put("message", "搜索失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/search-by-tags")
    @ApiOperation("根据标签搜索离线数据")
    public Map<String, Object> searchOfflineDatasByTags(@RequestBody List<String> tags) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MobileOfflineData> offlineDataList = mobileOfflineDataService.searchOfflineDatasByTags(tags);
            result.put("success", true);
            result.put("message", "搜索成功");
            result.put("data", offlineDataList);
            result.put("total", offlineDataList != null ? offlineDataList.size() : 0);
        } catch (Exception e) {
            log.error("根据标签搜索离线数据失败", e);
            result.put("success", false);
            result.put("message", "搜索失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 验证功能 ====================

    @PostMapping("/validate")
    @ApiOperation("验证离线数据")
    public Map<String, Object> validateOfflineData(@RequestBody MobileOfflineData offlineData) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean isValid = mobileOfflineDataService.validateOfflineData(offlineData);
            result.put("success", true);
            result.put("message", "验证完成");
            result.put("data", isValid);
            result.put("valid", isValid);
        } catch (Exception e) {
            log.error("验证离线数据失败", e);
            result.put("success", false);
            result.put("message", "验证失败：" + e.getMessage());
        }
        return result;
    }

    @PostMapping("/test-connection/{offlineDataId}")
    @ApiOperation("测试离线数据连接")
    public Map<String, Object> testOfflineDataConnection(@PathVariable String offlineDataId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = mobileOfflineDataService.testOfflineDataConnection(offlineDataId);
            result.put("success", success);
            result.put("message", success ? "连接测试成功" : "连接测试失败");
            result.put("connected", success);
        } catch (Exception e) {
            log.error("测试离线数据连接失败", e);
            result.put("success", false);
            result.put("message", "连接测试失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 统计分析 ====================

    @GetMapping("/count")
    @ApiOperation("统计离线数据总数")
    public Map<String, Object> countOfflineData() {
        Map<String, Object> result = new HashMap<>();
        try {
            Long count = mobileOfflineDataService.countOfflineData();
            result.put("success", true);
            result.put("message", "统计成功");
            result.put("data", count);
        } catch (Exception e) {
            log.error("统计离线数据总数失败", e);
            result.put("success", false);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/count-by-type")
    @ApiOperation("根据类型统计离线数据")
    public Map<String, Object> countOfflineDataByType(@RequestParam String offlineDataType) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long count = mobileOfflineDataService.countOfflineDataByType(offlineDataType);
            result.put("success", true);
            result.put("message", "统计成功");
            result.put("data", count);
        } catch (Exception e) {
            log.error("根据类型统计离线数据失败", e);
            result.put("success", false);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/count-by-status")
    @ApiOperation("根据状态统计离线数据")
    public Map<String, Object> countOfflineDataByStatus(@RequestParam String status) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long count = mobileOfflineDataService.countOfflineDataByStatus(status);
            result.put("success", true);
            result.put("message", "统计成功");
            result.put("data", count);
        } catch (Exception e) {
            log.error("根据状态统计离线数据失败", e);
            result.put("success", false);
            result.put("message", "统计失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/system-overview")
    @ApiOperation("获取系统概览")
    public Map<String, Object> getSystemOverview() {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> overview = mobileOfflineDataService.getSystemOverview();
            result.put("success", true);
            result.put("message", "获取成功");
            result.put("data", overview);
        } catch (Exception e) {
            log.error("获取系统概览失败", e);
            result.put("success", false);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
}
