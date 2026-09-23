package com.huabo.finance.mapper;

import com.huabo.finance.entity.caiji.GlAssBalane;
import com.huabo.finance.mappersql.GlAssBalaneMapperSqlConfig;
import com.huabo.finance.vo.ExportRequestVo;
import com.huabo.finance.vo.GlAssBalaneVo;
import com.huabo.finance.vr.GlAssBalaneVr;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.FaAccbookinfoUtil;

/**
 * <p>
 * 辅助账余额 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-04-20
 */
public interface GlAssBalaneMapper extends BaseMapper<GlAssBalane> {

	@SelectProvider(type = GlAssBalaneMapperSqlConfig.class , method = "selectFinanceAccAssBalanceList")
	@Results({
		@Result(property = "pkAssbalance" , column = "PK_ASSBALANCE"),
		@Result(property = "pkOrg" , column = "PK_ORG"),
		@Result(property = "pkAccasoa" , column = "PK_ACCASOA"),
		@Result(property = "year" , column = "YEAR"),
		@Result(property = "period" , column = "PERIOD"),
		@Result(property = "pkAccass" , column = "PK_ACCASS"),
		@Result(property = "debitamount" , column = "DEBITAMOUNT"),
		@Result(property = "creditmount" , column = "CREDITMOUNT"),
		@Result(property = "beginbalancemount" , column = "BEGINBALANCEMOUNT"),
		@Result(property = "endbalancemount" , column = "ENDBALANCEMOUNT"),
		@Result(property = "yeardebitamount" , column = "YEARDEBITAMOUNT"),
		@Result(property = "yearcreditmount" , column = "YEARCREDITMOUNT"),
		@Result(property = "asstype" , column = "ASSTYPE"),
		@Result(property = "assname" , column = "ASSNAME"),
		@Result(property = "assdes" , column = "ASSDES"),
		@Result(property = "balanorient" , column = "BALANORIENT"),
		@Result(property = "assdd" , column = "ASSDD"),
	})
	IPage<GlAssBalaneVr> selectFinanceAccAssBalanceList(Page<GlAssBalaneVr> page, GlAssBalaneVo vo,
			FaAccbookinfoUtil bookInfo) throws Exception;
	
	@SelectProvider(type = GlAssBalaneMapperSqlConfig.class , method = "selectFinanceAccAssGeneralLedgerList")
	@Results({
		@Result(property = "pkAssbalance" , column = "PK_ASSBALANCE"),
		@Result(property = "pkOrg" , column = "PK_ORG"),
		@Result(property = "pkAccasoa" , column = "PK_ACCASOA"),
		@Result(property = "year" , column = "YEAR"),
		@Result(property = "period" , column = "PERIOD"),
		@Result(property = "pkAccass" , column = "PK_ACCASS"),
		@Result(property = "debitamount" , column = "DEBITAMOUNT"),
		@Result(property = "creditmount" , column = "CREDITMOUNT"),
		@Result(property = "beginbalancemount" , column = "BEGINBALANCEMOUNT"),
		@Result(property = "endbalancemount" , column = "ENDBALANCEMOUNT"),
		@Result(property = "yeardebitamount" , column = "YEARDEBITAMOUNT"),
		@Result(property = "yearcreditmount" , column = "YEARCREDITMOUNT"),
		@Result(property = "asstype" , column = "ASSTYPE"),
		@Result(property = "assname" , column = "ASSNAME"),
		@Result(property = "assdes" , column = "ASSDES"),
		@Result(property = "assdd" , column = "ASSDD"),
	})
	IPage<GlAssBalaneVr> selectFinanceAccAssGeneralLedgerList(Page<GlAssBalaneVr> page, GlAssBalaneVo vo,FaAccbookinfoUtil bookInfo) throws Exception;

	@SelectProvider(type = GlAssBalaneMapperSqlConfig.class , method = "selectMinPeriod")
	String selectMinPeriod(GlAssBalaneVr vr) throws Exception;

	
	@SelectProvider(type = GlAssBalaneMapperSqlConfig.class , method = "exportAccAssBalanceList")
	@Results({
		@Result(property = "pkAssbalance" , column = "PK_ASSBALANCE"),
		@Result(property = "pkOrg" , column = "PK_ORG"),
		@Result(property = "pkAccasoa" , column = "PK_ACCASOA"),
		@Result(property = "year" , column = "YEAR"),
		@Result(property = "period" , column = "PERIOD"),
		@Result(property = "pkAccass" , column = "PK_ACCASS"),
		@Result(property = "debitamount" , column = "DEBITAMOUNT"),
		@Result(property = "creditmount" , column = "CREDITMOUNT"),
		@Result(property = "beginbalancemount" , column = "BEGINBALANCEMOUNT"),
		@Result(property = "endbalancemount" , column = "ENDBALANCEMOUNT"),
		@Result(property = "yeardebitamount" , column = "YEARDEBITAMOUNT"),
		@Result(property = "yearcreditmount" , column = "YEARCREDITMOUNT"),
		@Result(property = "asstype" , column = "ASSTYPE"),
		@Result(property = "assname" , column = "ASSNAME"),
		@Result(property = "assdes" , column = "ASSDES"),
		@Result(property = "assdd" , column = "ASSDD"),
	})
	List<GlAssBalaneVr> exportAccAssBalanceList(ExportRequestVo exportRequestVo) throws Exception;

	@SelectProvider(type = GlAssBalaneMapperSqlConfig.class , method = "selectFinanceAccAssGeneralLedgerList")
	@Results({
		@Result(property = "pkAssbalance" , column = "PK_ASSBALANCE"),
		@Result(property = "pkOrg" , column = "PK_ORG"),
		@Result(property = "pkAccasoa" , column = "PK_ACCASOA"),
		@Result(property = "year" , column = "YEAR"),
		@Result(property = "period" , column = "PERIOD"),
		@Result(property = "pkAccass" , column = "PK_ACCASS"),
		@Result(property = "debitamount" , column = "DEBITAMOUNT"),
		@Result(property = "creditmount" , column = "CREDITMOUNT"),
		@Result(property = "beginbalancemount" , column = "BEGINBALANCEMOUNT"),
		@Result(property = "endbalancemount" , column = "ENDBALANCEMOUNT"),
		@Result(property = "yeardebitamount" , column = "YEARDEBITAMOUNT"),
		@Result(property = "yearcreditmount" , column = "YEARCREDITMOUNT"),
		@Result(property = "asstype" , column = "ASSTYPE"),
		@Result(property = "assname" , column = "ASSNAME"),
		@Result(property = "assdes" , column = "ASSDES"),
		@Result(property = "assdd" , column = "ASSDD"),
	})
	List<GlAssBalaneVr> exportAccAssGeneralLedgerList(ExportRequestVo exportRequestVo) throws Exception;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
