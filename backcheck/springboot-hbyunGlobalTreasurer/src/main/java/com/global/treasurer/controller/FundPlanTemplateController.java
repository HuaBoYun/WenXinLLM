package com.global.treasurer.controller;

import com.global.treasurer.entity.TblFundPlanTemplate;
import com.global.treasurer.mapper.FundPlanTemplateMapper;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金计划模板控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/fund/template")
@Api(tags = "资金计划模板管理")
public class FundPlanTemplateController {
    @Resource
    private FundPlanTemplateMapper fundPlanTemplateMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询资金计划模板
     */
    @GetMapping("/page")
    @ApiOperation("分页查询资金计划模板")
    public String getFundPlanTemplatePage(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Integer page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
            Integer limit = params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 20;

            params.put("offset", (page - 1) * limit);
            params.put("limit", limit);

            List<TblFundPlanTemplate> list = fundPlanTemplateMapper.selectTemplatePage(params);
            int total = fundPlanTemplateMapper.countTemplateList(params);

            Map<String, Object> result = new HashMap<>();
            result.put("records", list);
            result.put("total", total);
            result.put("current", page);
            result.put("size", limit);

            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建资金计划模板
     */
    @PostMapping("")
    @ApiOperation("创建资金计划模板")
    public String createFundPlanTemplate(@FlexibleRequestBody TblFundPlanTemplate template) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            template.setTemplateId(null);
            template.setDeleteFlag(0);
            template.setCreatedTime(new Date());
            template.setUsageCount(template.getUsageCount() != null ? template.getUsageCount() : 0);
            if (template.getTemplateStatus() == null || template.getTemplateStatus().isEmpty()) {
                template.setTemplateStatus("ACTIVE");
            }
            if (loginStaff.getCurrentOrg() != null) {
                template.setOrgId(loginStaff.getCurrentOrg().getOrgid() != null
                        ? loginStaff.getCurrentOrg().getOrgid().longValue() : null);
                template.setOrgName(loginStaff.getCurrentOrg().getOrgname());
            }

            int result = fundPlanTemplateMapper.insert(template);
            if (result > 0) {
                return JsonBean.success("创建成功");
            } else {
                return new JsonBean(0, "创建失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新资金计划模板
     */
    @PutMapping("")
    @ApiOperation("更新资金计划模板")
    public String updateFundPlanTemplate(@FlexibleRequestBody TblFundPlanTemplate template) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanTemplateMapper.updateById(template);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除资金计划模板
     */
    @DeleteMapping("/{templateId}")
    @ApiOperation("删除资金计划模板")
    public String deleteFundPlanTemplate(@PathVariable Long templateId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanTemplateMapper.deleteById(templateId);
            if (result > 0) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取模板汇总信息
     */
    @GetMapping("/summary")
    @ApiOperation("获取模板汇总信息")
    public String getFundPlanTemplateSummary(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> summary = fundPlanTemplateMapper.selectTemplateSummary(params);
            return JsonBean.success(summary);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 激活资金计划模板
     */
    @PutMapping("/{templateId}/activate")
    @ApiOperation("激活资金计划模板")
    public String activateFundPlanTemplate(@PathVariable Long templateId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblFundPlanTemplate template = fundPlanTemplateMapper.selectTemplateById(templateId);
            if (template != null) {
                fundPlanTemplateMapper.updateTemplateStatus(templateId, "ACTIVE");
                return JsonBean.success("激活成功");
            } else {
                return new JsonBean(0, "模板不存在", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "激活失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 使用资金计划模板
     */
    @PostMapping("/{templateId}/use")
    @ApiOperation("使用资金计划模板")
    public String useFundPlanTemplate(@PathVariable Long templateId, @RequestParam(required = false) Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblFundPlanTemplate template = fundPlanTemplateMapper.selectTemplateById(templateId);
            if (template == null) {
                return new JsonBean(0, "模板不存在", null).toJson();
            }

            fundPlanTemplateMapper.updateTemplateUsage(templateId);

            return JsonBean.success("使用成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "使用失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出资金计划模板
     */
    @GetMapping("/{templateId}/export")
    @ApiOperation("导出资金计划模板")
    public void exportFundPlanTemplate(@PathVariable Long templateId, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                response.setStatus(401);
                return;
            }

            TblFundPlanTemplate template = fundPlanTemplateMapper.selectTemplateById(templateId);
            if (template == null) {
                response.setStatus(404);
                return;
            }

            String json = new com.fasterxml.jackson.databind.ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(template);

            String filename = "template_" + template.getTemplateNo() + ".json";
            response.setContentType("application/json;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            response.getWriter().write(json);
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }

    /**
     * 停用资金计划模板
     */
    @PutMapping("/{templateId}/deactivate")
    @ApiOperation("停用资金计划模板")
    public String deactivateFundPlanTemplate(@PathVariable Long templateId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblFundPlanTemplate template = fundPlanTemplateMapper.selectTemplateById(templateId);
            if (template != null) {
                fundPlanTemplateMapper.updateTemplateStatus(templateId, "INACTIVE");
                return JsonBean.success("停用成功");
            } else {
                return new JsonBean(0, "模板不存在", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "停用失败: " + e.getMessage(), null).toJson();
        }
    }
}
