package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblAttachment;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaSealFormOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblAttachmentService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaSealFormOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaSealFormService;
import com.huabo.central.enterprises.audit.service.FileUploadService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaSealFormQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CeaSealFormServiceImpl implements CeaSealFormService {

	@Resource
	private TblCeaSealFormOracleService tblCeaSealFormOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblAttachmentService tblAttachmentService;

	/**
	 * 印信使用单 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaSealFormOracle> getTblCeaSealFormList(TblCeaSealFormQueryParam param) {
		PageInfo<TblCeaSealFormOracle> pageInfo = tblCeaSealFormOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> workUnitList = new ArrayList<>();
			List<Long> transactors = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getTransactor())).distinct()
					.map(TblCeaSealFormOracle::getTransactor).distinct().collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaSealFormOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(transactors)) {
				creatorList.addAll(transactors);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			List<Long> auditWorkUnits = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getAuditWorkUnit())).distinct()
					.map(TblCeaSealFormOracle::getAuditWorkUnit).distinct().collect(Collectors.toList());
			List<Long> sealWorkUnits = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getSealWorkUnit())).distinct()
					.map(TblCeaSealFormOracle::getSealWorkUnit).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(auditWorkUnits)) {
				workUnitList.addAll(auditWorkUnits);
			}
			if (CollectionUtil.isNotEmpty(sealWorkUnits)) {
				workUnitList.addAll(sealWorkUnits);
			}
			Map<Long, String> workUnitIdUserInfoMap = tblStaffOracleService.getWorkUnitIdUserInfoMap(StringUtils.join(workUnitList, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setTransactorName(creatorUserInfoMap.getOrDefault(item.getTransactor(), ""));
				item.setAuditWorkUnitName(workUnitIdUserInfoMap.getOrDefault(item.getAuditWorkUnit(), ""));
				item.setSealWorkUnitName(workUnitIdUserInfoMap.getOrDefault(item.getSealWorkUnit(), ""));
			});
			PageResult<TblCeaSealFormOracle> build = new PageResult<TblCeaSealFormOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 印信使用单 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaSealFormOracle> saveOrUpdateTblCeaSealForm(TblCeaSealFormOracle param) {
		TblCeaSealFormOracle model = tblCeaSealFormOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 印信使用单 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaSealForm(Long id) {
		TblCeaSealFormOracle model = tblCeaSealFormOracleService.findById(id);
		tblCeaSealFormOracleService.delete(id);
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 印信使用单 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaSealFormOracle> getTblCeaSealForm(Long id) {
		FileVo<TblCeaSealFormOracle> result = new FileVo<>();
		TblCeaSealFormOracle model = tblCeaSealFormOracleService.findById(id);
		if (Objects.nonNull(model.getTransactor())) {
			model.setTransactorName(tblStaffOracleService.getCreatorUserInfo(model.getTransactor()));
		}
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (Objects.nonNull(model.getAuditWorkUnit())) {
			model.setAuditWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(model.getAuditWorkUnit()));
		}
		if (Objects.nonNull(model.getSealWorkUnit())) {
			model.setSealWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(model.getSealWorkUnit()));
		}
		if (StringUtils.isNotEmpty(model.getFileIds())) {
			List<TblAttachment> file = tblAttachmentService.findByIds(model.getFileIds());
			result.setFile(file);
		}
		result.setData(model);
		return MyResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 印信使用单 台账列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaSealFormOracle> getTblCeaSealFormAllList(TblCeaSealFormQueryParam param) {
		param.setState(6);
		param.setOrderBy(1);
		return getTblCeaSealFormList(param);
	}

	/**
	 * 印信使用单 台账-上移
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> updateTblCeaSealFormMoveUp(Long id) {
		tblCeaSealFormOracleService.updateTblCeaSealFormMoveUp(id);
		return MyResponseFormat.retParam(200, 200, null);

	}

	/**
	 * 印信使用单 台账-下移
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> updateTblCeaSealFormMoveDown(Long id) {
		tblCeaSealFormOracleService.updateTblCeaSealFormMoveDown(id);
		return MyResponseFormat.retParam(200, 200, null);
	}
}
