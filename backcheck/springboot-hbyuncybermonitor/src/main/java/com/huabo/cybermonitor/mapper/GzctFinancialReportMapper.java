package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.GzctFinancialReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GzctFinancialReportMapper extends BaseMapper<GzctFinancialReport> {

    Map<String, Object> selectStatisticsByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    List<Map<String, Object>> selectReportTypeDistribution(@Param("enterpriseId") String enterpriseId);
}
