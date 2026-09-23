package com.huabo.audit.controller;


import java.math.BigDecimal;

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
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsGcjsSettlement;
import com.huabo.audit.service.TblYqnsGcjsSettlementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsGcjsSettlementController
 * @PACKAGE_NAME:
 * @date 2024/07/11
 * @version: V1.0
 * @description: 央企内审-计划编制-工程结算审计项目汇总
 */
@RestController
@Slf4j
@Tag(name="工程结算审计项目汇总",description="工程结算审计项目汇总")
@RequestMapping(value = "/audit/settlement")
public class TblYqnsGcjsSettlementController {

    @Resource
    private TblYqnsGcjsSettlementService tblYqnsGcjsSettlementService;

    @Operation(summary = "工程结算审计项目汇总--获取工程结算审计项目汇总列表")
    @GetMapping(value = "getSettlementList", produces = "application/json;charset=utf-8")
    public JsonBean getSettlementList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "vo", description = "工程结算审计项目汇总entity") TblYqnsGcjsSettlement vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsGcjsSettlementService.getSettlementList(token,pageNumber, pageSize, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("工程结算审计项目汇总 -- 获取工程结算审计项目汇总列表接口异常", e);
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
                                 TblYqnsGcjsSettlement vo) {
        try {
            return tblYqnsGcjsSettlementService.saveOrUpdate(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


    @Operation(summary = "工程结算审计项目汇总-删除")
    @GetMapping(value = "delete", produces = "application/json;charset=utf-8")
    public JsonBean delete(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "-工程结算审计项目汇总-ID", required = true) @RequestParam(value = "id") BigDecimal id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsGcjsSettlementService.delete(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("工程结算审计项目汇总 -- 删除接口异常 ", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "导出数据接口", description = "ids=?,?")
    @GetMapping("/exportData")
    public JsonBean exportData(HttpServletRequest request,
                               HttpServletResponse response,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               TblYqnsGcjsSettlement vo
    ) {
        try {
            return this.tblYqnsGcjsSettlementService.exportData(response, token, vo);
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
            return this.tblYqnsGcjsSettlementService.importData(file, token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "工程结算审计项目汇总--同步数据")
    @GetMapping(value = "/syncConstructionProject", produces = "application/json;charset=utf-8")
    public JsonBean syncConstructionProject(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsGcjsSettlementService.syncConstructionProject(token);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("工程结算审计项目汇总 -- 获取工程结算审计项目汇总列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }
}
