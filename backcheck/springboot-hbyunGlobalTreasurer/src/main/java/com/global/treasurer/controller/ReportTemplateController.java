package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblReportTemplate;
import com.global.treasurer.service.ReportTemplateService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 报告模板Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Controller
@RequestMapping({"/regulatory/template", "/globalTreasurer/regulatory/template"})
@Api(tags = "报告模板管理")
public class ReportTemplateController {
    private static final Logger log = LoggerFactory.getLogger(ReportTemplateController.class);

    @Resource
    private ReportTemplateService templateService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询报告模板列表")
    public String getTemplateList(@RequestParam(required = false) String templateName,
                                  @RequestParam(required = false) String authorityId,
                                  @RequestParam(required = false) Integer isEnabled,
                                  @RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> params = new HashMap<>();
            params.put("templateName", templateName);
            params.put("authorityId", authorityId);
            params.put("isEnabled", isEnabled);
            params.put("pageNum", pageNum);
            params.put("pageSize", pageSize);

            PageInfo<TblReportTemplate> pageInfo = templateService.getTemplateList(params);
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询报告模板列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{templateId}")
    @ResponseBody
    @ApiOperation("根据ID获取报告模板详情")
    public String getTemplateById(@PathVariable String templateId,
                                  @RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblReportTemplate template = templateService.getTemplateById(templateId);
            return new JsonBean(1, "成功", template).toJson();
        } catch (Exception e) {
            log.error("获取报告模板详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ResponseBody
    @ApiOperation("新增报告模板")
    public String addTemplate(@FlexibleRequestBody TblReportTemplate template,
                              @RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            template.setTemplateId(null);
            TblReportTemplate saved = templateService.saveTemplate(template);
            return new JsonBean(1, "新增成功", saved).toJson();
        } catch (Exception e) {
            log.error("新增报告模板失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ResponseBody
    @ApiOperation("修改报告模板")
    public String updateTemplate(@FlexibleRequestBody TblReportTemplate template,
                                 @RequestHeader(value = "token", required = false) String token,
                                 HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            if (template.getTemplateId() == null || template.getTemplateId().isEmpty()) {
                return new JsonBean(0, "模板ID不能为空", null).toJson();
            }

            TblReportTemplate saved = templateService.saveTemplate(template);
            return new JsonBean(1, "修改成功", saved).toJson();
        } catch (Exception e) {
            log.error("修改报告模板失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/{templateIds}")
    @ResponseBody
    @ApiOperation("删除报告模板")
    public String deleteTemplate(@PathVariable String templateIds,
                                 @RequestHeader(value = "token", required = false) String token,
                                 HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            String[] ids = templateIds.split(",");
            if (ids.length == 1) {
                templateService.deleteTemplate(ids[0]);
            } else {
                templateService.batchDeleteTemplates(Arrays.asList(ids));
            }
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除报告模板失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/authority/{authorityId}")
    @ResponseBody
    @ApiOperation("根据监管机构查询模板")
    public String getTemplatesByAuthority(@PathVariable String authorityId,
                                          @RequestHeader(value = "token", required = false) String token,
                                          HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblReportTemplate> templates = templateService.getTemplatesByAuthority(authorityId);
            return new JsonBean(1, "成功", templates).toJson();
        } catch (Exception e) {
            log.error("根据监管机构查询模板失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/usable")
    @ResponseBody
    @ApiOperation("查询可使用的模板")
    public String getUsableTemplates(@RequestHeader(value = "token", required = false) String token,
                                     HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblReportTemplate> templates = templateService.getUsableTemplates();
            return new JsonBean(1, "成功", templates).toJson();
        } catch (Exception e) {
            log.error("查询可使用的模板失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/expiring/{days}")
    @ResponseBody
    @ApiOperation("查询即将过期的模板")
    public String getExpiringSoonTemplates(@PathVariable Integer days,
                                           @RequestHeader(value = "token", required = false) String token,
                                           HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblReportTemplate> templates = templateService.getExpiringSoonTemplates(days);
            return new JsonBean(1, "成功", templates).toJson();
        } catch (Exception e) {
            log.error("查询即将过期的模板失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/{templateId}/status/{isEnabled}")
    @ResponseBody
    @ApiOperation("启用/停用报告模板")
    public String toggleTemplateStatus(@PathVariable String templateId,
                                       @PathVariable Integer isEnabled,
                                       @RequestHeader(value = "token", required = false) String token,
                                       HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            templateService.toggleTemplateStatus(templateId, isEnabled);
            return new JsonBean(1, "操作成功", null).toJson();
        } catch (Exception e) {
            log.error("启用/停用报告模板失败", e);
            return new JsonBean(0, "操作失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{sourceTemplateId}/copy")
    @ResponseBody
    @ApiOperation("复制报告模板")
    public String copyTemplate(@PathVariable String sourceTemplateId,
                               @RequestParam String newTemplateCode,
                               @RequestParam String newTemplateName,
                               @RequestHeader(value = "token", required = false) String token,
                               HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblReportTemplate template = templateService.copyTemplate(sourceTemplateId, newTemplateCode, newTemplateName);
            return new JsonBean(1, "复制成功", template).toJson();
        } catch (Exception e) {
            log.error("复制报告模板失败", e);
            return new JsonBean(0, "复制失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{sourceTemplateId}/version")
    @ResponseBody
    @ApiOperation("创建模板新版本")
    public String createTemplateVersion(@PathVariable String sourceTemplateId,
                                        @RequestParam String newVersion,
                                        @RequestHeader(value = "token", required = false) String token,
                                        HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblReportTemplate template = templateService.createTemplateVersion(sourceTemplateId, newVersion);
            return new JsonBean(1, "创建版本成功", template).toJson();
        } catch (Exception e) {
            log.error("创建模板新版本失败", e);
            return new JsonBean(0, "创建版本失败: " + e.getMessage(), null).toJson();
        }
    }
}
