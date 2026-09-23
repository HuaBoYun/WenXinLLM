package com.global.treasurer.service;

import com.global.treasurer.entity.BankReconciliation;

import java.util.List;
import java.util.Map;

/**
 * 银行对账服务接口
 *
 * @author AI Developer
 * @date 2025-01-15
 */
public interface BankReconciliationService {

    /**
     * 分页查询银行对账列表
     */
    Map<String, Object> getReconciliationPage(Map<String, Object> param);

    /**
     * 根据ID查询银行对账详情
     */
    BankReconciliation getReconciliationById(Long reconciliationId);

    /**
     * 创建银行对账单
     */
    int createReconciliation(BankReconciliation reconciliation);

    /**
     * 更新银行对账单
     */
    int updateReconciliation(BankReconciliation reconciliation);

    /**
     * 批量删除银行对账单
     */
    int batchDelete(List<Long> ids);

    /**
     * 执行银行对账
     */
    int executeReconciliation(Long reconciliationId, String executeByName) throws Exception;

    /**
     * 生成对账单号
     */
    String generateReconciliationNo(Long orgId);

    /**
     * 获取对账概览统计
     */
    Map<String, Object> getReconciliationOverview(Map<String, Object> params);
}
