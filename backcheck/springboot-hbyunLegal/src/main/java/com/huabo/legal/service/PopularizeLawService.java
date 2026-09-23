package com.huabo.legal.service;

import com.hbfk.util.JsonBean;
import com.huabo.legal.vo.param.*;
import com.huabo.legal.vo.result.TblFwglActivityManagement;
import com.huabo.legal.vo.result.TblFwglPopularizeLawPlan;
import com.huabo.legal.vo.result.TblFwglSubjectManagement;
import com.huabo.legal.vo.result.TblFwglXfExam;

public interface PopularizeLawService {

	/**
	 * 普法计划列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglPopularizeLawPlanList(TblFwglPopularizeLawPlanQueryParam param);

	/**
	 * 普法计划 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglPopularizeLawPlan(TblFwglPopularizeLawPlan param);

	/**
	 * 普法计划 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteFwglPopularizeLawPlan(Long id);

	/**
	 * 普法计划详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglPopularizeLawPlan(Long id);

	/**
	 * 活动管理列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglActivityManagementList(TblFwglActivityManagementQueryParam param);

	/**
	 * 活动管理 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglActivityManagement(TblFwglActivityManagement param);

	/**
	 * 活动管理 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglActivityManagement(Long id);

	/**
	 * 活动管理详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglActivityManagement(Long id);

	/**
	 * 课题管理列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglSubjectManagementList(TblFwglSubjectManagementQueryParam param);

	/**
	 * 课题管理 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglSubjectManagement(TblFwglSubjectManagement param);

	/**
	 * 课题管理 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglSubjectManagement(Long id);

	/**
	 * 课题管理详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglSubjectManagement(Long id);

	/**
	 * 学法考试列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglXfExamList(TblFwglXfExamQueryParam param);

	/**
	 * 学法考试 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglXfExam(TblFwglXfExam param);

	/**
	 * 学法考试 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglXfExam(Long id);

	/**
	 * 学法考试详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglXfExam(Long id);

	/**
	 * 领导学法列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglLeaderXfList(TblFwglLeaderXfQueryParam param);

	/**
	 * 领导学法 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglLeaderXf(TblFwglLeaderXf param);

	/**
	 * 领导学法 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglLeaderXf(Long id);

	/**
	 * 领导学法详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglLeaderXf(Long id);
}
