package com.huabo.system.oracle.service.impl;

import com.huabo.system.entity.TblStaffOracle;
import com.huabo.system.mapper.TblStaffOracleMapper;
import com.huabo.system.oracle.service.TblStaffOracleService;
import com.huabo.system.vo.result.StaffResult;
import com.huabo.system.vo.result.UserInfo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
	public String getBelongGroupIdUserInfo(Long orgid) {
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
	public String getWorkUnitIdUserInfo(Long orgid) {
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
		if (StringUtils.isBlank(staffId)) {
			return Collections.emptyList();
		}
		List<UserInfo> userInfos = tblStaffOracleMapper.findCreatorUserInfos(staffId);
		if (CollectionUtil.isNotEmpty(userInfos)) {
			return userInfos;
		}
		return Collections.emptyList();
	}

	/**
	 * 根据创建人ID 查询名称
	 * @param orgid
	 * @return
	 */
	@Override
	public String getCreatorUserInfo(Long orgid) {
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
	public StaffResult getUserInfo(Long staffId) {
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
	 * 根据集团id 批量查询名称 Orgmeno
	 * @param orgid
	 * @return
	 */
	@Override
	public List<UserInfo> getBelongGroupIdUserInfoOrgmeno(String orgid) {
		List<UserInfo> userInfos = tblStaffOracleMapper.findBelongGroupIdUserInfoOrgmeno(orgid);
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
	public StaffResult getUserInfoExam(Long staffId, Long orgid, String type) {
		return tblStaffOracleMapper.findUserInfoExam(staffId, tblStaffOracleMapper.findLikeUser(orgid, type));
	}

	/**
	 * 根据集团id 批量查询名称Map
	 * @param orgid
	 * @return
	 */
	@Override
	public Map<Long, String> getBelongGroupIdUserInfoMap(String orgid) {
		if (StringUtils.isEmpty(orgid)) {
			return Collections.emptyMap();
		}
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
	public Map<Long, String> getWorkUnitIdUserInfoMap(String orgid) {
		if (StringUtils.isEmpty(orgid)) {
			return Collections.emptyMap();
		}
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
	public Map<Long, String> getCreatorUserInfoMap(String staffId) {
		if (StringUtils.isEmpty(staffId)) {
			return Collections.emptyMap();
		}
		List<UserInfo> userInfos = getCreatorUserInfos(staffId);
		if (CollectionUtil.isEmpty(userInfos)) {
			return Collections.emptyMap();
		}
		return userInfos.stream().collect(Collectors.toMap(UserInfo::getStaffId, UserInfo::getRealName));
	}

	@Override
	public TblStaffOracle getUserInfoForId(Long staffId) {
		// TODO Auto-generated method stub
		return tblStaffOracleMapper.getUserInfoForId(staffId);
	}

	@Override
	public Map<Long, TblStaffOracle> getUserInfoForIdMap(List<Long> staffIds) {
		if (CollectionUtil.isEmpty(staffIds)) {
			return Collections.emptyMap();
		}
		List<TblStaffOracle> list = tblStaffOracleMapper.getUserInfoForIdMap(staffIds);
		if (CollectionUtil.isEmpty(list)) {
			return Collections.emptyMap();
		}
		return list.stream().collect(Collectors.toMap(TblStaffOracle::getStaffId, tblStaffOracle -> tblStaffOracle));
	}

	/**
	 * 根据用户名称与部门ID 查询用户信息
	 * @param staffName
	 * @return
	 */
	@Override
	public TblStaffOracle getUserInfoForName(String staffName, Long orgid) {
		TblStaffOracle userInfoForName = tblStaffOracleMapper.findUserInfoForName(staffName, orgid);
		return userInfoForName;
	}

	/**
	 * 根据部门名称与部门ID 查询用户信息
	 * @param workUnitName
	 * @return
	 */
	@Override
	public StaffResult getUserInfoForWorkUnitName(String workUnitName, Long orgid) {
		return tblStaffOracleMapper.findUserInfoForWorkUnitName(workUnitName, orgid);
	}

}
