package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblMonitorSolutionMySql;
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
public interface TblMonitorSolutionMySqlMapper extends BaseMapper<TblMonitorSolutionMySql> {
    @SelectProvider(type = TblMonitorSolutionMapperSqlMySqlConfig.class, method = "tblMonitorSolutionService")
    List<TblMonitorSolutionMySql> tblMonitorSolutionService(PageInfo<TblMonitorSolutionMySql> pageInfo, BigDecimal staffid, String type);

    @SelectProvider(type = TblMonitorSolutionMapperSqlMySqlConfig.class, method = "tblMonitorSolutionServiceCount")
    Integer tblMonitorSolutionServiceCount(PageInfo<TblMonitorSolutionMySql> pageInfo,BigDecimal staffid, String type);

    @Select("SELECT * FROM TBL_MONITOR_SOLUTION WHERE SOLUTIONID = #{solutionid}")
    TblMonitorSolutionMySql selectSolutionid(String solutionid);


//    @SelectProvider(type=TblMonitorSolutionMapperSqlConfig.class,method="tblMonitorSolutionService")
//    List<TblMonitorSolution> tblMonitorSolutionService(PageInfo<TblMonitorSolution> pageInfo, BigDecimal staffid, String type);
//
//    @Select("SELECT * from TBL_MONITOR_SOLUTION where SOLUTIONID in ( SELECT SOLUTIONID FROM TBL_MONITOR_SOLUTION_STAFF WHERE STAFFID= #{staffid} ) and TYPE= #{type}")
//    Integer tblMonitorSolutionServiceCount(BigDecimal staffid, String type);
}
