package com.financial.sharing.service.impl;
import com.financial.sharing.oracle.entity.AccountingRuleEntity;


import com.financial.sharing.util.Java8Collections;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.service.AccountingRuleService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.AccountingRuleQueryParam;
import com.financial.sharing.vo.param.AccountingRuleSaveParam;
import com.financial.sharing.vo.result.AccountingRuleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会计规则服务实现类
 * 
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Service
public class AccountingRuleServiceImpl implements AccountingRuleService {

    @Autowired
    private DateBaseConfig dateBaseConfig;

    // 规则类型名称映射
    private static final Map<Integer, String> RULE_TYPE_NAME_MAP = new HashMap<>();
    
    // 事项类型名称映射
    private static final Map<String, String> TRANSACTION_TYPE_NAME_MAP = new HashMap<>();
    
    static {
        RULE_TYPE_NAME_MAP.put(1, "分录规则");
        RULE_TYPE_NAME_MAP.put(2, "分发规则");
        RULE_TYPE_NAME_MAP.put(3, "转换规则");
        
        TRANSACTION_TYPE_NAME_MAP.put("SALES_ORDER", "销售订单");
        TRANSACTION_TYPE_NAME_MAP.put("PURCHASE_ORDER", "采购订单");
        TRANSACTION_TYPE_NAME_MAP.put("PAYMENT", "付款");
        TRANSACTION_TYPE_NAME_MAP.put("RECEIPT", "收款");
        TRANSACTION_TYPE_NAME_MAP.put("INVENTORY_IN", "入库");
        TRANSACTION_TYPE_NAME_MAP.put("INVENTORY_OUT", "出库");
        TRANSACTION_TYPE_NAME_MAP.put("EXPENSE", "费用");
        TRANSACTION_TYPE_NAME_MAP.put("INCOME", "收入");
    }

