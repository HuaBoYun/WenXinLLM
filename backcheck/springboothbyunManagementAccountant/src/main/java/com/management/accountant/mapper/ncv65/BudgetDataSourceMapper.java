package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 数据源数据访问接口
 * 
 * @description 数据源数据访问层，提供数据源的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetDataSourceMapper extends BaseMapper<BudgetDataSource> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据数据源编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_DATA_SOURCE WHERE DATA_SOURCE_CODE = #{dataSourceCode} AND IS_DELETED = 0")
    BudgetDataSource selectByDataSourceCode(@Param("dataSourceCode") String dataSourceCode);

    /**
     * 根据数据源类型查询数据源列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_DATA_SOURCE WHERE DATA_SOURCE_TYPE = #{dataSourceType} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetDataSource> selectByDataSourceType(@Param("dataSourceType") String dataSourceType);

    /**
     * 根据数据库类型查询数据源列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_DATA_SOURCE WHERE DATABASE_TYPE = #{databaseType} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetDataSource> selectByDatabaseType(@Param("databaseType") String databaseType);

    /**
     * 根据数据源状态查询数据源列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_DATA_SOURCE WHERE DATA_SOURCE_STATUS = #{dataSourceStatus} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetDataSource> selectByDataSourceStatus(@Param("dataSourceStatus") String dataSourceStatus);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询数据源
     */
    IPage<BudgetDataSource> selectBudgetDataSourcePage(Page<BudgetDataSource> page, @Param("params") Map<String, Object> params);

    /**
     * 查询激活的数据源列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_DATA_SOURCE WHERE DATA_SOURCE_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetDataSource> selectActiveDataSources();

    /**
     * 查询错误的数据源列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_DATA_SOURCE WHERE DATA_SOURCE_STATUS = 'ERROR' AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetDataSource> selectErrorDataSources();

    // ==================== 业务操作方法 ====================

    /**
     * 激活数据源
     */
    int activateDataSource(@Param("dataSourceId") String dataSourceId, @Param("updateBy") String updateBy);

    /**
     * 停用数据源
     */
    int deactivateDataSource(@Param("dataSourceId") String dataSourceId, @Param("updateBy") String updateBy);

    /**
     * 更新连接测试结果
     */
    int updateConnectionTestResult(@Param("dataSourceId") String dataSourceId, 
                                  @Param("connectionTestResult") String connectionTestResult,
                                  @Param("updateBy") String updateBy);

    // ==================== 统计分析方法 ====================

    /**
     * 统计数据源总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_DATA_SOURCE WHERE IS_DELETED = 0")
    int countTotalDataSources();

    /**
     * 按数据源类型统计数量
     */
    List<Map<String, Object>> countByDataSourceType();

    /**
     * 按数据源状态统计数量
     */
    List<Map<String, Object>> countByDataSourceStatus();

    // ==================== 数据验证方法 ====================

    /**
     * 检查数据源编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_DATA_SOURCE WHERE DATA_SOURCE_CODE = #{dataSourceCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkDataSourceCodeExists(@Param("dataSourceCode") String dataSourceCode, @Param("excludeId") String excludeId);

    /**
     * 检查数据源是否可以删除
     */
    boolean checkDataSourceCanDelete(@Param("dataSourceId") String dataSourceId);
}
