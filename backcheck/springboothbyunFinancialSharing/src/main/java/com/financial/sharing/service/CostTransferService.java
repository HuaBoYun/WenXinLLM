package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 成本结转服务接口
 * 
 * @author system
 * @since 2026-01-26
 */
public interface CostTransferService {

    /**
     * 分页查询成本结转列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getTransferPage(Map<String, Object> param);

    /**
     * 根据ID查询成本结转详情
     * 
     * @param transferId 结转ID
     * @return 结转详情
     */
    Map<String, Object> getTransferById(Long transferId);

    /**
     * 创建成本结转单
     * 
     * @param param 结转参数
     * @return 是否成功
     */
    boolean createTransfer(Map<String, Object> param);

    /**
     * 执行成本结转
     * 
     * @param transferId 结转ID
     * @return 是否成功
     */
    boolean executeTransfer(Long transferId);

    /**
     * 撤销成本结转
     * 
     * @param transferId 结转ID
     * @return 是否成功
     */
    boolean revokeTransfer(Long transferId);

    /**
     * 批量执行成本结转
     * 
     * @param transferIds 结转ID列表
     * @return 是否成功
     */
    boolean batchExecuteTransfer(List<Long> transferIds);

    /**
     * 批量撤销成本结转
     * 
     * @param transferIds 结转ID列表
     * @return 是否成功
     */
    boolean batchRevokeTransfer(List<Long> transferIds);

    /**
     * 获取成本结转统计
     * 
     * @param param 查询参数
     * @return 统计结果
     */
    Map<String, Object> getTransferStatistics(Map<String, Object> param);

    /**
     * 导出成本结转数据
     * 
     * @param param 查询参数
     * @return 导出文件路径
     */
    String exportTransfer(Map<String, Object> param);
}

