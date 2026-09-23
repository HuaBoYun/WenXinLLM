package com.huabo.compliance.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.compliance.oracle.entity.TblComplianceDtyOracle;
import com.huabo.compliance.vo.param.TblComplianceDtyQueryParam;

public interface TblComplianceDtyOracleService {

	/**
	 * 重点岗位合规责任 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblComplianceDtyOracle> getList(TblComplianceDtyQueryParam param);

	/**
	 * 重点岗位合规责任 新增/更新
	 * @param param
	 * @return
	 */
	TblComplianceDtyOracle saveOrUpdate(TblComplianceDtyOracle param);

	/**
	 * 重点岗位合规责任 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 重点岗位合规责任 详情查询
	 * @param id
	 * @return
	 */
	TblComplianceDtyOracle findById(Integer id);
}
