package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetCustomization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - BudgetCustomization数据访问接口
 * 
 * @description BudgetCustomization数据访问层，提供BudgetCustomization的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetCustomizationMapper extends BaseMapper<BudgetCustomization> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据编码查询
     */
    @Select("SELECT * FROM ${tableName} WHERE CODE = #{code} AND IS_DELETED = 0")
    BudgetCustomization selectByCode(@Param("code") String code);

    /**
     * 根据名称查询
     */
    @Select("SELECT * FROM ${tableName} WHERE NAME = #{name} AND IS_DELETED = 0")
    List<BudgetCustomization> selectByName(@Param("name") String name);

    /**
     * 根据状态查询
     */
    @Select("SELECT * FROM ${tableName} WHERE STATUS = #{status} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetCustomization> selectByStatus(@Param("status") String status);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询
     */
    IPage<BudgetCustomization> selectBudgetCustomizationPage(Page<BudgetCustomization> page, @Param("params") Map<String, Object> params);

    /**
     * 查询激活的记录列表
     */
    @Select("SELECT * FROM ${tableName} WHERE STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetCustomization> selectActiveRecords();

    // ==================== 业务操作方法 ====================

    /**
     * 激活记录
     */
    int activateRecord(@Param("id") String id, @Param("updateBy") String updateBy);

    /**
     * 停用记录
     */
    int deactivateRecord(@Param("id") String id, @Param("updateBy") String updateBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计总数
     */
    @Select("SELECT COUNT(*) FROM ${tableName} WHERE IS_DELETED = 0")
    int countTotal();

    /**
     * 按状态统计数量
     */
    List<Map<String, Object>> countByStatus();

    // ==================== 数据验证方法 ====================

    /**
     * 检查编码是否存在
     */
    @Select("SELECT COUNT(*) FROM ${tableName} WHERE CODE = #{code} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkCodeExists(@Param("code") String code, @Param("excludeId") String excludeId);

    /**
     * 检查记录是否可以删除
     */
    boolean checkCanDelete(@Param("id") String id);
}
