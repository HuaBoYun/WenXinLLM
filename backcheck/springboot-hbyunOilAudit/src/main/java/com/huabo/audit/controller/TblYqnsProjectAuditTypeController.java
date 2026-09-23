package com.huabo.audit.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTypeEntity;
import com.huabo.audit.oracle.service.TblYqnsProjectAuditTypeNameService;
import com.huabo.audit.oracle.service.TblYqnsProjectAuditTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProjectAuditTypeController
 * @PACKAGE_NAME: com.huabo.audit.controller
 * @date 2023/10/18 14:00.
 * @version: V1.0
 * @description: 央企内审-基础配置-工程审计类型
 */
@RestController
@Slf4j
@Tag(name="工程审计类型",description="工程审计类型")
@RequestMapping(value = "/project/auditType")
public class TblYqnsProjectAuditTypeController {

    @Resource
    private TblYqnsProjectAuditTypeService tblYqnsProjectAuditTypeService;
    @Resource
    private TblYqnsProjectAuditTypeNameService tblYqnsProjectAuditTypeNameService;


    @Operation(summary = "工程审计类型--获取方法维护列表分页 Status=0 ")
    @GetMapping(value = "getAuditTypePage", produces = "application/json;charset=utf-8")
    public JsonBean getAuditProposeList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "vo", description = "工程审计类型entity") TblYqnsProjectAuditTypeEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTypeService.getAuditTypePage(token, pageNumber, pageSize, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("工程审计类型 -- 获取列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "工程审计类型--获取方法维护列表 status=0")
    @GetMapping(value = "getAuditTypeList", produces = "application/json;charset=utf-8")
    public JsonBean getAuditProposeList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "vo", description = "工程审计类型entity") TblYqnsProjectAuditTypeEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTypeService.getAuditTypeList(token, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("工程审计类型 -- 获取列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "工程审计类型--获取审计类型单个详情信息")
    @GetMapping(value = "getAuditTypeById", produces = "application/json;charset=utf-8")
    public JsonBean getAuditTypeById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "id", description = "方法维护-ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTypeService.getAuditTypeById(token, id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) { 
            log.error("工程审计类型 -- 获取方法维护-单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "工程审计类型--获取审计类型多个详情信息")
    @GetMapping(value = "getAuditTypeByIds", produces = "application/json;charset=utf-8")
    public JsonBean getAuditTypeByIds(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "ids", description = "方法维护-IDs", required = true) @RequestParam(value = "ids") String ids) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTypeService.getAuditTypeByIds(token, ids);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("工程审计类型 -- 获取方法维护-ids详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "工程审计类型--新增/更新")
    @PostMapping(value = "saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean saveOrUpdate(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "vo", description = "工程审计类型entity") @RequestBody TblYqnsProjectAuditTypeEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTypeService.saveOrUpdate(token, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("基础配置-工程审计类型 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }


    @Operation(summary = "工程审计类型--删除（直接删除）")
    @GetMapping(value = "delete", produces = "application/json;charset=utf-8")
    public JsonBean delete(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "id", description = "方法维护ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTypeService.delete(token, id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("工程审计类型 -- 删除工程审计类型单个信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "工程审计类型-Name--删除（直接删除）")
    @GetMapping(value = "deleteNameById", produces = "application/json;charset=utf-8")
    public JsonBean deleteNameById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "id", description = "方法维护-NameID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTypeNameService.delete(token, id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("工程审计类型-Name -- 删除工程审计类型-Name接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }
}
