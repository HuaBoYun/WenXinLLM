package com.financial.sharing.dataCollection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.dataCollection.dto.DataQualityRuleQueryParam;
import com.financial.sharing.dataCollection.entity.TblDataQualityRule;
import com.financial.sharing.dataCollection.mapper.DataQualityRuleMapper;
import com.financial.sharing.dataCollection.service.DataQualityRuleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 数据质量规则服务实现类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class DataQualityRuleServiceImpl implements DataQualityRuleService {

    @Autowired
    private DataQualityRuleMapper dataQualityRuleMapper;

    @Override
    public IPage<TblDataQualityRule> queryPage(DataQualityRuleQueryParam param, Long orgId) {
        log.info("分页查询质量规则, param={}, orgId={}", param, orgId);

        // 构建分页对象
        Page<TblDataQualityRule> page = new Page<>(param.getPageNumber(), param.getPageSize());

        // 构建查询条件
        QueryWrapper<TblDataQualityRule> wrapper = new QueryWrapper<>();
        wrapper.eq("ORG_ID", orgId);

        if (StringUtils.isNotBlank(param.getRuleCode())) {
            wrapper.like("RULE_CODE", param.getRuleCode());
        }
        if (StringUtils.isNotBlank(param.getRuleName())) {
            wrapper.like("RULE_NAME", param.getRuleName());
        }
        if (StringUtils.isNotBlank(param.getRuleType())) {
            wrapper.eq("RULE_TYPE", param.getRuleType());
        }
        if (StringUtils.isNotBlank(param.getCheckLevel())) {
            wrapper.eq("CHECK_LEVEL", param.getCheckLevel());
        }
        if (StringUtils.isNotBlank(param.getIsEnabled())) {
            wrapper.eq("IS_ENABLED", param.getIsEnabled());
        }

        wrapper.orderByAsc("SORT_NO");
        wrapper.orderByDesc("CREATE_TIME");

        return dataQualityRuleMapper.selectPage(page, wrapper);
    }

    @Override
    public TblDataQualityRule queryById(Long ruleId, Long orgId) {
        log.info("根据ID查询质量规则, ruleId={}, orgId={}", ruleId, orgId);

        QueryWrapper<TblDataQualityRule> wrapper = new QueryWrapper<>();
        wrapper.eq("RULE_ID", ruleId);
        wrapper.eq("ORG_ID", orgId);

        return dataQualityRuleMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRule(TblDataQualityRule rule, Long orgId, String userId) {
        log.info("保存质量规则, rule={}, orgId={}, userId={}", rule, orgId, userId);

        rule.setOrgId(orgId);

        if (rule.getRuleId() == null) {
            // 新增
            rule.setCreateUser(userId);
            rule.setCreateTime(new Date());
            rule.setUpdateUser(userId);
            rule.setUpdateTime(new Date());
            return dataQualityRuleMapper.insert(rule) > 0;
        } else {
            // 更新
            rule.setUpdateUser(userId);
            rule.setUpdateTime(new Date());
            return dataQualityRuleMapper.updateById(rule) > 0;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRule(Long ruleId, Long orgId) {
        log.info("删除质量规则, ruleId={}, orgId={}", ruleId, orgId);

        QueryWrapper<TblDataQualityRule> wrapper = new QueryWrapper<>();
        wrapper.eq("RULE_ID", ruleId);
        wrapper.eq("ORG_ID", orgId);

        return dataQualityRuleMapper.delete(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteRule(List<Long> ruleIds, Long orgId) {
        log.info("批量删除质量规则, ruleIds={}, orgId={}", ruleIds, orgId);

        if (ruleIds == null || ruleIds.isEmpty()) {
            return 0;
        }

        QueryWrapper<TblDataQualityRule> wrapper = new QueryWrapper<>();
        wrapper.in("RULE_ID", ruleIds);
        wrapper.eq("ORG_ID", orgId);

        return dataQualityRuleMapper.delete(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleEnabled(Long ruleId, String isEnabled, Long orgId, String userId) {
        log.info("启用/禁用质量规则, ruleId={}, isEnabled={}, orgId={}, userId={}", ruleId, isEnabled, orgId, userId);

        TblDataQualityRule rule = queryById(ruleId, orgId);
        if (rule == null) {
            return false;
        }

        rule.setIsEnabled(isEnabled);
        rule.setUpdateUser(userId);
        rule.setUpdateTime(new Date());

        return dataQualityRuleMapper.updateById(rule) > 0;
    }

    @Override
    public List<TblDataQualityRule> queryEnabledRules(Long orgId) {
        log.info("查询所有启用的规则, orgId={}", orgId);

        QueryWrapper<TblDataQualityRule> wrapper = new QueryWrapper<>();
        wrapper.eq("ORG_ID", orgId);
        wrapper.eq("IS_ENABLED", "Y");
        wrapper.orderByAsc("SORT_NO");

        return dataQualityRuleMapper.selectList(wrapper);
    }
}

