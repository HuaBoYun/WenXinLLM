package com.global.treasurer.financialProductDefinition.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblBillRiskAlert;

import java.util.Map;

/**
 * 风险预警Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
public interface RiskAlertService extends IService<TblBillRiskAlert> {

    /**
     * 分页查询风险预警列表
     *
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @param alertType 预警类型
     * @param alertLevel 预警级别
     * @param alertStatus 预警状态
     * @param productName 产品名称
     * @param orgId 组织ID
     * @return 分页结果
     */
    IPage<TblBillRiskAlert> getAlertPage(Integer pageNo, Integer pageSize,
                                         String alertType, String alertLevel,
                                         String alertStatus, String productName,
                                         Long orgId);

    /**
     * 获取预警统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getStatistics();
}
