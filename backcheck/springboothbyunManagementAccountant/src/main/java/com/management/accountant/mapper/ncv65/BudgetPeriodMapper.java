package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetPeriod;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算期间数据访问接口
 * 
 * @description 预算期间数据访问层，提供预算期间的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetPeriodMapper extends BaseMapper<BudgetPeriod> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据期间编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_PERIOD WHERE PERIOD_CODE = #{periodCode} AND IS_DELETED = 0")
    BudgetPeriod selectByPeriodCode(@Param("periodCode") String periodCode);

    /**
     * 根据预算年度查询期间列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PERIOD WHERE FISCAL_YEAR = #{fiscalYear} AND IS_DELETED = 0 ORDER BY PERIOD_NUMBER")
    List<BudgetPeriod> selectByFiscalYear(@Param("fiscalYear") Integer fiscalYear);

    /**
     * 根据期间类型查询期间列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PERIOD WHERE PERIOD_TYPE = #{periodType} AND IS_DELETED = 0 ORDER BY FISCAL_YEAR, PERIOD_NUMBER")
    List<BudgetPeriod> selectByPeriodType(@Param("periodType") String periodType);

    /**
     * 根据期间状态查询期间列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PERIOD WHERE PERIOD_STATUS = #{periodStatus} AND IS_DELETED = 0 ORDER BY FISCAL_YEAR, PERIOD_NUMBER")
    List<BudgetPeriod> selectByPeriodStatus(@Param("periodStatus") String periodStatus);

    /**
     * 根据父期间ID查询子期间列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PERIOD WHERE PARENT_PERIOD_ID = #{parentPeriodId} AND IS_DELETED = 0 ORDER BY PERIOD_NUMBER")
    List<BudgetPeriod> selectByParentPeriodId(@Param("parentPeriodId") String parentPeriodId);

    /**
     * 根据期间层级查询期间列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PERIOD WHERE PERIOD_LEVEL = #{periodLevel} AND IS_DELETED = 0 ORDER BY FISCAL_YEAR, PERIOD_NUMBER")
    List<BudgetPeriod> selectByPeriodLevel(@Param("periodLevel") Integer periodLevel);

    /**
     * 查询当前期间
     */
    @Select("SELECT * FROM NCV65_BUDGET_PERIOD WHERE IS_CURRENT = 1 AND IS_DELETED = 0")
    BudgetPeriod selectCurrentPeriod();

    /**
     * 根据日期查询所属期间
     */
    @Select("SELECT * FROM NCV65_BUDGET_PERIOD WHERE #{date} BETWEEN START_DATE AND END_DATE AND IS_DELETED = 0")
    List<BudgetPeriod> selectByDate(@Param("date") LocalDateTime date);

    /**
     * 根据日期范围查询期间列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PERIOD WHERE START_DATE <= #{endDate} AND END_DATE >= #{startDate} AND IS_DELETED = 0 ORDER BY START_DATE")
    List<BudgetPeriod> selectByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算期间
     */
    IPage<BudgetPeriod> selectBudgetPeriodPage(Page<BudgetPeriod> page, @Param("params") Map<String, Object> params);

    /**
     * 查询期间树形结构
     */
    List<BudgetPeriod> selectPeriodTree(@Param("fiscalYear") Integer fiscalYear);

    /**
     * 查询激活的期间列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PERIOD WHERE PERIOD_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY FISCAL_YEAR, PERIOD_NUMBER")
    List<BudgetPeriod> selectActivePeriods();

    /**
     * 查询可编制预算的期间列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PERIOD WHERE ALLOW_BUDGET_PREPARATION = 1 AND PERIOD_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY FISCAL_YEAR, PERIOD_NUMBER")
    List<BudgetPeriod> selectBudgetPreparationPeriods();

    /**
     * 查询可调整预算的期间列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PERIOD WHERE ALLOW_BUDGET_ADJUSTMENT = 1 AND PERIOD_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY FISCAL_YEAR, PERIOD_NUMBER")
    List<BudgetPeriod> selectBudgetAdjustmentPeriods();

    /**
     * 查询可录入数据的期间列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_PERIOD WHERE ALLOW_DATA_ENTRY = 1 AND PERIOD_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY FISCAL_YEAR, PERIOD_NUMBER")
    List<BudgetPeriod> selectDataEntryPeriods();

    /**
     * 查询期间层次结构
     */
    List<BudgetPeriod> selectPeriodHierarchy(@Param("rootPeriodId") String rootPeriodId);

    /**
     * 查询期间及其所有子期间
     */
    List<BudgetPeriod> selectPeriodWithChildren(@Param("periodId") String periodId);

    /**
     * 查询期间及其所有父期间
     */
    List<BudgetPeriod> selectPeriodWithParents(@Param("periodId") String periodId);

    /**
     * 查询重叠的期间
     */
    List<BudgetPeriod> selectOverlappingPeriods(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("excludeId") String excludeId);

    /**
     * 查询相邻的期间
     */
    List<BudgetPeriod> selectAdjacentPeriods(@Param("periodId") String periodId);

    /**
     * 查询期间统计信息
     */
    Map<String, Object> selectPeriodStatistics(@Param("fiscalYear") Integer fiscalYear);

    // ==================== 业务操作方法 ====================

    /**
     * 激活期间
     */
    int activatePeriod(@Param("periodId") String periodId, @Param("updateBy") String updateBy);

    /**
     * 关闭期间
     */
    int closePeriod(@Param("periodId") String periodId, @Param("updateBy") String updateBy);

    /**
     * 锁定期间
     */
    int lockPeriod(@Param("periodId") String periodId, @Param("updateBy") String updateBy);

    /**
     * 解锁期间
     */
    int unlockPeriod(@Param("periodId") String periodId, @Param("updateBy") String updateBy);

    /**
     * 设置当前期间
     */
    int setCurrentPeriod(@Param("periodId") String periodId, @Param("updateBy") String updateBy);

    /**
     * 清除当前期间标记
     */
    int clearCurrentPeriod(@Param("updateBy") String updateBy);

    /**
     * 更新期间权限
     */
    int updatePeriodPermissions(@Param("periodId") String periodId, 
                               @Param("allowBudgetPreparation") Boolean allowBudgetPreparation,
                               @Param("allowBudgetAdjustment") Boolean allowBudgetAdjustment,
                               @Param("allowDataEntry") Boolean allowDataEntry,
                               @Param("allowDataQuery") Boolean allowDataQuery,
                               @Param("updateBy") String updateBy);

    /**
     * 批量更新期间状态
     */
    int batchUpdatePeriodStatus(@Param("periodIds") List<String> periodIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量激活期间
     */
    int batchActivatePeriods(@Param("periodIds") List<String> periodIds, @Param("updateBy") String updateBy);

    /**
     * 批量关闭期间
     */
    int batchClosePeriods(@Param("periodIds") List<String> periodIds, @Param("updateBy") String updateBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计期间总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PERIOD WHERE IS_DELETED = 0")
    int countTotalPeriods();

    /**
     * 按期间类型统计数量
     */
    List<Map<String, Object>> countPeriodsByType();

    /**
     * 按期间状态统计数量
     */
    List<Map<String, Object>> countPeriodsByStatus();

    /**
     * 按预算年度统计数量
     */
    List<Map<String, Object>> countPeriodsByFiscalYear();

    /**
     * 按期间层级统计数量
     */
    List<Map<String, Object>> countPeriodsByLevel();

    /**
     * 统计激活期间数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PERIOD WHERE PERIOD_STATUS = 'ACTIVE' AND IS_DELETED = 0")
    int countActivePeriods();

    /**
     * 统计关闭期间数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PERIOD WHERE PERIOD_STATUS = 'CLOSED' AND IS_DELETED = 0")
    int countClosedPeriods();

    /**
     * 统计锁定期间数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PERIOD WHERE PERIOD_STATUS = 'LOCKED' AND IS_DELETED = 0")
    int countLockedPeriods();

    // ==================== 数据验证方法 ====================

    /**
     * 检查期间编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PERIOD WHERE PERIOD_CODE = #{periodCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkPeriodCodeExists(@Param("periodCode") String periodCode, @Param("excludeId") String excludeId);

    /**
     * 检查期间名称是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PERIOD WHERE PERIOD_NAME = #{periodName} AND FISCAL_YEAR = #{fiscalYear} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkPeriodNameExists(@Param("periodName") String periodName, @Param("fiscalYear") Integer fiscalYear, @Param("excludeId") String excludeId);

    /**
     * 检查期间是否有重叠
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PERIOD WHERE START_DATE < #{endDate} AND END_DATE > #{startDate} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkPeriodOverlap(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("excludeId") String excludeId);

    /**
     * 检查是否存在当前期间
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_PERIOD WHERE IS_CURRENT = 1 AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkCurrentPeriodExists(@Param("excludeId") String excludeId);

    /**
     * 检查期间是否可以删除
     */
    boolean checkPeriodCanDelete(@Param("periodId") String periodId);

    /**
     * 检查期间是否可以修改
     */
    boolean checkPeriodCanModify(@Param("periodId") String periodId);

    /**
     * 验证期间层次结构
     */
    boolean validatePeriodHierarchy(@Param("periodId") String periodId, @Param("parentPeriodId") String parentPeriodId);
}
