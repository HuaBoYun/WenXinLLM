package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.PeriodSetting;

import java.util.List;

/**
 * 期间设置Service接口
 *
 * @author AI Agent
 * @date 2026-03-31
 */
public interface PeriodSettingService {
    PeriodSetting getSetting();
    PeriodSetting save(PeriodSetting setting);
    void reset();
}
