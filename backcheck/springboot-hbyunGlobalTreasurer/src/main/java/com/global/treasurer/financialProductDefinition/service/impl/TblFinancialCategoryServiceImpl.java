package com.global.treasurer.financialProductDefinition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.SnowflakeIdWorker;
import com.global.treasurer.financialProductDefinition.entity.TblFinancialCategory;
import com.global.treasurer.financialProductDefinition.mapper.TblFinancialCategoryMapper;
import com.global.treasurer.financialProductDefinition.service.TblFinancialCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 理财分类管理Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class TblFinancialCategoryServiceImpl extends ServiceImpl<TblFinancialCategoryMapper, TblFinancialCategory>
        implements TblFinancialCategoryService {
    private static final Logger log = LoggerFactory.getLogger(TblFinancialCategoryServiceImpl.class);

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public IPage<TblFinancialCategory> getPage(Integer pageNo, Integer pageSize, String categoryCode,
                                                String categoryName, Integer isEnabled, Long orgId, Long parentCategoryId) {
        log.info("=== Service层查询参数 === pageNo={}, pageSize={}, categoryCode={}, categoryName={}, isEnabled={}, orgId={}, parentCategoryId={}",
                pageNo, pageSize, categoryCode, categoryName, isEnabled, orgId, parentCategoryId);

        Page<TblFinancialCategory> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TblFinancialCategory> wrapper = new QueryWrapper<>();

        if (StringUtils.hasText(categoryCode)) {
            wrapper.like("CATEGORY_CODE", categoryCode);
            log.info("=== 添加查询条件 === CATEGORY_CODE LIKE {}", categoryCode);
        }
        if (StringUtils.hasText(categoryName)) {
            wrapper.like("CATEGORY_NAME", categoryName);
            log.info("=== 添加查询条件 === CATEGORY_NAME LIKE {}", categoryName);
        }
        if (parentCategoryId != null) {
            wrapper.eq("PARENT_ID", parentCategoryId);
            log.info("=== 添加查询条件 === PARENT_ID = {}", parentCategoryId);
        } else {
            // 如果parentCategoryId为null或空，可以查询顶级分类（PARENT_ID为null或0）
            // 这里不添加条件，查询所有分类
            log.info("=== parentCategoryId为null，查询所有分类 ===");
        }
        if (isEnabled != null) {
            wrapper.eq("IS_ENABLED", isEnabled);
            log.info("=== 添加查询条件 === IS_ENABLED = {}", isEnabled);
        }
        if (orgId != null) {
            wrapper.eq("ORG_ID", orgId);
            log.info("=== 添加查询条件 === ORG_ID = {}", orgId);
        }

        wrapper.orderByAsc("SORT_ORDER").orderByDesc("CREATE_TIME");

        // 输出最终SQL
        log.info("=== 最终查询SQL === {}", wrapper.getCustomSqlSegment());
        log.info("=== WHERE条件 === {}", wrapper);

        IPage<TblFinancialCategory> result = this.page(page, wrapper);

        log.info("=== 分页查询结果 === total={}, records.size={}", result.getTotal(), result.getRecords().size());

        // 如果查询结果为0,尝试无条件查询
        if (result.getTotal() == 0) {
            log.warn("=== 查询结果为0,尝试无条件查询表总数 ===");
            long totalCount = this.count();
            log.warn("=== 表总记录数 === {}", totalCount);
        }

        return result;
    }

    @Override
    public TblFinancialCategory getDetail(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblFinancialCategory create(TblFinancialCategory entity, String createBy) {
        entity.setCategoryId(snowflakeIdWorker.nextId());
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

        // 添加调试日志
        log.info("=== 创建金融分类 ===");
        log.info("categoryCode: {}", entity.getCategoryCode());
        log.info("categoryName: {}", entity.getCategoryName());
        log.info("parentId: {}", entity.getParentId());
        log.info("description: {}", entity.getDescription());
        log.info("isEnabled: {}", entity.getIsEnabled());
        log.info("orgId: {}", entity.getOrgId());

        this.save(entity);
        return entity;
    }

    @Override
    public boolean update(TblFinancialCategory entity, String updateBy) {
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
        TblFinancialCategory entity = new TblFinancialCategory();
        entity.setCategoryId(id);
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
    public List<TblFinancialCategory> getEnabledList(Long orgId) {
        log.info("=== getEnabledList开始 === orgId={}", orgId);
        QueryWrapper<TblFinancialCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_ENABLED", 1);
        if (orgId != null) {
            wrapper.eq("ORG_ID", orgId);
            log.info("=== 添加查询条件 === ORG_ID = {}", orgId);
        }
        wrapper.orderByAsc("SORT_ORDER");

        long total = this.count(wrapper);
        log.info("=== getEnabledList查询前总数 === total={}", total);

        List<TblFinancialCategory> list = this.list(wrapper);
        log.info("=== getEnabledList查询结果 === size={}", list.size());

        return list;
    }

    @Override
    public boolean checkCodeUnique(String categoryCode, Long excludeId) {
        // 使用MyBatis-Plus的QueryWrapper替代自定义SQL映射
        QueryWrapper<TblFinancialCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("CATEGORY_CODE", categoryCode);
        if (excludeId != null) {
            wrapper.ne("CATEGORY_ID", excludeId);
        }
        return this.count(wrapper) == 0;
    }

    @Override
    public List<TblFinancialCategory> getTree(Long orgId) {
        log.info("=== getTree开始 === orgId={}", orgId);
        List<TblFinancialCategory> allList = getEnabledList(orgId);
        log.info("=== getEnabledList返回 === size={}", allList.size());
        List<TblFinancialCategory> tree = buildTree(allList, null);
        log.info("=== buildTree完成 === tree.size={}", tree.size());
        return tree;
    }

    private List<TblFinancialCategory> buildTree(List<TblFinancialCategory> list, Long parentId) {
        return list.stream()
                .filter(item -> Objects.equals(item.getParentId(), parentId))
                .peek(item -> item.setChildren(buildTree(list, item.getCategoryId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<TblFinancialCategory> getByParentId(Long parentId, Long orgId) {
        return baseMapper.selectByParentId(parentId, orgId);
    }

    @Override
    public boolean sort(List<TblFinancialCategory> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSortOrder(i);
        }
        return baseMapper.batchUpdateSort(list) > 0;
    }

    @Override
    public TblFinancialCategory copy(Long id, String newCode, String newName, String createBy) {
        TblFinancialCategory source = this.getById(id);
        if (source == null) {
            return null;
        }
        TblFinancialCategory target = new TblFinancialCategory();
        BeanUtils.copyProperties(source, target);
        target.setCategoryId(snowflakeIdWorker.nextId());
        target.setCategoryCode(newCode);
        target.setCategoryName(newName);
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
        QueryWrapper<TblFinancialCategory> totalWrapper = new QueryWrapper<>();
        if (orgId != null) {
            totalWrapper.eq("ORG_ID", orgId);
        }
        long total = this.count(totalWrapper);
        result.put("total", total);

        // 启用数
        QueryWrapper<TblFinancialCategory> enabledWrapper = new QueryWrapper<>();
        enabledWrapper.eq("IS_ENABLED", 1);
        if (orgId != null) {
            enabledWrapper.eq("ORG_ID", orgId);
        }
        long enabled = this.count(enabledWrapper);
        result.put("enabled", enabled);

        return result;
    }

    @Override
    public Map<String, Object> validateDelete(Long id) {
        Map<String, Object> result = new HashMap<>();
        int usage = baseMapper.countUsage(id);
        result.put("canDelete", usage == 0);
        result.put("usageCount", usage);
        if (usage > 0) {
            result.put("message", "该分类已被使用,无法删除");
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
