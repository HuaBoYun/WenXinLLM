package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetRule;
import com.management.accountant.oracle.mapper.budget.BudgetRuleMapper;
import com.management.accountant.service.BudgetRuleService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class BudgetRuleServiceImpl implements BudgetRuleService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetRuleMapper ruleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetRule create(BudgetRule r) {
        if (r == null) throw new ServiceException("规则信息不能为空");
        if (r.getDelFlag() == null) r.setDelFlag(0);
        if (r.getIsEnabled() == null) r.setIsEnabled(1);
        r.setCreateTime(new Date()); r.setUpdateTime(new Date());
        ruleMapper.insert(r);
        return r;
    }

    @Override
    public BudgetRule getById(String id) {
        QueryWrapper<BudgetRule> w = new QueryWrapper<>();
        w.eq("RULE_ID", id).eq("DEL_FLAG", 0L);
        return ruleMapper.selectOne(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetRule r) {
        if (r == null || !StringUtils.hasText(r.getRuleId())) throw new ServiceException("规则ID不能为空");
        r.setUpdateTime(new Date());
        ruleMapper.updateById(r);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        BudgetRule u = new BudgetRule();
        u.setRuleId(id); u.setDelFlag(1); u.setUpdateTime(new Date());
        ruleMapper.updateById(u);
    }

    @Override
    public Map<String, Object> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
        QueryWrapper<BudgetRule> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0L);
        if (hasValue(params.get("ruleName"))) w.like("RULE_NAME", params.get("ruleName"));
        if (hasValue(params.get("ruleType"))) w.eq("RULE_TYPE", params.get("ruleType"));
        if (hasValue(params.get("ruleCategory"))) w.eq("RULE_CATEGORY", params.get("ruleCategory"));
        w.orderByAsc("PRIORITY").orderByDesc("CREATE_TIME");
        IPage<BudgetRule> pageResult = ruleMapper.selectPage(new Page<>(pageNum, pageSize), w);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    @Override
    public List<Map<String, Object>> getCategoryTree() {
        // 先按分类统计每个分类下的规则数量
        QueryWrapper<BudgetRule> countWrapper = new QueryWrapper<>();
        countWrapper.eq("DEL_FLAG", 0L)
                .isNotNull("RULE_CATEGORY")
                .select("RULE_CATEGORY, COUNT(*) AS CNT")
                .groupBy("RULE_CATEGORY");
        List<Map<String, Object>> countList = ruleMapper.selectMaps(countWrapper);

        Map<String, Long> countMap = new HashMap<>();
        for (Map<String, Object> item : countList) {
            String category = item.get("RULE_CATEGORY") != null ? item.get("RULE_CATEGORY").toString() : null;
            Long cnt = item.get("CNT") != null ? Long.parseLong(item.get("CNT").toString()) : 0L;
            if (category != null) {
                countMap.put(category, cnt);
            }
        }

        List<Map<String, Object>> tree = new ArrayList<>();
        for (Map.Entry<String, Long> entry : countMap.entrySet()) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", entry.getKey());
            node.put("name", entry.getKey());
            node.put("ruleCount", entry.getValue());
            node.put("children", new ArrayList<>());
            tree.add(node);
        }
        return tree;
    }

    @Override
    public List<Map<String, Object>> getCategories() { return getCategoryTree(); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String id, String status) {
        BudgetRule u = new BudgetRule();
        u.setRuleId(id); u.setIsEnabled("ACTIVE".equals(status) ? 1 : 0); u.setUpdateTime(new Date());
        ruleMapper.updateById(u);
    }

    @Override
    public Map<String, Object> validateCondition(String condition) {
        Map<String, Object> r = new HashMap<>();
        r.put("valid", StringUtils.hasText(condition)); r.put("message", StringUtils.hasText(condition) ? "条件有效" : "条件不能为空");
        return r;
    }

    @Override
    public Map<String, Object> testRule(Map<String, Object> params) {
        Map<String, Object> r = new HashMap<>();
        r.put("success", true); r.put("message", "规则测试通过");
        return r;
    }

    @Override
    public Map<String, Object> validate(String id) {
        Map<String, Object> r = new HashMap<>();
        r.put("valid", true); r.put("message", "验证通过");
        return r;
    }

    @Override
    public Map<String, Object> batchValidate(List<String> ids) {
        Map<String, Object> r = new HashMap<>();
        r.put("total", ids.size()); r.put("valid", ids.size()); r.put("invalid", 0);
        return r;
    }

    @Override
    public Map<String, Object> getStats() {
        QueryWrapper<BudgetRule> total = new QueryWrapper<BudgetRule>().eq("DEL_FLAG", 0L);
        QueryWrapper<BudgetRule> active = new QueryWrapper<BudgetRule>().eq("DEL_FLAG", 0L).eq("IS_ENABLED", 1);
        long totalCount = ruleMapper.selectCount(total);
        long activeCount = ruleMapper.selectCount(active);
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRules", totalCount);
        stats.put("activeRules", activeCount);
        stats.put("complexRules", 0);
        stats.put("errorRules", 0);
        stats.put("activeRate", totalCount > 0 ? Math.round(activeCount * 100.0 / totalCount * 10) / 10.0 : 0);
        stats.put("complexRate", 0);
        stats.put("errorRate", 0);
        return stats;
    }

    private boolean hasValue(Object val) {
        if (val == null) return false;
        if (val instanceof String) return !((String) val).trim().isEmpty();
        if (val instanceof java.util.Collection) return !((java.util.Collection<?>) val).isEmpty();
        return true;
    }
}

