package com.huabo.contract.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblOppblackRemove;

/**
 * <p>
 * 相对方移出黑名单记录表 Mapper 接口
 * </p>
 *
 * @author lhp
 * @since 2025-02-26
 */
public interface TblOppblackRemoveMapper extends BaseMapper<TblOppblackRemove> {

	@Select("SELECT * FROM TBL_OPPBLACK_REMOVE WHERE BRID = #{brid} AND OPPOID = #{budgetid}")
	TblOppblackRemove selectUniqueByBrIdOppId(@Param("brid") String brid,@Param("budgetid") BigDecimal budgetid);

}
