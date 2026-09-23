package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.FinancialIndicators;
import com.huabo.cybermonitor.vo.FinancialIndicatorsQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FinancialIndicatorsMapper extends BaseMapper<FinancialIndicators> {

    List<FinancialIndicators> selectFinancialIndicatorsList(FinancialIndicatorsQueryVO queryVO);

    List<FinancialIndicators> selectByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    List<FinancialIndicators> selectByReportYear(@Param("reportYear") Integer reportYear);

    List<Map<String, Object>> selectProfitabilityAnalysis(@Param("enterpriseId") String enterpriseId,
                                                          @Param("startYear") Integer startYear,
                                                          @Param("endYear") Integer endYear);

    List<Map<String, Object>> selectSolvencyAnalysis(@Param("enterpriseId") String enterpriseId,
                                                     @Param("startYear") Integer startYear,
                                                     @Param("endYear") Integer endYear);

    List<Map<String, Object>> selectOperatingAbilityAnalysis(@Param("enterpriseId") String enterpriseId,
                                                              @Param("startYear") Integer startYear,
                                                              @Param("endYear") Integer endYear);

    List<Map<String, Object>> selectDevelopmentAbilityAnalysis(@Param("enterpriseId") String enterpriseId,
                                                                @Param("startYear") Integer startYear,
                                                                @Param("endYear") Integer endYear);

    List<Map<String, Object>> selectCashFlowAnalysis(@Param("enterpriseId") String enterpriseId,
                                                     @Param("startYear") Integer startYear,
                                                     @Param("endYear") Integer endYear);

    int deleteExpiredIndicatorRecords(@Param("days") Integer days);
}
