package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetExcelClient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - Excel客户端数据访问接口
 * 
 * @description Excel客户端数据访问层，提供Excel客户端的数据库操作方法
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetExcelClientMapper extends BaseMapper<BudgetExcelClient> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据客户端编码查询
     */
    @Select("SELECT * FROM NCV65_BUDGET_EXCEL_CLIENT WHERE CLIENT_CODE = #{clientCode} AND IS_DELETED = 0")
    BudgetExcelClient selectByClientCode(@Param("clientCode") String clientCode);

    /**
     * 根据用户ID查询客户端列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_EXCEL_CLIENT WHERE USER_ID = #{userId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetExcelClient> selectByUserId(@Param("userId") String userId);

    /**
     * 根据机器码查询客户端列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_EXCEL_CLIENT WHERE MACHINE_CODE = #{machineCode} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetExcelClient> selectByMachineCode(@Param("machineCode") String machineCode);

    /**
     * 根据客户端状态查询客户端列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_EXCEL_CLIENT WHERE CLIENT_STATUS = #{clientStatus} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetExcelClient> selectByClientStatus(@Param("clientStatus") String clientStatus);

    /**
     * 根据在线状态查询客户端列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_EXCEL_CLIENT WHERE ONLINE_STATUS = #{onlineStatus} AND IS_DELETED = 0 ORDER BY LAST_ACTIVITY_TIME DESC")
    List<BudgetExcelClient> selectByOnlineStatus(@Param("onlineStatus") String onlineStatus);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询Excel客户端
     */
    IPage<BudgetExcelClient> selectBudgetExcelClientPage(Page<BudgetExcelClient> page, @Param("params") Map<String, Object> params);

    /**
     * 查询激活的客户端列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_EXCEL_CLIENT WHERE CLIENT_STATUS = 'ACTIVE' AND IS_DELETED = 0 ORDER BY LAST_ACTIVITY_TIME DESC")
    List<BudgetExcelClient> selectActiveClients();

    /**
     * 查询在线的客户端列表
     */
    @Select("SELECT * FROM NCV65_BUDGET_EXCEL_CLIENT WHERE ONLINE_STATUS = 'ONLINE' AND IS_DELETED = 0 ORDER BY LAST_ACTIVITY_TIME DESC")
    List<BudgetExcelClient> selectOnlineClients();

    /**
     * 查询即将过期的客户端列表
     */
    List<BudgetExcelClient> selectExpiringClients(@Param("days") Integer days);

    // ==================== 业务操作方法 ====================

    /**
     * 激活客户端
     */
    int activateClient(@Param("clientId") String clientId, @Param("updateBy") String updateBy);

    /**
     * 停用客户端
     */
    int deactivateClient(@Param("clientId") String clientId, @Param("updateBy") String updateBy);

    /**
     * 锁定客户端
     */
    int lockClient(@Param("clientId") String clientId, @Param("updateBy") String updateBy);

    /**
     * 更新在线状态
     */
    int updateOnlineStatus(@Param("clientId") String clientId, @Param("onlineStatus") String onlineStatus);

    /**
     * 更新最后活动时间
     */
    int updateLastActivityTime(@Param("clientId") String clientId);

    // ==================== 统计分析方法 ====================

    /**
     * 统计客户端总数
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_EXCEL_CLIENT WHERE IS_DELETED = 0")
    int countTotalClients();

    /**
     * 按客户端状态统计数量
     */
    List<Map<String, Object>> countByClientStatus();

    /**
     * 按在线状态统计数量
     */
    List<Map<String, Object>> countByOnlineStatus();

    /**
     * 统计在线客户端数量
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_EXCEL_CLIENT WHERE ONLINE_STATUS = 'ONLINE' AND IS_DELETED = 0")
    int countOnlineClients();

    // ==================== 数据验证方法 ====================

    /**
     * 检查客户端编码是否存在
     */
    @Select("SELECT COUNT(*) FROM NCV65_BUDGET_EXCEL_CLIENT WHERE CLIENT_CODE = #{clientCode} AND ID != #{excludeId} AND IS_DELETED = 0")
    int checkClientCodeExists(@Param("clientCode") String clientCode, @Param("excludeId") String excludeId);

    /**
     * 检查客户端是否可以删除
     */
    boolean checkClientCanDelete(@Param("clientId") String clientId);
}
