package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaInternalWebsiteApplyOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaInternalWebsiteApplyQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaInternalWebsiteApplyOracleMapper extends Mapper<TblCeaInternalWebsiteApplyOracle> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaInternalWebsiteApplyOracle> getList(@Param("param") TblCeaInternalWebsiteApplyQueryParam param);
}