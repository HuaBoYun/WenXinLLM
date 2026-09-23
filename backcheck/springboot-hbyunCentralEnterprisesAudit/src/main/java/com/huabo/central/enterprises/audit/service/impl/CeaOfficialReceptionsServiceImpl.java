package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblAttachment;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaOfficialReceptions;
import com.huabo.central.enterprises.audit.oracle.service.TblAttachmentService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaOfficialReceptionsService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaOfficialReceptionsService;
import com.huabo.central.enterprises.audit.service.FileUploadService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaOfficialReceptionsQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CeaOfficialReceptionsServiceImpl implements CeaOfficialReceptionsService {

	@Resource
	private TblCeaOfficialReceptionsService tblCeaOfficialReceptionsService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblAttachmentService tblAttachmentService;

	/**
	 * 公务接待 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaOfficialReceptions> getTblCeaOfficialReceptionsList(TblCeaOfficialReceptionsQueryParam param) {
		PageInfo<TblCeaOfficialReceptions> pageInfo = tblCeaOfficialReceptionsService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creatorList = new ArrayList<>();
			List<Long> accompanyIds = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getAccompanyId())).distinct()
					.map(TblCeaOfficialReceptions::getAccompanyId).distinct().collect(Collectors.toList());
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaOfficialReceptions::getCreator).distinct().collect(Collectors.toList());
			List<Long> applyBelongGroupCreators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApplyBelongGroupCreator())).distinct()
					.map(TblCeaOfficialReceptions::getApplyBelongGroupCreator).distinct().collect(Collectors.toList());
			List<Long> applyBelongGroupOperator = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getApplyBelongGroupOperator())).distinct()
					.map(TblCeaOfficialReceptions::getApplyBelongGroupOperator).distinct().collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(accompanyIds)) {
				creatorList.addAll(accompanyIds);
			}
			if (CollectionUtil.isNotEmpty(creators)) {
				creatorList.addAll(creators);
			}
			if (CollectionUtil.isNotEmpty(applyBelongGroupCreators)) {
				creatorList.addAll(applyBelongGroupCreators);
			}
			if (CollectionUtil.isNotEmpty(applyBelongGroupOperator)) {
				creatorList.addAll(applyBelongGroupOperator);
			}
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creatorList, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
				item.setApplyBelongGroupCreatorName(creatorUserInfoMap.getOrDefault(item.getApplyBelongGroupCreator(), ""));
				item.setApplyBelongGroupOperatorName(creatorUserInfoMap.getOrDefault(item.getApplyBelongGroupOperator(), ""));
			});
			PageResult<TblCeaOfficialReceptions> build = new PageResult<TblCeaOfficialReceptions>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 公务接待 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaOfficialReceptions> saveOrUpdateTblCeaOfficialReceptions(TblCeaOfficialReceptions param) {
		TblCeaOfficialReceptions model = tblCeaOfficialReceptionsService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 公务接待 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaOfficialReceptions(Long id) {
		TblCeaOfficialReceptions model = tblCeaOfficialReceptionsService.findById(id);
		tblCeaOfficialReceptionsService.delete(id);
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 公务接待 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<FileVo<TblCeaOfficialReceptions>> getTblCeaOfficialReceptions(Long id) {
		FileVo<TblCeaOfficialReceptions> result = new FileVo<>();
		TblCeaOfficialReceptions model = tblCeaOfficialReceptionsService.findById(id);
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (Objects.nonNull(model.getApplyBelongGroupOperator())) {
			model.setApplyBelongGroupOperatorName(tblStaffOracleService.getCreatorUserInfo(model.getApplyBelongGroupOperator()));
		}
		if (Objects.nonNull(model.getApplyBelongGroupCreator())) {
			model.setApplyBelongGroupCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getApplyBelongGroupCreator()));
		}
		if (StringUtils.isNotEmpty(model.getFileIds())) {
			List<TblAttachment> file = tblAttachmentService.findByIds(model.getFileIds());
			result.setFile(file);
		}
		result.setData(model);
		return MyResponseFormat.retParam(200, 200, result);
	}
}
