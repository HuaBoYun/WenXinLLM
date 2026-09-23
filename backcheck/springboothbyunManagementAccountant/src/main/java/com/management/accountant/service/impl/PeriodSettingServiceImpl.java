package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.PeriodSetting;
import com.management.accountant.oracle.mapper.budget.PeriodSettingMapper;
import com.management.accountant.service.PeriodSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PeriodSettingServiceImpl implements PeriodSettingService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private PeriodSettingMapper periodSettingMapper;

    @Override
    public PeriodSetting getSetting() {
        QueryWrapper<PeriodSetting> w = new QueryWrapper<>();
        w.orderByDesc("UPDATE_TIME");
        w.last("LIMIT 1");
        return periodSettingMapper.selectOne(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PeriodSetting save(PeriodSetting setting) {
        if (setting == null) throw new ServiceException("设置信息不能为空");
        setting.setUpdateTime(new Date());
        if (setting.getSettingId() == null || setting.getSettingId().isEmpty()) {
            setting.setCreateTime(new Date());
            periodSettingMapper.insert(setting);
        } else {
            periodSettingMapper.updateById(setting);
        }
        return setting;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reset() {
        PeriodSetting defaultSetting = new PeriodSetting();
        defaultSetting.setDefaultPeriodType("MONTHLY");
        defaultSetting.setNamingRule("{YEAR}-{MONTH}");
        defaultSetting.setAutoOpenNext(true);
        defaultSetting.setPeriodEndDate("LAST_DAY");
        defaultSetting.setFixedDay(28);
        defaultSetting.setAllowCrossPeriod(false);
        defaultSetting.setLockRule("MANUAL");
        defaultSetting.setAllowEditAfterClose(false);
        defaultSetting.setCreateTime(new Date());
        defaultSetting.setUpdateTime(new Date());
        periodSettingMapper.insert(defaultSetting);
    }
}
