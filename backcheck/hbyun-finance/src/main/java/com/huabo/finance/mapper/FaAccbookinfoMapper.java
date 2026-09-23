package com.huabo.finance.mapper;

import com.huabo.finance.entity.caiji.FaAccbookinfo;
import com.huabo.finance.mappersql.FaAccbookinfoMapperSqlConfig;
import com.huabo.finance.vo.FaAccbookinfoVo;
import com.huabo.finance.vr.FaAccbookinfoVr;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 账簿信息 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
public interface FaAccbookinfoMapper extends BaseMapper<FaAccbookinfo> {

	@SelectProvider(type = FaAccbookinfoMapperSqlConfig.class , method = "selectPageInfo")
	IPage<FaAccbookinfoVr> selectPageInfo(Page<FaAccbookinfoVr> page, FaAccbookinfoVo vo) throws Exception;

	@Select("SELECT ABI.*,OSB.NAME AS SETOFNAME,BFP.FNAME AS PLANNAME FROM FA_ACCBOOKINFO ABI LEFT JOIN ORG_SETOFBOOK OSB ON ABI.PK_SETOFBOOK = OSB.PK_SETOFBOOK LEFT JOIN BD_FINANCEPLAN BFP ON ABI.PK_FINANPLANID = BFP.FID WHERE ABI.PK_ACCBOOKINFO = #{pkAccbookinfo}")
	FaAccbookinfoVr selectEntityById(@Param("pkAccbookinfo")String pkAccbookinfo);

	@SelectProvider(type = FaAccbookinfoMapperSqlConfig.class , method = "selectPageInfoByUserRole")
	IPage<FaAccbookinfoVr> selectPageInfoByUserRole(Page<FaAccbookinfoVr> page, FaAccbookinfoVo vo, String roleIdStrs, BigDecimal staffId) throws Exception;

}
