package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingRiskDTO;
import com.global.treasurer.dto.FinancingRiskQueryDTO;
import com.global.treasurer.entity.TblFinancingMonitoring;

import java.util.List;
import java.util.Map;

/**
 * 融资风险监控服务接口
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
public interface FinancingRiskService {

    /**
     * 分页查询风险列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblFinancingMonitoring> getRiskList(FinancingRiskQueryDTO queryDTO);

    /**
     * 根据ID查询风险详情
     *
     * @param monitoringId 监控ID
     * @return 融资风险监控
     */
    TblFinancingMonitoring getRiskById(Long monitoringId);

    /**
     * 创建风险记录
     *
     * @param dto 融资风险DTO
     * @return 创建后的风险记录
     */
    TblFinancingMonitoring createRisk(FinancingRiskDTO dto);

    /**
     * 更新风险记录
     *
     * @param dto 融资风险DTO
     * @return 更新后的风险记录
     */
    TblFinancingMonitoring updateRisk(FinancingRiskDTO dto);

    /**
     * 风险评估
     *
     * @param relatedFinancingId 关联融资ID
     * @return 评估结果
     */
    Map<String, Object> assessRisk(Long relatedFinancingId);

    /**
     * 风险预警
     *
     * @param relatedFinancingId 关联融资ID
     * @return 预警信息
     */
    List<TblFinancingMonitoring> alertRisk(Long relatedFinancingId);

    /**
     * 获取风险趋势统计
     *
     * @param companyId 公司ID
     * @param days 统计天数
     * @return 趋势数据
     */
    Map<String, Object> getRiskTrend(Long companyId, Integer days);

    /**
     * 处理风险
     *
     * @param monitoringId 监控ID
     * @param handlerName 处理人姓名
     * @param handleOpinion 处理意见
     * @param alertStatus 预警状态（HANDLED-已处理, CLOSED-已关闭）
     * @return 处理结果
     */
    boolean processRisk(Long monitoringId, String handlerName, String handleOpinion, String alertStatus);

    /**
     * 删除风险记录
     *
     * @param monitoringId 监控ID
     */
    void deleteRisk(Long monitoringId);

    /**
     * 批量删除风险记录
     *
     * @param monitoringIds 监控ID列表
     */
    void batchDeleteRisks(List<Long> monitoringIds);
}
