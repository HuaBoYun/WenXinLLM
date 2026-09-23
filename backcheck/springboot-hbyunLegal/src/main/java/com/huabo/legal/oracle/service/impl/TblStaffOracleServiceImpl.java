package com.huabo.legal.oracle.service.impl;

import com.huabo.legal.oracle.entity.TblOrganization;
import com.huabo.legal.oracle.entity.TblStaffOracle;
import com.huabo.legal.oracle.mapper.TblStaffOracleMapper;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.HeadquartersLegalQueryParam;
import com.huabo.legal.vo.result.StaffResult;
import com.huabo.legal.vo.result.UserInfo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;
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
	 * 查询该用户是否是总部法务人员
	 * @param param
	 * @return
	 */
	@Override
	public Boolean isHeadquartersLegal(HeadquartersLegalQueryParam param) {
		Integer count = tblStaffOracleMapper.isHeadquartersLegal(param);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据集团id 批量查询名称Map
	 * @param orgid
	 * @return
	 */
	@Override
	public Map<String, String> getBelongGroupIdUserInfoMap(String orgid) {
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
	public Map<String, String> getWorkUnitIdUserInfoMap(String orgid) {
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
	public Map<String, String> getCreatorUserInfoMap(String staffId) {
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

	/**
	 * 查询当前公司及子公司
	 * @param orgid
	 * @return
	 */
	@Override
	@Deprecated
	public List<Long> getTblOrganization(Long orgid) {
		List<Long> arrayList = new ArrayList<>();
		arrayList.add(orgid);
		List<Long> fatherorgids = tblStaffOracleMapper.findTblOrganizationFatherorgidList(orgid);
		if (CollectionUtil.isEmpty(fatherorgids)) {
			return ListUtil.newArrayList(orgid);
		}
		arrayList.addAll(fatherorgids);
		//递归子公司及以下公司
		recursion(arrayList, fatherorgids);
		log.info("公司总数量：{}", arrayList.size());
		return arrayList;
	}

	private void recursion(List<Long> ids, List<Long> list) {
		list.forEach(item -> {
			List<Long> fatherorgids = tblStaffOracleMapper.findTblOrganizationFatherorgidList(item);
			if (CollectionUtil.isEmpty(fatherorgids)) {
				return;
			}
			ids.addAll(fatherorgids);
			recursion(ids, fatherorgids);
		});
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
