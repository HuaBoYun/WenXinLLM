package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Transfer Service接口
 * 
 * @description Transfer业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetTransferService {

    /**
     * create
     */
    Map<String, Object> create(Map<String, Object> params, String companyId, String userId);

    /**
     * update
     */
    Map<String, Object> update(Map<String, Object> params, String companyId, String userId);

    /**
     * transfer
     */
    Map<String, Object> transfer(Map<String, Object> params, String companyId, String userId);

}
