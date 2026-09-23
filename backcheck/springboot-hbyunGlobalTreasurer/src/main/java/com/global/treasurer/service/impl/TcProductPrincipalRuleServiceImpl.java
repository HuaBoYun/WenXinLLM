package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TcProductPrincipalRule;
import com.global.treasurer.mapper.TcProductPrincipalRuleMapper;
import com.global.treasurer.service.TcProductPrincipalRuleService;
import com.global.treasurer.util.PageResult;
import org.springframework.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

/**
 * 产品本金规则Service实现类
 * @author hbyun-admin
 * @date 2024-01-01
 */
@Service
public class TcProductPrincipalRuleServiceImpl implements TcProductPrincipalRuleService {
    @Autowired
    private TcProductPrincipalRuleMapper principalRuleMapper;

    @Override
    public PageResult<TcProductPrincipalRule> selectByPage(int pageNum, int pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNum, pageSize);
        
        Example example = new Example(TcProductPrincipalRule.class);
        Example.Criteria criteria = example.createCriteria();
        
        if (params != null) {
            if (!StringUtils.isEmpty((String) params.get("productId"))) {
                criteria.andEqualTo("productId", params.get("productId"));
            }
            if (!StringUtils.isEmpty((String) params.get("principalType"))) {
                criteria.andEqualTo("principalType", params.get("principalType"));
            }
            if (!StringUtils.isEmpty((String) params.get("principalFrequency"))) {
                criteria.andEqualTo("principalFrequency", params.get("principalFrequency"));
            }
            if (!StringUtils.isEmpty((String) params.get("status"))) {
                criteria.andEqualTo("status", params.get("status"));
            }
        }
        
        example.orderBy("createTime").desc();
        List<TcProductPrincipalRule> list = principalRuleMapper.selectByExample(example);
        PageInfo<TcProductPrincipalRule> pageInfo = new PageInfo<>(list);
        
