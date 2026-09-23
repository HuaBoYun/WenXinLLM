package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.GzctFinancialWarning;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GzctFinancialWarningMapper extends BaseMapper<GzctFinancialWarning> {

    Map<String, Object> selectStatisticsByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    List<Map<String, Object>> selectWarningTypeDistribution(@Param("enterpriseId") String enterpriseId);

    List<Map<String, Object>> selectWarningTrend(@Param("enterpriseId") String enterpriseId, @Param("months") Integer months);
}
