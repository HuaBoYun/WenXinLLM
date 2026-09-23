package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaQualityAssessment;
import com.huabo.central.enterprises.audit.vo.param.TblCeaQualityAssessmentQueryParam;

public interface TblCeaQualityAssessmentService {

	/**
	 * 项目评优-审计工质量评估 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaQualityAssessment> getList(TblCeaQualityAssessmentQueryParam param);

	/**
	 * 项目评优-审计工质量评估 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaQualityAssessment saveOrUpdate(TblCeaQualityAssessment param);

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
	TblCeaQualityAssessment findById(Long id);
}
