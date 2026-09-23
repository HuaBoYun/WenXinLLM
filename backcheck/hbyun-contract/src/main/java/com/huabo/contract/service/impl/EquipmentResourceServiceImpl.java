package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.contract.entity.EquipmentResource;
import com.huabo.contract.mapper.EquipmentResourceMapper;
import com.huabo.contract.service.EquipmentResourceService;
import com.huabo.contract.vo.EquipmentResourceQueryParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 设备资源服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Service
public class EquipmentResourceServiceImpl extends ServiceImpl<EquipmentResourceMapper, EquipmentResource> implements EquipmentResourceService {

    @Override
    public IPage<EquipmentResource> getEquipmentResourcePage(EquipmentResourceQueryParam queryParam) {
        Page<EquipmentResource> page = new Page<>(queryParam.getCurrent(), queryParam.getSize());
        return baseMapper.selectEquipmentResourcePage(page, queryParam);
    }

    @Override
    public List<EquipmentResource> getByPlanningId(Long planningId) {
        return baseMapper.selectByPlanningId(planningId);
    }

    @Override
    public List<EquipmentResource> getByEquipmentType(Integer equipmentType) {
        return baseMapper.selectByEquipmentType(equipmentType);
    }

    @Override
    public List<EquipmentResource> getByAllocationStatus(Integer allocationStatus) {
        return baseMapper.selectByAllocationStatus(allocationStatus);
    }

    @Override
    public List<EquipmentResource> getByEquipmentStatus(Integer equipmentStatus) {
        return baseMapper.selectByEquipmentStatus(equipmentStatus);
    }

    @Override
    public List<EquipmentResource> getByLeaseType(Integer leaseType) {
        return baseMapper.selectByLeaseType(leaseType);
    }

    @Override
    public List<EquipmentResource> getBySupplierId(Long supplierId) {
        return baseMapper.selectBySupplierId(supplierId);
    }

    @Override
    public List<EquipmentResource> getByManagerId(Long managerId) {
        return baseMapper.selectByManagerId(managerId);
    }

    @Override
    public List<EquipmentResource> getByUsageLocation(String usageLocation) {
        return baseMapper.selectByUsageLocation(usageLocation);
    }

    @Override
    public BigDecimal sumBudgetedCostByPlanning(Long planningId) {
        return baseMapper.sumBudgetedCostByPlanning(planningId);
    }

    @Override
    public BigDecimal sumActualCostByPlanning(Long planningId) {
        return baseMapper.sumActualCostByPlanning(planningId);
    }

    @Override
    public Integer sumRequiredQuantityByType(Integer equipmentType) {
        return baseMapper.sumRequiredQuantityByType(equipmentType);
    }

    @Override
    public Integer sumAllocatedQuantityByType(Integer equipmentType) {
        return baseMapper.sumAllocatedQuantityByType(equipmentType);
    }

    @Override
    public Integer countByAllocationStatus(Integer allocationStatus) {
        return baseMapper.countByAllocationStatus(allocationStatus);
    }

    @Override
    public Integer countByEquipmentStatus(Integer equipmentStatus) {
        return baseMapper.countByEquipmentStatus(equipmentStatus);
    }

    @Override
    public List<EquipmentResource> getOverBudget() {
        return baseMapper.selectOverBudget();
    }

    @Override
    public List<EquipmentResource> getUnderAllocated() {
        return baseMapper.selectUnderAllocated();
    }

