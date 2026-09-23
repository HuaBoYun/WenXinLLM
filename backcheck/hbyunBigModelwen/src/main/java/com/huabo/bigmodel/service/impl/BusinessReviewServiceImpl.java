package com.huabo.bigmodel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.huabo.bigmodel.entity.BusinessDocConfirm;
import com.huabo.bigmodel.entity.BusinessDocVersion;
import com.huabo.bigmodel.entity.BusinessTemplate;
import com.huabo.bigmodel.entity.BusinessUserDraft;
import com.huabo.bigmodel.entity.RegulatoryKpi;
import com.huabo.bigmodel.entity.RegulatoryRequirement;
import com.huabo.bigmodel.mapper.BusinessDocConfirmMapper;
import com.huabo.bigmodel.mapper.BusinessDocVersionMapper;
import com.huabo.bigmodel.mapper.BusinessTemplateMapper;
import com.huabo.bigmodel.mapper.BusinessUserDraftMapper;
import com.huabo.bigmodel.mapper.RegulatoryKpiMapper;
import com.huabo.bigmodel.mapper.RegulatoryRequirementMapper;
import com.huabo.bigmodel.service.BusinessReviewService;
import com.huabo.bigmodel.vo.BusinessDocVersionVO;
import com.huabo.bigmodel.vo.BusinessUserDraftVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务梳理服务实现
 */
@Slf4j
@Service
public class BusinessReviewServiceImpl implements BusinessReviewService {

    @Autowired
    private BusinessTemplateMapper templateMapper;

    @Autowired
    private BusinessDocVersionMapper versionMapper;

    @Autowired
    private BusinessUserDraftMapper draftMapper;

    @Autowired
    private BusinessDocConfirmMapper confirmMapper;

    // 【2026-08-25 新增】级联删除关联的监管模型需求（一对一关联：ai_regulatory_requirement.template_id）
    @Autowired
    private RegulatoryRequirementMapper regulatoryRequirementMapper;

    @Autowired
    private RegulatoryKpiMapper regulatoryKpiMapper;

    // ====================== 模板 ======================

