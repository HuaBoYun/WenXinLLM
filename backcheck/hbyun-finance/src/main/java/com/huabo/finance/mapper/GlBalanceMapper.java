package com.huabo.finance.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.huabo.finance.entity.caiji.GlBalance;
import com.huabo.finance.mappersql.GlBalanceMapperSqlConfig;
import com.huabo.finance.vo.ExportRequestVo;
import com.huabo.finance.vo.GlBalanceVo;
import com.huabo.finance.vr.GlBalanceVr;
import com.huabo.finance.vr.GlDetailVr;

/**
 * <p>
 * 凭证余额 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
public interface GlBalanceMapper extends BaseMapper<GlBalance> {

	@SelectProvider(type = GlBalanceMapperSqlConfig.class , method = "selectPageList")
	IPage<GlBalanceVr> selectPageList(Page<GlBalanceVr> page, GlBalanceVo vo, FaAccbookinfoUtil bookInfo) throws Exception;

	@SelectProvider(type = GlBalanceMapperSqlConfig.class , method = "selectFinanceDataSumTotalList")
	IPage<GlBalanceVr> selectFinanceDataSumTotalList(Page<GlBalanceVr> page, GlBalanceVo vo,FaAccbookinfoUtil bookInfo) throws Exception;

	@SelectProvider(type = GlBalanceMapperSqlConfig.class , method = "selectMinPeriod")
	String selectMinPeriod(GlBalanceVr vr) throws Exception;

	
	@SelectProvider(type = GlBalanceMapperSqlConfig.class , method = "exportSumTotalList")
	List<GlBalanceVr> exportSumTotalList(ExportRequestVo exportRequestVo) throws Exception;

	@SelectProvider(type = GlBalanceMapperSqlConfig.class , method = "selectYearAmount")
	GlBalance selectYearAmount(GlBalanceVr vr) throws Exception;

}
