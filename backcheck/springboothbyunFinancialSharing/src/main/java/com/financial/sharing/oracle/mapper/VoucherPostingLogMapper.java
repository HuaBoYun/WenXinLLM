package com.financial.sharing.oracle.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 凭证过账日志 Mapper接口
 *
 * @author system
 * @since 2026-01-03
 */
public interface VoucherPostingLogMapper {

    /**
     * 根据凭证ID查询过账日志列表
     *
     * @param voucherId 凭证ID
     * @return 过账日志列表
     */
    List<Map<String, Object>> selectLogsByVoucherId(@Param("voucherId") Long voucherId);

    /**
     * 查询过账日志总数
     *
     * @param voucherId 凭证ID
     * @return 日志总数
     */
    Long selectLogsCountByVoucherId(@Param("voucherId") Long voucherId);

    /**
     * 插入过账日志
     *
     * @param logData 日志数据
     * @return 插入结果
     */
    int insertPostingLog(@Param("logData") Map<String, Object> logData);

    /**
     * 批量插入过账日志
     *
     * @param logList 日志列表
     * @return 插入结果
     */
    int batchInsertPostingLogs(@Param("logList") List<Map<String, Object>> logList);

    /**
     * 根据日志ID查询详情
     *
     * @param logId 日志ID
     * @return 日志详情
     */
    Map<String, Object> selectLogById(@Param("logId") Long logId);

    /**
     * 分页查询过账日志
     *
     * @param param 查询参数
     * @return 日志列表
     */
    List<Map<String, Object>> selectLogsByPage(@Param("param") Map<String, Object> param);

    /**
     * 查询分页总数
     *
     * @param param 查询参数
     * @return 总数
     */
    Long selectLogsCountByPage(@Param("param") Map<String, Object> param);
}

