package com.huabo.bigmodel.service;

import com.huabo.bigmodel.entity.Blueprint;
import com.huabo.bigmodel.entity.BlueprintDraft;

import java.util.List;
import java.util.Map;

/**
 * 业务蓝图服务接口
 */
public interface BlueprintService {

    // ============ AI 拆分 ============

    /**
     * AI 拆分需求文档为功能点列表
     *
     * @param title   文档标题
     * @param content 文档正文
     * @return { success, requirements:[...], message }
     */
    Map<String, Object> analyze(String title, String content);

    // ============ 草稿 ============

    BlueprintDraft saveDraft(BlueprintDraft draft);

    List<BlueprintDraft> listDrafts(String userId);

    BlueprintDraft getDraft(String id);

    void deleteDraft(String id);

    // ============ 结构化 ============

    /**
     * 保存蓝图（主表 + 需求点 + Bug，子表全量重写）
     *
     * @param payload 含主表字段、requirements、bugs
     * @return 蓝图ID
     */
    String saveBlueprint(Map<String, Object> payload);

    /**
     * 蓝图详情（含 requirements 与 bugs）
     */
    Map<String, Object> getBlueprintDetail(String id);

    /**
     * 蓝图列表（不含子表明细）
     */
    List<Blueprint> listBlueprints(String userId);
}
