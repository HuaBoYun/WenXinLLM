package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetPeriod;
import com.management.accountant.service.BudgetPeriodService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.excel.ExcelExport;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = {"NCV65全面预算-期间管理"})
@RequestMapping(value = "/accountant/budget/period")
@Slf4j
public class BudgetPeriodController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetPeriodService periodService;

    @Operation(summary = "创建期间")
    @ApiOperation("创建期间") @PostMapping("/create")
    public MyJsonBean<BudgetPeriod> create(@RequestBody @Validated BudgetPeriod data) {
        MyJsonBean<BudgetPeriod> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("创建成功"); r.setData(periodService.create(data));
        } catch (ServiceException ex) { r.setCode(0); r.setMsg(ex.getMessage());
        } catch (Exception e) { log.error("创建期间异常", e); r.setCode(0); r.setMsg("创建失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "获取期间详情")
    @ApiOperation("获取期间详情") @GetMapping("/detail/{id}")
    public MyJsonBean<BudgetPeriod> detail(@PathVariable String id) {
        MyJsonBean<BudgetPeriod> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(periodService.getById(id));
        } catch (Exception e) { r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "更新期间")
    @ApiOperation("更新期间") @PutMapping("/update/{id}")
    public MyJsonBean<Void> update(@PathVariable String id, @RequestBody @Validated BudgetPeriod data) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { data.setPeriodId(id); periodService.update(data); r.setCode(1); r.setMsg("更新成功");
        } catch (ServiceException ex) { r.setCode(0); r.setMsg(ex.getMessage());
        } catch (Exception e) { log.error("更新期间异常", e); r.setCode(0); r.setMsg("更新失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "删除期间")
    @ApiOperation("删除期间") @DeleteMapping("/delete/{id}")
    public MyJsonBean<Void> delete(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { periodService.delete(id); r.setCode(1); r.setMsg("删除成功");
        } catch (Exception e) { log.error("删除期间异常", e); r.setCode(0); r.setMsg("删除失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "分页查询")
    @ApiOperation("分页查询") @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(periodService.getPage(params));
        } catch (Exception e) { log.error("分页查询异常", e); r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "初始化年度")
    @ApiOperation("初始化年度") @PostMapping("/init-year")
    public MyJsonBean<Void> initYear(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { periodService.initYear(params); r.setCode(1); r.setMsg("初始化成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("初始化失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "关闭年度")
    @ApiOperation("关闭年度") @PostMapping("/close-year")
    public MyJsonBean<Void> closeYear(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { periodService.closeYear(params); r.setCode(1); r.setMsg("关闭成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("关闭失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "开启期间")
    @ApiOperation("开启期间") @PostMapping("/{id}/open")
    public MyJsonBean<Void> openPeriod(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { periodService.openPeriod(id); r.setCode(1); r.setMsg("开启成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("开启失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "关闭期间")
    @ApiOperation("关闭期间") @PostMapping("/{id}/close")
    public MyJsonBean<Void> closePeriod(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { periodService.closePeriod(id); r.setCode(1); r.setMsg("关闭成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("关闭失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "批量开启")
    @ApiOperation("批量开启") @PostMapping("/batch-open")
    public MyJsonBean<Void> batchOpen(@RequestBody List<String> ids) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { periodService.batchOpen(ids); r.setCode(1); r.setMsg("批量开启成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("批量开启失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "批量关闭")
    @ApiOperation("批量关闭") @PostMapping("/batch-close")
    public MyJsonBean<Void> batchClose(@RequestBody List<String> ids) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { periodService.batchClose(ids); r.setCode(1); r.setMsg("批量关闭成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("批量关闭失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "设为当前期间")
    @ApiOperation("设为当前期间") @PostMapping("/{id}/current")
    public MyJsonBean<Void> setCurrent(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { periodService.setCurrent(id); r.setCode(1); r.setMsg("设置成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("设置失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "更新锁定状态")
    @ApiOperation("更新锁定状态") @PutMapping("/{id}/lock")
    public MyJsonBean<Void> updateLockStatus(@PathVariable String id, @RequestBody Map<String, Object> body) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try { periodService.updateLockStatus(id, (Boolean) body.get("isLocked")); r.setCode(1); r.setMsg("更新成功");
        } catch (Exception e) { r.setCode(0); r.setMsg("更新失败：" + e.getMessage()); }
        return r;
    }

    @Operation(summary = "导出期间数据")
    @ApiOperation("导出期间数据") @PostMapping("/export")
    public void exportPeriods(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            periodService.exportPeriods(params, response);
        } catch (Exception e) {
            log.error("导出期间数据异常", e);
        }
    }

    @Operation(summary = "导出单个期间")
    @ApiOperation("导出单个期间") @GetMapping("/{id}/export")
    public void exportSingle(@PathVariable String id, HttpServletResponse response) {
        try {
            BudgetPeriod period = periodService.getById(id);
            if (period != null) {
                List<BudgetPeriod> list = new ArrayList<>();
                list.add(period);
                new ExcelExport("预算期间数据", BudgetPeriod.class)
                        .setDataList(list)
                        .write(response, "预算期间_" + id + ".xlsx");
            }
        } catch (Exception e) {
            log.error("导出单个期间异常", e);
        }
    }

    @Operation(summary = "获取统计数据")
    @ApiOperation("获取统计数据") @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try { r.setCode(1); r.setMsg("查询成功"); r.setData(periodService.getStats());
        } catch (Exception e) { log.error("获取统计数据异常", e); r.setCode(0); r.setMsg("查询失败：" + e.getMessage()); }
        return r;
    }
}

