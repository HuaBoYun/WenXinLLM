package com.huabo.system.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblSystemTableNoConfig;

public interface TblSystemTableNoConfigMapper extends BaseMapper<TblSystemTableNoConfig> {

	@Select("${sql}")
	BigDecimal executeSelectSql(@Param("sql")String sql) throws Exception;


}
