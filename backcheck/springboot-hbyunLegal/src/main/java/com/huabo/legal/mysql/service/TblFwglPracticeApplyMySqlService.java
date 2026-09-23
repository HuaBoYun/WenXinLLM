package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglPracticeApplyMySql;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.param.TblFwglPracticeApplyQueryParam;
import com.huabo.legal.vo.result.LegalPersonnelCardEmploymentRateResult;

public interface TblFwglPracticeApplyMySqlService {

	/**
	 * 执业申请列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglPracticeApplyMySql> getList(TblFwglPracticeApplyQueryParam param);

	/**
	 * 执业申请 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglPracticeApplyMySql saveOrUpdate(TblFwglPracticeApplyMySql param);

	/**
	 * 执业申请 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 执业申请详情 查询
	 * @param id
	 * @return
	 */
	TblFwglPracticeApplyMySql findById(Integer id);

	/**
	 * 法务人员持证上岗率
	 * @param param
	 */
	LegalPersonnelCardEmploymentRateResult getLegalPersonnelCardEmploymentRate(TblFwglParam param);

	/**
	 * 公司律师人数占比
	 * @param param
	 * @return
	 */
	LegalPersonnelCardEmploymentRateResult getFirmLegalProportion(TblFwglParam param);
}
