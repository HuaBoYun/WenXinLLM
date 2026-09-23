package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcSealUsageLog;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

/**
 * 财资公共模块 - 印鉴使用记录管理Mapper
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
public interface TcSealUsageLogMapper extends Mapper<TcSealUsageLog> {

    /**
     * 根据印鉴ID查询使用记录
     * 
     * @param sealId 印鉴ID
     * @return 印鉴使用记录列表
     */
    List<TcSealUsageLog> selectBySealId(@Param("sealId") String sealId);

    /**
     * 根据组合ID查询使用记录
     * 
     * @param combinationId 组合ID
     * @return 印鉴使用记录列表
     */
    List<TcSealUsageLog> selectByCombinationId(@Param("combinationId") String combinationId);

    /**
     * 根据使用人查询使用记录
     * 
     * @param usageUser 使用人
     * @return 印鉴使用记录列表
     */
    List<TcSealUsageLog> selectByUsageUser(@Param("usageUser") String usageUser);

    /**
     * 根据业务单据号查询使用记录
     * 
     * @param businessDocNo 业务单据号
     * @return 印鉴使用记录列表
     */
    List<TcSealUsageLog> selectByBusinessDocNo(@Param("businessDocNo") String businessDocNo);

    /**
     * 根据条件查询使用记录列表
     * 
     * @param sealId 印鉴ID
     * @param combinationId 组合ID
     * @param usageUser 使用人
     * @param businessType 业务类型
     * @param usageResult 使用结果
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 印鉴使用记录列表
     */
    List<TcSealUsageLog> selectByCondition(@Param("sealId") String sealId,
                                           @Param("combinationId") String combinationId,
                                           @Param("usageUser") String usageUser,
                                           @Param("businessType") String businessType,
                                           @Param("usageResult") String usageResult,
                                           @Param("startTime") Date startTime,
                                           @Param("endTime") Date endTime);

    /**
     * 根据时间范围查询使用记录
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 印鉴使用记录列表
     */
    List<TcSealUsageLog> selectByTimeRange(@Param("startTime") Date startTime,
                                           @Param("endTime") Date endTime);

    /**
     * 根据业务类型查询使用记录
     * 
     * @param businessType 业务类型
     * @return 印鉴使用记录列表
     */
    List<TcSealUsageLog> selectByBusinessType(@Param("businessType") String businessType);

    /**
     * 统计印鉴使用次数
     * 
     * @param sealId 印鉴ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 使用次数
     */
    int countUsageBySealId(@Param("sealId") String sealId,
                           @Param("startTime") Date startTime,
                           @Param("endTime") Date endTime);

    /**
     * 统计用户使用次数
     * 
     * @param usageUser 使用人
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 使用次数
     */
    int countUsageByUser(@Param("usageUser") String usageUser,
                         @Param("startTime") Date startTime,
                         @Param("endTime") Date endTime);

    /**
     * 统计各业务类型使用情况
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 业务类型统计结果
     */
    List<java.util.Map<String, Object>> countByBusinessType(@Param("startTime") Date startTime,
                                                             @Param("endTime") Date endTime);

    /**
     * 统计各使用结果情况
     * 
     * @return 使用结果统计
     */
    List<java.util.Map<String, Object>> countByUsageResult();
}
