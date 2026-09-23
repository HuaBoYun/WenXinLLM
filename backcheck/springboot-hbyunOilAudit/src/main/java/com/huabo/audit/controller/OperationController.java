package com.huabo.audit.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.Pamas;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.service.OperationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 智能审计操作控制器
 * <p>提供智能审计的操作管理接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping("/operation")
@Slf4j
@Tag(name="智能审计操作接口",description="智能审计操作接口")
public class OperationController {

	@Resource
	public OperationService operationService;
	
	/**
	 * @Title: manuscript 
	* @Description: 发送至底稿
	* @param modelTye 类型，例如nbsj_yigl
	* @param pamas
	* @return ModelAndView
	* @throws
	 */
	@Operation(summary = "操作发送至底稿")
	@GetMapping(value = "/manuscript/{modelTye}")
	public JsonBean manuscript(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
			@Parameter(name = "modelTye", description = "操作模块判断，疑点管理-nbsj_yigl", required = true)@PathVariable String modelTye,Pamas pamas){
		JsonBean jsonBean = null;
		try {
			jsonBean = this.operationService.dealSendDigao(token,modelTye,pamas);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}
	
	
	
	/**
	 * @Title: manuscript 
	* @Description: 发送至底稿附件获取数据
	* @param modelTye 类型，例如nbsj_yigl
	* @param pamas
	* @return ModelAndView
	* @throws
	 */
	@Operation(summary = "操作发送至底稿附件 获取底稿列表")
	@GetMapping(value = "/manuscript/gzdg/{modelTye}")
	public JsonBean manuscript_gzdg(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,TblNbsjSheetEntity sheet,
			@Parameter(name = "modelTye", description = "操作模块判断，疑点管理-nbsj_yigl", required = true)@PathVariable String modelTye,Pamas pamas,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize){
		JsonBean jsonBean = null;
		try {
			jsonBean = this.operationService.dealSendDigaoListFile(token,modelTye,pamas,pageNumber,pageSize,sheet);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}
	
	/**
	 * @Title: manuscript 
	* @Description: 发送至底稿附件获取数据
	* @param modelTye 类型，例如nbsj_yigl
	* @param pamas
	* @return ModelAndView
	* @throws
	 */
	@Operation(summary = "操作发送至底稿附件 保存附件关系方法")
	@GetMapping(value = "/manuscript/att/{modelType}")
	public JsonBean saveatt(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
			@Parameter(name = "workId", description = "选择底稿主键", required = true) @RequestParam(value = "workId", required = true) Integer workId
			,@Parameter(name = "modelTye", description = "操作模块判断，疑点管理-nbsj_yigl", required = true)@PathVariable String modelTye,Pamas pamas) {
		
		JsonBean jsonBean = null;
		try {
			jsonBean = this.operationService.saveSendDigaoListFile(token,modelTye,pamas,workId);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}
	
	/**
	 * @Title: manuscript 
	* @Description: 发送至底稿
	* @param modelTye 类型，例如nbsj_yigl
	* @param pamas
	* @return ModelAndView
	* @throws
	 */
	@Operation(summary = "操作发送至疑点")
	@GetMapping(value = "/doubtful/{modelTye}")
	public JsonBean doubtful(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
			@Parameter(name = "modelTye", description = "操作模块判断，疑点管理-nbsj_yigl", required = true)@PathVariable String modelTye,Pamas pamas){
		JsonBean jsonBean = null;
		try {
			jsonBean = this.operationService.dealSendDoubtful(token,modelTye,pamas);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}
	
	
	
	/**
	 * @Title: manuscript 
	* @Description: 发送至缺陷
	* @param modelTye 类型，例如nbsj_yigl
	* @param pamas
	* @return ModelAndView
	* @throws
	 */
	@Operation(summary = "操作发送至缺陷")
	@GetMapping(value = "/defect/{modelTye}")
	public JsonBean defect(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
			@Parameter(name = "modelTye", description = "操作模块判断，疑点管理-nbsj_yigl", required = true)@PathVariable String modelTye,Pamas pamas){
		JsonBean jsonBean = null;
		try {
			jsonBean = this.operationService.dealSendDefect(token,modelTye,pamas);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}
	
	/**
	 * @Title: manuscript 
	* @Description: 发送至底稿
	* @param modelTye 类型，例如nbsj_yigl
	* @param pamas
	* @return ModelAndView
	* @throws
	 */
	@Operation(summary = "操作发送至风险")
	@GetMapping(value = "/risk/{modelTye}")
	public JsonBean risk(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
			@Parameter(name = "modelTye", description = "操作模块判断，疑点管理-nbsj_yigl", required = true)@PathVariable String modelTye,Pamas pamas){
		JsonBean jsonBean = null;
		try {
			jsonBean = this.operationService.dealSendDoubtful(token,modelTye,pamas);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}
}
