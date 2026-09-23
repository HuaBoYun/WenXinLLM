package com.huabo.bigmodel.service;

import com.huabo.bigmodel.entity.BusinessDocVersion;
import com.huabo.bigmodel.entity.BusinessTemplate;
import com.huabo.bigmodel.entity.BusinessUserDraft;
import com.huabo.bigmodel.entity.BusinessDocConfirm;
import com.huabo.bigmodel.vo.BusinessDocVersionVO;
import com.huabo.bigmodel.vo.BusinessUserDraftVO;

import java.util.List;

/**
 * 业务梳理服务接口
 */
public interface BusinessReviewService {

    // ============ 模板 ============

    /** 获取启用的模板列表（按 sort_no 升序） */
    List<BusinessTemplate> listTemplates();

    /** 新建模板 */
    BusinessTemplate createTemplate(BusinessTemplate template);

    /** 删除模板（仅允许删除非系统模板，且会级联删除其版本与草稿） */
    void deleteTemplate(String templateId);

    /** 获取单个模板 */
    BusinessTemplate getTemplate(String templateId);

    // ============ 版本 ============

    /** 获取最新版本（含 content） */
    BusinessDocVersion getLatestVersion(String templateId);

    /** 发布新版本（自增 version_no、切换 is_latest） */
    BusinessDocVersion saveVersion(String templateId, String content,
                                   String changeSummary,
                                   String creatorId, String creatorName);

    /** 首次种子内容写入：仅当当前模板没有任何版本时写入 v1，避免覆盖 */
    BusinessDocVersion seedInitialVersion(String templateId, String content,
                                          String creatorId, String creatorName);

    /** 版本列表（不含 content） */
    List<BusinessDocVersionVO> listVersions(String templateId);

    /** 获取指定版本（含 content） */
    BusinessDocVersion getVersion(String versionId);

    // ============ 草稿 ============

    /** 保存草稿（每次新增一条） */
    BusinessUserDraft saveDraft(BusinessUserDraft draft);

    /** 用户草稿列表（不含 content） */
    List<BusinessUserDraftVO> listDrafts(String userId, String templateId);

    /** 获取草稿详情（含 content） */
    BusinessUserDraft getDraft(String draftId);

    /** 删除草稿 */
    void deleteDraft(String draftId);

    // ============ 签字确认（纯追加，对既有逻辑零侵入） ============

    /**
     * 对需求文档的当前最新版本进行签字确认。
     * 仅允许对最新版本确认；确认后置 confirm_status=1 并锁定 confirmed_version_id。
     *
     * @param confirm 含 templateId、signatureImg、confirmerNames、confirmOpinion、operatorId、operatorName
     * @return 落库后的确认记录
     */
    BusinessDocConfirm confirmDoc(BusinessDocConfirm confirm);

    /** 某需求的确认记录列表（倒序，不含签名大图） */
    List<BusinessDocConfirm> listConfirms(String templateId);

    /**
     * 发布新版本（带编辑类型）。editType != confirm 时，将该需求的 confirm_status 回退为 0（需重新确认）。
     * 原 5 参 saveVersion 委托至此方法，editType 默认 manual。
     */
    BusinessDocVersion saveVersion(String templateId, String content,
                                   String changeSummary,
                                   String creatorId, String creatorName,
                                   String editType);
}

