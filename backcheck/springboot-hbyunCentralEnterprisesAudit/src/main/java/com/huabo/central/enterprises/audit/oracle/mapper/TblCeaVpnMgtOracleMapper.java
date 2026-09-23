package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaVpnMgtOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaVpnMgtQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaVpnMgtOracleMapper extends Mapper<TblCeaVpnMgtOracle> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaVpnMgtOracle> getList(@Param("param") TblCeaVpnMgtQueryParam param);
}