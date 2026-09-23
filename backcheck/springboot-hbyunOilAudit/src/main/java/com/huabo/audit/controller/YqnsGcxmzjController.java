package com.huabo.audit.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzj;
import com.huabo.audit.service.TblYqnsGcxmzjService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author wangys
 * @description 计划管理_工程项目造价控制器
 * @createDate 2023-09-07 16:46:40
 */
@Tag(name="计划管理_工程项目造价",description="计划管理_工程项目造价")
@RestController
@RequestMapping(value = "/gcxmzj")
public class YqnsGcxmzjController {

    @Resource
    TblYqnsGcxmzjService beanService;

    @Operation(summary = "新增修改接口")
    @RequestMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                       @Parameter(name = "bean", description = "实体", required = true)@RequestBody TblYqnsGcxmzj vo
    ) {
        try {
            return this.beanService.saveOrUpdate(token, vo);
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
                                @Parameter(name = "order", description = "序号排序规则，1-倒序  ，0-升序，默认1") @RequestParam(value = "order", required = false, defaultValue = "1") Integer order,
                                TblYqnsGcxmzj vo
    ) {
        try {
            return this.beanService.list(token, pageNumber, pageSize, vo, order);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "列表查询接口")
    @GetMapping("/getChoiceList")
    public JsonBean getChoiceList(HttpServletRequest request,
                                HttpServletResponse response,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                TblYqnsGcxmzj vo
    ) {
        try {
            return this.beanService.getChoiceList(token, pageNumber, pageSize, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "单个详情接口", description = "gcxmzjid=?")
    @GetMapping("/detail")
    public JsonBean detail(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 TblYqnsGcxmzj vo
    ) {
        try {
            return this.beanService.detail(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "删除记录接口", description = "ids=?,?")
    @GetMapping("/delete")
    public JsonBean delete(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 TblYqnsGcxmzj vo
    ) {
        try {
            return this.beanService.delete(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "导出数据接口", description = "ids=?,?")
    @GetMapping("/exportData")
    public JsonBean exportData(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 TblYqnsGcxmzj vo
    ) {
        try {
            return this.beanService.exportData(response, token, vo);
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
            return this.beanService.importData(file, token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


}

