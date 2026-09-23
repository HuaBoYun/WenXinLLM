package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblComplianceResult;
import com.global.treasurer.service.ComplianceResultService;
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
import com.global.treasurer.dto.export.ExportComplianceResultDTO;
import com.global.treasurer.util.excel.ExcelExport;

/**
 * 合规检查结果Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Controller
@RequestMapping({"/regulatory/result", "/globalTreasurer/regulatory/result"})
@Api(tags = "合规检查结果管理")
public class ComplianceResultController {
    private static final Logger log = LoggerFactory.getLogger(ComplianceResultController.class);

    @Resource
    private ComplianceResultService resultService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询合规检查结果列表")
    public String getResultList(@RequestParam(required = false) String ruleId,
                                @RequestParam(required = false) String reportId,
                                @RequestParam(required = false) String checkStatus,
                                @RequestParam(required = false) Integer isPassed,
                                @RequestParam(required = false) String severityLevel,
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
            params.put("ruleId", ruleId);
            params.put("reportId", reportId);
            params.put("checkStatus", checkStatus);
            params.put("isPassed", isPassed);
            params.put("severityLevel", severityLevel);
            params.put("pageNum", pageNum);
            params.put("pageSize", pageSize);

            PageInfo<TblComplianceResult> pageInfo = resultService.getResultList(params);
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询合规检查结果列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{resultId}")
    @ResponseBody
    @ApiOperation("根据ID获取合规检查结果详情")
    public String getResultById(@PathVariable String resultId,
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

            TblComplianceResult result = resultService.getResultById(resultId);
            return new JsonBean(1, "成功", result).toJson();
        } catch (Exception e) {
            log.error("获取合规检查结果详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ResponseBody
    @ApiOperation("新增合规检查结果")
    public String addResult(@FlexibleRequestBody TblComplianceResult result,
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

            result.setResultId(null);
            TblComplianceResult saved = resultService.saveResult(result);
            return new JsonBean(1, "新增成功", saved).toJson();
        } catch (Exception e) {
            log.error("新增合规检查结果失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ResponseBody
    @ApiOperation("修改合规检查结果")
    public String updateResult(@FlexibleRequestBody TblComplianceResult result,
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

            if (result.getResultId() == null || result.getResultId().isEmpty()) {
                return new JsonBean(0, "结果ID不能为空", null).toJson();
            }

            TblComplianceResult saved = resultService.saveResult(result);
            return new JsonBean(1, "修改成功", saved).toJson();
        } catch (Exception e) {
            log.error("修改合规检查结果失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/{resultIds}")
    @ResponseBody
    @ApiOperation("删除合规检查结果")
    public String deleteResult(@PathVariable String resultIds,
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

            String[] ids = resultIds.split(",");
            if (ids.length == 1) {
                resultService.deleteResult(ids[0]);
            } else {
                resultService.batchDeleteResults(Arrays.asList(ids));
            }
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除合规检查结果失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/attention")
    @ResponseBody
    @ApiOperation("查询需要关注的检查结果")
    public String getResultsNeedingAttention(@RequestHeader(value = "token", required = false) String token,
                                             HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblComplianceResult> results = resultService.getResultsNeedingAttention();
            return new JsonBean(1, "成功", results).toJson();
        } catch (Exception e) {
            log.error("查询需要关注的检查结果失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/immediate-action")
    @ResponseBody
    @ApiOperation("查询需要立即处理的检查结果")
    public String getResultsNeedingImmediateAction(@RequestHeader(value = "token", required = false) String token,
                                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblComplianceResult> results = resultService.getResultsNeedingImmediateAction();
            return new JsonBean(1, "成功", results).toJson();
        } catch (Exception e) {
            log.error("查询需要立即处理的检查结果失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{resultId}/process")
    @ResponseBody
    @ApiOperation("处理检查结果")
    public String processResult(@PathVariable String resultId,
                                @RequestParam(required = false) String processNote,
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

            resultService.processResult(resultId, processNote);
            return new JsonBean(1, "处理成功", null).toJson();
        } catch (Exception e) {
            log.error("处理检查结果失败", e);
            return new JsonBean(0, "处理失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{resultId}/resolve")
    @ResponseBody
    @ApiOperation("解决检查结果")
    public String resolveResult(@PathVariable String resultId,
                                @RequestParam(required = false) String resolution,
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

            resultService.resolveResult(resultId, resolution);
            return new JsonBean(1, "解决成功", null).toJson();
        } catch (Exception e) {
            log.error("解决检查结果失败", e);
            return new JsonBean(0, "解决失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{resultId}/escalate")
    @ResponseBody
    @ApiOperation("升级检查结果")
    public String escalateResult(@PathVariable String resultId,
                                 @RequestParam(required = false) String escalateReason,
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

            resultService.escalateResult(resultId, escalateReason);
            return new JsonBean(1, "升级成功", null).toJson();
        } catch (Exception e) {
            log.error("升级检查结果失败", e);
            return new JsonBean(0, "升级失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batch-process")
    @ResponseBody
    @ApiOperation("批量处理检查结果")
    public String batchProcessResults(@RequestBody List<String> resultIds,
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

            resultService.batchProcessResults(resultIds);
            return new JsonBean(1, "批量处理成功", null).toJson();
        } catch (Exception e) {
            log.error("批量处理检查结果失败", e);
            return new JsonBean(0, "批量处理失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batch-resolve")
    @ResponseBody
    @ApiOperation("批量解决检查结果")
    public String batchResolveResults(@RequestBody List<String> resultIds,
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

            resultService.batchResolveResults(resultIds);
            return new JsonBean(1, "批量解决成功", null).toJson();
        } catch (Exception e) {
            log.error("批量解决检查结果失败", e);
            return new JsonBean(0, "批量解决失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batch-escalate")
    @ResponseBody
    @ApiOperation("批量升级检查结果")
    public String batchEscalateResults(@RequestBody List<String> resultIds,
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

            resultService.batchEscalateResults(resultIds);
            return new JsonBean(1, "批量升级成功", null).toJson();
        } catch (Exception e) {
            log.error("批量升级检查结果失败", e);
            return new JsonBean(0, "批量升级失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出合规检查结果")
    public void exportResults(@RequestParam(required = false) String ruleId,
                              @RequestParam(required = false) String checkStatus,
                              @RequestParam(required = false) Integer isPassed,
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
            params.put("ruleId", ruleId);
            params.put("checkStatus", checkStatus);
            params.put("isPassed", isPassed);

            List<TblComplianceResult> list = resultService.exportResults(params);
            List<ExportComplianceResultDTO> exportList = list.stream()
                    .map(ExportComplianceResultDTO::fromEntity)
                    .collect(Collectors.toList());

            String filename = "合规检查结果_" + System.currentTimeMillis() + ".xlsx";
            try (ExcelExport export = new ExcelExport("合规检查结果", ExportComplianceResultDTO.class)) {
                export.setDataList(exportList).write(response, filename);
            }
        } catch (Exception e) {
            log.error("导出合规检查结果失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    @GetMapping("/statistics")
    @ResponseBody
    @ApiOperation("获取合规检查统计信息")
    public String getComplianceStatistics(@RequestHeader(value = "token", required = false) String token,
                                           HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> statistics = resultService.getComplianceStatistics();
            return new JsonBean(1, "成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取合规检查统计信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }
}
