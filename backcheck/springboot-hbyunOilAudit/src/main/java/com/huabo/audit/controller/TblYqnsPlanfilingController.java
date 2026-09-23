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
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsPlanfiling;
import com.huabo.audit.service.TblYqnsPlanfilingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author smf
 * @CLASS_NAME: TblYqnsPlanfilingController
 * @PACKAGE_NAME:
 * @date 2024/07/14
 * @version: V1.0
 * @description: 央企内审-计划编制-计划备案
 */
@RestController
@Slf4j
@Tag(name="计划备案",description="计划备案")
@RequestMapping(value = "/audit/plan")
public class TblYqnsPlanfilingController {

    @Resource
    private TblYqnsPlanfilingService tblYqnsPlanfilingService;

    @Operation(summary = "计划备案--计划备案列表")
    @GetMapping(value = "getPlanFilingList", produces = "application/json;charset=utf-8")
    public JsonBean getSettlementList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "vo", description = "计划备案entity") TblYqnsPlanfiling vo) {
        JsonBean jsonBean = new JsonBean();
         try {
            jsonBean = tblYqnsPlanfilingService.getPlanFilingList(token,pageNumber, pageSize, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("计划备案 -- 计划备案列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "新增修改接口")
    @RequestMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean saveOrUpdate(HttpServletRequest request, HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 TblYqnsPlanfiling vo) {
        try {
            return tblYqnsPlanfilingService.saveOrUpdate(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "计划备案-删除")
    @GetMapping(value = "delete", produces = "application/json;charset=utf-8")
    public JsonBean delete(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "-计划备案-ID", required = true) @RequestParam(value = "id") BigDecimal id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsPlanfilingService.delete(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("计划备案 -- 删除接口异常 ", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "单个详情接口", description = "gcxmzjzjbid=?")
    @GetMapping("/detail")
    public JsonBean detail(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsPlanfiling vo
    ) {
        try {
            return this.tblYqnsPlanfilingService.detail(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

}
