package com.huabo.finance.mapper;

import com.huabo.finance.entity.BdFinanceIncrementinfo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 增量采集编辑表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-03-20
 */
public interface BdFinanceIncrementinfoMapper extends BaseMapper<BdFinanceIncrementinfo> {

	@Select("SELECT LASTINDEX FROM BD_FINANCE_INCREMENTINFO WHERE PLANID = #{planId} AND INITSQLID = #{sqlId} AND CREATETIME = (SELECT MAX(CREATETIME) FROM BD_FINANCE_INCREMENTINFO WHERE PLANID = #{planId} AND INITSQLID = #{sqlId})")
	String selectLastIndex(@Param("sqlId")String sqlId,@Param("planId") String planId);

}
