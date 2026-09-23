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
import com.huabo.audit.oracle.entity.TblYqnsTemplateManagerEntity;
import com.huabo.audit.oracle.service.TblYqnsTemplateManagerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsTemplateManagerController
 * @PACKAGE_NAME: com.huabo.audit.controller
 * @date 2023/10/10 14:00.
 * @version: V1.0
 * @description:  央企内审-基础配置-模板管理
 */
@RestController
@Slf4j
@Tag(name="模板管理",description="模板管理")
@RequestMapping(value = "/audit/TemplateManager")
public class TblYqnsTemplateManagerController {

    @Resource
    private TblYqnsTemplateManagerService TblYqnsTemplateManagerService;


    @Operation(summary = "模板管理--获取模板列表")
    @GetMapping(value = "getRecordsList", produces = "application/json;charset=utf-8")
    public JsonBean getAuditProposeList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "vo", description = "模板管理entity") TblYqnsTemplateManagerEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = TblYqnsTemplateManagerService.getTemplateList(token,pageNumber, pageSize, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("模板管理 -- 获取模板管理列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "模板管理--获取模板管理单个详情信息")
    @GetMapping(value = "getTemplateById", produces = "application/json;charset=utf-8")
    public JsonBean getRecordsById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "模板ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = TblYqnsTemplateManagerService.getTemplateById(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("模板管理 -- 获取模板管理单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "模板管理--新增/更新")
    @PostMapping(value = "saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean saveOrUpdate(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "vo", description = "模板管理entity") TblYqnsTemplateManagerEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = TblYqnsTemplateManagerService.saveOrUpdate(token,vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("基础配置-模板管理 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }



    @Operation(summary = "模板管理--删除（直接删除）")
    @GetMapping(value = "delete", produces = "application/json;charset=utf-8")
    public JsonBean delete(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "模板ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = TblYqnsTemplateManagerService.delete(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("模板管理 -- 获取模板单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }



    @Operation(summary = "模板管理 附件-删除（直接删除）")
    @GetMapping(value = "deleteFileAttach", produces = "application/json;charset=utf-8")
    public JsonBean deleteFileAttach(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "attId", description = "附件ID", required = true) @RequestParam(value = "attId") String attId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = TblYqnsTemplateManagerService.deleteFileAttach(token,attId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("模板管理 - 附件 -- 删除接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


}
