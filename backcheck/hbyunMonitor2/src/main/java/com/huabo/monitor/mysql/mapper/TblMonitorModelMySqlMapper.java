package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.TblMonitorModel;
import com.huabo.monitor.mysql.entity.TblMonitorModelMySql;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblMonitorModelMySqlMapper extends BaseMapper<TblMonitorModelMySql> {

    @SelectProvider(type = TblMonitorModelMapperSqlMySqlConfig.class, method = "findByModelJKZX")
    List<TblMonitorModel> findByModelJKZX(String solutionid, PageInfo<TblMonitorModel> pageInfo);

    @Select("SELECT COUNT(*) from TBL_MONITOR_MODEL TMM LEFT JOIN TBL_MONITOR_SOLUTION_MODEL TMSM ON TMM.MODELID = TMSM.MODELID LEFT JOIN TBL_MONITOR_SOLUTION TMS ON TMSM.SOLUTIONID = TMS.SOLUTIONID WHERE TMS.SOLUTIONID = #{solutionid} ")
    Integer findByModelJKZXCount(String solutionid);
}
