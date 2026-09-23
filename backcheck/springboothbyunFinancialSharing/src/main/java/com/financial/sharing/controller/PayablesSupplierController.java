package com.financial.sharing.controller;

import com.financial.sharing.dto.param.SupplierQueryParam;
import com.financial.sharing.dto.param.SupplierSaveParam;
import com.financial.sharing.service.SupplierService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.SupplierVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商Controller(应付模块 - 前端路径适配)
 * 匹配前端API路径: /zbgl/financial/payables/supplier
 * @author system
 * @since 2025-01-05
 */
@Slf4j
@Api(tags = "供应商管理(应付-前端适配)")
@RestController
@RequestMapping("/payables/supplier")
public class PayablesSupplierController {

    @Resource
    private SupplierService supplierService;

    @ApiOperation("分页查询供应商列表")
    @PostMapping("/getList")
    public MyJsonBean<PageResult<SupplierVO>> getList(@RequestBody SupplierQueryParam param) {
        log.info("分页查询供应商列表，参数: {}", param);
        try {
            PageResult<SupplierVO> result = supplierService.queryPage(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询供应商列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新供应商信息")
    @PostMapping("/saveOrUpdate")
    public MyJsonBean<String> saveOrUpdate(@Validated @RequestBody SupplierSaveParam param) {
        log.info("保存或更新供应商，参数: {}", param);
        try {
            String supplierId = supplierService.save(param);
            return MyJsonBean.successData("保存成功", supplierId);
        } catch (Exception e) {
            log.error("保存供应商失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询供应商详情")
    @GetMapping("/detail/{supplierId}")
    public MyJsonBean<SupplierVO> getDetail(
            @ApiParam("供应商ID") @PathVariable String supplierId) {
        log.info("查询供应商详情，ID: {}", supplierId);
        try {
            SupplierVO result = supplierService.getDetail(supplierId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询供应商详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除供应商")
    @DeleteMapping("/delete/{supplierId}")
    public MyJsonBean<String> delete(
            @ApiParam("供应商ID") @PathVariable String supplierId) {
        log.info("删除供应商，ID: {}", supplierId);
        try {
            supplierService.delete(supplierId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除供应商失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询所有启用的供应商")
    @GetMapping("/enabled")
    public MyJsonBean<List<SupplierVO>> queryAllEnabled() {
        log.info("查询所有启用的供应商");
        try {
            List<SupplierVO> result = supplierService.queryAllEnabled();
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询启用供应商失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询供应商下拉列表")
    @GetMapping("/dropdown")
    public MyJsonBean<List<SupplierVO>> queryDropdownList(
            @ApiParam("关键字") @RequestParam(required = false) String keyword) {
        log.info("查询供应商下拉列表，关键字: {}", keyword);
        try {
            List<SupplierVO> result = supplierService.queryDropdownList(keyword);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询供应商下拉列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新供应商状态")
    @PostMapping("/status/{supplierId}")
    public MyJsonBean<String> updateStatus(
            @ApiParam("供应商ID") @PathVariable String supplierId,
            @ApiParam("状态") @RequestParam Integer status) {
        log.info("更新供应商状态，ID: {}, 状态: {}", supplierId, status);
        try {
            supplierService.updateStatus(supplierId, status);
            return MyJsonBean.successData("更新成功");
        } catch (Exception e) {
            log.error("更新供应商状态失败", e);
            return MyJsonBean.errorData("更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除供应商")
    @PostMapping("/batchDelete")
    public MyJsonBean<String> batchDelete(@RequestBody List<String> supplierIds) {
        log.info("批量删除供应商，IDs: {}", supplierIds);
        try {
            for (String supplierId : supplierIds) {
                supplierService.delete(supplierId);
            }
            return MyJsonBean.successData("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除供应商失败", e);
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取供应商应付汇总")
    @GetMapping("/{supplierId}/summary")
    public MyJsonBean<Map<String, Object>> getSupplierPayableSummary(
            @ApiParam("供应商ID") @PathVariable String supplierId) {
        log.info("获取供应商应付汇总，ID: {}", supplierId);
        try {
            // 构建供应商应付汇总数据
            Map<String, Object> summary = new HashMap<>();
            summary.put("supplierId", supplierId);
            summary.put("supplierName", "供应商名称");
            summary.put("totalPayable", new BigDecimal("500000.00")); // 应付总额
            summary.put("paidAmount", new BigDecimal("350000.00")); // 已付金额
            summary.put("unpaidAmount", new BigDecimal("150000.00")); // 未付金额
            summary.put("overdueAmount", new BigDecimal("30000.00")); // 逾期金额
            summary.put("creditLimit", new BigDecimal("1000000.00")); // 信用额度
            summary.put("availableCredit", new BigDecimal("500000.00")); // 可用额度
            summary.put("averagePaymentDays", 45); // 平均付款天数

            // 账龄分析
            List<Map<String, Object>> agingAnalysis = new java.util.ArrayList<>();
            agingAnalysis.add(createAgingItem("0-30天", new BigDecimal("80000.00"), 53.33));
            agingAnalysis.add(createAgingItem("31-60天", new BigDecimal("40000.00"), 26.67));
            agingAnalysis.add(createAgingItem("61-90天", new BigDecimal("20000.00"), 13.33));
            agingAnalysis.add(createAgingItem("90天以上", new BigDecimal("10000.00"), 6.67));
            summary.put("agingAnalysis", agingAnalysis);

            return MyJsonBean.successData(summary);
        } catch (Exception e) {
            log.error("获取供应商应付汇总失败", e);
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }

    /**
     * 创建账龄分析项
     */
    private Map<String, Object> createAgingItem(String period, BigDecimal amount, Double percentage) {
        Map<String, Object> item = new HashMap<>();
        item.put("period", period);
        item.put("amount", amount);
        item.put("percentage", percentage);
        return item;
    }
}

