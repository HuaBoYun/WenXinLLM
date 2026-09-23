package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetSystemConfigEntity;
import com.management.accountant.oracle.mapper.budget.BudgetSystemConfigMapper;
import com.management.accountant.service.BudgetSystemConfigService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BudgetSystemConfigServiceImpl implements BudgetSystemConfigService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetSystemConfigMapper configMapper;

    @Override
    public PageResult<BudgetSystemConfigEntity> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetSystemConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);

        if (params.get("configType") != null && StringUtils.hasText(params.get("configType").toString())) {
            wrapper.eq("CONFIG_TYPE", params.get("configType"));
        }
        if (params.get("configName") != null && StringUtils.hasText(params.get("configName").toString())) {
            wrapper.like("CONFIG_NAME", params.get("configName"));
        }
        if (params.get("configKey") != null && StringUtils.hasText(params.get("configKey").toString())) {
            wrapper.like("CONFIG_KEY", params.get("configKey"));
        }
        wrapper.orderByAsc("SORT_ORDER");

        Page<BudgetSystemConfigEntity> page = new Page<>(pageNum, pageSize);
        IPage<BudgetSystemConfigEntity> pageResult = configMapper.selectPage(page, wrapper);

        PageResult<BudgetSystemConfigEntity> result = new PageResult<>();
        result.setList(pageResult.getRecords());
        result.setTotal((int) pageResult.getTotal());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public List<BudgetSystemConfigEntity> getAll() {
        QueryWrapper<BudgetSystemConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0).eq("IS_ENABLED", 1);
        wrapper.orderByAsc("CONFIG_TYPE", "SORT_ORDER");
        return configMapper.selectList(wrapper);
    }

    @Override
    public List<BudgetSystemConfigEntity> getByType(String configType) {
        QueryWrapper<BudgetSystemConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0).eq("CONFIG_TYPE", configType);
        wrapper.orderByAsc("SORT_ORDER");
        return configMapper.selectList(wrapper);
    }

    @Override
    public BudgetSystemConfigEntity getById(String configId) {
        if (!StringUtils.hasText(configId)) {
            return null;
        }
        QueryWrapper<BudgetSystemConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("CONFIG_ID", configId).eq("IS_DELETED", 0);
        return configMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetSystemConfigEntity create(BudgetSystemConfigEntity config) {
        if (config == null) {
            throw new ServiceException("配置信息不能为空");
        }
        if (!StringUtils.hasText(config.getConfigKey())) {
            throw new ServiceException("配置键不能为空");
        }
        QueryWrapper<BudgetSystemConfigEntity> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("CONFIG_KEY", config.getConfigKey()).eq("IS_DELETED", 0);
        if (configMapper.selectCount(checkWrapper) > 0) {
            throw new ServiceException("配置键已存在");
        }
        if (config.getIsDeleted() == null) {
            config.setIsDeleted(0);
        }
        if (config.getIsEnabled() == null) {
            config.setIsEnabled(1);
        }
        config.setCreateTime(new Date());
        config.setUpdateTime(new Date());

        int result = configMapper.insert(config);
        if (result <= 0) {
            throw new ServiceException("创建配置失败");
        }
        log.info("创建系统配置成功，ID: {}", config.getConfigId());
        return config;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetSystemConfigEntity config) {
        if (config == null || !StringUtils.hasText(config.getConfigId())) {
            throw new ServiceException("配置ID不能为空");
        }
        BudgetSystemConfigEntity existing = getById(config.getConfigId());
        if (existing == null) {
            throw new ServiceException("配置不存在");
        }
        config.setUpdateTime(new Date());
        configMapper.updateById(config);
        log.info("更新系统配置成功，ID: {}", config.getConfigId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String configId) {
        if (!StringUtils.hasText(configId)) {
            throw new ServiceException("配置ID不能为空");
        }
        BudgetSystemConfigEntity existing = getById(configId);
        if (existing == null) {
            throw new ServiceException("配置不存在");
        }
        BudgetSystemConfigEntity update = new BudgetSystemConfigEntity();
        update.setConfigId(configId);
        update.setIsDeleted(1);
        update.setUpdateTime(new Date());
        configMapper.updateById(update);
        log.info("删除系统配置成功，ID: {}", configId);
    }
}

