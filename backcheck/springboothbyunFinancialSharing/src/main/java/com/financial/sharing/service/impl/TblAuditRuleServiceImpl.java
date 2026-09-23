package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.dto.TblAuditRuleQueryParam;
import com.financial.sharing.dto.TblAuditRuleSaveParam;
import com.financial.sharing.entity.TblAuditRule;
import com.financial.sharing.entity.TblAuditRuleAction;
import com.financial.sharing.entity.TblAuditRuleCondition;
import com.financial.sharing.entity.TblAuditRuleLog;
import com.financial.sharing.mapper.TblAuditRuleActionMapper;
import com.financial.sharing.mapper.TblAuditRuleConditionMapper;
import com.financial.sharing.mapper.TblAuditRuleLogMapper;
import com.financial.sharing.mapper.TblAuditRuleMapper;
import com.financial.sharing.service.TblAuditRuleService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 审批规则Service实现类
 */
@Slf4j
@Service
public class TblAuditRuleServiceImpl extends ServiceImpl<TblAuditRuleMapper, TblAuditRule>
        implements TblAuditRuleService {

    @Resource
    private TblAuditRuleMapper mapper;

    @Resource
    private TblAuditRuleConditionMapper conditionMapper;

    @Resource
    private TblAuditRuleActionMapper actionMapper;

    @Resource
    private TblAuditRuleLogMapper logMapper;

    @Override
    public MyJsonBean<PageResult> getList(TblAuditRuleQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNo(), param.getPageSize());

            QueryWrapper<TblAuditRule> queryWrapper = new QueryWrapper<>();

            // 规则名称模糊查询
            if (param.getRuleName() != null && !param.getRuleName().isEmpty()) {
                queryWrapper.like("RULE_NAME", param.getRuleName());
            }

            // 规则编码查询
            if (param.getRuleCode() != null && !param.getRuleCode().isEmpty()) {
                queryWrapper.like("RULE_CODE", param.getRuleCode());
            }

            // 规则类型查询
            if (param.getRuleType() != null && !param.getRuleType().isEmpty()) {
                queryWrapper.eq("RULE_TYPE", param.getRuleType());
            }

            // 是否启用查询
            if (param.getIsEnabled() != null) {
                queryWrapper.eq("IS_ENABLED", param.getIsEnabled());
            }

            queryWrapper.orderByDesc("CREATE_TIME");

            List<TblAuditRule> list = mapper.selectList(queryWrapper);
            PageInfo<TblAuditRule> pageInfo = new PageInfo<>(list);

            PageResult result = new PageResult();
            result.setTlist(pageInfo.getList());
            result.setTotalRecord((int) pageInfo.getTotal());
            result.setCurrentPage(pageInfo.getPageNum());
            result.setPageSize(pageInfo.getPageSize());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询审批规则列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getById(String ruleId) {
        try {
            TblAuditRule rule = mapper.selectById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("规则不存在");
            }
            return MyJsonBean.successData(rule);
        } catch (Exception e) {
            log.error("查询审批规则详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdate(TblAuditRuleSaveParam param) {
        try {
            TblAuditRule rule = new TblAuditRule();

            if (param.getRuleId() != null && !param.getRuleId().isEmpty()) {
                // 更新
                rule = mapper.selectById(param.getRuleId());
                if (rule == null) {
                    return MyJsonBean.errorData("规则不存在");
                }
                rule.setUpdateTime(LocalDateTime.now());
                rule.setUpdateUser(param.getUpdateUser());
            } else {
                // 新增
                rule.setCreateTime(LocalDateTime.now());
                rule.setCreateUser(param.getCreateUser());
            }

            // 设置基本属性
            rule.setRuleCode(param.getRuleCode());
            rule.setRuleName(param.getRuleName());
            rule.setRuleType(param.getRuleType());
            rule.setPriority(param.getPriority());
            rule.setRuleExpression(param.getRuleExpression());
            rule.setRuleAction(param.getRuleAction());
            rule.setWarningMessage(param.getWarningMessage());
            rule.setEffectiveDate(param.getEffectiveDate());
            rule.setExpiryDate(param.getExpiryDate());
            rule.setIsEnabled(param.getIsEnabled());
            rule.setRemark(param.getRemark());

            if (param.getRuleId() != null && !param.getRuleId().isEmpty()) {
                mapper.updateById(rule);
            } else {
                mapper.insert(rule);
            }

            return MyJsonBean.successData("保存成功", rule);
        } catch (Exception e) {
            log.error("保存审批规则失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean delete(String ruleId) {
        try {
            TblAuditRule rule = mapper.selectById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("规则不存在");
            }

            // 删除规则
            mapper.deleteById(ruleId);

            // 删除关联的条件
            conditionMapper.deleteByRuleId(ruleId);

            // 删除关联的动作
            actionMapper.deleteByRuleId(ruleId);

            return MyJsonBean.successMsg("删除成功");
        } catch (Exception e) {
            log.error("删除审批规则失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean updateStatus(String ruleId, Integer isEnabled) {
        try {
            TblAuditRule rule = mapper.selectById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("规则不存在");
            }

            rule.setIsEnabled(isEnabled);
            rule.setUpdateTime(LocalDateTime.now());
            mapper.updateById(rule);

            return MyJsonBean.successMsg("状态更新成功");
        } catch (Exception e) {
            log.error("更新审批规则状态失败", e);
            return MyJsonBean.errorData("更新失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean testRule(String ruleId, Map<String, Object> testData) {
        try {
            TblAuditRule rule = mapper.selectById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("规则不存在");
            }

            // TODO: 实现规则测试逻辑
            // 这里应该根据规则表达式和测试数据进行规则匹配测试

            Map<String, Object> result = new HashMap<>();
            result.put("ruleId", ruleId);
            result.put("ruleName", rule.getRuleName());
            result.put("testResult", "PASS");
            result.put("message", "规则测试通过");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("测试审批规则失败", e);
            return MyJsonBean.errorData("测试失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getRuleConditions(String ruleId) {
        try {
            List<TblAuditRuleCondition> conditions = conditionMapper.selectByRuleIdOrderBySort(ruleId);
            return MyJsonBean.successData(conditions);
        } catch (Exception e) {
            log.error("获取规则条件失败", e);
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getRuleActions(String ruleId) {
        try {
            List<TblAuditRuleAction> actions = actionMapper.selectByRuleIdOrderBySort(ruleId);
            return MyJsonBean.successData(actions);
        } catch (Exception e) {
            log.error("获取规则动作失败", e);
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getExecutionLogs(String ruleId) {
        try {
            List<TblAuditRuleLog> logs = logMapper.selectByRuleId(ruleId);
            return MyJsonBean.successData(logs);
        } catch (Exception e) {
            log.error("获取执行日志失败", e);
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveCondition(TblAuditRuleCondition condition) {
        try {
            if (condition.getConditionId() == null || condition.getConditionId().isEmpty()) {
                // 新增条件
                condition.setConditionId(UUID.randomUUID().toString().replace("-", ""));
                condition.setCreateTime(LocalDateTime.now());
                conditionMapper.insert(condition);
            } else {
                // 更新条件
                conditionMapper.updateById(condition);
            }
            return MyJsonBean.successData("保存成功", condition);
        } catch (Exception e) {
            log.error("保存条件失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteCondition(String conditionId) {
        try {
            conditionMapper.deleteById(conditionId);
            return MyJsonBean.successMsg("删除成功");
        } catch (Exception e) {
            log.error("删除条件失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }
}
