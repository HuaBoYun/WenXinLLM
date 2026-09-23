package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.GzctFinAnomalyRectification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GzctFinAnomalyRectificationMapper extends BaseMapper<GzctFinAnomalyRectification> {

    @Select("SELECT * FROM GZCT_FIN_ANOMALY_RECTIFICATION WHERE ANOMALY_ID = #{anomalyId} ORDER BY CREATE_TIME DESC")
    List<GzctFinAnomalyRectification> selectByAnomalyId(@Param("anomalyId") String anomalyId);
}
