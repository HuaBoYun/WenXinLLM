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
 * 辅助核算管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Slf4j
@Api(tags = "辅助核算管理")
@RestController
@RequestMapping("/auxiliary-accounting")
@CrossOrigin
public class AuxiliaryAccountingController {

    @Resource
    private UserProvider userProvider;

    // ==================== 辅助核算管理 ====================

    @ApiOperation("分页查询辅助核算管理")
    @PostMapping("/page")
    public String getAuxiliaryAccountingPage(HttpServletRequest request,
                                             HttpServletResponse response,
                                             @RequestBody Map<String, Object> param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 查询辅助核算管理", loginStaff.getStaffid());

            List<Map<String, Object>> items = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", "AUXACC" + String.format("%03d", i));
                item.put("accountCode", "AUX" + String.format("%03d", i));
                item.put("accountName", "辅助核算" + i);
                item.put("accountType", i % 2 == 0 ? "部门" : "项目");
                item.put("subjectCode", String.valueOf(1000 + i));
                item.put("subjectName", "科目" + i);
                item.put("balance", new BigDecimal(10000 * i));
                item.put("debitAmount", new BigDecimal(5000 * i));
                item.put("creditAmount", new BigDecimal(3000 * i));
                item.put("status", 1);
                item.put("createdBy", "张三");
                item.put("createTime", "2024-12-01T10:00:00.000Z");
                items.add(item);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", items);
            data.put("totalRecord", 100);
            data.put("pageNo", param.get("pageNo") != null ? (Integer) param.get("pageNo") : 1);
            data.put("pageSize", param.get("pageSize") != null ? (Integer) param.get("pageSize") : 10);

            return createSuccessResponse("查询成功", data);
        } catch (Exception e) {
            log.error("查询辅助核算管理失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存辅助核算管理")
    @PostMapping("/save")
    public String saveAuxiliaryAccounting(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @RequestBody Map<String, Object> param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 保存辅助核算管理", loginStaff.getStaffid());

            Map<String, Object> result = new HashMap<>();
            result.put("id", "AUXACC" + System.currentTimeMillis());
            result.put("accountCode", param.get("accountCode"));
            result.put("accountName", param.get("accountName"));
            result.put("accountType", param.get("accountType"));
            result.put("message", "辅助核算保存成功");

            return createSuccessResponse("保存成功", result);
        } catch (Exception e) {
            log.error("保存辅助核算管理失败", e);
            return createErrorResponse("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新辅助核算管理")
    @PutMapping("/update")
    public String updateAuxiliaryAccounting(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody Map<String, Object> param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            String id = (String) param.get("id");
            if (id == null || id.isEmpty()) {
                return createErrorResponse("ID不能为空");
            }

            log.info("用户 {} 更新辅助核算管理，ID：{}", loginStaff.getStaffid(), id);

            Map<String, Object> result = new HashMap<>();
            result.put("id", id);
            result.put("accountCode", param.get("accountCode"));
            result.put("accountName", param.get("accountName"));
            result.put("accountType", param.get("accountType"));
            result.put("message", "辅助核算更新成功");

            return createSuccessResponse("更新成功", result);
        } catch (Exception e) {
            log.error("更新辅助核算管理失败", e);
            return createErrorResponse("更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除辅助核算管理")
    @DeleteMapping("/{id}")
    public String deleteAuxiliaryAccounting(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @PathVariable String id) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 删除辅助核算管理，ID：{}", loginStaff.getStaffid(), id);

            Map<String, Object> result = new HashMap<>();
            result.put("id", id);
            result.put("message", "辅助核算删除成功");

            return createSuccessResponse("删除成功", result);
        } catch (Exception e) {
            log.error("删除辅助核算管理失败", e);
            return createErrorResponse("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取辅助核算详情")
    @GetMapping("/{id}")
    public String getAuxiliaryAccountingById(HttpServletRequest request,
                                             HttpServletResponse response,
                                             @PathVariable String id) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 获取辅助核算详情，ID：{}", loginStaff.getStaffid(), id);

            Map<String, Object> detail = new HashMap<>();
            detail.put("id", id);
            detail.put("accountCode", "AUX001");
            detail.put("accountName", "部门1");
            detail.put("accountType", "部门");
            detail.put("subjectCode", "6601");
            detail.put("subjectName", "销售费用");
            detail.put("balance", new BigDecimal("50000.00"));
            detail.put("debitAmount", new BigDecimal("30000.00"));
            detail.put("creditAmount", new BigDecimal("20000.00"));
            detail.put("status", 1);
            detail.put("createdBy", "张三");
            detail.put("createTime", "2024-12-01T10:00:00.000Z");
            detail.put("updatedBy", "李四");
            detail.put("updateTime", "2024-12-01T15:00:00.000Z");

            return createSuccessResponse("查询成功", detail);
        } catch (Exception e) {
            log.error("获取辅助核算详情失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取辅助核算统计")
    @GetMapping("/statistics")
    public String getAuxiliaryAccountingStatistics(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   @ApiParam(value = "会计期间", required = false) @RequestParam(required = false) String period) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 获取辅助核算统计", loginStaff.getStaffid());

            List<Map<String, Object>> statistics = new ArrayList<>();

            // 按类型统计
            Map<String, Object> typeStats = new HashMap<>();
            typeStats.put("type", "按类型统计");
            List<Map<String, Object>> typeData = new ArrayList<>();

            Map<String, Object> deptStat = new HashMap<>();
            deptStat.put("type", "部门");
            deptStat.put("count", 15);
            deptStat.put("totalBalance", new BigDecimal("500000.00"));
            typeData.add(deptStat);

            Map<String, Object> projectStat = new HashMap<>();
            projectStat.put("type", "项目");
            projectStat.put("count", 8);
            projectStat.put("totalBalance", new BigDecimal("300000.00"));
            typeData.add(projectStat);

            Map<String, Object> employeeStat = new HashMap<>();
            employeeStat.put("type", "员工");
            employeeStat.put("count", 50);
            employeeStat.put("totalBalance", new BigDecimal("200000.00"));
            typeData.add(employeeStat);
            typeStats.put("data", typeData);
            statistics.add(typeStats);

            // 按科目统计
            Map<String, Object> subjectStats = new HashMap<>();
            subjectStats.put("type", "按科目统计");
            List<Map<String, Object>> subjectData = new ArrayList<>();

            Map<String, Object> expenseStat = new HashMap<>();
            expenseStat.put("subjectCode", "6601");
            expenseStat.put("subjectName", "销售费用");
            expenseStat.put("count", 25);
            expenseStat.put("totalBalance", new BigDecimal("200000.00"));
            subjectData.add(expenseStat);

            Map<String, Object> adminStat = new HashMap<>();
            adminStat.put("subjectCode", "6602");
            adminStat.put("subjectName", "管理费用");
            adminStat.put("count", 20);
            adminStat.put("totalBalance", new BigDecimal("150000.00"));
            subjectData.add(adminStat);
            subjectStats.put("data", subjectData);
            statistics.add(subjectStats);

            // 总体统计
            Map<String, Object> overall = new HashMap<>();
            overall.put("totalCount", 73);
            overall.put("totalBalance", new BigDecimal("1000000.00"));
            overall.put("totalDebit", new BigDecimal("600000.00"));
            overall.put("totalCredit", new BigDecimal("400000.00"));

            Map<String, Object> result = new HashMap<>();
            result.put("period", period);
            result.put("statistics", statistics);
            result.put("overall", overall);

            return createSuccessResponse("查询成功", result);
        } catch (Exception e) {
            log.error("获取辅助核算统计失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量操作辅助核算")
    @PostMapping("/batch")
    public String batchAuxiliaryAccounting(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @RequestBody Map<String, Object> param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            String operation = (String) param.get("operation");
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) param.get("ids");

            if (operation == null || operation.isEmpty()) {
                return createErrorResponse("操作类型不能为空");
            }

            if (ids == null || ids.isEmpty()) {
                return createErrorResponse("ID列表不能为空");
            }

            log.info("用户 {} 批量操作辅助核算，操作：{}，数量：{}", loginStaff.getStaffid(), operation, ids.size());

            Map<String, Object> result = new HashMap<>();
            result.put("operation", operation);
            result.put("totalCount", ids.size());
            result.put("successCount", ids.size());
            result.put("failCount", 0);
            result.put("message", "批量操作成功");

            return createSuccessResponse("操作成功", result);
        } catch (Exception e) {
            log.error("批量操作辅助核算失败", e);
            return createErrorResponse("操作失败: " + e.getMessage());
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