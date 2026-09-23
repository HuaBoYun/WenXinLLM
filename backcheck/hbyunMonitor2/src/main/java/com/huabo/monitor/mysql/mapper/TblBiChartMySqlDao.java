package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblBiChartMySql;
import org.apache.ibatis.annotations.InsertProvider;

public interface TblBiChartMySqlDao extends BaseMapper<TblBiChartMySql> {

    @InsertProvider(type = TblBichartDaoSqlMySqlConfig.class, method = "insertBichart")
    void insertBichart(TblBiChartMySql biChart);
}
