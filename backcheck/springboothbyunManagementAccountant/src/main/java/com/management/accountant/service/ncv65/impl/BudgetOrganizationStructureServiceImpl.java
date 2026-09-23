package com.management.accountant.service.ncv65.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.ncv65.BudgetOrganizationStructure;
import com.management.accountant.mapper.ncv65.BudgetOrganizationStructureMapper;
import com.management.accountant.service.ncv65.IBudgetOrganizationStructureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * NCV65全面预算系统 - 预算组织体系服务实现类
 * 
 * @description 预算组织体系业务逻辑实现，兼容达梦数据库和MySQL数据库
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Slf4j
@Service
public class BudgetOrganizationStructureServiceImpl extends ServiceImpl<BudgetOrganizationStructureMapper, BudgetOrganizationStructure> 
        implements IBudgetOrganizationStructureService {

    @Resource
    private BudgetOrganizationStructureMapper budgetOrganizationStructureMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createStructure(BudgetOrganizationStructure structure) {
        try {
            // 检查体系编码是否存在
            if (checkStructureCodeExists(structure.getStructureCode(), null)) {
                throw new RuntimeException("体系编码已存在：" + structure.getStructureCode());
            }

            // 设置默认值
            if (structure.getIsEnabled() == null) {
                structure.setIsEnabled(true);
            }
            if (!StringUtils.hasText(structure.getStatus())) {
                structure.setStatus(BudgetOrganizationStructure.STATUS_ACTIVE);
            }
            if (structure.getMaxLevels() == null) {
                structure.setMaxLevels(8);
            }

            // 保存组织体系
            boolean result = save(structure);
            
            if (result) {
                log.info("创建组织体系成功，ID：{}，编码：{}", structure.getId(), structure.getStructureCode());
            }
            
            return result;
        } catch (Exception e) {
            log.error("创建组织体系失败：{}", e.getMessage(), e);
            throw new RuntimeException("创建组织体系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStructure(BudgetOrganizationStructure structure) {
        try {
            // 检查体系是否存在
            BudgetOrganizationStructure existingStructure = getById(structure.getId());
            if (existingStructure == null) {
                throw new RuntimeException("组织体系不存在，ID：" + structure.getId());
            }

            // 检查体系编码是否重复
            if (StringUtils.hasText(structure.getStructureCode()) && 
                !structure.getStructureCode().equals(existingStructure.getStructureCode())) {
                if (checkStructureCodeExists(structure.getStructureCode(), structure.getId())) {
                    throw new RuntimeException("体系编码已存在：" + structure.getStructureCode());
                }
            }

            // 更新组织体系
            boolean result = updateById(structure);
            
            if (result) {
                log.info("更新组织体系成功，ID：{}，编码：{}", structure.getId(), structure.getStructureCode());
            }
            
            return result;
        } catch (Exception e) {
            log.error("更新组织体系失败：{}", e.getMessage(), e);
            throw new RuntimeException("更新组织体系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteStructure(String id) {
        try {
            // 检查体系是否存在
            BudgetOrganizationStructure structure = getById(id);
            if (structure == null) {
                throw new RuntimeException("组织体系不存在，ID：" + id);
            }

            // 检查是否可以删除（是否被使用）
            Map<String, Object> usageStats = getStructureUsageStatistics(id);
            if (usageStats != null && !usageStats.isEmpty()) {
                Integer usageCount = (Integer) usageStats.get("totalUsage");
                if (usageCount != null && usageCount > 0) {
                    throw new RuntimeException("组织体系正在使用中，无法删除");
                }
            }

            // 删除组织体系
            boolean result = removeById(id);
            
            if (result) {
                log.info("删除组织体系成功，ID：{}，编码：{}", id, structure.getStructureCode());
            }
            
            return result;
        } catch (Exception e) {
            log.error("删除组织体系失败：{}", e.getMessage(), e);
            throw new RuntimeException("删除组织体系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteStructures(List<String> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return true;
            }

            // 检查每个体系是否可以删除
            for (String id : ids) {
                Map<String, Object> usageStats = getStructureUsageStatistics(id);
                if (usageStats != null && !usageStats.isEmpty()) {
                    Integer usageCount = (Integer) usageStats.get("totalUsage");
                    if (usageCount != null && usageCount > 0) {
                        BudgetOrganizationStructure structure = getById(id);
                        throw new RuntimeException("组织体系正在使用中，无法删除：" + 
                            (structure != null ? structure.getStructureName() : id));
                    }
                }
            }

            // 批量删除
            boolean result = removeByIds(ids);
            
            if (result) {
                log.info("批量删除组织体系成功，数量：{}", ids.size());
            }
            
            return result;
        } catch (Exception e) {
            log.error("批量删除组织体系失败：{}", e.getMessage(), e);
            throw new RuntimeException("批量删除组织体系失败：" + e.getMessage());
        }
    }

    @Override
    public BudgetOrganizationStructure getStructureById(String id) {
        try {
            return getById(id);
        } catch (Exception e) {
            log.error("查询组织体系失败，ID：{}，错误：{}", id, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public BudgetOrganizationStructure getStructureByCode(String structureCode) {
        try {
            // 获取当前租户ID（这里需要从上下文获取，暂时使用默认值）
            String tenantId = getCurrentTenantId();
            return budgetOrganizationStructureMapper.selectByStructureCode(structureCode, tenantId);
        } catch (Exception e) {
            log.error("根据编码查询组织体系失败，编码：{}，错误：{}", structureCode, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public IPage<BudgetOrganizationStructure> getStructurePage(Long current, Long size, Map<String, Object> params) {
        try {
            Page<BudgetOrganizationStructure> page = new Page<>(current, size);
            
            // 添加租户ID到查询参数
            if (params == null) {
                params = new HashMap<>();
            }
            params.put("tenantId", getCurrentTenantId());
            
            return budgetOrganizationStructureMapper.selectPageWithConditions(page, params);
        } catch (Exception e) {
            log.error("分页查询组织体系失败：{}", e.getMessage(), e);
            return new Page<>(current, size);
        }
    }

    @Override
    public List<BudgetOrganizationStructure> getStructuresByType(String structureType) {
        try {
            String tenantId = getCurrentTenantId();
            return budgetOrganizationStructureMapper.selectByStructureType(structureType, tenantId);
        } catch (Exception e) {
            log.error("根据类型查询组织体系失败，类型：{}，错误：{}", structureType, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<BudgetOrganizationStructure> getEnabledStructures() {
        try {
            String tenantId = getCurrentTenantId();
            return budgetOrganizationStructureMapper.selectEnabledStructures(tenantId);
        } catch (Exception e) {
            log.error("查询启用的组织体系失败：{}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableStructure(String id) {
        try {
            BudgetOrganizationStructure structure = new BudgetOrganizationStructure();
            structure.setId(id);
            structure.setIsEnabled(true);
            structure.setStatus(BudgetOrganizationStructure.STATUS_ACTIVE);
            
            boolean result = updateById(structure);
            
            if (result) {
                log.info("启用组织体系成功，ID：{}", id);
            }
            
            return result;
        } catch (Exception e) {
            log.error("启用组织体系失败，ID：{}，错误：{}", id, e.getMessage(), e);
            throw new RuntimeException("启用组织体系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disableStructure(String id) {
        try {
            BudgetOrganizationStructure structure = new BudgetOrganizationStructure();
            structure.setId(id);
            structure.setIsEnabled(false);
            structure.setStatus(BudgetOrganizationStructure.STATUS_INACTIVE);
            
            boolean result = updateById(structure);
            
            if (result) {
                log.info("禁用组织体系成功，ID：{}", id);
            }
            
            return result;
        } catch (Exception e) {
            log.error("禁用组织体系失败，ID：{}，错误：{}", id, e.getMessage(), e);
            throw new RuntimeException("禁用组织体系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateStatus(List<String> ids, String status) {
        try {
            if (ids == null || ids.isEmpty()) {
                return true;
            }

            String tenantId = getCurrentTenantId();
            String updateBy = getCurrentUserId();
            
            int updateCount = budgetOrganizationStructureMapper.batchUpdateStatus(ids, status, updateBy, tenantId);
            
            boolean result = updateCount > 0;
            
            if (result) {
                log.info("批量更新组织体系状态成功，数量：{}，状态：{}", updateCount, status);
            }
            
            return result;
        } catch (Exception e) {
            log.error("批量更新组织体系状态失败：{}", e.getMessage(), e);
            throw new RuntimeException("批量更新组织体系状态失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateEnabled(List<String> ids, Boolean isEnabled) {
        try {
            if (ids == null || ids.isEmpty()) {
                return true;
            }

            String tenantId = getCurrentTenantId();
            String updateBy = getCurrentUserId();
            
            int updateCount = budgetOrganizationStructureMapper.batchUpdateEnabled(ids, isEnabled, updateBy, tenantId);
            
            boolean result = updateCount > 0;
            
            if (result) {
                log.info("批量更新组织体系启用状态成功，数量：{}，启用：{}", updateCount, isEnabled);
            }
            
            return result;
        } catch (Exception e) {
            log.error("批量更新组织体系启用状态失败：{}", e.getMessage(), e);
            throw new RuntimeException("批量更新组织体系启用状态失败：" + e.getMessage());
        }
    }

    @Override
    public boolean checkStructureCodeExists(String structureCode, String excludeId) {
        try {
            String tenantId = getCurrentTenantId();
            String excludeIdValue = excludeId != null ? excludeId : "";
            int count = budgetOrganizationStructureMapper.checkStructureCodeExists(structureCode, excludeIdValue, tenantId);
            return count > 0;
        } catch (Exception e) {
            log.error("检查体系编码是否存在失败，编码：{}，错误：{}", structureCode, e.getMessage(), e);
            return false;
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
