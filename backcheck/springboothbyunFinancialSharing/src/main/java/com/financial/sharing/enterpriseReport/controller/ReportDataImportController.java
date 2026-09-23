package com.financial.sharing.enterpriseReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.enterpriseReport.dto.ReportDataImportResult;
import com.financial.sharing.enterpriseReport.service.ReportDataImportService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 报表数据导入导出Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "企业报表-报表数据导入导出")
@RestController
@RequestMapping("/enterpriseReport/reportDataImport")
public class ReportDataImportController {

    @Autowired
    private ReportDataImportService reportDataImportService;

    /**
     * 下载导入模板
     */
    @ApiOperation("下载导入模板")
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) {
        if (UserUtils.getUser() == null) {
            throw new RuntimeException("用户未登录");
        }
        
        try {
            reportDataImportService.downloadTemplate(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("下载模板失败: " + e.getMessage());
        }
    }

    /**
     * 导入Excel数据
     */
    @ApiOperation("导入Excel数据")
    @PostMapping("/importExcel")
    public MyJsonBean importExcel(@RequestParam("file") MultipartFile file) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            if (file == null || file.isEmpty()) {
                return MyJsonBean.errorData("请选择要导入的文件");
            }
            
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.endsWith(".xlsx")) {
                return MyJsonBean.errorData("只支持.xlsx格式的Excel文件");
            }
            
            ReportDataImportResult result = reportDataImportService.importExcel(file);
            
            if (result.hasError()) {
                return MyJsonBean.errorData("导入完成，但存在错误。成功: " + result.getSuccessRows() + 
                    " 行，失败: " + result.getFailRows() + " 行", result);
            } else {
                return MyJsonBean.ok("导入成功，共导入 " + result.getSuccessRows() + " 行数据", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("导入失败: " + e.getMessage());
        }
    }

    /**
     * 导出数据为Excel
     */
    @ApiOperation("导出数据为Excel")
    @PostMapping("/exportExcel")
    public void exportExcel(@RequestBody Map<String, String> params, HttpServletResponse response) {
        if (UserUtils.getUser() == null) {
            throw new RuntimeException("用户未登录");
        }
        
        try {
            reportDataImportService.exportExcel(params, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }
}

