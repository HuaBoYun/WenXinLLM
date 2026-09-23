package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblBillRecognitionLog;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 账单识别日志Mapper
 */
public interface TblBillRecognitionLogMapper extends BaseMapper<TblBillRecognitionLog> {

    /**
     * 根据配置ID查询
     */
    List<TblBillRecognitionLog> selectByConfigId(@Param("configId") String configId);

    /**
     * 根据业务类型和业务ID查询
     */
    List<TblBillRecognitionLog> selectByBusiness(@Param("businessType") String businessType, @Param("businessId") String businessId);

    /**
     * 根据识别状态查询
     */
    List<TblBillRecognitionLog> selectByRecognitionStatus(@Param("recognitionStatus") String recognitionStatus);

    /**
     * 根据识别时间范围查询
     */
    List<TblBillRecognitionLog> selectByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据识别人查询
     */
    List<TblBillRecognitionLog> selectByRecognitionUser(@Param("recognitionUser") String recognitionUser);

    /**
     * 查询最近的识别记录
     */
    List<TblBillRecognitionLog> selectRecentLogs(@Param("limit") Integer limit);
}