    @Override
    public PageResult<AccountingRuleVO> getAccountingRulePage(AccountingRuleQueryParam param) {
        Page<AccountingRuleVO> page = new Page<>(param.getPageNum(), param.getPageSize());
        
        IPage<AccountingRuleVO> result;
        
            result = dateBaseConfig.getOracleAccountingRuleMapper().selectAccountingRulePage(page, param);
        
        
        // 设置显示名称
        if (!Java8Collections.isEmpty(result.getRecords())) {
            result.getRecords().forEach(this::setDisplayNames);
        }
        return new PageResult<>((int)result.getTotal(), (int)result.getCurrent(), (int)result.getPages(), (int)result.getSize(), result.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountingRuleVO saveOrUpdateAccountingRule(AccountingRuleSaveParam param) {
        // 检查规则编码是否重复
        if (checkRuleCodeExists(param.getRuleCode(), param.getBookId(), param.getTenantId(), param.getRuleId())) {
            throw new RuntimeException("规则编码已存在");
        }
        
        AccountingRuleEntity entity = new AccountingRuleEntity();
        BeanUtils.copyProperties(param, entity);
        
        // 设置默认值
        if (entity.getPriority() == null) {
            entity.setPriority(1);
        }
        if (entity.getIsEnabled() == null) {
            entity.setIsEnabled(1);
        }

        boolean success;

        com.financial.sharing.oracle.entity.AccountingRuleEntity oracleEntity =
            new com.financial.sharing.oracle.entity.AccountingRuleEntity();
        BeanUtils.copyProperties(entity, oracleEntity);

        if (param.getRuleId() == null) {
            success = dateBaseConfig.getOracleAccountingRuleMapper().insert(oracleEntity) > 0;
        } else {
            success = dateBaseConfig.getOracleAccountingRuleMapper().updateById(oracleEntity) > 0;
        }
        entity.setRuleId(oracleEntity.getRuleId());
        if (!success) {
            throw new RuntimeException("保存会计规则失败");
        }
        return getAccountingRuleById(entity.getRuleId());
    }

    @Override
    public AccountingRuleVO getAccountingRuleById(Long ruleId) {
        AccountingRuleEntity entity;

        com.financial.sharing.oracle.entity.AccountingRuleEntity oracleEntity =
            dateBaseConfig.getOracleAccountingRuleMapper().selectById(ruleId);
        if (oracleEntity == null) {
            return null;
        }

        entity = new AccountingRuleEntity();
        BeanUtils.copyProperties(oracleEntity, entity);

        AccountingRuleVO vo = new AccountingRuleVO();
        BeanUtils.copyProperties(entity, vo);
        setDisplayNames(vo);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAccountingRule(Long ruleId) {
        boolean success;
        
            success = dateBaseConfig.getOracleAccountingRuleMapper().deleteById(ruleId) > 0;
        
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteAccountingRules(List<Long> ruleIds) {
        if (Java8Collections.isEmpty(ruleIds)) {
            return false;
        }

        int result;

        result = dateBaseConfig.getOracleAccountingRuleMapper().batchDelete(ruleIds, null);

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRuleEnabled(Long ruleId, Integer isEnabled) {
        int result;
        
            result = dateBaseConfig.getOracleAccountingRuleMapper().batchUpdateEnabled(
                Java8Collections.listOf(ruleId), isEnabled, null);
        
        
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateRuleEnabled(List<Long> ruleIds, Integer isEnabled) {
        if (Java8Collections.isEmpty(ruleIds)) {
            return false;
        }
        
        int result;
        
            result = dateBaseConfig.getOracleAccountingRuleMapper().batchUpdateEnabled(ruleIds, isEnabled, null);
        
        
        return result > 0;
    }

    @Override
    public boolean checkRuleCodeExists(String ruleCode, Long bookId, Long tenantId, Long excludeId) {
        AccountingRuleEntity entity;
        
            com.financial.sharing.oracle.entity.AccountingRuleEntity oracleEntity = 
                dateBaseConfig.getOracleAccountingRuleMapper().selectByRuleCode(
                    ruleCode, bookId, tenantId, excludeId);
            entity = oracleEntity != null ? new AccountingRuleEntity() : null;
        
        
        return entity != null;
    }

    @Override
    public List<AccountingRuleVO> getAccountingRulesByType(Integer ruleType, Long bookId, Long tenantId) {
        List<AccountingRuleVO> list;
        
            list = dateBaseConfig.getOracleAccountingRuleMapper().selectByRuleType(ruleType, bookId, tenantId);
        
        
        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    public List<AccountingRuleVO> getAccountingRulesByTransactionType(String transactionType, Long bookId, Long tenantId) {
        List<AccountingRuleVO> list;
        
            list = dateBaseConfig.getOracleAccountingRuleMapper().selectByTransactionType(transactionType, bookId, tenantId);
        
        
        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    public List<AccountingRuleVO> getAccountingRulesByEnabled(Integer isEnabled, Long bookId, Long tenantId) {
        List<AccountingRuleVO> list;
        
            list = dateBaseConfig.getOracleAccountingRuleMapper().selectByEnabled(isEnabled, bookId, tenantId);
        
        
        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    public List<Integer> getRuleTypes(Long bookId, Long tenantId) {
        
            return dateBaseConfig.getOracleAccountingRuleMapper().selectRuleTypes(bookId, tenantId);
        
    }

    @Override
    public List<String> getTransactionTypes(Long bookId, Long tenantId) {
        
            return dateBaseConfig.getOracleAccountingRuleMapper().selectTransactionTypes(bookId, tenantId);
        
    }

    @Override
    public List<AccountingRuleVO> countRulesByType(Long bookId, Long tenantId) {
        List<AccountingRuleVO> list;
        
            list = dateBaseConfig.getOracleAccountingRuleMapper().countByRuleType(bookId, tenantId);
        
        
        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    public List<AccountingRuleVO> countRulesByTransactionType(Long bookId, Long tenantId) {
        List<AccountingRuleVO> list;
        
            list = dateBaseConfig.getOracleAccountingRuleMapper().countByTransactionType(bookId, tenantId);
        
        
        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    public Object testAccountingRule(Long ruleId, Object testData) {
        // TODO: 实现规则测试逻辑
        // 这里应该根据规则条件和动作，对测试数据进行处理
        // 返回测试结果，包括生成的分录等信息
        
        AccountingRuleVO rule = getAccountingRuleById(ruleId);
        if (rule == null) {
            throw new RuntimeException("规则不存在");
        }
        if (rule.getIsEnabled() != 1) {
            throw new RuntimeException("规则未启用");
        }

        // 模拟测试结果
        Map<String, Object> result = new HashMap<>();
        result.put("ruleId", ruleId);
        result.put("success", true);
        result.put("executionTime", 25);
        result.put("message", "规则测试成功");
        result.put("testData", testData);

        return result;
    }

    /**
     * 设置显示名称
     */
    private void setDisplayNames(AccountingRuleVO vo) {
        if (vo != null) {
            if (vo.getRuleType() != null) {
                vo.setRuleTypeName(RULE_TYPE_NAME_MAP.getOrDefault(vo.getRuleType(), "未知"));
            }
            if (vo.getTransactionType() != null) {
                vo.setTransactionTypeName(TRANSACTION_TYPE_NAME_MAP.getOrDefault(vo.getTransactionType(), vo.getTransactionType()));
            }
            if (vo.getIsEnabled() != null) {
                vo.setIsEnabledName(vo.getIsEnabled() == 1 ? "是" : "否");
            }
        }
    }
}
