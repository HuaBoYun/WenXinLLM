package com.huabo.bigmodel.controller;

import com.huabo.bigmodel.common.Result;
import com.huabo.bigmodel.entity.BusinessDocConfirm;
import com.huabo.bigmodel.entity.BusinessDocVersion;
import com.huabo.bigmodel.entity.BusinessTemplate;
import com.huabo.bigmodel.entity.BusinessUserDraft;
import com.huabo.bigmodel.service.BusinessReviewService;
import com.huabo.bigmodel.vo.BusinessDocVersionVO;
import com.huabo.bigmodel.vo.BusinessUserDraftVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 业务梳理控制器
 * 路径：/v1/ai/business
 * 网关访问：/bigmodel/v1/ai/business
 */
@Slf4j
@RestController
@RequestMapping("/v1/ai/business")
@Tag(name = "业务梳理管理", description = "模板/文档版本/用户草稿")
public class BusinessReviewController {

    @Autowired
    private BusinessReviewService businessReviewService;

    // ============ 模板 ============

    @GetMapping("/template/list")
    @Operation(summary = "模板列表（左侧树）")
    public Result<List<BusinessTemplate>> listTemplates() {
        try {
            return Result.success(businessReviewService.listTemplates());
        } catch (Exception e) {
            log.error("获取模板列表失败", e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/template/create")
    @Operation(summary = "新建模板")
    public Result<BusinessTemplate> createTemplate(@RequestBody BusinessTemplate template) {
        try {
            if (template.getTitle() == null || template.getTitle().trim().isEmpty()) {
                return Result.error("模板标题不能为空");
            }
            // 用户新建强制为非系统模板
            template.setIsSystem(0);
            return Result.success(businessReviewService.createTemplate(template));
        } catch (Exception e) {
            log.error("创建模板失败", e);
            return Result.error("创建失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/template/{id}")
    @Operation(summary = "删除模板")
    public Result<Void> deleteTemplate(@PathVariable("id") String id) {
        try {
            businessReviewService.deleteTemplate(id);
            return Result.success();
        } catch (Exception e) {
            log.error("删除模板失败 id={}", id, e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/template/{id}")
    @Operation(summary = "获取模板详情")
    public Result<BusinessTemplate> getTemplate(@PathVariable("id") String id) {
        try {
            return Result.success(businessReviewService.getTemplate(id));
        } catch (Exception e) {
            log.error("获取模板失败 id={}", id, e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    // ============ 版本 ============

    @GetMapping("/doc/latest/{templateId}")
    @Operation(summary = "获取最新版本（含 content）")
    public Result<BusinessDocVersion> getLatestVersion(@PathVariable("templateId") String templateId) {
        try {
            return Result.success(businessReviewService.getLatestVersion(templateId));
        } catch (Exception e) {
            log.error("获取最新版本失败 templateId={}", templateId, e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/doc/save-version")
    @Operation(summary = "发布新版本")
    public Result<BusinessDocVersion> saveVersion(@RequestBody Map<String, String> body) {
        try {
            String templateId = body.get("templateId");
            String content = body.get("content");
            String changeSummary = body.getOrDefault("changeSummary", "");
            String creatorId = body.getOrDefault("creatorId", "");
            String creatorName = body.getOrDefault("creatorName", "");
            String editType = body.getOrDefault("editType", "manual");
            if (templateId == null || templateId.isEmpty()) {
                return Result.error("templateId 不能为空");
            }
            if (content == null) content = "";
            return Result.success(businessReviewService.saveVersion(
                    templateId, content, changeSummary, creatorId, creatorName, editType));
        } catch (Exception e) {
            log.error("发布新版本失败", e);
            return Result.error("发布失败: " + e.getMessage());
        }
    }

    @PostMapping("/doc/seed")
    @Operation(summary = "首次种子内容写入（仅在无版本时生效）")
    public Result<BusinessDocVersion> seedInitialVersion(@RequestBody Map<String, String> body) {
        try {
            String templateId = body.get("templateId");
            String content = body.get("content");
            String creatorId = body.getOrDefault("creatorId", "system");
            String creatorName = body.getOrDefault("creatorName", "系统初始化");
            if (templateId == null || templateId.isEmpty()) {
                return Result.error("templateId 不能为空");
            }
            if (content == null) content = "";
            return Result.success(businessReviewService.seedInitialVersion(
                    templateId, content, creatorId, creatorName));
        } catch (Exception e) {
            log.error("种子内容写入失败", e);
            return Result.error("种子写入失败: " + e.getMessage());
        }
    }

    @GetMapping("/doc/versions/{templateId}")
    @Operation(summary = "版本列表（不含 content）")
    public Result<List<BusinessDocVersionVO>> listVersions(@PathVariable("templateId") String templateId) {
        try {
            return Result.success(businessReviewService.listVersions(templateId));
        } catch (Exception e) {
            log.error("获取版本列表失败 templateId={}", templateId, e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/doc/version/{versionId}")
    @Operation(summary = "获取指定版本（含 content）")
    public Result<BusinessDocVersion> getVersion(@PathVariable("versionId") String versionId) {
        try {
            return Result.success(businessReviewService.getVersion(versionId));
        } catch (Exception e) {
            log.error("获取版本失败 versionId={}", versionId, e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    // ============ 草稿 ============

    @PostMapping("/draft/save")
    @Operation(summary = "保存草稿（每次新增一条）")
    public Result<BusinessUserDraft> saveDraft(@RequestBody BusinessUserDraft draft) {
        try {
            if (draft.getUserId() == null || draft.getUserId().isEmpty()) {
                return Result.error("userId 不能为空");
            }
            if (draft.getTemplateId() == null || draft.getTemplateId().isEmpty()) {
                return Result.error("templateId 不能为空");
            }
            return Result.success(businessReviewService.saveDraft(draft));
        } catch (Exception e) {
            log.error("保存草稿失败", e);
            return Result.error("保存失败: " + e.getMessage());
        }
    }

    @GetMapping("/draft/list")
    @Operation(summary = "用户草稿列表")
    public Result<List<BusinessUserDraftVO>> listDrafts(
            @RequestParam("userId") String userId,
            @RequestParam(value = "templateId", required = false) String templateId) {
        try {
            return Result.success(businessReviewService.listDrafts(userId, templateId));
        } catch (Exception e) {
            log.error("获取草稿列表失败 userId={}", userId, e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/draft/{id}")
    @Operation(summary = "获取草稿详情（含 content）")
    public Result<BusinessUserDraft> getDraft(@PathVariable("id") String id) {
        try {
            return Result.success(businessReviewService.getDraft(id));
        } catch (Exception e) {
            log.error("获取草稿失败 id={}", id, e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/draft/{id}")
    @Operation(summary = "删除草稿")
    public Result<Void> deleteDraft(@PathVariable("id") String id) {
        try {
            businessReviewService.deleteDraft(id);
            return Result.success();
        } catch (Exception e) {
            log.error("删除草稿失败 id={}", id, e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    // ============ 签字确认（纯追加） ============

    @PostMapping("/confirm")
    @Operation(summary = "对需求最新版本签字确认")
    public Result<BusinessDocConfirm> confirmDoc(@RequestBody BusinessDocConfirm confirm) {
        try {
            if (confirm.getTemplateId() == null || confirm.getTemplateId().isEmpty()) {
                return Result.error("templateId 不能为空");
            }
            if (confirm.getConfirmerNames() == null || confirm.getConfirmerNames().trim().isEmpty()) {
                return Result.error("确认人姓名不能为空");
            }
            return Result.success(businessReviewService.confirmDoc(confirm));
        } catch (Exception e) {
            log.error("签字确认失败", e);
            return Result.error("确认失败: " + e.getMessage());
        }
    }

    @GetMapping("/confirm/{templateId}")
    @Operation(summary = "某需求的确认记录列表")
    public Result<List<BusinessDocConfirm>> listConfirms(@PathVariable("templateId") String templateId) {
        try {
            return Result.success(businessReviewService.listConfirms(templateId));
        } catch (Exception e) {
            log.error("获取确认记录失败 templateId={}", templateId, e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }
}

