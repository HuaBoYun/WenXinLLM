package com.global.treasurer.financialProductDefinition.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.global.treasurer.financialProductDefinition.entity.TblFinancialProduct;
import com.global.treasurer.financialProductDefinition.service.TblFinancialProductService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/financial/product-definition/financial-product")
@Api(tags = "金融产品管理")
public class FinancialProductController {
    private static final Logger log = LoggerFactory.getLogger(FinancialProductController.class);

    @Autowired
    private TblFinancialProductService productService;
    @Resource
    private UserProvider userProvider;

    @PostMapping("/getList")
    @ApiOperation("分页查询金融产品列表")
    public String getList(@RequestBody Map<String, Object> params) {
        try {
            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
            params.put("orgId", getOrgId());
            IPage<TblFinancialProduct> result = productService.getPage(pageNo, pageSize, params);
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("查询金融产品列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getById")
    @ApiOperation("根据ID查询金融产品详情")
    public String getById(@ApiParam("ID") @RequestParam Long ID) {
        try {
            TblFinancialProduct entity = productService.getDetail(ID);
            return entity == null ? JsonBean.error("数据不存在") : new JsonBean(1, "查询成功", entity).toString();
        } catch (Exception e) {
            log.error("查询金融产品详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation("新增金融产品")
    public String create(@RequestBody TblFinancialProduct entity) {
        try {
            entity.setOrgId(getOrgId());
            TblFinancialProduct result = productService.create(entity, getCurrentUser());
            return new JsonBean(1, "创建成功", result).toString();
        } catch (Exception e) {
            log.error("创建金融产品失败", e);
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation("修改金融产品")
    public String update(@RequestBody TblFinancialProduct entity) {
        try {
            boolean result = productService.update(entity, getCurrentUser());
            return result ? new JsonBean(1, "更新成功", null).toString() : JsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新金融产品失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除金融产品")
    public String delete(@ApiParam("ID") @RequestParam Long ID) {
        try {
            return productService.delete(ID) ? new JsonBean(1, "删除成功", null).toString() : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除金融产品失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除金融产品")
    public String batchDelete(@ApiParam("ID列表") @RequestParam String IDs) {
        try {
            String[] idArray = IDs.split(",");
            List<Long> idList = new java.util.ArrayList<>();
            for (String id : idArray) idList.add(Long.parseLong(id.trim()));
            return productService.batchDelete(idList) ? new JsonBean(1, "批量删除成功", null).toString() : JsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除金融产品失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateStatus")
    @ApiOperation("更新金融产品状态")
    public String updateStatus(@ApiParam("ID") @RequestParam Long ID, @ApiParam("状态") @RequestParam String productStatus) {
        try {
            return productService.updateStatus(ID, productStatus, getCurrentUser()) ? new JsonBean(1, "状态更新成功", null).toString() : JsonBean.error("状态更新失败");
        } catch (Exception e) {
            log.error("更新金融产品状态失败", e);
            return JsonBean.error("状态更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateShelfStatus")
    @ApiOperation("更新金融产品上下架状态")
    public String updateShelfStatus(@ApiParam("ID") @RequestParam Long ID, @ApiParam("上下架状态") @RequestParam String shelfStatus) {
        try {
            return productService.updateShelfStatus(ID, shelfStatus, getCurrentUser()) ? new JsonBean(1, "上下架状态更新成功", null).toString() : JsonBean.error("上下架状态更新失败");
        } catch (Exception e) {
            log.error("更新金融产品上下架状态失败", e);
            return JsonBean.error("上下架状态更新失败: " + e.getMessage());
        }
    }

    @GetMapping("/getEnabledList")
    @ApiOperation("获取启用的金融产品列表")
    public String getEnabledList() {
        try {
            return new JsonBean(1, "查询成功", productService.getEnabledList(getOrgId())).toString();
        } catch (Exception e) {
            log.error("获取启用的金融产品列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/checkCodeUnique")
    @ApiOperation("检查金融产品编码唯一性")
    public String checkCodeUnique(@ApiParam("编码") @RequestParam String productCode, @ApiParam("排除ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean isUnique = productService.checkCodeUnique(productCode, excludeId);
            return new JsonBean(isUnique ? 1 : 0, isUnique ? "编码可用" : "编码已存在", null).toString();
        } catch (Exception e) {
            log.error("检查编码唯一性失败", e);
            return JsonBean.error("检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/getStatistics")
    @ApiOperation("获取金融产品统计信息")
    public String getStatistics() {
        try {
            return new JsonBean(1, "查询成功", productService.getStatistics(getOrgId())).toString();
        } catch (Exception e) {
            log.error("获取金融产品统计信息失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getRiskLevelList")
    @ApiOperation("获取风险等级列表")
    public String getRiskLevelList() {
        try {
            Map<String, Object> riskLevels = new HashMap<>();
            riskLevels.put("R1", "低风险");
            riskLevels.put("R2", "中低风险");
            riskLevels.put("R3", "中等风险");
            riskLevels.put("R4", "中高风险");
            riskLevels.put("R5", "高风险");
            return new JsonBean(1, "查询成功", riskLevels).toString();
        } catch (Exception e) {
            log.error("获取风险等级列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getProductTypeList")
    @ApiOperation("获取产品类型列表")
    public String getProductTypeList() {
        try {
            Map<String, Object> productTypes = new HashMap<>();
            productTypes.put("DEPOSIT", "存款类");
            productTypes.put("LOAN", "贷款类");
            productTypes.put("FINANCING", "理财类");
            productTypes.put("BOND", "债券类");
            productTypes.put("FUND", "基金类");
            productTypes.put("INSURANCE", "保险类");
            productTypes.put("TRUST", "信托类");
            productTypes.put("OTHER", "其他");
            return new JsonBean(1, "查询成功", productTypes).toString();
        } catch (Exception e) {
            log.error("获取产品类型列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getCurrencyList")
    @ApiOperation("获取币种列表")
    public String getCurrencyList() {
        try {
            Map<String, Object> currencies = new HashMap<>();
            currencies.put("CNY", "人民币");
            currencies.put("USD", "美元");
            currencies.put("EUR", "欧元");
            currencies.put("HKD", "港币");
            currencies.put("JPY", "日元");
            currencies.put("GBP", "英镑");
            return new JsonBean(1, "查询成功", currencies).toString();
        } catch (Exception e) {
            log.error("获取币种列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/testData")
    @ApiOperation("测试数据是否存在")
    public String testData() {
        try {
            long totalCount = productService.count();
            Map<String, Object> result = new HashMap<>();
            result.put("totalRecords", totalCount);
            result.put("message", totalCount > 0 ? "数据存在" : "数据不存在,请先执行测试数据SQL");
            return new JsonBean(1, "查询成功", result).toString();
        } catch (Exception e) {
            log.error("测试数据查询失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    private Long getOrgId() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                Long orgId = loginStaff.getCurrentOrg().getOrgid().longValue();
                log.info("=== 当前用户orgId: {} ===", orgId);
                // 临时返回null,不限制ORG_ID,查询所有数据
                log.warn("=== 临时返回null,不限制ORG_ID ===");
                return null;
            }
        } catch (Exception e) {
            log.error("获取组织ID失败", e);
        }
        log.warn("未获取到用户orgId,返回null查询所有数据");
        return null;
    }

    private String getCurrentUser() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            return loginStaff != null ? loginStaff.getUsername() : "system";
        } catch (Exception e) { return "system"; }
    }
}

