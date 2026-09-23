package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcProductTermRule;
import com.global.treasurer.mapper.TcProductTermRuleMapper;
import com.global.treasurer.service.TcProductTermRuleService;
import com.global.treasurer.util.PageResult;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * 产品期限规则表 Service业务层处理
 * @author hbyun-admin
 * @date 2024-01-01
 */
@Service
public class TcProductTermRuleServiceImpl implements TcProductTermRuleService {
    @Autowired
    private TcProductTermRuleMapper termRuleMapper;

    @Override
    public PageResult<TcProductTermRule> selectPage(Integer pageNum, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNum, pageSize);
        
        Example example = new Example(TcProductTermRule.class);
        Example.Criteria criteria = example.createCriteria();
        
        // 构建查询条件
        if (params != null) {
            if (!StringUtils.isEmpty((String) params.get("productId"))) {
                criteria.andEqualTo("productId", params.get("productId"));
            }
            if (!StringUtils.isEmpty((String) params.get("termType"))) {
                criteria.andEqualTo("termType", params.get("termType"));
            }
            if (!StringUtils.isEmpty((String) params.get("calculationMethod"))) {
                criteria.andEqualTo("calculationMethod", params.get("calculationMethod"));
            }
            if (!StringUtils.isEmpty((String) params.get("status"))) {
                criteria.andEqualTo("status", params.get("status"));
            }
        }
        
        example.setOrderByClause("create_time DESC");
        List<TcProductTermRule> list = termRuleMapper.selectByExample(example);
        PageInfo<TcProductTermRule> pageInfo = new PageInfo<>(list);
        
