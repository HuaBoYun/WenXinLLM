package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTemplateEntity;
import com.huabo.audit.oracle.service.TblYqnsProjectAuditTemplateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProjectAuditTemplateController
 * @PACKAGE_NAME: com.huabo.audit.controller
 * @date 2023/10/10 14:00.
 * @version: V1.0
 * @description: 央企内审-基础配置-工程审计模板
 */
@RestController
@Slf4j
@Tag(name="工程审计模板",description="工程审计模板")
@RequestMapping(value = "/project/auditTemplate")
public class TblYqnsProjectAuditTemplateController {

    @Resource
    private TblYqnsProjectAuditTemplateService tblYqnsProjectAuditTemplateService;


    @Operation(summary = "工程审计模板--获取审计模板列表 分页")
    @GetMapping(value = "getAuditTemplatePage", produces = "application/json;charset=utf-8")
    public JsonBean getAuditTemplatePage(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "vo", description = "工程审计模板entity") TblYqnsProjectAuditTemplateEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTemplateService.getAuditTemplatePage(token,pageNumber, pageSize, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计建议 -- 获取建议列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "工程审计模板--获取审计模板列表明细")
    @GetMapping(value = "getTemplateList", produces = "application/json;charset=utf-8")
    public JsonBean getAuditTemplateList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "vo", description = "工程审计模板entity") TblYqnsProjectAuditTemplateEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTemplateService.getAuditTemplateList(token,vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计建议 -- 获取建议列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "工程审计模板--获取审计模板单个详情信息")
    @GetMapping(value = "getTemplateById", produces = "application/json;charset=utf-8")
    public JsonBean getTemplateById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "审计模板ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTemplateService.getAuditTemplateById(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("工程审计模板 -- 获取审计模板单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "工程审计模板--新增/更新 [增加复制数据，和删除复制数据]")
    @PostMapping(value = "saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean increaseOrUpdateAuditProject(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "vo", description = "工程审计模板entity") TblYqnsProjectAuditTemplateEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTemplateService.saveOrUpdate(token,vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---审计项目情况表 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }

    @Operation(summary = "工程审计模板--[复制数据]")
    @PostMapping(value = "cpTemplateByTemplateId", produces = "application/json;charset=utf-8")
    public JsonBean cpTemplateByTemplateId(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "templateId", description = "审计模板ID", required = true) @RequestParam(value = "templateId") Long templateId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTemplateService.cpTemplateByTemplateId(token,String.valueOf(templateId));
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---审计项目情况表 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }



    @Operation(summary = "工程审计模板--删除（直接删除） 未删除复制数据")
    @GetMapping(value = "delete", produces = "application/json;charset=utf-8")
    public JsonBean delete(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "审计模板ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTemplateService.delete(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("工程审计模板 -- 获取审计模板删除单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "工程审计模板--预览审计模板子集所有信息 - 获取复制数据")
    @GetMapping(value = "previewTemplateById", produces = "application/json;charset=utf-8")
    public JsonBean previewTemplateById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "审计模板ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTemplateService.previewTemplateById(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("工程审计模板 -- 预览审计模板子集所有信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    
    
    
    @Operation(summary = "我的任务-获取模板内容")
    @GetMapping(value = "mytaskTemplateById", produces = "application/json;charset=utf-8")
    public JsonBean mytaskTemplateById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "审计模板ID", required = true) @RequestParam(value = "id") Long id,
            @Parameter(name = "templateId", description = "审计模板ID", required = false) @RequestParam(value = "templateId", required = false) Long templateId,
            @Parameter(name = "projectId", description = "审计模板ID", required = false) @RequestParam(value = "projectId", required = false) BigDecimal projectId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTemplateService.mytaskTemplateById(token,id,templateId,projectId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("工程审计模板 -- 预览审计模板子集所有信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }
    
    

}
