package com.huabo.system.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.entity.TblTransferWork;
import com.huabo.system.service.TblStaffService;
import com.huabo.system.service.TblTransferWorkService;
import com.huabo.system.service.TblUserOrgRelationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户信息控制器
 * <p>提供用户信息查询、密码修改、组织关系查询等接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name="StaffController",description="用户信息接口")
public class StaffController {

	@Resource
	private TblStaffService tblStaffService;
	
	@Resource
	private TblUserOrgRelationService tblUserOrgRelationService;
	
	@Resource
	private TblTransferWorkService tblTransferWorkService;
	
	@RequestMapping(value = "/getUserInfoEntity", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="根据token获取当前登录用户信息")
	public JsonBean getUserInfoEntity(HttpServletRequest request,@Parameter(name = "token", description = "登录用户token", required = true)@RequestHeader(value="token",required=true) String token){
		 try {
			 log.info("获取用户信息接口");
			return this.tblStaffService.findUserInfoByToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/modifyUserPassWord", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="当前用户修改密码")
	public JsonBean modifyUserPassWord(HttpServletRequest request,@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			 @Parameter(name = "staffId", description = "用户主键", required = true) @RequestParam(value = "staffId", required = true) BigDecimal staffId,
			 @Parameter(name = "oldPassWord", description = "原密码", required = true) @RequestParam(value = "oldPassWord", required = true) String oldPassWord,
			 @Parameter(name = "newPassWord", description = "新密码", required = true) @RequestParam(value = "newPassWord", required = true) String newPassWord,
			 @Parameter(name = "twoPassWord", description = "第二次新密码", required = true) @RequestParam(value = "twoPassWord", required = true) String twoPassWord){
		 try {
			return this.tblStaffService.modifyUserPassWord(token,staffId,oldPassWord,newPassWord,twoPassWord);
		} catch (Exception e) {
			log.error("获取用户信息失败："+e.getMessage());
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getLoginUserOrgRelationList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="获取用户切换公司的集合")
	public JsonBean user_getOrgRelationList(HttpServletRequest request,
			@Parameter(name="orgname",description="公司名称，查询条件",required=false) @RequestParam(value="orgname",required=false)String orgname,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token){
		 try {
			return this.tblUserOrgRelationService.getLoginUserOrgRelationList(token,orgname);
		} catch (Exception e) {
			log.error("获取用户信息失败："+e.getMessage());
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
	}
	
	@RequestMapping(value = "/setLoginUserOrgInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="设置用户当前所在公司")
	public JsonBean user_setLoginUserOrgInfo(HttpServletRequest request,@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			 @Parameter(name = "orgId", description = "用户主键", required = true) @RequestParam(value = "orgId", required = true) BigDecimal orgId,
			 @Parameter(name = "deptId", description = "用户主键", required = false) @RequestParam(value = "deptId", required = false) BigDecimal deptId){
		 try {
			return this.tblStaffService.setLoginUserOrgInfo(token,orgId,deptId);
		} catch (Exception e) {
			log.error("获取用户信息失败："+e.getMessage());
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getListByThemeHouse", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="主题仓库用户下发取消-获取用户集合")
	public JsonBean user_getListByThemeHouse(HttpServletRequest request,@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			 @Parameter(name = "pageids", description = "选择主题仓库主键数组", required = true) @RequestParam(value = "pageids", required = true) String[] pageids,
			 @Parameter(name = "pageNumber", description = "分页起始页", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			 @Parameter(name = "pageSize", description = "每页数量", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			 @Parameter(name = "userName", description = "筛选条件-用户账号", required = false)@RequestParam(value = "userName", required = false) String userName,
			 @Parameter(name = "realName", description = "筛选条件-用户真实姓名", required = false)@RequestParam(value = "realName", required = false) String realName){
		 try {
			return this.tblStaffService.getListByThemeHouse(token,pageids,pageNumber,pageSize,userName,realName);
		} catch (Exception e) {
			log.error("获取用户信息失败："+e.getMessage());
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
	}
	
	@RequestMapping(value = "/transfer/workList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="工作移交获取分页列表")
	public JsonBean transfer_workList(HttpServletRequest request,@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblTransferWork work,
			 @Parameter(name = "pageNumber", description = "分页起始页", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			 @Parameter(name = "pageSize", description = "每页数量", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize){
		 try {
			return this.tblTransferWorkService.getPageList(token,pageNumber,pageSize,work);
		} catch (Exception e) {
			log.error("获取用户信息失败："+e.getMessage());
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/transfer/menger", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="工作移交新增或修改")
	public JsonBean transfer_menger(HttpServletRequest request,@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblTransferWork work){
		 try {
			return this.tblTransferWorkService.mengerEntity(token,work);
		} catch (Exception e) {
			log.error("获取用户信息失败："+e.getMessage());
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
	}
	
	@RequestMapping(value = "/transfer/detail", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="工作移交详情接口")
	public JsonBean transfer_detail(HttpServletRequest request,@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			 @Parameter(name = "transferid", description = "工作移交主键", required = true)@RequestParam(value = "transferid", required = true) BigDecimal transferid){
		 try {
			return this.tblTransferWorkService.getDetail(token,transferid);
		} catch (Exception e) {
			log.error("获取用户信息失败："+e.getMessage());
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
	}
	
	@RequestMapping(value = "/transfer/enableStatus", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="工作移交启用弃用")
	public JsonBean transfer_enableStatus(HttpServletRequest request,@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "transferid", description = "对接主键", required = true)@RequestParam(value = "transferid", required = true) BigDecimal transferid,
			@Parameter(name = "transtatus", description = "状态 1-启用，2-弃用 ，默认0-未生效", required = true)@RequestParam(value = "transtatus", required = true) Integer transtatus){
		 try {
			return this.tblTransferWorkService.enableStatus(token,transferid,transtatus);
		} catch (Exception e) {
			log.error("获取用户信息失败："+e.getMessage());
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getUserPartDept", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="根据用户主键获取其所有的兼职部门")
	public JsonBean getUserPartDept(HttpServletRequest request,@Parameter(name = "token", description = "登录用户token", required = true)@RequestHeader(value="token",required=true) String token,
			@Parameter(name = "staffId", description = "用户主键主键", required = true)@RequestParam(value = "staffId", required = true) BigDecimal staffId){
		 try {
			 log.info("获取用户信息接口");
			return this.tblStaffService.getUserPartDept(token,staffId);
		} catch (Exception e) {
			log.error("获取用户信息失败："+e.getMessage());
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
	}
	
}
