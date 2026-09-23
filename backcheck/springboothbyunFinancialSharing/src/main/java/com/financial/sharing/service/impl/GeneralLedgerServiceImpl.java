package com.financial.sharing.service.impl;

import com.financial.sharing.dto.GeneralLedgerQueryParam;
import com.financial.sharing.oracle.entity.GeneralLedgerEntity;
import com.financial.sharing.oracle.mapper.GeneralLedgerMapper;
import com.financial.sharing.service.GeneralLedgerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 总账服务实现类
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Slf4j
@Service
public class GeneralLedgerServiceImpl implements GeneralLedgerService {

    @Resource
    private GeneralLedgerMapper generalLedgerMapper;

    @Override
    public PageInfo<GeneralLedgerEntity> getDetailLedgerPage(GeneralLedgerQueryParam param) {
        log.info("查询总账明细，参数：{}", param);

        try {
            // 设置分页
            if (param.getPageNo() != null && param.getPageSize() != null) {
                PageHelper.startPage(param.getPageNo(), param.getPageSize());
            }

            // 查询数据
            List<GeneralLedgerEntity> ledgerList = generalLedgerMapper.selectLedgerPage(param);

            return new PageInfo<>(ledgerList);
        } catch (Exception e) {
            log.error("查询总账明细失败", e);
            throw new RuntimeException("查询总账明细失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getSubjectDetailLedger(String subjectCode, String period, Long bookId, Long tenantId) {
        log.info("获取科目明细账，科目：{}，期间：{}", subjectCode, period);

        try {
            // 查询明细账数据
            List<GeneralLedgerEntity> details = generalLedgerMapper.selectSubjectDetailLedger(
                    subjectCode, period, bookId, tenantId);

            // 查询总账汇总数据
            Map<String, Object> summary = generalLedgerMapper.selectSubjectLedgerSummary(
                    subjectCode, period, bookId, tenantId);

            Map<String, Object> result = new HashMap<>();
            result.put("subjectCode", subjectCode);
            result.put("subjectName", summary != null ? summary.get("subjectName") : "");
            result.put("period", period);
            result.put("openingBalance", summary != null ? summary.get("openingBalance") : 0);
            result.put("totalDebit", summary != null ? summary.get("totalDebit") : 0);
            result.put("totalCredit", summary != null ? summary.get("totalCredit") : 0);
            result.put("closingBalance", summary != null ? summary.get("closingBalance") : 0);
            result.put("details", details);

            return result;
        } catch (Exception e) {
            log.error("获取科目明细账失败", e);
            throw new RuntimeException("获取科目明细账失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> exportDetailLedger(GeneralLedgerQueryParam param) {
        log.info("导出明细账，参数：{}", param);

        try {
            // 查询数据（不分页）
            param.setPageSize(null);
            param.setPageNo(null);
            List<GeneralLedgerEntity> data = generalLedgerMapper.selectLedgerPage(param);

            // 创建导出任务
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", "EXPORT" + System.currentTimeMillis());
            result.put("fileName", "明细账导出_" + new Date() + ".xlsx");
            result.put("status", "PROCESSING");
            result.put("dataCount", data.size());
            result.put("estimatedTime", data.size() > 1000 ? "60秒" : "30秒");

            // TODO: 实际的导出逻辑（异步处理）

            return result;
        } catch (Exception e) {
            log.error("导出明细账失败", e);
            throw new RuntimeException("导出明细账失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getSubjectLedgerQuery(GeneralLedgerQueryParam param) {
        log.info("查询科目总账，参数：{}", param);

        try {
            List<Map<String, Object>> ledgers = new ArrayList<>();

            // 如果指定了科目编码，查询单个科目
            if (param.getSubjectCode() != null && !param.getSubjectCode().isEmpty()) {
                Map<String, Object> summary = generalLedgerMapper.selectSubjectLedgerSummary(
                        param.getSubjectCode(), param.getAccountingPeriod(), param.getBookId(), param.getTenantId());
                if (summary != null) {
                    ledgers.add(summary);
                }
            } else {
                // 查询所有科目的汇总数据
                param.setPageSize(100); // 限制查询数量
                param.setPageNo(1);
                List<GeneralLedgerEntity> ledgerList = generalLedgerMapper.selectLedgerPage(param);

                // 按科目分组汇总
                Map<String, Map<String, Object>> subjectMap = new HashMap<>();
                for (GeneralLedgerEntity ledger : ledgerList) {
                    String key = ledger.getAccountCode();
                    if (!subjectMap.containsKey(key)) {
                        Map<String, Object> subjectData = new HashMap<>();
                        subjectData.put("subjectCode", ledger.getAccountCode());
                        subjectData.put("subjectName", ledger.getAccountName());
                        subjectData.put("beginningBalance", 0);
                        subjectData.put("debitTotal", 0);
                        subjectData.put("creditTotal", 0);
                        subjectData.put("endingBalance", 0);
                        subjectMap.put(key, subjectData);
                    }

                    Map<String, Object> subjectData = subjectMap.get(key);
                    BigDecimal debitTotal = (BigDecimal) subjectData.get("debitTotal");
                    BigDecimal creditTotal = (BigDecimal) subjectData.get("creditTotal");
                    subjectData.put("debitTotal", debitTotal.add(ledger.getDebitAmount()));
                    subjectData.put("creditTotal", creditTotal.add(ledger.getCreditAmount()));
                }

                ledgers = new ArrayList<>(subjectMap.values());
            }
            return ledgers;
        } catch (Exception e) {
            log.error("查询科目总账失败", e);
            throw new RuntimeException("查询科目总账失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getLedgerQueryStatistics(String period, Long bookId, Long tenantId) {
        log.info("获取总账查询统计，期间：{}", period);

        try {
            return generalLedgerMapper.selectLedgerStatistics(period, bookId, tenantId);
        } catch (Exception e) {
            log.error("获取总账查询统计失败", e);
            throw new RuntimeException("获取总账查询统计失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getMultiColumnLedgerQuery(String subjectCode, String periodRange, Long bookId, Long tenantId) {
        log.info("查询多栏式总账，科目：{}", subjectCode);

        try {
            List<Map<String, Object>> columns = generalLedgerMapper.selectMultiColumnLedger(
                    subjectCode, periodRange, bookId, tenantId);

            Map<String, Object> result = new HashMap<>();
            result.put("subjectCode", subjectCode);
            result.put("periodRange", periodRange);
            result.put("totalAmount", columns.stream().mapToDouble(m -> ((Number) m.get("amount")).doubleValue()).sum());
            result.put("columns", columns);

            return result;
        } catch (Exception e) {
            log.error("查询多栏式总账失败", e);
            throw new RuntimeException("查询多栏式总账失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getAuxiliaryLedgerQuery(String subjectCode, String auxiliaryType, String auxiliaryValue, String period, Long bookId, Long tenantId) {
        log.info("查询辅助核算总账");

        try {
            return generalLedgerMapper.selectAuxiliaryLedger(
                    subjectCode, auxiliaryType, auxiliaryValue, period, bookId, tenantId);
        } catch (Exception e) {
            log.error("查询辅助核算总账失败", e);
            throw new RuntimeException("查询辅助核算总账失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getLedgerSummaryQuery(GeneralLedgerQueryParam param) {
        log.info("查询总账汇总");

        try {
            return generalLedgerMapper.selectLedgerSummary(
                    param.getSubjectLevel(), param.getSubjectType(), param.getAccountingPeriod(),
                    param.getBookId(), param.getTenantId());
        } catch (Exception e) {
            log.error("查询总账汇总失败", e);
            throw new RuntimeException("查询总账汇总失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> exportLedgerQuery(GeneralLedgerQueryParam param) {
        log.info("导出总账查询结果");

        try {
            // 查询数据
            List<Map<String, Object>> data = new ArrayList<>();

            // 根据查询条件获取不同类型的数据
            if (param.getSubjectLevel() != null || param.getSubjectType() != null) {
                Map<String, Object> summary = getLedgerSummaryQuery(param);
                data.add(summary);
            } else {
                data = getSubjectLedgerQuery(param);
            }

            // 创建导出任务
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", "EXPORT" + System.currentTimeMillis());
            result.put("fileName", "总账查询_" + new Date() + ".xlsx");
            result.put("status", "PROCESSING");
            result.put("dataCount", data.size());

            // TODO: 实际的导出逻辑（异步处理）

            return result;
        } catch (Exception e) {
            log.error("导出总账查询结果失败", e);
            throw new RuntimeException("导出总账查询结果失败: " + e.getMessage(), e);
        }
    }

    @Override
    public int generateLedgerFromVoucher(String period, Long bookId, Long tenantId) {
        log.info("从凭证生成总账数据，期间：{}", period);

        try {
            return generalLedgerMapper.generateLedgerFromVoucher(period, bookId, tenantId);
        } catch (Exception e) {
            log.error("从凭证生成总账数据失败", e);
            throw new RuntimeException("从凭证生成总账数据失败: " + e.getMessage(), e);
        }
    }

    @Override
    public int batchInsertLedger(List<GeneralLedgerEntity> ledgers) {
        log.info("批量插入总账数据，数量：{}", ledgers.size());

        try {
            return generalLedgerMapper.batchInsertLedger(ledgers);
        } catch (Exception e) {
            log.error("批量插入总账数据失败", e);
            throw new RuntimeException("批量插入总账数据失败: " + e.getMessage(), e);
        }
    }
}