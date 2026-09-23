package com.financial.sharing.controller;

import com.financial.sharing.service.SubjectBalanceExportService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.vo.param.SubjectBalanceExportParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 科目余额导出控制器
 *
 * @author system
 * @since 2024-12-19
 */
@RestController
@RequestMapping("/general-ledger")
@CrossOrigin
@Api(tags = "科目余额导出")
public class SubjectBalanceExportController {

    @Autowired
    private SubjectBalanceExportService subjectBalanceExportService;

    @PostMapping("/opening-balance/export")
    @ApiOperation("导出科目余额")
    public void exportBalance(@RequestBody SubjectBalanceExportParam param,
                              HttpServletResponse response) {
        try {
            // 设置默认文件名
            if (param.getExportFileName() == null || param.getExportFileName().isEmpty()) {
                param.setExportFileName("科目余额表_" + param.getAccountingPeriod());
            }

            subjectBalanceExportService.exportBalance(param, response);
        } catch (Exception e) {
            throw new RuntimeException("导出失败: " + e.getMessage(), e);
        }
    }

    @PostMapping("/opening-balance/async-export")
    @ApiOperation("异步导出科目余额")
    public MyJsonBean<String> asyncExportBalance(@RequestBody SubjectBalanceExportParam param) {
        try {
            // 设置默认文件名
            if (param.getExportFileName() == null || param.getExportFileName().isEmpty()) {
                param.setExportFileName("科目余额表_" + param.getAccountingPeriod());
            }

            // 设置为异步导出
            param.setAsyncExport(true);

            String taskId = subjectBalanceExportService.asyncExportBalance(param);
            return MyJsonBean.successData("导出任务已创建", taskId);
        } catch (Exception e) {
            return MyJsonBean.errorData("创建导出任务失败: " + e.getMessage());
        }
    }

    @GetMapping("/opening-balance/export-progress/{taskId}")
    @ApiOperation("查询导出进度")
    public MyJsonBean getExportProgress(@ApiParam("任务ID") @PathVariable String taskId) {
        try {
            Object progress = subjectBalanceExportService.getExportProgress(taskId);
            return MyJsonBean.successData(progress);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询导出进度失败: " + e.getMessage());
        }
    }

    @PostMapping("/opening-balance/export-preview")
    @ApiOperation("导出预览（返回前100条数据）")
    public MyJsonBean exportPreview(@RequestBody SubjectBalanceExportParam param) {
        try {
            // 限制预览数据量
            param.setSubjectIds(null); // 清除科目ID限制
            Object previewData = subjectBalanceExportService.exportPreview(param);
            return MyJsonBean.successData(previewData);
        } catch (Exception e) {
            return MyJsonBean.errorData("预览失败: " + e.getMessage());
        }
    }

    @PostMapping("/opening-balance/export-cancel/{taskId}")
    @ApiOperation("取消导出任务")
    public MyJsonBean cancelExport(@ApiParam("任务ID") @PathVariable String taskId) {
        try {
            boolean result = subjectBalanceExportService.cancelExport(taskId);
            if (result) {
                return MyJsonBean.successData("导出任务已取消");
            } else {
                return MyJsonBean.errorData("取消任务失败，任务可能已完成或不存在");
            }
        } catch (Exception e) {
            return MyJsonBean.errorData("取消任务失败: " + e.getMessage());
        }
    }
}