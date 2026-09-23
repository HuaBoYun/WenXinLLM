package com.global.treasurer.financialProductDefinition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.SnowflakeIdWorker;
import com.global.treasurer.financialProductDefinition.entity.TblProductRiskControl;
import com.global.treasurer.financialProductDefinition.mapper.TblProductRiskControlMapper;
import com.global.treasurer.financialProductDefinition.service.TblProductRiskControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 产品风控规则管理Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class TblProductRiskControlServiceImpl extends ServiceImpl<TblProductRiskControlMapper, TblProductRiskControl>
        implements TblProductRiskControlService {
    private static final Logger log = LoggerFactory.getLogger(TblProductRiskControlServiceImpl.class);

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public IPage<TblProductRiskControl> getPage(Integer pageNo, Integer pageSize, String riskControlCode,
                                                 String riskControlName, String productType, Integer isEnabled, Long orgId) {
        log.info("=== 查询参数 === pageNo={}, pageSize={}, riskControlCode={}, riskControlName={}, isEnabled={}, orgId={}",
                pageNo, pageSize, riskControlCode, riskControlName, isEnabled, orgId);

        Page<TblProductRiskControl> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TblProductRiskControl> wrapper = new QueryWrapper<>();

        if (StringUtils.hasText(riskControlCode)) {
            wrapper.like("RISK_CONTROL_CODE", riskControlCode);
            log.info("添加查询条件: RISK_CONTROL_CODE LIKE {}", riskControlCode);
        }
        if (StringUtils.hasText(riskControlName)) {
            wrapper.like("RISK_CONTROL_NAME", riskControlName);
            log.info("添加查询条件: RISK_CONTROL_NAME LIKE {}", riskControlName);
        }
        // productType参数在数据库表中无对应字段,忽略此查询条件
        if (isEnabled != null) {
            wrapper.eq("IS_ENABLED", isEnabled);
            log.info("添加查询条件: IS_ENABLED = {}", isEnabled);
        }
        // orgId参数可选,如果为null或<=0则不添加过滤条件
        if (orgId != null && orgId > 0) {
            wrapper.eq("ORG_ID", orgId);
            log.info("添加查询条件: ORG_ID = {}", orgId);
        } else {
            log.info("不添加ORG_ID过滤条件,查询所有组织数据");
        }
        wrapper.orderByDesc("CREATE_TIME");

        IPage<TblProductRiskControl> result = this.page(page, wrapper);
        log.info("=== 查询结果 === 总记录数: {}, 当前页记录数: {}", result.getTotal(), result.getRecords().size());

        return result;
    }

    /**
     * 分页查询产品风控规则列表（支持风险等级和风险类型筛选）
     */
    @Override
    public IPage<TblProductRiskControl> getPageWithFilters(Integer pageNo, Integer pageSize,
                                                          String riskControlCode, String riskControlName,
                                                          String riskLevel, String riskType,
                                                          String monitoringFrequency,
                                                          Integer isEnabled, Long orgId) {
        log.info("=== 查询参数（含筛选） === pageNo={}, pageSize={}, riskControlCode={}, riskControlName={}, riskLevel={}, riskType={}, monitoringFrequency={}, isEnabled={}, orgId={}",
                pageNo, pageSize, riskControlCode, riskControlName, riskLevel, riskType, monitoringFrequency, isEnabled, orgId);

        Page<TblProductRiskControl> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TblProductRiskControl> wrapper = new QueryWrapper<>();

        // 风控策略编码
        if (StringUtils.hasText(riskControlCode)) {
            wrapper.like("RISK_CONTROL_CODE", riskControlCode);
            log.info("添加查询条件: RISK_CONTROL_CODE LIKE {}", riskControlCode);
        }
        // 风控策略名称
        if (StringUtils.hasText(riskControlName)) {
            wrapper.like("RISK_CONTROL_NAME", riskControlName);
            log.info("添加查询条件: RISK_CONTROL_NAME LIKE {}", riskControlName);
        }
        // 风险等级
        if (StringUtils.hasText(riskLevel)) {
            wrapper.eq("RISK_LEVEL", riskLevel);
            log.info("添加查询条件: RISK_LEVEL = {}", riskLevel);
        }
        // 风险类型
        if (StringUtils.hasText(riskType)) {
            wrapper.eq("RISK_TYPE", riskType);
            log.info("添加查询条件: RISK_TYPE = {}", riskType);
        }
        // 监控频率
        if (StringUtils.hasText(monitoringFrequency)) {
            wrapper.eq("MONITORING_FREQUENCY", monitoringFrequency);
            log.info("添加查询条件: MONITORING_FREQUENCY = {}", monitoringFrequency);
        }
        // 是否启用
        if (isEnabled != null) {
            wrapper.eq("IS_ENABLED", isEnabled);
            log.info("添加查询条件: IS_ENABLED = {}", isEnabled);
        }
        // 组织ID
        if (orgId != null && orgId > 0) {
            wrapper.eq("ORG_ID", orgId);
            log.info("添加查询条件: ORG_ID = {}", orgId);
        } else {
            log.info("不添加ORG_ID过滤条件,查询所有组织数据");
        }

        wrapper.orderByDesc("CREATE_TIME");

        IPage<TblProductRiskControl> result = this.page(page, wrapper);
        log.info("=== 查询结果 === 总记录数: {}, 当前页记录数: {}", result.getTotal(), result.getRecords().size());

        return result;
    }

    @Override
    public TblProductRiskControl getDetail(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblProductRiskControl create(TblProductRiskControl entity, String createBy) {
        entity.setRiskControlId(snowflakeIdWorker.nextId());
        entity.setCreateBy(createBy);
        entity.setCreateTime(new Date());
        entity.setUpdateBy(createBy);
        entity.setUpdateTime(new Date());
        if (entity.getIsEnabled() == null) {
            entity.setIsEnabled(1);
        }
        this.save(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(TblProductRiskControl entity, String updateBy) {
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(new Date());
        return this.updateById(entity);
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public boolean updateStatus(Long id, Integer isEnabled, String updateBy) {
        TblProductRiskControl entity = new TblProductRiskControl();
        entity.setRiskControlId(id);
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
    public List<TblProductRiskControl> getEnabledList(Long orgId) {
        QueryWrapper<TblProductRiskControl> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_ENABLED", 1);
        // orgId参数可选,如果为null或<=0则不添加过滤条件
        if (orgId != null && orgId > 0) {
            wrapper.eq("ORG_ID", orgId);
        }
        return this.list(wrapper);
    }

    @Override
    public boolean checkCodeUnique(String riskControlCode, Long excludeId) {
        return baseMapper.checkCodeUnique(riskControlCode, excludeId) == 0;
    }

    @Override
    public List<TblProductRiskControl> getByProductType(String productType, Long orgId) {
        return baseMapper.selectByProductType(productType, orgId);
    }

    @Override
    public TblProductRiskControl copy(Long id, String newCode, String newName, String createBy) {
        TblProductRiskControl source = this.getById(id);
        if (source == null) {
            return null;
        }
        TblProductRiskControl target = new TblProductRiskControl();
        BeanUtils.copyProperties(source, target);
        target.setRiskControlId(snowflakeIdWorker.nextId());
        target.setRiskControlCode(newCode);
        target.setRiskControlName(newName);
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
        if (usage > 0) {
            result.put("message", "该风控规则已被使用,无法删除");
        }
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
