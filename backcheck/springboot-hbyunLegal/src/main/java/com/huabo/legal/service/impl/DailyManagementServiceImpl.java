package com.huabo.legal.service.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.oracle.entity.TblFwglConferenceManagementOracle;
import com.huabo.legal.oracle.entity.TblFwglFileOracle;
import com.huabo.legal.oracle.entity.TblFwglOtherFileMessageOracle;
import com.huabo.legal.oracle.service.TblFwglConferenceManagementOracleService;
import com.huabo.legal.oracle.service.TblFwglFileOracleService;
import com.huabo.legal.oracle.service.TblFwglOtherFileMessageOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.service.DailyManagementService;
import com.huabo.legal.service.FileUploadService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.vo.param.TblFwglConferenceManagementQueryParam;
import com.huabo.legal.vo.param.TblFwglOtherFileMessageQueryParam;
import com.huabo.legal.vo.result.TblFwglConferenceManagement;
import com.huabo.legal.vo.result.TblFwglOtherFileMessage;
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
public class DailyManagementServiceImpl implements DailyManagementService {
	@Resource
	private TblFwglConferenceManagementOracleService tblFwglConferenceManagementOracleService;
	@Resource
	private TblFwglFileOracleService tblFwglFileOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblFwglOtherFileMessageOracleService tblFwglOtherFileMessageOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;


	/**
	 * 会议管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglConferenceManagementList(TblFwglConferenceManagementQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglConferenceManagementOracle> pageInfo = tblFwglConferenceManagementOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<UserInfo> comperes;
			Map<String, String> comperesMap = new HashMap<>();
			Map<String, String> participantMap = new HashMap<>();
			//查询会议主持人
			String compere = pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCompere()))
					.map(TblFwglConferenceManagementOracle::getCompere).distinct().collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(compere)) {
				comperes = tblStaffOracleService.getCreatorUserInfos(compere);
				if (CollectionUtil.isNotEmpty(comperes)) {
					comperesMap = comperes.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
				}
			}
			//查询会议参与者
			List<String> participant = pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getParticipants()))
					.map(TblFwglConferenceManagementOracle::getParticipants).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(participant)) {
				participant.forEach(x -> {
					List<UserInfo> participantList = tblStaffOracleService.getCreatorUserInfos(x);
					if (CollectionUtil.isNotEmpty(participantList)) {
						String participants = participantList.stream().map(UserInfo::getRealName).collect(Collectors.joining(","));
						participantMap.put(x, participants);
					}
				});
			}
			Map<String, String> finalComperesMap = comperesMap;
			pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCompere())).forEach(x -> {
				x.setCompereName(finalComperesMap.getOrDefault(x.getCompere(), ""));
				x.setParticipantsNames(participantMap.getOrDefault(x.getParticipants(), ""));
			});
			PageResult<TblFwglConferenceManagementOracle> build = new PageResult<TblFwglConferenceManagementOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 会议管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglConferenceManagement(TblFwglConferenceManagement param) {
		TblFwglConferenceManagementOracle conferenceManagementMySql = new TblFwglConferenceManagementOracle();
		BeanUtils.copyProperties(param, conferenceManagementMySql);
		TblFwglConferenceManagementOracle conferenceManagement = tblFwglConferenceManagementOracleService.saveOrUpdate(conferenceManagementMySql);
		return ResponseFormat.retParam(200, 200, conferenceManagement);
	}

	/**
	 * 会议管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteTblFwglConferenceManagement(Long id) {
		TblFwglConferenceManagementOracle conferenceManagement = tblFwglConferenceManagementOracleService.findById(id);
		//会议管理 刪除
		tblFwglConferenceManagementOracleService.delete(id);
		//会议管理-文件 删除
		if (StringUtils.isNotBlank(conferenceManagement.getFileIds())) {
			List<String> fileIds = Arrays.asList(conferenceManagement.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 会议管理详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglConferenceManagement(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//会议管理详情
		TblFwglConferenceManagementOracle conferenceManagement = tblFwglConferenceManagementOracleService.findById(id);
		//会议主持人
		if (StringUtils.isNotBlank(conferenceManagement.getCompere())) {
			conferenceManagement.setCompereName(tblStaffOracleService.getCreatorUserInfo(Long.valueOf(conferenceManagement.getCompere())));
		}
		//查询会议参与者
		if (StringUtils.isNotBlank(conferenceManagement.getParticipants())) {
			List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(conferenceManagement.getParticipants());
			if (CollectionUtil.isNotEmpty(userInfos)) {
				List<String> collect = userInfos.stream().map(UserInfo::getRealName).collect(Collectors.toList());
				conferenceManagement.setParticipantsNames(StringUtils.join(collect, ","));
			}
		}
		//会议管理-文件列表
		if (StringUtils.isNotEmpty(conferenceManagement.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(conferenceManagement.getFileIds());
			map.put("files", files);
		}
		map.put("conferenceManagement", conferenceManagement);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 其他文件报文列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglOtherFileMessageList(TblFwglOtherFileMessageQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglOtherFileMessageOracle> pageInfo = tblFwglOtherFileMessageOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			PageResult<TblFwglOtherFileMessageOracle> build = new PageResult<TblFwglOtherFileMessageOracle>().build(pageInfo);
			//查询创建者
			String creatorIds = pageInfo.getList().stream().map(TblFwglOtherFileMessageOracle::getCreator).distinct()
					.collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(creatorIds)) {
				List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(creatorIds);
				if (CollectionUtil.isNotEmpty(userInfos)) {
					Map<String, String> map = userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
					pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCreator()))
							.forEach(x -> x.setCreatorName(map.getOrDefault(x.getCreator(), "")));
				}
			}
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 其他文件报文 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglOtherFileMessage(TblFwglOtherFileMessage param) {
		TblFwglOtherFileMessageOracle tblFwglOtherFileMessage = new TblFwglOtherFileMessageOracle();
		BeanUtils.copyProperties(param, tblFwglOtherFileMessage);
		TblFwglOtherFileMessageOracle otherFileMessage = tblFwglOtherFileMessageOracleService.saveOrUpdate(tblFwglOtherFileMessage);
		return ResponseFormat.retParam(200, 200, otherFileMessage);
	}

	/**
	 * 其他文件报文 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblFwglOtherFileMessage(Long id) {
		TblFwglOtherFileMessageOracle otherFileMessage = tblFwglOtherFileMessageOracleService.findById(id);
		//其他文件报文 刪除
		tblFwglOtherFileMessageOracleService.delete(id);
		//其他文件报文-文件 刪除
		if (StringUtils.isNotBlank(otherFileMessage.getFileIds())) {
			List<String> fileIds = Arrays.asList(otherFileMessage.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 其他文件报文详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglOtherFileMessage(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		TblFwglOtherFileMessageOracle otherFileMessage = tblFwglOtherFileMessageOracleService.findById(id);
		//会议管理-文件列表
		if (StringUtils.isNotEmpty(otherFileMessage.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(otherFileMessage.getFileIds());
			map.put("files", files);
		}
		map.put("otherFileMessage", otherFileMessage);
		return ResponseFormat.retParam(200, 200, map);
	}
}
