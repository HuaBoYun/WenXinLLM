package com.huabo.system.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblSystemHomeAuthorizationOracle;

public interface TblSystemHomeAuthorizationOracleMapper extends BaseMapper<TblSystemHomeAuthorizationOracle> {

	@Delete("delete from TBL_SYSTEM_HOME_AUTHORIZATION where belongGroup=#{belongGroup}")
	void deleteAuthCompany(@Param("belongGroup") BigDecimal belongGroup);
}