package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaOfficeExpensesOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaOfficeExpensesQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaOfficeExpensesOracleMapper extends Mapper<TblCeaOfficeExpensesOracle> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaOfficeExpensesOracle> getList(@Param("param") TblCeaOfficeExpensesQueryParam param);
}