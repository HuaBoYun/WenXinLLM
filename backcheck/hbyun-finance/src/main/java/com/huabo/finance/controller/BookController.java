package com.huabo.finance.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.finance.entity.caiji.FaAccbookinfo;
import com.huabo.finance.entity.caiji.OrgSetofbook;
import com.huabo.finance.service.FaAccbookRoleService;
import com.huabo.finance.service.FaAccbookUserService;
import com.huabo.finance.service.FaAccbookinfoService;
import com.huabo.finance.service.OrgSetofbookService;
import com.huabo.finance.service.TblRoleService;
import com.huabo.finance.vo.FaAccbookinfoVo;
import com.huabo.finance.vo.OrgSetofbookVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 账簿类型 前端控制器
 * </p>
 *
 * @author L
 * @since 2025-03-31
 */
@RestController
@RequestMapping("/book")
@Tag(name="财务微服务",description="财务微服务")
public class BookController {

	@Autowired
	private UserProvider userProvider;
	
	@Resource
	private OrgSetofbookService orgSetofbookService;
	
	@Resource
	private FaAccbookinfoService faAccbookinfoService;
	
	@Resource
	private FaAccbookRoleService faAccbookRoleService;
	
	@Resource
	private FaAccbookUserService faAccbookUserService;
	
	@Resource
	private TblRoleService tblRoleService;
	
	
	/**
	 * 用户选择默认查询的账簿
	 */
	@GetMapping(value = "/bookStaff/getBookList",produces = "application/json; charset=utf-8")
	@Operation(summary = "用户获取自己查看的所有账簿")
	public JsonBean bookStaff_getBookList(HttpServletRequest request, HttpServletResponse response,FaAccbookinfoVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.faAccbookUserService.getBookList(request,staff,vo);
	}
	
	
	@PostMapping(value = "/bookStaff/selected",produces = "application/json; charset=utf-8")
	@Operation(summary = "账簿信息选中")
	public JsonBean bookStaff_selected(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pkAccbookinfo",description="账簿信息主键",required=true)@RequestParam(value = "pkAccbookinfo",required = true)String pkAccbookinfo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.faAccbookUserService.selected(staff,pkAccbookinfo);
	}
	
	
	
	/**
	 * 账簿信息授权给角色
	 */
	@PostMapping(value = "/bookRole/grant",produces = "application/json; charset=utf-8")
	@Operation(summary = "账簿信息授权给角色")
	public JsonBean bookRole_grant(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pkAccbookinfo",description="账簿信息主键",required=true)@RequestParam(value = "pkAccbookinfo",required = true)String pkAccbookinfo,
			@Parameter(name="roleIds",description="角色信息主键数组",required=true)@RequestParam(value = "roleIds",required = true)String[] roleIds) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.faAccbookRoleService.grant(staff,pkAccbookinfo,roleIds);
	}
	
	@PostMapping(value = "/bookRole/cancel",produces = "application/json; charset=utf-8")
	@Operation(summary = "账簿信息取消授权给角色")
	public JsonBean bookRole_cancel(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pkAccbookinfo",description="账簿信息主键",required=true)@RequestParam(value = "pkAccbookinfo",required = true)String pkAccbookinfo,
			@Parameter(name="roleIds",description="角色信息主键数组",required=true)@RequestParam(value = "roleIds",required = true)String[] roleIds) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.faAccbookRoleService.cancel(staff,pkAccbookinfo,roleIds);
	}
	
	@GetMapping(value = "/bookRole/getRoleListByBook",produces = "application/json; charset=utf-8")
	@Operation(summary = "获取所有拥有此账簿的角色列表")
	public JsonBean bookRole_getRoleListByBook(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pkAccbookinfo",description="账簿信息主键",required=true)@RequestParam(value = "pkAccbookinfo",required = true)String pkAccbookinfo,
			@Parameter(name="roleName",description="筛选条件-角色名称",required=false)@RequestParam(value = "roleName",required = false)String roleName,
			@Parameter(name="pageNumber",description="起始页",required=false)@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
			@Parameter(name="pageSize",description="每页数量",required=false)@RequestParam(value = "pageSize",required = false,defaultValue = "20")Integer pageSize) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.tblRoleService.getRoleListByBook(staff,pkAccbookinfo,roleName,pageNumber,pageSize);
	}
	
	
	
	
	
	/***
	 * 账簿信息开始
	 */
	@GetMapping(value = "/bookInfo/getList",produces = "application/json; charset=utf-8")
	@Operation(summary = "账簿信息列表接口")
	public JsonBean bookInfo_getList(HttpServletRequest request, HttpServletResponse response,FaAccbookinfoVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.faAccbookinfoService.getList(request,staff,vo);
	}
	
	@PostMapping(value = "/bookInfo/save",produces = "application/json; charset=utf-8")
	@Operation(summary = "账簿信息列表接口新增、修改接口")
	public JsonBean bookInfo_save(HttpServletRequest request, HttpServletResponse response,FaAccbookinfo fab) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.faAccbookinfoService.save(staff,fab);
	}
	
	@GetMapping(value = "/bookInfo/del",produces = "application/json; charset=utf-8")
	@Operation(summary = "账簿信息删除接口")
	public JsonBean bookInfo_del(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pkAccbookinfo",description="主键",required=true)@RequestParam(value = "pkAccbookinfo",required = true)String pkAccbookinfo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.faAccbookinfoService.del(staff,pkAccbookinfo);
	}
	
	@GetMapping(value = "/bookInfo/detail",produces = "application/json; charset=utf-8")
	@Operation(summary = "账簿信息详情接口")
	public JsonBean bookInfo_detail(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pkAccbookinfo",description="主键",required=true)@RequestParam(value = "pkAccbookinfo",required = true)String pkAccbookinfo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.faAccbookinfoService.detail(staff,pkAccbookinfo);
	}
	
	/**
	 * 账簿信息结束
	 */
	
	/**
	 * 账簿类型开始
	 */
	
	@GetMapping(value = "/setof/getList",produces = "application/json; charset=utf-8")
	@Operation(summary = "账簿类型列表接口")
	public JsonBean setOf_getList(HttpServletRequest request, HttpServletResponse response,OrgSetofbookVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.orgSetofbookService.getList(request,staff,vo);
	}
	
	@PostMapping(value = "/setof/save",produces = "application/json; charset=utf-8")
	@Operation(summary = "账簿类型列表接口新增、修改接口")
	public JsonBean setof_save(HttpServletRequest request, HttpServletResponse response,OrgSetofbook sob) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.orgSetofbookService.save(staff,sob);
	}
	
	@GetMapping(value = "/setof/del",produces = "application/json; charset=utf-8")
	@Operation(summary = "账簿类型删除接口")
	public JsonBean setof_del(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pkSetofbook",description="主键",required=true)@RequestParam(value = "pkSetofbook",required = true)String pkSetofbook) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.orgSetofbookService.del(staff,pkSetofbook);
	}
	
	@GetMapping(value = "/setof/detail",produces = "application/json; charset=utf-8")
	@Operation(summary = "账簿类型详情接口")
	public JsonBean setof_detail(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pkSetofbook",description="主键",required=true)@RequestParam(value = "pkSetofbook",required = true)String pkSetofbook) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.orgSetofbookService.detail(staff,pkSetofbook);
	}
	
	/**
	 * 账簿类型结束
	 */
	
}

