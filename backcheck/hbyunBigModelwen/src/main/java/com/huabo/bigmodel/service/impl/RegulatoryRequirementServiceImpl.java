package com.huabo.bigmodel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huabo.bigmodel.entity.RegulatoryKpi;
import com.huabo.bigmodel.entity.RegulatoryRequirement;
import com.huabo.bigmodel.mapper.RegulatoryKpiMapper;
import com.huabo.bigmodel.mapper.RegulatoryRequirementMapper;
import com.huabo.bigmodel.service.RegulatoryRequirementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 监管模型需求 Service 实现
 */
@Slf4j
@Service
public class RegulatoryRequirementServiceImpl implements RegulatoryRequirementService {

    @Autowired
    private RegulatoryRequirementMapper requirementMapper;
    @Autowired
    private RegulatoryKpiMapper kpiMapper;

    // 忽略 payload 未知字段（kpiList 等在实体里没有映射）
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveRequirement(Map<String, Object> payload) {
        Date now = new Date();
        RegulatoryRequirement req = objectMapper.convertValue(payload, RegulatoryRequirement.class);
        String id = req.getId();

        // 计算优先级得分：value*2 - difficulty - invest
        Integer v = req.getValueScore();
        Integer d = req.getDifficultyScore();
        Integer i = req.getInvestScore();
        if (v != null && d != null && i != null) {
            int score = v * 2 - d - i;
            req.setPriorityScore(score);
            req.setPriority(score >= 5 ? "高" : (score >= 2 ? "中" : "低"));
        }

        if (id == null || id.isEmpty()) {
            // 【2026-08-25 新增】一对一关联去重：未传 id 但携带 templateId 时，
            // 若该模板已关联需求则复用其 id 走更新分支，避免同一梳理需求产生多条评审记录
            if (req.getTemplateId() != null && !req.getTemplateId().isEmpty()) {
                RegulatoryRequirement existing = requirementMapper.selectOne(
                        new LambdaQueryWrapper<RegulatoryRequirement>()
                                .eq(RegulatoryRequirement::getTemplateId, req.getTemplateId())
                                .last("LIMIT 1"));
                if (existing != null) {
                    id = existing.getId();
                    req.setId(id);
                    req.setReqNo(existing.getReqNo());
                    req.setCreateTime(existing.getCreateTime());
                }
            }
        }

        if (id == null || id.isEmpty()) {
            req.setId(null);
            req.setCreateTime(now);
            req.setUpdateTime(now);
            if (req.getSubmitStatus() == null) req.setSubmitStatus(0);
            if (req.getReqNo() == null || req.getReqNo().isEmpty()) {
                req.setReqNo(generateReqNo(now));
            }
            requirementMapper.insert(req);
            id = req.getId();
        } else {
            req.setUpdateTime(now);
            requirementMapper.updateById(req);
            // 子表全量重写：先删
            kpiMapper.delete(new LambdaQueryWrapper<RegulatoryKpi>()
                    .eq(RegulatoryKpi::getRequirementId, id));
        }

        // 插入 KPI 子表
        List<Map<String, Object>> kpis = asList(payload.get("kpiList"));
        int seq = 1;
        for (Map<String, Object> m : kpis) {
            RegulatoryKpi k = objectMapper.convertValue(stripBlob(m), RegulatoryKpi.class);
            k.setId(null);
            k.setRequirementId(id);
            k.setSeqNo(seq++);
            k.setCreateTime(now);
            // kpiList/attachList 字段前端可能传数组，转 JSON 字符串
            k.setKpiList(toJsonStr(m.get("kpiList")));
            k.setAttachList(toJsonStr(m.get("attachList")));
            kpiMapper.insert(k);
        }
        log.info("[Regulatory] 保存需求 id={}, KPI {}条", id, kpis.size());
        return id;
    }

    /** 生成需求编号：REQ-yyyyMMdd-xxxx（当天流水） */
    private String generateReqNo(Date now) {
        String day = new SimpleDateFormat("yyyyMMdd").format(now);
        String prefix = "REQ-" + day + "-";
        QueryWrapper<RegulatoryRequirement> qw = new QueryWrapper<>();
        qw.likeRight("req_no", prefix);
        Long count = requirementMapper.selectCount(qw);
        long next = (count == null ? 0L : count) + 1L;
        return prefix + String.format("%04d", next);
    }

    /** 去掉 kpiList/attachList 数组字段避免 convertValue 抛异常 */
    private Map<String, Object> stripBlob(Map<String, Object> src) {
        Map<String, Object> copy = new HashMap<>(src);
        copy.remove("kpiList");
        copy.remove("attachList");
        return copy;
    }

    private String toJsonStr(Object o) {
        if (o == null) return null;
        if (o instanceof String) return (String) o;
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> asList(Object o) {
        if (o instanceof List) {
            return (List<Map<String, Object>>) o;
        }
        return Collections.emptyList();
    }

    @Override
    public Map<String, Object> getRequirementDetail(String id) {
        Map<String, Object> result = new LinkedHashMap<>();
        RegulatoryRequirement req = requirementMapper.selectById(id);
        result.put("requirement", req);
        result.put("kpiList", kpiMapper.selectList(
                new LambdaQueryWrapper<RegulatoryKpi>()
                        .eq(RegulatoryKpi::getRequirementId, id)
                        .orderByAsc(RegulatoryKpi::getSeqNo)));
        return result;
    }

    /** 【2026-08-25 新增】按关联模板ID查需求详情（一对一），未关联时 data 为 null */
    @Override
    public Map<String, Object> getRequirementByTemplateId(String templateId) {
        RegulatoryRequirement req = requirementMapper.selectOne(
                new LambdaQueryWrapper<RegulatoryRequirement>()
                        .eq(RegulatoryRequirement::getTemplateId, templateId)
                        .orderByDesc(RegulatoryRequirement::getUpdateTime)
                        .last("LIMIT 1"));
        if (req == null) {
            return null;
        }
        return getRequirementDetail(req.getId());
    }

    @Override
    public List<RegulatoryRequirement> listRequirements(String userId) {
        LambdaQueryWrapper<RegulatoryRequirement> qw = new LambdaQueryWrapper<>();
        if (userId != null && !userId.isEmpty()) {
            qw.eq(RegulatoryRequirement::getCreatorId, userId);
        }
        qw.orderByDesc(RegulatoryRequirement::getUpdateTime).last("LIMIT 200");
        return requirementMapper.selectList(qw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRequirement(String id) {
        kpiMapper.delete(new LambdaQueryWrapper<RegulatoryKpi>()
                .eq(RegulatoryKpi::getRequirementId, id));
        requirementMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitApproval(String id) {
        RegulatoryRequirement req = requirementMapper.selectById(id);
        if (req == null) throw new IllegalArgumentException("需求不存在: " + id);
        req.setSubmitStatus(1);
        req.setSubmitTime(new Date());
        req.setUpdateTime(new Date());
        requirementMapper.updateById(req);
    }
}
