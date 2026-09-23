package com.huabo.compliance.oracle.mapper;

import com.huabo.compliance.oracle.entity.TblComplianceRectificationOracle;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblComplianceRectificationOracleMapper extends Mapper<TblComplianceRectificationOracle> {

	/**
	 * 已存在所有的检查实施IDS
	 * @return
	 */
	List<Integer> getIsRepeatAllList();

}