package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblMonitorSolutionresultMySql;
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
public interface TblMonitorSolutionresultMySqlMapper extends BaseMapper<TblMonitorSolutionresultMySql> {

    @Select("SELECT * FROM TBL_MONITOR_PREWARNING TMP LEFT JOIN TBL_MONITOR_SOLUTIONRESULT TMS ON TMP.SOLUTIONRESULTID " +
            " = TMS.SOLUTIONRESULTID WHERE TMS.SOURCE = 3 AND TMS.SOLUTIONID = #{soultionId} AND TMP.RULEID= #{ruleid} ")
    List<String> findRuleidAndAcctid(BigDecimal soultionId, String ruleid);

    @SelectProvider(type = TblMonitorSolutionresultMapperSqlMySqlConfig.class, method = "findBySoultionIdZKZX")
    List<TblMonitorSolutionresultMySql> findBySoultionIdZKZX(String zt, String table, BigDecimal soultionId, String rulid, PageInfo<TblMonitorSolutionresultMySql> pageInfo);

    @Select("select *  from #{zt}.#{table} where  EXECTIME = (SELECT SIGNID FROM TBL_MONITOR_PREWARNING WHERE SOLUTIONRESULTID = ( select SOLUTIONRESULTID ,@ROW := @ROW + 1 AS SIGNED from(SELECT @ROW := 0) R,( SELECT SOLUTIONRESULTID FROM TBL_MONITOR_SOLUTIONRESULT  WHERE SOURCE = 3 AND SOLUTIONID = #{soultionId} order by savetime desc) having signed = 1) and RULEID= #{rulid})")
    Integer findBySoultionIdZKZXCount(String zt, String table, BigDecimal soultionId, String rulid);


}
