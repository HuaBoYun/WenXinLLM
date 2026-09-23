package com.global.treasurer.service.impl;
import java.util.Arrays;
import com.global.treasurer.exception.ServiceException;
import com.global.treasurer.entity.TcBusinessRule;
import com.global.treasurer.mapper.TcBusinessRuleMapper;
import com.global.treasurer.service.TcBusinessRuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 业务规则管理Service实现类
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
@Service
public class TcBusinessRuleServiceImpl implements TcBusinessRuleService {
    private static final Logger log = LoggerFactory.getLogger(TcBusinessRuleServiceImpl.class);

    @Resource
    private TcBusinessRuleMapper tcBusinessRuleMapper;

    @Override
    public PageInfo<TcBusinessRule> getList(int pageNum, int pageSize, String ruleCode, String ruleName, String ruleType, 
                                           String ruleCategory, String businessDomain, String isActive, String status) {
        PageHelper.startPage(pageNum, pageSize);
        List<TcBusinessRule> list = tcBusinessRuleMapper.getList(ruleCode, ruleName, ruleType, ruleCategory, businessDomain, isActive, status);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TcBusinessRule saveOrUpdate(TcBusinessRule businessRule) {
        if (businessRule == null) {
            throw new ServiceException("业务规则数据不能为空");
        }

        // 验证必填字段
        if (!StringUtils.hasText(businessRule.getRuleCode())) {
            throw new ServiceException("规则编码不能为空");
        }
        if (!StringUtils.hasText(businessRule.getRuleName())) {
            throw new ServiceException("规则名称不能为空");
        }
        if (!StringUtils.hasText(businessRule.getRuleType())) {
            throw new ServiceException("规则类型不能为空");
        }
        if (!StringUtils.hasText(businessRule.getRuleExpression())) {
            throw new ServiceException("规则表达式不能为空");
        }

        // 检查规则编码是否重复
        TcBusinessRule existing = tcBusinessRuleMapper.getByRuleCode(businessRule.getRuleCode());

        Date now = new Date();
        if (StringUtils.hasText(businessRule.getId())) {
            // 更新
            if (existing != null && !existing.getId().equals(businessRule.getId())) {
                throw new ServiceException("规则编码已存在");
            }
            businessRule.setUpdateTime(now);
            tcBusinessRuleMapper.updateByPrimaryKeySelective(businessRule);
        } else {
            // 新增
            if (existing != null) {
                throw new ServiceException("规则编码已存在");
            }
            businessRule.setId(UUID.randomUUID().toString());
            businessRule.setCreateTime(now);
            businessRule.setUpdateTime(now);
            if (!StringUtils.hasText(businessRule.getStatus())) {
                businessRule.setStatus("1");
            }
            if (!StringUtils.hasText(businessRule.getIsActive())) {
                businessRule.setIsActive("1");
            }
            if (businessRule.getExecutionOrder() == null) {
                businessRule.setExecutionOrder(0);
            }
            if (!StringUtils.hasText(businessRule.getRuleSyntax())) {
                businessRule.setRuleSyntax("SIMPLE");
            }
            tcBusinessRuleMapper.insertSelective(businessRule);
        }

        return businessRule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        if (!StringUtils.hasText(id)) {
            throw new ServiceException("规则ID不能为空");
        }

        TcBusinessRule businessRule = tcBusinessRuleMapper.selectByPrimaryKey(id);
        if (businessRule == null) {
            throw new ServiceException("业务规则不存在");
        }

        tcBusinessRuleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public TcBusinessRule getById(String id) {
        if (!StringUtils.hasText(id)) {
            throw new ServiceException("规则ID不能为空");
        }

        TcBusinessRule businessRule = tcBusinessRuleMapper.selectByPrimaryKey(id);
        if (businessRule == null) {
            throw new ServiceException("业务规则不存在");
        }

        return businessRule;
    }

    @Override
    public TcBusinessRule getByRuleCode(String ruleCode) {
        if (!StringUtils.hasText(ruleCode)) {
            throw new ServiceException("规则编码不能为空");
        }

        return tcBusinessRuleMapper.getByRuleCode(ruleCode);
    }

    @Override
    public List<TcBusinessRule> getByRuleType(String ruleType) {
        if (!StringUtils.hasText(ruleType)) {
            throw new ServiceException("规则类型不能为空");
        }

        return tcBusinessRuleMapper.getByRuleType(ruleType);
    }

    @Override
    public List<TcBusinessRule> getByBusinessDomain(String businessDomain) {
        if (!StringUtils.hasText(businessDomain)) {
            throw new ServiceException("业务域不能为空");
        }

        return tcBusinessRuleMapper.getByBusinessDomain(businessDomain);
    }

    @Override
    public List<TcBusinessRule> getByRuleCategory(String ruleCategory) {
        if (!StringUtils.hasText(ruleCategory)) {
            throw new ServiceException("规则分类不能为空");
        }

        return tcBusinessRuleMapper.getByRuleCategory(ruleCategory);
    }

    @Override
    public List<TcBusinessRule> getActiveRules(String ruleType, String businessDomain) {
        return tcBusinessRuleMapper.getActiveRules(ruleType, businessDomain);
    }

    @Override
    public List<TcBusinessRule> getByExecutionOrder(String ruleType, String businessDomain) {
        return tcBusinessRuleMapper.getByExecutionOrder(ruleType, businessDomain);
    }

    @Override
    public List<TcBusinessRule> getValidRules(Date currentDate, String ruleType, String businessDomain) {
        if (currentDate == null) {
            currentDate = new Date();
        }

        return tcBusinessRuleMapper.getValidRules(currentDate, ruleType, businessDomain);
    }

    @Override
    public List<String> getAllRuleTypes() {
        return tcBusinessRuleMapper.getAllRuleTypes();
    }

    @Override
    public List<String> getAllRuleCategories() {
        return tcBusinessRuleMapper.getAllRuleCategories();
    }

    @Override
    public List<String> getAllBusinessDomains() {
        return tcBusinessRuleMapper.getAllBusinessDomains();
    }

    @Override
    public List<String> getAllRuleSyntax() {
        return tcBusinessRuleMapper.getAllRuleSyntax();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchImport(List<TcBusinessRule> businessRules, String updateMode) {
        if (businessRules == null || businessRules.isEmpty()) {
            throw new ServiceException("导入数据不能为空");
        }

        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errorMessages = new ArrayList<>();

        for (TcBusinessRule businessRule : businessRules) {
            try {
                if ("UPSERT".equals(updateMode)) {
                    // 检查是否存在
                    TcBusinessRule existing = tcBusinessRuleMapper.getByRuleCode(businessRule.getRuleCode());

                    if (existing != null) {
                        // 更新
                        businessRule.setId(existing.getId());
                        businessRule.setUpdateTime(new Date());
                        tcBusinessRuleMapper.updateByPrimaryKeySelective(businessRule);
                    } else {
                        // 新增
                        businessRule.setId(UUID.randomUUID().toString());
                        businessRule.setCreateTime(new Date());
                        businessRule.setUpdateTime(new Date());
                        if (!StringUtils.hasText(businessRule.getStatus())) {
                            businessRule.setStatus("ACTIVE");
                        }
                        if (!StringUtils.hasText(businessRule.getIsActive())) {
                            businessRule.setIsActive("Y");
                        }
                        if (businessRule.getExecutionOrder() == null) {
                            businessRule.setExecutionOrder(1);
                        }
                        tcBusinessRuleMapper.insertSelective(businessRule);
                    }
                } else {
                    // 仅新增
                    businessRule.setId(UUID.randomUUID().toString());
                    businessRule.setCreateTime(new Date());
                    businessRule.setUpdateTime(new Date());
                    if (!StringUtils.hasText(businessRule.getStatus())) {
                        businessRule.setStatus("ACTIVE");
                    }
                    if (!StringUtils.hasText(businessRule.getIsActive())) {
                        businessRule.setIsActive("Y");
                    }
                    if (businessRule.getExecutionOrder() == null) {
                        businessRule.setExecutionOrder(1);
                    }
                    tcBusinessRuleMapper.insertSelective(businessRule);
                }
                successCount++;
            } catch (Exception e) {
                failCount++;
                errorMessages.add("第" + (successCount + failCount) + "条记录导入失败：" + e.getMessage());
                log.error("业务规则导入失败", e);
            }
        }

        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errorMessages", errorMessages);
        result.put("totalCount", businessRules.size());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String id, String status, String updateUser) {
        if (!StringUtils.hasText(id)) {
            throw new ServiceException("规则ID不能为空");
        }
        if (!StringUtils.hasText(status)) {
            throw new ServiceException("状态不能为空");
        }

        TcBusinessRule businessRule = tcBusinessRuleMapper.selectByPrimaryKey(id);
        if (businessRule == null) {
            throw new ServiceException("业务规则不存在");
        }

        tcBusinessRuleMapper.updateStatus(id, status, updateUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateActiveStatus(String id, String isActive, String updateUser) {
        if (!StringUtils.hasText(id)) {
            throw new ServiceException("规则ID不能为空");
        }
        if (!StringUtils.hasText(isActive)) {
            throw new ServiceException("激活状态不能为空");
        }

        TcBusinessRule businessRule = tcBusinessRuleMapper.selectByPrimaryKey(id);
        if (businessRule == null) {
            throw new ServiceException("业务规则不存在");
        }

        tcBusinessRuleMapper.updateActiveStatus(id, isActive, updateUser);
    }

    @Override
    public Map<String, Object> getStatistics() {
        List<Map<String, Object>> statisticsList = tcBusinessRuleMapper.getStatistics();
        Map<String, Object> result = new HashMap<>();
        if (statisticsList != null && !statisticsList.isEmpty()) {
            result = statisticsList.get(0);
        }
        return result;
    }

    @Override
    public TcBusinessRule copyRule(String sourceRuleId, String newRuleCode, String newRuleName, String createUser) {
        try {
            // 获取源规则
            TcBusinessRule sourceRule = tcBusinessRuleMapper.selectByPrimaryKey(sourceRuleId);
            if (sourceRule == null) {
                throw new ServiceException("源规则不存在");
            }

            // 检查新规则编码是否已存在
            TcBusinessRule existRule = tcBusinessRuleMapper.getByRuleCode(newRuleCode);
            if (existRule != null) {
                throw new ServiceException("规则编码已存在: " + newRuleCode);
            }

            // 创建新规则
            TcBusinessRule newRule = new TcBusinessRule();
            newRule.setId(UUID.randomUUID().toString());
            newRule.setRuleCode(newRuleCode);
            newRule.setRuleName(newRuleName);
            newRule.setRuleType(sourceRule.getRuleType());
            newRule.setBusinessDomain(sourceRule.getBusinessDomain());
            newRule.setRuleCategory(sourceRule.getRuleCategory());
            newRule.setRuleExpression(sourceRule.getRuleExpression());
            newRule.setRuleSyntax(sourceRule.getRuleSyntax());
            newRule.setExecutionOrder(sourceRule.getExecutionOrder());
            newRule.setRuleDesc(sourceRule.getRuleDesc());
            newRule.setEffectiveDate(new Date());
            newRule.setExpireDate(sourceRule.getExpireDate());
            newRule.setStatus("ACTIVE");
            newRule.setCreateUser(createUser);
            newRule.setUpdateUser(createUser);
            newRule.setCreateTime(new Date());
            newRule.setUpdateTime(new Date());
            newRule.setVersionNo(1);

            // 保存新规则
            int result = tcBusinessRuleMapper.insert(newRule);
            if (result > 0) {
                log.info("规则复制成功，源规则ID: {}, 新规则编码: {}", sourceRuleId, newRuleCode);
                return newRule;
            } else {
                throw new ServiceException("规则复制失败");
            }
        } catch (Exception e) {
            log.error("规则复制异常，源规则ID: {}, 新规则编码: {}, 错误: {}", sourceRuleId, newRuleCode, e.getMessage());
            throw new ServiceException("规则复制失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> validateRuleExpression(String ruleExpression, String ruleSyntax, Map<String, Object> testData) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();

        try {
            // 基本验证
            if (!StringUtils.hasText(ruleExpression)) {
                errors.add("规则表达式不能为空");
            }
            if (!StringUtils.hasText(ruleSyntax)) {
                errors.add("规则语法不能为空");
            }

            if (!errors.isEmpty()) {
                result.put("valid", false);
                result.put("errors", errors);
                return result;
            }

            // 语法验证（这里可以根据不同的规则语法进行具体验证）
            boolean syntaxValid = true;
            String syntaxError = null;

            switch (ruleSyntax.toUpperCase()) {
                case "GROOVY":
                    // Groovy语法验证
                    syntaxValid = validateGroovyExpression(ruleExpression);
                    if (!syntaxValid) {
                        syntaxError = "Groovy表达式语法错误";
                    }
                    break;
                case "SPEL":
                    // Spring Expression Language验证
                    syntaxValid = validateSpelExpression(ruleExpression);
                    if (!syntaxValid) {
                        syntaxError = "SpEL表达式语法错误";
                    }
                    break;
                case "JAVASCRIPT":
                    // JavaScript验证
                    syntaxValid = validateJavaScriptExpression(ruleExpression);
                    if (!syntaxValid) {
                        syntaxError = "JavaScript表达式语法错误";
                    }
                    break;
                default:
                    syntaxError = "不支持的规则语法: " + ruleSyntax;
                    syntaxValid = false;
            }

            if (!syntaxValid) {
                errors.add(syntaxError);
            }

            // 如果有测试数据，尝试执行验证
            Object executionResult = null;
            if (syntaxValid && testData != null && !testData.isEmpty()) {
                try {
                    executionResult = executeRuleExpression(ruleExpression, ruleSyntax, testData);
                    result.put("executionResult", executionResult);
                } catch (Exception e) {
                    errors.add("规则执行失败: " + e.getMessage());
                }
            }

            result.put("valid", errors.isEmpty());
            result.put("errors", errors);

            if (errors.isEmpty()) {
                result.put("message", "规则表达式验证通过");
            }

        } catch (Exception e) {
            log.error("规则表达式验证异常", e);
            errors.add("验证过程发生异常: " + e.getMessage());
            result.put("valid", false);
            result.put("errors", errors);
        }

        return result;
    }

    private boolean validateGroovyExpression(String expression) {
        // 简单的Groovy语法验证
        try {
            // 这里可以使用Groovy引擎进行语法检查
            return true; // 暂时返回true
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validateSpelExpression(String expression) {
        // 简单的SpEL语法验证
        try {
            // 这里可以使用Spring的SpEL解析器进行语法检查
            return true; // 暂时返回true
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validateJavaScriptExpression(String expression) {
        // 简单的JavaScript语法验证
        try {
            // 这里可以使用JavaScript引擎进行语法检查
            return true; // 暂时返回true
        } catch (Exception e) {
            return false;
        }
    }

    private Object executeRuleExpression(String expression, String syntax, Map<String, Object> data) {
        // 执行规则表达式
        try {
            // 这里可以根据不同的语法执行表达式
            return "执行结果示例"; // 暂时返回示例结果
        } catch (Exception e) {
            throw new RuntimeException("规则执行失败", e);
        }
    }

    @Override
    public Map<String, Object> executeRule(String ruleCode, Map<String, Object> inputData, String businessKey,
                                          String businessType, String executionUser) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 根据规则编码查找规则
            TcBusinessRule rule = tcBusinessRuleMapper.selectByRuleCode(ruleCode);
            if (rule == null) {
                result.put("success", false);
                result.put("message", "规则不存在: " + ruleCode);
                return result;
            }

            // 检查规则是否激活
            if (!"ACTIVE".equals(rule.getStatus()) || !"Y".equals(rule.getIsActive())) {
                result.put("success", false);
                result.put("message", "规则未激活: " + ruleCode);
                return result;
            }

            // 检查规则是否在有效期内
            Date now = new Date();
            if (rule.getEffectiveDate() != null && now.before(rule.getEffectiveDate())) {
                result.put("success", false);
                result.put("message", "规则尚未生效");
                return result;
            }

            if (rule.getExpireDate() != null && now.after(rule.getExpireDate())) {
                result.put("success", false);
                result.put("message", "规则已过期");
                return result;
            }

            // 执行规则表达式
            Object executeResult = executeRuleExpression(rule.getRuleExpression(), rule.getRuleSyntax(), inputData);

            result.put("success", true);
            result.put("ruleCode", ruleCode);
            result.put("result", executeResult);
            result.put("message", "规则执行成功");

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "规则执行失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> executeRules(String ruleType, String businessDomain, Map<String, Object> inputData,
                                           String businessKey, String businessType, String executionUser) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<Map<String, Object>> executionResults = new ArrayList<>();

        try {
            // 获取符合条件的规则
            List<TcBusinessRule> rules = tcBusinessRuleMapper.getActiveRules(ruleType, businessDomain);

            if (rules == null || rules.isEmpty()) {
                result.put("success", true);
                result.put("message", "没有找到匹配的规则");
                result.put("executionResults", executionResults);
                return result;
            }

            // 按执行顺序排序
            rules.sort((r1, r2) -> {
                Integer order1 = r1.getExecutionOrder() != null ? r1.getExecutionOrder() : 999;
                Integer order2 = r2.getExecutionOrder() != null ? r2.getExecutionOrder() : 999;
                return order1.compareTo(order2);
            });

            boolean allSuccess = true;

            // 逐个执行规则
            for (TcBusinessRule rule : rules) {
                Map<String, Object> ruleResult = new HashMap<>();
                ruleResult.put("ruleCode", rule.getRuleCode());
                ruleResult.put("ruleName", rule.getRuleName());
                ruleResult.put("executionOrder", rule.getExecutionOrder());

                try {
                    // 检查规则是否在有效期内
                    Date now = new Date();
                    if (rule.getEffectiveDate() != null && now.before(rule.getEffectiveDate())) {
                        ruleResult.put("success", false);
                        ruleResult.put("message", "规则尚未生效");
                        ruleResult.put("skipped", true);
                        executionResults.add(ruleResult);
                        continue;
                    }

                    if (rule.getExpireDate() != null && now.after(rule.getExpireDate())) {
                        ruleResult.put("success", false);
                        ruleResult.put("message", "规则已过期");
                        ruleResult.put("skipped", true);
                        executionResults.add(ruleResult);
                        continue;
                    }

                    // 执行规则表达式
                    Object executeResult = executeRuleExpression(rule.getRuleExpression(), rule.getRuleSyntax(), inputData);

                    ruleResult.put("success", true);
                    ruleResult.put("result", executeResult);
                    ruleResult.put("message", "规则执行成功");

                } catch (Exception e) {
                    log.error("规则执行失败: " + rule.getRuleCode(), e);
                    ruleResult.put("success", false);
                    ruleResult.put("message", "规则执行失败: " + e.getMessage());
                    ruleResult.put("error", e.getMessage());
                    allSuccess = false;
                    errors.add("规则[" + rule.getRuleCode() + "]执行失败: " + e.getMessage());
                }

                executionResults.add(ruleResult);
            }

            result.put("success", allSuccess);
            result.put("message", allSuccess ? "所有规则执行成功" : "部分规则执行失败");
            result.put("executionResults", executionResults);
            result.put("totalRules", rules.size());
            result.put("successCount", (int) executionResults.stream().filter(r -> Boolean.TRUE.equals(r.get("success"))).count());
            result.put("errors", errors);

        } catch (Exception e) {
            log.error("规则执行过程异常", e);
            result.put("success", false);
            result.put("message", "规则执行过程异常: " + e.getMessage());
            result.put("errors", Arrays.asList(e.getMessage()));
        }

        return result;
    }

    @Override
    public List<TcBusinessRule> getExpiringRules(int days) {
        try {
            // 查询即将过期的规则
            return tcBusinessRuleMapper.selectExpiringRules(days);
        } catch (Exception e) {
            log.error("查询即将过期的规则失败", e);
            throw new ServiceException("查询即将过期的规则失败: " + e.getMessage());
        }
    }

    @Override
    public int countByTypeAndDomain(String ruleType, String businessDomain) {
        try {
            return tcBusinessRuleMapper.countByTypeAndDomain(ruleType, businessDomain);
        } catch (Exception e) {
            log.error("统计规则数量失败", e);
            throw new ServiceException("统计规则数量失败: " + e.getMessage());
        }
    }

    @Override
    public boolean checkRuleCodeExists(String ruleCode, String excludeId) {
        try {
            int count = tcBusinessRuleMapper.checkRuleCodeExists(ruleCode, excludeId);
            return count > 0;
        } catch (Exception e) {
            log.error("检查规则编码是否存在失败", e);
            throw new ServiceException("检查规则编码是否存在失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> batchUpdate(List<TcBusinessRule> businessRules) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errorMessages = new ArrayList<>();

        try {
            for (TcBusinessRule businessRule : businessRules) {
                try {
                    // 验证数据
                    if (businessRule.getId() == null) {
                        failCount++;
                        errorMessages.add("业务规则ID不能为空");
                        continue;
                    }

                    // 更新业务规则数据
                    businessRule.setUpdateTime(new Date());
                    int updateCount = tcBusinessRuleMapper.updateByPrimaryKeySelective(businessRule);

                    if (updateCount > 0) {
                        successCount++;
                    } else {
                        failCount++;
                        errorMessages.add("业务规则不存在: " + businessRule.getId());
                    }
                } catch (Exception e) {
                    failCount++;
                    errorMessages.add("更新业务规则失败: " + e.getMessage());
                    log.error("批量更新业务规则失败", e);
                }
            }

            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("totalCount", businessRules.size());
            result.put("errorMessages", errorMessages);
            result.put("success", failCount == 0);

        } catch (Exception e) {
            log.error("批量更新业务规则失败", e);
            result.put("success", false);
            result.put("message", "批量更新失败: " + e.getMessage());
        }

        return result;
    }
}
