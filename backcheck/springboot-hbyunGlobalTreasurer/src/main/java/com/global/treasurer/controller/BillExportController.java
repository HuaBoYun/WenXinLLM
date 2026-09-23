package com.global.treasurer.controller;

import com.global.treasurer.service.IBillExportService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 票据导出导入Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/bill")
@Api(tags = "票据导出导入")
public class BillExportController {
    private static final Logger log = LoggerFactory.getLogger(BillExportController.class);

    @Autowired
    private IBillExportService billExportService;

    @Resource
    private UserProvider userProvider;

    /**
     * 导出票据台账
     */
    @PostMapping("/export/ledger")
    @ApiOperation(value = "导出票据台账", notes = "导出票据台账Excel")
    public void exportLedger(Map<String, Object> params, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                response.setStatus(401);
                return;
            }
            billExportService.exportBillLedger(params, response);
        } catch (Exception e) {
            log.error("导出票据台账失败", e);
            response.setStatus(500);
        }
    }

    /**
     * 导出票据统计报表
     */
    @PostMapping("/export/statistics")
    @ApiOperation(value = "导出票据统计报表", notes = "导出票据统计报表Excel")
    public void exportStatistics(Map<String, Object> params, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                response.setStatus(401);
                return;
            }
            billExportService.exportBillStatistics(params, response);
        } catch (Exception e) {
            log.error("导出票据统计报表失败", e);
            response.setStatus(500);
        }
    }

    /**
     * 获取票据历史记录
     */
    @PostMapping("/history/{billId}")
    @ApiOperation(value = "获取票据历史记录", notes = "根据票据ID获取操作历史记录")
    public String getBillHistory(@PathVariable Long billId) {
        try {
            List<Map<String, Object>> historyList = billExportService.getBillHistory(billId);
            return JsonBean.success(historyList);
        } catch (Exception e) {
            log.error("获取票据历史记录失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 批量导入票据（Excel文件上传）
     */
    @PostMapping("/registration/batch-import-file")
    @ApiOperation(value = "批量导入票据", notes = "通过Excel批量导入票据")
    public String batchImport(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }
            if (file.isEmpty()) {
                return JsonBean.error("上传文件不能为空");
            }
            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".xls") && !filename.endsWith(".xlsx"))) {
                return JsonBean.error("只支持Excel文件格式(.xls/.xlsx)");
            }
            Map<String, Object> result = billExportService.batchImportBills(file, loginStaff);
            return JsonBean.success("导入完成", result);
        } catch (Exception e) {
            log.error("批量导入票据失败", e);
            return JsonBean.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 下载票据导入模板
     */
    @PostMapping("/registration/download-template")
    @ApiOperation(value = "下载票据导入模板", notes = "下载票据批量导入的Excel模板")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                response.setStatus(401);
                return;
            }
            billExportService.downloadImportTemplate(response);
        } catch (Exception e) {
            log.error("下载票据导入模板失败", e);
            response.setStatus(500);
        }
    }

    /**
     * 导出票据数据
     */
    @PostMapping("/export/data")
    @ApiOperation(value = "导出票据数据", notes = "根据条件导出票据数据Excel")
    public void exportData(Map<String, Object> params, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                response.setStatus(401);
                return;
            }
            billExportService.exportBillData(params, response);
        } catch (Exception e) {
            log.error("导出票据数据失败", e);
            response.setStatus(500);
        }
    }
}

