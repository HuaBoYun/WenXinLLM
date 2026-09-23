package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExpatriateApplyOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExpatriateApplyQueryParam;
import com.huabo.central.enterprises.audit.vo.param.UserAllQueryParam;
import com.huabo.central.enterprises.audit.vo.result.UserAllResult;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaExpatriateApplyOracleMapper extends Mapper<TblCeaExpatriateApplyOracle> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaExpatriateApplyOracle> getList(@Param("param") TblCeaExpatriateApplyQueryParam param);

	/**
	 * 外派人员台账 列表查询
	 * @param param
	 * @return
	 */
	List<TblCeaExpatriateApplyOracle> findTblCeaExpatriateApplyAllList(@Param("param") UserAllQueryParam param);
}