package com.huabo.cybermonitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.QueryTemplate;
import com.huabo.cybermonitor.service.IQueryTemplateService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.QueryTemplateQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 查询模板管理 Controller
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="查询模板管理",description="查询模板管理")
@RestController
@RequestMapping("/v1/query/template")
public class QueryTemplateController {

	private static final Logger log = LoggerFactory.getLogger(QueryTemplateController.class);

    @Autowired
    private IQueryTemplateService templateService;

    @Operation(summary = "分页查询查询模板列表")
    @PostMapping("/list")
    public R<PageResult<QueryTemplate>> getTemplateList(@RequestBody QueryTemplateQueryVO queryVO) {
        try {
            IPage<QueryTemplate> page = templateService.getTemplateList(queryVO);
            PageResult<QueryTemplate> pageResult = new PageResult<QueryTemplate>();
            pageResult.setTlist(page.getRecords());
            pageResult.setTotalRecord((int) page.getTotal());
            pageResult.setPageNumber((int) page.getCurrent());
            pageResult.setPageSize((int) page.getSize());

            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询查询模板列表失败", e);
            return R.fail("查询查询模板列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取查询模板详情")
    @PostMapping("/detail")
    public R<QueryTemplate> getTemplateDetail(@RequestBody Map<String, String> params) {
        try {
            String templateId = params.get("templateId");
            if (templateId == null || templateId.trim().isEmpty()) {
                return R.fail("模板ID不能为空");
            }
            
            QueryTemplate template = templateService.getTemplateDetail(templateId);
            return R.success(template);
        } catch (Exception e) {
            log.error("获取查询模板详情失败", e);
            return R.fail("获取查询模板详情失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增查询模板")
    @PostMapping("/add")
    public R<String> addTemplate(@RequestBody QueryTemplate template) {
        try {
            boolean success = templateService.addTemplate(template);
            if (success) {
                return R.success("新增查询模板成功");
            } else {
                return R.fail("新增查询模板失败");
            }
        } catch (Exception e) {
            log.error("新增查询模板失败", e);
            return R.fail("新增查询模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新查询模板")
    @PostMapping("/update")
    public R<String> updateTemplate(@RequestBody QueryTemplate template) {
        try {
            if (template.getTemplateId() == null || template.getTemplateId().trim().isEmpty()) {
                return R.fail("模板ID不能为空");
            }
            
            boolean success = templateService.updateTemplate(template);
            if (success) {
                return R.success("更新查询模板成功");
            } else {
                return R.fail("更新查询模板失败");
            }
        } catch (Exception e) {
            log.error("更新查询模板失败", e);
            return R.fail("更新查询模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除查询模板")
    @PostMapping("/delete")
    public R<String> deleteTemplate(@RequestBody Map<String, String> params) {
        try {
            String templateId = params.get("templateId");
            if (templateId == null || templateId.trim().isEmpty()) {
                return R.fail("模板ID不能为空");
            }
            
            boolean success = templateService.deleteTemplate(templateId);
            if (success) {
                return R.success("删除查询模板成功");
            } else {
                return R.fail("删除查询模板失败");
            }
        } catch (Exception e) {
            log.error("删除查询模板失败", e);
            return R.fail("删除查询模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除查询模板")
    @PostMapping("/batch-delete")
    public R<String> batchDeleteTemplate(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> templateIds = params.get("templateIds");
            if (templateIds == null || templateIds.isEmpty()) {
                return R.fail("模板ID列表不能为空");
            }
            
            boolean success = templateService.batchDeleteTemplate(templateIds);
            if (success) {
                return R.success("批量删除查询模板成功");
            } else {
                return R.fail("批量删除查询模板失败");
            }
        } catch (Exception e) {
            log.error("批量删除查询模板失败", e);
            return R.fail("批量删除查询模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "启用查询模板")
    @PostMapping("/enable")
    public R<String> enableTemplate(@RequestBody Map<String, String> params) {
        try {
            String templateId = params.get("templateId");
            if (templateId == null || templateId.trim().isEmpty()) {
                return R.fail("模板ID不能为空");
            }
            
            boolean success = templateService.enableTemplate(templateId);
            if (success) {
                return R.success("启用查询模板成功");
            } else {
                return R.fail("启用查询模板失败");
            }
        } catch (Exception e) {
            log.error("启用查询模板失败", e);
            return R.fail("启用查询模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "禁用查询模板")
    @PostMapping("/disable")
    public R<String> disableTemplate(@RequestBody Map<String, String> params) {
        try {
            String templateId = params.get("templateId");
            if (templateId == null || templateId.trim().isEmpty()) {
                return R.fail("模板ID不能为空");
            }
            
            boolean success = templateService.disableTemplate(templateId);
            if (success) {
                return R.success("禁用查询模板成功");
            } else {
                return R.fail("禁用查询模板失败");
            }
        } catch (Exception e) {
            log.error("禁用查询模板失败", e);
            return R.fail("禁用查询模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量启用查询模板")
    @PostMapping("/batch-enable")
    public R<String> batchEnableTemplate(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> templateIds = params.get("templateIds");
            if (templateIds == null || templateIds.isEmpty()) {
                return R.fail("模板ID列表不能为空");
            }
            
            boolean success = templateService.batchEnableTemplate(templateIds);
            if (success) {
                return R.success("批量启用查询模板成功");
            } else {
                return R.fail("批量启用查询模板失败");
            }
        } catch (Exception e) {
            log.error("批量启用查询模板失败", e);
            return R.fail("批量启用查询模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量禁用查询模板")
    @PostMapping("/batch-disable")
    public R<String> batchDisableTemplate(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> templateIds = params.get("templateIds");
            if (templateIds == null || templateIds.isEmpty()) {
                return R.fail("模板ID列表不能为空");
            }
            
            boolean success = templateService.batchDisableTemplate(templateIds);
            if (success) {
                return R.success("批量禁用查询模板成功");
            } else {
                return R.fail("批量禁用查询模板失败");
            }
        } catch (Exception e) {
            log.error("批量禁用查询模板失败", e);
            return R.fail("批量禁用查询模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据模板类型查询模板列表")
    @PostMapping("/list-by-type")
    public R<List<QueryTemplate>> getTemplatesByType(@RequestBody Map<String, String> params) {
        try {
            String templateType = params.get("templateType");
            if (templateType == null || templateType.trim().isEmpty()) {
                return R.fail("模板类型不能为空");
            }
            
            List<QueryTemplate> templates = templateService.getTemplatesByType(templateType);
            return R.success(templates);
        } catch (Exception e) {
            log.error("根据模板类型查询模板列表失败", e);
            return R.fail("根据模板类型查询模板列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询公开的模板列表")
    @PostMapping("/public")
    public R<List<QueryTemplate>> getPublicTemplates() {
        try {
            List<QueryTemplate> templates = templateService.getPublicTemplates();
            return R.success(templates);
        } catch (Exception e) {
            log.error("查询公开的模板列表失败", e);
            return R.fail("查询公开的模板列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询启用的模板列表")
    @PostMapping("/enabled")
    public R<List<QueryTemplate>> getEnabledTemplates() {
        try {
            List<QueryTemplate> templates = templateService.getEnabledTemplates();
            return R.success(templates);
        } catch (Exception e) {
            log.error("查询启用的模板列表失败", e);
            return R.fail("查询启用的模板列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "验证模板名称唯一性")
    @PostMapping("/validate-name")
    public R<Map<String, Object>> validateTemplateName(@RequestBody Map<String, String> params) {
        try {
            String templateName = params.get("templateName");
            String excludeId = params.get("excludeId");
            
            if (templateName == null || templateName.trim().isEmpty()) {
                return R.fail("模板名称不能为空");
            }
            
            boolean isDuplicate = templateService.validateTemplateName(templateName, excludeId);

            Map<String, Object> result = new HashMap<>();
            result.put("isDuplicate", isDuplicate);
            result.put("message", isDuplicate ? "模板名称已存在" : "模板名称可用");

            return R.success(result);
        } catch (Exception e) {
            log.error("验证模板名称唯一性失败", e);
            return R.fail("验证模板名称唯一性失败：" + e.getMessage());
        }
    }

    @Operation(summary = "复制查询模板")
    @PostMapping("/copy")
    public R<String> copyTemplate(@RequestBody Map<String, String> params) {
        try {
            String templateId = params.get("templateId");
            String newName = params.get("newName");
            
            if (templateId == null || templateId.trim().isEmpty()) {
                return R.fail("源模板ID不能为空");
            }
            if (newName == null || newName.trim().isEmpty()) {
                return R.fail("新模板名称不能为空");
            }
            
            boolean success = templateService.copyTemplate(templateId, newName);
            if (success) {
                return R.success("复制查询模板成功");
            } else {
                return R.fail("复制查询模板失败");
            }
        } catch (Exception e) {
            log.error("复制查询模板失败", e);
            return R.fail("复制查询模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "执行查询模板")
    @PostMapping("/execute")
    public R<List<Map<String, Object>>> executeTemplate(@RequestBody Map<String, Object> params) {
        try {
            String templateId = (String) params.get("templateId");
            @SuppressWarnings("unchecked")
            Map<String, Object> queryParams = (Map<String, Object>) params.get("params");
            
            if (templateId == null || templateId.trim().isEmpty()) {
                return R.fail("模板ID不能为空");
            }
            
            List<Map<String, Object>> result = templateService.executeTemplate(templateId, queryParams);
            return R.success(result);
        } catch (Exception e) {
            log.error("执行查询模板失败", e);
            return R.fail("执行查询模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "验证查询模板SQL")
    @PostMapping("/validate-sql")
    public R<Map<String, Object>> validateTemplateSql(@RequestBody Map<String, String> params) {
        try {
            String querySql = params.get("querySql");
            
            Map<String, Object> result = templateService.validateTemplateSql(querySql);
            return R.success(result);
        } catch (Exception e) {
            log.error("验证查询模板SQL失败", e);
            return R.fail("验证查询模板SQL失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取模板统计信息")
    @PostMapping("/statistics/overview")
    public R<Map<String, Object>> getTemplateStatistics() {
        try {
            Map<String, Object> statistics = templateService.getTemplateStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取模板统计信息失败", e);
            return R.fail("获取模板统计信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取模板类型分布统计")
    @PostMapping("/statistics/type-distribution")
    public R<List<Map<String, Object>>> getTemplateTypeDistribution() {
        try {
            List<Map<String, Object>> distribution = templateService.getTemplateTypeDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取模板类型分布统计失败", e);
            return R.fail("获取模板类型分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取模板使用频率统计")
    @PostMapping("/statistics/usage")
    public R<List<Map<String, Object>>> getTemplateUsageStatistics() {
        try {
            List<Map<String, Object>> usage = templateService.getTemplateUsageStatistics();
            return R.success(usage);
        } catch (Exception e) {
            log.error("获取模板使用频率统计失败", e);
            return R.fail("获取模板使用频率统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出模板列表")
    @PostMapping("/export")
    public void exportTemplateList(@RequestBody QueryTemplateQueryVO queryVO, HttpServletResponse response) {
        try {
            templateService.exportTemplateList(queryVO, response);
        } catch (Exception e) {
            log.error("导出模板列表失败", e);
            throw new RuntimeException("导出模板列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "下载模板导入模板")
    @GetMapping("/template")
    public void downloadTemplateTemplate(HttpServletResponse response) {
        try {
            templateService.downloadTemplateTemplate(response);
        } catch (Exception e) {
            log.error("下载模板导入模板失败", e);
            throw new RuntimeException("下载模板导入模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量导入模板")
    @PostMapping("/import")
    public R<Map<String, Object>> importTemplateList(
            @Parameter(description = "导入文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return R.fail("导入文件不能为空");
            }
            
            Map<String, Object> result = templateService.importTemplateList(file);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量导入模板失败", e);
            return R.fail("批量导入模板失败：" + e.getMessage());
        }
    }
}
