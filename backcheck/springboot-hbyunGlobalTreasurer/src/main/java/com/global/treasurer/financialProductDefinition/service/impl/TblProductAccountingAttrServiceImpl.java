package com.global.treasurer.financialProductDefinition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.financialProductDefinition.entity.TblProductAccountingAttr;
import com.global.treasurer.financialProductDefinition.mapper.TblProductAccountingAttrMapper;
import com.global.treasurer.financialProductDefinition.service.TblProductAccountingAttrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 产品核算属性管理Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class TblProductAccountingAttrServiceImpl extends ServiceImpl<TblProductAccountingAttrMapper, TblProductAccountingAttr>
        implements TblProductAccountingAttrService {
    private static final Logger log = LoggerFactory.getLogger(TblProductAccountingAttrServiceImpl.class);

    @Override
    public IPage<TblProductAccountingAttr> getPage(Integer pageNo, Integer pageSize, String attrCode,
                                                    String attrName, String productType,
                                                    String accountingSubjectType, String accountingMethod,
                                                    Integer isEnabled, Long orgId) {
        Page<TblProductAccountingAttr> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TblProductAccountingAttr> wrapper = new QueryWrapper<>();

        // 使用Entity类的属性名(驼峰命名),MyBatis-Plus会自动转换为数据库字段名
        if (StringUtils.hasText(attrCode)) {
            wrapper.like("attr_code", attrCode);
        }
        if (StringUtils.hasText(attrName)) {
            wrapper.like("attr_name", attrName);
        }
        if (StringUtils.hasText(productType)) {
            wrapper.eq("product_type", productType);
        }
        if (StringUtils.hasText(accountingSubjectType)) {
            wrapper.eq("accounting_subject_type", accountingSubjectType);
        }
        if (StringUtils.hasText(accountingMethod)) {
            wrapper.eq("accounting_method", accountingMethod);
        }
        // 只有明确传递了isEnabled参数才过滤,否则查询所有
        if (isEnabled != null) {
            wrapper.eq("is_enabled", isEnabled);
        }
        // 移除orgId过滤,先查询所有数据测试
        // if (orgId != null) {
        //     wrapper.eq("org_id", orgId);
        // }

        log.info("查询条件 - attrCode:{}, attrName:{}, productType:{}, accountingSubjectType:{}, accountingMethod:{}, isEnabled:{}, orgId:{}",
                 attrCode, attrName, productType, accountingSubjectType, accountingMethod, isEnabled, orgId);
        log.info("SQL: {}", wrapper.getCustomSqlSegment());

        wrapper.orderByDesc("create_time");
        IPage<TblProductAccountingAttr> result = this.page(page, wrapper);

        log.info("查询结果 - 总记录数:{}, 当前页记录数:{}", result.getTotal(), result.getRecords().size());

        return result;
    }

    @Override
    public TblProductAccountingAttr getDetail(Long id) {
        return this.getById(id);
    }

    /**
     * 生成唯一ID（使用时间戳+随机数，避免SnowflakeIdWorker依赖）
     */
    private Long generateId() {
        // 使用当前时间戳（毫秒）+ 3位随机数，保证基本唯一性
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return timestamp * 1000 + random;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblProductAccountingAttr create(TblProductAccountingAttr entity, String createBy) {
        // 使用简化的ID生成策略，避免SnowflakeIdWorker依赖
        entity.setAttrId(generateId());
        entity.setCreateBy(createBy);
        entity.setCreateTime(new Date());
        entity.setUpdateBy(createBy);
        entity.setUpdateTime(new Date());
        if (entity.getIsEnabled() == null) {
            entity.setIsEnabled(1);
        }
        this.save(entity);
        log.info("创建产品核算属性成功 - attrId:{}, attrCode:{}", entity.getAttrId(), entity.getAttrCode());
        return entity;
    }

    @Override
    public boolean update(TblProductAccountingAttr entity, String updateBy) {
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
        TblProductAccountingAttr entity = new TblProductAccountingAttr();
        entity.setAttrId(id);
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
    public List<TblProductAccountingAttr> getEnabledList(Long orgId) {
        QueryWrapper<TblProductAccountingAttr> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_ENABLED", 1);
        if (orgId != null) {
            wrapper.eq("ORG_ID", orgId);
        }
        return this.list(wrapper);
    }

    @Override
    public boolean checkCodeUnique(String attrCode, Long excludeId) {
        return baseMapper.checkCodeUnique(attrCode, excludeId) == 0;
    }

    @Override
    public List<TblProductAccountingAttr> getByProductType(String productType, Long orgId) {
        return baseMapper.selectByProductType(productType, orgId);
    }

    @Override
    public TblProductAccountingAttr copy(Long id, String newCode, String newName, String createBy) {
        TblProductAccountingAttr source = this.getById(id);
        if (source == null) {
            return null;
        }
        TblProductAccountingAttr target = new TblProductAccountingAttr();
        BeanUtils.copyProperties(source, target);
        target.setAttrId(generateId());
        target.setAttrCode(newCode);
        target.setAttrName(newName);
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
            result.put("message", "该核算属性已被使用,无法删除");
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
