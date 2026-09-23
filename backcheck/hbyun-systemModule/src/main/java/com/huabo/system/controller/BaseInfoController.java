package com.huabo.system.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSystemUrgentEvents;
import com.huabo.system.service.TblOrganizaService;
import com.huabo.system.service.TblSystemBaseInfoService;
import com.huabo.system.service.TblSystemImportLogService;
import com.huabo.system.service.UserService;
import com.huabo.system.vo.param.TblOrganizationQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 系统基本信息控制器
 * <p>提供组织机构树、公司信息树、部门信息树等基础信息查询接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name = "oauth", description = "系统基本信息接口调用")
@RequestMapping("/baseInfo")
public class BaseInfoController {

	@Resource
    public TblOrganizaService tblOrganizaService;
	
	@Resource
    private UserService userService;
	
	@Resource
	private TblSystemImportLogService tblSystemImportLogService;
	
	@Resource
	private TblSystemBaseInfoService tblSystemBaseInfoService;
	
	
	
	  @OperationLog(
	            success = "获取公司和部门树结构数据",
	            busType = "系统设置",
	            fail = "获取公司和部门树结构数据",
	            operationType = OperationType.SELECT,
	            subType = "系统设置"
	    )
	@RequestMapping(value = "/getAllOrgInfoTree", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "获取公司和部门树结构数据")
    public JsonBean getAllOrgInfoTree(HttpServletRequest request,
    	@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        TblOrganizationQueryParam query) throws Exception {
        return this.tblOrganizaService.getAllOrgTree(token,query);
    }
	
