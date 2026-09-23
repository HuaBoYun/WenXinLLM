package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.contract.entity.MaterialRequirement;
import com.huabo.contract.mapper.MaterialRequirementMapper;
import com.huabo.contract.service.MaterialRequirementService;
import com.huabo.contract.vo.MaterialRequirementQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 材料需求服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
public class MaterialRequirementServiceImpl extends ServiceImpl<MaterialRequirementMapper, MaterialRequirement> implements MaterialRequirementService {

    @Override
    public IPage<MaterialRequirement> getMaterialRequirementPage(MaterialRequirementQueryParam queryParam) {
        Page<MaterialRequirement> page = new Page<>(queryParam.getCurrent(), queryParam.getSize());
        return baseMapper.selectMaterialRequirementPage(page, queryParam);
    }

    @Override
    public List<MaterialRequirement> getByPlanningId(Long planningId) {
        return baseMapper.selectByPlanningId(planningId);
    }

    @Override
    public List<MaterialRequirement> getByMaterialType(Integer materialType) {
        return baseMapper.selectByMaterialType(materialType);
    }

    @Override
    public List<MaterialRequirement> getByProcurementStatus(Integer procurementStatus) {
        return baseMapper.selectByProcurementStatus(procurementStatus);
    }

    @Override
    public List<MaterialRequirement> getByPriority(Integer priority) {
        return baseMapper.selectByPriority(priority);
    }

    @Override
    public List<MaterialRequirement> getBySupplierId(Long supplierId) {
        return baseMapper.selectBySupplierId(supplierId);
    }

    @Override
    public List<MaterialRequirement> getByManagerId(Long managerId) {
        return baseMapper.selectByManagerId(managerId);
    }

    @Override
    public List<MaterialRequirement> getByUsageLocation(String usageLocation) {
        return baseMapper.selectByUsageLocation(usageLocation);
    }

    @Override
    public List<MaterialRequirement> getByPurchaser(String purchaser) {
        return baseMapper.selectByPurchaser(purchaser);
    }

