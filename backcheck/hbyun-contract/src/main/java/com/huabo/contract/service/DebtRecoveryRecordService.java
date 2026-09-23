package com.huabo.contract.service;

import com.huabo.contract.entity.DebtRecoveryRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 债权回收登记记录服务接口
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface DebtRecoveryRecordService {

    /**
     * 根据债权ID查询回收记录列表
     *
     * @param debtId 债权ID
     * @return 回收记录列表
     */
    List<DebtRecoveryRecord> getRecoveryRecordsByDebtId(Long debtId);

    /**
     * 保存回收记录
     *
     * @param recoveryRecord 回收记录
     * @return 保存结果
     */
    boolean saveRecoveryRecord(DebtRecoveryRecord recoveryRecord);

    /**
     * 批量保存回收记录
     *
     * @param recoveryRecords 回收记录列表
     * @return 保存结果
     */
    boolean batchSaveRecoveryRecords(List<DebtRecoveryRecord> recoveryRecords);

    /**
     * 根据ID删除回收记录
     *
     * @param id 主键ID
     * @return 删除结果
     */
    boolean deleteRecoveryRecord(Long id);

    /**
     * 根据ID获取回收记录详情
     *
     * @param id 主键ID
     * @return 回收记录详情
     */
    DebtRecoveryRecord getRecoveryRecordById(Long id);

    /**
     * 根据回收人员ID查询回收记录列表
     *
     * @param recoveryPersonId 回收人员ID
     * @return 回收记录列表
     */
    List<DebtRecoveryRecord> getRecoveryRecordsByPersonId(Long recoveryPersonId);

    /**
     * 根据回收方式查询回收记录列表
     *
     * @param recoveryMethod 回收方式
     * @return 回收记录列表
     */
    List<DebtRecoveryRecord> getRecoveryRecordsByMethod(Integer recoveryMethod);

    /**
     * 根据债权ID计算总回收金额
     *
     * @param debtId 债权ID
     * @return 总回收金额
     */
    BigDecimal getTotalRecoveryAmountByDebtId(Long debtId);

    /**
     * 获取债权回收统计信息
     *
     * @param debtId 债权ID
     * @return 统计信息
     */
    Map<String, Object> getRecoverySummary(Long debtId);

    /**
     * 获取回收数据（包含记录和统计）
     *
     * @param debtId 债权ID
     * @return 回收数据
     */
    Map<String, Object> getRecoveryData(Long debtId);
}
