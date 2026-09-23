package com.financial.sharing.oracle.mapper;

import com.financial.sharing.dto.BalanceRecalculateParam;
import com.financial.sharing.dto.GeneralLedgerQueryParam;
import com.financial.sharing.oracle.entity.SubjectBalanceEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 科目余额Mapper接口
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
public interface SubjectBalanceMapper {

    /**
     * 分页查询科目余额
     *
     * @param param 查询参数
     * @return 科目余额列表
     */
    List<SubjectBalanceEntity> selectBalancePage(@Param("param") GeneralLedgerQueryParam param);

    /**
     * 查询科目余额总数
     *
     * @param param 查询参数
     * @return 总记录数
     */
    Long selectBalanceCount(@Param("param") GeneralLedgerQueryParam param);

    /**
     * 根据科目编码和期间查询余额详情
     *
     * @param subjectCode 科目编码
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 科目余额详情
     */
    SubjectBalanceEntity selectSubjectBalanceDetail(
            @Param("subjectCode") String subjectCode,
            @Param("period") String period,
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId);

    /**
     * 计算科目余额
     *
     * @param subjectCode 科目编码
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 余额计算结果
     */
    Map<String, Object> calculateSubjectBalance(
            @Param("subjectCode") String subjectCode,
            @Param("period") String period,
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId);

    /**
     * 获取科目期初余额
     *
     * @param subjectId 科目ID
     * @param period 当前期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 期初余额
     */
    BigDecimal getBeginningBalance(
            @Param("subjectId") Long subjectId,
            @Param("period") String period,
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId);

    /**
     * 获取科目本期发生额
     *
     * @param subjectCode 科目编码
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 发生额（借方、贷方）
     */
    Map<String, BigDecimal> getPeriodAmount(
            @Param("subjectCode") String subjectCode,
            @Param("period") String period,
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId);

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
    int batchUpsertBalance(@Param("balances") List<SubjectBalanceEntity> balances);

    /**
     * 重新计算科目余额
     *
     * @param param 重新计算参数
     * @return 处理行数
     */
    int recalculateBalance(@Param("param") BalanceRecalculateParam param);

    /**
     * 删除指定期间的科目余额
     *
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 删除行数
     */
    int deleteBalanceByPeriod(
            @Param("period") String period,
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId);

    /**
     * 重新计算所有科目余额
     *
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 处理科目数
     */
    int recalculateAllBalance(
            @Param("period") String period,
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId);

    /**
     * 查询余额统计信息
     *
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计信息
     */
    Map<String, Object> selectBalanceStatistics(
            @Param("period") String period,
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId);
}