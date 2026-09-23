package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetFreeze;
import com.management.accountant.oracle.entity.budget.BudgetFreezeHistory;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.entity.TblStaffOracle;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.oracle.mapper.TblStaffOracleMapper;
import com.management.accountant.service.BudgetFreezeService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import com.management.accountant.util.excel.ExcelExport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 预算冻结Controller
 * 
 * @description 预算冻结管理接口，支持全额冻结、部分冻结、临时冻结等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-预算冻结"})
@RequestMapping(value = "/accountant/budget/freeze")
@Slf4j
public class BudgetFreezeController {

    @Resource
    private BudgetFreezeService freezeService;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    @Resource
    private TblStaffOracleMapper staffMapper;

    /**
     * 创建预算冻结
     */
    @Operation(summary = "创建预算冻结")
    @ApiOperation("创建预算冻结")
    @PostMapping("/create")
    public MyJsonBean<BudgetFreeze> create(@RequestBody @Validated BudgetFreeze freeze) {
        MyJsonBean<BudgetFreeze> result = new MyJsonBean<>();
        try {
            BudgetFreeze created = freezeService.create(freeze);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建预算冻结失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建预算冻结异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询冻结详情
     */
    @Operation(summary = "查询冻结详情")
    @ApiOperation("查询冻结详情")
    @GetMapping("/detail/{freezeId}")
    public MyJsonBean<BudgetFreeze> getDetail(
            @ApiParam(value = "冻结ID", required = true) @PathVariable String freezeId) {
        MyJsonBean<BudgetFreeze> result = new MyJsonBean<>();
        try {
            BudgetFreeze freeze = freezeService.getById(freezeId);
            if (freeze != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(freeze);
            } else {
                result.setCode(0);
                result.setMsg("预算冻结不存在");
            }
        } catch (Exception e) {
            log.error("查询冻结详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新预算冻结
     */
    @Operation(summary = "更新预算冻结")
    @ApiOperation("更新预算冻结")
    @PutMapping("/update/{freezeId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "冻结ID", required = true) @PathVariable String freezeId,
            @RequestBody @Validated BudgetFreeze freeze) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            freeze.setFreezeId(freezeId);
            freezeService.update(freeze);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新预算冻结失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新预算冻结异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除预算冻结
     */
    @Operation(summary = "删除预算冻结")
    @ApiOperation("删除预算冻结")
    @DeleteMapping("/delete/{freezeId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "冻结ID", required = true) @PathVariable String freezeId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            freezeService.delete(freezeId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除预算冻结失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除预算冻结异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询冻结列表
     */
    @Operation(summary = "分页查询冻结列表")
    @ApiOperation("分页查询冻结列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetFreeze>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetFreeze>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetFreeze> pageResult = freezeService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询冻结列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行冻结
     */
    @Operation(summary = "执行冻结")
    @ApiOperation("执行冻结")
    @PostMapping("/execute/{freezeId}")
    public MyJsonBean<Void> executeFreeze(
            @ApiParam(value = "冻结ID", required = true) @PathVariable String freezeId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            freezeService.executeFreeze(freezeId);
            result.setCode(1);
            result.setMsg("冻结成功");
        } catch (ServiceException ex) {
            log.error("执行冻结失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行冻结异常", e);
            result.setCode(0);
            result.setMsg("冻结失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 解冻
     */
    @Operation(summary = "解冻")
    @ApiOperation("解冻")
    @PostMapping("/unfreeze/{freezeId}")
    public MyJsonBean<Void> unfreeze(
            @ApiParam(value = "冻结ID", required = true) @PathVariable String freezeId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            freezeService.unfreeze(freezeId);
            result.setCode(1);
            result.setMsg("解冻成功");
        } catch (ServiceException ex) {
            log.error("解冻失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("解冻异常", e);
            result.setCode(0);
            result.setMsg("解冻失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量冻结
     */
    @Operation(summary = "批量冻结")
    @ApiOperation("批量冻结")
    @PostMapping("/batch/freeze")
    public MyJsonBean<Map<String, Object>> batchFreeze(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> batchResult = freezeService.batchFreeze(params);
            result.setCode(1);
            result.setMsg("批量冻结完成");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量冻结失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量冻结异常", e);
            result.setCode(0);
            result.setMsg("批量冻结失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量解冻
     */
    @Operation(summary = "批量解冻")
    @ApiOperation("批量解冻")
    @PostMapping("/batch/unfreeze")
    public MyJsonBean<Map<String, Object>> batchUnfreeze(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> batchResult = freezeService.batchUnfreeze(params);
            result.setCode(1);
            result.setMsg("批量解冻完成");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量解冻失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量解冻异常", e);
            result.setCode(0);
            result.setMsg("批量解冻失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出冻结数据
     */
    @Operation(summary = "导出冻结数据")
    @ApiOperation("导出冻结数据")
    @PostMapping("/export")
    public void exportData(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetFreeze> dataList = freezeService.exportData(params);

            // 使用ExcelExport工具类导出
            String fileName = "预算冻结数据.xlsx";
            try (ExcelExport ee = new ExcelExport("预算冻结", BudgetFreeze.class)) {
                ee.setDataList(dataList);
                ee.write(response, fileName);
            }

            log.info("导出预算冻结数据成功，数量: {}", dataList.size());
        } catch (Exception e) {
            log.error("导出预算冻结数据异常", e);
            try {
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (java.io.IOException ex) {
                log.error("响应写入异常", ex);
            }
        }
    }

    @Operation(summary = "获取组织列表")
    @ApiOperation("获取组织列表")
    @GetMapping("/organizations")
    public MyJsonBean<List<Map<String, Object>>> getOrganizations() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetOrganization> wrapper = new QueryWrapper<>();
            wrapper.eq("IS_ENABLED", 1);
            wrapper.orderByAsc("SORT_ORDER");
            List<BudgetOrganization> orgList = organizationMapper.selectList(wrapper);
            List<Map<String, Object>> orgs = new ArrayList<>();
            for (BudgetOrganization org : orgList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", org.getOrganizationId());
                map.put("name", org.getOrganizationName());
                map.put("code", org.getOrganizationCode());
                orgs.add(map);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(orgs);
        } catch (Exception e) {
            log.error("获取组织列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取预算科目列表")
    @ApiOperation("获取预算科目列表")
    @GetMapping("/accounts")
    public MyJsonBean<List<Map<String, Object>>> getAccounts() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetAccount> wrapper = new QueryWrapper<>();
            wrapper.eq("IS_ENABLED", 1);
            wrapper.orderByAsc("SORT_ORDER");
            List<BudgetAccount> accList = accountMapper.selectList(wrapper);
            List<Map<String, Object>> accounts = new ArrayList<>();
            for (BudgetAccount acc : accList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", acc.getAccountId());
                map.put("name", acc.getAccountName());
                map.put("code", acc.getAccountCode());
                accounts.add(map);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(accounts);
        } catch (Exception e) {
            log.error("获取预算科目列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取用户列表")
    @ApiOperation("获取用户列表")
    @GetMapping("/users")
    public MyJsonBean<List<Map<String, Object>>> getUsers() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
           List<TblStaffOracle> staffList = staffMapper.selectAllActiveStaff();
            List<Map<String, Object>> users = new ArrayList<>();
            for (TblStaffOracle staff : staffList) {
               Map<String, Object> map = new HashMap<>();
               map.put("id", staff.getStaffId());
               map.put("name", staff.getRealName());
               map.put("department", staff.getWorkUnitName());
               users.add(map);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(users);
        } catch (Exception e) {
            log.error("获取用户列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "验证冻结")
    @ApiOperation("验证冻结")
    @PostMapping("/validate")
    public MyJsonBean<Map<String, Object>> validateFreeze(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String freezeId = params.get("freezeId") != null ? params.get("freezeId").toString() : null;
            Map<String, Object> validateResult = new HashMap<>();
            boolean valid = true;
            String message = "冻结验证通过";
            if (freezeId != null) {
                BudgetFreeze freeze = freezeService.getById(freezeId);
                if (freeze == null) {
                    valid = false;
                    message = "冻结记录不存在";
                } else if ("FROZEN".equals(freeze.getFreezeStatus())) {
                    valid = false;
                    message = "该记录已处于冻结状态";
                } else if (freeze.getFreezeAmount() == null || freeze.getFreezeAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                    valid = false;
                    message = "冻结金额无效";
                }
            }
            validateResult.put("valid", valid);
            validateResult.put("message", message);
            result.setCode(1);
            result.setMsg("验证成功");
            result.setData(validateResult);
        } catch (Exception e) {
            log.error("验证冻结异常", e);
            result.setCode(0);
            result.setMsg("验证失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出单个冻结数据")
    @ApiOperation("导出单个冻结数据")
    @GetMapping("/export/{id}")
    public void exportSingle(
            @ApiParam(value = "冻结ID", required = true) @PathVariable String id,
            HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=freeze_" + id + ".xlsx");
            StringBuilder sb = new StringBuilder();
            sb.append("ID,冻结标题,冻结类型,冻结金额,状态,创建时间\n");
            BudgetFreeze freeze = freezeService.getById(id);
            if (freeze != null) {
                sb.append(freeze.getFreezeId() != null ? freeze.getFreezeId() : id).append(",");
                sb.append(freeze.getFreezeTitle() != null ? freeze.getFreezeTitle() : "").append(",");
                sb.append(freeze.getFreezeType() != null ? freeze.getFreezeType() : "").append(",");
                sb.append(freeze.getFreezeAmount() != null ? freeze.getFreezeAmount() : "").append(",");
                sb.append(freeze.getFreezeStatus() != null ? freeze.getFreezeStatus() : "").append(",");
                sb.append(freeze.getCreateTime() != null ? freeze.getCreateTime() : "").append("\n");
            }
            response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出单个冻结数据异常", e);
        }
    }

    /**
     * 获取冻结统计信息
     */
    @Operation(summary = "获取冻结统计信息")
    @ApiOperation("获取冻结统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getStatistics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = freezeService.getStatistics();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (Exception e) {
            log.error("获取冻结统计信息异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询冻结操作历史
     */
    @Operation(summary = "查询冻结操作历史")
    @ApiOperation("查询冻结操作历史")
    @GetMapping("/history/{freezeId}")
    public MyJsonBean<List<BudgetFreezeHistory>> getHistory(
            @ApiParam(value = "冻结ID", required = true) @PathVariable String freezeId) {
        MyJsonBean<List<BudgetFreezeHistory>> result = new MyJsonBean<>();
        try {
            List<BudgetFreezeHistory> historyList = freezeService.getHistory(freezeId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(historyList);
        } catch (ServiceException ex) {
            log.error("查询冻结历史失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("查询冻结历史异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 延期冻结
     */
    @Operation(summary = "延期冻结")
    @ApiOperation("延期冻结")
    @PostMapping("/extend/{freezeId}")
    public MyJsonBean<Void> extendFreeze(
            @ApiParam(value = "冻结ID", required = true) @PathVariable String freezeId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String dateStr = params.get("newPlannedUnfreezeDate") != null
                    ? params.get("newPlannedUnfreezeDate").toString() : null;
            String reason = params.get("reason") != null
                    ? params.get("reason").toString() : null;

            if (dateStr == null || dateStr.isEmpty()) {
                result.setCode(0);
                result.setMsg("新的计划解冻日期不能为空");
                return result;
            }

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date newDate = sdf.parse(dateStr);

            freezeService.extendFreeze(freezeId, newDate, reason);
            result.setCode(1);
            result.setMsg("延期成功");
        } catch (ServiceException ex) {
            log.error("延期冻结失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (java.text.ParseException pe) {
            log.error("日期格式错误", pe);
            result.setCode(0);
            result.setMsg("日期格式错误，请使用 yyyy-MM-dd 格式");
        } catch (Exception e) {
            log.error("延期冻结异常", e);
            result.setCode(0);
            result.setMsg("延期失败：" + e.getMessage());
        }
        return result;
    }
}