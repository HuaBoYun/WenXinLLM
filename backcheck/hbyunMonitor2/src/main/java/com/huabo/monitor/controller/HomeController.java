package com.huabo.monitor.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.businessservice.HomeService;
import com.huabo.monitor.config.ServiceException;
import com.huabo.monitor.entity.TblAssessVo;
import com.huabo.monitor.util.JsonBean;
import com.huabo.monitor.util.ResponseFormat;
import com.huabo.monitor.util.TokenUtils;
import com.huabo.monitor.vo.param.UserInfoParam;
import com.huabo.monitor.vo.result.EvaluationTrackingStatisticsResult;
import com.huabo.monitor.vo.result.OneLevelProcessStatisticsResult;
import com.huabo.monitor.vo.result.TestTrackingStatisticsResult;
import com.huabo.monitor.vo.result.YearStatisticsResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name="内控-首页",description="内控-首页")

public class HomeController {

	@Resource
	private HomeService homeService;
	
	@Resource
	private UserProvider userProvider;
	

	@OperationLog(
			success = "内部控制缺陷分布查询成功",
			busType = "内控-首页",
			fail = "内部控制缺陷分布失败",
			operationType = OperationType.SELECT,
			subType = "内控-首页"
	)
	@GetMapping("/home/statistics/problem-discovery/one-level-process")
	@Operation(summary = "问题发现-一级流程统计：内部控制缺陷分布")
	public JsonBean<List<OneLevelProcessStatisticsResult>> getProblemDiscoveryOneLevelProcessStatistics(
			@RequestHeader("token") String token,
			@Parameter(name = "orgId", description = "orgId--公司下拉传值") @RequestParam(value = "orgId", required = false) BigDecimal orgId,
			@Parameter(name = "year", description = "year--年度") @RequestParam(value = "year", required = false) String  year) {
		JsonBean<List<OneLevelProcessStatisticsResult>> jsonBean = null;
		try {
			TblStaffUtil user = userProvider.get();
	        if (user == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
			
		        if(orgId==null||orgId.compareTo(new BigDecimal("0"))==0){
		        	orgId=user.getLinkOrg().getOrgid();
		        }
		        if(StringUtils.isBlank(year)){
		        	Calendar calendar = Calendar.getInstance();
		             year = calendar.get(Calendar.YEAR)+"";
		        }
			jsonBean = homeService.getProblemDiscoveryOneLevelProcessStatistics(orgId,year);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("内部控制缺陷分布：按一级流程统计 ...接口 异常", e);
		}
		return jsonBean;
	}


	@OperationLog(
			success = "问题发现-年度统计查询成功",
			busType = "内控-首页",
			fail = "问题发现-年度统计查询失败",
			operationType = OperationType.SELECT,
			subType = "内控-首页"
	)
	@GetMapping("/home/statistics/problem-discovery/year")
	@Operation(summary = "问题发现-年度统计")
	public JsonBean<List<YearStatisticsResult>> getProblemDiscoveryYearStatistics(@RequestHeader("token") String token) {
		JsonBean<List<YearStatisticsResult>> jsonBean = null;
		try {
			TblStaffUtil user = userProvider.get();
	        if (user == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
			UserInfoParam userInfo = TokenUtils.getUserInfo(user);
			jsonBean = homeService.getProblemDiscoveryYearStatistics(userInfo.getBelongGroup());
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("问题发现-年度统计 ...接口 异常", e);
		}
		return jsonBean;
	}

	@OperationLog(
			success = "评价跟踪-状态统计查询成功",
			busType = "内控-首页",
			fail = "评价跟踪-状态统计查询失败",
			operationType = OperationType.SELECT,
			subType = "内控-首页"
	)
	@GetMapping("/home/statistics/evaluation-tracking")
	@Operation(summary = "评价跟踪-状态统计")
	public JsonBean<List<EvaluationTrackingStatisticsResult>> getEvaluationTrackingStatistics(@RequestHeader("token") String token) {
		JsonBean<List<EvaluationTrackingStatisticsResult>> jsonBean = null;
		try {
			TblStaffUtil user = userProvider.get();
	        if (user == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
			UserInfoParam userInfo = TokenUtils.getUserInfo(user);
			jsonBean = homeService.getEvaluationTrackingStatistics(userInfo.getBelongGroup());
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("评价跟踪-状态统计 ...接口 异常", e);
		}
		return jsonBean;
	}

	@OperationLog(
			success = "测试跟踪-状态统计查询成功",
			busType = "内控-首页",
			fail = "测试跟踪-状态统计查询失败",
			operationType = OperationType.SELECT,
			subType = "内控-首页"
	)
	@GetMapping("/home/statistics/test-tracking")
	@Operation(summary = "测试跟踪-状态统计")
	public JsonBean<List<TestTrackingStatisticsResult>> getTestTrackingStatistics(@RequestHeader("token") String token) {
		JsonBean<List<TestTrackingStatisticsResult>> jsonBean = null;
		try {
			TblStaffUtil user = userProvider.get();
	        if (user == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
			UserInfoParam userInfo = TokenUtils.getUserInfo(user);
			jsonBean = homeService.getTestTrackingStatistics(userInfo.getBelongGroup());
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("测试跟踪-状态统计 ...接口 异常", e);
		}
		return jsonBean;
	}
	
	
	
	@OperationLog(
			success = "各单位缺陷数量问题对比",
			busType = "内控-首页",
			fail = "各单位缺陷数量问题对比",
			operationType = OperationType.SELECT,
			subType = "内控-首页"
	)
	@GetMapping("/home/statistics/getDefectQuantityIssues")
	@Operation(summary = "各单位缺陷数量问题对比")
	public JsonBean<Map<String,Object>> getDefectQuantityIssues(@RequestHeader("token") String token) {
		 Map<String,Object> result = new HashMap<>();
		try {
			TblStaffUtil user = userProvider.get();
	        if (user == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
			result= homeService.getDefectQuantityIssues();
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("各单位缺陷数量问题对比...接口 异常", e);
		}
		return ResponseFormat.retParam(1, 200, result);
	}
	
	@OperationLog(
			success = "公司评价项目数统计",
			busType = "内控-首页",
			fail = "公司评价项目数统计",
			operationType = OperationType.SELECT,
			subType = "内控-首页"
	)
	@GetMapping("/home/statistics/getCompanyProjectEvaluations")
	@Operation(summary = "公司评价项目数统计")
	public JsonBean<Map<String,Object>> getCompanyProjectEvaluations(@RequestHeader("token") String token,
			@Parameter(name = "year", description = "year--年度") @RequestParam(value = "year", required = false) String  year) {
		 Map<String,Object> result = new HashMap<>();
		try {
			TblStaffUtil user = userProvider.get();
	        if (user == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
	        if(StringUtils.isBlank(year)){
	        	Calendar calendar = Calendar.getInstance();
	             year = calendar.get(Calendar.YEAR)+"";
	        }
			result= homeService.getCompanyProjectEvaluations(year);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("公司评价项目数统计...接口 异常", e);
		}
		return ResponseFormat.retParam(1, 200, result);
	}
	
	@OperationLog(
			success = "项目成熟度占比分析",
			busType = "内控-首页",
			fail = "项目成熟度占比分析",
			operationType = OperationType.SELECT,
			subType = "内控-首页"
	)
	@GetMapping("/home/statistics/getProjectMaturityAnalysis")
	@Operation(summary = "项目成熟度占比分析")
	public JsonBean<Map<String,Object>> getProjectMaturityAnalysis(@RequestHeader("token") String token,
			@Parameter(name = "orgId", description = "orgId--公司下拉传值") @RequestParam(value = "orgId", required = false) BigDecimal orgId,
			@Parameter(name = "year", description = "year--年度") @RequestParam(value = "year", required = false) String  year) {
		 Map<String,Object> result = new HashMap<>();
		try {
			
			TblStaffUtil user = userProvider.get();
	        if (user == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
		        if(orgId==null||orgId.compareTo(new BigDecimal("0"))==0){
		        	orgId=user.getLinkOrg().getOrgid();
		        }
		        if(StringUtils.isBlank(year)){
		        	Calendar calendar = Calendar.getInstance();
		             year = calendar.get(Calendar.YEAR)+"";
		        }
			result= homeService.getProjectMaturityAnalysis(orgId,year);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目成熟度占比分析...接口 异常", e);
		}
		return ResponseFormat.retParam(1, 200, result);
	}
	
	
	@OperationLog(
			success = "各部门缺陷数量问题对比",
			busType = "内控-首页",
			fail = "各部门缺陷数量问题对比",
			operationType = OperationType.SELECT,
			subType = "内控-首页"
	)
	@GetMapping("/home/statistics/getDefectQuantityIssuesByDep")
	@Operation(summary = "各部门缺陷数量问题对比")
	public JsonBean<Map<String,Object>> getDefectQuantityIssuesByDep(@RequestHeader("token") String token,
			@Parameter(name = "orgId", description = "orgId--公司下拉传值") @RequestParam(value = "orgId", required = false) BigDecimal orgId,
			@Parameter(name = "year", description = "year--年度") @RequestParam(value = "year", required = false) String  year
			) {
		 Map<String,Object> result = new HashMap<>();
		try {
			TblStaffUtil user = userProvider.get();
	        if (user == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
		        if(orgId==null||orgId.compareTo(new BigDecimal("0"))==0){
		        	orgId=user.getLinkOrg().getOrgid();
		        }
		        if(StringUtils.isBlank(year)){
		        	Calendar calendar = Calendar.getInstance();
		             year = calendar.get(Calendar.YEAR)+"";
		        }
			result= homeService.getDefectQuantityIssuesByDep(orgId,year);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("各单位缺陷数量问题对比...接口 异常", e);
		}
		return ResponseFormat.retParam(1, 200, result);
	}
	//按照年度下每个月度查询公司下的缺陷问题总数
	@OperationLog(
			success = "本年度缺陷项目趋势分析",
			busType = "内控-首页",
			fail = "本年度缺陷项目趋势分析",
			operationType = OperationType.SELECT,
			subType = "内控-首页"
	) 
	@GetMapping("/home/statistics/getDefectProjectsYearAnalysis")
	@Operation(summary = "本年度缺陷项目趋势分析")
	public JsonBean<Map<String,Object>> getDefectProjectsYearAnalysis(@RequestHeader("token") String token,
			@Parameter(name = "orgId", description = "orgId--公司下拉传值") @RequestParam(value = "orgId", required = false) BigDecimal orgId,
			@Parameter(name = "year", description = "year--年度") @RequestParam(value = "year", required = false) String  year) {
		 Map<String,Object> result = new HashMap<>();
		try {
			TblStaffUtil user = userProvider.get();
	        if (user == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
		        if(orgId==null||orgId.compareTo(new BigDecimal("0"))==0){
		        	orgId=user.getLinkOrg().getOrgid();
		        }
		        if(StringUtils.isBlank(year)){
		        	Calendar calendar = Calendar.getInstance();
		             year = calendar.get(Calendar.YEAR)+"";
		        }
			result= homeService.getDefectProjectsYearAnalysis(orgId,year);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("本年度缺陷项目趋势分析...接口 异常", e);
		}
		return ResponseFormat.retParam(1, 200, result);
	}
	
	
	@OperationLog(
			success = "年度缺陷类型分布分析",
			busType = "内控-首页",
			fail = "年度缺陷类型分布分析",
			operationType = OperationType.SELECT,
			subType = "内控-首页"
	) 
	@GetMapping("/home/statistics/getDistributionDefectTypes")
	@Operation(summary = "年度缺陷类型分布分析")
	public JsonBean<Map<String,Object>> getDistributionDefectTypes(@RequestHeader("token") String token,
			@Parameter(name = "orgId", description = "orgId--公司下拉传值") @RequestParam(value = "orgId", required = false) BigDecimal orgId,
			@Parameter(name = "year", description = "year--年度") @RequestParam(value = "year", required = false) String  year) {
		 Map<String,Object> result = new HashMap<>();
		try {
			TblStaffUtil user = userProvider.get();
	        if (user == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
		        if(orgId==null||orgId.compareTo(new BigDecimal("0"))==0){
		        	orgId=user.getLinkOrg().getOrgid();
		        }
		        if(StringUtils.isBlank(year)){
		        	Calendar calendar = Calendar.getInstance();
		             year = calendar.get(Calendar.YEAR)+"";
		        }
			result= homeService.getDistributionDefectTypes(orgId,year);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度缺陷类型分布分析..接口 异常", e);
		}
		return ResponseFormat.retParam(1, 200, result);
	}
	
	
	@OperationLog(
			success = "内控评价结果一览表",
			busType = "内控-首页",
			fail = "内控评价结果一览表",
			operationType = OperationType.SELECT,
			subType = "内控-首页"
	) 
	@PostMapping(value = "/home/statistics/getEvaluationResultsList")
	@Operation(summary = "内控评价结果一览表")
	public JsonBean<Map<String,Object>> getEvaluationResultsList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "orgId", description = "orgId--公司下拉传值") @RequestParam(value = "orgId", required = false) BigDecimal orgId,
			@Parameter(name = "year", description = "year--年度") @RequestParam(value = "year", required = false) String  year,
			@Parameter(name = "pageNo", description = "pageNo") @RequestParam(value = "pageNo", required = false) Integer pageNo,
			@Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
		
		Map<String,Object> result = new HashMap<>();
		try {
			TblStaffUtil user = userProvider.get();
	        if (user == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
		        if(orgId==null||orgId.compareTo(new BigDecimal("0"))==0){
		        	orgId=user.getLinkOrg().getOrgid();
		        }
		        if(StringUtils.isBlank(year)){
		        	Calendar calendar = Calendar.getInstance();
		             year = calendar.get(Calendar.YEAR)+"";
		        }
			PageInfo<TblAssessVo> pageinfo= homeService.getEvaluationResultsList(token,orgId,year,pageNo,pageSize);
			result.put("pageInfo", pageinfo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("内控评价结果一览表..接口 异常", e);
		}
		return ResponseFormat.retParam(1, 200, result);
	}
	

	@OperationLog(
			success = "内部控制缺陷等级查询成功",
			busType = "内控-首页",
			fail = "内部控制缺陷等级查询失败",
			operationType = OperationType.SELECT,
			subType = "内控-首页"
	)
	@GetMapping("/home/statistics/problem-discovery/defect-grade")
	@Operation(summary = "问题发现-缺陷等级统计:内部控制缺陷等级")
	public JsonBean<Map<String,Object>> getProblemDiscoveryDefectGradeStatistics(
			@RequestHeader("token") String token,
			@Parameter(name = "orgId", description = "orgId--公司下拉传值") @RequestParam(value = "orgId", required = false) BigDecimal orgId,
			@Parameter(name = "year", description = "year--年度") @RequestParam(value = "year", required = false) String  year) {
		 Map<String,Object> result = new HashMap<>();
		try {
			TblStaffUtil user = userProvider.get();
	        if (user == null) {
	        	return ResponseFormat.retParam(0, 20006, null);
	        }
	        if(orgId==null||orgId.compareTo(new BigDecimal("0"))==0){
	        	orgId=user.getLinkOrg().getOrgid();
	        }
	        if(StringUtils.isBlank(year)){
	        	Calendar calendar = Calendar.getInstance();
	             year = calendar.get(Calendar.YEAR)+"";
	        }
	        result = homeService.getProblemDiscoveryDefectGradeStatistics(orgId,year);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("问题发现-缺陷等级统计 ...接口 异常", e);
		}
		return ResponseFormat.retParam(1, 200, result);
	}

	
	  @OperationLog(
	success = "获取公司下拉成功",
	busType = "内控模块",
	fail = "获取公司下拉失败",
	operationType = OperationType.SELECT,
	subType = "首页分析"
	) 
		@Operation(summary = "获取公司下拉")
		@RequestMapping(value = "getAssessCompanyList", method = {
				RequestMethod.GET }, produces = "application/json; charset=utf-8")
		public JsonBean<Map<String,Object>> getAssessCompanyList(
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token)
						throws Exception {
			Map<String, Object> result = new HashMap<>();
			try {
				TblStaffUtil user = userProvider.get();
		        if (user == null) {
		        	return ResponseFormat.retParam(0, 20006, null);
		        }
				result = homeService.getAssessCompanyList(token);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("获取公司下拉，异常");
			}
			return ResponseFormat.retParam(1, 200, result);
		}
}
