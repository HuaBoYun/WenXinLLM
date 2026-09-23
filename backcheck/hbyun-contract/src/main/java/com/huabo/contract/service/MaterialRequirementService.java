package com.huabo.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.contract.entity.MaterialRequirement;
import com.huabo.contract.vo.MaterialRequirementQueryParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 材料需求服务接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface MaterialRequirementService extends IService<MaterialRequirement> {

    /**
     * 分页查询材料需求列表
     * 
     * @param queryParam 查询参数
     * @return 分页结果
     */
    IPage<MaterialRequirement> getMaterialRequirementPage(MaterialRequirementQueryParam queryParam);

    /**
     * 根据项目策划ID查询材料需求列表
     * 
     * @param planningId 项目策划ID
     * @return 材料需求列表
     */
    List<MaterialRequirement> getByPlanningId(Long planningId);

    /**
     * 根据材料类型查询材料需求列表
     * 
     * @param materialType 材料类型
     * @return 材料需求列表
     */
    List<MaterialRequirement> getByMaterialType(Integer materialType);

    /**
     * 根据采购状态查询材料需求列表
     * 
     * @param procurementStatus 采购状态
     * @return 材料需求列表
     */
    List<MaterialRequirement> getByProcurementStatus(Integer procurementStatus);

    /**
     * 根据优先级查询材料需求列表
     * 
     * @param priority 优先级
     * @return 材料需求列表
     */
    List<MaterialRequirement> getByPriority(Integer priority);

    /**
     * 根据供应商ID查询材料需求列表
     * 
     * @param supplierId 供应商ID
     * @return 材料需求列表
     */
    List<MaterialRequirement> getBySupplierId(Long supplierId);

    /**
     * 查询负责人的材料需求列表
     * 
     * @param managerId 负责人ID
     * @return 材料需求列表
     */
    List<MaterialRequirement> getByManagerId(Long managerId);

    /**
     * 根据使用地点查询材料需求列表
     * 
     * @param usageLocation 使用地点
     * @return 材料需求列表
     */
    List<MaterialRequirement> getByUsageLocation(String usageLocation);

    /**
     * 根据采购人员查询材料需求列表
     * 
     * @param purchaser 采购人员
     * @return 材料需求列表
     */
    List<MaterialRequirement> getByPurchaser(String purchaser);

    /**
     * 根据验收人员查询材料需求列表
     * 
     * @param inspector 验收人员
     * @return 材料需求列表
     */
    List<MaterialRequirement> getByInspector(String inspector);

    /**
     * 根据项目策划ID统计预算成本总额
     * 
     * @param planningId 项目策划ID
     * @return 预算成本总额
     */
    BigDecimal sumBudgetedCostByPlanning(Long planningId);

    /**
     * 根据项目策划ID统计实际成本总额
     * 
     * @param planningId 项目策划ID
     * @return 实际成本总额
     */
    BigDecimal sumActualCostByPlanning(Long planningId);

    /**
     * 根据材料类型统计需求数量
     * 
     * @param materialType 材料类型
     * @return 需求数量
     */
    BigDecimal sumRequiredQuantityByType(Integer materialType);

    /**
     * 根据材料类型统计已采购数量
     * 
     * @param materialType 材料类型
     * @return 已采购数量
     */
    BigDecimal sumPurchasedQuantityByType(Integer materialType);

    /**
     * 根据采购状态统计数量
     * 
     * @param procurementStatus 采购状态
     * @return 数量
     */
    Integer countByProcurementStatus(Integer procurementStatus);

    /**
     * 查询超预算的材料需求列表
     * 
     * @return 超预算的材料需求列表
     */
    List<MaterialRequirement> getOverBudget();

    /**
     * 查询紧急需求的材料列表（3天内需求）
     * 
     * @return 紧急需求的材料列表
     */
    List<MaterialRequirement> getUrgentRequirements();

    /**
     * 查询采购不足的材料需求列表（已采购数量 < 需求数量）
     * 
     * @return 采购不足的材料需求列表
     */
    List<MaterialRequirement> getUnderProcured();

    /**
     * 批量更新采购状态
     * 
     * @param ids 材料需求ID列表
     * @param procurementStatus 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    Integer batchUpdateStatus(List<Long> ids, Integer procurementStatus, Long updateBy);

    /**
     * 根据品牌查询材料需求列表
     * 
     * @param brand 品牌
     * @return 材料需求列表
     */
    List<MaterialRequirement> getByBrand(String brand);

    /**
     * 材料采购
     * 
     * @param id 材料需求ID
     * @param purchasedQuantity 采购数量
     * @param actualUnitPrice 实际单价
     * @param supplierId 供应商ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    Boolean procureMaterial(Long id, BigDecimal purchasedQuantity, BigDecimal actualUnitPrice, Long supplierId, Long updateBy);

    /**
     * 材料验收
     * 
     * @param id 材料需求ID
     * @param inspector 验收人员
     * @param inspectionResult 验收结果
     * @param updateBy 更新人
     * @return 是否成功
     */
    Boolean inspectMaterial(Long id, String inspector, String inspectionResult, Long updateBy);

    /**
     * 材料入库
     * 
     * @param id 材料需求ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    Boolean storeMaterial(Long id, Long updateBy);

    /**
     * 查询材料需求统计信息
     * 
     * @return 统计信息
     */
    List<MaterialRequirement> getMaterialStatistics();

    /**
     * 材料需求优化建议
     * 
     * @param planningId 项目策划ID
     * @return 优化建议
     */
    List<String> getOptimizationSuggestions(Long planningId);

    /**
     * 验证材料需求信息
     * 
     * @param materialRequirement 材料需求信息
     * @return 验证结果
     */
    Boolean validateMaterialInfo(MaterialRequirement materialRequirement);
}
