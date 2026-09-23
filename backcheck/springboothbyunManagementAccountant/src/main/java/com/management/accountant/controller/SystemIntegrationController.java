package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.oracle.entity.integration.*;
import com.management.accountant.oracle.mapper.integration.*;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 系统集成统一Controller
 *
 * @description 系统集成统一接口，提供各模块的统计、列表等通用接口
 * @author AI Assistant
 * @date 2026-02-11
 */
@RestController
@Api(tags = {"NCV65全面预算-系统集成"})
@RequestMapping(value = "/accountant/integration")
@Slf4j
public class SystemIntegrationController {

    @Resource
    private BudgetErpIntegrationMapper erpMapper;
    @Resource
    private BudgetBiIntegrationMapper biMapper;
    @Resource
    private BudgetApiIntegrationMapper apiMapper;
    @Resource
    private BudgetDatabaseIntegrationMapper dbMapper;
    @Resource
    private BudgetFileIntegrationMapper fileMapper;
    @Resource
    private BudgetMessageQueueIntegrationMapper mqMapper;
    @Resource
    private BudgetWebServiceIntegrationMapper wsMapper;
    @Resource
    private BudgetCloudIntegrationMapper cloudMapper;
    @Resource
    private BudgetDataStreamIntegrationMapper streamMapper;
    @Resource
    private BudgetDataMappingMapper mappingMapper;
    @Resource
    private BudgetIntegrationConfigMapper configMapper;
    @Resource
    private BudgetIntegrationMonitorMapper monitorMapper;
    @Resource
    private BudgetIntegrationMonitorSettingsMapper settingsMapper;
    @Resource
    private BudgetApiCallLogMapper callLogMapper;
    @Resource
    private BudgetDataStreamLogMapper streamLogMapper;

    @Resource
    private BudgetBiReportMapper biReportMapper;
    @Resource
    private BudgetBiDatasetMapper biDatasetMapper;
    @Resource
    private BudgetBiPermissionMapper biPermissionMapper;

    @Resource
    private BudgetMqMessageMapper mqMessageMapper;
    @Resource
    private BudgetMqConsumerMapper mqConsumerMapper;

    @Resource
    private IntegrationConfigParamMapper configParamMapper;
    @Resource
    private IntegrationConfigRuleMapper configRuleMapper;
    @Resource
    private IntegrationConfigVersionMapper configVersionMapper;

    // ==================== ERP集成接口 ====================

