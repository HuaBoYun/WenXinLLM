package com.huabo.bigmodel.service;

import com.huabo.bigmodel.entity.RegulatoryRequirement;

import java.util.List;
import java.util.Map;

/**
 * 监管模型需求 服务接口
 */
public interface RegulatoryRequirementService {

    /**
     * 新增/更新需求（含子表全量重写）
     *
     * @param payload 主表字段 + kpiList 子表数组
     * @return 主表ID
     */
    String saveRequirement(Map<String, Object> payload);

    /**
     * 需求详情（含 kpiList）
     */
    Map<String, Object> getRequirementDetail(String id);

    /**
     * 【2026-08-25 新增】按关联的业务梳理模板ID查询需求详情（含 kpiList）
     * 需求管理页"需求评审"按 templateId 一对一加载对应需求；未关联时 data 为 null
     */
    Map<String, Object> getRequirementByTemplateId(String templateId);

    /**
     * 需求列表（不含子表明细）
     */
    List<RegulatoryRequirement> listRequirements(String userId);

    /**
     * 删除需求（含子表）
     */
    void deleteRequirement(String id);

    /**
     * 提交审批：更新 submit_status = 1
     */
    void submitApproval(String id);
}
