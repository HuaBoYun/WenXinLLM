package com.management.accountant.controller;

import com.management.accountant.oracle.service.common.ColumnSettingsService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 列设置Controller
 * 
 * @description 提供统一的列设置接口
 * @author AI Assistant
 * @date 2026-02-06
 */
@Slf4j
@RestController
@RequestMapping("/accountant/common")
@Api(tags = "通用功能-列设置")
public class ColumnSettingsController {

    @Resource
    private ColumnSettingsService columnSettingsService;

    /**
     * 获取列设置
     */
    @Operation(summary = "获取列设置")
    @ApiOperation("获取列设置")
    @GetMapping("/column-settings")
    public MyJsonBean<Map<String, Object>> getColumnSettings(
            @ApiParam("页面编码") @RequestParam String pageCode,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String userId = request.getHeader("userId");
            String companyId = request.getHeader("companyId");
            
            Map<String, Object> settings = columnSettingsService.getColumnSettings(
                    pageCode, userId, companyId);
            
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(settings);
        } catch (Exception e) {
            log.error("获取列设置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 保存列设置
     */
    @Operation(summary = "保存列设置")
    @ApiOperation("保存列设置")
    @PostMapping("/column-settings")
    public MyJsonBean<Boolean> saveColumnSettings(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            String userId = request.getHeader("userId");
            String companyId = request.getHeader("companyId");
            
            String pageCode = (String) params.get("pageCode");
            @SuppressWarnings("unchecked")
            Map<String, Object> settings = (Map<String, Object>) params.get("settings");
            
            boolean success = columnSettingsService.saveColumnSettings(
                    pageCode, settings, userId, companyId);
            
            result.setCode(1);
            result.setMsg("保存成功");
            result.setData(success);
        } catch (Exception e) {
            log.error("保存列设置异常", e);
            result.setCode(0);
            result.setMsg("保存失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 重置列设置
     */
    @Operation(summary = "重置列设置")
    @ApiOperation("重置列设置")
    @DeleteMapping("/column-settings")
    public MyJsonBean<Boolean> resetColumnSettings(
            @ApiParam("页面编码") @RequestParam String pageCode,
            HttpServletRequest request) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            String userId = request.getHeader("userId");
            String companyId = request.getHeader("companyId");
            
            boolean success = columnSettingsService.resetColumnSettings(
                    pageCode, userId, companyId);
            
            result.setCode(1);
            result.setMsg("重置成功");
            result.setData(success);
        } catch (Exception e) {
            log.error("重置列设置异常", e);
            result.setCode(0);
            result.setMsg("重置失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取默认列设置
     */
    @Operation(summary = "获取默认列设置")
    @ApiOperation("获取默认列设置")
    @GetMapping("/column-settings/default")
    public MyJsonBean<Map<String, Object>> getDefaultColumnSettings(
            @ApiParam("页面编码") @RequestParam String pageCode) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> settings = columnSettingsService.getDefaultColumnSettings(pageCode);
            
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(settings);
        } catch (Exception e) {
            log.error("获取默认列设置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量更新列设置
     */
    @Operation(summary = "批量更新列设置")
    @ApiOperation("批量更新列设置")
    @PostMapping("/column-settings/batch")
    public MyJsonBean<Boolean> batchUpdateColumnSettings(
            @RequestBody Map<String, Object> params,
            HttpServletRequest request) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            String userId = request.getHeader("userId");
            String companyId = request.getHeader("companyId");
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> settingsList = 
                    (List<Map<String, Object>>) params.get("settingsList");
            
            boolean success = columnSettingsService.batchUpdateColumnSettings(
                    settingsList, userId, companyId);
            
            result.setCode(1);
            result.setMsg("批量更新成功");
            result.setData(success);
        } catch (Exception e) {
            log.error("批量更新列设置异常", e);
            result.setCode(0);
            result.setMsg("批量更新失败：" + e.getMessage());
        }
        return result;
    }
}

