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
import com.huabo.audit.oracle.entity.TblYqnsOtherAudit;
import com.huabo.audit.oracle.service.TblYqnsOtherAuditService;
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
@Tag(name="其他审计接口",description="其他审计接口")
@RequestMapping(value = "/other/audit")
public class TblYqnsOtherAuditController {

	@Resource
    private TblYqnsOtherAuditService tblYqnsOtherAuditService;
	
    @Resource
    private TblYqnsProjectAuditTemplateService tblYqnsProjectAuditTemplateService;
    
    @Operation(summary = "其他审计通过计划Id获取关联选择的数据")
    @GetMapping(value = "/getListForChoose", produces = "application/json;charset=utf-8")
    public JsonBean getListForChoose(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "draftPlanId", description = "计划主键", required = true) @RequestParam(value = "draftPlanId") BigDecimal draftPlanId,
            @Parameter(name = "chooseType", description = "选择来源  1-计划初稿 ，2-计划终稿", required = true) @RequestParam(value = "draftPlanId") Integer chooseType,
            @Parameter(name = "auditIdStrs", description = "当前页面已存在的审计主键 , 逗号拼接", required = false) @RequestParam(value = "auditIdStrs",required = false) String auditIdStrs) throws Exception {
        JsonBean jsonBean = new JsonBean();
            jsonBean = tblYqnsOtherAuditService.getListForChoose(token,draftPlanId,chooseType,auditIdStrs);
        return jsonBean;
    }
    
    @Operation(summary = "其他审计新增获取基础数据接口")
    @GetMapping(value = "toPreAdd", produces = "application/json;charset=utf-8")
    public JsonBean toPreAdd(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) throws Exception {
        JsonBean jsonBean = new JsonBean();
            jsonBean = tblYqnsOtherAuditService.toPreAdd(token);
        return jsonBean;
    }
    
    @Operation(summary = "其他审计新增修改保存接口")
    @PostMapping(value = "/mengerEntity", produces = "application/json;charset=utf-8")
    public JsonBean mengerEntity(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "vo", description = "工程审计模板entity") TblYqnsOtherAudit vo) throws Exception {
        JsonBean jsonBean = new JsonBean();
            jsonBean = tblYqnsOtherAuditService.mengerEntity(token,vo);
        return jsonBean;
    }
    
    @Operation(summary = "其他审计详情接口")
    @GetMapping(value = "getDetailById", produces = "application/json;charset=utf-8")
    public JsonBean getTemplateById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "auditId", description = "其他审计主键", required = true) @RequestParam(value = "auditId") BigDecimal auditId) throws Exception {
        JsonBean jsonBean = new JsonBean();
            jsonBean = tblYqnsOtherAuditService.getDetailById(token,auditId);
        return jsonBean;
    }
    
    
    @Operation(summary = "其他审计删除接口")
    @GetMapping(value = "removById", produces = "application/json;charset=utf-8")
    public JsonBean removById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "auditId", description = "其他审计主键", required = true) @RequestParam(value = "auditId") BigDecimal auditId) throws Exception {
        JsonBean jsonBean = new JsonBean();
            jsonBean = tblYqnsOtherAuditService.removById(token,auditId);
        return jsonBean;
    }

    @Operation(summary = "其他审计-附件删除")
    @GetMapping(value = "removeAttInfo", produces = "application/json;charset=utf-8")
    public JsonBean removeAttInfo(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "attId", description = "附件主键", required = true) @RequestParam(value = "attId") BigDecimal attId) throws Exception {
        JsonBean jsonBean = new JsonBean();
            jsonBean = tblYqnsOtherAuditService.removeAttInfo(token,attId);
        return jsonBean;
    }
}
