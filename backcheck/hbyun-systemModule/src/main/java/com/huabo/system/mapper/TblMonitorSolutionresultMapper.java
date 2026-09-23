package com.huabo.system.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblMonitorSolutionresult;
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
 * @since 2022-04-28
 */
public interface TblMonitorSolutionresultMapper extends BaseMapper<TblMonitorSolutionresult> {

    @Select("SELECT * FROM TBL_MONITOR_PREWARNING TMP LEFT JOIN TBL_MONITOR_SOLUTIONRESULT TMS ON TMP.SOLUTIONRESULTID " +
            " = TMS.SOLUTIONRESULTID WHERE TMS.SOURCE = 3 AND TMS.SOLUTIONID = #{soultionId} AND TMP.RULEID= #{ruleid} ")
    List<String> findRuleidAndAcctid(BigDecimal soultionId, String ruleid);

    @SelectProvider(type=TblMonitorSolutionresultMapperSqlConfig.class,method="findBySoultionIdZKZX")
    IPage<TblMonitorSolutionresult> findBySoultionIdZKZX(String zt, String table, BigDecimal soultionId, String rulid, IPage<TblMonitorSolutionresult> page);

}
