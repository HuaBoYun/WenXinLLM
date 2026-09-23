package com.financial.sharing.service;

import com.financial.sharing.dto.GeneralLedgerQueryParam;
import com.financial.sharing.oracle.entity.GeneralLedgerEntity;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 总账服务接口
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
public interface GeneralLedgerService {

    /**
     * 分页查询总账明细
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<GeneralLedgerEntity> getDetailLedgerPage(GeneralLedgerQueryParam param);

    /**
     * 获取科目明细账
     *
     * @param subjectCode 科目编码
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 明细账数据
     */
    Map<String, Object> getSubjectDetailLedger(String subjectCode, String period, Long bookId, Long tenantId);

    /**
     * 导出明细账数据
     *
     * @param param 查询参数
     * @return 导出任务信息
     */
    Map<String, Object> exportDetailLedger(GeneralLedgerQueryParam param);

    /**
     * 查询科目总账
     *
     * @param param 查询参数
     * @return 总账数据
     */
    List<Map<String, Object>> getSubjectLedgerQuery(GeneralLedgerQueryParam param);

    /**
     * 获取总账查询统计数据
     *
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计数据
     */
    Map<String, Object> getLedgerQueryStatistics(String period, Long bookId, Long tenantId);

    /**
     * 查询多栏式总账
     *
     * @param subjectCode 科目编码
     * @param periodRange 期间范围
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 多栏式数据
     */
    Map<String, Object> getMultiColumnLedgerQuery(String subjectCode, String periodRange, Long bookId, Long tenantId);

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
    List<Map<String, Object>> getAuxiliaryLedgerQuery(String subjectCode, String auxiliaryType, String auxiliaryValue, String period, Long bookId, Long tenantId);

    /**
     * 查询总账汇总
     *
     * @param param 查询参数
     * @return 汇总数据
     */
    Map<String, Object> getLedgerSummaryQuery(GeneralLedgerQueryParam param);

    /**
     * 导出总账查询结果
     *
     * @param param 查询参数
     * @return 导出任务信息
     */
    Map<String, Object> exportLedgerQuery(GeneralLedgerQueryParam param);

    /**
     * 从凭证生成总账数据
     *
     * @param period 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 生成记录数
     */
    int generateLedgerFromVoucher(String period, Long bookId, Long tenantId);

    /**
     * 批量插入总账数据
     *
     * @param ledgers 总账数据列表
     * @return 插入记录数
     */
    int batchInsertLedger(List<GeneralLedgerEntity> ledgers);
}