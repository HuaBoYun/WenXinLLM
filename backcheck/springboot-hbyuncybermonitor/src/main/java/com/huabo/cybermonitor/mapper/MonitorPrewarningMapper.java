package com.huabo.cybermonitor.mapper;

import com.huabo.cybermonitor.entity.MonitorPrewarning;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-08-12
 */
public interface MonitorPrewarningMapper extends BaseMapper<MonitorPrewarning> {

    @Select("SELECT mp.* FROM TBL_Monitor_Prewarning mp LEFT JOIN TBL_Monitor_SolutionResult ms ON mp.solutionresultid = MS.SOLUTIONRESULTID LEFT JOIN TBL_Monitor_Solution tms ON tms.solutionid = ms.solutionid WHERE " +
            "tms.solutionid =#{modelId} and   MS.SOLUTIONRESULTID = #{solutionResultId}")
    List<MonitorPrewarning> getResultBySignId(BigDecimal modelId, BigDecimal solutionResultId);

}
