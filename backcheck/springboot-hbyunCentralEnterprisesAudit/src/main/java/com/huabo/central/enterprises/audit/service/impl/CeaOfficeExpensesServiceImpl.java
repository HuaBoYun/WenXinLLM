package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblAttachment;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaOfficeExpensesOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblAttachmentService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaOfficeExpensesOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaOfficeExpensesService;
import com.huabo.central.enterprises.audit.service.FileUploadService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaOfficeExpensesQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CeaOfficeExpensesServiceImpl implements CeaOfficeExpensesService {

	@Resource
	private TblCeaOfficeExpensesOracleService tblCeaOfficeExpensesOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblAttachmentService tblAttachmentService;

	/**
	 * 办公经费申请 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaOfficeExpensesOracle> getTblCeaOfficeExpensesList(TblCeaOfficeExpensesQueryParam param) {
		PageInfo<TblCeaOfficeExpensesOracle> pageInfo = tblCeaOfficeExpensesOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> transactors = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getTransactor())).distinct()
					.map(TblCeaOfficeExpensesOracle::getTransactor).distinct().collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaOfficeExpensesOracle::getCreator).distinct().collect(Collectors.toList());
			List<Long> departmentHeads = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getDepartmentHead())).distinct()
					.map(TblCeaOfficeExpensesOracle::getDepartmentHead).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(transactors)) {
				creatorList.addAll(transactors);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			if (CollectionUtil.isNotEmpty(departmentHeads)) {
				creatorList.addAll(departmentHeads);
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			List<Long> applyBelongGroups = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApplyBelongGroup())).distinct()
					.map(TblCeaOfficeExpensesOracle::getApplyBelongGroup).distinct().collect(Collectors.toList());
			Map<Long, String> belongGroupIdUserInfoMap = tblStaffOracleService.getBelongGroupIdUserInfoMap(StringUtils.join(applyBelongGroups, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setTransactorName(creatorUserInfoMap.getOrDefault(item.getTransactor(), ""));
				item.setDepartmentHeadName(creatorUserInfoMap.getOrDefault(item.getDepartmentHead(), ""));
				item.setApplyBelongGroupName(belongGroupIdUserInfoMap.getOrDefault(item.getApplyBelongGroup(), ""));
			});
			PageResult<TblCeaOfficeExpensesOracle> build = new PageResult<TblCeaOfficeExpensesOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 办公经费申请 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaOfficeExpensesOracle> saveOrUpdateTblCeaOfficeExpenses(TblCeaOfficeExpensesOracle param) {
		TblCeaOfficeExpensesOracle model = tblCeaOfficeExpensesOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 办公经费申请 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaOfficeExpenses(Long id) {
		TblCeaOfficeExpensesOracle model = tblCeaOfficeExpensesOracleService.findById(id);
		tblCeaOfficeExpensesOracleService.delete(id);
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 办公经费申请 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaOfficeExpensesOracle> getTblCeaOfficeExpenses(Long id) {
		FileVo<TblCeaOfficeExpensesOracle> result = new FileVo<>();
		TblCeaOfficeExpensesOracle model = tblCeaOfficeExpensesOracleService.findById(id);
		if (Objects.nonNull(model.getTransactor())) {
			model.setTransactorName(tblStaffOracleService.getCreatorUserInfo(model.getTransactor()));
		}
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (Objects.nonNull(model.getDepartmentHead())) {
			model.setDepartmentHeadName(tblStaffOracleService.getCreatorUserInfo(model.getDepartmentHead()));
		}
		if (Objects.nonNull(model.getApplyBelongGroup())) {
			model.setApplyBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(model.getApplyBelongGroup()));
		}
		if (StringUtils.isNotEmpty(model.getFileIds())) {
			List<TblAttachment> file = tblAttachmentService.findByIds(model.getFileIds());
			result.setFile(file);
		}
		result.setData(model);
		return MyResponseFormat.retParam(200, 200, result);
	}
}
