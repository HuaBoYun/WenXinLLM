package com.financial.sharing.controller;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 固定资产模块控制器
 * 
 * @author Financial Sharing System
 * @since 2024-01-01
 */
@Api(tags = "固定资产模块")
@RestController
@RequestMapping("/fixedAssets")
@CrossOrigin
public class FixedAssetsController {

    @ApiOperation("获取资产卡片列表")
    @GetMapping("/assetCards")
    public MyJsonBean getAssetCards(PageableParam pageableParam,
                                  @RequestParam(required = false) String assetCode,
                                  @RequestParam(required = false) String assetName,
                                  @RequestParam(required = false) String category) {
        try {
            // 模拟数据
            Map<String, Object> data = new HashMap<>();
            data.put("totalAssets", 1256);
            data.put("totalValue", "125,680,000.00");
            data.put("depreciationValue", "45,230,000.00");
            data.put("netValue", "80,450,000.00");
            
            return MyJsonBean.successData("查询成功", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("新增资产卡片")
    @PostMapping("/assetCards")
    public MyJsonBean createAssetCard(@RequestBody Map<String, Object> assetData) {
        try {
            // 模拟新增逻辑
            Map<String, Object> data = new HashMap<>();
            data.put("assetId", "FA202401001");
            data.put("assetCode", assetData.get("assetCode"));
            data.put("status", "created");
            
            return MyJsonBean.successData("资产卡片创建成功", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("资产卡片创建失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新资产卡片")
    @PutMapping("/assetCards/{id}")
    public MyJsonBean updateAssetCard(@PathVariable String id, 
                                    @RequestBody Map<String, Object> assetData) {
        try {
            // 模拟更新逻辑
            Map<String, Object> data = new HashMap<>();
            data.put("assetId", id);
            data.put("status", "updated");
            
            return MyJsonBean.successData("资产卡片更新成功", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("资产卡片更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除资产卡片")
    @DeleteMapping("/assetCards/{id}")
    public MyJsonBean deleteAssetCard(@PathVariable String id) {
        try {
            // 模拟删除逻辑
            Map<String, Object> data = new HashMap<>();
            data.put("assetId", id);
            data.put("status", "deleted");
            
            return MyJsonBean.successData("资产卡片删除成功", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("资产卡片删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("计提折旧")
    @PostMapping("/depreciation")
    public MyJsonBean calculateDepreciation(@RequestParam String period) {
        try {
            // 模拟折旧计提逻辑
            Map<String, Object> data = new HashMap<>();
            data.put("processedAssets", 1156);
            data.put("depreciationAmount", "2,350,000.00");
            data.put("period", period);
            data.put("status", "completed");
            
            return MyJsonBean.successData("折旧计提完成", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("折旧计提失败: " + e.getMessage());
        }
    }

    @ApiOperation("资产变动")
    @PostMapping("/assetChange")
    public MyJsonBean assetChange(@RequestBody Map<String, Object> changeData) {
        try {
            // 模拟资产变动逻辑
            Map<String, Object> data = new HashMap<>();
            data.put("changeId", "AC202401001");
            data.put("changeType", changeData.get("changeType"));
            data.put("status", "processed");
            
            return MyJsonBean.successData("资产变动处理完成", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("资产变动处理失败: " + e.getMessage());
        }
    }

    @ApiOperation("资产处置")
    @PostMapping("/assetDisposal")
    public MyJsonBean assetDisposal(@RequestBody Map<String, Object> disposalData) {
        try {
            // 模拟资产处置逻辑
            Map<String, Object> data = new HashMap<>();
            data.put("disposalId", "AD202401001");
            data.put("disposalAmount", disposalData.get("disposalAmount"));
            data.put("status", "completed");
            
            return MyJsonBean.successData("资产处置完成", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("资产处置失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取固定资产统计")
    @GetMapping("/statistics")
    public MyJsonBean getStatistics() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("totalAssets", 1256);
            data.put("totalValue", "125,680,000.00");
            data.put("depreciationValue", "45,230,000.00");
            data.put("netValue", "80,450,000.00");
            data.put("depreciationRate", 36.0);

            return MyJsonBean.successData("查询成功", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 新增功能 API ====================

    @ApiOperation("获取资产卡片详情")
    @GetMapping("/assetCards/{id}")
    public MyJsonBean getAssetCardDetail(@PathVariable String id) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("assetId", id);
            data.put("assetCode", "FA202401001");
            data.put("assetName", "办公楼A栋");
            data.put("assetCategory", "房屋建筑物");
            data.put("originalValue", 5000000.00);
            data.put("netBookValue", 4500000.00);
            data.put("accumulatedDepreciation", 500000.00);
            data.put("depreciationMethod", "直线法");
            data.put("depreciationYears", 20);
            data.put("purchaseDate", "2020-01-01");
            data.put("location", "北京市海淀区");
            data.put("responsiblePerson", "张三");
            data.put("department", "行政部");
            data.put("status", "NORMAL");
            data.put("statusName", "正常");

            return MyJsonBean.successData("查询成功", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取折旧计划")
    @GetMapping("/depreciation/schedule/{assetId}")
    public MyJsonBean getDepreciationSchedule(@PathVariable String assetId) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("assetId", assetId);
            data.put("assetName", "办公楼A栋");
            data.put("depreciationMethod", "直线法");
            data.put("depreciationYears", 20);
            data.put("monthlyDepreciation", 20833.33);
            data.put("annualDepreciation", 250000.00);

            // 模拟折旧计划表
            data.put("schedule", java.util.Arrays.asList(
                createScheduleItem("2024-01", 20833.33, 520833.33, 4479166.67),
                createScheduleItem("2024-02", 20833.33, 541666.66, 4458333.34),
                createScheduleItem("2024-03", 20833.33, 562499.99, 4437500.01),
                createScheduleItem("2024-04", 20833.33, 583333.32, 4416666.68),
                createScheduleItem("2024-05", 20833.33, 604166.65, 4395833.35),
                createScheduleItem("2024-06", 20833.33, 624999.98, 4375000.02)
            ));

            return MyJsonBean.successData("查询成功", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("调整折旧")
    @PostMapping("/depreciation/adjust/{assetId}")
    public MyJsonBean adjustDepreciation(@PathVariable String assetId,
                                       @RequestBody Map<String, Object> adjustData) {
        try {
            String adjustReason = (String) adjustData.get("adjustReason");
            Double adjustAmount = (Double) adjustData.get("adjustAmount");

            Map<String, Object> data = new HashMap<>();
            data.put("assetId", assetId);
            data.put("adjustReason", adjustReason);
            data.put("adjustAmount", adjustAmount);
            data.put("adjustDate", new java.util.Date());
            data.put("status", "completed");

            return MyJsonBean.successData("折旧调整成功", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("折旧调整失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取资产变动列表")
    @GetMapping("/assetChange/list")
    public MyJsonBean getAssetChangesList(PageableParam pageableParam,
                                        @RequestParam(required = false) String assetId,
                                        @RequestParam(required = false) String changeType) {
        try {
            // 模拟资产变动数据
            java.util.List<Map<String, Object>> changesList = java.util.Arrays.asList(
                createAssetChange("CHG202412190001", "FA202412190001", "办公楼A栋", "PURCHASE", "购置", 5000000.00),
                createAssetChange("CHG202412190002", "FA202412190002", "生产设备", "TRANSFER", "部门调拨", 0.00),
                createAssetChange("CHG202412190003", "FA202412190003", "办公电脑", "MAINTENANCE", "维修", 15000.00),
                createAssetChange("CHG202412190004", "FA202412190004", "公司车辆", "UPGRADE", "升级改造", 20000.00)
            );

            PageResult result = new PageResult();
            result.setTlist(changesList);
            result.setTotalRecord(changesList.size());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("审批资产变动")
    @PostMapping("/assetChange/approve/{changeId}")
    public MyJsonBean approveAssetChange(@PathVariable String changeId,
                                       @RequestBody Map<String, Object> approvalData) {
        try {
            String approvalResult = (String) approvalData.get("approvalResult"); // APPROVE, REJECT
            String approvalComment = (String) approvalData.get("approvalComment");

            Map<String, Object> data = new HashMap<>();
            data.put("changeId", changeId);
            data.put("approvalResult", approvalResult);
            data.put("approvalComment", approvalComment);
            data.put("approvalDate", new java.util.Date());
            data.put("status", "approved");

            return MyJsonBean.successData("资产变动审批完成", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("审批失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取资产处置列表")
    @GetMapping("/assetDisposal/list")
    public MyJsonBean getAssetDisposalList(PageableParam pageableParam,
                                         @RequestParam(required = false) String assetId,
                                         @RequestParam(required = false) String disposalType) {
        try {
            // 模拟资产处置数据
            java.util.List<Map<String, Object>> disposalList = java.util.Arrays.asList(
                createAssetDisposal("AD202412190001", "FA202412190001", "办公电脑", "SCRAP", "报废", 0.00),
                createAssetDisposal("AD202412190002", "FA202412190002", "旧设备", "SALE", "出售", 50000.00),
                createAssetDisposal("AD202412190003", "FA202412190003", "车辆", "TRANSFER", "转让", 200000.00)
            );

            PageResult result = new PageResult();
            result.setTlist(disposalList);
            result.setTotalRecord(disposalList.size());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    private Map<String, Object> createScheduleItem(String period, Double monthlyDepreciation,
                                                   Double accumulatedDepreciation, Double netBookValue) {
        Map<String, Object> item = new HashMap<>();
        item.put("period", period);
        item.put("monthlyDepreciation", monthlyDepreciation);
        item.put("accumulatedDepreciation", accumulatedDepreciation);
        item.put("netBookValue", netBookValue);
        return item;
    }

    private Map<String, Object> createAssetChange(String changeId, String assetId, String assetName,
                                                 String changeType, String changeTypeName, Double changeAmount) {
        Map<String, Object> change = new HashMap<>();
        change.put("changeId", changeId);
        change.put("assetId", assetId);
        change.put("assetName", assetName);
        change.put("changeType", changeType);
        change.put("changeTypeName", changeTypeName);
        change.put("changeAmount", changeAmount);
        change.put("changeDate", new java.util.Date());
        change.put("status", "PENDING");
        change.put("statusName", "待审批");
        change.put("operator", "张三");
        return change;
    }

    private Map<String, Object> createAssetDisposal(String disposalId, String assetId, String assetName,
                                                   String disposalType, String disposalTypeName, Double disposalAmount) {
        Map<String, Object> disposal = new HashMap<>();
        disposal.put("disposalId", disposalId);
        disposal.put("assetId", assetId);
        disposal.put("assetName", assetName);
        disposal.put("disposalType", disposalType);
        disposal.put("disposalTypeName", disposalTypeName);
        disposal.put("disposalAmount", disposalAmount);
        disposal.put("disposalDate", new java.util.Date());
        disposal.put("status", "PENDING");
        disposal.put("statusName", "待审批");
        disposal.put("operator", "李四");
        return disposal;
    }

    // ==================== 资产类别管理 API ====================

    @ApiOperation("获取资产类别列表")
    @PostMapping("/category/getList")
    public MyJsonBean getAssetCategoryList(@RequestBody Map<String, Object> params) {
        try {
            // 模拟资产类别数据
            java.util.List<Map<String, Object>> categoryList = java.util.Arrays.asList(
                createAssetCategory("1001", "01", "房屋建筑物", null, 1, 240, 0.05),
                createAssetCategory("1002", "02", "机器设备", null, 1, 120, 0.05),
                createAssetCategory("1003", "03", "运输工具", null, 1, 48, 0.05),
                createAssetCategory("1004", "04", "电子设备", null, 1, 36, 0.05),
                createAssetCategory("1005", "05", "办公设备", null, 1, 60, 0.05)
            );

            PageResult result = new PageResult();
            result.setTlist(categoryList);
            result.setTotalRecord(categoryList.size());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存资产类别")
    @PostMapping("/category/saveOrUpdate")
    public MyJsonBean saveAssetCategory(@RequestBody Map<String, Object> categoryData) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("categoryId", categoryData.get("categoryId") != null ? categoryData.get("categoryId") : "1006");
            data.put("categoryCode", categoryData.get("categoryCode"));
            data.put("categoryName", categoryData.get("categoryName"));
            data.put("status", "saved");

            return MyJsonBean.successData("资产类别保存成功", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除资产类别")
    @DeleteMapping("/category/delete/{categoryId}")
    public MyJsonBean deleteAssetCategory(@PathVariable String categoryId) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("categoryId", categoryId);
            data.put("status", "deleted");

            return MyJsonBean.successData("资产类别删除成功", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取资产类别详情")
    @GetMapping("/category/getById/{categoryId}")
    public MyJsonBean getAssetCategoryDetail(@PathVariable String categoryId) {
        try {
            Map<String, Object> data = createAssetCategory(categoryId, "01", "房屋建筑物", null, 1, 240, 0.05);
            return MyJsonBean.successData("查询成功", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 使用权资产管理 API ====================

    @ApiOperation("获取使用权资产列表")
    @PostMapping("/rightofuse/getList")
    public MyJsonBean getRightOfUseAssetList(@RequestBody Map<String, Object> params) {
        try {
            // 模拟使用权资产数据
            java.util.List<Map<String, Object>> assetList = java.util.Arrays.asList(
                createRightOfUseAsset("ROU001", "租赁办公楼", "LEASE001", "2024-01-01", "2026-12-31", 2400000.00),
                createRightOfUseAsset("ROU002", "租赁设备", "LEASE002", "2024-06-01", "2027-05-31", 1800000.00),
                createRightOfUseAsset("ROU003", "租赁车辆", "LEASE003", "2024-03-01", "2026-02-28", 600000.00)
            );

            PageResult result = new PageResult();
            result.setTlist(assetList);
            result.setTotalRecord(assetList.size());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("确认使用权资产")
    @PostMapping("/rightofuse/recognize")
    public MyJsonBean recognizeRightOfUseAsset(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("assetId", "ROU" + System.currentTimeMillis());
            result.put("status", "recognized");
            result.put("recognizeDate", new java.util.Date());

            return MyJsonBean.successData("使用权资产确认成功", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("确认失败: " + e.getMessage());
        }
    }

    @ApiOperation("使用权资产折旧计算")
    @PostMapping("/rightofuse/depreciation")
    public MyJsonBean calculateRightOfUseDepreciation(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("processedAssets", 15);
            result.put("depreciationAmount", "125,000.00");
            result.put("period", data.get("period"));
            result.put("status", "completed");

            return MyJsonBean.successData("使用权资产折旧计算完成", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("计算失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取使用权资产详情")
    @GetMapping("/rightofuse/getById/{assetId}")
    public MyJsonBean getRightOfUseAssetDetail(@PathVariable String assetId) {
        try {
            Map<String, Object> data = createRightOfUseAsset(assetId, "租赁办公楼", "LEASE001", "2024-01-01", "2026-12-31", 2400000.00);
            return MyJsonBean.successData("查询成功", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    // ==================== 多账簿资产核算 API ====================

    @ApiOperation("多账簿折旧计算")
    @PostMapping("/multibook/depreciation")
    public MyJsonBean calculateMultiBookDepreciation(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("processedBooks", 3);
            result.put("processedAssets", 856);
            result.put("totalDepreciation", "2,850,000.00");
            result.put("period", data.get("period"));
            result.put("status", "completed");

            return MyJsonBean.successData("多账簿折旧计算完成", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("计算失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取多账簿资产信息")
    @GetMapping("/multibook/getAssetInfo/{assetId}")
    public MyJsonBean getMultiBookAssetInfo(@PathVariable String assetId) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("assetId", assetId);
            data.put("assetCode", "FA202412190001");
            data.put("assetName", "办公楼A栋");

            // 模拟多账簿信息
            data.put("bookInfo", java.util.Arrays.asList(
                createBookInfo("BOOK001", "企业会计准则账簿", 5000000.00, 500000.00, 4500000.00),
                createBookInfo("BOOK002", "税务账簿", 5000000.00, 600000.00, 4400000.00),
                createBookInfo("BOOK003", "管理账簿", 5000000.00, 450000.00, 4550000.00)
            ));

            return MyJsonBean.successData("查询成功", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("同步多账簿资产数据")
    @PostMapping("/multibook/sync")
    public MyJsonBean syncMultiBookAssetData(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("syncedAssets", 856);
            result.put("syncedBooks", 3);
            result.put("syncTime", new java.util.Date());
            result.put("status", "completed");

            return MyJsonBean.successData("多账簿数据同步完成", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("同步失败: " + e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    private Map<String, Object> createAssetCategory(String categoryId, String categoryCode, String categoryName,
                                                   String parentId, Integer depreciationMethod, Integer usefulLife, Double residualRate) {
        Map<String, Object> category = new HashMap<>();
        category.put("categoryId", categoryId);
        category.put("categoryCode", categoryCode);
        category.put("categoryName", categoryName);
        category.put("parentId", parentId);
        category.put("depreciationMethod", depreciationMethod);
        category.put("depreciationMethodName", getDepreciationMethodName(depreciationMethod));
        category.put("usefulLife", usefulLife);
        category.put("residualRate", residualRate);
        category.put("isLeaf", 1);
        category.put("isEnabled", 1);
        return category;
    }

    private Map<String, Object> createRightOfUseAsset(String assetCode, String assetName, String leaseContractNo,
                                                     String leaseStartDate, String leaseEndDate, Double initialCost) {
        Map<String, Object> asset = new HashMap<>();
        asset.put("assetCode", assetCode);
        asset.put("assetName", assetName);
        asset.put("leaseContractNo", leaseContractNo);
        asset.put("leaseStartDate", leaseStartDate);
        asset.put("leaseEndDate", leaseEndDate);
        asset.put("initialCost", initialCost);
        asset.put("accumulatedDepreciation", initialCost * 0.2);
        asset.put("netBookValue", initialCost * 0.8);
        asset.put("depreciationMethod", 1);
        asset.put("depreciationMethodName", "直线法");
        asset.put("usefulLife", 36);
        asset.put("monthlyDepreciation", initialCost / 36);
        asset.put("status", "NORMAL");
        asset.put("statusName", "正常");
        return asset;
    }

    private Map<String, Object> createBookInfo(String bookId, String bookName, Double originalValue,
                                              Double accumulatedDepreciation, Double netBookValue) {
        Map<String, Object> bookInfo = new HashMap<>();
        bookInfo.put("bookId", bookId);
        bookInfo.put("bookName", bookName);
        bookInfo.put("originalValue", originalValue);
        bookInfo.put("accumulatedDepreciation", accumulatedDepreciation);
        bookInfo.put("netBookValue", netBookValue);
        return bookInfo;
    }

    private String getDepreciationMethodName(Integer method) {
        switch (method) {
            case 1: return "直线法";
            case 2: return "年数总和法";
            case 3: return "双倍余额递减法";
            case 4: return "工作量法";
            default: return "未知方法";
        }
    }
}
