package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetDriverBased;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 驱动因子预算数据访问接口
 * 
 * @description 驱动因子预算数据访问层，提供驱动因子预算的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetDriverBasedMapper extends BaseMapper<BudgetDriverBased> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据驱动因子预算编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_DRIVER_BASED WHERE DRIVER_BUDGET_CODE = #{driverBudgetCode} AND IS_DELETED = 0")
    BudgetDriverBased selectByDriverBudgetCode(@Param("driverBudgetCode") String driverBudgetCode);

    /**
     * 根据驱动因子类型查询驱动因子预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_DRIVER_BASED WHERE DRIVER_TYPE = #{driverType} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetDriverBased> selectByDriverType(@Param("driverType") String driverType);

    /**
     * 根据驱动因子分类查询驱动因子预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_DRIVER_BASED WHERE DRIVER_CATEGORY = #{driverCategory} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetDriverBased> selectByDriverCategory(@Param("driverCategory") String driverCategory);

    /**
     * 根据预算状态查询驱动因子预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_DRIVER_BASED WHERE BUDGET_STATUS = #{budgetStatus} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetDriverBased> selectByBudgetStatus(@Param("budgetStatus") String budgetStatus);

    /**
     * 根据组织ID查询驱动因子预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_DRIVER_BASED WHERE ORGANIZATION_ID = #{organizationId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetDriverBased> selectByOrganizationId(@Param("organizationId") String organizationId);

    /**
     * 根据预算科目ID查询驱动因子预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_DRIVER_BASED WHERE BUDGET_ACCOUNT_ID = #{budgetAccountId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetDriverBased> selectByBudgetAccountId(@Param("budgetAccountId") String budgetAccountId);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询驱动因子预算
     */
    IPage<BudgetDriverBased> selectBudgetDriverBasedPage(Page<BudgetDriverBased> page, @Param("params") Map<String, Object> params);

    /**
     * 查询激活的驱动因子预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_DRIVER_BASED WHERE BUDGET_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetDriverBased> selectActiveBudgets();

    /**
     * 查询已批准的驱动因子预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_DRIVER_BASED WHERE APPROVAL_STATUS = 'APPROVED' AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetDriverBased> selectApprovedBudgets();

    /**
     * 查询自动更新的驱动因子预算列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_DRIVER_BASED WHERE IS_AUTO_UPDATE = 1 AND BUDGET_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY NEXT_UPDATE_TIME")
    List<BudgetDriverBased> selectAutoUpdateBudgets();

    /**
     * 查询需要更新的驱动因子预算列表
     */
    List<BudgetDriverBased> selectBudgetsToUpdate();

    // ==================== 业务操作方法 ====================

    /**
     * 激活预算
     */
    int activateBudget(@Param("budgetId") String budgetId, @Param("updateBy") String updateBy);

    /**
     * 停用预算
     */
    int deactivateBudget(@Param("budgetId") String budgetId, @Param("updateBy") String updateBy);

    /**
     * 审批通过
     */
    int approveBudget(@Param("budgetId") String budgetId, @Param("approvedBy") String approvedBy, @Param("approvalComments") String approvalComments);

    /**
     * 审批拒绝
     */
    int rejectBudget(@Param("budgetId") String budgetId, @Param("approvedBy") String approvedBy, @Param("approvalComments") String approvalComments);

    /**
     * 更新驱动因子数据
     */
    int updateDriverData(@Param("budgetId") String budgetId, 
                        @Param("driverQuantity") java.math.BigDecimal driverQuantity,
                        @Param("unitCost") java.math.BigDecimal unitCost,
                        @Param("budgetAmount") java.math.BigDecimal budgetAmount,
                        @Param("updateBy") String updateBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计驱动因子预算总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_DRIVER_BASED WHERE IS_DELETED = 0")
    int countTotalDriverBased();

    /**
     * 按驱动因子类型统计数量
     */
    List<Map<String, Object>> countByDriverType();

    /**
     * 按驱动因子分类统计数量
     */
    List<Map<String, Object>> countByDriverCategory();

    /**
     * 按预算状态统计数量
     */
    List<Map<String, Object>> countByBudgetStatus();

    /**
     * 统计预算金额汇总
     */
    Map<String, Object> sumBudgetAmounts(@Param("fiscalYear") Integer fiscalYear);

    // ==================== 数据验证方法 ====================

    /**
     * 检查驱动因子预算编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_DRIVER_BASED WHERE DRIVER_BUDGET_CODE = #{driverBudgetCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkDriverBudgetCodeExists(@Param("driverBudgetCode") String driverBudgetCode, @Param("excludeId") String excludeId);

    /**
     * 检查驱动因子预算是否可以删除
     */
    boolean checkDriverBudgetCanDelete(@Param("budgetId") String budgetId);

    /**
     * 检查驱动因子预算是否可以修改
     */
    boolean checkDriverBudgetCanModify(@Param("budgetId") String budgetId);
}
