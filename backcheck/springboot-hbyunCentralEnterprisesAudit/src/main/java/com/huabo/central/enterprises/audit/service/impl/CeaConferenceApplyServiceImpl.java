package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblAttachment;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaConferenceApplyOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblAttachmentService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaConferenceApplyOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaConferenceApplyService;
import com.huabo.central.enterprises.audit.service.FileUploadService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaConferenceApplyQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CeaConferenceApplyServiceImpl implements CeaConferenceApplyService {

	@Resource
	private TblCeaConferenceApplyOracleService tblCeaConferenceApplyOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblAttachmentService tblAttachmentService;

	/**
	 * 会议申请 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaConferenceApplyOracle> getTblCeaConferenceApplyList(TblCeaConferenceApplyQueryParam param) {
		PageInfo<TblCeaConferenceApplyOracle> pageInfo = tblCeaConferenceApplyOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> conferenceComperes = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getConferenceCompere())).distinct()
					.map(TblCeaConferenceApplyOracle::getConferenceCompere).distinct().collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaConferenceApplyOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(conferenceComperes)) {
				creatorList.addAll(conferenceComperes);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			pageInfo.getList().forEach(item -> {
				item.setConferenceCompereName(creatorUserInfoMap.getOrDefault(item.getConferenceCompere(), ""));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
			});
			PageResult<TblCeaConferenceApplyOracle> build = new PageResult<TblCeaConferenceApplyOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 会议申请 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaConferenceApplyOracle> saveOrUpdateTblCeaConferenceApply(TblCeaConferenceApplyOracle param) {
		//相同会议地点，校验时间范围是否已存在会议
		if (StringUtils.isNotBlank(param.getConferencePlace())) {
			tblCeaConferenceApplyOracleService
					.doConferencePlace(param.getConferencePlace(), param.getConferenceTimeStart(), param.getConferenceTimeEnd(), param.getId());
		}
		TblCeaConferenceApplyOracle tblCeaConferenceApplyOracle = tblCeaConferenceApplyOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, tblCeaConferenceApplyOracle);
	}

	/**
	 * 会议申请 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaConferenceApply(Long id) {
		TblCeaConferenceApplyOracle ceaConferenceApply = tblCeaConferenceApplyOracleService.findById(id);
		tblCeaConferenceApplyOracleService.delete(id);
		if (StringUtils.isNotBlank(ceaConferenceApply.getFileIds())) {
			List<String> fileIds = Arrays.asList(ceaConferenceApply.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 会议申请 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaConferenceApplyOracle> getTblCeaConferenceApply(Long id) {
		FileVo<TblCeaConferenceApplyOracle> result = new FileVo<>();
		TblCeaConferenceApplyOracle ceaConferenceApply = tblCeaConferenceApplyOracleService.findById(id);
		if (Objects.nonNull(ceaConferenceApply.getConferenceCompere())) {
			ceaConferenceApply.setConferenceCompereName(tblStaffOracleService.getCreatorUserInfo(ceaConferenceApply.getConferenceCompere()));
		}
		if (Objects.nonNull(ceaConferenceApply.getCreator())) {
			ceaConferenceApply.setCreatorName(tblStaffOracleService.getCreatorUserInfo(ceaConferenceApply.getCreator()));
		}
		if (StringUtils.isNotBlank(ceaConferenceApply.getAttendees())) {
			ceaConferenceApply.setAttendeesName(tblStaffOracleService.getCreatorUserInfos(ceaConferenceApply.getAttendees()));
		}
		if (StringUtils.isNotEmpty(ceaConferenceApply.getFileIds())) {
			List<TblAttachment> file = tblAttachmentService.findByIds(ceaConferenceApply.getFileIds());
			result.setFile(file);
		}
		result.setData(ceaConferenceApply);
		return MyResponseFormat.retParam(200, 200, result);
	}
}
