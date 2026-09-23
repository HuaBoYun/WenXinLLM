package com.huabo.compliance.oracle.service.impl;

import com.huabo.compliance.oracle.entity.TblStaffOracle;
import com.huabo.compliance.oracle.mapper.TblStaffOracleMapper;
import com.huabo.compliance.oracle.service.TblStaffOracleService;
import com.huabo.compliance.vo.result.StaffResult;
import com.huabo.compliance.vo.result.UserInfo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TblStaffOracleServiceImpl implements TblStaffOracleService {

	@Resource
	private TblStaffOracleMapper tblStaffOracleMapper;

	/**
	 * 根据集团id 查询名称
	 * @param orgid
	 * @return
	 */
	@Override
	public String getBelongGroupIdUserInfo(Integer orgid) {
		StaffResult belongGroupIdUserInfo = tblStaffOracleMapper.findBelongGroupIdUserInfo(orgid);
		if (belongGroupIdUserInfo != null) {
			return belongGroupIdUserInfo.getBelongGroupName();
		}
		return null;
	}

	/**
	 * 根据工作单位id 查询名称
	 * @param orgid
	 * @return
	 */
	@Override
	public String getWorkUnitIdUserInfo(Integer orgid) {
		StaffResult workUnitIdUserInfo = tblStaffOracleMapper.findWorkUnitIdUserInfo(orgid);
		if (workUnitIdUserInfo != null) {
			return workUnitIdUserInfo.getWorkUnitName();
		}
		return null;
	}

	/**
	 * 根据工作单位id 批量查询名称
	 * @param orgid
	 * @return
	 */
	@Override
	public List<UserInfo> getWorkUnitIdUserInfos(String orgid) {
		if (orgid == null || StringUtils.isBlank(orgid)) {
			return null;
		}
		List<UserInfo> userInfos = tblStaffOracleMapper.findWorkUnitIdUserInfos(orgid);
		if (CollectionUtil.isNotEmpty(userInfos)) {
			return userInfos;
		}
		return null;
	}

	/**
	 * 根据创建人ID 批量查询名称
	 * @param staffId
	 * @return
	 */
	@Override
	public List<UserInfo> getCreatorUserInfos(String staffId) {
		if (staffId == null || StringUtils.isBlank(staffId)) {
			return null;
		}
		List<UserInfo> userInfos = tblStaffOracleMapper.findCreatorUserInfos(staffId);
		if (CollectionUtil.isNotEmpty(userInfos)) {
			return userInfos;
		}
		return null;
	}

	/**
	 * 根据创建人ID 查询名称
	 * @param orgid
	 * @return
	 */
	@Override
	public String getCreatorUserInfo(Integer orgid) {
		if (Objects.isNull(orgid)) {
			return null;
		}
		StaffResult creatorUserInfo = tblStaffOracleMapper.findCreatorUserInfo(orgid);
		if (creatorUserInfo != null) {
			return creatorUserInfo.getRealName();
		}
		return null;
	}

	/**
	 * 根据用户id 查询名称
	 * @param staffId
	 * @return
	 */
	@Override
	public StaffResult getUserInfo(Integer staffId) {
		return tblStaffOracleMapper.findUserInfo(staffId);
	}

	/**
	 * 根据集团id 批量查询名称
	 * @param orgid
	 * @return
	 */
	@Override
	public List<UserInfo> getBelongGroupIdUserInfos(String orgid) {
		List<UserInfo> userInfos = tblStaffOracleMapper.findBelongGroupIdUserInfos(orgid);
		if (CollectionUtil.isNotEmpty(userInfos)) {
			return userInfos;
		}
		return null;
	}

	/**
	 * 根据用户类型获取信息
	 * @param staffId 当前人员信息
	 * @param orgid 集团id
	 * @param type 考试-考试人员
	 * @return
	 */
	@Override
	public StaffResult getUserInfoExam(Integer staffId, Integer orgid, String type) {
		return tblStaffOracleMapper.findUserInfoExam(staffId, tblStaffOracleMapper.findLikeUser(orgid, type));
	}

	/**
	 * 根据集团id 批量查询名称Map
	 * @param orgid
	 * @return
	 */
	@Override
	public Map<Integer, String> getBelongGroupIdUserInfoMap(String orgid) {
		List<UserInfo> userInfos = getBelongGroupIdUserInfos(orgid);
		if (CollectionUtil.isEmpty(userInfos)) {
			return Collections.emptyMap();
		}
		return userInfos.stream().collect(Collectors.toMap(UserInfo::getBelongGroupId, UserInfo::getBelongGroupName));
	}

	/**
	 * 根据工作单位id 批量查询名称Map
	 * @param orgid
	 * @return
	 */
	@Override
	public Map<Integer, String> getWorkUnitIdUserInfoMap(String orgid) {
		List<UserInfo> userInfos = getWorkUnitIdUserInfos(orgid);
		if (CollectionUtil.isEmpty(userInfos)) {
			return Collections.emptyMap();
		}
		return userInfos.stream().collect(Collectors.toMap(UserInfo::getWorkUnitId, UserInfo::getWorkUnitName));
	}

	/**
	 * 根据创建人ID 批量查询名称Map
	 * @param staffId
	 * @return
	 */
	@Override
	public Map<Integer, String> getCreatorUserInfoMap(String staffId) {
		List<UserInfo> userInfos = getCreatorUserInfos(staffId);
		if (CollectionUtil.isEmpty(userInfos)) {
			return Collections.emptyMap();
		}
		return userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
	}

	@Override
	public TblStaffOracle getUserInfoForId(Integer staffId) {
		// TODO Auto-generated method stub
		return tblStaffOracleMapper.getUserInfoForId(staffId);
	}

}
