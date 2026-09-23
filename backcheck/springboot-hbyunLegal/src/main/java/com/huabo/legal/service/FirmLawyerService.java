package com.huabo.legal.service;

import com.hbfk.util.JsonBean;
import com.huabo.legal.vo.param.TblFwglPracticeActivityQueryParam;
import com.huabo.legal.vo.param.TblFwglPracticeApplyQueryParam;
import com.huabo.legal.vo.param.TblFwglPracticeExamineQueryParam;
import com.huabo.legal.vo.result.TblFwglPracticeActivity;
import com.huabo.legal.vo.result.TblFwglPracticeApply;
import com.huabo.legal.vo.result.TblFwglPracticeApplyExt;
import com.huabo.legal.vo.result.TblFwglPracticeExamine;

public interface FirmLawyerService {

	/**
	 * 执业申请列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglPracticeApplyList(TblFwglPracticeApplyQueryParam param);

	/**
	 * 执业申请 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglPracticeApply(TblFwglPracticeApply param);

	/**
	 * 执业申请 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglPracticeApply(Long id);

	/**
	 * 执业申请详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglPracticeApply(Long id);

	/**
	 * 人员台账列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getPersonnelAllList(TblFwglPracticeApplyQueryParam param);

	/**
	 * 执业活动列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglPracticeActivityList(TblFwglPracticeActivityQueryParam param);

	/**
	 * 执业活动 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglPracticeActivity(TblFwglPracticeActivity param);

	/**
	 * 执业活动 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglPracticeActivity(Long id);

	/**
	 * 执业活动详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglPracticeActivity(Long id);

	/**
	 * 执业考核列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglPracticeExamineList(TblFwglPracticeExamineQueryParam param);

	/**
	 * 执业考核 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglPracticeExamine(TblFwglPracticeExamine param);

	/**
	 * 执业考核 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglPracticeExamine(Long id);

	/**
	 * 执业考核详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglPracticeExamine(Long id);

	/**
	 * 执业申请-简历 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglPracticeApplyExt(TblFwglPracticeApplyExt param);

	/**
	 * 执业申请-简历 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglPracticeApplyExt(Long id);

	/**
	 * 执业申请-简历详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglPracticeApplyExt(Long id);

	/**
	 * 人员台账详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getPersonnel(Long id);

	/**
	 * 执业活动-人员详情 查询
	 * @param staffId
	 * @return
	 */
	JsonBean getPersonnelExt(Long staffId);
}
