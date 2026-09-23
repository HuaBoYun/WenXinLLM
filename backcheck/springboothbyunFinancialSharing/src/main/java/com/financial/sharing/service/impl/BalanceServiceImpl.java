package com.financial.sharing.service.impl;

import com.financial.sharing.dto.BalanceRecalculateParam;
import com.financial.sharing.dto.GeneralLedgerQueryParam;
import com.financial.sharing.oracle.entity.SubjectBalanceEntity;
import com.financial.sharing.oracle.mapper.SubjectBalanceMapper;
import com.financial.sharing.service.BalanceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 科目余额服务实现类
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Slf4j
@Service
public class BalanceServiceImpl implements BalanceService {

    @Resource
    private SubjectBalanceMapper subjectBalanceMapper;

    @Override
    public PageInfo<SubjectBalanceEntity> getGeneralLedgerBalancePage(GeneralLedgerQueryParam param) {
        log.info("查询科目余额，参数：{}", param);

        try {
            // 设置分页
            if (param.getPageNo() != null && param.getPageSize() != null) {
                PageHelper.startPage(param.getPageNo(), param.getPageSize());
            }

            // 查询数据
            List<SubjectBalanceEntity> balanceList = subjectBalanceMapper.selectBalancePage(param);

            return new PageInfo<>(balanceList);
        } catch (Exception e) {
            log.error("查询科目余额失败", e);
            throw new RuntimeException("查询科目余额失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> recalculateSubjectBalance(BalanceRecalculateParam param) {
        log.info("重新计算科目余额，参数：{}", param);

        try {
            // 创建任务信息
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", "RECALC" + System.currentTimeMillis());
            result.put("period", param.getPeriod());
            result.put("status", "PROCESSING");
            result.put("startTime", new Date());

            int processedCount;

            if (param.getSubjectCode() != null && !param.getSubjectCode().isEmpty()) {
                // 重新计算指定科目
                processedCount = subjectBalanceMapper.recalculateBalance(param);
            } else {
                // 重新计算所有科目
                processedCount = subjectBalanceMapper.recalculateAllBalance(
                        param.getPeriod(), param.getBookId(), param.getTenantId());
            }

            result.put("subjectCount", processedCount);
            result.put("processedCount", processedCount);
            result.put("endTime", new Date());
            result.put("status", "COMPLETED");

            return result;
        } catch (Exception e) {
            log.error("重新计算科目余额失败", e);
            throw new RuntimeException("重新计算科目余额失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getSubjectBalanceDetail(String subjectCode, String period, Long bookId, Long tenantId) {
        log.info("获取科目余额详情，科目：{}，期间：{}", subjectCode, period);

        try {
            SubjectBalanceEntity balance = subjectBalanceMapper.selectSubjectBalanceDetail(
                    subjectCode, period, bookId, tenantId);

            Map<String, Object> detail = new HashMap<>();
            if (balance != null) {
                detail.put("subjectCode", balance.getSubjectCode());
                detail.put("subjectName", balance.getSubjectName());
                detail.put("period", balance.getAccountingPeriod());
                detail.put("beginningBalance", balance.getBeginningBalance());
                detail.put("currentDebit", balance.getPeriodDebitAmount());
                detail.put("currentCredit", balance.getPeriodCreditAmount());
                detail.put("endingBalance", balance.getEndingBalance());
                detail.put("balanceDirection", balance.getBalanceDirection());
                detail.put("lastUpdateTime", balance.getUpdatedTime());
            } else {
                // 如果没有找到余额，尝试计算
                Map<String, Object> calculated = calculateSubjectBalance(subjectCode, period, bookId, tenantId);
                if (calculated != null) {
                    detail.putAll(calculated);
                }
            }
            return detail;
        } catch (Exception e) {
            log.error("获取科目余额详情失败", e);
            throw new RuntimeException("获取科目余额详情失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> calculateSubjectBalance(String subjectCode, String period, Long bookId, Long tenantId) {
        log.info("计算科目余额，科目：{}，期间：{}", subjectCode, period);

        try {
            return subjectBalanceMapper.calculateSubjectBalance(subjectCode, period, bookId, tenantId);
        } catch (Exception e) {
            log.error("计算科目余额失败", e);
            throw new RuntimeException("计算科目余额失败: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal getBeginningBalance(Long subjectId, String period, Long bookId, Long tenantId) {
        log.info("获取科目期初余额，科目ID：{}，期间：{}", subjectId, period);

        try {
            return subjectBalanceMapper.getBeginningBalance(subjectId, period, bookId, tenantId);
        } catch (Exception e) {
            log.error("获取科目期初余额失败", e);
            throw new RuntimeException("获取科目期初余额失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, BigDecimal> getPeriodAmount(String subjectCode, String period, Long bookId, Long tenantId) {
        log.info("获取科目本期发生额，科目：{}，期间：{}", subjectCode, period);

        try {
            return subjectBalanceMapper.getPeriodAmount(subjectCode, period, bookId, tenantId);
        } catch (Exception e) {
            log.error("获取科目本期发生额失败", e);
            throw new RuntimeException("获取科目本期发生额失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int upsertBalance(SubjectBalanceEntity balance) {
        log.info("插入或更新科目余额，科目：{}", balance.getSubjectCode());

        try {
            return subjectBalanceMapper.upsertBalance(balance);
        } catch (Exception e) {
            log.error("插入或更新科目余额失败", e);
            throw new RuntimeException("插入或更新科目余额失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpsertBalance(List<SubjectBalanceEntity> balances) {
        log.info("批量插入或更新科目余额，数量：{}", balances.size());

        try {
            return subjectBalanceMapper.batchUpsertBalance(balances);
        } catch (Exception e) {
            log.error("批量插入或更新科目余额失败", e);
            throw new RuntimeException("批量插入或更新科目余额失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBalanceByPeriod(String period, Long bookId, Long tenantId) {
        log.info("删除指定期间的科目余额，期间：{}", period);

        try {
            return subjectBalanceMapper.deleteBalanceByPeriod(period, bookId, tenantId);
        } catch (Exception e) {
            log.error("删除指定期间的科目余额失败", e);
            throw new RuntimeException("删除指定期间的科目余额失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int recalculateAllBalance(String period, Long bookId, Long tenantId) {
        log.info("重新计算所有科目余额，期间：{}", period);

        try {
            return subjectBalanceMapper.recalculateAllBalance(period, bookId, tenantId);
        } catch (Exception e) {
            log.error("重新计算所有科目余额失败", e);
            throw new RuntimeException("重新计算所有科目余额失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> selectBalanceStatistics(String period, Long bookId, Long tenantId) {
        log.info("查询余额统计信息，期间：{}", period);

        try {
            return subjectBalanceMapper.selectBalanceStatistics(period, bookId, tenantId);
        } catch (Exception e) {
            log.error("查询余额统计信息失败", e);
            throw new RuntimeException("查询余额统计信息失败: " + e.getMessage(), e);
        }
    }
}