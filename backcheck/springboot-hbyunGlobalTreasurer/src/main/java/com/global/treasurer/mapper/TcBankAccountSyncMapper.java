package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcBankAccountSync;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 银企直连账户同步Mapper接口
 * 
 * @author AI Assistant
 * @date 2025-09-20
 */
@Mapper
public interface TcBankAccountSyncMapper extends BaseMapper<TcBankAccountSync> {

    /**
     * 根据账户ID查询同步记录
     * 
     * @param accountId 账户ID
     * @return 同步记录列表
     */
    List<TcBankAccountSync> selectByAccountId(@Param("accountId") String accountId);

    /**
     * 根据同步状态查询同步记录
     * 
     * @param syncStatus 同步状态
     * @return 同步记录列表
     */
    List<TcBankAccountSync> selectBySyncStatus(@Param("syncStatus") String syncStatus);

    /**
     * 根据银行代码查询同步记录
     * 
     * @param bankCode 银行代码
     * @return 同步记录列表
     */
    List<TcBankAccountSync> selectByBankCode(@Param("bankCode") String bankCode);

    /**
     * 根据同步类型查询同步记录
     * 
     * @param syncType 同步类型
     * @return 同步记录列表
     */
    List<TcBankAccountSync> selectBySyncType(@Param("syncType") String syncType);

    /**
     * 查询需要重试的同步记录
     * 
     * @return 同步记录列表
     */
    List<TcBankAccountSync> selectNeedRetry();

    /**
     * 查询同步失败的记录
     * 
     * @return 同步记录列表
     */
    List<TcBankAccountSync> selectFailedSync();

    /**
     * 更新同步状态
     * 
     * @param id 记录ID
     * @param syncStatus 同步状态
     * @param responseData 响应数据
     * @param errorCode 错误代码
     * @param errorMessage 错误信息
     * @param updateUser 更新人
     * @return 更新数量
     */
    int updateSyncStatus(@Param("id") String id, @Param("syncStatus") String syncStatus, 
                        @Param("responseData") String responseData, @Param("errorCode") String errorCode, 
                        @Param("errorMessage") String errorMessage, @Param("updateUser") String updateUser);

    /**
     * 增加重试次数
     * 
     * @param id 记录ID
     * @param nextRetryTime 下次重试时间
     * @param updateUser 更新人
     * @return 更新数量
     */
    int increaseRetryCount(@Param("id") String id, @Param("nextRetryTime") String nextRetryTime, @Param("updateUser") String updateUser);

    /**
     * 批量更新同步状态
     * 
     * @param ids 记录ID列表
     * @param syncStatus 同步状态
     * @param updateUser 更新人
     * @return 更新数量
     */
    int batchUpdateSyncStatus(@Param("ids") List<String> ids, @Param("syncStatus") String syncStatus, @Param("updateUser") String updateUser);

    /**
     * 查询同步统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> selectSyncStatistics();

    /**
     * 根据时间范围查询同步记录
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 同步记录列表
     */
    List<TcBankAccountSync> selectByTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询同步成功率统计
     * 
     * @param bankCode 银行代码
     * @param days 统计天数
     * @return 成功率统计
     */
    Map<String, Object> selectSuccessRateStatistics(@Param("bankCode") String bankCode, @Param("days") Integer days);

    /**
     * 多条件查询同步记录
     * 
     * @param params 查询参数
     * @return 同步记录列表
     */
    List<TcBankAccountSync> selectByMultipleConditions(@Param("params") Map<String, Object> params);

    /**
     * 清理历史同步记录
     * 
     * @param days 保留天数
     * @return 清理数量
     */
    int cleanHistoryRecords(@Param("days") Integer days);

    /**
     * 查询最新同步记录
     * 
     * @param accountId 账户ID
     * @param syncType 同步类型
     * @return 同步记录
     */
    TcBankAccountSync selectLatestSync(@Param("accountId") String accountId, @Param("syncType") String syncType);
}
