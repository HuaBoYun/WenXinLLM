package com.financial.sharing.oracle.mapper;

import com.financial.sharing.dto.GeneralLedgerQueryParam;
import com.financial.sharing.oracle.entity.GeneralLedgerEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 总账Mapper接口
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
public interface GeneralLedgerMapper {

    /**
     * 分页查询总账数据
     *
     * @param param 查询参数
     * @return 总账数据列表
     */
    List<GeneralLedgerEntity> selectLedgerPage(@Param("param") GeneralLedgerQueryParam param);

    /**
     * 查询总账数据总数
     *
     * @param param 查询参数
     * @return 总记录数
     */
    Long selectLedgerCount(@Param("param") GeneralLedgerQueryParam param);

    /**
     * 根据科目编码查询明细账
     *
     * @param subjectCode 科目编码
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 明细账数据
     */
    List<GeneralLedgerEntity> selectSubjectDetailLedger(
            @Param("subjectCode") String subjectCode,
            @Param("period") String period,
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId);

    /**
     * 查询科目总账
     *
     * @param subjectCode 科目编码
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 总账汇总数据
     */
    Map<String, Object> selectSubjectLedgerSummary(
            @Param("subjectCode") String subjectCode,
            @Param("period") String period,
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId);

    /**
     * 查询总账统计数据
     *
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计数据
     */
    Map<String, Object> selectLedgerStatistics(
            @Param("period") String period,
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId);

    /**
     * 查询多栏式总账
     *
     * @param subjectCode 主科目编码
     * @param periodRange 期间范围
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 多栏式数据
     */
    List<Map<String, Object>> selectMultiColumnLedger(
            @Param("subjectCode") String subjectCode,
            @Param("periodRange") String periodRange,
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId);

    /**
     * 查询辅助核算总账
     *
     * @param subjectCode 科目编码
     * @param auxiliaryType 辅助核算类型
     * @param auxiliaryValue 辅助核算值
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 辅助核算数据
     */
    List<Map<String, Object>> selectAuxiliaryLedger(
            @Param("subjectCode") String subjectCode,
            @Param("auxiliaryType") String auxiliaryType,
            @Param("auxiliaryValue") String auxiliaryValue,
            @Param("period") String period,
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId);

    /**
     * 查询总账汇总
     *
     * @param subjectLevel 科目级次
     * @param subjectType 科目类型
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 汇总数据
     */
    Map<String, Object> selectLedgerSummary(
            @Param("subjectLevel") String subjectLevel,
            @Param("subjectType") String subjectType,
            @Param("period") String period,
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId);

    /**
     * 插入总账数据
     *
     * @param ledger 总账数据
     * @return 插入行数
     */
    int insertLedger(GeneralLedgerEntity ledger);

    /**
     * 批量插入总账数据
     *
     * @param ledgers 总账数据列表
     * @return 插入行数
     */
    int batchInsertLedger(@Param("ledgers") List<GeneralLedgerEntity> ledgers);

    /**
     * 从凭证生成总账数据
     *
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 生成行数
     */
    int generateLedgerFromVoucher(
            @Param("period") String period,
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId);
}