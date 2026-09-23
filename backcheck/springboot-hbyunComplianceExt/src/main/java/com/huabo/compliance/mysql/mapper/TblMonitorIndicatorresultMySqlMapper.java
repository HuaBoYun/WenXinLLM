package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;

import com.huabo.compliance.entity.TblMonitorIndicatorresult;
import com.huabo.compliance.mysql.entity.TblMonitorIndicatorresultMySql;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblMonitorIndicatorresultMySqlMapper extends BaseMapper<TblMonitorIndicatorresultMySql> {

    @SelectProvider(type = TblMonitorIndicatorresultMapperSqlMySqlConfig.class, method = "getResultListJKZX")
    List<TblMonitorIndicatorresult> getResultListJKZX(PageInfo<TblMonitorIndicatorresult> pageInfo, BigDecimal indicatorid, BigDecimal solutionresultid);

    @Select("SELECT COUNT(*) FROM TBL_MONITOR_INDICATORRESULT where RESULTID =(select RESULTID from ( SELECT RESULTID FROM TBL_MONITOR_INDICATORRESULT WHERE SOLUTIONRESULTID= #{solutionresultid} and INDICATORID= #{indicatorid} and SOURCE=3 order by savetime desc) as a )")
    Integer getResultListJKZXCount(BigDecimal indicatorid, BigDecimal solutionresultid);

}
