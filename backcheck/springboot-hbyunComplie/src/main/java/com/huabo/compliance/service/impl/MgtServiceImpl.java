package com.huabo.compliance.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.compliance.config.DateBaseConfig;
import com.huabo.compliance.oracle.entity.*;
import com.huabo.compliance.oracle.service.*;
import com.huabo.compliance.service.FileUploadService;
import com.huabo.compliance.service.MgtService;
import com.huabo.compliance.util.JsonBean;
import com.huabo.compliance.util.PageResult;
import com.huabo.compliance.util.ResponseFormat;
import com.huabo.compliance.vo.param.*;
import com.huabo.compliance.vo.result.TblComplianceRiskResult;
import com.huabo.compliance.vo.result.UserInfo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MgtServiceImpl implements MgtService {

	@Resource
	private TblCompliancePlanMgtOracleService tblCompliancePlanMgtOracleService;
	@Resource
	private TblComplianceDtyOracleService tblComplianceDtyOracleService;
	@Resource
	private TblComplianceImOracleService tblComplianceImOracleService;
	@Resource
	private TblComplianceManualMgtOracleService tblComplianceManualMgtOracleService;
	@Resource
	private TblComplianceRiskOracleService tblComplianceRiskOracleService;

	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblComplianceFileOracleService tblComplianceFileOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 计划管理 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblCompliancePlanMgtList(TblCompliancePlanMgtQueryParam param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			PageInfo<TblCompliancePlanMgtOracle> pageInfo = tblCompliancePlanMgtOracleService.getList(param);
			if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
				Map<Integer, String> userInfosMap = new HashMap<>();
				//提取 部门负责人 与 编制人  创建人 ID
				List<Integer> departmentHeads = pageInfo.getList().stream().filter(x -> x.getDepartmentHead() != null)
						.map(TblCompliancePlanMgtOracle::getDepartmentHead).distinct().collect(Collectors.toList());
				List<Integer> compilers = pageInfo.getList().stream().filter(x -> x.getCompiler() != null)
						.map(TblCompliancePlanMgtOracle::getCompiler).distinct().collect(Collectors.toList());
				List<Integer> creator = pageInfo.getList().stream().filter(x -> x.getCreator() != null).map(TblCompliancePlanMgtOracle::getCreator)
						.distinct().collect(Collectors.toList());
				departmentHeads.addAll(compilers);
				departmentHeads.addAll(creator);
				List<Integer> collect = departmentHeads.stream().distinct().collect(Collectors.toList());
				//获取用户信息map
				List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(StringUtils.join(collect, ","));
				if (CollectionUtil.isNotEmpty(userInfos)) {
					userInfosMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
				}
				Map<Integer, String> finalUserInfosMapMap = userInfosMap;
				//参数组装
				pageInfo.getList().forEach(x -> {
					x.setDepartmentHeadName(finalUserInfosMapMap.getOrDefault(x.getDepartmentHead(), ""));
					x.setCompilerName(finalUserInfosMapMap.getOrDefault(x.getCompiler(), ""));
					x.setCreatorName(finalUserInfosMapMap.getOrDefault(x.getCreator(), ""));
				});
				PageResult<TblCompliancePlanMgtOracle> build = new PageResult<TblCompliancePlanMgtOracle>().build(pageInfo);
				return ResponseFormat.retParam(200, 200, build);
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 计划管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblCompliancePlanMgt(TblCompliancePlanMgtOracle param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblCompliancePlanMgtOracle planMgtOracle = tblCompliancePlanMgtOracleService.saveOrUpdate(param);
			return ResponseFormat.retParam(200, 200, planMgtOracle);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 计划管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblCompliancePlanMgt(Integer id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblCompliancePlanMgtOracle planMgt = tblCompliancePlanMgtOracleService.findById(id);
			tblCompliancePlanMgtOracleService.delete(id);
			//附件-删除
			if (StringUtils.isNotBlank(planMgt.getFileIds())) {
				List<String> fileIds = Arrays.asList(planMgt.getFileIds().split(","));
				fileIds.forEach(x -> fileUploadService.fileRemove(Integer.valueOf(x)));
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 计划管理 详情
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblCompliancePlanMgt(Integer id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			Map<String, Object> map = new HashMap<>();
			TblCompliancePlanMgtOracle planMgt = tblCompliancePlanMgtOracleService.findById(id);
			if (planMgt.getDepartmentHead() != null) {
				planMgt.setDepartmentHeadName(tblStaffOracleService.getCreatorUserInfo(planMgt.getDepartmentHead()));
			}
			if (planMgt.getCompiler() != null) {
				planMgt.setCompilerName(tblStaffOracleService.getCreatorUserInfo(planMgt.getCompiler()));
			}
			if (StringUtils.isNotEmpty(planMgt.getFileIds())) {
				List<TblComplianceFileOracle> file = tblComplianceFileOracleService.findByIds(planMgt.getFileIds());
				map.put("file", file);
			}
			map.put("planMgt", planMgt);
			return ResponseFormat.retParam(200, 200, map);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 重点岗位合规责任 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblComplianceDtyList(TblComplianceDtyQueryParam param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			PageInfo<TblComplianceDtyOracle> pageInfo = tblComplianceDtyOracleService.getList(param);
			if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
				Map<Integer, String> departmentMap = new HashMap<>();
				Map<Integer, String> creatorMap = new HashMap<>();
				//提取 部门  创建
				List<Integer> department = pageInfo.getList().stream().filter(x -> x.getDepartment() != null)
						.map(TblComplianceDtyOracle::getDepartment).collect(Collectors.toList());
				List<Integer> creator = pageInfo.getList().stream().filter(x -> x.getCreator() != null).map(TblComplianceDtyOracle::getCreator)
						.distinct().collect(Collectors.toList());
				//获取 部门信息
				List<UserInfo> departmentUserInfos = tblStaffOracleService.getWorkUnitIdUserInfos(StringUtils.join(department, ","));
				if (CollectionUtil.isNotEmpty(departmentUserInfos)) {
					departmentMap = departmentUserInfos.stream().collect(Collectors.toMap(UserInfo::getWorkUnitId, UserInfo::getWorkUnitName));
				}
				//获取 创建人信息
				List<UserInfo> creatorUserInfos = tblStaffOracleService.getCreatorUserInfos(StringUtils.join(creator, ","));
				if (CollectionUtil.isNotEmpty(departmentUserInfos)) {
					creatorMap = creatorUserInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
				}
				//组装返回值
				Map<Integer, String> finalDepartmentMap = departmentMap;
				Map<Integer, String> finalCreatorMap = creatorMap;
				pageInfo.getList().forEach(x -> {
					x.setDepartmentName(finalDepartmentMap.getOrDefault(x.getDepartment(), ""));
					x.setCreatorName(finalCreatorMap.getOrDefault(x.getCreator(), ""));
				});
				PageResult<TblComplianceDtyOracle> build = new PageResult<TblComplianceDtyOracle>().build(pageInfo);
				return ResponseFormat.retParam(200, 200, build);
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 重点岗位合规责任 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblComplianceDty(TblComplianceDtyOracle param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceDtyOracle dty = tblComplianceDtyOracleService.saveOrUpdate(param);
			return ResponseFormat.retParam(200, 200, dty);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 重点岗位合规责任 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblComplianceDty(Integer id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceDtyOracle dty = tblComplianceDtyOracleService.findById(id);
			tblComplianceDtyOracleService.delete(id);
			//附件-删除
			if (StringUtils.isNotBlank(dty.getFileIds())) {
				List<String> fileIds = Arrays.asList(dty.getFileIds().split(","));
				fileIds.forEach(x -> fileUploadService.fileRemove(Integer.valueOf(x)));
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 重点岗位合规责任 详情
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblComplianceDty(Integer id) {
		Map<String, Object> map = new HashMap<>();
		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceDtyOracle dty = tblComplianceDtyOracleService.findById(id);
			if (dty.getDepartment() != null) {
				dty.setDepartmentName(tblStaffOracleService.getWorkUnitIdUserInfo(dty.getDepartment()));
			}
			if (StringUtils.isNotEmpty(dty.getFileIds())) {
				List<TblComplianceFileOracle> file = tblComplianceFileOracleService.findByIds(dty.getFileIds());
				map.put("file", file);
			}
			map.put("dty", dty);
			return ResponseFormat.retParam(200, 200, map);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 合规管理员信息管理 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblComplianceImList(TblComplianceImQueryParam param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			PageInfo<TblComplianceImOracle> pageInfo = tblComplianceImOracleService.getList(param);
			if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
				Map<Integer, String> departmentMap = new HashMap<>();
				Map<Integer, String> creatorMap = new HashMap<>();
				//提取 部门  创建人
				List<Integer> departments = pageInfo.getList().stream().filter(x -> x.getDepartment() != null)
						.map(TblComplianceImOracle::getDepartment).collect(Collectors.toList());
				List<Integer> creator = pageInfo.getList().stream().filter(x -> x.getCreator() != null).map(TblComplianceImOracle::getCreator)
						.distinct().collect(Collectors.toList());
				//获取 创建人 部门信息
				List<UserInfo> departmentUserInfos = tblStaffOracleService.getWorkUnitIdUserInfos(StringUtils.join(departments, ","));
				if (CollectionUtil.isNotEmpty(departmentUserInfos)) {
					departmentMap = departmentUserInfos.stream().collect(Collectors.toMap(UserInfo::getWorkUnitId, UserInfo::getWorkUnitName));
				}
				List<UserInfo> creatorUserInfos = tblStaffOracleService.getCreatorUserInfos(StringUtils.join(creator, ","));
				if (CollectionUtil.isNotEmpty(departmentUserInfos)) {
					creatorMap = creatorUserInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
				}
				//参数组装
				Map<Integer, String> finalDepartmentMap = departmentMap;
				Map<Integer, String> finalCreatorMap = creatorMap;
				pageInfo.getList().forEach(x -> {
					x.setDepartmentName(finalDepartmentMap.getOrDefault(x.getDepartment(), ""));
					x.setCreatorName(finalCreatorMap.getOrDefault(x.getCreator(), ""));
				});
				PageResult<TblComplianceImOracle> build = new PageResult<TblComplianceImOracle>().build(pageInfo);
				return ResponseFormat.retParam(200, 200, build);
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 合规管理员信息管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblComplianceIm(TblComplianceImOracle param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceImOracle im = tblComplianceImOracleService.saveOrUpdate(param);
			return ResponseFormat.retParam(200, 200, im);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 合规管理员信息管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblComplianceIm(Integer id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceImOracle im = tblComplianceImOracleService.findById(id);
			tblComplianceImOracleService.delete(id);
			//附件-删除
			if (StringUtils.isNotBlank(im.getFileIds())) {
				List<String> fileIds = Arrays.asList(im.getFileIds().split(","));
				fileIds.forEach(x -> fileUploadService.fileRemove(Integer.valueOf(x)));
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 合规管理员信息管理 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblComplianceIm(Integer id) {
		Map<String, Object> map = new HashMap<>();
		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceImOracle im = tblComplianceImOracleService.findById(id);
			if (im.getDepartment() != null) {
				im.setDepartmentName(tblStaffOracleService.getWorkUnitIdUserInfo(im.getDepartment()));
			}
			if (StringUtils.isNotEmpty(im.getFileIds())) {
				List<TblComplianceFileOracle> file = tblComplianceFileOracleService.findByIds(im.getFileIds());
				map.put("file", file);
			}
			map.put("im", im);
			return ResponseFormat.retParam(200, 200, map);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 合规手册管理 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblComplianceManualMgtList(TblComplianceManualMgtQueryParam param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			PageInfo<TblComplianceManualMgtOracle> pageInfo = tblComplianceManualMgtOracleService.getList(param);
			if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
				Map<Integer, String> userInfosMap = new HashMap<>();
				//提取 部门负责人 与 经办人 ID
				List<Integer> departmentHeads = pageInfo.getList().stream().filter(x -> x.getDepartmentHead() != null)
						.map(TblComplianceManualMgtOracle::getDepartmentHead).distinct().collect(Collectors.toList());
				List<Integer> transactors = pageInfo.getList().stream().filter(x -> x.getTransactor() != null)
						.map(TblComplianceManualMgtOracle::getTransactor).distinct().collect(Collectors.toList());
				List<Integer> creator = pageInfo.getList().stream().filter(x -> x.getCreator() != null).map(TblComplianceManualMgtOracle::getCreator)
						.distinct().collect(Collectors.toList());
				departmentHeads.addAll(transactors);
				departmentHeads.addAll(creator);
				List<Integer> collect = departmentHeads.stream().distinct().collect(Collectors.toList());
				//获取用户信息map
				List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(StringUtils.join(collect, ","));
				if (CollectionUtil.isNotEmpty(userInfos)) {
					userInfosMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
				}
				Map<Integer, String> finalUserInfosMap = userInfosMap;
				pageInfo.getList().forEach(x -> {
					x.setTransactorName(finalUserInfosMap.getOrDefault(x.getTransactor(), ""));
					x.setDepartmentHeadName(finalUserInfosMap.getOrDefault(x.getDepartmentHead(), ""));
					x.setCreatorName(finalUserInfosMap.getOrDefault(x.getCreator(), ""));
				});
				PageResult<TblComplianceManualMgtOracle> build = new PageResult<TblComplianceManualMgtOracle>().build(pageInfo);
				return ResponseFormat.retParam(200, 200, build);
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 合规手册管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblComplianceManualMgt(TblComplianceManualMgtOracle param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceManualMgtOracle manualMgt = tblComplianceManualMgtOracleService.saveOrUpdate(param);
			return ResponseFormat.retParam(200, 200, manualMgt);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 合规手册管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblComplianceManualMgt(Integer id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			tblComplianceManualMgtOracleService.delete(id);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 合规手册管理 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblComplianceManualMgt(Integer id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			Map<String, Object> map = new HashMap<>();
			TblComplianceManualMgtOracle manualMgt = tblComplianceManualMgtOracleService.findById(id);
			if (manualMgt.getDepartmentHead() != null) {
				manualMgt.setDepartmentHeadName(tblStaffOracleService.getCreatorUserInfo(manualMgt.getDepartmentHead()));
			}
			if (manualMgt.getTransactor() != null) {
				manualMgt.setTransactorName(tblStaffOracleService.getCreatorUserInfo(manualMgt.getTransactor()));
			}
			if (StringUtils.isNotEmpty(manualMgt.getFileIds())) {
				List<TblComplianceFileOracle> file = tblComplianceFileOracleService.findByIds(manualMgt.getFileIds());
				map.put("file", file);
			}
			map.put("manualMgt", manualMgt);
			return ResponseFormat.retParam(200, 200, map);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 合规风险 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<PageResult<TblComplianceRiskOracle>> getTblComplianceRiskList(TblComplianceRiskQueryParam param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			PageInfo<TblComplianceRiskOracle> pageInfo = tblComplianceRiskOracleService.getList(param);
			if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
				Map<Integer, String> departmentMap = new HashMap<>();
				Map<Integer, String> creatorMap = new HashMap<>();
				List<Integer> departments = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getDepartment()))
						.map(TblComplianceRiskOracle::getDepartment).distinct().collect(Collectors.toList());
				List<Integer> workUnitIds = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getWorkUnit()))
						.map(TblComplianceRiskOracle::getCreator).distinct().collect(Collectors.toList());
				if (CollectionUtil.isNotEmpty(departments)) {
					workUnitIds.addAll(departments);
				}
				List<Integer> creatorIds = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator()))
						.map(TblComplianceRiskOracle::getCreator).distinct().collect(Collectors.toList());
				if (CollectionUtil.isNotEmpty(workUnitIds)) {
					departmentMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(workUnitIds, ","));
				}
				if (CollectionUtil.isNotEmpty(creatorIds)) {
					creatorMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorIds, ","));
				}
				Map<Integer, String> finalDepartmentMap = departmentMap;
				Map<Integer, String> finalCreatorMap = creatorMap;
				pageInfo.getList().forEach(x -> {
					x.setDepartmentName(finalDepartmentMap.getOrDefault(x.getDepartment(), ""));
					x.setCreatorName(finalCreatorMap.getOrDefault(x.getCreator(), ""));
					x.setWorkUnitName(finalDepartmentMap.getOrDefault(x.getWorkUnit(), ""));
				});
				PageResult<TblComplianceRiskOracle> build = new PageResult<TblComplianceRiskOracle>().build(pageInfo);
				return ResponseFormat.retParam(200, 200, build);
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 合规风险 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<TblComplianceRiskOracle> saveOrUpdateTblComplianceRisk(TblComplianceRiskOracle param) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceRiskOracle risk = tblComplianceRiskOracleService.saveOrUpdate(param);
			return ResponseFormat.retParam(200, 200, risk);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 合规风险 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblComplianceRisk(Integer id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceRiskOracle risk = tblComplianceRiskOracleService.findById(id);
			tblComplianceRiskOracleService.delete(id);
			//附件-删除
			if (StringUtils.isNotBlank(risk.getFileIds())) {
				List<String> fileIds = Arrays.asList(risk.getFileIds().split(","));
				fileIds.forEach(x -> fileUploadService.fileRemove(Integer.valueOf(x)));
			}
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 合规风险 详情查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean<TblComplianceRiskResult> getTblComplianceRisk(Integer id) {

		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceRiskResult result = new TblComplianceRiskResult();
			TblComplianceRiskOracle risk = tblComplianceRiskOracleService.findById(id);
			risk.setCreatorName(tblStaffOracleService.getCreatorUserInfo(risk.getCreator()));
			if (Objects.nonNull(risk.getDepartment())) {
				risk.setDepartmentName(tblStaffOracleService.getWorkUnitIdUserInfo(risk.getDepartment()));
				risk.setWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(risk.getWorkUnit()));
			}
			if (StringUtils.isNotEmpty(risk.getFileIds())) {
				List<TblComplianceFileOracle> file = tblComplianceFileOracleService.findByIds(risk.getFileIds());
				result.setFile(file);
			}
			result.setData(risk);
			return ResponseFormat.retParam(200, 200, result);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}
}
