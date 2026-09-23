package com.huabo.compliance.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.compliance.oracle.entity.TblComplianceImOracle;
import com.huabo.compliance.vo.param.TblComplianceImQueryParam;

public interface TblComplianceImOracleService {

	/**
	 * 合规管理员信息管理 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblComplianceImOracle> getList(TblComplianceImQueryParam param);

	/**
	 * 合规管理员信息管理 新增/更新
	 * @param param
	 * @return
	 */
	TblComplianceImOracle saveOrUpdate(TblComplianceImOracle param);

	/**
	 * 合规管理员信息管理 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 合规管理员信息管理 详情查询
	 * @param id
	 * @return
	 */
	TblComplianceImOracle findById(Integer id);
}
