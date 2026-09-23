package com.financial.sharing.service.impl;
import com.financial.sharing.oracle.entity.RuleVoucherEntity;


import com.financial.sharing.util.Java8Collections;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.service.RuleVoucherService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.RuleVoucherQueryParam;
import com.financial.sharing.vo.param.RuleVoucherSaveParam;
import com.financial.sharing.vo.result.RuleVoucherVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规则凭证服务实现类
 * 
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Service
public class RuleVoucherServiceImpl implements RuleVoucherService {

    @Autowired
    private DateBaseConfig dateBaseConfig;

    // 规则类型名称映射
    private static final Map<Integer, String> RULE_TYPE_NAME_MAP = new HashMap<>();
    
    static {
        RULE_TYPE_NAME_MAP.put(1, "期末损益结转");
        RULE_TYPE_NAME_MAP.put(2, "普通规则凭证");
    }

    @Override
    public PageResult<RuleVoucherVO> getRuleVoucherPage(RuleVoucherQueryParam param) {
        Page<RuleVoucherVO> page = new Page<>(param.getPageNum(), param.getPageSize());
        
        IPage<RuleVoucherVO> result;
        
            result = dateBaseConfig.getOracleRuleVoucherMapper().selectRuleVoucherPage(page, param);
        

        // 设置显示名称
        if (!Java8Collections.isEmpty(result.getRecords())) {
            result.getRecords().forEach(this::setDisplayNames);
        }
        return new PageResult<>((int)result.getTotal(), (int)result.getCurrent(), (int)result.getPages(), (int)result.getSize(), result.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RuleVoucherVO saveOrUpdateRuleVoucher(RuleVoucherSaveParam param) {
        // 检查规则编码是否重复
        if (checkRuleCodeExists(param.getRuleCode(), param.getBookId(), param.getTenantId(), param.getRuleVoucherId())) {
            throw new RuntimeException("规则编码已存在");
        }
        
        RuleVoucherEntity entity = new RuleVoucherEntity();
        BeanUtils.copyProperties(param, entity);
        
        // 设置默认值
        if (entity.getIsEnabled() == null) {
            entity.setIsEnabled(1);
        }
        
        boolean success;

        // 使用Oracle数据库
        com.financial.sharing.oracle.entity.RuleVoucherEntity oracleEntity =
            new com.financial.sharing.oracle.entity.RuleVoucherEntity();
        BeanUtils.copyProperties(entity, oracleEntity);

        if (param.getRuleVoucherId() == null) {
            success = dateBaseConfig.getOracleRuleVoucherMapper().insert(oracleEntity) > 0;
        } else {
            success = dateBaseConfig.getOracleRuleVoucherMapper().updateById(oracleEntity) > 0;
        }
        entity.setRuleVoucherId(oracleEntity.getRuleVoucherId());

        if (!success) {
            throw new RuntimeException("保存规则凭证失败");
        }
        return getRuleVoucherById(entity.getRuleVoucherId());
    }

    @Override
    public RuleVoucherVO getRuleVoucherById(Long ruleVoucherId) {
        RuleVoucherEntity entity;

        com.financial.sharing.oracle.entity.RuleVoucherEntity oracleEntity =
            dateBaseConfig.getOracleRuleVoucherMapper().selectById(ruleVoucherId);
        if (oracleEntity == null) {
            return null;
        }

        entity = new RuleVoucherEntity();
        BeanUtils.copyProperties(oracleEntity, entity);

        if (entity == null) {
            return null;
        }

        RuleVoucherVO vo = new RuleVoucherVO();
        BeanUtils.copyProperties(entity, vo);
        setDisplayNames(vo);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRuleVoucher(Long ruleVoucherId) {
        boolean success;
        
            success = dateBaseConfig.getOracleRuleVoucherMapper().deleteById(ruleVoucherId) > 0;
        
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteRuleVouchers(List<Long> ruleVoucherIds) {
        if (Java8Collections.isEmpty(ruleVoucherIds)) {
            return false;
        }
        
        int result;
        
            result = dateBaseConfig.getOracleRuleVoucherMapper().batchDelete(ruleVoucherIds, null);
        
        
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRuleVoucherEnabled(Long ruleVoucherId, Integer isEnabled) {
        int result;
        
            result = dateBaseConfig.getOracleRuleVoucherMapper().batchUpdateEnabled(
                Java8Collections.listOf(ruleVoucherId), isEnabled, null);
        
        
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateRuleVoucherEnabled(List<Long> ruleVoucherIds, Integer isEnabled) {
        if (Java8Collections.isEmpty(ruleVoucherIds)) {
            return false;
        }
        
        int result;
        
            result = dateBaseConfig.getOracleRuleVoucherMapper().batchUpdateEnabled(ruleVoucherIds, isEnabled, null);
        
        
        return result > 0;
    }

    @Override
    public boolean checkRuleCodeExists(String ruleCode, Long bookId, Long tenantId, Long excludeId) {
        RuleVoucherEntity entity;

        com.financial.sharing.oracle.entity.RuleVoucherEntity oracleEntity =
            dateBaseConfig.getOracleRuleVoucherMapper().selectByRuleCode(
                ruleCode, bookId, tenantId, excludeId);
        entity = oracleEntity != null ? new RuleVoucherEntity() : null;

        return entity != null;
    }

    @Override
    public List<RuleVoucherVO> getRuleVouchersByType(Integer ruleType, Long bookId, Long tenantId) {
        List<RuleVoucherVO> list;

        list = dateBaseConfig.getOracleRuleVoucherMapper().selectByRuleType(ruleType, bookId, tenantId);

        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    public List<RuleVoucherVO> getRuleVouchersByExecutionPeriod(String executionPeriod, Long bookId, Long tenantId) {
        List<RuleVoucherVO> list;

        list = dateBaseConfig.getOracleRuleVoucherMapper().selectByExecutionPeriod(executionPeriod, bookId, tenantId);

        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    public List<RuleVoucherVO> getRuleVouchersByEnabled(Integer isEnabled, Long bookId, Long tenantId) {
        List<RuleVoucherVO> list;

        list = dateBaseConfig.getOracleRuleVoucherMapper().selectByEnabled(isEnabled, bookId, tenantId);

        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    public List<Integer> getRuleTypes(Long bookId, Long tenantId) {
        return dateBaseConfig.getOracleRuleVoucherMapper().selectRuleTypes(bookId, tenantId);
    }

    @Override
    public List<String> getExecutionPeriods(Long bookId, Long tenantId) {
        return dateBaseConfig.getOracleRuleVoucherMapper().selectExecutionPeriods(bookId, tenantId);
    }

    @Override
    public List<RuleVoucherVO> countRuleVouchersByType(Long bookId, Long tenantId) {
        List<RuleVoucherVO> list = dateBaseConfig.getOracleRuleVoucherMapper().countByRuleType(bookId, tenantId);

        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    public List<RuleVoucherVO> countRuleVouchersByExecutionPeriod(Long bookId, Long tenantId) {
        List<RuleVoucherVO> list = dateBaseConfig.getOracleRuleVoucherMapper().countByExecutionPeriod(bookId, tenantId);

        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setDisplayNames);
        }
        return list;
    }

    @Override
    public Object executeRuleVoucher(Long ruleVoucherId, String executionPeriod) {
        // TODO: 实现规则凭证执行逻辑
        // 这里应该根据凭证模板，生成实际的会计凭证

        RuleVoucherVO ruleVoucher = getRuleVoucherById(ruleVoucherId);
        if (ruleVoucher == null) {
            throw new RuntimeException("规则凭证不存在");
        }
        if (ruleVoucher.getIsEnabled() != 1) {
            throw new RuntimeException("规则凭证未启用");
        }
        
        // 模拟执行结果
        Map<String, Object> result = new HashMap<>();
        result.put("ruleVoucherId", ruleVoucherId);
        result.put("executionPeriod", executionPeriod);
        result.put("success", true);
        result.put("voucherCount", 1);
        result.put("executionTime", 150);
        result.put("message", "规则凭证执行成功");
        
        return result;
    }

    /**
     * 设置显示名称
     */
    private void setDisplayNames(RuleVoucherVO vo) {
        if (vo != null) {
            if (vo.getRuleType() != null) {
                vo.setRuleTypeName(RULE_TYPE_NAME_MAP.getOrDefault(vo.getRuleType(), "未知"));
            }
            if (vo.getIsEnabled() != null) {
                vo.setIsEnabledName(vo.getIsEnabled() == 1 ? "是" : "否");
            }
        }
    }
}
