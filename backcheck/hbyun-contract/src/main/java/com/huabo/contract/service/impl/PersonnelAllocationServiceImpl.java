package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.contract.entity.PersonnelAllocation;
import com.huabo.contract.mapper.PersonnelAllocationMapper;
import com.huabo.contract.service.PersonnelAllocationService;
import com.huabo.contract.vo.PersonnelAllocationQueryParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 人员配置服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Service
public class PersonnelAllocationServiceImpl extends ServiceImpl<PersonnelAllocationMapper, PersonnelAllocation> implements PersonnelAllocationService {

    @Override
    public IPage<PersonnelAllocation> getPersonnelAllocationPage(PersonnelAllocationQueryParam queryParam) {
        Page<PersonnelAllocation> page = new Page<>(queryParam.getCurrent(), queryParam.getSize());
        return baseMapper.selectPersonnelAllocationPage(page, queryParam);
    }

    @Override
    public List<PersonnelAllocation> getByPlanningId(Long planningId) {
        return baseMapper.selectByPlanningId(planningId);
    }

    @Override
    public List<PersonnelAllocation> getByAllocationStatus(Integer allocationStatus) {
        return baseMapper.selectByAllocationStatus(allocationStatus);
    }

    @Override
    public List<PersonnelAllocation> getByPositionType(Integer positionType) {
        return baseMapper.selectByPositionType(positionType);
    }

    @Override
    public List<PersonnelAllocation> getByPriority(Integer priority) {
        return baseMapper.selectByPriority(priority);
    }

    @Override
    public List<PersonnelAllocation> getByManagerId(Long managerId) {
        return baseMapper.selectByManagerId(managerId);
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
    public Integer sumRequiredCountByPositionType(Integer positionType) {
        return baseMapper.sumRequiredCountByPositionType(positionType);
    }

    @Override
    public Integer sumAllocatedCountByPositionType(Integer positionType) {
        return baseMapper.sumAllocatedCountByPositionType(positionType);
    }

    @Override
    public Integer countByAllocationStatus(Integer allocationStatus) {
        return baseMapper.countByAllocationStatus(allocationStatus);
    }

    @Override
    public List<PersonnelAllocation> getOverBudget() {
        return baseMapper.selectOverBudget();
    }

    @Override
    public List<PersonnelAllocation> getUnderAllocated() {
        return baseMapper.selectUnderAllocated();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateStatus(List<Long> ids, Integer allocationStatus, Long updateBy) {
        return baseMapper.batchUpdateStatus(ids, allocationStatus, updateBy);
    }

    @Override
    public List<PersonnelAllocation> getByWorkLocation(String workLocation) {
        return baseMapper.selectByWorkLocation(workLocation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean allocatePersonnel(Long id, String personnelList, Long updateBy) {
        PersonnelAllocation allocation = getById(id);
        if (allocation == null) {
            return false;
        }
        
        allocation.setAllocatedPersonnel(personnelList);
        allocation.setAllocationStatus(2); // 已分配
        allocation.setUpdateBy(updateBy);
        allocation.setUpdateTime(new Date());
        
        return updateById(allocation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean adjustPersonnel(Long id, String newPersonnelList, Long updateBy) {
        PersonnelAllocation allocation = getById(id);
        if (allocation == null) {
            return false;
        }
        
        allocation.setAllocatedPersonnel(newPersonnelList);
        allocation.setUpdateBy(updateBy);
        allocation.setUpdateTime(new Date());
        
        return updateById(allocation);
    }

    @Override
    public List<PersonnelAllocation> getAllocationStatistics() {
        return baseMapper.selectAllocationStatistics();
    }

    @Override
    public List<String> getOptimizationSuggestions(Long planningId) {
        List<String> suggestions = new ArrayList<>();
        
        // 查询项目的人员配置
        List<PersonnelAllocation> allocations = getByPlanningId(planningId);
        
        for (PersonnelAllocation allocation : allocations) {
            // 检查分配不足
            if (allocation.getAllocatedCount() != null && allocation.getRequiredCount() != null) {
                if (allocation.getAllocatedCount() < allocation.getRequiredCount()) {
                    suggestions.add("岗位 " + allocation.getPositionName() + " 人员配置不足，需要补充 " + 
                                  (allocation.getRequiredCount() - allocation.getAllocatedCount()) + " 人");
                }
            }
            
            // 检查超预算
            if (allocation.isOverBudget()) {
                suggestions.add("岗位 " + allocation.getPositionName() + " 成本超预算，建议优化人员配置");
            }
            
            // 检查高优先级未分配
            if (allocation.getPriority() != null && allocation.getPriority() >= 3 && 
                allocation.getAllocationStatus() != null && allocation.getAllocationStatus() == 1) {
                suggestions.add("高优先级岗位 " + allocation.getPositionName() + " 尚未分配人员，请尽快处理");
            }
        }
        
        return suggestions;
    }

    @Override
    public Boolean validateAllocationInfo(PersonnelAllocation personnelAllocation) {
        if (personnelAllocation == null) {
            return false;
        }
        
        // 验证必填字段
        if (personnelAllocation.getPlanningId() == null || 
            personnelAllocation.getPositionName() == null ||
            personnelAllocation.getPositionType() == null ||
            personnelAllocation.getRequiredCount() == null) {
            return false;
        }
        
        // 验证数量
        if (personnelAllocation.getRequiredCount() <= 0) {
            return false;
        }
        
        // 验证预算
        if (personnelAllocation.getBudgetedCost() != null && 
            personnelAllocation.getBudgetedCost().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        
        return true;
    }
}
