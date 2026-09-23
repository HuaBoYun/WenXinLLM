package com.financial.sharing.oracle.mapper;

import com.financial.sharing.oracle.entity.TblCeaConferenceMgtOracle;
import com.financial.sharing.vo.param.TblCeaConferenceMgtQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaConferenceMgtOracleMapper extends Mapper<TblCeaConferenceMgtOracle> {


	List<TblCeaConferenceMgtOracle> getList(@Param("example") TblCeaConferenceMgtQueryParam example);
}