        return new PageResult<>((int) pageInfo.getTotal(), pageNum, (int) pageInfo.getPages(), pageSize, list);
    }

    @Override
    public TcProductPrincipalRule selectById(String id) {
        return principalRuleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TcProductPrincipalRule> selectByProductId(String productId) {
        return principalRuleMapper.selectByProductId(productId);
    }

    @Override
    @Transactional
    public int insert(TcProductPrincipalRule rule) {
        if (StringUtils.isEmpty(rule.getId())) {
            rule.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        rule.setCreateTime(new Date());
        rule.setUpdateTime(new Date());
        rule.setVersionNo(0L);
        if (StringUtils.isEmpty(rule.getStatus())) {
            rule.setStatus("1");
        }
        return principalRuleMapper.insert(rule);
    }

    @Override
    @Transactional
    public int update(TcProductPrincipalRule rule) {
        rule.setUpdateTime(new Date());
        return principalRuleMapper.updateByPrimaryKeySelective(rule);
    }

    @Override
    @Transactional
    public int deleteById(String id) {
        return principalRuleMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int deleteByIds(String[] ids) {
        int count = 0;
        for (String id : ids) {
            count += deleteById(id);
        }
        return count;
    }

    @Override
    @Transactional
    public int deleteByProductId(String productId) {
        return principalRuleMapper.deleteByProductId(productId);
    }

    @Override
    @Transactional
    public int batchSave(String productId, List<TcProductPrincipalRule> rules) {
        // 先删除原有规则
        deleteByProductId(productId);
        
        // 批量插入新规则
        if (rules != null && !rules.isEmpty()) {
            for (TcProductPrincipalRule rule : rules) {
                rule.setProductId(productId);
                insert(rule);
            }
            return rules.size();
        }
        return 0;
    }

    @Override
    public Map<String, Object> validateRule(TcProductPrincipalRule rule) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        
        // 验证必填字段
        if (StringUtils.isEmpty(rule.getProductId())) {
            errors.add("产品ID不能为空");
        }
        if (StringUtils.isEmpty(rule.getPrincipalFrequency())) {
            errors.add("本金发生次数不能为空");
        }
        if (StringUtils.isEmpty(rule.getPrincipalType())) {
            errors.add("本金类型不能为空");
        }
        if (StringUtils.isEmpty(rule.getDocumentMode())) {
            errors.add("单据模式不能为空");
        }
        if (StringUtils.isEmpty(rule.getRepaymentMethod())) {
            errors.add("还本赎回方式不能为空");
        }
        
        // 验证金额范围
        if (rule.getMinAmount() != null && rule.getMaxAmount() != null) {
            if (rule.getMinAmount().compareTo(rule.getMaxAmount()) > 0) {
                errors.add("最小金额不能大于最大金额");
            }
        }
        
        // 验证递增金额
        if (rule.getStepAmount() != null && rule.getStepAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("递增金额必须大于0");
        }
        
        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        return result;
    }

    @Override
    public int countByPrincipalType(String principalType) {
        List<TcProductPrincipalRule> rules = principalRuleMapper.selectByPrincipalType(principalType);
        return rules != null ? rules.size() : 0;
    }

    @Override
    public List<Map<String, Object>> getPrincipalTypeOptions() {
        List<Map<String, Object>> options = new ArrayList<>();
        
        Map<String, Object> option1 = new HashMap<>();
        option1.put("value", "FIXED_AMOUNT");
        option1.put("label", "固定金额");
        option1.put("description", "银行贷款、银行定期理财");
        options.add(option1);
        
        Map<String, Object> option2 = new HashMap<>();
        option2.put("value", "QUANTITY_PRICE");
        option2.put("label", "数量单价计算");
        option2.put("description", "发债、债券投资、购买基金、购买股票");
        options.add(option2);
        
        Map<String, Object> option3 = new HashMap<>();
        option3.put("value", "BALANCE");
        option3.put("label", "余额方式");
        option3.put("description", "循环额度贷款业务");
        options.add(option3);
        
        return options;
    }

    @Override
    public List<Map<String, Object>> getRepaymentMethodOptions() {
        List<Map<String, Object>> options = new ArrayList<>();
        
        Map<String, Object> option1 = new HashMap<>();
        option1.put("value", "MATURITY_ONCE");
        option1.put("label", "到期一次性");
        options.add(option1);
        
        Map<String, Object> option2 = new HashMap<>();
        option2.put("value", "INSTALLMENT");
        option2.put("label", "分期还本");
        options.add(option2);
        
        Map<String, Object> option3 = new HashMap<>();
        option3.put("value", "ANYTIME");
        option3.put("label", "随时赎回");
        options.add(option3);
        
        return options;
    }

    @Override
    @Transactional
    public int copyRules(String sourceProductId, String targetProductId) {
        List<TcProductPrincipalRule> sourceRules = selectByProductId(sourceProductId);
        if (sourceRules != null && !sourceRules.isEmpty()) {
            List<TcProductPrincipalRule> targetRules = new ArrayList<>();
            for (TcProductPrincipalRule sourceRule : sourceRules) {
                TcProductPrincipalRule targetRule = new TcProductPrincipalRule();
                // 复制属性
                targetRule.setProductId(targetProductId);
                targetRule.setPrincipalFrequency(sourceRule.getPrincipalFrequency());
                targetRule.setPrincipalType(sourceRule.getPrincipalType());
                targetRule.setCurrencyCodes(sourceRule.getCurrencyCodes());
                targetRule.setDocumentMode(sourceRule.getDocumentMode());
                targetRule.setRepaymentMethod(sourceRule.getRepaymentMethod());
                targetRule.setMinAmount(sourceRule.getMinAmount());
                targetRule.setMaxAmount(sourceRule.getMaxAmount());
                targetRule.setStepAmount(sourceRule.getStepAmount());
                targetRule.setCalculationFormula(sourceRule.getCalculationFormula());
                targetRule.setStatus(sourceRule.getStatus());
                targetRule.setRemark(sourceRule.getRemark());
                targetRules.add(targetRule);
            }
            return batchSave(targetProductId, targetRules);
        }
        return 0;
    }
}
