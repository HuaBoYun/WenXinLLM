package com.huabo.audit.controller;



import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.Tree;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjProjectTeamEntity;
import com.huabo.audit.oracle.vo.TblNbsjTempleteVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectRwfpVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectZXYLVo;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.service.TblNbsjAuditplanService;
import com.huabo.audit.service.TblNbsjOperateService;
import com.huabo.audit.service.TblNbsjPlanProjectService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjProjectteamService;
import com.huabo.audit.service.TblNbsjQuestionTypeService;
import com.huabo.audit.service.TblNbsjTempleteService;
import com.huabo.audit.service.TblNbsjTypeService;
import com.huabo.audit.service.TblOrganizaService;
import com.huabo.audit.service.TblStaffService;
import com.huabo.audit.util.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计项目管理
 */
@RestController
@Slf4j
@Tag(name="审计项目管理",description="审计项目管理")
@RequestMapping(value = "/auditProject")
public class NbsjProjectController {
	
	@Resource
	public TblNbsjProjectService tblnbsjProjectService;
	
	@Resource
	public TblNbsjProjectteamService tblNbsjProjectteamService;
	
	@Resource
	private TblNbsjAuditplanService tblNbsjAuditplanService;
	
	@Resource
	private TblNbsjTypeService tblNbsjTypeService;
	
	@Resource
	private TblNbsjPlanProjectService tblNbsjPlanProjectService;
	
	@Resource
	private TblOrganizaService tblOrganizaService;
	
	@Resource
	public TblNbsjTempleteService tblNbsjTempleteService;
	
	@Resource
	public TblNbsjOperateService tblNbsjOperateService;
	
	@Resource
	private TblStaffService tblStaffService;
	
	@Resource
	private TblAttachmentService tblAttachmentService;
	
	@Resource
    private TblNbsjQuestionTypeService tblNbsjQuestionTypeService;
	
	@Resource
    private UserProvider userProvider;

