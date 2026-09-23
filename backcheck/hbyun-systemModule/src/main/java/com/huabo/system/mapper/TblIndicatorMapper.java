package com.huabo.system.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblIndicator;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-27
 */
public interface TblIndicatorMapper extends BaseMapper<TblIndicator> {

    @SelectProvider(type=TblIndicatorMapperSqlConfig.class,method="findIndicatorByUseridAndSlouid")
    IPage<TblIndicator> findIndicatorByUseridAndSlouid(IPage<TblIndicator> page, BigDecimal staffid);

    @SelectProvider(type=TblIndicatorMapperSqlConfig.class,method="findIndicatorByJKZX")
    IPage<TblIndicator> findIndicatorByJKZX(String solutionid, IPage<TblIndicator> page);

    @Select("SELECT * FROM TBL_INDICATOR TI LEFT JOIN TBL_INDICATOR_FLOW TIF ON TI.INDICATORID = TIF.INDICATORID " +
            " LEFT JOIN TBL_FLOW TF ON TIF.FLOWID = TF.FLOWID WHERE TI.INDICATORID = #{indicatorid}")
    TblIndicator findIndicatorid(String indicatorid);
}
