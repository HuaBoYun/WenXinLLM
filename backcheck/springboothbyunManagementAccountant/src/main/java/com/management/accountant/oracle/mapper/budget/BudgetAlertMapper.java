package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetAlert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算警报Mapper
 * 
 * @description 预算警报数据访问接口
 * @author AI Agent
 * @date 2026-02-09
 */
@Mapper
public interface BudgetAlertMapper extends BaseMapper<BudgetAlert> {

    /**
     * 根据警报编码查询警报
     * 
     * @param alertCode 警报编码
     * @return 警报对象
     */
    BudgetAlert selectByAlertCode(@Param("alertCode") String alertCode);

    /**
     * 根据预算ID查询警报列表
     * 
     * @param budgetId 预算ID
     * @return 警报列表
     */
    List<BudgetAlert> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据警报状态查询警报列表
     * 
     * @param alertStatus 警报状态
     * @return 警报列表
     */
    List<BudgetAlert> selectByAlertStatus(@Param("alertStatus") String alertStatus);

    /**
     * 根据警报级别查询警报列表
     * 
     * @param alertLevel 警报级别
     * @return 警报列表
     */
    List<BudgetAlert> selectByAlertLevel(@Param("alertLevel") String alertLevel);

    /**
     * 查询待处理警报列表
     * 
     * @return 待处理警报列表
     */
    List<BudgetAlert> selectPendingAlerts();

    /**
     * 查询用户相关警报
     * 
     * @param userId 用户ID
     * @return 警报列表
     */
    List<BudgetAlert> selectByUserId(@Param("userId") String userId);

    /**
     * 查询过期警报
     * 
     * @return 过期警报列表
     */
    List<BudgetAlert> selectExpiredAlerts();

    /**
     * 批量更新警报状态
     * 
     * @param alertIds 警报ID列表
     * @param alertStatus 目标状态
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("alertIds") List<String> alertIds, @Param("alertStatus") String alertStatus);

    /**
     * 批量删除警报
     * 
     * @param alertIds 警报ID列表
     * @return 删除数量
     */
    int batchDeleteAlerts(@Param("alertIds") List<String> alertIds);

    /**
     * 统计各状态警报数量
     * 
     * @param companyId 公司ID
     * @return 统计结果
     */
    List<Map<String, Object>> countByStatus(@Param("companyId") String companyId);

    /**
     * 统计各级别警报数量
     * 
     * @param companyId 公司ID
     * @return 统计结果
     */
    List<Map<String, Object>> countByLevel(@Param("companyId") String companyId);

    /**
     * 统计各类型警报数量
     * 
     * @param companyId 公司ID
     * @return 统计结果
     */
    List<Map<String, Object>> countByType(@Param("companyId") String companyId);
}