    @Override
    public List<BusinessTemplate> listTemplates() {
        LambdaQueryWrapper<BusinessTemplate> qw = new LambdaQueryWrapper<>();
        qw.eq(BusinessTemplate::getStatus, 1)
                .orderByAsc(BusinessTemplate::getSortNo)
                .orderByAsc(BusinessTemplate::getCreateTime);
        return templateMapper.selectList(qw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusinessTemplate createTemplate(BusinessTemplate template) {
        Date now = new Date();
        if (template.getIsSystem() == null) template.setIsSystem(0);
        if (template.getStatus() == null) template.setStatus(1);
        if (template.getSortNo() == null) {
            // 新建排到最末
            Integer maxSort = templateMapper.selectList(
                    new LambdaQueryWrapper<BusinessTemplate>()
                            .select(BusinessTemplate::getSortNo)
                            .orderByDesc(BusinessTemplate::getSortNo).last("LIMIT 1")
            ).stream().findFirst().map(BusinessTemplate::getSortNo).orElse(0);
            template.setSortNo(maxSort == null ? 1 : maxSort + 1);
        }
        // template_no 自动补齐：如果未传，则使用 sort_no 两位补零
        if (template.getTemplateNo() == null || template.getTemplateNo().isEmpty()) {
            template.setTemplateNo(String.format("%02d", template.getSortNo()));
        }
        template.setCreateTime(now);
        template.setUpdateTime(now);
        templateMapper.insert(template);
        log.info("创建业务梳理模板: id={}, title={}", template.getId(), template.getTitle());
        return template;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplate(String templateId) {
        BusinessTemplate t = templateMapper.selectById(templateId);
        if (t == null) {
            throw new RuntimeException("模板不存在: " + templateId);
        }
        if (t.getIsSystem() != null && t.getIsSystem() == 1) {
            throw new RuntimeException("系统内置模板不可删除");
        }
        // 级联删版本与草稿
        versionMapper.delete(new LambdaQueryWrapper<BusinessDocVersion>()
                .eq(BusinessDocVersion::getTemplateId, templateId));
        draftMapper.delete(new LambdaQueryWrapper<BusinessUserDraft>()
                .eq(BusinessUserDraft::getTemplateId, templateId));
        confirmMapper.delete(new LambdaQueryWrapper<BusinessDocConfirm>()
                .eq(BusinessDocConfirm::getTemplateId, templateId));
        // 【2026-08-25 新增】级联删除一对一关联的监管模型需求（含 KPI 子表），
        // 仅影响通过需求管理页建立的关联数据，AI 编程蓝图流程（BlueprintController）不受影响
        List<RegulatoryRequirement> linkedRequirements = regulatoryRequirementMapper.selectList(
                new LambdaQueryWrapper<RegulatoryRequirement>()
                        .eq(RegulatoryRequirement::getTemplateId, templateId));
        for (RegulatoryRequirement linked : linkedRequirements) {
            regulatoryKpiMapper.delete(new LambdaQueryWrapper<RegulatoryKpi>()
                    .eq(RegulatoryKpi::getRequirementId, linked.getId()));
            regulatoryRequirementMapper.deleteById(linked.getId());
        }
        templateMapper.deleteById(templateId);
        log.info("删除业务梳理模板及关联数据: id={}, 级联删除关联需求 {} 条", templateId, linkedRequirements.size());
    }

    @Override
    public BusinessTemplate getTemplate(String templateId) {
        return templateMapper.selectById(templateId);
    }

    // ====================== 版本 ======================

    @Override
    public BusinessDocVersion getLatestVersion(String templateId) {
        return versionMapper.selectOne(
                new LambdaQueryWrapper<BusinessDocVersion>()
                        .eq(BusinessDocVersion::getTemplateId, templateId)
                        .eq(BusinessDocVersion::getIsLatest, 1)
                        .last("LIMIT 1")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusinessDocVersion saveVersion(String templateId, String content,
                                          String changeSummary,
                                          String creatorId, String creatorName) {
        // 委托至带 editType 的重载，默认 manual，保持对既有调用零侵入
        return saveVersion(templateId, content, changeSummary, creatorId, creatorName, "manual");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusinessDocVersion saveVersion(String templateId, String content,
                                          String changeSummary,
                                          String creatorId, String creatorName,
                                          String editType) {
        if (templateMapper.selectById(templateId) == null) {
            throw new RuntimeException("模板不存在: " + templateId);
        }
        // 把已有最新版本重置为非最新
        versionMapper.update(null, new LambdaUpdateWrapper<BusinessDocVersion>()
                .eq(BusinessDocVersion::getTemplateId, templateId)
                .eq(BusinessDocVersion::getIsLatest, 1)
                .set(BusinessDocVersion::getIsLatest, 0));

        // 计算新版本号
        BusinessDocVersion last = versionMapper.selectOne(
                new LambdaQueryWrapper<BusinessDocVersion>()
                        .eq(BusinessDocVersion::getTemplateId, templateId)
                        .orderByDesc(BusinessDocVersion::getVersionNo)
                        .last("LIMIT 1"));
        int nextVer = last == null ? 1 : (last.getVersionNo() == null ? 1 : last.getVersionNo() + 1);

        BusinessDocVersion v = new BusinessDocVersion();
        v.setTemplateId(templateId);
        v.setVersionNo(nextVer);
        v.setContent(content);
        v.setIsLatest(1);
        v.setChangeSummary(changeSummary);
        v.setCreatorId(creatorId);
        v.setCreatorName(creatorName);
        v.setEditType(editType == null || editType.isEmpty() ? "manual" : editType);
        v.setCreateTime(new Date());
        versionMapper.insert(v);

        // 确认态回退：非 confirm 的内容变更，使已确认状态失效，需重新确认
        if (!"confirm".equals(editType)) {
            templateMapper.update(null, new LambdaUpdateWrapper<BusinessTemplate>()
                    .eq(BusinessTemplate::getId, templateId)
                    .eq(BusinessTemplate::getConfirmStatus, 1)
                    .set(BusinessTemplate::getConfirmStatus, 0)
                    .set(BusinessTemplate::getConfirmedVersionId, null));
        }
        log.info("发布新版本: templateId={}, versionNo={}, editType={}, creator={}",
                templateId, nextVer, editType, creatorId);
        return v;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusinessDocVersion seedInitialVersion(String templateId, String content,
                                                 String creatorId, String creatorName) {
        // 仅当当前模板没有任何版本时才写入
        Long existing = versionMapper.selectCount(
                new LambdaQueryWrapper<BusinessDocVersion>()
                        .eq(BusinessDocVersion::getTemplateId, templateId));
        if (existing != null && existing > 0) {
            log.info("种子写入跳过：模板已存在版本 templateId={}", templateId);
            return getLatestVersion(templateId);
        }
        return saveVersion(templateId, content, "初始版本", creatorId, creatorName);
    }

    @Override
    public List<BusinessDocVersionVO> listVersions(String templateId) {
        // 不查 content
        LambdaQueryWrapper<BusinessDocVersion> qw = new LambdaQueryWrapper<>();
        qw.select(BusinessDocVersion::getId, BusinessDocVersion::getTemplateId,
                        BusinessDocVersion::getVersionNo, BusinessDocVersion::getIsLatest,
                        BusinessDocVersion::getChangeSummary, BusinessDocVersion::getCreatorId,
                        BusinessDocVersion::getCreatorName, BusinessDocVersion::getCreateTime)
                .eq(BusinessDocVersion::getTemplateId, templateId)
                .orderByDesc(BusinessDocVersion::getVersionNo);
        List<BusinessDocVersion> list = versionMapper.selectList(qw);
        List<BusinessDocVersionVO> result = new ArrayList<>(list.size());
        for (BusinessDocVersion v : list) {
            BusinessDocVersionVO vo = new BusinessDocVersionVO();
            BeanUtils.copyProperties(v, vo);
            result.add(vo);
        }
        return result;
    }

    @Override
    public BusinessDocVersion getVersion(String versionId) {
        return versionMapper.selectById(versionId);
    }

    // ====================== 草稿 ======================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusinessUserDraft saveDraft(BusinessUserDraft draft) {
        Date now = new Date();
        draft.setCreateTime(now);
        draft.setUpdateTime(now);
        if (draft.getTitle() == null || draft.getTitle().isEmpty()) {
            BusinessTemplate t = templateMapper.selectById(draft.getTemplateId());
            String tplTitle = t == null ? "未命名" : t.getTitle();
            draft.setTitle(tplTitle + " · 草稿");
        }
        draftMapper.insert(draft);
        log.info("保存草稿: id={}, userId={}, templateId={}",
                draft.getId(), draft.getUserId(), draft.getTemplateId());
        return draft;
    }

    @Override
    public List<BusinessUserDraftVO> listDrafts(String userId, String templateId) {
        LambdaQueryWrapper<BusinessUserDraft> qw = new LambdaQueryWrapper<>();
        qw.select(BusinessUserDraft::getId, BusinessUserDraft::getTemplateId,
                        BusinessUserDraft::getUserId, BusinessUserDraft::getUserName,
                        BusinessUserDraft::getTitle, BusinessUserDraft::getBaseVersionNo,
                        BusinessUserDraft::getCreateTime, BusinessUserDraft::getUpdateTime)
                .eq(BusinessUserDraft::getUserId, userId)
                .orderByDesc(BusinessUserDraft::getUpdateTime)
                .last("LIMIT 200");
        if (templateId != null && !templateId.isEmpty()) {
            qw.eq(BusinessUserDraft::getTemplateId, templateId);
        }
        List<BusinessUserDraft> list = draftMapper.selectList(qw);

        // 收集模板标题，做冗余展示
        Map<String, String> titleMap = new HashMap<>();
        for (BusinessUserDraft d : list) {
            if (!titleMap.containsKey(d.getTemplateId())) {
                BusinessTemplate t = templateMapper.selectById(d.getTemplateId());
                titleMap.put(d.getTemplateId(), t == null ? "" : t.getTitle());
            }
        }

        List<BusinessUserDraftVO> result = new ArrayList<>(list.size());
        for (BusinessUserDraft d : list) {
            BusinessUserDraftVO vo = new BusinessUserDraftVO();
            BeanUtils.copyProperties(d, vo);
            vo.setTemplateTitle(titleMap.getOrDefault(d.getTemplateId(), ""));
            result.add(vo);
        }
        return result;
    }

    @Override
    public BusinessUserDraft getDraft(String draftId) {
        return draftMapper.selectById(draftId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDraft(String draftId) {
        draftMapper.deleteById(draftId);
        log.info("删除草稿: id={}", draftId);
    }

    // ====================== 签字确认（纯追加） ======================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusinessDocConfirm confirmDoc(BusinessDocConfirm confirm) {
        String templateId = confirm.getTemplateId();
        if (templateId == null || templateId.isEmpty()) {
            throw new RuntimeException("templateId 不能为空");
        }
        BusinessTemplate tpl = templateMapper.selectById(templateId);
        if (tpl == null) {
            throw new RuntimeException("需求不存在: " + templateId);
        }
        // 取当前最新版本，只允许对最新版本签字确认（防止对旧版本误签）
        BusinessDocVersion latest = getLatestVersion(templateId);
        if (latest == null) {
            throw new RuntimeException("该需求暂无文档版本，无法确认");
        }
        // 落库确认记录
        confirm.setVersionId(latest.getId());
        confirm.setVersionNo(latest.getVersionNo());
        confirm.setConfirmTime(new Date());
        confirmMapper.insert(confirm);

        // 更新需求确认态：置为已确认并锁定确认版本
        templateMapper.update(null, new LambdaUpdateWrapper<BusinessTemplate>()
                .eq(BusinessTemplate::getId, templateId)
                .set(BusinessTemplate::getConfirmStatus, 1)
                .set(BusinessTemplate::getConfirmedVersionId, latest.getId()));

        log.info("需求签字确认: templateId={}, versionNo={}, confirmers={}, operator={}",
                templateId, latest.getVersionNo(), confirm.getConfirmerNames(), confirm.getOperatorId());
        return confirm;
    }

    @Override
    public List<BusinessDocConfirm> listConfirms(String templateId) {
        // 不查签名大图，仅列表展示
        LambdaQueryWrapper<BusinessDocConfirm> qw = new LambdaQueryWrapper<>();
        qw.select(BusinessDocConfirm::getId, BusinessDocConfirm::getTemplateId,
                        BusinessDocConfirm::getVersionId, BusinessDocConfirm::getVersionNo,
                        BusinessDocConfirm::getConfirmerNames, BusinessDocConfirm::getConfirmOpinion,
                        BusinessDocConfirm::getOperatorId, BusinessDocConfirm::getOperatorName,
                        BusinessDocConfirm::getConfirmTime)
                .eq(BusinessDocConfirm::getTemplateId, templateId)
                .orderByDesc(BusinessDocConfirm::getConfirmTime)
                .last("LIMIT 200");
        return confirmMapper.selectList(qw);
    }
}

