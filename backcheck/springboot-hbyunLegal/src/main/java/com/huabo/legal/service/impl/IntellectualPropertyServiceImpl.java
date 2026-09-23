package com.huabo.legal.service.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.oracle.entity.TblFwglFileOracle;
import com.huabo.legal.oracle.entity.TblFwglRegisterManagementOracle;
import com.huabo.legal.oracle.service.TblFwglFileOracleService;
import com.huabo.legal.oracle.service.TblFwglRegisterManagementOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.service.FileUploadService;
import com.huabo.legal.service.IntellectualPropertyService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.vo.param.TblFwglRegisterManagementQueryParam;
import com.huabo.legal.vo.result.TblFwglRegisterManagement;
import com.huabo.legal.vo.result.UserInfo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IntellectualPropertyServiceImpl implements IntellectualPropertyService {

	@Resource
	TblFwglRegisterManagementOracleService tblFwglRegisterManagementOracleService;
	@Resource
	private TblFwglFileOracleService tblFwglFileOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 登记管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglRegisterManagementList(TblFwglRegisterManagementQueryParam param) {
		//权限控制 集团条件查询
		param.setWorkUnit(null);
		param.setCreator(null);
		PageInfo<TblFwglRegisterManagementOracle> pageInfo = tblFwglRegisterManagementOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			PageResult<TblFwglRegisterManagementOracle> build = new PageResult<TblFwglRegisterManagementOracle>().build(pageInfo);
			//查询创建者
			String creatorIds = pageInfo.getList().stream().map(TblFwglRegisterManagementOracle::getCreator).distinct()
					.collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(creatorIds)) {
				List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(creatorIds);
				if (CollectionUtil.isNotEmpty(userInfos)) {
					Map<String, String> map = userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
					pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCreator()))
							.forEach(x -> x.setCreatorName(map.getOrDefault(x.getCreator(), "")));
				}
			}
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 登记管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblFwglRegisterManagement(TblFwglRegisterManagement param) {
		TblFwglRegisterManagementOracle fwglRegisterManagement = new TblFwglRegisterManagementOracle();
		BeanUtils.copyProperties(param, fwglRegisterManagement);
		TblFwglRegisterManagementOracle registerManagement = tblFwglRegisterManagementOracleService.saveOrUpdate(fwglRegisterManagement);
		return ResponseFormat.retParam(200, 200, registerManagement);
	}

	/**
	 * 登记管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public JsonBean deleteTblTblFwglRegisterManagement(Long id) {
		TblFwglRegisterManagementOracle registerManagement = tblFwglRegisterManagementOracleService.findById(id);
		//登记管理 刪除
		tblFwglRegisterManagementOracleService.delete(id);
		//登记管理-文件 删除
		if (StringUtils.isNotBlank(registerManagement.getFileIds())) {
			List<String> fileIds = Arrays.asList(registerManagement.getFileIds().split(","));
			fileIds.forEach(x -> fileUploadService.fileRemove(Long.valueOf(x)));
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 登记管理详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblFwglRegisterManagement(Long id) {
		HashMap<String, Object> map = new HashMap<>();
		//登记管理详情
		TblFwglRegisterManagementOracle registerManagement = tblFwglRegisterManagementOracleService.findById(id);
		//文件列表
		if (StringUtils.isNotEmpty(registerManagement.getFileIds())) {
			List<TblFwglFileOracle> files = tblFwglFileOracleService.findByIds(registerManagement.getFileIds());
			map.put("files", files);
		}
		map.put("registerManagement", registerManagement);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 登记管理-台账管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblFwglRegisterManagementAllList(TblFwglRegisterManagementQueryParam param) {
		//登记管理-台账管理 权限管理
		param.setCreator(null);
		param.setBelongGroup(null);
		param.setWorkUnit(null);
		PageInfo<TblFwglRegisterManagementOracle> pageInfo = tblFwglRegisterManagementOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			//查询创建者
			String creatorIds = pageInfo.getList().stream().map(TblFwglRegisterManagementOracle::getCreator).distinct()
					.collect(Collectors.joining(","));
			if (StringUtils.isNotBlank(creatorIds)) {
				List<UserInfo> userInfos = tblStaffOracleService.getCreatorUserInfos(creatorIds);
				if (CollectionUtil.isNotEmpty(userInfos)) {
					Map<String, String> map = userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
					pageInfo.getList().stream().filter(x -> StringUtils.isNotBlank(x.getCreator()))
							.forEach(x -> x.setCreatorName(map.getOrDefault(x.getCreator(), "")));
				}
			}
			PageResult<TblFwglRegisterManagementOracle> result = new PageResult<TblFwglRegisterManagementOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, result);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}
}
