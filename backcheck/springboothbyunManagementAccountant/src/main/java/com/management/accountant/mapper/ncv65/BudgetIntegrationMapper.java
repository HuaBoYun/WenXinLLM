package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetIntegration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 系统集成数据访问接口
 * 
 * @description 系统集成数据访问层，提供系统集成的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetIntegrationMapper extends BaseMapper<BudgetIntegration> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据集成编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_INTEGRATION WHERE INTEGRATION_CODE = #{integrationCode} AND IS_DELETED = 0")
    BudgetIntegration selectByIntegrationCode(@Param("integrationCode") String integrationCode);

    /**
     * 根据集成类型查询集成列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_INTEGRATION WHERE INTEGRATION_TYPE = #{integrationType} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetIntegration> selectByIntegrationType(@Param("integrationType") String integrationType);

    /**
     * 根据集成方式查询集成列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_INTEGRATION WHERE INTEGRATION_METHOD = #{integrationMethod} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetIntegration> selectByIntegrationMethod(@Param("integrationMethod") String integrationMethod);

    /**
     * 根据集成状态查询集成列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_INTEGRATION WHERE INTEGRATION_STATUS = #{integrationStatus} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetIntegration> selectByIntegrationStatus(@Param("integrationStatus") String integrationStatus);

    /**
     * 根据同步频率查询集成列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_INTEGRATION WHERE SYNC_FREQUENCY = #{syncFrequency} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetIntegration> selectBySyncFrequency(@Param("syncFrequency") String syncFrequency);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询系统集成
     */
    IPage<BudgetIntegration> selectBudgetIntegrationPage(Page<BudgetIntegration> page, @Param("params") Map<String, Object> params);

    /**
     * 查询激活的集成列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_INTEGRATION WHERE INTEGRATION_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetIntegration> selectActiveIntegrations();

    /**
     * 查询需要同步的集成列表
     */
    List<BudgetIntegration> selectIntegrationsToSync();

    /**
     * 查询错误的集成列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_INTEGRATION WHERE INTEGRATION_STATUS = 'ERROR' AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetIntegration> selectErrorIntegrations();

    // ==================== 业务操作方法 ====================

    /**
     * 激活集成
     */
    int activateIntegration(@Param("integrationId") String integrationId, @Param("updateBy") String updateBy);

    /**
     * 停用集成
     */
    int deactivateIntegration(@Param("integrationId") String integrationId, @Param("updateBy") String updateBy);

    /**
     * 更新同步状态
     */
    int updateSyncStatus(@Param("integrationId") String integrationId, 
                        @Param("syncRecordCount") Integer syncRecordCount,
                        @Param("errorMessage") String errorMessage,
                        @Param("updateBy") String updateBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计集成总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_INTEGRATION WHERE IS_DELETED = 0")
    int countTotalIntegrations();

    /**
     * 按集成类型统计数量
     */
    List<Map<String, Object>> countByIntegrationType();

    /**
     * 按集成状态统计数量
     */
    List<Map<String, Object>> countByIntegrationStatus();

    // ==================== 数据验证方法 ====================

    /**
     * 检查集成编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_INTEGRATION WHERE INTEGRATION_CODE = #{integrationCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkIntegrationCodeExists(@Param("integrationCode") String integrationCode, @Param("excludeId") String excludeId);

    /**
     * 检查集成是否可以删除
     */
    boolean checkIntegrationCanDelete(@Param("integrationId") String integrationId);
}
