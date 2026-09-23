package com.huabo.central.enterprises.audit.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.hbfk.util.JsonBean;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectAppraising;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectDeclare;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectDeclareGroup;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectNotice;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectNoticeExt;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectQuality;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaQualityAssessment;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectAppraisingQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectDeclareGroupQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectDeclareQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectDeclareSortQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectNoticeExtQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectNoticeQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectQualityQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectQueryVoParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaQualityAssessmentQueryParam;
import com.huabo.central.enterprises.audit.vo.param.UpdateTblCeaProjectDeclareSortParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.huabo.central.enterprises.audit.vo.result.TblCeaProjectVoResult;

public interface CeaProjectNoticeService {

	/**
	 * 项目评优-通知 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaProjectNotice> getTblCeaProjectNoticeList(TblCeaProjectNoticeQueryParam param);

	/**
	 * 项目评优-通知 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaProjectNotice> saveOrUpdateTblCeaProjectNoticeService(TblCeaProjectNotice param);

	/**
	 * 项目评优-通知 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaProjectNoticeService(Long id);

	/**
	 * 项目评优-通知 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<FileVo<TblCeaProjectNotice>> getTblCeaProjectNoticeService(Long id);

	/**
	 * 项目评优-通知-下发 列表查询
	 * @param projectNoticeId
	 * @return
	 */
	MyJsonBean<List<TblCeaProjectNoticeExt>> getTblCeaProjectNoticeExtList(Long projectNoticeId);

	/**
	 * 项目评优-通知-下发-批量新增
	 * @param projectNoticeId
	 * @param param
	 * @return
	 */
	MyJsonBean<Void> updatesTblCeaProjectNoticeExt(Long projectNoticeId, List<TblCeaProjectNoticeExt> param);

	/**
	 * 项目评优-通知-首页展示 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaProjectNoticeExt> getTblCeaProjectNoticeExtHomeList(TblCeaProjectNoticeExtQueryParam param);

	/**
	 * 项目评优-通知-首页展示-同意
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaProjectNoticeExt> submitTblCeaProjectNoticeExtHomeList(Long id);

	/**
	 * 项目评优-申报 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaProjectDeclare> getTblCeaProjectDeclareList(TblCeaProjectDeclareQueryParam param);

	/**
	 * 项目评优-申报 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaProjectDeclare> saveOrUpdateTblCeaProjectDeclareService(TblCeaProjectDeclare param);

	/**
	 * 项目评优-申报 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaProjectDeclareService(Long id);

	/**
	 * 项目评优-申报 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<FileVo<TblCeaProjectDeclare>> getTblCeaProjectDeclareService(Long id);

	/**
	 * 项目评优-评优 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaProjectAppraising> getTblCeaProjectAppraisingList(TblCeaProjectAppraisingQueryParam param);

	/**
	 * 项目评优-评优 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaProjectAppraising> saveOrUpdateTblCeaProjectAppraisingService(TblCeaProjectAppraising param);

	/**
	 * 项目评优-评优 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaProjectAppraisingService(Long id);

	/**
	 * 项目评优-评优 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<FileVo<TblCeaProjectAppraising>> getTblCeaProjectAppraisingService(Long id);

	/**
	 * 项目评优-质量 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaProjectQuality> getTblCeaProjectQualityList(TblCeaProjectQualityQueryParam param);

	/**
	 * 项目评优-质量 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaProjectQuality> saveOrUpdateTblCeaProjectQuality(TblCeaProjectQuality param);

	/**
	 * 项目评优-质量 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaProjectQuality(Long id);

	/**
	 * 项目评优-质量 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaProjectQuality> getTblCeaProjectQuality(Long id);

	/**
	 * 项目管理-实施方案 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaProjectVoResult> getTblCeaProjectList(TblCeaProjectQueryVoParam param);

	/**
	 * 项目评优-申报-分组 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaProjectDeclareGroup> getTblCeaProjectDeclareGroupList(TblCeaProjectDeclareGroupQueryParam param);

	/**
	 * 项目评优-申报-分组 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaProjectDeclareGroup> saveOrUpdateTblCeaProjectDeclareGroupService(TblCeaProjectDeclareGroup param);

	/**
	 * 项目评优-申报-分组 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaProjectDeclareGroupService(Long id);

	/**
	 * 项目评优-申报-分组 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<FileVo<TblCeaProjectDeclareGroup>> getTblCeaProjectDeclareGroupService(Long id);

	/**
	 * 项目评优排序 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaProjectDeclare> getTblCeaProjectDeclareSortList(TblCeaProjectDeclareSortQueryParam param);

	/**
	 * 项目评优排序-更新
	 * @param param
	 * @return
	 */
	MyJsonBean<Void> updateTblCeaProjectDeclareSort(UpdateTblCeaProjectDeclareSortParam param);

	/**
	 * 项目评优-申报-分组 是否可以审批判断
	 * @param id
	 * @return
	 */
	MyJsonBean<Boolean> getTblCeaProjectDeclareGroupIsApproval(Long id);

	/**
	 * 审计工质量评估 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaQualityAssessment> getTblCeaQualityAssessmentList(TblCeaQualityAssessmentQueryParam param);

	/**
	 * 审计工质量评估 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaQualityAssessment> saveOrUpdateTblCeaQualityAssessment(TblCeaQualityAssessment param);

	/**
	 * 审计工质量评估 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaQualityAssessment(Long id);

	/**
	 * 审计工质量评估 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaQualityAssessment> getTblCeaQualityAssessmentService(Long id);
	
}
