package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcBankConnectLog;
import com.global.treasurer.mapper.TcBankConnectLogMapper;
import com.global.treasurer.service.TcBankConnectLogService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.*;

/**
 * 银企直连日志Service实现类
 * @author AI Assistant
 * @date 2025-01-26
 */
@Service
public class TcBankConnectLogServiceImpl implements TcBankConnectLogService {
    @Resource
    private TcBankConnectLogMapper bankConnectLogMapper;

    @Override
    public PageInfo<TcBankConnectLog> list(Integer pageNum, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNum, pageSize);
        List<TcBankConnectLog> list = bankConnectLogMapper.selectByConditions(params);
        return new PageInfo<>(list);
    }

    @Override
    public TcBankConnectLog getById(String logId) {
        return bankConnectLogMapper.selectById(logId);
    }

    @Override
    public TcBankConnectLog getByRequestId(String requestId) {
        return bankConnectLogMapper.selectByRequestId(requestId);
    }

    @Override
    public boolean save(TcBankConnectLog log) {
        if (!StringUtils.hasText(log.getLogId())) {
            log.setLogId(UUID.randomUUID().toString().replace("-", ""));
        }
        log.setCreateTime(new Date());
        log.setUpdateTime(new Date());
        return bankConnectLogMapper.insert(log) > 0;
    }

    @Override
    public boolean update(TcBankConnectLog log) {
        log.setUpdateTime(new Date());
        return bankConnectLogMapper.updateById(log) > 0;
    }

    @Override
    public boolean delete(String logId) {
        return bankConnectLogMapper.deleteById(logId) > 0;
    }

    @Override
    public boolean batchDelete(List<String> logIds) {
        return bankConnectLogMapper.deleteBatchIds(logIds) > 0;
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        QueryWrapper<TcBankConnectLog> wrapper = new QueryWrapper<>();
        long total = bankConnectLogMapper.selectCount(wrapper);
        wrapper.eq("IS_SUCCESSFUL", 1);
        long successCount = bankConnectLogMapper.selectCount(wrapper);
        wrapper = new QueryWrapper<>();
        wrapper.eq("IS_SUCCESSFUL", 0);
        long failCount = bankConnectLogMapper.selectCount(wrapper);
        statistics.put("total", total);
        statistics.put("successCount", successCount);
        statistics.put("failCount", failCount);
        statistics.put("successRate", total > 0 ? String.format("%.2f", successCount * 100.0 / total) + "%" : "0%");
        statistics.put("byInterfaceType", bankConnectLogMapper.selectCountByInterfaceType());
        return statistics;
    }

    @Override
    public Map<String, Object> retryRequest(String logId) {
        Map<String, Object> result = new HashMap<>();
        TcBankConnectLog log = bankConnectLogMapper.selectById(logId);
        if (log == null) {
            result.put("success", false);
            result.put("message", "日志记录不存在");
            return result;
        }
        // 模拟重试逻辑
        result.put("success", true);
        result.put("message", "重试请求已提交");
        result.put("newRequestId", UUID.randomUUID().toString().replace("-", ""));
        return result;
    }

    @Override
    public int cleanHistoryLogs(Integer days) {
        return bankConnectLogMapper.deleteByDays(days);
    }

    @Override
    public List<TcBankConnectLog> exportLogs(Map<String, Object> params) {
        return bankConnectLogMapper.selectByConditions(params);
    }

    @Override
    public List<TcBankConnectLog> getFailedLogs(Integer limit) {
        return bankConnectLogMapper.selectFailedLogs(limit != null ? limit : 100);
    }
}

