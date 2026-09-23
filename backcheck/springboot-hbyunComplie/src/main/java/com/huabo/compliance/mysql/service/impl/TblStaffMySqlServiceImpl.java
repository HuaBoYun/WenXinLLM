package com.huabo.compliance.mysql.service.impl;

import com.huabo.compliance.mysql.mapper.TblStaffMySqlMapper;
import com.huabo.compliance.mysql.service.TblStaffMySqlService;
import com.huabo.compliance.vo.result.UserInfo;
import com.huabo.compliance.vo.result.StaffResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TblStaffMySqlServiceImpl implements TblStaffMySqlService {

	@Resource
	private TblStaffMySqlMapper tblStaffMySqlMapper;

	/**
	 * 根据集团id 查询名称
	 * @param orgid
	 * @return
	 */
	@Override
	public String getBelongGroupIdUserInfo(Integer orgid) {
		StaffResult belongGroupIdUserInfo = tblStaffMySqlMapper.findBelongGroupIdUserInfo(orgid);
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
		StaffResult workUnitIdUserInfo = tblStaffMySqlMapper.findWorkUnitIdUserInfo(orgid);
		if (workUnitIdUserInfo != null) {
			return workUnitIdUserInfo.getWorkUnitName();
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
		List<UserInfo> userInfos = tblStaffMySqlMapper.findCreatorUserInfos(staffId);
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
		StaffResult creatorUserInfo = tblStaffMySqlMapper.findCreatorUserInfo(orgid);
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
		return tblStaffMySqlMapper.findUserInfo(staffId);
	}
	
}
