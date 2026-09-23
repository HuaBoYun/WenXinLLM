package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 库存预警服务接口
 * 
 * @author system
 * @since 2026-01-26
 */
public interface InventoryAlertService {

    /**
     * 获取预警统计
     * 
     * @param param 查询参数
     * @return 统计结果
     */
    Map<String, Object> getAlertStatistics(Map<String, Object> param);

    /**
     * 分页查询预警列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getAlertPage(Map<String, Object> param);

    /**
     * 处理预警
     * 
     * @param alertId 预警ID
     * @param param 处理参数
     * @return 是否成功
     */
    boolean processAlert(Long alertId, Map<String, Object> param);

    /**
     * 忽略预警
     * 
     * @param alertId 预警ID
     * @return 是否成功
     */
    boolean ignoreAlert(Long alertId);

    /**
     * 批量处理预警
     * 
     * @param alertIds 预警ID列表
     * @param param 处理参数
     * @return 是否成功
     */
    boolean batchProcessAlert(List<Long> alertIds, Map<String, Object> param);

    /**
     * 分页查询预警规则列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getRulePage(Map<String, Object> param);

    /**
     * 保存或更新预警规则
     * 
     * @param param 规则参数
     * @return 是否成功
     */
    boolean saveOrUpdateRule(Map<String, Object> param);

    /**
     * 删除预警规则
     * 
     * @param ruleId 规则ID
     * @return 是否成功
     */
    boolean deleteRule(Long ruleId);

    /**
     * 启用/禁用预警规则
     * 
     * @param ruleId 规则ID
     * @param status 状态
     * @return 是否成功
     */
    boolean toggleRuleStatus(Long ruleId, Integer status);

    /**
     * 批量启用/禁用规则
     * 
     * @param ruleIds 规则ID列表
     * @param status 状态
     * @return 是否成功
     */
    boolean batchToggleRuleStatus(List<Long> ruleIds, Integer status);

    /**
     * 导出预警数据
     * 
     * @param param 查询参数
     * @return 导出文件路径
     */
    String exportAlert(Map<String, Object> param);

    /**
     * 获取预警趋势分析
     * 
     * @param param 查询参数
     * @return 趋势数据
     */
    List<Map<String, Object>> getAlertTrend(Map<String, Object> param);

    /**
     * 获取预警类型分布
     * 
     * @param param 查询参数
     * @return 分布数据
     */
    List<Map<String, Object>> getAlertDistribution(Map<String, Object> param);
}

