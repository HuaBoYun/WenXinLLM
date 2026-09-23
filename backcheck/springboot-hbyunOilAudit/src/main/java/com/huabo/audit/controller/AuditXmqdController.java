package com.huabo.audit.controller;


import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsXmdq;
import com.huabo.audit.oracle.service.TblYqnsXmdqService;
import com.huabo.audit.oracle.vo.XmdqVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计项目启动控制器
 * <p>提供审计项目启动相关的操作接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="项目管理-项目启动接口",description="项目管理-项目启动接口")
@Slf4j
public class AuditXmqdController {

	@Resource
	private TblYqnsXmdqService tblYqnsXmdqService ; 
	 

	
	@Operation(summary = "项目启动- 列表查询")
	@GetMapping("/xmqd/getTipList")
	public JsonBean getTipList(XmdqVo vo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.getTipList(token, vo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目启动- 列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "项目启动- 列表查询")
	@GetMapping("/xmqd/getList")
	public JsonBean getsheetList(XmdqVo vo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.findAllList(token, pageNumber, pageSize, vo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目启动- 列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	
	@Operation(summary = "项目启动- 保存或修改")
	@PostMapping("/xmqd/saveOrupdate")
	public JsonBean saveOrupdate(TblYqnsXmdq xmqd,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attids", description = "上传底稿id拼接字符串", required = false) @RequestParam(value = "attids", required = false) String attids) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.saveOrupdate(token, xmqd, attids);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目启动-保存或修改 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	@Operation(summary = "项目启动- 查询详情")
	@GetMapping("/xmqd/getone")
	public JsonBean getone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "xmdqid", description = "id", required = false) @RequestParam(value = "xmdqid", required = false) BigDecimal xmdqid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.findByid(token, xmdqid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目启动-查询详情 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "项目启动- 查询附件列表")
	@GetMapping("/xmqd/getattList")
	public JsonBean getattList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "xmdqid", description = "id", required = false) @RequestParam(value = "xmdqid", required = false) BigDecimal xmdqid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.findattlistByid(token, xmdqid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目启动-查询附件列表 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "项目启动- 删除")
	@PostMapping("/xmqd/deleteone")
	public JsonBean deleteone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "xmdqid", description = "id", required = false) @RequestParam(value = "xmdqid", required = false) BigDecimal xmdqid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.deleteone(token, xmdqid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目启动- 删除 ...接口 异常", e); 
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	@Operation(summary = "项目启动- 删除附件")
	@PostMapping("/xmqd/deleteatt")
	public JsonBean deleteatt(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attid", description = "附件id", required = false) @RequestParam(value = "attid", required = false) String attid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.deleteatt(token, attid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目启动- 删除附件 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "项目启动- 启动项目")
	@PostMapping("/xmqd/qdproject")
	public JsonBean qdproject(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "xmdqid", description = "id", required = false) @RequestParam(value = "xmdqid", required = false) BigDecimal xmdqid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.qdproject(token, xmdqid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目启动-  启动项目 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	} 
	
	@Operation(summary = "项目启动- 停止项目")
	@PostMapping("/xmqd/tzproject")
	public JsonBean tzproject(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "xmdqid", description = "id", required = false) @RequestParam(value = "xmdqid", required = false) BigDecimal xmdqid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.tzproject(token, xmdqid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目启动-  启动项目 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	} 
	
	

	
	@Operation(summary = "项目启动- 启动状态项目列表查询")
	@GetMapping("/xmqd/getqdList")
	public JsonBean getqdList(XmdqVo vo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.findqdAllList(token, pageNumber, pageSize, vo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目启动- 列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
 
	@Operation(summary = "年度审计项目计划执行分析")
	@GetMapping("/auditProject/planAnalysis")
	public JsonBean auditProject_planAnalysis(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "xmnd", description = "筛选条件项目年度", required = false) @RequestParam(value = "xmnd", required = false) Integer xmnd) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.auditProjectPlanAnalysis(token, pageNumber, pageSize, xmnd);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目启动- 列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "年度审计项目计划执行分析 查看 启动与未启动的分页列表")
	@GetMapping("/auditProject/getplanAnalysisXmList")
	public JsonBean getplanAnalysisXmList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "xmnd", description = "筛选条件项目年度", required = true) @RequestParam(value = "xmnd", required = true) Integer xmnd,
			@Parameter(name = "dataType", description = "项目启动类型  1-启动 ， 2-未启动", required = true) @RequestParam(value = "dataType", required = true) Integer dataType) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.findPlanAnalysisXmList(token, pageNumber, pageSize, xmnd, dataType);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	//年度审计项目计划执行分析 查看未启动项目列表相关接口
	@Operation(summary = "年度审计项目计划执行分析 查看 未启动项目列表 - 财务/工程  - 11/12")
	@GetMapping("/auditProject/getEvaluationList")
	public JsonBean getEvaluationList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
			@Parameter(name = "xmnd", description = "筛选条件项目年度", required = true) @RequestParam(value = "xmnd", required = true) Integer xmnd,
			@Parameter(name = "projectType", description = "11-财务 , 12-工程 ", required = true) @RequestParam(value = "projectType", required = true) String projectType,
			@Parameter(name = "projectName", description = "项目名称 -查询条件 ", required = false) @RequestParam(value = "projectName", required = false) String projectName ) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.getMx11(token, pageNumber, pageSize, xmnd,projectType,projectName);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "年度审计项目计划执行分析 查看 未启动项目列表 - 二级单位离任 21")
	@GetMapping("/auditProject/getLeaveAuditTwoLevel")
	public JsonBean getLeaveAuditTwoLevel(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
			@Parameter(name = "xmnd", description = "筛选条件项目年度", required = true) @RequestParam(value = "xmnd", required = true) Integer xmnd,
			@Parameter(name = "projectName", description = "项目名称 -查询条件 ", required = false) @RequestParam(value = "projectName", required = false) String projectName ) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.getMx21(token, pageNumber, pageSize, xmnd,projectName);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "年度审计项目计划执行分析 查看 未启动项目列表 - 任中审计明细 22")
	@GetMapping("/auditProject/getRzsjmxListDraftPlan")
	public JsonBean getRzsjmxListDraftPlan(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
			@Parameter(name = "xmnd", description = "筛选条件项目年度", required = true) @RequestParam(value = "xmnd", required = true) Integer xmnd,
			@Parameter(name = "projectName", description = "项目名称 -查询条件 ", required = false) @RequestParam(value = "projectName", required = false) String projectName ) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.getMx22(token, pageNumber, pageSize, xmnd,projectName);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	@Operation(summary = "年度审计项目计划执行分析 查看 未启动项目列表 - 汇总数据列表  ，23，,31,32")
	@GetMapping("/auditProject/getJhGlhzList")
	public JsonBean getJhGlhzList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
			@Parameter(name = "xmnd", description = "筛选条件项目年度", required = true) @RequestParam(value = "xmnd", required = true) Integer xmnd,
			@Parameter(name = "glType", description = "关联类型 ， 23（三级单位离任审计汇总） 、 31（工程项目结算项目汇总）、32（建设项目投资基本情况汇总）", required = true) @RequestParam(value = "glType", required = true) String glType,
			@Parameter(name = "projectName", description = "项目名称 -查询条件 ", required = false) @RequestParam(value = "projectName", required = false) String projectName ) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.getMxhzList(token, pageNumber, pageSize, xmnd,glType,projectName);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "年度审计项目计划执行分析 查看 未启动项目列表 - 其他审计列表  ，41")
	@GetMapping("/auditProject/getOtherAuditList")
	public JsonBean getOtherAuditList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
			@Parameter(name = "xmnd", description = "筛选条件项目年度", required = true) @RequestParam(value = "xmnd", required = true) Integer xmnd,
			@Parameter(name = "auditItemName", description = "审计项目名称 -查询条件 ", required = false) @RequestParam(value = "auditItemName", required = false) String auditItemName ) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmdqService.getMxOtherAuditList(token, pageNumber, pageSize, xmnd,auditItemName);
		} catch (ServiceException ex) {
			throw ex; 
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
}
