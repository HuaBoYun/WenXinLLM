package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblAttachment;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaWeekly;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaWeeklyExt;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaWeeklyExtMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblAttachmentService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaWeeklyExtService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaWeeklyService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaWeeklyService;
import com.huabo.central.enterprises.audit.service.FileUploadService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.RemindTblCeaWeeklyParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaWeeklyQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.huabo.central.enterprises.audit.vo.result.RemindTblCeaWeeklyResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CeaWeeklyServiceImpl implements CeaWeeklyService {

	@Resource
	private TblCeaWeeklyService tblCeaWeeklyService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblAttachmentService tblAttachmentService;
	@Resource
	private TblCeaWeeklyExtService ceaWeeklyExtService;
	@Resource
	private TblCeaWeeklyExtMapper tblCeaWeeklyExtMapper;

	/**
	 * 周报 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaWeekly> getTblCeaWeeklyList(TblCeaWeeklyQueryParam param) {
		PageInfo<TblCeaWeekly> pageInfo = tblCeaWeeklyService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> staffId = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getStaffId())).map(TblCeaWeekly::getStaffId).distinct()
					.collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).map(TblCeaWeekly::getCreator).distinct()
					.collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(staffId)) {
				creatorList.addAll(staffId);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			List<Long> reportWorkUnits = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getReportWorkUnit()))
					.map(TblCeaWeekly::getReportWorkUnit).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			Map<Long, String> workUnitInfoMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(reportWorkUnits, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setStaffName(creatorUserInfoMap.getOrDefault(item.getStaffId(), ""));
				item.setReportWorkUnitName(workUnitInfoMap.getOrDefault(item.getReportWorkUnit(), ""));
			});
			PageResult<TblCeaWeekly> build = new PageResult<TblCeaWeekly>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 周报 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<TblCeaWeekly> saveOrUpdateTblCeaWeekly(TblCeaWeekly param) {
		TblCeaWeekly model = tblCeaWeeklyService.saveOrUpdate(param);
		if (Objects.equals(param.getFlagSummary(), 1)) {
			if (CollectionUtil.isEmpty(param.getIds())) {
				throw new ServiceException(400, "汇总周报IDS必填");
			}
			//先删除 后新增
			TblCeaWeeklyExt tblCeaWeeklyExt = new TblCeaWeeklyExt();
			tblCeaWeeklyExt.setWeeklyId(model.getId());
			tblCeaWeeklyExtMapper.delete(tblCeaWeeklyExt);
			//周报汇总
			param.getIds().forEach(item -> {
				TblCeaWeeklyExt save = new TblCeaWeeklyExt();
				save.setWeeklyId(model.getId());
				save.setRelationWeeklyId(item);
				ceaWeeklyExtService.saveOrUpdate(save);
			});
		}
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 周报 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaWeekly(Long id) {
		TblCeaWeekly model = tblCeaWeeklyService.findById(id);
		tblCeaWeeklyService.delete(id);
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 周报 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<FileVo<TblCeaWeekly>> getTblCeaWeekly(Long id) {
		FileVo<TblCeaWeekly> result = new FileVo<>();
		TblCeaWeekly model = tblCeaWeeklyService.findById(id);
		if (Objects.nonNull(model.getStaffId())) {
			model.setStaffName(tblStaffOracleService.getCreatorUserInfo(model.getStaffId()));
		}
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (Objects.nonNull(model.getReportWorkUnit())) {
			model.setReportWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(model.getReportWorkUnit()));
		}
		if (StringUtils.isNotEmpty(model.getFileIds())) {
			List<TblAttachment> file = tblAttachmentService.findByIds(model.getFileIds());
			result.setFile(file);
		}
		if (Objects.equals(model.getFlagSummary(), 1)) {
			List<TblCeaWeeklyExt> list = ceaWeeklyExtService.getList(id);
			if (CollectionUtil.isNotEmpty(list)) {
				List<Long> ids = list.stream().map(TblCeaWeeklyExt::getRelationWeeklyId).distinct().collect(Collectors.toList());
				model.setIds(ids);
				List<TblCeaWeekly> list1 = tblCeaWeeklyService.getIdsList(ids);
				model.setInfoIds(list1);
			}
		}
		result.setData(model);
		return MyResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 周报每周四提醒-指定角色 查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<RemindTblCeaWeeklyResult> remindTblCeaWeekly(RemindTblCeaWeeklyParam param) {
		Long staffId = param.getCreator();
		Boolean flagRole = tblStaffOracleService.getUserRole(staffId, 796709L);
		if (Boolean.FALSE.equals(flagRole)) {
			return MyResponseFormat.retParam(200, 200, new RemindTblCeaWeeklyResult(false));
		}
		Calendar calendar = Calendar.getInstance();
		//获取当前日期是一周中的周几
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		day -= 1;
		if (day == 0) {
			day = 7;
		}
		log.info("当前周几:{}", day);
		if (day == 4) {
			RemindTblCeaWeeklyResult result = new RemindTblCeaWeeklyResult();
			result.setFlagRemind(true);
			result.setRemindMsg("今天周四，记得填写周报");
			return MyResponseFormat.retParam(200, 200, result);
		} else {
			return MyResponseFormat.retParam(200, 200, new RemindTblCeaWeeklyResult(false));
		}
	}
}
