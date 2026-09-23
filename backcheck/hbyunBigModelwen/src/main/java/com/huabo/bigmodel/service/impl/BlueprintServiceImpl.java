package com.huabo.bigmodel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huabo.bigmodel.dto.AiChatRequest;
import com.huabo.bigmodel.entity.*;
import com.huabo.bigmodel.mapper.*;
import com.huabo.bigmodel.service.AiChatService;
import com.huabo.bigmodel.service.BlueprintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 业务蓝图服务实现
 */
@Slf4j
@Service
public class BlueprintServiceImpl implements BlueprintService {

    @Autowired
    private AiChatService aiChatService;
    @Autowired
    private BlueprintDraftMapper draftMapper;
    @Autowired
    private BlueprintMapper blueprintMapper;
    @Autowired
    private BlueprintRequirementMapper requirementMapper;
    @Autowired
    private BlueprintBugMapper bugMapper;

    // 忽略 payload 中的未知字段：保存蓝图时整个 payload（含 requirements/bugs 子表数据）
    // 会先尝试转成 Blueprint 主表实体，requirements/bugs 等非主表字段需被忽略，
    // 之后再由本类单独取出插入子表（见 saveBlueprint）。
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // ====================== AI 拆分 ======================

    @Override
    public Map<String, Object> analyze(String title, String content) {
        Map<String, Object> result = new HashMap<>();
        if (content == null || content.trim().isEmpty()) {
            result.put("success", false);
            result.put("requirements", Collections.emptyList());
            result.put("message", "文档内容为空，无法拆分");
            return result;
        }
        try {
            String prompt = buildPrompt(title, content);
            AiChatRequest req = new AiChatRequest();
            req.setMessage(prompt);
            req.setEnableThinking(false);
            req.setEnableWebSearch(false);
            req.setStream(false);
            // 【2026-09-04 治理】文档拆分是纯文本分析：不附带 SQL 工具（避免模型中途
            // 调工具拖慢响应/污染 JSON 输出），并用专用系统提示词覆盖开发向默认提示词
            req.setEnableTools(false);
            req.setSystemPrompt("你是需求分析专家，只负责将业务需求文档拆分为结构化需求点，"
                    + "不调用任何工具，严格按用户要求的 JSON 数组格式输出，不要输出任何多余文字。");
            String aiText = aiChatService.chat(req);
            log.info("[Blueprint] analyze AI返回原文: {}", aiText);

            List<Map<String, Object>> requirements = parseRequirements(aiText);
            result.put("success", true);
            result.put("requirements", requirements);
            result.put("message", requirements.isEmpty() ? "未能从文档中提取到有效需求" : "");
            return result;
        } catch (Exception e) {
            log.error("[Blueprint] AI拆分失败", e);
            result.put("success", false);
            result.put("requirements", Collections.emptyList());
            result.put("message", "AI拆分失败: " + e.getMessage());
            return result;
        }
    }

    // 业务域枚举须与前端 BlueprintPage 的 BUSINESS_DOMAINS 保持一致：
    // AI 从中选值，前端评审表"业务域"下拉才能直接命中预填
    private static final String BUSINESS_DOMAIN_PROMPT =
            "投资穿透式监管/金融风险穿透式监管/采购与供应链穿透式监管/军品业务穿透式监管/"
                    + "境外单位穿透式监管/合同穿透式监管/薪酬分配穿透式监管/产权穿透式监管/"
                    + "财务穿透式监管/会计穿透式监管/行业穿透式监管/资金穿透式监管";

    private String buildPrompt(String title, String content) {
        return "你是需求分析专家，请将以下业务需求文档拆分为可独立开发的功能点。\n"
                + "每个功能点输出对象，字段为：domain(固定'新增需求')、module(推测的归属模块)、"
                + "description(该功能点的清晰描述)、urgency(取 高/中/低)、"
                // 【2026-09-04 新增】三个字段对应需求评审表的业务域/关键指标列表/预期效果列，
                // 拆分时一并生成，前端"发送至需求评审"封装时直接预填
                + "businessDomain(从以下固定列表中选择最匹配的一项，确实无法判断时选'行业穿透式监管'："
                + BUSINESS_DOMAIN_PROMPT + ")、"
                + "kpiList(关键指标字符串数组，2~4条可量化指标，从文档提取，文档未写明则根据功能点合理推测)、"
                + "expectedEffect(预期效果，一句话描述该功能点上线后的可感知收益)。\n"
                + "严格以JSON数组返回，不要任何多余文字、不要markdown代码块标记。\n"
                + "文档标题：" + (title == null ? "" : title) + "\n"
                + "文档正文：\n" + content;
    }

