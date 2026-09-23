package com.huabo.audit.controller;

import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.service.AuditControlAnalysisService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 分析报告控制器
 * <p>提供审计分析报告的查询、生成等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="analysisReport",description="analysisReport")
@RequestMapping(value = "/analysisReport")
public class AnalysisReportController {
	
	@Resource
	private AuditControlAnalysisService auditControlAnalysisService;
	
	
	@Operation(summary = "指挥决策建设方案数据接口")
	@GetMapping("/commandPlanData")
	public JsonBean commandPlanData(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "queryYear", description = "查询年度") @RequestParam(value = "queryYear", required = false) Integer queryYear) {
		JsonBean jsonBean = null;
		try {
			jsonBean = auditControlAnalysisService.commandPlanData(token, queryYear);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	
	
	
	
	
	@Operation(summary = "审计计划执行率(包含年初)")
	@GetMapping("/beginYearPlanRate") 
	public JsonBean beginYearPlanRate(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "queryYear", description = "查询年度") @RequestParam(value = "queryYear", required = false) Integer queryYear) {
		JsonBean jsonBean = null;
		try {
			jsonBean = auditControlAnalysisService.beginYearPlanRate(token, queryYear);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "本年审计项目计划完成率")
	@GetMapping("/planCompletionRate") 
	public JsonBean planCompletionRate(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "queryYear", description = "查询年度") @RequestParam(value = "queryYear", required = false) Integer queryYear) {
		JsonBean jsonBean = null;
		try {
			jsonBean = auditControlAnalysisService.planCompletionRate(token, queryYear);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "审计发现问题整改金额")
	@GetMapping("/rectificationAmount") 
	public JsonBean rectificationAmount(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "queryYear", description = "查询年度") @RequestParam(value = "queryYear", required = false) Integer queryYear) {
		JsonBean jsonBean = null;
		try {
			jsonBean = auditControlAnalysisService.rectificationAmount(token, queryYear);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "审计发现问题个数整改率")
	@GetMapping("/rectificationRate") 
	public JsonBean rectificationRate(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "queryYear", description = "查询年度") @RequestParam(value = "queryYear", required = false) Integer queryYear) {
		JsonBean jsonBean = null;
		try {
			jsonBean = auditControlAnalysisService.rectificationRate(token, queryYear);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "审计建议采纳率")
	@GetMapping("/auditAdoptionRate") 
	public JsonBean auditAdoptionRate(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "queryYear", description = "查询年度") @RequestParam(value = "queryYear", required = false) Integer queryYear) {
		JsonBean jsonBean = null;
		try {
			jsonBean = auditControlAnalysisService.auditAdoptionRate(token, queryYear);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	@Operation(summary = "本年审计项目数量统计")
	@GetMapping("/plancount") 
	public JsonBean plancount(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "queryYear", description = "查询年度") @RequestParam(value = "queryYear", required = false) Integer queryYear) {
		JsonBean jsonBean = null;
		try {
			jsonBean = auditControlAnalysisService.selectPlancount(token, queryYear);
		} catch (ServiceException ex) {
			throw ex; 
		} catch (Exception e) { 
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean; 
	}
	
	
	@Operation(summary = "审计问题整改迟缓事项")
	@GetMapping("/projectListcount") 
	public JsonBean projectListcount(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			 @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
             @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "queryYear", description = "查询年度") @RequestParam(value = "queryYear", required = false) Integer queryYear) {
		JsonBean jsonBean = null;
		try {
			if(queryYear == null) {
				Calendar calendar = Calendar.getInstance();
				queryYear = calendar.get(Calendar.YEAR);
			}
			jsonBean = auditControlAnalysisService.selectProjectcount(token,pageNumber,pageSize, queryYear);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	@Operation(summary = "查询本年每月项目数量")
	@GetMapping("/projectYfcount") 
	public JsonBean projectYfcount(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "queryYear", description = "查询年度") @RequestParam(value = "queryYear", required = false) Integer queryYear) {
		JsonBean jsonBean = null;
		try {
			jsonBean = auditControlAnalysisService.selectPlanyfcount(token, queryYear);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "查询本年项目状态数量")
	@GetMapping("/projectZfcount") 
	public JsonBean projectZfcount(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "queryYear", description = "查询年度") @RequestParam(value = "queryYear", required = false) Integer queryYear) {
		JsonBean jsonBean = null;
		try {
			jsonBean = auditControlAnalysisService.selectPlanZtcount(token, queryYear);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "项目运行情况；按照审前准备-现场实施-审计报告（交换意见稿/定稿）-审计整改-已完成")
	@GetMapping("/projectYxStageCount") 
	public JsonBean projectYxStageCount(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
			@Parameter(name = "queryYear", description = "查询年度") @RequestParam(value = "queryYear", required = false) Integer queryYear) {
		JsonBean jsonBean = null;
		try {
			jsonBean = auditControlAnalysisService.selectProjectYxStageCount(token, queryYear);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "人员情况；请假（请假时间）；在岗（审计部事务工作人员-在岗闲置；开展审计项目人员-在岗项目内）；外派任务（所执行任务的名称、地点）")
	@GetMapping("/staffStateCount") 
	public JsonBean staffStateCount(
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = auditControlAnalysisService.selectStaffStateCount(token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
}