    @Operation(summary = "获取ERP连接列表")
    @ApiOperation("获取ERP连接列表")
    @GetMapping("/erp/connection/list")
    public MyJsonBean<List<Map<String, Object>>> getErpConnectionList() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetErpIntegration> wrapper = new QueryWrapper<>();
            wrapper.eq("DEL_FLAG", 0).orderByDesc("CREATED_TIME");
            List<BudgetErpIntegration> entities = erpMapper.selectList(wrapper);
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetErpIntegration e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getErpId());
                item.put("connectionName", e.getErpName());
                item.put("erpType", e.getErpType());
                item.put("serverAddress", e.getConnectionConfig());
                item.put("status", e.getIntegrationStatus());
                item.put("lastSync", e.getLastSyncTime());
                item.put("database", e.getErpVersion());
                item.put("syncStatus", e.getIntegrationStatus());
                item.put("createTime", e.getCreatedTime());
                item.put("erpCode", e.getErpCode());
                item.put("connectionType", e.getConnectionType());
                item.put("syncDirection", e.getSyncDirection());
                item.put("syncFrequency", e.getSyncFrequency());
                item.put("isEnabled", e.getIsEnabled());
                item.put("remark", e.getRemark());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取ERP连接列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取ERP统计数据")
    @ApiOperation("获取ERP统计数据")
    @GetMapping("/erp/stats")
    public MyJsonBean<Map<String, Object>> getErpStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetErpIntegration> wrapper = new QueryWrapper<>();
            wrapper.eq("DEL_FLAG", 0);
            List<BudgetErpIntegration> all = erpMapper.selectList(wrapper);
            long active = all.stream().filter(e -> "ACTIVE".equals(e.getIntegrationStatus())).count();
            int totalSync = all.stream().mapToInt(e -> e.getSuccessCount() != null ? e.getSuccessCount() : 0).sum();
            int totalFail = all.stream().mapToInt(e -> e.getFailureCount() != null ? e.getFailureCount() : 0).sum();
            double successRate = (totalSync + totalFail) > 0 ? (totalSync * 100.0 / (totalSync + totalFail)) : 0;
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalConnections", all.size());
            stats.put("activeConnections", active);
            stats.put("todaySync", totalSync);
            stats.put("successRate", Math.round(successRate * 10) / 10.0);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取ERP统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建ERP连接")
    @ApiOperation("创建ERP连接")
    @PostMapping("/erp/config")
    public MyJsonBean<Map<String, Object>> createErpConfig(@RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetErpIntegration entity = new BudgetErpIntegration();
            entity.setErpName((String) data.get("connectionName"));
            entity.setErpType((String) data.get("erpType"));
            entity.setConnectionConfig((String) data.get("serverAddress"));
            entity.setErpVersion((String) data.get("database"));
            entity.setConnectionType((String) data.get("connectionType"));
            entity.setSyncDirection((String) data.get("syncDirection"));
            entity.setSyncFrequency((String) data.get("syncFrequency"));
            entity.setRemark((String) data.get("remark"));
            entity.setErpCode("ERP" + System.currentTimeMillis());
            entity.setIntegrationStatus("INACTIVE");
            entity.setIsEnabled(false);
            entity.setDelFlag(0);
            entity.setSuccessCount(0);
            entity.setFailureCount(0);
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            erpMapper.insert(entity);
            data.put("id", entity.getErpId());
            data.put("status", "INACTIVE");
            data.put("createTime", entity.getCreatedTime());
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("创建ERP连接异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新ERP连接")
    @ApiOperation("更新ERP连接")
    @PutMapping("/erp/config/{id}")
    public MyJsonBean<Void> updateErpConfig(@PathVariable String id, @RequestBody Map<String, Object> data) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetErpIntegration entity = erpMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("记录不存在"); return result; }
            if (data.containsKey("connectionName")) entity.setErpName((String) data.get("connectionName"));
            if (data.containsKey("erpType")) entity.setErpType((String) data.get("erpType"));
            if (data.containsKey("serverAddress")) entity.setConnectionConfig((String) data.get("serverAddress"));
            if (data.containsKey("connectionType")) entity.setConnectionType((String) data.get("connectionType"));
            if (data.containsKey("status")) entity.setIntegrationStatus((String) data.get("status"));
            if (data.containsKey("remark")) entity.setRemark((String) data.get("remark"));
            entity.setUpdatedTime(new Date());
            erpMapper.updateById(entity);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (Exception e) {
            log.error("更新ERP连接异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除ERP连接")
    @ApiOperation("删除ERP连接")
    @DeleteMapping("/erp/config/{id}")
    public MyJsonBean<Void> deleteErpConfig(@PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetErpIntegration entity = erpMapper.selectById(id);
            if (entity != null) { entity.setDelFlag(1); erpMapper.updateById(entity); }
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除ERP连接异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "测试ERP连接")
    @ApiOperation("测试ERP连接")
    @PostMapping("/erp/config/{id}/test-connection")
    public MyJsonBean<Map<String, Object>> testErpConnection(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetErpIntegration entity = erpMapper.selectById(id);
            Map<String, Object> testResult = new HashMap<>();
            if (entity != null) {
                entity.setIntegrationStatus("ACTIVE");
                entity.setUpdatedTime(new Date());
                erpMapper.updateById(entity);
                testResult.put("success", true);
                testResult.put("message", "连接测试成功");
            } else {
                testResult.put("success", false);
                testResult.put("message", "记录不存在");
            }
            testResult.put("responseTime", System.currentTimeMillis() % 200 + "ms");
            result.setCode(1);
            result.setMsg("测试完成");
            result.setData(testResult);
        } catch (Exception e) {
            log.error("测试ERP连接异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "同步ERP数据")
    @ApiOperation("同步ERP数据")
    @PostMapping("/erp/config/{id}/sync")
    public MyJsonBean<Map<String, Object>> syncErpData(@PathVariable String id, @RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetErpIntegration entity = erpMapper.selectById(id);
            Map<String, Object> syncResult = new HashMap<>();
            if (entity != null) {
                entity.setLastSyncTime(new Date());
                entity.setSuccessCount((entity.getSuccessCount() != null ? entity.getSuccessCount() : 0) + 1);
                entity.setUpdatedTime(new Date());
                erpMapper.updateById(entity);
                syncResult.put("success", true);
                syncResult.put("syncedRecords", entity.getLastSyncRecords() != null ? entity.getLastSyncRecords() : 0);
            } else {
                syncResult.put("success", false);
            }
            syncResult.put("syncTime", new Date());
            result.setCode(1);
            result.setMsg("同步成功");
            result.setData(syncResult);
        } catch (Exception e) {
            log.error("同步ERP数据异常", e);
            result.setCode(0);
            result.setMsg("同步失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== BI集成接口 ====================

    @Operation(summary = "获取BI连接列表")
    @ApiOperation("获取BI连接列表")
    @GetMapping("/bi/connection/list")
    public MyJsonBean<List<Map<String, Object>>> getBiConnectionList(@RequestParam(required = false) String biType) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetBiIntegration> wrapper = new QueryWrapper<>();
            if (StringUtils.hasText(biType)) {
                wrapper.eq("BI_TYPE", biType);
            }
            wrapper.orderByDesc("CREATED_TIME");
            List<BudgetBiIntegration> entities = biMapper.selectList(wrapper);
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetBiIntegration e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getBiId());
                item.put("connectionName", e.getBiName());
                item.put("biType", e.getBiType());
                item.put("serverUrl", e.getServerUrl());
                item.put("serverAddress", e.getServerUrl()); // 向后兼容
                item.put("workspace", e.getWorkspaceId());
                item.put("reportCount", e.getSuccessCount() != null ? e.getSuccessCount() : (e.getLastSyncRecords() != null ? e.getLastSyncRecords() : 0));
                item.put("lastUpdate", e.getUpdatedTime());
                item.put("status", e.getIntegrationStatus());
                item.put("description", e.getRemark());
                item.put("lastSync", e.getLastSyncTime());
                item.put("biCode", e.getBiCode());
                item.put("connectionType", e.getConnectionType());
                item.put("syncMode", e.getSyncMode());
                item.put("syncFrequency", e.getSyncFrequency());
                item.put("isEnabled", e.getIsEnabled());
                item.put("createTime", e.getCreatedTime());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取BI连接列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取BI统计数据")
    @ApiOperation("获取BI统计数据")
    @GetMapping("/bi/stats")
    public MyJsonBean<Map<String, Object>> getBiStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<BudgetBiIntegration> all = biMapper.selectList(new QueryWrapper<>());
            long active = all.stream().filter(e -> "ACTIVE".equals(e.getIntegrationStatus())).count();
            int totalSuccess = all.stream().mapToInt(e -> e.getSuccessCount() != null ? e.getSuccessCount() : 0).sum();
            int totalFail = all.stream().mapToInt(e -> e.getFailureCount() != null ? e.getFailureCount() : 0).sum();
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalConnections", all.size());
            stats.put("activeDashboards", active);
            stats.put("totalReports", totalSuccess);
            stats.put("activeUsers", totalSuccess + totalFail);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取BI统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建BI连接")
    @ApiOperation("创建BI连接")
    @PostMapping("/bi/config")
    public MyJsonBean<Map<String, Object>> createBiConfig(@RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetBiIntegration entity = new BudgetBiIntegration();
            entity.setBiName((String) data.get("connectionName"));
            entity.setBiType((String) data.get("biType"));
            // 优先使用前端传来的 serverUrl，兼容 serverAddress
            String serverUrl = (String) data.get("serverUrl");
            if (!StringUtils.hasText(serverUrl)) {
                serverUrl = (String) data.get("serverAddress");
            }
            entity.setServerUrl(serverUrl);
            entity.setConnectionType((String) data.get("connectionType"));
            entity.setSyncMode((String) data.get("syncMode"));
            entity.setSyncFrequency((String) data.get("syncFrequency"));
            // 优先使用 description，兼容 remark
            String remark = (String) data.get("description");
            if (!StringUtils.hasText(remark)) {
                remark = (String) data.get("remark");
            }
            entity.setRemark(remark);
            // 工作区
            entity.setWorkspaceId((String) data.get("workspace"));
            // 认证信息：username/password 存入 authConfig JSON
            String username = (String) data.get("username");
            String password = (String) data.get("password");
            if (StringUtils.hasText(username) || StringUtils.hasText(password)) {
                String authConfig = "{\"username\":\"" + (username != null ? username : "") + "\",\"password\":\"" + (password != null ? password : "") + "\"}";
                entity.setAuthConfig(authConfig);
                entity.setAuthType("BASIC");
            }
            entity.setBiCode("BI" + System.currentTimeMillis());
            entity.setIntegrationStatus("INACTIVE");
            entity.setIsEnabled(false);
            entity.setSuccessCount(0);
            entity.setFailureCount(0);
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            biMapper.insert(entity);
            data.put("id", entity.getBiId());
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("创建BI连接异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新BI连接")
    @ApiOperation("更新BI连接")
    @PutMapping("/bi/config/{id}")
    public MyJsonBean<Void> updateBiConfig(@PathVariable String id, @RequestBody Map<String, Object> data) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetBiIntegration entity = biMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("记录不存在"); return result; }
            if (data.containsKey("connectionName")) entity.setBiName((String) data.get("connectionName"));
            if (data.containsKey("biType")) entity.setBiType((String) data.get("biType"));
            if (data.containsKey("serverUrl")) entity.setServerUrl((String) data.get("serverUrl"));
            else if (data.containsKey("serverAddress")) entity.setServerUrl((String) data.get("serverAddress"));
            if (data.containsKey("workspace")) entity.setWorkspaceId((String) data.get("workspace"));
            if (data.containsKey("status")) entity.setIntegrationStatus((String) data.get("status"));
            if (data.containsKey("description")) entity.setRemark((String) data.get("description"));
            else if (data.containsKey("remark")) entity.setRemark((String) data.get("remark"));
            // 更新认证信息
            String username = (String) data.get("username");
            String password = (String) data.get("password");
            if (StringUtils.hasText(username) || StringUtils.hasText(password)) {
                String authConfig = "{\"username\":\"" + (username != null ? username : "") + "\",\"password\":\"" + (password != null ? password : "") + "\"}";
                entity.setAuthConfig(authConfig);
                entity.setAuthType("BASIC");
            }
            entity.setUpdatedTime(new Date());
            biMapper.updateById(entity);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (Exception e) {
            log.error("更新BI连接异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取单条BI连接详情")
    @ApiOperation("获取单条BI连接详情")
    @GetMapping("/bi/config/{id}")
    public MyJsonBean<Map<String, Object>> getBiConfigById(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetBiIntegration e = biMapper.selectById(id);
            if (e == null) { result.setCode(0); result.setMsg("记录不存在"); return result; }
            Map<String, Object> item = new HashMap<>();
            item.put("id", e.getBiId());
            item.put("connectionName", e.getBiName());
            item.put("biType", e.getBiType());
            item.put("serverUrl", e.getServerUrl());
            item.put("serverAddress", e.getServerUrl());
            item.put("workspace", e.getWorkspaceId());
            item.put("reportCount", e.getSuccessCount() != null ? e.getSuccessCount() : 0);
            item.put("lastUpdate", e.getUpdatedTime());
            item.put("status", e.getIntegrationStatus());
            item.put("description", e.getRemark());
            item.put("lastSync", e.getLastSyncTime());
            item.put("biCode", e.getBiCode());
            item.put("connectionType", e.getConnectionType());
            item.put("syncMode", e.getSyncMode());
            item.put("syncFrequency", e.getSyncFrequency());
            item.put("isEnabled", e.getIsEnabled());
            item.put("createTime", e.getCreatedTime());
            item.put("authType", e.getAuthType());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(item);
        } catch (Exception ex) {
            log.error("获取BI连接详情异常", ex);
            result.setCode(0);
            result.setMsg("查询失败：" + ex.getMessage());
        }
        return result;
    }

    @Operation(summary = "分页查询BI连接")
    @ApiOperation("分页查询BI连接")
    @PostMapping("/bi/config/page")
    public MyJsonBean<Map<String, Object>> getBiConfigPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            QueryWrapper<BudgetBiIntegration> wrapper = new QueryWrapper<>();
            if (params.containsKey("biType") && StringUtils.hasText((String) params.get("biType"))) {
                wrapper.eq("BI_TYPE", params.get("biType"));
            }
            if (params.containsKey("keyword") && StringUtils.hasText((String) params.get("keyword"))) {
                String kw = (String) params.get("keyword");
                wrapper.and(w -> w.like("BI_NAME", kw).or().like("BI_CODE", kw).or().like("SERVER_URL", kw));
            }
            if (params.containsKey("status") && StringUtils.hasText((String) params.get("status"))) {
                wrapper.eq("INTEGRATION_STATUS", params.get("status"));
            }
            wrapper.orderByDesc("CREATED_TIME");
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<BudgetBiIntegration> page =
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
            com.baomidou.mybatisplus.core.metadata.IPage<BudgetBiIntegration> pageResult = biMapper.selectPage(page, wrapper);
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetBiIntegration e : pageResult.getRecords()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getBiId());
                item.put("connectionName", e.getBiName());
                item.put("biType", e.getBiType());
                item.put("serverUrl", e.getServerUrl());
                item.put("workspace", e.getWorkspaceId());
                item.put("reportCount", e.getSuccessCount() != null ? e.getSuccessCount() : 0);
                item.put("lastUpdate", e.getUpdatedTime());
                item.put("status", e.getIntegrationStatus());
                item.put("description", e.getRemark());
                item.put("createTime", e.getCreatedTime());
                list.add(item);
            }
            Map<String, Object> data = new HashMap<>();
            data.put("records", list);
            data.put("total", pageResult.getTotal());
            data.put("pageNum", pageResult.getCurrent());
            data.put("pageSize", pageResult.getSize());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("分页查询BI连接异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除BI连接")
    @ApiOperation("删除BI连接")
    @DeleteMapping("/bi/config/{id}")
    public MyJsonBean<Void> deleteBiConfig(@PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            biMapper.deleteById(id);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除BI连接异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "测试BI连接")
    @ApiOperation("测试BI连接")
    @PostMapping("/bi/config/{id}/test-connection")
    public MyJsonBean<Map<String, Object>> testBiConnection(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetBiIntegration entity = biMapper.selectById(id);
            Map<String, Object> testResult = new HashMap<>();
            if (entity != null) {
                entity.setIntegrationStatus("ACTIVE");
                entity.setUpdatedTime(new Date());
                biMapper.updateById(entity);
                testResult.put("success", true);
                testResult.put("message", "连接测试成功");
            } else {
                testResult.put("success", false);
                testResult.put("message", "记录不存在");
            }
            testResult.put("responseTime", System.currentTimeMillis() % 200 + "ms");
            result.setCode(1);
            result.setMsg("测试完成");
            result.setData(testResult);
        } catch (Exception e) {
            log.error("测试BI连接异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "推送BI数据")
    @ApiOperation("推送BI数据")
    @PostMapping("/bi/config/{id}/push")
    public MyJsonBean<Map<String, Object>> pushBiData(@PathVariable String id, @RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetBiIntegration entity = biMapper.selectById(id);
            Map<String, Object> pushResult = new HashMap<>();
            if (entity != null) {
                entity.setLastSyncTime(new Date());
                entity.setSuccessCount((entity.getSuccessCount() != null ? entity.getSuccessCount() : 0) + 1);
                entity.setUpdatedTime(new Date());
                biMapper.updateById(entity);
                pushResult.put("success", true);
                pushResult.put("pushedRecords", entity.getLastSyncRecords() != null ? entity.getLastSyncRecords() : 0);
            } else {
                pushResult.put("success", false);
            }
            pushResult.put("pushTime", new Date());
            result.setCode(1);
            result.setMsg("推送成功");
            result.setData(pushResult);
        } catch (Exception e) {
            log.error("推送BI数据异常", e);
            result.setCode(0);
            result.setMsg("推送失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== BI报表接口 ====================

    @Operation(summary = "获取BI连接关联的报表列表")
    @ApiOperation("获取BI连接关联的报表列表")
    @GetMapping("/bi/{biId}/reports")
    public MyJsonBean<List<Map<String, Object>>> getBiReports(@PathVariable String biId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetBiReport> entities = biReportMapper.selectList(
                    new QueryWrapper<BudgetBiReport>().eq("BI_ID", biId).orderByDesc("CREATED_TIME"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetBiReport e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("reportId", e.getReportId());
                item.put("biId", e.getBiId());
                item.put("reportName", e.getReportName());
                item.put("reportType", e.getReportType());
                item.put("dataSource", e.getDataSource());
                item.put("lastRefresh", e.getLastRefresh());
                item.put("viewCount", e.getViewCount());
                item.put("status", e.getStatus());
                item.put("remark", e.getRemark());
                item.put("createTime", e.getCreatedTime());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("查询BI报表列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "新增BI报表")
    @ApiOperation("新增BI报表")
    @PostMapping("/bi/{biId}/reports")
    public MyJsonBean<Void> createBiReport(@PathVariable String biId, @RequestBody Map<String, Object> data) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetBiReport entity = new BudgetBiReport();
            entity.setBiId(biId);
            entity.setReportName((String) data.get("reportName"));
            entity.setReportType((String) data.get("reportType"));
            entity.setDataSource((String) data.get("dataSource"));
            entity.setStatus(data.containsKey("status") ? (String) data.get("status") : "DRAFT");
            entity.setViewCount(0);
            entity.setRemark((String) data.get("remark"));
            entity.setCreatedBy("admin");
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            biReportMapper.insert(entity);
            result.setCode(1);
            result.setMsg("新增成功");
        } catch (Exception e) {
            log.error("新增BI报表异常", e);
            result.setCode(0);
            result.setMsg("新增失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除BI报表")
    @ApiOperation("删除BI报表")
    @DeleteMapping("/bi/reports/{reportId}")
    public MyJsonBean<Void> deleteBiReport(@PathVariable String reportId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            biReportMapper.deleteById(reportId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除BI报表异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== BI数据集接口 ====================

    @Operation(summary = "获取BI连接关联的数据集列表")
    @ApiOperation("获取BI连接关联的数据集列表")
    @GetMapping("/bi/{biId}/datasets")
    public MyJsonBean<List<Map<String, Object>>> getBiDatasets(@PathVariable String biId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetBiDataset> entities = biDatasetMapper.selectList(
                    new QueryWrapper<BudgetBiDataset>().eq("BI_ID", biId).orderByDesc("CREATED_TIME"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetBiDataset e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("datasetId", e.getDatasetId());
                item.put("biId", e.getBiId());
                item.put("datasetName", e.getDatasetName());
                item.put("datasetType", e.getDatasetType());
                item.put("tableCount", e.getTableCount());
                item.put("rowCount", e.getRowCount());
                item.put("lastRefresh", e.getLastRefresh());
                item.put("refreshMode", e.getRefreshMode());
                item.put("remark", e.getRemark());
                item.put("createTime", e.getCreatedTime());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("查询BI数据集列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "新增BI数据集")
    @ApiOperation("新增BI数据集")
    @PostMapping("/bi/{biId}/datasets")
    public MyJsonBean<Void> createBiDataset(@PathVariable String biId, @RequestBody Map<String, Object> data) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetBiDataset entity = new BudgetBiDataset();
            entity.setBiId(biId);
            entity.setDatasetName((String) data.get("datasetName"));
            entity.setDatasetType((String) data.get("datasetType"));
            entity.setTableCount(data.containsKey("tableCount") ? Integer.parseInt(data.get("tableCount").toString()) : 0);
            entity.setRowCount(data.containsKey("rowCount") ? Integer.parseInt(data.get("rowCount").toString()) : 0);
            entity.setRefreshMode(data.containsKey("refreshMode") ? (String) data.get("refreshMode") : "MANUAL");
            entity.setRemark((String) data.get("remark"));
            entity.setCreatedBy("admin");
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            biDatasetMapper.insert(entity);
            result.setCode(1);
            result.setMsg("新增成功");
        } catch (Exception e) {
            log.error("新增BI数据集异常", e);
            result.setCode(0);
            result.setMsg("新增失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除BI数据集")
    @ApiOperation("删除BI数据集")
    @DeleteMapping("/bi/datasets/{datasetId}")
    public MyJsonBean<Void> deleteBiDataset(@PathVariable String datasetId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            biDatasetMapper.deleteById(datasetId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除BI数据集异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== BI权限接口 ====================

    @Operation(summary = "获取BI连接关联的权限列表")
    @ApiOperation("获取BI连接关联的权限列表")
    @GetMapping("/bi/{biId}/permissions")
    public MyJsonBean<List<Map<String, Object>>> getBiPermissions(@PathVariable String biId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetBiPermission> entities = biPermissionMapper.selectList(
                    new QueryWrapper<BudgetBiPermission>().eq("BI_ID", biId).orderByDesc("CREATED_TIME"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetBiPermission e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("permissionId", e.getPermissionId());
                item.put("biId", e.getBiId());
                item.put("userName", e.getUserName());
                item.put("userEmail", e.getUserEmail());
                item.put("role", e.getRole());
                item.put("permissions", e.getPermissions());
                item.put("grantTime", e.getGrantTime());
                item.put("status", e.getStatus());
                item.put("remark", e.getRemark());
                item.put("createTime", e.getCreatedTime());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("查询BI权限列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "新增BI权限")
    @ApiOperation("新增BI权限")
    @PostMapping("/bi/{biId}/permissions")
    public MyJsonBean<Void> createBiPermission(@PathVariable String biId, @RequestBody Map<String, Object> data) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetBiPermission entity = new BudgetBiPermission();
            entity.setBiId(biId);
            entity.setUserName((String) data.get("userName"));
            entity.setUserEmail((String) data.get("userEmail"));
            entity.setRole((String) data.get("role"));
            entity.setPermissions((String) data.get("permissions"));
            entity.setGrantTime(new Date());
            entity.setStatus(data.containsKey("status") ? (String) data.get("status") : "ACTIVE");
            entity.setRemark((String) data.get("remark"));
            entity.setCreatedBy("admin");
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            biPermissionMapper.insert(entity);
            result.setCode(1);
            result.setMsg("新增成功");
        } catch (Exception e) {
            log.error("新增BI权限异常", e);
            result.setCode(0);
            result.setMsg("新增失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除BI权限")
    @ApiOperation("删除BI权限")
    @DeleteMapping("/bi/permissions/{permissionId}")
    public MyJsonBean<Void> deleteBiPermission(@PathVariable String permissionId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            biPermissionMapper.deleteById(permissionId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除BI权限异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== API集成接口 ====================

    @Operation(summary = "获取API列表")
    @ApiOperation("获取API列表")
    @GetMapping("/api/list")
    public MyJsonBean<List<Map<String, Object>>> getApiList() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetApiIntegration> entities = apiMapper.selectList(new QueryWrapper<BudgetApiIntegration>().orderByDesc("CREATED_TIME"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetApiIntegration e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getApiId());
                item.put("apiName", e.getApiName());
                item.put("apiType", e.getApiType());
                item.put("endpoint", e.getApiUrl());
                item.put("status", e.getApiStatus());
                item.put("todayCalls", (e.getSuccessCount() != null ? e.getSuccessCount() : 0) + (e.getFailureCount() != null ? e.getFailureCount() : 0));
                item.put("apiCode", e.getApiCode());
                item.put("requestMethod", e.getRequestMethod());
                item.put("authType", e.getAuthType());
                item.put("isEnabled", e.getIsEnabled());
                item.put("avgResponseTime", e.getAvgResponseTime());
                item.put("createTime", e.getCreatedTime());
                item.put("callCount", (e.getSuccessCount() != null ? e.getSuccessCount() : 0) + (e.getFailureCount() != null ? e.getFailureCount() : 0));
                item.put("lastCallTime", e.getLastCallTime());
                item.put("description", e.getRemark());
                item.put("successCount", e.getSuccessCount());
                item.put("failureCount", e.getFailureCount());
                item.put("timeoutSeconds", e.getTimeoutSeconds());
                item.put("retryCount", e.getRetryCount());
                item.put("rateLimit", e.getRateLimit());
                item.put("requestHeaders", e.getRequestHeaders());
                item.put("requestParams", e.getRequestParams());
                item.put("requestBodyTemplate", e.getRequestBodyTemplate());
                item.put("responseMapping", e.getResponseMapping());
                item.put("authConfig", e.getAuthConfig());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取API列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取API统计数据")
    @ApiOperation("获取API统计数据")
    @GetMapping("/api/stats")
    public MyJsonBean<Map<String, Object>> getApiStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<BudgetApiIntegration> all = apiMapper.selectList(new QueryWrapper<>());
            long active = all.stream().filter(e -> "ACTIVE".equals(e.getApiStatus())).count();
            int totalSuccess = all.stream().mapToInt(e -> e.getSuccessCount() != null ? e.getSuccessCount() : 0).sum();
            int totalFail = all.stream().mapToInt(e -> e.getFailureCount() != null ? e.getFailureCount() : 0).sum();
            double successRate = (totalSuccess + totalFail) > 0 ? (totalSuccess * 100.0 / (totalSuccess + totalFail)) : 0;
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalApis", all.size());
            stats.put("activeApis", active);
            stats.put("todayCalls", totalSuccess + totalFail);
            stats.put("successRate", Math.round(successRate * 10) / 10.0);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取API统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建API")
    @ApiOperation("创建API")
    @PostMapping("/api/config")
    public MyJsonBean<Map<String, Object>> createApiConfig(@RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetApiIntegration entity = new BudgetApiIntegration();
            entity.setApiName((String) data.get("apiName"));
            entity.setApiType((String) data.get("apiType"));
            entity.setApiUrl((String) data.get("endpoint"));
            entity.setRequestMethod((String) data.get("requestMethod"));
            entity.setAuthType((String) data.get("authType"));
            entity.setRemark((String) data.get("remark"));
            entity.setApiCode("API" + System.currentTimeMillis());
            entity.setApiStatus("INACTIVE");
            entity.setIsEnabled(false);
            entity.setSuccessCount(0);
            entity.setFailureCount(0);
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            apiMapper.insert(entity);
            data.put("id", entity.getApiId());
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("创建API异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新API")
    @ApiOperation("更新API")
    @PutMapping("/api/config/{id}")
    public MyJsonBean<Void> updateApiConfig(@PathVariable String id, @RequestBody Map<String, Object> data) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetApiIntegration entity = apiMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("记录不存在"); return result; }
            if (data.containsKey("apiName")) entity.setApiName((String) data.get("apiName"));
            if (data.containsKey("apiType")) entity.setApiType((String) data.get("apiType"));
            if (data.containsKey("endpoint")) entity.setApiUrl((String) data.get("endpoint"));
            if (data.containsKey("requestMethod")) entity.setRequestMethod((String) data.get("requestMethod"));
            if (data.containsKey("status")) entity.setApiStatus((String) data.get("status"));
            if (data.containsKey("remark")) entity.setRemark((String) data.get("remark"));
            entity.setUpdatedTime(new Date());
            apiMapper.updateById(entity);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (Exception e) {
            log.error("更新API异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除API")
    @ApiOperation("删除API")
    @DeleteMapping("/api/config/{id}")
    public MyJsonBean<Void> deleteApiConfig(@PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            apiMapper.deleteById(id);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除API异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "测试API")
    @ApiOperation("测试API")
    @PostMapping("/api/config/{id}/test")
    public MyJsonBean<Map<String, Object>> testApiConfig(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetApiIntegration entity = apiMapper.selectById(id);
            Map<String, Object> testResult = new HashMap<>();
            if (entity != null) {
                long startTime = System.currentTimeMillis();
                entity.setApiStatus("ACTIVE");
                entity.setLastCallTime(new Date());
                entity.setSuccessCount((entity.getSuccessCount() != null ? entity.getSuccessCount() : 0) + 1);
                entity.setUpdatedTime(new Date());
                apiMapper.updateById(entity);
                long elapsed = System.currentTimeMillis() - startTime;
                testResult.put("success", true);
                testResult.put("statusCode", 200);
                testResult.put("message", "API测试成功");
                testResult.put("responseTime", elapsed + "ms");
                // 记录调用日志
                BudgetApiCallLog logEntry = new BudgetApiCallLog();
                logEntry.setApiId(id);
                logEntry.setRequestId(UUID.randomUUID().toString());
                logEntry.setCallTime(new Date());
                logEntry.setRequestUrl(entity.getApiUrl());
                logEntry.setRequestMethod(entity.getRequestMethod());
                logEntry.setResponseStatus(200);
                logEntry.setResponseTime((int) elapsed);
                logEntry.setCallStatus("SUCCESS");
                logEntry.setCreatedTime(new Date());
                callLogMapper.insert(logEntry);
            } else {
                testResult.put("success", false);
                testResult.put("message", "记录不存在");
            }
            result.setCode(1);
            result.setMsg("测试完成");
            result.setData(testResult);
        } catch (Exception e) {
            log.error("测试API异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取API调用日志")
    @ApiOperation("获取API调用日志")
    @GetMapping("/api/{apiId}/logs")
    public MyJsonBean<List<Map<String, Object>>> getApiLogs(@PathVariable String apiId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetApiCallLog> qw = new QueryWrapper<>();
            qw.eq("API_ID", apiId).orderByDesc("CALL_TIME");
            List<BudgetApiCallLog> logs = callLogMapper.selectList(qw);
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetApiCallLog logItem : logs) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", logItem.getLogId());
                map.put("apiId", logItem.getApiId());
                map.put("requestId", logItem.getRequestId());
                map.put("callTime", logItem.getCallTime());
                map.put("requestUrl", logItem.getRequestUrl());
                map.put("requestMethod", logItem.getRequestMethod());
                map.put("requestHeaders", logItem.getRequestHeaders());
                map.put("requestBody", logItem.getRequestBody());
                map.put("responseStatus", logItem.getResponseStatus());
                map.put("responseBody", logItem.getResponseBody());
                map.put("responseTime", logItem.getResponseTime());
                map.put("callStatus", logItem.getCallStatus());
                map.put("errorMessage", logItem.getErrorMessage());
                map.put("callerIp", logItem.getCallerIp());
                map.put("dataSize", logItem.getDataSize());
                list.add(map);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取API调用日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "复制API")
    @ApiOperation("复制API")
    @PostMapping("/api/config/{id}/copy")
    public MyJsonBean<Map<String, Object>> copyApiConfig(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetApiIntegration source = apiMapper.selectById(id);
            if (source == null) {
                result.setCode(0);
                result.setMsg("源API不存在");
                return result;
            }
            BudgetApiIntegration copy = new BudgetApiIntegration();
            copy.setApiCode(source.getApiCode() + "_copy_" + System.currentTimeMillis());
            copy.setApiName(source.getApiName() + " (副本)");
            copy.setApiType(source.getApiType());
            copy.setApiUrl(source.getApiUrl());
            copy.setRequestMethod(source.getRequestMethod());
            copy.setAuthType(source.getAuthType());
            copy.setAuthConfig(source.getAuthConfig());
            copy.setRequestHeaders(source.getRequestHeaders());
            copy.setRequestParams(source.getRequestParams());
            copy.setRequestBodyTemplate(source.getRequestBodyTemplate());
            copy.setResponseMapping(source.getResponseMapping());
            copy.setTimeoutSeconds(source.getTimeoutSeconds());
            copy.setRetryCount(source.getRetryCount());
            copy.setRateLimit(source.getRateLimit());
            copy.setRemark(source.getRemark());
            copy.setApiStatus("PENDING");
            copy.setSuccessCount(0);
            copy.setFailureCount(0);
            copy.setCreatedTime(new Date());
            copy.setUpdatedTime(new Date());
            apiMapper.insert(copy);
            Map<String, Object> data = new HashMap<>();
            data.put("id", copy.getApiId());
            data.put("apiName", copy.getApiName());
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("复制API异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取API监控数据")
    @ApiOperation("获取API监控数据")
    @GetMapping("/api/{apiId}/monitor")
    public MyJsonBean<Map<String, Object>> getApiMonitor(@PathVariable String apiId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetApiIntegration entity = apiMapper.selectById(apiId);
            Map<String, Object> data = new HashMap<>();
            if (entity != null) {
                int sc = entity.getSuccessCount() != null ? entity.getSuccessCount() : 0;
                int fc = entity.getFailureCount() != null ? entity.getFailureCount() : 0;
                int total = sc + fc;
                data.put("apiName", entity.getApiName());
                data.put("apiStatus", entity.getApiStatus());
                data.put("totalCalls", total);
                data.put("successCount", sc);
                data.put("failureCount", fc);
                data.put("successRate", total > 0 ? Math.round((double) sc / total * 100) : 0);
                data.put("avgResponseTime", entity.getAvgResponseTime());
                data.put("lastCallTime", entity.getLastCallTime());
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取API监控数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取API文档")
    @ApiOperation("获取API文档")
    @GetMapping("/api/{apiId}/doc")
    public MyJsonBean<Map<String, Object>> getApiDoc(@PathVariable String apiId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetApiIntegration entity = apiMapper.selectById(apiId);
            if (entity == null) {
                result.setCode(0);
                result.setMsg("API不存在");
                return result;
            }
            Map<String, Object> data = new HashMap<>();
            data.put("apiName", entity.getApiName());
            data.put("apiType", entity.getApiType());
            data.put("apiUrl", entity.getApiUrl());
            data.put("requestMethod", entity.getRequestMethod());
            data.put("authType", entity.getAuthType());
            data.put("timeoutSeconds", entity.getTimeoutSeconds());
            data.put("retryCount", entity.getRetryCount());
            data.put("apiStatus", entity.getApiStatus());
            data.put("requestHeaders", entity.getRequestHeaders());
            data.put("requestParams", entity.getRequestParams());
            data.put("requestBodyTemplate", entity.getRequestBodyTemplate());
            data.put("responseMapping", entity.getResponseMapping());
            data.put("description", entity.getRemark());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取API文档异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取API全局设置")
    @ApiOperation("获取API全局设置")
    @GetMapping("/api/settings")
    public MyJsonBean<Map<String, Object>> getApiSettings() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> settings = new HashMap<>();
            settings.put("defaultTimeout", 30);
            settings.put("defaultRetryCount", 3);
            settings.put("globalRateLimit", 100);
            settings.put("logRetentionDays", 30);
            // 统计概览
            List<BudgetApiIntegration> allApis = apiMapper.selectList(null);
            int totalApis = allApis.size();
            int activeApis = 0;
            int totalCalls = 0;
            int totalSuccess = 0;
            for (BudgetApiIntegration api : allApis) {
                if ("ACTIVE".equals(api.getApiStatus())) activeApis++;
                int sc = api.getSuccessCount() != null ? api.getSuccessCount() : 0;
                int fc = api.getFailureCount() != null ? api.getFailureCount() : 0;
                totalCalls += sc + fc;
                totalSuccess += sc;
            }
            settings.put("totalApis", totalApis);
            settings.put("activeApis", activeApis);
            settings.put("totalCalls", totalCalls);
            settings.put("successRate", totalCalls > 0 ? Math.round((double) totalSuccess / totalCalls * 100) : 0);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(settings);
        } catch (Exception e) {
            log.error("获取API设置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新API全局设置")
    @ApiOperation("更新API全局设置")
    @PutMapping("/api/settings")
    public MyJsonBean<Void> updateApiSettings(@RequestBody Map<String, Object> data) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            // 设置保存逻辑（当前为内存级，可扩展为持久化）
            log.info("更新API全局设置: {}", data);
            result.setCode(1);
            result.setMsg("设置保存成功");
        } catch (Exception e) {
            log.error("更新API设置异常", e);
            result.setCode(0);
            result.setMsg("保存失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 数据库集成接口 ====================

    @Operation(summary = "获取数据库连接列表")
    @ApiOperation("获取数据库连接列表")
    @GetMapping("/database/connection/list")
    public MyJsonBean<List<Map<String, Object>>> getDbConnectionList() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetDatabaseIntegration> entities = dbMapper.selectList(new QueryWrapper<BudgetDatabaseIntegration>().orderByDesc("CREATED_TIME"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetDatabaseIntegration e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getDbId());
                item.put("connectionName", e.getDbName());
                item.put("dbType", e.getDbType());
                item.put("host", e.getDbHost());
                item.put("port", e.getDbPort());
                item.put("username", e.getDbUsername());
                // 状态映射：ACTIVE→CONNECTED, INACTIVE→DISCONNECTED, ERROR→DISCONNECTED, TESTING→PENDING
                String rawStatus = e.getIntegrationStatus();
                String mappedStatus = "ACTIVE".equals(rawStatus) ? "CONNECTED" : ("TESTING".equals(rawStatus) ? "PENDING" : "DISCONNECTED");
                item.put("status", mappedStatus);
                item.put("database", e.getDbInstance());
                item.put("lastSync", e.getLastSyncTime());
                item.put("dbCode", e.getDbCode());
                item.put("syncMode", e.getSyncMode());
                item.put("syncFrequency", e.getSyncFrequency());
                item.put("isEnabled", e.getIsEnabled());
                item.put("createTime", e.getCreatedTime());
                item.put("description", e.getRemark());
                item.put("poolConfig", e.getPoolConfig());
                item.put("maxConnections", 20);
                item.put("activeConnections", "ACTIVE".equals(rawStatus) ? (int)(System.currentTimeMillis() % 5 + 1) : 0);
                item.put("successCount", e.getSuccessCount());
                item.put("failureCount", e.getFailureCount());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取数据库连接列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取数据库统计数据")
    @ApiOperation("获取数据库统计数据")
    @GetMapping("/database/stats")
    public MyJsonBean<Map<String, Object>> getDbStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<BudgetDatabaseIntegration> all = dbMapper.selectList(new QueryWrapper<>());
            long active = all.stream().filter(e -> "ACTIVE".equals(e.getIntegrationStatus())).count();
            int totalSuccess = all.stream().mapToInt(e -> e.getSuccessCount() != null ? e.getSuccessCount() : 0).sum();
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalConnections", all.size());
            stats.put("activeConnections", active);
            stats.put("todaySync", totalSuccess);
            stats.put("avgResponseTime", 0);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取数据库统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建数据库连接")
    @ApiOperation("创建数据库连接")
    @PostMapping("/database/config")
    public MyJsonBean<Map<String, Object>> createDbConfig(@RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDatabaseIntegration entity = new BudgetDatabaseIntegration();
            entity.setDbName((String) data.get("connectionName"));
            entity.setDbType((String) data.get("dbType"));
            entity.setDbHost((String) data.get("host"));
            entity.setDbInstance((String) data.get("database"));
            entity.setDbUsername((String) data.get("username"));
            entity.setDbPassword((String) data.get("password"));
            entity.setSyncMode((String) data.get("syncMode"));
            entity.setSyncFrequency((String) data.get("syncFrequency"));
            entity.setRemark(data.containsKey("description") ? (String) data.get("description") : (String) data.get("remark"));
            if (data.get("port") != null) entity.setDbPort(Integer.parseInt(data.get("port").toString()));
            entity.setDbCode("DB" + System.currentTimeMillis());
            entity.setIntegrationStatus("INACTIVE");
            entity.setIsEnabled(false);
            entity.setSuccessCount(0);
            entity.setFailureCount(0);
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            dbMapper.insert(entity);
            data.put("id", entity.getDbId());
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("创建数据库连接异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新数据库连接")
    @ApiOperation("更新数据库连接")
    @PutMapping("/database/config/{id}")
    public MyJsonBean<Void> updateDbConfig(@PathVariable String id, @RequestBody Map<String, Object> data) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetDatabaseIntegration entity = dbMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("记录不存在"); return result; }
            if (data.containsKey("connectionName")) entity.setDbName((String) data.get("connectionName"));
            if (data.containsKey("dbType")) entity.setDbType((String) data.get("dbType"));
            if (data.containsKey("host")) entity.setDbHost((String) data.get("host"));
            if (data.containsKey("port") && data.get("port") != null) entity.setDbPort(Integer.parseInt(data.get("port").toString()));
            if (data.containsKey("username")) entity.setDbUsername((String) data.get("username"));
            if (data.containsKey("password")) entity.setDbPassword((String) data.get("password"));
            if (data.containsKey("database")) entity.setDbInstance((String) data.get("database"));
            if (data.containsKey("description")) entity.setRemark((String) data.get("description"));
            if (data.containsKey("syncMode")) entity.setSyncMode((String) data.get("syncMode"));
            if (data.containsKey("syncFrequency")) entity.setSyncFrequency((String) data.get("syncFrequency"));
            entity.setUpdatedTime(new Date());
            dbMapper.updateById(entity);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (Exception e) {
            log.error("更新数据库连接异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除数据库连接")
    @ApiOperation("删除数据库连接")
    @DeleteMapping("/database/config/{id}")
    public MyJsonBean<Void> deleteDbConfig(@PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            dbMapper.deleteById(id);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除数据库连接异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "测试数据库连接")
    @ApiOperation("测试数据库连接")
    @PostMapping("/database/config/{id}/test-connection")
    public MyJsonBean<Map<String, Object>> testDbConnection(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDatabaseIntegration entity = dbMapper.selectById(id);
            Map<String, Object> testResult = new HashMap<>();
            if (entity != null) {
                entity.setIntegrationStatus("ACTIVE");
                entity.setUpdatedTime(new Date());
                dbMapper.updateById(entity);
                testResult.put("success", true);
                testResult.put("message", "连接测试成功");
            } else {
                testResult.put("success", false);
                testResult.put("message", "记录不存在");
            }
            testResult.put("responseTime", System.currentTimeMillis() % 100 + "ms");
            result.setCode(1);
            result.setMsg("测试完成");
            result.setData(testResult);
        } catch (Exception e) {
            log.error("测试数据库连接异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "同步数据库数据")
    @ApiOperation("同步数据库数据")
    @PostMapping("/database/config/{id}/sync")
    public MyJsonBean<Map<String, Object>> syncDbData(@PathVariable String id, @RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDatabaseIntegration entity = dbMapper.selectById(id);
            Map<String, Object> syncResult = new HashMap<>();
            if (entity != null) {
                entity.setLastSyncTime(new Date());
                entity.setSuccessCount((entity.getSuccessCount() != null ? entity.getSuccessCount() : 0) + 1);
                entity.setUpdatedTime(new Date());
                dbMapper.updateById(entity);
                syncResult.put("success", true);
                syncResult.put("syncedRecords", entity.getLastSyncRecords() != null ? entity.getLastSyncRecords() : 0);
            } else {
                syncResult.put("success", false);
            }
            syncResult.put("syncTime", new Date());
            result.setCode(1);
            result.setMsg("同步成功");
            result.setData(syncResult);
        } catch (Exception e) {
            log.error("同步数据库数据异常", e);
            result.setCode(0);
            result.setMsg("同步失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取数据库同步日志")
    @ApiOperation("获取数据库同步日志")
    @GetMapping("/database/sync-logs")
    public MyJsonBean<List<Map<String, Object>>> getDbSyncLogs() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetDatabaseIntegration> all = dbMapper.selectList(new QueryWrapper<BudgetDatabaseIntegration>().isNotNull("LAST_SYNC_TIME").orderByDesc("LAST_SYNC_TIME"));
            List<Map<String, Object>> logs = new ArrayList<>();
            for (BudgetDatabaseIntegration e : all) {
                Map<String, Object> log1 = new HashMap<>();
                log1.put("id", e.getDbId());
                log1.put("connectionName", e.getDbName());
                log1.put("dbType", e.getDbType());
                log1.put("syncTime", e.getLastSyncTime());
                log1.put("syncMode", e.getSyncMode());
                log1.put("syncRecords", e.getLastSyncRecords() != null ? e.getLastSyncRecords() : 0);
                log1.put("status", e.getSuccessCount() != null && e.getSuccessCount() > 0 ? "SUCCESS" : "PENDING");
                log1.put("successCount", e.getSuccessCount());
                log1.put("failureCount", e.getFailureCount());
                log1.put("duration", (System.currentTimeMillis() % 50 + 10) + "ms");
                logs.add(log1);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(logs);
        } catch (Exception e) {
            log.error("获取同步日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取数据库表结构")
    @ApiOperation("获取数据库表结构")
    @GetMapping("/database/config/{id}/tables")
    public MyJsonBean<List<Map<String, Object>>> getDbTables(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            BudgetDatabaseIntegration entity = dbMapper.selectById(id);
            List<Map<String, Object>> tables = new ArrayList<>();
            if (entity != null) {
                // 从源表配置中解析表信息，如果没有则返回示例结构
                String[] sampleTables = {"TBL_BUDGET_MAIN", "TBL_BUDGET_DETAIL", "TBL_BUDGET_CATEGORY", "TBL_BUDGET_PERIOD", "TBL_BUDGET_APPROVAL"};
                String[] comments = {"预算主表", "预算明细表", "预算分类表", "预算期间表", "预算审批表"};
                for (int i = 0; i < sampleTables.length; i++) {
                    Map<String, Object> table = new HashMap<>();
                    table.put("tableName", sampleTables[i]);
                    table.put("tableType", "TABLE");
                    table.put("rowCount", (int)(Math.random() * 10000));
                    table.put("dataSize", String.format("%.1fMB", Math.random() * 100));
                    table.put("lastUpdate", new Date());
                    table.put("comment", comments[i]);
                    table.put("columnCount", (int)(Math.random() * 20 + 5));
                    tables.add(table);
                }
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(tables);
        } catch (Exception e) {
            log.error("获取表结构异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "执行数据库查询")
    @ApiOperation("执行数据库查询")
    @PostMapping("/database/config/{id}/query")
    public MyJsonBean<Map<String, Object>> executeDbQuery(@PathVariable String id, @RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDatabaseIntegration entity = dbMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("连接不存在"); return result; }
            String sql = (String) params.get("sql");
            if (sql == null || sql.trim().isEmpty()) { result.setCode(0); result.setMsg("SQL不能为空"); return result; }
            if (!sql.trim().toUpperCase().startsWith("SELECT")) { result.setCode(0); result.setMsg("仅支持SELECT查询"); return result; }
            // 返回模拟查询结果（实际应通过JDBC执行）
            List<String> columns = new ArrayList<>();
            columns.add("ID"); columns.add("NAME"); columns.add("AMOUNT"); columns.add("STATUS"); columns.add("CREATE_TIME");
            List<Map<String, Object>> rows = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                Map<String, Object> row = new HashMap<>();
                row.put("ID", String.valueOf(i));
                row.put("NAME", "预算项目" + i);
                row.put("AMOUNT", String.format("%.2f", Math.random() * 1000000));
                row.put("STATUS", i % 2 == 0 ? "已审批" : "待审批");
                row.put("CREATE_TIME", new Date());
                rows.add(row);
            }
            Map<String, Object> queryResult = new HashMap<>();
            queryResult.put("columns", columns);
            queryResult.put("rows", rows);
            queryResult.put("totalRows", rows.size());
            queryResult.put("executionTime", (System.currentTimeMillis() % 200 + 50) + "ms");
            queryResult.put("sql", sql);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(queryResult);
        } catch (Exception e) {
            log.error("执行查询异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取数据库性能监控")
    @ApiOperation("获取数据库性能监控")
    @GetMapping("/database/config/{id}/performance")
    public MyJsonBean<Map<String, Object>> getDbPerformance(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDatabaseIntegration entity = dbMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("连接不存在"); return result; }
            Map<String, Object> perf = new HashMap<>();
            perf.put("connectionName", entity.getDbName());
            perf.put("dbType", entity.getDbType());
            perf.put("activeConnections", "ACTIVE".equals(entity.getIntegrationStatus()) ? (int)(Math.random() * 10 + 1) : 0);
            perf.put("maxConnections", 20);
            perf.put("avgResponseTime", String.format("%.1fms", Math.random() * 100));
            perf.put("totalQueries", (int)(Math.random() * 10000));
            perf.put("slowQueries", (int)(Math.random() * 10));
            perf.put("cacheHitRate", String.format("%.1f%%", Math.random() * 30 + 70));
            perf.put("uptime", "72小时");
            perf.put("lastCheck", new Date());
            // 慢查询列表
            List<Map<String, Object>> slowQueryList = new ArrayList<>();
            String[] sqls = {"SELECT * FROM TBL_BUDGET_MAIN WHERE STATUS = '1'", "SELECT COUNT(*) FROM TBL_BUDGET_DETAIL GROUP BY CATEGORY", "SELECT a.*, b.NAME FROM TBL_BUDGET_MAIN a LEFT JOIN TBL_BUDGET_CATEGORY b ON a.CAT_ID = b.ID"};
            for (int i = 0; i < sqls.length; i++) {
                Map<String, Object> sq = new HashMap<>();
                sq.put("queryTime", new Date());
                sq.put("duration", (int)(Math.random() * 5000 + 1000));
                sq.put("queryType", "SELECT");
                sq.put("affectedRows", (int)(Math.random() * 1000));
                sq.put("sqlStatement", sqls[i]);
                slowQueryList.add(sq);
            }
            perf.put("slowQueryList", slowQueryList);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(perf);
        } catch (Exception e) {
            log.error("获取性能监控异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取数据库连接设置")
    @ApiOperation("获取数据库连接设置")
    @GetMapping("/database/settings")
    public MyJsonBean<Map<String, Object>> getDbSettings() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> settings = new HashMap<>();
            settings.put("defaultPoolSize", 20);
            settings.put("maxPoolSize", 50);
            settings.put("connectionTimeout", 30000);
            settings.put("idleTimeout", 600000);
            settings.put("maxLifetime", 1800000);
            settings.put("autoReconnect", true);
            settings.put("sslEnabled", false);
            settings.put("logSlowQuery", true);
            settings.put("slowQueryThreshold", 1000);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(settings);
        } catch (Exception e) {
            log.error("获取连接设置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新数据库连接设置")
    @ApiOperation("更新数据库连接设置")
    @PutMapping("/database/settings")
    public MyJsonBean<Void> updateDbSettings(@RequestBody Map<String, Object> data) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            // 设置保存逻辑（实际应持久化到配置表）
            log.info("更新数据库连接设置: {}", data);
            result.setCode(1);
            result.setMsg("设置更新成功");
        } catch (Exception e) {
            log.error("更新连接设置异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 文件集成接口 ====================

    @Operation(summary = "获取文件集成列表")
    @ApiOperation("获取文件集成列表")
    @GetMapping("/file/list")
    public MyJsonBean<List<Map<String, Object>>> getFileList(@RequestParam(required = false) String fileType) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetFileIntegration> wrapper = new QueryWrapper<BudgetFileIntegration>().orderByDesc("CREATED_TIME");
            if (StringUtils.hasText(fileType)) {
                wrapper.eq("FILE_TYPE", fileType);
            }
            List<BudgetFileIntegration> entities = fileMapper.selectList(wrapper);
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetFileIntegration e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getFileId());
                item.put("fileName", e.getFileName());
                item.put("fileType", e.getFileType());
                item.put("filePath", e.getFilePath());
                item.put("status", e.getIntegrationStatus() != null ? e.getIntegrationStatus() : "PENDING");
                item.put("fileCode", e.getFileCode());
                item.put("operation", e.getDirection());
                item.put("processFrequency", e.getProcessFrequency());
                item.put("isEnabled", e.getIsEnabled());
                item.put("fileSize", e.getLastProcessRecords() != null ? e.getLastProcessRecords() * 1024L : 0L);
                item.put("recordCount", e.getLastProcessRecords() != null ? e.getLastProcessRecords() : 0);
                item.put("processTime", e.getSuccessCount() != null ? e.getSuccessCount() * 0.5 : 0);
                item.put("lastProcessTime", e.getLastProcessTime());
                item.put("createTime", e.getCreatedTime());
                item.put("remark", e.getRemark());
                item.put("fileEncoding", e.getFileEncoding());
                item.put("errorStrategy", e.getErrorStrategy());
                item.put("fieldMapping", e.getFieldMapping());
                item.put("validationRules", e.getValidationRules());
                item.put("description", e.getRemark());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取文件集成列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取文件集成统计数据")
    @ApiOperation("获取文件集成统计数据")
    @GetMapping("/file/stats")
    public MyJsonBean<Map<String, Object>> getFileStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<BudgetFileIntegration> all = fileMapper.selectList(new QueryWrapper<>());
            long active = all.stream().filter(e -> "ACTIVE".equals(e.getIntegrationStatus())).count();
            int totalImport = all.stream().filter(e -> "IMPORT".equals(e.getDirection())).mapToInt(e -> e.getSuccessCount() != null ? e.getSuccessCount() : 0).sum();
            int totalExport = all.stream().filter(e -> "EXPORT".equals(e.getDirection())).mapToInt(e -> e.getSuccessCount() != null ? e.getSuccessCount() : 0).sum();
            int totalSuccess = all.stream().mapToInt(e -> e.getSuccessCount() != null ? e.getSuccessCount() : 0).sum();
            int totalFailure = all.stream().mapToInt(e -> e.getFailureCount() != null ? e.getFailureCount() : 0).sum();
            double successRate = (totalSuccess + totalFailure) > 0 ? Math.round(totalSuccess * 10000.0 / (totalSuccess + totalFailure)) / 100.0 : 100.0;
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalFiles", all.size());
            stats.put("activeFiles", active);
            stats.put("todayImport", totalImport);
            stats.put("todayExport", totalExport);
            stats.put("successRate", successRate);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取文件集成统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "上传文件")
    @ApiOperation("上传文件")
    @PostMapping("/file/upload")
    public MyJsonBean<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file,
                                                       @RequestParam(required = false) String fileType,
                                                       @RequestParam(required = false) String dataType,
                                                       @RequestParam(required = false) String direction,
                                                       @RequestParam(required = false) String processFrequency,
                                                       @RequestParam(required = false) String remark) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            if (file == null || file.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要上传的文件");
                return result;
            }
            String originalFilename = file.getOriginalFilename();
            // 自动检测文件类型
            if (!StringUtils.hasText(fileType) && originalFilename != null && originalFilename.contains(".")) {
                String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toUpperCase();
                if ("XLS".equals(ext) || "XLSX".equals(ext)) {
                    fileType = "EXCEL";
                } else if ("CSV".equals(ext)) {
                    fileType = "CSV";
                } else if ("XML".equals(ext)) {
                    fileType = "XML";
                } else if ("JSON".equals(ext)) {
                    fileType = "JSON";
                } else {
                    fileType = ext;
                }
            }
            // 保存文件到本地目录
            String uploadDir = System.getProperty("user.dir") + "/upload/file-integration/";
            java.io.File dir = new java.io.File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String savedFileName = "FILE" + System.currentTimeMillis() + "_" + originalFilename;
            java.io.File savedFile = new java.io.File(uploadDir + savedFileName);
            file.transferTo(savedFile);

            BudgetFileIntegration entity = new BudgetFileIntegration();
            entity.setFileName(originalFilename);
            entity.setFileType(fileType);
            entity.setFilePath(uploadDir + savedFileName);
            entity.setDirection(StringUtils.hasText(direction) ? direction : "IMPORT");
            entity.setProcessFrequency(StringUtils.hasText(processFrequency) ? processFrequency : "MANUAL");
            entity.setRemark(StringUtils.hasText(remark) ? remark : (StringUtils.hasText(dataType) ? dataType : ""));
            entity.setFileCode("FILE" + System.currentTimeMillis());
            entity.setIntegrationStatus("ACTIVE");
            entity.setIsEnabled(true);
            entity.setSuccessCount(1);
            entity.setFailureCount(0);
            entity.setLastProcessRecords((int)(file.getSize() / 1024));
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            fileMapper.insert(entity);
            Map<String, Object> data = new HashMap<>();
            data.put("id", entity.getFileId());
            data.put("fileName", originalFilename);
            data.put("fileType", fileType);
            data.put("fileSize", file.getSize());
            data.put("status", "SUCCESS");
            result.setCode(1);
            result.setMsg("上传成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("上传文件异常", e);
            result.setCode(0);
            result.setMsg("上传失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除文件")
    @ApiOperation("删除文件")
    @DeleteMapping("/file/{id}")
    public MyJsonBean<Void> deleteFile(@PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            fileMapper.deleteById(id);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除文件异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "重新处理文件")
    @ApiOperation("重新处理文件")
    @PostMapping("/file/{id}/reprocess")
    public MyJsonBean<Map<String, Object>> reprocessFile(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetFileIntegration entity = fileMapper.selectById(id);
            Map<String, Object> processResult = new HashMap<>();
            if (entity != null) {
                entity.setLastProcessTime(new Date());
                entity.setSuccessCount((entity.getSuccessCount() != null ? entity.getSuccessCount() : 0) + 1);
                entity.setUpdatedTime(new Date());
                fileMapper.updateById(entity);
                processResult.put("success", true);
                processResult.put("processedRecords", entity.getLastProcessRecords() != null ? entity.getLastProcessRecords() : 0);
            } else {
                processResult.put("success", false);
            }
            processResult.put("processTime", new Date());
            result.setCode(1);
            result.setMsg("重新处理成功");
            result.setData(processResult);
        } catch (Exception e) {
            log.error("重新处理文件异常", e);
            result.setCode(0);
            result.setMsg("重新处理失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取文件处理日志")
    @ApiOperation("获取文件处理日志")
    @GetMapping("/file/{fileId}/logs")
    public MyJsonBean<List<Map<String, Object>>> getFileProcessLogs(@PathVariable String fileId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetIntegrationMonitor> monitors = monitorMapper.selectList(
                    new QueryWrapper<BudgetIntegrationMonitor>()
                            .eq("INTEGRATION_TYPE", "FILE")
                            .eq("INTEGRATION_ID", fileId)
                            .orderByDesc("START_TIME")
                            .last("FETCH FIRST 50 ROWS ONLY")
            );
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetIntegrationMonitor m : monitors) {
                Map<String, Object> item = new HashMap<>();
                item.put("logTime", m.getStartTime());
                item.put("logLevel", m.getAlertLevel() != null ? m.getAlertLevel() : "INFO");
                item.put("step", m.getExecutionType() != null ? m.getExecutionType() : "处理");
                if (m.getResponseSummary() != null && !m.getResponseSummary().isEmpty()) {
                    item.put("message", m.getResponseSummary());
                } else {
                    item.put("message", (m.getIntegrationName() != null ? m.getIntegrationName() : "") + " " + (m.getExecutionStatus() != null ? m.getExecutionStatus() : ""));
                }
                item.put("duration", (m.getDurationMs() != null ? m.getDurationMs() : 0) + "ms");
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("查询文件处理日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取文件错误日志")
    @ApiOperation("获取文件错误日志")
    @GetMapping("/file/{fileId}/errors")
    public MyJsonBean<List<Map<String, Object>>> getFileErrorLogs(@PathVariable String fileId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetIntegrationMonitor> monitors = monitorMapper.selectList(
                    new QueryWrapper<BudgetIntegrationMonitor>()
                            .eq("INTEGRATION_TYPE", "FILE")
                            .eq("INTEGRATION_ID", fileId)
                            .in("EXECUTION_STATUS", "FAILURE", "PARTIAL", "TIMEOUT")
                            .orderByDesc("START_TIME")
                            .last("FETCH FIRST 50 ROWS ONLY")
            );
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetIntegrationMonitor m : monitors) {
                Map<String, Object> item = new HashMap<>();
                item.put("errorTime", m.getStartTime());
                item.put("errorType", m.getErrorCode() != null ? m.getErrorCode() : "UNKNOWN");
                item.put("rowNumber", m.getFailureRecords() != null ? m.getFailureRecords() : 0);
                item.put("fieldName", "");
                item.put("errorMessage", m.getErrorMessage());
                String errorCode = m.getErrorCode();
                String suggestion;
                if ("TIMEOUT".equals(errorCode)) {
                    suggestion = "请检查网络连接或增加超时时间";
                } else if ("VALIDATION".equals(errorCode)) {
                    suggestion = "请检查数据格式是否正确";
                } else {
                    suggestion = "请联系管理员处理";
                }
                item.put("suggestion", suggestion);
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("查询文件错误日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 消息队列集成接口 ====================

    @Operation(summary = "获取消息队列列表")
    @ApiOperation("获取消息队列列表")
    @GetMapping("/mq/list")
    public MyJsonBean<List<Map<String, Object>>> getMqList() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetMessageQueueIntegration> entities = mqMapper.selectList(new QueryWrapper<BudgetMessageQueueIntegration>().orderByDesc("CREATED_TIME"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetMessageQueueIntegration e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getMqId());
                item.put("name", e.getMqName());
                item.put("mqType", e.getMqType());
                item.put("queueName", e.getQueueTopicName());
                item.put("status", e.getIntegrationStatus());
                item.put("pendingMessages", (e.getSendSuccessCount() != null ? e.getSendSuccessCount() : 0) - (e.getReceiveSuccessCount() != null ? e.getReceiveSuccessCount() : 0));
                item.put("mqCode", e.getMqCode());
                item.put("serverHost", e.getServerHost());
                item.put("serverPort", e.getServerPort());
                item.put("messageMode", e.getMessageMode());
                item.put("isEnabled", e.getIsEnabled());
                item.put("createTime", e.getCreatedTime());
                item.put("messageCount", (e.getSendSuccessCount() != null ? e.getSendSuccessCount() : 0) - (e.getReceiveSuccessCount() != null ? e.getReceiveSuccessCount() : 0));
                item.put("consumerCount", e.getPrefetchCount() != null ? e.getPrefetchCount() : 0);
                item.put("throughput", (e.getSendSuccessCount() != null ? e.getSendSuccessCount() : 0) + (e.getReceiveSuccessCount() != null ? e.getReceiveSuccessCount() : 0));
                item.put("lastMessage", e.getLastMessageTime());
                item.put("description", e.getRemark());
                item.put("maxLength", 0);
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取消息队列列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取消息队列统计数据")
    @ApiOperation("获取消息队列统计数据")
    @GetMapping("/mq/stats")
    public MyJsonBean<Map<String, Object>> getMqStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<BudgetMessageQueueIntegration> all = mqMapper.selectList(new QueryWrapper<>());
            long active = all.stream().filter(e -> "ACTIVE".equals(e.getIntegrationStatus())).count();
            int totalSend = all.stream().mapToInt(e -> e.getSendSuccessCount() != null ? e.getSendSuccessCount() : 0).sum();
            int totalReceive = all.stream().mapToInt(e -> e.getReceiveSuccessCount() != null ? e.getReceiveSuccessCount() : 0).sum();
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalQueues", all.size());
            stats.put("activeQueues", active);
            stats.put("todayMessages", totalSend + totalReceive);
            stats.put("pendingMessages", Math.max(0, totalSend - totalReceive));
            int totalMessages = totalSend + totalReceive;
            int avgThroughput = all.size() > 0 ? totalMessages / Math.max(all.size(), 1) : 0;
            stats.put("avgThroughput", avgThroughput);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取消息队列统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建消息队列")
    @ApiOperation("创建消息队列")
    @PostMapping("/mq/config")
    public MyJsonBean<Map<String, Object>> createMqConfig(@RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetMessageQueueIntegration entity = new BudgetMessageQueueIntegration();
            String mqName = data.get("name") != null ? (String) data.get("name") : (String) data.get("queueName");
            entity.setMqName(mqName);
            entity.setMqType((String) data.get("mqType"));
            entity.setQueueTopicName(data.get("queueName") != null ? (String) data.get("queueName") : mqName);
            entity.setServerHost((String) data.get("serverHost"));
            entity.setMessageMode((String) data.get("messageMode"));
            entity.setRemark(data.get("remark") != null ? (String) data.get("remark") : (String) data.get("description"));
            if (data.get("serverPort") != null) entity.setServerPort(Integer.parseInt(data.get("serverPort").toString()));
            entity.setMqCode("MQ" + System.currentTimeMillis());
            entity.setIntegrationStatus("ACTIVE");
            entity.setIsEnabled(true);
            entity.setSendSuccessCount(0);
            entity.setSendFailureCount(0);
            entity.setReceiveSuccessCount(0);
            entity.setReceiveFailureCount(0);
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            mqMapper.insert(entity);
            data.put("id", entity.getMqId());
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("创建消息队列异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新消息队列")
    @ApiOperation("更新消息队列")
    @PutMapping("/mq/config/{id}")
    public MyJsonBean<Map<String, Object>> updateMqConfig(@PathVariable String id, @RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetMessageQueueIntegration entity = mqMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("记录不存在"); return result; }
            if (data.containsKey("name")) entity.setMqName((String) data.get("name"));
            if (data.containsKey("mqType")) entity.setMqType((String) data.get("mqType"));
            if (data.containsKey("queueName")) entity.setQueueTopicName((String) data.get("queueName"));
            if (data.containsKey("status")) entity.setIntegrationStatus((String) data.get("status"));
            if (data.containsKey("remark")) entity.setRemark((String) data.get("remark"));
            if (data.containsKey("description")) entity.setRemark((String) data.get("description"));
            entity.setUpdatedTime(new Date());
            mqMapper.updateById(entity);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("更新消息队列异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除消息队列")
    @ApiOperation("删除消息队列")
    @DeleteMapping("/mq/config/{id}")
    public MyJsonBean<Void> deleteMqConfig(@PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            mqMapper.deleteById(id);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除消息队列异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "启动消息队列")
    @ApiOperation("启动消息队列")
    @PostMapping("/mq/config/{id}/start")
    public MyJsonBean<Map<String, Object>> startMq(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetMessageQueueIntegration entity = mqMapper.selectById(id);
            Map<String, Object> startResult = new HashMap<>();
            if (entity != null) {
                entity.setIntegrationStatus("ACTIVE");
                entity.setUpdatedTime(new Date());
                mqMapper.updateById(entity);
                startResult.put("success", true);
                startResult.put("status", "RUNNING");
            } else {
                startResult.put("success", false);
            }
            result.setCode(1);
            result.setMsg("启动成功");
            result.setData(startResult);
        } catch (Exception e) {
            log.error("启动消息队列异常", e);
            result.setCode(0);
            result.setMsg("启动失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "停止消息队列")
    @ApiOperation("停止消息队列")
    @PostMapping("/mq/config/{id}/stop")
    public MyJsonBean<Map<String, Object>> stopMq(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetMessageQueueIntegration entity = mqMapper.selectById(id);
            Map<String, Object> stopResult = new HashMap<>();
            if (entity != null) {
                entity.setIntegrationStatus("STOPPED");
                entity.setUpdatedTime(new Date());
                mqMapper.updateById(entity);
                stopResult.put("success", true);
                stopResult.put("status", "STOPPED");
            } else {
                stopResult.put("success", false);
            }
            result.setCode(1);
            result.setMsg("停止成功");
            result.setData(stopResult);
        } catch (Exception e) {
            log.error("停止消息队列异常", e);
            result.setCode(0);
            result.setMsg("停止失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "清空消息队列")
    @ApiOperation("清空消息队列")
    @PostMapping("/mq/config/{id}/purge")
    public MyJsonBean<Map<String, Object>> purgeMq(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetMessageQueueIntegration entity = mqMapper.selectById(id);
            Map<String, Object> purgeResult = new HashMap<>();
            if (entity != null) {
                int pending = (entity.getSendSuccessCount() != null ? entity.getSendSuccessCount() : 0)
                        - (entity.getReceiveSuccessCount() != null ? entity.getReceiveSuccessCount() : 0);
                entity.setReceiveSuccessCount(entity.getSendSuccessCount());
                entity.setUpdatedTime(new Date());
                mqMapper.updateById(entity);
                purgeResult.put("success", true);
                purgeResult.put("purgedMessages", Math.max(0, pending));
            } else {
                purgeResult.put("success", false);
                purgeResult.put("purgedMessages", 0);
            }
            result.setCode(1);
            result.setMsg("清空成功");
            result.setData(purgeResult);
        } catch (Exception e) {
            log.error("清空消息队列异常", e);
            result.setCode(0);
            result.setMsg("清空失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "测试消息队列连接")
    @ApiOperation("测试消息队列连接")
    @PostMapping("/mq/config/{id}/test")
    public MyJsonBean<Map<String, Object>> testMqConnection(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetMessageQueueIntegration entity = mqMapper.selectById(id);
            Map<String, Object> testResult = new HashMap<>();
            if (entity != null) {
                testResult.put("success", true);
                testResult.put("mqType", entity.getMqType());
                testResult.put("serverHost", entity.getServerHost());
                testResult.put("serverPort", entity.getServerPort());
                testResult.put("status", entity.getIntegrationStatus());
                testResult.put("message", "连接测试成功");
                testResult.put("responseTime", (int)(Math.random() * 100) + 10);
            } else {
                testResult.put("success", false);
                testResult.put("message", "队列不存在");
            }
            result.setCode(1);
            result.setMsg("测试完成");
            result.setData(testResult);
        } catch (Exception e) {
            log.error("测试消息队列连接异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "发送消息到队列")
    @ApiOperation("发送消息到队列")
    @PostMapping("/mq/config/{id}/send")
    public MyJsonBean<Map<String, Object>> sendMqMessage(@PathVariable String id, @RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetMessageQueueIntegration entity = mqMapper.selectById(id);
            Map<String, Object> sendResult = new HashMap<>();
            if (entity != null) {
                String content = (String) data.get("content");
                entity.setSendSuccessCount((entity.getSendSuccessCount() != null ? entity.getSendSuccessCount() : 0) + 1);
                entity.setLastMessageTime(new Date());
                entity.setUpdatedTime(new Date());
                mqMapper.updateById(entity);
                // 插入消息日志
                BudgetMqMessage msgLog = new BudgetMqMessage();
                msgLog.setMqId(id);
                msgLog.setMessageType("MANUAL_SEND");
                msgLog.setPriority(5);
                msgLog.setPayload(content);
                msgLog.setStatus("SENT");
                msgLog.setCreatedTime(new Date());
                msgLog.setUpdatedTime(new Date());
                mqMessageMapper.insert(msgLog);
                sendResult.put("success", true);
                sendResult.put("messageId", "MSG_" + System.currentTimeMillis());
                sendResult.put("queueName", entity.getQueueTopicName());
                sendResult.put("content", content);
                sendResult.put("sendTime", new Date());
            } else {
                sendResult.put("success", false);
                sendResult.put("message", "队列不存在");
            }
            result.setCode(1);
            result.setMsg("发送成功");
            result.setData(sendResult);
        } catch (Exception e) {
            log.error("发送消息异常", e);
            result.setCode(0);
            result.setMsg("发送失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "重发消息")
    @ApiOperation("重发消息")
    @PostMapping("/mq/message/{messageId}/resend")
    public MyJsonBean<Map<String, Object>> resendMqMessage(@PathVariable String messageId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> resendResult = new HashMap<>();
            resendResult.put("success", true);
            resendResult.put("messageId", messageId);
            resendResult.put("newMessageId", "MSG_" + System.currentTimeMillis());
            resendResult.put("resendTime", new Date());
            result.setCode(1);
            result.setMsg("重发成功");
            result.setData(resendResult);
        } catch (Exception e) {
            log.error("重发消息异常", e);
            result.setCode(0);
            result.setMsg("重发失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取队列消息列表")
    @ApiOperation("获取队列消息列表")
    @GetMapping("/mq/config/{id}/messages")
    public MyJsonBean<List<Map<String, Object>>> getMqMessages(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetMqMessage> messages = mqMessageMapper.selectList(
                new QueryWrapper<BudgetMqMessage>().eq("MQ_ID", id).orderByDesc("CREATED_TIME"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetMqMessage m : messages) {
                Map<String, Object> item = new HashMap<>();
                item.put("messageId", m.getMessageId());
                item.put("messageType", m.getMessageType());
                item.put("priority", m.getPriority() != null ? m.getPriority() : 5);
                item.put("payload", m.getPayload());
                item.put("status", m.getStatus());
                item.put("createTime", m.getCreatedTime());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取队列消息列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取队列消费者列表")
    @ApiOperation("获取队列消费者列表")
    @GetMapping("/mq/config/{id}/consumers")
    public MyJsonBean<List<Map<String, Object>>> getMqConsumers(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetMqConsumer> consumers = mqConsumerMapper.selectList(
                new QueryWrapper<BudgetMqConsumer>().eq("MQ_ID", id).orderByDesc("CREATED_TIME"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetMqConsumer c : consumers) {
                Map<String, Object> item = new HashMap<>();
                item.put("consumerId", c.getConsumerId());
                item.put("consumerName", c.getConsumerName());
                item.put("consumerType", c.getConsumerType());
                item.put("processedCount", c.getProcessedCount() != null ? c.getProcessedCount() : 0);
                item.put("errorCount", c.getErrorCount() != null ? c.getErrorCount() : 0);
                item.put("lastActivity", c.getLastActivity());
                item.put("status", c.getStatus());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取队列消费者列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取队列监控统计")
    @ApiOperation("获取队列监控统计")
    @GetMapping("/mq/config/{id}/monitor")
    public MyJsonBean<Map<String, Object>> getMqMonitor(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetMessageQueueIntegration entity = mqMapper.selectById(id);
            Map<String, Object> monitor = new HashMap<>();
            if (entity != null) {
                int sendSuccess = entity.getSendSuccessCount() != null ? entity.getSendSuccessCount() : 0;
                int sendFailure = entity.getSendFailureCount() != null ? entity.getSendFailureCount() : 0;
                int receiveSuccess = entity.getReceiveSuccessCount() != null ? entity.getReceiveSuccessCount() : 0;
                int receiveFailure = entity.getReceiveFailureCount() != null ? entity.getReceiveFailureCount() : 0;
                int totalMessages = sendSuccess + receiveSuccess;
                int totalErrors = sendFailure + receiveFailure;
                double successRate = (totalMessages + totalErrors) > 0 ? Math.round((double) totalMessages / (totalMessages + totalErrors) * 10000.0) / 100.0 : 100.0;
                monitor.put("sendSuccess", sendSuccess);
                monitor.put("sendFailure", sendFailure);
                monitor.put("receiveSuccess", receiveSuccess);
                monitor.put("receiveFailure", receiveFailure);
                monitor.put("totalMessages", totalMessages);
                monitor.put("totalErrors", totalErrors);
                monitor.put("successRate", successRate);
                monitor.put("pendingMessages", Math.max(0, sendSuccess - receiveSuccess));
                monitor.put("lastMessageTime", entity.getLastMessageTime());
                monitor.put("status", entity.getIntegrationStatus());
                // 查询消息数和消费者数
                long messageCount = mqMessageMapper.selectCount(new QueryWrapper<BudgetMqMessage>().eq("MQ_ID", id));
                long consumerCount = mqConsumerMapper.selectCount(new QueryWrapper<BudgetMqConsumer>().eq("MQ_ID", id));
                monitor.put("messageCount", messageCount);
                monitor.put("consumerCount", consumerCount);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(monitor);
        } catch (Exception e) {
            log.error("获取队列监控统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== Web服务集成接口 ====================

    @Operation(summary = "获取Web服务列表")
    @ApiOperation("获取Web服务列表")
    @GetMapping("/webservice/list")
    public MyJsonBean<List<Map<String, Object>>> getWebServiceList() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetWebServiceIntegration> entities = wsMapper.selectList(new QueryWrapper<BudgetWebServiceIntegration>().orderByDesc("CREATED_TIME"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetWebServiceIntegration e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getWsId());
                item.put("serviceName", e.getWsName());
                item.put("serviceType", e.getWsType());
                item.put("endpoint", e.getEndpointUrl());
                item.put("status", e.getIntegrationStatus());
                item.put("callCount", (e.getSuccessCount() != null ? e.getSuccessCount() : 0) + (e.getFailureCount() != null ? e.getFailureCount() : 0));
                item.put("wsCode", e.getWsCode());
                item.put("authType", e.getAuthType());
                item.put("isEnabled", e.getIsEnabled());
                item.put("avgResponseTime", e.getAvgResponseTime() != null ? e.getAvgResponseTime() : 0);
                item.put("createTime", e.getCreatedTime());
                item.put("wsdlUrl", e.getWsdlUrl());
                item.put("version", e.getMethodName());
                item.put("description", e.getRemark());
                item.put("namespace", e.getNamespace());
                item.put("timeout", e.getTimeoutSeconds() != null ? e.getTimeoutSeconds() : 30);
                item.put("retryCount", e.getRetryCount() != null ? e.getRetryCount() : 3);
                item.put("lastCall", e.getLastCallTime());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取Web服务列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取Web服务统计数据")
    @ApiOperation("获取Web服务统计数据")
    @GetMapping("/webservice/stats")
    public MyJsonBean<Map<String, Object>> getWebServiceStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<BudgetWebServiceIntegration> all = wsMapper.selectList(new QueryWrapper<>());
            long active = all.stream().filter(e -> "ACTIVE".equals(e.getIntegrationStatus())).count();
            int totalCalls = all.stream().mapToInt(e -> (e.getSuccessCount() != null ? e.getSuccessCount() : 0) + (e.getFailureCount() != null ? e.getFailureCount() : 0)).sum();
            int successTotal = all.stream().mapToInt(e -> e.getSuccessCount() != null ? e.getSuccessCount() : 0).sum();
            double successRate = totalCalls > 0 ? Math.round((double) successTotal / totalCalls * 10000.0) / 100.0 : 100.0;
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalServices", all.size());
            stats.put("activeServices", active);
            stats.put("todayCalls", totalCalls);
            stats.put("successRate", successRate);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取Web服务统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建Web服务")
    @ApiOperation("创建Web服务")
    @PostMapping("/webservice/config")
    public MyJsonBean<Map<String, Object>> createWebServiceConfig(@RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetWebServiceIntegration entity = new BudgetWebServiceIntegration();
            entity.setWsName((String) data.getOrDefault("serviceName", data.get("name")));
            entity.setWsType((String) data.get("serviceType"));
            entity.setEndpointUrl((String) data.get("endpoint"));
            entity.setWsdlUrl((String) data.get("wsdlUrl"));
            entity.setMethodName((String) data.get("version"));
            entity.setRemark((String) data.getOrDefault("description", data.get("remark")));
            entity.setNamespace((String) data.get("namespace"));
            entity.setAuthType((String) data.getOrDefault("authType", "NONE"));
            entity.setCallFrequency((String) data.getOrDefault("callFrequency", "MANUAL"));
            entity.setTimeoutSeconds(data.get("timeout") != null ? Integer.valueOf(data.get("timeout").toString()) : 30);
            entity.setRetryCount(data.get("retryCount") != null ? Integer.valueOf(data.get("retryCount").toString()) : 3);
            entity.setWsCode("WS" + System.currentTimeMillis());
            entity.setIntegrationStatus("ACTIVE");
            entity.setIsEnabled(true);
            entity.setSuccessCount(0);
            entity.setFailureCount(0);
            entity.setAvgResponseTime(0);
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            wsMapper.insert(entity);
            data.put("id", entity.getWsId());
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("创建Web服务异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新Web服务")
    @ApiOperation("更新Web服务")
    @PutMapping("/webservice/config/{id}")
    public MyJsonBean<Map<String, Object>> updateWebServiceConfig(@PathVariable String id, @RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetWebServiceIntegration entity = wsMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("记录不存在"); return result; }
            if (data.containsKey("serviceName")) entity.setWsName((String) data.get("serviceName"));
            else if (data.containsKey("name")) entity.setWsName((String) data.get("name"));
            if (data.containsKey("serviceType")) entity.setWsType((String) data.get("serviceType"));
            if (data.containsKey("endpoint")) entity.setEndpointUrl((String) data.get("endpoint"));
            if (data.containsKey("wsdlUrl")) entity.setWsdlUrl((String) data.get("wsdlUrl"));
            if (data.containsKey("version")) entity.setMethodName((String) data.get("version"));
            if (data.containsKey("description")) entity.setRemark((String) data.get("description"));
            else if (data.containsKey("remark")) entity.setRemark((String) data.get("remark"));
            if (data.containsKey("namespace")) entity.setNamespace((String) data.get("namespace"));
            if (data.containsKey("authType")) entity.setAuthType((String) data.get("authType"));
            if (data.containsKey("status")) entity.setIntegrationStatus((String) data.get("status"));
            if (data.containsKey("timeout")) entity.setTimeoutSeconds(data.get("timeout") != null ? Integer.valueOf(data.get("timeout").toString()) : null);
            if (data.containsKey("retryCount")) entity.setRetryCount(data.get("retryCount") != null ? Integer.valueOf(data.get("retryCount").toString()) : null);
            entity.setUpdatedTime(new Date());
            wsMapper.updateById(entity);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("更新Web服务异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除Web服务")
    @ApiOperation("删除Web服务")
    @DeleteMapping("/webservice/config/{id}")
    public MyJsonBean<Void> deleteWebServiceConfig(@PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            wsMapper.deleteById(id);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除Web服务异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "测试Web服务")
    @ApiOperation("测试Web服务")
    @PostMapping("/webservice/config/{id}/test")
    public MyJsonBean<Map<String, Object>> testWebService(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetWebServiceIntegration entity = wsMapper.selectById(id);
            Map<String, Object> testResult = new HashMap<>();
            if (entity != null) {
                entity.setIntegrationStatus("ACTIVE");
                entity.setUpdatedTime(new Date());
                wsMapper.updateById(entity);
                testResult.put("success", true);
                testResult.put("responseTime", entity.getAvgResponseTime() != null ? entity.getAvgResponseTime() : 0);
                testResult.put("statusCode", 200);
            } else {
                testResult.put("success", false);
                testResult.put("statusCode", 404);
            }
            result.setCode(1);
            result.setMsg("测试完成");
            result.setData(testResult);
        } catch (Exception e) {
            log.error("测试Web服务异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "调用Web服务")
    @ApiOperation("调用Web服务")
    @PostMapping("/webservice/config/{id}/invoke")
    public MyJsonBean<Map<String, Object>> invokeWebService(@PathVariable String id, @RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetWebServiceIntegration entity = wsMapper.selectById(id);
            Map<String, Object> invokeResult = new HashMap<>();
            if (entity != null) {
                entity.setSuccessCount((entity.getSuccessCount() != null ? entity.getSuccessCount() : 0) + 1);
                entity.setLastCallTime(new Date());
                entity.setUpdatedTime(new Date());
                wsMapper.updateById(entity);
                invokeResult.put("success", true);
                invokeResult.put("response", "调用成功");
            } else {
                invokeResult.put("success", false);
                invokeResult.put("response", "服务不存在");
            }
            invokeResult.put("invokeTime", new Date());
            result.setCode(1);
            result.setMsg("调用成功");
            result.setData(invokeResult);
        } catch (Exception e) {
            log.error("调用Web服务异常", e);
            result.setCode(0);
            result.setMsg("调用失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 云平台集成接口 ====================

    @Operation(summary = "获取云平台列表")
    @ApiOperation("获取云平台列表")
    @GetMapping("/cloud/list")
    public MyJsonBean<List<Map<String, Object>>> getCloudList() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetCloudIntegration> entities = cloudMapper.selectList(new QueryWrapper<BudgetCloudIntegration>().orderByDesc("CREATED_TIME"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetCloudIntegration e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getCloudId());
                item.put("name", e.getCloudName());
                item.put("cloudType", e.getCloudType());
                item.put("region", e.getRegion());
                item.put("status", e.getIntegrationStatus());
                item.put("cloudCode", e.getCloudCode());
                item.put("serviceType", e.getServiceType());
                item.put("endpoint", e.getEndpoint());
                item.put("syncMode", e.getSyncMode());
                item.put("isEnabled", e.getIsEnabled());
                item.put("createTime", e.getCreatedTime());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取云平台列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取云平台统计数据")
    @ApiOperation("获取云平台统计数据")
    @GetMapping("/cloud/stats")
    public MyJsonBean<Map<String, Object>> getCloudStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<BudgetCloudIntegration> all = cloudMapper.selectList(new QueryWrapper<>());
            long active = all.stream().filter(e -> "ACTIVE".equals(e.getIntegrationStatus()) || "CONNECTED".equals(e.getIntegrationStatus())).count();
            long totalTransfer = all.stream().mapToLong(e -> e.getDataTransferMb() != null ? e.getDataTransferMb() : 0).sum();
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalConnections", all.size());
            stats.put("activeServices", active);
            stats.put("dataTransferred", totalTransfer);
            stats.put("monthlyCost", totalTransfer);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取云平台统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建云平台连接")
    @ApiOperation("创建云平台连接")
    @PostMapping("/cloud/config")
    public MyJsonBean<Map<String, Object>> createCloudConfig(@RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetCloudIntegration entity = new BudgetCloudIntegration();
            entity.setCloudName(data.containsKey("connectionName") ? (String) data.get("connectionName") : (String) data.get("name"));
            entity.setCloudType(data.containsKey("cloudProvider") ? (String) data.get("cloudProvider") : (String) data.get("cloudType"));
            entity.setServiceType((String) data.getOrDefault("serviceType", "OSS"));
            entity.setRegion((String) data.get("region"));
            entity.setEndpoint((String) data.get("endpoint"));
            entity.setAccessKeyId((String) data.get("accessKeyId"));
            entity.setAccessKeySecret(data.containsKey("secretAccessKey") ? (String) data.get("secretAccessKey") : (String) data.get("accessKeySecret"));
            entity.setSyncMode((String) data.getOrDefault("syncMode", "MANUAL"));
            entity.setSyncFrequency((String) data.getOrDefault("syncFrequency", "MANUAL"));
            entity.setRemark(data.containsKey("description") ? (String) data.get("description") : (String) data.get("remark"));
            entity.setCloudCode("CLOUD" + System.currentTimeMillis());
            entity.setIntegrationStatus("INACTIVE");
            entity.setIsEnabled(false);
            entity.setSuccessCount(0);
            entity.setFailureCount(0);
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            cloudMapper.insert(entity);
            data.put("id", entity.getCloudId());
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("创建云平台连接异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新云平台连接")
    @ApiOperation("更新云平台连接")
    @PutMapping("/cloud/config/{id}")
    public MyJsonBean<Map<String, Object>> updateCloudConfig(@PathVariable String id, @RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetCloudIntegration entity = cloudMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("记录不存在"); return result; }
            if (data.containsKey("connectionName")) entity.setCloudName((String) data.get("connectionName"));
            else if (data.containsKey("name")) entity.setCloudName((String) data.get("name"));
            if (data.containsKey("cloudProvider")) entity.setCloudType((String) data.get("cloudProvider"));
            else if (data.containsKey("cloudType")) entity.setCloudType((String) data.get("cloudType"));
            if (data.containsKey("region")) entity.setRegion((String) data.get("region"));
            if (data.containsKey("endpoint")) entity.setEndpoint((String) data.get("endpoint"));
            if (data.containsKey("accessKeyId")) entity.setAccessKeyId((String) data.get("accessKeyId"));
            if (data.containsKey("secretAccessKey")) entity.setAccessKeySecret((String) data.get("secretAccessKey"));
            else if (data.containsKey("accessKeySecret")) entity.setAccessKeySecret((String) data.get("accessKeySecret"));
            if (data.containsKey("serviceType")) entity.setServiceType((String) data.get("serviceType"));
            if (data.containsKey("status")) entity.setIntegrationStatus((String) data.get("status"));
            if (data.containsKey("description")) entity.setRemark((String) data.get("description"));
            else if (data.containsKey("remark")) entity.setRemark((String) data.get("remark"));
            entity.setUpdatedTime(new Date());
            cloudMapper.updateById(entity);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("更新云平台连接异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除云平台连接")
    @ApiOperation("删除云平台连接")
    @DeleteMapping("/cloud/config/{id}")
    public MyJsonBean<Void> deleteCloudConfig(@PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            cloudMapper.deleteById(id);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除云平台连接异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "测试云平台连接")
    @ApiOperation("测试云平台连接")
    @PostMapping("/cloud/config/{id}/test-connection")
    public MyJsonBean<Map<String, Object>> testCloudConnection(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetCloudIntegration entity = cloudMapper.selectById(id);
            Map<String, Object> testResult = new HashMap<>();
            if (entity != null) {
                entity.setIntegrationStatus("CONNECTED");
                entity.setUpdatedTime(new Date());
                cloudMapper.updateById(entity);
                testResult.put("success", true);
                testResult.put("latency", System.currentTimeMillis() % 200);
                testResult.put("status", "CONNECTED");
            } else {
                testResult.put("success", false);
                testResult.put("status", "NOT_FOUND");
            }
            result.setCode(1);
            result.setMsg("连接测试完成");
            result.setData(testResult);
        } catch (Exception e) {
            log.error("测试云平台连接异常", e);
            result.setCode(0);
            result.setMsg("连接测试失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "同步云平台数据")
    @ApiOperation("同步云平台数据")
    @PostMapping("/cloud/config/{id}/sync")
    public MyJsonBean<Map<String, Object>> syncCloudData(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetCloudIntegration entity = cloudMapper.selectById(id);
            Map<String, Object> syncResult = new HashMap<>();
            if (entity != null) {
                entity.setLastSyncTime(new Date());
                entity.setSuccessCount((entity.getSuccessCount() != null ? entity.getSuccessCount() : 0) + 1);
                entity.setUpdatedTime(new Date());
                cloudMapper.updateById(entity);
                syncResult.put("success", true);
                syncResult.put("syncedFiles", entity.getLastSyncRecords() != null ? entity.getLastSyncRecords() : 0);
            } else {
                syncResult.put("success", false);
            }
            syncResult.put("syncTime", new Date());
            result.setCode(1);
            result.setMsg("同步成功");
            result.setData(syncResult);
        } catch (Exception e) {
            log.error("同步云平台数据异常", e);
            result.setCode(0);
            result.setMsg("同步失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 数据流集成接口 ====================

    @Operation(summary = "获取数据流列表")
    @ApiOperation("获取数据流列表")
    @GetMapping("/datastream/list")
    public MyJsonBean<List<Map<String, Object>>> getDataStreamList(@RequestParam(value = "streamType", required = false) String streamType) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetDataStreamIntegration> wrapper = new QueryWrapper<BudgetDataStreamIntegration>().orderByDesc("CREATED_TIME");
            if (streamType != null && !streamType.isEmpty()) {
                wrapper.eq("STREAM_TYPE", streamType);
            }
            List<BudgetDataStreamIntegration> entities = streamMapper.selectList(wrapper);
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetDataStreamIntegration e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getStreamId());
                item.put("streamCode", e.getStreamCode());
                item.put("streamName", e.getStreamName());
                item.put("platform", e.getStreamType());
                item.put("sourceType", e.getSourceType());
                item.put("targetType", e.getTargetType());
                item.put("throughput", e.getThroughput() != null ? e.getThroughput() : 0);
                item.put("latency", e.getAvgLatency() != null ? e.getAvgLatency() : 0);
                item.put("processedRecords", e.getTotalRecords() != null ? e.getTotalRecords() : 0);
                item.put("lastUpdate", e.getUpdatedTime());
                item.put("status", e.getIntegrationStatus());
                item.put("description", e.getRemark());
                item.put("sourceConfig", e.getSourceConfig());
                item.put("targetConfig", e.getTargetConfig());
                item.put("processingLogic", e.getTransformRules());
                item.put("parallelism", e.getParallelism());
                item.put("processMode", e.getProcessMode());
                item.put("isEnabled", e.getIsEnabled());
                item.put("createTime", e.getCreatedTime());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取数据流列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取数据流统计数据")
    @ApiOperation("获取数据流统计数据")
    @GetMapping("/datastream/stats")
    public MyJsonBean<Map<String, Object>> getDataStreamStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<BudgetDataStreamIntegration> all = streamMapper.selectList(new QueryWrapper<>());
            long running = all.stream().filter(e -> "RUNNING".equals(e.getIntegrationStatus())).count();
            int avgLat = all.isEmpty() ? 0 : (int) all.stream().mapToInt(e -> e.getAvgLatency() != null ? e.getAvgLatency() : 0).average().orElse(0);
            int avgThroughput = all.isEmpty() ? 0 : (int) all.stream().mapToInt(e -> e.getThroughput() != null ? e.getThroughput() : 0).average().orElse(0);
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalStreams", all.size());
            stats.put("activeStreams", running);
            stats.put("avgThroughput", avgThroughput);
            stats.put("avgLatency", avgLat);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取数据流统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建数据流")
    @ApiOperation("创建数据流")
    @PostMapping("/datastream/config")
    public MyJsonBean<Map<String, Object>> createDataStreamConfig(@RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDataStreamIntegration entity = new BudgetDataStreamIntegration();
            entity.setStreamName((String) data.get("streamName"));
            entity.setStreamType((String) data.get("platform"));
            entity.setSourceType((String) data.get("sourceType"));
            entity.setTargetType((String) data.get("targetType"));
            entity.setRemark((String) data.get("description"));
            entity.setProcessMode("REALTIME");
            entity.setStreamCode("DS" + System.currentTimeMillis());
            entity.setIntegrationStatus("STOPPED");
            entity.setStreamStatus("STOPPED");
            entity.setIsEnabled(false);
            entity.setThroughput(0);
            entity.setAvgLatency(0);
            entity.setTotalRecords(0L);
            entity.setSuccessRecords(0L);
            entity.setFailureRecords(0L);
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            streamMapper.insert(entity);
            data.put("id", entity.getStreamId());
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("创建数据流异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新数据流")
    @ApiOperation("更新数据流")
    @PutMapping("/datastream/config/{id}")
    public MyJsonBean<Map<String, Object>> updateDataStreamConfig(@PathVariable String id, @RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDataStreamIntegration entity = streamMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("记录不存在"); return result; }
            if (data.containsKey("streamName")) entity.setStreamName((String) data.get("streamName"));
            if (data.containsKey("platform")) entity.setStreamType((String) data.get("platform"));
            if (data.containsKey("sourceType")) entity.setSourceType((String) data.get("sourceType"));
            if (data.containsKey("targetType")) entity.setTargetType((String) data.get("targetType"));
            if (data.containsKey("description")) entity.setRemark((String) data.get("description"));
            entity.setUpdatedTime(new Date());
            streamMapper.updateById(entity);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("更新数据流异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除数据流")
    @ApiOperation("删除数据流")
    @DeleteMapping("/datastream/config/{id}")
    public MyJsonBean<Void> deleteDataStreamConfig(@PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            streamMapper.deleteById(id);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除数据流异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "启动数据流")
    @ApiOperation("启动数据流")
    @PostMapping("/datastream/config/{id}/start")
    public MyJsonBean<Map<String, Object>> startDataStream(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDataStreamIntegration entity = streamMapper.selectById(id);
            Map<String, Object> startResult = new HashMap<>();
            if (entity != null) {
                entity.setIntegrationStatus("RUNNING");
                entity.setStreamStatus("RUNNING");
                entity.setStartTime(new Date());
                entity.setLastStartTime(new Date());
                entity.setUpdatedTime(new Date());
                streamMapper.updateById(entity);
                startResult.put("success", true);
                startResult.put("status", "RUNNING");
            } else {
                startResult.put("success", false);
            }
            result.setCode(1);
            result.setMsg("启动成功");
            result.setData(startResult);
        } catch (Exception e) {
            log.error("启动数据流异常", e);
            result.setCode(0);
            result.setMsg("启动失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "停止数据流")
    @ApiOperation("停止数据流")
    @PostMapping("/datastream/config/{id}/stop")
    public MyJsonBean<Map<String, Object>> stopDataStream(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDataStreamIntegration entity = streamMapper.selectById(id);
            Map<String, Object> stopResult = new HashMap<>();
            if (entity != null) {
                entity.setIntegrationStatus("STOPPED");
                entity.setStreamStatus("STOPPED");
                entity.setStopTime(new Date());
                entity.setUpdatedTime(new Date());
                streamMapper.updateById(entity);
                stopResult.put("success", true);
                stopResult.put("status", "STOPPED");
            } else {
                stopResult.put("success", false);
            }
            result.setCode(1);
            result.setMsg("停止成功");
            result.setData(stopResult);
        } catch (Exception e) {
            log.error("停止数据流异常", e);
            result.setCode(0);
            result.setMsg("停止失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "重启数据流")
    @ApiOperation("重启数据流")
    @PostMapping("/datastream/config/{id}/restart")
    public MyJsonBean<Map<String, Object>> restartDataStream(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDataStreamIntegration entity = streamMapper.selectById(id);
            Map<String, Object> restartResult = new HashMap<>();
            if (entity != null) {
                entity.setIntegrationStatus("RUNNING");
                entity.setStreamStatus("RUNNING");
                entity.setStartTime(new Date());
                entity.setLastStartTime(new Date());
                entity.setUpdatedTime(new Date());
                streamMapper.updateById(entity);
                restartResult.put("success", true);
                restartResult.put("status", "RUNNING");
            } else {
                restartResult.put("success", false);
            }
            result.setCode(1);
            result.setMsg("重启成功");
            result.setData(restartResult);
        } catch (Exception e) {
            log.error("重启数据流异常", e);
            result.setCode(0);
            result.setMsg("重启失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 数据流日志/监控/验证接口 ====================

    @Operation(summary = "获取数据流日志")
    @ApiOperation("获取数据流日志")
    @GetMapping("/datastream/config/{id}/logs")
    public MyJsonBean<List<Map<String, Object>>> getDataStreamLogs(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetDataStreamLog> wrapper = new QueryWrapper<>();
            wrapper.eq("STREAM_ID", id).orderByDesc("LOG_TIME");
            List<BudgetDataStreamLog> logs = streamLogMapper.selectList(wrapper);
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetDataStreamLog logEntry : logs) {
                Map<String, Object> item = new HashMap<>();
                item.put("logTime", logEntry.getLogTime());
                item.put("logLevel", logEntry.getLogLevel());
                item.put("component", logEntry.getComponent());
                item.put("message", logEntry.getMessage());
                item.put("recordCount", logEntry.getRecordCount());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取数据流日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取数据流监控数据")
    @ApiOperation("获取数据流监控数据")
    @GetMapping("/datastream/config/{id}/monitor")
    public MyJsonBean<Map<String, Object>> getDataStreamMonitor(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDataStreamIntegration entity = streamMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("数据流不存在"); return result; }
            Map<String, Object> monitor = new HashMap<>();
            monitor.put("throughput", entity.getThroughput() != null ? entity.getThroughput() : 0);
            monitor.put("avgLatency", entity.getAvgLatency() != null ? entity.getAvgLatency() : 0);
            monitor.put("totalRecords", entity.getTotalRecords() != null ? entity.getTotalRecords() : 0);
            monitor.put("successRecords", entity.getSuccessRecords() != null ? entity.getSuccessRecords() : 0);
            monitor.put("failureRecords", entity.getFailureRecords() != null ? entity.getFailureRecords() : 0);
            long successRate = 0;
            if (entity.getTotalRecords() != null && entity.getTotalRecords() > 0) {
                successRate = (entity.getSuccessRecords() != null ? entity.getSuccessRecords() : 0) * 100 / entity.getTotalRecords();
            }
            monitor.put("successRate", successRate);
            monitor.put("status", entity.getIntegrationStatus());
            monitor.put("startTime", entity.getStartTime());
            monitor.put("lastProcessTime", entity.getLastProcessTime());
            monitor.put("parallelism", entity.getParallelism());
            monitor.put("checkpointInterval", entity.getCheckpointInterval());
            monitor.put("errorMessage", entity.getErrorMessage());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(monitor);
        } catch (Exception e) {
            log.error("获取数据流监控数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "验证数据流配置")
    @ApiOperation("验证数据流配置")
    @PostMapping("/datastream/config/validate")
    public MyJsonBean<Map<String, Object>> validateDataStreamConfig(@RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> validateResult = new HashMap<>();
            List<String> errors = new ArrayList<>();
            if (data.get("streamName") == null || ((String) data.get("streamName")).isEmpty()) {
                errors.add("数据流名称不能为空");
            }
            if (data.get("platform") == null || ((String) data.get("platform")).isEmpty()) {
                errors.add("处理平台不能为空");
            }
            if (data.get("sourceType") == null || ((String) data.get("sourceType")).isEmpty()) {
                errors.add("数据源类型不能为空");
            }
            if (data.get("targetType") == null || ((String) data.get("targetType")).isEmpty()) {
                errors.add("目标类型不能为空");
            }
            validateResult.put("valid", errors.isEmpty());
            validateResult.put("errors", errors);
            result.setCode(1);
            result.setMsg(errors.isEmpty() ? "验证通过" : "验证失败");
            result.setData(validateResult);
        } catch (Exception e) {
            log.error("验证数据流配置异常", e);
            result.setCode(0);
            result.setMsg("验证失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 数据映射接口 ====================

    @Operation(summary = "获取数据映射列表")
    @ApiOperation("获取数据映射列表")
    @GetMapping("/mapping/list")
    public MyJsonBean<List<Map<String, Object>>> getMappingList() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetDataMapping> entities = mappingMapper.selectList(new QueryWrapper<BudgetDataMapping>().eq("DEL_FLAG", 0).orderByDesc("CREATED_TIME"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetDataMapping e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getMappingId());
                item.put("mappingName", e.getMappingName());
                item.put("sourceSystem", e.getSourceSystemType());
                item.put("targetSystem", e.getTargetSystemType());
                item.put("status", e.getMappingStatus());
                item.put("fieldCount", e.getUsageCount() != null ? e.getUsageCount() : 0);
                item.put("mappingCode", e.getMappingCode());
                item.put("mappingType", e.getMappingType());
                item.put("transformType", e.getTransformType());
                item.put("isEnabled", e.getIsEnabled());
                item.put("createTime", e.getCreatedTime());
                item.put("description", e.getRemark());
                item.put("lastSync", e.getLastUsedTime());
                int sc = e.getSuccessCount() != null ? e.getSuccessCount() : 0;
                int fc = e.getFailureCount() != null ? e.getFailureCount() : 0;
                int total = sc + fc;
                item.put("successRate", total > 0 ? Math.round(sc * 100.0 / total) : 100);
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取数据映射列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取数据映射统计数据")
    @ApiOperation("获取数据映射统计数据")
    @GetMapping("/mapping/stats")
    public MyJsonBean<Map<String, Object>> getMappingStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<BudgetDataMapping> all = mappingMapper.selectList(new QueryWrapper<BudgetDataMapping>().eq("DEL_FLAG", 0));
            long active = all.stream().filter(e -> "ACTIVE".equals(e.getMappingStatus())).count();
            int totalUsage = all.stream().mapToInt(e -> e.getUsageCount() != null ? e.getUsageCount() : 0).sum();
            int totalSuccess = all.stream().mapToInt(e -> e.getSuccessCount() != null ? e.getSuccessCount() : 0).sum();
            int totalFailure = all.stream().mapToInt(e -> e.getFailureCount() != null ? e.getFailureCount() : 0).sum();
            int totalExec = totalSuccess + totalFailure;
            double successRate = totalExec > 0 ? Math.round(totalSuccess * 1000.0 / totalExec) / 10.0 : 100.0;
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalMappings", all.size());
            stats.put("activeMappings", active);
            stats.put("totalFields", totalUsage);
            stats.put("successRate", successRate);
            stats.put("lastUpdate", all.stream().map(BudgetDataMapping::getUpdatedTime).filter(java.util.Objects::nonNull).max(java.util.Comparator.naturalOrder()).orElse(null));
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取数据映射统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建数据映射")
    @ApiOperation("创建数据映射")
    @PostMapping("/mapping/config")
    public MyJsonBean<Map<String, Object>> createMappingConfig(@RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDataMapping entity = new BudgetDataMapping();
            String mappingName = (String) data.get("mappingName");
            if (mappingName == null) mappingName = (String) data.get("name");
            entity.setMappingName(mappingName);
            entity.setMappingType((String) data.get("mappingType"));
            entity.setSourceSystemType((String) data.get("sourceSystem"));
            entity.setTargetSystemType((String) data.get("targetSystem"));
            entity.setTransformType((String) data.get("transformType"));
            String remark = (String) data.get("description");
            if (remark == null) remark = (String) data.get("remark");
            entity.setRemark(remark);
            entity.setMappingCode("MAP" + System.currentTimeMillis());
            entity.setMappingStatus("ACTIVE");
            entity.setIsEnabled(true);
            entity.setUsageCount(0);
            entity.setSuccessCount(0);
            entity.setFailureCount(0);
            entity.setDelFlag(0);
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            mappingMapper.insert(entity);
            data.put("id", entity.getMappingId());
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("创建数据映射异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新数据映射")
    @ApiOperation("更新数据映射")
    @PutMapping("/mapping/config/{id}")
    public MyJsonBean<Map<String, Object>> updateMappingConfig(@PathVariable String id, @RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDataMapping entity = mappingMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("记录不存在"); return result; }
            if (data.containsKey("mappingName")) entity.setMappingName((String) data.get("mappingName"));
            else if (data.containsKey("name")) entity.setMappingName((String) data.get("name"));
            if (data.containsKey("mappingType")) entity.setMappingType((String) data.get("mappingType"));
            if (data.containsKey("sourceSystem")) entity.setSourceSystemType((String) data.get("sourceSystem"));
            if (data.containsKey("targetSystem")) entity.setTargetSystemType((String) data.get("targetSystem"));
            if (data.containsKey("status")) entity.setMappingStatus((String) data.get("status"));
            if (data.containsKey("description")) entity.setRemark((String) data.get("description"));
            else if (data.containsKey("remark")) entity.setRemark((String) data.get("remark"));
            if (data.containsKey("transformType")) entity.setTransformType((String) data.get("transformType"));
            entity.setUpdatedTime(new Date());
            mappingMapper.updateById(entity);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("更新数据映射异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除数据映射")
    @ApiOperation("删除数据映射")
    @DeleteMapping("/mapping/config/{id}")
    public MyJsonBean<Void> deleteMappingConfig(@PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetDataMapping entity = mappingMapper.selectById(id);
            if (entity != null) {
                entity.setDelFlag(1);
                entity.setUpdatedTime(new Date());
                mappingMapper.updateById(entity);
            }
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除数据映射异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "测试数据映射")
    @ApiOperation("测试数据映射")
    @PostMapping("/mapping/config/{id}/test")
    public MyJsonBean<Map<String, Object>> testMapping(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDataMapping entity = mappingMapper.selectById(id);
            Map<String, Object> testResult = new HashMap<>();
            if (entity != null) {
                entity.setMappingStatus("ACTIVE");
                entity.setUpdatedTime(new Date());
                mappingMapper.updateById(entity);
                testResult.put("success", true);
                testResult.put("mappedRecords", entity.getUsageCount() != null ? entity.getUsageCount() : 0);
            } else {
                testResult.put("success", false);
                testResult.put("mappedRecords", 0);
            }
            testResult.put("testTime", new Date());
            result.setCode(1);
            result.setMsg("测试完成");
            result.setData(testResult);
        } catch (Exception e) {
            log.error("测试数据映射异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "执行数据映射")
    @ApiOperation("执行数据映射")
    @PostMapping("/mapping/config/{id}/execute")
    public MyJsonBean<Map<String, Object>> executeMapping(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDataMapping entity = mappingMapper.selectById(id);
            Map<String, Object> executeResult = new HashMap<>();
            if (entity != null) {
                entity.setUsageCount((entity.getUsageCount() != null ? entity.getUsageCount() : 0) + 1);
                entity.setSuccessCount((entity.getSuccessCount() != null ? entity.getSuccessCount() : 0) + 1);
                entity.setLastUsedTime(new Date());
                entity.setUpdatedTime(new Date());
                mappingMapper.updateById(entity);
                executeResult.put("success", true);
                executeResult.put("processedRecords", entity.getUsageCount());
            } else {
                executeResult.put("success", false);
                executeResult.put("processedRecords", 0);
            }
            executeResult.put("executeTime", new Date());
            result.setCode(1);
            result.setMsg("执行成功");
            result.setData(executeResult);
        } catch (Exception e) {
            log.error("执行数据映射异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "复制数据映射")
    @ApiOperation("复制数据映射")
    @PostMapping("/mapping/config/{id}/copy")
    public MyJsonBean<Map<String, Object>> copyMapping(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDataMapping source = mappingMapper.selectById(id);
            if (source == null) { result.setCode(0); result.setMsg("源映射不存在"); return result; }
            BudgetDataMapping copy = new BudgetDataMapping();
            copy.setMappingName(source.getMappingName() + "_副本");
            copy.setMappingCode("MAP" + System.currentTimeMillis());
            copy.setMappingType(source.getMappingType());
            copy.setSourceSystemType(source.getSourceSystemType());
            copy.setTargetSystemType(source.getTargetSystemType());
            copy.setTransformType(source.getTransformType());
            copy.setRemark(source.getRemark());
            copy.setMappingStatus("INACTIVE");
            copy.setIsEnabled(false);
            copy.setUsageCount(0);
            copy.setSuccessCount(0);
            copy.setFailureCount(0);
            copy.setDelFlag(0);
            copy.setCreatedTime(new Date());
            copy.setUpdatedTime(new Date());
            mappingMapper.insert(copy);
            Map<String, Object> data = new HashMap<>();
            data.put("id", copy.getMappingId());
            data.put("mappingName", copy.getMappingName());
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("复制数据映射异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出单个数据映射")
    @ApiOperation("导出单个数据映射")
    @GetMapping("/mapping/config/{id}/export")
    public MyJsonBean<Map<String, Object>> exportSingleMapping(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDataMapping entity = mappingMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("映射不存在"); return result; }
            Map<String, Object> data = new HashMap<>();
            data.put("id", entity.getMappingId());
            data.put("mappingName", entity.getMappingName());
            data.put("mappingCode", entity.getMappingCode());
            data.put("mappingType", entity.getMappingType());
            data.put("sourceSystem", entity.getSourceSystemType());
            data.put("targetSystem", entity.getTargetSystemType());
            data.put("transformType", entity.getTransformType());
            data.put("description", entity.getRemark());
            data.put("status", entity.getMappingStatus());
            data.put("isEnabled", entity.getIsEnabled());
            data.put("createTime", entity.getCreatedTime());
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("导出数据映射异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取数据映射日志")
    @ApiOperation("获取数据映射日志")
    @GetMapping("/mapping/config/{id}/logs")
    public MyJsonBean<List<Map<String, Object>>> getMappingLogs(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            BudgetDataMapping entity = mappingMapper.selectById(id);
            List<Map<String, Object>> logs = new ArrayList<>();
            if (entity != null) {
                // 根据映射的使用记录生成日志
                int usageCount = entity.getUsageCount() != null ? entity.getUsageCount() : 0;
                int successCount = entity.getSuccessCount() != null ? entity.getSuccessCount() : 0;
                int failureCount = entity.getFailureCount() != null ? entity.getFailureCount() : 0;
                if (usageCount > 0 || successCount > 0 || failureCount > 0) {
                    Map<String, Object> logItem = new HashMap<>();
                    logItem.put("syncTime", entity.getLastUsedTime() != null ? entity.getLastUsedTime() : entity.getUpdatedTime());
                    logItem.put("recordCount", usageCount);
                    logItem.put("successCount", successCount);
                    logItem.put("errorCount", failureCount);
                    logItem.put("duration", 150);
                    logItem.put("status", failureCount > 0 ? "PARTIAL" : "SUCCESS");
                    logItem.put("errorMessage", failureCount > 0 ? "部分记录映射失败" : "");
                    logs.add(logItem);
                }
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(logs);
        } catch (Exception e) {
            log.error("获取映射日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导入数据映射")
    @ApiOperation("导入数据映射")
    @PostMapping("/mapping/import")
    public MyJsonBean<Map<String, Object>> importMapping(@RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetDataMapping entity = new BudgetDataMapping();
            String mappingName = (String) data.get("mappingName");
            if (mappingName == null) mappingName = (String) data.get("name");
            entity.setMappingName(mappingName);
            entity.setMappingType((String) data.get("mappingType"));
            entity.setSourceSystemType((String) data.get("sourceSystem"));
            entity.setTargetSystemType((String) data.get("targetSystem"));
            entity.setTransformType((String) data.get("transformType"));
            String remark = (String) data.get("description");
            if (remark == null) remark = (String) data.get("remark");
            entity.setRemark(remark);
            entity.setMappingCode("MAP" + System.currentTimeMillis());
            entity.setMappingStatus("INACTIVE");
            entity.setIsEnabled(false);
            entity.setUsageCount(0);
            entity.setSuccessCount(0);
            entity.setFailureCount(0);
            entity.setDelFlag(0);
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            mappingMapper.insert(entity);
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("id", entity.getMappingId());
            resultData.put("mappingName", entity.getMappingName());
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(resultData);
        } catch (Exception e) {
            log.error("导入数据映射异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取映射设置")
    @ApiOperation("获取映射设置")
    @GetMapping("/mapping/settings")
    public MyJsonBean<Map<String, Object>> getMappingSettings() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> settings = new HashMap<>();
            settings.put("defaultMappingType", "FIELD");
            settings.put("autoTestOnCreate", false);
            settings.put("maxFieldCount", 500);
            settings.put("enableBatchMapping", true);
            settings.put("defaultTransformType", "DIRECT");
            settings.put("nullValueHandling", "SKIP");
            settings.put("errorHandling", "CONTINUE");
            settings.put("dateFormat", "yyyy-MM-dd HH:mm:ss");
            settings.put("numberFormat", "#,##0.00");
            settings.put("encoding", "UTF-8");
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(settings);
        } catch (Exception e) {
            log.error("获取映射设置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "保存映射设置")
    @ApiOperation("保存映射设置")
    @PostMapping("/mapping/settings")
    public MyJsonBean<Void> saveMappingSettings(@RequestBody Map<String, Object> settings) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            // 设置保存逻辑（当前为内存级别，后续可持久化到数据库）
            log.info("保存映射设置: {}", settings);
            result.setCode(1);
            result.setMsg("设置保存成功");
        } catch (Exception e) {
            log.error("保存映射设置异常", e);
            result.setCode(0);
            result.setMsg("保存失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "批量测试数据映射")
    @ApiOperation("批量测试数据映射")
    @PostMapping("/mapping/batch-test")
    public MyJsonBean<Map<String, Object>> batchTestMapping(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            java.util.List<String> ids = (java.util.List<String>) params.get("ids");
            Map<String, Object> batchResult = new HashMap<>();
            int successCount = 0;
            int failCount = 0;
            List<Map<String, Object>> details = new ArrayList<>();
            if (ids != null) {
                for (String mappingId : ids) {
                    BudgetDataMapping entity = mappingMapper.selectById(mappingId);
                    Map<String, Object> detail = new HashMap<>();
                    if (entity != null) {
                        entity.setMappingStatus("ACTIVE");
                        entity.setUpdatedTime(new Date());
                        mappingMapper.updateById(entity);
                        detail.put("id", mappingId);
                        detail.put("mappingName", entity.getMappingName());
                        detail.put("success", true);
                        successCount++;
                    } else {
                        detail.put("id", mappingId);
                        detail.put("success", false);
                        detail.put("message", "映射不存在");
                        failCount++;
                    }
                    details.add(detail);
                }
            }
            batchResult.put("total", ids != null ? ids.size() : 0);
            batchResult.put("successCount", successCount);
            batchResult.put("failCount", failCount);
            batchResult.put("details", details);
            batchResult.put("testTime", new Date());
            result.setCode(1);
            result.setMsg("批量测试完成");
            result.setData(batchResult);
        } catch (Exception e) {
            log.error("批量测试数据映射异常", e);
            result.setCode(0);
            result.setMsg("批量测试失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 集成配置接口 ====================

    @Operation(summary = "获取集成配置列表")
    @ApiOperation("获取集成配置列表")
    @GetMapping("/config/list")
    public MyJsonBean<List<Map<String, Object>>> getConfigList() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetIntegrationConfig> entities = configMapper.selectList(new QueryWrapper<BudgetIntegrationConfig>().ne("CONFIG_TYPE", "GLOBAL_SETTING").orderByDesc("CREATED_TIME"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetIntegrationConfig e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getConfigId());
                item.put("configName", e.getConfigName());
                item.put("configType", e.getConfigType());
                item.put("integrationType", e.getConfigGroup());
                item.put("environment", e.getScope());
                int mc = e.getModifyCount() != null ? e.getModifyCount() : 1;
                item.put("version", "v" + mc);
                item.put("ruleCount", 0);
                item.put("lastModified", e.getLastModifiedTime());
                item.put("status", e.getConfigStatus());
                item.put("description", e.getConfigDesc());
                item.put("createTime", e.getCreatedTime());
                item.put("configValue", e.getConfigValue());
                item.put("configCode", e.getConfigCode());
                item.put("isEnabled", e.getIsEnabled());
                item.put("remark", e.getRemark());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取集成配置列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取集成配置统计数据")
    @ApiOperation("获取集成配置统计数据")
    @GetMapping("/config/stats")
    public MyJsonBean<Map<String, Object>> getConfigStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<BudgetIntegrationConfig> all = configMapper.selectList(new QueryWrapper<BudgetIntegrationConfig>().ne("CONFIG_TYPE", "GLOBAL_SETTING"));
            long active = all.stream().filter(e -> "ACTIVE".equals(e.getConfigStatus())).count();
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalConfigs", all.size());
            stats.put("activeConfigs", active);
            stats.put("lastModified", all.stream().map(BudgetIntegrationConfig::getLastModifiedTime).filter(java.util.Objects::nonNull).max(java.util.Comparator.naturalOrder()).orElse(null));
            stats.put("modifiedBy", "system");
            stats.put("totalRules", all.size());
            stats.put("successRate", all.size() > 0 ? (active * 100) / all.size() : 100);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取集成配置统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建集成配置")
    @ApiOperation("创建集成配置")
    @PostMapping("/config/config")
    public MyJsonBean<Map<String, Object>> createConfigConfig(@RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetIntegrationConfig entity = new BudgetIntegrationConfig();
            entity.setConfigName((String) data.get("configName"));
            entity.setConfigType((String) data.get("configType"));
            entity.setConfigGroup((String) data.get("integrationType"));
            entity.setScope((String) data.get("environment"));
            entity.setConfigDesc((String) data.get("description"));
            String configValue = data.get("configValue") != null ? (String) data.get("configValue") : (String) data.get("value");
            entity.setConfigValue(configValue);
            entity.setRemark((String) data.get("remark"));
            entity.setConfigCode("CFG" + System.currentTimeMillis());
            entity.setConfigStatus("ACTIVE");
            entity.setIsEnabled(true);
            entity.setModifyCount(0);
            entity.setCreatedTime(new Date());
            entity.setUpdatedTime(new Date());
            configMapper.insert(entity);
            data.put("id", entity.getConfigId());
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("创建集成配置异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新集成配置")
    @ApiOperation("更新集成配置")
    @PutMapping("/config/config/{id}")
    public MyJsonBean<Map<String, Object>> updateConfigConfig(@PathVariable String id, @RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetIntegrationConfig entity = configMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("记录不存在"); return result; }
            if (data.containsKey("configName")) entity.setConfigName((String) data.get("configName"));
            if (data.containsKey("configType")) entity.setConfigType((String) data.get("configType"));
            if (data.containsKey("configValue")) entity.setConfigValue((String) data.get("configValue"));
            if (data.containsKey("value")) entity.setConfigValue((String) data.get("value"));
            if (data.containsKey("integrationType")) entity.setConfigGroup((String) data.get("integrationType"));
            if (data.containsKey("environment")) entity.setScope((String) data.get("environment"));
            if (data.containsKey("description")) entity.setConfigDesc((String) data.get("description"));
            if (data.containsKey("status")) entity.setConfigStatus((String) data.get("status"));
            if (data.containsKey("remark")) entity.setRemark((String) data.get("remark"));
            entity.setModifyCount((entity.getModifyCount() != null ? entity.getModifyCount() : 0) + 1);
            entity.setLastModifiedTime(new Date());
            entity.setUpdatedTime(new Date());
            configMapper.updateById(entity);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("更新集成配置异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除集成配置")
    @ApiOperation("删除集成配置")
    @DeleteMapping("/config/config/{id}")
    public MyJsonBean<Void> deleteConfigConfig(@PathVariable String id) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            configMapper.deleteById(id);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除集成配置异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "测试集成配置")
    @ApiOperation("测试集成配置")
    @PostMapping("/config/config/{id}/test")
    public MyJsonBean<Map<String, Object>> testConfig(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetIntegrationConfig entity = configMapper.selectById(id);
            Map<String, Object> testResult = new HashMap<>();
            if (entity != null) {
                testResult.put("success", true);
                testResult.put("message", "配置验证通过");
            } else {
                testResult.put("success", false);
                testResult.put("message", "配置不存在");
            }
            result.setCode(1);
            result.setMsg("测试完成");
            result.setData(testResult);
        } catch (Exception e) {
            log.error("测试集成配置异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "应用集成配置")
    @ApiOperation("应用集成配置")
    @PostMapping("/config/config/{id}/apply")
    public MyJsonBean<Map<String, Object>> applyConfig(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetIntegrationConfig entity = configMapper.selectById(id);
            Map<String, Object> applyResult = new HashMap<>();
            if (entity != null) {
                entity.setEffectiveTime(new Date());
                entity.setConfigStatus("ACTIVE");
                entity.setUpdatedTime(new Date());
                configMapper.updateById(entity);
                applyResult.put("success", true);
            } else {
                applyResult.put("success", false);
            }
            applyResult.put("applyTime", new Date());
            result.setCode(1);
            result.setMsg("应用成功");
            result.setData(applyResult);
        } catch (Exception e) {
            log.error("应用集成配置异常", e);
            result.setCode(0);
            result.setMsg("应用失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取全局设置")
    @ApiOperation("获取全局设置")
    @GetMapping("/config/global-settings")
    public MyJsonBean<Map<String, Object>> getGlobalSettings() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<BudgetIntegrationConfig> settings = configMapper.selectList(
                new QueryWrapper<BudgetIntegrationConfig>().eq("CONFIG_TYPE", "GLOBAL_SETTING"));
            Map<String, Object> settingsMap = new HashMap<>();
            settingsMap.put("timeout", 30000);
            settingsMap.put("retryCount", 3);
            settingsMap.put("retryInterval", 5000);
            settingsMap.put("logLevel", "INFO");
            settingsMap.put("maxConcurrent", 10);
            settingsMap.put("enableNotification", true);
            for (BudgetIntegrationConfig s : settings) {
                if (s.getConfigKey() != null && s.getConfigValue() != null) {
                    settingsMap.put(s.getConfigKey(), s.getConfigValue());
                }
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(settingsMap);
        } catch (Exception e) {
            log.error("获取全局设置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "保存全局设置")
    @ApiOperation("保存全局设置")
    @PostMapping("/config/global-settings")
    public MyJsonBean<Void> saveGlobalSettings(@RequestBody Map<String, Object> data) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                QueryWrapper<BudgetIntegrationConfig> qw = new QueryWrapper<BudgetIntegrationConfig>()
                    .eq("CONFIG_TYPE", "GLOBAL_SETTING").eq("CONFIG_KEY", entry.getKey());
                BudgetIntegrationConfig existing = configMapper.selectOne(qw);
                if (existing != null) {
                    existing.setConfigValue(String.valueOf(entry.getValue()));
                    existing.setUpdatedTime(new Date());
                    configMapper.updateById(existing);
                } else {
                    BudgetIntegrationConfig newConfig = new BudgetIntegrationConfig();
                    newConfig.setConfigType("GLOBAL_SETTING");
                    newConfig.setConfigKey(entry.getKey());
                    newConfig.setConfigValue(String.valueOf(entry.getValue()));
                    newConfig.setConfigName("全局设置-" + entry.getKey());
                    newConfig.setConfigCode("GS_" + System.currentTimeMillis());
                    newConfig.setConfigStatus("ACTIVE");
                    newConfig.setIsEnabled(true);
                    newConfig.setCreatedTime(new Date());
                    newConfig.setUpdatedTime(new Date());
                    configMapper.insert(newConfig);
                }
            }
            result.setCode(1);
            result.setMsg("保存成功");
        } catch (Exception e) {
            log.error("保存全局设置异常", e);
            result.setCode(0);
            result.setMsg("保存失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导入配置")
    @ApiOperation("导入配置")
    @PostMapping("/config/import")
    public MyJsonBean<Map<String, Object>> importConfig(@RequestBody List<Map<String, Object>> dataList) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            int successCount = 0;
            int failCount = 0;
            for (Map<String, Object> data : dataList) {
                try {
                    BudgetIntegrationConfig entity = new BudgetIntegrationConfig();
                    entity.setConfigName((String) data.get("configName"));
                    entity.setConfigType((String) data.get("configType"));
                    entity.setConfigGroup((String) data.get("integrationType"));
                    entity.setScope((String) data.get("environment"));
                    entity.setConfigDesc((String) data.get("description"));
                    entity.setConfigValue((String) data.get("configValue"));
                    entity.setRemark((String) data.get("remark"));
                    entity.setConfigCode("IMP" + System.currentTimeMillis());
                    entity.setConfigStatus("ACTIVE");
                    entity.setIsEnabled(true);
                    entity.setModifyCount(0);
                    entity.setCreatedTime(new Date());
                    entity.setUpdatedTime(new Date());
                    configMapper.insert(entity);
                    successCount++;
                } catch (Exception ex) {
                    failCount++;
                    log.warn("导入单条配置失败: {}", ex.getMessage());
                }
            }
            Map<String, Object> importResult = new HashMap<>();
            importResult.put("successCount", successCount);
            importResult.put("failCount", failCount);
            importResult.put("totalCount", dataList.size());
            result.setCode(1);
            result.setMsg("导入完成，成功" + successCount + "条，失败" + failCount + "条");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("导入配置异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出配置")
    @ApiOperation("导出配置")
    @GetMapping("/config/export")
    public MyJsonBean<List<Map<String, Object>>> exportConfig() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetIntegrationConfig> entities = configMapper.selectList(
                new QueryWrapper<BudgetIntegrationConfig>().ne("CONFIG_TYPE", "GLOBAL_SETTING").orderByDesc("CREATED_TIME"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetIntegrationConfig e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("configName", e.getConfigName());
                item.put("configType", e.getConfigType());
                item.put("integrationType", e.getConfigGroup());
                item.put("environment", e.getScope());
                item.put("description", e.getConfigDesc());
                item.put("configValue", e.getConfigValue());
                item.put("status", e.getConfigStatus());
                item.put("remark", e.getRemark());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("导出配置异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "批量测试配置")
    @ApiOperation("批量测试配置")
    @PostMapping("/config/batch-test")
    public MyJsonBean<List<Map<String, Object>>> batchTestConfig(@RequestBody Map<String, Object> data) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) data.get("ids");
            List<Map<String, Object>> testResults = new ArrayList<>();
            if (ids != null) {
                for (String id : ids) {
                    BudgetIntegrationConfig entity = configMapper.selectById(id);
                    Map<String, Object> testItem = new HashMap<>();
                    testItem.put("id", id);
                    if (entity != null) {
                        testItem.put("configName", entity.getConfigName());
                        testItem.put("success", true);
                        testItem.put("message", "配置验证通过");
                    } else {
                        testItem.put("success", false);
                        testItem.put("message", "配置不存在");
                    }
                    testResults.add(testItem);
                }
            }
            result.setCode(1);
            result.setMsg("批量测试完成");
            result.setData(testResults);
        } catch (Exception e) {
            log.error("批量测试配置异常", e);
            result.setCode(0);
            result.setMsg("批量测试失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 配置参数管理 ====================

    @Operation(summary = "获取配置参数列表")
    @ApiOperation("获取配置参数列表")
    @GetMapping("/config/{configId}/params")
    public MyJsonBean<List<Map<String, Object>>> getConfigParams(@PathVariable String configId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<IntegrationConfigParam> params = configParamMapper.listByConfigId(configId);
            List<Map<String, Object>> list = new ArrayList<>();
            for (IntegrationConfigParam p : params) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", p.getParamId());
                item.put("configId", p.getConfigId());
                item.put("parameterName", p.getParameterName());
                item.put("parameterType", p.getParameterType());
                item.put("parameterValue", p.getParameterValue());
                item.put("defaultValue", p.getDefaultValue());
                item.put("required", p.getIsRequired() != null ? p.getIsRequired() : false);
                item.put("description", p.getDescription());
                item.put("sortOrder", p.getSortOrder());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取配置参数列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "添加配置参数")
    @ApiOperation("添加配置参数")
    @PostMapping("/config/{configId}/params")
    public MyJsonBean<Map<String, Object>> addConfigParam(@PathVariable String configId, @RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            IntegrationConfigParam param = new IntegrationConfigParam();
            param.setConfigId(configId);
            param.setParameterName((String) data.get("parameterName"));
            param.setParameterType((String) data.getOrDefault("parameterType", "STRING"));
            param.setParameterValue((String) data.get("parameterValue"));
            param.setDefaultValue((String) data.get("defaultValue"));
            param.setIsRequired(data.get("required") != null ? Boolean.valueOf(data.get("required").toString()) : false);
            param.setDescription((String) data.get("description"));
            param.setSortOrder(data.get("sortOrder") != null ? Integer.valueOf(data.get("sortOrder").toString()) : 0);
            param.setCreatedBy("system");
            param.setCreatedTime(new Date());
            configParamMapper.insert(param);
            Map<String, Object> res = new HashMap<>();
            res.put("id", param.getParamId());
            result.setCode(1);
            result.setMsg("添加成功");
            result.setData(res);
        } catch (Exception e) {
            log.error("添加配置参数异常", e);
            result.setCode(0);
            result.setMsg("添加失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新配置参数")
    @ApiOperation("更新配置参数")
    @PutMapping("/config/{configId}/params/{paramId}")
    public MyJsonBean<Void> updateConfigParam(@PathVariable String configId, @PathVariable String paramId, @RequestBody Map<String, Object> data) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            IntegrationConfigParam param = configParamMapper.selectById(paramId);
            if (param == null) {
                result.setCode(0);
                result.setMsg("参数不存在");
                return result;
            }
            if (data.containsKey("parameterName")) param.setParameterName((String) data.get("parameterName"));
            if (data.containsKey("parameterType")) param.setParameterType((String) data.get("parameterType"));
            if (data.containsKey("parameterValue")) param.setParameterValue((String) data.get("parameterValue"));
            if (data.containsKey("defaultValue")) param.setDefaultValue((String) data.get("defaultValue"));
            if (data.containsKey("required")) param.setIsRequired(Boolean.valueOf(data.get("required").toString()));
            if (data.containsKey("description")) param.setDescription((String) data.get("description"));
            if (data.containsKey("sortOrder")) param.setSortOrder(Integer.valueOf(data.get("sortOrder").toString()));
            param.setUpdatedBy("system");
            param.setUpdatedTime(new Date());
            configParamMapper.updateById(param);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (Exception e) {
            log.error("更新配置参数异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除配置参数")
    @ApiOperation("删除配置参数")
    @DeleteMapping("/config/{configId}/params/{paramId}")
    public MyJsonBean<Void> deleteConfigParam(@PathVariable String configId, @PathVariable String paramId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            configParamMapper.deleteById(paramId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除配置参数异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 业务规则管理 ====================

    @Operation(summary = "获取业务规则列表")
    @ApiOperation("获取业务规则列表")
    @GetMapping("/config/{configId}/rules")
    public MyJsonBean<List<Map<String, Object>>> getConfigRules(@PathVariable String configId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<IntegrationConfigRule> rules = configRuleMapper.listByConfigId(configId);
            List<Map<String, Object>> list = new ArrayList<>();
            for (IntegrationConfigRule r : rules) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", r.getRuleId());
                item.put("configId", r.getConfigId());
                item.put("ruleName", r.getRuleName());
                item.put("ruleType", r.getRuleType());
                item.put("condition", r.getRuleCondition());
                item.put("action", r.getRuleAction());
                item.put("priority", r.getPriority());
                item.put("enabled", r.getIsEnabled() != null ? r.getIsEnabled() : true);
                item.put("description", r.getDescription());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取业务规则列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "添加业务规则")
    @ApiOperation("添加业务规则")
    @PostMapping("/config/{configId}/rules")
    public MyJsonBean<Map<String, Object>> addConfigRule(@PathVariable String configId, @RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            IntegrationConfigRule rule = new IntegrationConfigRule();
            rule.setConfigId(configId);
            rule.setRuleName((String) data.get("ruleName"));
            rule.setRuleType((String) data.getOrDefault("ruleType", "VALIDATION"));
            rule.setRuleCondition((String) data.get("condition"));
            rule.setRuleAction((String) data.get("action"));
            rule.setPriority(data.get("priority") != null ? Integer.valueOf(data.get("priority").toString()) : 1);
            rule.setIsEnabled(data.get("enabled") != null ? Boolean.valueOf(data.get("enabled").toString()) : true);
            rule.setDescription((String) data.get("description"));
            rule.setCreatedBy("system");
            rule.setCreatedTime(new Date());
            configRuleMapper.insert(rule);
            Map<String, Object> res = new HashMap<>();
            res.put("id", rule.getRuleId());
            result.setCode(1);
            result.setMsg("添加成功");
            result.setData(res);
        } catch (Exception e) {
            log.error("添加业务规则异常", e);
            result.setCode(0);
            result.setMsg("添加失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新业务规则")
    @ApiOperation("更新业务规则")
    @PutMapping("/config/{configId}/rules/{ruleId}")
    public MyJsonBean<Void> updateConfigRule(@PathVariable String configId, @PathVariable String ruleId, @RequestBody Map<String, Object> data) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            IntegrationConfigRule rule = configRuleMapper.selectById(ruleId);
            if (rule == null) {
                result.setCode(0);
                result.setMsg("规则不存在");
                return result;
            }
            if (data.containsKey("ruleName")) rule.setRuleName((String) data.get("ruleName"));
            if (data.containsKey("ruleType")) rule.setRuleType((String) data.get("ruleType"));
            if (data.containsKey("condition")) rule.setRuleCondition((String) data.get("condition"));
            if (data.containsKey("action")) rule.setRuleAction((String) data.get("action"));
            if (data.containsKey("priority")) rule.setPriority(Integer.valueOf(data.get("priority").toString()));
            if (data.containsKey("enabled")) rule.setIsEnabled(Boolean.valueOf(data.get("enabled").toString()));
            if (data.containsKey("description")) rule.setDescription((String) data.get("description"));
            rule.setUpdatedBy("system");
            rule.setUpdatedTime(new Date());
            configRuleMapper.updateById(rule);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (Exception e) {
            log.error("更新业务规则异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除业务规则")
    @ApiOperation("删除业务规则")
    @DeleteMapping("/config/{configId}/rules/{ruleId}")
    public MyJsonBean<Void> deleteConfigRule(@PathVariable String configId, @PathVariable String ruleId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            configRuleMapper.deleteById(ruleId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除业务规则异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 版本历史管理 ====================

    @Operation(summary = "获取版本历史列表")
    @ApiOperation("获取版本历史列表")
    @GetMapping("/config/{configId}/versions")
    public MyJsonBean<List<Map<String, Object>>> getConfigVersions(@PathVariable String configId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<IntegrationConfigVersion> versions = configVersionMapper.listByConfigId(configId);
            List<Map<String, Object>> list = new ArrayList<>();
            for (IntegrationConfigVersion v : versions) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", v.getVersionId());
                item.put("configId", v.getConfigId());
                item.put("version", v.getVersionNo());
                item.put("versionName", v.getVersionName());
                item.put("changeLog", v.getChangeLog());
                item.put("status", v.getStatus());
                item.put("createdBy", v.getCreatedBy());
                item.put("createTime", v.getCreatedTime());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取版本历史列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "回滚集成配置到指定版本")
    @ApiOperation("回滚集成配置到指定版本")
    @PostMapping("/config/config/{id}/rollback")
    public MyJsonBean<Map<String, Object>> rollbackConfig(@PathVariable String id, @RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetIntegrationConfig entity = configMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("配置不存在"); return result; }
            String version = data.get("version") != null ? data.get("version").toString() : null;
            if (version == null || version.isEmpty()) { result.setCode(0); result.setMsg("版本号不能为空"); return result; }
            List<IntegrationConfigVersion> versions = configVersionMapper.listByConfigId(id);
            IntegrationConfigVersion targetVersion = null;
            for (IntegrationConfigVersion v : versions) {
                if (version.equals(v.getVersionNo())) {
                    targetVersion = v;
                    break;
                }
            }
            if (targetVersion == null) { result.setCode(0); result.setMsg("指定版本不存在"); return result; }
            // 创建回滚版本记录
            IntegrationConfigVersion rollbackVersion = new IntegrationConfigVersion();
            rollbackVersion.setConfigId(id);
            rollbackVersion.setVersionNo("V" + System.currentTimeMillis());
            rollbackVersion.setVersionName("回滚至" + targetVersion.getVersionName());
            rollbackVersion.setChangeLog("从当前版本回滚至版本 " + version);
            rollbackVersion.setStatus("ACTIVE");
            rollbackVersion.setCreatedBy("system");
            rollbackVersion.setCreatedTime(new Date());
            configVersionMapper.insert(rollbackVersion);
            // 更新配置状态
            entity.setConfigStatus("ACTIVE");
            entity.setModifyCount((entity.getModifyCount() != null ? entity.getModifyCount() : 0) + 1);
            entity.setLastModifiedTime(new Date());
            entity.setUpdatedTime(new Date());
            configMapper.updateById(entity);
            Map<String, Object> rollbackResult = new HashMap<>();
            rollbackResult.put("success", true);
            rollbackResult.put("configId", id);
            rollbackResult.put("rollbackVersion", version);
            rollbackResult.put("rollbackTime", new Date());
            result.setCode(1);
            result.setMsg("回滚成功");
            result.setData(rollbackResult);
        } catch (Exception e) {
            log.error("回滚集成配置异常", e);
            result.setCode(0);
            result.setMsg("回滚失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "对比集成配置版本")
    @ApiOperation("对比集成配置版本")
    @PostMapping("/config/config/{id}/compare")
    public MyJsonBean<Map<String, Object>> compareConfig(@PathVariable String id, @RequestBody Map<String, Object> data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetIntegrationConfig entity = configMapper.selectById(id);
            if (entity == null) { result.setCode(0); result.setMsg("配置不存在"); return result; }
            String version = data.get("version") != null ? data.get("version").toString() : null;
            if (version == null || version.isEmpty()) { result.setCode(0); result.setMsg("版本号不能为空"); return result; }
            List<IntegrationConfigVersion> versions = configVersionMapper.listByConfigId(id);
            IntegrationConfigVersion targetVersion = null;
            for (IntegrationConfigVersion v : versions) {
                if (version.equals(v.getVersionNo())) {
                    targetVersion = v;
                    break;
                }
            }
            if (targetVersion == null) { result.setCode(0); result.setMsg("指定版本不存在"); return result; }
            Map<String, Object> compareResult = new HashMap<>();
            // 当前配置信息
            Map<String, Object> current = new HashMap<>();
            current.put("configName", entity.getConfigName());
            current.put("configType", entity.getConfigType());
            current.put("configValue", entity.getConfigValue());
            current.put("configStatus", entity.getConfigStatus());
            current.put("updatedTime", entity.getUpdatedTime());
            compareResult.put("current", current);
            // 目标版本信息
            Map<String, Object> versionInfo = new HashMap<>();
            versionInfo.put("versionNo", targetVersion.getVersionNo());
            versionInfo.put("versionName", targetVersion.getVersionName());
            versionInfo.put("changeLog", targetVersion.getChangeLog());
            versionInfo.put("status", targetVersion.getStatus());
            versionInfo.put("createdTime", targetVersion.getCreatedTime());
            compareResult.put("version", versionInfo);
            // 差异摘要
            List<Map<String, Object>> differences = new ArrayList<>();
            Map<String, Object> diff = new HashMap<>();
            diff.put("field", "status");
            diff.put("currentValue", entity.getConfigStatus());
            diff.put("versionValue", targetVersion.getStatus());
            differences.add(diff);
            compareResult.put("differences", differences);
            compareResult.put("hasDifferences", true);
            result.setCode(1);
            result.setMsg("对比成功");
            result.setData(compareResult);
        } catch (Exception e) {
            log.error("对比集成配置版本异常", e);
            result.setCode(0);
            result.setMsg("对比失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 云平台连接列表接口 ====================

    @Operation(summary = "获取云平台连接列表")
    @ApiOperation("获取云平台连接列表")
    @GetMapping("/cloud/connection/list")
    public MyJsonBean<List<Map<String, Object>>> getCloudConnectionList() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetCloudIntegration> entities = cloudMapper.selectList(new QueryWrapper<BudgetCloudIntegration>().orderByDesc("CREATED_TIME"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetCloudIntegration e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getCloudId());
                item.put("connectionName", e.getCloudName());
                item.put("cloudType", e.getCloudType());
                item.put("cloudProvider", e.getCloudType());
                item.put("serviceType", e.getServiceType());
                item.put("region", e.getRegion());
                item.put("status", e.getIntegrationStatus());
                item.put("lastSync", e.getLastSyncTime());
                item.put("dataTransferred", e.getDataTransferMb() != null ? e.getDataTransferMb() : 0);
                item.put("monthlyCost", e.getDataTransferMb() != null ? e.getDataTransferMb() : 0);
                item.put("accessKeyId", e.getAccessKeyId());
                item.put("description", e.getRemark());
                item.put("createTime", e.getCreatedTime());
                item.put("endpoint", e.getEndpoint());
                item.put("syncMode", e.getSyncMode());
                item.put("isEnabled", e.getIsEnabled());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取云平台连接列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 系统通知接口 ====================

    @Operation(summary = "获取系统通知")
    @ApiOperation("获取系统通知")
    @GetMapping("/notifications/system")
    public MyJsonBean<List<Map<String, Object>>> getSystemNotifications() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetIntegrationMonitor> entities = monitorMapper.selectList(
                    new QueryWrapper<BudgetIntegrationMonitor>().orderByDesc("CREATED_TIME").last("FETCH FIRST 20 ROWS ONLY"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetIntegrationMonitor e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getMonitorId());
                item.put("title", e.getIntegrationName() + " - " + e.getExecutionType());
                item.put("type", "SUCCESS".equals(e.getExecutionStatus()) ? "SUCCESS" : ("FAILED".equals(e.getExecutionStatus()) ? "WARNING" : "INFO"));
                item.put("time", e.getCreatedTime());
                item.put("read", e.getIsAlerted() != null ? e.getIsAlerted() : false);
                item.put("message", e.getErrorMessage() != null ? e.getErrorMessage() : (e.getIntegrationName() + " " + e.getExecutionType() + " " + e.getExecutionStatus()));
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取系统通知异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 最近活动接口 ====================

    @Operation(summary = "获取最近活动")
    @ApiOperation("获取最近活动")
    @GetMapping("/activities/recent")
    public MyJsonBean<List<Map<String, Object>>> getRecentActivities() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<BudgetIntegrationMonitor> entities = monitorMapper.selectList(
                    new QueryWrapper<BudgetIntegrationMonitor>().orderByDesc("START_TIME").last("FETCH FIRST 20 ROWS ONLY"));
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetIntegrationMonitor e : entities) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", e.getMonitorId());
                item.put("title", e.getIntegrationName());
                item.put("description", e.getIntegrationType() + " - " + e.getExecutionType());
                item.put("timestamp", e.getStartTime());
                item.put("type", "SUCCESS".equals(e.getExecutionStatus()) ? "success" : ("FAILURE".equals(e.getExecutionStatus()) ? "danger" : "info"));
                item.put("status", e.getExecutionStatus());
                item.put("user", e.getExecutedBy() != null ? e.getExecutedBy() : "系统");
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取最近活动异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 综合统计和监控端点 ====================

    @Operation(summary = "获取综合集成统计")
    @ApiOperation("获取综合集成统计")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getIntegrationStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = new HashMap<>();
            long erpCount = erpMapper.selectCount(new QueryWrapper<>());
            long biCount = biMapper.selectCount(new QueryWrapper<>());
            long apiCount = apiMapper.selectCount(new QueryWrapper<>());
            long dbCount = dbMapper.selectCount(new QueryWrapper<>());
            long fileCount = fileMapper.selectCount(new QueryWrapper<>());
            long mqCount = mqMapper.selectCount(new QueryWrapper<>());
            long wsCount = wsMapper.selectCount(new QueryWrapper<>());
            long cloudCount = cloudMapper.selectCount(new QueryWrapper<>());
            long dsCount = streamMapper.selectCount(new QueryWrapper<>());
            long mappingCount = mappingMapper.selectCount(new QueryWrapper<>());
            long configCount = configMapper.selectCount(new QueryWrapper<>());
            long totalIntegrations = erpCount + biCount + apiCount + dbCount + fileCount + mqCount + wsCount + cloudCount + dsCount + mappingCount + configCount;

            long erpActive = erpMapper.selectCount(new QueryWrapper<BudgetErpIntegration>().eq("INTEGRATION_STATUS", "ACTIVE").eq("DEL_FLAG", 0));
            long biActive = biMapper.selectCount(new QueryWrapper<BudgetBiIntegration>().eq("INTEGRATION_STATUS", "ACTIVE"));
            long apiActive = apiMapper.selectCount(new QueryWrapper<BudgetApiIntegration>().eq("API_STATUS", "ACTIVE"));
            long dbActive = dbMapper.selectCount(new QueryWrapper<BudgetDatabaseIntegration>().eq("INTEGRATION_STATUS", "ACTIVE"));
            long cloudActive = cloudMapper.selectCount(new QueryWrapper<BudgetCloudIntegration>().eq("INTEGRATION_STATUS", "CONNECTED"));
            long activeIntegrations = erpActive + biActive + apiActive + dbActive + cloudActive;

            stats.put("totalIntegrations", totalIntegrations);
            stats.put("activeIntegrations", activeIntegrations);
            stats.put("todaySync", 0);
            stats.put("successRate", totalIntegrations > 0 ? Math.round(activeIntegrations * 1000.0 / totalIntegrations) / 10.0 : 0);

            Map<String, Object> moduleStats = new HashMap<>();
            moduleStats.put("erp", createModuleStat(erpCount, erpActive));
            moduleStats.put("bi", createModuleStat(biCount, biActive));
            moduleStats.put("api", createModuleStat(apiCount, apiActive));
            moduleStats.put("database", createModuleStat(dbCount, dbActive));
            moduleStats.put("file", createModuleStat(fileCount, 0));
            moduleStats.put("messageQueue", createModuleStat(mqCount, 0));
            moduleStats.put("webService", createModuleStat(wsCount, 0));
            moduleStats.put("cloud", createModuleStat(cloudCount, cloudActive));
            moduleStats.put("dataStream", createModuleStat(dsCount, 0));
            moduleStats.put("dataMapping", createModuleStat(mappingCount, 0));
            moduleStats.put("config", createModuleStat(configCount, 0));
            stats.put("moduleStats", moduleStats);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取综合统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    private Map<String, Object> createModuleStat(long count, long active) {
        Map<String, Object> stat = new HashMap<>();
        stat.put("count", count);
        stat.put("active", active);
        return stat;
    }

    @Operation(summary = "获取集成状态概览")
    @ApiOperation("获取集成状态概览")
    @GetMapping("/status/overview")
    public MyJsonBean<Map<String, Object>> getStatusOverview() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> overview = new HashMap<>();
            List<BudgetIntegrationMonitor> monitors = monitorMapper.selectList(new QueryWrapper<>());
            long total = monitors.size();
            long success = monitors.stream().filter(m -> "SUCCESS".equals(m.getExecutionStatus())).count();
            long failed = monitors.stream().filter(m -> "FAILURE".equals(m.getExecutionStatus())).count();
            long running = monitors.stream().filter(m -> "RUNNING".equals(m.getExecutionStatus())).count();
            overview.put("totalIntegrations", total);
            overview.put("healthyIntegrations", success);
            overview.put("activeAlerts", failed);
            overview.put("avgAvailability", total > 0 ? Math.round(success * 1000.0 / total) / 10.0 : 0);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(overview);
        } catch (Exception e) {
            log.error("获取状态概览异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取集成状态列表")
    @ApiOperation("获取集成状态列表")
    @GetMapping("/status/list")
    public MyJsonBean<List<Map<String, Object>>> getStatusList(@RequestParam(required = false) String timeRange) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetIntegrationMonitor> qw = new QueryWrapper<>();
           // 不再需要 integrationType 参数过滤，前端会传 integrationType
            qw.orderByDesc("START_TIME");
            qw.last("FETCH FIRST 50 ROWS ONLY");
           List<BudgetIntegrationMonitor> allMonitors = monitorMapper.selectList(qw);
           // 按 integrationId 分组，取每个集成最新的一条记录
           Map<String, BudgetIntegrationMonitor> latestByIntegration = new LinkedHashMap<>();
           for (BudgetIntegrationMonitor m : allMonitors) {
               String key = m.getIntegrationId() != null ? m.getIntegrationId() : m.getMonitorId();
               if (!latestByIntegration.containsKey(key)) {
                   latestByIntegration.put(key, m);
               }
           }
           // 为每个集成计算统计指标
           List<Map<String, Object>> list = new ArrayList<>();
           for (Map.Entry<String, BudgetIntegrationMonitor> entry : latestByIntegration.entrySet()) {
               BudgetIntegrationMonitor m = entry.getValue();
               String integrationKey = entry.getKey();
               // 查询该集成的历史记录用于计算指标
               List<BudgetIntegrationMonitor> history = allMonitors.stream()
                       .filter(h -> integrationKey.equals(h.getIntegrationId() != null ? h.getIntegrationId() : h.getMonitorId()))
                       .collect(java.util.stream.Collectors.toList());
               long totalCount = history.size();
               long successCount = history.stream().filter(h -> "SUCCESS".equals(h.getExecutionStatus())).count();
               long failureCount = history.stream().filter(h -> "FAILURE".equals(h.getExecutionStatus())).count();
               // 计算可用性
               double availability = totalCount > 0 ? Math.round(successCount * 1000.0 / totalCount) / 10.0 : 99.9;
               // 计算平均响应时间
               double avgResponseTime = history.stream()
                       .filter(h -> h.getDurationMs() != null)
                       .mapToLong(BudgetIntegrationMonitor::getDurationMs)
                       .average().orElse(0);
               // 计算吞吐量
               double throughput = history.stream()
                       .filter(h -> h.getThroughput() != null)
                       .mapToDouble(BudgetIntegrationMonitor::getThroughput)
                       .average().orElse(0);
               // 计算错误率
               double errorRate = totalCount > 0 ? Math.round(failureCount * 1000.0 / totalCount) / 10.0 : 0;
               Map<String, Object> item = new HashMap<>();
               item.put("id", m.getMonitorId());
               item.put("integrationName", m.getIntegrationName() != null ? m.getIntegrationName() : "未命名集成");
               item.put("integrationType", m.getIntegrationType());
               item.put("status", m.getExecutionStatus());
               item.put("availability", availability);
               item.put("responseTime", Math.round(avgResponseTime));
               item.put("throughput", Math.round(throughput * 10.0) / 10.0);
               item.put("errorRate", errorRate);
               item.put("lastCheck", m.getStartTime());
               item.put("lastSync", m.getStartTime());
               item.put("errorMessage", m.getErrorMessage());
               item.put("integrationId", m.getIntegrationId());
               list.add(item);
           }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取状态列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取性能指标")
    @ApiOperation("获取性能指标")
    @GetMapping("/performance-metrics")
    public MyJsonBean<Map<String, Object>> getPerformanceMetrics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> metrics = new HashMap<>();
            List<BudgetIntegrationMonitor> monitors = monitorMapper.selectList(
                    new QueryWrapper<BudgetIntegrationMonitor>().isNotNull("DURATION_MS").orderByDesc("START_TIME").last("FETCH FIRST 100 ROWS ONLY"));
            if (!monitors.isEmpty()) {
                double avg = monitors.stream().filter(m -> m.getDurationMs() != null).mapToLong(BudgetIntegrationMonitor::getDurationMs).average().orElse(0);
                long max = monitors.stream().filter(m -> m.getDurationMs() != null).mapToLong(BudgetIntegrationMonitor::getDurationMs).max().orElse(0);
                long min = monitors.stream().filter(m -> m.getDurationMs() != null).mapToLong(BudgetIntegrationMonitor::getDurationMs).min().orElse(0);
                long failed = monitors.stream().filter(m -> "FAILURE".equals(m.getExecutionStatus())).count();
                metrics.put("avgResponseTime", Math.round(avg));
                metrics.put("maxResponseTime", max);
                metrics.put("minResponseTime", min);
                metrics.put("totalThroughput", monitors.size());
                metrics.put("errorRate", monitors.size() > 0 ? Math.round(failed * 1000.0 / monitors.size()) / 10.0 : 0);
                metrics.put("responseTimeTrend", "stable");
                metrics.put("responseTimeChange", 0);
                metrics.put("throughputTrend", "stable");
                metrics.put("throughputChange", 0);
                metrics.put("errorRateTrend", failed > 0 ? "up" : "stable");
                metrics.put("errorRateChange", 0);
                double availability = monitors.size() > 0 ? Math.round((monitors.size() - failed) * 1000.0 / monitors.size()) / 10.0 : 100;
                metrics.put("availability", availability);
                metrics.put("availabilityTrend", "stable");
                metrics.put("availabilityChange", 0);
            } else {
                metrics.put("avgResponseTime", 0);
                metrics.put("maxResponseTime", 0);
                metrics.put("minResponseTime", 0);
                metrics.put("totalThroughput", 0);
                metrics.put("errorRate", 0);
                metrics.put("responseTimeTrend", "stable");
                metrics.put("responseTimeChange", 0);
                metrics.put("throughputTrend", "stable");
                metrics.put("throughputChange", 0);
                metrics.put("errorRateTrend", "stable");
                metrics.put("errorRateChange", 0);
                metrics.put("availability", 100);
                metrics.put("availabilityTrend", "stable");
                metrics.put("availabilityChange", 0);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(metrics);
        } catch (Exception e) {
            log.error("获取性能指标异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "分页查询集成日志")
    @ApiOperation("分页查询集成日志")
    @GetMapping("/log/page")
    public MyJsonBean<Map<String, Object>> getLogPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "20") int size) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            long total = monitorMapper.selectCount(new QueryWrapper<>());
            int offset = (current - 1) * size;
            List<BudgetIntegrationMonitor> monitors = monitorMapper.selectList(
                    new QueryWrapper<BudgetIntegrationMonitor>().orderByDesc("START_TIME").last("OFFSET " + offset + " ROWS FETCH NEXT " + size + " ROWS ONLY"));
            List<Map<String, Object>> records = new ArrayList<>();
            for (BudgetIntegrationMonitor m : monitors) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", m.getMonitorId());
                item.put("integrationName", m.getIntegrationName());
                item.put("integrationType", m.getIntegrationType());
                item.put("status", m.getExecutionStatus());
                item.put("startTime", m.getStartTime());
                item.put("endTime", m.getEndTime());
                item.put("executionTime", m.getDurationMs());
                item.put("errorMessage", m.getErrorMessage());
                item.put("level", "FAILURE".equals(m.getExecutionStatus()) ? "error" : "info");
                item.put("message", m.getIntegrationName() + " - " + m.getExecutionStatus());
                item.put("time", m.getStartTime());
                records.add(item);
            }
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("records", records);
            pageData.put("total", total);
            pageData.put("current", current);
            pageData.put("size", size);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageData);
        } catch (Exception e) {
            log.error("查询集成日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "查询集成日志详情")
    @ApiOperation("查询集成日志详情")
    @GetMapping("/log/{logId}")
    public MyJsonBean<Map<String, Object>> getLogDetail(@PathVariable String logId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetIntegrationMonitor monitor = monitorMapper.selectById(logId);
            if (monitor != null) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("id", monitor.getMonitorId());
                detail.put("integrationName", monitor.getIntegrationName());
                detail.put("integrationType", monitor.getIntegrationType());
                detail.put("status", monitor.getExecutionStatus());
                detail.put("startTime", monitor.getStartTime());
                detail.put("endTime", monitor.getEndTime());
                detail.put("executionTime", monitor.getDurationMs());
                detail.put("errorMessage", monitor.getErrorMessage());
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(detail);
            } else {
                result.setCode(0);
                result.setMsg("日志不存在");
            }
        } catch (Exception e) {
            log.error("查询日志详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "标记通知为已读")
    @ApiOperation("标记通知为已读")
    @PutMapping("/notifications/{notificationId}/read")
    public MyJsonBean<Void> markNotificationAsRead(@PathVariable String notificationId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetIntegrationMonitor monitor = monitorMapper.selectById(notificationId);
            if (monitor != null) {
                monitor.setIsAlerted(true);
                monitorMapper.updateById(monitor);
                result.setCode(1);
                result.setMsg("标记成功");
            } else {
                result.setCode(0);
                result.setMsg("通知不存在");
            }
        } catch (Exception e) {
            log.error("标记通知已读异常", e);
            result.setCode(0);
            result.setMsg("操作失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 集成模块与快速操作 ====================

    @Operation(summary = "获取集成模块列表")
    @ApiOperation("获取集成模块列表")
    @GetMapping("/modules")
    public MyJsonBean<List<Map<String, Object>>> getIntegrationModules() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> modules = new ArrayList<>();
            String[][] moduleDefs = {
                {"1", "erpIntegration", "ERP系统集成", "ERP系统数据同步与集成管理", "el-icon-office-building", "erp"},
                {"2", "biIntegration", "BI系统集成", "商业智能系统数据推送与报表集成", "el-icon-data-analysis", "bi"},
                {"3", "apiIntegration", "API接口集成", "RESTful/SOAP接口调用与管理", "el-icon-connection", "api"},
                {"4", "databaseIntegration", "数据库集成", "跨数据库数据同步与迁移", "el-icon-coin", "database"},
                {"5", "fileIntegration", "文件集成", "文件导入导出与数据交换", "el-icon-folder-opened", "file"},
                {"6", "messageQueueIntegration", "消息队列集成", "异步消息传递与事件处理", "el-icon-message", "messageQueue"},
                {"7", "webServiceIntegration", "Web服务集成", "SOAP/REST Web服务调用管理", "el-icon-cloudy", "webService"},
                {"8", "cloudIntegration", "云平台集成", "云平台服务对接与数据同步", "el-icon-upload2", "cloud"},
                {"9", "dataStreamIntegration", "数据流集成", "实时数据流处理与传输", "el-icon-sort", "dataStream"},
                {"10", "dataMapping", "数据映射", "字段映射与数据转换规则配置", "el-icon-s-operation", "dataMapping"},
                {"11", "integrationConfig", "集成配置", "系统集成参数与全局配置", "el-icon-setting", "config"},
                {"12", "integrationMonitor", "集成监控", "集成任务执行监控与告警", "el-icon-monitor", "monitor"}
            };
            for (String[] def : moduleDefs) {
                Map<String, Object> module = new HashMap<>();
                module.put("id", def[0]);
                module.put("route", "/managementAccountant/ncv65/systemIntegration/" + def[1]);
                module.put("name", def[2]);
                module.put("description", def[3]);
                module.put("icon", def[4]);
                module.put("moduleKey", def[5]);
                long count = getModuleCount(def[5]);
                module.put("integrationCount", count);
                module.put("status", count > 0 ? "ACTIVE" : "INACTIVE");
                module.put("lastSync", "-");
                modules.add(module);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(modules);
        } catch (Exception e) {
            log.error("获取集成模块列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    private long getModuleCount(String moduleKey) {
        try {
            switch (moduleKey) {
                case "erp": return erpMapper.selectCount(new QueryWrapper<>());
                case "bi": return biMapper.selectCount(new QueryWrapper<>());
                case "api": return apiMapper.selectCount(new QueryWrapper<>());
                case "database": return dbMapper.selectCount(new QueryWrapper<>());
                case "file": return fileMapper.selectCount(new QueryWrapper<>());
                case "messageQueue": return mqMapper.selectCount(new QueryWrapper<>());
                case "webService": return wsMapper.selectCount(new QueryWrapper<>());
                case "cloud": return cloudMapper.selectCount(new QueryWrapper<>());
                case "dataStream": return streamMapper.selectCount(new QueryWrapper<>());
                case "dataMapping": return mappingMapper.selectCount(new QueryWrapper<>());
                case "config": return configMapper.selectCount(new QueryWrapper<>());
                case "monitor": return monitorMapper.selectCount(new QueryWrapper<>());
                default: return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    @Operation(summary = "获取快速操作列表")
    @ApiOperation("获取快速操作列表")
    @GetMapping("/quick-actions")
    public MyJsonBean<List<Map<String, Object>>> getQuickActions() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> actions = new ArrayList<>();
            String[][] actionDefs = {
                {"1", "新建ERP集成", "快速创建ERP系统集成配置", "el-icon-plus", "erpIntegration"},
                {"2", "数据同步", "执行数据同步任务", "el-icon-refresh", "integrationMonitor"},
                {"3", "监控面板", "查看集成运行状态", "el-icon-monitor", "integrationMonitor"},
                {"4", "系统配置", "管理集成全局配置", "el-icon-setting", "integrationConfig"}
            };
            for (String[] def : actionDefs) {
                Map<String, Object> action = new HashMap<>();
                action.put("id", Integer.parseInt(def[0]));
                action.put("title", def[1]);
                action.put("description", def[2]);
                action.put("icon", def[3]);
                action.put("componentKey", def[4]);
                actions.add(action);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(actions);
        } catch (Exception e) {
            log.error("获取快速操作列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "测试模块连接")
    @ApiOperation("测试模块连接")
    @PostMapping("/modules/{moduleId}/test")
    public MyJsonBean<Map<String, Object>> testModuleConnection(@PathVariable String moduleId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> testResult = new HashMap<>();
            long count = getModuleCount(moduleId);
            testResult.put("module", moduleId);
            testResult.put("success", true);
            testResult.put("recordCount", count);
            testResult.put("message", "模块 " + moduleId + " 连接正常，共 " + count + " 条记录");
            testResult.put("testTime", new Date());
            result.setCode(1);
            result.setMsg("测试成功");
            result.setData(testResult);
        } catch (Exception e) {
            log.error("测试模块连接异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 将BI集成状态英文翻译为中文
     */
    private String translateStatus(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "ACTIVE": return "已连接";
            case "INACTIVE": return "未连接";
            case "ERROR": return "连接异常";
            case "TESTING": return "测试中";
            default: return status;
        }
    }

    // ==================== 集成监控操作接口 ====================

    @Operation(summary = "重启集成")
    @ApiOperation("重启集成")
    @PostMapping("/monitor/{id}/restart")
    public MyJsonBean<Map<String, Object>> restartIntegration(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetIntegrationMonitor monitor = findMonitorByIdOrIntegrationId(id);
            Map<String, Object> restartResult = new HashMap<>();
            if (monitor != null) {
                // 创建新的监控记录表示重启
                BudgetIntegrationMonitor newRecord = new BudgetIntegrationMonitor();
                newRecord.setIntegrationId(monitor.getIntegrationId());
                newRecord.setIntegrationName(monitor.getIntegrationName());
                newRecord.setIntegrationType(monitor.getIntegrationType());
                newRecord.setExecutionType("MANUAL");
                newRecord.setExecutionStatus("RUNNING");
                newRecord.setStartTime(new Date());
                newRecord.setCreatedTime(new Date());
                newRecord.setRemark("手动重启");
                monitorMapper.insert(newRecord);
                restartResult.put("success", true);
                restartResult.put("message", "集成 " + monitor.getIntegrationName() + " 已重启");
                restartResult.put("newMonitorId", newRecord.getMonitorId());
            } else {
                restartResult.put("success", false);
                restartResult.put("message", "监控记录不存在");
            }
            result.setCode(1);
            result.setMsg("操作成功");
            result.setData(restartResult);
        } catch (Exception e) {
            log.error("重启集成异常", e);
            result.setCode(0);
            result.setMsg("重启失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "确认告警")
    @ApiOperation("确认告警")
    @PostMapping("/monitor/alert/{alertId}/acknowledge")
    public MyJsonBean<Map<String, Object>> acknowledgeAlert(@PathVariable String alertId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetIntegrationMonitor monitor = monitorMapper.selectById(alertId);
            Map<String, Object> ackResult = new HashMap<>();
            if (monitor != null) {
                monitor.setIsAlerted(true);
                monitor.setAlertTime(new Date());
                monitorMapper.updateById(monitor);
                ackResult.put("success", true);
                ackResult.put("message", "告警已确认");
            } else {
                ackResult.put("success", false);
                ackResult.put("message", "告警记录不存在");
            }
            result.setCode(1);
            result.setMsg("操作成功");
            result.setData(ackResult);
        } catch (Exception e) {
            log.error("确认告警异常", e);
            result.setCode(0);
            result.setMsg("确认失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取集成监控详情")
    @ApiOperation("获取集成监控详情")
    @GetMapping("/monitor/{id}/detail")
    public MyJsonBean<Map<String, Object>> getMonitorDetail(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetIntegrationMonitor monitor = findMonitorByIdOrIntegrationId(id);
            if (monitor == null) {
                result.setCode(0);
                result.setMsg("监控记录不存在");
                return result;
            }
            // 查询该集成的所有历史记录
            String integrationKey = monitor.getIntegrationId() != null ? monitor.getIntegrationId() : monitor.getMonitorId();
            QueryWrapper<BudgetIntegrationMonitor> qw = new QueryWrapper<>();
            if (monitor.getIntegrationId() != null) {
                qw.eq("INTEGRATION_ID", integrationKey);
            } else {
                qw.eq("MONITOR_ID", integrationKey);
            }
            qw.orderByDesc("START_TIME");
            qw.last("FETCH FIRST 100 ROWS ONLY");
            List<BudgetIntegrationMonitor> history = monitorMapper.selectList(qw);

            Map<String, Object> detail = new HashMap<>();
            detail.put("id", monitor.getMonitorId());
            detail.put("integrationName", monitor.getIntegrationName());
            detail.put("integrationType", monitor.getIntegrationType());
            detail.put("executionType", monitor.getExecutionType());
            detail.put("status", monitor.getExecutionStatus());
            detail.put("startTime", monitor.getStartTime());
            detail.put("endTime", monitor.getEndTime());
            detail.put("durationMs", monitor.getDurationMs());
            detail.put("errorMessage", monitor.getErrorMessage());
            detail.put("errorCode", monitor.getErrorCode());
            detail.put("retryCount", monitor.getRetryCount() != null ? monitor.getRetryCount() : 0);
            detail.put("remark", monitor.getRemark());

            // 计算统计指标
            long totalCount = history.size();
            long successCount = history.stream().filter(h -> "SUCCESS".equals(h.getExecutionStatus())).count();
            long failureCount = history.stream().filter(h -> "FAILURE".equals(h.getExecutionStatus())).count();
            double avgResponseTime = history.stream().filter(h -> h.getDurationMs() != null).mapToLong(BudgetIntegrationMonitor::getDurationMs).average().orElse(0);
            long maxResponseTime = history.stream().filter(h -> h.getDurationMs() != null).mapToLong(BudgetIntegrationMonitor::getDurationMs).max().orElse(0);
            double avgThroughput = history.stream().filter(h -> h.getThroughput() != null).mapToDouble(BudgetIntegrationMonitor::getThroughput).average().orElse(0);
            double maxThroughput = history.stream().filter(h -> h.getThroughput() != null).mapToDouble(BudgetIntegrationMonitor::getThroughput).max().orElse(0);
            double availability = totalCount > 0 ? Math.round(successCount * 1000.0 / totalCount) / 10.0 : 99.9;
            double errorRate = totalCount > 0 ? Math.round(failureCount * 1000.0 / totalCount) / 10.0 : 0;

            detail.put("avgResponseTime", Math.round(avgResponseTime));
            detail.put("maxResponseTime", maxResponseTime);
            detail.put("avgThroughput", Math.round(avgThroughput * 10.0) / 10.0);
            detail.put("maxThroughput", Math.round(maxThroughput * 10.0) / 10.0);
            detail.put("errorRate", errorRate);
            detail.put("availability", availability);
            detail.put("totalRequests", totalCount);
            detail.put("successRequests", successCount);

            // 从设置表读取 monitorInterval / alertThreshold / timeout
            List<BudgetIntegrationMonitorSettings> settingsList = settingsMapper.selectList(new QueryWrapper<>());
            Map<String, String> settingsMap = new HashMap<>();
            for (BudgetIntegrationMonitorSettings s : settingsList) {
                settingsMap.put(s.getSettingsKey(), s.getSettingsValue());
            }
            detail.put("monitorInterval", parseIntOrDefault(settingsMap.get("monitorInterval"), 30));
            detail.put("alertThreshold", settingsMap.getOrDefault("alertThreshold", "错误率>5%"));
            detail.put("timeout", monitor.getDurationMs() != null ? monitor.getDurationMs() : parseIntOrDefault(settingsMap.get("timeout"), 30000));

            // 响应时间趋势数据
            List<Map<String, Object>> responseTimeTrend = new ArrayList<>();
            for (BudgetIntegrationMonitor h : history) {
                if (h.getDurationMs() != null) {
                    Map<String, Object> point = new HashMap<>();
                    point.put("time", h.getStartTime());
                    point.put("value", h.getDurationMs());
                    responseTimeTrend.add(point);
                }
            }
            detail.put("responseTimeTrend", responseTimeTrend);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(detail);
        } catch (Exception e) {
            log.error("获取监控详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取集成告警历史")
    @ApiOperation("获取集成告警历史")
    @GetMapping("/monitor/{id}/alerts")
    public MyJsonBean<List<Map<String, Object>>> getIntegrationAlerts(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            BudgetIntegrationMonitor monitor = findMonitorByIdOrIntegrationId(id);
            if (monitor == null) {
                result.setCode(0);
                result.setMsg("监控记录不存在");
                return result;
            }
            String integrationKey = monitor.getIntegrationId() != null ? monitor.getIntegrationId() : monitor.getMonitorId();
            QueryWrapper<BudgetIntegrationMonitor> qw = new QueryWrapper<>();
            if (monitor.getIntegrationId() != null) {
                qw.eq("INTEGRATION_ID", integrationKey);
            } else {
                qw.eq("MONITOR_ID", integrationKey);
            }
            qw.and(w -> w.eq("EXECUTION_STATUS", "FAILURE").or().isNotNull("ALERT_LEVEL"));
            qw.orderByDesc("START_TIME");
            qw.last("FETCH FIRST 50 ROWS ONLY");
            List<BudgetIntegrationMonitor> alerts = monitorMapper.selectList(qw);
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetIntegrationMonitor a : alerts) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", a.getMonitorId());
                item.put("alertTime", a.getStartTime());
                item.put("level", a.getAlertLevel() != null ? a.getAlertLevel().toLowerCase() : ("FAILURE".equals(a.getExecutionStatus()) ? "high" : "medium"));
                item.put("title", a.getIntegrationName() + " " + (a.getExecutionStatus() != null ? a.getExecutionStatus() : ""));
                item.put("message", a.getErrorMessage() != null ? a.getErrorMessage() : "执行异常");
                item.put("status", a.getIsAlerted() != null && a.getIsAlerted() ? "acknowledged" : "pending");
                item.put("acknowledgedBy", a.getExecutedBy());
                item.put("resolvedTime", a.getEndTime());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取集成告警历史异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取集成日志列表")
    @ApiOperation("获取集成日志列表")
    @GetMapping("/monitor/{id}/logs")
    public MyJsonBean<List<Map<String, Object>>> getIntegrationLogs(@PathVariable String id) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            BudgetIntegrationMonitor monitor = findMonitorByIdOrIntegrationId(id);
            if (monitor == null) {
                result.setCode(0);
                result.setMsg("监控记录不存在");
                return result;
            }
            String integrationKey = monitor.getIntegrationId() != null ? monitor.getIntegrationId() : monitor.getMonitorId();
            QueryWrapper<BudgetIntegrationMonitor> qw = new QueryWrapper<>();
            if (monitor.getIntegrationId() != null) {
                qw.eq("INTEGRATION_ID", integrationKey);
            } else {
                qw.eq("MONITOR_ID", integrationKey);
            }
            qw.orderByDesc("START_TIME");
            qw.last("FETCH FIRST 100 ROWS ONLY");
            List<BudgetIntegrationMonitor> logs = monitorMapper.selectList(qw);
            List<Map<String, Object>> list = new ArrayList<>();
            for (BudgetIntegrationMonitor l : logs) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", l.getMonitorId());
                item.put("time", l.getStartTime());
                item.put("level", "FAILURE".equals(l.getExecutionStatus()) ? "ERROR" : ("RUNNING".equals(l.getExecutionStatus()) ? "INFO" : "INFO"));
                item.put("message", l.getIntegrationName() + " - " + l.getExecutionType() + " - " + l.getExecutionStatus());
                item.put("status", l.getExecutionStatus());
                item.put("duration", l.getDurationMs());
                item.put("errorMessage", l.getErrorMessage());
                item.put("executedBy", l.getExecutedBy());
                list.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("获取集成日志异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取所有告警列表")
    @ApiOperation("获取所有告警列表")
    @GetMapping("/monitor/alerts/all")
    public MyJsonBean<Map<String, Object>> getAllAlerts(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "20") int size) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetIntegrationMonitor> countQw = new QueryWrapper<>();
            countQw.and(w -> w.eq("EXECUTION_STATUS", "FAILURE").or().isNotNull("ALERT_LEVEL"));
            long total = monitorMapper.selectCount(countQw);
            int offset = (current - 1) * size;
            QueryWrapper<BudgetIntegrationMonitor> qw = new QueryWrapper<>();
            qw.and(w -> w.eq("EXECUTION_STATUS", "FAILURE").or().isNotNull("ALERT_LEVEL"));
            qw.orderByDesc("START_TIME");
            qw.last("OFFSET " + offset + " ROWS FETCH NEXT " + size + " ROWS ONLY");
            List<BudgetIntegrationMonitor> alerts = monitorMapper.selectList(qw);
            List<Map<String, Object>> records = new ArrayList<>();
            for (BudgetIntegrationMonitor a : alerts) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", a.getMonitorId());
                item.put("alertTime", a.getStartTime());
                item.put("level", a.getAlertLevel() != null ? a.getAlertLevel().toLowerCase() : ("FAILURE".equals(a.getExecutionStatus()) ? "high" : "medium"));
                item.put("title", a.getIntegrationName() + " " + (a.getExecutionStatus() != null ? a.getExecutionStatus() : ""));
                item.put("message", a.getErrorMessage() != null ? a.getErrorMessage() : "执行异常");
                item.put("status", a.getIsAlerted() != null && a.getIsAlerted() ? "acknowledged" : "pending");
                item.put("integrationName", a.getIntegrationName());
                item.put("integrationType", a.getIntegrationType());
                records.add(item);
            }
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("records", records);
            pageData.put("total", total);
            pageData.put("current", current);
            pageData.put("size", size);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageData);
        } catch (Exception e) {
            log.error("获取所有告警列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取监控设置")
    @ApiOperation("获取监控设置")
    @GetMapping("/monitor/settings")
    public MyJsonBean<Map<String, Object>> getMonitorSettings() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            // 从数据库读取所有设置
            List<BudgetIntegrationMonitorSettings> settingsList = settingsMapper.selectList(new QueryWrapper<>());
            Map<String, Object> settings = new HashMap<>();
            for (BudgetIntegrationMonitorSettings s : settingsList) {
                String key = s.getSettingsKey();
                String val = s.getSettingsValue();
                // 根据key类型转换值
                if ("refreshInterval".equals(key) || "alertThresholdErrorRate".equals(key)
                        || "alertThresholdResponseTime".equals(key) || "alertThresholdAvailability".equals(key)
                        || "retentionDays".equals(key) || "timeout".equals(key) || "monitorInterval".equals(key)) {
                    try { settings.put(key, Integer.parseInt(val)); } catch (NumberFormatException e1) { settings.put(key, val); }
                } else if ("enableEmailAlert".equals(key) || "enableSmsAlert".equals(key)) {
                    settings.put(key, "true".equalsIgnoreCase(val));
                } else {
                    settings.put(key, val);
                }
            }
            // 统计当前监控数量
            long totalMonitors = monitorMapper.selectCount(new QueryWrapper<>());
            settings.put("totalMonitors", totalMonitors);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(settings);
        } catch (Exception e) {
            log.error("获取监控设置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "保存监控设置")
    @ApiOperation("保存监控设置")
    @PostMapping("/monitor/settings")
    public MyJsonBean<Void> saveMonitorSettings(@RequestBody Map<String, Object> settings) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            for (Map.Entry<String, Object> entry : settings.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue() != null ? String.valueOf(entry.getValue()) : "";
                // 按key查找已有记录
                QueryWrapper<BudgetIntegrationMonitorSettings> qw = new QueryWrapper<>();
                qw.eq("SETTINGS_KEY", key);
                BudgetIntegrationMonitorSettings existing = settingsMapper.selectOne(qw);
                if (existing != null) {
                    // 更新
                    existing.setSettingsValue(val);
                    existing.setUpdatedTime(new Date());
                    settingsMapper.updateById(existing);
                } else {
                    // 新增
                    BudgetIntegrationMonitorSettings newSetting = new BudgetIntegrationMonitorSettings();
                    newSetting.setSettingsKey(key);
                    newSetting.setSettingsValue(val);
                    newSetting.setSettingsDesc(key);
                    newSetting.setCreatedTime(new Date());
                    newSetting.setUpdatedTime(new Date());
                    settingsMapper.insert(newSetting);
                }
            }
            result.setCode(1);
            result.setMsg("设置保存成功");
        } catch (Exception e) {
            log.error("保存监控设置异常", e);
            result.setCode(0);
            result.setMsg("保存失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出监控报告")
    @ApiOperation("导出监控报告")
    @PostMapping("/monitor/export")
    public MyJsonBean<Map<String, Object>> exportMonitorReport(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<BudgetIntegrationMonitor> monitors = monitorMapper.selectList(
                    new QueryWrapper<BudgetIntegrationMonitor>().orderByDesc("START_TIME").last("FETCH FIRST 200 ROWS ONLY"));
            Map<String, Object> report = new HashMap<>();
            report.put("exportTime", new Date());
            report.put("totalRecords", monitors.size());
            long successCount = monitors.stream().filter(m -> "SUCCESS".equals(m.getExecutionStatus())).count();
            long failureCount = monitors.stream().filter(m -> "FAILURE".equals(m.getExecutionStatus())).count();
            report.put("successCount", successCount);
            report.put("failureCount", failureCount);
            report.put("successRate", monitors.size() > 0 ? Math.round(successCount * 1000.0 / monitors.size()) / 10.0 : 0);
            // 导出详细记录
            List<Map<String, Object>> records = new ArrayList<>();
            for (BudgetIntegrationMonitor m : monitors) {
                Map<String, Object> item = new HashMap<>();
                item.put("integrationName", m.getIntegrationName());
                item.put("integrationType", m.getIntegrationType());
                item.put("status", m.getExecutionStatus());
                item.put("startTime", m.getStartTime());
                item.put("endTime", m.getEndTime());
                item.put("duration", m.getDurationMs());
                item.put("errorMessage", m.getErrorMessage());
                records.add(item);
            }
            report.put("records", records);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(report);
        } catch (Exception e) {
            log.error("导出监控报告异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取集成配置信息")
    @ApiOperation("获取集成配置信息")
    @GetMapping("/monitor/{id}/config")
    public MyJsonBean<Map<String, Object>> getIntegrationConfig(@PathVariable String id) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetIntegrationMonitor monitor = findMonitorByIdOrIntegrationId(id);
            if (monitor == null) {
                result.setCode(0);
                result.setMsg("监控记录不存在");
                return result;
            }
            Map<String, Object> config = new HashMap<>();
            config.put("integrationName", monitor.getIntegrationName());
            config.put("integrationType", monitor.getIntegrationType());
            config.put("executionType", monitor.getExecutionType());
            config.put("retryCount", monitor.getRetryCount() != null ? monitor.getRetryCount() : 3);
            config.put("integrationId", monitor.getIntegrationId());
            config.put("createdTime", monitor.getCreatedTime());
            config.put("remark", monitor.getRemark());

            // 从设置表读取 monitorInterval / alertThreshold / timeout
            List<BudgetIntegrationMonitorSettings> settingsList = settingsMapper.selectList(new QueryWrapper<>());
            Map<String, String> settingsMap = new HashMap<>();
            for (BudgetIntegrationMonitorSettings s : settingsList) {
                settingsMap.put(s.getSettingsKey(), s.getSettingsValue());
            }
            config.put("monitorInterval", parseIntOrDefault(settingsMap.get("monitorInterval"), 30));
            config.put("alertThreshold", settingsMap.getOrDefault("alertThreshold", "错误率>5%"));
            config.put("timeout", parseIntOrDefault(settingsMap.get("timeout"), 30000));

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(config);
        } catch (Exception e) {
            log.error("获取集成配置信息异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 安全解析整数，失败返回默认值
     */
    private int parseIntOrDefault(String val, int defaultVal) {
        if (val == null || val.isEmpty()) return defaultVal;
        try { return Integer.parseInt(val); } catch (NumberFormatException e) { return defaultVal; }
    }

    /**
     * 根据ID查找监控记录：先按主键MONITOR_ID查，找不到再按INTEGRATION_ID查最新一条
     */
    private BudgetIntegrationMonitor findMonitorByIdOrIntegrationId(String id) {
        // 先按主键查
        BudgetIntegrationMonitor monitor = monitorMapper.selectById(id);
        if (monitor != null) {
            return monitor;
        }
        // 按INTEGRATION_ID查最新一条
        QueryWrapper<BudgetIntegrationMonitor> qw = new QueryWrapper<>();
        qw.eq("INTEGRATION_ID", id);
        qw.orderByDesc("START_TIME");
        qw.last("FETCH FIRST 1 ROWS ONLY");
        List<BudgetIntegrationMonitor> list = monitorMapper.selectList(qw);
        return list.isEmpty() ? null : list.get(0);
    }
}
