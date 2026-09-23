package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsGcjsSettlement;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

public interface TblYqnsGcjsSettlementMapper  extends BaseMapper<TblYqnsGcjsSettlement> {

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsGcjsSettlementMapperSqlConfig.class)
    @Results({
    })
    List<TblYqnsGcjsSettlement> selectListByPageInfo(PageInfo<TblYqnsGcjsSettlement> pageInfo, TblYqnsGcjsSettlement vo);

    @Select("SELECT MAX(SORTINDEX) FROM TBL_YQNS_GCJS_SETTLEMENT")
	BigDecimal selectMaxNo();
    
    
    @Select("SELECT count(*) FROM TBL_YQNS_GCJS_SETTLEMENT where SETTLEMENTCODE=#{no} ")
    Integer selectbyHtbh(String no);
}
