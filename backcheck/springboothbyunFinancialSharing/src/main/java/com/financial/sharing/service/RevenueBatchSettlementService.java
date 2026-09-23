package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.RevenueBatchSettlementQueryParam;

import java.util.Map;

/**
 * 收入批量结账Service接口
 * 
 * @author AI Agent
 * @since 2025-11-29
 */
public interface RevenueBatchSettlementService {
    
    /**
     * 分页查询收入批量结账列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getSettlementPage(RevenueBatchSettlementQueryParam param);
    
    /**
     * 根据ID查询结账详情
     * 
     * @param settlementId 结账ID
     * @return 结账详情
     */
    Map<String, Object> getSettlementById(Long settlementId);
    
    /**
     * 执行结账
     * 
     * @param settlementId 结账ID
     * @param userId 用户ID
     * @param userName 用户姓名
     * @return 是否成功
     */
    boolean doSettlement(Long settlementId, Long userId, String userName);
    
    /**
     * 取消结账
     * 
     * @param settlementId 结账ID
     * @param userId 用户ID
     * @param userName 用户姓名
     * @return 是否成功
     */
    boolean cancelSettlement(Long settlementId, Long userId, String userName);
}

