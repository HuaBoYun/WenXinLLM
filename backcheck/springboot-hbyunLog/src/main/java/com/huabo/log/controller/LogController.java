package com.huabo.log.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.sdk.log.enums.OperationType;
import com.huabo.log.db.entity.UserLoginLog;
import com.huabo.log.db.entity.UserRequestLog;
import com.huabo.log.service.LogService;
import com.huabo.log.util.JsonBean;
import com.huabo.log.vo.LogPageReq;
import com.huabo.log.vo.OperationLog;

import cn.hutool.core.collection.CollUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author: 61
 */
@RestController
@Slf4j
@Tag(name="日志管理项目",description="日志管理项目")
@RequestMapping(value = "/log/bus")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;
    
  
    
 
    @PostMapping("/save")
    @Operation(summary = "日志保存接口")
    public JsonBean<?> saveBusLog(@RequestBody OperationLog operationLog) {
        logService.saveBusLog(operationLog);
        return JsonBean.success();
    }

    
    @com.hbfk.sdk.log.annotation.OperationLog(
            success = "日志台账接口",
            busType = "系统日志",
            fail = "日志台账接口",
            operationType = OperationType.SELECT,
            subType = "系统日志"
    )
    @PostMapping("/list")
    @Operation(summary = "日志台账接口")
    public JsonBean<Page<UserRequestLog>> list(@RequestBody LogPageReq req) throws Exception {
        return JsonBean.success(logService.list(req, false));
    }
    
    
    @com.hbfk.sdk.log.annotation.OperationLog(
            success = "异常日志详情",
            busType = "系统日志",
            fail = "异常日志详情",
            operationType = OperationType.SELECT,
            subType = "异常日志"
    )
    @GetMapping("/detail")
    @Operation(summary = "系统日志，异常日志详情接口")
    public JsonBean<UserRequestLog> detail(HttpServletRequest request,
    		@Parameter(name = "id", description = "主键", required = true) @RequestParam(value = "id", required = true) long id
    		) throws Exception {
        return JsonBean.success(logService.detail(id));
    }
    

    @com.hbfk.sdk.log.annotation.OperationLog(
            success = "导出接口",
            busType = "系统日志",
            fail = "导出接口",
            operationType = OperationType.EXPORT,
            subType = "系统日志"
    )
    @PostMapping("/export")
    @Operation(summary = "系统日志导出接口")
    public void export(HttpServletResponse response, @RequestBody LogPageReq req) throws Exception {
        logService.export(response, req);
    }
    
    
    @com.hbfk.sdk.log.annotation.OperationLog(
            success = "导出接口",
            busType = "系统日志",
            fail = "导出接口",
            operationType = OperationType.EXPORT,
            subType = "异常日志"
    )
    @PostMapping("/export/error")
    @Operation(summary = "异常日志导出接口")
    public void exportError(HttpServletResponse response, @RequestBody LogPageReq req) throws Exception {
        logService.exportError(response, req);
    }
    
    @com.hbfk.sdk.log.annotation.OperationLog(
            success = "导出接口",
            busType = "系统日志",
            fail = "导出接口",
            operationType = OperationType.EXPORT,
            subType = "登录日志"
    )
    @PostMapping("/list/login/export")
    @Operation(summary = "登录日志导出接口")
    public void listLoginExport(HttpServletResponse response, @RequestBody LogPageReq req) throws Exception {
    	logService.exportlistLogin(req,response);
    }
    
    @com.hbfk.sdk.log.annotation.OperationLog(
            success = "日志批量删除接口",
            busType = "系统日志",
            fail = "日志批量删除接口",
            operationType = OperationType.DELETE,
            subType = "系统日志"
    )
    @PostMapping("/batch/del")
    @Operation(summary = "日志批量删除接口")
    public JsonBean<?> delBatch(@RequestBody List<String> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            logService.delBatch(ids);
        }
        return JsonBean.success();
    }

    
    @com.hbfk.sdk.log.annotation.OperationLog(
            success = "日志异常台账接口",
            busType = "系统日志",
            fail = "日志异常台账接口",
            operationType = OperationType.SELECT,
            subType = "异常日志"
    )
    @PostMapping("/list/error")
    @Operation(summary = "日志异常台账接口")
    public JsonBean<Page<UserRequestLog>> listError(@RequestBody LogPageReq req) {
        return JsonBean.success(logService.listError(req));
    }
    
    
    @com.hbfk.sdk.log.annotation.OperationLog(
            success = "登陆日志台账接口",
            busType = "系统日志",
            fail = "登陆日志台账接口",
            operationType = OperationType.SELECT,
            subType = "登陆日志"
    )
    @PostMapping("/list/login")
    @Operation(summary = "登陆日志台账接口")
    public JsonBean<Page<UserLoginLog>> listLogin(@RequestBody LogPageReq req) {
        return JsonBean.success(logService.listLogin(req));
    }

    
    @com.hbfk.sdk.log.annotation.OperationLog(
            success = "用户登陆日志批量删除接口",
            busType = "系统日志",
            fail = "用户登陆日志批量删除接口",
            operationType = OperationType.DELETE,
            subType = "登陆日志"
    )
    @PostMapping("/login/batch/del")
    @Operation(summary = "用户登陆日志批量删除接口")
    public JsonBean<?> delLogBatch(@RequestBody List<String> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            logService.delLogBatch(ids);
        }
        return JsonBean.success();
    }
}
