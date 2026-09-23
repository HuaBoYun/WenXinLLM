package com.global.treasurer.financialProductDefinition.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.global.treasurer.financialProductDefinition.dto.PrincipalCalculateRequest;
import com.global.treasurer.financialProductDefinition.dto.PrincipalCalculateResponse;
import com.global.treasurer.financialProductDefinition.entity.TblProductPrincipalRule;
import com.global.treasurer.financialProductDefinition.service.TblProductPrincipalRuleService;
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
@RequestMapping({"/financial/product-definition/principal-rule"})
@Api(tags = "产品本金规则管理")
public class ProductPrincipalRuleController {
    private static final Logger log = LoggerFactory.getLogger(ProductPrincipalRuleController.class);

    @Autowired
    private TblProductPrincipalRuleService ruleService;
    @Resource
    private UserProvider userProvider;

    @GetMapping("/getList")
    @ApiOperation("分页查询产品本金规则列表")
    public String getList(@ApiParam("页码") @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @ApiParam("每页数量") @RequestParam(name = "limit", defaultValue = "20") Integer limit,
                          @ApiParam("规则编码") @RequestParam(name = "ruleCode", required = false) String ruleCode,
                          @ApiParam("规则名称") @RequestParam(name = "ruleName", required = false) String ruleName,
                          @ApiParam("产品类型") @RequestParam(name = "productType", required = false) String productType,
                          @ApiParam("是否启用") @RequestParam(name = "isEnabled", required = false) Integer isEnabled) {
        try {
            log.info("=== 接收到的查询参数 ===");
            log.info("page: {}, limit: {}, ruleCode: {}, ruleName: {}, productType: {}, isEnabled: {}",
                     page, limit, ruleCode, ruleName, productType, isEnabled);

            Long orgId = getOrgId();
            log.info("查询产品本金规则列表 - page:{}, limit:{}, orgId:{}, ruleCode:{}, ruleName:{}, productType:{}, isEnabled:{}",
                     page, limit, orgId, ruleCode, ruleName, productType, isEnabled);

            // 暂时移除orgId过滤,查询所有数据以便调试
            IPage<TblProductPrincipalRule> result = ruleService.getPage(page, limit, ruleCode, ruleName, productType, isEnabled, null);
            // 如果上述查询有数据,说明是orgId匹配问题
            // IPage<TblProductPrincipalRule> result = ruleService.getPage(page, limit, ruleCode, ruleName, productType, isEnabled, orgId);

            log.info("查询结果 - 总记录数:{}, 当前页记录数:{}", result.getTotal(), result.getRecords().size());

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            String response = new JsonBean(1, "查询成功", data).toString();
            log.info("返回数据: {}", response);
            return response;
        } catch (Exception e) {
            log.error("查询产品本金规则列表失败", e);
            e.printStackTrace();
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    @ApiOperation("测试查询-不使用分页")
    public String test() {
        try {
            Long orgId = getOrgId();
            log.info("测试查询 - orgId:{}", orgId);

            // 不使用分页,直接查询所有数据
            List<TblProductPrincipalRule> list = ruleService.list();
            log.info("测试查询结果 - 记录数:{}", list.size());

            // 打印前3条记录
            for (int i = 0; i < Math.min(3, list.size()); i++) {
                TblProductPrincipalRule rule = list.get(i);
                log.info("记录{} - RULE_ID:{}, RULE_CODE:{}, RULE_NAME:{}, ORG_ID:{}",
                         i+1, rule.getRuleId(), rule.getRuleCode(), rule.getRuleName(), rule.getOrgId());
            }

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", list);
            data.put("totalRecord", list.size());

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("测试查询失败", e);
            e.printStackTrace();
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getById")
    @ApiOperation("根据ID查询产品本金规则详情")
    public String getById(@ApiParam("ID") @RequestParam Long ID) {
        try {
            TblProductPrincipalRule entity = ruleService.getDetail(ID);
            return entity == null ? JsonBean.error("数据不存在") : new JsonBean(1, "查询成功", entity).toString();
        } catch (Exception e) {
            log.error("查询产品本金规则详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation("新增产品本金规则")
    public String create(@RequestBody TblProductPrincipalRule entity) {
        try {
            entity.setOrgId(getOrgId());
            TblProductPrincipalRule result = ruleService.create(entity, getCurrentUser());
            return new JsonBean(1, "创建成功", result).toString();
        } catch (Exception e) {
            log.error("创建产品本金规则失败", e);
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation("修改产品本金规则")
    public String update(@RequestBody TblProductPrincipalRule entity) {
        try {
            boolean result = ruleService.update(entity, getCurrentUser());
            return result ? new JsonBean(1, "更新成功", null).toString() : JsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新产品本金规则失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除产品本金规则")
    public String delete(@ApiParam("ID") @RequestParam Long ID) {
        try {
            return ruleService.delete(ID) ? new JsonBean(1, "删除成功", null).toString() : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除产品本金规则失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除产品本金规则")
    public String batchDelete(@ApiParam("ID列表") @RequestParam String IDs) {
        try {
            String[] idArray = IDs.split(",");
            List<Long> idList = new java.util.ArrayList<>();
            for (String id : idArray) idList.add(Long.parseLong(id.trim()));
            return ruleService.batchDelete(idList) ? new JsonBean(1, "批量删除成功", null).toString() : JsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除产品本金规则失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateStatus")
    @ApiOperation("更新产品本金规则状态")
    public String updateStatus(@ApiParam("ID") @RequestParam Long ID, @ApiParam("状态") @RequestParam Integer isEnabled) {
        try {
            return ruleService.updateStatus(ID, isEnabled, getCurrentUser()) ? new JsonBean(1, "状态更新成功", null).toString() : JsonBean.error("状态更新失败");
        } catch (Exception e) {
            log.error("更新产品本金规则状态失败", e);
            return JsonBean.error("状态更新失败: " + e.getMessage());
        }
    }

    @GetMapping("/getEnabledList")
    @ApiOperation("获取启用的产品本金规则列表")
    public String getEnabledList() {
        try {
            return new JsonBean(1, "查询成功", ruleService.getEnabledList(getOrgId())).toString();
        } catch (Exception e) {
            log.error("获取启用的产品本金规则列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getByProductType")
    @ApiOperation("根据产品类型获取本金规则列表")
    public String getByProductType(@ApiParam("产品类型") @RequestParam String productType) {
        try {
            return new JsonBean(1, "查询成功", ruleService.getByProductType(productType, getOrgId())).toString();
        } catch (Exception e) {
            log.error("根据产品类型获取本金规则列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/checkCodeUnique")
    @ApiOperation("检查产品本金规则编码唯一性")
    public String checkCodeUnique(@ApiParam("编码") @RequestParam String ruleCode, @ApiParam("排除ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean isUnique = ruleService.checkCodeUnique(ruleCode, excludeId);
            return new JsonBean(isUnique ? 1 : 0, isUnique ? "编码可用" : "编码已存在", null).toString();
        } catch (Exception e) {
            log.error("检查编码唯一性失败", e);
            return JsonBean.error("检查失败: " + e.getMessage());
        }
    }

    @PostMapping("/calculate")
    @ApiOperation("本金计算")
    public String calculate(@RequestBody PrincipalCalculateRequest request) {
        try {
            log.info("本金计算请求 - ruleId:{}, amount:{}, period:{}, unit:{}",
                     request.getRuleId(), request.getInvestmentAmount(),
                     request.getInvestmentPeriod(), request.getPeriodUnit());

            PrincipalCalculateResponse result = ruleService.calculate(request);
            return new JsonBean(1, "计算成功", result).toString();
        } catch (Exception e) {
            log.error("本金计算失败", e);
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
        log.warn("未获取到用户orgId,使用默认值1");
        return 1L;
    }

    private String getCurrentUser() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            return loginStaff != null ? loginStaff.getUsername() : "system";
        } catch (Exception e) { return "system"; }
    }
}
