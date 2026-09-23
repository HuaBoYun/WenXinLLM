package com.regulatory.penetration.oracle.mapper;

import com.regulatory.penetration.oracle.entity.TblCeaConferenceMgtOracle;
import com.regulatory.penetration.vo.param.TblCeaConferenceMgtQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaConferenceMgtOracleMapper extends Mapper<TblCeaConferenceMgtOracle> {


	List<TblCeaConferenceMgtOracle> getList(@Param("example") TblCeaConferenceMgtQueryParam example);
}