    @Override
    public List<EquipmentResource> getFaultyEquipment() {
        return baseMapper.selectFaultyEquipment();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateAllocationStatus(List<Long> ids, Integer allocationStatus, Long updateBy) {
        return baseMapper.batchUpdateAllocationStatus(ids, allocationStatus, updateBy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateEquipmentStatus(List<Long> ids, Integer equipmentStatus, Long updateBy) {
        return baseMapper.batchUpdateEquipmentStatus(ids, equipmentStatus, updateBy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean allocateEquipment(Long id, Integer allocatedQuantity, Long updateBy) {
        EquipmentResource equipment = getById(id);
        if (equipment == null) {
            return false;
        }
        
        // 检查分配数量是否超过需求数量
        if (equipment.getRequiredQuantity() != null && allocatedQuantity > equipment.getRequiredQuantity()) {
            return false;
        }
        
        equipment.setAllocatedQuantity(allocatedQuantity);
        equipment.setAllocationStatus(2); // 已分配
        equipment.setUpdateBy(updateBy);
        equipment.setUpdateTime(new Date());
        
        return updateById(equipment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean returnEquipment(Long id, Integer returnQuantity, Long updateBy) {
        EquipmentResource equipment = getById(id);
        if (equipment == null) {
            return false;
        }
        
        // 检查归还数量是否超过已分配数量
        if (equipment.getAllocatedQuantity() != null && returnQuantity > equipment.getAllocatedQuantity()) {
            return false;
        }
        
        int newAllocatedQuantity = equipment.getAllocatedQuantity() - returnQuantity;
        equipment.setAllocatedQuantity(newAllocatedQuantity);
        
        if (newAllocatedQuantity == 0) {
            equipment.setAllocationStatus(4); // 已归还
        }
        
        equipment.setUpdateBy(updateBy);
        equipment.setUpdateTime(new Date());
        
        return updateById(equipment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean maintainEquipment(Long id, String maintenanceReason, Long updateBy) {
        EquipmentResource equipment = getById(id);
        if (equipment == null) {
            return false;
        }
        
        equipment.setEquipmentStatus(2); // 维修中
        equipment.setMaintenanceRequirements(maintenanceReason);
        equipment.setUpdateBy(updateBy);
        equipment.setUpdateTime(new Date());
        
        return updateById(equipment);
    }

    @Override
    public List<EquipmentResource> getEquipmentStatistics() {
        return baseMapper.selectEquipmentStatistics();
    }

    @Override
    public List<String> getOptimizationSuggestions(Long planningId) {
        List<String> suggestions = new ArrayList<>();
        
        // 查询项目的设备资源
        List<EquipmentResource> equipments = getByPlanningId(planningId);
        
        for (EquipmentResource equipment : equipments) {
            // 检查分配不足
            if (equipment.getAllocatedQuantity() != null && equipment.getRequiredQuantity() != null) {
                if (equipment.getAllocatedQuantity() < equipment.getRequiredQuantity()) {
                    suggestions.add("设备 " + equipment.getEquipmentName() + " 分配不足，需要补充 " + 
                                  (equipment.getRequiredQuantity() - equipment.getAllocatedQuantity()) + " " + equipment.getUnit());
                }
            }
            
            // 检查超预算
            if (equipment.isOverBudget()) {
                suggestions.add("设备 " + equipment.getEquipmentName() + " 成本超预算，建议重新评估或寻找替代方案");
            }
            
            // 检查故障设备
            if (equipment.getEquipmentStatus() != null && equipment.getEquipmentStatus() == 3) {
                suggestions.add("设备 " + equipment.getEquipmentName() + " 出现故障，请及时维修或更换");
            }
            
            // 检查维修中设备
            if (equipment.getEquipmentStatus() != null && equipment.getEquipmentStatus() == 2) {
                suggestions.add("设备 " + equipment.getEquipmentName() + " 正在维修中，请关注维修进度");
            }
        }
        
        return suggestions;
    }

    @Override
    public Boolean validateEquipmentInfo(EquipmentResource equipmentResource) {
        if (equipmentResource == null) {
            return false;
        }
        
        // 验证必填字段
        if (equipmentResource.getPlanningId() == null || 
            equipmentResource.getEquipmentName() == null ||
            equipmentResource.getEquipmentType() == null ||
            equipmentResource.getRequiredQuantity() == null) {
            return false;
        }
        
        // 验证数量
        if (equipmentResource.getRequiredQuantity() <= 0) {
            return false;
        }
        
        // 验证预算
        if (equipmentResource.getBudgetedCost() != null && 
            equipmentResource.getBudgetedCost().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        
        // 验证单价
        if (equipmentResource.getUnitPrice() != null && 
            equipmentResource.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        
        return true;
    }
}
