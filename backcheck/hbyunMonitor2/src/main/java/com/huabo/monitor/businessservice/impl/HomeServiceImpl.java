package com.huabo.monitor.businessservice.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.businessservice.HomeService;
import com.huabo.monitor.entity.TblAssessVo;
import com.huabo.monitor.entity.TblTesttaskProblemFind;
import com.huabo.monitor.mapper.TblAssessMapper;
import com.huabo.monitor.mapper.TblTestplanMapper;
import com.huabo.monitor.mapper.TblTesttaskProblemFindMapper;
import com.huabo.monitor.service.ITblTesttaskProblemFindService;
import com.huabo.monitor.service.TblOrganizaService;
import com.huabo.monitor.util.ConstClass;
import com.huabo.monitor.util.JsonBean;
import com.huabo.monitor.util.ResponseFormat;
import com.huabo.monitor.vo.result.*;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import cn.hutool.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {

	@Value("${application.administrators:}")
	private String administrators;
	@Resource
	private TblTesttaskProblemFindMapper tblTesttaskProblemFindMapper;

	@Resource
	private TblAssessMapper tblAssessMapper;
	@Resource
	private TblTestplanMapper tblTestplanMapper;

	@Resource
	private TblOrganizaService  tblOrganizaService;

	@Resource
    private UserProvider userProvider;


	/**
	 * 内部控制缺陷分布：按一级流程统计
	 * @param belongGroup
	 * @return
	 */
	@Override
	public JsonBean<List<OneLevelProcessStatisticsResult>> getProblemDiscoveryOneLevelProcessStatistics(BigDecimal belongGroup,String year) {
		//List<OneLevelProcessStatisticsResult> list = tblTesttaskProblemFindMapper.findOneLevelProcessStatistics(belongGroup);
		List<BigDecimal> orgList=null;
		try {
			orgList = tblOrganizaService.getOrgIdLisBySy(belongGroup);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OneLevelProcessStatisticsResult r = new OneLevelProcessStatisticsResult();
		r.setOrgid(belongGroup);
		r.setOrgIds(orgList);
		r.setYear(year);
		List<OneLevelProcessStatisticsResult> list = tblTesttaskProblemFindMapper.findOneLevelProcessStatisticsNew(r);
		if (CollectionUtil.isEmpty(list)) {
			return ResponseFormat.retParam(200, 200, Collections.emptyList());
		}
		//过滤空
		List<OneLevelProcessStatisticsResult> collect = list.stream().filter(item -> StringUtils.isNotBlank(item.getOneprocess()))
				.collect(Collectors.toList());
		return ResponseFormat.retParam(200, 200, collect);
	}

	/**
	 * 问题发现-缺陷等级统计
	 * @param belongGroup
	 * @return
	 */
	@Override
	public JsonBean<List<DefectGradeStatisticsResult>> getProblemDiscoveryDefectGradeStatistics(BigDecimal belongGroup) {
		//List<DefectGradeStatisticsResult> list = tblTesttaskProblemFindMapper.findDefectGradeStatistics(belongGroup);
		DefectGradeStatisticsResult r=new DefectGradeStatisticsResult();
		r.setOrgid(belongGroup);
		List<BigDecimal> orgList;
		try {
			  orgList = tblOrganizaService.getOrgIdLisBySy(belongGroup);
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<DefectGradeStatisticsResult> list = tblTesttaskProblemFindMapper.findDefectGradeStatisticsNew(r);
		if (CollectionUtil.isEmpty(list)) {
			List<DefectGradeStatisticsResult> results = new ArrayList<>();
			DefectGradeStatisticsResult defectGradeStatisticsResult = new DefectGradeStatisticsResult();
			defectGradeStatisticsResult.setDefecttype("重要");
			defectGradeStatisticsResult.setNum(0);
			results.add(defectGradeStatisticsResult);
			DefectGradeStatisticsResult defectGradeStatisticsResult1 = new DefectGradeStatisticsResult();
			defectGradeStatisticsResult1.setDefecttype("一般");
			defectGradeStatisticsResult1.setNum(0);
			results.add(defectGradeStatisticsResult1);
			DefectGradeStatisticsResult defectGradeStatisticsResult2 = new DefectGradeStatisticsResult();
			defectGradeStatisticsResult2.setDefecttype("重大");
			defectGradeStatisticsResult2.setNum(0);
			results.add(defectGradeStatisticsResult2);
			return ResponseFormat.retParam(200, 200, results);
		}
		//过滤空
		List<DefectGradeStatisticsResult> collect = list.stream().filter(item -> StringUtils.isNotBlank(item.getDefecttype()))
				.collect(Collectors.toList());
		List<DefectGradeStatisticsResult> important = collect.stream().filter(item -> StringUtils.equals(item.getDefecttype(), "重要"))
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(important)) {
			DefectGradeStatisticsResult defectGradeStatisticsResult = new DefectGradeStatisticsResult();
			defectGradeStatisticsResult.setDefecttype("重要");
			defectGradeStatisticsResult.setNum(0);
			collect.add(defectGradeStatisticsResult);
		}
		List<DefectGradeStatisticsResult> general = collect.stream().filter(item -> StringUtils.equals(item.getDefecttype(), "一般"))
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(general)) {
			DefectGradeStatisticsResult defectGradeStatisticsResult = new DefectGradeStatisticsResult();
			defectGradeStatisticsResult.setDefecttype("一般");
			defectGradeStatisticsResult.setNum(0);
			collect.add(defectGradeStatisticsResult);
		}
		List<DefectGradeStatisticsResult> significant = collect.stream().filter(item -> StringUtils.equals(item.getDefecttype(), "重大"))
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(significant)) {
			DefectGradeStatisticsResult defectGradeStatisticsResult = new DefectGradeStatisticsResult();
			defectGradeStatisticsResult.setDefecttype("重大");
			defectGradeStatisticsResult.setNum(0);
			collect.add(defectGradeStatisticsResult);
		}
		return ResponseFormat.retParam(200, 200, collect);
	}

	/**
	 * 问题发现-年度统计
	 * @param belongGroup
	 * @return
	 */
	@Override
	public JsonBean<List<YearStatisticsResult>> getProblemDiscoveryYearStatistics(BigDecimal belongGroup) {
		//List<YearStatisticsResult> list = tblTesttaskProblemFindMapper.findYearStatistics(belongGroup);

		List<YearStatisticsResult> collect=null;
		//start with ORGID=#{belongGroup}  and ORGTYPE!=0 AND ORGTYPE<100  connect by prior orgid= FATHERORGID
		try {
			List<BigDecimal> orgList=tblOrganizaService.getOrgIdLisBySy(belongGroup);
			YearStatisticsResult r=new YearStatisticsResult();
			r.setOrgid(belongGroup);
			r.setOrgIds(orgList);
			List<YearStatisticsResult> list = tblTesttaskProblemFindMapper.findYearStatisticsNew(r);
			if (CollectionUtil.isEmpty(list)) {
				return ResponseFormat.retParam(200, 200, Collections.emptyList());
			}
			//过滤空
			 collect = list.stream().filter(item -> Objects.nonNull(item.getTestYear())).collect(Collectors.toList());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseFormat.retParam(200, 200, collect);
	}

	/**
	 * 评价跟踪-状态统计
	 * @param belongGroup
	 * @return
	 */
	@Override
	public JsonBean<List<EvaluationTrackingStatisticsResult>> getEvaluationTrackingStatistics(BigDecimal belongGroup) {
		//List<EvaluationTrackingStatisticsResult> list = tblAssessMapper.findEvaluationTrackingStatistics(belongGroup);
		EvaluationTrackingStatisticsResult r=new EvaluationTrackingStatisticsResult();
		r.setOrgid(belongGroup);
		List<BigDecimal> orgList=null;
		try {
			  orgList = tblOrganizaService.getOrgIdLisBySy(belongGroup);
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<EvaluationTrackingStatisticsResult> list = tblAssessMapper.findEvaluationTrackingStatisticsNew(r);
		if (CollectionUtil.isEmpty(list)) {
			List<EvaluationTrackingStatisticsResult> results = new ArrayList<>();
			EvaluationTrackingStatisticsResult evaluationTrackingStatisticsResult = new EvaluationTrackingStatisticsResult();
			evaluationTrackingStatisticsResult.setStatus(1);
			evaluationTrackingStatisticsResult.setNum(0);
			results.add(evaluationTrackingStatisticsResult);
			EvaluationTrackingStatisticsResult evaluationTrackingStatisticsResult1 = new EvaluationTrackingStatisticsResult();
			evaluationTrackingStatisticsResult1.setStatus(2);
			evaluationTrackingStatisticsResult1.setNum(0);
			results.add(evaluationTrackingStatisticsResult1);
//			EvaluationTrackingStatisticsResult evaluationTrackingStatisticsResult2 = new EvaluationTrackingStatisticsResult();
//			evaluationTrackingStatisticsResult2.setStatus(3);
//			evaluationTrackingStatisticsResult2.setNum(0);
//			results.add(evaluationTrackingStatisticsResult2);
//			EvaluationTrackingStatisticsResult evaluationTrackingStatisticsResult3 = new EvaluationTrackingStatisticsResult();
//			evaluationTrackingStatisticsResult2.setStatus(4);
//			evaluationTrackingStatisticsResult2.setNum(0);
//			results.add(evaluationTrackingStatisticsResult3);
//			EvaluationTrackingStatisticsResult evaluationTrackingStatisticsResult4 = new EvaluationTrackingStatisticsResult();
//			evaluationTrackingStatisticsResult2.setStatus(5);
//			evaluationTrackingStatisticsResult2.setNum(0);
//			results.add(evaluationTrackingStatisticsResult4);
//			EvaluationTrackingStatisticsResult evaluationTrackingStatisticsResult5 = new EvaluationTrackingStatisticsResult();
//			evaluationTrackingStatisticsResult2.setStatus(6);
//			evaluationTrackingStatisticsResult2.setNum(0);
//			results.add(evaluationTrackingStatisticsResult5);
			return ResponseFormat.retParam(200, 200, results);
		}
		//过滤空
		List<EvaluationTrackingStatisticsResult> collect = list.stream().filter(item -> Objects.nonNull(item.getStatus()))
				.collect(Collectors.toList());
		List<EvaluationTrackingStatisticsResult> a = collect.stream().filter(item -> Objects.equals(item.getStatus(), 1))
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(a)) {
			EvaluationTrackingStatisticsResult evaluationTrackingStatisticsResult = new EvaluationTrackingStatisticsResult();
			evaluationTrackingStatisticsResult.setStatus(1);
			evaluationTrackingStatisticsResult.setNum(0);
			collect.add(evaluationTrackingStatisticsResult);
		}
		List<EvaluationTrackingStatisticsResult> b = collect.stream().filter(item -> Objects.equals(item.getStatus(), 2))
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(b)) {
			EvaluationTrackingStatisticsResult evaluationTrackingStatisticsResult = new EvaluationTrackingStatisticsResult();
			evaluationTrackingStatisticsResult.setStatus(2);
			evaluationTrackingStatisticsResult.setNum(0);
			collect.add(evaluationTrackingStatisticsResult);
		}
		List<EvaluationTrackingStatisticsResult> c = collect.stream().filter(item -> Objects.equals(item.getStatus(), 3))
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(c)) {
			EvaluationTrackingStatisticsResult evaluationTrackingStatisticsResult = new EvaluationTrackingStatisticsResult();
			evaluationTrackingStatisticsResult.setStatus(3);
			evaluationTrackingStatisticsResult.setNum(0);
			collect.add(evaluationTrackingStatisticsResult);
		}
		List<EvaluationTrackingStatisticsResult> d = collect.stream().filter(item -> Objects.equals(item.getStatus(), 4))
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(d)) {
			EvaluationTrackingStatisticsResult evaluationTrackingStatisticsResult = new EvaluationTrackingStatisticsResult();
			evaluationTrackingStatisticsResult.setStatus(4);
			evaluationTrackingStatisticsResult.setNum(0);
			collect.add(evaluationTrackingStatisticsResult);
		}
		List<EvaluationTrackingStatisticsResult> e = collect.stream().filter(item -> Objects.equals(item.getStatus(), 5))
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(e)) {
			EvaluationTrackingStatisticsResult evaluationTrackingStatisticsResult = new EvaluationTrackingStatisticsResult();
			evaluationTrackingStatisticsResult.setStatus(5);
			evaluationTrackingStatisticsResult.setNum(0);
			collect.add(evaluationTrackingStatisticsResult);
		}
		List<EvaluationTrackingStatisticsResult> f = collect.stream().filter(item -> Objects.equals(item.getStatus(), 6))
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(f)) {
			EvaluationTrackingStatisticsResult evaluationTrackingStatisticsResult = new EvaluationTrackingStatisticsResult();
			evaluationTrackingStatisticsResult.setStatus(6);
			evaluationTrackingStatisticsResult.setNum(0);
			collect.add(evaluationTrackingStatisticsResult);
		}
		return ResponseFormat.retParam(200, 200, collect);
	}

	/**
	 * 测试跟踪-状态统计
	 * @param belongGroup
	 * @return
	 */
	@Override
	public JsonBean<List<TestTrackingStatisticsResult>> getTestTrackingStatistics(BigDecimal belongGroup) {
		// List<TestTrackingStatisticsResult> list =
		// tblTestplanMapper.findTestTrackingStatistics(belongGroup);
		List<BigDecimal> orgList;
		List<TestTrackingStatisticsResult> collect=null;
		try {
			orgList = tblOrganizaService.getOrgIdLisBySy(belongGroup);
			TestTrackingStatisticsResult r = new TestTrackingStatisticsResult();
			r.setOrgid(belongGroup);
			r.setOrgIds(orgList);
			List<TestTrackingStatisticsResult> list = tblTestplanMapper.findTestTrackingStatisticsNew(r);
			if (CollectionUtil.isEmpty(list)) {
				return ResponseFormat.retParam(200, 200, Collections.emptyList());
			}
			// 过滤空
			collect = list.stream().filter(item -> StringUtils.isNotBlank(item.getPlanstatus()))
					.collect(Collectors.toList());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseFormat.retParam(200, 200, collect);
	}

	@Override
	public Map<String, Object> getDefectQuantityIssues() {
		// TODO Auto-generated method stub
		 Map<String,Object> result=new HashMap<String,Object>();
		try {
			List<Map<String, Object>> list=tblTesttaskProblemFindMapper.getDefectQuantityIssues();
			String[] nameList=new String[list.size()];
			Long[] valueList=new Long[list.size()];
			for(int i=0;i<list.size();i++){
				Map<String, Object> o=list.get(i);
				nameList[i]=(String) o.get("NAME");
				valueList[i]=(Long) o.get("VALUE");
			}
			result.put("nameList", nameList);
			result.put("valueList", valueList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Map<String, Object> getCompanyProjectEvaluations(String year) {
		// TODO Auto-generated method stub
				 Map<String,Object> result=new HashMap<String,Object>();
				try {
					List<Map<String, Object>> list=tblAssessMapper.getCompanyProjectEvaluations(year);
					String[] nameList=new String[list.size()];
					Long[] valueList=new Long[list.size()];
					for(int i=0;i<list.size();i++){
						Map<String, Object> o=list.get(i);
						nameList[i]=(String) o.get("NAME");
						valueList[i]=(Long) o.get("VALUE");
					}
					result.put("nameList", nameList);
					result.put("valueList", valueList);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return result;
	}

	@Override
	public Map<String, Object> getProjectMaturityAnalysis(BigDecimal orgid,String year) {
		// TODO Auto-generated method stub
		 Map<String,Object> result=new HashMap<String,Object>();
			try {
				List<Map<String, Object>> list=tblAssessMapper.getProjectMaturityAnalysis(orgid,year);
				result.put("data", list);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return result;
	}

	@Override
	public Map<String, Object> getDefectQuantityIssuesByDep(BigDecimal orgid, String year) {
		// TODO Auto-generated method stub
		 Map<String,Object> result=new HashMap<String,Object>();
			try {
				List<Map<String, Object>> list=tblTesttaskProblemFindMapper.getDefectQuantityIssuesByDep(orgid,year);
				String[] nameList=new String[list.size()];
				Long[] valueList=new Long[list.size()];
				for(int i=0;i<list.size();i++){
					Map<String, Object> o=list.get(i);
					nameList[i]=(String) o.get("NAME");
					valueList[i]=(Long) o.get("VALUE");
				}
				result.put("nameList", nameList);
				result.put("valueList", valueList);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return result;
	}

	@Override
	public Map<String, Object> getDefectProjectsYearAnalysis(BigDecimal orgid, String year) {
		// TODO Auto-generated method stub
				 Map<String,Object> result=new HashMap<String,Object>();
					try {
						List<Map<String, Object>> list=tblTesttaskProblemFindMapper.getDefectProjectsYearAnalysis(orgid,year);
						String[] nameList=new String[list.size()];
						Long[] valueList=new Long[list.size()];
						for(int i=0;i<list.size();i++){
							Map<String, Object> o=list.get(i);
							nameList[i]=(String) o.get("NAME");
							valueList[i]=(Long) o.get("VALUE");
						}
						result.put("xList", nameList);
						result.put("yList", valueList);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					return result;
	}

	@Override
	public Map<String, Object> getDistributionDefectTypes(BigDecimal orgid, String year) {
		// TODO Auto-generated method stub
		Map<String,Object> result=new HashMap<String,Object>();
	try {
		//x轴缺陷等级

		String[] xType={"一般","重要","重大"};
		String[] yType={"执行缺陷","设计缺陷"};
		List<JSONObject> obj=new ArrayList<JSONObject>();
		Map<String,Long[]> map=new HashMap<>();
        for(int i=0;i<yType.length;i++){

        	List<Map<String, Object>> items=tblTesttaskProblemFindMapper.getDistributionDefectTypes(orgid,year,yType[i]);
        	Long[] valueList=new Long[items.size()];
			for(int j=0;j<items.size();j++){
				Map<String, Object> o=items.get(j);
				valueList[j]=(Long) o.get("VALUE");
			}
        	map.put(yType[i],valueList);
        }
        result.put("yAxis", map);
        result.put("xAxis", xType);
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
		return result;
	}

	@Override
	public PageInfo<TblAssessVo> getEvaluationResultsList(String token,BigDecimal orgid, String year,Integer pageNo,Integer pagesize) {
		// TODO Auto-generated method stub
		PageInfo<TblAssessVo> pageInfo=null;
	try {
		//x轴缺陷等级
		 TblStaffUtil user = userProvider.get();
		 Integer authorityType;
			if (JudgeRoleRight.judgeRoleRight(administrators, user.getRoleNames())) {
				authorityType = 1;
			} else {
				authorityType = 0;
			}
			 pageInfo=PageMethod.startPage(pageNo,pagesize).doSelectPageInfo(()->tblAssessMapper.getEvaluationResultsList(orgid, year, authorityType,user.getStaffid()));
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
		return pageInfo;
	}

	@Override
	public  Map<String, Object>  getProblemDiscoveryDefectGradeStatistics(BigDecimal orgid,
			String year) {
		// TODO Auto-generated method stub
				 Map<String,Object> result=new HashMap<String,Object>();
					try {
						List<Map<String, Object>> list=tblTesttaskProblemFindMapper.getProblemDiscoveryDefectGradeStatistics(orgid,year);
						String[] nameList=new String[list.size()];
						Long[] valueList=new Long[list.size()];
						for(int i=0;i<list.size();i++){
							Map<String, Object> o=list.get(i);
							nameList[i]=(String) o.get("NAME");
							valueList[i]=(Long) o.get("VALUE");
						}
						result.put("nameList", nameList);
						result.put("valueList", valueList);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					return result;
	}

	@Override
	public Map<String, Object> getAssessCompanyList(String token) {
		// TODO Auto-generated method stub
		 Map<String,Object> result=new HashMap<String,Object>();
			try {
				TblStaffUtil tblStaffUtil = userProvider.get();
				List<com.huabo.monitor.entity.TblOrganization> orgList=tblAssessMapper.getAssessCompanyList();
				result.put("data", orgList);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return result;
	}
}
