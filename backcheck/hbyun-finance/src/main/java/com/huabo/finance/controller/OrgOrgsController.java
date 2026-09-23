package com.huabo.finance.controller;

import java.math.BigDecimal;

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
import com.huabo.finance.service.OrgOrgsService;
import com.huabo.finance.vo.OrgOrgsVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/orgs")
@Tag(name="财务微服务",description="财务微服务")
public class OrgOrgsController {

	@Autowired
	private UserProvider userProvider;
	
	@Resource
	private OrgOrgsService orgOrgsService;
	
	@GetMapping(value = "/getList",produces = "application/json; charset=utf-8")
	@Operation(summary = "财务组织信息列表")
	public JsonBean getList(HttpServletRequest request, HttpServletResponse response,OrgOrgsVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.orgOrgsService.getList(staff,vo);
	}
	
	@PostMapping(value = "/setCompanyInfo",produces = "application/json; charset=utf-8")
	@Operation(summary = "设置财务组织与公司的关联关系")
	public JsonBean setCompanyInfo(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="财务组织主键",description="pkOrg",required=true)@RequestParam(value = "pkOrg",required = true)String pkOrg,
			@Parameter(name="系统组织主键",description="orgId",required=true)@RequestParam(value = "orgId",required = true)BigDecimal orgId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.orgOrgsService.setCompanyInfo(staff,pkOrg,orgId);
	}

	@GetMapping(value = "/getTreeList",produces = "application/json; charset=utf-8")
	@Operation(summary = "财务组织信息列表")
	public JsonBean getTreeList(HttpServletRequest request, HttpServletResponse response,OrgOrgsVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.orgOrgsService.getTreeList(staff,vo);
	}
}
