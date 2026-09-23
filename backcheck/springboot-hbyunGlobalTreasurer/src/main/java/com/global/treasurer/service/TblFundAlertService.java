package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblFundAlert;

import java.util.List;

/**
 * 资金告警Service接口
 * @author Claude
 * @date 2026-01-20
 */
public interface TblFundAlertService {

    /**
     * 分页查询告警列表
     */
    PageInfo<TblFundAlert> getAlertPage(Integer pageNum, Integer pageSize,
                                        String alertType, String alertLevel, String alertStatus);

    /**
     * 根据ID查询告警
     */
    TblFundAlert getAlertById(String alertId);

    /**
     * 保存告警
     */
    TblFundAlert saveAlert(TblFundAlert alert);

    /**
     * 更新告警
     */
    void updateAlert(TblFundAlert alert);

    /**
     * 处理告警
     */
    void handleAlert(String alertId, String handleResult, String handleRemark);

    /**
     * 批量处理告警
     */
    void batchHandleAlerts(List<String> alertIds);

    /**
     * 统计总数
     */
    long count();

    /**
     * 按状态统计数量
     */
    long countByStatus(String status);
}

