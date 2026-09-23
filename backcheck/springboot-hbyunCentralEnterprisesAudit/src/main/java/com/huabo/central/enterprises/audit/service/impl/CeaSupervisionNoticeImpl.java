package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblAttachment;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaSupervisionNoticeOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblAttachmentService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaSupervisionNoticeOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaSupervisionNotice;
import com.huabo.central.enterprises.audit.service.FileUploadService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaSupervisionNoticeQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CeaSupervisionNoticeImpl implements CeaSupervisionNotice {

	@Resource
	private TblCeaSupervisionNoticeOracleService tblCeaSupervisionNoticeOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblAttachmentService tblAttachmentService;

	/**
	 * 督办通知单 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaSupervisionNoticeOracle> getTblCeaSupervisionNoticeList(TblCeaSupervisionNoticeQueryParam param) {
		PageInfo<TblCeaSupervisionNoticeOracle> pageInfo = tblCeaSupervisionNoticeOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> transactors = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getTransactor())).distinct()
					.map(TblCeaSupervisionNoticeOracle::getTransactor).distinct().collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaSupervisionNoticeOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(transactors)) {
				creatorList.addAll(transactors);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			List<Long> supervisionWorkUnits = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getSupervisionWorkUnit())).distinct()
					.map(TblCeaSupervisionNoticeOracle::getSupervisionWorkUnit).distinct().collect(Collectors.toList());
			Map<Long, String> workUnitIdUserInfoMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(supervisionWorkUnits, ","));
			pageInfo.getList().forEach(item -> {
				item.setSupervisionWorkUnitName(workUnitIdUserInfoMap.getOrDefault(item.getSupervisionWorkUnit(), ""));
				item.setTransactorName(creatorUserInfoMap.getOrDefault(item.getTransactor(), ""));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
			});
			PageResult<TblCeaSupervisionNoticeOracle> build = new PageResult<TblCeaSupervisionNoticeOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 督办通知单 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaSupervisionNoticeOracle> saveOrUpdateTblCeaSupervisionNotice(TblCeaSupervisionNoticeOracle param) {
		TblCeaSupervisionNoticeOracle model = tblCeaSupervisionNoticeOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 督办通知单 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaSupervisionNotice(Long id) {
		TblCeaSupervisionNoticeOracle model = tblCeaSupervisionNoticeOracleService.findById(id);
		tblCeaSupervisionNoticeOracleService.delete(id);
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 督办通知单 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<FileVo<TblCeaSupervisionNoticeOracle>> getTblCeaSupervisionNotice(Long id) {
		FileVo<TblCeaSupervisionNoticeOracle> result = new FileVo<>();
		TblCeaSupervisionNoticeOracle model = tblCeaSupervisionNoticeOracleService.findById(id);
		if (Objects.nonNull(model.getTransactor())) {
			model.setTransactorName(tblStaffOracleService.getCreatorUserInfo(model.getTransactor()));
		}
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (Objects.nonNull(model.getSupervisionWorkUnit())) {
			model.setSupervisionWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(model.getSupervisionWorkUnit()));
		}
		if (StringUtils.isNotEmpty(model.getFileIds())) {
			List<TblAttachment> file = tblAttachmentService.findByIds(model.getFileIds());
			result.setFile(file);
		}
		result.setData(model);
		return MyResponseFormat.retParam(200, 200, result);
	}
	
	
	/**
	 * 督办通知办理 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaSupervisionNoticeOracle> getBlList(TblCeaSupervisionNoticeQueryParam param) {
		param.setCreator(null);
		PageInfo<TblCeaSupervisionNoticeOracle> pageInfo = tblCeaSupervisionNoticeOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> transactors = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getTransactor())).distinct()
					.map(TblCeaSupervisionNoticeOracle::getTransactor).distinct().collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaSupervisionNoticeOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(transactors)) {
				creatorList.addAll(transactors);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			List<Long> supervisionWorkUnits = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getSupervisionWorkUnit())).distinct()
					.map(TblCeaSupervisionNoticeOracle::getSupervisionWorkUnit).distinct().collect(Collectors.toList());
			Map<Long, String> workUnitIdUserInfoMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(supervisionWorkUnits, ","));
			pageInfo.getList().forEach(item -> {
				item.setSupervisionWorkUnitName(workUnitIdUserInfoMap.getOrDefault(item.getSupervisionWorkUnit(), ""));
				item.setTransactorName(creatorUserInfoMap.getOrDefault(item.getTransactor(), ""));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
			});
			PageResult<TblCeaSupervisionNoticeOracle> build = new PageResult<TblCeaSupervisionNoticeOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}
	
	/**
	 * 督办通知办理 提交
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> saveBlSubmit(Long id) {
		TblCeaSupervisionNoticeOracle param = new TblCeaSupervisionNoticeOracle();
		param.setHandstatus(2);//已办理
		param.setId(id);
		TblCeaSupervisionNoticeOracle model = tblCeaSupervisionNoticeOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, null);
	}
	
}


