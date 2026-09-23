package com.management.accountant.controller;

import com.alibaba.fastjson.JSON;
import com.management.accountant.oracle.entity.budget.BudgetTemplate;
import com.management.accountant.oracle.service.budget.BudgetTemplateCategoryService;
import com.management.accountant.oracle.service.budget.BudgetTemplateService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.vo.param.BudgetTemplateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 预算模板管理Controller
 *
 * @author AI Agent
 * @date 2026-01-29
 */
@RestController
@Api(tags = {"NCV65全面预算-模板管理"})
@RequestMapping(value = "/accountant/budget/template")
@Slf4j
public class BudgetTemplateManagementController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetTemplateService budgetTemplateService;

    @Resource
    private BudgetTemplateCategoryService budgetTemplateCategoryService;

    /**
     * 保存模板
     */
    @Operation(summary = "保存模板")
    @ApiOperation("保存模板")
    @PostMapping("/save")
    public String saveTemplate(@RequestBody BudgetTemplateDTO dto) {
        try {
            boolean result = budgetTemplateService.saveTemplate(dto);
            if (result) {
                return JSON.toJSONString(new MyJsonBean<>(1, "保存成功", null));
            } else {
                return JSON.toJSONString(new MyJsonBean<>(0, "保存失败", null));
            }
        } catch (Exception e) {
            log.error("保存模板异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "保存失败：" + e.getMessage(), null));
        }
    }

    /**
     * 应用模板
     */
    @Operation(summary = "应用模板")
    @ApiOperation("应用模板")
    @PostMapping("/apply")
    public String applyTemplate(@RequestParam String templateId,
                               @RequestParam String budgetId) {
        try {
            boolean result = budgetTemplateService.applyTemplate(templateId, budgetId);
            if (result) {
                return JSON.toJSONString(new MyJsonBean<>(1, "应用成功", null));
            } else {
                return JSON.toJSONString(new MyJsonBean<>(0, "应用失败", null));
            }
        } catch (Exception e) {
            log.error("应用模板异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "应用失败：" + e.getMessage(), null));
        }
    }

    /**
     * 复制模板
     */
    @Operation(summary = "复制模板")
    @ApiOperation("复制模板")
    @PostMapping("/copy")
    public String copyTemplate(@RequestParam String templateId) {
        try {
            // copyTemplate返回的是BudgetTemplate对象
            BudgetTemplate newTemplate = budgetTemplateService.copyTemplate(templateId);
            if (newTemplate != null) {
                return JSON.toJSONString(new MyJsonBean<>(1, "复制成功", newTemplate.getTemplateId()));
            } else {
                return JSON.toJSONString(new MyJsonBean<>(0, "复制失败", null));
            }
        } catch (Exception e) {
            log.error("复制模板异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "复制失败：" + e.getMessage(), null));
        }
    }

    /**
     * 查询模板列表
     */
    @Operation(summary = "查询模板列表")
    @ApiOperation("查询模板列表")
    @PostMapping("/list")
    public String listTemplates(@RequestParam(required = false) String templateType) {
        try {
            List<BudgetTemplate> list = budgetTemplateService.listByType(templateType);
            return JSON.toJSONString(new MyJsonBean<>(1, "查询成功", list));
        } catch (Exception e) {
            log.error("查询模板列表异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "查询失败：" + e.getMessage(), null));
        }
    }

    /**
     * 查询模板详情
     */
    @Operation(summary = "查询模板详情")
    @ApiOperation("查询模板详情")
    @PostMapping("/detail")
    public String getTemplateDetail(@RequestParam String templateId) {
        try {
            // getTemplateDetail返回的是Map<String, Object>
            java.util.Map<String, Object> detailMap = budgetTemplateService.getTemplateDetail(templateId);
            if (detailMap != null && (Boolean) detailMap.getOrDefault("success", false)) {
                return JSON.toJSONString(new MyJsonBean<>(1, "查询成功", detailMap.get("data")));
            } else {
                return JSON.toJSONString(new MyJsonBean<>(0, "模板不存在", null));
            }
        } catch (Exception e) {
            log.error("查询模板详情异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "查询失败：" + e.getMessage(), null));
        }
    }

    /**
     * 启用/停用模板
     */
    @Operation(summary = "启用/停用模板")
    @ApiOperation("启用/停用模板")
    @PostMapping("/toggle-status")
    public String toggleStatus(@RequestParam String templateId,
                              @RequestParam Integer isEnabled) {
        try {
            boolean result = budgetTemplateService.toggleStatus(templateId, isEnabled);
            if (result) {
                return JSON.toJSONString(new MyJsonBean<>(1, "操作成功", null));
            } else {
                return JSON.toJSONString(new MyJsonBean<>(0, "操作失败", null));
            }
        } catch (Exception e) {
            log.error("切换模板状态异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "操作失败：" + e.getMessage(), null));
        }
    }

    /**
     * 删除模板
     */
    @Operation(summary = "删除模板")
    @ApiOperation("删除模板")
    @PostMapping("/delete")
    public String deleteTemplate(@RequestParam String templateId) {
        try {
            boolean result = budgetTemplateService.removeById(templateId);
            if (result) {
                return JSON.toJSONString(new MyJsonBean<>(1, "删除成功", null));
            } else {
                return JSON.toJSONString(new MyJsonBean<>(0, "删除失败", null));
            }
        } catch (Exception e) {
            log.error("删除模板异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "删除失败：" + e.getMessage(), null));
        }
    }

    /**
     * 查询系统模板
     */
    @Operation(summary = "查询系统模板")
    @ApiOperation("查询系统模板")
    @PostMapping("/system-templates")
    public String listSystemTemplates() {
        try {
            List<BudgetTemplate> list = budgetTemplateService.listSystemTemplates();
            return JSON.toJSONString(new MyJsonBean<>(1, "查询成功", list));
        } catch (Exception e) {
            log.error("查询系统模板异常", e);
            return JSON.toJSONString(new MyJsonBean<>(0, "查询失败：" + e.getMessage(), null));
        }
    }

    @Operation(summary = "创建模板")
    @ApiOperation("创建模板") @PostMapping("/create")
    public MyJsonBean<BudgetTemplate> create(@RequestBody BudgetTemplateDTO dto) {
        MyJsonBean<BudgetTemplate> r = new MyJsonBean<>();
        try { boolean ok = budgetTemplateService.saveTemplate(dto); r.setCode(ok ? 1 : 0); r.setMsg(ok ? "创建成功" : "创建失败");
        } catch (Exception e) { log.error("创建模板异常", e); r.setCode(0); r.setMsg("创建失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "更新模板")
    @ApiOperation("更新模板") @PutMapping("/update/{templateId}")
    public MyJsonBean<Void> update(@PathVariable String templateId, @RequestBody BudgetTemplateDTO dto) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { dto.setTemplateId(templateId); budgetTemplateService.saveTemplate(dto); r.setCode(1); r.setMsg("更新成功");
        } catch (Exception e) { log.error("更新模板异常", e); r.setCode(0); r.setMsg("更新失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "删除模板")
    @ApiOperation("删除模板") @DeleteMapping("/delete/{templateId}")
    public MyJsonBean<Void> deleteById(@PathVariable String templateId) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { budgetTemplateService.deleteTemplate(templateId); r.setCode(1); r.setMsg("删除成功");
        } catch (Exception e) { log.error("删除模板异常", e); r.setCode(0); r.setMsg("删除失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "分页查询模板")
    @ApiOperation("分页查询模板") @PostMapping("/page")
    public MyJsonBean<Object> getPage(@RequestBody java.util.Map<String, Object> params) {
        MyJsonBean<Object> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(budgetTemplateService.listTemplates(params));
        } catch (Exception e) { log.error("分页查询模板异常", e); r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取分类树")
    @ApiOperation("获取分类树") @GetMapping("/category/tree")
    public MyJsonBean<List<java.util.Map<String, Object>>> getCategoryTree() {
        MyJsonBean<List<java.util.Map<String, Object>>> r = new MyJsonBean<>();
        try {
            List<java.util.Map<String, Object>> tree = budgetTemplateCategoryService.getCategoryTree();
            r.setCode(1); r.setMsg("查询成功"); r.setData(tree);
        } catch (Exception e) { log.error("获取分类树异常", e); r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取分类列表")
    @ApiOperation("获取分类列表") @GetMapping("/categories")
    public MyJsonBean<List<java.util.Map<String, Object>>> getCategories() {
        MyJsonBean<List<java.util.Map<String, Object>>> r = new MyJsonBean<>();
        try {
            List<java.util.Map<String, Object>> categories = budgetTemplateCategoryService.getCategories();
            r.setCode(1); r.setMsg("查询成功"); r.setData(categories);
        } catch (Exception e) { log.error("获取分类列表异常", e); r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "导出模板")
    @ApiOperation("导出模板") @PostMapping("/export")
    public void exportTemplates(@RequestBody java.util.Map<String, Object> params, HttpServletResponse response) {
        try {
            Object data = budgetTemplateService.listTemplates(params);
            String json = JSON.toJSONString(data);
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            String filename = URLEncoder.encode("预算模板列表.json", "UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (IOException e) {
            log.error("导出模板IO异常", e);
        } catch (Exception e) {
            log.error("导出模板异常", e);
        }
    }

    @Operation(summary = "导出单个模板")
    @ApiOperation("导出单个模板") @GetMapping("/{templateId}/export")
    public void exportSingle(@PathVariable String templateId, HttpServletResponse response) {
        try {
            BudgetTemplate template = budgetTemplateService.getById(templateId);
            if (template == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            String json = JSON.toJSONString(template);
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            String filename = URLEncoder.encode(template.getTemplateName() + ".json", "UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (IOException e) {
            log.error("导出单个模板IO异常", e);
        } catch (Exception e) {
            log.error("导出单个模板异常", e);
        }
    }
}

