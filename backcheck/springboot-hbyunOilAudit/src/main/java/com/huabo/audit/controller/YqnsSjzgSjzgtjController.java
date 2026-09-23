package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblYqnsSjzgSjzgtj;
import com.huabo.audit.service.TblYqnsSjzgSjzgtjService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author wangxilu
 * @description 审计整改_审计整改统计信息
 */
@Tag(name="审计整改_审计整改统计信息",description="审计整改_审计整改统计信息")
@RestController
@RequestMapping(value = "/sjzgtj")
public class YqnsSjzgSjzgtjController {

    @Resource
    TblYqnsSjzgSjzgtjService service;

    @Operation(summary = "新增修改接口")
    @RequestMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @RequestBody TblYqnsSjzgSjzgtj vo
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
                         TblYqnsSjzgSjzgtj vo
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
                           TblYqnsSjzgSjzgtj vo
    ) {
        try {
            return this.service.detail(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    
    @Operation(summary = "获取统计信息数据")
    @GetMapping("/getStatisticsInfo")
    public JsonBean getStatisticsInfo(HttpServletRequest request,
           HttpServletResponse response,
           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
           @Parameter(name = "projectId", description = "所属项目主键") @RequestParam(value = "projectId", required = true) BigDecimal projectId,
           @Parameter(name = "dqzjjjcgtype", description = "当期直接经济成果类型") @RequestParam(value = "dqzjjjcgtype", required = false) String dqzjjjcgtype,
           @Parameter(name = "dqqtjjcgtype", description = "当期其他经济成果类型") @RequestParam(value = "dqqtjjcgtype", required = false) String dqqtjjcgtype
    ) {
        try {
            return this.service.getStatisticsInfo(token, projectId,dqzjjjcgtype,dqqtjjcgtype);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "删除记录接口")
    @GetMapping("/delete")
    public JsonBean delete(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsSjzgSjzgtj vo
    ) {
        try {
            return this.service.delete(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "删除整改统计附件接口")    @GetMapping("/deleteZgtjAttach")
    public JsonBean deleteZgtjAttach(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @Parameter(name = "attid", description = "附件ID") @RequestParam(value = "attid", required = true) String attid,
                                 @Parameter(name = "sjzgtjid", description = "统计信息主键") @RequestParam(value = "sjzgtjid", required = false) BigDecimal sjzgtjid
    ) {
        try {
            return this.service.deleteZgtjAttach(token, attid, sjzgtjid);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "删除整改情况说明附件接口")
    @GetMapping("/deleteZgqksmAttach")
    public JsonBean deleteZgqksmAttach(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "attid", description = "附件ID") @RequestParam(value = "attid", required = false) String attid,
                                     @Parameter(name = "gzzdqksmid", description = "整改情况说明ID") @RequestParam(value = "gzzdqksmid", required = true) BigDecimal gzzdqksmid
    ) {
        try {
            return this.service.deleteQksmAttach(token, attid, gzzdqksmid);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

}

