package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 存货盘点服务接口
 * 
 * @author system
 * @since 2026-01-26
 */
public interface InventoryCheckService {

    /**
     * 分页查询存货盘点列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getCheckPage(Map<String, Object> param);

    /**
     * 根据ID查询存货盘点详情
     * 
     * @param checkId 盘点ID
     * @return 盘点详情
     */
    Map<String, Object> getCheckById(Long checkId);

    /**
     * 创建存货盘点任务
     * 
     * @param param 盘点参数
     * @return 是否成功
     */
    boolean createCheck(Map<String, Object> param);

    /**
     * 执行存货盘点
     * 
     * @param checkId 盘点ID
     * @return 是否成功
     */
    boolean executeCheck(Long checkId);

    /**
     * 提交盘点结果
     * 
     * @param checkId 盘点ID
     * @param results 盘点结果列表
     * @return 是否成功
     */
    boolean submitCheckResults(Long checkId, List<Map<String, Object>> results);

    /**
     * 审批盘点结果
     * 
     * @param checkId 盘点ID
     * @param param 审批参数
     * @return 是否成功
     */
    boolean approveCheck(Long checkId, Map<String, Object> param);

    /**
     * 获取盘点结果列表
     * 
     * @param checkId 盘点ID
     * @return 结果列表
     */
    List<Map<String, Object>> getCheckResults(Long checkId);

    /**
     * 处理盘点差异
     * 
     * @param param 处理参数
     * @return 是否成功
     */
    boolean processDifference(Map<String, Object> param);

    /**
     * 批量执行盘点任务
     * 
     * @param checkIds 盘点ID列表
     * @return 是否成功
     */
    boolean batchExecuteCheck(List<Long> checkIds);

    /**
     * 获取存货盘点统计
     * 
     * @param param 查询参数
     * @return 统计结果
     */
    Map<String, Object> getCheckStatistics(Map<String, Object> param);

    /**
     * 导出盘点任务
     * 
     * @param param 查询参数
     * @return 导出文件路径
     */
    String exportCheck(Map<String, Object> param);

    /**
     * 生成盘点报告
     * 
     * @param checkId 盘点ID
     * @return 报告文件路径
     */
    String generateCheckReport(Long checkId);
}

