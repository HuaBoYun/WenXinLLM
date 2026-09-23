package com.global.treasurer.service;

import com.global.treasurer.entity.TcPartnerRiskList;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 伙伴风险管理服务接口
 * 
 * @author AI Assistant
 * @date 2025-01-26
 */
public interface TcPartnerRiskService {

    /**
     * 分页查询风险评估列表
     */
    PageInfo<TcPartnerRiskList> list(Integer pageNum, Integer pageSize, Map<String, Object> params);

    /**
     * 根据ID查询风险评估
     */
    TcPartnerRiskList getById(String id);

    /**
     * 新增风险评估
     */
    boolean save(TcPartnerRiskList riskList);

    /**
     * 更新风险评估
     */
    boolean update(TcPartnerRiskList riskList);

    /**
     * 删除风险评估
     */
    boolean delete(String id);

    /**
     * 批量删除风险评估
     */
    boolean batchDelete(List<String> ids);

    /**
     * 根据伙伴ID查询风险列表
     */
    List<TcPartnerRiskList> getByPartnerId(String partnerId);

    /**
     * 根据名单类型查询
     */
    List<TcPartnerRiskList> getByListType(String listType);

    /**
     * 根据风险等级查询
     */
    List<TcPartnerRiskList> getByRiskLevel(String riskLevel);

    /**
     * 查询风险统计信息
     */
    Map<String, Object> getRiskStatistics();

    /**
     * 获取风险预警列表
     */
    List<TcPartnerRiskList> getRiskAlerts();

    /**
     * 批量评估
     */
    Map<String, Object> batchAssess(List<String> partnerIds, String assessUser);

    /**
     * 更新风险状态
     */
    boolean updateRiskStatus(String id, String status, String updateUser);
}

