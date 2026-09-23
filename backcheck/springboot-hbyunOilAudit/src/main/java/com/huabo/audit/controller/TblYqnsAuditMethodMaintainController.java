package com.huabo.audit.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsAuditMethodMaintainEntity;
import com.huabo.audit.oracle.service.TblYqnsAuditMethodMaintainService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditOverseeRecordsController
 * @PACKAGE_NAME: com.huabo.audit.controller
 * @date 2023/10/10 14:00.
 * @version: V1.0
 * @description: 央企内审-基础配置-审计方法维护
 */
@RestController
@Slf4j
@Tag(name="审计方法维护",description="审计方法维护")
@RequestMapping(value = "/audit/methodMaintain")
public class TblYqnsAuditMethodMaintainController {

    @Resource
    private TblYqnsAuditMethodMaintainService tblYqnsAuditMethodMaintainService;


    @Operation(summary = "审计方法维护--获取方法维护列表-分页")
    @GetMapping(value = "getMethodMaintainPage", produces = "application/json;charset=utf-8")
    public JsonBean getMethodMaintainPage(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "vo", description = "审计方法维护entity") TblYqnsAuditMethodMaintainEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMethodMaintainService.getMethodMaintainPage(token, pageNumber, pageSize, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计方法维护 -- 获取列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "审计方法维护--获取方法维护列表")
    @GetMapping(value = "getMethodMaintainList", produces = "application/json;charset=utf-8")
    public JsonBean getMethodMaintainList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "vo", description = "审计方法维护entity") TblYqnsAuditMethodMaintainEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMethodMaintainService.getMethodMaintainList(token, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计方法维护 -- 获取列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "审计方法维护--获取方法维护-单个详情信息")
    @GetMapping(value = "getMethodMaintainById", produces = "application/json;charset=utf-8")
    public JsonBean getRecordsById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "id", description = "方法维护-ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMethodMaintainService.getMethodMaintainById(token, id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计方法维护 -- 获取方法维护-单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "审计方法维护--新增/更新")
    @PostMapping(value = "saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean increaseOrUpdateAuditProject(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "vo", description = "审计方法维护entity") TblYqnsAuditMethodMaintainEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMethodMaintainService.saveOrUpdate(token, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("基础配置-审计方法维护 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }


    @Operation(summary = "审计方法维护--删除（直接删除）")
    @GetMapping(value = "delete", produces = "application/json;charset=utf-8")
    public JsonBean delete(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "id", description = "方法维护ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMethodMaintainService.delete(token, id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计方法维护 -- 删除审计方法维护单个信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }
}
