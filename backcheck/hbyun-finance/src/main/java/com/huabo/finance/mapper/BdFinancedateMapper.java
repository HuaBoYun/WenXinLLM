package com.huabo.finance.mapper;

import com.huabo.finance.entity.BdFinancedate;

import io.lettuce.core.dynamic.annotation.Param;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 财务数据采集配置信息表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-03-11
 */
public interface BdFinancedateMapper extends BaseMapper<BdFinancedate> {

	@Select("SELECT COUNT(0) FROM BD_FINANCEPLAN_DATACONFIG WHERE DATACONFIG = #{fid} ")
	Integer selectUseCountById(@Param("fid") String fid) throws Exception;

	@Select("SELECT * FROM BD_FINANCEDATE WHERE FID IN (SELECT DATACONFIG FROM BD_FINANCEPLAN_DATACONFIG WHERE PLANID = #{planid} AND FTYPE = 1)")
	BdFinancedate selectByPlanId(@Param("planid")String planid) throws Exception;

}
