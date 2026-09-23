package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblAttachment;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaOfficeExpensesOracle;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaRepairExpensesOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblAttachmentService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaRepairExpensesOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaRepairExpensesService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaRepairExpensesQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CeaRepairExpensesServiceImpl implements CeaRepairExpensesService {

	@Resource
	private TblCeaRepairExpensesOracleService tblCeaRepairExpensesOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private TblAttachmentService tblAttachmentService;

	/**
	 * 修理费支出 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaRepairExpensesOracle> getTblCeaRepairExpensesList(TblCeaRepairExpensesQueryParam param) {
		PageInfo<TblCeaRepairExpensesOracle> pageInfo = tblCeaRepairExpensesOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> transactors = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getTransactor())).distinct()
					.map(TblCeaRepairExpensesOracle::getTransactor).collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaRepairExpensesOracle::getCreator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(transactors)) {
				creatorList.addAll(transactors);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			List<Long> repairBelongGroups = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getRepairBelongGroup())).distinct()
					.map(TblCeaRepairExpensesOracle::getRepairBelongGroup).collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			Map<Long, String> belongGroupInfoMap = tblStaffOracleService.getBelongGroupIdUserInfoMap(StringUtils.join(repairBelongGroups, ","));
			pageInfo.getList().forEach(item -> {
				item.setTransactorName(creatorUserInfoMap.getOrDefault(item.getTransactor(), ""));
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setRepairBelongGroupName(belongGroupInfoMap.getOrDefault(item.getRepairBelongGroup(), ""));
			});
			PageResult<TblCeaRepairExpensesOracle> build = new PageResult<TblCeaRepairExpensesOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 修理费支出 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaRepairExpensesOracle> saveOrUpdateTblCeaRepairExpenses(TblCeaRepairExpensesOracle param) {
		TblCeaRepairExpensesOracle model = tblCeaRepairExpensesOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 修理费支出 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteTblCeaRepairExpenses(Long id) {
		tblCeaRepairExpensesOracleService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 修理费支出 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaRepairExpensesOracle> getTblCeaRepairExpenses(Long id) {
		FileVo<TblCeaRepairExpensesOracle> result = new FileVo<>();
		TblCeaRepairExpensesOracle model = tblCeaRepairExpensesOracleService.findById(id);
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (Objects.nonNull(model.getTransactor())) {
			model.setTransactorName(tblStaffOracleService.getCreatorUserInfo(model.getTransactor()));
		}
		if (Objects.nonNull(model.getRepairBelongGroup())) {
			model.setRepairBelongGroupName(tblStaffOracleService.getBelongGroupIdUserInfo(model.getRepairBelongGroup()));
		}
		if (StringUtils.isNotEmpty(model.getFileIds())) {
			List<TblAttachment> file = tblAttachmentService.findByIds(model.getFileIds());
			result.setFile(file);
		}
		result.setData(model);
		return MyResponseFormat.retParam(200, 200, result);
	}
}
