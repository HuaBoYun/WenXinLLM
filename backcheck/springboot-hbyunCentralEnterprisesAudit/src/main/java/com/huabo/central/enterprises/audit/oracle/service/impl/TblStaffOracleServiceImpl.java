package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblOrganization;
import com.huabo.central.enterprises.audit.oracle.entity.TblStaffOracle;
import com.huabo.central.enterprises.audit.oracle.mapper.TblStaffOracleMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.vo.param.UpdateUserOnDutyStatusParam;
import com.huabo.central.enterprises.audit.vo.param.UserAllQueryParam;
import com.huabo.central.enterprises.audit.vo.result.StaffResult;
import com.huabo.central.enterprises.audit.vo.result.UserAllResult;
import com.huabo.central.enterprises.audit.vo.result.UserInfo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
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
	 * 部门id或者单位id 查询名称
	 * @param orgid
	 * @return
	 */
	@Override
	public String getIdUserInfo(Long orgid) {
		StaffResult userInfo = tblStaffOracleMapper.findIdUserInfo(orgid);
		if (userInfo != null) {
			return userInfo.getName();
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
	public StaffResult getUserInfoExam(Integer staffId, Integer orgid, String type) {
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

	/**
	 * 根据部门或者单位id 批量查询名称Map
	 * @param orgid
	 * @return
	 */
	@Override
	public Map<Long, String> getIdUserInfoMap(String orgid) {
		if (StringUtils.isEmpty(orgid)) {
			return Collections.emptyMap();
		}
		List<UserInfo> userInfos = tblStaffOracleMapper.findIdUserInfos(orgid);
		if (CollectionUtil.isEmpty(userInfos)) {
			return Collections.emptyMap();
		}
		return userInfos.stream().collect(Collectors.toMap(UserInfo::getOrgid, UserInfo::getName));
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
		return tblStaffOracleMapper.findUserInfoForName(staffName, orgid);
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

	/**
	 * 根据单位名称与部门ID 查询用户信息
	 * @param belongGroupName
	 * @return
	 */
	@Override
	public StaffResult getUserInfoForBelongGroupName(String belongGroupName, Long orgid) {
		return tblStaffOracleMapper.findUserInfoForBelongGroupName(belongGroupName, orgid);
	}

	/**
	 * 查询当前公司及子公司 递归优化后
	 * @param orgid
	 * @return
	 */
	@Override
	public List<Long> getTblOrganizationAll(Long orgid) {
		List<TblOrganization> allList = tblStaffOracleMapper.findAllList();
		if (CollectionUtil.isEmpty(allList)) {
			return Collections.emptyList();
		}
		List<Long> addList = new ArrayList<>();
		addList.add(orgid);
		List<Long> collect = allList.stream().filter(item -> Objects.equals(item.getFatherorgid(), orgid)).map(TblOrganization::getOrgid)
				.collect(Collectors.toList());
		if (CollectionUtil.isEmpty(collect)) {
			return addList;
		}
		addList.addAll(collect);
		doAllList(allList, addList, collect);
		log.info("公司总数量：{}", addList.size());
		return addList;
	}

	/**
	 * 更新用户在岗状态
	 * @param param
	 * @return
	 */
	@Override
	public void updateUserOnDutyStatus(UpdateUserOnDutyStatusParam param) {
		TblStaffOracle update = new TblStaffOracle();
		update.setStaffId(param.getUserId());
		update.setOnDutyStatus(param.getOnDutyStatus());
		tblStaffOracleMapper.updateByPrimaryKeySelective(update);
	}

	/**
	 * 人员台账列表
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<UserAllResult> getUserAllList(UserAllQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblStaffOracleMapper.findUserAllList(param));
	}

	/**
	 * 根据用户ID、角色ID 查询该用户是否是这个角色
	 * @param staffId
	 * @return
	 */
	@Override
	public Boolean getUserRole(Long staffId, Long roleId) {
		Integer count = tblStaffOracleMapper.findFlagUserRole(staffId, roleId);
		return count != 0;
	}

	/**
	 * 项目组成员表、项目负责人项目表  查询该用户是否在项目中
	 * @param userId
	 * @return
	 */
	@Override
	public Long getUserJobProjectCount(Long userId) {
		String st1 = null;
		try {
			st1 = DataBaseSqlConfig.getWhereColumnInStr("TEAM_MEMBERS_IDS", userId.toString(), ",");
		} catch (Exception e) {
			log.error("用户判断异常", e);
			throw new ServiceException("用户判断异常");
		}
		List<Integer> list = tblStaffOracleMapper.findUserJobProjectCount(userId, st1);
		if (CollectionUtil.isEmpty(list)) {
			return 0L;
		}
		return list.stream().map(Long::valueOf).collect(Collectors.summarizingLong(item -> item)).getSum();
	}

	/**
	 * 查询子公司下的子公司 递归
	 * @param allList
	 * @param addList
	 * @param collect1
	 */
	private void doAllList(List<TblOrganization> allList, List<Long> addList, List<Long> collect1) {
		collect1.forEach(it1 -> {
			List<Long> collect2 = allList.stream().filter(it2 -> Objects.equals(it2.getFatherorgid(), it1)).map(TblOrganization::getOrgid)
					.collect(Collectors.toList());
			if (CollectionUtil.isEmpty(collect2)) {
				return;
			}
			addList.addAll(collect2);
			doAllList(allList, addList, collect2);
		});
	}


}
