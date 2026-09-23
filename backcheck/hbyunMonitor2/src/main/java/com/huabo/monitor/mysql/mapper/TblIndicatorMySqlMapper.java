package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.TblIndicator;
import com.huabo.monitor.mysql.entity.TblIndicatorMySql;
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
 * @since 2022-04-27
 */
public interface TblIndicatorMySqlMapper extends BaseMapper<TblIndicatorMySql> {

    @SelectProvider(type = TblIndicatorMapperSqlMySqlConfig.class, method = "findIndicatorByUseridAndSlouid")
    List<TblIndicator> findIndicatorByUseridAndSlouid(PageInfo<TblIndicator> pageInfo, BigDecimal staffid);

    @Select(" SELECT COUNT(*) from TBL_INDICATOR ind LEFT JOIN TBL_MONITOR_SOLUTION_INDICATOR msi on IND.INDICATORID=MSI.INDICATORID LEFT JOIN TBL_MONITOR_SOLUTION TMS ON TMS.SOLUTIONID = TMS.SOLUTIONID LEFT JOIN TBL_MONITOR_SOLUTION_STAFF TMSS ON TMS.SOLUTIONID = TMSS.SOLUTIONID where TMSS.STAFFID= #{staffid} and TMS.TYPE=2 ")
    Integer findIndicatorByUseridAndSlouidCount(BigDecimal staffid);

    @SelectProvider(type = TblIndicatorMapperSqlMySqlConfig.class, method = "findIndicatorByJKZX")
    List<TblIndicator> findIndicatorByJKZX(String solutionid, PageInfo<TblIndicator> pageInfo);

    @Select(" select COUNT(*) from TBL_INDICATOR TI LEFT JOIN TBL_MONITOR_SOLUTION_INDICATOR TMSI ON TI.INDICATORID =TMSI.INDICATORID LEFT JOIN TBL_MONITOR_SOLUTION TMS ON TMSI.SOLUTIONID = TMS.SOLUTIONID WHERE TMS.SOLUTIONID = #{solutionid}")
    Integer findIndicatorByJKZXCount(String solutionid);

    @Select("SELECT * FROM TBL_INDICATOR TI LEFT JOIN TBL_INDICATOR_FLOW TIF ON TI.INDICATORID = TIF.INDICATORID " +
            " LEFT JOIN TBL_FLOW TF ON TIF.FLOWID = TF.FLOWID WHERE TI.INDICATORID = #{indicatorid}")
    TblIndicatorMySql findIndicatorid(String indicatorid);
}
