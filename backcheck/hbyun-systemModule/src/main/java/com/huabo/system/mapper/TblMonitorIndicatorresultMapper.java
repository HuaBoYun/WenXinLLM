package com.huabo.system.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblMonitorIndicatorresult;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblMonitorIndicatorresultMapper extends BaseMapper<TblMonitorIndicatorresult> {

    @SelectProvider(type=TblMonitorIndicatorresultMapperSqlConfig.class,method="getResultListJKZX")
    IPage<TblMonitorIndicatorresult> getResultListJKZX(IPage<TblMonitorIndicatorresult> page, BigDecimal indicatorid, BigDecimal solutionresultid);

    @Select("SELECT COUNT(*) FROM TBL_MONITOR_INDICATORRESULT where RESULTID =(select RESULTID from (SELECT RESULTID FROM TBL_MONITOR_INDICATORRESULT WHERE SOLUTIONRESULTID= #{solutionresultid} and INDICATORID= #{indicatorid} and SOURCE=3 order by savetime desc) where rownum =1)")
    Integer getResultListJKZXCount(BigDecimal indicatorid, BigDecimal solutionresultid);

}
