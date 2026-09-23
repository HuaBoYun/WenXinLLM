package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.oracle.entity.budget.BudgetAuditSetting;
import com.management.accountant.oracle.mapper.budget.BudgetAuditSettingMapper;
import com.management.accountant.service.BudgetAuditSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BudgetAuditSettingServiceImpl implements BudgetAuditSettingService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetAuditSettingMapper settingMapper;

    @Override
    public List<BudgetAuditSetting> getAll() {
        QueryWrapper<BudgetAuditSetting> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        wrapper.orderByAsc("SETTING_TYPE", "SETTING_KEY");
        return settingMapper.selectList(wrapper);
    }

    @Override
    public BudgetAuditSetting getByKey(String settingKey) {
        if (!StringUtils.hasText(settingKey)) {
            return null;
        }
        QueryWrapper<BudgetAuditSetting> wrapper = new QueryWrapper<>();
        wrapper.eq("SETTING_KEY", settingKey).eq("IS_DELETED", 0);
        return settingMapper.selectOne(wrapper);
    }

    @Override
    public void saveOrUpdate(BudgetAuditSetting setting) {
        if (setting == null) return;
        setting.setUpdateTime(new Date());
        if (StringUtils.hasText(setting.getSettingId())) {
            settingMapper.updateById(setting);
        } else {
            setting.setCreateTime(new Date());
            setting.setIsDeleted(0);
            settingMapper.insert(setting);
        }
    }

    @Override
    public void deleteById(String settingId) {
        if (!StringUtils.hasText(settingId)) return;
        BudgetAuditSetting setting = settingMapper.selectById(settingId);
        if (setting != null) {
            setting.setIsDeleted(1);
            setting.setUpdateTime(new Date());
            settingMapper.updateById(setting);
        }
    }
}
