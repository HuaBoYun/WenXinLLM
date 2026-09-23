package com.financial.sharing.oracle.service;

import com.hbfk.entity.TblStaffUtil;

import java.util.List;
import java.util.Map;

/**
 * 凭证过账日志服务接口
 *
 * @author system
 * @since 2026-01-03
 */
public interface VoucherPostingLogService {

    /**
     * 根据凭证ID查询过账日志列表
     *
     * @param voucherId 凭证ID
     * @return 过账日志列表
     */
    List<Map<String, Object>> getLogsByVoucherId(Long voucherId);

    /**
     * 查询过账日志总数
     *
     * @param voucherId 凭证ID
     * @return 日志总数
     */
    Long getLogsCountByVoucherId(Long voucherId);

    /**
     * 记录过账日志
     *
     * @param voucherId 凭证ID
     * @param voucherNo 凭证号
     * @param operationType 操作类型：POST-过账, UNPOST-反过账
     * @param operationStatus 操作状态：SUCCESS-成功, FAILED-失败
     * @param errorMessage 错误信息
     * @param remark 备注
     * @param loginStaff 操作人
     * @return 是否成功
     */
    boolean recordPostingLog(Long voucherId, String voucherNo, String operationType,
                            String operationStatus, String errorMessage, String remark,
                            TblStaffUtil loginStaff);

    /**
     * 批量记录过账日志
     *
     * @param logList 日志列表
     * @return 是否成功
     */
    boolean batchRecordPostingLogs(List<Map<String, Object>> logList);

    /**
     * 根据日志ID查询详情
     *
     * @param logId 日志ID
     * @return 日志详情
     */
    Map<String, Object> getLogById(Long logId);

    /**
     * 分页查询过账日志
     *
     * @param param 查询参数
     * @return 分页结果
     */
    Map<String, Object> getLogsByPage(Map<String, Object> param);
}

