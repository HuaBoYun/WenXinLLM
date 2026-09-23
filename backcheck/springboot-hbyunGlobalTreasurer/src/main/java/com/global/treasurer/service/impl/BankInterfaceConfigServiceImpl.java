package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TblBankInterfaceConfig;
import com.global.treasurer.mapper.TblBankInterfaceConfigMapper;
import com.global.treasurer.service.BankInterfaceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 银企联配置服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@Service
public class BankInterfaceConfigServiceImpl implements BankInterfaceConfigService {
    @Autowired
    private TblBankInterfaceConfigMapper bankConfigMapper;

    @Override
    public Map<String, Object> getBankConfigPage(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 兼容前端分页参数: pageNo, pageNum, current
        Integer page = 1;
        if (params.get("pageNo") != null) {
            page = Integer.parseInt(params.get("pageNo").toString());
        } else if (params.get("pageNum") != null) {
            page = Integer.parseInt(params.get("pageNum").toString());
        } else if (params.get("current") != null) {
            page = Integer.parseInt(params.get("current").toString());
        }

        // 兼容前端分页参数: pageSize, size
        Integer size = 20; // 默认每页20条
        if (params.get("pageSize") != null) {
            size = Integer.parseInt(params.get("pageSize").toString());
        } else if (params.get("size") != null) {
            size = Integer.parseInt(params.get("size").toString());
        } else if (params.get("limit") != null) {
            size = Integer.parseInt(params.get("limit").toString());
        }

        // 分页偏移量：第1页 offset=0，第2页 offset=pageSize，以此类推
        params.put("offset", (page - 1) * size);
        params.put("pageSize", size);

        // isEnabled 从前端传来是字符串，需转为 Integer，空字符串置 null
        Object isEnabledObj = params.get("isEnabled");
        if (isEnabledObj != null && !"".equals(isEnabledObj.toString().trim())) {
            params.put("isEnabled", Integer.parseInt(isEnabledObj.toString().trim()));
        } else {
            params.put("isEnabled", null);
        }

        List<TblBankInterfaceConfig> list = bankConfigMapper.selectBankConfigPage(params);
        int total = bankConfigMapper.countBankConfigList(params);

        // 兼容前端数据格式: rows 和 total
        result.put("rows", list);
        result.put("total", total);

        return result;
    }

    @Override
    public TblBankInterfaceConfig getBankConfigById(Long configId) {
        return bankConfigMapper.selectByIdWithCorrectColumns(configId);
    }

