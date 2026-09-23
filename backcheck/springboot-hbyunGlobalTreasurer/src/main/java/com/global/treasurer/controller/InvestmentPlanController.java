package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.InvestmentPlanDTO;
import com.global.treasurer.dto.InvestmentPlanQueryDTO;
import com.global.treasurer.entity.TblInvestmentPlan;
import com.global.treasurer.service.InvestmentPlanService;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投资计划Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@Controller
@RequestMapping({"/investment/plan", "/centralaudit/investment/plan"})
@Api(tags = "投资计划管理")
public class InvestmentPlanController {

    private static final Logger log = LoggerFactory.getLogger(InvestmentPlanController.class);

    @Resource
    private InvestmentPlanService investmentPlanService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("获取投资计划列表")
    public String getInvestmentPlanList(InvestmentPlanQueryDTO queryDTO,
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

            PageInfo<TblInvestmentPlan> pageInfo = investmentPlanService.getPlanList(queryDTO);
            // 构建前端期望的响应格式 {rows: [...], total: xxx}
            // 将分页数据放入 data 字段中,前端通过 response.rows 和 response.total 访问
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询投资计划列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/getById")
    @ResponseBody
    @ApiOperation("根据ID获取投资计划详情")
    public String getInvestmentPlanById(@RequestParam Long planId,
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

            TblInvestmentPlan plan = investmentPlanService.getPlanById(planId);
            return new JsonBean(1, "成功", plan).toJson();
        } catch (Exception e) {
            log.error("获取投资计划详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/save")
    @ResponseBody
    @ApiOperation("保存投资计划")
    public String saveInvestmentPlan(@FlexibleRequestBody InvestmentPlanDTO dto,
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

            TblInvestmentPlan plan = investmentPlanService.savePlan(dto);
            return new JsonBean(1, "保存成功", plan).toJson();
        } catch (Exception e) {
            log.error("保存投资计划失败", e);
            return new JsonBean(0, "保存失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    @ApiOperation("删除投资计划")
    public String deleteInvestmentPlan(@RequestParam Long planId,
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

            investmentPlanService.deletePlan(planId);
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除投资计划失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batchDelete")
    @ResponseBody
    @ApiOperation("批量删除投资计划")
    public String batchDeleteInvestmentPlans(List<Long> planIds,
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

            investmentPlanService.batchDeletePlans(planIds);
            return new JsonBean(1, "批量删除成功", null).toJson();
        } catch (Exception e) {
            log.error("批量删除投资计划失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/submit")
    @ResponseBody
    @ApiOperation("提交计划")
    public String submitPlan(@RequestParam Long planId,
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

            investmentPlanService.submitPlan(planId);
            return new JsonBean(1, "提交成功", null).toJson();
        } catch (Exception e) {
            log.error("提交计划失败", e);
            return new JsonBean(0, "提交失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/approve")
    @ResponseBody
    @ApiOperation("审批通过计划")
    public String approvePlan(@RequestParam Long planId,
                             @RequestParam(required = false) String approvalComments,
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

            investmentPlanService.approvePlan(planId, approvalComments);
            return new JsonBean(1, "审批成功", null).toJson();
        } catch (Exception e) {
            log.error("审批计划失败", e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/reject")
    @ResponseBody
    @ApiOperation("驳回计划")
    public String rejectPlan(@RequestParam Long planId,
                            @RequestParam(required = false) String rejectionReason,
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

            investmentPlanService.rejectPlan(planId, rejectionReason);
            return new JsonBean(1, "驳回成功", null).toJson();
        } catch (Exception e) {
            log.error("驳回计划失败", e);
            return new JsonBean(0, "驳回失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/execute")
    @ResponseBody
    @ApiOperation("开始执行计划")
    public String executePlan(@RequestParam Long planId,
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

            investmentPlanService.executePlan(planId);
            return new JsonBean(1, "执行成功", null).toJson();
        } catch (Exception e) {
            log.error("执行计划失败", e);
            return new JsonBean(0, "执行失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/complete")
    @ResponseBody
    @ApiOperation("完成计划")
    public String completePlan(@RequestParam Long planId,
                              @RequestParam(required = false) String completionNotes,
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

            investmentPlanService.completePlan(planId, completionNotes);
            return new JsonBean(1, "完成成功", null).toJson();
        } catch (Exception e) {
            log.error("完成计划失败", e);
            return new JsonBean(0, "完成失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/cancel")
    @ResponseBody
    @ApiOperation("取消计划")
    public String cancelPlan(@RequestParam Long planId,
                            @RequestParam(required = false) String cancelReason,
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

            investmentPlanService.cancelPlan(planId, cancelReason);
            return new JsonBean(1, "取消成功", null).toJson();
        } catch (Exception e) {
            log.error("取消计划失败", e);
            return new JsonBean(0, "取消失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/updateInvestedAmount")
    @ResponseBody
    @ApiOperation("更新已投资金额")
    public String updateInvestedAmount(@RequestParam Long planId,
                                      @RequestParam BigDecimal investedAmount,
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

            investmentPlanService.updateInvestedAmount(planId, investedAmount);
            return new JsonBean(1, "更新成功", null).toJson();
        } catch (Exception e) {
            log.error("更新已投资金额失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/updateActualReturnRate")
    @ResponseBody
    @ApiOperation("更新实际收益率")
    public String updateActualReturnRate(@RequestParam Long planId,
                                        @RequestParam BigDecimal actualReturnRate,
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

            investmentPlanService.updateActualReturnRate(planId, actualReturnRate);
            return new JsonBean(1, "更新成功", null).toJson();
        } catch (Exception e) {
            log.error("更新实际收益率失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ResponseBody
    @ApiOperation("获取计划统计信息")
    public String getPlanStatistics(@RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> statistics = investmentPlanService.getPlanStatistics();
            return new JsonBean(1, "成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/nearExpiry")
    @ResponseBody
    @ApiOperation("获取即将到期的计划")
    public String getNearExpiryPlans(@RequestParam(defaultValue = "7") Integer days,
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

            List<TblInvestmentPlan> plans = investmentPlanService.getNearExpiryPlans(days);
            return new JsonBean(1, "成功", plans).toJson();
        } catch (Exception e) {
            log.error("获取即将到期计划失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/overdue")
    @ResponseBody
    @ApiOperation("获取逾期计划")
    public String getOverduePlans(@RequestHeader(value = "token", required = false) String token,
                                 HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblInvestmentPlan> plans = investmentPlanService.getOverduePlans();
            return new JsonBean(1, "成功", plans).toJson();
        } catch (Exception e) {
            log.error("获取逾期计划失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出投资计划数据
     */
    @GetMapping("/export")
    @ApiOperation("导出投资计划")
    public void exportPlans(InvestmentPlanQueryDTO queryDTO,
                           @RequestHeader(value = "token", required = false) String token,
                           HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 查询要导出的数据
            List<TblInvestmentPlan> plans = investmentPlanService.exportPlans(queryDTO);

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = "投资计划列表_" + System.currentTimeMillis() + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));

            // 使用POI导出Excel
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("投资计划");

            // 创建表头
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"计划编号", "计划名称", "投资类型", "计划金额", "预期收益率(%)", "风险等级", "计划状态", "开始日期", "结束日期"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 填充数据
            for (int i = 0; i < plans.size(); i++) {
                TblInvestmentPlan plan = plans.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(plan.getPlanNo() != null ? plan.getPlanNo() : "");
                row.createCell(1).setCellValue(plan.getPlanName() != null ? plan.getPlanName() : "");
                row.createCell(2).setCellValue(plan.getInvestmentType() != null ? plan.getInvestmentType() : "");
                row.createCell(3).setCellValue(plan.getPlanAmount() != null ? plan.getPlanAmount().doubleValue() : 0);
                row.createCell(4).setCellValue(plan.getExpectedReturnRate() != null ? plan.getExpectedReturnRate().doubleValue() * 100 : 0);
                row.createCell(5).setCellValue(plan.getRiskLevel() != null ? plan.getRiskLevel() : "");
                row.createCell(6).setCellValue(plan.getPlanStatus() != null ? plan.getPlanStatus() : "");
                row.createCell(7).setCellValue(plan.getPlanStartDate() != null ? plan.getPlanStartDate().toString() : "");
                row.createCell(8).setCellValue(plan.getPlanEndDate() != null ? plan.getPlanEndDate().toString() : "");
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入响应流
            workbook.write(response.getOutputStream());
            workbook.close();

            log.info("导出投资计划列表成功, count: {}", plans.size());
        } catch (Exception e) {
            log.error("导出投资计划列表失败", e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception ex) {
                log.error("设置错误状态失败", ex);
            }
        }
    }

    /**
     * 批量更新计划状态
     */
    @PostMapping("/batch/status")
    @ResponseBody
    @ApiOperation("批量更新状态")
    public String batchUpdateStatus(@RequestParam Map<String, Object> params,
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

            List<Long> planIds = (List<Long>) params.get("planIds");
            String status = (String) params.get("status");
            
            if (planIds == null || planIds.isEmpty()) {
                return new JsonBean(0, "请选择要更新的计划", null).toJson();
            }
            
            // 调用Service层批量更新
            for (Long planId : planIds) {
                TblInvestmentPlan plan = investmentPlanService.getPlanById(planId);
                if (plan != null) {
                    plan.setPlanStatus(status);
                    plan.setUpdatedByName(loginStaff.getRealname());
                    InvestmentPlanDTO dto = new InvestmentPlanDTO();
                    // 复制属性...
                    investmentPlanService.savePlan(dto);
                }
            }
            
            return new JsonBean(1, "批量更新成功", null).toJson();
        } catch (Exception e) {
            log.error("批量更新状态失败", e);
            return new JsonBean(0, "批量更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取计划提醒信息
     */
    @GetMapping("/reminders")
    @ResponseBody
    @ApiOperation("获取提醒信息")
    public String getReminders(@RequestParam(defaultValue = "7") Integer days,
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

            List<TblInvestmentPlan> plans = investmentPlanService.getNearExpiryPlans(days);
            return new JsonBean(1, "成功", plans).toJson();
        } catch (Exception e) {
            log.error("获取提醒信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取计划预警信息
     */
    @GetMapping("/alerts")
    @ResponseBody
    @ApiOperation("获取预警信息")
    public String getAlerts(@RequestHeader(value = "token", required = false) String token,
                           HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 获取逾期计划作为预警
            List<TblInvestmentPlan> overduePlans = investmentPlanService.getOverduePlans();
            List<TblInvestmentPlan> nearExpiryPlans = investmentPlanService.getNearExpiryPlans(7);
            
            Map<String, Object> alerts = new HashMap<>();
            alerts.put("overduePlans", overduePlans);
            alerts.put("nearExpiryPlans", nearExpiryPlans);
            alerts.put("overdueCount", overduePlans.size());
            alerts.put("nearExpiryCount", nearExpiryPlans.size());
            
            return new JsonBean(1, "成功", alerts).toJson();
        } catch (Exception e) {
            log.error("获取预警信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取投资建议
     */
    @GetMapping("/recommendations")
    @ResponseBody
    @ApiOperation("获取投资建议")
    public String getRecommendations(@RequestHeader(value = "token", required = false) String token,
                                    HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 获取统计信息并生成建议
            Map<String, Object> statistics = investmentPlanService.getPlanStatistics();

            // 简单的建议逻辑
            Map<String, Object> recommendations = new HashMap<>();
            recommendations.put("totalPlans", statistics.get("totalPlans"));
            recommendations.put("suggestions", Arrays.asList(
                "建议关注即将到期的投资计划",
                "建议优化投资组合,降低风险",
                "建议增加固定收益类投资比例"
            ));

            return new JsonBean(1, "成功", recommendations).toJson();
        } catch (Exception e) {
            log.error("获取投资建议失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }
}