	/**
	 * 审计分析-审计分析报告-查询条件年度查询
	 */
	@OperationLog(
			success = "审计分析报告",
			busType = "智能审计",
			fail = "审计分析报告",
			operationType = OperationType.SELECT,
			subType = "成果运用——审计分析-审计分析报告-查询条件年度查询"
	)
	@GetMapping("/sjfx/sjsl")
	@Operation(summary = "审计分析-审计分析报告-查询条件年度查询")
	public JsonBean sjsl(HttpServletRequest request,
							@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
						 	@Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(value = "year", required = false,defaultValue = "2025") Integer year,
						 	@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
						 	@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.sjsl(token,year,pageNumber,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 整改问题一览表
	 */
	@OperationLog(
			success = "整改问题一览表",
			busType = "智能审计",
			fail = "整改问题一览表",
			operationType = OperationType.SELECT,
			subType = "成果运用——审计分析-审计分析报告-查询条件年度查询【{{year}}】"
	)
	@GetMapping("/sjfx/zgwt")
	@Operation(summary = "审计分析-整改问题一览表-查询条件年度查询")
	public JsonBean zgwt(HttpServletRequest request,
						 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
						 @Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(value = "year", required = false,defaultValue = "2025") Integer year,
						 @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
						 @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.zgwt(token,year,pageNumber,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 整改问题详细数据查询(支持按主管部门和查询类型筛选)
	 */
	@OperationLog(
			success = "整改问题详细数据",
			busType = "智能审计",
			fail = "整改问题详细数据",
			operationType = OperationType.SELECT,
			subType = "成果运用——整改问题详细数据查询"
	)
	@GetMapping("/sjfx/zgwt/detail")
	@Operation(summary = "审计分析-整改问题详细数据查询")
	public JsonBean zgwtDetail(HttpServletRequest request,
							 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
							 @Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(value = "year", required = false) Integer year,
							 @Parameter(name = "orgName", description = "主管部门名称", required = false) @RequestParam(value = "orgName", required = false) String orgName,
							 @Parameter(name = "projectId", description = "项目ID", required = false) @RequestParam(value = "projectId", required = false) String projectId,
							 @Parameter(name = "audittype", description = "审计类型", required = false) @RequestParam(value = "audittype", required = false) String audittype,
							 @Parameter(name = "projectsource", description = "审计来源", required = false) @RequestParam(value = "projectsource", required = false) String projectsource,
							 @Parameter(name = "queryType", description = "查询类型: yzg(已整改) | wzg(未整改) | zs(整改总数) | yxh(已销号) | wxh(未销号) | xhzs(销号总数)", required = true) @RequestParam(value = "queryType", required = true) String queryType,
							 @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
							 @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.zgwtDetail(token, year, orgName, projectId, audittype, projectsource, queryType, pageNumber, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "整改问题一览表详细数据",
			busType = "智能审计",
			fail = "整改问题一览表详细数据",
			operationType = OperationType.SELECT,
			subType = "成果运用——整改问题一览表详细数据查询（基于整改清单）"
	)
	@GetMapping("/sjfx/zgwt/issues/detail")
	@Operation(summary = "审计分析-整改问题一览表详细数据查询（基于整改清单）")
	public JsonBean zgwtIssuesDetail(HttpServletRequest request,
								   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
								   @Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(value = "year", required = false) Integer year,
								   @Parameter(name = "orgName", description = "主管部门名称", required = false) @RequestParam(value = "orgName", required = false) String orgName,
								   @Parameter(name = "queryType", description = "查询类型: yzg(已整改) | wzg(未整改) | zs(整改总数) | yxh(已销号) | wxh(未销号) | xhzs(销号总数)", required = true) @RequestParam(value = "queryType", required = true) String queryType,
								   @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
								   @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.zgwtIssuesDetail(token, year, orgName, queryType, pageNumber, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}


	/**
	 * 审计分析-查询条件年度查询
	 */
	@OperationLog(
			success = "审计分析",
			busType = "智能审计",
			fail = "审计分析",
			operationType = OperationType.SELECT,
			subType = "成果运用——审计分析-查询条件年度查询"
	)
	@GetMapping("/sjfx/allyear")
	@Operation(summary = "审计分析-查询条件年度查询")
	public JsonBean allyear(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.findReportYearList(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	

	/**
	 * 审计分析-审计项目情况表
	 */
	@OperationLog(
			success = "审计项目情况",
			busType = "智能审计",
			fail = "审计项目情况",
			operationType = OperationType.SELECT,
			subType = "成果运用——审计分析-审计项目情况表"
	)
	@GetMapping("/sjfx/sjxmqk")
	@Operation(summary = "审计分析-审计项目情况表")
	public JsonBean sjxmqk(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(value = "year", required = false) Integer year,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.findProjectItemReport(token, pageNumber, pageSize, year);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	/**
	 * 审计分析-各公司审计项目数
	 */
	@OperationLog(
			success = "各公司审计项目数",
			busType = "智能审计",
			fail = "各公司审计项目数",
			operationType = OperationType.SELECT,
			subType = "成果运用——审计分析-各公司审计项目数"
	)
	@GetMapping("/sjfx/ggsxm")
	@Operation(summary = "审计分析-各公司审计项目数")
	public JsonBean ggsxm(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(value = "year", required = false) Integer year) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.findNbsjProjectCountByCompanyId(token, year);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	/**
	 * 审计分析-审计类型项目
	 */
	@OperationLog(
			success = "审计类型项目",
			busType = "智能审计",
			fail = "审计类型项目",
			operationType = OperationType.SELECT,
			subType = "审计分析-审计类型项目"
	)
	@GetMapping("/sjfx/sjlxxm")
	@Operation(summary = "审计分析-审计类型项目")
	public JsonBean sjlxxm(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(value = "year", required = false) Integer year) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.findAuditTypeCount(token, year);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "查询本年每月项目数量",
			busType = "智能审计",
			fail = "查询本年每月项目数量",
			operationType = OperationType.SELECT,
			subType = "查询本年每月项目数量"
	)
	@Operation(summary = "查询本年每月项目数量")
	@GetMapping("/projectYfcount") 
	public JsonBean projectYfcount(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "year", description = "查询年度") @RequestParam(value = "year", required = false) Integer year) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.selectPlanyfcount(token, year);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	@OperationLog(
			success = "查询本年项目状态数量",
			busType = "智能审计",
			fail = "查询本年项目状态数量",
			operationType = OperationType.SELECT,
			subType = "查询本年项目状态数量，并按状态分类统计"
	)
	@Operation(summary = "查询本年项目状态数量")
	@GetMapping("/projectZfcount") 
	public JsonBean projectZfcount(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "year", description = "查询年度") @RequestParam(value = "year", required = false) Integer year) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.selectPlanZtcount(token, year);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	@OperationLog(
			success = "本年审计项目数量",
			busType = "智能审计",
			fail = "本年审计项目数量",
			operationType = OperationType.SELECT,
			subType = "查询本年审计项目数量统计"
	)
	@Operation(summary = "本年审计项目数量统计")
	@GetMapping("/plancount")
	public JsonBean plancount(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "year", description = "查询年度") @RequestParam(value = "year", required = false) Integer year) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.selectPlancount(token, year);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	/**
	 * 审计项目数详细数据(统一接口)
	 * 支持按公司、审计类型、状态、月份查询
	 */
	@OperationLog(
			success = "审计项目数详细数据",
			busType = "智能审计",
			fail = "审计项目数详细数据",
			operationType = OperationType.SELECT,
			subType = "查询审计项目详细数据(支持按公司/审计类型/状态/月份查询)"
	)
	@Operation(summary = "查询审计项目详细数据(统一接口)")
	@GetMapping("/plancount/detail") 
	public JsonBean plancountDetail(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "year", description = "查询年度") @RequestParam(value = "year", required = false) String year,
			@Parameter(name = "companyName", description = "公司名称(按公司查询时传递)") @RequestParam(value = "companyName", required = false) String companyName,
			@Parameter(name = "auditType", description = "审计类型(按类型查询时传递)") @RequestParam(value = "auditType", required = false) String auditType,
			@Parameter(name = "status", description = "完成状态(按状态查询时传递)") @RequestParam(value = "status", required = false) String status,
			@Parameter(name = "month", description = "月份(按月查询时传递,格式: 1-12)") @RequestParam(value = "month", required = false) String month,
			@Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.selectPlancountDetail(token, year, companyName, auditType, status, month, pageNumber, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 审计项目类型详细数据
	 */
	@OperationLog(
			success = "审计项目类型详细数据",
			busType = "智能审计",
			fail = "审计项目类型详细数据",
			operationType = OperationType.SELECT,
			subType = "查询指定审计类型的项目详细数据"
	)
	@Operation(summary = "查询指定审计类型的项目详细数据")
	@GetMapping("/sjfx/sjlxxm/detail")
	public JsonBean auditTypeDetail(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "year", description = "查询年度") @RequestParam(value = "year", required = false) String year,
			@Parameter(name = "auditType", description = "审计类型") @RequestParam(value = "auditType") String auditType,
			@Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.selectAuditTypeDetail(token, year, auditType, pageNumber, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}


//	/**
//	 * 项目管理列表
//	 */
//	@GetMapping("/xmgl/project_list")
//	@Operation(summary = "项目管理列表")
//	public JsonBean project_list(HttpServletRequest request, TblnbsjProjectVo tblnbsjProjectVo,
//			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
//			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
//			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
//
//		JsonBean jsonBean = null;
//		try {
//			jsonBean = tblnbsjProjectService.projectPageList(token, pageNumber, pageSize,tblnbsjProjectVo);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return jsonBean;
//	}
//	
//	/**
//	 * 项目管理-新增与修改
//	 */
//	@RequestMapping(value = "/xmgl/project_add", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
//	@Operation(summary = "项目管理-新增与修改")
//    public JsonBean project_add(HttpServletRequest request,@Parameter(name = "pj", description = "实体", required = false)TblNbsjProject pj,
//			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
//			 @Parameter(name = "planStartDate", description = "计划开始时间", required = false)@RequestParam(value = "planStartDate", required = false) String planStartDate,
//			 @Parameter(name = "planEndDate", description = "计划结束时间", required = false)@RequestParam(value = "planEndDate", required = false) String planEndDate,
//			 @Parameter(name = "attids", description = "附件id数组", required = false)@RequestParam(value = "attids", required = false) String attids,
//			 @Parameter(name="pd_dx",description="pd_dx",required=false)@RequestParam(value = "pd_dx", required = false) String pd_dx,
//			 @Parameter(name = "pjTeamJson", description = "项目小组json字符串", required = false)@RequestParam(value = "pjTeamJson", required = false) String pjTeamJson)throws Exception{
//		JsonBean jsonBean = null;
//		try {
//			jsonBean = this.tblnbsjProjectService.projectAdd(pj,token,planStartDate,planEndDate,attids,pd_dx,pjTeamJson);
//		} catch (Exception e) {
//			e.printStackTrace();
//			ResponseFormat.retParam(1,1000,e.getMessage());
//		}
//		return jsonBean;
//    }
	
	/**
	 * 项目管理列表
	 */
    @OperationLog(
            success = "项目管理列表",
            busType = "智能审计",
            fail = "项目管理列表",
            operationType = OperationType.SELECT,
            subType = "项目管理——获取项目管理列表信息"
    )
	@GetMapping("/xmgl/project_list")
	@Operation(summary = "项目管理列表")
	public JsonBean project_list(HttpServletRequest request, TblnbsjProjectVo tblnbsjProjectVo,
								 @Parameter(name = "sortFields", description = "排序字段", required = false) @RequestParam(value="sortFields", required = false) String sortFields,
								 @Parameter(name = "sortFlag", description = "排序标记asc|desc", required = false) @RequestParam(value="sortFlag", required = false) String sortFlag,
								 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.projectPageList(token, pageNumber, pageSize,tblnbsjProjectVo,sortFields,sortFlag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	/**
	 * 项目管理-新增与修改
	 */
	@OperationLog(
			success = "项目管理新增",
			busType = "智能审计",
			fail = "项目管理新增",
			operationType = OperationType.ADD,
			subType = "项目管理-项目列表管理"
	)
	@RequestMapping(value = "/xmgl/project_add", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "项目管理-新增与修改")
    public JsonBean project_add(HttpServletRequest request,@Parameter(name = "pj", description = "实体", required = false)TblNbsjProject pj,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "planStartDate", description = "计划开始时间", required = false)@RequestParam(value = "planStartDate", required = false) String planStartDate,
			 @Parameter(name = "planEndDate", description = "计划结束时间", required = false)@RequestParam(value = "planEndDate", required = false) String planEndDate,
			 @Parameter(name = "attids", description = "附件id数组", required = false)@RequestParam(value = "attids", required = false) String attids,
			 @Parameter(name="pd_dx",description="pd_dx",required=false)@RequestParam(value = "pd_dx", required = false) String pd_dx,
			 @Parameter(name = "pjTeamJson", description = "项目小组json字符串", required = false)@RequestParam(value = "pjTeamJson", required = false) String pjTeamJson)throws Exception{
		JsonBean jsonBean = null;
		try {
			
			jsonBean = this.tblnbsjProjectService.projectAdd(pj,token,planStartDate,planEndDate,attids,pd_dx,pjTeamJson);
			
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	
	
	
	/**
	 * 查询项目符合的计划信息
	 */
	@OperationLog(
			success = "项目管理-符合项目条件的计划",
			busType = "智能审计",
			fail = "项目管理-符合项目条件的计划",
			operationType = OperationType.SELECT,
			subType = "项目管理-查询符合项目条件的计划"
	)
	@GetMapping("/xmgl/project_plan_list")
	@Operation(summary = "项目管理-符合项目条件的计划")
	public JsonBean project_plan_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "projectId", description = "projectId", required = false) @RequestParam(value = "projectId", required = false) BigDecimal projectId,
			@Parameter(name = "planId", description = "planId", required = false) @RequestParam(value = "planId", required = false) BigDecimal planId) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblnbsjProjectService.getProjectPlanList(token,projectId,planId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	/**
	 * 项目管理-附件列表
	 */
	@OperationLog(
			success = "项目管理-项目附件",
			busType = "智能审计",
			fail = "项目管理-项目附件",
			operationType = OperationType.SELECT,
			subType = "项目管理-获取项目附件列表"
	)
    @GetMapping("/xmgl/project_file_list")
	@Operation(summary = "项目管理-附件列表")
	public JsonBean project_file_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "projectId", description = "业务主键", required = false) @RequestParam(value = "projectId", required = false) BigDecimal projectId) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblAttachmentService.projectFileList(token,projectId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    /**
     * 项目管理-附件删除
     */
	@OperationLog(
			success = "项目管理-附件删除",
			busType = "智能审计",
			fail = "项目管理-附件删除",
			operationType = OperationType.DELETE,
			subType = "项目管理-项目附件删除"
	)
    @GetMapping("/xmgl/project_file_del")
    @Operation(summary = "项目管理-附件删除")
    public R project_file_del(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
    	return this.tblnbsjProjectService.removeAttInfoByAttId(token, attId);
    }
	
	/**
	 * 项目管理-删除
	 */
	@OperationLog(
			success = "删除项目",
			busType = "智能审计",
			fail = "删除项目",
			operationType = OperationType.DELETE,
			subType = "项目管理-项目附件删除"
	)
	@GetMapping("/xmgl/project_del")
	@Operation(summary = "项目管理-删除")
    public JsonBean project_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectid", description = "主键", required = true)@RequestParam(value = "projectid", required = true) BigDecimal projectid) {
        
        try {
			return tblnbsjProjectService.projectDelete(projectid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	/**
	 * 项目管理-明细
	 */
	@OperationLog(
			success = "项目明细",
			busType = "智能审计",
			fail = "项目明细",
			operationType = OperationType.DELETE,
			subType = "项目管理-获取项目详细内容"
	)
	@GetMapping("/xmgl/project_detail")
    @Operation(summary = "项目管理-明细")
    public JsonBean project_detail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectid", description = "主键", required = true)@RequestParam(value = "projectid", required = false) BigDecimal projectid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblnbsjProjectService.findProjectDetail(token,projectid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	/**
	 * 获取当前实施项目
	 */
	@OperationLog(
			success = "实施项目",
			busType = "智能审计",
			fail = "实施项目",
			operationType = OperationType.SELECT,
			subType = "获取用户当前实施项目"
	)
	@GetMapping("/xmgl/curr_ss_project")
    @Operation(summary = "获取当前实施项目")
    public JsonBean curr_ss_project(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblnbsjProjectService.findProjectDetail(token,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	/**
	 * 项目实施
	 */
	@OperationLog(
			success = "项目实施",
			busType = "智能审计",
			fail = "项目实施",
			operationType = OperationType.UPDATE,
			subType = "项目管理——项目实施"
	)
	@GetMapping("/xmgl/xmproject_plan_cycurr")
    @Operation(summary = "项目实施")
    public JsonBean xmproject_plan_cycurr(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectid", description = "主键", required = true)@RequestParam(value = "projectid", required = true) BigDecimal projectid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblnbsjProjectService.projectSS(token,projectid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	/**
	 * 项目小组列表
	 */
	@OperationLog(
			success = "项目小组",
			busType = "智能审计",
			fail = "项目小组",
			operationType = OperationType.SELECT,
			subType = "项目管理——获取项目小组列表详"
	)
	@GetMapping("/xmgl/project_pjteam_list")
    @Operation(summary = "项目管理-项目小组列表")
    public JsonBean project_pjteam_list(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectid", description = "主键", required = false)@RequestParam(value = "projectid", required = false) BigDecimal projectid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjProjectteamService.findProjectteamList(token,projectid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	/**
	 * 项目管理-新增修改页面，获取统计类型维护List
	 */
	@OperationLog(
			success = "统计类型维护列表",
			busType = "智能审计",
			fail = "统计类型维护列表",
			operationType = OperationType.SELECT,
			subType = "项目管理-新增修改页面，获取统计类型维护List"
	)
	@RequestMapping(value = "/xmgl/question_type_list", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "项目管理-新增修改页面，获取统计类型维护List")
    public JsonBean question_type_list(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjQuestionTypeService.getNbsjStatTypeListPage(token,1,999);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }

	@OperationLog(
			success = "获取审计计划集合",
			busType = "智能审计",
			fail = "获取审计计划集合",
			operationType = OperationType.SELECT,
			subType = "项目管理-新增修改页面获取审计计划集合"
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
			success = "获取审计计划集合",
			busType = "智能审计",
			fail = "获取审计计划集合",
			operationType = OperationType.SELECT,
			subType = "项目管理-新增修改页面获取所选计划的计划项目"
	)
	@RequestMapping(value = "/getPlanProjectListByPlanId", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "项目管理-新增修改获取所选计划的计划项目")
	public JsonBean getPlanProjectListByPlanId(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "planId", description = "计划主键 ", required = true) @RequestParam(value = "planId", required = false) BigDecimal planId)throws Exception{
		 JsonBean jsonBean = null;
			try {
//				jsonBean = this.tblNbsjPlanProjectService.getPlanProjectListByPlanIdPageInfo(token,project);
				jsonBean = this.tblNbsjPlanProjectService.findPlanProjectListInfoByPlanId(token,planId);
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	}
	@OperationLog(
			success = "获取时间",
			busType = "智能审计",
			fail = "获取时间",
			operationType = OperationType.SELECT,
			subType = "项目管理-新增修改页面获取审计计划后获取审计开始和结束时间"
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
			success = "获取审计类型",
			busType = "智能审计",
			fail = "获取审计类型",
			operationType = OperationType.SELECT,
			subType = "项目管理-新增修改页面获取审计类型集合"
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
			success = "获取公司菜单",
			busType = "智能审计",
			fail = "获取公司菜单",
			operationType = OperationType.SELECT,
			subType = "项目管理-新增修改页获取审计对象的公司树形菜单"
	)
	@GetMapping(value = "/getOrgTreeListByAuditObj")
    @Operation(summary = "项目管理-新增修改获取审计对象的公司树形菜单")
	public JsonBean getOrgTreeListByAuditObj( HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token) throws Exception{
    	
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblOrganizaService.getOrgTreeListByAuditObj(token,null);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}


	@OperationLog(
			success = "获取审计模板",
			busType = "智能审计",
			fail = "获取审计模板",
			operationType = OperationType.SELECT,
			subType = "智能审计——查询获取审计模板/指引模板列表"
	)
	@RequestMapping(value = "/getNbsjTempleteList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模板/指引模板")
    public JsonBean getNbsjTempleteList(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
   		@Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
   		@Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
   		TblNbsjTempleteVo tblNbsjTempleteVo) {
    	JsonBean jsonBean = null;
    	try {
//    		tblNbsjTempleteVo.setTempType("0");
			jsonBean = tblNbsjTempleteService.selectNbsjTempleteListByPageInfo(token,pageNumber,pageSize,tblNbsjTempleteVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	
	/**
	 * 项目任务分配
	 */
	@OperationLog(
			success = "任务分配",
			busType = "智能审计",
			fail = "任务分配",
			operationType = OperationType.SELECT,
			subType = "项目管理——任务分配——查询获取项目任务分配列表"
	)
	@GetMapping("/xmgl/proj_role_list_xmgl")
	@Operation(summary = "项目任务分配列表")
	public JsonBean proj_role_list_xmgl(HttpServletRequest request, TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo,
										@Parameter(name = "sortFields", description = "排序字段", required = false) @RequestParam(value="sortFields", required = false) String sortFields,
										@Parameter(name = "sortFlag", description = "排序标记asc|desc", required = false) @RequestParam(value="sortFlag", required = false) String sortFlag,
										@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.projectRwfpPageList(token, pageNumber, pageSize,tblnbsjProjectRwfpVo,sortFields,sortFlag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	/**
	 * 任务分配-列表
	 */
	@OperationLog(
			success = "任务分配",
			busType = "智能审计",
			fail = "任务分配",
			operationType = OperationType.SELECT,
			subType = "项目管理——任务分配——查询获取任务分配——任务分配列表【{{projectid}}】"
	)
	@GetMapping("/xmgl/proj_rw_list")
	@Operation(summary = "任务分配-列表")
	public JsonBean proj_rw_list(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "projectid", description = "项目id", required = false) @RequestParam(value = "projectid", required = false) BigDecimal projectid,
			@Parameter(name="targetId",description="targetId",required=false) @RequestParam(value = "targetId", required = false) BigDecimal targetId) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjOperateService.projRwList(token, pageNumber, pageSize,projectid,targetId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 查询项目下的所有小组成员
	 */
	@OperationLog(
			success = "查询小组成员",
			busType = "智能审计",
			fail = "查询小组成员",
			operationType = OperationType.SELECT,
			subType = "项目管理——任务分配——查询项目下的所有小组成员"
	)
	@GetMapping("/xmgl/proj_pjteam_usr_list")
    @Operation(summary = "查询项目下的所有小组成员")
    public JsonBean proj_pjteam_usr_list(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectid", description = "项目id", required = true)@RequestParam(value = "projectid", required = true) BigDecimal projectid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjProjectteamService.findProjectteamUsrList(token,projectid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	/**
	 * 查询项目下的所有小组成员+项目主审
	 */
	@OperationLog(
			success = "查询小组成员+项目主审",
			busType = "智能审计",
			fail = "查询小组成员+项目主审",
			operationType = OperationType.SELECT,
			subType = "项目管理——任务分配——查询项目下的所有小组成员+项目主审"
	)
	@GetMapping("/xmgl/pjteam_usr_list")
    @Operation(summary = "查询项目下的所有小组成员+项目主审")
    public JsonBean pjteam_usr_list(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectid", description = "项目id", required = true)@RequestParam(value = "projectid", required = true) BigDecimal projectid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjProjectteamService.findProjectUsrList(token,projectid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	/**
	 * 分配-选择人员保存
	 */
	@OperationLog(
			success = "人员保存",
			busType = "智能审计",
			fail = "人员保存",
			operationType = OperationType.UPDATE,
			subType = "项目管理——任务分配——选择人员保存"
	)
	@RequestMapping(value = "/xmgl/jsfp_role_manage_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "分配-选择人员保存")
    public JsonBean jsfp_role_manage_save(HttpServletRequest request,
    		@Parameter(name="ids",description="ids",required=false)@RequestParam(value = "ids", required = false)String ids,
    		@Parameter(name="teamStaffId",description="teamStaffId",required=false)@RequestParam(value = "teamStaffId", required = false)BigDecimal teamStaffId,
    		@Parameter(name="projectId",description="projectId",required=false)@RequestParam(value = "projectId", required = false)BigDecimal projectId,
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		JsonBean jsonBean = null;
		try { 
			jsonBean = this.tblnbsjProjectService.jsfpRoleManageSave(token, ids, teamStaffId, projectId);
			
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
			e.printStackTrace();
		}
		return jsonBean;
    }
	
	/**
	 * 通过小组id，查询小组信息
	 */
	@OperationLog(
			success = "小组信息",
			busType = "智能审计",
			fail = "小组信息",
			operationType = OperationType.SELECT,
			subType = "项目管理——任务分配——通过小组id，查询小组信息"
	)
	@GetMapping("/xmgl/proj_pjteam_info")
    @Operation(summary = "通过小组id，查询小组信息")
    public JsonBean proj_pjteam_info(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "teamId", description = "主键", required = true)@RequestParam(value = "teamId", required = true) BigDecimal teamId) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjProjectteamService.getPjteamInfoById(token,teamId);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	/**
	 * 小组信息--新增修改
	 */
	@OperationLog(
			success = "修改小组信息",
			busType = "智能审计",
			fail = "修改小组信息",
			operationType = OperationType.ADD,
			subType = "项目管理——任务分配——修改小组信息"
	)
	@RequestMapping(value = "/xmgl/proj_pjteam_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "修改小组信息")
    public JsonBean proj_pjteam_add(HttpServletRequest request,
    		@Parameter(name = "team", description = "实体", required = false)TblNbsjProjectTeamEntity team,
//    		@Parameter(name = "zystaffids", description = "组员ids（用,分隔）", required = false)String zystaffids,
//    		@Parameter(name = "leaderid", description = "组长id", required = false)BigDecimal leaderid,
//    		@Parameter(name = "projectid", description = "项目id", required = false)BigDecimal projectid,
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjProjectteamService.pjItemAdd(token,team);
			
		} catch (Exception e) {
			
			//修改异常！则新增小组
			team.setTeamId(null);
			jsonBean = this.tblNbsjProjectteamService.pjItemAdd(token,team);
			
			ResponseFormat.retParam(0,1000,e.getMessage());
		}
		return jsonBean;
    }
	
	/**
	 * 通过小组id，删除小组信息
	 */
	@OperationLog(
			success = "小组删除",
			busType = "智能审计",
			fail = "小组删除",
			operationType = OperationType.DELETE,
			subType = "项目管理——任务分配——通过小组id，删除小组信息"
	)
	@GetMapping("/xmgl/proj_pjteam_del")
    @Operation(summary = "通过小组id，删除小组信息")
    public JsonBean proj_pjteam_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "teamId", description = "主键", required = true)@RequestParam(value = "teamId", required = true) BigDecimal teamId) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjProjectteamService.pjItemDelete(token,teamId);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	/**
	 * 修改项目经理
	 */
	@OperationLog(
			success = "修改项目经理",
			busType = "智能审计",
			fail = "修改项目经理",
			operationType = OperationType.UPDATE,
			subType = "项目管理——根据项目id，修改项目经理"
	)
	@RequestMapping(value = "/xmgl/proj_pm_modi", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "修改项目经理")
    public JsonBean proj_pm_modi(HttpServletRequest request,
    		@Parameter(name = "pmId", description = "项目经理id", required = false)@RequestParam(value = "pmId", required = false)BigDecimal pmId,
    		@Parameter(name = "projectid", description = "项目id", required = false)@RequestParam(value = "projectid", required = false)BigDecimal projectid,
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblnbsjProjectService.pjPmModi(token,pmId,projectid);
			
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	/**
	 * 启动
	 */
	@OperationLog(
			success = "启动项目",
			busType = "智能审计",
			fail = "启动项目",
			operationType = OperationType.UPDATE,
			subType = "项目管理——根据项目id，启动项目"
	)
	@RequestMapping(value = "/xmgl/proj_pj_start", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "启动")
    public JsonBean proj_pj_start(HttpServletRequest request,
    		@Parameter(name = "projectid", description = "项目id", required = false)BigDecimal projectid,
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblnbsjProjectService.pjStart(token,projectid);
			
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	
	/**
	 * 项目执行一览
	 */
	@OperationLog(
			success = "项目执行一览",
			busType = "智能审计",
			fail = "项目执行一览",
			operationType = OperationType.SELECT,
			subType = "项目管理——获取项目执行一览列表"
	)
	@GetMapping("/sjgl/project_run_month")
	@Operation(summary = "项目执行一览列表")
	public JsonBean project_run_month(HttpServletRequest request, TblnbsjProjectZXYLVo tblnbsjProjectZXYLVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.projectZxylPageList(token, pageNumber, pageSize,tblnbsjProjectZXYLVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	
	/**
	 * 选择人员-左侧部门
	 */
	@OperationLog(
			success = "获取部门",
			busType = "智能审计",
			fail = "获取部门",
			operationType = OperationType.SELECT,
			subType = "智能审计——选择人员-左侧部门"
	)
	@RequestMapping(value = "/htdl/findOrganizationByTreeAllss", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "选择人员-左侧部门")
	public @ResponseBody String htdlfindOrganizationByTree(@Parameter(name = "nodeId", description = "nodeId", required = false)BigDecimal nodeId,
														   @Parameter(name = "type", description = "type", required = false)String type,
														   @Parameter(name = "orgId", description = "orgId", required = false)BigDecimal orgId,
														   @Parameter(name = "idname", description = "idname", required = false)String idname,
														   @Parameter(name = "textname", description = "textname", required = false)String textname,
														   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
														   @Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId,
														   HttpServletRequest request) throws Exception {
		String json = "";
		if (null == nodeId) {
			nodeId = orgId;
			if (null == orgId) {
				TblStaffUtil user = userProvider.get();
				nodeId = user.getCurrentOrg().getOrgid();
			}
		}
		if (StringUtils.isNotBlank(type)) {
			List<Tree> list = this.tblOrganizaService.getTrees(nodeId);
			for (Tree tree : list) {
				if (!tree.getIsParent()) {
					tree.setTarget("mainFramex");
					tree.setUrl("/nbkz/pjlx/list?pid=" + tree.getId() + "&idname=" + idname + "&textname=" + textname);
				}
			}
			json = JSONObject.toJSONString(list);
		} else {
			List<Tree> list = this.tblOrganizaService.getNodeAlls(nodeId);
			for (Tree tree : list) {
				setUrlByTree(tree, "/nbkz/pjlx/list?idname=" + idname + "&textname=" + textname + "&pid=");
			}
			json = JSONObject.toJSONString(list);
		}
		return json;
	}
	private void setUrlByTree(Tree tree, String url) {
		for (Tree tre : tree.getChildren()) {
			/// if (!tre.getIsParent()) {
			tre.setTarget("mainFramex");
			tre.setUrl(url + tre.getId());
			// }
			if (tre.getChildren().size() > 0) {
				setUrlByTree(tre, url);
			}
		}
	}
	/**
	 * 选择人员-人员列表
	 */
	@OperationLog(
			success = "人员列表",
			busType = "智能审计",
			fail = "人员列表",
			operationType = OperationType.SELECT,
			subType = "智能审计——选择人员-人员列表"
	)
	@RequestMapping(value = "/htdl/list", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "选择人员-人员列表")
	public String htdluserListss(HttpServletRequest request,
									   @Parameter(name = "pid", description = "pid", required = true)@RequestParam(value = "pid", required = false)String pid,
									   @Parameter(description = "username", required = false)@RequestParam(value = "username", required = false)String username,
									   @Parameter(description = "ralename", required = false)@RequestParam(value = "ralename", required = false)String ralename,
									   @Parameter(description="pageNumber",required=false)@RequestParam(value = "pageNumber", required = false)Integer pageNumber,
									   @Parameter(description="pageSize",required=false)@RequestParam(value = "pageSize", required = false)Integer pageSize,
									   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									   @Parameter(name = "staffId", description = "登录用户主键", required = false)@RequestParam(value = "staffId", required = false)BigDecimal staffId) {
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblStaffService.findAllPageBeanPid(username,ralename,pid,pageNumber,pageSize,token,staffId);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 查询人员明细
	 */
	@OperationLog(
			success = "人员明细",
			busType = "智能审计",
			fail = "人员明细",
			operationType = OperationType.SELECT,
			subType = "智能审计——查询人员明细"
	)
	@GetMapping("/htdl/usr_detail")
    @Operation(summary = "查询人员明细")
    public JsonBean usr_detail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "staffid", description = "主键", required = true)@RequestParam(value = "staffid", required = true) BigDecimal staffid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblStaffService.findUsrDetail(token,staffid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	/**
	 * 查询审计模板下业务单元数量
	 */
	@OperationLog(
			success = "业务单元数量",
			busType = "智能审计",
			fail = "业务单元数量",
			operationType = OperationType.SELECT,
			subType = "项目管理——查询审计模板下业务单元数量"
	)
	@GetMapping("/xmgl/tempele_biz_cnt")
    @Operation(summary = "查询审计模板下业务单元数量")
    public JsonBean tempele_biz_cnt(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name="templeteId",description="templeteId",required=true)@RequestParam(value = "templeteId", required = true) BigDecimal templeteId) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblnbsjProjectService.getTempeleBizCnt(token,templeteId);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }

	/**
	 * 项目数趋势变化查询(最近12个月)
	 */
	@OperationLog(
			success = "项目数趋势变化",
			busType = "智能审计",
			fail = "项目数趋势变化",
			operationType = OperationType.SELECT,
			subType = "审计分析——项目数趋势变化查询(最近12个月)"
	)
	@GetMapping("/sjfx/projectTrend")
	@Operation(summary = "项目数趋势变化查询(最近12个月)")
	public JsonBean projectTrend(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.selectProjectTrend(token);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0, "查询失败", e);
		}
		return jsonBean;
	}

	/**
	 * 整改问题状态统计查询
	 */
	@OperationLog(
			success = "整改问题状态统计",
			busType = "智能审计",
			fail = "整改问题状态统计",
			operationType = OperationType.SELECT,
			subType = "审计分析——整改问题状态统计查询"
	)
	@GetMapping("/sjfx/issuesStatusStatistics")
	@Operation(summary = "整改问题状态统计查询")
	public JsonBean issuesStatusStatistics(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.selectIssuesStatusStatistics(token);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0, "查询失败", e);
		}
		return jsonBean;
	}


}
