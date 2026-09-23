package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.export.ExportBusinessRuleDTO;
import com.global.treasurer.entity.TblGtBusinessRule;
import com.global.treasurer.service.xjgl.dataRulesManage.BusinessRuleService;
import com.global.treasurer.util.excel.ExcelExport;
import com.global.treasurer.util.excel.ExcelImport;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务规则管理Controller
 * 匹配前端API路径: /qqsk/financial/data-rules/business-rule/*
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Controller
@RequestMapping({"/financial/xjgl/dataRulesManage/businessRule", "/financial/data-rules/business-rule"})
@Api(tags = "业务规则管理")
public class BusinessRuleController {
    private static final Logger log = LoggerFactory.getLogger(BusinessRuleController.class);

    @Resource
    private BusinessRuleService businessRuleService;

    @Resource
    private UserProvider userProvider;

    @PostMapping({"/getList", "/list"})
    @ResponseBody
    @ApiOperation("获取业务规则列表")
    public String getBusinessRuleList(@RequestParam(required = false) Map<String, Object> params,
                                      @RequestHeader(value = "token", required = false) String token,
                                      HttpServletResponse response) {
        try {
            // 权限验证 - 暂时注释，允许访问
            // TblStaffUtil loginStaff = userProvider.get();
            // if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
            //     JsonBean json = new JsonBean(401, "用户已失效", null);
            //     response.setCharacterEncoding("UTF-8");
            //     response.setHeader("Content-Type", "application/json;charset=UTF-8");
            //     response.getWriter().write(json.toJson());
            //     return null;
            // }

            if (params == null) params = new HashMap<>();
            int pageNum = Integer.parseInt(params.getOrDefault("page", "1").toString());
            int pageSize = Integer.parseInt(params.getOrDefault("pageSize", params.getOrDefault("limit", "20")).toString());

            PageInfo<TblGtBusinessRule> pageInfo = businessRuleService.getBusinessRuleList(params, pageNum, pageSize);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());

            return new JsonBean(1, "成功", data).toJson();
        } catch (Exception e) {
            log.error("获取业务规则列表失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping({"/create", "/add"})
    @ResponseBody
    @ApiOperation("新增业务规则")
    public String createBusinessRule(@FlexibleRequestBody TblGtBusinessRule businessRule,
                                     @RequestHeader(value = "token", required = false) String token,
                                     HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toJson());
                return null;
            }

            businessRule.setCreateBy(loginStaff.getUsername());
            businessRule.setUpdateBy(loginStaff.getUsername());

            int result = businessRuleService.createBusinessRule(businessRule);
            if (result > 0) {
                return new JsonBean(1, "新增成功", null).toJson();
            } else {
                return new JsonBean(0, "新增失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("新增业务规则失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/update")
    @ResponseBody
    @ApiOperation("更新业务规则")
    public String updateBusinessRule(@FlexibleRequestBody TblGtBusinessRule businessRule,
                                     @RequestHeader(value = "token", required = false) String token,
                                     HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toJson());
                return null;
            }

            businessRule.setUpdateBy(loginStaff.getUsername());

            int result = businessRuleService.updateBusinessRule(businessRule);
            if (result > 0) {
                return new JsonBean(1, "更新成功", null).toJson();
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新业务规则失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    @ApiOperation("删除业务规则")
    public String deleteBusinessRule(@RequestBody Map<String, Object> params,
                                     @RequestHeader(value = "token", required = false) String token,
                                     HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toJson());
                return null;
            }

            Long ruleId = Long.parseLong(params.get("ruleId").toString());
            int result = businessRuleService.deleteBusinessRule(ruleId);
            if (result > 0) {
                return new JsonBean(1, "删除成功", null).toJson();
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除业务规则失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @RequestMapping(value = {"/getStatistics", "/statistics"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @ApiOperation("获取规则统计信息")
    public String getStatistics(@RequestHeader(value = "token", required = false) String token,
                               HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toJson());
                return null;
            }

            Map<String, Object> statistics = businessRuleService.getStatistics();
            return new JsonBean(1, "成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/detail")
    @ResponseBody
    @ApiOperation("获取业务规则详情")
    public String getDetail(@RequestParam Long id, HttpServletResponse response) {
        try {
            TblGtBusinessRule rule = businessRuleService.getById(id);
            if (rule == null) {
                return new JsonBean(0, "规则不存在", null).toJson();
            }
            return new JsonBean(1, "成功", rule).toJson();
        } catch (Exception e) {
            log.error("获取业务规则详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出业务规则
     */
    @GetMapping("/export")
    @ResponseBody
    @ApiOperation("导出业务规则")
    public void exportBusinessRule(@RequestParam(required = false) String ruleType,
                                   @RequestParam(required = false) String moduleCode,
                                   @RequestParam(required = false) Integer status,
                                   HttpServletResponse response) {
        try {
            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (ruleType != null) params.put("ruleType", ruleType);
            if (moduleCode != null) params.put("moduleCode", moduleCode);
            if (status != null) params.put("status", status);

            // 查询数据
            PageInfo<TblGtBusinessRule> pageInfo = businessRuleService.getBusinessRuleList(params, 1, 10000);
            List<TblGtBusinessRule> list = pageInfo.getList();

            // 转换为导出DTO
            List<ExportBusinessRuleDTO> exportList = list.stream()
                    .map(ExportBusinessRuleDTO::fromEntity)
                    .collect(Collectors.toList());

            // 生成Excel并导出
            String filename = "业务规则_" + System.currentTimeMillis() + ".xlsx";
            try (ExcelExport export = new ExcelExport("业务规则", ExportBusinessRuleDTO.class)) {
                export.setDataList(exportList).write(response, filename);
            }
        } catch (Exception e) {
            log.error("导出业务规则失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    /**
     * 导入业务规则
     */
    @PostMapping("/import")
    @ResponseBody
    @ApiOperation("导入业务规则")
    public String importBusinessRule(@RequestPart("file") MultipartFile file,
                                     @RequestHeader(value = "token", required = false) String token,
                                     HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (file == null || file.isEmpty()) {
                return new JsonBean(0, "请选择要导入的文件", null).toJson();
            }

            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();

            try (ExcelImport excelImport = new ExcelImport(file, 1, 0)) {
                List<TblGtBusinessRule> dataList = excelImport.getDataList(TblGtBusinessRule.class);

                for (TblGtBusinessRule rule : dataList) {
                    try {
                        rule.setCreateBy(loginStaff.getUsername());
                        rule.setUpdateBy(loginStaff.getUsername());
                        rule.setCreateTime(new Date());
                        rule.setUpdateTime(new Date());

                        int result = businessRuleService.createBusinessRule(rule);
                        if (result > 0) {
                            successCount++;
                        } else {
                            failCount++;
                            errorMessages.add("保存失败: " + rule.getRuleName());
                        }
                    } catch (Exception e) {
                        failCount++;
                        errorMessages.add("导入失败[" + rule.getRuleName() + "]: " + e.getMessage());
                    }
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errorMessages", errorMessages);

            return new JsonBean(1, String.format("导入完成，成功%d条，失败%d条", successCount, failCount), result).toJson();
        } catch (Exception e) {
            log.error("导入业务规则失败", e);
            return new JsonBean(0, "导入失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 切换业务规则状态
     */
    @PostMapping("/toggleStatus")
    @ResponseBody
    @ApiOperation("切换业务规则状态")
    public String toggleBusinessRuleStatus(@RequestParam Map<String, Object> params,
                                          @RequestHeader(value = "token", required = false) String token,
                                          HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toJson());
                return null;
            }

            Long id = Long.parseLong(params.get("id").toString());
            Integer status = Integer.parseInt(params.get("status").toString());

            TblGtBusinessRule rule = new TblGtBusinessRule();
            rule.setRuleId(id);
            rule.setIsActive(status);
            rule.setUpdateBy(loginStaff.getUsername());

            int result = businessRuleService.updateBusinessRule(rule);
            if (result > 0) {
                return new JsonBean(1, "状态更新成功", null).toJson();
            } else {
                return new JsonBean(0, "状态更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("切换业务规则状态失败", e);
            return new JsonBean(0, "状态更新失败: " + e.getMessage(), null).toJson();
        }
    }
}
