package com.huabo.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.contract.entity.EquipmentResource;
import com.huabo.contract.vo.EquipmentResourceQueryParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 设备资源服务接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface EquipmentResourceService extends IService<EquipmentResource> {

    /**
     * 分页查询设备资源列表
     * 
     * @param queryParam 查询参数
     * @return 分页结果
     */
    IPage<EquipmentResource> getEquipmentResourcePage(EquipmentResourceQueryParam queryParam);

    /**
     * 根据项目策划ID查询设备资源列表
     * 
     * @param planningId 项目策划ID
     * @return 设备资源列表
     */
    List<EquipmentResource> getByPlanningId(Long planningId);

    /**
     * 根据设备类型查询设备资源列表
     * 
     * @param equipmentType 设备类型
     * @return 设备资源列表
     */
    List<EquipmentResource> getByEquipmentType(Integer equipmentType);

    /**
     * 根据分配状态查询设备资源列表
     * 
     * @param allocationStatus 分配状态
     * @return 设备资源列表
     */
    List<EquipmentResource> getByAllocationStatus(Integer allocationStatus);

    /**
     * 根据设备状态查询设备资源列表
     * 
     * @param equipmentStatus 设备状态
     * @return 设备资源列表
     */
    List<EquipmentResource> getByEquipmentStatus(Integer equipmentStatus);

    /**
     * 根据租赁方式查询设备资源列表
     * 
     * @param leaseType 租赁方式
     * @return 设备资源列表
     */
    List<EquipmentResource> getByLeaseType(Integer leaseType);

    /**
     * 根据供应商ID查询设备资源列表
     * 
     * @param supplierId 供应商ID
     * @return 设备资源列表
     */
    List<EquipmentResource> getBySupplierId(Long supplierId);

    /**
     * 查询负责人的设备资源列表
     * 
     * @param managerId 负责人ID
     * @return 设备资源列表
     */
    List<EquipmentResource> getByManagerId(Long managerId);

    /**
     * 根据使用地点查询设备资源列表
     * 
     * @param usageLocation 使用地点
     * @return 设备资源列表
     */
    List<EquipmentResource> getByUsageLocation(String usageLocation);

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
     * 根据设备类型统计需求数量
     * 
     * @param equipmentType 设备类型
     * @return 需求数量
     */
    Integer sumRequiredQuantityByType(Integer equipmentType);

    /**
     * 根据设备类型统计已分配数量
     * 
     * @param equipmentType 设备类型
     * @return 已分配数量
     */
    Integer sumAllocatedQuantityByType(Integer equipmentType);

    /**
     * 根据分配状态统计数量
     * 
     * @param allocationStatus 分配状态
     * @return 数量
     */
    Integer countByAllocationStatus(Integer allocationStatus);

    /**
     * 根据设备状态统计数量
     * 
     * @param equipmentStatus 设备状态
     * @return 数量
     */
    Integer countByEquipmentStatus(Integer equipmentStatus);

    /**
     * 查询超预算的设备资源列表
     * 
     * @return 超预算的设备资源列表
     */
    List<EquipmentResource> getOverBudget();

    /**
     * 查询分配不足的设备资源列表（已分配数量 < 需求数量）
     * 
     * @return 分配不足的设备资源列表
     */
    List<EquipmentResource> getUnderAllocated();

    /**
     * 查询故障设备列表
     * 
     * @return 故障设备列表
     */
    List<EquipmentResource> getFaultyEquipment();

    /**
     * 批量更新分配状态
     * 
     * @param ids 设备ID列表
     * @param allocationStatus 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    Integer batchUpdateAllocationStatus(List<Long> ids, Integer allocationStatus, Long updateBy);

    /**
     * 批量更新设备状态
     * 
     * @param ids 设备ID列表
     * @param equipmentStatus 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    Integer batchUpdateEquipmentStatus(List<Long> ids, Integer equipmentStatus, Long updateBy);

    /**
     * 设备分配
     * 
     * @param id 设备ID
     * @param allocatedQuantity 分配数量
     * @param updateBy 更新人
     * @return 是否成功
     */
    Boolean allocateEquipment(Long id, Integer allocatedQuantity, Long updateBy);

    /**
     * 设备归还
     * 
     * @param id 设备ID
     * @param returnQuantity 归还数量
     * @param updateBy 更新人
     * @return 是否成功
     */
    Boolean returnEquipment(Long id, Integer returnQuantity, Long updateBy);

    /**
     * 设备维修
     * 
     * @param id 设备ID
     * @param maintenanceReason 维修原因
     * @param updateBy 更新人
     * @return 是否成功
     */
    Boolean maintainEquipment(Long id, String maintenanceReason, Long updateBy);

    /**
     * 查询设备资源统计信息
     * 
     * @return 统计信息
     */
    List<EquipmentResource> getEquipmentStatistics();

    /**
     * 设备资源优化建议
     * 
     * @param planningId 项目策划ID
     * @return 优化建议
     */
    List<String> getOptimizationSuggestions(Long planningId);

    /**
     * 验证设备资源信息
     * 
     * @param equipmentResource 设备资源信息
     * @return 验证结果
     */
    Boolean validateEquipmentInfo(EquipmentResource equipmentResource);
}