    /**
     * 容错解析 AI 返回，截取首个 [ 到末个 ] 之间内容，并补齐默认字段
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> parseRequirements(String aiText) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (aiText == null) return list;
        int start = aiText.indexOf('[');
        int end = aiText.lastIndexOf(']');
        if (start < 0 || end <= start) return list;
        String json = aiText.substring(start, end + 1);
        try {
            List<Map<String, Object>> raw = objectMapper.readValue(json, List.class);
            for (Map<String, Object> item : raw) {
                Map<String, Object> r = new LinkedHashMap<>();
                r.put("selected", false);
                r.put("status", "待处理");
                r.put("submitter", "");
                r.put("submitTime", "");
                r.put("domain", str(item.get("domain"), "新增需求"));
                r.put("module", str(item.get("module"), ""));
                r.put("description", str(item.get("description"), ""));
                r.put("businessDomain", str(item.get("businessDomain"), ""));
                r.put("kpiList", strList(item.get("kpiList")));
                r.put("expectedEffect", str(item.get("expectedEffect"), ""));
                r.put("imageList", new ArrayList<>());
                r.put("urgency", str(item.get("urgency"), "中"));
                r.put("resolveDate", "");
                r.put("handler", "");
                r.put("solution", "");
                r.put("resolved", "否");
                list.add(r);
            }
        } catch (Exception e) {
            log.warn("[Blueprint] JSON解析失败，返回空列表: {}", e.getMessage());
        }
        return list;
    }

    private String str(Object o, String def) {
        if (o == null) return def;
        String s = String.valueOf(o).trim();
        return s.isEmpty() ? def : s;
    }

    /**
     * kpiList 容错归一：数组则逐项转字符串；模型偶发输出为单个字符串时
     * 按换行/中英文分号切分，均过滤空白项
     */
    private List<String> strList(Object o) {
        List<String> list = new ArrayList<>();
        if (o == null) return list;
        if (o instanceof Collection) {
            for (Object item : (Collection<?>) o) {
                if (item != null) {
                    String s = String.valueOf(item).trim();
                    if (!s.isEmpty()) list.add(s);
                }
            }
            return list;
        }
        String s = String.valueOf(o).trim();
        if (s.isEmpty()) return list;
        for (String part : s.split("[\\n；;]")) {
            String p = part.trim();
            if (!p.isEmpty()) list.add(p);
        }
        return list;
    }

