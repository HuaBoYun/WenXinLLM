package com.huabo.compliance.service;

import com.huabo.compliance.oracle.entity.*;
import com.huabo.compliance.util.JsonBean;
import com.huabo.compliance.util.PageResult;
import com.huabo.compliance.vo.param.*;
import com.huabo.compliance.vo.result.TblComplianceRiskResult;

public interface MgtService {

	/**
	 * 计划管理 列表查询
	 * @param param
	 * @return
	 */
	JsonBean getTblCompliancePlanMgtList(TblCompliancePlanMgtQueryParam param);

	/**
	 * 计划管理 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblCompliancePlanMgt(TblCompliancePlanMgtOracle param);

	/**
	 * 计划管理 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblCompliancePlanMgt(Integer id);

	/**
	 * 计划管理 详情
	 * @param id
	 * @return
	 */
	JsonBean getTblCompliancePlanMgt(Integer id);

	/**
	 * 重点岗位合规责任 列表查询
	 * @param param
	 * @return
	 */
	JsonBean getTblComplianceDtyList(TblComplianceDtyQueryParam param);

	/**
	 * 重点岗位合规责任 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblComplianceDty(TblComplianceDtyOracle param);

	/**
	 * 重点岗位合规责任 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblComplianceDty(Integer id);

	/**
	 * 重点岗位合规责任 详情
	 * @param id
	 * @return
	 */
	JsonBean getTblComplianceDty(Integer id);

	/**
	 * 合规管理员信息管理 列表查询
	 * @param param
	 * @return
	 */
	JsonBean getTblComplianceImList(TblComplianceImQueryParam param);

	/**
	 * 合规管理员信息管理 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblComplianceIm(TblComplianceImOracle param);

	/**
	 * 合规管理员信息管理 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblComplianceIm(Integer id);

	/**
	 * 合规管理员信息管理 详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblComplianceIm(Integer id);

	/**
	 * 合规手册管理 列表查询
	 * @param param
	 * @return
	 */
	JsonBean getTblComplianceManualMgtList(TblComplianceManualMgtQueryParam param);

	/**
	 * 合规手册管理 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblComplianceManualMgt(TblComplianceManualMgtOracle param);

	/**
	 * 合规手册管理 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblComplianceManualMgt(Integer id);

	/**
	 * 合规手册管理 详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblComplianceManualMgt(Integer id);

	/**
	 * 合规风险 列表查询
	 * @param param
	 * @return
	 */
	JsonBean<PageResult<TblComplianceRiskOracle>> getTblComplianceRiskList(TblComplianceRiskQueryParam param);

	/**
	 * 合规风险 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean<TblComplianceRiskOracle> saveOrUpdateTblComplianceRisk(TblComplianceRiskOracle param);

	/**
	 * 合规风险 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblComplianceRisk(Integer id);

	/**
	 * 合规风险 详情查询
	 * @param id
	 * @return
	 */
	JsonBean<TblComplianceRiskResult> getTblComplianceRisk(Integer id);
}
