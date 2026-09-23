package com.huabo.legal.service.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.oracle.entity.*;
import com.huabo.legal.oracle.service.*;
import com.huabo.legal.service.FileUploadService;
import com.huabo.legal.service.PlanExamineService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.vo.param.*;
import com.huabo.legal.vo.result.TblFwglAnnualExamine;
import com.huabo.legal.vo.result.TblFwglAnnualPlan;
import com.huabo.legal.vo.result.TblFwglPlanManagement;
import com.huabo.legal.vo.result.UserInfo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanExamineServiceImpl implements PlanExamineService {

	@Resource
	private TblFwglPlanManagementOracleService tblFwglPlanManagementOracleService;
	@Resource
	private TblFwglFileOracleService tblFwglFileOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblFwglAnnualPlanOracleService tblFwglAnnualPlanOracleService;
	@Resource
	private TblFwglAnnualExamineOracleService tblFwglAnnualExamineOracleService;
	@Resource
	private TblFwglAnnualExamineTopicExtOracleService tblFwglAnnualExamineTopicExtOracleService;
	@Resource
	private TblFwglAnnualExamineScoreExtOracleService tblFwglAnnualExamineScoreExtOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 规划管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglPlanManagementList(TblFwglPlanManagementQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglPlanManagementOracle> pageInfo = tblFwglPlanManagementOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			PageResult<TblFwglPlanManagementOracle> build = new PageResult<TblFwglPlanManagementOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 规划管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglPlanManagement(TblFwglPlanManagement param) {
		TblFwglPlanManagementOracle fwglPlanManagement = new TblFwglPlanManagementOracle();
		BeanUtils.copyProperties(param, fwglPlanManagement);
		TblFwglPlanManagementOracle planManagement = tblFwglPlanManagementOracleService.saveOrUpdate(fwglPlanManagement);
		return ResponseFormat.retParam(200, 200, planManagement);
	}

	/**
	 * 规划管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteTblFwglPlanManagement(Long id) {
		TblFwglPlanManagementOracle planManagement = tblFwglPlanManagementOracleService.findById(id);
		//规划管理 刪除
		tblFwglPlanManagementOracleService.delete(id);
		//规划管理-文件 删除
		if (StringUtils.isNotBlank(planManagement.getFileIds())) {
			List<String> fileIds = Arrays.asList(planManagement.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 规划管理详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglPlanManagement(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//规划管理详情
		TblFwglPlanManagementOracle planManagement = tblFwglPlanManagementOracleService.findById(id);
		//规划管理-文件
		if (StringUtils.isNotEmpty(planManagement.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(planManagement.getFileIds());
			map.put("files", files);
		}
		map.put("planManagement", planManagement);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 年度计划列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglAnnualPlanList(TblFwglAnnualPlanQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglAnnualPlanOracle> pageInfo = tblFwglAnnualPlanOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			PageResult<TblFwglAnnualPlanOracle> build = new PageResult<TblFwglAnnualPlanOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 年度计划 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglAnnualPlan(TblFwglAnnualPlan param) {
		TblFwglAnnualPlanOracle fwglAnnualPlan = new TblFwglAnnualPlanOracle();
		BeanUtils.copyProperties(param, fwglAnnualPlan);
		TblFwglAnnualPlanOracle annualPlan = tblFwglAnnualPlanOracleService.saveOrUpdate(fwglAnnualPlan);
		return ResponseFormat.retParam(200, 200, annualPlan);
	}

	/**
	 * 年度计划 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglAnnualPlan(Long id) {
		TblFwglAnnualPlanOracle annualPlan = tblFwglAnnualPlanOracleService.findById(id);
		//年度计划 刪除
		tblFwglAnnualPlanOracleService.delete(id);
		//年度计划-文件 删除
		if (StringUtils.isNotBlank(annualPlan.getFileIds())) {
			List<String> fileIds = Arrays.asList(annualPlan.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 年度计划详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglAnnualPlan(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		TblFwglAnnualPlanOracle annualPlan = tblFwglAnnualPlanOracleService.findById(id);
		//规划管理-文件
		if (StringUtils.isNotEmpty(annualPlan.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(annualPlan.getFileIds());
			map.put("files", files);
		}
		map.put("annualPlan", annualPlan);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 年度考核列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglAnnualExamineList(TblFwglAnnualExamineQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglAnnualExamineOracle> pageInfo = tblFwglAnnualExamineOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			//查询创建者ID
			String creatorIds = pageInfo.getList().stream().map(TblFwglAnnualExamineOracle::getCreator).distinct().collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(creatorIds)) {
				List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(creatorIds);
				if (CollectionUtil.isNotEmpty(userInfos)) {
					Map<String, String> map = userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
					pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCreator()))
							.forEach(x -> x.setCreatorName(map.getOrDefault(x.getCreator(), "")));
				}
			}
			PageResult<TblFwglAnnualExamineOracle> build = new PageResult<TblFwglAnnualExamineOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 年度考核 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglAnnualExamine(TblFwglAnnualExamine param) {
		TblFwglAnnualExamineOracle fwglAnnualExamine = new TblFwglAnnualExamineOracle();
		BeanUtils.copyProperties(param, fwglAnnualExamine);
		TblFwglAnnualExamineOracle annualExamine = tblFwglAnnualExamineOracleService.saveOrUpdate(fwglAnnualExamine);
		return ResponseFormat.retParam(200, 200, annualExamine);
	}

	/**
	 * 年度考核 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteTblFwglAnnualExamine(Long id) {
		TblFwglAnnualExamineOracle annualExamine = tblFwglAnnualExamineOracleService.findById(id);
		//年度考核 刪除
		tblFwglAnnualExamineOracleService.delete(id);
		//年度考核-考核评分 删除
		tblFwglAnnualExamineScoreExtOracleService.deleteTblFwglAnnualExamineScoreExt(annualExamine.getAnnualExamineId());
		//年度考核-文件 删除
		if (StringUtils.isNotBlank(annualExamine.getFileIds())) {
			List<String> fileIds = Arrays.asList(annualExamine.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 年度考核详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglAnnualExamine(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//年度考核详情
		TblFwglAnnualExamineOracle annualExamine = tblFwglAnnualExamineOracleService.findById(id);
		annualExamine.setCreatorName(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(annualExamine.getCreator())));
		if (annualExamine.getSonCompanyId() != null) {
			annualExamine.setSonCompanyName(tblStaffOracleService.getWorkUnitIdUserInfo(annualExamine.getSonCompanyId()));
		}
		//年度考核-文件
		if (StringUtils.isNotEmpty(annualExamine.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(annualExamine.getFileIds());
			map.put("files", files);
		}
		map.put("annualExamine", annualExamine);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 年度考核-考核题目列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglAnnualExamineTopicExtList(TblFwglAnnualExamineTopicExtQueryParam param) {
		List<TblFwglAnnualExamineTopicExtOracle> annualExamineTopicExtList = tblFwglAnnualExamineTopicExtOracleService.getList(param);
		return ResponseFormat.retParam(200, 200, annualExamineTopicExtList);
	}

	/**
	 * 年度考核-考核评分列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglAnnualExamineScoreExtList(TblFwglAnnualExamineScoreExtQueryParam param) {
		HashMap<String, Object> map = new HashMap<>();
		List<TblFwglAnnualExamineScoreExtOracle> annualExamineScoreExtList = tblFwglAnnualExamineScoreExtOracleService.getList(param);
		return ResponseFormat.retParam(200, 200, annualExamineScoreExtList);
	}

	/**
	 * 年度考核-考核评分 批量新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglAnnualExamineScoreExt(List<TblFwglAnnualExamineScoreExtParam> param) {
		String transactionId = "";
		if (CollectionUtil.isNotEmpty(param)) {
			List<TblFwglAnnualExamineScoreExtOracle> collect = param.stream().map(x -> {
				TblFwglAnnualExamineScoreExtOracle tblFwglAnnualExamineScoreExtOracle = new TblFwglAnnualExamineScoreExtOracle();
				BeanUtils.copyProperties(x, tblFwglAnnualExamineScoreExtOracle);
				return tblFwglAnnualExamineScoreExtOracle;
			}).collect(Collectors.toList());
			transactionId = tblFwglAnnualExamineScoreExtOracleService.saveOrUpdate(collect);
		}
		return ResponseFormat.retParam(200, 200, transactionId);
	}

	/**
	 * 年度考核-考核题目 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglAnnualExamineTopicExt(TblFwglAnnualExamineTopicExtBatchAdd param) {
		tblFwglAnnualExamineTopicExtOracleService.saveOrUpdate(param);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 年度考核-考核评分-附件列表 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglAnnualExamineScoreExtFile(Long id) {
		TblFwglAnnualExamineScoreExtOracle annualExamineScoreExt = tblFwglAnnualExamineScoreExtOracleService.findById(id);
		if (StringUtils.isNotBlank(annualExamineScoreExt.getFileIds())) {
			List<TblFwglFileOracle> file = tblFwglFileOracleService.findByIds(annualExamineScoreExt.getFileIds());
			return ResponseFormat.retParam(200, 200, file);
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 年度考核-考核评分-附件-确定按钮
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglAnnualExamineScoreExtFile(TblFwglAnnualExamineScoreExtFileParam param) {
		TblFwglAnnualExamineScoreExtOracle annualExamineScoreExt = tblFwglAnnualExamineScoreExtOracleService.saveOrUpdate(param);
		return ResponseFormat.retParam(200, 200, annualExamineScoreExt);
	}

	/**
	 * 考核台账列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getExamineAllList(ExamineAllQueryParam param) {
		param.setBelongGroup(null);
		param.setCreator(null);
		param.setWorkUnit(null);
		TblFwglAnnualExamineQueryParam queryParam = new TblFwglAnnualExamineQueryParam();
		BeanUtils.copyProperties(param, queryParam);
		PageInfo<TblFwglAnnualExamineOracle> pageInfo = tblFwglAnnualExamineOracleService.getList(queryParam);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<String> belongGroup = pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getBelongGroup()))
					.map(TblFwglAnnualExamineOracle::getBelongGroup).distinct().collect(Collectors.toList());
			List<Long> sonCompany = pageInfo.getList().stream().filter(x -> x.getSonCompanyId() != null)
					.map(TblFwglAnnualExamineOracle::getSonCompanyId).distinct().collect(Collectors.toList());
			//获取集团map
			Map<String, String> belongGroupMap = tblStaffOracleService.getBelongGroupIdUserInfoMap(StringUtils.join(belongGroup, ","));
			//获取单位map
			Map<String, String> sonCompanyMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(sonCompany, ","));
			//参数组装 查询名称
			pageInfo.getList().forEach(x -> {
				x.setBelongGroupName(belongGroupMap.getOrDefault(x.getBelongGroup(), ""));
				if (Objects.nonNull(x.getSonCompanyId())) {
					x.setSonCompanyName(sonCompanyMap.getOrDefault(String.valueOf(x.getSonCompanyId()), ""));
				}
			});
			PageResult<TblFwglAnnualExamineOracle> build = new PageResult<TblFwglAnnualExamineOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}
}