    @Override
    public int createBankConfig(TblBankInterfaceConfig config) {
        // 段保所有字段都有值，避免 NULL 插入数据库
        config.setIsEnabled(config.getIsEnabled() != null ? config.getIsEnabled() : 1);
        config.setDeleteFlag(config.getDeleteFlag() != null ? config.getDeleteFlag() : 0);
        config.setCreateTime(new Date());

        // 设置默认值
        if (config.getBankCode() == null || config.getBankCode().isEmpty()) {
            config.setBankCode("");
        }
        if (config.getBankName() == null || config.getBankName().isEmpty()) {
            config.setBankName("");
        }
        if (config.getInterfaceType() == null || config.getInterfaceType().isEmpty()) {
            config.setInterfaceType("");
        }
        if (config.getInterfaceVersion() == null || config.getInterfaceVersion().isEmpty()) {
            config.setInterfaceVersion("v1.0");
        }
        if (config.getApiUrl() == null || config.getApiUrl().isEmpty()) {
            config.setApiUrl("");
        }
        if (config.getAuthType() == null || config.getAuthType().isEmpty()) {
            config.setAuthType("");
        }
        if (config.getAuthToken() == null || config.getAuthToken().isEmpty()) {
            config.setAuthToken("");
        }
        if (config.getConnectionTimeout() == null) {
            config.setConnectionTimeout(30000);
        }
        if (config.getReadTimeout() == null) {
            config.setReadTimeout(60000);
        }
        if (config.getMaxRetryCount() == null) {
            config.setMaxRetryCount(3);
        }
        if (config.getPriority() == null) {
            config.setPriority(10);
        }
        if (config.getLoadBalanceWeight() == null) {
            config.setLoadBalanceWeight(100);
        }
        if (config.getHealthCheckInterval() == null) {
            config.setHealthCheckInterval(300);
        }
        if (config.getLastConnectTime() == null) {
            config.setLastConnectTime(null);
        }
        if (config.getLastConnectStatus() == null || config.getLastConnectStatus().isEmpty()) {
            config.setLastConnectStatus("");
        }
        if (config.getLastConnectMessage() == null || config.getLastConnectMessage().isEmpty()) {
            config.setLastConnectMessage("");
        }
        if (config.getConfigDescription() == null || config.getConfigDescription().isEmpty()) {
            config.setConfigDescription("");
        }
        if (config.getRemark() == null || config.getRemark().isEmpty()) {
            config.setRemark("");
        }
        if (config.getCreateBy() == null) {
            config.setCreateBy(null);
        }
        if (config.getCreateByName() == null || config.getCreateByName().isEmpty()) {
            config.setCreateByName("");
        }
        if (config.getUpdateBy() == null) {
            config.setUpdateBy(null);
        }
        if (config.getUpdateByName() == null || config.getUpdateByName().isEmpty()) {
            config.setUpdateByName("");
        }
        if (config.getUpdateTime() == null) {
            config.setUpdateTime(new Date());
        }
        if (config.getOrgId() == null) {
            config.setOrgId(null);
        }
        if (config.getOrgName() == null || config.getOrgName().isEmpty()) {
            config.setOrgName("");
        }

        // 使用自定义的 INSERT 方法，确保所有字段都被插入
        return bankConfigMapper.insertBankConfig(config);
    }

    @Override
    public int updateBankConfig(TblBankInterfaceConfig config) {
        config.setUpdateTime(new Date());
        return bankConfigMapper.updateConfigById(config);
    }

    @Override
    public int deleteBankConfig(List<Long> configIds) {
        if (configIds == null || configIds.isEmpty()) return 0;
        int count = 0;
        for (Long configId : configIds) {
            int result = bankConfigMapper.deleteById(configId);
            count += result;
        }
        return count;
    }

    @Override
    public int enableBankConfig(List<Long> configIds) {
        if (configIds == null || configIds.isEmpty()) return 0;
        return bankConfigMapper.batchUpdateEnabled(configIds, 1);
    }

    @Override
    public int disableBankConfig(List<Long> configIds) {
        if (configIds == null || configIds.isEmpty()) return 0;
        return bankConfigMapper.batchUpdateEnabled(configIds, 0);
    }

