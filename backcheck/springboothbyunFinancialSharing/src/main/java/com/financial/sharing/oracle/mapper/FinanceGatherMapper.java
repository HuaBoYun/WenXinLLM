package com.financial.sharing.oracle.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 财务数据收集Mapper接口
 */
public interface FinanceGatherMapper {
    
    /**
     * 查询收集列表
     */
    List<Map<String, Object>> selectGatherList(@Param("params") Map<String, Object> params);
    
    /**
     * 根据ID查询收集详情
     */
    Map<String, Object> selectGatherById(@Param("gatherId") String gatherId);
    
    /**
     * 插入收集
     */
    int insertGather(@Param("gather") Map<String, Object> gather);
    
    /**
     * 更新收集
     */
    int updateGather(@Param("gather") Map<String, Object> gather);
    
    /**
     * 删除收集
     */
    int deleteGather(@Param("gatherId") String gatherId);
    
    /**
     * 更新收集状态
     */
    int updateGatherStatus(@Param("params") Map<String, Object> params);
    
    /**
     * 查询收集统计
     */
    Map<String, Object> selectGatherStatistics(@Param("params") Map<String, Object> params);

    // ==================== 数据采集相关方法 ====================

    /**
     * 执行抽取任务
     */
    int executeExtractTask(@Param("params") Map<String, Object> params);

    /**
     * 停止抽取任务
     */
    int stopExtractTask(@Param("params") Map<String, Object> params);

    /**
     * 查询采集方案树列表
     */
    List<Map<String, Object>> selectPlanTreeList(@Param("params") Map<String, Object> params);

    /**
     * 统计采集方案树总数
     */
    int countPlanTreeList(@Param("params") Map<String, Object> params);

    /**
     * 停止方案采集
     */
    int stopPlanGather(@Param("params") Map<String, Object> params);

    /**
     * 启动方案采集
     */
    int startPlanGather(@Param("params") Map<String, Object> params);

    /**
     * 查询方案采集状态
     */
    Map<String, Object> selectPlanGatherStatus(@Param("params") Map<String, Object> params);

    /**
     * 停止子方案采集
     */
    int stopSubPlanGather(@Param("params") Map<String, Object> params);

    /**
     * 查询采集记录列表
     */
    List<Map<String, Object>> selectGatherRecordList(@Param("params") Map<String, Object> params);

    /**
     * 统计采集记录总数
     */
    int countGatherRecordList(@Param("params") Map<String, Object> params);

    /**
     * 查询采集记录结果
     */
    Map<String, Object> selectGatherRecordResult(@Param("params") Map<String, Object> params);
}