package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 存货计价服务接口
 * 
 * @author system
 * @since 2026-01-26
 */
public interface InventoryValuationService {

    /**
     * 分页查询存货计价列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getValuationPage(Map<String, Object> param);

    /**
     * 根据ID查询存货计价详情
     * 
     * @param valuationId 计价ID
     * @return 计价详情
     */
    Map<String, Object> getValuationById(Long valuationId);

    /**
     * 保存或更新存货计价方法
     * 
     * @param param 计价参数
     * @return 是否成功
     */
    boolean saveOrUpdateValuation(Map<String, Object> param);

    /**
     * 删除存货计价方法
     * 
     * @param valuationId 计价ID
     * @return 是否成功
     */
    boolean deleteValuation(Long valuationId);

    /**
     * 批量更新存货计价方法
     * 
     * @param param 更新参数
     * @return 是否成功
     */
    boolean batchUpdateValuation(Map<String, Object> param);

    /**
     * 批量删除存货计价方法
     * 
     * @param valuationIds 计价ID列表
     * @return 是否成功
     */
    boolean batchDeleteValuation(List<Long> valuationIds);

    /**
     * 计算存货成本
     * 
     * @param valuationId 计价ID
     * @return 计算结果
     */
    Map<String, Object> calculateCost(Long valuationId);

    /**
     * 获取仓库列表
     * 
     * @return 仓库列表
     */
    List<Map<String, Object>> getWarehouseList();

    /**
     * 导出存货计价数据
     * 
     * @param param 查询参数
     * @return 导出文件路径
     */
    String exportValuation(Map<String, Object> param);
}

