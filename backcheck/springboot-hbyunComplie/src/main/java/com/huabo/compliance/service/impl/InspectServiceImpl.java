package com.huabo.compliance.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.compliance.oracle.entity.TblComplianceFileOracle;
import com.huabo.compliance.oracle.entity.TblComplianceInspectImpOracle;
import com.huabo.compliance.oracle.entity.TblComplianceInspectPlanOracle;
import com.huabo.compliance.oracle.entity.TblComplianceRectificationOracle;
import com.huabo.compliance.oracle.mapper.TblComplianceInspectPlanOracleMapper;
import com.huabo.compliance.oracle.service.*;
import com.huabo.compliance.service.FileUploadService;
import com.huabo.compliance.service.InspectService;
import com.huabo.compliance.util.JsonBean;
import com.huabo.compliance.util.PageResult;
import com.huabo.compliance.util.ResponseFormat;
import com.huabo.compliance.vo.param.TblComplianceInspectImpQueryParam;
import com.huabo.compliance.vo.param.TblComplianceInspectPlanQueryParam;
import com.huabo.compliance.vo.param.TblComplianceRectificationQueryParam;
import com.huabo.compliance.vo.result.FileVo;
import com.huabo.compliance.vo.result.TblComplianceRectificationFileVo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InspectServiceImpl implements InspectService {

	@Resource
	private TblComplianceInspectPlanOracleService tblComplianceInspectPlanOracleService;
	@Resource
	private TblComplianceInspectImpOracleService tblComplianceInspectImpOracleService;
	@Resource
	private TblComplianceRectificationOracleService tblComplianceRectificationOracleService;


	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private TblComplianceFileOracleService tblComplianceFileOracleService;
	
	
	@Resource
	private TblComplianceInspectPlanOracleMapper tblComplianceInspectPlanOracleMapper;

	/**
	 * 检查方案 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<PageResult<TblComplianceInspectPlanOracle>> getTblComplianceInspectPlanList(TblComplianceInspectPlanQueryParam param) {
		PageInfo<TblComplianceInspectPlanOracle> pageInfo = tblComplianceInspectPlanOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			Map<Integer, String> workUnitMap = new HashMap<>();
			Map<Integer, String> belongGroupMap = new HashMap<>();
			Map<Integer, String> userMap = new HashMap<>();
//			List<Integer> workUnitIds = new ArrayList<>();
			List<Integer> belongGroupIds = new ArrayList<>();
			List<Integer> userIds = new ArrayList<>();
//			//配合部门
//			List<Integer> cooperateDepartmentIds = pageInfo.getList().stream().filter(item -> Objects.nonNull(item.getCooperateDepartment()))
//					.map(TblComplianceInspectPlanOracle::getCooperateDepartment).distinct().collect(Collectors.toList());
//			//检查部门
//			List<Integer> inspectDepartmentIds = pageInfo.getList().stream().filter(item -> Objects.nonNull(item.getInspectDepartment()))
//					.map(TblComplianceInspectPlanOracle::getInspectDepartment).distinct().collect(Collectors.toList());
			//负责人
			List<Integer> responsiblePersonIds = pageInfo.getList().stream().filter(item -> Objects.nonNull(item.getResponsiblePerson()))
					.map(TblComplianceInspectPlanOracle::getResponsiblePerson).distinct().collect(Collectors.toList());
			//创建人
			List<Integer> creatorIds = pageInfo.getList().stream().filter(item -> Objects.nonNull(item.getCreator()))
					.map(TblComplianceInspectPlanOracle::getCreator).distinct().collect(Collectors.toList());
			//检查公司
			List<Integer> inspectCompanyIds = pageInfo.getList().stream().filter(item -> Objects.nonNull(item.getInspectCompany()))
					.map(TblComplianceInspectPlanOracle::getInspectCompany).distinct().collect(Collectors.toList());
//			if (CollectionUtil.isNotEmpty(cooperateDepartmentIds)) {
//				workUnitIds.addAll(cooperateDepartmentIds);
//			}
//			if (CollectionUtil.isNotEmpty(inspectDepartmentIds)) {
//				workUnitIds.addAll(inspectDepartmentIds);
//			}
			if (CollectionUtil.isNotEmpty(responsiblePersonIds)) {
				userIds.addAll(responsiblePersonIds);
			}
			if (CollectionUtil.isNotEmpty(creatorIds)) {
				userIds.addAll(creatorIds);
			}
			if (CollectionUtil.isNotEmpty(inspectCompanyIds)) {
				belongGroupIds.addAll(inspectCompanyIds);
			}
//			workUnitIds = workUnitIds.stream().distinct().collect(Collectors.toList());
			userIds = userIds.stream().distinct().collect(Collectors.toList());
			belongGroupIds = belongGroupIds.stream().distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(userIds)) {
				userMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(userIds, ","));
			}
//			if (CollectionUtil.isNotEmpty(workUnitIds)) {
//				workUnitMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(workUnitIds, ","));
//			}
			if (CollectionUtil.isNotEmpty(belongGroupIds)) {
				belongGroupMap = tblStaffOracleService.getBelongGroupIdUserInfoMap(StringUtils.join(belongGroupIds, ","));
			}
			Map<Integer, String> finalUserMap = userMap;
			Map<Integer, String> finalWorkUnitMap = workUnitMap;
			Map<Integer, String> finalBelongGroupMap = belongGroupMap;
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(finalUserMap.getOrDefault(item.getCreator(), ""));
//				item.setCooperateDepartmentName(finalWorkUnitMap.getOrDefault(item.getCooperateDepartment(), ""));
//				item.setInspectDepartmentName(finalWorkUnitMap.getOrDefault(item.getInspectDepartment(), ""));
				item.setResponsiblePersonName(finalUserMap.getOrDefault(item.getResponsiblePerson(), ""));
				item.setInspectCompanyName(finalBelongGroupMap.getOrDefault(item.getInspectCompany(), ""));
			});
			PageResult<TblComplianceInspectPlanOracle> build = new PageResult<TblComplianceInspectPlanOracle>().build(pageInfo);
			return ResponseFormat.retParam(1, 200, build);
		}
		return ResponseFormat.retParam(1, 200, PageResult.buildNoData());
	}

	/**
	 * 检查方案 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<TblComplianceInspectPlanOracle> saveOrUpdateTblComplianceInspectPlan(TblComplianceInspectPlanOracle param) {
		TblComplianceInspectPlanOracle inspectPlanOracle = tblComplianceInspectPlanOracleService.saveOrUpdate(param);
		return ResponseFormat.retParam(1, 200, inspectPlanOracle);
	}

	/**
	 * 检查方案 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonBean<Void> deleteTblComplianceInspectPlan(Integer id) {
		TblComplianceInspectPlanOracle inspectPlan = tblComplianceInspectPlanOracleService.findById(id);
		//删除
		tblComplianceInspectPlanOracleService.delete(id);
		//删除-附件
		if (Objects.nonNull(inspectPlan) && StringUtils.isNotBlank(inspectPlan.getFileIds())) {
			List<String> fileIds = Arrays.asList(inspectPlan.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Integer.valueOf(x)));
		}
		return ResponseFormat.retParam(1, 200, null);
	}

	/**
	 * 检查方案 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean<FileVo<TblComplianceInspectPlanOracle>> getTblComplianceInspectPlan(Integer id) {
		FileVo<TblComplianceInspectPlanOracle> result = new FileVo<>();
		TblComplianceInspectPlanOracle inspectPlan = tblComplianceInspectPlanOracleService.findById(id);
		if (Objects.nonNull(inspectPlan) && Objects.nonNull(inspectPlan.getCooperateDepartment())) {
			inspectPlan.setCooperateDepartmentName(tblStaffOracleService.getWorkUnitIdUserInfo(inspectPlan.getCooperateDepartment()));
		}
		if (Objects.nonNull(inspectPlan) && Objects.nonNull(inspectPlan.getInspectDepartment())) {
			inspectPlan.setInspectDepartmentName(tblStaffOracleService.getWorkUnitIdUserInfo(inspectPlan.getInspectDepartment()));
		}
		if (Objects.nonNull(inspectPlan) && Objects.nonNull(inspectPlan.getResponsiblePerson())) {
			inspectPlan.setResponsiblePersonName(tblStaffOracleService.getCreatorUserInfo(inspectPlan.getResponsiblePerson()));
		}
		if (Objects.nonNull(inspectPlan) && Objects.nonNull(inspectPlan.getCreator())) {
			inspectPlan.setCreatorName(tblStaffOracleService.getCreatorUserInfo(inspectPlan.getCreator()));
		}
		if (Objects.nonNull(inspectPlan) && Objects.nonNull(inspectPlan.getInspectCompany())) {
			inspectPlan.setInspectCompanyName(tblStaffOracleService.getBelongGroupIdUserInfo(inspectPlan.getInspectCompany()));
		}
		//附件
		if (Objects.nonNull(inspectPlan) && StringUtils.isNotBlank(inspectPlan.getFileIds())) {
			List<TblComplianceFileOracle> file = tblComplianceFileOracleService.findByIds(inspectPlan.getFileIds());
			result.setFile(file);
		}
		result.setData(inspectPlan);
		return ResponseFormat.retParam(1, 200, result);
	}

	/**
	 * 检查实施 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<PageResult<TblComplianceInspectImpOracle>> getTblComplianceInspectImpList(TblComplianceInspectImpQueryParam param) {
		PageInfo<TblComplianceInspectImpOracle> pageInfo = tblComplianceInspectImpOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			Map<Integer, String> creatorMap = new HashMap<>();
			//创建人
			List<Integer> creatorIds = pageInfo.getList().stream().filter(item -> Objects.nonNull(item.getCreator()))
					.map(TblComplianceInspectImpOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(creatorIds)) {
				creatorMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorIds, ","));
			}
			Map<Integer, String> finalCreatorMap = creatorMap;
			pageInfo.getList().forEach(item -> item.setCreatorName(finalCreatorMap.getOrDefault(item.getCreator(), "")));
			PageResult<TblComplianceInspectImpOracle> build = new PageResult<TblComplianceInspectImpOracle>().build(pageInfo);
			return ResponseFormat.retParam(1, 200, build);
		}
		return ResponseFormat.retParam(1, 200, PageResult.buildNoData());
	}

	/**
	 * 检查实施 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<TblComplianceInspectImpOracle> saveOrUpdateTblComplianceInspectImp(TblComplianceInspectImpOracle param) {
		TblComplianceInspectImpOracle inspectImpOracle = tblComplianceInspectImpOracleService.saveOrUpdate(param);
		return ResponseFormat.retParam(1, 200, inspectImpOracle);
	}

	/**
	 * 检查实施 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean<Void> deleteTblComplianceInspectImp(Integer id) {
		TblComplianceInspectImpOracle inspectImp = tblComplianceInspectImpOracleService.findById(id);
		//删除
		tblComplianceInspectImpOracleService.delete(id);
		//删除-附件
		if (Objects.nonNull(inspectImp) && StringUtils.isNotBlank(inspectImp.getFileIds())) {
			List<String> fileIds = Arrays.asList(inspectImp.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Integer.valueOf(x)));
		}
		return ResponseFormat.retParam(1, 200, null);
	}

	/**
	 * 检查实施 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean<FileVo<TblComplianceInspectImpOracle>> getTblComplianceInspectImp(Integer id) {
		FileVo<TblComplianceInspectImpOracle> result = new FileVo<>();
		TblComplianceInspectImpOracle inspectImp = tblComplianceInspectImpOracleService.findById(id);
		inspectImp.setCreatorName(tblStaffOracleService.getCreatorUserInfo(inspectImp.getCreator()));
		//附件
		if (Objects.nonNull(inspectImp) && StringUtils.isNotBlank(inspectImp.getFileIds())) {
			List<TblComplianceFileOracle> file = tblComplianceFileOracleService.findByIds(inspectImp.getFileIds());
			result.setFile(file);
		}
		result.setData(inspectImp);
		return ResponseFormat.retParam(1, 200, result);
	}

	/**
	 * 问题整改 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<PageResult<TblComplianceRectificationOracle>> getTblComplianceRectificationList(TblComplianceRectificationQueryParam param) {
		PageInfo<TblComplianceRectificationOracle> pageInfo = tblComplianceRectificationOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			Map<Integer, String> creatorMap = new HashMap<>();
			Map<Integer, TblComplianceInspectImpOracle> impMap = new HashMap<>();
			//创建人
			List<Integer> creatorIds = pageInfo.getList().stream().filter(item -> Objects.nonNull(item.getCreator()))
					.map(TblComplianceRectificationOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(creatorIds)) {
				creatorMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorIds, ","));
			}
			List<Integer> impIds = pageInfo.getList().stream().filter(item -> Objects.nonNull(item.getCreator()))
					.map(TblComplianceRectificationOracle::getImpId).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(impIds)) {
				impMap = tblComplianceInspectImpOracleService.getInspectImpMap(impIds);
			}
			Map<Integer, String> finalCreatorMap = creatorMap;
			Map<Integer, TblComplianceInspectImpOracle> finalImpMap = impMap;
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(finalCreatorMap.getOrDefault(item.getCreator(), ""));
				item.setInspectImp(finalImpMap.getOrDefault(item.getImpId(), null));
			});
			PageResult<TblComplianceRectificationOracle> build = new PageResult<TblComplianceRectificationOracle>().build(pageInfo);
			return ResponseFormat.retParam(1, 200, build);
		}
		return ResponseFormat.retParam(1, 200, PageResult.buildNoData());
	}

	/**
	 * 问题整改 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<TblComplianceRectificationOracle> saveOrUpdateTblComplianceRectification(TblComplianceRectificationOracle param) {
		TblComplianceRectificationOracle rectification = tblComplianceRectificationOracleService.saveOrUpdate(param);
		return ResponseFormat.retParam(1, 200, rectification);
	}

	/**
	 * 问题整改 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean<Void> deleteTblComplianceRectification(Integer id) {
		TblComplianceRectificationOracle rectification = tblComplianceRectificationOracleService.findById(id);
		//删除
		tblComplianceRectificationOracleService.delete(id);
		//删除-附件
		if (Objects.nonNull(rectification) && StringUtils.isNotBlank(rectification.getFileIds())) {
			List<String> fileIds = Arrays.asList(rectification.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Integer.valueOf(x)));
		}
		return ResponseFormat.retParam(1, 200, null);
	}

	/**
	 * 问题整改 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean<TblComplianceRectificationFileVo<TblComplianceRectificationOracle>> getTblComplianceRectification(Integer id) {
		TblComplianceRectificationFileVo<TblComplianceRectificationOracle> result = new TblComplianceRectificationFileVo<>();
		TblComplianceRectificationOracle rectification = tblComplianceRectificationOracleService.findById(id);
		rectification.setCreatorName(tblStaffOracleService.getCreatorUserInfo(rectification.getCreator()));
		//检查实施
		if (Objects.nonNull(rectification.getImpId())) {
			TblComplianceInspectImpOracle inspectImp = tblComplianceInspectImpOracleService.findById(rectification.getImpId());
			result.setInspectImp(inspectImp);
			//附件
			if (Objects.nonNull(inspectImp) && StringUtils.isNotBlank(inspectImp.getFileIds())) {
				List<TblComplianceFileOracle> file = tblComplianceFileOracleService.findByIds(inspectImp.getFileIds());
				result.setFile(file);
			}
		}
		result.setData(rectification);
		return ResponseFormat.retParam(1, 200, result);
	}

	/**
	 * 检查方案-自动编码
	 * @param year
	 * @return
	 */
	@Override
	public JsonBean<String> getTblComplianceInspectPlanAutoNum(String year) {
		String autoNum = tblComplianceInspectPlanOracleService.getTblComplianceInspectPlanAutoNum(year);
		return ResponseFormat.retParam(1, 200, autoNum);
	}
	
	/**
	 * 首页-合规审查数量
	 * @return
	 */
	@Override
	public JsonBean compcnt() {
		Map<String,Object> mapCompCnt = new HashedMap();
		//规章制度
		Integer institution_cnt = tblComplianceInspectPlanOracleMapper.compcnt_institution();
		//重要事项法审
		Integer matters_cnt = tblComplianceInspectPlanOracleMapper.compcnt_matters();
		//合同
		
		mapCompCnt.put("institution_cnt", institution_cnt);
		mapCompCnt.put("matters_cnt", matters_cnt);
		
		return ResponseFormat.retParam(1, 200, mapCompCnt);
	}
	
	@Override
	public JsonBean topcnt() {
		Map<String,Object> mapCompCnt = new HashedMap();
		//疑似问题数量
		Integer suspected_cnt = tblComplianceInspectPlanOracleMapper.compcnt_suspected();
		//整改问题数量
		Integer reform_cnt = tblComplianceInspectPlanOracleMapper.compcnt_reform();
		
		mapCompCnt.put("suspected_cnt", suspected_cnt);
		mapCompCnt.put("reform_cnt", reform_cnt);
		
		return ResponseFormat.retParam(1, 200, mapCompCnt);
	}
}
