package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAssetAdjustmentOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAssetAdjustmentQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaAssetAdjustmentOracleMapper extends Mapper<TblCeaAssetAdjustmentOracle> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaAssetAdjustmentOracle> getList(@Param("param") TblCeaAssetAdjustmentQueryParam param);
}