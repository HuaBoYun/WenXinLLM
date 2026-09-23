package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaMailMgtOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaMailMgtQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaMailMgtOracleMapper extends Mapper<TblCeaMailMgtOracle> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaMailMgtOracle> getList(@Param("param") TblCeaMailMgtQueryParam param);
}