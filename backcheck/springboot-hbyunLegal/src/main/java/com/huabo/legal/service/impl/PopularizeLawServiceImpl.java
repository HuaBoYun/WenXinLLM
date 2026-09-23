package com.huabo.legal.service.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.oracle.entity.*;
import com.huabo.legal.oracle.service.*;
import com.huabo.legal.service.FileUploadService;
import com.huabo.legal.service.PopularizeLawService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.vo.param.*;
import com.huabo.legal.vo.result.*;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PopularizeLawServiceImpl implements PopularizeLawService {

	@Resource
	private TblFwglPopularizeLawPlanOracleService tblFwglPopularizeLawPlanOracleService;
	@Resource
	private TblFwglFileOracleService tblFwglFileOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblFwglActivityManagementOracleService tblFwglActivityManagementOracleService;
	@Resource
	private TblFwglSubjectManagementOracleService tblFwglSubjectManagementOracleService;
	@Resource
	private TblFwglXfExamOracleService tblFwglXfExamOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private TblFwglLeaderXfOracleService tblFwglLeaderXfOracleService;

	/**
	 * 普法计划列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglPopularizeLawPlanList(TblFwglPopularizeLawPlanQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglPopularizeLawPlanOracle> pageInfo = tblFwglPopularizeLawPlanOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			Map<String, String> belongGroupMap = new HashMap<>();
			List<String> belongGroup = pageInfo.getList().stream().map(TblFwglPopularizeLawPlanOracle::getBelongGroup).distinct()
					.collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(belongGroup)) {
				belongGroupMap = tblStaffOracleService.getBelongGroupIdUserInfoMap(StringUtils.join(belongGroup, ","));
			}
			Map<String, String> finalBelongGroupMap = belongGroupMap;
			pageInfo.getList().forEach(x -> {
				x.setBelongGroupName(finalBelongGroupMap.getOrDefault(x.getBelongGroup(), ""));
			});
			PageResult<TblFwglPopularizeLawPlanOracle> build = new PageResult<TblFwglPopularizeLawPlanOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 普法计划 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglPopularizeLawPlan(TblFwglPopularizeLawPlan param) {
		TblFwglPopularizeLawPlanOracle fwglPopularizeLawPlan = new TblFwglPopularizeLawPlanOracle();
		BeanUtils.copyProperties(param, fwglPopularizeLawPlan);
		TblFwglPopularizeLawPlanOracle popularizeLawPlan = tblFwglPopularizeLawPlanOracleService.saveOrUpdate(fwglPopularizeLawPlan);
		return ResponseFormat.retParam(200, 200, popularizeLawPlan);
	}

	/**
	 * 普法计划 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteFwglPopularizeLawPlan(Long id) {
		TblFwglPopularizeLawPlanOracle popularizeLawPlan = tblFwglPopularizeLawPlanOracleService.findById(id);
		//普法计划 刪除
		tblFwglPopularizeLawPlanOracleService.delete(id);
		//普法计划-文件 删除
		if (StringUtils.isNotBlank(popularizeLawPlan.getFileIds())) {
			List<String> fileIds = Arrays.asList(popularizeLawPlan.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 普法计划详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglPopularizeLawPlan(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//普法计划详情
		TblFwglPopularizeLawPlanOracle popularizeLawPlan = tblFwglPopularizeLawPlanOracleService.findById(id);
		//普法计划-文件列表
		if (StringUtils.isNotEmpty(popularizeLawPlan.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(popularizeLawPlan.getFileIds());
			map.put("files", files);
		}
		map.put("popularizeLawPlan", popularizeLawPlan);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 活动管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglActivityManagementList(TblFwglActivityManagementQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglActivityManagementOracle> pageInfo = tblFwglActivityManagementOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			//查询公司（工作单位）
			String blogGroupIds = pageInfo.getList().stream().map(TblFwglActivityManagementOracle::getBelongGroup).distinct()
					.collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(blogGroupIds)) {
				List<UserInfo> blogGroups = tblStaffOracleService.getBelongGroupIdUserInfos(blogGroupIds);
				if (CollectionUtil.isNotEmpty(blogGroups)) {
					Map<String, String> blogGroupMap = blogGroups.stream()
							.collect(Collectors.toMap(UserInfo::getBelongGroupId, UserInfo::getBelongGroupName));
					pageInfo.getList().forEach(x -> x.setWorkUnitName(blogGroupMap.getOrDefault(x.getBelongGroup(), "")));
				}
			}
			PageResult<TblFwglActivityManagementOracle> build = new PageResult<TblFwglActivityManagementOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 活动管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglActivityManagement(TblFwglActivityManagement param) {
		TblFwglActivityManagementOracle fwglActivityManagement = new TblFwglActivityManagementOracle();
		BeanUtils.copyProperties(param, fwglActivityManagement);
		TblFwglActivityManagementOracle activityManagement = tblFwglActivityManagementOracleService.saveOrUpdate(fwglActivityManagement);
		return ResponseFormat.retParam(200, 200, activityManagement);
	}

	/**
	 * 活动管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteTblFwglActivityManagement(Long id) {
		TblFwglActivityManagementOracle activityManagement = tblFwglActivityManagementOracleService.findById(id);
		//活动管理 刪除
		tblFwglActivityManagementOracleService.delete(id);
		//活动管理-上传活动附件-培训通知-文件 删除
		if (StringUtils.isNotBlank(activityManagement.getActivityFileIds())) {
			List<String> fileIds = Arrays.asList(activityManagement.getActivityFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		//活动管理-上传活动附件-其他-文件 删除
		if (StringUtils.isNotBlank(activityManagement.getActivityOtherFileIds())) {
			List<String> fileIds = Arrays.asList(activityManagement.getActivityOtherFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 活动管理详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglActivityManagement(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		TblFwglActivityManagementOracle activityManagement = tblFwglActivityManagementOracleService.findById(id);
		if (StringUtils.isNotBlank(activityManagement.getInformPersonnel())) {
			List<UserInfo> creatorUserInfos = tblStaffOracleService.getCreatorUserInfos(activityManagement.getInformPersonnel());
			if (CollectionUtil.isNotEmpty(creatorUserInfos)) {
				List<String> collect = creatorUserInfos.stream().map(UserInfo::getRealName).collect(Collectors.toList());
				if (CollectionUtil.isNotEmpty(collect)) {
					activityManagement.setInformPersonnelName(StringUtils.join(collect, ","));
				}
			}
		}
		activityManagement.setWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(Long.valueOf(activityManagement.getWorkUnit())));
		if (StringUtils.isNotEmpty(activityManagement.getSitePhoto())) {
			List<TblFwglFileOracle> sitePhotoFile = tblFwglFileOracleService.findByIds(activityManagement.getSitePhoto());
			map.put("sitePhotoFile", sitePhotoFile);
		}
		if (StringUtils.isNotEmpty(activityManagement.getActivityFileIds())) {
			List<TblFwglFileOracle> activityFile = tblFwglFileOracleService.findByIds(activityManagement.getActivityFileIds());
			map.put("activityFile", activityFile);
		}
		if (StringUtils.isNotEmpty(activityManagement.getActivityOtherFileIds())) {
			List<TblFwglFileOracle> activityOtherFile = tblFwglFileOracleService.findByIds(activityManagement.getActivityOtherFileIds());
			map.put("activityOtherFile", activityOtherFile);
		}
		map.put("activityManagement", activityManagement);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 课题管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglSubjectManagementList(TblFwglSubjectManagementQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglSubjectManagementOracle> pageInfo = tblFwglSubjectManagementOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			//查询创建者
			String creatorIds = pageInfo.getList().stream().map(TblFwglSubjectManagementOracle::getCreator).distinct()
					.collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(creatorIds)) {
				List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(creatorIds);
				if (CollectionUtil.isNotEmpty(userInfos)) {
					Map<String, String> map = userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
					pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCreator()))
							.forEach(x -> x.setCreatorName(map.getOrDefault(x.getCreator(), "")));
				}
			}
			PageResult<TblFwglSubjectManagementOracle> result = new PageResult<TblFwglSubjectManagementOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, result);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 课题管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglSubjectManagement(TblFwglSubjectManagement param) {
		TblFwglSubjectManagementOracle tblFwglSubjectManagement = new TblFwglSubjectManagementOracle();
		BeanUtils.copyProperties(param, tblFwglSubjectManagement);
		TblFwglSubjectManagementOracle subjectManagement = tblFwglSubjectManagementOracleService.saveOrUpdate(tblFwglSubjectManagement);
		return ResponseFormat.retParam(200, 200, subjectManagement);
	}

	/**
	 * 课题管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteTblFwglSubjectManagement(Long id) {
		TblFwglSubjectManagementOracle subjectManagement = tblFwglSubjectManagementOracleService.findById(id);
		//课题管理 刪除
		tblFwglSubjectManagementOracleService.delete(id);
		//课题管理-文件 删除
		if (StringUtils.isNotBlank(subjectManagement.getFileIds())) {
			List<String> fileIds = Arrays.asList(subjectManagement.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 课题管理详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglSubjectManagement(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//课题管理详情
		TblFwglSubjectManagementOracle subjectManagement = tblFwglSubjectManagementOracleService.findById(id);
		//课题管理-文件列表
		if (StringUtils.isNotEmpty(subjectManagement.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(subjectManagement.getFileIds());
			map.put("files", files);
		}
		map.put("activityManagement", subjectManagement);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 学法考试列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglXfExamList(TblFwglXfExamQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglXfExamOracle> pageInfo = tblFwglXfExamOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			PageResult<TblFwglXfExamOracle> build = new PageResult<TblFwglXfExamOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());


	}

	/**
	 * 学法考试 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglXfExam(TblFwglXfExam param) {
		TblFwglXfExamOracle fwglXfExam = new TblFwglXfExamOracle();
		BeanUtils.copyProperties(param, fwglXfExam);
		TblFwglXfExamOracle xfExam = tblFwglXfExamOracleService.saveOrUpdate(fwglXfExam);
		return ResponseFormat.retParam(200, 200, xfExam);
	}

	/**
	 * 学法考试 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteTblFwglXfExam(Long id) {
		TblFwglXfExamOracle xfExam = tblFwglXfExamOracleService.findById(id);
		//学法考试 刪除
		tblFwglXfExamOracleService.delete(id);
		//学法考试-文件 删除
		if (StringUtils.isNotBlank(xfExam.getFileIds())) {
			List<String> fileIds = Arrays.asList(xfExam.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 学法考试详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglXfExam(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//学法考试详情
		TblFwglXfExamOracle xfExam = tblFwglXfExamOracleService.findById(id);
		//学法考试-文件列表
		if (StringUtils.isNotEmpty(xfExam.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(xfExam.getFileIds());
			map.put("files", files);
		}
		map.put("xfExam", xfExam);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 领导学法列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglLeaderXfList(TblFwglLeaderXfQueryParam param) {
		param.setCreator(null);
		param.setWorkUnit(null);
		PageInfo<TblFwglLeaderXfOracle> pageInfo = tblFwglLeaderXfOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			Map<String, String> workUnitMap = new HashMap<>();
			List<UserInfo> workUnits;
			List<String> workUnit = pageInfo.getList().stream().map(TblFwglLeaderXfOracle::getWorkUnit).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(workUnit)) {
				workUnits = tblStaffOracleService.getWorkUnitIdUserInfos(StringUtils.join(workUnit, ","));
				if (CollectionUtil.isNotEmpty(workUnits)) {
					workUnitMap = workUnits.stream().collect(Collectors.toMap(UserInfo::getWorkUnitId, UserInfo::getWorkUnitName));
				}
			}
			//组装列表返回参数
			Map<String, String> finalWorkUnitMap = workUnitMap;
			pageInfo.getList().forEach(x -> x.setWorkUnitName(finalWorkUnitMap.getOrDefault(x.getWorkUnit(), "")));
			PageResult<TblFwglLeaderXfOracle> build = new PageResult<TblFwglLeaderXfOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 领导学法 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglLeaderXf(TblFwglLeaderXf param) {
		TblFwglLeaderXfOracle tblFwglLeaderXf = new TblFwglLeaderXfOracle();
		BeanUtils.copyProperties(param, tblFwglLeaderXf);
		TblFwglLeaderXfOracle tblFwglLeaderXfOracle = tblFwglLeaderXfOracleService.saveOrUpdate(tblFwglLeaderXf);
		return ResponseFormat.retParam(200, 200, tblFwglLeaderXfOracle);
	}

	/**
	 * 领导学法 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglLeaderXf(Long id) {
		TblFwglLeaderXfOracle leaderXf = tblFwglLeaderXfOracleService.findById(id);
		//领导学法 刪除
		tblFwglLeaderXfOracleService.delete(id);
		//领导学法-文件 刪除
		if (StringUtils.isNotBlank(leaderXf.getFileIds())) {
			List<String> fileIds = Arrays.asList(leaderXf.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 领导学法详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglLeaderXf(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		TblFwglLeaderXfOracle leaderXf = tblFwglLeaderXfOracleService.findById(id);
		leaderXf.setCreatorName(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(leaderXf.getCreator())));
		leaderXf.setWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(Long.valueOf(leaderXf.getWorkUnit())));
		if (StringUtils.isNotEmpty(leaderXf.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(leaderXf.getFileIds());
			map.put("files", files);
		}
		map.put("leaderXf", leaderXf);
		return ResponseFormat.retParam(200, 200, map);
	}
}
