package com.huabo.audit.controller;


import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.dto.TblYqnsAuditProjectDto;
import com.huabo.audit.oracle.dto.TblYqnsSiteReviewContentDto;
import com.huabo.audit.oracle.service.AuditProjectService;
import com.huabo.audit.oracle.service.SiteReviewContentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ExecPhraseController
 * @Description 审计实施
 * @Author yan
 * @Date 2022/4/12 16:27
 * @Version 1.0
 */
@RestController
@Slf4j
@Tag(name="审计实施",description="审计实施")
@RequestMapping(value = "/audit/execPharse")
public class ExecPhraseController {

    @Autowired
    private AuditProjectService auditProjectService;
    @Autowired
    private SiteReviewContentService siteReviewContentService;


    @Operation(summary = "审计实施---审计项目情况表---查询审计项目情况列表")
    @GetMapping(value = "project/allAuditProject", produces = "application/json;charset=utf-8")
    public JsonBean getAuditProjectList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "name", description = "项目名称") @RequestParam(value = "name", defaultValue = "") String name,
            @Parameter(name = "auditUnitId", description = "被审计单位") @RequestParam(value = "auditUnitId", defaultValue = "") String auditUnitId,
            @Parameter(name = "sceneApproveStaerTime", description = "开始时间") @RequestParam(value = "sceneApproveStaerTime", defaultValue = "") String sceneApproveStaerTime,
            @Parameter(name = "sceneApproveEndTime", description = "结束时间") @RequestParam(value = "sceneApproveEndTime", defaultValue = "") String sceneApproveEndTime) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = auditProjectService.getProjectList(token,pageNumber, pageSize, name,auditUnitId,sceneApproveStaerTime,sceneApproveEndTime);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) { 
            log.error("审计实施---审计项目情况表---查询审计项目情况列表异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "审计实施---审计项目情况表---根据ID查询审计项目情况表")
    @GetMapping(value = "project/auditProjectById", produces = "application/json;charset=utf-8")
    public JsonBean getAuditProjectById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "projectId", description = "审计项目id", required = true) @RequestParam(value = "projectId") Long projectId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = auditProjectService.getProjectById(token,projectId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---审计项目情况表---根据ID查询审计项目情况表异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "审计实施---审计项目情况表--新增/更新")
    @RequestMapping(value = "project/saveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean increaseOrUpdateAuditProject(
            HttpServletRequest request,
            HttpServletResponse response,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody TblYqnsAuditProjectDto param) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = auditProjectService.saveOrUpdate(token,param);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---审计项目情况表 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }

    @Operation(summary = "审计实施---审计项目情况表--删除")
    @GetMapping(value = "project/delete", produces = "application/json;charset=utf-8")
    public JsonBean deleteAuditProject(@Parameter(name="id",required=true) @RequestParam Integer id
            ,@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = auditProjectService.delete(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---审计项目情况表 -- 删除接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }
        return jsonBean;
    }

    @Operation(summary = "审计实施---现场审查主要内容--新增/更新")
    @PostMapping(value = "siteReview/saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean increaseOrUpdateSiteReview(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody TblYqnsSiteReviewContentDto param) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = siteReviewContentService.saveOrUpdate(token,param);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---审计项目情况表 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }

    @Operation(summary = "审计实施---现场审查主要内容--删除")
    @GetMapping(value = "siteReview/delete", produces = "application/json;charset=utf-8")
    public JsonBean deleteSiteReview(@Parameter(name="id",required=true) @RequestParam Long id,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = siteReviewContentService.delete(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---现场审查主要内容 -- 删除接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }
        return jsonBean;
    }

    @Operation(summary = "审计实施---现场审查主要内容---根据入参现场审查主要内容列表")
    @GetMapping(value = "siteReview/findSiteReviewContentListByParam", produces = "application/json;charset=utf-8")
    public JsonBean findSiteReviewContentListByParam(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "projectName", description = "项目名称", required = false) @RequestParam(value = "projectName", required = false) String projectName,
            @Parameter(name = "settleProjectNum", description = "结算项目编号", required = false) @RequestParam(value = "settleProjectNum", required = false) String settleProjectNum,
            @Parameter(name="projectId",description="projectId",required=false)@RequestParam(value = "projectId", required = false)BigDecimal projectId,
            @Parameter(name = "templateId", description = "当前审计实施项目主键", required = false) @RequestParam(value = "templateId", required = false) BigDecimal templateId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = siteReviewContentService.findSiteReviewContentListByParam(token,projectName, settleProjectNum, pageNumber, pageSize,templateId,projectId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---审计项目情况表---根据ID查询审计项目情况表异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "审计实施---现场审查主要内容---根据ID查询现场审查主要内容")
    @GetMapping(value = "siteReview/findOneById", produces = "application/json;charset=utf-8")
    public JsonBean findOneSiteReviewById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "siteReviewId", description = "现场审查内容id", required = true) @RequestParam(value = "siteReviewId") Long siteReviewId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = siteReviewContentService.findOneSiteReviewById(token,siteReviewId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---现场审查主要内容---根据ID查询现场审查主要内容异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

}