	  @OperationLog(
	            success = "获取公司树结构数据",
	            busType = "系统设置",
	            fail = "获取公司树结构数据",
	            operationType = OperationType.SELECT,
	            subType = "系统设置"
	    )
	@RequestMapping(value = "/getCompanyInfoTree", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "获取公司树结构数据")
    public JsonBean getCompanyInfoTree(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        TblOrganizationQueryParam query) throws Exception {
        return this.tblOrganizaService.getCompanyInfoTree(token,query);
    }
	
	  
	  @OperationLog(
	            success = "获取部门树结构数据",
	            busType = "系统设置",
	            fail = "获取部门树结构数据",
	            operationType = OperationType.SELECT,
	            subType = "系统设置"
	    )
	@RequestMapping(value = "/getDepartmentInfoTree", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	  @Operation(summary = "获取部门树结构数据")
    public JsonBean getDepartmentInfoTree(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        TblOrganizationQueryParam query) throws Exception {
        return this.tblOrganizaService.getDepartmentInfoTree(token,query);
    }
	
	  
	  @OperationLog(
	            success = "获取公司列表页数据",
	            busType = "系统设置",
	            fail = "获取公司列表页数据",
	            operationType = OperationType.SELECT,
	            subType = "系统设置"
	    )
	@RequestMapping(value = "/getCompanyListInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	  @Operation(summary = "获取公司列表页数据")
    public JsonBean getCompanyListInfo(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        TblOrganizationQueryParam query,
        @Parameter(name = "pageNumber",description="起始页 默认为1", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
        @Parameter(name = "pageSize",description="每页数量默认为20", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) throws Exception {
        return this.tblOrganizaService.getCompanyListInfo(token,query,pageNumber,pageSize);
    }
	
	  
	  @OperationLog(
	            success = "获取部门列表页数据",
	            busType = "机构管理",
	            fail = "获取部门列表页数据",
	            operationType = OperationType.SELECT,
	            subType = "组织架构"
	    )
	@RequestMapping(value = "/getDepartmentListInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	  @Operation(summary = "获取部门列表页数据")
    public JsonBean getDepartmentListInfo(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        TblOrganizationQueryParam query,
        @Parameter(name = "pageNumber",description="起始页 默认为1", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
        @Parameter(name = "pageSize",description="每页数量默认为20", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) throws Exception {
        return this.tblOrganizaService.getDepartmentListInfo(token,query,pageNumber,pageSize);
    }
	
	  
	  @OperationLog(
	            success = "查询没有被禁用的用户",
	            busType = "权限管理",
	            fail = "查询没有被禁用的用户",
	            operationType = OperationType.SELECT,
	            subType = "用户管理"
	    )
	@RequestMapping(value = "/getUserList" , produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	  @Operation(summary = "用户管理列表-查询没有被禁用的用户")
    public JsonBean getUserList(HttpServletRequest request, TblStaff staff,
    		@Parameter(name = "pageNumber",description="起始页 默认为1", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
    		@Parameter(name = "pageSize",description="每页数量默认为20", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @RequestParam(value = "isAll", required = false,defaultValue="0") @Parameter(name = "isAll", description = "是否开启全集团查询0-否，1-是，默认0", required = false)Integer isAll) throws Exception {
		staff.setStatus(1);
        staff.setAddress( "管理员");
        return userService.finduserAllList(staff, pageNumber, pageSize, token,isAll);
    }

	  
	  @OperationLog(
	            success = "查询全部用户",
	            busType = "权限管理",
	            fail = "查询全部用户",
	            operationType = OperationType.SELECT,
	            subType = "用户管理"
	    )
    @RequestMapping(value = "/getUserListALL" , produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	  @Operation(summary = "用户管理列表-查询全部用户")
    public JsonBean getUserListALL(HttpServletRequest request, TblStaff staff,
    		@Parameter(name = "pageNumber",description="起始页 默认为1", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
    		@Parameter(name = "pageSize",description="每页数量默认为20", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @RequestParam(value = "isAll", required = false,defaultValue="0") @Parameter(name = "isAll", description = "是否开启全集团查询0-否，1-是，默认0", required = false)Integer isAll) throws Exception {
        return userService.finduserAllList(staff, pageNumber, pageSize, token,isAll);
    }
	
	  
	  
	  @OperationLog(
	            success = "导出",
	            busType = "权限管理",
	            fail = "导出",
	            operationType = OperationType.EXPORT,
	            subType = "用户管理"
	    )
	@RequestMapping(value = "/exportUserInfoList" , produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	  @Operation(summary = "用户管理列表-导出")
    public void exportUserInfoList(HttpServletRequest request, HttpServletResponse response, TblStaff staff,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @RequestParam(value = "orgId", required = false) @Parameter(name = "orgId", description = "所属公司", required = false)BigDecimal orgId) throws Exception {
		userService.exportUserInfoList(staff, token, orgId,response);
    }
	
	  
	  @OperationLog(
	            success = "导入",
	            busType = "权限管理",
	            fail = "导入",
	            operationType = OperationType.IMPORT,
	            subType = "用户管理"
	    )
	@RequestMapping(value = "/importUserInfoList" , produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	  @Operation(summary = "用户管理列表-导入")
    public JsonBean importUserInfoList(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,MultipartFile file) throws Exception {
		return userService.exportUserInfoList(token, file);
    }
	
	
	  @OperationLog(
	            success = "导入记录 分页查询",
	            busType = "权限管理",
	            fail = "导入记录 分页查询",
	            operationType = OperationType.SELECT,
	            subType = "用户管理"
	    )
	@RequestMapping(value = "/getSystemImportLogList" , produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	  @Operation(summary = "导入记录 分页查询")
    public JsonBean getSystemImportLogList(HttpServletRequest request,
    		@Parameter(name = "pageNumber",description="起始页 默认为1", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
    		@Parameter(name = "pageSize",description="每页数量默认为20", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @RequestParam(value = "createTime", required = false) @Parameter(name = "createTime", description = "创建时间", required = false)String createTime,
        @RequestParam(value = "importType", required = false) @Parameter(name = "importType", description = "导入类型 1-组织 2-人员", required = false)Integer importType) throws Exception {
		
		return tblSystemImportLogService.getSystemImportLogList(pageNumber, pageSize, token,createTime,importType);
    }
	
	  
	  @OperationLog(
	            success = "获取自定义编号",
	            busType = "系统设置",
	            fail = "获取自定义编号",
	            operationType = OperationType.SELECT,
	            subType = "系统设置"
	    )
	@RequestMapping(value = "/getAutoNumber" , produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	  @Operation(summary = "获取自定义编号")
    public JsonBean getAutoNumber(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @RequestParam(value = "configId", required = true) @Parameter(name = "configId", description = "权限表主键", required = true)String configId) throws Exception{
		return tblSystemBaseInfoService.getAutoNumber(token,configId);
    }
	
	  
	  @OperationLog(
	            success = "保存催办信息",
	            busType = "催办管理",
	            fail = "保存催办信息",
	            operationType = OperationType.ADD,
	            subType = "催办信息"
	    )
	@RequestMapping(value = "/saveUrgentEvent" , produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "保存催办信息")
    public JsonBean saveUrgentEvent(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblSystemUrgentEvents events) throws Exception{
		return tblSystemBaseInfoService.saveUrgentEvent(token,events);
    }
	
	  
	  @OperationLog(
	            success = "修改催办信息",
	            busType = "催办管理",
	            fail = "修改催办信息",
	            operationType = OperationType.UPDATE,
	            subType = "催办信息"
	    )
	@RequestMapping(value = "/modifyUrgentEvent" , produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "修改催办信息")
    public JsonBean modifyUrgentEvent(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblSystemUrgentEvents events) throws Exception{
		return tblSystemBaseInfoService.modifyUrgentEvent(token,events);
    }
	
	  
	  @OperationLog(
	            success = "催办详情",
	            busType = "催办管理",
	            fail = "催办详情",
	            operationType = OperationType.SELECT,
	            subType = "催办信息"
	    )
	@RequestMapping(value = "/getUrgentEvent" , produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "详情催办信息")
    public JsonBean getUrgentEvent(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @RequestParam(value = "eventId", required = true) @Parameter(name = "eventId", description = "主键", required = true)String eventId) throws Exception{
		return tblSystemBaseInfoService.getUrgentEvent(token,eventId);
    }
	
	  
	  @OperationLog(
	            success = "接受人获取催办信息。分页",
	            busType = "催办管理",
	            fail = "接受人获取催办信息。分页",
	            operationType = OperationType.SELECT,
	            subType = "催办信息"
	    )
	@RequestMapping(value = "/recipientList" , produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "接受人获取催办信息。分页")
    public JsonBean recipientList(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblSystemUrgentEvents events,
        @Parameter(name = "pageNumber",description="起始页 默认为1", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
    	@Parameter(name = "pageSize",description="每页数量默认为20", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) throws Exception{
		return tblSystemBaseInfoService.recipientList(token,events,pageNumber,pageSize);
    }
	
	  
	  @OperationLog(
	            success = "发起人获取自己发起的催办分页数据",
	            busType = "催办管理",
	            fail = "发起人获取自己发起的催办分页数据",
	            operationType = OperationType.SELECT,
	            subType = "催办信息"
	    )
	@RequestMapping(value = "/initiatiorUrgentList" , produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "发起人获取自己发起的催办分页数据")
    public JsonBean initiatiorUrgentList(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblSystemUrgentEvents events,
        @Parameter(name = "pageNumber",description="起始页 默认为1", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
    	@Parameter(name = "pageSize",description="每页数量默认为20", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) throws Exception{
		return tblSystemBaseInfoService.initiatiorUrgentList(token,events,pageNumber,pageSize);
    }
	
	
	  @OperationLog(
	            success = "登录时获取未处理的催办信息",
	            busType = "催办管理",
	            fail = "登录时获取未处理的催办信息",
	            operationType = OperationType.SELECT,
	            subType = "催办信息"
	    )
	@RequestMapping(value = "/loginRecipientList" , produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "登录时获取未处理的催办信息")
    public JsonBean loginRecipientList(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception{
		return tblSystemBaseInfoService.loginRecipientList(token);
    }
	
	  
	@RequestMapping(value = "/getRefreshInfo" , produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取在线用户数量")
    public JsonBean getRefreshInfo(HttpServletRequest request) throws Exception{
		return tblSystemBaseInfoService.getRefreshInfo();
    }
	
	@RequestMapping(value = "/getPendingProcessingAllNum" , produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "获取当前用户所有待处理信息的数量")
    public JsonBean getPendingProcessingAllNum(HttpServletRequest request) throws Exception{
		return tblSystemBaseInfoService.getPendingProcessingAllNum();
    }
	
	
	
	
	@RequestMapping(value = "/getFtpInfo" , produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "测试FTP是否可以登录")
    public JsonBean getFtpInfo(HttpServletRequest request,
    		@Parameter(name = "url",description="路径", required = true)@RequestParam(value = "url", required = true) String url,
        	@Parameter(name = "port",description="端口", required = true)@RequestParam(value = "port", required = true) Integer port,
        	@Parameter(name = "userName",description="用户", required = true)@RequestParam(value = "userName", required = true) String userName,
        	@Parameter(name = "userp",description="pwd", required = true)@RequestParam(value = "userp", required = true) String userp) throws Exception{
		return tblSystemBaseInfoService.getFtpInfo(url,port,userName,userp);
    }
}
