package com.huabo.contract.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblOppblackRecord;

/**
 * <p>
 * 相对方加入黑名单记录表 Mapper 接口
 * </p>
 *
 * @author lhp
 * @since 2025-02-26
 */
public interface TblOppblackRecordMapper extends BaseMapper<TblOppblackRecord> {

	@Select("SELECT COUNT(0) FROM TBL_OPPBLACK_RECORD WHERE OPPOID = #{budgetId}")
	Integer selectAddBlackVersionById(@Param("budgetId") BigDecimal budgetId) throws Exception;

	@Select("SELECT TOR.* FROM TBL_OPPBLACK_RECORD TOR INNER JOIN (SELECT MAX(VERSION) AS MAVER FROM TBL_OPPBLACK_RECORD WHERE OPPOID = #{budgetid}) TORV ON TOR.VERSION = TORV.MAVER  WHERE TOR.OPPOID = #{budgetid}")
	TblOppblackRecord selectByOppId(@Param("budgetid") BigDecimal budgetid) throws Exception;

}
