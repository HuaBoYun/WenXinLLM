package com.huabo.audit.controller;

import java.math.BigDecimal;
import java.util.List;

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
import com.huabo.audit.oracle.entity.TblYqnsSjdd;
import com.huabo.audit.oracle.service.TblYqnsProjectAuditTemplateService;
import com.huabo.audit.oracle.service.TblYqnsSjddService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 督导任务
 */
@RestController
@Slf4j
@Tag(name="督导任务",description="督导任务")
@RequestMapping(value = "/sjdd")
public class TblYqnsDdrwController {

    @Resource
    private TblYqnsProjectAuditTemplateService tblYqnsProjectAuditTemplateService;
    
    @Resource
    private TblYqnsSjddService tblYqnsSjddService;
 
    
    
    @Operation(summary = "督导任务-工程项目获取模板内容")
    @GetMapping(value = "/ddtaskTemplateById", produces = "application/json;charset=utf-8")
    public JsonBean ddtaskTemplateById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "审计模板ID", required = true) @RequestParam(value = "id") Long id,
            @Parameter(name = "projectId", description = "项目ID", required = false) @RequestParam(value = "projectId", required = false) BigDecimal projectId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProjectAuditTemplateService.ddtaskTemplateById(token,id,projectId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) { 
            log.error("督导任务-获取模板内容子集所有信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage()); 
            jsonBean.setData(null);
        }
        return jsonBean;
    }
    
    
    @Operation(summary = "督导任务-督导内容--新增/更新 明细")
    @PostMapping(value = "/saveOrUpdateList", produces = "application/json;charset=utf-8")
    public JsonBean saveOrUpdateList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "vo", description = "督导任务-entityList") @RequestBody List<TblYqnsSjdd> list) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsSjddService.saveOrupdate(token, list);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("督导任务-督导内容--新增/更新 明细", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }



    @Operation(summary = "督导任务--删除（直接删除）")
    @GetMapping(value = "delete", produces = "application/json;charset=utf-8")
    public JsonBean delete(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "rwid", description = "-督导任务-督导内容ID", required = true) @RequestParam(value = "rwid") BigDecimal rwid) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsSjddService.deleteone(token, rwid);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("督导任务--删除（直接删除） ", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }
    

}
