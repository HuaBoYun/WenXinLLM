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
import com.huabo.audit.oracle.entity.TblYqnsXmsqsq;
import com.huabo.audit.oracle.service.TblYqnsXmsqsqService;
import com.huabo.audit.oracle.vo.XmdqVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计项目延期申请控制器
 * <p>提供审计项目延期申请的分页查询、提交等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="项目管理-项目延期申请接口",description="项目管理-项目延期申请接口")
@Slf4j
public class AuditXmyqsqController {

	@Resource
	private TblYqnsXmsqsqService tblYqnsXmsqsqService ; 
	 

	
	@Operation(summary = "项目延期申请- 列表查询")
	@GetMapping("/xmyqsq/getList")
	public JsonBean getsheetList(XmdqVo vo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmsqsqService.findAllList(token, pageNumber, pageSize, vo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目延期申请- 列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	
	@Operation(summary = "项目延期申请- 保存或修改")
	@PostMapping("/xmyqsq/saveOrupdate")
	public JsonBean saveOrupdate(TblYqnsXmsqsq xmyqsq,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attids", description = "上传底稿id拼接字符串", required = false) @RequestParam(value = "attids", required = false) String attids) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmsqsqService.saveOrupdate(token, xmyqsq, attids);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目延期申请-保存或修改 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	@Operation(summary = "项目延期申请- 查询详情")
	@GetMapping("/xmyqsq/getone")
	public JsonBean getone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "xmdqid", description = "id", required = false) @RequestParam(value = "xmdqid", required = false) BigDecimal xmdqid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmsqsqService.findByid(token, xmdqid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目延期申请-查询详情 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "项目延期申请- 查询附件列表")
	@GetMapping("/xmyqsq/getattList")
	public JsonBean getattList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "xmdqid", description = "id", required = false) @RequestParam(value = "xmdqid", required = false) BigDecimal xmdqid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmsqsqService.findattlistByid(token, xmdqid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目延期申请-查询附件列表 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "项目延期申请- 删除")
	@PostMapping("/xmyqsq/deleteone")
	public JsonBean deleteone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "xmdqid", description = "id", required = false) @RequestParam(value = "xmdqid", required = false) BigDecimal xmdqid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmsqsqService.deleteone(token, xmdqid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目延期申请- 删除 ...接口 异常", e); 
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	@Operation(summary = "项目延期申请- 删除附件")
	@PostMapping("/xmyqsq/deleteatt")
	public JsonBean deleteatt(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attid", description = "附件id", required = false) @RequestParam(value = "attid", required = false) String attid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsXmsqsqService.deleteatt(token, attid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目延期申请- 删除附件 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	
	
	
}
