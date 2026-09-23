package com.huabo.audit.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsProposalNoticeEntity;
import com.huabo.audit.oracle.service.TblYqnsProposalNoticeService;
import com.huabo.audit.oracle.service.TblYqnsProposalNoticeXfService;
import com.huabo.audit.service.ProjectSuggestionNoticeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProposalNoticeController
 * @PACKAGE_NAME: com.huabo.audit.controller
 * @date 2023/10/10 14:00.
 * @version: V1.0
 * @description: 央企模块-计划编制-审计立项建议通知
 */
@RestController
@Slf4j
@Tag(name="审计立项建议通知",description="审计立项建议通知")
@RequestMapping(value = "/audit/proposalNotice")
public class TblYqnsProposalNoticeController {

    @Resource
    private TblYqnsProposalNoticeService tblYqnsProposalNoticeService;
    @Resource
    private TblYqnsProposalNoticeXfService tblYqnsProposalNoticeXfService;
    
    @Autowired
    private ProjectSuggestionNoticeService projectSuggestionNoticeService;
    
    @Resource
    private UserProvider userProvider;


    @Operation(summary = "审计立项建议通知--获取建议通知列表")
    @GetMapping(value = "getNoticeList", produces = "application/json;charset=utf-8")
    public JsonBean getAuditProposeList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "vo", description = "审计立项建议通知entity") TblYqnsProposalNoticeEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProposalNoticeService.getNoticeList(token,pageNumber, pageSize, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计立项建议通知 -- 获取建议通知列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "审计立项建议通知--获取建议通知单个详情信息")
    @GetMapping(value = "getNoticeById", produces = "application/json;charset=utf-8")
    public JsonBean getNoticeById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "建议通知ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProposalNoticeService.getNoticeById(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计立项建议通知 -- 获取建议通知单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "审计立项建议通知--新增/更新")
    @PostMapping(value = "saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean increaseOrUpdateAuditProject(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "vo", description = "审计立项建议通知entity") TblYqnsProposalNoticeEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProposalNoticeService.saveOrUpdate(token,vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---审计立项建议通知 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }



    @Operation(summary = "审计立项建议通知--删除（直接删除）")
    @GetMapping(value = "delete", produces = "application/json;charset=utf-8")
    public JsonBean delete(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "建议通知ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProposalNoticeService.delete(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计立项建议通知 -- 获取建议通知单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }



    @Operation(summary = "审计立项建议通知- 附件-删除（直接删除）")
    @GetMapping(value = "deleteFileAttach", produces = "application/json;charset=utf-8")
    public JsonBean deleteFileAttach(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "attId", description = "附件ID", required = true) @RequestParam(value = "attId") String attId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProposalNoticeService.deleteFileAttach(token,attId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计立项建议通知- 附件 -- 删除接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "分发（删除项目,重新增加人员）") 
    @GetMapping(value = "distribute", produces = "application/json;charset=utf-8")
    public JsonBean distribute(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "noticeId", description = "审计立项建议通知ID", required = true) @RequestParam(value = "noticeId") String noticeId,
            @Parameter(name = "userIds", description = "分发的userIds 格式: 1,2)", required = true) @RequestParam(value = "userIds") String userIds) {
        JsonBean jsonBean = new JsonBean();
        try {
        	 TblStaffUtil user = userProvider.get();
        	 if (user == null ){
                 return  ResponseFormat.retParam(0,20006,null);
             }
        	if(StringUtil.isEmpty(noticeId)){
                return ResponseFormat.retParam(30001,"未选择下发的立项建议通知");
            }
            if(StringUtils.isEmpty(userIds)){
                return ResponseFormat.retParam(30001,"未选择下发的人员");
            }
            projectSuggestionNoticeService.distribute(token,noticeId,userIds);
            jsonBean= ResponseFormat.retParam(1,200,null);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("计划编制_审计立项建议通知 --分发（删除项目id,重新增加人员） 接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "通过审计立项建议通知id 查询分发人员")
    @GetMapping(value = "getDistributeList", produces = "application/json;charset=utf-8")
    public JsonBean getDistributeList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "noticeId", description = "审计立项建议通知ID", required = true) @RequestParam(value = "noticeId") String noticeId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsProposalNoticeXfService.getListByNoticeId(token, noticeId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("计划编制_分管领导汇总 --查新分发List 接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }
}
