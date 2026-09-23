package com.huabo.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.contract.entity.PersonnelAllocation;
import com.huabo.contract.vo.PersonnelAllocationQueryParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 人员配置服务接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface PersonnelAllocationService extends IService<PersonnelAllocation> {

    /**
     * 分页查询人员配置列表
     * 
     * @param queryParam 查询参数
     * @return 分页结果
     */
    IPage<PersonnelAllocation> getPersonnelAllocationPage(PersonnelAllocationQueryParam queryParam);

    /**
     * 根据项目策划ID查询人员配置列表
     * 
     * @param planningId 项目策划ID
     * @return 人员配置列表
     */
    List<PersonnelAllocation> getByPlanningId(Long planningId);

    /**
     * 根据配置状态查询人员配置列表
     * 
     * @param allocationStatus 配置状态
     * @return 人员配置列表
     */
    List<PersonnelAllocation> getByAllocationStatus(Integer allocationStatus);

    /**
     * 根据岗位类型查询人员配置列表
     * 
     * @param positionType 岗位类型
     * @return 人员配置列表
     */
    List<PersonnelAllocation> getByPositionType(Integer positionType);

    /**
     * 根据优先级查询人员配置列表
     * 
     * @param priority 优先级
     * @return 人员配置列表
     */
    List<PersonnelAllocation> getByPriority(Integer priority);

    /**
     * 查询负责人的人员配置列表
     * 
     * @param managerId 负责人ID
     * @return 人员配置列表
     */
    List<PersonnelAllocation> getByManagerId(Long managerId);

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
     * 根据岗位类型统计需求人数
     * 
     * @param positionType 岗位类型
     * @return 需求人数
     */
    Integer sumRequiredCountByPositionType(Integer positionType);

    /**
     * 根据岗位类型统计已分配人数
     * 
     * @param positionType 岗位类型
     * @return 已分配人数
     */
    Integer sumAllocatedCountByPositionType(Integer positionType);

    /**
     * 根据配置状态统计数量
     * 
     * @param allocationStatus 配置状态
     * @return 数量
     */
    Integer countByAllocationStatus(Integer allocationStatus);

    /**
     * 查询超预算的人员配置列表
     * 
     * @return 超预算的人员配置列表
     */
    List<PersonnelAllocation> getOverBudget();

    /**
     * 查询分配不足的人员配置列表（已分配人数 < 需求人数）
     * 
     * @return 分配不足的人员配置列表
     */
    List<PersonnelAllocation> getUnderAllocated();

    /**
     * 批量更新配置状态
     * 
     * @param ids 配置ID列表
     * @param allocationStatus 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    Integer batchUpdateStatus(List<Long> ids, Integer allocationStatus, Long updateBy);

    /**
     * 根据工作地点查询人员配置列表
     * 
     * @param workLocation 工作地点
     * @return 人员配置列表
     */
    List<PersonnelAllocation> getByWorkLocation(String workLocation);

    /**
     * 人员分配
     * 
     * @param id 配置ID
     * @param personnelList 人员列表（JSON格式）
     * @param updateBy 更新人
     * @return 是否成功
     */
    Boolean allocatePersonnel(Long id, String personnelList, Long updateBy);

    /**
     * 人员调整
     * 
     * @param id 配置ID
     * @param newPersonnelList 新人员列表（JSON格式）
     * @param updateBy 更新人
     * @return 是否成功
     */
    Boolean adjustPersonnel(Long id, String newPersonnelList, Long updateBy);

    /**
     * 查询人员配置统计信息
     * 
     * @return 统计信息
     */
    List<PersonnelAllocation> getAllocationStatistics();

    /**
     * 人员配置优化建议
     * 
     * @param planningId 项目策划ID
     * @return 优化建议
     */
    List<String> getOptimizationSuggestions(Long planningId);

    /**
     * 验证人员配置信息
     * 
     * @param personnelAllocation 人员配置信息
     * @return 验证结果
     */
    Boolean validateAllocationInfo(PersonnelAllocation personnelAllocation);
}
