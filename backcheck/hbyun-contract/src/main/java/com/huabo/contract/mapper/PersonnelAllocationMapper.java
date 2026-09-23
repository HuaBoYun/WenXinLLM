package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.contract.entity.PersonnelAllocation;
import com.huabo.contract.vo.PersonnelAllocationQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 人员配置Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface PersonnelAllocationMapper extends BaseMapper<PersonnelAllocation> {

    /**
     * 分页查询人员配置列表
     * 
     * @param page 分页参数
     * @param queryParam 查询参数
     * @return 分页结果
     */
    IPage<PersonnelAllocation> selectPersonnelAllocationPage(Page<PersonnelAllocation> page, @Param("param") PersonnelAllocationQueryParam queryParam);

    /**
     * 根据项目策划ID查询人员配置列表
     * 
     * @param planningId 项目策划ID
     * @return 人员配置列表
     */
    List<PersonnelAllocation> selectByPlanningId(@Param("planningId") Long planningId);

    /**
     * 根据配置状态查询人员配置列表
     * 
     * @param allocationStatus 配置状态
     * @return 人员配置列表
     */
    List<PersonnelAllocation> selectByAllocationStatus(@Param("allocationStatus") Integer allocationStatus);

    /**
     * 根据岗位类型查询人员配置列表
     * 
     * @param positionType 岗位类型
     * @return 人员配置列表
     */
    List<PersonnelAllocation> selectByPositionType(@Param("positionType") Integer positionType);

    /**
     * 根据优先级查询人员配置列表
     * 
     * @param priority 优先级
     * @return 人员配置列表
     */
    List<PersonnelAllocation> selectByPriority(@Param("priority") Integer priority);

    /**
     * 查询负责人的人员配置列表
     * 
     * @param managerId 负责人ID
     * @return 人员配置列表
     */
    List<PersonnelAllocation> selectByManagerId(@Param("managerId") Long managerId);

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
     * 根据岗位类型统计需求人数
     * 
     * @param positionType 岗位类型
     * @return 需求人数
     */
    Integer sumRequiredCountByPositionType(@Param("positionType") Integer positionType);

    /**
     * 根据岗位类型统计已分配人数
     * 
     * @param positionType 岗位类型
     * @return 已分配人数
     */
    Integer sumAllocatedCountByPositionType(@Param("positionType") Integer positionType);

    /**
     * 根据配置状态统计数量
     * 
     * @param allocationStatus 配置状态
     * @return 数量
     */
    Integer countByAllocationStatus(@Param("allocationStatus") Integer allocationStatus);

    /**
     * 查询超预算的人员配置列表
     * 
     * @return 超预算的人员配置列表
     */
    List<PersonnelAllocation> selectOverBudget();

    /**
     * 查询分配不足的人员配置列表（已分配人数 < 需求人数）
     * 
     * @return 分配不足的人员配置列表
     */
    List<PersonnelAllocation> selectUnderAllocated();

    /**
     * 批量更新配置状态
     * 
     * @param ids 配置ID列表
     * @param allocationStatus 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    Integer batchUpdateStatus(@Param("ids") List<Long> ids, @Param("allocationStatus") Integer allocationStatus, @Param("updateBy") Long updateBy);

    /**
     * 根据工作地点查询人员配置列表
     * 
     * @param workLocation 工作地点
     * @return 人员配置列表
     */
    List<PersonnelAllocation> selectByWorkLocation(@Param("workLocation") String workLocation);

    /**
     * 查询人员配置统计信息
     * 
     * @return 统计信息
     */
    List<PersonnelAllocation> selectAllocationStatistics();
}
