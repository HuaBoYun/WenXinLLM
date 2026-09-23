package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblProcessingRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface TblProcessingRecordMapper extends BaseMapper<TblProcessingRecord> {
    List<TblProcessingRecord> selectProcessingRecordPage(Map<String, Object> params);
    int countProcessingRecordList(Map<String, Object> params);
    List<TblProcessingRecord> selectRetryableRecords(@Param("orgId") Long orgId);
    List<TblProcessingRecord> selectTimeoutRecords(Map<String, Object> params);
    Map<String, Object> selectProcessingSummary(Map<String, Object> params);
}
