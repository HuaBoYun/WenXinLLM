package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetDataIntegrationConfig;
import com.management.accountant.oracle.mapper.budget.BudgetDataIntegrationConfigMapper;
import com.management.accountant.service.BudgetDataIntegrationConfigService;
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
public class BudgetDataIntegrationConfigServiceImpl implements BudgetDataIntegrationConfigService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetDataIntegrationConfigMapper integrationMapper;

    @Override
    public PageResult<BudgetDataIntegrationConfig> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetDataIntegrationConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);

        if (params.get("integrationType") != null && StringUtils.hasText(params.get("integrationType").toString())) {
            wrapper.eq("INTEGRATION_TYPE", params.get("integrationType"));
        }
        if (params.get("integrationName") != null && StringUtils.hasText(params.get("integrationName").toString())) {
            wrapper.like("INTEGRATION_NAME", params.get("integrationName"));
        }
        if (params.get("syncStatus") != null && StringUtils.hasText(params.get("syncStatus").toString())) {
            wrapper.eq("SYNC_STATUS", params.get("syncStatus"));
        }
        wrapper.orderByDesc("CREATE_TIME");

        Page<BudgetDataIntegrationConfig> page = new Page<>(pageNum, pageSize);
        IPage<BudgetDataIntegrationConfig> pageResult = integrationMapper.selectPage(page, wrapper);

        PageResult<BudgetDataIntegrationConfig> result = new PageResult<>();
        result.setList(pageResult.getRecords());
        result.setTotal((int) pageResult.getTotal());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public BudgetDataIntegrationConfig getById(String integrationId) {
        if (!StringUtils.hasText(integrationId)) {
            return null;
        }
        QueryWrapper<BudgetDataIntegrationConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("INTEGRATION_ID", integrationId).eq("IS_DELETED", 0);
        return integrationMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetDataIntegrationConfig create(BudgetDataIntegrationConfig config) {
        if (config == null) {
            throw new ServiceException("集成配置不能为空");
        }
        if (!StringUtils.hasText(config.getIntegrationName())) {
            throw new ServiceException("集成名称不能为空");
        }
        if (config.getIsDeleted() == null) {
            config.setIsDeleted(0);
        }
        if (config.getIsEnabled() == null) {
            config.setIsEnabled(1);
        }
        config.setCreateTime(new Date());
        config.setUpdateTime(new Date());

        int result = integrationMapper.insert(config);
        if (result <= 0) {
            throw new ServiceException("创建集成配置失败");
        }
        log.info("创建集成配置成功，ID: {}", config.getIntegrationId());
        return config;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetDataIntegrationConfig config) {
        if (config == null || !StringUtils.hasText(config.getIntegrationId())) {
            throw new ServiceException("集成ID不能为空");
        }
        BudgetDataIntegrationConfig existing = getById(config.getIntegrationId());
        if (existing == null) {
            throw new ServiceException("集成配置不存在");
        }
        config.setUpdateTime(new Date());
        integrationMapper.updateById(config);
        log.info("更新集成配置成功，ID: {}", config.getIntegrationId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String integrationId) {
        if (!StringUtils.hasText(integrationId)) {
            throw new ServiceException("集成ID不能为空");
        }
        BudgetDataIntegrationConfig existing = getById(integrationId);
        if (existing == null) {
            throw new ServiceException("集成配置不存在");
        }
        BudgetDataIntegrationConfig update = new BudgetDataIntegrationConfig();
        update.setIntegrationId(integrationId);
        update.setIsDeleted(1);
        update.setUpdateTime(new Date());
        integrationMapper.updateById(update);
        log.info("删除集成配置成功，ID: {}", integrationId);
    }

    @Override
    public List<BudgetDataIntegrationConfig> exportData(Map<String, Object> params) {
        QueryWrapper<BudgetDataIntegrationConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        wrapper.orderByDesc("CREATE_TIME");
        return integrationMapper.selectList(wrapper);
    }
}

