package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblFundAlert;
import com.global.treasurer.mapper.TblFundAlertMapper;
import com.global.treasurer.service.TblFundAlertService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 资金告警Service实现类
 * @author Claude
 * @date 2026-01-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblFundAlertServiceImpl implements TblFundAlertService {
    @Resource
    private TblFundAlertMapper tblFundAlertMapper;

    @Override
    public PageInfo<TblFundAlert> getAlertPage(Integer pageNum, Integer pageSize,
                                               String alertType, String alertLevel, String alertStatus) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<TblFundAlert> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(alertType), TblFundAlert::getAlertType, alertType)
               .eq(StringUtils.isNotBlank(alertLevel), TblFundAlert::getAlertLevel, alertLevel)
               .eq(StringUtils.isNotBlank(alertStatus), TblFundAlert::getAlertStatus, alertStatus)
               .orderByDesc(TblFundAlert::getCreateTime);
        List<TblFundAlert> list = tblFundAlertMapper.selectList(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public TblFundAlert getAlertById(String alertId) {
        return tblFundAlertMapper.selectById(alertId);
    }

    @Override
    public TblFundAlert saveAlert(TblFundAlert alert) {
        alert.setAlertNo("ALT" + System.currentTimeMillis());
        alert.setCreateTime(new Date());
        if (alert.getAlertStatus() == null) {
            alert.setAlertStatus("PENDING");
        }
        tblFundAlertMapper.insert(alert);
        return alert;
    }

    @Override
    public void updateAlert(TblFundAlert alert) {
        alert.setUpdateTime(new Date());
        tblFundAlertMapper.updateById(alert);
    }

    @Override
    public void handleAlert(String alertId, String handleResult, String handleRemark) {
        TblFundAlert alert = new TblFundAlert();
        alert.setAlertId(alertId);
        alert.setAlertStatus("RESOLVED");
        alert.setHandleResult(handleResult);
        alert.setResolveRemark(handleRemark);
        alert.setHandleTime(new Date());
        alert.setResolveTime(new Date());
        alert.setUpdateTime(new Date());
        tblFundAlertMapper.updateById(alert);
    }

    @Override
    public void batchHandleAlerts(List<String> alertIds) {
        for (String alertId : alertIds) {
            handleAlert(alertId, "BATCH_RESOLVED", "批量处理");
        }
    }

    @Override
    public long count() {
        LambdaQueryWrapper<TblFundAlert> wrapper = new LambdaQueryWrapper<>();
        return tblFundAlertMapper.selectCount(wrapper);
    }

    @Override
    public long countByStatus(String status) {
        LambdaQueryWrapper<TblFundAlert> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblFundAlert::getAlertStatus, status);
        return tblFundAlertMapper.selectCount(wrapper);
    }
}

