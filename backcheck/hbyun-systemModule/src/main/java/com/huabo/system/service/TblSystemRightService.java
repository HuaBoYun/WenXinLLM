package com.huabo.system.service;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblManageRight;
import com.huabo.system.entity.TblManageUserRight;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblSystemRight;
import com.huabo.system.utils.Tree;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TblSystemRightService {

	JsonBean saveManageRight(TblSystemRight right, String token) throws Exception;

	JsonBean findRightEntityById(BigDecimal id) throws Exception;

	JsonBean modifyManageRight(TblSystemRight right, String token) throws Exception;

	JsonBean removeManageRight(BigDecimal rightId) throws Exception;

	JsonBean findRightLIstByUser(String token, BigDecimal rightId, String moduleType) throws Exception;

	JsonBean findChildrenRightListByUser(String token, BigDecimal rightId, String moduleType) throws Exception;

	JsonBean findAllRightListByCompany(String token, BigDecimal rightId, BigDecimal orgId, String moduleType) throws Exception;

	JsonBean grantUserRight(String rightIds, String token, BigDecimal staffId) throws Exception;

	JsonBean grantCompanyRight(String rightIds, String token, String orgId,String moduleType) throws Exception;

	JsonBean getCompanySettingRightList(String token, BigDecimal rightId, String moduleType) throws Exception;

	JsonBean getCompanySettingRightListInfo(String token, BigDecimal rightId, String moduleType) throws Exception;

	JsonBean findSystemRightSettingById(BigDecimal id) throws Exception;

	JsonBean modifySystemSettingRight(TblSystemRight right, String token) throws Exception;

	JsonBean findAllRightListcf(String token, TblSystemRight right,Integer judge) throws Exception;

	JsonBean findRightListByRole(String token, BigDecimal rightId, String moduleType) throws Exception;

	JsonBean grantRoleRight(String rightIds, BigDecimal roleId,String moduleType) throws Exception;

	JsonBean findRoleRigetListByType(String token, Integer type, String switchType,String moduleType) throws Exception;

	JsonBean modifySystemRightVisible(String token, BigDecimal rightId, Integer visible) throws Exception;

	JsonBean findAllRightListByCompanyToGrant(String token, BigDecimal rightId, BigDecimal roleId, String moduleType) throws Exception;

	/**
	 * 批量获取树名称
	 * @param catalogueIds
	 * @return
	 */
	Map<BigDecimal, TblSystemRight> getRightNamesMap(List<BigDecimal> catalogueIds);

	JsonBean grantRoleDataRight(BigDecimal roleId,String companyIds) throws Exception;

	JsonBean getRoleDataDeptInfo(BigDecimal roleId, String token) throws Exception;

	JsonBean getGrantRoleDataDeptInfo(BigDecimal roleId, String token, Integer pageNumber, Integer pageSize,
			String deptName, String companyName) throws Exception;

	JsonBean removeDataRight(BigDecimal roleId, String dataJson) throws Exception;

	JsonBean getGrantDataRightDeptList(Integer pageNumber, String token, Integer pageSize, BigDecimal pid,
			String deptNumber, String deptName, BigDecimal roleId) throws Exception;

	JsonBean getGrantSystemRightStaffList(Integer pageNumber, String token, Integer pageSize, String userName,
			String realName, BigDecimal roleId, String companyName, String deptName) throws Exception;

	JsonBean getSystemRightStaffList(Integer pageNumber, String token, Integer pageSize, String userName,
			String realName, BigDecimal roleId, String companyName, String deptName, Integer isAll, BigDecimal orgId) throws Exception;

	JsonBean zdyInsertRightList();

	List<String> findNameByRithIds(String rightIds) throws Exception;
}
