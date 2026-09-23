package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetIntegrationFileService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算文件集成Controller
 * 
 * @description 预算文件集成接口，支持文件上传、下载、解析、转换等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-文件集成"})
@RequestMapping(value = "/accountant/budget/integration/file")
@Slf4j
public class BudgetIntegrationFileController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetIntegrationFileService integrationFileService;

    /**
     * 文件上传
     */
    @Operation(summary = "文件上传")
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public MyJsonBean<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file,
                                                       @RequestParam(required = false) String fileType) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> uploadResult = integrationFileService.uploadFile(file, fileType);
            result.setCode(1);
            result.setMsg("上传成功");
            result.setData(uploadResult);
        } catch (ServiceException ex) {
            log.error("文件上传失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("文件上传异常", e);
            result.setCode(0);
            result.setMsg("上传失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 文件下载
     */
    @Operation(summary = "文件下载")
    @ApiOperation("文件下载")
    @PostMapping("/download")
    public MyJsonBean<Map<String, Object>> downloadFile(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> downloadResult = integrationFileService.downloadFile(params);
            result.setCode(1);
            result.setMsg("下载成功");
            result.setData(downloadResult);
        } catch (ServiceException ex) {
            log.error("文件下载失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("文件下载异常", e);
            result.setCode(0);
            result.setMsg("下载失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 文件解析
     */
    @Operation(summary = "文件解析")
    @ApiOperation("文件解析")
    @PostMapping("/parse")
    public MyJsonBean<Map<String, Object>> parseFile(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> parseResult = integrationFileService.parseFile(params);
            result.setCode(1);
            result.setMsg("解析成功");
            result.setData(parseResult);
        } catch (ServiceException ex) {
            log.error("文件解析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("文件解析异常", e);
            result.setCode(0);
            result.setMsg("解析失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 文件转换
     */
    @Operation(summary = "文件转换")
    @ApiOperation("文件转换")
    @PostMapping("/convert")
    public MyJsonBean<Map<String, Object>> convertFile(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> convertResult = integrationFileService.convertFile(params);
            result.setCode(1);
            result.setMsg("转换成功");
            result.setData(convertResult);
        } catch (ServiceException ex) {
            log.error("文件转换失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("文件转换异常", e);
            result.setCode(0);
            result.setMsg("转换失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量导入
     */
    @Operation(summary = "批量导入")
    @ApiOperation("批量导入")
    @PostMapping("/import/batch")
    public MyJsonBean<Map<String, Object>> batchImport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> importResult = integrationFileService.batchImport(params);
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (ServiceException ex) {
            log.error("批量导入失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量导出
     */
    @Operation(summary = "批量导出")
    @ApiOperation("批量导出")
    @PostMapping("/export/batch")
    public MyJsonBean<Map<String, Object>> batchExport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportResult = integrationFileService.batchExport(params);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportResult);
        } catch (ServiceException ex) {
            log.error("批量导出失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量导出异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 文件模板管理
     */
    @Operation(summary = "文件模板管理")
    @ApiOperation("文件模板管理")
    @PostMapping("/template/manage")
    public MyJsonBean<Map<String, Object>> manageTemplate(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> template = integrationFileService.manageTemplate(params);
            result.setCode(1);
            result.setMsg("操作成功");
            result.setData(template);
        } catch (ServiceException ex) {
            log.error("文件模板管理失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("文件模板管理异常", e);
            result.setCode(0);
            result.setMsg("操作失败：" + e.getMessage());
        }
        return result;
    }
}

