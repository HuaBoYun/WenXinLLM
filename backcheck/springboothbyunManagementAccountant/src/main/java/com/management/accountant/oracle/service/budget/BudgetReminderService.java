package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Reminder Service接口
 * 
 * @description Reminder业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetReminderService {

    /**
     * getFormulaDependencies
     */
    Map<String, Object> getFormulaDependencies(Map<String, Object> params, String companyId, String userId);

    /**
     * getReminderStrategy
     */
    Map<String, Object> getReminderStrategy(Map<String, Object> params, String companyId, String userId);

    /**
     * executeReminder
     */
    Map<String, Object> executeReminder(Map<String, Object> params, String companyId, String userId);

}
