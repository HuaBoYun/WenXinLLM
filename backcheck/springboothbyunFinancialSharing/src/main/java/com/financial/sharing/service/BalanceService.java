package com.financial.sharing.service;

import com.financial.sharing.dto.BalanceRecalculateParam;
import com.financial.sharing.dto.GeneralLedgerQueryParam;
import com.financial.sharing.oracle.entity.SubjectBalanceEntity;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 科目余额服务接口
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
public interface BalanceService {

    /**
     * 分页查询科目余额
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<SubjectBalanceEntity> getGeneralLedgerBalancePage(GeneralLedgerQueryParam param);

    /**
     * 重新计算科目余额
     *
     * @param param 重新计算参数
     * @return 任务信息
     */
    Map<String, Object> recalculateSubjectBalance(BalanceRecalculateParam param);

    /**
     * 获取科目余额详情
     *
     * @param subjectCode 科目编码
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 科目余额详情
     */
    Map<String, Object> getSubjectBalanceDetail(String subjectCode, String period, Long bookId, Long tenantId);

    /**
     * 计算科目余额
     *
     * @param subjectCode 科目编码
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 余额计算结果
     */
    Map<String, Object> calculateSubjectBalance(String subjectCode, String period, Long bookId, Long tenantId);

    /**
     * 获取科目期初余额
     *
     * @param subjectId 科目ID
     * @param period 当前期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 期初余额
     */
    BigDecimal getBeginningBalance(Long subjectId, String period, Long bookId, Long tenantId);

    /**
     * 获取科目本期发生额
     *
     * @param subjectCode 科目编码
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 发生额（借方、贷方）
     */
    Map<String, BigDecimal> getPeriodAmount(String subjectCode, String period, Long bookId, Long tenantId);

    /**
     * 插入或更新科目余额
     *
     * @param balance 科目余额
     * @return 更新行数
     */
    int upsertBalance(SubjectBalanceEntity balance);

    /**
     * 批量插入或更新科目余额
     *
     * @param balances 科目余额列表
     * @return 更新行数
     */
    int batchUpsertBalance(List<SubjectBalanceEntity> balances);

    /**
     * 删除指定期间的科目余额
     *
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 删除行数
     */
    int deleteBalanceByPeriod(String period, Long bookId, Long tenantId);

    /**
     * 重新计算所有科目余额
     *
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 处理科目数
     */
    int recalculateAllBalance(String period, Long bookId, Long tenantId);

    /**
     * 查询余额统计信息
     *
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计信息
     */
    Map<String, Object> selectBalanceStatistics(String period, Long bookId, Long tenantId);
}