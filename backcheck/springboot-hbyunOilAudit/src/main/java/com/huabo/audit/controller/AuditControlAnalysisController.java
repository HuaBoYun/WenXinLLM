package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.service.AuditControlAnalysisService;
import com.huabo.audit.oracle.service.TblYqnsAuditMyManuscriptService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName AuditControlAnalysis
 * @Description 审计管控分析
 * @Author ZiYao
 * @Date 2022/4/12 16:27
 * @Version 1.0
 */
@RestController
@Slf4j
@Tag(name="审计管控分析",description="审计管控分析")
@RequestMapping(value = "/auditControlAnalysis")
public class AuditControlAnalysisController {
	
	@Resource
	private AuditControlAnalysisService auditControlAnalysisService;
	
	@Resource
	private TblYqnsAuditMyManuscriptService tblYqnsAuditMyManuscriptService;
	
	@Operation(summary = "审计情况总览")
	@GetMapping("/auditSituationOverview") 
	public synchronized JsonBean auditSituationOverview(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "preXmnd", description = "筛选条件-项目年度起始", required = false) @RequestParam(value = "preXmnd", required = false) Integer preXmnd,
			@Parameter(name = "endXmnd", description = "筛选条件-项目年度结束", required = false) @RequestParam(value = "endXmnd", required = false) Integer endXmnd,
			@Parameter(name = "choiceType", description = "维度类型，1-年度，2-人员和年度", required = true) @RequestParam(value = "choiceType", required = true) Integer choiceType,
			@Parameter(name = "staffId", description = "筛选条件-人员主键", required = false) @RequestParam(value = "staffId", required = false) BigDecimal staffId,
			@Parameter(name = "staffName", description = "筛选条件-人员真实姓名", required = false) @RequestParam(value = "staffName", required = false) String staffName) {
		JsonBean jsonBean = null;
		try {
			jsonBean = auditControlAnalysisService.auditControlAnalysisService(token, preXmnd, endXmnd, choiceType,staffId,staffName,pageNumber,pageSize);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
}
