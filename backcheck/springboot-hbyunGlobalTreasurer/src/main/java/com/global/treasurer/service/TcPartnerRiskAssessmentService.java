package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcPartnerRiskAssessment;

import java.util.List;
import java.util.Map;

/**
 * 合作伙伴风险评估服务接口
 * 
 * @author AI Assistant
 * @date 2026-03-05
 */
public interface TcPartnerRiskAssessmentService {

    /**
     * 分页查询风险评估列表
     */
    PageInfo<TcPartnerRiskAssessment> list(Integer pageNum, Integer pageSize, Map<String, Object> params);

    /**
     * 根据ID查询风险评估
     */
    TcPartnerRiskAssessment getById(String id);

    /**
     * 新增风险评估
     */
    boolean save(TcPartnerRiskAssessment assessment);

    /**
     * 更新风险评估
     */
    boolean update(TcPartnerRiskAssessment assessment);

    /**
     * 删除风险评估
     */
    boolean delete(String id);

    /**
     * 批量删除风险评估
     */
    boolean batchDelete(List<String> ids);

    /**
     * 根据伙伴ID查询风险评估列表
     */
    List<TcPartnerRiskAssessment> getByPartnerId(String partnerId);

    /**
     * 根据风险等级查询
     */
    List<TcPartnerRiskAssessment> getByRiskLevel(String riskLevel);

    /**
     * 根据风险状态查询
     */
    List<TcPartnerRiskAssessment> getByRiskStatus(String riskStatus);

    /**
     * 获取风险统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 获取风险等级列表
     */
    List<Map<String, Object>> getRiskLevels();

    /**
     * 获取评估类型列表
     */
    List<Map<String, Object>> getAssessmentTypes();

    /**
     * 获取合作伙伴列表
     */
    List<Map<String, Object>> getPartners();

    /**
     * 更新风险状态
     */
    boolean updateRiskStatus(String id, String riskStatus);

    /**
     * 导出风险报告
     */
    List<TcPartnerRiskAssessment> exportReport(Map<String, Object> params);
}

