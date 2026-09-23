package com.management.accountant.controller.eps;

import com.management.accountant.util.MyJsonBean;
import com.management.accountant.entity.eps.EpsBudgetImportLog;
import com.management.accountant.service.eps.EpsBudgetDataImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 预算数据导入导出控制器
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Api(tags = "预算数据导入导出")
@RestController
@RequestMapping("/eps/budget-data-import")
@Validated
public class EpsBudgetDataImportController {

    @Autowired
    private EpsBudgetDataImportService budgetDataImportService;

    /**
     * 上传预算数据文件
     */
    @ApiOperation("上传预算数据文件")
    @PostMapping("/upload")
    public MyJsonBean<Map<String, Object>> uploadBudgetDataFile(
            @ApiParam("上传文件") @RequestParam("file") MultipartFile file,
            @ApiParam("预算体系ID") @RequestParam @NotNull Long systemId,
            @ApiParam("预算版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("模板ID") @RequestParam(required = false) Long templateId,
            @ApiParam("导入类型") @RequestParam(defaultValue = "EXCEL") String importType) {
        try {
            Map<String, Object> result = budgetDataImportService.uploadBudgetDataFile(
                    file, systemId, versionId, templateId, importType);
            return MyJsonBean.success("文件上传成功", result);
        } catch (Exception e) {
            log.error("上传预算数据文件失败", e);
            return MyJsonBean.error("上传失败：" + e.getMessage());
        }
    }

    /**
     * 验证预算数据
     */
    @ApiOperation("验证预算数据")
    @PostMapping("/validate")
    public MyJsonBean<Map<String, Object>> validateBudgetData(
            @ApiParam("导入批次号") @RequestParam @NotNull String batchNumber,
            @ApiParam("验证规则") @RequestParam(required = false) String validationRules) {
        try {
            Map<String, Object> result = budgetDataImportService.validateBudgetData(batchNumber, validationRules);
            return MyJsonBean.success("数据验证完成", result);
        } catch (Exception e) {
            log.error("验证预算数据失败", e);
            return MyJsonBean.error("验证失败：" + e.getMessage());
        }
    }

    /**
     * 确认导入预算数据
     */
    @ApiOperation("确认导入预算数据")
    @PostMapping("/confirm")
    public MyJsonBean<Boolean> confirmImportBudgetData(
            @ApiParam("导入批次号") @RequestParam @NotNull String batchNumber,
            @ApiParam("是否覆盖已有数据") @RequestParam(defaultValue = "false") Boolean overwrite) {
        try {
            boolean result = budgetDataImportService.confirmImportBudgetData(batchNumber, overwrite);
            if (result) {
                return MyJsonBean.success("导入成功", true);
            } else {
                return MyJsonBean.error("导入失败");
            }
        } catch (Exception e) {
            log.error("确认导入预算数据失败", e);
            return MyJsonBean.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 取消导入
     */
    @ApiOperation("取消导入")
    @PostMapping("/cancel")
    public MyJsonBean<Boolean> cancelImport(
            @ApiParam("导入批次号") @RequestParam @NotNull String batchNumber) {
        try {
            boolean result = budgetDataImportService.cancelImport(batchNumber);
            if (result) {
                return MyJsonBean.success("取消导入成功", true);
            } else {
                return MyJsonBean.error("取消导入失败");
            }
        } catch (Exception e) {
            log.error("取消导入失败", e);
            return MyJsonBean.error("取消失败：" + e.getMessage());
        }
    }

    /**
     * 导出预算数据
     */
    @ApiOperation("导出预算数据")
    @GetMapping("/export")
    public void exportBudgetData(
            @ApiParam("预算体系ID") @RequestParam @NotNull Long systemId,
            @ApiParam("预算版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("导出格式") @RequestParam(defaultValue = "EXCEL") String exportFormat,
            @ApiParam("导出范围") @RequestParam(required = false) String exportScope,
            @ApiParam("科目ID列表") @RequestParam(required = false) List<Long> subjectIds,
            @ApiParam("组织ID列表") @RequestParam(required = false) List<Long> orgIds,
            HttpServletResponse response) {
        try {
            budgetDataImportService.exportBudgetData(
                    systemId, versionId, exportFormat, exportScope, subjectIds, orgIds, response);
        } catch (Exception e) {
            log.error("导出预算数据失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    /**
     * 下载导入模板
     */
    @ApiOperation("下载导入模板")
    @GetMapping("/template/download")
    public void downloadImportTemplate(
            @ApiParam("模板ID") @RequestParam @NotNull Long templateId,
            @ApiParam("模板格式") @RequestParam(defaultValue = "EXCEL") String templateFormat,
            HttpServletResponse response) {
        try {
            budgetDataImportService.downloadImportTemplate(templateId, templateFormat, response);
        } catch (Exception e) {
            log.error("下载导入模板失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"下载失败：" + e.getMessage() + "\"}");
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    /**
     * 查询导入历史
     */
    @ApiOperation("查询导入历史")
    @GetMapping("/history")
    public MyJsonBean<List<EpsBudgetImportLog>> getImportHistory(
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId,
            @ApiParam("预算版本ID") @RequestParam(required = false) Long versionId,
            @ApiParam("导入状态") @RequestParam(required = false) String importStatus,
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate,
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size) {
        try {
            List<EpsBudgetImportLog> result = budgetDataImportService.getImportHistory(
                    systemId, versionId, importStatus, startDate, endDate, current, size);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询导入历史失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询导入详情
     */
    @ApiOperation("查询导入详情")
    @GetMapping("/detail/{batchNumber}")
    public MyJsonBean<Map<String, Object>> getImportDetail(
            @ApiParam("导入批次号") @PathVariable @NotNull String batchNumber) {
        try {
            Map<String, Object> result = budgetDataImportService.getImportDetail(batchNumber);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询导入详情失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询导入进度
     */
    @ApiOperation("查询导入进度")
    @GetMapping("/progress/{batchNumber}")
    public MyJsonBean<Map<String, Object>> getImportProgress(
            @ApiParam("导入批次号") @PathVariable @NotNull String batchNumber) {
        try {
            Map<String, Object> result = budgetDataImportService.getImportProgress(batchNumber);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询导入进度失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 重新导入
     */
    @ApiOperation("重新导入")
    @PostMapping("/retry")
    public MyJsonBean<Boolean> retryImport(
            @ApiParam("导入批次号") @RequestParam @NotNull String batchNumber) {
        try {
            boolean result = budgetDataImportService.retryImport(batchNumber);
            if (result) {
                return MyJsonBean.success("重新导入成功", true);
            } else {
                return MyJsonBean.error("重新导入失败");
            }
        } catch (Exception e) {
            log.error("重新导入失败", e);
            return MyJsonBean.error("重新导入失败：" + e.getMessage());
        }
    }

    /**
     * 删除导入记录
     */
    @ApiOperation("删除导入记录")
    @DeleteMapping("/{batchNumber}")
    public MyJsonBean<Boolean> deleteImportRecord(
            @ApiParam("导入批次号") @PathVariable @NotNull String batchNumber) {
        try {
            boolean result = budgetDataImportService.deleteImportRecord(batchNumber);
            if (result) {
                return MyJsonBean.success("删除成功", true);
            } else {
                return MyJsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除导入记录失败", e);
            return MyJsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除导入记录
     */
    @ApiOperation("批量删除导入记录")
    @DeleteMapping("/batch")
    public MyJsonBean<Boolean> batchDeleteImportRecords(
            @RequestBody List<String> batchNumbers) {
        try {
            boolean result = budgetDataImportService.batchDeleteImportRecords(batchNumbers);
            if (result) {
                return MyJsonBean.success("批量删除成功", true);
            } else {
                return MyJsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除导入记录失败", e);
            return MyJsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 清理过期导入记录
     */
    @ApiOperation("清理过期导入记录")
    @PostMapping("/cleanup")
    public MyJsonBean<Integer> cleanupExpiredImportRecords(
            @ApiParam("保留天数") @RequestParam(defaultValue = "30") Integer retentionDays) {
        try {
            int cleanupCount = budgetDataImportService.cleanupExpiredImportRecords(retentionDays);
            return MyJsonBean.success("清理完成，共清理" + cleanupCount + "条记录", cleanupCount);
        } catch (Exception e) {
            log.error("清理过期导入记录失败", e);
            return MyJsonBean.error("清理失败：" + e.getMessage());
        }
    }
}
