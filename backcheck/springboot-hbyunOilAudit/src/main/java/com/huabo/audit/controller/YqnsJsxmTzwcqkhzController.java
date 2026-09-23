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
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqkhz;
import com.huabo.audit.service.TblYqnsJsxmTzwcqkhzService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author wangys
 * @description 计划管理_建设项目投资完成情况汇总控制器
 * @createDate 2023-09-07 16:46:40
 */
@Tag(name="计划管理_建设项目投资完成情况汇总",description="计划管理_建设项目投资完成情况汇总")
@RestController
@RequestMapping(value = "/jsxmtzwcqkhz")
public class YqnsJsxmTzwcqkhzController {

    @Resource
    TblYqnsJsxmTzwcqkhzService service;


    @Operation(summary = "新增修改接口")
    @RequestMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 TblYqnsJsxmTzwcqkhz vo
    ) {
        try {
            return this.service.saveOrUpdate(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


    @Operation(summary = "单个详情接口", description = "jsxmtzwcqkid=?")
    @GetMapping("/detail")
    public JsonBean detail(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsJsxmTzwcqkhz vo
    ) {
        try {
            return this.service.detail(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "删除记录接口", description = "ids=?,?")
    @GetMapping("/delete")
    public JsonBean delete(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsJsxmTzwcqkhz vo
    ) {
        try {
            return this.service.delete(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    
    @Operation(summary = "汇总列表查询接口")
    @GetMapping("/hz/list")
    public JsonBean hzlist(HttpServletRequest request,
                         HttpServletResponse response,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                         @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                         @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                         TblYqnsJsxmTzwcqkhz vo
    ) {
        try {
            return this.service.hzlist(token, pageNumber, pageSize, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
//
//    @Operation(summary = "建设项目投资完成情况汇总")
//    @GetMapping("/getJsxmtzhzList")
//    public JsonBean getJsxmtzList(HttpServletRequest request,HttpServletResponse response,
//                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
//                           @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
//                           @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
//    ) throws Exception {
//        try {
//            return service.getJsxmtzList(token,pageNumber,pageSize);
//        } catch (Exception e) {
//            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
//        }
//    }
    @Operation(summary = "建设项目投资完成情况汇总")
    @GetMapping("/getJsxmtzhzList")
    public JsonBean getJsxmtzhzList(HttpServletRequest request, HttpServletResponse response,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                    @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                    @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                    @Parameter(name = "year", description = "年度") @RequestParam(value = "year", required = false) String year
    ) throws Exception {
        try {
            return service.getJsxmjgjsList(token, pageNumber, pageSize,year);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


}

