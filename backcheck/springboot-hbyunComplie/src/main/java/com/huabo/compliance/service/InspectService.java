package com.huabo.compliance.service;

import com.huabo.compliance.oracle.entity.TblComplianceInspectImpOracle;
import com.huabo.compliance.oracle.entity.TblComplianceInspectPlanOracle;
import com.huabo.compliance.oracle.entity.TblComplianceRectificationOracle;
import com.huabo.compliance.util.JsonBean;
import com.huabo.compliance.util.PageResult;
import com.huabo.compliance.vo.param.TblComplianceInspectImpQueryParam;
import com.huabo.compliance.vo.param.TblComplianceInspectPlanQueryParam;
import com.huabo.compliance.vo.param.TblComplianceRectificationQueryParam;
import com.huabo.compliance.vo.result.FileVo;
import com.huabo.compliance.vo.result.TblComplianceRectificationFileVo;

public interface InspectService {

	/**
	 * 检查方案 列表查询
	 * @param param
	 * @return
	 */
	JsonBean<PageResult<TblComplianceInspectPlanOracle>> getTblComplianceInspectPlanList(TblComplianceInspectPlanQueryParam param);

	/**
	 * 检查方案 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean<TblComplianceInspectPlanOracle> saveOrUpdateTblComplianceInspectPlan(TblComplianceInspectPlanOracle param);

	/**
	 * 检查方案 刪除
	 * @param id
	 * @return
	 */
	JsonBean<Void> deleteTblComplianceInspectPlan(Integer id);

	/**
	 * 检查方案 详情 查询
	 * @param id
	 * @return
	 */
	JsonBean<FileVo<TblComplianceInspectPlanOracle>> getTblComplianceInspectPlan(Integer id);

	/**
	 * 检查实施 列表查询
	 * @param param
	 * @return
	 */
	JsonBean<PageResult<TblComplianceInspectImpOracle>> getTblComplianceInspectImpList(TblComplianceInspectImpQueryParam param);

	/**
	 * 检查实施 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean<TblComplianceInspectImpOracle> saveOrUpdateTblComplianceInspectImp(TblComplianceInspectImpOracle param);

	/**
	 * 检查实施 刪除
	 * @param id
	 * @return
	 */
	JsonBean<Void> deleteTblComplianceInspectImp(Integer id);

	/**
	 * 检查实施 详情 查询
	 * @param id
	 * @return
	 */
	JsonBean<FileVo<TblComplianceInspectImpOracle>> getTblComplianceInspectImp(Integer id);

	/**
	 * 问题整改 列表查询
	 * @param param
	 * @return
	 */
	JsonBean<PageResult<TblComplianceRectificationOracle>> getTblComplianceRectificationList(TblComplianceRectificationQueryParam param);

	/**
	 * 问题整改 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean<TblComplianceRectificationOracle> saveOrUpdateTblComplianceRectification(TblComplianceRectificationOracle param);

	/**
	 * 问题整改 刪除
	 * @param id
	 * @return
	 */
	JsonBean<Void> deleteTblComplianceRectification(Integer id);

	/**
	 * 问题整改 详情 查询
	 * @param id
	 * @return
	 */
	JsonBean<TblComplianceRectificationFileVo<TblComplianceRectificationOracle>> getTblComplianceRectification(Integer id);

	/**
	 * 检查方案-自动编码
	 * @param year
	 * @return
	 */
	JsonBean<String> getTblComplianceInspectPlanAutoNum(String year);
	
	/**
	 * 首页-合规审查数量
	 * @param 
	 * @return
	 */
	JsonBean compcnt();
	
	/**
	 * 首页-顶部统计数量
	 * @param 
	 * @return
	 */
	JsonBean topcnt();
	
}
