package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BondInvestmentDTO;
import com.global.treasurer.dto.BondInvestmentQueryDTO;
import com.global.treasurer.entity.TblBondInvestment;
import com.global.treasurer.service.BondInvestmentService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 债券投资管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@RestController
@RequestMapping({"/investment/bond"})
@Api(tags = "债券投资管理")
public class BondInvestmentController {
    private static final Logger log = LoggerFactory.getLogger(BondInvestmentController.class);

    @Autowired
    private BondInvestmentService bondInvestmentService;

    @Autowired
    private UserProvider userProvider;

    @GetMapping("/list")
    @ApiOperation(value = "分页查询债券投资列表")
    public String list(BondInvestmentQueryDTO queryDTO) {
        try {
            PageInfo<TblBondInvestment> pageInfo = bondInvestmentService.getInvestmentList(queryDTO);

            // 构建前端期望的响应格式 {rows: [...], total: xxx}
            Map<String, Object> result = new HashMap<>();
            result.put("rows", pageInfo.getList());
            result.put("total", pageInfo.getTotal());

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询债券投资列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/{investmentId}")
    @ApiOperation(value = "获取债券投资详情")
    public String getInfo(@PathVariable Long investmentId) {
        try {
            TblBondInvestment investment = bondInvestmentService.getInvestmentById(investmentId);
            return JsonBean.success(investment);
        } catch (Exception e) {
            log.error("获取债券投资详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation(value = "新增债券投资")
    public String add(@Valid @FlexibleRequestBody BondInvestmentDTO dto) {
        try {
            dto.setInvestmentId(null);
            TblBondInvestment investment = bondInvestmentService.saveInvestment(dto);
            return JsonBean.success("新增成功", investment);
        } catch (Exception e) {
            log.error("新增债券投资失败", e);
            return JsonBean.error("新增失败: " + e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation(value = "修改债券投资")
    public String update(@Valid @FlexibleRequestBody BondInvestmentDTO dto) {
        try {
            if (dto.getInvestmentId() == null) {
                return JsonBean.error("投资ID不能为空");
            }
            TblBondInvestment investment = bondInvestmentService.saveInvestment(dto);
            return JsonBean.success("修改成功", investment);
        } catch (Exception e) {
            log.error("修改债券投资失败", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{investmentIds}")
    @ApiOperation(value = "删除债券投资")
    public String delete(@PathVariable String investmentIds) {
        try {
            String[] ids = investmentIds.split(",");
            for (String id : ids) {
                bondInvestmentService.deleteInvestment(Long.parseLong(id.trim()));
            }
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除债券投资失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/purchase")
    @ApiOperation(value = "购买债券")
    public String purchase(@RequestParam Long investmentId,
                          @RequestParam String bondCode,
                          @RequestParam BigDecimal purchasePrice,
                          @RequestParam Integer purchaseQuantity,
                          @RequestParam String purchaseDate) {
        try {
            // 获取现有投资记录
            TblBondInvestment investment = bondInvestmentService.getInvestmentById(investmentId);
            if (investment == null) {
                return JsonBean.error("投资记录不存在");
            }

            // 更新购买信息
            investment.setPurchasePrice(purchasePrice);
            investment.setPurchaseQuantity(purchaseQuantity);

            // 重新计算投资金额
            BigDecimal investmentAmount = purchasePrice.multiply(new BigDecimal(purchaseQuantity));
            investment.setInvestmentAmount(investmentAmount);

            // 保存更新
            BondInvestmentDTO dto = new BondInvestmentDTO();
            org.springframework.beans.BeanUtils.copyProperties(investment, dto);
            bondInvestmentService.saveInvestment(dto);

            return JsonBean.success("购买成功");
        } catch (Exception e) {
            log.error("购买债券失败", e);
            return JsonBean.error("购买失败: " + e.getMessage());
        }
    }

    @PostMapping("/sell/{investmentId}")
    @ApiOperation(value = "卖出债券")
    public String sell(@PathVariable Long investmentId, @RequestParam BigDecimal sellPrice, @RequestParam Integer sellQuantity) {
        try {
            bondInvestmentService.sellBond(investmentId, sellPrice, sellQuantity);
            return JsonBean.success("卖出成功");
        } catch (Exception e) {
            log.error("卖出债券失败", e);
            return JsonBean.error("卖出失败: " + e.getMessage());
        }
    }

    @PostMapping("/coupon/{investmentId}")
    @ApiOperation(value = "处理债券付息")
    public String processCoupon(@PathVariable Long investmentId,
                               @RequestParam BigDecimal couponAmount,
                               @RequestParam String paymentDate) {
        try {
            TblBondInvestment investment = bondInvestmentService.getInvestmentById(investmentId);
            if (investment == null) {
                return JsonBean.error("投资记录不存在");
            }

            // 更新应计利息
            BigDecimal currentAccumulated = investment.getAccumulatedInterest() != null
                ? investment.getAccumulatedInterest() : BigDecimal.ZERO;
            investment.setAccumulatedInterest(currentAccumulated.add(couponAmount));
            investment.setUpdatedTime(new java.sql.Timestamp(System.currentTimeMillis()));

            // 使用service保存更新
            BondInvestmentDTO dto = new BondInvestmentDTO();
            org.springframework.beans.BeanUtils.copyProperties(investment, dto);
            bondInvestmentService.saveInvestment(dto);
            return JsonBean.success("付息处理成功");
        } catch (Exception e) {
            log.error("处理付息失败", e);
            return JsonBean.error("付息处理失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateValuation/{investmentId}")
    @ApiOperation(value = "更新债券估值")
    public String updateValuation(@PathVariable Long investmentId,
                                  @RequestParam BigDecimal newPrice,
                                  @RequestParam(required = false) String newYield) {
        try {
            TblBondInvestment investment = bondInvestmentService.getInvestmentById(investmentId);
            if (investment == null) {
                return JsonBean.error("投资记录不存在");
            }

            // 更新收益率（如果提供了有效值且不是NaN）
            if (newYield != null && !newYield.isEmpty() && !"NaN".equalsIgnoreCase(newYield)) {
                try {
                    BigDecimal yieldValue = new BigDecimal(newYield);
                    investment.setYieldToMaturity(yieldValue);
                } catch (NumberFormatException e) {
                    log.warn("无效的收益率值: {}", newYield);
                }
            }

            // 计算新的市值：新价格 × 购买数量
            if (investment.getPurchaseQuantity() != null) {
                BigDecimal quantity = new BigDecimal(investment.getPurchaseQuantity());
                BigDecimal newMarketValue = newPrice.multiply(quantity);
                investment.setCurrentMarketValue(newMarketValue);

                // 计算未实现损益：当前市值 - 投资金额
                if (investment.getInvestmentAmount() != null) {
                    BigDecimal unrealizedPnl = newMarketValue.subtract(investment.getInvestmentAmount());
                    investment.setUnrealizedPnl(unrealizedPnl);
                }
            }

            investment.setUpdatedTime(new java.sql.Timestamp(System.currentTimeMillis()));

            // 使用service保存更新
            BondInvestmentDTO dto = new BondInvestmentDTO();
            org.springframework.beans.BeanUtils.copyProperties(investment, dto);
            bondInvestmentService.saveInvestment(dto);

            return JsonBean.success("估值更新成功");
        } catch (Exception e) {
            log.error("更新估值失败", e);
            return JsonBean.error("估值更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateMarketValue/{investmentId}")
    @ApiOperation(value = "更新市值")
    public String updateMarketValue(@PathVariable Long investmentId, @RequestParam BigDecimal marketValue) {
        try {
            bondInvestmentService.updateMarketValue(investmentId, marketValue);
            return JsonBean.success("更新成功");
        } catch (Exception e) {
            log.error("更新市值失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics")
    @ApiOperation(value = "获取投资统计信息")
    public String statistics() {
        try {
            Map<String, Object> statistics = bondInvestmentService.getInvestmentStatistics();
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("获取投资统计信息失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/maturitySoon")
    @ApiOperation(value = "获取即将到期的债券")
    public String maturitySoon(@RequestParam(required = false, defaultValue = "30") Integer days) {
        try {
            List<TblBondInvestment> list = bondInvestmentService.getMaturitySoon(days);
            return JsonBean.success(list);
        } catch (Exception e) {
            log.error("获取即将到期的债券失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/analysisByType")
    @ApiOperation(value = "获取投资分析数据（按债券类型）")
    public String analysisByType() {
        try {
            List<Map<String, Object>> analysis = bondInvestmentService.getInvestmentAnalysisByType();
            return JsonBean.success(analysis);
        } catch (Exception e) {
            log.error("获取投资分析数据失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出投资数据")
    public void export(BondInvestmentQueryDTO queryDTO,
                      @RequestHeader(value = "token", required = false) String token,
                      HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            List<TblBondInvestment> investments = bondInvestmentService.exportInvestments(queryDTO);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = "债券投资列表_" + System.currentTimeMillis() + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));

            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("债券投资");

            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"投资编号", "债券代码", "债券名称", "债券类型", "投资金额", "购买价格", "当前价格", "投资状态", "购买日期"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            for (int i = 0; i < investments.size(); i++) {
                TblBondInvestment investment = investments.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(investment.getInvestmentId() != null ? String.valueOf(investment.getInvestmentId()) : "");
                row.createCell(1).setCellValue(investment.getBondCode() != null ? investment.getBondCode() : "");
                row.createCell(2).setCellValue(investment.getBondName() != null ? investment.getBondName() : "");
                row.createCell(3).setCellValue(investment.getBondType() != null ? investment.getBondType() : "");
                row.createCell(4).setCellValue(investment.getInvestmentAmount() != null ? investment.getInvestmentAmount().doubleValue() : 0);
                row.createCell(5).setCellValue(investment.getPurchasePrice() != null ? investment.getPurchasePrice().doubleValue() : 0);
                row.createCell(6).setCellValue(investment.getCurrentPrice() != null ? investment.getCurrentPrice().doubleValue() : 0);
                row.createCell(7).setCellValue(investment.getInvestmentStatus() != null ? investment.getInvestmentStatus() : "");
                row.createCell(8).setCellValue(investment.getPurchaseDate() != null ? investment.getPurchaseDate().toString() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());
            workbook.close();

            log.info("导出债券投资列表成功, count: {}", investments.size());
        } catch (Exception e) {
            log.error("导出债券投资列表失败", e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception ex) {
                log.error("设置错误状态失败", ex);
            }
        }
    }
}

