package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaIpManageOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaIpManageQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaIpManageOracleMapper extends Mapper<TblCeaIpManageOracle> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaIpManageOracle> getList(@Param("param") TblCeaIpManageQueryParam param);
}