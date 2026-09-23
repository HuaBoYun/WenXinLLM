package com.huabo.audit.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblYqnsSjbgSjgzjl;
import com.huabo.audit.service.TblYqnsSjbgSjgzjlService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @description 审计报告_审计工作记录
 */
@Tag(name="审计报告_审计工作记录",description="审计报告_审计工作记录")
@RestController
@RequestMapping(value = "/sjgzjl")
public class YqnsSjbgSjgzjlController {

    @Resource
    TblYqnsSjbgSjgzjlService service;

    @Operation(summary = "新增修改接口")
    @RequestMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 TblYqnsSjbgSjgzjl vo
    ) {
        try {
            return this.service.saveOrUpdate(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


    @Operation(summary = "列表查询接口")
    @GetMapping("/list")
    public JsonBean list(HttpServletRequest request,
                         HttpServletResponse response,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                         @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                         @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                         TblYqnsSjbgSjgzjl vo
    ) {
        try {
            return this.service.list(token, pageNumber, pageSize, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "单个详情接口")
    @GetMapping("/detail")
    public JsonBean detail(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsSjbgSjgzjl vo
    ) {
        try {
            return this.service.detail(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "删除记录接口")
    @GetMapping("/delete")
    public JsonBean delete(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsSjbgSjgzjl vo
    ) {
        try {
            return this.service.delete(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

}

