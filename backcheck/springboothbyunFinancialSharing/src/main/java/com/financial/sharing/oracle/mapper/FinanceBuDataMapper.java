package com.financial.sharing.oracle.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * BU数据Mapper接口
 */
public interface FinanceBuDataMapper {
    
    /**
     * 查询BU数据列表
     */
    List<Map<String, Object>> selectBuDataList(@Param("params") Map<String, Object> params);
    
    /**
     * 根据ID查询BU数据详情
     */
    Map<String, Object> selectBuDataById(@Param("buId") String buId);
    
    /**
     * 插入BU数据
     */
    int insertBuData(@Param("buData") Map<String, Object> buData);
    
    /**
     * 更新BU数据
     */
    int updateBuData(@Param("buData") Map<String, Object> buData);
    
    /**
     * 删除BU数据
     */
    int deleteBuData(@Param("buId") String buId);
    
    /**
     * 查询BU数据统计
     */
    Map<String, Object> selectBuDataStatistics(@Param("params") Map<String, Object> params);
    
    /**
     * 批量插入BU数据
     */
    int batchInsertBuData(@Param("buDataList") List<Map<String, Object>> buDataList);
    
    /**
     * 查询导出BU数据
     */
    List<Map<String, Object>> selectBuDataForExport(@Param("params") Map<String, Object> params);

    // ==================== 业务数据管理相关方法 ====================

    /**
     * 查询业务数据列表
     */
    List<Map<String, Object>> selectYWSJList(@Param("params") Map<String, Object> params);

    /**
     * 统计业务数据总数
     */
    int countYWSJList(@Param("params") Map<String, Object> params);

    /**
     * 插入业务数据
     */
    int insertYWSJ(@Param("data") Map<String, Object> data);

    /**
     * 更新业务数据
     */
    int updateYWSJ(@Param("data") Map<String, Object> data);

    /**
     * 查询业务数据详情
     */
    Map<String, Object> selectYWSJDetail(@Param("params") Map<String, Object> params);

    /**
     * 删除业务数据
     */
    int deleteYWSJ(@Param("params") Map<String, Object> params);

    /**
     * 发布业务数据
     */
    int publishYWSJ(@Param("params") Map<String, Object> params);

    /**
     * 更新业务数据状态
     */
    int updateYWSJStatus(@Param("params") Map<String, Object> params);

    /**
     * 启动采集任务
     */
    int startGatherTask(@Param("params") Map<String, Object> params);

    /**
     * 停止采集任务
     */
    int stopGatherTask(@Param("params") Map<String, Object> params);

    /**
     * 执行SQL测试
     */
    List<Map<String, Object>> executeSqlTest(@Param("data") Map<String, Object> data);
}