package com.huabo.audit.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjStaffscore;
import com.huabo.audit.oracle.entity.TblNbsjStaffscoreDetails;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.TblNbsjStaffscoreDetailsMapper;
import com.huabo.audit.oracle.vo.TblnbsjProjectVo;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjStaffscoreService;
import com.huabo.audit.service.TblStaffService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 人员评分控制器
 * <p>提供审计人员评分的查询、评分、统计等管理接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="人员评分",description="人员评分")
@RequestMapping(value = "/audit/Staffscore")
public class TblNbsjStaffscoreController {

    @Autowired
    TblNbsjStaffscoreService tblNbsjStaffscoreService;

    @Autowired
    TblNbsjStaffscoreDetailsMapper tblNbsjStaffscoreDetailsMapper;
    
    @Autowired
    TblStaffService staffservice;
    
    @Autowired
    TblNbsjProjectService projectService;



	@OperationLog(
			success = "审计对象库选择人项目",
			busType = "智能审计",
			fail = "审计对象库选择人项目",
			operationType = OperationType.SELECT,
			subType = "基础配置——审计对象库——选择人项目"
	)
    @GetMapping("/getprojectLisbystaffid")
    @Operation(summary = "审计对象库-选择人项目")
    public JsonBean getprojectLisbystaffid(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
   		@Parameter(name = "staffid", description = "选择的人员id", required = false)@RequestParam(value = "staffid", required = false) BigDecimal staffid,
   		TblnbsjProjectVo project) {
    	JsonBean jsonBean = null;
    	try {
    		if(staffid!=null) {
    			project.setStaffId(staffid);
    		}
			jsonBean = projectService.getprojectLisbystaffidt(token,project);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,1000,e.getMessage());
		}
        return jsonBean;
    }

	@OperationLog(
			success = "评价管理选择人项目",
			busType = "智能审计",
			fail = "评价管理选择人项目",
			operationType = OperationType.SELECT,
			subType = "基础配置——评价管理——选择人项目"
	)
    @GetMapping("/getpjprojectList")
    @Operation(summary = "评价管理-选择人项目")
    public JsonBean getNbsjProjectPageList(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
   		@Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
   		@Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
   		@Parameter(name = "projectStartDate", description = "项目开始时间 格式年-月-日", required = false)@RequestParam(value = "projectStartDate", required = false) String projectStartDate,
   		@Parameter(name = "projectEndDate", description = "项目结束时间 格式年-月-日", required = false)@RequestParam(value = "projectEndDate", required = false) String projectEndDate,
   		@Parameter(name = "staffid", description = "选择的人员id", required = false)@RequestParam(value = "staffid", required = false) BigDecimal staffid,
   		TblnbsjProjectVo project) {
    	JsonBean jsonBean = null;
    	try {
    		if(staffid!=null) {
    			project.setStaffId(staffid);
    		}
			jsonBean = projectService.getNbsjProjecGdtPageList(token,pageNumber,pageSize,project,projectStartDate,projectEndDate);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,1000,e.getMessage());
		}
        return jsonBean;
    }


	@OperationLog(
			success = "选择人员分页",
			busType = "智能审计",
			fail = "选择人员分页",
			operationType = OperationType.SELECT,
			subType = "基础配置——评价管理——选择人员分页"
	)
    @GetMapping("/getpjuserList")
    @Operation(summary = "评价管理-选择人员分页")
    public JsonBean getpjuserList(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
   		@Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
   		@Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
   		@Parameter(name = "username", description = "用户名", required = false) @RequestParam(value = "username", required = false) String username,
   	    @Parameter(name = "realName", description = "真实姓名", required = false) @RequestParam(value = "realName", required = false) String realName,
   	    @Parameter(name = "major", description = "专业", required = false) @RequestParam(value="major",required=false)String major,
		@Parameter(name = "education", description = "学历", required = false) @RequestParam(value="education",required=false)String education,
		@Parameter(name = "jobExperiences", description = "经验", required = false) @RequestParam(value="jobExperiences",required=false)String jobExperiences,
		@Parameter(name = "jobName", description = "岗位", required = false) @RequestParam(value = "jobName",required = false)String jobName)
	{
    	JsonBean jsonBean = null;
    	try {
    		 TblStaff staff=new TblStaff();
    		staff.setRealname(realName);
			staff.setUsername(username);
    		staff.setMajor(major);
    		staff.setEducation(education);
    		staff.setJobName(jobName);
    		staff.setJobexperiences(jobExperiences);
			jsonBean =staffservice.findByAllPageBean(username, pageNumber, pageSize, token, staff);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,1000,e.getMessage());
		}
        return jsonBean;
    }


	@OperationLog(
			success = "评分列表页",
			busType = "智能审计",
			fail = "评分列表页",
			operationType = OperationType.SELECT,
			subType = "获基础配置——评价管理——获取评分列表信息"
	)
    @GetMapping("/getscoreList")
    @Operation(summary = "评价管理-评分列表分页")
    public JsonBean getscoreList(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
   		@Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
   		@Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
   		@Parameter(name = "staffid", description = "关联人员id", required = false) @RequestParam(value = "staffid", required = false) BigDecimal staffid,
   	    @Parameter(name = "projectname", description = "项目名称", required = false) @RequestParam(value = "projectname", required = false) String projectname) {
    	JsonBean jsonBean = null;
    	try {
    		TblNbsjStaffscore re=new TblNbsjStaffscore();
    		re.setAuditors(staffid);
    		re.setAuditProjectName(projectname);
			jsonBean =tblNbsjStaffscoreService.findAll(re, pageNumber, pageSize, token);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(0,1000,e.getMessage());
		}
        return jsonBean;
    }

	@OperationLog(
			success = "列表记录详情",
			busType = "智能审计",
			fail = "列表记录详情",
			operationType = OperationType.SELECT,
			subType = "基础配置——评价管理——获取评分列表指定记录详细信息"
	)
    @GetMapping("/getScoreDetail")
    @Operation(summary = "评价管理-查看详情信息")
    public JsonBean getAuditPlanInfo(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "staffScoreid", description = "评价主键id", required = true)@RequestParam(value = "staffScoreid", required = true) BigDecimal staffScoreid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjStaffscoreService.getAttListByStaffScoreid(token, staffScoreid);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(0,1000,e.getMessage());
		}
        return jsonBean;
    }

	@OperationLog(
			success = "列表记录新增修改",
			busType = "智能审计",
			fail = "列表记录新增修改",
			operationType = OperationType.ADD,
			subType = "基础配置——评价管理——评分列表记录新增修改"
	)
    @RequestMapping(value = "/mergeScoreInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
   	@Operation(summary = "评价管理-新增或修改")
   	public JsonBean mergePlanProjectManageInfoList(HttpServletRequest request,
   			 @Parameter(name = "staffScoreid", description = "评价主键 ，如果主键为空则新增信息", required = false) BigDecimal staffScoreid,
   			@Parameter(name = "score", description = "评价实体", required = true)TblNbsjStaffscore re,
   			@Parameter(name = "detailListJson", description = "评分详情", required = true)@RequestParam(value = "detailListJson", required = true) String detailListJson,
   			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
   		 JsonBean jsonBean = null;
   			try {
//   				detailListJson="[{\"project\":\"参与情况\",\"consideration\":\"1\",\"grading\":\"1\",\"auditTeamLeaderScore\":\"1\",\"remark\":\"1\",\"staffScore_details_id\":null},{\"project\":\"工作态度\",\"consideration\":null,\"grading\":null,\"auditTeamLeaderScore\":null,\"remark\":null,\"staffScore_details_id\":null},{\"project\":\"专业技术\",\"consideration\":null,\"grading\":null,\"auditTeamLeaderScore\":null,\"remark\":null,\"staffScore_details_id\":null},{\"project\":\"敏感性\",\"consideration\":null,\"grading\":null,\"auditTeamLeaderScore\":null,\"remark\":null,\"staffScore_details_id\":null},{\"project\":\"文字能力\",\"consideration\":null,\"grading\":null,\"auditTeamLeaderScore\":null,\"remark\":null,\"staffScore_details_id\":null},{\"project\":\"沟通能力\",\"consideration\":null,\"grading\":null,\"auditTeamLeaderScore\":null,\"remark\":null,\"staffScore_details_id\":null}]";
   			List<TblNbsjStaffscoreDetails> list = JSONArray.parseArray(detailListJson,TblNbsjStaffscoreDetails.class);
   				jsonBean = this.tblNbsjStaffscoreService.saveOrUpdate(re, token, staffScoreid, list);
   			} catch (Exception e) {
   				e.printStackTrace();
   				ResponseFormat.retParam(1,1000,e.getMessage());
   			}
   			return jsonBean;
   	}

	@OperationLog(
			success = "列表记录删除",
			busType = "智能审计",
			fail = "列表记录删除",
			operationType = OperationType.DELETE,
			subType = "基础配置——评价管理——评分列表记录删除"
	)
    @RequestMapping(value = "/delScoreInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
   	@Operation(summary = "评价管理-删除信息")
       public JsonBean delSolutionAttInfo(HttpServletRequest request,
       		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
       		@Parameter(name = "staffScoreid", description = "评价管理ID", required = false) @RequestParam(value = "staffScoreid", required = false) BigDecimal staffScoreid
       		//@Parameter(name = "staffScoreid", description = "评价管理ID ", required = true)  BigDecimal staffScoreid
       		) {
       	JsonBean jsonBean = null;
   		try {
   			jsonBean = this.tblNbsjStaffscoreService.delete(staffScoreid, token);
   		} catch (Exception e) {
   			e.printStackTrace();
   			ResponseFormat.retParam(0,1000,e.getMessage());
   		}
   		return jsonBean;
       }

	@OperationLog(
			success = "往年审计考核结果",
			busType = "智能审计",
			fail = "往年审计考核结果",
			operationType = OperationType.SELECT,
			subType = "基础配置——审计人员管理——获取往年审计考核结果"
	)
    @GetMapping("/getscoreListnopage")
    @Operation(summary = "审计人员管理-往年审计考核结果")
    public JsonBean getscoreListnopage(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
   		@Parameter(name = "staffid", description = "关联人员id", required = false) @RequestParam(value = "staffid", required = false) BigDecimal staffid
   	    ) {
    	JsonBean jsonBean = null;
    	try {
    		TblNbsjStaffscore re=new TblNbsjStaffscore();
    		re.setAuditors(staffid);
			jsonBean =tblNbsjStaffscoreService.getscoreListnopage(re,token);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(0,1000,e.getMessage());
		}
        return jsonBean;
    }

	@OperationLog(
			success = "参与的审计项目列表",
			busType = "智能审计",
			fail = "参与的审计项目列表",
			operationType = OperationType.SELECT,
			subType = "基础配置——审计人员管理——获取参与的审计项目列表"
	)
    @RequestMapping(value = "getAuditItemsList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
   	@Operation(summary = "审计人员管理-参与的审计项目列表")
       public JsonBean getAuditItemsList(HttpServletRequest request,
       		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
       		@Parameter(name = "staffId", description = "人员id", required = true) @RequestParam(value = "staffId", required = true) BigDecimal staffId
       		) {
       	JsonBean jsonBean = null;
   		try {
			jsonBean = projectService.getUserAuditItem(token,staffId);
   		} catch (Exception e) {
   			e.printStackTrace();
   			ResponseFormat.retParam(0,1000,e.getMessage());
   		}
   		return jsonBean;
       }
     
}
