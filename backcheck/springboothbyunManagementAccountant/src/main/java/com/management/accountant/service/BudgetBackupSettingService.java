package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetBackupSetting;

import java.util.List;

/**
 * 预算备份设置服务接口
 */
public interface BudgetBackupSettingService {

    /**
     * 获取所有设置
     */
    List<BudgetBackupSetting> getAllSettings();

    /**
     * 保存或更新设置
     */
    BudgetBackupSetting saveSetting(BudgetBackupSetting setting);

    /**
     * 批量更新设置
     */
    void batchUpdate(List<BudgetBackupSetting> settings);
}
