package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.contract.util.Tree;

public interface TblOrganizaService {

	List<Tree> getTree(BigDecimal nodeId) throws Exception;

	List<Tree> getNodeAll(BigDecimal nodeId) throws Exception;

	/***
	 * 返回当前公司所有的下属公司子集ID
	 * @param string
	 * @return
	 * @throws Exception
	 */
	String selectChidrenIdStrsByFatherOrgId(String string) throws Exception;

	/**
	 * 返回当前公司所有部门的ID
	 * @param orgid
	 * @return
	 * @throws Exception
	 */
	String selectDeptIdStrsByFatherOrgId(BigDecimal orgid) throws Exception;

	String findOrgByAllGSJT(String token, String staffId);

	JsonBean findOrgDeptTree(String token) throws Exception;
	
	String findOrgByAllJT(String orgid) throws Exception;

	List<Tree> getTrees(BigDecimal nodeId);

	List<Tree> getNodeAlls(BigDecimal nodeId) throws Exception ;

	JsonBean getOrgTreeListByAuditObj(String token, BigDecimal nodeId) throws Exception;

	/**
	 * 获取组织名称
	 * @param orgId
	 * @return
	 */
	String getOrgName(Long orgId);

	/**
	 * 获取组织名称
	 * @param orgIds
	 * @return
	 */
	String getOrgNames(String orgIds);
}
