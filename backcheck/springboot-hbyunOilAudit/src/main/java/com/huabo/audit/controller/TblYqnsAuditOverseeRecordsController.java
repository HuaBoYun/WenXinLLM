package com.huabo.audit.controller;

import java.math.BigDecimal;

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
import com.huabo.audit.oracle.entity.TblYqnsAuditOverseeRecordsEntity;
import com.huabo.audit.oracle.service.TblYqnsAuditOverseeRecordsService;

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
 * @description: 央企内审-审计实施-审计督导记录
 */
@RestController
@Slf4j
@Tag(name="审计督导记录",description="审计督导记录")
@RequestMapping(value = "/audit/overseeRecords")
public class TblYqnsAuditOverseeRecordsController {

    @Resource
    private TblYqnsAuditOverseeRecordsService tblYqnsAuditOverseeRecordsService;


    @Operation(summary = "审计督导记录--获取督导记录列表") 
    @GetMapping(value = "getRecordsList", produces = "application/json;charset=utf-8")
    public JsonBean getAuditProposeList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "xmnd", description = "筛选条件-项目年度结束", required = false) @RequestParam(value = "xmnd", required = false) Integer xmnd,
			@Parameter(name = "staffId", description = "筛选条件-人员主键", required = false) @RequestParam(value = "staffId", required = false) BigDecimal staffId,
            @Parameter(name = "vo", description = "审计督导记录entity") TblYqnsAuditOverseeRecordsEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditOverseeRecordsService.getRecordsList(token,pageNumber, pageSize, vo,xmnd,staffId);
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


    @Operation(summary = "审计督导记录--获取督导记录单个详情信息")
    @GetMapping(value = "getRecordsById", produces = "application/json;charset=utf-8")
    public JsonBean getRecordsById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "督导记录ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditOverseeRecordsService.getRecordsById(token,id);
        } catch (ServiceException se) { 
            throw se;
        } catch (Exception e) {
            log.error("审计督导记录 -- 获取督导记录单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "审计督导记录--新增/更新")
    @PostMapping(value = "saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean increaseOrUpdateAuditProject(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "vo", description = "审计督导记录entity") @RequestBody TblYqnsAuditOverseeRecordsEntity vo,
            @Parameter(name = "attids", description = "上传附件id拼接字符串", required = false) @RequestParam(value = "attids", required = false) String attids) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditOverseeRecordsService.saveOrUpdate(token,vo,attids);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---审计项目情况表 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }



    @Operation(summary = "审计督导记录--删除（直接删除）")
    @GetMapping(value = "delete", produces = "application/json;charset=utf-8")
    public JsonBean delete(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "督导记录ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditOverseeRecordsService.delete(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计督导记录 -- 获取督导记录单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }
}