    // ====================== 草稿 ======================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BlueprintDraft saveDraft(BlueprintDraft draft) {
        Date now = new Date();
        draft.setCreateTime(now);
        draft.setUpdateTime(now);
        if (draft.getTitle() == null || draft.getTitle().isEmpty()) {
            draft.setTitle("未命名草稿");
        }
        draftMapper.insert(draft);
        return draft;
    }

    @Override
    public List<BlueprintDraft> listDrafts(String userId) {
        LambdaQueryWrapper<BlueprintDraft> qw = new LambdaQueryWrapper<>();
        qw.select(BlueprintDraft::getId, BlueprintDraft::getUserId, BlueprintDraft::getUserName,
                        BlueprintDraft::getTitle, BlueprintDraft::getCreateTime, BlueprintDraft::getUpdateTime)
                .eq(BlueprintDraft::getUserId, userId)
                .orderByDesc(BlueprintDraft::getUpdateTime)
                .last("LIMIT 200");
        return draftMapper.selectList(qw);
    }

    @Override
    public BlueprintDraft getDraft(String id) {
        return draftMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDraft(String id) {
        draftMapper.deleteById(id);
    }

    // ====================== 结构化 ======================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveBlueprint(Map<String, Object> payload) {
        Date now = new Date();
        Blueprint bp = objectMapper.convertValue(payload, Blueprint.class);
        String id = bp.getId();
        if (id == null || id.isEmpty()) {
            bp.setId(null);
            bp.setCreateTime(now);
            bp.setUpdateTime(now);
            blueprintMapper.insert(bp);
            id = bp.getId();
        } else {
            bp.setUpdateTime(now);
            blueprintMapper.updateById(bp);
            // 子表全量重写：先删
            requirementMapper.delete(new LambdaQueryWrapper<BlueprintRequirement>()
                    .eq(BlueprintRequirement::getBlueprintId, id));
            bugMapper.delete(new LambdaQueryWrapper<BlueprintBug>()
                    .eq(BlueprintBug::getBlueprintId, id));
        }

        // 插入需求点
        List<Map<String, Object>> reqs = asList(payload.get("requirements"));
        int seq = 1;
        for (Map<String, Object> m : reqs) {
            BlueprintRequirement r = objectMapper.convertValue(stripImageList(m), BlueprintRequirement.class);
            r.setId(null);
            r.setBlueprintId(id);
            r.setSeqNo(seq++);
            r.setCreateTime(now);
            r.setImageList(toImageListJson(m.get("imageList")));
            requirementMapper.insert(r);
        }

        // 插入 Bug
        List<Map<String, Object>> bugs = asList(payload.get("bugs"));
        seq = 1;
        for (Map<String, Object> m : bugs) {
            BlueprintBug b = objectMapper.convertValue(stripImageList(m), BlueprintBug.class);
            b.setId(null);
            b.setBlueprintId(id);
            b.setSeqNo(seq++);
            b.setCreateTime(now);
            b.setImageList(toImageListJson(m.get("imageList")));
            bugMapper.insert(b);
        }
        log.info("[Blueprint] 保存蓝图 id={}, 需求{}条, Bug{}条", id, reqs.size(), bugs.size());
        return id;
    }

    /**
     * 复制一份去掉 imageList 的 map：实体中 imageList 为 String，而前端常传数组，
     * 若直接 convertValue 会抛"数组无法转 String"异常，故转换前先剥离，转换后单独处理。
     */
    private Map<String, Object> stripImageList(Map<String, Object> src) {
        Map<String, Object> copy = new HashMap<>(src);
        copy.remove("imageList");
        return copy;
    }

    /** imageList 在实体中为 String：集合则序列化为 JSON，字符串原样保留，其它返回 null */
    private String toImageListJson(Object img) {
        if (img instanceof Collection) {
            try {
                return objectMapper.writeValueAsString(img);
            } catch (Exception e) {
                return null;
            }
        }
        if (img instanceof String) {
            return (String) img;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> asList(Object o) {
        if (o instanceof List) {
            return (List<Map<String, Object>>) o;
        }
        return Collections.emptyList();
    }

    @Override
    public Map<String, Object> getBlueprintDetail(String id) {
        Map<String, Object> result = new LinkedHashMap<>();
        Blueprint bp = blueprintMapper.selectById(id);
        result.put("blueprint", bp);
        result.put("requirements", requirementMapper.selectList(
                new LambdaQueryWrapper<BlueprintRequirement>()
                        .eq(BlueprintRequirement::getBlueprintId, id)
                        .orderByAsc(BlueprintRequirement::getSeqNo)));
        result.put("bugs", bugMapper.selectList(
                new LambdaQueryWrapper<BlueprintBug>()
                        .eq(BlueprintBug::getBlueprintId, id)
                        .orderByAsc(BlueprintBug::getSeqNo)));
        return result;
    }

    @Override
    public List<Blueprint> listBlueprints(String userId) {
        LambdaQueryWrapper<Blueprint> qw = new LambdaQueryWrapper<>();
        if (userId != null && !userId.isEmpty()) {
            qw.eq(Blueprint::getCreatorId, userId);
        }
        qw.orderByDesc(Blueprint::getUpdateTime).last("LIMIT 200");
        return blueprintMapper.selectList(qw);
    }
}
