package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.DebtCollectionLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 债权催收记录Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Mapper
public interface DebtCollectionLogMapper extends BaseMapper<DebtCollectionLog> {

    /**
     * 根据债权ID查询催收记录列表
     *
     * @param debtId 债权ID
     * @return 催收记录列表
     */
    @Select("SELECT * FROM debt_collection_log WHERE debt_id = #{debtId} AND deleted = 0 ORDER BY collection_date DESC")
    List<DebtCollectionLog> selectByDebtId(@Param("debtId") Long debtId);

    /**
     * 根据催收人员ID查询催收记录列表
     *
     * @param collectorId 催收人员ID
     * @return 催收记录列表
     */
    @Select("SELECT * FROM debt_collection_log WHERE collector_id = #{collectorId} AND deleted = 0 ORDER BY collection_date DESC")
    List<DebtCollectionLog> selectByCollectorId(@Param("collectorId") Long collectorId);

    /**
     * 根据催收方式查询催收记录列表
     *
     * @param collectionMethod 催收方式
     * @return 催收记录列表
     */
    @Select("SELECT * FROM debt_collection_log WHERE collection_method = #{collectionMethod} AND deleted = 0 ORDER BY collection_date DESC")
    List<DebtCollectionLog> selectByCollectionMethod(@Param("collectionMethod") Integer collectionMethod);

    /**
     * 根据催收结果查询催收记录列表
     *
     * @param collectionResult 催收结果
     * @return 催收记录列表
     */
    @Select("SELECT * FROM debt_collection_log WHERE collection_result = #{collectionResult} AND deleted = 0 ORDER BY collection_date DESC")
    List<DebtCollectionLog> selectByCollectionResult(@Param("collectionResult") Integer collectionResult);

    /**
     * 获取债权催收统计信息
     *
     * @param debtId 债权ID
     * @return 统计信息
     */
    @Select("SELECT " +
            "COUNT(*) as totalCollections, " +
            "COUNT(CASE WHEN collection_result = 1 THEN 1 END) as successfulCollections, " +
            "COALESCE(SUM(promised_amount), 0) as totalPromisedAmount, " +
            "CASE WHEN COUNT(*) > 0 THEN ROUND(COUNT(CASE WHEN collection_result = 1 THEN 1 END) * 100.0 / COUNT(*), 2) ELSE 0 END as successRate " +
            "FROM debt_collection_log WHERE debt_id = #{debtId} AND deleted = 0")
    Map<String, Object> getCollectionSummary(@Param("debtId") Long debtId);
}
