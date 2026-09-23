package com.huabo.system.controller;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.DealUserToken;
import com.huabo.system.entity.*;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblSystemOrgRightMapper;
import com.huabo.system.utils.HttpClient;
import com.huabo.system.utils.Tree;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.config.SystemStaticValue;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.service.TblAuthorizationRecordService;
import com.huabo.system.service.TblManageRightService;
import com.huabo.system.service.TblManageScreenRightService;
import com.huabo.system.service.TblOrganizaService;
import com.huabo.system.service.TblRoleService;
import com.huabo.system.service.TblSystemRightService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 权限管理控制器
 * <p>提供角色权限列表查询、权限保存、全部权限获取等接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@RequestMapping("/right")
@Tag(name="获取权限信息接口",description="获取权限接口")
public class RightController {
	
	@Resource
	private TblManageRightService tblManageRightService;
	
	@Resource
	private TblSystemRightService tblSystemRightService;

	@Resource
    private TblManageScreenRightService tblManageScreenRightService;
	
	@Resource
	private TblAuthorizationRecordService tblAuthorizationRecordService;
	
	@Resource
	private TblRoleService tblRoleService;
	
	@Resource
	private TblOrganizaService tblOrganizaService;
	
	@Resource
	private UserProvider userProvider;


	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——根据角色获取权限列表"
	)
	@RequestMapping(value = "/screenRight/getRoleRightList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="报表授权，根据角色获取权限列表")
    public JsonBean screenRight_getRoleRightList(HttpServletRequest reques,
    		 @Parameter(name="rightId",description="权限主键",required=false) @RequestParam(value = "rightId", required = false) BigDecimal rightId) throws Exception {
           JsonBean jsonBean = null;
			try {
				jsonBean =  this.tblManageScreenRightService.getScreenRoleRightList( rightId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
    }


	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.UPDATE,
			subType = "权限管理——菜单管理——报表授权，保存角色授权信息"
	)
	@RequestMapping(value = "/screenRight/saveRightInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="报表授权，保存角色授权信息")
    public String screenRight_saveRightInfo(HttpServletRequest reques,
    		 @Parameter(name="roleId",description="角色ID主键",required=true)@RequestParam(value = "roleId", required = true) String roleId,
    		 @Parameter(name="rightIds",description="权限主键集合，用,分割 示例 1,2,3,4",required=true) @RequestParam(value = "rightIds", required = true) String rightIds) throws Exception {
        
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
        	return JsonBean.error("用户已失效");
        }
		if (StringUtils.isNotBlank(roleId) && StringUtils.isNotBlank(rightIds)) {
           this.tblManageScreenRightService.grantScreenRightToRole(roleId, rightIds);
           return JsonBean.success();
        }
        return JsonBean.error();
    }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——报表授权，授权页面获取权所有报表权限"
	)
	@RequestMapping(value = "/screenRight/getAllRightList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="报表授权，授权页面获取权所有报表权限")
    public JsonBean screenRight_getAllRightList(HttpServletRequest reques,
    		 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		 @Parameter(name="rightId",description="权限主键",required=false) @RequestParam(value = "rightId", required = false) BigDecimal rightId,
    		 @Parameter(name="roleId",description="授权角色主键",required=false) @RequestParam(value = "roleId", required = true) BigDecimal roleId) throws Exception {
           JsonBean jsonBean = null;
			try {
				jsonBean =  this.tblManageScreenRightService.getAllRightList(token, rightId, roleId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
    }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——角色权限获取-根据类型，模块类型获取该节点下的所有权限节点"
	)
	@RequestMapping(value = "/getRoleRigetListByType", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="角色权限获取-根据类型，模块类型获取该节点下的所有权限节点")
	public JsonBean getRoleRigetListByType(HttpServletRequest request,
			 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
			 @Parameter(name="type",description="权限类型 为空默认返回0和1树形菜单",required=false)@RequestParam(value="type",required=false)Integer type,
			 @Parameter(name="switchType",description="判断切换公司还是权限",required=false)@RequestParam(value="switchType",required=false)String switchType,
			 @Parameter(name="moduleType",description="模块类型",required=false)@RequestParam(value="moduleType",required=false)String moduleType)throws Exception{
			JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.findRoleRigetListByType(token,type,switchType,moduleType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.UPDATE,
			subType = "权限管理——菜单管理——根据权限ID修改权限菜单的显示隐藏"
	)
	@RequestMapping(value = "/modifySystemRightVisible", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="权限管理-根据权限ID修改权限菜单的显示隐藏")
	public JsonBean modifySystemRightVisible(HttpServletRequest request,
			 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
			 @Parameter(name="rightId",description="权限ID",required=true)@RequestParam(value="rightId",required=true)BigDecimal rightId,
			 @Parameter(name="visible",description="是否隐藏 1正常显示  0隐藏",required=true)@RequestParam(value="visible",required=true)Integer visible)throws Exception{
			JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.modifySystemRightVisible(token,rightId,visible);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——角色权限获取-根据rightId，模块类型获取该节点下的所有权限节点"
	)
	@RequestMapping(value = "/roleRightList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="角色权限获取-根据rightId，模块类型获取该节点下的所有权限节点")
	public JsonBean childrenRightListAllRole(HttpServletRequest request,
			 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
			 @Parameter(name="rightId",description="父级权限Id 默认0",required=false)@RequestParam(value="rightId",required=false)BigDecimal rightId,
			 @Parameter(name="moduleType",description="模块类型",required=false)@RequestParam(value="moduleType",required=false)String moduleType)throws Exception{
			JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.findRightListByRole(token,rightId,moduleType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——角色权限获取-角色权限获取-根据rightId，模块类型获取该节点下的子级权限"
	)
	@RequestMapping(value = "/childrenRoleRightList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="角色权限获取-根据rightId，模块类型获取该节点下的子级权限")
	 public JsonBean childrenRoleRightList(HttpServletRequest request,
			 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
			 @Parameter(name="rightId",description="父级权限Id 默认0",required=false)@RequestParam(value="rightId",required=false)BigDecimal rightId,
			 @Parameter(name="moduleType",description="模块类型",required=false)@RequestParam(value="moduleType",required=false)String moduleType)throws Exception{
		 JsonBean jsonBean = null;
			try {
				if(rightId == null) {
					rightId = new BigDecimal(0);
				}
				jsonBean = this.tblSystemRightService.findRightListByRole(token,rightId,moduleType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——获取授权权限树形列表"
	)
	@RequestMapping(value = "/getRoleRightList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="角色管理-权限设定-获取授权权限树形列表")
	 public JsonBean getRoleRightList(HttpServletRequest request,
			 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
			 @Parameter(name="rightId",description="权限Id 默认0",required=false)BigDecimal rightId,
			 @Parameter(name="roleId",description="授权角色Id",required=true)BigDecimal roleId,
			 @Parameter(name="moduleType",description="模块类型",required=false)@RequestParam(value="moduleType",required=false)String moduleType)throws Exception{
			JsonBean jsonBean = null;
			try {
				if(rightId == null) {
					rightId = new BigDecimal(0);
				}
				jsonBean = this.tblSystemRightService.findAllRightListByCompanyToGrant(token,rightId,roleId,moduleType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——角色管理权限设定-保存角色授权信息"
	)
	 @RequestMapping(value = "/grantRoleRight", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	 @Operation(summary="角色管理权限设定-保存角色授权信息")
	 public JsonBean grantRoleRight(HttpServletRequest request,@Parameter(name="rightIds",description="权限多个主键，用,号分割 ，示例 1,2,3",required=true)String rightIds,
			 @Parameter(name="roleId",description="角色主键",required=true)BigDecimal roleId,
			 @Parameter(name="moduleType",description="模块类型",required=false)@RequestParam(value="moduleType",required=false)String moduleType,
			 @Parameter(name="token",description="登录用户token",required=false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				TblStaffUtil loginStaff = userProvider.get();
				if (loginStaff == null) {
					return ResponseFormat.retParam(0, 20006, null);
				}
				
				TblAuthorizationRecord confirm = null;
				 if(SystemStaticValue.REQUIREMENTVALIDATE) {
		            	//判断是否有正在审批的数据
					confirm = this.tblAuthorizationRecordService.findSpzRecordInfoByTargetId(roleId.toString());
					List<String> nameList = this.tblSystemRightService.findNameByRithIds(rightIds);
					String names = String.join(",", nameList);
					TblRole role = this.tblRoleService.findByid(roleId.toString());
					JSONObject dataJson = new JSONObject();
					dataJson.put("rname", role.getRname());
					dataJson.put("rightIds", rightIds);
					dataJson.put("rightNames", names);
					dataJson.put("moduleType", moduleType);
					if(confirm != null) {
						//修改审批中的确认数据
						confirm.setModifiedTime(new Date());
						confirm.setModifier(loginStaff.getStaffid());
						confirm.setModifyerName(loginStaff.getRealname());
						confirm.setOperationData(dataJson.toString());
						confirm.setOperationMemo(role.getRname()+"角色授权以下菜单："+names);
						confirm.setRecordText(role.getRname()+"角色菜单授权");
						this.tblAuthorizationRecordService.modifyEntity(confirm);
					}else {
						//新增确认记录需要发起流程
						confirm = new TblAuthorizationRecord();
						confirm.setRecordId(RandomUtil.uuStringId());
						confirm.setCreationTime(new Date());
						confirm.setCreator(loginStaff.getStaffid());
						confirm.setCreatorName(loginStaff.getRealname());
					 	confirm.setOperationData(dataJson.toString());
					 	confirm.setOperationMemo(role.getRname()+"角色授权以下菜单："+names);
					 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONGRANTMENU);
					 	confirm.setStatus(0);
					 	confirm.setTargetId(roleId.toString());
					 	confirm.setRecordText(role.getRname()+"角色菜单授权");
					 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEGRANT);
					 	this.tblAuthorizationRecordService.addEntity(confirm);
					}
					return ResponseFormat.retParam(1,200,confirm);
				 }else{
					jsonBean = this.tblSystemRightService.grantRoleRight(rightIds,roleId,moduleType);
				 }
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——获取指定菜单详情信息"
	)
	 @RequestMapping(value = "/rightManageRightList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="权限管理-列表页获取所有权限")
	 public JsonBean rightManageRightList(HttpServletRequest request,
			 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
			 @Parameter(name="right",description="系统权限实体",required=false)TblSystemRight right,
			 @Parameter(name="judge",description="是否传入筛选条件 0不带入 1带入",required=false)@RequestParam(value="judge",required=false)Integer judge)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.findAllRightListcf(token,right,judge);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.ADD,
			subType = "权限管理——菜单管理——列表修改"
	)
	 @RequestMapping(value = "/saveManageRight", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	 @Operation(summary="权限管理 - 新增、修改权限接口")
	 public JsonBean saveManageRight(HttpServletRequest request,@Parameter(name="right",description="权限Entity",required=true)TblSystemRight right,
			 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.saveManageRight(right,token);
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	 }


	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——根据权限ID获取权限信息"
	)
	 @RequestMapping(value = "/findRightEntityById", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="权限管理-根据权限ID获取权限信息")
	 public JsonBean findRightEntityById(HttpServletRequest request,
			 @Parameter(name="id",description="权限主键Id",required=true)@RequestParam(value="id",required=true)BigDecimal id)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.findRightEntityById(id);
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.DELETE,
			subType = "权限管理——菜单管理——删除指定菜单"
	)
	 @RequestMapping(value = "/removeManageRight", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	 @Operation(summary="权限管理-删除权限接口")
	 public JsonBean removeManageRight(HttpServletRequest request, 
			 @Parameter(name="rightId",description="权限主键Id",required=true)@RequestParam(value="rightId",required=true)BigDecimal rightId)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.removeManageRight(rightId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——公司管理-菜单设定-获取权限树形列表"
	)
	 @RequestMapping(value = "/getCompanyRightList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="公司管理-菜单设定-获取权限树形列表")
	 public JsonBean getCompanyRightList(HttpServletRequest request,
			 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
			 @Parameter(name="rightId",description="权限Id 默认0",required=false)BigDecimal rightId,
			 @Parameter(name="orgId",description="授权公司主键",required=true)BigDecimal orgId,
			 @Parameter(name="moduleType",description="模块类型",required=false)@RequestParam(value="moduleType",required=false)String moduleType
			 )throws Exception{
			JsonBean jsonBean = null;
			try {
				if(rightId == null) {
					rightId = BigDecimal.valueOf(0);
				}
				jsonBean = this.tblSystemRightService.findAllRightListByCompany(token,rightId,orgId,moduleType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.UPDATE,
			subType = "权限管理——菜单管理——公司管理-菜单设置-保存公司授权信息"
	)
	 @RequestMapping(value = "/grantCompanyRight", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	 @Operation(summary="公司管理-菜单设置-保存公司授权信息")
	 public JsonBean grantCompanyRight(HttpServletRequest request,@Parameter(name="rightIds",description="权限多个主键，用,号分割 ，示例 1,2,3",required=true)String rightIds,
			 @Parameter(name="orgId",description="公司主键",required=true)String orgId,
			 @Parameter(name="token",description="登录用户token",required=false) @RequestHeader("token")String token,
			 @Parameter(name="moduleType",description="模块类型",required=true)@RequestParam(value="moduleType",required=true)String moduleType)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.grantCompanyRight(rightIds,token,orgId,moduleType);
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——左侧获取权限树形列表"
	)
	 @RequestMapping(value = "/getCompanySettingRightList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="系统配置-菜单设置-左侧获取权限树形列表")
	 public JsonBean getCompanySettingRightList(HttpServletRequest request,
			 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
			 @Parameter(name="rightId",description="权限Id 默认0",required=false)BigDecimal rightId,
			 @Parameter(name="moduleType",description="模块类型",required=false)@RequestParam(value="moduleType",required=false)String moduleType)throws Exception{
			JsonBean jsonBean = null;
			try {
				if(rightId == null) {
					rightId = BigDecimal.valueOf(0);
				}
				jsonBean = this.tblSystemRightService.getCompanySettingRightList(token,rightId,moduleType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
	 }


	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——右侧获取权限列表信息"
	)
	 @RequestMapping(value = "/getCompanySettingRightListInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="系统配置-菜单设置-右侧获取权限列表信息")
	 public JsonBean getCompanySettingRightListInfo(HttpServletRequest request,
			 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
			 @Parameter(name="rightId",description="权限Id 默认0",required=false)BigDecimal rightId,
			 @Parameter(name="moduleType",description="模块类型",required=false)@RequestParam(value="moduleType",required=false)String moduleType)throws Exception{
			JsonBean jsonBean = null;
			try {
				if(rightId == null) {
					rightId = BigDecimal.valueOf(0);
				}
				jsonBean = this.tblSystemRightService.getCompanySettingRightListInfo(token,rightId,moduleType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——根据Id获取权限信息"
	)
	 @RequestMapping(value = "/findSystemRightSettingById", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="系统配置-菜单设置-根据Id获取权限信息")
	 public JsonBean findSystemRightSettingById(HttpServletRequest request,
			 @Parameter(name="id",description="权限主键Id",required=true)@RequestParam(value="id",required=true)BigDecimal id)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.findSystemRightSettingById(id);
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——角色授权-授权查询数据权限"
	)
	 @RequestMapping(value = "/grantRoleDataRight", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	 @Operation(summary="角色授权-授权查询数据权限")
	 public JsonBean grantRoleDataRight(HttpServletRequest request,@Parameter(name="roleId",description="角色权限",required=true)BigDecimal roleId,
			 @Parameter(name="companyIds",description="公司主键~部门主键,部门主键,部门主键-公司主键~部门主键,部门主键",required=true)String companyIds,
			 @Parameter(name="token",description="登录用户token",required=false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				TblStaffUtil loginStaff = userProvider.get();
				if (loginStaff == null) {
					return ResponseFormat.retParam(0, 20006, null);
				}
				
				TblAuthorizationRecord confirm = null;
				 if(SystemStaticValue.REQUIREMENTVALIDATE) {
		            	//判断是否有正在审批的数据
					String[] strs = companyIds.split(",");
					
					List<String> deptidList = new ArrayList<String>();
					for (String s : strs) {
						deptidList.add(s.split("~")[1]);
					}
					 
					confirm = this.tblAuthorizationRecordService.findSpzRecordInfoByTargetId(roleId.toString());
					List<String> nameList = this.tblOrganizaService.findNameByOrgIds(String.join(",", deptidList));
					String names = String.join(",", nameList);
					TblRole role = this.tblRoleService.findByid(roleId.toString());
					JSONObject dataJson = new JSONObject();
					dataJson.put("rname", role.getRname());
					dataJson.put("companyIds", companyIds);
					dataJson.put("companyNames", names);
					if(confirm != null) {
						//修改审批中的确认数据
						confirm.setModifiedTime(new Date());
						confirm.setModifier(loginStaff.getStaffid());
						confirm.setModifyerName(loginStaff.getRealname());
						confirm.setOperationData(dataJson.toString());
						confirm.setOperationMemo(role.getRname()+"角色授权查看以下组织的数据："+names);
						confirm.setRecordText(role.getRname()+"角色数据授权");
						this.tblAuthorizationRecordService.modifyEntity(confirm);
					}else {
						//新增确认记录需要发起流程
						confirm = new TblAuthorizationRecord();
						confirm.setRecordId(RandomUtil.uuStringId());
						confirm.setCreationTime(new Date());
						confirm.setCreator(loginStaff.getStaffid());
						confirm.setCreatorName(loginStaff.getRealname());
					 	confirm.setOperationData(dataJson.toString());
					 	confirm.setOperationMemo(role.getRname()+"角色授权查看以下组织的数据："+names);
					 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONGRANTDATA);
					 	confirm.setStatus(0);
					 	confirm.setTargetId(roleId.toString());
					 	confirm.setRecordText(role.getRname()+"角色数据授权");
					 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEGRANT);
					 	this.tblAuthorizationRecordService.addEntity(confirm);
					}
					jsonBean = ResponseFormat.retParam(1,200,confirm);
				 }else{
					 jsonBean = this.tblSystemRightService.grantRoleDataRight(roleId,companyIds);
				 }
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——角色授权-授权查询数据权限"
	)
	 @RequestMapping(value = "/getRoleDataDeptInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="角色授权-授权查询数据权限")
	 public JsonBean getRoleDataDeptInfo(HttpServletRequest request,@Parameter(name="roleId",description="角色权限",required=true)BigDecimal roleId,
			 @Parameter(name="token",description="登录用户token",required=false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.getRoleDataDeptInfo(roleId,token);
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——角色授权-查询该授权的部门信息"
	)
	 @RequestMapping(value = "/getGrantDataRightDeptList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="角色数据授权-查询该授权的部门信息")
	 public JsonBean getGrantDataRightDeptList(HttpServletRequest request,
			 @Parameter(name = "pageNumber",description = "当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
             @Parameter(name = "pageSize",description = "每页数量", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
             @Parameter(name = "pid",description = "公司主键ID，初始访问为空", required = false)@RequestParam(value = "pid", required = false)  BigDecimal pid,
             @Parameter(name = "roleId",description = "当前角色授权主键", required = true)@RequestParam(value = "roleId", required = true)  BigDecimal roleId,
             @Parameter(name = "deptNumber",description = "筛选条件-部门编号", required = false)@RequestParam(value = "deptNumber", required = false)  String deptNumber,
             @Parameter(name = "deptName",description = "筛选条件-部门名称", required = false)@RequestParam(value = "deptName", required = false)  String deptName
             )throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.getGrantDataRightDeptList(pageNumber,token,pageSize,pid,deptNumber,deptName,roleId);
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "菜单管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——菜单管理——用户取消授权，获取用户列表页面"
	)
	 @RequestMapping(value = "/getGrantSystemRightStaffList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="角色管理-用户取消授权，获取用户列表页面")
	 public JsonBean getGrantSystemRightStaffList(HttpServletRequest request,
			 @Parameter(name = "pageNumber",description = "当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
             @Parameter(name = "pageSize",description = "每页数量", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
             @Parameter(name = "roleId",description = "当前角色授权主键", required = true)@RequestParam(value = "roleId", required = true)  BigDecimal roleId,
             @Parameter(name = "userName",description = "筛选条件-用户账号", required = false)@RequestParam(value = "userName", required = false)  String userName,
             @Parameter(name = "realName",description = "筛选条件-用户姓名", required = false)@RequestParam(value = "realName", required = false)  String realName,
             @Parameter(name = "companyName",description = "筛选条件-公司名称", required = false)@RequestParam(value = "companyName", required = false)  String companyName,
             @Parameter(name = "deptName",description = "筛选条件-部门名称", required = false)@RequestParam(value = "deptName", required = false)  String deptName
             )throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.getGrantSystemRightStaffList(pageNumber,token,pageSize,userName,realName,roleId,companyName,deptName);
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	 }


	@OperationLog(
			success = "权限管理",
			busType = "角色管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——角色管理-用户授权角色，获取用户列表页面"
	)
	 @RequestMapping(value = "/getSystemRightStaffList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="角色管理-用户授权角色，获取用户列表页面")
	 public JsonBean getSystemRightStaffList(HttpServletRequest request,
			 @Parameter(name = "pageNumber",description = "当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
             @Parameter(name = "pageSize",description = "每页数量", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
             @Parameter(name = "roleId",description = "当前角色授权主键", required = true)@RequestParam(value = "roleId", required = true)  BigDecimal roleId,
             @Parameter(name = "userName",description = "筛选条件-用户账号", required = false)@RequestParam(value = "userName", required = false)  String userName,
             @Parameter(name = "realName",description = "筛选条件-用户姓名", required = false)@RequestParam(value = "realName", required = false)  String realName,
             @Parameter(name = "companyName",description = "筛选条件-公司名称", required = false)@RequestParam(value = "companyName", required = false)  String companyName,
             @Parameter(name = "deptName",description = "筛选条件-部门名称", required = false)@RequestParam(value = "deptName", required = false)  String deptName,
             @Parameter(name = "orgId",description = "左侧组织树结构元素的主键", required = false)@RequestParam(value = "orgId", required = false)  BigDecimal orgId,
             @RequestParam(value = "isAll", required = false,defaultValue="0") @Parameter(name = "isAll", description = "是否开启全集团查询0-否，1-是，默认0", required = false)Integer isAll
             )throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.getSystemRightStaffList(pageNumber,token,pageSize,userName,realName,roleId,companyName,deptName,isAll,orgId);
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "角色管理",
			fail = "权限管理",
			operationType = OperationType.SELECT,
			subType = "权限管理——角色管理-角色取消数据授权-查询该角色已授权的部门信息"
	)
	 @RequestMapping(value = "/getGrantRoleDataDeptInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="角色取消数据授权-查询该角色已授权的部门信息")
	 public JsonBean getGrantRoleDataDeptInfo(HttpServletRequest request,@Parameter(name="roleId",description="角色权限",required=true)BigDecimal roleId,
			 @Parameter(name="token",description="登录用户token",required=false) @RequestHeader("token")String token,
			 @Parameter(name = "pageNumber",description = "当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
             @Parameter(name = "pageSize",description = "每页数量", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
             @Parameter(name = "deptName",description = "筛选条件-部门名称", required = false)@RequestParam(value = "deptName", required = false) String deptName,
             @Parameter(name = "companyName",description = "筛选条件-公司名称", required = false)@RequestParam(value = "companyName", required = false) String companyName)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.getGrantRoleDataDeptInfo(roleId,token,pageNumber,pageSize,deptName,companyName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
	 }

	@OperationLog(
			success = "权限管理",
			busType = "角色管理",
			fail = "权限管理",
			operationType = OperationType.UPDATE,
			subType = "权限管理——角色管理-角色取消数据授权-取消角色数据授权"
	)
	 @RequestMapping(value = "/removeDataRight", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	 @Operation(summary="角色取消数据授权-取消角色数据授权")
	 public JsonBean removeDataRight(HttpServletRequest request,@Parameter(name="roleId",description="角色权限",required=true)BigDecimal roleId,
			 @Parameter(name="token",description="登录用户token",required=false) @RequestHeader("token")String token,
             @Parameter(name = "dataJson",description = "取消授权的部门公司json字符串，示例：[{\"orgId\": 242266,\"deptId\": \"247028,247046},{\"orgId\": 242343,\"deptId\": \"244973,245156\"}]", required = true)@RequestParam(value = "dataJson", required = true) String dataJson)throws Exception{
		 JsonBean jsonBean = null;
			try {
				TblStaffUtil loginStaff = userProvider.get();
				if (loginStaff == null) {
					return ResponseFormat.retParam(0, 20006, null);
				}
				
				TblAuthorizationRecord confirm = null;
				 if(SystemStaticValue.REQUIREMENTVALIDATE) {
		            	//判断是否有正在审批的数据
					confirm = this.tblAuthorizationRecordService.findSpzRecordInfoByTargetId(roleId.toString());
					JSONArray dataArray = JSONArray.parseArray(dataJson);
					JSONObject dataObj = null;
			        String deptIds = "";
					for (int i = 0 ; i < dataArray.size() ; i++) {
						dataObj = dataArray.getJSONObject(i);
						deptIds += dataObj.getString("deptId") + ",";
					}
					deptIds = deptIds.substring(0,deptIds.length()-1);
					
					List<String> nameList = this.tblOrganizaService.findNameByOrgIds(deptIds);
					String names = String.join(",", nameList);
					TblRole role = this.tblRoleService.findByid(roleId.toString());
					JSONObject djson = new JSONObject();
					djson.put("rname", role.getRname());
					djson.put("dataJson", dataJson);
					djson.put("companyNames", names);
					if(confirm != null) {
						//修改审批中的确认数据
						confirm.setModifiedTime(new Date());
						confirm.setModifier(loginStaff.getStaffid());
						confirm.setModifyerName(loginStaff.getRealname());
						confirm.setOperationData(djson.toString());
						confirm.setOperationMemo(role.getRname()+"角色授权取消查看以下组织的数据："+names);
						confirm.setRecordText(role.getRname()+"角色取消数据授权");
						this.tblAuthorizationRecordService.modifyEntity(confirm);
					}else {
						//新增确认记录需要发起流程
						confirm = new TblAuthorizationRecord();
						confirm.setRecordId(RandomUtil.uuStringId());
						confirm.setCreationTime(new Date());
						confirm.setCreator(loginStaff.getStaffid());
						confirm.setCreatorName(loginStaff.getRealname());
					 	confirm.setOperationData(djson.toString());
					 	confirm.setOperationMemo(role.getRname()+"角色授权取消查看以下组织的数据："+names);
					 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONGUNRANTDATA);
					 	confirm.setStatus(0);
					 	confirm.setTargetId(roleId.toString());
					 	confirm.setRecordText(role.getRname()+"角色取消数据授权");
					 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEGRANT);
					 	this.tblAuthorizationRecordService.addEntity(confirm);
					}
					jsonBean = ResponseFormat.retParam(1,200,confirm);
				 }else{
					 jsonBean = this.tblSystemRightService.removeDataRight(roleId,dataJson);
				 }
				
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	 }


	@OperationLog(
			success = "权限管理",
			busType = "设置管理",
			fail = "权限管理",
			operationType = OperationType.ADD,
			subType = "批量新增流程自用"
	)
	 @RequestMapping(value = "/zdyRightInsert", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	 @Operation(summary="批量新增流程自用")
	 public JsonBean zdyRightInsert(HttpServletRequest request)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblSystemRightService.zdyInsertRightList();
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	 }
}
