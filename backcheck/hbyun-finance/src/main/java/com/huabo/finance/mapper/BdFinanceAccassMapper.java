package com.huabo.finance.mapper;

import com.huabo.finance.entity.caiji.BdFinanceAccass;
import com.huabo.finance.mappersql.BdFinanceAccassMapperSqlConfig;
import com.huabo.finance.vo.BdFinanceAccassVo;
import com.huabo.finance.vo.ExportRequestVo;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.FaAccbookinfoUtil;

/**
 * <p>
 * 会计辅助信息 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-04-20
 */
public interface BdFinanceAccassMapper extends BaseMapper<BdFinanceAccass> {

	@SelectProvider(type = BdFinanceAccassMapperSqlConfig.class,method = "exportAccAssList")
	List<BdFinanceAccass> exportAccAssList(ExportRequestVo exportRequestVo) throws Exception;

	@SelectProvider(type = BdFinanceAccassMapperSqlConfig.class,method = "selectPageList")
	IPage<BdFinanceAccass> selectPageList(Page<BdFinanceAccass> page, BdFinanceAccassVo vo, FaAccbookinfoUtil bookInfo) throws Exception;

}
