package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financial.sharing.oracle.entity.InventoryCategoryEntity;
import com.financial.sharing.oracle.mapper.InventoryCategoryMapper;
import com.financial.sharing.service.InventoryCategoryService;
import com.financial.sharing.util.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 存货分类服务实现类
 *
 * @author system
 * @since 2026-01-27
 */
@Service
public class InventoryCategoryServiceImpl implements InventoryCategoryService {

    @Autowired
    private InventoryCategoryMapper categoryMapper;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public List<Map<String, Object>> getCategoryTree() {
        Long tenantId = 1L; // TODO: 从上下文获取租户ID
        // 查询所有分类
        List<Map<String, Object>> allCategories = categoryMapper.selectCategoryTree(tenantId);

        // 临时：直接返回查询结果，不构建树形结构，用于调试
        return allCategories;

        // 构建树形结构
        // return buildTree(allCategories, 0L);
    }

    /**
     * 构建树形结构
     */
    private List<Map<String, Object>> buildTree(List<Map<String, Object>> allCategories, Long parentId) {
        List<Map<String, Object>> tree = new ArrayList<>();

        if (allCategories == null || allCategories.isEmpty()) {
            return tree;
        }

        for (Map<String, Object> category : allCategories) {
            // 安全获取 parentId
            Object parentIdObj = category.get("parentId");
            Long pid = null;
            if (parentIdObj != null) {
                try {
                    // 处理 BigDecimal 类型
                    if (parentIdObj instanceof java.math.BigDecimal) {
                        pid = ((java.math.BigDecimal) parentIdObj).longValue();
                    } else if (parentIdObj instanceof Number) {
                        pid = ((Number) parentIdObj).longValue();
                    } else {
                        pid = Long.valueOf(parentIdObj.toString());
                    }
                } catch (Exception e) {
                    // 如果转换失败，默认为0
                    pid = 0L;
                }
            } else {
                pid = 0L;
            }

            if (pid.equals(parentId)) {
                // 安全获取 categoryId
                Object categoryIdObj = category.get("categoryId");
                if (categoryIdObj != null) {
                    try {
                        Long categoryId = null;
                        // 处理 BigDecimal 类型
                        if (categoryIdObj instanceof java.math.BigDecimal) {
                            categoryId = ((java.math.BigDecimal) categoryIdObj).longValue();
                        } else if (categoryIdObj instanceof Number) {
                            categoryId = ((Number) categoryIdObj).longValue();
                        } else {
                            categoryId = Long.valueOf(categoryIdObj.toString());
                        }

                        // 递归查找子节点
                        List<Map<String, Object>> children = buildTree(allCategories, categoryId);

                        if (!children.isEmpty()) {
                            category.put("children", children);
                            category.put("hasChildren", true);
                        } else {
                            category.put("hasChildren", false);
                        }

                        tree.add(category);
                    } catch (Exception e) {
                        // 如果 categoryId 转换失败，跳过这条记录
                        continue;
                    }
                }
            }
        }

        return tree;
    }

    @Override
    public Map<String, Object> getCategoryById(Long categoryId) {
        Long tenantId = 1L; // TODO: 从上下文获取租户ID
        LambdaQueryWrapper<InventoryCategoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryCategoryEntity::getCategoryId, categoryId)
               .eq(InventoryCategoryEntity::getTenantId, tenantId)
               .eq(InventoryCategoryEntity::getDeleted, 0);

        InventoryCategoryEntity entity = categoryMapper.selectOne(wrapper);
        if (entity == null) {
            return null;
        }

