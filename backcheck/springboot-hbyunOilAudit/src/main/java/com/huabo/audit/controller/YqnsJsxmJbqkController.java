package com.huabo.audit.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblYqnsJsxmJbqk;
import com.huabo.audit.service.TblYqnsJsxmJbqkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author wangys
 * @description 计划管理_建设项目基本情况表控制器
 * @createDate 2023-09-07 16:46:40
 */
@Tag(name="计划管理_建设项目基本情况表",description="计划管理_建设项目基本情况表")
@RestController
@RequestMapping(value = "/jsxmjbqk")
public class YqnsJsxmJbqkController {

    @Resource
    TblYqnsJsxmJbqkService service;

    @Operation(summary = "分类校验", description = "分类参数fl：三类|四类")
    @RequestMapping(value = "/flVerify", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean flVerify(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 TblYqnsJsxmJbqk vo
    ) {
        try {
            return this.service.flVerify(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "新增修改接口")
    @RequestMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                       TblYqnsJsxmJbqk vo
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
                         TblYqnsJsxmJbqk vo
    ) {  
        try {
            return this.service.list(token, pageNumber, pageSize, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "计划草稿列表查询接口") 
    @GetMapping("/getListDraftPlan")
    public JsonBean getListDraftPlan(HttpServletRequest request,
                                HttpServletResponse response,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                         TblYqnsJsxmJbqk vo
    ) {
        try {
            return this.service.getListDraftPlan(token, pageNumber, pageSize, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "单个详情接口", description = "jsxmjbqkid=?")
    @GetMapping("/detail")
    public JsonBean detail(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsJsxmJbqk vo
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
                           TblYqnsJsxmJbqk vo
    ) {
        try {
            return this.service.delete(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "导出数据接口", description = "ids=?,?")
    @GetMapping("/exportData")
    public JsonBean exportData(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               TblYqnsJsxmJbqk vo
    ) {
        try {
            return this.service.exportData(response, token, vo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "导入数据接口", description = "file=?.xlsx")
    @PostMapping("/importData")
    public JsonBean importData(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @Parameter(name = "file", description = "文件", required = true) @RequestParam("file") MultipartFile file
    ) {
        try {
            return this.service.importData(file, token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    
    
    @Operation(summary = "建设项目完成情况选择列表查询接口") 
    @GetMapping("/jswcgetlist")
    public JsonBean jswcgetlist(HttpServletRequest request,
                                HttpServletResponse response,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                         TblYqnsJsxmJbqk vo
    ) {  
        try {
            return this.service.jswclist(token, pageNumber, pageSize, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    


}

