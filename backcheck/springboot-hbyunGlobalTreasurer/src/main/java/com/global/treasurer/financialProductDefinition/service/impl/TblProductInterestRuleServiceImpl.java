package com.global.treasurer.financialProductDefinition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.SnowflakeIdWorker;
import com.global.treasurer.financialProductDefinition.entity.TblProductInterestRule;
import com.global.treasurer.financialProductDefinition.mapper.TblProductInterestRuleMapper;
import com.global.treasurer.financialProductDefinition.service.TblProductInterestRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 产品利息规则管理Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class TblProductInterestRuleServiceImpl extends ServiceImpl<TblProductInterestRuleMapper, TblProductInterestRule>
        implements TblProductInterestRuleService {
    private static final Logger log = LoggerFactory.getLogger(TblProductInterestRuleServiceImpl.class);

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public IPage<TblProductInterestRule> getPage(Integer pageNo, Integer pageSize, String ruleCode,
                                                  String ruleName, String productType, Integer isEnabled, Long orgId) {
        Page<TblProductInterestRule> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TblProductInterestRule> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(ruleCode)) {
            wrapper.like("rule_code", ruleCode);
        }
        if (StringUtils.hasText(ruleName)) {
            wrapper.like("rule_name", ruleName);
        }
        // productType字段在数据库表中不存在,移除此过滤条件
        // if (StringUtils.hasText(productType)) {
        //     wrapper.eq("product_type", productType);
        // }
        if (isEnabled != null) {
            wrapper.eq("is_enabled", isEnabled);
        }
        // 暂时移除orgId过滤,查询所有数据
        // if (orgId != null) {
        //     wrapper.eq("org_id", orgId);
        // }

        log.info("查询条件 - ruleCode:{}, ruleName:{}, productType:{}, isEnabled:{}, orgId:{}",
                 ruleCode, ruleName, productType, isEnabled, orgId);

        wrapper.orderByDesc("create_time");
        IPage<TblProductInterestRule> result = this.page(page, wrapper);

        log.info("查询结果 - 总记录数:{}, 当前页记录数:{}", result.getTotal(), result.getRecords().size());

        return result;
    }

    @Override
    public TblProductInterestRule getDetail(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblProductInterestRule create(TblProductInterestRule entity, String createBy) {
        entity.setRuleId(snowflakeIdWorker.nextId());
        entity.setCreateBy(createBy);
        entity.setCreateTime(new Date());
        entity.setUpdateBy(createBy);
        entity.setUpdateTime(new Date());
        if (entity.getIsEnabled() == null) {
            entity.setIsEnabled(1);
        }
        this.save(entity);
        return entity;
    }

    @Override
    public boolean update(TblProductInterestRule entity, String updateBy) {
        entity.setUpdateBy(updateBy);
        return this.updateById(entity);
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public boolean updateStatus(Long id, Integer isEnabled, String updateBy) {
        TblProductInterestRule entity = new TblProductInterestRule();
        entity.setRuleId(id);
        entity.setIsEnabled(isEnabled);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(new Date());
        return this.updateById(entity);
    }

    @Override
    public boolean batchUpdateStatus(List<Long> ids, Integer isEnabled, String updateBy) {
        return baseMapper.batchUpdateStatus(ids, isEnabled, updateBy) > 0;
    }

    @Override
    public List<TblProductInterestRule> getEnabledList(Long orgId) {
        QueryWrapper<TblProductInterestRule> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_ENABLED", 1);
        if (orgId != null) {
            wrapper.eq("ORG_ID", orgId);
        }
        return this.list(wrapper);
    }

    @Override
    public boolean checkCodeUnique(String ruleCode, Long excludeId) {
        return baseMapper.checkCodeUnique(ruleCode, excludeId) == 0;
    }

    @Override
    public List<TblProductInterestRule> getByProductType(String productType, Long orgId) {
        return baseMapper.selectByProductType(productType, orgId);
    }

    @Override
    public TblProductInterestRule copy(Long id, String newCode, String newName, String createBy) {
        TblProductInterestRule source = this.getById(id);
        if (source == null) {
            return null;
        }
        TblProductInterestRule target = new TblProductInterestRule();
        BeanUtils.copyProperties(source, target);
        target.setRuleId(snowflakeIdWorker.nextId());
        target.setRuleCode(newCode);
        target.setRuleName(newName);
        target.setCreateBy(createBy);
        target.setCreateTime(new Date());
        target.setUpdateBy(createBy);
        target.setUpdateTime(new Date());
        this.save(target);
        return target;
    }

    @Override
    public Map<String, Object> validateDelete(Long id) {
        Map<String, Object> result = new HashMap<>();
        int usage = baseMapper.countUsage(id);
        result.put("canDelete", usage == 0);
        result.put("usageCount", usage);
        if (usage > 0) {
            result.put("message", "该利息规则已被使用,无法删除");
        }
        return result;
    }

    @Override
    public Map<String, Object> getUsage(Long id) {
        Map<String, Object> result = new HashMap<>();
        int usage = baseMapper.countUsage(id);
        result.put("usageCount", usage);
        result.put("isUsed", usage > 0);
        return result;
    }
}
