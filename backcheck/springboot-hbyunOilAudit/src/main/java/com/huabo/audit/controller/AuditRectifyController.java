package com.huabo.audit.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsAuditMyManuscriptEntity;
import com.huabo.audit.oracle.entity.TblYqnsIssueListEntity;
import com.huabo.audit.oracle.entity.TblYqnsProposeAdopt;
import com.huabo.audit.oracle.entity.TblYqnsProposeEntity;
import com.huabo.audit.oracle.service.AuditIssueListService;
import com.huabo.audit.oracle.service.AuditProposeService;
import com.huabo.audit.oracle.service.TblYqnsAuditMyManuscriptService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @ Author: wangxilu
 * @ Date: 2023/10/8
 * @ TODO:
 **/
@RestController
@Slf4j
@Tag(name="审计整改",description="审计整改")
@RequestMapping(value = "/audit/auditRectify", produces = "application/json;charset=utf-8")
public class AuditRectifyController {

    @Autowired
    AuditProposeService auditProposeService;

    @Autowired
    AuditIssueListService auditIssueListService;
    
    @Resource
    private TblYqnsAuditMyManuscriptService tblYqnsAuditMyManuscriptService;
    
    @Resource
    private UserProvider userProvider;
    
    
    @Operation(summary = "问题清单--获取我的底稿列表-分页")
    @GetMapping(value = "/wtqd/getManuscriptPage", produces = "application/json;charset=utf-8")
    public JsonBean wtqd_getManuscriptPage(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "vo", description = "我的底稿entity") TblYqnsAuditMyManuscriptEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
        	vo.setStatus(6);
            vo.setProbleMdraft("1");
            jsonBean = tblYqnsAuditMyManuscriptService.getwtqdManuscriptPage(token, pageNumber, pageSize, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("我的底稿 -- 获取列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }
    
    
    
    @Operation(summary = "我的底稿--获取我的底稿列表-分页")
    @GetMapping(value = "/issue/getManuscriptPage", produces = "application/json;charset=utf-8")
    public JsonBean getManuscriptPage(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "vo", description = "我的底稿entity") TblYqnsAuditMyManuscriptEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
        	vo.setStatus(6);
            vo.setProbleMdraft("1");
            jsonBean = tblYqnsAuditMyManuscriptService.getManuscriptPage(token, pageNumber, pageSize, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("我的底稿 -- 获取列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }
    
    @GetMapping("/getAuditOrgList")
    @Operation(summary = "通过审计报告定稿获取被审计单位")
    public JsonBean getAuditOrgList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "sjbgdgid", description = "审计报告定稿主键", required = true)@RequestParam(value = "sjbgdgid", required = true) BigDecimal sjbgdgid
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = this.auditIssueListService.getAuditOrgList(token,sjbgdgid);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    @Operation(summary = "分派接口")
    @RequestMapping(value = "/assignment", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean assignment(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "issueId", description = "清单表数据ID", required = true) @RequestParam("issueId") BigDecimal issueId,
    		@Parameter(name = "rectPerson", description = "整改人主键", required = true) @RequestParam(value = "rectPerson",required = true) BigDecimal rectPerson,
    		@Parameter(name = "rectPersonName", description = "整改人姓名", required = true) @RequestParam(value = "rectPersonName",required = true) String rectPersonName
    ) {
        try {
            return this.auditIssueListService.assignment(token, issueId,rectPerson,rectPersonName);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    /***
     * 问题清单 功能
     * @param token
     * @return
     */
    
    @Operation(summary = "问题清单--新增前置接口（无用）")
    @GetMapping(value = "issue/preAdd", produces = "application/json;charset=utf-8")
    public JsonBean preAdd(
    		@Parameter(name = "token", description = "用户标识Token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = new JsonBean();
        try {
        	jsonBean = this.auditIssueListService.getPreAddInfo(token);
        }  catch (Exception e) {
            log.error("问题清单--新增/修改数据接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }
    
    @Operation(summary = "问题清单--新增/修改数据")
    @PostMapping(value = "issue/saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean saveOrUpdateIssue(@RequestBody TblYqnsIssueListEntity param,
                                      @Parameter(name = "token", description = "用户标识Token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = new JsonBean();
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			
			if(param.getId() != null) {
				param.setUpdateTime(new Date());
			}else {
				param.setOperator(loginStaff.getRealname());
	            param.setOperatorId(loginStaff.getStaffid());
	            param.setAddTime(new Date());
	            param.setLinkDeptId(loginStaff.getLinkDetp().getOrgid());
	            param.setLinkOrgId(loginStaff.getCurrentOrg().getOrgid());
			}
			jsonBean = auditIssueListService.saveOrUpdate(param);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("问题清单--新增/修改数据接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }
    
    @Operation(summary = "问题清单--根据ID查询清单数据")
    @GetMapping(value = "issue/getIssueById", produces = "application/json;charset=utf-8")
    public JsonBean getIssueByIssueId(@Parameter(name = "issueId", description = "清单表数据ID", required = true) @RequestParam("issueId") BigDecimal issueId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = auditIssueListService.getIssueById(issueId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("问题清单--根据ID查询清单数据接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }
        return jsonBean;
    }
    
    @Operation(summary = "问题清单-查询问题数据")
    @GetMapping(value = "/issue/getAllIssueInfo", produces = "application/json;charset=utf-8")
    public JsonBean getAllIssueInfo(
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            TblYqnsIssueListEntity param) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = auditIssueListService.getAllIssueInfo(pageNumber, pageSize, param);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("问题清单--查询问题数据接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }

    @Operation(summary = "问题清单-删除问题数据的附件文件")
    @GetMapping(value = "issue/deleteAttachFile", produces = "application/json;charset=utf-8")
    public JsonBean deleteAttachFileByAttId(
            @Parameter(name = "issueId", description = "修改的数据ID", required = true) @RequestParam(value = "issueId", required = true) BigDecimal issueId,
            @Parameter(name = "fileIds", description = "要修改的附件文件ID列表", required = true) @RequestParam(value = "fileIds", required = true) String fileIds) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = auditIssueListService.deleteAttachFile(issueId, fileIds);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("问题清单--删除问题数据的附件文件接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }

    @Operation(summary = "问题清单-删除问题数据")
    @GetMapping(value = "issue/delete/{id}", produces = "application/json;charset=utf-8")
    public JsonBean deleteAuditIssueInfo(@PathVariable BigDecimal id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = auditIssueListService.delete(id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("问题清单-删除问题数据接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }
        return jsonBean;
    }

    @PostMapping("/issue/export")
    @Operation(summary = "问题清单-列表导出")
    public JsonBean issueExportList( HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		TblYqnsIssueListEntity param,
            @Parameter(name = "idList", description = "问题清单id集合") @RequestParam(value = "idList", required = false, defaultValue = "") List<String> idList){
        response.setContentType("application/binary;charset=UTF-8");
        JsonBean jsonBean = new JsonBean();
        try{
        	jsonBean =  auditIssueListService.excelUtils(response,token,param, idList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    
    
    
  /**
   * 审计建议 相关功能
   */
    @Operation(summary = "审计建议--获取建议列表")
    @GetMapping(value = "propose/allAuditPropose", produces = "application/json;charset=utf-8")
    public JsonBean getAuditProposeList(
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "title", description = "查询条件，根据标题查询", required = false) @RequestParam(value = "title", required = false) String title) {
        JsonBean jsonBean = new JsonBean(); 
        try {
            jsonBean = auditProposeService.getProposeList(pageNumber, pageSize, title);
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

    @Operation(summary = "审计建议--根据ID获取获取建议")
    @GetMapping(value = "propose/auditProposeById", produces = "application/json;charset=utf-8")
    public JsonBean getAuditProposeById(
            @Parameter(name = "proposeId", description = "审计建议ID", required = true) @RequestParam(value = "proposeId", required = true) BigDecimal proposeId,
            @Parameter(name = "wtzgid", description = "问题整改ID", required = false) @RequestParam(value = "wtzgid", required = false) BigDecimal wtzgid) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = auditProposeService.getProposeById(proposeId,wtzgid);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计建议 -- 根据ID获取获取建议接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "审计建议--新增/更新")
    @PostMapping(value = "propose/saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean increaseOrUpdateAuditPropose(
            @RequestBody TblYqnsProposeEntity param,
            @Parameter(name = "token", description = "用户标识Token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = auditProposeService.saveOrUpdate(param,token);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计建议 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }

    @Operation(summary = "审计建议--保存采纳审计建议")
    @PostMapping(value = "propose/saveAuditProposeAdopt", produces = "application/json;charset=utf-8")
    public JsonBean saveAuditProposeAdopt(
            @RequestBody TblYqnsProposeAdopt adopt,
            @Parameter(name = "token", description = "用户标识Token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = auditProposeService.saveAuditProposeAdopt(adopt,token);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计建议 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }
    
    @Operation(summary = "审计建议--获取采纳审计建议")
    @PostMapping(value = "propose/getAuditProposeAdopt", produces = "application/json;charset=utf-8")
    public JsonBean getAuditProposeAdopt(
    		@Parameter(name = "adoptId", description = "审计建议采纳ID", required = true) @RequestParam(value = "adoptId") String adoptId,
            @Parameter(name = "token", description = "用户标识Token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = auditProposeService.getAuditProposeAdopt(adoptId,token);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计建议 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }
    
    
    @Operation(summary = "审计建议--删除")
    @GetMapping(value = "propose/delete/{id}", produces = "application/json;charset=utf-8")
    public JsonBean deleteAuditPropose(@PathVariable BigDecimal id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = auditProposeService.delete(id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计建议 -- 删除接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }
        return jsonBean;
    }
}
