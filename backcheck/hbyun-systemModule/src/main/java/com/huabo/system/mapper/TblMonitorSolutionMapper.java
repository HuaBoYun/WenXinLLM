package com.huabo.system.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblMonitorRule;
import com.huabo.system.entity.TblMonitorSolution;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-27
 */
public interface TblMonitorSolutionMapper extends BaseMapper<TblMonitorSolution> {
    @SelectProvider(type = TblMonitorSolutionMapperSqlConfig.class, method = "tblMonitorSolutionService")
    IPage<TblMonitorSolution> tblMonitorSolutionService(IPage<TblMonitorSolution> page, BigDecimal staffid, String type);

    @Select("SELECT * FROM TBL_MONITOR_SOLUTION WHERE SOLUTIONID = #{solutionid}")
    TblMonitorSolution selectSolutionid(String solutionid);


//    @SelectProvider(type=TblMonitorSolutionMapperSqlConfig.class,method="tblMonitorSolutionService")
//    List<TblMonitorSolution> tblMonitorSolutionService(PageInfo<TblMonitorSolution> pageInfo, BigDecimal staffid, String type);
//
//    @Select("SELECT * from TBL_MONITOR_SOLUTION where SOLUTIONID in ( SELECT SOLUTIONID FROM TBL_MONITOR_SOLUTION_STAFF WHERE STAFFID= #{staffid} ) and TYPE= #{type}")
//    Integer tblMonitorSolutionServiceCount(BigDecimal staffid, String type);
}
