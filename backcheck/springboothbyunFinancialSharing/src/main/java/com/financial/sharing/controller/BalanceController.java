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
 * 余额管理控制器
 *
 * @author system
 * @since 2024-12-07
 */
@Slf4j
@Api(tags = "余额管理")
@RestController
@RequestMapping("/balance")
@CrossOrigin
public class BalanceController {

    @Resource
    private UserProvider userProvider;

    @ApiOperation("刷新科目余额")
    @PostMapping("/refresh")
    public String refreshBalance(HttpServletRequest request,
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
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 模拟刷新结果
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", "TASK" + System.currentTimeMillis());
            result.put("status", "RUNNING");
            result.put("message", "余额刷新任务已启动");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("刷新成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("刷新科目余额失败", e);
            return createErrorResponse("刷新失败: " + e.getMessage());
        }
    }

    @ApiOperation("分页查询余额列表")
    @PostMapping("/getList")
    public String getBalancePage(HttpServletRequest request,
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
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 模拟余额数据
            List<Map<String, Object>> balanceList = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                Map<String, Object> balance = new HashMap<>();
                balance.put("balanceId", "BAL" + String.format("%03d", i));
                balance.put("accountCode", "100" + String.format("%02d", i));
                balance.put("accountName", "科目" + i);
                balance.put("period", "2024-12");
                balance.put("currencyCode", "CNY");
                balance.put("beginBalance", new BigDecimal(10000 * i));
                balance.put("debitAmount", new BigDecimal(5000 * i));
                balance.put("creditAmount", new BigDecimal(3000 * i));
                balance.put("endBalance", new BigDecimal(12000 * i));
                balance.put("status", "NORMAL");
                balanceList.add(balance);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", balanceList);
            data.put("totalRecord", 100);
            data.put("pageNo", param.get("pageNo") != null ? (Integer) param.get("pageNo") : 1);
            data.put("pageSize", param.get("pageSize") != null ? (Integer) param.get("pageSize") : 10);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(data);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询余额列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("余额调整")
    @PostMapping("/adjustment")
    public String createBalanceAdjustment(HttpServletRequest request,
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
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            Map<String, Object> result = new HashMap<>();
            result.put("adjustmentId", "ADJ" + System.currentTimeMillis());
            result.put("status", "PENDING");
            result.put("message", "余额调整申请已创建");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("调整成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("创建余额调整失败", e);
            return createErrorResponse("调整失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID查询余额详情")
    @GetMapping("/{balanceId}")
    public String getBalanceById(HttpServletRequest request,
                                HttpServletResponse response,
                                @PathVariable String balanceId) {
        try {
            // 权限验证
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

            Map<String, Object> balance = new HashMap<>();
            balance.put("balanceId", balanceId);
            balance.put("accountCode", "1001");
            balance.put("accountName", "现金");
            balance.put("period", "2024-12");
            balance.put("currencyCode", "CNY");
            balance.put("beginBalance", new BigDecimal("10000.00"));
            balance.put("debitAmount", new BigDecimal("5000.00"));
            balance.put("creditAmount", new BigDecimal("3000.00"));
            balance.put("endBalance", new BigDecimal("12000.00"));
            balance.put("status", "NORMAL");
            balance.put("statusName", "正常");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(balance);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询余额详情失败", e);
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
        return JsonMapper.nonNullMapper().toJson(json);
    }
}