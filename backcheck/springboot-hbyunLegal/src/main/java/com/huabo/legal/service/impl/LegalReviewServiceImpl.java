package com.huabo.legal.service.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.mysql.service.TblFwglFileMySqlService;
import com.huabo.legal.mysql.service.TblFwglInstitutionAuditExtMySqlService;
import com.huabo.legal.mysql.service.TblFwglInstitutionAuditMySqlService;
import com.huabo.legal.mysql.service.TblStaffMySqlService;
import com.huabo.legal.oracle.entity.TblFwglFileOracle;
import com.huabo.legal.oracle.entity.TblFwglInstitutionAuditExtOracle;
import com.huabo.legal.oracle.entity.TblFwglInstitutionAuditOracle;
import com.huabo.legal.oracle.service.TblFwglFileOracleService;
import com.huabo.legal.oracle.service.TblFwglInstitutionAuditExtOracleService;
import com.huabo.legal.oracle.service.TblFwglInstitutionAuditOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.service.FileUploadService;
import com.huabo.legal.service.LegalReviewService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.vo.param.TblFwglInstitutionAuditQueryParam;
import com.huabo.legal.vo.result.TblFwglInstitutionAudit;
import com.huabo.legal.vo.result.TblFwglInstitutionAuditExt;
import com.huabo.legal.vo.result.UserInfo;
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
public class LegalReviewServiceImpl implements LegalReviewService {

