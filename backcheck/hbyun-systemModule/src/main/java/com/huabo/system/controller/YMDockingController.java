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
import com.huabo.system.service.TblStaffService;
import com.huabo.system.service.YMBusinessService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 流程平台对接控制器
 * <p>提供流程平台的人员同步、权限同步、单点登录等对接接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name = "流程平台对接Controller", description = "流程平台的对接Controoler")
@RequestMapping("/ym")
public class YMDockingController {


	@Resource
	public YMBusinessService ymBusinessService;

	@Resource
	public TblStaffService tblStaffService;


	@RequestMapping(value = "/synchronizeStaffUserRela", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="处理用户和组织的中间表的表关系，用于数据初始化")
    public JsonBean synchronizeStaffUserRela(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token) throws Exception {
        return this.tblStaffService.dealUserOrgRelation(token);
    }

	@RequestMapping(value = "/synchronizeUserRightRela", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="处理用户和组织的中间表的表关系，用于数据初始化")
    public JsonBean synchronizeUserRightRela(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token) throws Exception {
        return this.tblStaffService.dealUserRightRelation(token);
    }

	@RequestMapping(value = "/paikeSingLogin", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="派可报表单点登录接口")
    public JsonBean paikeSingLogin(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token) throws Exception {
        return this.ymBusinessService.paikeSingLogin(token);
    }


	@RequestMapping(value = "/singleSign", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="单点登录访问获取token等用户信息")
    public JsonBean singleSignMothed(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="origin",description="登录系统来源 app-移动端，pc-电脑端",required=false) @RequestParam(value = "origin", required = false) String origin) throws Exception {
        return this.ymBusinessService.singleSignMothed(token,origin);
    }


	@RequestMapping(value = "/synchronizeOrgInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="批量同步组织信息")
    public JsonBean synchronizeOrgInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="orgId",description="同步公司组织信息",required=false) @RequestParam(value = "orgId", required = false) BigDecimal orgId) throws Exception {
        return this.ymBusinessService.synchronizeOrgInfo(token,orgId);
    }

	@RequestMapping(value = "/synchronizeRoleInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="批量同步角色信息-需要先同步组织")
    public JsonBean synchronizeRoleInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="orgId",description="同步公司Id",required=false) @RequestParam(value = "orgId", required = false) BigDecimal orgId) throws Exception {
        return this.ymBusinessService.synchronizeRoleInfo(token,orgId);
    }

	@RequestMapping(value = "/synchronizeJobInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="批量同步岗位信息-需要先同步组织")
    public JsonBean synchronizeJobInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="orgId",description="同步公司Id",required=false) @RequestParam(value = "orgId", required = false) BigDecimal orgId) throws Exception {
        return this.ymBusinessService.synchronizeJobInfo(token,orgId);
    }

	@RequestMapping(value = "/synchronizeStaffInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="批量同步用户信息-需要先同步组织，角色")
    public JsonBean synchronizeStaffInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="orgId",description="同步公司组织信息",required=true) @RequestParam(value = "orgId", required = true) BigDecimal orgId) throws Exception {
        return this.ymBusinessService.synchronizeStaffInfo(token,orgId);
    }


	@RequestMapping(value = "/synchronizeDeptManage", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="批量同步部门负责人")
    public JsonBean synchronizeDeptManage(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="orgId",description="同步公司组织信息",required=true) @RequestParam(value = "orgId", required = true) BigDecimal orgId) throws Exception {
        return this.ymBusinessService.synchronizeDeptManage(token,orgId);
    }

	@RequestMapping(value = "/synchronizeStaffManageInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="批量分管领导-需要先同步组织，角色")
    public JsonBean synchronizeStaffManageInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="orgId",description="同步公司组织信息",required=true) @RequestParam(value = "orgId", required = true) BigDecimal orgId) throws Exception {
        return this.ymBusinessService.synchronizeStaffManageInfo(token,orgId);
    }

}
