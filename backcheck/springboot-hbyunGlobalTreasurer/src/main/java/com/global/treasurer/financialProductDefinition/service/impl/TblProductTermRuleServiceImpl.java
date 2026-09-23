package com.global.treasurer.financialProductDefinition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.SnowflakeIdWorker;
import com.global.treasurer.financialProductDefinition.entity.TblProductTermRule;
import com.global.treasurer.financialProductDefinition.mapper.TblProductTermRuleMapper;
import com.global.treasurer.financialProductDefinition.service.TblProductTermRuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.*;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TblProductTermRuleServiceImpl extends ServiceImpl<TblProductTermRuleMapper, TblProductTermRule>
        implements TblProductTermRuleService {
    private static final Logger log = LoggerFactory.getLogger(TblProductTermRuleServiceImpl.class);
    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;
    @Override
    public IPage<TblProductTermRule> getPage(Integer pageNo, Integer pageSize, String ruleCode,
                                              String ruleName, String productType, String termType, String termUnit, Integer isEnabled, Long orgId) {
        log.info("=== Service层 === 分页查询参数 - pageNo:{}, pageSize:{}, ruleCode:{}, ruleName:{}, productType:{}, termType:{}, termUnit:{}, isEnabled:{}, orgId:{}",
                pageNo, pageSize, ruleCode, ruleName, productType, termType, termUnit, isEnabled, orgId);

        Page<TblProductTermRule> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TblProductTermRule> wrapper = new QueryWrapper<>();
        // 使用数据库列名（大写下划线格式），确保 SQL 正确生成
        if (StringUtils.hasText(ruleCode)) wrapper.like("RULE_CODE", ruleCode);
        if (StringUtils.hasText(ruleName)) wrapper.like("RULE_NAME", ruleName);
        // productType字段在数据库表中存在，可以直接过滤
        if (StringUtils.hasText(productType)) wrapper.eq("PRODUCT_TYPE", productType);
        // 添加 termType 和 termUnit 过滤条件
        if (StringUtils.hasText(termType)) wrapper.eq("TERM_TYPE", termType);
        if (StringUtils.hasText(termUnit)) wrapper.eq("TERM_UNIT", termUnit);
        if (isEnabled != null) wrapper.eq("IS_ENABLED", isEnabled);
        if (orgId != null) wrapper.eq("ORG_ID", orgId);
        wrapper.orderByDesc("CREATE_TIME");

        // 查询数据
        IPage<TblProductTermRule> resultPage = this.page(page, wrapper);

        log.info("=== Service层 === 查询结果 - 总数:{}, 当前页数据量:{}, 当前页码:{}, 每页大小:{}",
                resultPage.getTotal(), resultPage.getRecords().size(), resultPage.getCurrent(), resultPage.getSize());

        // 为记录设置默认值
        for (TblProductTermRule entity : resultPage.getRecords()) {
            if (entity.getProductType() == null || entity.getProductType().isEmpty()) {
                entity.setProductType("BANK_WEALTH"); // 默认值
            }
            if (entity.getLockPeriod() == null) {
                entity.setLockPeriod(0); // 默认锁定期
            }
        }

        return resultPage;
    }
    public TblProductTermRule getDetail(Long id) { return this.getById(id); }
    @Transactional(rollbackFor = Exception.class)
    public TblProductTermRule create(TblProductTermRule entity, String createBy) {
        // 使用雪花ID生成主键
        Long newId = snowflakeIdWorker.nextId();
        entity.setRuleId(newId);
        entity.setId(newId);

        // 转换createBy字符串为Long
        if (createBy != null && !createBy.isEmpty()) {
            entity.setCreateBy(createBy);
            entity.setUpdateBy(createBy);
        } else {
            entity.setCreateBy("");
            entity.setUpdateBy("");
        }

        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        if (entity.getIsEnabled() == null) entity.setIsEnabled(1);
        if (entity.getExtensionAllowed() == null) entity.setExtensionAllowed(0);

        this.save(entity);

        // 设置返回对象的前端需要的字段
        if (entity.getProductType() == null || entity.getProductType().isEmpty()) {
            entity.setProductType("BANK_WEALTH"); // 默认产品类型
        }
        if (entity.getLockPeriod() == null) {
            entity.setLockPeriod(0); // 默认锁定期
        }

        return entity;
    }

    @Override
    public boolean update(TblProductTermRule entity, String updateBy) {
        // 设置updateBy字符串
        if (updateBy != null && !updateBy.isEmpty()) {
            entity.setUpdateBy(updateBy);
        } else {
            entity.setUpdateBy("");
        }

        entity.setUpdateTime(new Date());
        return this.updateById(entity);
    }

    @Override
    public boolean delete(Long id) {
        try {
            log.info("删除产品期限规则，id: {}", id);
            // 先查询记录是否存在
            TblProductTermRule entity = this.getById(id);
            if (entity == null) {
                log.warn("产品期限规则不存在，id: {}", id);
                return false;
            }
            log.info("找到记录: ruleId={}, ruleCode={}", entity.getRuleId(), entity.getRuleCode());
            // 使用自定义删除方法
            int result = baseMapper.deleteById(id);
            log.info("删除结果: {}", result);
            return result > 0;
        } catch (Exception e) {
            log.error("删除产品期限规则失败，id: {}", id, e);
            throw new RuntimeException("删除失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean batchDelete(List<Long> ruleIds) {
        try {
            log.info("批量删除产品期限规则，ruleIds: {}", ruleIds);
            // 使用自定义批量删除方法
            int result = baseMapper.deleteByIds(ruleIds);
            log.info("批量删除结果: {}", result);
            return result > 0;
        } catch (Exception e) {
            log.error("批量删除产品期限规则失败，ruleIds: {}", ruleIds, e);
            throw new RuntimeException("批量删除失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateStatus(Long id, Integer isEnabled, String updateBy) {
        TblProductTermRule entity = new TblProductTermRule();
        entity.setRuleId(id);
        entity.setIsEnabled(isEnabled);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(new Date());
        return this.updateById(entity);
    }

    @Override
    public boolean batchUpdateStatus(List<Long> ids, Integer isEnabled, String updateBy) {
        return baseMapper.batchUpdateStatus(ids, isEnabled, updateBy) > 0;
    }

    @Override
    public List<TblProductTermRule> getEnabledList(Long orgId) {
        QueryWrapper<TblProductTermRule> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_ENABLED", 1);
        if (orgId != null) {
            wrapper.eq("ORG_ID", orgId);
        }
        wrapper.orderByDesc("CREATE_TIME");
        return this.list(wrapper);
    }

    @Override
    public boolean checkCodeUnique(String ruleCode, Long excludeId) {
        return baseMapper.checkCodeUnique(ruleCode, excludeId) == 0;
    }

    @Override
    public List<TblProductTermRule> getByProductType(String productType, Long orgId) {
        return baseMapper.selectByProductType(productType, orgId);
    }

    @Override
    public TblProductTermRule copy(Long id, String newCode, String newName, String createBy) {
        TblProductTermRule source = this.getById(id);
        if (source == null) return null;
        TblProductTermRule target = new TblProductTermRule();
        BeanUtils.copyProperties(source, target);
        target.setRuleId(snowflakeIdWorker.nextId());
        target.setRuleCode(newCode);
        target.setRuleName(newName);
        target.setCreateBy(createBy);
        target.setCreateTime(new Date());
        target.setUpdateBy(createBy);
        target.setUpdateTime(new Date());
        this.save(target);
        return target;
    }

    @Override
    public Map<String, Object> validateDelete(Long id) {
        Map<String, Object> result = new HashMap<>();
        int usage = baseMapper.countUsage(id);
        result.put("canDelete", usage == 0);
        result.put("usageCount", usage);
        if (usage > 0) result.put("message", "该期限规则已被使用，无法删除");
        return result;
    }

    @Override
    public Map<String, Object> getUsage(Long id) {
        Map<String, Object> result = new HashMap<>();
        int usage = baseMapper.countUsage(id);
        result.put("usageCount", usage);
        result.put("isUsed", usage > 0);
        return result;
    }
}
