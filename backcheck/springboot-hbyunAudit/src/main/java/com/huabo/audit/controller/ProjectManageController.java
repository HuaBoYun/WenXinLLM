package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.vo.TblNbsjPlanProjectVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectVo;
import com.huabo.audit.service.TblNbsjAuditplanService;
import com.huabo.audit.service.TblNbsjPlanProjectService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjTypeService;
import com.huabo.audit.service.TblOrganizaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ProjectManageController
 * @Description 审计项目管理
 * @Author ZiYao
 * @Date 2022/4/15 17:52
 * @Version 1.0
 */
@RestController
@Slf4j
@Tag(name="审计项目管理",description="审计项目管理")
@RequestMapping(value = "/projectManage")
public class ProjectManageController {
	
	@Resource
	private TblNbsjProjectService tblNbsjProjectService;
	
	@Resource
	private TblNbsjAuditplanService tblNbsjAuditplanService;
	
	@Resource
	private TblNbsjTypeService tblNbsjTypeService;
	
	@Resource
	private TblNbsjPlanProjectService tblNbsjPlanProjectService;
	
	@Resource
	private TblOrganizaService tblOrganizaService;

	@OperationLog(
			success = "项目管理列表",
			busType = "智能审计",
			fail = "项目管理列表",
			operationType = OperationType.SELECT,
			subType = "项目管理-项目管理-分页列表获取数据"
	)
	@RequestMapping(value = "/getNbsjProjectPageList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "项目管理-分页列表获取数据")
    public JsonBean getNbsjProjectPageList(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
   		@Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
   		@Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
   		@Parameter(name = "projectStartDate", description = "项目开始时间 格式年-月-日", required = false)@RequestParam(value = "projectStartDate", required = false) String projectStartDate,
   		@Parameter(name = "projectEndDate", description = "项目结束时间 格式年-月-日", required = false)@RequestParam(value = "projectEndDate", required = false) String projectEndDate,
   		TblnbsjProjectVo project) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjProjectService.getNbsjProjectPageList(token,pageNumber,pageSize,project,projectStartDate,projectEndDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }


	@OperationLog(
			success = "审计计划集合",
			busType = "智能审计",
			fail = "审计计划集合",
			operationType = OperationType.SELECT,
			subType = "项目管理-项目管理-新增修改页面获取审计计划集合"
	)
	@RequestMapping(value = "/getNbsjAuditPlanListForMerge", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "项目管理-新增修改页面获取审计计划集合")
    public JsonBean getNbsjAuditPlanListForMerge(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = this.tblNbsjAuditplanService.getNbsjAuditPlanListForMergeNbsjProject(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }

	@OperationLog(
			success = "时间获取",
			busType = "智能审计",
			fail = "时间获取",
			operationType = OperationType.SELECT,
			subType = "项目管理-项目管理-新增修改页面获取审计计划后获取审计开始和结束时间合"
	)
	@RequestMapping(value = "/getNbsjAuditPlanDateInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "项目管理-新增修改页面获取审计计划后获取审计开始和结束时间")
    public JsonBean getNbsjAuditPlanDateInfo(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "planId", description = "审计计划主键", required = true)@RequestParam(value = "planId", required = true)BigDecimal planId) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = this.tblNbsjAuditplanService.getNbsjAuditPlanDateInfo(token,planId);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }


	@OperationLog(
			success = "审计类型获取",
			busType = "智能审计",
			fail = "审计类型获取",
			operationType = OperationType.SELECT,
			subType = "项目管理-项目管理-新增修改页面获取审计类型集合"
	)
	@RequestMapping(value = "/getNbsjTypeListForMerge", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "项目管理-新增修改页面获取审计类型集合")
    public JsonBean getNbsjTypeListForMerge(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = this.tblNbsjTypeService.getNbsjTypeListForMerge(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }

	@OperationLog(
			success = "获取计划项目",
			busType = "智能审计",
			fail = "获取计划项目",
			operationType = OperationType.SELECT,
			subType = "项目管理-项目管理-新增修改获取所选计划的计划项目列表"
	)
    @RequestMapping(value = "/getPlanProjectListByPlanId", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "项目管理-新增修改获取所选计划的计划项目")
	public JsonBean getPlanProjectListByPlanId(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,TblNbsjPlanProjectVo project)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjPlanProjectService.getPlanProjectListByPlanIdPageInfo(token,project);
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	}

	@OperationLog(
			success = "公司数菜单获取",
			busType = "智能审计",
			fail = "公司数菜单获取",
			operationType = OperationType.SELECT,
			subType = "项目管理-项目管理-新增修改获取审计对象的公司树形菜单"
	)
    @RequestMapping(value = "/getOrgTreeListByAuditObj", produces = "application/json; charset=utf-8")
    @Operation(summary = "项目管理-新增修改获取审计对象的公司树形菜单")
	public JsonBean getOrgTreeListByAuditObj( HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			@Parameter(name = "nodeId", description = "公司主键Id", required = false)@RequestParam(value = "nodeId", required = false)BigDecimal nodeId) throws Exception{
    	
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblOrganizaService.getOrgTreeListByAuditObj(token,nodeId);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}
    
    
}
