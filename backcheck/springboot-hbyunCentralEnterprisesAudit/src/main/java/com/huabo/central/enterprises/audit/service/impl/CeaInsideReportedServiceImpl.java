package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblAttachment;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaInsideReportedOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblAttachmentService;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaInsideReportedOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaInsideReportedService;
import com.huabo.central.enterprises.audit.service.FileUploadService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaInsideReportedQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CeaInsideReportedServiceImpl implements CeaInsideReportedService {

	@Resource
	private TblCeaInsideReportedOracleService tblCeaInsideReportedOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblAttachmentService tblAttachmentService;

	/**
	 * 内部文件呈报 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaInsideReportedOracle> getTblCeaInsideReportedList(TblCeaInsideReportedQueryParam param) {
		PageInfo<TblCeaInsideReportedOracle> pageInfo = tblCeaInsideReportedOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaInsideReportedOracle::getCreator).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
			});
			PageResult<TblCeaInsideReportedOracle> build = new PageResult<TblCeaInsideReportedOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 内部文件呈报 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaInsideReportedOracle> saveOrUpdateTblCeaInsideReported(TblCeaInsideReportedOracle param) {
		TblCeaInsideReportedOracle model = tblCeaInsideReportedOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 内部文件呈报 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> deleteTblCeaInsideReported(Long id) {
		TblCeaInsideReportedOracle model = tblCeaInsideReportedOracleService.findById(id);
		tblCeaInsideReportedOracleService.delete(id);
		if (StringUtils.isNotBlank(model.getFileIds())) {
			List<String> fileIds = Arrays.asList(model.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 内部文件呈报 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<FileVo<TblCeaInsideReportedOracle>> getTblCeaInsideReported(Long id) {
		FileVo<TblCeaInsideReportedOracle> result = new FileVo<>();
		TblCeaInsideReportedOracle model = tblCeaInsideReportedOracleService.findById(id);
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
