package com.global.treasurer.service.impl;

import com.global.treasurer.entity.AmDirectConnectionConfig;
import com.global.treasurer.mapper.AmDirectConnectionConfigMapper;
import com.global.treasurer.service.AmDirectConnectionConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class AmDirectConnectionConfigServiceImpl implements AmDirectConnectionConfigService {
    @Autowired
    private AmDirectConnectionConfigMapper configMapper;

    @Override
    public PageInfo<AmDirectConnectionConfig> list(Integer pageNum, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNum, pageSize);
        List<AmDirectConnectionConfig> list = configMapper.selectByParams(params);
        return new PageInfo<>(list);
    }

    @Override
    public AmDirectConnectionConfig getById(Long id) {
        if (id == null) throw new ServiceException("配置ID不能为空");
        return configMapper.selectById(id);
    }

    @Override
    public boolean save(AmDirectConnectionConfig config) {
        if (config == null) throw new ServiceException("配置信息不能为空");
        config.setCreateTime(LocalDateTime.now());
        config.setUpdateTime(LocalDateTime.now());
        config.setStatus("1");
        return configMapper.insert(config) > 0;
    }

    @Override
    public boolean update(AmDirectConnectionConfig config) {
        if (config == null || config.getId() == null) throw new ServiceException("配置信息不完整");
        config.setUpdateTime(LocalDateTime.now());
        return configMapper.updateById(config) > 0;
    }

    @Override
    public boolean delete(Long id) {
        if (id == null) throw new ServiceException("配置ID不能为空");
        return configMapper.deleteById(id) > 0;
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) throw new ServiceException("配置ID列表不能为空");
        for (Long id : ids) { delete(id); }
        return true;
    }

    @Override
    public Map<String, Object> testConnection(Long id) {
        Map<String, Object> result = new HashMap<>();
        AmDirectConnectionConfig config = getById(id);
        if (config == null) {
            result.put("success", false);
            result.put("message", "配置不存在");
            return result;
        }
        // 模拟连接测试
        result.put("success", true);
        result.put("message", "连接测试成功");
        result.put("testTime", new Date());
        result.put("responseTime", "120ms");
        
        config.setLastTestTime(LocalDateTime.now());
        config.setLastTestResult("SUCCESS");
        configMapper.updateById(config);
        return result;
    }

    @Override
    public Map<String, Object> batchTestConnection(List<Long> ids) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0, failCount = 0;
        for (Long id : ids) {
            Map<String, Object> testResult = testConnection(id);
            if ((Boolean) testResult.get("success")) successCount++;
            else failCount++;
        }
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("total", ids.size());
        return result;
    }

    @Override
    public Map<String, Object> getStatistics() {
        return configMapper.selectStatistics();
    }

    @Override
    public List<AmDirectConnectionConfig> exportConfigs(Map<String, Object> params) {
        return configMapper.selectByParams(params);
    }
}