        return new PageResult<>((int) pageInfo.getTotal(), pageNum, (int) pageInfo.getPages(), pageSize, list);
    }

    @Override
    public TcProductTermRule selectById(String id) {
        return termRuleMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int insert(TcProductTermRule rule) {
        if (StringUtils.isEmpty(rule.getId())) {
            rule.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        rule.setCreateTime(new Date());
        rule.setUpdateTime(new Date());
        rule.setVersionNo(0L);
        if (StringUtils.isEmpty(rule.getStatus())) {
            rule.setStatus("1");
        }
        return termRuleMapper.insert(rule);
    }

    @Override
    @Transactional
    public int update(TcProductTermRule rule) {
        rule.setUpdateTime(new Date());
        return termRuleMapper.updateByPrimaryKeySelective(rule);
    }

    @Override
    @Transactional
    public int deleteById(String id) {
        return termRuleMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int deleteByIds(String[] ids) {
        return Arrays.stream(ids).mapToInt(this::deleteById).sum();
    }

    @Override
    public List<TcProductTermRule> selectByProductId(String productId) {
        Example example = new Example(TcProductTermRule.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId", productId);
        criteria.andEqualTo("status", "1");
        example.setOrderByClause("create_time ASC");
        
        return termRuleMapper.selectByExample(example);
    }

    @Override
    public List<String> validateTermRule(TcProductTermRule rule) {
        List<String> errors = new ArrayList<>();
        
        // 验证必填字段
        if (StringUtils.isEmpty(rule.getProductId())) {
            errors.add("产品ID不能为空");
        }
        if (StringUtils.isEmpty(rule.getTermType())) {
            errors.add("期限类型不能为空");
        }
        if (StringUtils.isEmpty(rule.getCalculationMethod())) {
            errors.add("期限计算方式不能为空");
        }
        
        // 验证期限范围
        if (rule.getMinTerm() != null && rule.getMaxTerm() != null) {
            if (rule.getMinTerm() > rule.getMaxTerm()) {
                errors.add("最小期限不能大于最大期限");
            }
        }
        
        // 验证提前终止规则
        if ("1".equals(rule.getEarlyTerminationAllowed()) && !StringUtils.hasText(rule.getEarlyTerminationRules())) {
            errors.add("允许提前终止时必须设置提前终止规则");
        }

        // 验证展期规则
        if ("1".equals(rule.getExtensionAllowed()) && !StringUtils.hasText(rule.getExtensionRules())) {
            errors.add("允许展期时必须设置展期规则");
        }
        
        return errors;
    }

    @Override
    public List<Map<String, Object>> getTermTypeOptions() {
        List<Map<String, Object>> options = new ArrayList<>();
        
        Map<String, Object> option1 = new HashMap<>();
        option1.put("value", "FIXED");
        option1.put("label", "固定期限");
        option1.put("description", "产品有固定的存续期限");
        options.add(option1);
        
        Map<String, Object> option2 = new HashMap<>();
        option2.put("value", "FLEXIBLE");
        option2.put("label", "灵活期限");
        option2.put("description", "产品期限可以灵活调整");
        options.add(option2);
        
        Map<String, Object> option3 = new HashMap<>();
        option3.put("value", "PERPETUAL");
        option3.put("label", "永续期限");
        option3.put("description", "产品无固定到期日");
        options.add(option3);
        
        return options;
    }

    @Override
    public List<Map<String, Object>> getCalculationMethodOptions() {
        List<Map<String, Object>> options = new ArrayList<>();
        
        Map<String, Object> option1 = new HashMap<>();
        option1.put("value", "NATURAL_DAYS");
        option1.put("label", "自然日");
        option1.put("description", "按自然日计算期限");
        options.add(option1);
        
        Map<String, Object> option2 = new HashMap<>();
        option2.put("value", "BUSINESS_DAYS");
        option2.put("label", "工作日");
        option2.put("description", "按工作日计算期限");
        options.add(option2);
        
        Map<String, Object> option3 = new HashMap<>();
        option3.put("value", "MONTHS");
        option3.put("label", "月份");
        option3.put("description", "按月份计算期限");
        options.add(option3);
        
        Map<String, Object> option4 = new HashMap<>();
        option4.put("value", "YEARS");
        option4.put("label", "年份");
        option4.put("description", "按年份计算期限");
        options.add(option4);
        
        return options;
    }

    @Override
    public List<Map<String, Object>> getEarlyTerminationOptions() {
        List<Map<String, Object>> options = new ArrayList<>();
        
        Map<String, Object> option1 = new HashMap<>();
        option1.put("value", "ALLOW");
        option1.put("label", "允许");
        option1.put("description", "允许提前终止");
        options.add(option1);
        
        Map<String, Object> option2 = new HashMap<>();
        option2.put("value", "FORBID");
        option2.put("label", "禁止");
        option2.put("description", "禁止提前终止");
        options.add(option2);
        
        Map<String, Object> option3 = new HashMap<>();
        option3.put("value", "CONDITIONAL");
        option3.put("label", "有条件允许");
        option3.put("description", "满足特定条件时允许提前终止");
        options.add(option3);
        
        return options;
    }

    @Override
    public List<Map<String, Object>> getExtensionRuleOptions() {
        List<Map<String, Object>> options = new ArrayList<>();
        
        Map<String, Object> option1 = new HashMap<>();
        option1.put("value", "ALLOW");
        option1.put("label", "允许展期");
        option1.put("description", "允许产品展期");
        options.add(option1);
        
        Map<String, Object> option2 = new HashMap<>();
        option2.put("value", "FORBID");
        option2.put("label", "禁止展期");
        option2.put("description", "禁止产品展期");
        options.add(option2);
        
        Map<String, Object> option3 = new HashMap<>();
        option3.put("value", "AUTO_RENEWAL");
        option3.put("label", "自动续期");
        option3.put("description", "产品到期自动续期");
        options.add(option3);
        
        return options;
    }

    @Override
    @Transactional
    public int copyTermRules(String sourceProductId, String targetProductId) {
        List<TcProductTermRule> sourceRules = selectByProductId(sourceProductId);
        int count = 0;
        
        for (TcProductTermRule sourceRule : sourceRules) {
            TcProductTermRule newRule = new TcProductTermRule();
            // 复制属性
            newRule.setProductId(targetProductId);
            newRule.setTermType(sourceRule.getTermType());
            newRule.setCalculationMethod(sourceRule.getCalculationMethod());
            newRule.setMinTerm(sourceRule.getMinTerm());
            newRule.setMaxTerm(sourceRule.getMaxTerm());
            newRule.setTermUnit(sourceRule.getTermUnit());
            newRule.setEarlyTerminationAllowed(sourceRule.getEarlyTerminationAllowed());
            newRule.setEarlyTerminationRules(sourceRule.getEarlyTerminationRules());
            newRule.setExtensionAllowed(sourceRule.getExtensionAllowed());
            newRule.setExtensionRules(sourceRule.getExtensionRules());
            newRule.setAutoRenewalAllowed(sourceRule.getAutoRenewalAllowed());
            newRule.setRenewalRules(sourceRule.getRenewalRules());
            newRule.setRemark(sourceRule.getRemark());
            
            count += insert(newRule);
        }
        
        return count;
    }

    @Override
    @Transactional
    public int batchUpdateStatus(String[] ids, String status) {
        int count = 0;
        for (String id : ids) {
            TcProductTermRule rule = new TcProductTermRule();
            rule.setId(id);
            rule.setStatus(status);
            rule.setUpdateTime(new Date());
            count += termRuleMapper.updateByPrimaryKeySelective(rule);
        }
        return count;
    }

    @Override
    public Map<String, Object> getTermRuleStatistics(String productId) {
        Map<String, Object> statistics = new HashMap<>();
        
        Example example = new Example(TcProductTermRule.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("productId", productId);
        
        List<TcProductTermRule> allRules = termRuleMapper.selectByExample(example);
        
        // 总规则数
        statistics.put("totalCount", allRules.size());
        
        // 启用规则数
        long enabledCount = allRules.stream().filter(rule -> "1".equals(rule.getStatus())).count();
        statistics.put("enabledCount", enabledCount);
        
        // 禁用规则数
        statistics.put("disabledCount", allRules.size() - enabledCount);
        
        // 按期限类型统计
        Map<String, Long> termTypeCount = new HashMap<>();
        allRules.forEach(rule -> {
            String termType = rule.getTermType();
            termTypeCount.put(termType, termTypeCount.getOrDefault(termType, 0L) + 1);
        });
        statistics.put("termTypeCount", termTypeCount);
        
        return statistics;
    }
}
