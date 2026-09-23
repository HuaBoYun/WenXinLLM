package com.financial.sharing.controller;

import com.financial.sharing.oracle.entity.InventoryCategoryEntity;
import com.financial.sharing.service.InventoryCategoryService;
import com.financial.sharing.service.InventoryValuationService;
import com.financial.sharing.service.CostTransferService;
import com.financial.sharing.service.InventoryCheckService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import com.financial.sharing.util.SnowflakeIdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 存货核算控制器
 * @author system
 * @date 2024-12-19
 */
@Slf4j
@Api(tags = "存货核算管理")
@RestController
@RequestMapping("/inventory")
public class InventoryAccountingController {

    @Autowired(required = false)
    private InventoryCategoryService inventoryCategoryService;

    @Autowired(required = false)
    private InventoryValuationService inventoryValuationService;

    @Autowired(required = false)
    private CostTransferService costTransferService;

    @Autowired(required = false)
    private InventoryCheckService inventoryCheckService;

    @Autowired(required = false)
    private SnowflakeIdWorker snowflakeIdWorker;

    // ==================== 存货核算管理 ====================

    @PostMapping("/accounting/getList")
    @ApiOperation("分页查询存货核算列表")
    public MyJsonBean getInventoryAccountingList(@RequestBody Map<String, Object> params) {
        try {
            // 模拟分页数据
            List<Map<String, Object>> accountingList = new ArrayList<>();
            for (int i = 1; i <= 15; i++) {
                Map<String, Object> accounting = new HashMap<>();
                accounting.put("accountingId", 15000 + i);
                accounting.put("inventoryId", 3000 + i);
                accounting.put("inventoryCode", "INV" + String.format("%03d", i));
                accounting.put("inventoryName", "存货" + i);
                accounting.put("warehouseId", 2001);
                accounting.put("warehouseName", "主仓库");
                accounting.put("accountingPeriod", "2024-12");
                accounting.put("pricingMethod", (i % 4) + 1);
                accounting.put("pricingMethodName", getPricingMethodName((i % 4) + 1));
                accounting.put("beginningQuantity", 1000 + i * 10);
                accounting.put("beginningAmount", 50000.00 + i * 1000);
                accounting.put("inQuantity", 500 + i * 5);
                accounting.put("inAmount", 26000.00 + i * 500);
                accounting.put("outQuantity", 300 + i * 3);
                accounting.put("outAmount", 15000.00 + i * 300);
                accounting.put("endingQuantity", 1200 + i * 12);
                accounting.put("endingAmount", 61000.00 + i * 1200);
                accounting.put("unitCost", 50.83 + i * 0.1);
                accounting.put("updateTime", "2024-12-19 10:30:00");
                accountingList.add(accounting);
            }

            PageResult<Map<String, Object>> result = new PageResult<>();
            result.setTlist(accountingList);
            result.setTotalRecord(150);
            result.setCurrentPage(1);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/accounting/getById/{accountingId}")
    @ApiOperation("根据ID获取存货核算详情")
    public MyJsonBean getInventoryAccountingById(@PathVariable @ApiParam("核算ID") String accountingId) {
        try {
            Map<String, Object> accounting = new HashMap<>();
            accounting.put("accountingId", accountingId);
            accounting.put("inventoryId", 3001);
            accounting.put("inventoryCode", "INV001");
            accounting.put("inventoryName", "原材料A");
            accounting.put("warehouseId", 2001);
            accounting.put("warehouseName", "主仓库");
            accounting.put("accountingPeriod", "2024-12");
            accounting.put("pricingMethod", 1);
            accounting.put("pricingMethodName", "移动平均法");
            accounting.put("beginningQuantity", 1000);
            accounting.put("beginningAmount", 50000.00);
            accounting.put("inQuantity", 500);
            accounting.put("inAmount", 26000.00);
            accounting.put("outQuantity", 300);
            accounting.put("outAmount", 15000.00);
            accounting.put("endingQuantity", 1200);
            accounting.put("endingAmount", 61000.00);
            accounting.put("unitCost", 50.83);
            accounting.put("updateTime", "2024-12-19 10:30:00");
            accounting.put("createTime", "2024-12-01 09:00:00");

            return MyJsonBean.successData(accounting);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/accounting/execute")
    @ApiOperation("执行存货核算")
    public MyJsonBean executeInventoryAccounting(@RequestBody Map<String, Object> data) {
        try {
            String accountingPeriod = (String) data.get("accountingPeriod");
            @SuppressWarnings("unchecked")
            List<Integer> inventoryIds = (List<Integer>) data.get("inventoryIds");
            @SuppressWarnings("unchecked")
            List<Integer> warehouseIds = (List<Integer>) data.get("warehouseIds");
            Integer pricingMethod = (Integer) data.get("pricingMethod");
            Boolean isRealTimeAccounting = (Boolean) data.get("isRealTimeAccounting");
            Boolean isGenerateVoucher = (Boolean) data.get("isGenerateVoucher");

            // 模拟执行结果
            Map<String, Object> result = new HashMap<>();
            result.put("accountingId", "ACC_" + System.currentTimeMillis());
            result.put("accountingPeriod", accountingPeriod);
            result.put("processedInventoryCount", inventoryIds != null ? inventoryIds.size() : 2);
            result.put("totalCostAdjustment", 1200.00);
            result.put("generatedVoucherIds", isGenerateVoucher ? Arrays.asList(5015, 5016) : new ArrayList<>());

            List<Map<String, Object>> accountingResults = new ArrayList<>();
            Map<String, Object> result1 = new HashMap<>();
            result1.put("inventoryId", 3001);
            result1.put("inventoryName", "原材料A");
            result1.put("costAdjustment", 800.00);
            result1.put("newUnitCost", 50.67);
            accountingResults.add(result1);

            Map<String, Object> result2 = new HashMap<>();
            result2.put("inventoryId", 3002);
            result2.put("inventoryName", "原材料B");
            result2.put("costAdjustment", 400.00);
            result2.put("newUnitCost", 32.50);
            accountingResults.add(result2);

            result.put("accountingResults", accountingResults);

            return MyJsonBean.successData("存货核算成功", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/accounting/delete/{accountingId}")
    @ApiOperation("删除存货核算记录")
    public MyJsonBean deleteInventoryAccounting(@PathVariable @ApiParam("核算ID") String accountingId) {
        try {
            // TODO: 实现删除逻辑
            return MyJsonBean.successData("删除成功", null);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/accounting/batchDelete")
    @ApiOperation("批量删除存货核算记录")
    public MyJsonBean batchDeleteInventoryAccounting(@RequestBody Map<String, Object> data) {
        try {
            @SuppressWarnings("unchecked")
            List<String> accountingIds = (List<String>) data.get("accountingIds");
            // TODO: 实现批量删除逻辑
            return MyJsonBean.successData("批量删除成功", null);
        } catch (Exception e) {
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    // ==================== 存货计价管理 ====================

    @PostMapping("/valuation/getList")
    @ApiOperation("分页查询存货计价列表")
    public MyJsonBean getInventoryValuationList(@RequestBody Map<String, Object> params) {
        try {
            // 调用真实的 Service 查询数据库
            PageResult<Map<String, Object>> result = inventoryValuationService.getValuationPage(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/valuation/saveOrUpdate")
    @ApiOperation("保存或更新存货计价方法")
    public MyJsonBean saveOrUpdateInventoryValuation(@RequestBody Map<String, Object> data) {
        try {
            log.info("保存或更新存货计价，参数: {}", data);
            boolean success = inventoryValuationService.saveOrUpdateValuation(data);

            if (success) {
                Map<String, Object> result = new HashMap<>();
                result.put("action", data.get("valuationId") != null ? "更新" : "新增");
                result.put("valuationId", data.get("valuationId"));
                return MyJsonBean.successData(data.get("valuationId") != null ? "更新成功" : "新增成功", result);
            } else {
                return MyJsonBean.errorData("保存失败");
            }
        } catch (Exception e) {
            log.error("保存或更新存货计价失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @GetMapping("/valuation/getById/{valuationId}")
    @ApiOperation("获取存货计价方法详情")
    public MyJsonBean getInventoryValuationById(@PathVariable @ApiParam("计价ID") String valuationId) {
        try {
            Map<String, Object> valuation = new HashMap<>();
            valuation.put("valuationId", valuationId);
            valuation.put("inventoryId", 3001);
            valuation.put("inventoryCode", "INV001");
            valuation.put("inventoryName", "原材料A");
            valuation.put("categoryId", 1001);
            valuation.put("categoryName", "原材料");
            valuation.put("warehouseId", 2001);
            valuation.put("warehouseName", "主仓库");
            valuation.put("pricingMethod", 1);
            valuation.put("pricingMethodName", "移动平均法");
            valuation.put("unit", "千克");
            valuation.put("unitCost", 50.83);
            valuation.put("currentQuantity", 1200);
            valuation.put("minQuantity", 100);
            valuation.put("maxQuantity", 5000);
            valuation.put("status", 1);
            valuation.put("batchManaged", true);
            valuation.put("remark", "重要原材料，需要严格管控");
            valuation.put("totalValue", 60996.00);
            valuation.put("updateTime", "2024-12-19 10:30:00");
            valuation.put("createTime", "2024-12-01 09:00:00");

            return MyJsonBean.successData(valuation);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/valuation/delete/{valuationId}")
    @ApiOperation("删除存货计价方法")
    public MyJsonBean deleteInventoryValuation(@PathVariable @ApiParam("计价ID") String valuationId) {
        try {
            // TODO: 实现删除逻辑
            return MyJsonBean.successData("删除成功", null);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    // ==================== 仓库管理 ====================

    @GetMapping("/warehouse/list")
    @ApiOperation("获取仓库列表")
    public MyJsonBean getWarehouseList() {
        try {
            // 模拟仓库数据
            List<Map<String, Object>> warehouseList = new ArrayList<>();

            Map<String, Object> warehouse1 = new HashMap<>();
            warehouse1.put("warehouseId", 2001);
            warehouse1.put("warehouseCode", "WH001");
            warehouse1.put("warehouseName", "主仓库");
            warehouse1.put("warehouseType", "MAIN");
            warehouse1.put("status", 1);
            warehouseList.add(warehouse1);

            Map<String, Object> warehouse2 = new HashMap<>();
            warehouse2.put("warehouseId", 2002);
            warehouse2.put("warehouseCode", "WH002");
            warehouse2.put("warehouseName", "原材料仓库");
            warehouse2.put("warehouseType", "MATERIAL");
            warehouse2.put("status", 1);
            warehouseList.add(warehouse2);

            Map<String, Object> warehouse3 = new HashMap<>();
            warehouse3.put("warehouseId", 2003);
            warehouse3.put("warehouseCode", "WH003");
            warehouse3.put("warehouseName", "成品仓库");
            warehouse3.put("warehouseType", "PRODUCT");
            warehouse3.put("status", 1);
            warehouseList.add(warehouse3);

            Map<String, Object> warehouse4 = new HashMap<>();
            warehouse4.put("warehouseId", 2004);
            warehouse4.put("warehouseCode", "WH004");
            warehouse4.put("warehouseName", "半成品仓库");
            warehouse4.put("warehouseType", "SEMI_PRODUCT");
            warehouse4.put("status", 1);
            warehouseList.add(warehouse4);

            return MyJsonBean.successData(warehouseList);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 成本结转管理 ====================

    @PostMapping("/costTransfer/getList")
    @ApiOperation("分页查询成本结转列表")
    public MyJsonBean getCostTransferList(@RequestParam(required = false) Map<String, Object> params, HttpServletRequest request) {
        try {
            // 兼容 application/x-www-form-urlencoded 和 application/json 两种格式
            if (params == null) {
                params = new HashMap<>();
            }

            // 从 request 中获取所有参数（支持 form-urlencoded）
            if (request.getParameterMap() != null && !request.getParameterMap().isEmpty()) {
                for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
                    String key = entry.getKey();
                    String[] values = entry.getValue();
                    if (values != null && values.length > 0) {
                        params.put(key, values.length == 1 ? values[0] : values);
                    }
                }
            }

            // 调用真实的 Service 层
            PageResult<Map<String, Object>> result = costTransferService.getTransferPage(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询成本结转列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/costTransfer/execute")
    @ApiOperation("执行成本结转")
    public MyJsonBean executeCostTransfer(@RequestBody Map<String, Object> data) {
        try {
            log.info("执行成本结转, 参数: {}", data);

            // 先创建成本结转记录
            boolean created = costTransferService.createTransfer(data);
            if (!created) {
                return MyJsonBean.errorData("创建成本结转记录失败");
            }

            // 模拟执行结果（实际应该查询刚创建的记录ID并执行）
            Map<String, Object> result = new HashMap<>();
            result.put("transferId", "TRF" + System.currentTimeMillis());
            result.put("processedInventoryCount", 15);
            result.put("totalTransferAmount", 125000.00);
            result.put("totalVarianceAmount", 2500.00);
            result.put("generatedVoucherCount", 8);
            result.put("executeTime", new Date());

            return MyJsonBean.successData("成本结转执行成功", result);
        } catch (Exception e) {
            log.error("执行成本结转失败", e);
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    @GetMapping("/costTransfer/getById/{transferId}")
    @ApiOperation("获取成本结转详情")
    public MyJsonBean getCostTransferById(@PathVariable @ApiParam("结转ID") String transferId) {
        try {
            Map<String, Object> transfer = new HashMap<>();
            transfer.put("transferId", transferId);
            transfer.put("transferPeriod", "2024-12");
            transfer.put("transferType", "SALES_COST");
            transfer.put("transferTypeName", "销售成本结转");
            transfer.put("inventoryCount", 25);
            transfer.put("transferAmount", 125000.00);
            transfer.put("varianceAmount", 2500.00);
            transfer.put("status", "TRANSFERRED");
            transfer.put("voucherCount", 8);
            transfer.put("transferTime", "2024-12-19 14:30:00");
            transfer.put("operatorName", "操作员1");
            transfer.put("description", "12月份销售成本结转");

            return MyJsonBean.successData(transfer);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/costTransfer/revoke/{transferId}")
    @ApiOperation("撤销成本结转")
    public MyJsonBean revokeCostTransfer(@PathVariable @ApiParam("结转ID") String transferId) {
        try {
            // TODO: 实现撤销逻辑
            Map<String, Object> result = new HashMap<>();
            result.put("transferId", transferId);
            result.put("revokeTime", new Date());
            result.put("deletedVoucherCount", 8);

            return MyJsonBean.successData("成本结转撤销成功", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("撤销失败: " + e.getMessage());
        }
    }

    @GetMapping("/costTransfer/statistics")
    @ApiOperation("获取成本结转统计")
    public MyJsonBean getCostTransferStatistics(@RequestParam Map<String, Object> params) {
        try {
            // 设置默认租户ID（实际应从用户上下文获取）
            if (!params.containsKey("tenantId")) {
                params.put("tenantId", 1L);
            }

            // 模拟统计数据（实际应调用Service层查询数据库）
            Map<String, Object> stats = new HashMap<>();
            stats.put("pendingCount", 25);
            stats.put("transferredCount", 156);
            stats.put("totalAmount", 2850000.00);
            stats.put("varianceAmount", 15600.00);

            return MyJsonBean.successData(stats);
        } catch (Exception e) {
            log.error("获取成本结转统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/costTransfer/export")
    @ApiOperation("导出成本结转数据")
    public MyJsonBean exportCostTransfer(@RequestBody Map<String, Object> params) {
        try {
            // TODO: 实现导出功能
            log.info("导出成本结转数据, params: {}", params);
            String filePath = "/export/cost_transfer_" + System.currentTimeMillis() + ".xlsx";
            return MyJsonBean.successData("导出成功", filePath);
        } catch (Exception e) {
            log.error("导出成本结转数据失败", e);
            return MyJsonBean.errorData("导出失败: " + e.getMessage());
        }
    }

    // ==================== 存货盘点管理 ====================

    @PostMapping(value = "/check/getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("分页查询存货盘点列表")
    public MyJsonBean getInventoryCheckList(@RequestBody Map<String, Object> params) {
        try {
            log.info("查询存货盘点列表, 参数: {}", params);

            // 调用真实的 Service 层查询数据库
            PageResult<Map<String, Object>> result = inventoryCheckService.getCheckPage(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询存货盘点列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/check/create")
    @ApiOperation("创建存货盘点任务")
    public MyJsonBean createInventoryCheck(@RequestBody Map<String, Object> data) {
        try {
            log.info("创建存货盘点任务, 参数: {}", data);

            // 调用真实的 Service 层插入数据库
            boolean result = inventoryCheckService.createCheck(data);

            if (result) {
                // 返回创建成功的信息
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("checkId", "CHK" + System.currentTimeMillis());
                responseData.put("checkNumber", data.get("checkNo"));
                responseData.put("checkName", data.get("checkName"));
                responseData.put("checkType", data.get("checkType"));
                responseData.put("status", "PLANNING");
                responseData.put("createTime", new Date());

                return MyJsonBean.successData("盘点任务创建成功", responseData);
            } else {
                return MyJsonBean.errorData("创建失败");
            }
        } catch (Exception e) {
            log.error("创建存货盘点任务失败", e);
            return MyJsonBean.errorData("创建失败: " + e.getMessage());
        }
    }

    @GetMapping("/check/getById/{checkId}")
    @ApiOperation("获取存货盘点详情")
    public MyJsonBean getInventoryCheckById(@PathVariable @ApiParam("盘点ID") String checkId) {
        try {
            log.info("获取存货盘点详情, checkId: {}", checkId);

            // 调用真实的 Service 层查询数据库
            Long id = Long.parseLong(checkId);
            Map<String, Object> check = inventoryCheckService.getCheckById(id);

            return MyJsonBean.successData(check);
        } catch (Exception e) {
            log.error("获取存货盘点详情失败, checkId: {}", checkId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/check/execute/{checkId}")
    @ApiOperation("执行存货盘点")
    public MyJsonBean executeInventoryCheck(@PathVariable @ApiParam("盘点ID") String checkId) {
        try {
            log.info("执行存货盘点, checkId: {}", checkId);

            // 调用真实的 Service 层执行盘点
            Long id = Long.parseLong(checkId);
            boolean result = inventoryCheckService.executeCheck(id);

            if (result) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("checkId", checkId);
                responseData.put("status", "CHECKING");
                responseData.put("startTime", new Date());

                return MyJsonBean.successData("盘点任务已开始", responseData);
            } else {
                return MyJsonBean.errorData("执行失败");
            }
        } catch (Exception e) {
            log.error("执行存货盘点失败, checkId: {}", checkId, e);
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    @PostMapping("/check/submit/{checkId}")
    @ApiOperation("提交盘点结果")
    public MyJsonBean submitInventoryCheckResult(@PathVariable @ApiParam("盘点ID") String checkId, @RequestBody Map<String, Object> data) {
        try {
            log.info("提交盘点结果, checkId: {}, data: {}", checkId, data);

            // 调用真实的 Service 层提交盘点结果
            Long id = Long.parseLong(checkId);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> results = (List<Map<String, Object>>) data.get("results");

            boolean result = inventoryCheckService.submitCheckResults(id, results);

            if (result) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("checkId", checkId);
                responseData.put("status", "COMPLETED");
                responseData.put("submitTime", new Date());

                return MyJsonBean.successData("盘点结果提交成功", responseData);
            } else {
                return MyJsonBean.errorData("提交失败");
            }
        } catch (Exception e) {
            log.error("提交盘点结果失败, checkId: {}", checkId, e);
            return MyJsonBean.errorData("提交失败: " + e.getMessage());
        }
    }

    @PostMapping("/check/approve/{checkId}")
    @ApiOperation("审批盘点结果")
    public MyJsonBean approveInventoryCheck(@PathVariable @ApiParam("盘点ID") String checkId, @RequestBody Map<String, Object> data) {
        try {
            log.info("审批盘点结果, checkId: {}, data: {}", checkId, data);

            // 调用真实的 Service 层审批盘点
            Long id = Long.parseLong(checkId);
            boolean result = inventoryCheckService.approveCheck(id, data);

            if (result) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("checkId", checkId);
                responseData.put("status", "APPROVED");
                responseData.put("approveTime", new Date());

                return MyJsonBean.successData("盘点结果审批成功", responseData);
            } else {
                return MyJsonBean.errorData("审批失败");
            }
        } catch (Exception e) {
            log.error("审批盘点结果失败, checkId: {}", checkId, e);
            return MyJsonBean.errorData("审批失败: " + e.getMessage());
        }
    }

    @GetMapping("/check/results/{checkId}")
    @ApiOperation("获取盘点结果列表")
    public MyJsonBean getInventoryCheckResults(@PathVariable @ApiParam("盘点ID") String checkId,
                                                @RequestParam(required = false) Integer pageNumber,
                                                @RequestParam(required = false) Integer pageSize,
                                                @RequestParam(required = false) String varianceStatus,
                                                @RequestParam(required = false) String checkStatus) {
        try {
            log.info("获取盘点结果列表, checkId: {}, pageNumber: {}, pageSize: {}", checkId, pageNumber, pageSize);

            // 调用真实的 Service 层查询数据库
            Long id = Long.parseLong(checkId);
            List<Map<String, Object>> results = inventoryCheckService.getCheckResults(id);

            // 如果需要分页，可以在这里处理
            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTlist(results);
            pageResult.setTotalRecord(results.size());
            pageResult.setCurrentPage(pageNumber != null ? pageNumber : 1);

            return MyJsonBean.successData(pageResult);
        } catch (Exception e) {
            log.error("获取盘点结果列表失败, checkId: {}", checkId, e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/check/statistics")
    @ApiOperation("获取存货盘点统计")
    public MyJsonBean getInventoryCheckStatistics(@RequestParam(required = false) Map<String, Object> params) {
        try {
            log.info("获取存货盘点统计, 参数: {}", params);

            if (params == null) {
                params = new HashMap<>();
            }

            // 调用真实的 Service 层查询数据库
            Map<String, Object> result = inventoryCheckService.getCheckStatistics(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取存货盘点统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    private String getPricingMethodName(int method) {
        switch (method) {
            case 1: return "移动平均法";
            case 2: return "先进先出法";
            case 3: return "加权平均法";
            case 4: return "个别计价法";
            default: return "未知方法";
        }
    }

    private String getTransferType(int index) {
        String[] types = {"SALES_COST", "PRODUCTION_COST", "PERIOD_EXPENSE", "COST_VARIANCE"};
        return types[index];
    }

    private String getTransferTypeName(int index) {
        String[] names = {"销售成本结转", "生产成本结转", "期间费用分摊", "成本差异结转"};
        return names[index];
    }

    private String getTransferStatus(int index) {
        String[] statuses = {"PENDING", "TRANSFERRED", "REVOKED"};
        return statuses[index];
    }

    private String getCheckType(int index) {
        String[] types = {"FULL", "PARTIAL", "CYCLE", "DYNAMIC"};
        return types[index];
    }

    private String getCheckTypeName(int index) {
        String[] names = {"全盘", "抽盘", "循环盘点", "动态盘点"};
        return names[index];
    }

    private String getCheckStatus(int index) {
        String[] statuses = {"PLANNING", "CHECKING", "COMPLETED", "APPROVED", "CANCELLED"};
        return statuses[index];
    }

    // ==================== 存货分类管理 ====================

    @GetMapping("/category/tree")
    @ApiOperation("查询存货分类树")
    public MyJsonBean getCategoryTree() {
        try {
            List<Map<String, Object>> tree = inventoryCategoryService.getCategoryTree();
            return MyJsonBean.successData("查询成功", tree);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/category/getTree")
    @ApiOperation("查询存货分类树（兼容接口）")
    public MyJsonBean getCategoryTreeCompat() {
        return getCategoryTree();
    }

    @GetMapping("/category/detail/{categoryId}")
    @ApiOperation("查询分类详情")
    public MyJsonBean getCategoryDetail(@PathVariable @ApiParam("分类ID") Long categoryId) {
        try {
            Map<String, Object> category = inventoryCategoryService.getCategoryById(categoryId);
            return MyJsonBean.successData("查询成功", category);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/category/add")
    @ApiOperation("新增分类")
    public MyJsonBean addCategory(@RequestBody Map<String, Object> param) {
        try {
            boolean success = inventoryCategoryService.saveOrUpdateCategory(param);
            if (success) {
                return MyJsonBean.successData("新增成功", null);
            } else {
                return MyJsonBean.errorData("新增失败");
            }
        } catch (Exception e) {
            return MyJsonBean.errorData("新增失败: " + e.getMessage());
        }
    }

    @PostMapping("/category/update")
    @ApiOperation("更新分类")
    public MyJsonBean updateCategory(@RequestBody Map<String, Object> param) {
        try {
            boolean success = inventoryCategoryService.saveOrUpdateCategory(param);
            if (success) {
                return MyJsonBean.successData("更新成功", null);
            } else {
                return MyJsonBean.errorData("更新失败");
            }
        } catch (Exception e) {
            return MyJsonBean.errorData("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/category/delete/{categoryId}")
    @ApiOperation("删除分类")
    public MyJsonBean deleteCategory(@PathVariable @ApiParam("分类ID") Long categoryId) {
        try {
            boolean success = inventoryCategoryService.deleteCategory(categoryId);
            if (success) {
                return MyJsonBean.successData("删除成功", null);
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/category/batchDelete")
    @ApiOperation("批量删除分类")
    public MyJsonBean batchDeleteCategory(@RequestBody Map<String, Object> data) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> categoryIds = (List<Long>) data.get("categoryIds");

            for (Long categoryId : categoryIds) {
                inventoryCategoryService.deleteCategory(categoryId);
            }
            return MyJsonBean.successData("批量删除成功", null);
        } catch (Exception e) {
            return MyJsonBean.errorData("批量删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/category/inventories/{categoryId}")
    @ApiOperation("查询分类下的存货列表")
    public MyJsonBean getCategoryInventories(@PathVariable @ApiParam("分类ID") Long categoryId) {
        try {
            List<Map<String, Object>> inventories = inventoryCategoryService.getInventoriesByCategory(categoryId);
            return MyJsonBean.successData("查询成功", inventories);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}
