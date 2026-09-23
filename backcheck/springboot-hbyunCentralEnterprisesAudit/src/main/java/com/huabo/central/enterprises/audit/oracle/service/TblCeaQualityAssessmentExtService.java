package com.huabo.central.enterprises.audit.oracle.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaQualityAssessmentExt;
import com.huabo.central.enterprises.audit.vo.param.TblCeaQualityAssessmentExtQueryParam;

import java.util.List;

public interface TblCeaQualityAssessmentExtService {

	/**
	 * 项目评优-审计工质量评估 列表查询
	 * @param param
	 * @return
	 */
	List<TblCeaQualityAssessmentExt> getList(TblCeaQualityAssessmentExtQueryParam param);

	/**
	 * 项目评优-审计工质量评估 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaQualityAssessmentExt saveOrUpdate(TblCeaQualityAssessmentExt param);

	/**
	 * 项目评优-审计工质量评估 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 项目评优-审计工质量评估 详情查询
	 * @param id
	 * @return
	 */
	TblCeaQualityAssessmentExt findById(Long id);
}
