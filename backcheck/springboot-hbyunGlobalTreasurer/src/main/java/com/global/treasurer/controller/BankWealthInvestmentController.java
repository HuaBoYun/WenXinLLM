package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BankWealthInvestmentDTO;
import com.global.treasurer.dto.BankWealthInvestmentQueryDTO;
import com.global.treasurer.entity.TblBankWealthInvestment;
import com.global.treasurer.service.BankWealthInvestmentService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行理财投资Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@Controller
@RequestMapping({"/investment/bankwealth",
 })
@Api(tags = "银行理财投资管理")
public class BankWealthInvestmentController {
    private static final Logger log = LoggerFactory.getLogger(BankWealthInvestmentController.class);

    @Resource
    private BankWealthInvestmentService bankWealthInvestmentService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("获取银行理财投资列表")
    public String getBankWealthInvestmentList(BankWealthInvestmentQueryDTO queryDTO,
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

            PageInfo<TblBankWealthInvestment> pageInfo = bankWealthInvestmentService.getBankWealthInvestmentList(queryDTO);
            // 构建前端期望的响应格式 {rows: [...], total: xxx}
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询银行理财投资列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/getById")
    @ResponseBody
    @ApiOperation("根据ID获取银行理财投资详情")
    public String getBankWealthInvestmentById(@RequestParam Long investmentId,
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

            TblBankWealthInvestment investment = bankWealthInvestmentService.getBankWealthInvestmentById(investmentId);
            return new JsonBean(1, "成功", investment).toJson();
        } catch (Exception e) {
            log.error("获取银行理财投资详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{investmentId}")
    @ResponseBody
    @ApiOperation("根据ID获取银行理财投资详情(REST风格)")
    public String getInvestmentById(@PathVariable Long investmentId,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        return getBankWealthInvestmentById(investmentId, token, response);
    }

    @PostMapping
    @ResponseBody
    @ApiOperation("新增银行理财投资(REST风格)")
    public String createInvestment(@FlexibleRequestBody BankWealthInvestmentDTO dto,
                                  @RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        return saveBankWealthInvestment(dto, token, response);
    }

    @PutMapping
    @ResponseBody
    @ApiOperation("修改银行理财投资(REST风格)")
    public String updateInvestment(@FlexibleRequestBody BankWealthInvestmentDTO dto,
                                  @RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        return saveBankWealthInvestment(dto, token, response);
    }

    @DeleteMapping("/{investmentId}")
    @ResponseBody
    @ApiOperation("删除银行理财投资(REST风格)")
    public String deleteInvestmentById(@PathVariable Long investmentId,
                                      @RequestHeader(value = "token", required = false) String token,
                                      HttpServletResponse response) {
        return deleteBankWealthInvestment(investmentId, token, response);
    }

    @PostMapping("/save")
    @ResponseBody
    @ApiOperation("保存银行理财投资")
    public String saveBankWealthInvestment(@FlexibleRequestBody BankWealthInvestmentDTO dto,
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

            TblBankWealthInvestment investment = bankWealthInvestmentService.saveBankWealthInvestment(dto);
            return new JsonBean(1, "保存成功", investment).toJson();
        } catch (Exception e) {
            log.error("保存银行理财投资失败", e);
            return new JsonBean(0, "保存失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    @ApiOperation("删除银行理财投资")
    public String deleteBankWealthInvestment(@RequestParam Long investmentId,
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

            bankWealthInvestmentService.deleteBankWealthInvestment(investmentId);
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除银行理财投资失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batchDelete")
    @ResponseBody
    @ApiOperation("批量删除银行理财投资")
    public String batchDeleteBankWealthInvestments(List<Long> investmentIds,
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

            bankWealthInvestmentService.batchDeleteBankWealthInvestments(investmentIds);
            return new JsonBean(1, "批量删除成功", null).toJson();
        } catch (Exception e) {
            log.error("批量删除银行理财投资失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/subscribe")
    @ResponseBody
    @ApiOperation("认购银行理财")
    public String subscribeProduct(@FlexibleRequestBody BankWealthInvestmentDTO dto,
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

            bankWealthInvestmentService.subscribeProduct(dto);
            return new JsonBean(1, "认购成功", null).toJson();
        } catch (Exception e) {
            log.error("认购银行理财失败", e);
            return new JsonBean(0, "认购失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/redeem")
    @ResponseBody
    @ApiOperation("赎回银行理财")
    public String redeemProduct(@RequestParam Long investmentId,
                               @RequestParam BigDecimal redeemAmount,
                               @RequestParam String redeemDate,
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

            bankWealthInvestmentService.redeemProduct(investmentId, redeemAmount, redeemDate);
            return new JsonBean(1, "赎回成功", null).toJson();
        } catch (Exception e) {
            log.error("赎回银行理财失败", e);
            return new JsonBean(0, "赎回失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/updateValuation")
    @ResponseBody
    @ApiOperation("更新银行理财估值")
    public String updateValuation(@RequestParam Long investmentId,
                                 @RequestParam BigDecimal newValue,
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

            bankWealthInvestmentService.updateValuation(investmentId, newValue);
            return new JsonBean(1, "估值更新成功", null).toJson();
        } catch (Exception e) {
            log.error("更新估值失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/updateActualReturn")
    @ResponseBody
    @ApiOperation("更新实际收益")
    public String updateActualReturn(@RequestParam Long investmentId,
                                    @RequestParam BigDecimal actualReturn,
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

            bankWealthInvestmentService.updateActualReturn(investmentId, actualReturn);
            return new JsonBean(1, "收益更新成功", null).toJson();
        } catch (Exception e) {
            log.error("更新实际收益失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ResponseBody
    @ApiOperation("获取银行理财投资统计信息")
    public String getBankWealthStatistics(@RequestHeader(value = "token", required = false) String token,
                                         HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> statistics = bankWealthInvestmentService.getBankWealthStatistics();
            return new JsonBean(1, "成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取银行理财投资分析数据
     */
    @GetMapping("/analysis")
    @ResponseBody
    @ApiOperation("获取银行理财投资分析数据")
    public String getInvestmentAnalysis(@RequestHeader(value = "token", required = false) String token,
                                       HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 获取投资分析数据：按银行分布、按产品类型分布、收益率分析等
            Map<String, Object> analysisData = new HashMap<>();
            // TODO: 调用Service层方法获取分析数据
            // analysisData.put("bankDistribution", service.getBankDistribution());
            // analysisData.put("productTypeDistribution", service.getProductTypeDistribution());
            // analysisData.put("returnRateAnalysis", service.getReturnRateAnalysis());
            
            return new JsonBean(1, "成功", analysisData).toJson();
        } catch (Exception e) {
            log.error("获取投资分析数据失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取银行理财投资趋势分析
     */
    @GetMapping("/trend")
    @ResponseBody
    @ApiOperation("获取银行理财投资趋势分析")
    public String getInvestmentTrend(@RequestParam(required = false, defaultValue = "12") Integer months,
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

            // 获取投资趋势数据：投资金额趋势、收益率趋势、产品数量趋势等
            Map<String, Object> trendData = new HashMap<>();
            // TODO: 调用Service层方法获取趋势数据
            // trendData.put("investmentAmountTrend", service.getInvestmentAmountTrend(months));
            // trendData.put("returnRateTrend", service.getReturnRateTrend(months));
            // trendData.put("productCountTrend", service.getProductCountTrend(months));
            
            return new JsonBean(1, "成功", trendData).toJson();
        } catch (Exception e) {
            log.error("获取投资趋势分析失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出银行理财投资数据
     */
    @GetMapping("/export")
    @ApiOperation("导出银行理财投资数据")
    public void exportInvestments(BankWealthInvestmentQueryDTO queryDTO,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 查询所有符合条件的投资数据（不分页）
            queryDTO.setPageNum(null);
            queryDTO.setPageSize(null);
            PageInfo<TblBankWealthInvestment> pageInfo = bankWealthInvestmentService.getBankWealthInvestmentList(queryDTO);
            List<TblBankWealthInvestment> investments = pageInfo.getList();

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = "银行理财列表_" + System.currentTimeMillis() + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));

            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("银行理财");

            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"产品编号", "产品名称", "发行银行", "产品类型", "投资金额", "预期收益率(%)", "风险等级", "产品状态", "购买日期"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            for (int i = 0; i < investments.size(); i++) {
                TblBankWealthInvestment investment = investments.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(investment.getProductCode() != null ? investment.getProductCode() : "");
                row.createCell(1).setCellValue(investment.getProductName() != null ? investment.getProductName() : "");
                row.createCell(2).setCellValue(investment.getIssuingBank() != null ? investment.getIssuingBank() : "");
                row.createCell(3).setCellValue(investment.getProductType() != null ? investment.getProductType() : "");
                row.createCell(4).setCellValue(investment.getInvestmentAmount() != null ? investment.getInvestmentAmount().doubleValue() : 0);
                row.createCell(5).setCellValue(investment.getExpectedReturnRate() != null ? investment.getExpectedReturnRate().doubleValue() * 100 : 0);
                row.createCell(6).setCellValue(investment.getRiskLevel() != null ? investment.getRiskLevel() : "");
                row.createCell(7).setCellValue(investment.getProductStatus() != null ? investment.getProductStatus() : "");
                row.createCell(8).setCellValue(investment.getPurchaseDate() != null ? investment.getPurchaseDate().toString() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());
            workbook.close();

            log.info("导出银行理财列表成功, count: {}", investments.size());
        } catch (Exception e) {
            log.error("导出银行理财投资数据失败", e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception ex) {
                log.error("设置错误状态失败", ex);
            }
        }
    }
}
