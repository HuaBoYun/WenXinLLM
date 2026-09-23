package com.financial.sharing.service.impl;

import com.financial.sharing.service.FinanceConfigService;
import com.financial.sharing.oracle.mapper.FinanceConfigMapper;
import com.financial.sharing.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务配置服务实现类
 */
@Service
public class FinanceConfigServiceImpl implements FinanceConfigService {

    @Autowired
    private FinanceConfigMapper financeConfigMapper;

    // ==================== 财务版本信息管理 ====================

    @Override
    public PageResult<Map<String, Object>> getCwbbxxType() {
        List<Map<String, Object>> list = financeConfigMapper.selectCwbbxxTypeList();
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(list.size());
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> getCwbbxxList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeConfigMapper.selectCwbbxxList(params);
        int total = financeConfigMapper.countCwbbxxList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public Map<String, Object> saveCwbbxx(Map<String, Object> data) {
        financeConfigMapper.insertCwbbxx(data);
        return data;
    }

    @Override
    public boolean deleteCwbbxx(Map<String, Object> params) {
        return financeConfigMapper.deleteCwbbxx(params) > 0;
    }

    @Override
    public Map<String, Object> getCwbbxxDetail(Map<String, Object> params) {
        return financeConfigMapper.selectCwbbxxDetail(params);
    }

    // ==================== 数据源管理 ====================

    @Override
    public PageResult<Map<String, Object>> getDataSourceList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeConfigMapper.selectDataSourceList(params);
        int total = financeConfigMapper.countDataSourceList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public Map<String, Object> getDataSourceDetail(Map<String, Object> params) {
        return financeConfigMapper.selectDataSourceDetail(params);
    }

    @Override
    public Map<String, Object> saveDataSource(Map<String, Object> data) {
        financeConfigMapper.insertDataSource(data);
        return data;
    }

    @Override
    public boolean deleteDataSource(Map<String, Object> params) {
        return financeConfigMapper.deleteDataSource(params) > 0;
    }

    @Override
    public Map<String, Object> testDataSource(Map<String, Object> data) {
        // 测试数据源连接
        boolean success;
 success = financeConfigMapper.testDataSourceConnection(data) > 0;
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "连接成功" : "连接失败");
        return result;
    }

    // ==================== 采集方案管理 ====================

    @Override
    public PageResult<Map<String, Object>> getCjfaList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeConfigMapper.selectCjfaList(params);
        int total = financeConfigMapper.countCjfaList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public Map<String, Object> getCjfaDetail(Map<String, Object> params) {
        return financeConfigMapper.selectCjfaDetail(params);
    }

    @Override
    public Map<String, Object> saveCjfa(Map<String, Object> data) {
        financeConfigMapper.insertCjfa(data);
        return data;
    }

    @Override
    public boolean deleteCjfa(Map<String, Object> params) {
        return financeConfigMapper.deleteCjfa(params) > 0;
    }

    // ==================== 采集SQL管理 ====================

    @Override
    public Map<String, Object> getCjsqlAddDetail(Map<String, Object> params) {
        return financeConfigMapper.selectCjsqlAddDetail(params);
    }

    @Override
    public Map<String, Object> getCjsqlDetail(Map<String, Object> params) {
        return financeConfigMapper.selectCjsqlDetail(params);
    }

    @Override
    public PageResult<Map<String, Object>> getCjsqlList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeConfigMapper.selectCjsqlList(params);
        int total = financeConfigMapper.countCjsqlList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public Map<String, Object> saveCjsql(Map<String, Object> data) {
        financeConfigMapper.insertCjsql(data);
        return data;
    }

    @Override
    public boolean deleteCjsql(Map<String, Object> data) {
        return financeConfigMapper.deleteCjsql(data) > 0;
    }

    @Override
    public Map<String, Object> testCjsql(Map<String, Object> data) {
        // 测试采集SQL
        List<Map<String, Object>> testResult = financeConfigMapper.testCjsqlExecution(data);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", testResult);
        result.put("message", "SQL测试成功");
        return result;
    }

    @Override
    public Map<String, Object> executeCjsql(Map<String, Object> data) {
        // 执行采集SQL
        financeConfigMapper.executeCjsqlTask(data);
        Map<String, Object> result = new HashMap<>();
        result.put("status", "executing");
        result.put("message", "SQL执行任务已启动");
        return result;
    }

    // ==================== 离线工具下载 ====================

    @Override
    public Map<String, Object> downloadOfflineTool() {
        Map<String, Object> result = new HashMap<>();
        // 获取离线工具下载信息
        Map<String, Object> toolInfo = financeConfigMapper.selectOfflineToolInfo();
        result.put("success", true);
        result.put("data", toolInfo);
        result.put("message", "获取离线工具信息成功");
        return result;
    }
}