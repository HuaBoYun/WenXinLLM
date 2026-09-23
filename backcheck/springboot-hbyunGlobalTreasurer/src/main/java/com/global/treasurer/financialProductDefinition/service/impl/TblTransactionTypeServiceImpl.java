package com.global.treasurer.financialProductDefinition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.SnowflakeIdWorker;
import com.global.treasurer.financialProductDefinition.entity.TblTransactionType;
import com.global.treasurer.financialProductDefinition.mapper.TblTransactionTypeMapper;
import com.global.treasurer.financialProductDefinition.service.TblTransactionTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

@Service
public class TblTransactionTypeServiceImpl extends ServiceImpl<TblTransactionTypeMapper, TblTransactionType>
        implements TblTransactionTypeService {
    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public IPage<TblTransactionType> getPage(Integer pageNo, Integer pageSize, String transactionTypeCode,
                                              String transactionTypeName, String transactionCategory,
                                              String direction, String riskLevel, Integer isEnabled, Long orgId) {
        Page<TblTransactionType> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TblTransactionType> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(transactionTypeCode)) wrapper.like("TRANSACTION_TYPE_CODE", transactionTypeCode);
        if (StringUtils.hasText(transactionTypeName)) wrapper.like("TRANSACTION_TYPE_NAME", transactionTypeName);
        if (StringUtils.hasText(transactionCategory)) wrapper.eq("TRANSACTION_CATEGORY", transactionCategory);
        if (StringUtils.hasText(direction)) wrapper.eq("TRANSACTION_DIRECTION", direction);
        if (StringUtils.hasText(riskLevel)) wrapper.eq("RISK_LEVEL", riskLevel);
        if (isEnabled != null) wrapper.eq("IS_ENABLED", isEnabled);
        if (orgId != null) wrapper.eq("ORG_ID", orgId);
        wrapper.orderByAsc("SORT_ORDER").orderByDesc("CREATE_TIME");
        return this.page(page, wrapper);
    }

    @Override
    public TblTransactionType getDetail(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblTransactionType create(TblTransactionType entity, String createBy) {
        entity.setTransactionTypeId(snowflakeIdWorker.nextId());
        entity.setCreateBy(createBy);
        entity.setCreateTime(new Date());
        entity.setUpdateBy(createBy);
        entity.setUpdateTime(new Date());
        if (entity.getIsEnabled() == null) entity.setIsEnabled(1);
        if (entity.getSortOrder() == null) entity.setSortOrder(0);
        this.save(entity);
        return entity;
    }

    @Override
    public boolean update(TblTransactionType entity, String updateBy) {
        entity.setUpdateBy(updateBy);
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
        TblTransactionType entity = new TblTransactionType();
        entity.setTransactionTypeId(id);
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
    public List<TblTransactionType> getEnabledList(Long orgId) {
        QueryWrapper<TblTransactionType> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_ENABLED", 1);
        if (orgId != null) {
            wrapper.eq("ORG_ID", orgId);
        }
        wrapper.orderByAsc("SORT_ORDER");
        return this.list(wrapper);
    }

    @Override
    public boolean checkCodeUnique(String transactionTypeCode, Long excludeId) {
        return baseMapper.checkCodeUnique(transactionTypeCode, excludeId) == 0;
    }

    @Override
    public List<TblTransactionType> getTree(Long orgId) {
        // TBL_TRANSACTION_TYPE 表为扁平结构，无父子层级关系，直接返回列表
        return getEnabledList(orgId);
    }

    @Override
    public boolean sort(List<TblTransactionType> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSortOrder(i);
        }
        return baseMapper.batchUpdateSort(list) > 0;
    }

    @Override
    public TblTransactionType copy(Long id, String newCode, String newName, String createBy) {
        TblTransactionType source = this.getById(id);
        if (source == null) return null;
        TblTransactionType target = new TblTransactionType();
        BeanUtils.copyProperties(source, target);
        target.setTransactionTypeId(snowflakeIdWorker.nextId());
        target.setTransactionTypeCode(newCode);
        target.setTransactionTypeName(newName);
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
        if (usage > 0) result.put("message", "该交易类型已被使用，无法删除");
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
