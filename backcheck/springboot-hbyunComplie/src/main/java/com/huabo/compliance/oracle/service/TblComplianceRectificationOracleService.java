package com.huabo.compliance.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.compliance.oracle.entity.TblComplianceRectificationOracle;
import com.huabo.compliance.vo.param.TblComplianceRectificationQueryParam;

import java.util.List;

public interface TblComplianceRectificationOracleService {

	/**
	 * 问题整改 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblComplianceRectificationOracle> getList(TblComplianceRectificationQueryParam param);

	/**
	 * 已存在所有的检查实施IDS
	 * @return
	 */
	List<Integer> getIsRepeatAllList();

	/**
	 * 问题整改 新增/更新
	 * @param param
	 * @return
	 */
	TblComplianceRectificationOracle saveOrUpdate(TblComplianceRectificationOracle param);

	/**
	 * 问题整改 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 问题整改 详情查询
	 * @param id
	 * @return
	 */
	TblComplianceRectificationOracle findById(Integer id);
}
