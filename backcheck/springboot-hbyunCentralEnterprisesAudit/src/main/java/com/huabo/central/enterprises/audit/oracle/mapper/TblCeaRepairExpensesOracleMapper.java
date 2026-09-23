package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaRepairExpensesOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaRepairExpensesQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaRepairExpensesOracleMapper extends Mapper<TblCeaRepairExpensesOracle> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaRepairExpensesOracle> getList(@Param("param") TblCeaRepairExpensesQueryParam param);
}