package com.huabo.audit.controller;

import java.math.BigDecimal;

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
import com.huabo.audit.oracle.entity.TblYqnsIssueListEntity;
import com.huabo.audit.oracle.entity.TblYqnsSjzgWtzg;
import com.huabo.audit.service.TblYqnsSjzgWtzgService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author wangxilu
 * @description 审计整改_跟踪回访
 */
@Tag(name="审计整改_跟踪回访",description="审计整改_跟踪回访")
@RestController
@RequestMapping(value = "/gzhf")
public class YqnsSjzgGzhfController {

    @Resource
    TblYqnsSjzgWtzgService service;

    @Operation(summary = "列表查询接口")
    @GetMapping("/list")
    public JsonBean list(HttpServletRequest request,
                         HttpServletResponse response,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                         @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                         @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                         TblYqnsSjzgWtzg vo,TblYqnsIssueListEntity issues
    ) {
        try {
            return this.service.getGzhfList(token, pageNumber, pageSize, vo,issues);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "完成接口")
    @RequestMapping(value = "/complete", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean complete(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "wtzgid", description = "问题整改主键") @RequestParam(value = "wtzgid") BigDecimal wtzgid
    ) {
        try {
            return this.service.complete(token, wtzgid);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "分派接口")
    @RequestMapping(value = "/assignment", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean assignment(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "wtzgid", description = "问题整改主键", required = true) @RequestParam(value = "wtzgid",required = true) BigDecimal wtzgid,
    		@Parameter(name = "rectPerson", description = "整改人主键", required = true) @RequestParam(value = "rectPerson",required = true) BigDecimal rectPerson,
    		@Parameter(name = "rectPersonName", description = "整改人姓名", required = true) @RequestParam(value = "rectPersonName",required = true) String rectPersonName
    ) {
        try {
        	TblYqnsSjzgWtzg vo = new TblYqnsSjzgWtzg();
        	vo.setWtzgid(wtzgid);
            return this.service.assignment(token, vo,rectPerson,rectPersonName);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "撤回接口")
    @RequestMapping(value = "/withdraw", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean withdraw(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "wtzgid", description = "问题整改主键", required = true) @RequestParam(value = "wtzgid",required = true) BigDecimal wtzgid
    ) {
        try {
            return this.service.withdraw(token, wtzgid);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


    @Operation(summary = "审计要情附件关系保存接口")
    @RequestMapping(value = "/saveSjyqbcAtt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean saveSjyqbcAtt(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "wtzgid", description = "问题整改主键", required = true) @RequestParam(value = "wtzgid",required = true) BigDecimal wtzgid,
    		@Parameter(name = "attId", description = "附件主键", required = true) @RequestParam(value = "attId",required = true) BigDecimal attId
    ) {
        try {
            return this.service.saveSjyqbcAtt(token, wtzgid,attId);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "审计要情附件关系删除接口")
    @RequestMapping(value = "/removeSjyqbcAtt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean removeSjyqbcAtt(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "wtzgid", description = "问题整改主键", required = true) @RequestParam(value = "wtzgid",required = true) BigDecimal wtzgid,
    		@Parameter(name = "attId", description = "附件主键", required = true) @RequestParam(value = "attId",required = true) BigDecimal attId
    ) {
        try {
            return this.service.removeSjyqbcAtt(token, wtzgid,attId);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "整改统计获取汇总数据接口")
    @RequestMapping(value = "/getZgtjhzData", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean getZgtjhzData(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "wtzgid", description = "问题整改主键", required = true) @RequestParam(value = "wtzgid",required = true) BigDecimal wtzgid
    ) {
        try {
            return this.service.getZgtjhzData(token, wtzgid);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    
    @Operation(summary = "获取审计流程类型")
    @RequestMapping(value = "/getAuditFlowType", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean getAuditFlowType(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "wtzgid", description = "问题整改主键", required = true) @RequestParam(value = "wtzgid",required = true) BigDecimal wtzgid
    ) {
        try {
            return this.service.getAuditFlowType(token, wtzgid);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "保存问题整改关联三个报告")
    @RequestMapping(value = "/saveWtzgSjtzs", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean getAuditFlowType(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "wtzgid", description = "问题整改主键", required = true) @RequestParam(value = "wtzgid",required = true) BigDecimal wtzgid,
    		@Parameter(name = "formid", description = "审计通知书主键", required = true) @RequestParam(value = "formid",required = true) BigDecimal formid,
    		@Parameter(name = "optype", description = "1-审计通知书，2-审计意见及决定书，3-经济责任审计结果报告", required = true) @RequestParam(value = "optype",required = true) Integer optype
    ) {
        try {
            return this.service.saveWtzgSjtzs(token, wtzgid ,formid,optype);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "删除问题整改关联三个报告")
    @RequestMapping(value = "/removeWtzgSjtzs", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean removeWtzgSjtzs(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "wtzgid", description = "问题整改主键", required = true) @RequestParam(value = "wtzgid",required = true) BigDecimal wtzgid,
    		@Parameter(name = "formid", description = "审计通知书主键", required = true) @RequestParam(value = "formid",required = true) BigDecimal formid,
    		@Parameter(name = "optype", description = "1-审计通知书，2-审计意见及决定书，3-经济责任审计结果报告", required = true) @RequestParam(value = "optype",required = true) Integer optype
    ) {
        try {
            return this.service.removeWtzgSjtzs(token, wtzgid ,formid,optype);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "问题整改获取关联三个报告")
    @RequestMapping(value = "/getWtzgReportInfoList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    public JsonBean removeWtzgSjtzs(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "wtzgid", description = "问题整改主键", required = true) @RequestParam(value = "wtzgid",required = true) BigDecimal wtzgid
    ) {
        try {
            return this.service.getWtzgReportInfoList(token, wtzgid);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    
    
    
    
    
    
}

