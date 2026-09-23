package com.huabo.finance.mapper;

import com.huabo.finance.entity.caiji.GlDetail;
import com.huabo.finance.mappersql.GlDetailMapperSqlConfig;
import com.huabo.finance.vo.ExportRequestVo;
import com.huabo.finance.vo.GlDetailVo;
import com.huabo.finance.vr.GlDetailVr;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.FaAccbookinfoUtil;

/**
 * <p>
 * 凭证明细 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-03-26
 */
public interface GlDetailMapper extends BaseMapper<GlDetail> {

	@SelectProvider(type = GlDetailMapperSqlConfig.class, method = "selectFinanceDataPage")
	IPage<GlDetailVr> selectFinanceDataPage(Page<GlDetailVr> page, GlDetailVo vo, FaAccbookinfoUtil bookInfo) throws Exception;

	@SelectProvider(type = GlDetailMapperSqlConfig.class, method = "selectFinanceDetailBookPage")
	@Results({
		@Result(column = "CODE" , property = "glBalanceVr.code"),
		@Result(column = "NAME" , property = "glBalanceVr.name"),
		@Result(column = "BALANORIENT" , property = "glBalanceVr.balanorient"),
		@Result(column = "GBLOCALDEBITAMOUNT" , property = "glBalanceVr.localdebitamount"),
		@Result(column = "GBLOCALCREDITAMOUNT" , property = "glBalanceVr.localcreditamount"),
		@Result(column = "GBFBEGINBALANCELOCAL" , property = "glBalanceVr.fbeginBalanceLocal"),
		@Result(column = "GBFENDBALANCELOCAL" , property = "glBalanceVr.fendBalanceLocal"),
		@Result(column = "GBFYEARCREDITLOCAL" , property = "glBalanceVr.fyearDeditLocal"),
		@Result(column = "GBFYEARDEDITLOCAL" , property = "glBalanceVr.fyearCreditLocal"),
	})
	IPage<GlDetailVr> selectFinanceDetailBookPage(Page<GlDetailVr> page, GlDetailVo vo, FaAccbookinfoUtil bookInfo) throws Exception;

	@SelectProvider(type = GlDetailMapperSqlConfig.class, method = "selectPreDetailSum")
	GlDetail selectPreDetailSum(GlDetailVr vr) throws Exception;

	@SelectProvider(type = GlDetailMapperSqlConfig.class, method = "selectFirstDayYear")
	List<String> selectFirstDayYear(GlDetailVo vo, FaAccbookinfoUtil bookInfo, String prePkAccasoa, String year, String period) throws Exception;

	@SelectProvider(type = GlDetailMapperSqlConfig.class, method = "selectNextDetailDate")
	Date selectNextDetailDate(GlDetailVr vr, FaAccbookinfoUtil bookInfo) throws Exception;

	@SelectProvider(type = GlDetailMapperSqlConfig.class, method = "selectCurrentDaySumDetail")
	GlDetail selectCurrentDaySumDetail(GlDetailVr vr) throws Exception;

	@SelectProvider(type = GlDetailMapperSqlConfig.class, method = "exportDetailBookPage")
	@Results({
		@Result(column = "CODE" , property = "glBalanceVr.code"),
		@Result(column = "NAME" , property = "glBalanceVr.name"),
		@Result(column = "BALANORIENT" , property = "glBalanceVr.balanorient"),
		@Result(column = "GBLOCALDEBITAMOUNT" , property = "glBalanceVr.localdebitamount"),
		@Result(column = "GBLOCALCREDITAMOUNT" , property = "glBalanceVr.localcreditamount"),
		@Result(column = "GBFBEGINBALANCELOCAL" , property = "glBalanceVr.fbeginBalanceLocal"),
		@Result(column = "GBFENDBALANCELOCAL" , property = "glBalanceVr.fendBalanceLocal"),
		@Result(column = "GBFYEARCREDITLOCAL" , property = "glBalanceVr.fyearDeditLocal"),
		@Result(column = "GBFYEARDEDITLOCAL" , property = "glBalanceVr.fyearCreditLocal"),
	})
	List<GlDetailVr> exportDetailBookPage(ExportRequestVo exportRequestVo) throws Exception;

	@SelectProvider(type = GlDetailMapperSqlConfig.class, method = "exportMxflzList")
	@Results({
		@Result(column = "CODE" , property = "glBalanceVr.code"),
		@Result(column = "NAME" , property = "glBalanceVr.name"),
		@Result(column = "BALANORIENT" , property = "glBalanceVr.balanorient"),
		@Result(column = "GBLOCALDEBITAMOUNT" , property = "glBalanceVr.localdebitamount"),
		@Result(column = "GBLOCALCREDITAMOUNT" , property = "glBalanceVr.localcreditamount"),
		@Result(column = "GBFBEGINBALANCELOCAL" , property = "glBalanceVr.fbeginBalanceLocal"),
		@Result(column = "GBFENDBALANCELOCAL" , property = "glBalanceVr.fendBalanceLocal"),
		@Result(column = "GBFYEARCREDITLOCAL" , property = "glBalanceVr.fyearDeditLocal"),
		@Result(column = "GBFYEARDEDITLOCAL" , property = "glBalanceVr.fyearCreditLocal"),
	})
	List<GlDetailVr> exportMxflzList(ExportRequestVo exportRequestVo) throws Exception;

}
