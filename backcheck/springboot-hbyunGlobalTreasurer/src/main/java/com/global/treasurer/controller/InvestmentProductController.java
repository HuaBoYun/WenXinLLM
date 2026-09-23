package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.InvestmentProductDTO;
import com.global.treasurer.dto.InvestmentProductQueryDTO;
import com.global.treasurer.entity.TblInvestmentProduct;
import com.global.treasurer.service.InvestmentProductService;
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
 * 投资产品Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@Controller
@RequestMapping({"/investment/product", "/centralaudit/investment/product"})
@Api(tags = "投资产品管理")
public class InvestmentProductController {

    private static final Logger log = LoggerFactory.getLogger(InvestmentProductController.class);

    @Resource
    private InvestmentProductService investmentProductService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("获取投资产品列表")
    public String getInvestmentProductList(InvestmentProductQueryDTO queryDTO,
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

            PageInfo<TblInvestmentProduct> pageInfo = investmentProductService.getProductList(queryDTO);
            // 构建前端期望的响应格式 {rows: [...], total: xxx}
            // 将分页数据放入 data 字段中,前端通过 response.rows 和 response.total 访问
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询投资产品列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/getById")
    @ResponseBody
    @ApiOperation("根据ID获取投资产品详情")
    public String getInvestmentProductById(@RequestParam Long productId,
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

            TblInvestmentProduct product = investmentProductService.getProductById(productId);
            return new JsonBean(1, "成功", product).toJson();
        } catch (Exception e) {
            log.error("获取投资产品详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/save")
    @ResponseBody
    @ApiOperation("保存投资产品")
    public String saveInvestmentProduct(@FlexibleRequestBody InvestmentProductDTO dto,
                                       @RequestHeader(value = "token", required = false) String token,
                                       HttpServletResponse response) {
        try {
            // 添加调试日志
            log.info("=== 接收到的DTO数据 ===");
            log.info("DTO对象: {}", dto);
            log.info("productCode: {}", dto.getProductCode());
            log.info("productName: {}", dto.getProductName());
            log.info("productType: {}", dto.getProductType());
            log.info("issuer: {}", dto.getIssuer());
            log.info("riskLevel: {}", dto.getRiskLevel());
            log.info("expectedReturnRate: {}", dto.getExpectedReturnRate());
            log.info("minInvestmentAmount: {}", dto.getMinInvestmentAmount());
            log.info("launchDate: {}", dto.getLaunchDate());
            
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblInvestmentProduct product = investmentProductService.saveProduct(dto);
            return new JsonBean(1, "保存成功", product).toJson();
        } catch (Exception e) {
            log.error("保存投资产品失败", e);
            return new JsonBean(0, "保存失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    @ApiOperation("删除投资产品")
    public String deleteInvestmentProduct(@RequestParam Long productId,
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

            investmentProductService.deleteProduct(productId);
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除投资产品失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batchDelete")
    @ResponseBody
    @ApiOperation("批量删除投资产品")
    public String batchDeleteInvestmentProducts(List<Long> productIds,
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

            for (Long productId : productIds) {
                investmentProductService.deleteProduct(productId);
            }
            return new JsonBean(1, "批量删除成功", null).toJson();
        } catch (Exception e) {
            log.error("批量删除投资产品失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/launch")
    @ResponseBody
    @ApiOperation("上架产品")
    public String launchProduct(@RequestParam Long productId,
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

            investmentProductService.launchProduct(productId);
            return new JsonBean(1, "上架成功", null).toJson();
        } catch (Exception e) {
            log.error("上架产品失败", e);
            return new JsonBean(0, "上架失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/suspend")
    @ResponseBody
    @ApiOperation("下架产品")
    public String suspendProduct(@RequestParam Long productId,
                                @RequestParam(required = false) String suspendReason,
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

            investmentProductService.suspendProduct(productId, suspendReason);
            return new JsonBean(1, "下架成功", null).toJson();
        } catch (Exception e) {
            log.error("下架产品失败", e);
            return new JsonBean(0, "下架失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/updateNav")
    @ResponseBody
    @ApiOperation("更新产品净值")
    public String updateNetValue(@RequestParam Long productId,
                               @RequestParam BigDecimal netValue,
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

            investmentProductService.updateNetValue(productId, netValue);
            return new JsonBean(1, "更新净值成功", null).toJson();
        } catch (Exception e) {
            log.error("更新产品净值失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ResponseBody
    @ApiOperation("获取产品统计信息")
    public String getProductStatistics(@RequestHeader(value = "token", required = false) String token,
                                     HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> statistics = investmentProductService.getProductStatistics();
            return new JsonBean(1, "成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取产品统计信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取产品分析数据
     */
    @GetMapping("/analysis")
    @ResponseBody
    @ApiOperation("获取产品分析数据")
    public String getProductAnalysis(@RequestHeader(value = "token", required = false) String token,
                                    HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> analysis = investmentProductService.getProductAnalysis();
            return new JsonBean(1, "成功", analysis).toJson();
        } catch (Exception e) {
            log.error("获取产品分析数据失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取产品趋势分析
     */
    @GetMapping("/trend")
    @ResponseBody
    @ApiOperation("获取产品趋势分析")
    public String getProductTrend(@RequestParam(required = false, defaultValue = "12") Integer months,
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

            List<Map<String, Object>> trend = investmentProductService.getProductTrend(months);
            return new JsonBean(1, "成功", trend).toJson();
        } catch (Exception e) {
            log.error("获取产品趋势分析失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出产品数据
     */
    @GetMapping("/export")
    @ApiOperation("导出产品数据")
    public void exportProducts(InvestmentProductQueryDTO queryDTO,
                              @RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 查询要导出的数据
            List<TblInvestmentProduct> products = investmentProductService.exportProducts(queryDTO);

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = "投资产品列表_" + System.currentTimeMillis() + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));

            // 使用POI导出Excel
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("投资产品");

            // 创建表头
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"产品代码", "产品名称", "产品类型", "发行机构", "风险等级", "预期收益率(%)", "最低投资金额", "产品状态", "发行日期"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 填充数据
            for (int i = 0; i < products.size(); i++) {
                TblInvestmentProduct product = products.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(product.getProductCode() != null ? product.getProductCode() : "");
                row.createCell(1).setCellValue(product.getProductName() != null ? product.getProductName() : "");
                row.createCell(2).setCellValue(product.getProductType() != null ? product.getProductType() : "");
                row.createCell(3).setCellValue(product.getIssuer() != null ? product.getIssuer() : "");
                row.createCell(4).setCellValue(product.getRiskLevel() != null ? product.getRiskLevel() : "");
                row.createCell(5).setCellValue(product.getExpectedReturnRate() != null ? product.getExpectedReturnRate().doubleValue() * 100 : 0);
                row.createCell(6).setCellValue(product.getMinInvestmentAmount() != null ? product.getMinInvestmentAmount().doubleValue() : 0);
                row.createCell(7).setCellValue(product.getProductStatus() != null ? product.getProductStatus() : "");
                row.createCell(8).setCellValue(product.getLaunchDate() != null ? product.getLaunchDate().toString() : "");
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入响应流
            workbook.write(response.getOutputStream());
            workbook.close();

            log.info("导出投资产品列表成功, count: {}", products.size());
        } catch (Exception e) {
            log.error("导出投资产品列表失败", e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception ex) {
                log.error("设置错误状态失败", ex);
            }
        }
    }
}
