package com.huabo.system.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblStaffSign;
import com.huabo.system.service.TblStaffSignService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 电子签名维护控制器
 * <p>提供电子签名的上传、查询、维护等接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping("/sign")
@Tag(name="SignController",description="电子签名维护接口")
public class SignController {

	@Resource
	private TblStaffSignService tblStaffSignService;
	
	
	@PostMapping("/saveSignInfo")
	@Operation(summary="新增电子签名信息")
	public JsonBean saveSignInfo(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			TblStaffSign sign) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblStaffSignService.saveSignInfo(sign,token);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	@PostMapping("/modifySignInfo")
	@Operation(summary="修改电子签名信息")
	public JsonBean modifySignInfo(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			TblStaffSign sign) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblStaffSignService.saveDistribution(sign,token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	@GetMapping("/removeSignInfo")
	@Operation(summary="删除电子签名信息")
	public JsonBean removeSignInfo(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "signid",description="电子签名主键",required=false) @RequestParam(value = "signid", required = true) String signid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblStaffSignService.removeSignInfo(signid,token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	@PostMapping("/getSignInfo")
	@Operation(summary="通过主键获取电子签名信息")
	public JsonBean getDistributionTypeList(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "signid",description="电子签名主键",required=false) @RequestParam(value = "signid", required = true) String signid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblStaffSignService.getSignInfo(signid,token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	@GetMapping("/getSignList")
	@Operation(summary="电子签名分页信息")
	public JsonBean getSignList(HttpServletRequest request,
			@Parameter(name = "pageNumber", description = "分页起始页", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页数量", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			TblStaffSign sign) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblStaffSignService.getSignList(sign,pageNumber,pageSize,token);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return jsonBean;
	}
	
	@PostMapping("/getSignNatureList")
	@Operation(summary="审批调用-获取当前审批人电子签名")
	public JsonBean getSignNatureList(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblStaffSignService.getSignNatureList(token);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return jsonBean;
	}
	
	
}
