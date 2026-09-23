package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.oracle.entity.budget.BudgetYear;
import com.management.accountant.service.BudgetYearService;
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
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = {"NCV65全面预算-年度管理"})
@RequestMapping(value = "/accountant/budget/year")
@Slf4j
public class BudgetYearController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetYearService yearService;

    @Operation(summary = "创建年度")
    @ApiOperation("创建年度") @PostMapping("/create")
    public MyJsonBean<BudgetYear> create(@RequestBody @Validated BudgetYear data) {
        MyJsonBean<BudgetYear> r = new MyJsonBean<>();
        try {
            r.setCode(1);
            r.setMsg("创建成功");
            r.setData(yearService.create(data));
        } catch (Exception e) {
            log.error("创建年度异常", e);
            r.setCode(0);
            r.setMsg("创建失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "获取年度详情")
    @ApiOperation("获取年度详情") @GetMapping("/detail/{id}")
    public MyJsonBean<BudgetYear> detail(@PathVariable String id) {
        MyJsonBean<BudgetYear> r = new MyJsonBean<>();
        try {
            r.setCode(1);
            r.setMsg("查询成功");
            r.setData(yearService.getById(id));
        } catch (Exception e) {
            log.error("查询年度异常", e);
            r.setCode(0);
            r.setMsg("查询失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "更新年度")
    @ApiOperation("更新年度") @PutMapping("/update/{id}")
    public MyJsonBean<Void> update(@PathVariable String id, @RequestBody @Validated BudgetYear data) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try {
            data.setYearId(id);
            yearService.update(data);
            r.setCode(1);
            r.setMsg("更新成功");
        } catch (Exception e) {
            log.error("更新年度异常", e);
            r.setCode(0);
            r.setMsg("更新失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "删除年度")
    @ApiOperation("删除年度") @DeleteMapping("/delete/{id}")
    public MyJsonBean<Void> delete(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try {
            yearService.delete(id);
            r.setCode(1);
            r.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除年度异常", e);
            r.setCode(0);
            r.setMsg("删除失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "分页查询")
    @ApiOperation("分页查询") @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try {
            IPage<BudgetYear> pageResult = yearService.getPage(params);
            Map<String, Object> data = new java.util.HashMap<>();
            data.put("records", pageResult.getRecords());
            data.put("total", pageResult.getTotal());
            data.put("pageNum", pageResult.getCurrent());
            data.put("pageSize", pageResult.getSize());
            r.setCode(1);
            r.setMsg("查询成功");
            r.setData(data);
        } catch (Exception e) {
            log.error("分页查询异常", e);
            r.setCode(0);
            r.setMsg("查询失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "获取所有年度")
    @ApiOperation("获取所有年度") @GetMapping("/list")
    public MyJsonBean<List<BudgetYear>> getAllYears() {
        MyJsonBean<List<BudgetYear>> r = new MyJsonBean<>();
        try {
            r.setCode(1);
            r.setMsg("查询成功");
            r.setData(yearService.getAllYears());
        } catch (Exception e) {
            log.error("查询年度列表异常", e);
            r.setCode(0);
            r.setMsg("查询失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "开启年度")
    @ApiOperation("开启年度") @PostMapping("/{id}/open")
    public MyJsonBean<Void> openYear(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try {
            yearService.openYear(id);
            r.setCode(1);
            r.setMsg("开启成功");
        } catch (Exception e) {
            log.error("开启年度异常", e);
            r.setCode(0);
            r.setMsg("开启失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "关闭年度")
    @ApiOperation("关闭年度") @PostMapping("/{id}/close")
    public MyJsonBean<Void> closeYear(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try {
            yearService.closeYear(id);
            r.setCode(1);
            r.setMsg("关闭成功");
        } catch (Exception e) {
            log.error("关闭年度异常", e);
            r.setCode(0);
            r.setMsg("关闭失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "设为当前年度")
    @ApiOperation("设为当前年度") @PostMapping("/{id}/current")
    public MyJsonBean<Void> setCurrent(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try {
            yearService.setCurrent(id);
            r.setCode(1);
            r.setMsg("设置成功");
        } catch (Exception e) {
            log.error("设置当前年度异常", e);
            r.setCode(0);
            r.setMsg("设置失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "导出年度数据")
    @ApiOperation("导出年度数据") @PostMapping("/export")
    public void exportYears(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            IPage<BudgetYear> pageResult = yearService.getPage(params);
            new ExcelExport("预算年度数据", BudgetYear.class)
                    .setDataList(pageResult.getRecords())
                    .write(response, "预算年度.xlsx");
        } catch (Exception e) {
            log.error("导出年度数据异常", e);
        }
    }
}
