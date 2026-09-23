package com.management.accountant.oracle.mapper;

import com.management.accountant.oracle.entity.TblCeaConferenceMgtOracle;
import com.management.accountant.vo.param.TblCeaConferenceMgtQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaConferenceMgtOracleMapper extends Mapper<TblCeaConferenceMgtOracle> {


	List<TblCeaConferenceMgtOracle> getList(@Param("example") TblCeaConferenceMgtQueryParam example);
}