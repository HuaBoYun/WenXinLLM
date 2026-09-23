package com.huabo.monitor.businessservice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.huabo.monitor.entity.TblAssessVo;
import com.huabo.monitor.entity.TblTesttaskProblemFind;
import com.huabo.monitor.util.JsonBean;
import com.huabo.monitor.vo.result.DefectGradeStatisticsResult;
import com.huabo.monitor.vo.result.EvaluationTrackingStatisticsResult;
import com.huabo.monitor.vo.result.OneLevelProcessStatisticsResult;
import com.huabo.monitor.vo.result.TestTrackingStatisticsResult;
import com.huabo.monitor.vo.result.YearStatisticsResult;

public interface HomeService {

	/**
	 * 内部控制缺陷分布：按一级流程统计
	 * @param belongGroup
	 * @return
	 */
	JsonBean<List<OneLevelProcessStatisticsResult>> getProblemDiscoveryOneLevelProcessStatistics(BigDecimal belongGroup,String year);

	/**
	 * 问题发现-缺陷等级统计
	 * @param belongGroup
	 * @return
	 */
	JsonBean<List<DefectGradeStatisticsResult>> getProblemDiscoveryDefectGradeStatistics(BigDecimal belongGroup);
	
	 Map<String, Object> getProblemDiscoveryDefectGradeStatistics(BigDecimal belongGroup,String year);

	/**
	 * 问题发现-年度统计
	 * @param belongGroup
	 * @return
	 */
	JsonBean<List<YearStatisticsResult>> getProblemDiscoveryYearStatistics(BigDecimal belongGroup);

	/**
	 * 评价跟踪-状态统计
	 * @param belongGroup
	 * @return
	 */
	JsonBean<List<EvaluationTrackingStatisticsResult>> getEvaluationTrackingStatistics(BigDecimal belongGroup);

	/**
	 * 测试跟踪-状态统计
	 * @param belongGroup
	 * @return
	 */
	JsonBean<List<TestTrackingStatisticsResult>> getTestTrackingStatistics(BigDecimal belongGroup);
	
	
	/**
	 * 各单位缺陷数量问题对比
	 * @param  
	 * @return
	 */
	Map<String, Object> getDefectQuantityIssues();
	
	/**
	 * 公司项目评价数统计
	 * @param  year 查询评价立项的创建时间对应的年度
	 * @return
	 */
	Map<String, Object> getCompanyProjectEvaluations(String year);
	/**
	 * 项目成熟度占比分析
	 * @param  
	 * @return
	 */
	
	Map<String, Object> getProjectMaturityAnalysis(BigDecimal orgid,String year);
	/**
	 * 各单位缺陷数量问题对比
	 * @param  
	 * @return
	 */
	Map<String, Object> getDefectQuantityIssuesByDep(BigDecimal orgid,String year);

	/**
	 * 本年度缺陷项目趋势分析
	 * @param  
	 * @return
	 */
	Map<String, Object> getDefectProjectsYearAnalysis(BigDecimal orgid,String year);
	
	
	/**
	 * 年度缺陷类型分布分析
	 * @param  
	 * @return
	 */
	Map<String, Object> getDistributionDefectTypes(BigDecimal orgid,String year);
	
	
	/**
	 * 内控评价结果一览表
	 * @param  
	 * @return
	 */
	PageInfo<TblAssessVo> getEvaluationResultsList(String token,BigDecimal orgid,String year,Integer pageNo,Integer pageSize);
	
	Map<String, Object> getAssessCompanyList(String token);

	
}
