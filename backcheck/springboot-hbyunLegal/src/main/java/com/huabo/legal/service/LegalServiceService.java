package com.huabo.legal.service;

import com.hbfk.util.JsonBean;
import com.huabo.legal.oracle.entity.TblFwglLawServiceEvaluateOracle;
import com.huabo.legal.vo.param.TblFwglLawServiceQueryParam;
import com.huabo.legal.vo.param.TblFwglSpecialLawServiceExamineQueryParam;
import com.huabo.legal.vo.result.*;

public interface LegalServiceService {

	/**
	 * 常年/专项法律服务列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglLawServiceList(TblFwglLawServiceQueryParam param);

	/**
	 * 常年/专项法律服务 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglLawService(TblFwglLawService param);

	/**
	 * 常年/专项法律服务 刪除
	 * @param id
	 * @param lawServiceType 法律服务类型1-常年 2-专项
	 * @return
	 */
	JsonBean deleteTblFwglLawService(Long id, Integer lawServiceType);

	/**
	 * 常年/专项法律服务详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglLawService(Long id);

	/**
	 * 常年/专项法律服务-律师信息 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglLawServiceLawyer(TblFwglLawServiceLawyer param);

	/**
	 * 常年/专项法律服务-律师信息 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglLawServiceLawyer(Long id);

	/**
	 * 常年/专项法律服务-工作记录 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglLawServiceWorkRecord(TblFwglLawServiceWorkRecord param);

	/**
	 * 常年/专项法律服务-工作记录 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglLawServiceWorkRecord(Long id);

	/**
	 * 常年/专项法律服务-工作报告表/服务登记 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglLawServiceWorkReport(TblFwglLawServiceWorkReport param);

	/**
	 * 常年/专项法律服务-工作报告表/服务登记 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglLawServiceWorkReport(Long id);

	/**
	 * 常年法律服务-评分 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglPerennialLawServiceGrade(TblFwglPerennialLawServiceGrade param);

	/**
	 * 常年法律服务-评分 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglPerennialLawServiceGrade(Long id);

	/**
	 * 专项法律服务-考核 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglSpecialLawServiceExamine(TblFwglSpecialLawServiceExamine param);

	/**
	 * 专项法律服务-考核 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglSpecialLawServiceExamine(Long id);

	/**
	 * 常年/专项法律服务-律师信息详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglLawServiceLawyer(Long id);

	/**
	 * 常年/专项法律服务-工作记录详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglLawServiceWorkRecord(Long id);

	/**
	 * 常年/专项法律服务-工作报告表/服务登记详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglLawServiceWorkReport(Long id);

	/**
	 * 常年法律服务-评分详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglPerennialLawServiceGrade(Long id);

	/**
	 * 专项法律服务-考核详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglSpecialLawServiceExamine(Long id);

	/**
	 * 考核台账列表
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglSpecialLawServiceExamineList(TblFwglSpecialLawServiceExamineQueryParam param);

	/**
	 * 常年法律服务-评价表 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglLawServiceEvaluate(TblFwglLawServiceEvaluateOracle param);

	/**
	 * 常年法律服务-评价详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglLawServiceEvaluate(Long id);

	/**
	 * 专项法律服务-评价 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglLawServiceEvaluate(Long id);
}
