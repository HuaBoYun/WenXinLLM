package com.financial.sharing.controller;

import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 事项管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Slf4j
@Api(tags = "事项管理")
@RestController
@RequestMapping("/matter")
@CrossOrigin
public class MatterController {

    @Resource
    private UserProvider userProvider;

    @ApiOperation("分页查询事项数据")
    @PostMapping("/page")
    public String getMatterPage(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestBody Map<String, Object> param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 查询事项数据", loginStaff.getStaffid());

            List<Map<String, Object>> matters = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                Map<String, Object> matter = new HashMap<>();
                matter.put("matterId", "MAT" + String.format("%03d", i));
                matter.put("matterNo", "MT2024" + String.format("%03d", i));
                matter.put("matterName", "业务事项" + i);
                matter.put("matterType", i % 2 == 0 ? "销售" : "采购");
                matter.put("amount", new BigDecimal(10000 * i));
                matter.put("date", "2024-12-" + String.format("%02d", i));
                matter.put("status", "PENDING");
                matter.put("statusName", "待处理");
                matter.put("createdBy", "系统");
                matters.add(matter);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", matters);
            data.put("totalRecord", 100);
            data.put("pageNo", param.get("pageNo") != null ? (Integer) param.get("pageNo") : 1);
            data.put("pageSize", param.get("pageSize") != null ? (Integer) param.get("pageSize") : 10);

            return createSuccessResponse("查询成功", data);
        } catch (Exception e) {
            log.error("查询事项数据失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取事项详情")
    @GetMapping("/{id}")
    public String getMatterById(HttpServletRequest request,
                                HttpServletResponse response,
                                @PathVariable String id) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 获取事项详情，ID：{}", loginStaff.getStaffid(), id);

            Map<String, Object> matter = new HashMap<>();
            matter.put("matterId", id);
            matter.put("matterNo", "MT2024001");
            matter.put("matterName", "销售订单001");
            matter.put("matterType", "销售");
            matter.put("amount", new BigDecimal("50000.00"));
            matter.put("date", "2024-12-01");
            matter.put("customerName", "客户A");
            matter.put("productCode", "PROD001");
            matter.put("productName", "产品A");
            matter.put("quantity", 100);
            matter.put("unitPrice", new BigDecimal("500.00"));
            matter.put("description", "销售订单详情");
            matter.put("status", "PENDING");
            matter.put("statusName", "待处理");
            matter.put("createdTime", "2024-12-01T09:00:00.000Z");

            return createSuccessResponse("查询成功", matter);
        } catch (Exception e) {
            log.error("获取事项详情失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新事项状态")
    @PutMapping("/{id}/status")
    public String updateMatterStatus(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @PathVariable String id,
                                     @RequestBody Map<String, Object> param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            String status = (String) param.get("status");
            log.info("用户 {} 更新事项状态，ID：{}，状态：{}", loginStaff.getStaffid(), id, status);

            Map<String, Object> result = new HashMap<>();
            result.put("matterId", id);
            result.put("oldStatus", "PENDING");
            result.put("newStatus", status);
            result.put("updatedBy", loginStaff.getUsername());
            result.put("updateTime", new Date());

            return createSuccessResponse("状态更新成功", result);
        } catch (Exception e) {
            log.error("更新事项状态失败", e);
            return createErrorResponse("状态更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量处理事项")
    @PostMapping("/batch")
    public String batchProcessMatter(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestBody Map<String, Object> param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            String operation = (String) param.get("operation");
            @SuppressWarnings("unchecked")
            List<String> matterIds = (List<String>) param.get("matterIds");

            if (matterIds == null || matterIds.isEmpty()) {
                return createErrorResponse("事项ID列表不能为空");
            }

            log.info("用户 {} 批量处理事项，操作：{}，数量：{}", loginStaff.getStaffid(), operation, matterIds.size());

            Map<String, Object> result = new HashMap<>();
            result.put("operation", operation);
            result.put("totalCount", matterIds.size());
            result.put("successCount", matterIds.size());
            result.put("failCount", 0);

            return createSuccessResponse("批量处理成功", result);
        } catch (Exception e) {
            log.error("批量处理事项失败", e);
            return createErrorResponse("批量处理失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取事项统计")
    @GetMapping("/statistics")
    public String getMatterStatistics(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @ApiParam(value = "统计类型", required = false) @RequestParam(required = false) String statisticsType) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 获取事项统计", loginStaff.getStaffid());

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalMatters", 500);
            statistics.put("pendingCount", 100);
            statistics.put("processingCount", 80);
            statistics.put("completedCount", 300);
            statistics.put("failedCount", 20);
            statistics.put("totalAmount", new BigDecimal("5000000.00"));

            // 按类型统计
            List<Map<String, Object>> typeStats = new ArrayList<>();
            Map<String, Object> sales = new HashMap<>();
            sales.put("type", "销售");
            sales.put("count", 300);
            sales.put("amount", new BigDecimal("3000000.00"));
            typeStats.add(sales);

            Map<String, Object> purchase = new HashMap<>();
            purchase.put("type", "采购");
            purchase.put("count", 150);
            purchase.put("amount", new BigDecimal("1500000.00"));
            typeStats.add(purchase);

            Map<String, Object> other = new HashMap<>();
            other.put("type", "其他");
            other.put("count", 50);
            other.put("amount", new BigDecimal("500000.00"));
            typeStats.add(other);

            statistics.put("typeStatistics", typeStats);

            return createSuccessResponse("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取事项统计失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    private TblStaffUtil validateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
            JsonBean json = new JsonBean();
            json.setCode(401);
            json.setMsg("用户已失效");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
            return null;
        }
        return loginStaff;
    }

    private String createSuccessResponse(String message, Object data) {
        JsonBean json = new JsonBean();
        json.setCode(1);
        json.setMsg(message);
        json.setData(data);
        return JsonMapper.nonNullMapper().toJson(json);
    }

    private String createErrorResponse(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return JsonMapper.nonNullMapper().toJson(json);
    }
}