    @Override
    public List<MaterialRequirement> getByInspector(String inspector) {
        return baseMapper.selectByInspector(inspector);
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
    public BigDecimal sumRequiredQuantityByType(Integer materialType) {
        return baseMapper.sumRequiredQuantityByType(materialType);
    }

    @Override
    public BigDecimal sumPurchasedQuantityByType(Integer materialType) {
        return baseMapper.sumPurchasedQuantityByType(materialType);
    }

    @Override
    public Integer countByProcurementStatus(Integer procurementStatus) {
        return baseMapper.countByProcurementStatus(procurementStatus);
    }

    @Override
    public List<MaterialRequirement> getOverBudget() {
        return baseMapper.selectOverBudget();
    }

    @Override
    public List<MaterialRequirement> getUrgentRequirements() {
        return baseMapper.selectUrgentRequirements();
    }

    @Override
    public List<MaterialRequirement> getUnderProcured() {
        return baseMapper.selectUnderProcured();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateStatus(List<Long> ids, Integer procurementStatus, Long updateBy) {
        return baseMapper.batchUpdateStatus(ids, procurementStatus, updateBy);
    }

    @Override
    public List<MaterialRequirement> getByBrand(String brand) {
        return baseMapper.selectByBrand(brand);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean procureMaterial(Long id, BigDecimal purchasedQuantity, BigDecimal actualUnitPrice, Long supplierId, Long updateBy) {
        MaterialRequirement material = getById(id);
        if (material == null) {
            return false;
        }
        
        // 检查采购数量是否超过需求数量
        if (material.getRequiredQuantity() != null && purchasedQuantity.compareTo(material.getRequiredQuantity()) > 0) {
            return false;
        }
        
        material.setPurchasedQuantity(purchasedQuantity);
        material.setActualUnitPrice(actualUnitPrice);
        material.setActualTotalCost(purchasedQuantity.multiply(actualUnitPrice));
        material.setSupplierId(supplierId);
        material.setProcurementStatus(2); // 采购中
        material.setPurchaseTime(new Date());
        material.setUpdateBy(updateBy);
        material.setUpdateTime(new Date());
        
        return updateById(material);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean inspectMaterial(Long id, String inspector, String inspectionResult, Long updateBy) {
        MaterialRequirement material = getById(id);
        if (material == null) {
            return false;
        }
        
        material.setInspector(inspector);
        material.setInspectionResult(inspectionResult);
        material.setProcurementStatus(4); // 已验收
        material.setUpdateBy(updateBy);
        material.setUpdateTime(new Date());
        
        return updateById(material);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean storeMaterial(Long id, Long updateBy) {
        MaterialRequirement material = getById(id);
        if (material == null) {
            return false;
        }
        
        material.setProcurementStatus(5); // 已入库
        material.setUpdateBy(updateBy);
        material.setUpdateTime(new Date());
        
        return updateById(material);
    }

    @Override
    public List<MaterialRequirement> getMaterialStatistics() {
        return baseMapper.selectMaterialStatistics();
    }

    @Override
    public List<String> getOptimizationSuggestions(Long planningId) {
        List<String> suggestions = new ArrayList<>();
        
        // 查询项目的材料需求
        List<MaterialRequirement> materials = getByPlanningId(planningId);
        
        for (MaterialRequirement material : materials) {
            // 检查采购不足
            if (material.getPurchasedQuantity() != null && material.getRequiredQuantity() != null) {
                if (material.getPurchasedQuantity().compareTo(material.getRequiredQuantity()) < 0) {
                    BigDecimal shortage = material.getRequiredQuantity().subtract(material.getPurchasedQuantity());
                    suggestions.add("材料 " + material.getMaterialName() + " 采购不足，还需采购 " + 
                                  shortage + " " + material.getUnit());
                }
            }
            
            // 检查超预算
            if (material.isOverBudget()) {
                suggestions.add("材料 " + material.getMaterialName() + " 成本超预算，建议重新询价或寻找替代材料");
            }
            
            // 检查紧急需求
            if (material.isUrgent()) {
                suggestions.add("材料 " + material.getMaterialName() + " 需求紧急，请优先处理采购");
            }
            
            // 检查高优先级未采购
            if (material.getPriority() != null && material.getPriority() >= 3 && 
                material.getProcurementStatus() != null && material.getProcurementStatus() == 1) {
                suggestions.add("高优先级材料 " + material.getMaterialName() + " 尚未开始采购，请尽快处理");
            }
            
            // 检查长期未到货
            if (material.getProcurementStatus() != null && material.getProcurementStatus() == 2 &&
                material.getPurchaseTime() != null) {
                long daysSincePurchase = (System.currentTimeMillis() - material.getPurchaseTime().getTime()) / (24 * 60 * 60 * 1000);
                if (daysSincePurchase > 7) {
                    suggestions.add("材料 " + material.getMaterialName() + " 采购已超过7天未到货，请跟进供应商");
                }
            }
        }
        
        return suggestions;
    }

    @Override
    public Boolean validateMaterialInfo(MaterialRequirement materialRequirement) {
        if (materialRequirement == null) {
            log.error("材料需求对象为空");
            return false;
        }

        // 验证必填字段
        if (materialRequirement.getPlanningId() == null) {
            log.error("项目策划ID为空");
            return false;
        }

        if (materialRequirement.getMaterialName() == null || materialRequirement.getMaterialName().trim().isEmpty()) {
            log.error("材料名称为空");
            return false;
        }

        if (materialRequirement.getRequiredQuantity() == null) {
            log.error("需求数量为空");
            return false;
        }

        // 验证数量
        if (materialRequirement.getRequiredQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("需求数量必须大于0，当前值：" + materialRequirement.getRequiredQuantity());
            return false;
        }

        // 验证预估总成本
        if (materialRequirement.getEstimatedTotalCost() != null &&
            materialRequirement.getEstimatedTotalCost().compareTo(BigDecimal.ZERO) < 0) {
            log.error("预估总成本不能为负数");
            return false;
        }

        // 验证预估单价
        if (materialRequirement.getEstimatedUnitPrice() != null &&
            materialRequirement.getEstimatedUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            log.error("预估单价不能为负数");
            return false;
        }

        log.info("材料需求信息验证通过：" + materialRequirement.getMaterialName());
        return true;
    }
}
