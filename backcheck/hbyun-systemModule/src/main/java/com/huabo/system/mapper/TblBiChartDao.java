package com.huabo.system.mapper;

import org.apache.ibatis.annotations.InsertProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblBiChart;
import com.huabo.system.mappersql.TblBiChartDaoSqlConfig;


public interface TblBiChartDao extends BaseMapper<TblBiChart> {

    @InsertProvider(type=TblBiChartDaoSqlConfig.class,method="insertBichart")
    void insertBichart(TblBiChart biChart);
}
