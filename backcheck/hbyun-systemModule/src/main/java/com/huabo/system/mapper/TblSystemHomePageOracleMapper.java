package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.system.entity.TblSystemLoginPageOracle;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblSystemHomePageOracle;

public interface TblSystemHomePageOracleMapper extends BaseMapper<TblSystemHomePageOracle> {

	TblSystemHomePageOracle findAuthCompany(@Param("belongGroup") BigDecimal belongGroup);

	List<TblSystemHomePageOracle> selectAll(@Param("param") TblSystemHomePageOracle param);
}