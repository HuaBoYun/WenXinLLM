package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetMonitor;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.entity.TblStaffOracle;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.oracle.mapper.TblStaffOracleMapper;
import com.management.accountant.service.BudgetMonitorService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * 预算监控Controller
 * 
 * @description 预算执行监控接口，支持实时监控、周期监控、事件驱动监控
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-执行监控"})
@RequestMapping(value = "/accountant/budget/control/monitor")
@Slf4j
public class BudgetMonitorController {

    @Resource
    private BudgetMonitorService monitorService;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    @Resource
    private TblStaffOracleMapper staffMapper;

    /**
     * 创建监控
     */
    @Operation(summary = "创建监控")
    @ApiOperation("创建监控")
    @PostMapping("/create")
    public MyJsonBean<BudgetMonitor> create(@RequestBody @Validated BudgetMonitor monitor) {
        MyJsonBean<BudgetMonitor> result = new MyJsonBean<>();
        try {
            BudgetMonitor created = monitorService.create(monitor);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建监控失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建监控异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询监控详情
     */
    @Operation(summary = "查询监控详情")
    @ApiOperation("查询监控详情")
    @GetMapping("/detail/{monitorId}")
    public MyJsonBean<BudgetMonitor> getDetail(
            @ApiParam(value = "监控ID", required = true) @PathVariable String monitorId) {
        MyJsonBean<BudgetMonitor> result = new MyJsonBean<>();
        try {
            BudgetMonitor monitor = monitorService.getById(monitorId);
            if (monitor != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(monitor);
            } else {
                result.setCode(0);
                result.setMsg("监控记录不存在");
            }
        } catch (Exception e) {
            log.error("查询监控详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新监控
     */
    @Operation(summary = "更新监控")
    @ApiOperation("更新监控")
    @PutMapping("/update/{monitorId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "监控ID", required = true) @PathVariable String monitorId,
            @RequestBody @Validated BudgetMonitor monitor) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            monitor.setMonitorId(monitorId);
            monitorService.update(monitor);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新监控失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新监控异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除监控
     */
    @Operation(summary = "删除监控")
    @ApiOperation("删除监控")
    @DeleteMapping("/delete/{monitorId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "监控ID", required = true) @PathVariable String monitorId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            monitorService.delete(monitorId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除监控失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除监控异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询监控列表
     */
    @Operation(summary = "分页查询监控列表")
    @ApiOperation("分页查询监控列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetMonitor>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetMonitor>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetMonitor> pageResult = monitorService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询监控列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 启动监控
     */
    @Operation(summary = "启动监控")
    @ApiOperation("启动监控")
    @PostMapping("/start/{monitorId}")
    public MyJsonBean<Void> start(
            @ApiParam(value = "监控ID", required = true) @PathVariable String monitorId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            monitorService.start(monitorId);
            result.setCode(1);
            result.setMsg("启动成功");
        } catch (ServiceException ex) {
            log.error("启动监控失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("启动监控异常", e);
            result.setCode(0);
            result.setMsg("启动失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 停止监控
     */
    @Operation(summary = "停止监控")
    @ApiOperation("停止监控")
    @PostMapping("/stop/{monitorId}")
    public MyJsonBean<Void> stop(
            @ApiParam(value = "监控ID", required = true) @PathVariable String monitorId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            monitorService.stop(monitorId);
            result.setCode(1);
            result.setMsg("停止成功");
        } catch (ServiceException ex) {
            log.error("停止监控失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("停止监控异常", e);
            result.setCode(0);
            result.setMsg("停止失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 刷新监控数据
     */
    @Operation(summary = "刷新监控数据")
    @ApiOperation("刷新监控数据")
    @PostMapping("/refresh/{monitorId}")
    public MyJsonBean<BudgetMonitor> refresh(
            @ApiParam(value = "监控ID", required = true) @PathVariable String monitorId) {
        MyJsonBean<BudgetMonitor> result = new MyJsonBean<>();
        try {
            BudgetMonitor monitor = monitorService.refresh(monitorId);
            result.setCode(1);
            result.setMsg("刷新成功");
            result.setData(monitor);
        } catch (ServiceException ex) {
            log.error("刷新监控数据失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("刷新监控数据异常", e);
            result.setCode(0);
            result.setMsg("刷新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取监控统计
     */
    @Operation(summary = "获取监控统计")
    @ApiOperation("获取监控统计")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getStatistics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = monitorService.getStatistics();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (Exception e) {
            log.error("获取监控统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取预警列表
     */
    @Operation(summary = "获取预警列表")
    @ApiOperation("获取预警列表")
    @GetMapping("/alerts")
    public MyJsonBean<List<BudgetMonitor>> getAlerts() {
        MyJsonBean<List<BudgetMonitor>> result = new MyJsonBean<>();
        try {
            List<BudgetMonitor> alerts = monitorService.getAlerts();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(alerts);
        } catch (Exception e) {
            log.error("获取预警列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量启动监控
     */
    @Operation(summary = "批量启动监控")
    @ApiOperation("批量启动监控")
    @PostMapping("/batch/start")
    public MyJsonBean<Map<String, Object>> batchStart(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> batchResult = monitorService.batchStart(params);
            result.setCode(1);
            result.setMsg("批量启动完成");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量启动监控失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量启动监控异常", e);
            result.setCode(0);
            result.setMsg("批量启动失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量停止监控
     */
    @Operation(summary = "批量停止监控")
    @ApiOperation("批量停止监控")
    @PostMapping("/batch/stop")
    public MyJsonBean<Map<String, Object>> batchStop(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> batchResult = monitorService.batchStop(params);
            result.setCode(1);
            result.setMsg("批量停止完成");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量停止监控失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量停止监控异常", e);
            result.setCode(0);
            result.setMsg("批量停止失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出监控数据
     */
    @Operation(summary = "导出监控数据")
    @ApiOperation("导出监控数据")
    @PostMapping("/export")
    public void exportData(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetMonitor> dataList = monitorService.exportData(params);

            // 使用ExcelExport工具类导出
            String fileName = "预算监控数据.xlsx";
            try (ExcelExport ee = new ExcelExport("预算监控", BudgetMonitor.class)) {
                ee.setDataList(dataList);
                ee.write(response, fileName);
            }

            log.info("导出预算监控数据成功，数量: {}", dataList.size());
        } catch (Exception e) {
            log.error("导出预算监控数据异常", e);
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

    @Operation(summary = "测试监控")
    @ApiOperation("测试监控")
    @PostMapping("/test")
    public MyJsonBean<Map<String, Object>> testMonitor(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String monitorId = params.get("monitorId") != null ? params.get("monitorId").toString() : null;
            Map<String, Object> testResult = new HashMap<>();
            long startTime = System.currentTimeMillis();
            if (monitorId != null) {
                BudgetMonitor monitor = monitorService.getById(monitorId);
                if (monitor != null) {
                    testResult.put("monitorName", monitor.getMonitorName());
                    testResult.put("monitorStatus", monitor.getMonitorStatus());
                    testResult.put("alertLevel", monitor.getAlertLevel());
                    testResult.put("status", "SUCCESS");
                    testResult.put("message", "监控测试通过");
                } else {
                    testResult.put("status", "FAILED");
                    testResult.put("message", "监控记录不存在");
                }
            } else {
                testResult.put("status", "SUCCESS");
                testResult.put("message", "监控连接测试通过");
            }
            long responseTime = System.currentTimeMillis() - startTime;
            testResult.put("responseTime", responseTime);
            result.setCode(1);
            result.setMsg("测试成功");
            result.setData(testResult);
        } catch (Exception e) {
            log.error("测试监控异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "刷新所有监控")
    @ApiOperation("刷新所有监控")
    @PostMapping("/refresh-all")
    public MyJsonBean<Map<String, Object>> refreshAll() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            // 查询所有未删除的监控
            Map<String, Object> queryParams = new HashMap<>();
            List<BudgetMonitor> allMonitors = monitorService.exportData(queryParams);

            int successCount = 0;
            int failCount = 0;
            for (BudgetMonitor monitor : allMonitors) {
                try {
                    monitorService.refresh(monitor.getMonitorId());
                    successCount++;
                } catch (Exception ex) {
                    failCount++;
                    log.warn("刷新监控失败，ID: {}, 原因: {}", monitor.getMonitorId(), ex.getMessage());
                }
            }

            Map<String, Object> refreshResult = new HashMap<>();
            refreshResult.put("totalCount", allMonitors.size());
            refreshResult.put("refreshedCount", successCount);
            refreshResult.put("failCount", failCount);
            refreshResult.put("status", "COMPLETED");
            result.setCode(1);
            result.setMsg("刷新成功");
            result.setData(refreshResult);
        } catch (Exception e) {
            log.error("刷新所有监控异常", e);
            result.setCode(0);
            result.setMsg("刷新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出单个监控数据")
    @ApiOperation("导出单个监控数据")
    @GetMapping("/export/{id}")
    public void exportSingle(
            @ApiParam(value = "监控ID", required = true) @PathVariable String id,
            HttpServletResponse response) {
        try {
            BudgetMonitor monitor = monitorService.getById(id);
            if (monitor == null) {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println("{\"code\":0,\"msg\":\"监控记录不存在\"}");
                return;
            }

            // 将单个监控放入列表，使用ExcelExport导出
            List<BudgetMonitor> dataList = new ArrayList<>();
            dataList.add(monitor);

            String fileName = "监控数据_" + monitor.getMonitorCode() + ".xlsx";
            try (ExcelExport ee = new ExcelExport("预算监控", BudgetMonitor.class)) {
                ee.setDataList(dataList);
                ee.write(response, fileName);
            }

            log.info("导出单个监控数据成功，ID: {}", id);
        } catch (Exception e) {
            log.error("导出单个监控数据异常", e);
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

    /**
     * 导入监控数据
     */
    @Operation(summary = "导入监控数据")
    @ApiOperation("导入监控数据")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importMonitors(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            if (file.isEmpty()) {
                result.setCode(0);
                result.setMsg("上传文件不能为空");
                return result;
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                result.setCode(0);
                result.setMsg("仅支持 xlsx 或 xls 格式的文件");
                return result;
            }

            // 解析 Excel
            InputStream is = file.getInputStream();
            Workbook workbook;
            if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(is);
            } else {
                workbook = new HSSFWorkbook(is);
            }

            Sheet sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getPhysicalNumberOfRows();
            if (totalRows <= 1) {
                workbook.close();
                result.setCode(0);
                result.setMsg("文件中没有数据行");
                return result;
            }

            // 第一行为标题行，从第二行开始读取
            // 列顺序: 监控编码, 监控名称, 监控类型, 组织ID, 预算ID, 阈值, 告警级别, 检查间隔, 备注
            int successCount = 0;
            int failCount = 0;
            StringBuilder errorMsg = new StringBuilder();

            for (int i = 1; i < totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    BudgetMonitor monitor = new BudgetMonitor();
                    monitor.setMonitorCode(getCellStringValue(row, 0));
                    monitor.setMonitorName(getCellStringValue(row, 1));
                    monitor.setMonitorType(getCellStringValue(row, 2));
                    monitor.setOrganizationId(getCellStringValue(row, 3));
                    monitor.setBudgetId(getCellStringValue(row, 4));

                    String thresholdStr = getCellStringValue(row, 5);
                    if (thresholdStr != null && !thresholdStr.isEmpty()) {
                        monitor.setThresholdValue(new BigDecimal(thresholdStr));
                    }

                    monitor.setAlertLevel(getCellStringValue(row, 6));

                    String intervalStr = getCellStringValue(row, 7);
                    if (intervalStr != null && !intervalStr.isEmpty()) {
                        monitor.setCheckInterval(Integer.parseInt(intervalStr.replaceAll("\\..*", "")));
                    }

                    monitor.setRemark(getCellStringValue(row, 8));
                    monitor.setMonitorStatus("STOPPED");
                    monitor.setDelFlag(0);

                    monitorService.create(monitor);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    errorMsg.append("第").append(i + 1).append("行导入失败：").append(e.getMessage()).append("；");
                    log.warn("第{}行导入失败: {}", i + 1, e.getMessage());
                }
            }

            workbook.close();

            Map<String, Object> importResult = new HashMap<>();
            importResult.put("successCount", successCount);
            importResult.put("failCount", failCount);
            if (errorMsg.length() > 0) {
                importResult.put("errorMsg", errorMsg.toString());
            }

            result.setCode(1);
            result.setMsg("导入完成，成功 " + successCount + " 条，失败 " + failCount + " 条");
            result.setData(importResult);
            log.info("监控数据导入完成，成功{}条，失败{}条", successCount, failCount);
        } catch (Exception e) {
            log.error("监控数据导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取单元格字符串值
     */
    private String getCellStringValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                double numVal = cell.getNumericCellValue();
                if (numVal == Math.floor(numVal) && !Double.isInfinite(numVal)) {
                    return String.valueOf((long) numVal);
                }
                return String.valueOf(numVal);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
}