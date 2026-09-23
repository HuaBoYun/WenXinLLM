package com.financial.sharing.budgetControl.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.budgetControl.dto.ControlRuleQueryParam;
import com.financial.sharing.budgetControl.entity.TblControlRule;
import com.financial.sharing.budgetControl.mapper.ControlRuleMapper;
import com.financial.sharing.budgetControl.service.ControlRuleService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 控制规则Service实现类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Service
public class ControlRuleServiceImpl extends ServiceImpl<ControlRuleMapper, TblControlRule> implements ControlRuleService {

    @Override
    public MyJsonBean queryPage(ControlRuleQueryParam param) {
        try {
            // 构建查询条件
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
            if (StringUtils.isNotBlank(param.getIsEnabled())) {
                wrapper.eq(TblControlRule::getIsEnabled, param.getIsEnabled());
            }
            
            wrapper.orderByDesc(TblControlRule::getCreateTime);

            // 分页查询
            Page<TblControlRule> page = new Page<>(param.getPageNumber(), param.getPageSize());
            IPage<TblControlRule> result = this.page(page, wrapper);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询控制规则失败", e);
            return MyJsonBean.errorData("查询控制规则失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean queryById(String ruleId) {
        try {
            TblControlRule rule = this.getById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("控制规则不存在");
            }
            return MyJsonBean.successData(rule);
        } catch (Exception e) {
            log.error("查询控制规则失败", e);
            return MyJsonBean.errorData("查询控制规则失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean add(TblControlRule rule) {
        try {
            // 检查规则编码是否重复
            LambdaQueryWrapper<TblControlRule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblControlRule::getRuleCode, rule.getRuleCode());
            wrapper.eq(TblControlRule::getOrgId, rule.getOrgId());
            long count = this.count(wrapper);
            if (count > 0) {
                return MyJsonBean.errorData("规则编码已存在");
            }

            // 设置创建信息
            String userId = UserUtils.getUserId();
            rule.setCreateUser(userId);
            rule.setCreateTime(new Date());
            rule.setUpdateUser(userId);
            rule.setUpdateTime(new Date());

            // 保存
            this.save(rule);
            return MyJsonBean.successData("新增成功");
        } catch (Exception e) {
            log.error("新增控制规则失败", e);
            return MyJsonBean.errorData("新增控制规则失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean modify(TblControlRule rule) {
        try {
            // 检查规则是否存在
            TblControlRule existRule = this.getById(rule.getRuleId());
            if (existRule == null) {
                return MyJsonBean.errorData("控制规则不存在");
            }

            // 设置修改信息
            String userId = UserUtils.getUserId();
            rule.setUpdateUser(userId);
            rule.setUpdateTime(new Date());

            // 更新
            this.updateById(rule);
            return MyJsonBean.successData("修改成功");
        } catch (Exception e) {
            log.error("修改控制规则失败", e);
            return MyJsonBean.errorData("修改控制规则失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean remove(String ruleId) {
        try {
            // 检查规则是否存在
            TblControlRule rule = this.getById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("控制规则不存在");
            }

            // 删除
            this.removeById(ruleId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除控制规则失败", e);
            return MyJsonBean.errorData("删除控制规则失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean toggleStatus(String ruleId, String isEnabled) {
        try {
            TblControlRule rule = this.getById(ruleId);
            if (rule == null) {
                return MyJsonBean.errorData("控制规则不存在");
            }

            rule.setIsEnabled(isEnabled);
            rule.setUpdateUser(UserUtils.getUserId());
            rule.setUpdateTime(new Date());
            this.updateById(rule);

            return MyJsonBean.successData("操作成功");
        } catch (Exception e) {
            log.error("修改规则状态失败", e);
            return MyJsonBean.errorData("修改规则状态失败：" + e.getMessage());
        }
    }
}

