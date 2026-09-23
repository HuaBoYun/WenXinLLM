package com.huabo.compliance.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.compliance.oracle.entity.TblComplianceInspectImpOracle;
import com.huabo.compliance.vo.param.TblComplianceInspectImpQueryParam;

import java.util.List;
import java.util.Map;

public interface TblComplianceInspectImpOracleService {

	/**
	 * 检查实施 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblComplianceInspectImpOracle> getList(TblComplianceInspectImpQueryParam param);

	/**
	 * 检查实施 新增/更新
	 * @param param
	 * @return
	 */
	TblComplianceInspectImpOracle saveOrUpdate(TblComplianceInspectImpOracle param);

	/**
	 * 检查实施 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 检查实施 详情查询
	 * @param id
	 * @return
	 */
	TblComplianceInspectImpOracle findById(Integer id);

	/**
	 * 根据检查实施IDS 查询信息 Map
	 * @param impIds
	 * @return
	 */
	Map<Integer, TblComplianceInspectImpOracle> getInspectImpMap(List<Integer> impIds);
}
