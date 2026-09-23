package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblAttachment;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaLiQing;
import com.huabo.central.enterprises.audit.oracle.service.TblAttachmentService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaLiQingService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaLiQingService;
import com.huabo.central.enterprises.audit.service.FileUploadService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaLiQingQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CeaLiQingServiceImpl implements CeaLiQingService {

	@Resource
	private TblCeaLiQingService tblCeaLiQingService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblAttachmentService tblAttachmentService;

	/**
	 * 员工离庆 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaLiQing> getTblCeaLiQingList(TblCeaLiQingQueryParam param) {
		PageInfo<TblCeaLiQing> pageInfo = tblCeaLiQingService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> staffId = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getStaffId())).distinct().map(TblCeaLiQing::getStaffId)
					.distinct().collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct().map(TblCeaLiQing::getCreator)
					.distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(staffId)) {
				creatorList.addAll(staffId);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			List<Long> fillBelongGroups = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getFillBelongGroup())).distinct()
					.map(TblCeaLiQing::getFillBelongGroup).distinct().collect(Collectors.toList());
			Map<Long, String> belongGroupIdUserInfoMap = tblStaffOracleService.getBelongGroupIdUserInfoMap(StringUtils.join(fillBelongGroups, ","));
			List<Long> fillWorkUnits = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getFillWorkUnit())).distinct()
					.map(TblCeaLiQing::getFillWorkUnit).distinct().collect(Collectors.toList());
			Map<Long, String> workUnitIdUserInfoMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(fillWorkUnits, ","));
			pageInfo.getList().forEach(item -> {
				item.setFillBelongGroupName(belongGroupIdUserInfoMap.getOrDefault(item.getFillBelongGroup(), ""));
				item.setFillWorkUnitName(workUnitIdUserInfoMap.getOrDefault(item.getFillWorkUnit(), ""));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setStaffName(creatorUserInfoMap.getOrDefault(item.getStaffId(), ""));
			});
			PageResult<TblCeaLiQing> build = new PageResult<TblCeaLiQing>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 员工离庆 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaLiQing> saveOrUpdateTblCeaLiQing(TblCeaLiQing param) {
		TblCeaLiQing model = tblCeaLiQingService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 员工离庆 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaLiQing(Long id) {
		TblCeaLiQing model = tblCeaLiQingService.findById(id);
		tblCeaLiQingService.delete(id);
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 员工离庆 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<FileVo<TblCeaLiQing>> getTblCeaLiQing(Long id) {
		FileVo<TblCeaLiQing> result = new FileVo<>();
		TblCeaLiQing model = tblCeaLiQingService.findById(id);
		if (Objects.nonNull(model.getStaffId())) {
			model.setStaffName(tblStaffOracleService.getCreatorUserInfo(model.getStaffId()));
		}
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (Objects.nonNull(model.getFillBelongGroup())) {
			model.setFillBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(model.getFillBelongGroup()));
		}
		if (Objects.nonNull(model.getFillWorkUnit())) {
			model.setFillWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(model.getFillWorkUnit()));
		}
		if (StringUtils.isNotEmpty(model.getFileIds())) {
			List<TblAttachment> file = tblAttachmentService.findByIds(model.getFileIds());
			result.setFile(file);
		}
		result.setData(model);
		return MyResponseFormat.retParam(200, 200, result);
	}
}
