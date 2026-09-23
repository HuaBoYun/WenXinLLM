package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingCostDTO;
import com.global.treasurer.dto.FinancingCostQueryDTO;
import com.global.treasurer.entity.TblFinancingCostAnalysis;
import com.global.treasurer.service.FinancingCostService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 融资成本分析Controller
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
@Controller
@RequestMapping({"/rzgl/financing-cost", "/financial/rzgl/financing-cost", "/centralaudit/rzgl/financing-cost",
                "/rzgl/cost", "/financial/rzgl/cost", "/centralaudit/rzgl/cost",
               })
@Api(tags = "融资成本分析管理")
public class FinancingCostController {
    private static final Logger log = LoggerFactory.getLogger(FinancingCostController.class);

    @Resource
    private FinancingCostService financingCostService;

    @Resource
    private UserProvider userProvider;

    @PostMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询融资成本列表")
    public String getCostList(@FlexibleRequestBody FinancingCostQueryDTO queryDTO,
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

            log.info("查询成本分析列表参数: financingType={}, companyId={}, pageNum={}, pageSize={}",
                queryDTO.getFinancingType(), queryDTO.getCompanyId(), queryDTO.getPageNum(), queryDTO.getPageSize());

            PageInfo<TblFinancingCostAnalysis> pageInfo = financingCostService.getCostList(queryDTO);
            // 构建前端期望的响应格式 {rows: [...], total: xxx}
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询融资成本列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    @ApiOperation("根据ID获取融资成本详情")
    public String getCostById(@PathVariable Long id,
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

            TblFinancingCostAnalysis cost = financingCostService.getCostById(id);
            return new JsonBean(1, "成功", cost).toJson();
        } catch (Exception e) {
            log.error("获取融资成本详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/analysis")
    @ResponseBody
    @ApiOperation("执行成本分析")
    public String performAnalysis(@RequestParam Long financingId,
                                  @RequestParam String analysisDate,
                                  @RequestParam(defaultValue = "MONTH") String periodType,
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

            TblFinancingCostAnalysis result = financingCostService.performAnalysis(financingId, analysisDate, periodType);
            return new JsonBean(1, "分析成功", result).toJson();
        } catch (Exception e) {
            log.error("执行成本分析失败", e);
            return new JsonBean(0, "分析失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/compare")
    @ResponseBody
    @ApiOperation("成本对比")
    public String getCostComparison(@RequestParam(required = false) Long companyId,
                                    @RequestParam(required = false) String periodType,
                                    @RequestParam(required = false) String periodValue,
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
            if (companyId != null) {
                params.put("companyId", companyId);
            }
            if (periodType != null && !periodType.isEmpty()) {
                params.put("periodType", periodType);
            }
            if (periodValue != null && !periodValue.isEmpty()) {
                params.put("periodValue", periodValue);
            }

            List<Map<String, Object>> comparison = financingCostService.getCostComparison(params);
            return new JsonBean(1, "成功", comparison).toJson();
        } catch (Exception e) {
            log.error("成本对比失败", e);
            return new JsonBean(0, "对比失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/overview")
    @ResponseBody
    @ApiOperation("成本概览(前端专用)")
    public String getCostOverview(@RequestParam(required = false) Map<String, Object> params,
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

            // 从请求体中获取参数
            Long companyId = null;
            String periodType = null;
            String periodValue = null;
            if (params != null) {
                if (params.get("companyId") != null) {
                    companyId = Long.parseLong(params.get("companyId").toString());
                }
                periodType = (String) params.get("periodType");
                periodValue = (String) params.get("periodValue");
            }

            Map<String, Object> statistics = financingCostService.getCostStatistics(companyId, periodType, periodValue);

            // 转换字段名以匹配前端期望的格式
            Map<String, Object> result = new HashMap<>();
            result.put("totalFinancingAmount", statistics.get("totalPrincipal"));
            result.put("totalCost", statistics.get("totalCost"));
            result.put("averageCostRate", statistics.get("avgCostRate"));
            result.put("costSaving", statistics.getOrDefault("costSaving", 0));
            result.put("monthlyTrend", statistics.getOrDefault("monthlyTrend", "稳定"));
            result.put("optimizationRate", statistics.getOrDefault("optimizationRate", 0));

            return new JsonBean(1, "成功", result).toJson();
        } catch (Exception e) {
            log.error("获取成本概览失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ResponseBody
    @ApiOperation("成本统计")
    public String getCostStatistics(@RequestParam(required = false) Long companyId,
                                    @RequestParam(required = false) String periodType,
                                    @RequestParam(required = false) String periodValue,
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

            Map<String, Object> statistics = financingCostService.getCostStatistics(companyId, periodType, periodValue);
            return new JsonBean(1, "成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取成本统计失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ResponseBody
    @ApiOperation("导出成本报表")
    public String exportCostReport(@RequestParam(required = false) Long companyId,
                                   @RequestParam(required = false) String financingType,
                                   @RequestParam(required = false) String currencyCode,
                                   @RequestParam(required = false) String periodType,
                                   @RequestParam(required = false) String periodValue,
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
            if (companyId != null) {
                params.put("companyId", companyId);
            }
            if (financingType != null && !financingType.isEmpty()) {
                params.put("financingType", financingType);
            }
            if (currencyCode != null && !currencyCode.isEmpty()) {
                params.put("currencyCode", currencyCode);
            }
            if (periodType != null && !periodType.isEmpty()) {
                params.put("periodType", periodType);
            }
            if (periodValue != null && !periodValue.isEmpty()) {
                params.put("periodValue", periodValue);
            }

            List<TblFinancingCostAnalysis> data = financingCostService.exportCostReport(params);
            return new JsonBean(1, "导出成功", data).toJson();
        } catch (Exception e) {
            log.error("导出成本报表失败", e);
            return new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/create")
    @ResponseBody
    @ApiOperation("创建融资成本")
    public String createCost(@FlexibleRequestBody FinancingCostDTO dto,
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

            log.info("创建成本分析: companyName={}, financingType={}, principalAmount={}",
                dto.getCompanyName(), dto.getFinancingType(), dto.getPrincipalAmount());

            TblFinancingCostAnalysis cost = financingCostService.saveCost(dto);
            return new JsonBean(1, "创建成功", cost).toJson();
        } catch (Exception e) {
            log.error("创建融资成本失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/update")
    @ResponseBody
    @ApiOperation("更新融资成本")
    public String updateCost(@FlexibleRequestBody FinancingCostDTO dto,
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

            if (dto.getAnalysisId() == null) {
                return new JsonBean(0, "分析ID不能为空", null).toJson();
            }

            log.info("更新成本分析: analysisId={}, companyName={}, financingType={}",
                dto.getAnalysisId(), dto.getCompanyName(), dto.getFinancingType());

            TblFinancingCostAnalysis cost = financingCostService.saveCost(dto);
            return new JsonBean(1, "更新成功", cost).toJson();
        } catch (Exception e) {
            log.error("更新融资成本失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    @ApiOperation("删除融资成本")
    public String deleteCost(@PathVariable String id,
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

            // 支持批量删除：ID可以是单个或逗号分隔的多个
            String[] idArray = id.split(",");
            if (idArray.length == 1) {
                // 单个删除
                financingCostService.deleteCost(Long.parseLong(id.trim()));
            } else {
                // 批量删除
                List<Long> ids = new ArrayList<>();
                for (String idStr : idArray) {
                    ids.add(Long.parseLong(idStr.trim()));
                }
                financingCostService.batchDeleteCosts(ids);
            }
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (NumberFormatException e) {
            log.error("删除融资成本失败: ID格式错误", e);
            return new JsonBean(0, "删除失败: ID格式错误", null).toJson();
        } catch (Exception e) {
            log.error("删除融资成本失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }
}
