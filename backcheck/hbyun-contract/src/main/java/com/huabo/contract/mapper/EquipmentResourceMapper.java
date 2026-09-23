package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.contract.entity.EquipmentResource;
import com.huabo.contract.vo.EquipmentResourceQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 设备资源Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface EquipmentResourceMapper extends BaseMapper<EquipmentResource> {

    /**
     * 分页查询设备资源列表
     * 
     * @param page 分页参数
     * @param queryParam 查询参数
     * @return 分页结果
     */
    IPage<EquipmentResource> selectEquipmentResourcePage(Page<EquipmentResource> page, @Param("param") EquipmentResourceQueryParam queryParam);

    /**
     * 根据项目策划ID查询设备资源列表
     * 
     * @param planningId 项目策划ID
     * @return 设备资源列表
     */
    List<EquipmentResource> selectByPlanningId(@Param("planningId") Long planningId);

    /**
     * 根据设备类型查询设备资源列表
     * 
     * @param equipmentType 设备类型
     * @return 设备资源列表
     */
    List<EquipmentResource> selectByEquipmentType(@Param("equipmentType") Integer equipmentType);

    /**
     * 根据分配状态查询设备资源列表
     * 
     * @param allocationStatus 分配状态
     * @return 设备资源列表
     */
    List<EquipmentResource> selectByAllocationStatus(@Param("allocationStatus") Integer allocationStatus);

    /**
     * 根据设备状态查询设备资源列表
     * 
     * @param equipmentStatus 设备状态
     * @return 设备资源列表
     */
    List<EquipmentResource> selectByEquipmentStatus(@Param("equipmentStatus") Integer equipmentStatus);

    /**
     * 根据租赁方式查询设备资源列表
     * 
     * @param leaseType 租赁方式
     * @return 设备资源列表
     */
    List<EquipmentResource> selectByLeaseType(@Param("leaseType") Integer leaseType);

    /**
     * 根据供应商ID查询设备资源列表
     * 
     * @param supplierId 供应商ID
     * @return 设备资源列表
     */
    List<EquipmentResource> selectBySupplierId(@Param("supplierId") Long supplierId);

    /**
     * 查询负责人的设备资源列表
     * 
     * @param managerId 负责人ID
     * @return 设备资源列表
     */
    List<EquipmentResource> selectByManagerId(@Param("managerId") Long managerId);

    /**
     * 根据使用地点查询设备资源列表
     * 
     * @param usageLocation 使用地点
     * @return 设备资源列表
     */
    List<EquipmentResource> selectByUsageLocation(@Param("usageLocation") String usageLocation);

    /**
     * 根据项目策划ID统计预算成本总额
     * 
     * @param planningId 项目策划ID
     * @return 预算成本总额
     */
    BigDecimal sumBudgetedCostByPlanning(@Param("planningId") Long planningId);

    /**
     * 根据项目策划ID统计实际成本总额
     * 
     * @param planningId 项目策划ID
     * @return 实际成本总额
     */
    BigDecimal sumActualCostByPlanning(@Param("planningId") Long planningId);

    /**
     * 根据设备类型统计需求数量
     * 
     * @param equipmentType 设备类型
     * @return 需求数量
     */
    Integer sumRequiredQuantityByType(@Param("equipmentType") Integer equipmentType);

    /**
     * 根据设备类型统计已分配数量
     * 
     * @param equipmentType 设备类型
     * @return 已分配数量
     */
    Integer sumAllocatedQuantityByType(@Param("equipmentType") Integer equipmentType);

    /**
     * 根据分配状态统计数量
     * 
     * @param allocationStatus 分配状态
     * @return 数量
     */
    Integer countByAllocationStatus(@Param("allocationStatus") Integer allocationStatus);

    /**
     * 根据设备状态统计数量
     * 
     * @param equipmentStatus 设备状态
     * @return 数量
     */
    Integer countByEquipmentStatus(@Param("equipmentStatus") Integer equipmentStatus);

    /**
     * 查询超预算的设备资源列表
     * 
     * @return 超预算的设备资源列表
     */
    List<EquipmentResource> selectOverBudget();

    /**
     * 查询分配不足的设备资源列表（已分配数量 < 需求数量）
     * 
     * @return 分配不足的设备资源列表
     */
    List<EquipmentResource> selectUnderAllocated();

    /**
     * 查询故障设备列表
     * 
     * @return 故障设备列表
     */
    List<EquipmentResource> selectFaultyEquipment();

    /**
     * 批量更新分配状态
     * 
     * @param ids 设备ID列表
     * @param allocationStatus 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    Integer batchUpdateAllocationStatus(@Param("ids") List<Long> ids, @Param("allocationStatus") Integer allocationStatus, @Param("updateBy") Long updateBy);

    /**
     * 批量更新设备状态
     * 
     * @param ids 设备ID列表
     * @param equipmentStatus 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    Integer batchUpdateEquipmentStatus(@Param("ids") List<Long> ids, @Param("equipmentStatus") Integer equipmentStatus, @Param("updateBy") Long updateBy);

    /**
     * 查询设备资源统计信息
     * 
     * @return 统计信息
     */
    List<EquipmentResource> selectEquipmentStatistics();
}
