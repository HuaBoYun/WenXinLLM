package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblAttachment;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAssetMgtOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblAttachmentService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaAssetMgtOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaAssetMgtService;
import com.huabo.central.enterprises.audit.service.FileUploadService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAssetMgtQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CeaAssetMgtServiceImpl implements CeaAssetMgtService {

	@Resource
	private TblCeaAssetMgtOracleService tblCeaAssetMgtOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblAttachmentService tblAttachmentService;

	/**
	 * 资产管理 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaAssetMgtOracle> getTblCeaAssetMgtList(TblCeaAssetMgtQueryParam param) {

		PageInfo<TblCeaAssetMgtOracle> pageInfo = tblCeaAssetMgtOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> custodians = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCustodian())).distinct()
					.map(TblCeaAssetMgtOracle::getCustodian).collect(Collectors.toList());
			List<Long> usePeoples = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getUsePeople())).distinct()
					.map(TblCeaAssetMgtOracle::getUsePeople).collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaAssetMgtOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(custodians)) {
				creatorList.addAll(custodians);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			if (CollectionUtil.isNotEmpty(usePeoples)) {
				creatorList.addAll(usePeoples);
			}
			//信息提供单位
			List<Long> applyWorkUnits = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getUseDepartment())).distinct()
					.map(TblCeaAssetMgtOracle::getUseDepartment).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			Map<Long, String> workUnitIdUserInfoMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(applyWorkUnits, ","));
			pageInfo.getList().forEach(item -> {
				item.setCustodianName(creatorUserInfoMap.getOrDefault(item.getCustodian(), ""));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setUsePeopleName(creatorUserInfoMap.getOrDefault(item.getUsePeople(), ""));
				item.setUseDepartmentName(workUnitIdUserInfoMap.getOrDefault(item.getUseDepartment(), ""));
				if (Objects.nonNull(item.getFactoryTime())) {
					try {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//						item.setFactoryTimeString(simpleDateFormat.format(item.getFactoryTime()));
					} catch (Exception e) {
						log.error("导出时间转换异常：", e);
					}
				}
				if (Objects.nonNull(item.getProductionTime())) {
					try {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//						item.setProductionTimeString(simpleDateFormat.format(item.getProductionTime()));
					} catch (Exception e) {
						log.error("导出时间转换异常：", e);
					}
				}
				if (Objects.nonNull(item.getAddTime())) {
					try {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//						item.setAddTimeString(simpleDateFormat.format(item.getAddTime()));
					} catch (Exception e) {
						log.error("导出时间转换异常：", e);
					}
				}
				if (Objects.nonNull(item.getDiscontinuedTime())) {
					try {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//						item.setDiscontinuedTimeString(simpleDateFormat.format(item.getDiscontinuedTime()));
					} catch (Exception e) {
						log.error("导出时间转换异常：", e);
					}
				}
			});
			PageResult<TblCeaAssetMgtOracle> build = new PageResult<TblCeaAssetMgtOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 资产管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaAssetMgtOracle> saveOrUpdateTblCeaAssetMgt(TblCeaAssetMgtOracle param) {
		TblCeaAssetMgtOracle model = tblCeaAssetMgtOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 资产管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaAssetMgt(Long id) {
		TblCeaAssetMgtOracle model = tblCeaAssetMgtOracleService.findById(id);
		tblCeaAssetMgtOracleService.delete(id);
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 资产管理 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<FileVo<TblCeaAssetMgtOracle>> getTblCeaAssetMgt(Long id) {
		FileVo<TblCeaAssetMgtOracle> result = new FileVo<>();
		TblCeaAssetMgtOracle model = tblCeaAssetMgtOracleService.findById(id);
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (Objects.nonNull(model.getCustodian())) {
			model.setCustodianName(tblStaffOracleService.getCreatorUserInfo(model.getCustodian()));
		}
		if (Objects.nonNull(model.getUsePeople())) {
			model.setUsePeopleName(tblStaffOracleService.getCreatorUserInfo(model.getUsePeople()));
		}
		if (Objects.nonNull(model.getUseDepartment())) {
			model.setUseDepartmentName(tblStaffOracleService.getWorkUnitIdUserInfo(model.getUseDepartment()));
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
	public void batchSave(List<TblCeaAssetMgtOracle> dataList) {
		dataList.forEach(item -> tblCeaAssetMgtOracleService.saveOrUpdate(item));
	}
}
