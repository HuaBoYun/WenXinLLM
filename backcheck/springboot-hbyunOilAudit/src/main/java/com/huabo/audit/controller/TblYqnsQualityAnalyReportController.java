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
import com.huabo.audit.oracle.entity.TblYqnsQualityAnalyReportEntity;
import com.huabo.audit.oracle.service.TblYqnsQualityAnalyReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsQualityAnalyReportController
 * @PACKAGE_NAME: com.huabo.audit.controller
 * @date 2023/10/10 14:00.
 * @version: V1.0
 * @description: 央企内审-审计实施-质量分析报告
 */
@RestController
@Slf4j
@Tag(name="质量分析报告",description="质量分析报告")
@RequestMapping(value = "/audit/QualityAnalyReport")
public class TblYqnsQualityAnalyReportController {

    @Resource
    private TblYqnsQualityAnalyReportService tblYqnsQualityAnalyReportService;


    @Operation(summary = "质量分析报告--获取分析报告列表")
    @GetMapping(value = "getRecordsList", produces = "application/json;charset=utf-8")
    public JsonBean getAuditProposeList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "vo", description = "质量分析报告entity") TblYqnsQualityAnalyReportEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsQualityAnalyReportService.getReportList(token,pageNumber, pageSize, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("质量分析报告 -- 获取质量分析报告列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "质量分析报告--获取分析报告单个详情信息")
    @GetMapping(value = "getRecordsById", produces = "application/json;charset=utf-8")
    public JsonBean getRecordsById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "分析报告ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsQualityAnalyReportService.getReportById(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("质量分析报告 -- 获取分析报告单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "质量分析报告--新增/更新")
    @PostMapping(value = "saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean saveOrUpdate(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "vo", description = "质量分析报告entity") TblYqnsQualityAnalyReportEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsQualityAnalyReportService.saveOrUpdate(token,vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---审计项目情况表 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }



    @Operation(summary = "质量分析报告--删除（直接删除）")
    @GetMapping(value = "delete", produces = "application/json;charset=utf-8")
    public JsonBean delete(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "分析报告ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsQualityAnalyReportService.delete(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("质量分析报告 -- 获取分析报告单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }



    @Operation(summary = "质量分析报告 附件-删除（直接删除）")
    @GetMapping(value = "deleteFileAttach", produces = "application/json;charset=utf-8")
    public JsonBean deleteFileAttach(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "attId", description = "附件ID", required = true) @RequestParam(value = "attId") String attId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsQualityAnalyReportService.deleteFileAttach(token,attId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("质量分析报告 - 附件 -- 删除接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


}
