package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.contract.entity.MaterialRequirement;
import com.huabo.contract.vo.MaterialRequirementQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 材料需求Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface MaterialRequirementMapper extends BaseMapper<MaterialRequirement> {

    /**
     * 分页查询材料需求列表
     * 
     * @param page 分页参数
     * @param queryParam 查询参数
     * @return 分页结果
     */
    IPage<MaterialRequirement> selectMaterialRequirementPage(Page<MaterialRequirement> page, @Param("param") MaterialRequirementQueryParam queryParam);

    /**
     * 根据项目策划ID查询材料需求列表
     * 
     * @param planningId 项目策划ID
     * @return 材料需求列表
     */
    List<MaterialRequirement> selectByPlanningId(@Param("planningId") Long planningId);

    /**
     * 根据材料类型查询材料需求列表
     * 
     * @param materialType 材料类型
     * @return 材料需求列表
     */
    List<MaterialRequirement> selectByMaterialType(@Param("materialType") Integer materialType);

    /**
     * 根据采购状态查询材料需求列表
     * 
     * @param procurementStatus 采购状态
     * @return 材料需求列表
     */
    List<MaterialRequirement> selectByProcurementStatus(@Param("procurementStatus") Integer procurementStatus);

    /**
     * 根据优先级查询材料需求列表
     * 
     * @param priority 优先级
     * @return 材料需求列表
     */
    List<MaterialRequirement> selectByPriority(@Param("priority") Integer priority);

    /**
     * 根据供应商ID查询材料需求列表
     * 
     * @param supplierId 供应商ID
     * @return 材料需求列表
     */
    List<MaterialRequirement> selectBySupplierId(@Param("supplierId") Long supplierId);

    /**
     * 查询负责人的材料需求列表
     * 
     * @param managerId 负责人ID
     * @return 材料需求列表
     */
    List<MaterialRequirement> selectByManagerId(@Param("managerId") Long managerId);

    /**
     * 根据使用地点查询材料需求列表
     * 
     * @param usageLocation 使用地点
     * @return 材料需求列表
     */
    List<MaterialRequirement> selectByUsageLocation(@Param("usageLocation") String usageLocation);

    /**
     * 根据采购人员查询材料需求列表
     * 
     * @param purchaser 采购人员
     * @return 材料需求列表
     */
    List<MaterialRequirement> selectByPurchaser(@Param("purchaser") String purchaser);

    /**
     * 根据验收人员查询材料需求列表
     * 
     * @param inspector 验收人员
     * @return 材料需求列表
     */
    List<MaterialRequirement> selectByInspector(@Param("inspector") String inspector);

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
     * 根据材料类型统计需求数量
     * 
     * @param materialType 材料类型
     * @return 需求数量
     */
    BigDecimal sumRequiredQuantityByType(@Param("materialType") Integer materialType);

    /**
     * 根据材料类型统计已采购数量
     * 
     * @param materialType 材料类型
     * @return 已采购数量
     */
    BigDecimal sumPurchasedQuantityByType(@Param("materialType") Integer materialType);

    /**
     * 根据采购状态统计数量
     * 
     * @param procurementStatus 采购状态
     * @return 数量
     */
    Integer countByProcurementStatus(@Param("procurementStatus") Integer procurementStatus);

    /**
     * 查询超预算的材料需求列表
     * 
     * @return 超预算的材料需求列表
     */
    List<MaterialRequirement> selectOverBudget();

    /**
     * 查询紧急需求的材料列表（3天内需求）
     * 
     * @return 紧急需求的材料列表
     */
    List<MaterialRequirement> selectUrgentRequirements();

    /**
     * 查询采购不足的材料需求列表（已采购数量 < 需求数量）
     * 
     * @return 采购不足的材料需求列表
     */
    List<MaterialRequirement> selectUnderProcured();

    /**
     * 批量更新采购状态
     * 
     * @param ids 材料需求ID列表
     * @param procurementStatus 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    Integer batchUpdateStatus(@Param("ids") List<Long> ids, @Param("procurementStatus") Integer procurementStatus, @Param("updateBy") Long updateBy);

    /**
     * 根据品牌查询材料需求列表
     * 
     * @param brand 品牌
     * @return 材料需求列表
     */
    List<MaterialRequirement> selectByBrand(@Param("brand") String brand);

    /**
     * 查询材料需求统计信息
     * 
     * @return 统计信息
     */
    List<MaterialRequirement> selectMaterialStatistics();
}
