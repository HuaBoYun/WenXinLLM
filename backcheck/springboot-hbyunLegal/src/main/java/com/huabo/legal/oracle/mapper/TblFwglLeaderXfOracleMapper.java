package com.huabo.legal.oracle.mapper;

import com.huabo.legal.oracle.entity.TblFwglLeaderXfOracle;
import com.huabo.legal.vo.param.TblFwglLeaderXfQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblFwglLeaderXfOracleMapper extends Mapper<TblFwglLeaderXfOracle> {

	/**
	 * 列表查询
	 * @param param
	 * @return
	 */
	List<TblFwglLeaderXfOracle> getList(@Param("param") TblFwglLeaderXfQueryParam param, @Param("ids") List<Long> ids);
}