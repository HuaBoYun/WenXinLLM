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
import com.huabo.audit.oracle.entity.TblYqnsAuditWorkRecordsEntity;
import com.huabo.audit.oracle.service.TblYqnsAuditWorkRecordsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditWorkRecordsController
 * @PACKAGE_NAME: com.huabo.audit.controller
 * @date 2023/10/9 11:28.
 * @version: V1.0
 * @description: 央企内审-审计实施-审计工作记录
 */
@RestController
@Slf4j
@Tag(name="审计工作记录",description="审计工作记录")
@RequestMapping(value = "/audit/workRecords")
public class TblYqnsAuditWorkRecordsController {

    @Resource
    private TblYqnsAuditWorkRecordsService tblYqnsAuditWorkRecordsService;

    @Operation(summary = "我的底稿--获取工作记录列表")
    @GetMapping(value = "getRecordsListByMyDraft", produces = "application/json;charset=utf-8")
    public JsonBean getRecordsListByMyDraft(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "vo", description = "审计工作记录entity") TblYqnsAuditWorkRecordsEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditWorkRecordsService.getRecordsListByMyDraft(token,pageNumber, pageSize, vo);
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
    
     

    @Operation(summary = "审计工作记录--获取工作记录列表")
    @GetMapping(value = "getRecordsList", produces = "application/json;charset=utf-8")
    public JsonBean getAuditProposeList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "vo", description = "审计工作记录entity") TblYqnsAuditWorkRecordsEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditWorkRecordsService.getRecordsList(token,pageNumber, pageSize, vo);
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


    @Operation(summary = "审计工作记录--获取工作记录单个详情信息")
    @GetMapping(value = "getRecordsById", produces = "application/json;charset=utf-8")
    public JsonBean getRecordsById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "工作记录ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditWorkRecordsService.getRecordsById(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计工作记录 -- 获取工作记录单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "审计工作记录--新增/更新")
    @PostMapping(value = "saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean increaseOrUpdateAuditProject(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "vo", description = "审计工作记录entity") TblYqnsAuditWorkRecordsEntity vo,
            @Parameter(name = "attids", description = "上传附件的ID", required = false) @RequestParam(value = "attids", required = false) String attids) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditWorkRecordsService.saveOrUpdate(token,vo,attids);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---审计项目情况表 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }



    @Operation(summary = "审计工作记录--删除（直接删除）")
    @GetMapping(value = "delete", produces = "application/json;charset=utf-8")
    public JsonBean delete(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token, 
            @Parameter(name = "id", description = "工作记录ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditWorkRecordsService.delete(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计工作记录 -- 获取工作记录单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

}
