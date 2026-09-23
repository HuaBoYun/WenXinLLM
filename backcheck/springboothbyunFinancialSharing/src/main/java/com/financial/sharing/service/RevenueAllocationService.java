package com.financial.sharing.service;

import com.financial.sharing.util.PageableParam;

import java.util.Map;

/**
 * 收入分配Service接口
 */
public interface RevenueAllocationService {

    /**
     * 分页查询收入分配列表
     *
     * @param param 分页参数
     * @return 分页结果
     */
    Map<String, Object> getAllocationList(PageableParam param);

    /**
     * 创建收入分配
     *
     * @param allocationData 分配数据
     * @return 创建结果
     */
    Map<String, Object> createAllocation(Map<String, Object> allocationData);

    /**
     * 更新收入分配
     *
     * @param allocationId 分配ID
     * @param allocationData 分配数据
     * @return 更新结果
     */
    Map<String, Object> updateAllocation(Long allocationId, Map<String, Object> allocationData);

    /**
     * 删除收入分配
     *
     * @param allocationId 分配ID
     * @return 删除结果
     */
    Map<String, Object> deleteAllocation(Long allocationId);

    /**
     * 获取收入分配统计
     *
     * @param tenantId 租户ID
     * @return 统计结果
     */
    Map<String, Object> getAllocationStats(Long tenantId);
}

