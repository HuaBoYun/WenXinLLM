package com.global.treasurer.financialProductDefinition.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.global.treasurer.financialProductDefinition.entity.TblTransactionType;
import com.global.treasurer.financialProductDefinition.service.TblTransactionTypeService;
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
@RequestMapping({
        "/financial/product-definition/transaction-type",
})
@Api(tags = "交易类型管理")
public class TransactionTypeController {
    private static final Logger log = LoggerFactory.getLogger(TransactionTypeController.class);

    @Autowired
    private TblTransactionTypeService transactionTypeService;
    @Resource
    private UserProvider userProvider;

    @PostMapping("/getList")
    @ApiOperation("分页查询交易类型列表")
    public String getList(@ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
                          @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                          @ApiParam("类型编码") @RequestParam(required = false) String transactionTypeCode,
                          @ApiParam("类型名称") @RequestParam(required = false) String transactionTypeName,
                          @ApiParam("交易分类") @RequestParam(required = false) String transactionCategory,
                          @ApiParam("交易方向") @RequestParam(required = false) String transactionDirection,
                          @ApiParam("风险等级") @RequestParam(required = false) String riskLevel,
                          @ApiParam("是否启用") @RequestParam(required = false) Integer isEnabled) {
        try {
            Long orgId = getOrgId();
            // 临时解决方案:不传递orgId,查询所有数据
            // IPage<TblTransactionType> result = transactionTypeService.getPage(pageNo, pageSize, transactionTypeCode, transactionTypeName, transactionCategory, transactionDirection, riskLevel, isEnabled, orgId);
            IPage<TblTransactionType> result = transactionTypeService.getPage(pageNo, pageSize, transactionTypeCode, transactionTypeName, transactionCategory, transactionDirection, riskLevel, isEnabled, null);
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("查询交易类型列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getById")
    @ApiOperation("根据ID查询交易类型详情")
    public String getById(@ApiParam("ID") @RequestParam Long ID) {
        try {
            TblTransactionType entity = transactionTypeService.getDetail(ID);
            return entity == null ? JsonBean.error("数据不存在") : new JsonBean(1, "查询成功", entity).toString();
        } catch (Exception e) {
            log.error("查询交易类型详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation("新增交易类型")
    public String create(@RequestBody TblTransactionType entity) {
        try {
            entity.setOrgId(getOrgId());
            TblTransactionType result = transactionTypeService.create(entity, getCurrentUser());
            return new JsonBean(1, "创建成功", result).toString();
        } catch (Exception e) {
            log.error("创建交易类型失败", e);
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation("修改交易类型")
    public String update(@RequestBody TblTransactionType entity) {
        try {
            boolean result = transactionTypeService.update(entity, getCurrentUser());
            return result ? new JsonBean(1, "更新成功", null).toString() : JsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新交易类型失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除交易类型")
    public String delete(@ApiParam("ID") @RequestParam Long ID) {
        try {
            return transactionTypeService.delete(ID) ? new JsonBean(1, "删除成功", null).toString() : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除交易类型失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除交易类型")
    public String batchDelete(@ApiParam("ID列表") @RequestParam String IDs) {
        try {
            String[] idArray = IDs.split(",");
            List<Long> idList = new java.util.ArrayList<>();
            for (String id : idArray) idList.add(Long.parseLong(id.trim()));
            return transactionTypeService.batchDelete(idList) ? new JsonBean(1, "批量删除成功", null).toString() : JsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除交易类型失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateStatus")
    @ApiOperation("更新交易类型状态")
    public String updateStatus(@ApiParam("ID") @RequestParam Long ID, @ApiParam("状态") @RequestParam Integer isEnabled) {
        try {
            return transactionTypeService.updateStatus(ID, isEnabled, getCurrentUser()) ? new JsonBean(1, "状态更新成功", null).toString() : JsonBean.error("状态更新失败");
        } catch (Exception e) {
            log.error("更新交易类型状态失败", e);
            return JsonBean.error("状态更新失败: " + e.getMessage());
        }
    }

    @GetMapping("/getEnabledList")
    @ApiOperation("获取启用的交易类型列表")
    public String getEnabledList() {
        try {
            return new JsonBean(1, "查询成功", transactionTypeService.getEnabledList(getOrgId())).toString();
        } catch (Exception e) {
            log.error("获取启用的交易类型列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getTree")
    @ApiOperation("获取交易类型树形结构")
    public String getTree(@ApiParam("组织ID") @RequestParam(required = false) Long orgId) {
        try {
            return new JsonBean(1, "查询成功", transactionTypeService.getTree(orgId != null ? orgId : getOrgId())).toString();
        } catch (Exception e) {
            log.error("获取交易类型树形结构失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/checkCodeUnique")
    @ApiOperation("检查交易类型编码唯一性")
    public String checkCodeUnique(@ApiParam("编码") @RequestParam String transactionTypeCode, @ApiParam("排除ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean isUnique = transactionTypeService.checkCodeUnique(transactionTypeCode, excludeId);
            return new JsonBean(isUnique ? 1 : 0, isUnique ? "编码可用" : "编码已存在", null).toString();
        } catch (Exception e) {
            log.error("检查编码唯一性失败", e);
            return JsonBean.error("检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/getStatistics")
    @ApiOperation("获取交易类型统计信息")
    public String getStatistics() {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("total", transactionTypeService.count());
            result.put("enabled", transactionTypeService.getEnabledList(getOrgId()).size());
            result.put("categories", 6); // 交易分类数量
            result.put("today", 0); // 今日交易数量（需要从交易记录表统计）
            return new JsonBean(1, "查询成功", result).toString();
        } catch (Exception e) {
            log.error("获取交易类型统计信息失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/sort")
    @ApiOperation("排序")
    public String sort(@RequestBody List<com.global.treasurer.financialProductDefinition.entity.TblTransactionType> list) {
        try {
            boolean result = transactionTypeService.sort(list);
            return result ? new JsonBean(1, "排序成功", null).toString() : JsonBean.error("排序失败");
        } catch (Exception e) {
            log.error("排序失败", e);
            return JsonBean.error("排序失败: " + e.getMessage());
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

