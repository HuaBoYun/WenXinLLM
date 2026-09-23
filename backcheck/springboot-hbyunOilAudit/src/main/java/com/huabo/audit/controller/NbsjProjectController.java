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
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.Tree;
import com.hbfk.util.user.UserProvider;
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
	 * 审计分析-查询条件年度查询
	 */
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
	
	
	
	
	/**
	 * 项目管理列表
	 */
	@GetMapping("/xmgl/project_list")
	@Operation(summary = "项目管理列表")
	public JsonBean project_list(HttpServletRequest request, TblnbsjProjectVo tblnbsjProjectVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.projectPageList(token, pageNumber, pageSize,tblnbsjProjectVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	/**
	 * 项目管理-新增与修改
	 */
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
			
//			pjTeamJson = "[{\"teamName\":\"小组1\",\"leaderid\":\"794458\",\"projectid\":\"797127\",\"teamId\":\"797128\"}]";
			
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
	@GetMapping("/xmgl/project_plan_list")
	@Operation(summary = "项目管理-符合项目条件的计划")
	public JsonBean project_plan_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "projectId", description = "projectId", required = false) @RequestParam(value = "projectId", required = false) Integer projectId,
			@Parameter(name = "planId", description = "planId", required = false) @RequestParam(value = "planId", required = false) Integer planId) {

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
    @GetMapping("/xmgl/project_file_list")
	@Operation(summary = "项目管理-附件列表")
	public JsonBean project_file_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "projectId", description = "业务主键", required = false) @RequestParam(value = "projectId", required = false) Integer projectId) {

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
	@GetMapping("/xmgl/project_del")
	@Operation(summary = "项目管理-删除")
    public JsonBean project_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectid", description = "主键", required = true)@RequestParam(value = "projectid", required = true) Integer projectid) {
        
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
	@GetMapping("/xmgl/project_detail")
    @Operation(summary = "项目管理-明细")
    public JsonBean project_detail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectid", description = "主键", required = true)@RequestParam(value = "projectid", required = false) Integer projectid) {
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
	@GetMapping("/xmgl/xmproject_plan_cycurr")
    @Operation(summary = "项目实施")
    public JsonBean xmproject_plan_cycurr(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectid", description = "主键", required = true)@RequestParam(value = "projectid", required = true) Integer projectid) {
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
	@GetMapping("/xmgl/project_pjteam_list")
    @Operation(summary = "项目管理-项目小组列表")
    public JsonBean project_pjteam_list(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectid", description = "主键", required = false)@RequestParam(value = "projectid", required = false) Integer projectid) {
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
	
	@RequestMapping(value = "/getPlanProjectListByPlanId", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "项目管理-新增修改获取所选计划的计划项目")
	public JsonBean getPlanProjectListByPlanId(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "planId", description = "计划主键 ", required = true) @RequestParam(value = "planId", required = false) Integer planId)throws Exception{
		 JsonBean jsonBean = null;
			try {
//				jsonBean = this.tblNbsjPlanProjectService.getPlanProjectListByPlanIdPageInfo(token,project);
				jsonBean = this.tblNbsjPlanProjectService.findPlanProjectListInfoByPlanId(token,planId);
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
	}
	
	@RequestMapping(value = "/getNbsjAuditPlanDateInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "项目管理-新增修改页面获取审计计划后获取审计开始和结束时间")
    public JsonBean getNbsjAuditPlanDateInfo(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "planId", description = "审计计划主键", required = true)@RequestParam(value = "planId", required = true)Integer planId) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = this.tblNbsjAuditplanService.getNbsjAuditPlanDateInfo(token,planId);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
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
	@GetMapping("/xmgl/proj_role_list_xmgl")
	@Operation(summary = "项目任务分配列表")
	public JsonBean proj_role_list_xmgl(HttpServletRequest request, TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblnbsjProjectService.projectRwfpPageList(token, pageNumber, pageSize,tblnbsjProjectRwfpVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 任务分配-列表
	 */
	@GetMapping("/xmgl/proj_rw_list")
	@Operation(summary = "任务分配-列表")
	public JsonBean proj_rw_list(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "projectid", description = "项目id", required = false) @RequestParam(value = "projectid", required = false) Integer projectid,
			@Parameter(name="targetId",description="targetId",required=false) @RequestParam(value = "targetId", required = false) Integer targetId) {

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
	@GetMapping("/xmgl/proj_pjteam_usr_list")
    @Operation(summary = "查询项目下的所有小组成员")
    public JsonBean proj_pjteam_usr_list(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectid", description = "项目id", required = true)@RequestParam(value = "projectid", required = true) Integer projectid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjProjectteamService.findProjectteamUsrList(token,projectid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	/**
	 * 分配-选择人员保存
	 */
	@RequestMapping(value = "/xmgl/jsfp_role_manage_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "分配-选择人员保存")
    public JsonBean jsfp_role_manage_save(HttpServletRequest request,
    		@Parameter(name="ids",description="ids",required=false)@RequestParam(value = "ids", required = false)String ids,
    		@Parameter(name="teamStaffId",description="teamStaffId",required=false)@RequestParam(value = "teamStaffId", required = false)Integer teamStaffId,
    		@Parameter(name="projectId",description="projectId",required=false)@RequestParam(value = "projectId", required = false)Integer projectId,
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
	@GetMapping("/xmgl/proj_pjteam_info")
    @Operation(summary = "通过小组id，查询小组信息")
    public JsonBean proj_pjteam_info(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "teamId", description = "主键", required = true)@RequestParam(value = "teamId", required = true) Integer teamId) {
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
	@RequestMapping(value = "/xmgl/proj_pjteam_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "小组信息--新增修改")
    public JsonBean proj_pjteam_add(HttpServletRequest request,
    		@Parameter(name = "team", description = "实体", required = false)TblNbsjProjectTeamEntity team,
    		@Parameter(name = "zystaffids", description = "组员ids（用,分隔）", required = false)String zystaffids,
    		@Parameter(name = "leaderid", description = "组长id", required = false)Integer leaderid,
    		@Parameter(name = "projectid", description = "项目id", required = false)Integer projectid,
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjProjectteamService.pjItemAdd(token,team,zystaffids,leaderid,projectid);
			
		} catch (Exception e) {
			
			//修改异常！则新增小组
			team.setTeamId(null);
			jsonBean = this.tblNbsjProjectteamService.pjItemAdd(token,team,zystaffids,leaderid,projectid);
			
			ResponseFormat.retParam(0,1000,e.getMessage());
		}
		return jsonBean;
    }
	
	/**
	 * 通过小组id，删除小组信息
	 */
	@GetMapping("/xmgl/proj_pjteam_del")
    @Operation(summary = "通过小组id，删除小组信息")
    public JsonBean proj_pjteam_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "teamId", description = "主键", required = true)@RequestParam(value = "teamId", required = true) Integer teamId) {
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
	@RequestMapping(value = "/xmgl/proj_pm_modi", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "修改项目经理")
    public JsonBean proj_pm_modi(HttpServletRequest request,
    		@Parameter(name = "pmId", description = "项目经理id", required = false)@RequestParam(value = "pmId", required = false)BigDecimal pmId,
    		@Parameter(name = "projectid", description = "项目id", required = false)@RequestParam(value = "projectid", required = false)Integer projectid,
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblnbsjProjectService.pjPmModi(token,pmId,projectid);
			
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	/**
	 * 启动
	 */
	@RequestMapping(value = "/xmgl/proj_pj_start", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "启动")
    public JsonBean proj_pj_start(HttpServletRequest request,
    		@Parameter(name = "projectid", description = "项目id", required = false)Integer projectid,
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
	@RequestMapping(value = "/htdl/list", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "选择人员-人员列表")
	public String htdluserListss(HttpServletRequest request,
									   @Parameter(name = "pid", description = "pid", required = true)@RequestParam(value = "pid", required = false)String pid,
									   @Parameter(description = "username", required = false)@RequestParam(value = "username", required = false)String username,
									   @Parameter(description = "ralename", required = false)@RequestParam(value = "ralename", required = false)String ralename,
									   @Parameter(description="pageNumber",required=false)@RequestParam(value = "pageNumber", required = false)Integer pageNumber,
									   @Parameter(description="pageSize",required=false)@RequestParam(value = "pageSize", required = false)Integer pageSize,
									   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									   @Parameter(name = "staffId", description = "登录用户主键", required = false)@RequestParam(value = "staffId", required = false)String staffId) {
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
	@GetMapping("/htdl/usr_detail")
    @Operation(summary = "查询人员明细")
    public JsonBean usr_detail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "staffid", description = "主键", required = true)@RequestParam(value = "staffid", required = true) Integer staffid) {
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
	@GetMapping("/xmgl/tempele_biz_cnt")
    @Operation(summary = "查询审计模板下业务单元数量")
    public JsonBean tempele_biz_cnt(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name="templeteId",description="templeteId",required=true)@RequestParam(value = "templeteId", required = true) Integer templeteId) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblnbsjProjectService.getTempeleBizCnt(token,templeteId);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	
}
