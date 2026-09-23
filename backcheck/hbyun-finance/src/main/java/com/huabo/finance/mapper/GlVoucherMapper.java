package com.huabo.finance.mapper;

import com.huabo.finance.entity.caiji.GlVoucher;
import com.huabo.finance.mappersql.GlVoucherMapperSqlConfig;
import com.huabo.finance.vo.ExportRequestVo;
import com.huabo.finance.vo.GlVoucherVo;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.FaAccbookinfoUtil;

/**
 * <p>
 * 凭证库表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-03-19
 */
public interface GlVoucherMapper extends BaseMapper<GlVoucher> {

	@SelectProvider(type = GlVoucherMapperSqlConfig.class , method = "selectFinanceDataPage")
	IPage<GlVoucher> selectFinanceDataPage(Page<GlVoucher> page, GlVoucherVo vo, FaAccbookinfoUtil bookInfo) throws Exception;

	@SelectProvider(type = GlVoucherMapperSqlConfig.class , method = "exportVoucherList")
	List<GlVoucher> exportVoucherList(ExportRequestVo exportRequestVo) throws Exception;

}
