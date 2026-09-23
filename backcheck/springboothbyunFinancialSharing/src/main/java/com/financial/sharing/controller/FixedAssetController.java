package com.financial.sharing.controller;

import com.financial.sharing.service.FixedAssetCardService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.JsonMapper;
import com.financial.sharing.vo.param.FixedAssetCardQueryParam;
import com.financial.sharing.vo.param.FixedAssetCardSaveParam;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 固定资产管理控制器
 * @author system
 * @date 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/financial/fixed-assets")
@Api(tags = "固定资产管理")
@CrossOrigin
public class FixedAssetController {

    @Resource
    private UserProvider userProvider;

    @Resource
    private FixedAssetCardService fixedAssetCardService;

    @Resource
    private com.financial.sharing.service.FixedAssetDepreciationService fixedAssetDepreciationService;

    // ==================== 资产卡片管理 API ====================

    @GetMapping("/assetCards")
    @ApiOperation("获取资产卡片列表")
    public String getAssetCards(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestParam(required = false) String assetCode,
                               @RequestParam(required = false) String assetName,
                               @RequestParam(required = false) String categoryId,
                               @RequestParam(required = false) String deptId,
                               @RequestParam(required = false) String status,
                               @RequestParam(required = false) Integer pageNumber,
                               @RequestParam(required = false) Integer pageSize) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return null;
            }

            // 构建查询参数
            FixedAssetCardQueryParam param = new FixedAssetCardQueryParam();
            param.setAssetCode(assetCode);
            param.setAssetName(assetName);
            param.setCategoryId(categoryId);
            param.setDeptId(deptId);
            param.setStatus(status);
            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setPageNumber(pageNumber != null ? pageNumber : 1);
            param.setPageSize(pageSize != null ? pageSize : 10);

            // 调用Service查询
            MyJsonBean result = fixedAssetCardService.getAssetCardList(param);

            // 转换为JsonBean格式
            JsonBean json = new JsonBean();
            json.setCode(result.getCode());
            json.setMsg(result.getMsg());
            json.setData(result.getData());
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("获取资产卡片列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/assetCards")
    @ApiOperation("新增资产卡片")
    public String createAssetCard(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestBody FixedAssetCardSaveParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return null;
            }

            // 设置租户ID和操作人
            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setOperatorId(loginStaff.getStaffid().toString());

            // 调用Service保存
            MyJsonBean result = fixedAssetCardService.saveOrUpdateAssetCard(param);

            // 转换为JsonBean格式
            JsonBean json = new JsonBean();
            json.setCode(result.getCode());
            json.setMsg(result.getMsg());
            json.setData(result.getData());
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("新增资产卡片失败", e);
            return createErrorResponse("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/assetCards/{assetId}")
    @ApiOperation("更新资产卡片")
    public String updateAssetCard(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @PathVariable String assetId,
                                 @RequestBody FixedAssetCardSaveParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return null;
            }

            // 设置资产ID、租户ID和操作人
            param.setAssetId(assetId);
            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setOperatorId(loginStaff.getStaffid().toString());

            // 调用Service更新
            MyJsonBean result = fixedAssetCardService.saveOrUpdateAssetCard(param);

            // 转换为JsonBean格式
            JsonBean json = new JsonBean();
            json.setCode(result.getCode());
            json.setMsg(result.getMsg());
            json.setData(result.getData());
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("更新资产卡片失败", e);
            return createErrorResponse("更新失败: " + e.getMessage());
        }
    }

    @GetMapping("/assetCards/{assetId}")
    @ApiOperation("获取资产卡片详情")
    public String getAssetCardById(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @PathVariable String assetId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return null;
            }

            // 调用Service查询详情
            MyJsonBean result = fixedAssetCardService.getAssetCardById(assetId);

            // 转换为JsonBean格式
            JsonBean json = new JsonBean();
            json.setCode(result.getCode());
            json.setMsg(result.getMsg());
            json.setData(result.getData());
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("获取资产卡片详情失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/assetCards/{assetId}")
    @ApiOperation("删除资产卡片")
    public String deleteAssetCard(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @PathVariable String assetId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return null;
            }

            // 调用Service删除
            MyJsonBean result = fixedAssetCardService.deleteAssetCard(assetId);

            // 转换为JsonBean格式
            JsonBean json = new JsonBean();
            json.setCode(result.getCode());
            json.setMsg(result.getMsg());
            json.setData(result.getData());
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("删除资产卡片失败", e);
            return createErrorResponse("删除失败: " + e.getMessage());
        }
    }



    @GetMapping("/depreciation/history")
    @ApiOperation("查询折旧历史")
    public String getDepreciationHistory(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestParam(required = false) String assetId,
                                        @RequestParam(required = false) String startDate,
                                        @RequestParam(required = false) String endDate) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return null;
            }

            List<Map<String, Object>> history = Arrays.asList(
                createDepreciationRecord("2024-12", 125000.00, 25, "已完成"),
                createDepreciationRecord("2024-11", 125000.00, 25, "已完成"),
                createDepreciationRecord("2024-10", 123000.00, 24, "已完成"),
                createDepreciationRecord("2024-09", 123000.00, 24, "已完成")
            );

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(history);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询折旧历史失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/disposal/apply")
    @ApiOperation("资产处置申请")
    public String applyAssetDisposal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestBody Map<String, Object> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return null;
            }

            Map<String, Object> result = new HashMap<>();
            result.put("disposalId", "DISP" + System.currentTimeMillis());
            result.put("applicationNo", "DISPAPP" + System.currentTimeMillis());
            result.put("assetId", param.get("assetId"));
            result.put("status", "PENDING_APPROVAL");
            result.put("message", "资产处置申请已提交，等待审批");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("申请提交成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("资产处置申请失败", e);
            return createErrorResponse("申请失败: " + e.getMessage());
        }
    }

    @GetMapping("/inventory/list")
    @ApiOperation("查询资产盘点列表")
    public String getAssetInventoryList(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestParam(required = false) String status) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return null;
            }

            List<Map<String, Object>> inventoryList = Arrays.asList(
                createInventory("INV001", "2024年度全面盘点", "PLANNED", "计划中", "2024-12-01", "2024-12-31"),
                createInventory("INV002", "电子设备专项盘点", "IN_PROGRESS", "进行中", "2024-11-15", "2024-11-30"),
                createInventory("INV003", "车辆资产盘点", "COMPLETED", "已完成", "2024-10-01", "2024-10-15")
            );

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(inventoryList);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询资产盘点列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/import")
    @ApiOperation("批量导入资产卡片")
    public String importAssetCards(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @RequestParam("file") MultipartFile file) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return null;
            }

            Map<String, Object> result = new HashMap<>();
            result.put("taskId", "IMPORT" + System.currentTimeMillis());
            result.put("fileName", file.getOriginalFilename());
            result.put("status", "PROCESSING");
            result.put("totalCount", 0);
            result.put("successCount", 0);
            result.put("failureCount", 0);
            result.put("message", "文件上传成功，正在处理中");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("导入成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("批量导入资产卡片失败", e);
            return createErrorResponse("导入失败: " + e.getMessage());
        }
    }

    @GetMapping("/assetCards/summary")
    @ApiOperation("获取资产汇总信息")
    public String getAssetSummary(HttpServletRequest request,
                                 HttpServletResponse response) {
        return getAssetSummaryReport(request, response, null);
    }

    @GetMapping("/report/summary")
    @ApiOperation("固定资产统计报表")
    public String getAssetSummaryReport(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestParam(required = false) String department) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return null;
            }

            // 获取租户ID
            Long tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();

            // 调用Service层获取真实统计数据
            MyJsonBean<Map<String, Object>> summaryResult = fixedAssetCardService.getAssetSummary(tenantId);
            if (summaryResult.getCode() != 1) {
                return createErrorResponse(summaryResult.getMsg());
            }

            // 获取按类别统计数据
            MyJsonBean<List<Map<String, Object>>> categoryResult = fixedAssetCardService.getAssetByCategory(tenantId);
            if (categoryResult.getCode() != 1) {
                return createErrorResponse(categoryResult.getMsg());
            }

            // 组装返回数据 - 处理达梦数据库返回的大写字段名
            Map<String, Object> summaryData = summaryResult.getData();
            Map<String, Object> summary = new HashMap<>();

            // 转换字段名为小写驼峰格式（达梦数据库返回的是大写）
            summary.put("totalCount", summaryData.get("TOTALCOUNT") != null ? summaryData.get("TOTALCOUNT") : summaryData.get("totalCount"));
            summary.put("totalOriginalValue", summaryData.get("TOTALORIGINALVALUE") != null ? summaryData.get("TOTALORIGINALVALUE") : summaryData.get("totalOriginalValue"));
            summary.put("totalAccumulatedDepreciation", summaryData.get("TOTALACCUMULATEDDEPRECIATION") != null ? summaryData.get("TOTALACCUMULATEDDEPRECIATION") : summaryData.get("totalAccumulatedDepreciation"));
            summary.put("totalNetValue", summaryData.get("TOTALNETVALUE") != null ? summaryData.get("TOTALNETVALUE") : summaryData.get("totalNetValue"));

            // 将按类别统计数据转换为前端需要的格式
            Map<String, Object> categorySummary = new HashMap<>();
            List<Map<String, Object>> categoryList = categoryResult.getData();
            if (categoryList != null) {
                for (Map<String, Object> category : categoryList) {
                    String categoryName = (String) (category.get("CATEGORYNAME") != null ? category.get("CATEGORYNAME") : category.get("categoryName"));
                    if (categoryName != null) {
                        Map<String, Object> categoryData = new HashMap<>();
                        categoryData.put("count", category.get("ASSETCOUNT") != null ? category.get("ASSETCOUNT") : category.get("assetCount"));
                        categoryData.put("originalValue", category.get("TOTALORIGINALVALUE") != null ? category.get("TOTALORIGINALVALUE") : category.get("totalOriginalValue"));
                        categoryData.put("netValue", category.get("TOTALNETVALUE") != null ? category.get("TOTALNETVALUE") : category.get("totalNetValue"));
                        categorySummary.put(categoryName, categoryData);
                    }
                }
            }
            summary.put("categorySummary", categorySummary);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("统计完成");
            json.setData(summary);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("固定资产统计报表失败", e);
            return createErrorResponse("统计失败: " + e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    private Map<String, Object> createAsset(String assetId, String assetName, String category,
                                           String status, String statusName, Double originalValue,
                                           Double netValue, String purchaseDate) {
        Map<String, Object> asset = new HashMap<>();
        asset.put("assetId", assetId);
        asset.put("assetCode", assetId);
        asset.put("assetName", assetName);
        asset.put("category", category);
        asset.put("status", status);
        asset.put("statusName", statusName);
        asset.put("originalValue", new BigDecimal(originalValue.toString()));
        asset.put("netValue", new BigDecimal(netValue.toString()));
        asset.put("accumulatedDepreciation", new BigDecimal((originalValue - netValue) + ""));
        asset.put("purchaseDate", purchaseDate);
        return asset;
    }

    private Map<String, Object> createDepreciationRecord(String period, Double amount, Integer assetCount, String status) {
        Map<String, Object> record = new HashMap<>();
        record.put("period", period);
        record.put("amount", new BigDecimal(amount.toString()));
        record.put("assetCount", assetCount);
        record.put("status", status);
        record.put("createTime", period + "-31T23:59:59.000Z");
        return record;
    }

    private Map<String, Object> createInventory(String inventoryId, String inventoryName, String status,
                                              String statusName, String startDate, String endDate) {
        Map<String, Object> inventory = new HashMap<>();
        inventory.put("inventoryId", inventoryId);
        inventory.put("inventoryName", inventoryName);
        inventory.put("status", status);
        inventory.put("statusName", statusName);
        inventory.put("startDate", startDate);
        inventory.put("endDate", endDate);
        inventory.put("plannedCount", 100);
        inventory.put("actualCount", 98);
        inventory.put("differenceCount", 2);
        return inventory;
    }

    // ==================== 折旧管理 API ====================

    @GetMapping("/depreciation/stats")
    @ApiOperation("获取折旧统计数据")
    public String getDepreciationStats(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestParam(required = false) String period) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return createErrorResponse("用户已失效");
            }

            Long tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();
            MyJsonBean result = fixedAssetDepreciationService.getDepreciationStats(tenantId, period);
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("获取折旧统计数据失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/depreciation/details")
    @ApiOperation("获取折旧明细列表")
    public String getDepreciationDetails(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestParam(required = false) String assetCode,
                                        @RequestParam(required = false) String startPeriod,
                                        @RequestParam(required = false) String endPeriod,
                                        @RequestParam(required = false) Integer pageNum,
                                        @RequestParam(required = false) Integer pageSize) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return createErrorResponse("用户已失效");
            }

            com.financial.sharing.vo.param.FixedAssetDepreciationQueryParam param =
                new com.financial.sharing.vo.param.FixedAssetDepreciationQueryParam();
            param.setAssetCode(assetCode);
            param.setStartPeriod(startPeriod);
            param.setEndPeriod(endPeriod);
            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setPageNum(pageNum != null ? pageNum : 1);
            param.setPageSize(pageSize != null ? pageSize : 10);

            MyJsonBean result = fixedAssetDepreciationService.getDepreciationDetailList(param);
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("获取折旧明细列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/depreciation/calculate")
    @ApiOperation("计提折旧")
    public String calculateDepreciation(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestParam String period,
                                       @RequestParam(required = false) String categoryId,
                                       @RequestParam(required = false, defaultValue = "AUTO") String calculateType) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return createErrorResponse("用户已失效");
            }

            Long tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();
            String operatorId = loginStaff.getStaffid().toString();

            MyJsonBean result = fixedAssetDepreciationService.calculateDepreciation(
                period, categoryId, calculateType, tenantId, operatorId
            );
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("计提折旧失败", e);
            return createErrorResponse("计提失败: " + e.getMessage());
        }
    }

    @GetMapping("/depreciation/preview")
    @ApiOperation("预览折旧")
    public String previewDepreciation(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam String period,
                                     @RequestParam(required = false) String categoryId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return createErrorResponse("用户已失效");
            }

            Long tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();
            MyJsonBean result = fixedAssetDepreciationService.previewDepreciation(period, categoryId, tenantId);
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("预览折旧失败", e);
            return createErrorResponse("预览失败: " + e.getMessage());
        }
    }

    @GetMapping("/depreciation/trend")
    @ApiOperation("获取折旧趋势数据")
    public String getDepreciationTrend(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestParam String startPeriod,
                                      @RequestParam String endPeriod) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return createErrorResponse("用户已失效");
            }

            Long tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();
            MyJsonBean result = fixedAssetDepreciationService.getDepreciationTrend(startPeriod, endPeriod, tenantId);
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("获取折旧趋势数据失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/depreciation/category-distribution")
    @ApiOperation("获取资产类别折旧分布")
    public String getCategoryDistribution(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @RequestParam String period) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return createErrorResponse("用户已失效");
            }

            Long tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();
            MyJsonBean result = fixedAssetDepreciationService.getCategoryDistribution(period, tenantId);
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("获取类别分布数据失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建错误响应
     */
    private String createErrorResponse(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return JsonMapper.toJson(json);
    }
}