package com.huabo.central.enterprises.audit.service.impl;


import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblAttachment;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaIpInventory;
import com.huabo.central.enterprises.audit.oracle.service.TblAttachmentService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaIpInventoryService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaIpInventoryService;
import com.huabo.central.enterprises.audit.service.FileUploadService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaIpInventoryQueryParam;
import com.huabo.central.enterprises.audit.vo.result.ExportTblCeaIpInventory;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CeaIpInventoryServiceImpl implements CeaIpInventoryService {

	@Resource
	private TblCeaIpInventoryService tblCeaIpInventoryService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblAttachmentService tblAttachmentService;

	/**
	 * IP清单 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaIpInventory> getTblCeaIpInventoryList(TblCeaIpInventoryQueryParam param) {
		PageInfo<TblCeaIpInventory> pageInfo = tblCeaIpInventoryService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> usePeopleIds = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getUsePeopleId())).distinct()
					.map(TblCeaIpInventory::getUsePeopleId).distinct().collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaIpInventory::getCreator).distinct().collect(Collectors.toList());
			List<Long> useBelongGroupIds = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getUseBelongGroupId())).distinct()
					.map(TblCeaIpInventory::getUseBelongGroupId).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(usePeopleIds)) {
				creatorList.addAll(usePeopleIds);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			Map<Long, String> belongGroupIdUserInfoMap = tblStaffOracleService.getBelongGroupIdUserInfoMap(StringUtils.join(useBelongGroupIds, ","));
			pageInfo.getList().forEach(item -> {
				item.setUsePeopleName(creatorUserInfoMap.getOrDefault(item.getUsePeopleId(), ""));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setUseBelongGroupName(belongGroupIdUserInfoMap.getOrDefault(item.getUseBelongGroupId(), ""));
			});
			PageResult<TblCeaIpInventory> build = new PageResult<TblCeaIpInventory>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * IP清单 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaIpInventory> saveOrUpdateTblCeaIpInventory(TblCeaIpInventory param) {
		TblCeaIpInventory model = tblCeaIpInventoryService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * IP清单 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaIpInventory(Long id) {
		TblCeaIpInventory model = tblCeaIpInventoryService.findById(id);
		tblCeaIpInventoryService.delete(id);
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * IP清单 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<FileVo<TblCeaIpInventory>> getTblCeaIpInventory(Long id) {
		FileVo<TblCeaIpInventory> result = new FileVo<>();
		TblCeaIpInventory model = tblCeaIpInventoryService.findById(id);
		if (Objects.nonNull(model.getUsePeopleId())) {
			model.setUsePeopleName(tblStaffOracleService.getCreatorUserInfo(model.getUsePeopleId()));
		}
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (Objects.nonNull(model.getUseBelongGroupId())) {
			model.setUseBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(model.getUseBelongGroupId()));
		}
		if (StringUtils.isNotEmpty(model.getFileIds())) {
			List<TblAttachment> file = tblAttachmentService.findByIds(model.getFileIds());
			result.setFile(file);
		}
		result.setData(model);
		return MyResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 批量插入
	 * @param dataList
	 */
	@Override
	@Async()
	public void batchSave(List<ExportTblCeaIpInventory> dataList) {
		dataList.forEach(item -> {
			TblCeaIpInventory tblCeaIpInventory = new TblCeaIpInventory();
			BeanUtils.copyProperties(item, tblCeaIpInventory);
			tblCeaIpInventoryService.saveOrUpdate(tblCeaIpInventory);
		});
	}
}