	@Resource
	private TblFwglInstitutionAuditMySqlService tblFwglInstitutionAuditMySqlService;
	@Resource
	private TblFwglInstitutionAuditOracleService tblFwglInstitutionAuditOracleService;
	@Resource
	private TblFwglFileMySqlService tblFwglFileMySqlService;
	@Resource
	private TblFwglFileOracleService tblFwglFileOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblFwglInstitutionAuditExtMySqlService tblFwglInstitutionAuditExtMySqlService;
	@Resource
	private TblFwglInstitutionAuditExtOracleService tblFwglInstitutionAuditExtOracleService;
	@Resource
	private TblStaffMySqlService tblStaffMySqlService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 制度审核/经营事项审核列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglInstitutionAuditList(TblFwglInstitutionAuditQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglInstitutionAuditOracle> pageInfo = tblFwglInstitutionAuditOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			//查询创建者
			String creatorIds = pageInfo.getList().stream().map(TblFwglInstitutionAuditOracle::getCreator).distinct()
					.collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(creatorIds)) {
				List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(creatorIds);
				if (CollectionUtil.isNotEmpty(userInfos)) {
					Map<String, String> map = userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
					pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCreator()))
							.forEach(x -> x.setCreatorName(map.getOrDefault(x.getCreator(), "")));
				}
			}
			//查询部门
			String workUnitIds = pageInfo.getList().stream().map(TblFwglInstitutionAuditOracle::getWorkUnit).distinct()
					.collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(workUnitIds)) {
				List<UserInfo> workUnits = tblStaffOracleService.getWorkUnitIdUserInfos(workUnitIds);
				if (CollectionUtil.isNotEmpty(workUnits)) {
					Map<String, String> workUnitMap = workUnits.stream()
							.collect(Collectors.toMap(UserInfo::getWorkUnitId, UserInfo::getWorkUnitName));
					pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getWorkUnit()))
							.forEach(x -> x.setWorkUnitName(workUnitMap.getOrDefault(x.getWorkUnit(), "")));
				}
			}
			PageResult<TblFwglInstitutionAuditOracle> result = new PageResult<TblFwglInstitutionAuditOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, result);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 制度审核/经营事项审核 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglInstitutionAudit(TblFwglInstitutionAudit param) {
		TblFwglInstitutionAuditOracle fwglInstitutionAudit = new TblFwglInstitutionAuditOracle();
		BeanUtils.copyProperties(param, fwglInstitutionAudit);
		TblFwglInstitutionAuditOracle institutionAudit = tblFwglInstitutionAuditOracleService.saveOrUpdate(fwglInstitutionAudit);
		return ResponseFormat.retParam(200, 200, institutionAudit);
	}

	/**
	 * 制度审核/经营事项审核 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteTblFwglInstitutionAudit(Long id) {
		TblFwglInstitutionAuditOracle institutionAudit = tblFwglInstitutionAuditOracleService.findById(id);
		//制度审核/经营事项审核 刪除
		tblFwglInstitutionAuditOracleService.delete(id);
		//制度审核-制度 删除
		if (StringUtils.isNotBlank(institutionAudit.getInstitutionAuditExtId())) {
			List<String> fileIds = Arrays.asList(institutionAudit.getInstitutionAuditExtId().split(","));
			fileIds.forEach(x -> tblFwglInstitutionAuditExtOracleService.delete(Long.valueOf(x)));
		}
		//制度审核/经营事项审核-文件 删除
		if (StringUtils.isNotBlank(institutionAudit.getFileIds())) {
			List<String> fileIds = Arrays.asList(institutionAudit.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 制度审核/经营事项审核详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglInstitutionAudit(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//制度审核/经营事项审核 详情
		TblFwglInstitutionAuditOracle institutionAudit = tblFwglInstitutionAuditOracleService.findById(id);
		//参数组装 集团、创建者 名称
		institutionAudit.setWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(Long.valueOf(institutionAudit.getWorkUnit())));
		institutionAudit.setCreatorName(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(institutionAudit.getCreator())));
		//各部室及所属公司
		if (StringUtils.isNotBlank(institutionAudit.getAuditBelongGroupId())) {
			List<UserInfo> workUnitList = tblStaffOracleService.getWorkUnitIdUserInfos(institutionAudit.getAuditBelongGroupId());
			map.put("workUnitList", workUnitList);
		}
		//制度审核-制度 列表
		if (StringUtils.isNotBlank(institutionAudit.getInstitutionAuditExtId())) {
			List<TblFwglInstitutionAuditExtOracle> institutionAuditExtList = tblFwglInstitutionAuditExtOracleService
					.getList(institutionAudit.getInstitutionAuditExtId());
			//制度审核，制度文件详情
			for (TblFwglInstitutionAuditExtOracle tblFwglInstitutionAuditExtOracle : institutionAuditExtList) {
				if (StringUtils.isNotEmpty(tblFwglInstitutionAuditExtOracle.getFileIds())) {
					List<TblFwglFileOracle> institutionAuditfiles = tblFwglFileOracleService.findByIds(tblFwglInstitutionAuditExtOracle.getFileIds());
					map.put("institutionAuditfiles", institutionAuditfiles);
				}
			}
			map.put("institutionAuditExtList", institutionAuditExtList);
		}
		//制度审核/经营事项审核-文件 列表
		if (StringUtils.isNotEmpty(institutionAudit.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(institutionAudit.getFileIds());
			map.put("files", files);
		}
		map.put("institutionAudit", institutionAudit);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 审核台账列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getAllList(TblFwglInstitutionAuditQueryParam param) {
		//TODO("台账权限问题 处理")
		param.setBelongGroup(null);
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglInstitutionAuditOracle> pageInfo = tblFwglInstitutionAuditOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			//查询创建者
			String creatorIds = pageInfo.getList().stream().map(TblFwglInstitutionAuditOracle::getCreator).distinct()
					.collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(creatorIds)) {
				List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(creatorIds);
				if (CollectionUtil.isNotEmpty(userInfos)) {
					Map<String, String> map = userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
					pageInfo.getList().forEach(x -> x.setCreatorName(map.getOrDefault(x.getCreator(), "")));
				}
			}
			//查询部门
			String workUnitIds = pageInfo.getList().stream().map(TblFwglInstitutionAuditOracle::getWorkUnit).distinct()
					.collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(workUnitIds)) {
				List<UserInfo> workUnits = tblStaffOracleService.getWorkUnitIdUserInfos(workUnitIds);
				if (CollectionUtil.isNotEmpty(workUnits)) {
					Map<String, String> workUnitMap = workUnits.stream()
							.collect(Collectors.toMap(UserInfo::getWorkUnitId, UserInfo::getWorkUnitName));
					pageInfo.getList().forEach(x -> x.setWorkUnitName(workUnitMap.getOrDefault(x.getWorkUnit(), "")));
				}
			}
			PageResult<TblFwglInstitutionAuditOracle> result = new PageResult<TblFwglInstitutionAuditOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, result);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 制度审核-制度 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglInstitutionAuditExt(TblFwglInstitutionAuditExt param) {
		TblFwglInstitutionAuditExtOracle fwglInstitutionAuditExt = new TblFwglInstitutionAuditExtOracle();
		BeanUtils.copyProperties(param, fwglInstitutionAuditExt);
		TblFwglInstitutionAuditExtOracle institutionAuditExt = tblFwglInstitutionAuditExtOracleService.saveOrUpdate(fwglInstitutionAuditExt);
		return ResponseFormat.retParam(200, 200, institutionAuditExt);
	}

	/**
	 * 制度审核-制度 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglInstitutionAuditExt(Long id) {
		TblFwglInstitutionAuditExtOracle institutionAuditExt = tblFwglInstitutionAuditExtOracleService.findById(id);
		//制度审核-制度 刪除
		tblFwglInstitutionAuditExtOracleService.delete(id);
		//制度审核-制度-文件 删除
		if (StringUtils.isNotBlank(institutionAuditExt.getFileIds())) {
			List<String> fileIds = Arrays.asList(institutionAuditExt.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 制度审核-制度详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglInstitutionAuditExt(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		TblFwglInstitutionAuditExtOracle institutionAuditExt = tblFwglInstitutionAuditExtOracleService.findById(id);
		//制度审核/经营事项审核-文件列表
		if (StringUtils.isNotEmpty(institutionAuditExt.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(institutionAuditExt.getFileIds());
			map.put("files", files);
		}
		map.put("institutionAuditExt", institutionAuditExt);
		return ResponseFormat.retParam(200, 200, map);
	}
}
