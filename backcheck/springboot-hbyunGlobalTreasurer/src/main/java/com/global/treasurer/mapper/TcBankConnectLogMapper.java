package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TcBankConnectLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 银企直连日志Mapper接口
 * @author AI Assistant
 * @date 2025-01-26
 */
@Mapper
public interface TcBankConnectLogMapper extends BaseMapper<TcBankConnectLog> {

    /**
     * 多条件查询日志列表
     */
    List<TcBankConnectLog> selectByConditions(@Param("params") Map<String, Object> params);

    /**
     * 获取日志统计信息
     */
    Map<String, Object> selectStatistics();

    /**
     * 根据请求ID查询日志
     */
    TcBankConnectLog selectByRequestId(@Param("requestId") String requestId);

    /**
     * 清理历史日志
     */
    int deleteByDays(@Param("days") Integer days);

    /**
     * 按接口类型统计
     */
    List<Map<String, Object>> selectCountByInterfaceType();

    /**
     * 按执行状态统计
     */
    List<Map<String, Object>> selectCountByStatus();

    /**
     * 查询失败日志列表
     */
    List<TcBankConnectLog> selectFailedLogs(@Param("limit") Integer limit);
}

