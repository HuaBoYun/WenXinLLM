package com.huabo.legal.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglLearrningOracle;
import com.huabo.legal.oracle.entity.TblFwglNoticeOracle;
import com.huabo.legal.oracle.service.TblFwglLearrningOracleService;
import com.huabo.legal.oracle.service.TblFwglNoticeOracleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@Tag(name="法务管理-日常管理",description="法务管理-日常管理")
@RequestMapping(value = "/api-auth/daily/management")
public class LingController {


	@Resource
	private TblFwglLearrningOracleService tblFwglLearrningOracleService;
	@Resource
	private TblFwglNoticeOracleService tblFwglNoticeOracleService;

	@Operation(summary = "学习园地/获取编号")
	@PostMapping("/ling/gaincode")
	public JsonBean gaincode(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblFwglLearrningOracleService.findbycode(token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("学习园地/获取编号 ...接口 异常", e);
		}
		return jsonBean;
	}


	@Operation(summary = "学习园地/列表")
	@RequestMapping(value = "/ling/getlist", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public JsonBean getlist(HttpServletRequest request,
			@Parameter(name = "code", description = "查询条件-编号", required = false) @RequestParam(value = "code", required = false) String code,
			@Parameter(name = "name", description = "查询条件-名称", required = false) @RequestParam(value = "name", required = false) String name,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblFwglLearrningOracleService.findAll(code, name, pageNumber, pageSize, token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("学习园地/列表 ...接口 异常", e);
		}
		return jsonBean;
	}


	@Operation(summary = "学习园地/新增或修改")
	@RequestMapping(value = "/ling/saveOrupdate", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public JsonBean saveOrupdate(HttpServletRequest request, @Parameter(name = "cp", description = "实体", required = true) TblFwglLearrningOracle ling,
			@Parameter(name = "attids", description = "上传附件id拼接字符串", required = false) @RequestParam(value = "attids", required = false) String attids,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblFwglLearrningOracleService.saveEnity(token, ling, attids);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("学习园地/新增或修改...接口 异常", e);
		}
		return jsonBean;
	}


	@RequestMapping(value = "/ling/detail", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
	@Operation(summary = "学习园地/详情")
	public JsonBean legal_getDisputeNo(HttpServletRequest request,
			@Parameter(name = "lingid", description = "主键id", required = false) @RequestParam(value = "lingid", required = false) Long lingid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblFwglLearrningOracleService.findbyid(lingid, token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("学习园地/详情...接口 异常", e);
		}
		return jsonBean;
	}


	@Operation(summary = "学习园地/删除")
	@RequestMapping(value = "/ling/deleteone", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public JsonBean deleteone(HttpServletRequest request,
			@Parameter(name = "lingid", description = "主键id", required = true) @RequestParam(value = "lingid", required = true) Long lingid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblFwglLearrningOracleService.delete(lingid, token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("学习园地/详情...接口 异常", e);
		}
		return jsonBean;
	}


	@Operation(summary = "学习园地/附件列表")
	@RequestMapping(value = "/ling/getfjList", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public JsonBean getfjList(HttpServletRequest request,
			@Parameter(name = "lingid", description = "主键id", required = false) @RequestParam(value = "lingid", required = false) Long lingid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblFwglLearrningOracleService.findattlist(token, lingid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("学习园地/附件列表...接口 异常", e);
		}
		return jsonBean;
	}


	@Operation(summary = "学习园地/删除附件")
	@RequestMapping(value = "/ling/deletefj", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public JsonBean deletefj(HttpServletRequest request,
			@Parameter(name = "attid", description = "附件id", required = false) @RequestParam(value = "attid", required = false) Long attid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblFwglLearrningOracleService.deletefj(attid, token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("学习园地/删除附件...接口 异常", e);
		}
		return jsonBean;
	}


	@Operation(summary = "通知公告/获取编号")
	@PostMapping("/noctice/gaincode")
	public JsonBean noctice_gaincode(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblFwglNoticeOracleService.findbycode(token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("通知公告/获取编号 ...接口 异常", e);
		}
		return jsonBean;
	}


	@Operation(summary = "通知公告/列表")
	@RequestMapping(value = "/notice/getlist", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public JsonBean notice_getlist(HttpServletRequest request,
			@Parameter(name = "code", description = "查询条件-编号", required = false) @RequestParam(value = "code", required = false) String code,
			@Parameter(name = "name", description = "查询条件-名称", required = false) @RequestParam(value = "name", required = false) String name,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblFwglNoticeOracleService.findAll(code, name, pageNumber, pageSize, token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("通知公告/列表 ...接口 异常", e);
		}
		return jsonBean;
	}


	@Operation(summary = "通知公告/新增或修改")
	@RequestMapping(value = "/notice/saveOrupdate", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public JsonBean notice_saveOrupdate(HttpServletRequest request, @Parameter(name = "cp", description = "实体", required = true) TblFwglNoticeOracle notice,
			@Parameter(name = "attids", description = "上传附件id拼接字符串", required = false) @RequestParam(value = "attids", required = false) String attids,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblFwglNoticeOracleService.saveEnity(token, notice, attids);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("通知公告/新增或修改...接口 异常", e);
		}
		return jsonBean;
	}


	@RequestMapping(value = "/notice/detail", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
	@Operation(summary = "通知公告/详情")
	public JsonBean notice_getDisputeNo(HttpServletRequest request,
			@Parameter(name = "noticeid", description = "主键id", required = false) @RequestParam(value = "noticeid", required = false) Long noticeid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblFwglNoticeOracleService.findbyid(noticeid, token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("通知公告/详情...接口 异常", e);
		}
		return jsonBean;
	}


	@Operation(summary = "通知公告/删除")
	@RequestMapping(value = "/notice/deleteone", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public JsonBean notice_deleteone(HttpServletRequest request,
			@Parameter(name = "noticeid", description = "主键id", required = true) @RequestParam(value = "noticeid", required = true) Long noticeid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblFwglNoticeOracleService.delete(noticeid, token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("通知公告/详情...接口 异常", e);
		}
		return jsonBean;
	}


	@Operation(summary = "通知公告/附件列表")
	@RequestMapping(value = "/notice/getfjList", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
	public JsonBean notice_getfjList(HttpServletRequest request,
			@Parameter(name = "noticeid", description = "主键id", required = false) @RequestParam(value = "noticeid", required = false) Long noticeid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblFwglNoticeOracleService.findattlist(token, noticeid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("通知公告/附件列表...接口 异常", e);
		}
		return jsonBean;
	}


}
