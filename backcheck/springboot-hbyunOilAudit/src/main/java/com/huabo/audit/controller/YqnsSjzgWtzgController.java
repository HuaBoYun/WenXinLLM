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
import com.huabo.audit.oracle.entity.TblYqnsIssueListEntity;
import com.huabo.audit.oracle.entity.TblYqnsSjzgWtzg;
import com.huabo.audit.service.TblYqnsSjzgWtzgService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author wangxilu
 * @description 审计整改_问题整改
 */
@Tag(name="审计整改_问题整改",description="审计整改_问题整改")
@RestController
@RequestMapping(value = "/wtzg")
public class YqnsSjzgWtzgController {

    @Resource
    TblYqnsSjzgWtzgService service; 

    @Operation(summary = "新增修改接口")
    @RequestMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @RequestBody TblYqnsSjzgWtzg vo
    ) {
        try {
            return this.service.saveOrUpdate(token, vo);
        } catch (Exception e) {
        	e.printStackTrace();
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
                         TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues
    ) {
        try {
            return this.service.list(token, pageNumber, pageSize, vo,issues);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "单个详情接口")
    @GetMapping("/detail")
    public JsonBean detail(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "wtzgid", description = "问题整改主键") @RequestParam(value = "wtzgid", required = true) BigDecimal wtzgid
    ) {
        try {
            return this.service.detail(token, wtzgid);
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "查看历史版本接口列表")
    @GetMapping("/getHistoryVersion")
    public JsonBean getHistoryVersion(HttpServletRequest request, 
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "issuesId", description = "问题清单主键") @RequestParam(value = "issuesId", required = true) BigDecimal issuesId,
                           @Parameter(name = "wtzgid", description = "问题整改主键 剔除") @RequestParam(value = "wtzgid", required = false) BigDecimal wtzgid
    ) {
        try {
            return this.service.getHistoryVersion(token, issuesId,wtzgid);
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "删除记录接口")
    @GetMapping("/delete")
    public JsonBean delete(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsSjzgWtzg vo
    ) {
        try {
            return this.service.delete(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "删除附件接口")
    @GetMapping("/deleteAttach")
    public JsonBean deleteAttach(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "attid", description = "附件ID") @RequestParam(value = "attid", required = true) String attid,
                           @Parameter(name = "wtzgid", description = "问题整改主键") @RequestParam(value = "wtzgid", required = false) BigDecimal wtzgid
    ) {
        try {
            return this.service.deleteAttach(token, attid,wtzgid);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "删除上传问题线索登记表及相关附件")
    @GetMapping("/deleteYsAttach")
    public JsonBean deleteYsAttach(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "attid", description = "附件ID") @RequestParam(value = "attid", required = true) String attid,
                           @Parameter(name = "ysnrid", description = "问题整改主键") @RequestParam(value = "ysnrid", required = false) BigDecimal ysnrid
    ) {
        try {
            return this.service.deleteYsAttach(token, attid,ysnrid);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    
    @Operation(summary = "整改台账汇总接口")
    @GetMapping("/rectificationLedger")
    public JsonBean rectificationLedger(HttpServletRequest request,
                         HttpServletResponse response,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                         @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                         @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                         TblYqnsSjzgWtzg vo,TblYqnsIssueListEntity issues
    ) {
        try {
            return this.service.rectificationLedger(token, pageNumber, pageSize, vo,issues);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "整改台账汇总接口")
    @GetMapping("/rectificationLedgerFillin")
    public JsonBean rectificationLedgerFillin(HttpServletRequest request,
                         HttpServletResponse response,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                         @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                         @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                         TblYqnsSjzgWtzg vo,TblYqnsIssueListEntity issues
    ) {
        try {
            return this.service.rectificationLedgerFillin(token, pageNumber, pageSize, vo,issues);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }



    @Operation(summary = "问题整改-审计成果统计")
    @GetMapping("/selectWtzgAuditResultsStatistics")
    public JsonBean selectWtzgAuditResultsStatistics(HttpServletRequest request,
                         HttpServletResponse response,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                               @Parameter(name = "queryYear", description = "queryYear", required = false) @RequestParam(value = "queryYear", required = false) Integer queryYear
    ) {
        try {
            return this.service.selectWtzgAuditResultsStatistics(token,queryYear);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


}

