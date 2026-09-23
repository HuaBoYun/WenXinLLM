package com.financial.sharing.controller;

import com.financial.sharing.service.LedgerQueryExportService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.vo.param.LedgerQueryExportParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 总账查询导出控制器
 *
 * @author system
 * @since 2024-12-19
 */
@RestController
@RequestMapping("/general-ledger")
@CrossOrigin
@Api(tags = "总账查询导出")
public class LedgerQueryExportController {

    @Autowired
    private LedgerQueryExportService ledgerQueryExportService;

    @PostMapping("/ledger-query/export")
    @ApiOperation("导出总账查询数据")
    public void exportLedgerQuery(@RequestBody LedgerQueryExportParam param,
                                  HttpServletResponse response) {
        try {
            // 设置默认文件名
            if (param.getExportFileName() == null || param.getExportFileName().isEmpty()) {
                param.setExportFileName("总账查询_" + param.getStartDate() + "_" + param.getEndDate());
            }

            ledgerQueryExportService.exportLedgerQuery(param, response);
        } catch (Exception e) {
            throw new RuntimeException("导出失败: " + e.getMessage(), e);
        }
    }

    @PostMapping("/ledger-query/async-export")
    @ApiOperation("异步导出总账查询数据")
    public MyJsonBean<String> asyncExportLedgerQuery(@RequestBody LedgerQueryExportParam param) {
        try {
            // 设置默认文件名
            if (param.getExportFileName() == null || param.getExportFileName().isEmpty()) {
                param.setExportFileName("总账查询_" + param.getStartDate() + "_" + param.getEndDate());
            }

            // 设置为异步导出
            param.setAsyncExport(true);

            String taskId = ledgerQueryExportService.asyncExportLedgerQuery(param);
            return MyJsonBean.successData("导出任务已创建", taskId);
        } catch (Exception e) {
            return MyJsonBean.errorData("创建导出任务失败: " + e.getMessage());
        }
    }

    @GetMapping("/ledger-query/export-progress/{taskId}")
    @ApiOperation("查询导出进度")
    public MyJsonBean getExportProgress(@ApiParam("任务ID") @PathVariable String taskId) {
        try {
            Object progress = ledgerQueryExportService.getExportProgress(taskId);
            return MyJsonBean.successData(progress);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询导出进度失败: " + e.getMessage());
        }
    }

    @PostMapping("/ledger-query/export-preview")
    @ApiOperation("导出预览（返回前100条数据）")
    public MyJsonBean exportPreview(@RequestBody LedgerQueryExportParam param) {
        try {
            // 限制预览数据量
            Object previewData = ledgerQueryExportService.exportPreview(param);
            return MyJsonBean.successData(previewData);
        } catch (Exception e) {
            return MyJsonBean.errorData("预览失败: " + e.getMessage());
        }
    }

    @PostMapping("/ledger-query/export-cancel/{taskId}")
    @ApiOperation("取消导出任务")
    public MyJsonBean cancelExport(@ApiParam("任务ID") @PathVariable String taskId) {
        try {
            boolean result = ledgerQueryExportService.cancelExport(taskId);
            if (result) {
                return MyJsonBean.successData("导出任务已取消");
            } else {
                return MyJsonBean.errorData("取消任务失败，任务可能已完成或不存在");
            }
        } catch (Exception e) {
            return MyJsonBean.errorData("取消任务失败: " + e.getMessage());
        }
    }

    @PostMapping("/ledger-query/export-templates")
    @ApiOperation("获取导出模板列表")
    public MyJsonBean getExportTemplates() {
        try {
            Map<String, Object> templates = ledgerQueryExportService.getExportTemplates();
            return MyJsonBean.successData(templates);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取模板失败: " + e.getMessage());
        }
    }
}