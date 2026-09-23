package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblSettlementException;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface TblSettlementExceptionMapper extends BaseMapper<TblSettlementException> {
    List<TblSettlementException> selectExceptionPage(Map<String, Object> params);
    int countExceptionList(Map<String, Object> params);
    List<TblSettlementException> selectUnresolvedExceptions(@Param("orgId") Long orgId);
    List<TblSettlementException> selectCriticalExceptions(@Param("orgId") Long orgId);
    Map<String, Object> selectExceptionSummary(Map<String, Object> params);
    List<Map<String, Object>> selectExceptionTrend(Map<String, Object> params);
}
