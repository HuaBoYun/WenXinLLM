package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetControlRule;
import com.management.accountant.oracle.mapper.budget.BudgetControlRuleMapper;
import com.management.accountant.service.BudgetControlRuleService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 预算控制规则Service实现类
 * 
 * @description 预算控制规则业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetControlRuleServiceImpl implements BudgetControlRuleService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetControlRuleMapper controlRuleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetControlRule create(BudgetControlRule rule) {
        if (rule == null) {
            throw new ServiceException("规则信息不能为空");
        }
        if (!StringUtils.hasText(rule.getRuleName())) {
            throw new ServiceException("规则名称不能为空");
        }
        if (!StringUtils.hasText(rule.getControlType())) {
            throw new ServiceException("控制类型不能为空");
        }

        // 检查规则编码是否重复
        if (StringUtils.hasText(rule.getRuleCode())) {
            QueryWrapper<BudgetControlRule> wrapper = new QueryWrapper<>();
            wrapper.eq("RULE_CODE", rule.getRuleCode());
            long count = controlRuleMapper.selectCount(wrapper);
            if (count > 0) {
                throw new ServiceException("规则编码已存在");
            }
        } else {
            rule.setRuleCode(generateRuleCode());
        }

        // 设置默认值
        if (rule.getIsEnabled() == null) {
            rule.setIsEnabled(true);
        }
        rule.setCreateTime(new Date());
        rule.setUpdateTime(new Date());

        int result = controlRuleMapper.insert(rule);
        if (result <= 0) {
            throw new ServiceException("创建控制规则失败");
        }

        log.info("创建控制规则成功，ID: {}", rule.getRuleId());
        return rule;
    }

    @Override
    public BudgetControlRule getById(String ruleId) {
        if (!StringUtils.hasText(ruleId)) {
            throw new ServiceException("规则ID不能为空");
        }
        return controlRuleMapper.selectById(ruleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetControlRule rule) {
        if (rule == null || !StringUtils.hasText(rule.getRuleId())) {
            throw new ServiceException("规则ID不能为空");
        }

        BudgetControlRule existing = getById(rule.getRuleId());
        if (existing == null) {
            throw new ServiceException("控制规则不存在");
        }

        // 检查规则编码是否重复
        if (StringUtils.hasText(rule.getRuleCode()) && !rule.getRuleCode().equals(existing.getRuleCode())) {
            QueryWrapper<BudgetControlRule> wrapper = new QueryWrapper<>();
            wrapper.eq("RULE_CODE", rule.getRuleCode())
                   .ne("RULE_ID", rule.getRuleId());
            long count = controlRuleMapper.selectCount(wrapper);
            if (count > 0) {
                throw new ServiceException("规则编码已存在");
            }
        }

        rule.setUpdateTime(new Date());
        int result = controlRuleMapper.updateById(rule);
        if (result <= 0) {
            throw new ServiceException("更新控制规则失败");
        }

        log.info("更新控制规则成功，ID: {}", rule.getRuleId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String ruleId) {
        if (!StringUtils.hasText(ruleId)) {
            throw new ServiceException("规则ID不能为空");
        }

        BudgetControlRule rule = getById(ruleId);
        if (rule == null) {
            throw new ServiceException("控制规则不存在");
        }

        // 物理删除（表中没有DEL_FLAG列）
        int result = controlRuleMapper.deleteById(ruleId);
        if (result <= 0) {
            throw new ServiceException("删除控制规则失败");
        }

        log.info("删除控制规则成功，ID: {}", ruleId);
    }

    @Override
    public PageResult<BudgetControlRule> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetControlRule> wrapper = new QueryWrapper<>();

        if (params.get("ruleCode") != null && StringUtils.hasText(params.get("ruleCode").toString())) {
            wrapper.like("RULE_CODE", params.get("ruleCode"));
        }
        if (params.get("ruleName") != null && StringUtils.hasText(params.get("ruleName").toString())) {
            wrapper.like("RULE_NAME", params.get("ruleName"));
        }
        if (params.get("controlType") != null && StringUtils.hasText(params.get("controlType").toString())) {
            wrapper.eq("CONTROL_TYPE", params.get("controlType"));
        }
        if (params.get("isEnabled") != null) {
            wrapper.eq("IS_ENABLED", params.get("isEnabled"));
        }

        // 排序
        wrapper.orderByDesc("CREATE_TIME");

        Page<BudgetControlRule> page = new Page<>(pageNum, pageSize);
        IPage<BudgetControlRule> pageResult = controlRuleMapper.selectPage(page, wrapper);

        // 4. 封装返回结果
        PageResult<BudgetControlRule> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(String ruleId) {
        if (!StringUtils.hasText(ruleId)) {
            throw new ServiceException("规则ID不能为空");
        }

        BudgetControlRule rule = getById(ruleId);
        if (rule == null) {
            throw new ServiceException("控制规则不存在");
        }

        if (Boolean.TRUE.equals(rule.getIsEnabled())) {
            throw new ServiceException("规则已经是启用状态");
        }

        BudgetControlRule update = new BudgetControlRule();
        update.setRuleId(ruleId);
        update.setIsEnabled(true);
        update.setUpdateTime(new Date());

        int result = controlRuleMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("启用规则失败");
        }

        log.info("启用规则成功，ID: {}", ruleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String ruleId) {
        if (!StringUtils.hasText(ruleId)) {
            throw new ServiceException("规则ID不能为空");
        }

        BudgetControlRule rule = getById(ruleId);
        if (rule == null) {
            throw new ServiceException("控制规则不存在");
        }

        if (Boolean.FALSE.equals(rule.getIsEnabled())) {
            throw new ServiceException("规则已经是禁用状态");
        }

        BudgetControlRule update = new BudgetControlRule();
        update.setRuleId(ruleId);
        update.setIsEnabled(false);
        update.setUpdateTime(new Date());

        int result = controlRuleMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("禁用规则失败");
        }

        log.info("禁用规则成功，ID: {}", ruleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("请选择要删除的记录");
        }

        for (String id : ids) {
            try {
                delete(id);
            } catch (Exception e) {
                log.error("批量删除失败，ID: {}", id, e);
            }
        }

        log.info("批量删除完成，数量: {}", ids.size());
    }

    @Override
    public Map<String, Object> validateRule(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String controlType = (String) params.get("controlType");
            BigDecimal amount = params.get("amount") != null ? 
                new BigDecimal(params.get("amount").toString()) : BigDecimal.ZERO;
            
            List<BudgetControlRule> rules = getActiveRules(controlType);
            
            boolean passed = true;
            List<String> violations = new ArrayList<>();
            
            for (BudgetControlRule rule : rules) {
                if (rule.getControlThreshold() != null && amount.compareTo(rule.getControlThreshold()) > 0) {
                    passed = false;
                    violations.add(rule.getRuleName() + ": 超出控制阈值");
                } else if (rule.getWarningThreshold() != null && amount.compareTo(rule.getWarningThreshold()) > 0) {
                    violations.add(rule.getRuleName() + ": 超出预警阈值");
                }
            }
            
            result.put("passed", passed);
            result.put("violations", violations);
            result.put("rulesChecked", rules.size());
            
        } catch (Exception e) {
            log.error("校验规则异常", e);
            result.put("passed", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        QueryWrapper<BudgetControlRule> totalWrapper = new QueryWrapper<>();
        Integer totalCount = controlRuleMapper.selectCount(totalWrapper).intValue();
        statistics.put("totalCount", totalCount);

        QueryWrapper<BudgetControlRule> enabledWrapper = new QueryWrapper<>();
        enabledWrapper.eq("IS_ENABLED", 1);
        Integer enabledCount = controlRuleMapper.selectCount(enabledWrapper).intValue();
        statistics.put("enabledCount", enabledCount);

        statistics.put("disabledCount", totalCount - enabledCount);

        QueryWrapper<BudgetControlRule> typeWrapper = new QueryWrapper<>();
        typeWrapper.select("CONTROL_TYPE", "COUNT(*) as count")
               .groupBy("CONTROL_TYPE");
        List<Map<String, Object>> typeStats = controlRuleMapper.selectMaps(typeWrapper);
        statistics.put("typeStatistics", typeStats);

        return statistics;
    }

    @Override
    public List<BudgetControlRule> getActiveRules(String controlType) {
        QueryWrapper<BudgetControlRule> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_ENABLED", 1);
        
        if (StringUtils.hasText(controlType)) {
            wrapper.eq("CONTROL_TYPE", controlType);
        }
        
        wrapper.orderByDesc("CREATE_TIME");
        
        return controlRuleMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetControlRule copyRule(String ruleId) {
        if (!StringUtils.hasText(ruleId)) {
            throw new ServiceException("规则ID不能为空");
        }

        BudgetControlRule original = getById(ruleId);
        if (original == null) {
            throw new ServiceException("控制规则不存在");
        }

        BudgetControlRule copy = new BudgetControlRule();
        copy.setRuleCode(generateRuleCode());
        copy.setRuleName(original.getRuleName() + "_副本");
        copy.setControlType(original.getControlType());
        copy.setWarningThreshold(original.getWarningThreshold());
        copy.setControlThreshold(original.getControlThreshold());
        copy.setEffectiveDate(original.getEffectiveDate());
        copy.setExpiryDate(original.getExpiryDate());
        copy.setIsEnabled(false);
        copy.setRemark(original.getRemark());
        copy.setCreateTime(new Date());
        copy.setUpdateTime(new Date());

        int result = controlRuleMapper.insert(copy);
        if (result <= 0) {
            throw new ServiceException("复制规则失败");
        }

        log.info("复制规则成功，原ID: {}, 新ID: {}", ruleId, copy.getRuleId());
        return copy;
    }

    @Override
    public List<BudgetControlRule> exportData(Map<String, Object> params) {
        QueryWrapper<BudgetControlRule> wrapper = new QueryWrapper<>();

        if (params.get("controlType") != null && StringUtils.hasText(params.get("controlType").toString())) {
            wrapper.eq("CONTROL_TYPE", params.get("controlType"));
        }
        if (params.get("isEnabled") != null) {
            wrapper.eq("IS_ENABLED", params.get("isEnabled"));
        }

        wrapper.orderByDesc("CREATE_TIME");

        return controlRuleMapper.selectList(wrapper);
    }

    /**
     * 生成规则编码
     */
    private String generateRuleCode() {
        return "RULE" + System.currentTimeMillis();
    }
}