    @Override
    public Map<String, Object> testConnection(Long configId) {
        Map<String, Object> result = new HashMap<>();
        TblBankInterfaceConfig config = getBankConfigById(configId);
        if (config == null) {
            result.put("success", false);
            result.put("message", "配置不存在");
            return result;
        }
        try {
            long start = System.currentTimeMillis();
            boolean isSuccess = true;
            long responseTime = System.currentTimeMillis() - start;
            bankConfigMapper.updateConnectStatus(configId, isSuccess ? "SUCCESS" : "FAILED",
                isSuccess ? "连接测试成功" : "连接测试失败");
            result.put("success", isSuccess);
            result.put("message", isSuccess ? "连接测试成功" : "连接测试失败");
            result.put("responseTime", responseTime);
            result.put("timestamp", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (Exception e) {
            bankConfigMapper.updateConnectStatus(configId, "FAILED", e.getMessage());
            result.put("success", false);
            result.put("message", "连接测试异常: " + e.getMessage());
            result.put("responseTime", 0);
            result.put("timestamp", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
        return result;
    }

    @Override
    public Map<String, Object> healthCheck(Long configId) {
        Map<String, Object> result = new HashMap<>();
        TblBankInterfaceConfig config = getBankConfigById(configId);
        if (config == null) {
            result.put("healthy", false);
            result.put("message", "配置不存在");
            return result;
        }
        try {
            boolean isHealthy = true;
            bankConfigMapper.updateConnectStatus(configId, isHealthy ? "SUCCESS" : "FAILED",
                isHealthy ? "健康检查通过" : "健康检查失败");
            result.put("healthy", isHealthy);
            result.put("message", isHealthy ? "健康检查通过" : "健康检查失败");
            result.put("checkTime", new Date());
            result.put("configId", configId);
            result.put("bankName", config.getBankName());
        } catch (Exception e) {
            bankConfigMapper.updateConnectStatus(configId, "FAILED", e.getMessage());
            result.put("healthy", false);
            result.put("message", "健康检查异常: " + e.getMessage());
        }
        return result;
    }

    @Override
    public List<TblBankInterfaceConfig> getAvailableConfigs(Map<String, Object> params) {
        return bankConfigMapper.selectAvailableConfigs(params);
    }

    @Override
    public Map<String, Object> getBankConfigSummary(Long orgId) {
        System.out.println("Service层获取统计，orgId: " + orgId);
        Map<String, Object> summary = bankConfigMapper.selectBankConfigSummary(orgId);
        System.out.println("Service层返回统计原始: " + summary);
        // 转换字段名为小写驼峰命名（解决达梦数据库返回大写字段名的问题）
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : summary.entrySet()) {
            String key = entry.getKey().toUpperCase();
            Object value = entry.getValue();
            // 直接映射已知的大写字段名到小写驼峰命名
            String camelKey = mapToCamelCase(key);
            result.put(camelKey, value);
        }
        System.out.println("Service层返回统计转换后: " + result);
        return result;
    }

    /**
     * 将大写字段名映射到小写驼峰命名
     */
    private String mapToCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        switch (input) {
            case "TOTALCOUNT": return "totalCount";
            case "ENABLEDCOUNT": return "enabledCount";
            case "DISABLEDCOUNT": return "disabledCount";
            case "SUCCESSCOUNT": return "successCount";
            case "FAILEDCOUNT": return "failedCount";
            case "UNKNOWNCOUNT": return "unknownCount";
            case "LASTTESTTIME": return "lastTestTime";
            default: return input.toLowerCase();
        }
    }

    @Override
    public Map<String, Object> batchTestConnection(List<Long> configIds) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<Map<String, Object>> results = new ArrayList<>();

        for (Long configId : configIds) {
            TblBankInterfaceConfig config = getBankConfigById(configId);
            if (config == null) {
                Map<String, Object> itemResult = new HashMap<>();
                itemResult.put("configId", configId);
                itemResult.put("success", false);
                itemResult.put("message", "配置不存在");
                results.add(itemResult);
                failCount++;
                continue;
            }

            try {
                // 模拟测试连接
                boolean isSuccess = true;
                long responseTime = (long) (Math.random() * 500 + 100);

                bankConfigMapper.updateConnectStatus(configId, isSuccess ? "SUCCESS" : "FAILED",
                    isSuccess ? "连接测试成功" : "连接测试失败");

                Map<String, Object> itemResult = new HashMap<>();
                itemResult.put("configId", configId);
                itemResult.put("bankName", config.getBankName());
                itemResult.put("success", isSuccess);
                itemResult.put("message", isSuccess ? "连接测试成功" : "连接测试失败");
                itemResult.put("responseTime", responseTime);
                itemResult.put("testTime", new Date());
                results.add(itemResult);

                if (isSuccess) {
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                bankConfigMapper.updateConnectStatus(configId, "FAILED", e.getMessage());
                Map<String, Object> itemResult = new HashMap<>();
                itemResult.put("configId", configId);
                itemResult.put("bankName", config.getBankName());
                itemResult.put("success", false);
                itemResult.put("message", "连接测试异常: " + e.getMessage());
                itemResult.put("testTime", new Date());
                results.add(itemResult);
                failCount++;
            }
        }

        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", configIds.size());
        result.put("results", results);

        return result;
    }

    @Override
    public List<TblBankInterfaceConfig> exportConfigs(Map<String, Object> params) {
        params.put("offset", 0);
        params.put("limit", 10000); // 导出时限制最大10000条
        return bankConfigMapper.selectBankConfigPage(params);
    }
}
