package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetBackupSetting;
import com.management.accountant.oracle.mapper.budget.BudgetBackupSettingMapper;
import com.management.accountant.service.BudgetBackupSettingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 预算备份设置服务实现
 */
@Service
public class BudgetBackupSettingServiceImpl implements BudgetBackupSettingService {

    @Resource
    private BudgetBackupSettingMapper settingMapper;

    @Override
    public List<BudgetBackupSetting> getAllSettings() {
        QueryWrapper<BudgetBackupSetting> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        wrapper.orderByAsc("SETTING_GROUP");
        wrapper.orderByAsc("SETTING_KEY");
        return settingMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetBackupSetting saveSetting(BudgetBackupSetting setting) {
        if (setting == null || !StringUtils.hasText(setting.getSettingKey())) {
            throw new ServiceException("设置键不能为空");
        }
        // Check if setting with this key already exists
        QueryWrapper<BudgetBackupSetting> wrapper = new QueryWrapper<>();
        wrapper.eq("SETTING_KEY", setting.getSettingKey());
        wrapper.eq("IS_DELETED", 0);
        BudgetBackupSetting existing = settingMapper.selectOne(wrapper);

        if (existing != null) {
            existing.setSettingValue(setting.getSettingValue());
            if (StringUtils.hasText(setting.getSettingName())) {
                existing.setSettingName(setting.getSettingName());
            }
            if (StringUtils.hasText(setting.getSettingDesc())) {
                existing.setSettingDesc(setting.getSettingDesc());
            }
            existing.setUpdateTime(new Date());
            settingMapper.updateById(existing);
            return existing;
        } else {
            setting.setCreateTime(new Date());
            setting.setUpdateTime(new Date());
            if (setting.getIsDeleted() == null) {
                setting.setIsDeleted(0);
            }
            settingMapper.insert(setting);
            return setting;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(List<BudgetBackupSetting> settings) {
        if (settings == null || settings.isEmpty()) {
            return;
        }
        for (BudgetBackupSetting setting : settings) {
            saveSetting(setting);
        }
    }
}
