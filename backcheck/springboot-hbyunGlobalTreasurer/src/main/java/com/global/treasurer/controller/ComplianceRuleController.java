package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblComplianceRule;
import com.global.treasurer.service.ComplianceRuleService;
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
import java.util.stream.Collectors;
import com.global.treasurer.dto.export.ExportComplianceRuleDTO;
import com.global.treasurer.util.excel.ExcelExport;

/**
 * 合规检查规则Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Controller
@RequestMapping({"/regulatory/rule", "/globalTreasurer/regulatory/rule"})
@Api(tags = "合规检查规则管理")
public class ComplianceRuleController {
    private static final Logger log = LoggerFactory.getLogger(ComplianceRuleController.class);

    @Resource
    private ComplianceRuleService ruleService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询合规检查规则列表")
    public String getRuleList(@RequestParam(required = false) String ruleName,
                              @RequestParam(required = false) String ruleType,
                              @RequestParam(required = false) String severityLevel,
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
            params.put("ruleName", ruleName);
            params.put("ruleType", ruleType);
            params.put("severityLevel", severityLevel);
            params.put("isEnabled", isEnabled);
            params.put("pageNum", pageNum);
            params.put("pageSize", pageSize);

            PageInfo<TblComplianceRule> pageInfo = ruleService.getRuleList(params);
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询合规检查规则列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{ruleId}")
    @ResponseBody
    @ApiOperation("根据ID获取合规检查规则详情")
    public String getRuleById(@PathVariable String ruleId,
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

            TblComplianceRule rule = ruleService.getRuleById(ruleId);
            return new JsonBean(1, "成功", rule).toJson();
        } catch (Exception e) {
            log.error("获取合规检查规则详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ResponseBody
    @ApiOperation("新增合规检查规则")
    public String addRule(@FlexibleRequestBody TblComplianceRule rule,
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

            rule.setRuleId(null);
            TblComplianceRule saved = ruleService.saveRule(rule);
            return new JsonBean(1, "新增成功", saved).toJson();
        } catch (Exception e) {
            log.error("新增合规检查规则失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ResponseBody
    @ApiOperation("修改合规检查规则")
    public String updateRule(@FlexibleRequestBody TblComplianceRule rule,
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

            if (rule.getRuleId() == null || rule.getRuleId().isEmpty()) {
                return new JsonBean(0, "规则ID不能为空", null).toJson();
            }

            TblComplianceRule saved = ruleService.saveRule(rule);
            return new JsonBean(1, "修改成功", saved).toJson();
        } catch (Exception e) {
            log.error("修改合规检查规则失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/{ruleIds}")
    @ResponseBody
    @ApiOperation("删除合规检查规则")
    public String deleteRule(@PathVariable String ruleIds,
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

            String[] ids = ruleIds.split(",");
            if (ids.length == 1) {
                ruleService.deleteRule(ids[0]);
            } else {
                ruleService.batchDeleteRules(Arrays.asList(ids));
            }
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除合规检查规则失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/executable")
    @ResponseBody
    @ApiOperation("查询可执行的规则")
    public String getExecutableRules(@RequestHeader(value = "token", required = false) String token,
                                     HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblComplianceRule> rules = ruleService.getExecutableRules();
            return new JsonBean(1, "成功", rules).toJson();
        } catch (Exception e) {
            log.error("查询可执行的规则失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/{ruleId}/status/{isEnabled}")
    @ResponseBody
    @ApiOperation("启用/停用合规检查规则")
    public String toggleRuleStatus(@PathVariable String ruleId,
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

            ruleService.toggleRuleStatus(ruleId, isEnabled);
            return new JsonBean(1, "操作成功", null).toJson();
        } catch (Exception e) {
            log.error("启用/停用合规检查规则失败", e);
            return new JsonBean(0, "操作失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{ruleId}/execute")
    @ResponseBody
    @ApiOperation("执行合规检查")
    public String executeCheck(@PathVariable String ruleId,
                               @RequestParam(required = false) String targetId,
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

            ruleService.executeCheck(ruleId, targetId);
            return new JsonBean(1, "执行成功", null).toJson();
        } catch (Exception e) {
            log.error("执行合规检查失败", e);
            return new JsonBean(0, "执行失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batch-execute")
    @ResponseBody
    @ApiOperation("批量执行合规检查")
    public String batchExecuteRules(@RequestBody List<String> ruleIds,
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

            ruleService.batchExecuteRules(ruleIds);
            return new JsonBean(1, "批量执行成功", null).toJson();
        } catch (Exception e) {
            log.error("批量执行合规检查失败", e);
            return new JsonBean(0, "批量执行失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{ruleId}/copy")
    @ResponseBody
    @ApiOperation("复制合规检查规则")
    public String copyRule(@PathVariable String ruleId,
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

            TblComplianceRule newRule = ruleService.copyRule(ruleId);
            return new JsonBean(1, "复制成功", newRule).toJson();
        } catch (Exception e) {
            log.error("复制合规检查规则失败", e);
            return new JsonBean(0, "复制失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出合规检查规则")
    public void exportRules(@RequestParam(required = false) String ruleName,
                            @RequestParam(required = false) String ruleType,
                            @RequestParam(required = false) Integer isEnabled,
                            @RequestHeader(value = "token", required = false) String token,
                            HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(401, "用户已失效", null).toJson());
                return;
            }

            Map<String, Object> params = new HashMap<>();
            params.put("ruleName", ruleName);
            params.put("ruleType", ruleType);
            params.put("isEnabled", isEnabled);

            List<TblComplianceRule> list = ruleService.exportRules(params);
            List<ExportComplianceRuleDTO> exportList = list.stream()
                    .map(ExportComplianceRuleDTO::fromEntity)
                    .collect(Collectors.toList());

            String filename = "合规检查规则_" + System.currentTimeMillis() + ".xlsx";
            try (ExcelExport export = new ExcelExport("合规检查规则", ExportComplianceRuleDTO.class)) {
                export.setDataList(exportList).write(response, filename);
            }
        } catch (Exception e) {
            log.error("导出合规检查规则失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }
}
