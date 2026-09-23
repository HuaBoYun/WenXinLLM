package com.global.treasurer.financialProductDefinition.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.global.treasurer.financialProductDefinition.entity.TblProductInterestRule;
import com.global.treasurer.financialProductDefinition.service.TblProductInterestRuleService;
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
@RequestMapping({"/financial/product-definition/product-interest-rule", 
})
@Api(tags = "产品利息规则管理")
public class ProductInterestRuleController {
    private static final Logger log = LoggerFactory.getLogger(ProductInterestRuleController.class);

    @Autowired
    private TblProductInterestRuleService interestRuleService;
    @Resource
    private UserProvider userProvider;

    @PostMapping("/getList")
    @ApiOperation("分页查询产品利息规则列表")
    public String getList(@ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
                          @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                          @ApiParam("规则编码") @RequestParam(required = false) String ruleCode,
                          @ApiParam("规则名称") @RequestParam(required = false) String ruleName,
                          @ApiParam("产品类型") @RequestParam(required = false) String productType,
                          @ApiParam("是否启用") @RequestParam(required = false) Integer isEnabled) {
        try {
            Long orgId = getOrgId();
            IPage<TblProductInterestRule> result = interestRuleService.getPage(pageNo, pageSize, ruleCode, ruleName, productType, isEnabled, orgId);
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("查询产品利息规则列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getById")
    @ApiOperation("根据ID查询产品利息规则详情")
    public String getById(@ApiParam("ID") @RequestParam Long ID) {
        try {
            TblProductInterestRule entity = interestRuleService.getDetail(ID);
            return entity == null ? JsonBean.error("数据不存在") : new JsonBean(1, "查询成功", entity).toString();
        } catch (Exception e) {
            log.error("查询产品利息规则详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation("新增产品利息规则")
    public String create(@RequestBody TblProductInterestRule entity) {
        try {
            entity.setOrgId(getOrgId());
            TblProductInterestRule result = interestRuleService.create(entity, getCurrentUser());
            return new JsonBean(1, "创建成功", result).toString();
        } catch (Exception e) {
            log.error("创建产品利息规则失败", e);
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation("修改产品利息规则")
    public String update(@RequestBody TblProductInterestRule entity) {
        try {
            boolean result = interestRuleService.update(entity, getCurrentUser());
            return result ? new JsonBean(1, "更新成功", null).toString() : JsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新产品利息规则失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除产品利息规则")
    public String delete(@ApiParam("ID") @RequestParam Long ID) {
        try {
            return interestRuleService.delete(ID) ? new JsonBean(1, "删除成功", null).toString() : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除产品利息规则失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除产品利息规则")
    public String batchDelete(@ApiParam("ID列表") @RequestParam String IDs) {
        try {
            String[] idArray = IDs.split(",");
            List<Long> idList = new java.util.ArrayList<>();
            for (String id : idArray) idList.add(Long.parseLong(id.trim()));
            return interestRuleService.batchDelete(idList) ? new JsonBean(1, "批量删除成功", null).toString() : JsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除产品利息规则失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateStatus")
    @ApiOperation("更新产品利息规则状态")
    public String updateStatus(@ApiParam("ID") @RequestParam Long ID, @ApiParam("状态") @RequestParam Integer isEnabled) {
        try {
            return interestRuleService.updateStatus(ID, isEnabled, getCurrentUser()) ? new JsonBean(1, "状态更新成功", null).toString() : JsonBean.error("状态更新失败");
        } catch (Exception e) {
            log.error("更新产品利息规则状态失败", e);
            return JsonBean.error("状态更新失败: " + e.getMessage());
        }
    }

    @GetMapping("/getEnabledList")
    @ApiOperation("获取启用的产品利息规则列表")
    public String getEnabledList() {
        try {
            return new JsonBean(1, "查询成功", interestRuleService.getEnabledList(getOrgId())).toString();
        } catch (Exception e) {
            log.error("获取启用的产品利息规则列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getByProductType")
    @ApiOperation("根据产品类型获取利息规则列表")
    public String getByProductType(@ApiParam("产品类型") @RequestParam String productType) {
        try {
            return new JsonBean(1, "查询成功", interestRuleService.getByProductType(productType, getOrgId())).toString();
        } catch (Exception e) {
            log.error("根据产品类型获取利息规则列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/checkCodeUnique")
    @ApiOperation("检查产品利息规则编码唯一性")
    public String checkCodeUnique(@ApiParam("编码") @RequestParam String ruleCode, @ApiParam("排除ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean isUnique = interestRuleService.checkCodeUnique(ruleCode, excludeId);
            return new JsonBean(isUnique ? 1 : 0, isUnique ? "编码可用" : "编码已存在", null).toString();
        } catch (Exception e) {
            log.error("检查编码唯一性失败", e);
            return JsonBean.error("检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/calculate")
    @ApiOperation("利息计算")
    public String calculate(@RequestBody Map<String, Object> params) {
        try {
            Long principal = params.get("principal") != null ? Long.parseLong(params.get("principal").toString()) : 0L;
            Double rate = params.get("rate") != null ? Double.parseDouble(params.get("rate").toString()) : 0.0;
            Integer days = params.get("days") != null ? Integer.parseInt(params.get("days").toString()) : 0;

            // 利息 = 本金 × 年利率 × 天数 / 365
            Double interest = principal * rate * days / 365.0;

            Map<String, Object> result = new HashMap<>();
            result.put("principal", principal);
            result.put("rate", rate);
            result.put("days", days);
            result.put("interest", interest);
            result.put("totalAmount", principal + interest);
            return new JsonBean(1, "计算成功", result).toString();
        } catch (Exception e) {
            log.error("利息计算失败", e);
            return JsonBean.error("计算失败: " + e.getMessage());
        }
    }

    private Long getOrgId() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                Long orgId = loginStaff.getCurrentOrg().getOrgid().longValue();
                log.info("当前用户orgId: {}", orgId);
                return orgId;
            }
        } catch (Exception e) { log.error("获取组织ID失败", e); }
        log.warn("未获取到用户orgId，使用默认值1");
        return 1L;
    }

    private String getCurrentUser() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            return loginStaff != null ? loginStaff.getUsername() : "system";
        } catch (Exception e) { return "system"; }
    }
}

