package com.huabo.legal.service.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.constant.LawServiceType;
import com.huabo.legal.oracle.entity.*;
import com.huabo.legal.oracle.service.*;
import com.huabo.legal.service.FileUploadService;
import com.huabo.legal.service.LegalServiceService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.vo.param.TblFwglLawServiceQueryParam;
import com.huabo.legal.vo.param.TblFwglSpecialLawServiceExamineQueryParam;
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
public class LegalServiceServiceImpl implements LegalServiceService {

	@Resource
	private TblFwglLawServiceOracleService tblFwglLawServiceOracleService;
	@Resource
	private TblFwglLawServiceLawyerOracleService tblFwglLawServiceLawyerOracleService;
	@Resource
	private TblFwglLawServiceWorkRecordOracleService tblFwglLawServiceWorkRecordOracleService;
	@Resource
	private TblFwglLawServiceWorkReportOracleService tblFwglLawServiceWorkReportOracleService;
	@Resource
	private TblFwglPerennialLawServiceGradeOracleService tblFwglPerennialLawServiceGradeOracleService;
	@Resource
	private TblFwglSpecialLawServiceExamineOracleService tblFwglSpecialLawServiceExamineOracleService;
	@Resource
	private TblFwglFileOracleService tblFwglFileOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblFwglLawServiceEvaluateOracleService tblFwglLawServiceEvaluateOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 常年/专项法律服务列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglLawServiceList(TblFwglLawServiceQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglLawServiceOracle> pageInfo = tblFwglLawServiceOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			//组装创建者名称
			{
				String creatorIds = pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCreator()))
						.map(TblFwglLawServiceOracle::getCreator).distinct().collect(Collectors.joining(","));
				//获取创建者map
				Map<String, String> creatorMap = getCreatorMap(creatorIds);
				pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCreator()))
						.forEach(x -> x.setCreatorName(creatorMap.getOrDefault(x.getCreator(), "")));
			}
			//组装审核人名称
			{
				String auditPersonIds = pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getAuditPerson()))
						.map(TblFwglLawServiceOracle::getAuditPerson).distinct().collect(Collectors.joining(","));
				//获取审核人map
				Map<String, String> auditPersonMap = getCreatorMap(auditPersonIds);
				pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getAuditPerson()))
						.forEach(x -> x.setAuditPersonName(auditPersonMap.getOrDefault(x.getAuditPerson(), "")));
			}
			//组装集团名称
			{
				String belongGroupIds = pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getBelongGroupId()))
						.map(TblFwglLawServiceOracle::getBelongGroupId).distinct().collect(Collectors.joining(","));
				//获取集团map
				Map<String, String> belongGroupMap = getBelongGroupMap(belongGroupIds);
				pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getBelongGroupId()))
						.forEach(x -> x.setBelongGroupName(belongGroupMap.getOrDefault(x.getBelongGroupId(), "")));
			}
			//组装单位名称
			{
				String workUnitIds = pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getWorkUnitId()))
						.map(TblFwglLawServiceOracle::getWorkUnitId).distinct().collect(Collectors.joining(","));
				//获取集团map
				Map<String, String> workUnitMap = getWorkUnitMap(workUnitIds);
				pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getWorkUnitId()))
						.forEach(x -> x.setWorkUnitName(workUnitMap.getOrDefault(x.getWorkUnitId(), "")));
			}

			PageResult<TblFwglLawServiceOracle> build = new PageResult<TblFwglLawServiceOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
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
	 * 获取创建者名称
	 * @param creatorIds
	 */
	private Map<String, String> getCreatorMap(String creatorIds) {
		if (StringUtils.isNotBlank(creatorIds)) {
			List<UserInfo> creators = tblStaffOracleService.getCreatorUserInfos(creatorIds);
			if (CollectionUtil.isNotEmpty(creators)) {
				return creators.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
			}
		}
		return new HashMap<>();
	}

	/**
	 * 常年/专项法律服务 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglLawService(TblFwglLawService param) {
		TblFwglLawServiceOracle fwglLawService = new TblFwglLawServiceOracle();
		BeanUtils.copyProperties(param, fwglLawService);
		TblFwglLawServiceOracle lawService = tblFwglLawServiceOracleService.saveOrUpdate(fwglLawService);
		return ResponseFormat.retParam(200, 200, lawService);
	}

	/**
	 * 常年/专项法律服务 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteTblFwglLawService(Long id, Integer lawServiceType) {
		TblFwglLawServiceOracle lawService = tblFwglLawServiceOracleService.findById(id);
		// 常年/专项法律服务 删除
		tblFwglLawServiceOracleService.delete(id);
		// 常年/专项法律服务-律师信息 删除
		if (StringUtils.isNotEmpty(lawService.getLawyerId())) {
			List<String> lawyerId = Arrays.asList(lawService.getLawyerId().split(","));
			lawyerId.forEach(x -> tblFwglLawServiceLawyerOracleService.delete(Long.valueOf(x)));
		}
		// 常年/专项法律服务-工作记录 删除
		if (StringUtils.isNotEmpty(lawService.getWorkRecordId())) {
			List<String> workRecordId = Arrays.asList(lawService.getWorkRecordId().split(","));
			workRecordId.forEach(x -> tblFwglLawServiceWorkRecordOracleService.delete(Long.valueOf(x)));
		}
		// 常年/专项 法律服务-工作报告/服务登记 删除
		if (StringUtils.isNotEmpty(lawService.getWorkReportId())) {
			List<String> workReportId = Arrays.asList(lawService.getWorkReportId().split(","));
			workReportId.forEach(x -> tblFwglLawServiceWorkReportOracleService.delete(Long.valueOf(x)));
		}
		//常年法律服务-评价 删除
		if (lawServiceType == LawServiceType.PERENNIAL_LAW_SERVICE) {
			if (StringUtils.isNotEmpty(lawService.getGradeId())) {
				List<String> gradeId = Arrays.asList(lawService.getGradeId().split(","));
				gradeId.forEach(x -> tblFwglLawServiceEvaluateOracleService.delete(Long.valueOf(x)));
			}
		} else if (lawServiceType == LawServiceType.SPECIAL_LAW_SERVICE) {
			//专项法律服务-考核 删除
			if (StringUtils.isNotEmpty(lawService.getExamineId())) {
				List<String> examineId = Arrays.asList(lawService.getExamineId().split(","));
				examineId.forEach(x -> tblFwglSpecialLawServiceExamineOracleService.delete(Long.valueOf(x)));
			}
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 常年/专项法律服务详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglLawService(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		// 常年/专项法律服务
		TblFwglLawServiceOracle lawService = tblFwglLawServiceOracleService.findById(id);
		lawService.setCreatorName(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(lawService.getCreator())));
		//表单中的所属集团
		if (StringUtils.isNotBlank(lawService.getBelongGroupId())) {
			lawService.setBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(Long.valueOf(lawService.getBelongGroupId())));
		}
		//表单中的单位
		if (StringUtils.isNotBlank(lawService.getWorkUnitId())) {
			lawService.setWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(Long.valueOf(lawService.getWorkUnitId())));
		}
		//审核人
		if (StringUtils.isNotBlank(lawService.getAuditPerson())) {
			lawService.setAuditPersonName(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(lawService.getAuditPerson())));
		}
		// 常年/专项法律服务-律师信息
		if (StringUtils.isNotEmpty(lawService.getLawyerId())) {
			List<TblFwglLawServiceLawyerOracle> lawServiceLawyer = tblFwglLawServiceLawyerOracleService.getList(lawService.getLawyerId());
			map.put("lawServiceLawyer", lawServiceLawyer);
		}
		// 常年/专项法律服务-工作记录
		if (StringUtils.isNotEmpty(lawService.getWorkRecordId())) {
			List<TblFwglLawServiceWorkRecordOracle> lawServiceWorkRecord = tblFwglLawServiceWorkRecordOracleService
					.getList(lawService.getWorkRecordId());
			map.put("lawServiceWorkRecord", lawServiceWorkRecord);
		}
		// 常年/专项 法律服务-工作报告/服务登记
		if (StringUtils.isNotBlank(lawService.getWorkReportId())) {
			List<TblFwglLawServiceWorkReportOracle> lawServiceWorkReport = tblFwglLawServiceWorkReportOracleService
					.getList(lawService.getWorkReportId());
			map.put("lawServiceWorkReport", lawServiceWorkReport);
		}
		if (lawService.getLawServiceType() == LawServiceType.PERENNIAL_LAW_SERVICE) {
			//常年法律服务-评价
			if (StringUtils.isNotBlank(lawService.getGradeId())) {
				List<TblFwglLawServiceEvaluateOracle> lawServiceEvaluate = tblFwglLawServiceEvaluateOracleService.getList(lawService.getGradeId());
				List<String> collect = lawServiceEvaluate.stream().filter(x -> x.getHireUnit() != null)
						.map(TblFwglLawServiceEvaluateOracle::getHireUnit).map(String::valueOf).distinct().collect(Collectors.toList());
				//获取集团map
				Map<String, String> workUnitMap = getWorkUnitMap(StringUtils.join(collect, ","));
				lawServiceEvaluate.stream().filter(x -> x.getHireUnit() != null)
						.forEach(x -> x.setHireUnitName(workUnitMap.getOrDefault(String.valueOf(x.getHireUnit()), "")));
				map.put("lawServiceEvaluate", lawServiceEvaluate);
			}
		} else if (lawService.getLawServiceType() == LawServiceType.SPECIAL_LAW_SERVICE) {
			//专项法律服务-考核
			if (StringUtils.isNotBlank(lawService.getExamineId())) {
				List<TblFwglSpecialLawServiceExamineOracle> lawServiceExamine = tblFwglSpecialLawServiceExamineOracleService
						.getList(lawService.getExamineId());
				map.put("lawServiceExamine", lawServiceExamine);
			}
		}
		map.put("lawService", lawService);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 常年/专项法律服务-律师信息 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglLawServiceLawyer(TblFwglLawServiceLawyer param) {
		TblFwglLawServiceLawyerOracle fwglLawServiceLawyer = new TblFwglLawServiceLawyerOracle();
		BeanUtils.copyProperties(param, fwglLawServiceLawyer);
		TblFwglLawServiceLawyerOracle lawServiceLawyer = tblFwglLawServiceLawyerOracleService.saveOrUpdate(fwglLawServiceLawyer);
		return ResponseFormat.retParam(200, 200, lawServiceLawyer);
	}

	/**
	 * 常年/专项法律服务-律师信息 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglLawServiceLawyer(Long id) {
		tblFwglLawServiceLawyerOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 常年/专项法律服务-工作记录 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglLawServiceWorkRecord(TblFwglLawServiceWorkRecord param) {
		TblFwglLawServiceWorkRecordOracle fwglLawServiceWorkRecord = new TblFwglLawServiceWorkRecordOracle();
		BeanUtils.copyProperties(param, fwglLawServiceWorkRecord);
		TblFwglLawServiceWorkRecordOracle lawServiceWorkRecord = tblFwglLawServiceWorkRecordOracleService.saveOrUpdate(fwglLawServiceWorkRecord);
		return ResponseFormat.retParam(200, 200, lawServiceWorkRecord);
	}

	/**
	 * 常年/专项法律服务-工作记录 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglLawServiceWorkRecord(Long id) {
		tblFwglLawServiceWorkRecordOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 常年/专项法律服务-工作报告表/服务登记 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglLawServiceWorkReport(TblFwglLawServiceWorkReport param) {
		TblFwglLawServiceWorkReportOracle fwglLawServiceWorkReport = new TblFwglLawServiceWorkReportOracle();
		BeanUtils.copyProperties(param, fwglLawServiceWorkReport);
		TblFwglLawServiceWorkReportOracle lawServiceWorkReport = tblFwglLawServiceWorkReportOracleService.saveOrUpdate(fwglLawServiceWorkReport);
		return ResponseFormat.retParam(200, 200, lawServiceWorkReport);
	}

	/**
	 * 常年/专项法律服务-工作报告表/服务登记 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteTblFwglLawServiceWorkReport(Long id) {
		TblFwglLawServiceWorkReportOracle lawServiceWorkReport = tblFwglLawServiceWorkReportOracleService.findById(id);
		//常年/专项法律服务-工作报告表/服务登记 刪除
		tblFwglLawServiceWorkReportOracleService.delete(id);
		if (StringUtils.isNotBlank(lawServiceWorkReport.getFileIds())) {
			List<String> fileIds = Arrays.asList(lawServiceWorkReport.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 常年法律服务-评分 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglPerennialLawServiceGrade(TblFwglPerennialLawServiceGrade param) {
		TblFwglPerennialLawServiceGradeOracle tblFwglPerennialLawServiceGrade = new TblFwglPerennialLawServiceGradeOracle();
		BeanUtils.copyProperties(param, tblFwglPerennialLawServiceGrade);
		TblFwglPerennialLawServiceGradeOracle perennialLawServiceGrade = tblFwglPerennialLawServiceGradeOracleService
				.saveOrUpdate(tblFwglPerennialLawServiceGrade);
		return ResponseFormat.retParam(200, 200, perennialLawServiceGrade);
	}

	/**
	 * 常年法律服务-评分 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglPerennialLawServiceGrade(Long id) {
		tblFwglPerennialLawServiceGradeOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 专项法律服务-考核 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglSpecialLawServiceExamine(TblFwglSpecialLawServiceExamine param) {
		TblFwglSpecialLawServiceExamineOracle fwglSpecialLawServiceExamine = new TblFwglSpecialLawServiceExamineOracle();
		BeanUtils.copyProperties(param, fwglSpecialLawServiceExamine);
		TblFwglSpecialLawServiceExamineOracle lawServiceExamine = tblFwglSpecialLawServiceExamineOracleService
				.saveOrUpdate(fwglSpecialLawServiceExamine);
		return ResponseFormat.retParam(200, 200, lawServiceExamine);
	}

	/**
	 * 专项法律服务-考核 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglSpecialLawServiceExamine(Long id) {
		tblFwglSpecialLawServiceExamineOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 常年/专项法律服务-律师信息详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglLawServiceLawyer(Long id) {
		TblFwglLawServiceLawyerOracle lawServiceLawyer = tblFwglLawServiceLawyerOracleService.findById(id);
		return ResponseFormat.retParam(200, 200, lawServiceLawyer);
	}

	/**
	 * 常年/专项法律服务-工作记录详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglLawServiceWorkRecord(Long id) {
		TblFwglLawServiceWorkRecordOracle lawServiceWorkRecord = tblFwglLawServiceWorkRecordOracleService.findById(id);
		return ResponseFormat.retParam(200, 200, lawServiceWorkRecord);
	}

	/**
	 * 常年/专项法律服务-工作报告表/服务登记详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglLawServiceWorkReport(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//常年/专项法律服务-工作报告表/服务登记详情
		TblFwglLawServiceWorkReportOracle lawServiceWorkReport = tblFwglLawServiceWorkReportOracleService.findById(id);
		//常年/专项法律服务-工作报告表/服务登记-文件列表
		if (StringUtils.isNotEmpty(lawServiceWorkReport.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(lawServiceWorkReport.getFileIds());
			map.put("files", files);
		}
		map.put("lawServiceWorkReport", lawServiceWorkReport);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 常年法律服务-评分详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglPerennialLawServiceGrade(Long id) {
		TblFwglPerennialLawServiceGradeOracle perennialLawServiceGrade = tblFwglPerennialLawServiceGradeOracleService.findById(id);
		return ResponseFormat.retParam(200, 200, perennialLawServiceGrade);
	}

	/**
	 * 专项法律服务-考核详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglSpecialLawServiceExamine(Long id) {
		TblFwglSpecialLawServiceExamineOracle specialLawServiceExamine = tblFwglSpecialLawServiceExamineOracleService.findById(id);
		return ResponseFormat.retParam(200, 200, specialLawServiceExamine);
	}

	/**
	 * 考核台账列表
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglSpecialLawServiceExamineList(TblFwglSpecialLawServiceExamineQueryParam param) {
		param.setBelongGroup(null);
		param.setCreator(null);
		param.setWorkUnit(null);
		PageInfo<TblFwglSpecialLawServiceExamineOracle> list = tblFwglSpecialLawServiceExamineOracleService.getList(param);
		PageResult<TblFwglSpecialLawServiceExamineOracle> result = new PageResult<TblFwglSpecialLawServiceExamineOracle>().build(list);
		return ResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 常年法律服务-评价表 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglLawServiceEvaluate(TblFwglLawServiceEvaluateOracle param) {
		TblFwglLawServiceEvaluateOracle lawServiceEvaluate = tblFwglLawServiceEvaluateOracleService.saveOrUpdate(param);
		return ResponseFormat.retParam(200, 200, lawServiceEvaluate);
	}

	/**
	 * 常年法律服务-评价详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglLawServiceEvaluate(Long id) {
		TblFwglLawServiceEvaluateOracle lawServiceEvaluate = tblFwglLawServiceEvaluateOracleService.findById(id);
		if (lawServiceEvaluate.getHireUnit() != null) {
			lawServiceEvaluate.setHireUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(lawServiceEvaluate.getHireUnit()));
		}
		return ResponseFormat.retParam(200, 200, lawServiceEvaluate);
	}

	/**
	 * 常年法律服务-评价 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglLawServiceEvaluate(Long id) {
		tblFwglLawServiceEvaluateOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}
}
