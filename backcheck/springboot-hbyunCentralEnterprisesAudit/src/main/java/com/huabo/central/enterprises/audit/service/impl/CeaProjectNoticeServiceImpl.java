package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.*;
import com.huabo.central.enterprises.audit.oracle.service.*;
import com.huabo.central.enterprises.audit.service.CeaProjectNoticeService;
import com.huabo.central.enterprises.audit.service.FileUploadService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.*;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.huabo.central.enterprises.audit.vo.result.ReviewTeamJsonResult;
import com.huabo.central.enterprises.audit.vo.result.TblCeaProjectDeclareInfoResult;
import com.huabo.central.enterprises.audit.vo.result.TblCeaProjectVoResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CeaProjectNoticeServiceImpl implements CeaProjectNoticeService {

	@Resource
	private TblCeaProjectNoticeService tblCeaProjectNoticeService;
	@Resource
	private TblCeaProjectNoticeExtService tblCeaProjectNoticeExtService;
	@Resource
	private TblCeaProjectDeclareService tblCeaProjectDeclareService;
	@Resource
	private TblCeaProjectAppraisingService tblCeaProjectAppraisingService;
	@Resource
	private TblCeaProjectQualityService tblCeaProjectQualityService;
	@Resource
	private TblCeaProjectDeclareGroupService tblCeaProjectDeclareGroupService;
	@Resource
	private TblCeaQualityAssessmentService tblCeaQualityAssessmentService;
	@Resource
	private TblCeaQualityAssessmentExtService tblCeaQualityAssessmentExtService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblAttachmentService tblAttachmentService;


	/**
	 * 项目评优-通知 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectNotice> getTblCeaProjectNoticeList(TblCeaProjectNoticeQueryParam param) {
		PageInfo<TblCeaProjectNotice> pageInfo = tblCeaProjectNoticeService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaProjectNotice::getCreator).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
			});
			PageResult<TblCeaProjectNotice> build = new PageResult<TblCeaProjectNotice>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 项目评优-通知 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectNotice> saveOrUpdateTblCeaProjectNoticeService(TblCeaProjectNotice param) {
		TblCeaProjectNotice model = tblCeaProjectNoticeService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 项目评优-通知 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaProjectNoticeService(Long id) {
		TblCeaProjectNotice model = tblCeaProjectNoticeService.findById(id);
		tblCeaProjectNoticeService.delete(id);
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 项目评优-通知 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<FileVo<TblCeaProjectNotice>> getTblCeaProjectNoticeService(Long id) {
		FileVo<TblCeaProjectNotice> result = new FileVo<>();
		TblCeaProjectNotice model = tblCeaProjectNoticeService.findById(id);
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (StringUtils.isNotEmpty(model.getFileIds())) {
			List<TblAttachment> file = tblAttachmentService.findByIds(model.getFileIds());
			result.setFile(file);
		}
		result.setData(model);
		return MyResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 项目评优-通知-下发 列表查询
	 * @param projectNoticeId
	 * @return
	 */
	@Override
	public MyJsonBean<List<TblCeaProjectNoticeExt>> getTblCeaProjectNoticeExtList(Long projectNoticeId) {
		List<TblCeaProjectNoticeExt> list = tblCeaProjectNoticeExtService.getList(projectNoticeId);
		if (CollectionUtil.isEmpty(list)) {
			return MyResponseFormat.retParam(200, 200, Collections.emptyList());
		}
		return MyResponseFormat.retParam(200, 200, list);
	}

	/**
	 * 项目评优-通知-下发-批量新增
	 * @param projectNoticeId
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<Void> updatesTblCeaProjectNoticeExt(Long projectNoticeId, List<TblCeaProjectNoticeExt> param) {
		tblCeaProjectNoticeExtService.updatesTblCeaProjectNoticeExt(projectNoticeId, param);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 项目评优-通知-首页展示 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectNoticeExt> getTblCeaProjectNoticeExtHomeList(TblCeaProjectNoticeExtQueryParam param) {
		PageInfo<TblCeaProjectNoticeExt> pageInfo = tblCeaProjectNoticeExtService.getTblCeaProjectNoticeExtHomeList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> distributeIds = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getDistributeId())).distinct()
					.map(TblCeaProjectNoticeExt::getDistributeId).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(distributeIds, ","));
			pageInfo.getList().forEach(item -> {
				item.setDistributeName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				if (Objects.nonNull(item.getProjectNoticeId())) {
					TblCeaProjectNotice model = tblCeaProjectNoticeService.findById(item.getProjectNoticeId());
					item.setNoticeTitle(model.getNoticeTitle());
					item.setNoticeContent(model.getNoticeContent());
					item.setDistributeName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
				}
			});
			PageResult<TblCeaProjectNoticeExt> build = new PageResult<TblCeaProjectNoticeExt>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 项目评优-通知-首页展示-同意
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectNoticeExt> submitTblCeaProjectNoticeExtHomeList(Long id) {
		tblCeaProjectNoticeExtService.submitTblCeaProjectNoticeExtHomeList(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 项目评优-申报 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectDeclare> getTblCeaProjectDeclareList(TblCeaProjectDeclareQueryParam param) {
		PageResult<TblCeaProjectDeclare> build = getTblCeaProjectDeclare1List(param);
		return MyResponseFormat.retParam(200, 200, build);
	}

	private PageResult<TblCeaProjectDeclare> getTblCeaProjectDeclare1List(TblCeaProjectDeclareQueryParam param) {
		PageInfo<TblCeaProjectDeclare> pageInfo = tblCeaProjectDeclareService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaProjectDeclare::getCreator).distinct().collect(Collectors.toList());
			List<Long> belongGroups = new ArrayList<>();
			List<Long> approvalBelongGroups = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApprovalBelongGroup())).distinct()
					.map(TblCeaProjectDeclare::getApprovalBelongGroup).distinct().collect(Collectors.toList());
			List<Long> completeBelongGroups = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCompleteBelongGroup())).distinct()
					.map(TblCeaProjectDeclare::getCompleteBelongGroup).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(approvalBelongGroups)) {
				belongGroups.addAll(approvalBelongGroups);
			}
			if (CollectionUtil.isNotEmpty(completeBelongGroups)) {
				belongGroups.addAll(completeBelongGroups);
			}
			Map<Long, String> belongGroupIdUserInfoMap = new HashMap<>();
			if (CollectionUtil.isNotEmpty(belongGroups)) {
				belongGroupIdUserInfoMap = tblStaffOracleService.getBelongGroupIdUserInfoMap(StringUtils.join(belongGroups, ","));
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
			Map<Long, String> finalBelongGroupIdUserInfoMap = belongGroupIdUserInfoMap;
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setApprovalBelongGroupName(finalBelongGroupIdUserInfoMap.getOrDefault(item.getApprovalBelongGroup(), ""));
				item.setCompleteBelongGroupName(finalBelongGroupIdUserInfoMap.getOrDefault(item.getCompleteBelongGroup(), ""));
			});
			return new PageResult<TblCeaProjectDeclare>().build(pageInfo);
		}
		return PageResult.buildNoData();
	}


	/**
	 * 项目评优-申报 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectDeclare> saveOrUpdateTblCeaProjectDeclareService(TblCeaProjectDeclare param) {
		//评审组-针对项目申报评优评比
		String json;
		if (CollectionUtil.isNotEmpty(param.getReviewTeamList())) {
			json = JsonMapper.INSTANCE.toJson(param.getReviewTeamList());
		} else {
			json = JsonMapper.INSTANCE.toJson(Collections.emptyList());
		}
		param.setReviewTeamJson(json);
		TblCeaProjectDeclare model = tblCeaProjectDeclareService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 项目评优-申报 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteTblCeaProjectDeclareService(Long id) {
		TblCeaProjectDeclare model = tblCeaProjectDeclareService.findById(id);
		tblCeaProjectDeclareService.delete(id);
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 项目评优-申报 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<FileVo<TblCeaProjectDeclare>> getTblCeaProjectDeclareService(Long id) {
		FileVo<TblCeaProjectDeclare> result = new FileVo<>();
		TblCeaProjectDeclare model = tblCeaProjectDeclareService.findById(id);
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (Objects.nonNull(model.getApprovalBelongGroup())) {
			model.setApprovalBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(model.getApprovalBelongGroup()));
		}
		if (Objects.nonNull(model.getCompleteBelongGroup())) {
			model.setCompleteBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(model.getCompleteBelongGroup()));
		}
		if (StringUtils.isNotEmpty(model.getFileIds())) {
			List<TblAttachment> file = tblAttachmentService.findByIds(model.getFileIds());
			result.setFile(file);
		}
		if (StringUtils.isNotBlank(model.getReviewTeamJson())) {
			JsonMapper jsonMapper = JsonMapper.INSTANCE;
			List<ReviewTeamJsonResult> jsonList = jsonMapper
					.fromJson(model.getReviewTeamJson(), jsonMapper.buildCollectionType(List.class, ReviewTeamJsonResult.class));
			if (CollectionUtil.isNotEmpty(jsonList)) {
				jsonList.stream().filter(item -> Objects.nonNull(item.getUserId()))
						.forEach(item -> item.setUserName(tblStaffOracleService.getCreatorUserInfo(item.getUserId())));
			}
			model.setReviewTeamList(jsonList);
		}
		result.setData(model);
		return MyResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 项目评优-评优 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectAppraising> getTblCeaProjectAppraisingList(TblCeaProjectAppraisingQueryParam param) {
		PageInfo<TblCeaProjectAppraising> pageInfo = tblCeaProjectAppraisingService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaProjectAppraising::getCreator).distinct().collect(Collectors.toList());
			List<Long> contactId = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getContactId())).distinct()
					.map(TblCeaProjectAppraising::getContactId).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			if (CollectionUtil.isNotEmpty(contactId)) {
				creatorList.addAll(contactId);
			}
			List<Long> declareBelongGroups = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getDeclareBelongGroup())).distinct()
					.map(TblCeaProjectAppraising::getDeclareBelongGroup).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			Map<Long, String> belongGroupIdUserInfoMap = new HashMap<>();
			if (CollectionUtil.isNotEmpty(declareBelongGroups)) {
				belongGroupIdUserInfoMap = tblStaffOracleService.getBelongGroupIdUserInfoMap(StringUtils.join(declareBelongGroups, ","));

			}
			Map<Long, String> finalBelongGroupIdUserInfoMap = belongGroupIdUserInfoMap;
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setContactName(creatorUserInfoMap.getOrDefault(item.getContactId(), ""));
				item.setDeclareBelongGroupName(finalBelongGroupIdUserInfoMap.getOrDefault(item.getDeclareBelongGroup(), ""));
			});
			PageResult<TblCeaProjectAppraising> build = new PageResult<TblCeaProjectAppraising>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 项目评优-评优 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectAppraising> saveOrUpdateTblCeaProjectAppraisingService(TblCeaProjectAppraising param) {
		TblCeaProjectAppraising model = tblCeaProjectAppraisingService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 项目评优-评优 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteTblCeaProjectAppraisingService(Long id) {
		TblCeaProjectAppraising model = tblCeaProjectAppraisingService.findById(id);
		tblCeaProjectAppraisingService.delete(id);
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 项目评优-评优 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<FileVo<TblCeaProjectAppraising>> getTblCeaProjectAppraisingService(Long id) {
		FileVo<TblCeaProjectAppraising> result = new FileVo<>();
		TblCeaProjectAppraising model = tblCeaProjectAppraisingService.findById(id);
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (Objects.nonNull(model.getContactId())) {
			model.setContactName(tblStaffOracleService.getCreatorUserInfo(model.getContactId()));
		}
		if (Objects.nonNull(model.getDeclareBelongGroup())) {
			model.setDeclareBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(model.getDeclareBelongGroup()));
		}
		if (StringUtils.isNotEmpty(model.getFileIds())) {
			List<TblAttachment> file = tblAttachmentService.findByIds(model.getFileIds());
			result.setFile(file);
		}
		if (StringUtils.isNotBlank(model.getImplementationPlanId())) {
			List<Long> ids = Arrays.stream(model.getImplementationPlanId().split(",")).map(Long::valueOf).collect(Collectors.toList());
			TblCeaProjectDeclareQueryParam queryParam = new TblCeaProjectDeclareQueryParam();
			queryParam.setIds(ids);
			model.setImplementationPlanList(getTblCeaProjectDeclare1List(queryParam).getTlist());
		}
		result.setData(model);
		return MyResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 项目评优-质量 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectQuality> getTblCeaProjectQualityList(TblCeaProjectQualityQueryParam param) {
		PageInfo<TblCeaProjectQuality> pageInfo = tblCeaProjectQualityService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaProjectQuality::getCreator).distinct().collect(Collectors.toList());
			List<Long> assessIds = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getAssessId())).distinct()
					.map(TblCeaProjectQuality::getAssessId).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			if (CollectionUtil.isNotEmpty(assessIds)) {
				creatorList.addAll(assessIds);
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setAssessName(creatorUserInfoMap.getOrDefault(item.getAssessId(), ""));
				if (Objects.nonNull(item.getAssessProjectId())) {
					item.setAssessProjectName(tblCeaProjectQualityService.getTblCeaProjectName(item.getAssessProjectId()));
				}
			});
			PageResult<TblCeaProjectQuality> build = new PageResult<TblCeaProjectQuality>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 项目评优-质量 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectQuality> saveOrUpdateTblCeaProjectQuality(TblCeaProjectQuality param) {
		TblCeaProjectQuality model = tblCeaProjectQualityService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 项目评优-质量 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteTblCeaProjectQuality(Long id) {
		tblCeaProjectQualityService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 项目评优-质量 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectQuality> getTblCeaProjectQuality(Long id) {
		TblCeaProjectQuality result = tblCeaProjectQualityService.findById(id);
		if (Objects.nonNull(result.getCreator())) {
			result.setCreatorName(tblStaffOracleService.getCreatorUserInfo(result.getCreator()));
		}
		if (Objects.nonNull(result.getAssessId())) {
			result.setAssessName(tblStaffOracleService.getCreatorUserInfo(result.getAssessId()));
		}
		if (Objects.nonNull(result.getAssessProjectId())) {
			result.setAssessProjectName(tblCeaProjectQualityService.getTblCeaProjectName(result.getAssessProjectId()));
		}
		return MyResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 项目管理-实施方案 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectVoResult> getTblCeaProjectList(TblCeaProjectQueryVoParam param) {
		PageInfo<TblCeaProjectVoResult> pageInfo;
		if (Objects.nonNull(param.getAssessId())) {
			pageInfo = tblCeaProjectQualityService.getTblCeaProjectList(param);
		} else {
			pageInfo = tblCeaProjectQualityService.getTblCeaProjectVoList(param);
		}
		return MyResponseFormat.retParam(200, 200, pageInfo);
	}

	/**
	 * 项目评优-申报-分组 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectDeclareGroup> getTblCeaProjectDeclareGroupList(TblCeaProjectDeclareGroupQueryParam param) {
		PageInfo<TblCeaProjectDeclareGroup> pageInfo = tblCeaProjectDeclareGroupService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaProjectDeclareGroup::getCreator).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				if (StringUtils.isNotBlank(item.getReviewTeamPersonnelIds())) {
					item.setUserInfoList(tblStaffOracleService.getCreatorUserInfos(item.getReviewTeamPersonnelIds()));
				}
			});
			PageResult<TblCeaProjectDeclareGroup> build = new PageResult<TblCeaProjectDeclareGroup>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 项目评优-申报-分组 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectDeclareGroup> saveOrUpdateTblCeaProjectDeclareGroupService(TblCeaProjectDeclareGroup param) {
		TblCeaProjectDeclareGroup model = tblCeaProjectDeclareGroupService.saveOrUpdate(param);
		if (CollectionUtil.isNotEmpty(param.getProjectDeclareInfoList())) {
			param.getProjectDeclareInfoList().forEach(item -> {
				long sum = item.getReviewTeamList().stream().filter(it -> StringUtils.isNotBlank(it.getUserGrade()))
						.map(ReviewTeamJsonResult::getUserGrade).map(Long::valueOf).collect(Collectors.summarizingLong(Long::intValue)).getSum();
				item.setSort(sum);
			});
			List<TblCeaProjectDeclareInfoResult> collect = param.getProjectDeclareInfoList().stream()
					.sorted(Comparator.comparingLong(TblCeaProjectDeclareInfoResult::getSort)).collect(Collectors.toList());
			int i = 1;
			List<Long> sortList = new ArrayList<>();
			for (TblCeaProjectDeclareInfoResult item : collect) {
				item.setResultSort(i);
				//对比上一个排序是否相同
				if (CollectionUtil.isNotEmpty(sortList) && Objects.equals(sortList.get(i - 2), item.getSort())) {
					item.setResultSort(i - 1);
				}
				sortList.add(item.getSort());
				i++;
			}
			param.setProjectDeclareInfoList(collect);
			param.getProjectDeclareInfoList().forEach(item -> {
				//更新项目申报分组
				TblCeaProjectDeclare update = new TblCeaProjectDeclare();
				update.setId(item.getId());
				update.setGroupId(model.getId());
				update.setResultSort(item.getResultSort());
				if (CollectionUtil.isNotEmpty(item.getReviewTeamList())) {
					String json = JsonMapper.INSTANCE.toJson(item.getReviewTeamList());
					update.setReviewTeamJson(json);
				}
				tblCeaProjectDeclareService.saveOrUpdate(update);
			});
		}
		if (CollectionUtil.isNotEmpty(param.getDeleteIds())) {
			param.getDeleteIds().forEach(item -> tblCeaProjectDeclareService.updateNull(item));
		}
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 项目评优-申报-分组 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaProjectDeclareGroupService(Long id) {
		TblCeaProjectDeclareGroup model = tblCeaProjectDeclareGroupService.findById(id);
		tblCeaProjectDeclareGroupService.delete(id);
		//更新分组为空
		tblCeaProjectDeclareService.updateNullGroupList(model.getId());
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 项目评优-申报-分组 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<FileVo<TblCeaProjectDeclareGroup>> getTblCeaProjectDeclareGroupService(Long id) {
		FileVo<TblCeaProjectDeclareGroup> result = new FileVo<>();
		TblCeaProjectDeclareGroup model = tblCeaProjectDeclareGroupService.findById(id);
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (StringUtils.isNotBlank(model.getReviewTeamPersonnelIds())) {
			model.setUserInfoList(tblStaffOracleService.getCreatorUserInfos(model.getReviewTeamPersonnelIds()));
		}
		TblCeaProjectDeclareQueryParam queryParam = new TblCeaProjectDeclareQueryParam();
		queryParam.setPageNumber(1);
		queryParam.setPageSize(10000);
		queryParam.setGroupId(model.getId());
		List<TblCeaProjectDeclareInfoResult> collect = getTblCeaProjectDeclare1List(queryParam).getTlist().stream().map(item -> {
			TblCeaProjectDeclareInfoResult declareInfo = new TblCeaProjectDeclareInfoResult();
			BeanUtils.copyProperties(item, declareInfo);
			if (StringUtils.isNotBlank(item.getReviewTeamJson())) {
				JsonMapper jsonMapper = JsonMapper.INSTANCE;
				List<ReviewTeamJsonResult> jsonList = jsonMapper
						.fromJson(item.getReviewTeamJson(), jsonMapper.buildCollectionType(List.class, ReviewTeamJsonResult.class));
				if (CollectionUtil.isNotEmpty(jsonList)) {
					jsonList.stream().filter(it -> Objects.nonNull(it.getUserId()))
							.forEach(it -> it.setUserName(tblStaffOracleService.getCreatorUserInfo(it.getUserId())));
				}
				declareInfo.setReviewTeamList(jsonList);
			}
			return declareInfo;
		}).collect(Collectors.toList());
		model.setProjectDeclareInfoList(CollectionUtil.isNotEmpty(collect) ? collect : Collections.emptyList());
		if (StringUtils.isNotEmpty(model.getFileIds())) {
			List<TblAttachment> file = tblAttachmentService.findByIds(model.getFileIds());
			result.setFile(file);
		}
		result.setData(model);
		return MyResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 项目评优排序 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaProjectDeclare> getTblCeaProjectDeclareSortList(TblCeaProjectDeclareSortQueryParam param) {
		PageInfo<TblCeaProjectDeclare> pageInfo = tblCeaProjectDeclareService.getTblCeaProjectDeclareSortList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaProjectDeclare::getCreator).distinct().collect(Collectors.toList());
			List<Long> belongGroups = new ArrayList<>();
			List<Long> approvalBelongGroups = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApprovalBelongGroup())).distinct()
					.map(TblCeaProjectDeclare::getApprovalBelongGroup).distinct().collect(Collectors.toList());
			List<Long> completeBelongGroups = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCompleteBelongGroup())).distinct()
					.map(TblCeaProjectDeclare::getCompleteBelongGroup).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(approvalBelongGroups)) {
				belongGroups.addAll(approvalBelongGroups);
			}
			if (CollectionUtil.isNotEmpty(completeBelongGroups)) {
				belongGroups.addAll(completeBelongGroups);
			}
			Map<Long, String> belongGroupIdUserInfoMap = new HashMap<>();
			if (CollectionUtil.isNotEmpty(belongGroups)) {
				belongGroupIdUserInfoMap = tblStaffOracleService.getBelongGroupIdUserInfoMap(StringUtils.join(belongGroups, ","));
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
			Map<Long, String> finalBelongGroupIdUserInfoMap = belongGroupIdUserInfoMap;
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setApprovalBelongGroupName(finalBelongGroupIdUserInfoMap.getOrDefault(item.getApprovalBelongGroup(), ""));
				item.setCompleteBelongGroupName(finalBelongGroupIdUserInfoMap.getOrDefault(item.getCompleteBelongGroup(), ""));
			});
			PageResult<TblCeaProjectDeclare> build = new PageResult<TblCeaProjectDeclare>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 项目评优排序-更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<Void> updateTblCeaProjectDeclareSort(UpdateTblCeaProjectDeclareSortParam param) {
		TblCeaProjectDeclare update = new TblCeaProjectDeclare();
		update.setId(param.getId());
		update.setFlagAbandoned(param.getFlagAbandoned());
		update.setResultSort(param.getResultSort());
		tblCeaProjectDeclareService.saveOrUpdate(update);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 项目评优-申报-分组 是否可以审批判断
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Boolean> getTblCeaProjectDeclareGroupIsApproval(Long id) {
		List<TblCeaProjectDeclare> list = tblCeaProjectDeclareService.getGroupId(id);
		long count1 = list.stream().filter(it -> {
			String reviewTeamJson = it.getReviewTeamJson();
			if (StringUtils.isNotBlank(reviewTeamJson)) {
				JsonMapper jsonMapper = JsonMapper.INSTANCE;
				List<ReviewTeamJsonResult> reviewTeamJsonList = jsonMapper
						.fromJson(reviewTeamJson, jsonMapper.buildCollectionType(List.class, ReviewTeamJsonResult.class));
				long count = reviewTeamJsonList.stream().filter(it1 -> StringUtils.isBlank(it1.getUserGrade())).count();
				return count == 0;
			}
			return false;
		}).count();
		if (count1 == list.size()) {
			return MyResponseFormat.retParam(200, 200, true);
		} else {
			return MyResponseFormat.retParam(200, 200, false);
		}
	}

	/**
	 * 审计工质量评估 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaQualityAssessment> getTblCeaQualityAssessmentList(TblCeaQualityAssessmentQueryParam param) {
		PageInfo<TblCeaQualityAssessment> pageInfo = tblCeaQualityAssessmentService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaQualityAssessment::getCreator).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));

			List<Long> fillBelongGroups = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getFillBelongGroup())).distinct()
					.map(TblCeaQualityAssessment::getFillBelongGroup).distinct().collect(Collectors.toList());
			Map<Long, String> belongGroupIdUserInfoMap = tblStaffOracleService.getBelongGroupIdUserInfoMap(StringUtils.join(fillBelongGroups, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setFillBelongGroupName(belongGroupIdUserInfoMap.getOrDefault(item.getFillBelongGroup(), ""));
			});
			PageResult<TblCeaQualityAssessment> build = new PageResult<TblCeaQualityAssessment>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 审计工质量评估 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<TblCeaQualityAssessment> saveOrUpdateTblCeaQualityAssessment(TblCeaQualityAssessment param) {
		TblCeaQualityAssessment model = tblCeaQualityAssessmentService.saveOrUpdate(param);
		if (CollectionUtil.isNotEmpty(param.getQualityAssessmentExtList())) {
			param.getQualityAssessmentExtList().forEach(item -> {
				item.setQualityAssessmentId(model.getId());
				tblCeaQualityAssessmentExtService.saveOrUpdate(item);
			});
		}
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 审计工质量评估 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaQualityAssessment(Long id) {
		tblCeaQualityAssessmentService.delete(id);
		TblCeaQualityAssessmentExtQueryParam queryParam = new TblCeaQualityAssessmentExtQueryParam();
		queryParam.setQualityAssessmentId(id);
		List<TblCeaQualityAssessmentExt> list = tblCeaQualityAssessmentExtService.getList(queryParam);
		if (CollectionUtil.isNotEmpty(list)) {
			list.forEach(item -> tblCeaQualityAssessmentExtService.delete(item.getId()));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 审计工质量评估 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaQualityAssessment> getTblCeaQualityAssessmentService(Long id) {
		TblCeaQualityAssessment model = tblCeaQualityAssessmentService.findById(id);
		if (Objects.nonNull(model)) {
			if (Objects.nonNull(model.getFillBelongGroup())) {
				model.setFillBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(model.getFillBelongGroup()));
			}
			if (Objects.nonNull(model.getCreator())) {
				model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
			}
			TblCeaQualityAssessmentExtQueryParam queryParam = new TblCeaQualityAssessmentExtQueryParam();
			queryParam.setQualityAssessmentId(id);
			List<TblCeaQualityAssessmentExt> list = tblCeaQualityAssessmentExtService.getList(queryParam);
			if (CollectionUtil.isNotEmpty(list)) {
				list.forEach(item -> {
					if (StringUtils.isNotEmpty(item.getFileIds())) {
						List<TblAttachment> file = tblAttachmentService.findByIds(item.getFileIds());
						item.setFileList(file);
					}
				});
				model.setQualityAssessmentExtList(list);
			}
		}
		return MyResponseFormat.retParam(200, 200, model);
	}


	//	@Override
	//	public JsonBean ceaProjectDeclareExport(HttpServletResponse response,TblCeaProjectDeclareQueryParam param) {
	//		try {
	//			PageResult<TblCeaProjectDeclare> build = getCeaProjectDeclareExport(param);
	//			List<TblCeaProjectDeclare> listData = build.getTlist();
	//			String[] titles = {"项目名称", "实施单位", "项目类型","组长","副组长","主审","参审人员","项目主要成果及特点","核减内容"};
	//			TblCeaProjectDeclare entity = new TblCeaProjectDeclare();
	//			List<Object[]> objs = new ArrayList<>();
	//			for (TblCeaProjectDeclare bean : listData) {
	//			    Object[] obj = new Object[titles.length];
	//			    obj[0] = bean.getImplementationProjectName();
	//			    obj[1] = bean.getCompleteBelongGroupName();
	//			    obj[2] = bean.getImplementationProjectType();
	//			    obj[3] = bean.getImplementationPlanTeamLeader();
	//			    obj[4] = bean.getImplementationPlanTeamfzzname();
	//			    obj[5] = bean.getImplementationPlanMainReviewer();
	//			    List<UserInfo> listCsrz = bean.getImplementationPlanReviewers();
	//			    if(null!=listCsrz) {
	//			    	String csrz = "";
	//			    	for (int i = 0; i < listCsrz.size(); i++) {
	//			    		csrz = csrz+listCsrz.get(i).getRealName();
	//			    		if(listCsrz.size()!=(i+1)) {
	//			    			csrz = csrz+"，";
	//			    		}
	//					}
	//			    	obj[6] = csrz;
	//			    }else {
	//			    	obj[6] = "";
	//			    }
	//			    obj[7] = bean.getMainFeatures1();
	//			    Integer hjje = tblCeaProjectDeclareMapper.selectSumHjjeByProjectid(bean.getImplementationPlanId());
	//			    obj[8] = hjje;
	//			    objs.add(obj);
	//			}
	//			response.setContentType("application/binary;charset=UTF-8");
	//			response.setHeader("Content-Disposition", "attachment; filename=" + new String("项目申报汇总".getBytes(), "UTF-8") + ".xlsx");
	//			ImportOrExportExcelUtil.exportExcel(titles, objs, response.getOutputStream(), null);
	//		} catch (UnsupportedEncodingException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
	//
	//	}

}
