package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblYyPrice;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-07
 */
public interface TblYyPriceMapper extends BaseMapper<TblYyPrice> {

	@Select("SELECT PRICEID FROM TBL_YY_REPORT_MODEL WHERE REPORTID = #{reportid}")
    String findByIs(@Param("reportid") BigDecimal reportid) throws Exception;

    @Select("select * from TBL_YY_PRICE where PRICEID >= 2 order by PRICEID")
    List<TblYyPrice> findAll() throws Exception;

    @Select("select INTERFACENAME from TBL_YY_PRICE where PRICEID in (${priceIds}) and INTERFACENAME != '基本信息' and INTERFACENAME != '核心团队' ")
	List<TblYyPrice> findByPriceIds(@Param("priceIds")String priceIds) throws Exception;


}
