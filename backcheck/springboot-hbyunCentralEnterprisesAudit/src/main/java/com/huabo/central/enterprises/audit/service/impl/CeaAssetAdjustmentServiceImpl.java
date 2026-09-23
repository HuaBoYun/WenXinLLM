package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblAttachment;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAssetAdjustmentOracle;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAssetMgtOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblAttachmentService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaAssetAdjustmentOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaAssetMgtOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaAssetAdjustmentService;
import com.huabo.central.enterprises.audit.service.FileUploadService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAssetAdjustmentQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CeaAssetAdjustmentServiceImpl implements CeaAssetAdjustmentService {

	@Resource
	private TblCeaAssetAdjustmentOracleService tblCeaAssetAdjustmentOracleService;
	@Resource
	private TblCeaAssetMgtOracleService tblCeaAssetMgtOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblAttachmentService tblAttachmentService;

	/**
	 * 资产调剂申请 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaAssetAdjustmentOracle> getTblCeaAssetAdjustmentList(TblCeaAssetAdjustmentQueryParam param) {

		PageInfo<TblCeaAssetAdjustmentOracle> pageInfo = tblCeaAssetAdjustmentOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> belongGroupList = new ArrayList<>();
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaAssetAdjustmentOracle::getCreator).distinct().collect(Collectors.toList());
			List<Long> originalBelongGroups = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getOriginalBelongGroup())).distinct()
					.map(TblCeaAssetAdjustmentOracle::getOriginalBelongGroup).distinct().collect(Collectors.toList());
			List<Long> useBelongGroups = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getUseBelongGroup())).distinct()
					.map(TblCeaAssetAdjustmentOracle::getUseBelongGroup).distinct().collect(Collectors.toList());
			List<Long> originalUseBelongGroups = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getOriginalUseBelongGroup())).distinct()
					.map(TblCeaAssetAdjustmentOracle::getOriginalUseBelongGroup).distinct().collect(Collectors.toList());
			List<Long> stayAdjustedBelongGroups = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getStayAdjustedBelongGroup())).distinct()
					.map(TblCeaAssetAdjustmentOracle::getStayAdjustedBelongGroup).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(originalBelongGroups)) {
				belongGroupList.addAll(originalBelongGroups);
			}
			if (CollectionUtil.isNotEmpty(useBelongGroups)) {
				belongGroupList.addAll(useBelongGroups);
			}
			if (CollectionUtil.isNotEmpty(originalUseBelongGroups)) {
				belongGroupList.addAll(originalUseBelongGroups);
			}
			if (CollectionUtil.isNotEmpty(stayAdjustedBelongGroups)) {
				belongGroupList.addAll(stayAdjustedBelongGroups);
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
			Map<Long, String> belongGroupIdUserInfoMap = tblStaffOracleService.getBelongGroupIdUserInfoMap(StringUtils.join(belongGroupList, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setOriginalBelongGroupName(belongGroupIdUserInfoMap.getOrDefault(item.getOriginalBelongGroup(), ""));
				item.setUseBelongGroupName(belongGroupIdUserInfoMap.getOrDefault(item.getUseBelongGroup(), ""));
				item.setOriginalUseBelongGroupName(belongGroupIdUserInfoMap.getOrDefault(item.getOriginalUseBelongGroup(), ""));
				item.setStayAdjustedBelongGroupName(belongGroupIdUserInfoMap.getOrDefault(item.getStayAdjustedBelongGroup(), ""));
			});
			PageResult<TblCeaAssetAdjustmentOracle> build = new PageResult<TblCeaAssetAdjustmentOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 资产调剂申请 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaAssetAdjustmentOracle> saveOrUpdateTblCeaAssetAdjustment(TblCeaAssetAdjustmentOracle param) {
		TblCeaAssetAdjustmentOracle model = tblCeaAssetAdjustmentOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 资产调剂申请 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaAssetAdjustment(Long id) {
		TblCeaAssetAdjustmentOracle model = tblCeaAssetAdjustmentOracleService.findById(id);
		tblCeaAssetAdjustmentOracleService.delete(id);
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 资产调剂申请 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<FileVo<TblCeaAssetAdjustmentOracle>> getTblCeaAssetAdjustment(Long id) {
		FileVo<TblCeaAssetAdjustmentOracle> result = new FileVo<>();
		TblCeaAssetAdjustmentOracle model = tblCeaAssetAdjustmentOracleService.findById(id);
		if (Objects.nonNull(model.getUseBelongGroup())) {
			model.setUseBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(model.getUseBelongGroup()));
		}
		if (Objects.nonNull(model.getOriginalBelongGroup())) {
			model.setOriginalBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(model.getOriginalBelongGroup()));
		}
		if (Objects.nonNull(model.getOriginalUseBelongGroup())) {
			model.setOriginalUseBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(model.getOriginalUseBelongGroup()));
		}
		if (Objects.nonNull(model.getStayAdjustedBelongGroup())) {
			model.setStayAdjustedBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(model.getStayAdjustedBelongGroup()));
		}
		if (Objects.nonNull(model.getUseStaff())) {
			model.setUseStaffName(tblStaffOracleService.getCreatorUserInfo(model.getUseStaff()));
		}
		if (Objects.nonNull(model.getAssetMgtId())) {
			TblCeaAssetMgtOracle assetMgt = tblCeaAssetMgtOracleService.findById(model.getAssetMgtId());
			if (Objects.nonNull(assetMgt)) {
				model.setAssetName(assetMgt.getAssetName());
				model.setSpecificationType(assetMgt.getSpecificationType());
				model.setMeasurement(assetMgt.getMeasurement());
				model.setDiscontinuedTime(assetMgt.getDiscontinuedTime());
			}
		}
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
}
