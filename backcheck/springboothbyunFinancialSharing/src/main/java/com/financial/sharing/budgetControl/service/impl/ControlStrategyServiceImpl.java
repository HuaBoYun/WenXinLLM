package com.financial.sharing.budgetControl.service.impl;

import com.financial.sharing.util.UserUtils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.budgetControl.dto.ControlStrategyConfigDTO;
import com.financial.sharing.budgetControl.dto.ControlStrategyQueryParam;
import com.financial.sharing.budgetControl.entity.TblControlRule;
import com.financial.sharing.budgetControl.mapper.ControlRuleMapper;
import com.financial.sharing.budgetControl.service.ControlStrategyService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * 控制策略配置Service实现类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class ControlStrategyServiceImpl implements ControlStrategyService {

    @Autowired
    private ControlRuleMapper controlRuleMapper;

    @Override
    public MyJsonBean queryPage(ControlStrategyQueryParam param) {
        try {
            LambdaQueryWrapper<TblControlRule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblControlRule::getOrgId, param.getOrgId());
            
            if (StringUtils.isNotBlank(param.getRuleCode())) {
                wrapper.like(TblControlRule::getRuleCode, param.getRuleCode());
            }
            if (StringUtils.isNotBlank(param.getRuleName())) {
                wrapper.like(TblControlRule::getRuleName, param.getRuleName());
            }
            if (StringUtils.isNotBlank(param.getControlType())) {
                wrapper.eq(TblControlRule::getControlType, param.getControlType());
            }
            if (StringUtils.isNotBlank(param.getControlLevel())) {
                wrapper.eq(TblControlRule::getControlLevel, param.getControlLevel());
            }
            if (StringUtils.isNotBlank(param.getControlPeriod())) {
                wrapper.eq(TblControlRule::getControlPeriod, param.getControlPeriod());
            }
            if (StringUtils.isNotBlank(param.getIsEnabled())) {
                wrapper.eq(TblControlRule::getIsEnabled, param.getIsEnabled());
            }

            wrapper.orderByDesc(TblControlRule::getPriority);
            wrapper.orderByDesc(TblControlRule::getCreateTime);

            Page<TblControlRule> page = new Page<>(param.getPageNumber(), param.getPageSize());
            IPage<TblControlRule> result = controlRuleMapper.selectPage(page, wrapper);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询控制策略失败", e);
            return MyJsonBean.errorData("查询控制策略失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean queryByRuleId(String ruleId) {
        try {
            TblControlRule rule = controlRuleMapper.selectById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("控制策略不存在");
            }
            
            // 转换为DTO
            ControlStrategyConfigDTO config = convertToDTO(rule);
            return MyJsonBean.successData(config);
        } catch (Exception e) {
            log.error("查询控制策略失败", e);
            return MyJsonBean.errorData("查询控制策略失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveStrategy(ControlStrategyConfigDTO config) {
        try {
            TblControlRule rule;
            String userId = UserUtils.getUserId();
            
            if (StringUtils.isNotBlank(config.getRuleId())) {
                // 更新
                rule = controlRuleMapper.selectById(config.getRuleId());
                if (rule == null) {
                    return MyJsonBean.errorData("控制策略不存在");
                }
                rule.setUpdateUser(userId);
                rule.setUpdateTime(new Date());
            } else {
                // 新增
                rule = new TblControlRule();
                rule.setRuleId(UUID.randomUUID().toString().replace("-", ""));
                rule.setCreateUser(userId);
                rule.setCreateTime(new Date());
                rule.setUpdateUser(userId);
                rule.setUpdateTime(new Date());
                rule.setOrgId(config.getOrgId());
            }
            
            // 设置基本信息
            rule.setRuleCode(config.getRuleCode());
            rule.setRuleName(config.getRuleName());
            rule.setControlType(config.getControlType());
            rule.setControlLevel(config.getControlLevel());
            rule.setControlPeriod(config.getControlPeriod());
            rule.setThresholdType(config.getThresholdType());
            rule.setThresholdValue(config.getThresholdValue());
            rule.setWarningRatio(config.getWarningRatio());
            rule.setControlAction(config.getControlAction());
            rule.setApprovalWorkflowId(config.getApprovalWorkflowId());
            rule.setIsEnabled(config.getIsEnabled());
            rule.setPriority(config.getPriority());
            
            // 设置维度配置（JSON格式）
            if (config.getDimensionConfigs() != null && !config.getDimensionConfigs().isEmpty()) {
                rule.setControlDimension(JSON.toJSONString(config.getDimensionConfigs()));
            }
            
            // 设置例外用户（JSON格式）
            if (config.getExceptionUsers() != null && !config.getExceptionUsers().isEmpty()) {
                rule.setExceptionUsers(JSON.toJSONString(config.getExceptionUsers()));
            }
            
            if (StringUtils.isNotBlank(config.getRuleId())) {
                controlRuleMapper.updateById(rule);
            } else {
                controlRuleMapper.insert(rule);
            }
            
            return MyJsonBean.successData("保存成功");
        } catch (Exception e) {
            log.error("保存控制策略失败", e);
            return MyJsonBean.errorData("保存控制策略失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean testStrategy(ControlStrategyConfigDTO config, String testAmount,
                                   String testOrgId, String testSubjectCode, String testPeriod) {
        try {
            // 模拟测试控制策略
            BigDecimal amount = new BigDecimal(testAmount);
            BigDecimal threshold = config.getThresholdValue();

            StringBuilder testResult = new StringBuilder();
            testResult.append("=== 控制策略测试结果 ===\n\n");
            testResult.append("测试参数:\n");
            testResult.append("- 组织ID: ").append(testOrgId).append("\n");
            testResult.append("- 科目编码: ").append(testSubjectCode).append("\n");
            testResult.append("- 期间: ").append(testPeriod).append("\n");
            testResult.append("- 测试金额: ").append(testAmount).append("\n\n");

            testResult.append("策略配置:\n");
            testResult.append("- 控制类型: ").append(config.getControlType()).append("\n");
            testResult.append("- 控制级别: ").append(config.getControlLevel()).append("\n");
            testResult.append("- 阈值类型: ").append(config.getThresholdType()).append("\n");
            testResult.append("- 阈值: ").append(threshold).append("\n");
            testResult.append("- 预警比例: ").append(config.getWarningRatio()).append("%\n\n");

            testResult.append("测试结果:\n");

            // 根据控制类型进行测试
            if ("RIGID".equals(config.getControlType())) {
                if (amount.compareTo(threshold) > 0) {
                    testResult.append("- 控制结果: ❌ 阻止\n");
                    testResult.append("- 原因: 申请金额超过阈值，刚性控制将阻止该操作\n");
                } else {
                    testResult.append("- 控制结果: ✅ 通过\n");
                    testResult.append("- 原因: 申请金额未超过阈值\n");
                }
            } else if ("FLEXIBLE".equals(config.getControlType())) {
                if (amount.compareTo(threshold) > 0) {
                    testResult.append("- 控制结果: ⚠️ 需要审批\n");
                    testResult.append("- 原因: 申请金额超过阈值，弹性控制需要审批\n");
                } else {
                    testResult.append("- 控制结果: ✅ 通过\n");
                    testResult.append("- 原因: 申请金额未超过阈值\n");
                }
            } else if ("WARNING".equals(config.getControlType())) {
                if (amount.compareTo(threshold) > 0) {
                    testResult.append("- 控制结果: ⚠️ 警告\n");
                    testResult.append("- 原因: 申请金额超过阈值，预警控制仅提示警告\n");
                } else {
                    testResult.append("- 控制结果: ✅ 通过\n");
                    testResult.append("- 原因: 申请金额未超过阈值\n");
                }
            }

            // 检查预警比例
            if (config.getWarningRatio() != null) {
                BigDecimal warningThreshold = threshold.multiply(config.getWarningRatio())
                    .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
                if (amount.compareTo(warningThreshold) >= 0) {
                    testResult.append("- 预警提示: ⚠️ 已达到预警比例\n");
                }
            }

            return MyJsonBean.successData(testResult.toString());
        } catch (Exception e) {
            log.error("测试控制策略失败", e);
            return MyJsonBean.errorData("测试控制策略失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean copyStrategy(String ruleId, String newRuleCode, String newRuleName) {
        try {
            TblControlRule sourceRule = controlRuleMapper.selectById(ruleId);
            if (sourceRule == null) {
                return MyJsonBean.errorData("源控制策略不存在");
            }

            // 检查新规则编码是否重复
            LambdaQueryWrapper<TblControlRule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblControlRule::getRuleCode, newRuleCode);
            wrapper.eq(TblControlRule::getOrgId, sourceRule.getOrgId());
            long count = controlRuleMapper.selectCount(wrapper);
            if (count > 0) {
                return MyJsonBean.errorData("规则编码已存在");
            }

            // 复制规则
            TblControlRule newRule = new TblControlRule();
            BeanUtils.copyProperties(sourceRule, newRule);

            String userId = UserUtils.getUserId();
            newRule.setRuleId(UUID.randomUUID().toString().replace("-", ""));
            newRule.setRuleCode(newRuleCode);
            newRule.setRuleName(newRuleName);
            newRule.setCreateUser(userId);
            newRule.setCreateTime(new Date());
            newRule.setUpdateUser(userId);
            newRule.setUpdateTime(new Date());

            controlRuleMapper.insert(newRule);

            return MyJsonBean.successData("复制成功", newRule.getRuleId());
        } catch (Exception e) {
            log.error("复制控制策略失败", e);
            return MyJsonBean.errorData("复制控制策略失败：" + e.getMessage());
        }
    }

    /**
     * 将实体转换为DTO
     */
    private ControlStrategyConfigDTO convertToDTO(TblControlRule rule) {
        ControlStrategyConfigDTO config = new ControlStrategyConfigDTO();
        config.setRuleId(rule.getRuleId());
        config.setRuleCode(rule.getRuleCode());
        config.setRuleName(rule.getRuleName());
        config.setControlType(rule.getControlType());
        config.setControlLevel(rule.getControlLevel());
        config.setControlPeriod(rule.getControlPeriod());
        config.setThresholdType(rule.getThresholdType());
        config.setThresholdValue(rule.getThresholdValue());
        config.setWarningRatio(rule.getWarningRatio());
        config.setControlAction(rule.getControlAction());
        config.setApprovalWorkflowId(rule.getApprovalWorkflowId());
        config.setIsEnabled(rule.getIsEnabled());
        config.setPriority(rule.getPriority());
        config.setOrgId(rule.getOrgId());

        // 解析维度配置
        if (StringUtils.isNotBlank(rule.getControlDimension())) {
            try {
                config.setDimensionConfigs(JSON.parseArray(rule.getControlDimension(),
                    ControlStrategyConfigDTO.DimensionConfig.class));
            } catch (Exception e) {
                log.warn("解析维度配置失败", e);
            }
        }

        // 解析例外用户
        if (StringUtils.isNotBlank(rule.getExceptionUsers())) {
            try {
                config.setExceptionUsers(JSON.parseArray(rule.getExceptionUsers(), String.class));
            } catch (Exception e) {
                log.warn("解析例外用户失败", e);
            }
        }

        return config;
    }
}