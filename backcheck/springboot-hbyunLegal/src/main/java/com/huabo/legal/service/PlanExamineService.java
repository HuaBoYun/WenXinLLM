package com.huabo.legal.service;

import com.hbfk.util.JsonBean;
import com.huabo.legal.vo.param.*;
import com.huabo.legal.vo.result.TblFwglAnnualExamine;
import com.huabo.legal.vo.result.TblFwglAnnualPlan;
import com.huabo.legal.vo.result.TblFwglPlanManagement;

import java.util.List;

public interface PlanExamineService {

	/**
	 * 规划管理列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglPlanManagementList(TblFwglPlanManagementQueryParam param);

	/**
	 * 规划管理 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglPlanManagement(TblFwglPlanManagement param);

	/**
	 * 规划管理 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglPlanManagement(Long id);

	/**
	 * 规划管理详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglPlanManagement(Long id);

	/**
	 * 年度计划列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglAnnualPlanList(TblFwglAnnualPlanQueryParam param);

	/**
	 * 年度计划 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglAnnualPlan(TblFwglAnnualPlan param);

	/**
	 * 年度计划 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglAnnualPlan(Long id);

	/**
	 * 年度计划详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglAnnualPlan(Long id);

	/**
	 * 年度考核列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglAnnualExamineList(TblFwglAnnualExamineQueryParam param);

	/**
	 * 年度考核 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglAnnualExamine(TblFwglAnnualExamine param);

	/**
	 * 年度考核 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglAnnualExamine(Long id);

	/**
	 * 年度考核详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglAnnualExamine(Long id);

	/**
	 * 年度考核-考核题目列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglAnnualExamineTopicExtList(TblFwglAnnualExamineTopicExtQueryParam param);

	/**
	 * 年度考核-考核评分列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglAnnualExamineScoreExtList(TblFwglAnnualExamineScoreExtQueryParam param);

	/**
	 * 年度考核-考核评分 批量新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglAnnualExamineScoreExt(List<TblFwglAnnualExamineScoreExtParam> param);

	/**
	 * 年度考核-考核题目 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglAnnualExamineTopicExt(TblFwglAnnualExamineTopicExtBatchAdd param);

	/**
	 * 年度考核-考核评分-附件列表 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglAnnualExamineScoreExtFile(Long id);

	/**
	 * 年度考核-考核评分-附件-确定按钮
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglAnnualExamineScoreExtFile(TblFwglAnnualExamineScoreExtFileParam param);

	/**
	 * 考核台账列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getExamineAllList(ExamineAllQueryParam param);
}