        // 转换为Map
        Map<String, Object> result = new HashMap<>();
        result.put("categoryId", entity.getCategoryId());
        result.put("categoryCode", entity.getCategoryCode());
        result.put("categoryName", entity.getCategoryName());
        result.put("parentId", entity.getParentId());
        result.put("categoryLevel", entity.getCategoryLevel());
        result.put("categoryPath", entity.getCategoryPath());
        result.put("pricingMethod", entity.getPricingMethod());
        result.put("defaultUnitId", entity.getDefaultUnitId());
        result.put("defaultWarehouseId", entity.getDefaultWarehouseId());
        result.put("sortOrder", entity.getSortOrder());
        result.put("status", entity.getStatus());
        result.put("remark", entity.getRemark());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateCategory(Map<String, Object> param) {
        Long tenantId = 1L; // TODO: 从上下文获取租户ID

        // 先判断是新增还是更新
        boolean isUpdate = false;
        Object categoryIdObj = param.get("categoryId");
        if (categoryIdObj != null && !"null".equals(categoryIdObj.toString()) && !"".equals(categoryIdObj.toString().trim())) {
            isUpdate = true;
        }

        InventoryCategoryEntity category = new InventoryCategoryEntity();

        // 设置分类ID
        if (isUpdate) {
            category.setCategoryId(Long.valueOf(categoryIdObj.toString()));
        } else {
            category.setCategoryId(snowflakeIdWorker.nextId());
        }

        category.setCategoryCode((String) param.get("categoryCode"));
        category.setCategoryName((String) param.get("categoryName"));
        category.setTenantId(tenantId);

        if (param.get("parentId") != null) {
            category.setParentId(Long.valueOf(param.get("parentId").toString()));
        } else {
            category.setParentId(0L);
        }

        if (param.get("categoryLevel") != null) {
            category.setCategoryLevel(Integer.valueOf(param.get("categoryLevel").toString()));
        } else {
            category.setCategoryLevel(1);
        }

        if (param.get("pricingMethod") != null) {
            category.setPricingMethod(Integer.valueOf(param.get("pricingMethod").toString()));
        }

        if (param.get("defaultUnitId") != null) {
            category.setDefaultUnitId(Long.valueOf(param.get("defaultUnitId").toString()));
        }

        if (param.get("defaultWarehouseId") != null) {
            category.setDefaultWarehouseId(Long.valueOf(param.get("defaultWarehouseId").toString()));
        }

        if (param.get("sortOrder") != null) {
            category.setSortOrder(Integer.valueOf(param.get("sortOrder").toString()));
        } else {
            category.setSortOrder(0);
        }

        if (param.get("status") != null) {
            category.setStatus(Integer.valueOf(param.get("status").toString()));
        } else {
            category.setStatus(1);
        }

        category.setRemark((String) param.get("remark"));

        // 设置分类路径
        if (category.getParentId() == 0L) {
            category.setCategoryPath(category.getCategoryId().toString());
        } else {
            Map<String, Object> parent = getCategoryById(category.getParentId());
            if (parent != null) {
                category.setCategoryPath(parent.get("categoryPath") + "," + category.getCategoryId());
                category.setCategoryLevel((Integer) parent.get("categoryLevel") + 1);
            }
        }

        // 设置删除标记为0（未删除）
        category.setDeleted(0);

        // 执行新增或更新
        if (isUpdate) {
            return categoryMapper.updateById(category) > 0;
        } else {
            return categoryMapper.insert(category) > 0;
        }
    }




    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCategory(Long categoryId) {
        Long tenantId = 1L; // TODO: 从上下文获取租户ID

        // 检查是否有子分类
        int childCount = categoryMapper.countChildCategories(categoryId, tenantId);
        if (childCount > 0) {
            throw new RuntimeException("该分类下存在子分类，无法删除");
        }

        // 检查是否有存货
        int inventoryCount = categoryMapper.countInventories(categoryId, tenantId);
        if (inventoryCount > 0) {
            throw new RuntimeException("该分类下存在存货，无法删除");
        }

        // 使用 MyBatis-Plus 的 removeById 方法进行逻辑删除
        // 因为实体类的 deleted 字段有 @TableLogic 注解，会自动处理逻辑删除
        return categoryMapper.deleteById(categoryId) > 0;
    }

    @Override
    public List<Map<String, Object>> getInventoriesByCategory(Long categoryId) {
        Long tenantId = 1L; // TODO: 从上下文获取租户ID
        return categoryMapper.selectInventoriesByCategoryId(categoryId, tenantId);
    }

    @Override
    public String exportCategory(Map<String, Object> param) {
        // TODO: 实现导出功能
        throw new UnsupportedOperationException("导出功能暂未实现");
    }
}