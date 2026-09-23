package com.global.treasurer.financialProductDefinition.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.global.treasurer.financialProductDefinition.entity.TblProductTransactionEvent;
import com.global.treasurer.financialProductDefinition.service.TblProductTransactionEventService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
// import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        "/financial/product-definition/product-transaction-event",
})
@Api(tags = "产品交易事件管理")
public class ProductTransactionEventController {

    private static final Logger log = LoggerFactory.getLogger(ProductTransactionEventController.class);

    @Autowired
    private TblProductTransactionEventService transactionEventService;
    @Resource
    private UserProvider userProvider;

    @PostMapping("/getList")
    @ApiOperation("分页查询产品交易事件列表")
    public String getList(@ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
                          @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                          @ApiParam("事件编码") @RequestParam(required = false) String eventCode,
                          @ApiParam("事件名称") @RequestParam(required = false) String eventName,
                          @ApiParam("事件类型") @RequestParam(required = false) String eventType,
                          @ApiParam("产品类型") @RequestParam(required = false) String productType,
                          @ApiParam("事件状态") @RequestParam(required = false) String eventStatus,
                          @ApiParam("是否启用") @RequestParam(required = false) Integer isEnabled) {
        try {
            Long orgId = getOrgId();
            log.info("【调试】产品交易事件查询参数 - pageNo: {}, pageSize: {}, eventCode: {}, eventName: {}, eventType: {}, productType: {}, eventStatus: {}, isEnabled: {}, orgId: {}",
                    pageNo, pageSize, eventCode, eventName, eventType, productType, eventStatus, isEnabled, orgId);
            // 临时解决方案:不传递orgId,查询所有数据
            // IPage<TblProductTransactionEvent> result = transactionEventService.getPage(pageNo, pageSize, eventCode, eventName, eventType, productType, eventStatus, isEnabled, orgId);
            IPage<TblProductTransactionEvent> result = transactionEventService.getPage(pageNo, pageSize, eventCode, eventName, eventType, productType, eventStatus, isEnabled, null);
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());
            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("查询产品交易事件列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getById")
    @ApiOperation("根据ID查询产品交易事件详情")
    public String getById(@ApiParam("ID") @RequestParam Long ID) {
        try {
            TblProductTransactionEvent entity = transactionEventService.getDetail(ID);
            return entity == null ? JsonBean.error("数据不存在") : new JsonBean(1, "查询成功", entity).toString();
        } catch (Exception e) {
            log.error("查询产品交易事件详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation("新增产品交易事件")
    public String create(@RequestBody TblProductTransactionEvent entity) {
        try {
            entity.setOrgId(getOrgId());
            TblProductTransactionEvent result = transactionEventService.create(entity, getCurrentUser());
            return new JsonBean(1, "创建成功", result).toString();
        } catch (Exception e) {
            log.error("创建产品交易事件失败", e);
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation("修改产品交易事件")
    public String update(@RequestBody TblProductTransactionEvent entity) {
        try {
            boolean result = transactionEventService.update(entity, getCurrentUser());
            return result ? new JsonBean(1, "更新成功", null).toString() : JsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新产品交易事件失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除产品交易事件")
    public String delete(@ApiParam("ID") @RequestParam Long ID) {
        try {
            return transactionEventService.delete(ID) ? new JsonBean(1, "删除成功", null).toString() : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除产品交易事件失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除产品交易事件")
    public String batchDelete(@ApiParam("ID列表") @RequestParam String IDs) {
        try {
            String[] idArray = IDs.split(",");
            List<Long> idList = new java.util.ArrayList<>();
            for (String id : idArray) idList.add(Long.parseLong(id.trim()));
            return transactionEventService.batchDelete(idList) ? new JsonBean(1, "批量删除成功", null).toString() : JsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除产品交易事件失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateStatus")
    @ApiOperation("更新产品交易事件状态")
    public String updateStatus(@ApiParam("ID") @RequestParam Long ID, @ApiParam("状态") @RequestParam Integer isEnabled) {
        try {
            return transactionEventService.updateStatus(ID, isEnabled, getCurrentUser()) ? new JsonBean(1, "状态更新成功", null).toString() : JsonBean.error("状态更新失败");
        } catch (Exception e) {
            log.error("更新产品交易事件状态失败", e);
            return JsonBean.error("状态更新失败: " + e.getMessage());
        }
    }

    @GetMapping("/getEnabledList")
    @ApiOperation("获取启用的产品交易事件列表")
    public String getEnabledList() {
        try {
            return new JsonBean(1, "查询成功", transactionEventService.getEnabledList(getOrgId())).toString();
        } catch (Exception e) {
            log.error("获取启用的产品交易事件列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/getByProductType")
    @ApiOperation("根据产品类型获取交易事件列表")
    public String getByProductType(@ApiParam("产品类型") @RequestParam String productType) {
        try {
            return new JsonBean(1, "查询成功", transactionEventService.getByProductType(productType, getOrgId())).toString();
        } catch (Exception e) {
            log.error("根据产品类型获取交易事件列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/checkCodeUnique")
    @ApiOperation("检查产品交易事件编码唯一性")
    public String checkCodeUnique(@ApiParam("编码") @RequestParam String eventCode, @ApiParam("排除ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean isUnique = transactionEventService.checkCodeUnique(eventCode, excludeId);
            return new JsonBean(isUnique ? 1 : 0, isUnique ? "编码可用" : "编码已存在", null).toString();
        } catch (Exception e) {
            log.error("检查编码唯一性失败", e);
            return JsonBean.error("检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/getStatistics")
    @ApiOperation("获取产品交易事件统计信息")
    public String getStatistics() {
        try {
            Map<String, Object> result = new HashMap<>();
            List<TblProductTransactionEvent> allEvents = transactionEventService.list();
            List<TblProductTransactionEvent> enabledEvents = transactionEventService.getEnabledList(getOrgId());

            // 计算统计数据
            result.put("total", allEvents.size());
            result.put("enabled", enabledEvents.size());

            // 计算申购事件数量
            long purchaseCount = allEvents.stream()
                    .filter(e -> "INVESTMENT_PURCHASE".equals(e.getEventType()))
                    .count();
            result.put("purchase", purchaseCount);

            // 计算赎回事件数量
            long redemptionCount = allEvents.stream()
                    .filter(e -> "INVESTMENT_REDEMPTION".equals(e.getEventType()))
                    .count();
            result.put("redemption", redemptionCount);

            // 计算活跃状态事件数量
            long activeCount = allEvents.stream()
                    .filter(e -> "ACTIVE".equals(e.getEventStatus()))
                    .count();
            result.put("active", activeCount);

            return new JsonBean(1, "查询成功", result).toString();
        } catch (Exception e) {
            log.error("获取产品交易事件统计信息失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/getEventMonitorList")
    @ApiOperation("获取事件监控列表")
    public String getEventMonitorList(@RequestBody Map<String, Object> params) {
        try {
            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

            // 调用Service获取监控数据
            Map<String, Object> monitorData = transactionEventService.getEventMonitorData(pageNo, pageSize);

            return new JsonBean(1, "查询成功", monitorData).toString();
        } catch (Exception e) {
            log.error("获取事件监控列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出产品交易事件配置")
    public void export(@RequestBody(required = false) Map<String, Object> params,
                      javax.servlet.http.HttpServletResponse response) {
        com.global.treasurer.util.excel.ExcelExport export = null;
        try {
            // 查询所有事件数据
            List<TblProductTransactionEvent> list = transactionEventService.list();

            // 使用ExcelExport工具导出
            export = new com.global.treasurer.util.excel.ExcelExport(
                    "产品交易事件配置", TblProductTransactionEvent.class);
            export.setDataList(list);
            export.write(response, "产品交易事件配置.xlsx");
        } catch (Exception e) {
            log.error("导出产品交易事件配置失败", e);
            try {
                response.getWriter().write(JsonBean.error("导出失败: " + e.getMessage()).toString());
            } catch (Exception ex) {
                log.error("写入错误信息失败", ex);
            }
        } finally {
            if (export != null) {
                try {
                    export.close();
                } catch (Exception e) {
                    log.error("关闭ExcelExport失败", e);
                }
            }
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

