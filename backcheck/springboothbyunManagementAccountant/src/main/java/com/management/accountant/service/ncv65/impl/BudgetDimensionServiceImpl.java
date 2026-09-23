package com.management.accountant.service.ncv65.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.ncv65.BudgetDimension;
import com.management.accountant.mapper.ncv65.BudgetDimensionMapper;
import com.management.accountant.service.ncv65.IBudgetDimensionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * NCV65全面预算系统 - 预算维度服务实现类
 * 
 * @description 预算维度业务逻辑实现，兼容达梦数据库和MySQL数据库
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Slf4j
@Service
public class BudgetDimensionServiceImpl extends ServiceImpl<BudgetDimensionMapper, BudgetDimension> 
        implements IBudgetDimensionService {

    @Resource
    private BudgetDimensionMapper budgetDimensionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createDimension(BudgetDimension dimension) {
        try {
            // 检查维度编码是否存在
            if (checkDimensionCodeExists(dimension.getDimensionCode(), null)) {
                throw new RuntimeException("维度编码已存在：" + dimension.getDimensionCode());
            }

            // 设置默认值
            if (dimension.getIsEnabled() == null) {
                dimension.setIsEnabled(true);
            }
            if (dimension.getIsRequired() == null) {
                dimension.setIsRequired(false);
            }
            if (dimension.getIsSystem() == null) {
                dimension.setIsSystem(false);
            }
            if (!StringUtils.hasText(dimension.getStatus())) {
                dimension.setStatus(BudgetDimension.STATUS_ACTIVE);
            }
            if (dimension.getDimensionLevel() == null) {
                dimension.setDimensionLevel(1);
            }

            // 设置排序号
            if (dimension.getSortOrder() == null) {
                String parentId = dimension.getParentId();
                if (!StringUtils.hasText(parentId)) {
                    parentId = "";
                }
                Integer maxSortOrder = budgetDimensionMapper.getMaxSortOrder(parentId, getCurrentTenantId());
                dimension.setSortOrder(maxSortOrder + 1);
            }

            // 保存维度
            boolean result = save(dimension);
            
            if (result) {
                log.info("创建维度成功，ID：{}，编码：{}", dimension.getId(), dimension.getDimensionCode());
            }
            
            return result;
        } catch (Exception e) {
            log.error("创建维度失败：{}", e.getMessage(), e);
            throw new RuntimeException("创建维度失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDimension(BudgetDimension dimension) {
        try {
            // 检查维度是否存在
            BudgetDimension existingDimension = getById(dimension.getId());
            if (existingDimension == null) {
                throw new RuntimeException("维度不存在，ID：" + dimension.getId());
            }

            // 检查维度编码是否重复
            if (StringUtils.hasText(dimension.getDimensionCode()) && 
                !dimension.getDimensionCode().equals(existingDimension.getDimensionCode())) {
                if (checkDimensionCodeExists(dimension.getDimensionCode(), dimension.getId())) {
                    throw new RuntimeException("维度编码已存在：" + dimension.getDimensionCode());
                }
            }

            // 更新维度
            boolean result = updateById(dimension);
            
            if (result) {
                log.info("更新维度成功，ID：{}，编码：{}", dimension.getId(), dimension.getDimensionCode());
            }
            
            return result;
        } catch (Exception e) {
            log.error("更新维度失败：{}", e.getMessage(), e);
            throw new RuntimeException("更新维度失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDimension(String id) {
        try {
            // 检查维度是否存在
            BudgetDimension dimension = getById(id);
            if (dimension == null) {
                throw new RuntimeException("维度不存在，ID：" + id);
            }

            // 检查是否为系统维度
            if (dimension.getIsSystem() != null && dimension.getIsSystem()) {
                throw new RuntimeException("系统维度不能删除");
            }

            // 检查是否有子维度
            int childCount = budgetDimensionMapper.countChildDimensions(id, getCurrentTenantId());
            if (childCount > 0) {
                throw new RuntimeException("存在子维度，无法删除");
            }

            // 检查是否被使用
            Map<String, Object> usageStats = getDimensionUsageStatistics(id);
            if (usageStats != null && !usageStats.isEmpty()) {
                Integer usageCount = (Integer) usageStats.get("totalUsage");
                if (usageCount != null && usageCount > 0) {
                    throw new RuntimeException("维度正在使用中，无法删除");
                }
            }

            // 删除维度
            boolean result = removeById(id);
            
            if (result) {
                log.info("删除维度成功，ID：{}，编码：{}", id, dimension.getDimensionCode());
            }
            
            return result;
        } catch (Exception e) {
            log.error("删除维度失败：{}", e.getMessage(), e);
            throw new RuntimeException("删除维度失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteDimensions(List<String> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return true;
            }

            // 检查每个维度是否可以删除
            for (String id : ids) {
                BudgetDimension dimension = getById(id);
                if (dimension == null) {
                    continue;
                }

                // 检查是否为系统维度
                if (dimension.getIsSystem() != null && dimension.getIsSystem()) {
                    throw new RuntimeException("系统维度不能删除：" + dimension.getDimensionName());
                }

                // 检查是否有子维度
                int childCount = budgetDimensionMapper.countChildDimensions(id, getCurrentTenantId());
                if (childCount > 0) {
                    throw new RuntimeException("存在子维度，无法删除：" + dimension.getDimensionName());
                }

                // 检查是否被使用
                Map<String, Object> usageStats = getDimensionUsageStatistics(id);
                if (usageStats != null && !usageStats.isEmpty()) {
                    Integer usageCount = (Integer) usageStats.get("totalUsage");
                    if (usageCount != null && usageCount > 0) {
                        throw new RuntimeException("维度正在使用中，无法删除：" + dimension.getDimensionName());
                    }
                }
            }

            // 批量删除
            boolean result = removeByIds(ids);
            
            if (result) {
                log.info("批量删除维度成功，数量：{}", ids.size());
            }
            
            return result;
        } catch (Exception e) {
            log.error("批量删除维度失败：{}", e.getMessage(), e);
            throw new RuntimeException("批量删除维度失败：" + e.getMessage());
        }
    }

    @Override
    public BudgetDimension getDimensionById(String id) {
        try {
            return getById(id);
        } catch (Exception e) {
            log.error("查询维度失败，ID：{}，错误：{}", id, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public BudgetDimension getDimensionByCode(String dimensionCode) {
        try {
            String tenantId = getCurrentTenantId();
            return budgetDimensionMapper.selectByDimensionCode(dimensionCode, tenantId);
        } catch (Exception e) {
            log.error("根据编码查询维度失败，编码：{}，错误：{}", dimensionCode, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public IPage<BudgetDimension> getDimensionPage(Long current, Long size, Map<String, Object> params) {
        try {
            Page<BudgetDimension> page = new Page<>(current, size);
            
            // 添加租户ID到查询参数
            if (params == null) {
                params = new HashMap<>();
            }
            params.put("tenantId", getCurrentTenantId());
            
            return budgetDimensionMapper.selectPageWithConditions(page, params);
        } catch (Exception e) {
            log.error("分页查询维度失败：{}", e.getMessage(), e);
            return new Page<>(current, size);
        }
    }

    @Override
    public List<BudgetDimension> getDimensionsByType(String dimensionType) {
        try {
            String tenantId = getCurrentTenantId();
            return budgetDimensionMapper.selectByDimensionType(dimensionType, tenantId);
        } catch (Exception e) {
            log.error("根据类型查询维度失败，类型：{}，错误：{}", dimensionType, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<BudgetDimension> getEnabledDimensions() {
        try {
            String tenantId = getCurrentTenantId();
            return budgetDimensionMapper.selectEnabledDimensions(tenantId);
        } catch (Exception e) {
            log.error("查询启用的维度失败：{}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<BudgetDimension> getRequiredDimensions() {
        try {
            String tenantId = getCurrentTenantId();
            return budgetDimensionMapper.selectRequiredDimensions(tenantId);
        } catch (Exception e) {
            log.error("查询必填维度失败：{}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<BudgetDimension> getSystemDimensions() {
        try {
            String tenantId = getCurrentTenantId();
            return budgetDimensionMapper.selectSystemDimensions(tenantId);
        } catch (Exception e) {
            log.error("查询系统维度失败：{}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableDimension(String id) {
        try {
            BudgetDimension dimension = new BudgetDimension();
            dimension.setId(id);
            dimension.setIsEnabled(true);
            dimension.setStatus(BudgetDimension.STATUS_ACTIVE);
            
            boolean result = updateById(dimension);
            
            if (result) {
                log.info("启用维度成功，ID：{}", id);
            }
            
            return result;
        } catch (Exception e) {
            log.error("启用维度失败，ID：{}，错误：{}", id, e.getMessage(), e);
            throw new RuntimeException("启用维度失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disableDimension(String id) {
        try {
            BudgetDimension dimension = new BudgetDimension();
            dimension.setId(id);
            dimension.setIsEnabled(false);
            dimension.setStatus(BudgetDimension.STATUS_INACTIVE);
            
            boolean result = updateById(dimension);
            
            if (result) {
                log.info("禁用维度成功，ID：{}", id);
            }
            
            return result;
        } catch (Exception e) {
            log.error("禁用维度失败，ID：{}，错误：{}", id, e.getMessage(), e);
            throw new RuntimeException("禁用维度失败：" + e.getMessage());
        }
    }

    @Override
    public boolean checkDimensionCodeExists(String dimensionCode, String excludeId) {
        try {
            String tenantId = getCurrentTenantId();
            String excludeIdValue = excludeId != null ? excludeId : "";
            int count = budgetDimensionMapper.checkDimensionCodeExists(dimensionCode, excludeIdValue, tenantId);
            return count > 0;
        } catch (Exception e) {
            log.error("检查维度编码是否存在失败，编码：{}，错误：{}", dimensionCode, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getDimensionTree(String parentId) {
        try {
            String tenantId = getCurrentTenantId();
            List<BudgetDimension> dimensions = budgetDimensionMapper.selectDimensionTree(parentId, tenantId);
            
            List<Map<String, Object>> treeData = new ArrayList<>();
            for (BudgetDimension dimension : dimensions) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", dimension.getId());
                node.put("code", dimension.getDimensionCode());
                node.put("name", dimension.getDimensionName());
                node.put("type", dimension.getDimensionType());
                node.put("level", dimension.getDimensionLevel());
                node.put("isEnabled", dimension.getIsEnabled());
                node.put("isRequired", dimension.getIsRequired());
                node.put("isSystem", dimension.getIsSystem());
                
                // 递归获取子节点
                List<Map<String, Object>> children = getDimensionTree(dimension.getId());
                if (!children.isEmpty()) {
                    node.put("children", children);
                }
                
                treeData.add(node);
            }
            
            return treeData;
        } catch (Exception e) {
            log.error("获取维度树失败，父ID：{}，错误：{}", parentId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Map<String, Object>> getFullDimensionTree() {
        try {
            return getDimensionTree(null);
        } catch (Exception e) {
            log.error("获取完整维度树失败：{}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // 其他方法实现...

    /**
     * 获取当前租户ID
     * @return 租户ID
     */
    private String getCurrentTenantId() {
        // 这里应该从上下文获取当前租户ID，暂时返回默认值
        return "default";
    }

    /**
     * 获取当前用户ID
     * @return 用户ID
     */
    private String getCurrentUserId() {
        // 这里应该从上下文获取当前用户ID，暂时返回默认值
        return "system";
    }
}
