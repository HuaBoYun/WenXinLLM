package com.huabo.legal.service.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.oracle.entity.*;
import com.huabo.legal.oracle.service.*;
import com.huabo.legal.service.FileUploadService;
import com.huabo.legal.service.OrganizeInformationService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.vo.param.TblFwglFirmLawyerQueryParam;
import com.huabo.legal.vo.param.TblFwglLegalAdviserQueryParam;
import com.huabo.legal.vo.param.TblFwglLegalOrganizationQueryParam;
import com.huabo.legal.vo.param.TblFwglLegalPersonnelQueryParam;
import com.huabo.legal.vo.result.*;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrganizeInformationServiceImpl implements OrganizeInformationService {

	@Resource
	private TblFwglFirmLawyerOracleService tblFwglFirmLawyerOracleService;
	@Resource
	private TblFwglFirmLawyerExtOracleService tblFwglFirmLawyerExtOracleService;
	@Resource
	private TblFwglFileOracleService tblFwglFileOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblFwglLegalOrganizationOracleService tblFwglLegalOrganizationOracleService;
	@Resource
	private TblFwglLegalOrganizationExtOracleService tblFwglLegalOrganizationExtOracleService;
	@Resource
	private TblFwglLegalPersonnelOracleService tblFwglLegalPersonnelOracleService;
	@Resource
	private TblFwglLegalPersonnelExtOracleService tblFwglLegalPersonnelExtOracleService;
	@Resource
	private TblFwglLegalAdviserOracleService tblFwglLegalAdviserOracleService;
	@Resource
	private TblFwglLegalAdviserExtOracleService tblFwglLegalAdviserExtOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 查询公司律师列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglFirmLawyerList(TblFwglFirmLawyerQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglFirmLawyerOracle> pageInfo = tblFwglFirmLawyerOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			PageResult<TblFwglFirmLawyerOracle> build = new PageResult<TblFwglFirmLawyerOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 公司律师 新增/更新
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglFirmLawyer(TblFwglFirmLawyer param) {
		TblFwglFirmLawyerOracle fwglFirmLawyer = new TblFwglFirmLawyerOracle();
		BeanUtils.copyProperties(param, fwglFirmLawyer);
		TblFwglFirmLawyerOracle tblFwglFirmLawyer = tblFwglFirmLawyerOracleService.saveOrUpdate(fwglFirmLawyer);
		return ResponseFormat.retParam(200, 200, tblFwglFirmLawyer);
	}

	/**
	 * 公司律师 刪除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public JsonBean deleteTblFwglFirmLawyer(Long id) {
		TblFwglFirmLawyerOracle tblFwglFirmLawyer = tblFwglFirmLawyerOracleService.findById(id);
		//公司律师 删除
		tblFwglFirmLawyerOracleService.delete(id);
		//公司律师-简历 删除
		if (StringUtils.isNotBlank(tblFwglFirmLawyer.getLawyerExtId())) {
			List<String> lawyerExtId = Arrays.asList(tblFwglFirmLawyer.getLawyerExtId().split(","));
			lawyerExtId.forEach(x -> tblFwglFirmLawyerExtOracleService.delete(Long.valueOf(x)));
		}
		//公司律师-文件 删除
		if (StringUtils.isNotBlank(tblFwglFirmLawyer.getFileIds())) {
			List<String> fileIds = Arrays.asList(tblFwglFirmLawyer.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 公司律师详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglFirmLawyer(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//公司律师详情
		TblFwglFirmLawyerOracle lawyer = tblFwglFirmLawyerOracleService.findById(id);
		//公司律师-简历
		if (StringUtils.isNotBlank(lawyer.getLawyerExtId())) {
			List<TblFwglFirmLawyerExtOracle> lawyerExt = tblFwglFirmLawyerExtOracleService.getList(lawyer.getLawyerExtId());
			map.put("lawyerExt", lawyerExt);
		}
		//公司律师-文件列表
		if (StringUtils.isNotEmpty(lawyer.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(lawyer.getFileIds());
			map.put("files", files);
		}
		map.put("lawyer", lawyer);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 公司律师-简历 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglFirmLawyerExt(TblFwglFirmLawyerExt param) {
		TblFwglFirmLawyerExtOracle fwglFirmLawyerExt = new TblFwglFirmLawyerExtOracle();
		BeanUtils.copyProperties(param, fwglFirmLawyerExt);
		TblFwglFirmLawyerExtOracle tblFwglFirmLawyerExt = tblFwglFirmLawyerExtOracleService.saveOrUpdate(fwglFirmLawyerExt);
		return ResponseFormat.retParam(200, 200, tblFwglFirmLawyerExt);
	}

	/**
	 * 公司律师-简历 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglFirmLawyerExt(Long id) {
		tblFwglFirmLawyerExtOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 法务机构及负责人列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglLegalOrganizationList(TblFwglLegalOrganizationQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglLegalOrganizationOracle> pageInfo = tblFwglLegalOrganizationOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			PageResult<TblFwglLegalOrganizationOracle> build = new PageResult<TblFwglLegalOrganizationOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());

	}

	/**
	 * 法务机构及负责人 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglLegalOrganization(TblFwglLegalOrganization param) {
		TblFwglLegalOrganizationOracle fwglLegalOrganization = new TblFwglLegalOrganizationOracle();
		BeanUtils.copyProperties(param, fwglLegalOrganization);
		TblFwglLegalOrganizationOracle tblFwglFirmLawyer = tblFwglLegalOrganizationOracleService.saveOrUpdate(fwglLegalOrganization);
		return ResponseFormat.retParam(200, 200, tblFwglFirmLawyer);
	}

	/**
	 * 法务机构及负责人详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglLegalOrganization(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//法务机构及负责人 详情
		TblFwglLegalOrganizationOracle legalOrganization = tblFwglLegalOrganizationOracleService.findById(id);
		//参数组装 集团 工作单位 审批人 名称
		legalOrganization.setBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(Long.valueOf(legalOrganization.getBelongGroup())));
		legalOrganization.setWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(legalOrganization.getWorkUnitId()));
		if (StringUtils.isNotBlank(legalOrganization.getAuditPerson())) {
			legalOrganization.setAuditPersonName(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(legalOrganization.getAuditPerson())));
		}
		//法务机构及负责人-年度法律审核情况 详情
		if (StringUtils.isNotBlank(legalOrganization.getOrganizationExtId())) {
			List<TblFwglLegalOrganizationExtOracle> legalOrganizationExt = tblFwglLegalOrganizationExtOracleService
					.getList(legalOrganization.getOrganizationExtId());
			map.put("legalOrganizationExt", legalOrganizationExt);
		}
		map.put("legalOrganization", legalOrganization);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 法务机构及负责人 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteTblFwglLegalOrganization(Long id) {
		TblFwglLegalOrganizationOracle legalOrganization = tblFwglLegalOrganizationOracleService.findById(id);
		//法务机构及负责人 刪除
		tblFwglLegalOrganizationOracleService.delete(id);
		//法务机构及负责人-年度法律审核情况 刪除
		if (StringUtils.isNotBlank(legalOrganization.getOrganizationExtId())) {
			List<String> organizationExtIds = Arrays.asList(legalOrganization.getOrganizationExtId().split(","));
			organizationExtIds.forEach(x -> tblFwglLegalOrganizationExtOracleService.delete(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 法务机构及负责人-年度法律审核情况 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglLegalOrganizationExt(TblFwglLegalOrganizationExt param) {
		TblFwglLegalOrganizationExtOracle fwglLegalOrganizationExt = new TblFwglLegalOrganizationExtOracle();
		BeanUtils.copyProperties(param, fwglLegalOrganizationExt);
		TblFwglLegalOrganizationExtOracle legalOrganizationExt = tblFwglLegalOrganizationExtOracleService.saveOrUpdate(fwglLegalOrganizationExt);
		return ResponseFormat.retParam(200, 200, legalOrganizationExt);
	}

	/**
	 * 法务机构及负责人-年度法律审核情况 删除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglLegalOrganizationExt(Long id) {
		tblFwglLegalOrganizationExtOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 法务人员列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglLegalPersonnelList(TblFwglLegalPersonnelQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglLegalPersonnelOracle> pageInfo = tblFwglLegalPersonnelOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			//查询创建者ID
			String creatorIds = pageInfo.getList().stream().map(TblFwglLegalPersonnelOracle::getCreator).distinct().collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(creatorIds)) {
				List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(creatorIds);
				if (CollectionUtil.isNotEmpty(userInfos)) {
					Map<String, String> map = userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
					pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCreator()))
							.forEach(x -> x.setCreatorName(map.getOrDefault(x.getCreator(), "")));
				}
			}
			//审核人ID
			String auditPersonIds = pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getAuditPerson()))
					.map(TblFwglLegalPersonnelOracle::getAuditPerson).distinct().collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(auditPersonIds)) {
				List<UserInfo> auditPersons = tblStaffOracleService.getCreatorUserInfos(auditPersonIds);
				if (CollectionUtil.isNotEmpty(auditPersons)) {
					Map<String, String> auditPersonMap = auditPersons.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
					pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getAuditPerson()))
							.forEach(x -> x.setAuditPersonName(auditPersonMap.getOrDefault(x.getAuditPerson(), "")));
				}
			}
			pageInfo.getList().forEach(item -> {
				if (Objects.nonNull(item.getCreatedTime())) {
					try {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						item.setCreatedTimeString(simpleDateFormat.format(item.getCreatedTime()));
					} catch (Exception e) {
						log.error("导出时间转换异常：", e);
					}
				}
				if (Objects.nonNull(item.getStartYear())) {
					try {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						item.setStartYearString(simpleDateFormat.format(item.getStartYear()));
					} catch (Exception e) {
						log.error("导出时间转换异常：", e);
					}
				}
			});
			PageResult<TblFwglLegalPersonnelOracle> result = new PageResult<TblFwglLegalPersonnelOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, result);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 法务人员 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglLegalPersonnel(TblFwglLegalPersonnel param) {
		TblFwglLegalPersonnelOracle fwglLegalPersonnel = new TblFwglLegalPersonnelOracle();
		BeanUtils.copyProperties(param, fwglLegalPersonnel);
		TblFwglLegalPersonnelOracle legalPersonnel = tblFwglLegalPersonnelOracleService.saveOrUpdate(fwglLegalPersonnel);
		return ResponseFormat.retParam(200, 200, legalPersonnel);
	}

	/**
	 * 法务人员详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglLegalPersonnel(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//法务人员详情
		TblFwglLegalPersonnelOracle legalPersonnel = tblFwglLegalPersonnelOracleService.findById(id);
		//参数组装 集团 工作单位 名称
		legalPersonnel.setBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(Long.valueOf(legalPersonnel.getBelongGroup())));
		legalPersonnel.setWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(legalPersonnel.getWorkUnitId()));
		legalPersonnel.setCreatorName(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(legalPersonnel.getCreator())));
		if (StringUtils.isNotBlank(legalPersonnel.getAuditPerson())) {
			legalPersonnel.setAuditPersonName(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(legalPersonnel.getAuditPerson())));
		}
		//法务人员-工作经历列表
		if (StringUtils.isNotBlank(legalPersonnel.getPersonnelExtId())) {
			List<TblFwglLegalPersonnelExtOracle> legalPersonnelExt = tblFwglLegalPersonnelExtOracleService
					.getList(legalPersonnel.getPersonnelExtId());
			map.put("legalPersonnelExt", legalPersonnelExt);
		}
		map.put("legalPersonnel", legalPersonnel);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 法务人员 删除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteTblFwglLegalPersonnel(Long id) {
		TblFwglLegalPersonnelOracle legalPersonnel = tblFwglLegalPersonnelOracleService.findById(id);
		//法务人员 删除
		tblFwglLegalPersonnelOracleService.delete(id);
		//法务人员-工作经历 删除
		if (StringUtils.isNotBlank(legalPersonnel.getPersonnelExtId())) {
			List<String> personnelExtIds = Arrays.asList(legalPersonnel.getPersonnelExtId().split(","));
			personnelExtIds.forEach(x -> tblFwglLegalPersonnelExtOracleService.delete(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 法务人员-工作经历 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglLegalPersonnelExt(TblFwglLegalPersonnelExt param) {
		TblFwglLegalPersonnelExtOracle fwglLegalPersonnelExt = new TblFwglLegalPersonnelExtOracle();
		BeanUtils.copyProperties(param, fwglLegalPersonnelExt);
		TblFwglLegalPersonnelExtOracle legalPersonnelExt = tblFwglLegalPersonnelExtOracleService.saveOrUpdate(fwglLegalPersonnelExt);
		return ResponseFormat.retParam(200, 200, legalPersonnelExt);
	}

	/**
	 * 法务人员-工作经历 删除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglLegalPersonnelExt(Long id) {
		tblFwglLegalPersonnelExtOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 总法律顾问列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglLegalAdviserList(TblFwglLegalAdviserQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglLegalAdviserOracle> pageInfo = tblFwglLegalAdviserOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			//工作单位
			String workUnitIds = pageInfo.getList().stream().filter(x -> x.getWorkUnitId() != null).map(item -> item.getWorkUnitId().toString())
					.distinct().collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(workUnitIds)) {
				Map<String, String> workUnitMap = getWorkUnitMap(workUnitIds);
				pageInfo.getList().stream().filter(x -> x.getWorkUnitId() != null)
						.forEach(x -> x.setWorkUnitName(workUnitMap.getOrDefault(String.valueOf(x.getWorkUnitId()), "")));
			}
			//所属集团
			String belongGroupIds = pageInfo.getList().stream().filter(x -> x.getBelongGroupId() != null)
					.map(item -> item.getBelongGroupId().toString()).distinct().collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(belongGroupIds)) {
				//获取集团map
				Map<String, String> belongGroupMap = getBelongGroupMap(belongGroupIds);
				pageInfo.getList().stream().filter(x -> x.getBelongGroupId() != null)
						.forEach(x -> x.setBelongGroupName(belongGroupMap.getOrDefault(String.valueOf(x.getBelongGroupId()), "")));
			}
			//查询创建者ID
			String creatorIds = pageInfo.getList().stream().map(TblFwglLegalAdviserOracle::getCreator).distinct().collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(creatorIds)) {
				List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(creatorIds);
				if (CollectionUtil.isNotEmpty(userInfos)) {
					Map<String, String> map = userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
					pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCreator()))
							.forEach(x -> x.setCreatorName(map.getOrDefault(x.getCreator(), "")));
				}
			}
			//审核人ID
			String auditPersonIds = pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getAuditPerson()))
					.map(TblFwglLegalAdviserOracle::getAuditPerson).distinct().collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(auditPersonIds)) {
				List<UserInfo> auditPersons = tblStaffOracleService.getCreatorUserInfos(auditPersonIds);
				if (CollectionUtil.isNotEmpty(auditPersons)) {
					Map<String, String> auditPersonMap = auditPersons.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
					pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getAuditPerson()))
							.forEach(x -> x.setAuditPersonName(auditPersonMap.getOrDefault(x.getAuditPerson(), "")));
				}
			}
			pageInfo.getList().forEach(item -> {
				if (Objects.nonNull(item.getCreatedTime())) {
					try {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
						item.setCreatedTimeString(simpleDateFormat.format(item.getCreatedTime()));
					} catch (Exception e) {
						log.error("导出时间转换异常：", e);
					}
				}
			});
			PageResult<TblFwglLegalAdviserOracle> result = new PageResult<TblFwglLegalAdviserOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, result);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());

	}

	/**
	 * 获取单位名称
	 * @param workUnitIds
	 */
	private Map<String, String> getWorkUnitMap(String workUnitIds) {
		if (StringUtils.isNotBlank(workUnitIds)) {
			List<UserInfo> workUnits = tblStaffOracleService.getWorkUnitIdUserInfos(workUnitIds);
			if (CollectionUtil.isNotEmpty(workUnits)) {
				return workUnits.stream().collect(Collectors.toMap(UserInfo::getWorkUnitId, UserInfo::getWorkUnitName));
			}
		}
		return new HashMap<>();
	}

	/**
	 * 获取所属集团名称
	 * @param belongGroupIds
	 */
	private Map<String, String> getBelongGroupMap(String belongGroupIds) {
		if (StringUtils.isNotBlank(belongGroupIds)) {
			List<UserInfo> belongGroups = tblStaffOracleService.getBelongGroupIdUserInfos(belongGroupIds);
			if (CollectionUtil.isNotEmpty(belongGroups)) {
				return belongGroups.stream().collect(Collectors.toMap(UserInfo::getBelongGroupId, UserInfo::getBelongGroupName));
			}
		}
		return new HashMap<>();
	}


	/**
	 * 总法律顾问 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglLegalAdviser(TblFwglLegalAdviser param) {
		TblFwglLegalAdviserOracle fwglLegalAdviser = new TblFwglLegalAdviserOracle();
		BeanUtils.copyProperties(param, fwglLegalAdviser);
		TblFwglLegalAdviserOracle tblFwglLegalAdviser = tblFwglLegalAdviserOracleService.saveOrUpdate(fwglLegalAdviser);
		return ResponseFormat.retParam(200, 200, tblFwglLegalAdviser);
	}

	/**
	 * 总法律顾问详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglLegalAdviser(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//总法律顾问详情
		TblFwglLegalAdviserOracle legalAdviser = tblFwglLegalAdviserOracleService.findById(id);
		//参数组装 集团 工作单位 名称
		legalAdviser.setBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(Long.valueOf(legalAdviser.getBelongGroup())));
		legalAdviser.setWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(legalAdviser.getWorkUnitId()));
		legalAdviser.setCreatorName(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(legalAdviser.getCreator())));
		if (StringUtils.isNotBlank(legalAdviser.getAuditPerson())) {
			legalAdviser.setAuditPersonName(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(legalAdviser.getAuditPerson())));
		}
		//总法律顾问-工作经历列表
		if (StringUtils.isNotBlank(legalAdviser.getAdviserExtId())) {
			List<TblFwglLegalAdviserExtOracle> legalAdviserExt = tblFwglLegalAdviserExtOracleService.getList(legalAdviser.getAdviserExtId());
			map.put("legalAdviserExt", legalAdviserExt);
		}
		map.put("legalAdviser", legalAdviser);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 总法律顾问 删除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglLegalAdviser(Long id) {
		TblFwglLegalAdviserOracle legalAdviser = tblFwglLegalAdviserOracleService.findById(id);
		//总法律顾问 删除
		tblFwglLegalAdviserOracleService.delete(id);
		//总法律顾问-工作经历 删除
		if (StringUtils.isNotBlank(legalAdviser.getAdviserExtId())) {
			List<String> adviserExtIds = Arrays.asList(legalAdviser.getAdviserExtId().split(","));
			adviserExtIds.forEach(x -> tblFwglLegalAdviserExtOracleService.delete(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 总法律顾问-工作经历 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglLegalAdviserExt(TblFwglLegalAdviserExt param) {
		TblFwglLegalAdviserExtOracle fwglLegalAdviserExt = new TblFwglLegalAdviserExtOracle();
		BeanUtils.copyProperties(param, fwglLegalAdviserExt);
		TblFwglLegalAdviserExtOracle legalAdviserExt = tblFwglLegalAdviserExtOracleService.saveOrUpdate(fwglLegalAdviserExt);
		return ResponseFormat.retParam(200, 200, legalAdviserExt);
	}

	/**
	 * 总法律顾问-工作经历 删除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglLegalAdviserExt(Long id) {
		tblFwglLegalAdviserExtOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}
}
