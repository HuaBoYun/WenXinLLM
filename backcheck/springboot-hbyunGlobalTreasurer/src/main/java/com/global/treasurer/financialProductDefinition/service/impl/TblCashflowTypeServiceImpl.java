package com.global.treasurer.financialProductDefinition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.SnowflakeIdWorker;
import com.global.treasurer.financialProductDefinition.entity.TblCashflowType;
import com.global.treasurer.financialProductDefinition.mapper.TblCashflowTypeMapper;
import com.global.treasurer.financialProductDefinition.service.TblCashflowTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 现金流类型管理Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class TblCashflowTypeServiceImpl extends ServiceImpl<TblCashflowTypeMapper, TblCashflowType>
        implements TblCashflowTypeService {
    private static final Logger log = LoggerFactory.getLogger(TblCashflowTypeServiceImpl.class);

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public IPage<TblCashflowType> getPage(Integer pageNo, Integer pageSize, String cashflowTypeCode,
                                           String cashflowTypeName, String cashflowDirection,
                                           String businessCategory, String impactType, Long orgId) {
        log.info("=== 查询现金流类型列表 === pageNo={}, pageSize={}, orgId={}", pageNo, pageSize, orgId);
        Page<TblCashflowType> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TblCashflowType> wrapper = new QueryWrapper<>();

        if (StringUtils.hasText(cashflowTypeCode)) {
            wrapper.like("CASHFLOW_TYPE_CODE", cashflowTypeCode);
        }
        if (StringUtils.hasText(cashflowTypeName)) {
            wrapper.like("CASHFLOW_TYPE_NAME", cashflowTypeName);
        }
        if (StringUtils.hasText(cashflowDirection)) {
            wrapper.eq("CASHFLOW_DIRECTION", cashflowDirection);
        }
        if (StringUtils.hasText(businessCategory)) {
            wrapper.eq("BUSINESS_CATEGORY", businessCategory);
        }
        if (StringUtils.hasText(impactType)) {
            wrapper.eq("IMPACT_TYPE", impactType);
        }
        // 临时注释掉 orgId 过滤，用于调试
        // if (orgId != null) {
        //     wrapper.eq("ORG_ID", orgId);
        // }
        wrapper.orderByAsc("SORT_ORDER").orderByDesc("CREATE_TIME");
        IPage<TblCashflowType> result = this.page(page, wrapper);
        log.info("=== 查询结果 === total={}, records={}", result.getTotal(), result.getRecords().size());
        return result;
    }

    @Override
    public TblCashflowType getDetail(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblCashflowType create(TblCashflowType entity, String createBy) {
        // 检查编码是否已存在
        if (StringUtils.hasText(entity.getCashflowTypeCode())) {
            QueryWrapper<TblCashflowType> wrapper = new QueryWrapper<>();
            wrapper.eq("CASHFLOW_TYPE_CODE", entity.getCashflowTypeCode());
            int count = this.count(wrapper);
            if (count > 0) {
                throw new RuntimeException("编码【" + entity.getCashflowTypeCode() + "】已存在，请使用其他编码");
            }
        }

        entity.setCashflowTypeId(snowflakeIdWorker.nextId());
        entity.setCreateBy(createBy);
        entity.setCreateTime(new Date());
        entity.setUpdateBy(createBy);
        entity.setUpdateTime(new Date());
        if (entity.getIsEnabled() == null) {
            entity.setIsEnabled(1);
        }
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0);
        }
        this.save(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(TblCashflowType entity, String updateBy) {
        // 检查编码是否与其他记录冲突
        if (StringUtils.hasText(entity.getCashflowTypeCode())) {
            QueryWrapper<TblCashflowType> wrapper = new QueryWrapper<>();
            wrapper.eq("CASHFLOW_TYPE_CODE", entity.getCashflowTypeCode());
            // 排除当前记录
            if (entity.getCashflowTypeId() != null) {
                wrapper.ne("CASHFLOW_TYPE_ID", entity.getCashflowTypeId());
            }
            int count = this.count(wrapper);
            if (count > 0) {
                throw new RuntimeException("编码【" + entity.getCashflowTypeCode() + "】已被其他记录使用，请使用其他编码");
            }
        }

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
        TblCashflowType entity = new TblCashflowType();
        entity.setCashflowTypeId(id);
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
    public List<TblCashflowType> getEnabledList(Long orgId) {
        QueryWrapper<TblCashflowType> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_ENABLED", 1);
        if (orgId != null) {
            wrapper.eq("ORG_ID", orgId);
        }
        wrapper.orderByAsc("SORT_ORDER");
        return this.list(wrapper);
    }

    @Override
    public boolean checkCodeUnique(String cashflowTypeCode, Long excludeId) {
        return baseMapper.checkCodeUnique(cashflowTypeCode, excludeId) == 0;
    }

    @Override
    public List<TblCashflowType> getTree(Long orgId) {
        List<TblCashflowType> allList = getEnabledList(orgId);
        return buildTree(allList, null);
    }

    private List<TblCashflowType> buildTree(List<TblCashflowType> list, Long parentId) {
        return list.stream()
                .filter(item -> Objects.equals(item.getParentId(), parentId))
                .peek(item -> item.setChildren(buildTree(list, item.getCashflowTypeId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<TblCashflowType> getByParentId(Long parentId, Long orgId) {
        return baseMapper.selectByParentId(parentId, orgId);
    }

    @Override
    public boolean sort(List<TblCashflowType> list) {
        return baseMapper.batchUpdateSort(list) > 0;
    }

    @Override
    public TblCashflowType copy(Long id, String newCode, String newName, String createBy) {
        TblCashflowType source = this.getById(id);
        if (source == null) {
            return null;
        }
        TblCashflowType target = new TblCashflowType();
        BeanUtils.copyProperties(source, target);
        target.setCashflowTypeId(snowflakeIdWorker.nextId());
        target.setCashflowTypeCode(newCode);
        target.setCashflowTypeName(newName);
        target.setCreateBy(createBy);
        target.setCreateTime(new Date());
        target.setUpdateBy(createBy);
        target.setUpdateTime(new Date());
        this.save(target);
        return target;
    }

    @Override
    public Map<String, Object> getStatistics(Long orgId) {
        Map<String, Object> result = new HashMap<>();

        // 总数
        QueryWrapper<TblCashflowType> totalWrapper = new QueryWrapper<>();
        if (orgId != null) {
            totalWrapper.eq("ORG_ID", orgId);
        }
        long total = this.count(totalWrapper);
        result.put("total", total);

        // 启用数
        QueryWrapper<TblCashflowType> enabledWrapper = new QueryWrapper<>();
        enabledWrapper.eq("IS_ENABLED", 1);
        if (orgId != null) {
            enabledWrapper.eq("ORG_ID", orgId);
        }
        long enabled = this.count(enabledWrapper);
        result.put("enabled", enabled);

        // 流入类型数
        QueryWrapper<TblCashflowType> inflowWrapper = new QueryWrapper<>();
        inflowWrapper.eq("CASHFLOW_DIRECTION", "INFLOW");
        if (orgId != null) {
            inflowWrapper.eq("ORG_ID", orgId);
        }
        long inflow = this.count(inflowWrapper);
        result.put("inflow", inflow);

        // 流出类型数
        QueryWrapper<TblCashflowType> outflowWrapper = new QueryWrapper<>();
        outflowWrapper.eq("CASHFLOW_DIRECTION", "OUTFLOW");
        if (orgId != null) {
            outflowWrapper.eq("ORG_ID", orgId);
        }
        long outflow = this.count(outflowWrapper);
        result.put("outflow", outflow);

        return result;
    }

    @Override
    public Map<String, Object> validateDelete(Long id) {
        Map<String, Object> result = new HashMap<>();
        int usage = baseMapper.countUsage(id);
        result.put("canDelete", usage == 0);
        result.put("usageCount", usage);
        if (usage > 0) {
            result.put("message", "该现金流类型已被使用，无法删除");
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
