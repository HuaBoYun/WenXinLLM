package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblSettlementBatch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface TblSettlementBatchMapper extends BaseMapper<TblSettlementBatch> {
    List<TblSettlementBatch> selectBatchPage(Map<String, Object> params);
    int countBatchList(Map<String, Object> params);
    List<TblSettlementBatch> selectExecutableBatches(@Param("orgId") Long orgId);
    Map<String, Object> selectBatchSummary(Map<String, Object> params);
    Map<String, Object> selectPerformanceAnalysis(Map<String, Object> params);
}
