package com.huabo.legal.service.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.constant.ActivityCategoryType;
import com.huabo.legal.oracle.entity.*;
import com.huabo.legal.oracle.service.*;
import com.huabo.legal.service.FileUploadService;
import com.huabo.legal.service.FirmLawyerService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.vo.param.TblFwglPracticeActivityQueryParam;
import com.huabo.legal.vo.param.TblFwglPracticeApplyQueryParam;
import com.huabo.legal.vo.param.TblFwglPracticeExamineQueryParam;
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
public class FirmLawyerServiceImpl implements FirmLawyerService {

	@Resource
	private TblFwglPracticeApplyOracleService tblFwglPracticeApplyOracleService;
	@Resource
	private TblFwglFileOracleService tblFwglFileOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblFwglPracticeActivityOracleService tblFwglPracticeActivityOracleService;
	@Resource
	private TblFwglPracticeExamineOracleService tblFwglPracticeExamineOracleService;
	@Resource
	private TblFwglPracticeApplyExtOracleService tblFwglPracticeApplyExtOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 执业申请列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglPracticeApplyList(TblFwglPracticeApplyQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglPracticeApplyOracle> pageInfo = tblFwglPracticeApplyOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			PageResult<TblFwglPracticeApplyOracle> build = new PageResult<TblFwglPracticeApplyOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 执业申请 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglPracticeApply(TblFwglPracticeApply param) {
		TblFwglPracticeApplyOracle tblFwglPracticeApply = new TblFwglPracticeApplyOracle();
		BeanUtils.copyProperties(param, tblFwglPracticeApply);
		TblFwglPracticeApplyOracle practiceApply = tblFwglPracticeApplyOracleService.saveOrUpdate(tblFwglPracticeApply);
		return ResponseFormat.retParam(200, 200, practiceApply);
	}

	/**
	 * 执业申请 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteTblFwglPracticeApply(Long id) {
		TblFwglPracticeApplyOracle practiceApply = tblFwglPracticeApplyOracleService.findById(id);
		//执业申请 刪除
		tblFwglPracticeApplyOracleService.delete(id);
		//执业申请-简历 刪除
		if (StringUtils.isNotBlank(practiceApply.getPracticeApplyExtId())) {
			List<String> practiceApplyExtId = Arrays.asList(practiceApply.getPracticeApplyExtId().split(","));
			practiceApplyExtId.forEach(x -> tblFwglPracticeApplyExtOracleService.delete(Long.valueOf(x)));
		}
		//执业申请-文件 删除
		if (StringUtils.isNotBlank(practiceApply.getFileIds())) {
			List<String> fileIds = Arrays.asList(practiceApply.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		//执业申请-文件 删除(律师资格证书或法律职业资格证书（正、副本）复印件)
		if (StringUtils.isNotBlank(practiceApply.getCertificateFileIds())) {
			List<String> fileIds = Arrays.asList(practiceApply.getCertificateFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		//执业申请-文件 删除(在合同有效期内的劳动合同复印件)
		if (StringUtils.isNotBlank(practiceApply.getContractFileIds())) {
			List<String> fileIds = Arrays.asList(practiceApply.getContractFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		//执业申请-文件 删除(申请人符合《浙资运营律师管理办法》第七条第四项规定条件的工作经历、执业经历证明)
		if (StringUtils.isNotBlank(practiceApply.getProveFileIds())) {
			List<String> fileIds = Arrays.asList(practiceApply.getProveFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		//执业申请-文件 删除(申请人居民身份证复印件)
		if (StringUtils.isNotBlank(practiceApply.getIdentityCardFileIds())) {
			List<String> fileIds = Arrays.asList(practiceApply.getIdentityCardFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		//执业申请-文件 删除(公司律师执业证号)
		if (StringUtils.isNotBlank(practiceApply.getCertificateNumberFileIds())) {
			List<String> fileIds = Arrays.asList(practiceApply.getCertificateNumberFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		//执业申请-文件 删除(公司律师执业证书附件)
		if (StringUtils.isNotBlank(practiceApply.getPractisingFileIds())) {
			List<String> fileIds = Arrays.asList(practiceApply.getPractisingFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 执业申请详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglPracticeApply(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//执业申请详情
		TblFwglPracticeApplyOracle practiceApply = tblFwglPracticeApplyOracleService.findById(id);
		if (practiceApply.getStaffId() != null) {
			practiceApply.setRealName(tblStaffOracleService.getCreatorUserInfo(practiceApply.getStaffId()));
		}
		//执业申请-简历
		if (StringUtils.isNotBlank(practiceApply.getPracticeApplyExtId())) {
			List<TblFwglPracticeApplyExtOracle> practiceApplyExt = tblFwglPracticeApplyExtOracleService
					.getList(practiceApply.getPracticeApplyExtId());
			map.put("practiceApplyExt", practiceApplyExt);
		}
		//执业申请-文件列表(律师资格证书或法律职业资格证书（正、副本）复印件)
		if (StringUtils.isNotEmpty(practiceApply.getCertificateFileIds())) {
			List<TblFwglFileOracle> certificateFiles = tblFwglFileOracleService.findByIds(practiceApply.getCertificateFileIds());
			map.put("certificateFiles", certificateFiles);
		}
		//执业申请-文件列表(在合同有效期内的劳动合同复印件)
		if (StringUtils.isNotEmpty(practiceApply.getContractFileIds())) {
			List<TblFwglFileOracle> contractFiles = tblFwglFileOracleService.findByIds(practiceApply.getContractFileIds());
			map.put("contractFiles", contractFiles);
		}
		//执业申请-文件列表(申请人符合《浙资运营律师管理办法》第七条第四项规定条件的工作经历、执业经历证明)
		if (StringUtils.isNotEmpty(practiceApply.getProveFileIds())) {
			List<TblFwglFileOracle> proveFiles = tblFwglFileOracleService.findByIds(practiceApply.getProveFileIds());
			map.put("proveFiles", proveFiles);
		}
		//执业申请-文件列表(申请人居民身份证复印件)
		if (StringUtils.isNotEmpty(practiceApply.getIdentityCardFileIds())) {
			List<TblFwglFileOracle> identityCardFiles = tblFwglFileOracleService.findByIds(practiceApply.getIdentityCardFileIds());
			map.put("identityCardFiles", identityCardFiles);
		}
		//执业申请-文件列表(申请人居民身份证复印件)
		if (StringUtils.isNotEmpty(practiceApply.getCertificateNumberFileIds())) {
			List<TblFwglFileOracle> certificateNumberFiles = tblFwglFileOracleService.findByIds(practiceApply.getCertificateNumberFileIds());
			map.put("certificateNumberFiles", certificateNumberFiles);
		}
		//执业申请-文件列表
		if (StringUtils.isNotEmpty(practiceApply.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(practiceApply.getFileIds());
			map.put("files", files);
		}
		//执业申请(公司律师执业证书附件)
		if (StringUtils.isNotEmpty(practiceApply.getPractisingFileIds())) {
			List<TblFwglFileOracle> practiSingFiles = tblFwglFileOracleService.findByIds(practiceApply.getPractisingFileIds());
			map.put("practiSingFiles", practiSingFiles);
		}
		map.put("practiceApply", practiceApply);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 人员台账列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getPersonnelAllList(TblFwglPracticeApplyQueryParam param) {
		//台账权限
		param.setBelongGroup(null);
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglPracticeApplyOracle> pageInfo = tblFwglPracticeApplyOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			PageResult<TblFwglPracticeApplyOracle> build = new PageResult<TblFwglPracticeApplyOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 执业活动列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglPracticeActivityList(TblFwglPracticeActivityQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglPracticeActivityOracle> pageInfo = tblFwglPracticeActivityOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			//查询创建者ID
			String creatorIds = pageInfo.getList().stream().map(TblFwglPracticeActivityOracle::getCreator).distinct()
					.collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(creatorIds)) {
				List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(creatorIds);
				if (CollectionUtil.isNotEmpty(userInfos)) {
					Map<String, String> map = userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
					pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCreator()))
							.forEach(x -> x.setCreatorName(map.getOrDefault(x.getCreator(), "")));
				}
			}
			PageResult<TblFwglPracticeActivityOracle> result = new PageResult<TblFwglPracticeActivityOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, result);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 执业活动 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglPracticeActivity(TblFwglPracticeActivity param) {
		TblFwglPracticeActivityOracle tblFwglPracticeActivity = new TblFwglPracticeActivityOracle();
		BeanUtils.copyProperties(param, tblFwglPracticeActivity);
		TblFwglPracticeActivityOracle practiceActivity = tblFwglPracticeActivityOracleService.saveOrUpdate(tblFwglPracticeActivity);
		return ResponseFormat.retParam(200, 200, practiceActivity);
	}

	/**
	 * 执业活动 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteTblFwglPracticeActivity(Long id) {
		TblFwglPracticeActivityOracle practiceActivity = tblFwglPracticeActivityOracleService.findById(id);
		//执业活动 刪除
		tblFwglPracticeActivityOracleService.delete(id);
		//执业活动-文件 删除
		if (StringUtils.isNotBlank(practiceActivity.getFileIds())) {
			List<String> fileIds = Arrays.asList(practiceActivity.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 执业活动详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglPracticeActivity(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//执业活动详情
		TblFwglPracticeActivityOracle practiceActivity = tblFwglPracticeActivityOracleService.findById(id);
		practiceActivity.setCreatorName(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(practiceActivity.getCreator())));
		//执业活动-文件列表
		if (StringUtils.isNotEmpty(practiceActivity.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(practiceActivity.getFileIds());
			map.put("files", files);
		}
		map.put("practiceActivity", practiceActivity);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 执业考核列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglPracticeExamineList(TblFwglPracticeExamineQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglPracticeExamineOracle> pageInfo = tblFwglPracticeExamineOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			//查询创建者ID
			String creatorIds = pageInfo.getList().stream().map(TblFwglPracticeExamineOracle::getCreator).distinct().collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(creatorIds)) {
				List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(creatorIds);
				if (CollectionUtil.isNotEmpty(userInfos)) {
					Map<String, String> map = userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
					pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCreator()))
							.forEach(x -> x.setCreatorName(map.getOrDefault(x.getCreator(), "")));
				}
			}
			String workUnitIds = pageInfo.getList().stream().map(TblFwglPracticeExamineOracle::getPracticeExamineBelongGroup).distinct()
					.collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(workUnitIds)) {
				List<UserInfo> userInfos = tblStaffOracleService.getWorkUnitIdUserInfos(workUnitIds);
				if (CollectionUtil.isNotEmpty(userInfos)) {
					Map<String, String> map = userInfos.stream().collect(Collectors.toMap(UserInfo::getWorkUnitId, UserInfo::getWorkUnitName));
					pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getPracticeExamineBelongGroup()))
							.forEach(x -> x.setPracticeExamineBelongGroupName(map.getOrDefault(x.getPracticeExamineBelongGroup(), "")));
				}
			}
			PageResult<TblFwglPracticeExamineOracle> build = new PageResult<TblFwglPracticeExamineOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 执业考核 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglPracticeExamine(TblFwglPracticeExamine param) {
		TblFwglPracticeExamineOracle tblFwglPracticeExamine = new TblFwglPracticeExamineOracle();
		BeanUtils.copyProperties(param, tblFwglPracticeExamine);
		TblFwglPracticeExamineOracle practiceExamine = tblFwglPracticeExamineOracleService.saveOrUpdate(tblFwglPracticeExamine);
		return ResponseFormat.retParam(200, 200, practiceExamine);
	}

	/**
	 * 执业考核 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglPracticeExamine(Long id) {
		TblFwglPracticeExamineOracle practiceExamine = tblFwglPracticeExamineOracleService.findById(id);
		//执业考核 刪除
		tblFwglPracticeExamineOracleService.delete(id);
		//执业考核-文件 删除
		if (StringUtils.isNotBlank(practiceExamine.getFileIds())) {
			List<String> fileIds = Arrays.asList(practiceExamine.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 执业考核详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglPracticeExamine(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//执业考核详情
		TblFwglPracticeExamineOracle practiceExamine = tblFwglPracticeExamineOracleService.findById(id);
		practiceExamine.setCreatorName(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(practiceExamine.getCreator())));
		if (StringUtils.isNotBlank(practiceExamine.getPracticeExamineBelongGroup())) {
			practiceExamine.setPracticeExamineBelongGroupName(
					tblStaffOracleService.getWorkUnitIdUserInfo(Long.valueOf(practiceExamine.getPracticeExamineBelongGroup())));
		}
		//执业考核-文件列表
		if (StringUtils.isNotEmpty(practiceExamine.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(practiceExamine.getFileIds());
			map.put("files", files);
		}
		map.put("practiceExamine", practiceExamine);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 执业申请-简历 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglPracticeApplyExt(TblFwglPracticeApplyExt param) {
		TblFwglPracticeApplyExtOracle tblFwglPracticeApplyExt = new TblFwglPracticeApplyExtOracle();
		BeanUtils.copyProperties(param, tblFwglPracticeApplyExt);
		TblFwglPracticeApplyExtOracle practiceApplyExt = tblFwglPracticeApplyExtOracleService.saveOrUpdate(tblFwglPracticeApplyExt);
		return ResponseFormat.retParam(200, 200, practiceApplyExt);
	}

	/**
	 * 执业申请-简历 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglPracticeApplyExt(Long id) {
		tblFwglPracticeApplyExtOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 执业申请-简历详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglPracticeApplyExt(Long id) {
		TblFwglPracticeApplyExtOracle practiceApplyExt = tblFwglPracticeApplyExtOracleService.findById(id);
		return ResponseFormat.retParam(200, 200, practiceApplyExt);
	}

	/**
	 * 人员台账详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getPersonnel(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//执业申请详情
		TblFwglPracticeApplyOracle personnel = tblFwglPracticeApplyOracleService.findById(id);
		if (personnel.getStaffId() != null) {
			personnel.setRealName(tblStaffOracleService.getCreatorUserInfo(personnel.getStaffId()));
		}
		//执业申请-简历
		if (StringUtils.isNotBlank(personnel.getPracticeApplyExtId())) {
			List<TblFwglPracticeApplyExtOracle> practiceApplyExt = tblFwglPracticeApplyExtOracleService.getList(personnel.getPracticeApplyExtId());
			map.put("practiceApplyExt", practiceApplyExt);
		}
		if (personnel.getStaffId() != null) {
			map.putAll(getPersonnelInfo(personnel.getStaffId()));
		}
		map.put("personnel", personnel);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 人员关联信息
	 * @param staffId
	 * @return
	 */
	private Map<String, Object> getPersonnelInfo(Long staffId) {
		Map<String, Object> map = new HashMap<>();
		//文章发表
		List<TblFwglPracticeActivityOracle> articleList = tblFwglPracticeActivityOracleService
				.getPracticeActivityList(staffId, ActivityCategoryType.ARTICLE);
		map.put("articleList", articleList);
		//法律培训
		List<TblFwglPracticeActivityOracle> legalReviewList = tblFwglPracticeActivityOracleService
				.getPracticeActivityList(staffId, ActivityCategoryType.LEGAL_REVIEW);
		map.put("legalReviewList", legalReviewList);
		//法律尽调
		List<TblFwglPracticeActivityOracle> legalList = tblFwglPracticeActivityOracleService
				.getPracticeActivityList(staffId, ActivityCategoryType.LEGAL);
		map.put("legalList", legalList);
		return map;
	}

	/**
	 * 执业活动-人员详情 查询
	 * @param staffId
	 * @return
	 */
	@Override
	public JsonBean getPersonnelExt(Long staffId) {
		HashMap<String, Object> map = new HashMap<>();
		Map<String, Object> personnelInfo = getPersonnelInfo(staffId);
		map.putAll(personnelInfo);
		return ResponseFormat.retParam(200, 200, map);
	}
}
