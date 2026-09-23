package com.management.accountant.controller;

import com.management.accountant.oracle.entity.budget.TblAssetQuality;
import com.management.accountant.service.AssetQualityService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 资产质量评估控制器
 */
@RestController
@Api(tags = {"资产质量评估"})
@RequestMapping(value = "/v1/supervision/asset/quality")
@Slf4j
public class AssetQualityController {

    @Resource
    private AssetQualityService assetQualityService;

    @Operation(summary = "获取资产质量统计数据")
    @PostMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getStatistics(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = assetQualityService.getStatistics();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (Exception e) {
            log.error("获取资产质量统计数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取资产质量分页列表")
    @PostMapping("/list")
    public MyJsonBean<PageResult<TblAssetQuality>> getList(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<TblAssetQuality>> result = new MyJsonBean<>();
        try {
            PageResult<TblAssetQuality> pageResult = assetQualityService.getList(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("获取资产质量列表失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "新增资产质量记录")
    @PostMapping("/add")
    public MyJsonBean<TblAssetQuality> add(@RequestBody TblAssetQuality assetQuality) {
        MyJsonBean<TblAssetQuality> result = new MyJsonBean<>();
        try {
            TblAssetQuality saved = assetQualityService.add(assetQuality);
            result.setCode(1);
            result.setMsg("新增成功");
            result.setData(saved);
        } catch (Exception e) {
            log.error("新增资产质量记录失败", e);
            result.setCode(0);
            result.setMsg("新增失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新资产质量记录")
    @PostMapping("/update")
    public MyJsonBean<TblAssetQuality> update(@RequestBody TblAssetQuality assetQuality) {
        MyJsonBean<TblAssetQuality> result = new MyJsonBean<>();
        try {
            TblAssetQuality updated = assetQualityService.update(assetQuality);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(updated);
        } catch (Exception e) {
            log.error("更新资产质量记录失败", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除资产质量记录")
    @PostMapping("/delete")
    public MyJsonBean<String> delete(@RequestBody Map<String, Object> params) {
        MyJsonBean<String> result = new MyJsonBean<>();
        try {
            String id = params.get("assetQualityId") != null
                    ? params.get("assetQualityId").toString()
                    : (params.get("assetId") != null ? params.get("assetId").toString() : null);
            if (id == null) {
                result.setCode(0);
                result.setMsg("删除失败：缺少ID参数");
                return result;
            }
            assetQualityService.delete(id);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除资产质量记录失败", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取图表数据")
    @PostMapping("/charts")
    public MyJsonBean<Map<String, Object>> getCharts(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = assetQualityService.getChartData(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("获取图表数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Operation(summary = "批量评估")
    @PostMapping("/batch-assess")
    public MyJsonBean<String> batchAssess(@RequestBody Map<String, Object> params) {
        MyJsonBean<String> result = new MyJsonBean<>();
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("批量评估失败：缺少ID列表");
                return result;
            }
            assetQualityService.batchAssess(ids);
            result.setCode(1);
            result.setMsg("批量评估成功");
        } catch (Exception e) {
            log.error("批量评估失败", e);
            result.setCode(0);
            result.setMsg("批量评估失败：" + e.getMessage());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Operation(summary = "批量监控")
    @PostMapping("/batch-monitor")
    public MyJsonBean<String> batchMonitor(@RequestBody Map<String, Object> params) {
        MyJsonBean<String> result = new MyJsonBean<>();
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("批量监控失败：缺少ID列表");
                return result;
            }
            assetQualityService.batchMonitor(ids);
            result.setCode(1);
            result.setMsg("批量监控成功");
        } catch (Exception e) {
            log.error("批量监控失败", e);
            result.setCode(0);
            result.setMsg("批量监控失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出资产质量数据")
    @PostMapping("/export")
    public MyJsonBean<List<TblAssetQuality>> export(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<List<TblAssetQuality>> result = new MyJsonBean<>();
        try {
            List<TblAssetQuality> dataList = assetQualityService.export(params);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(dataList);
        } catch (Exception e) {
            log.error("导出资产质量数据失败", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "生成质量报告")
    @PostMapping("/generate-report")
    public MyJsonBean<Map<String, Object>> generateReport(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> report = assetQualityService.generateReport(params);
            result.setCode(1);
            result.setMsg("报告生成成功");
            result.setData(report);
        } catch (Exception e) {
            log.error("生成质量报告失败", e);
            result.setCode(0);
            result.setMsg("报告生成失败：" + e.getMessage());
        }
        return result;
    }
}
