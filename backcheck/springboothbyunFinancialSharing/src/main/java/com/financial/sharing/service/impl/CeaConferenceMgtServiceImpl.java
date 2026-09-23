package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.entity.TblAttachment;
import com.financial.sharing.oracle.entity.TblCeaConferenceMgtOracle;
import com.financial.sharing.oracle.service.TblAttachmentService;
import com.financial.sharing.oracle.service.TblCeaConferenceMgtOracleService;
import com.financial.sharing.oracle.service.TblStaffOracleService;
import com.financial.sharing.service.CeaConferenceMgtService;
import com.financial.sharing.service.FileUploadService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.MyResponseFormat;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.TblCeaConferenceMgtQueryParam;
import com.financial.sharing.vo.result.FileVo;
import com.github.pagehelper.PageInfo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CeaConferenceMgtServiceImpl implements CeaConferenceMgtService {

	@Resource
	private TblCeaConferenceMgtOracleService tblCeaConferenceMgtOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblAttachmentService tblAttachmentService;

	/**
	 * 会议管理 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaConferenceMgtOracle> getTblCeaConferenceMgtList(TblCeaConferenceMgtQueryParam param) {
		PageInfo<TblCeaConferenceMgtOracle> pageInfo = tblCeaConferenceMgtOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> conferenceComperes = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getConferenceCompere())).distinct()
					.map(TblCeaConferenceMgtOracle::getConferenceCompere).distinct().collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaConferenceMgtOracle::getCreator).distinct().collect(Collectors.toList());
			List<Long> recipient = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getRecipient())).distinct()
					.map(TblCeaConferenceMgtOracle::getRecipient).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(conferenceComperes)) {
				creatorList.addAll(conferenceComperes);
			}
        if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
        if (CollectionUtil.isNotEmpty(recipient)) {
				creatorList.addAll(recipient);
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			pageInfo.getList().forEach(item -> {
				item.setConferenceCompereName(creatorUserInfoMap.getOrDefault(item.getConferenceCompere(), ""));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setRecipientName(creatorUserInfoMap.getOrDefault(item.getRecipient(), ""));
			});
			// 返回第一条记录作为示例
			if (!pageInfo.getList().isEmpty()) {
				return MyResponseFormat.<TblCeaConferenceMgtOracle>retParam(200, 200, pageInfo.getList().get(0));
			}
        }
        return MyResponseFormat.<TblCeaConferenceMgtOracle>retParam(200, 200, null);
	}

	/**
	 * 会议管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaConferenceMgtOracle> saveOrUpdateTblCeaConferenceMgt(TblCeaConferenceMgtOracle param) {
		if (Objects.nonNull(param.getId())) {
			TblCeaConferenceMgtOracle model = tblCeaConferenceMgtOracleService.findById(param.getId());
			//原无下发人，更新后有下发人，则更改会议管理状态为已下发
			if (Objects.isNull(model.getRecipient()) || Objects.equals(model.getRecipient(), 0)) {
				if (Objects.nonNull(param.getRecipient()) && !Objects.equals(param.getRecipient(), 0)) {
					param.setState(2);
				}
			}
        }
        if (Objects.equals(param.getFlagSubmit(), 1)) {
			param.setRecipient(0L);
			param.setState(3);
        }
        if (StringUtils.isNotBlank(param.getAttendees())) {
			//用途参与人员查询
			String a = "," + param.getAttendees() + ",";
			param.setAttendees(a);
		}
		TblCeaConferenceMgtOracle tblCeaConferenceMgtOracle = tblCeaConferenceMgtOracleService.saveOrUpdate(param);
		if (StringUtils.isNotBlank(param.getAttendees())) {
			String substring = param.getAttendees().substring(1, param.getAttendees().length() - 1);
			param.setAttendees(substring);
		}
        return MyResponseFormat.retParam(200, 200, tblCeaConferenceMgtOracle);
	}

	/**
	 * 会议管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaConferenceMgt(Long id) {
		TblCeaConferenceMgtOracle model = tblCeaConferenceMgtOracleService.findById(id);
		tblCeaConferenceMgtOracleService.delete(id);
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
        return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 会议管理 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<FileVo<TblCeaConferenceMgtOracle>> getTblCeaConferenceMgt(Long id) {
		FileVo<TblCeaConferenceMgtOracle> result = new FileVo<>();
		TblCeaConferenceMgtOracle model = tblCeaConferenceMgtOracleService.findById(id);
		if (Objects.nonNull(model.getConferenceCompere())) {
			model.setConferenceCompereName(tblStaffOracleService.getCreatorUserInfo(model.getConferenceCompere()));
		}
        if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		//去除前后2个逗号
		if (StringUtils.isNotBlank(model.getAttendees())) {
			String substring = model.getAttendees().substring(1, model.getAttendees().length() - 1);
			model.setAttendees(substring);
		}
        if (StringUtils.isNotBlank(model.getAttendees())) {
			model.setAttendeesName(tblStaffOracleService.getCreatorUserInfos(model.getAttendees()));
		}
        if (StringUtils.isNotEmpty(model.getFileIds())) {
			List<TblAttachment> file = tblAttachmentService.findByIds(model.getFileIds());
			result.setFile(file);
		}
        result.setData(model);
		return MyResponseFormat.retParam(200, 200, result);
	}
}
