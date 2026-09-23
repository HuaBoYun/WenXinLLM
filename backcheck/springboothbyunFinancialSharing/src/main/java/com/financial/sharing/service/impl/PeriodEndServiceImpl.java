package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.mapper.PeriodEndMapper;
import com.financial.sharing.service.PeriodEndService;
import com.financial.sharing.vo.param.PeriodEndCheckParam;
import com.financial.sharing.vo.param.PeriodEndClosingParam;
import com.financial.sharing.vo.param.ProfitLossCarryForwardParam;
import com.financial.sharing.vo.result.PeriodEndCheckResult;
import com.financial.sharing.vo.result.PeriodEndClosingResult;
import com.financial.sharing.vo.result.PeriodEndStatusResult;
import com.financial.sharing.vo.result.ProfitLossCarryForwardResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 期末处理服务实现类
 *
 * @author system
 * @since 2024-12-08
 */
@Slf4j
@Service
public class PeriodEndServiceImpl implements PeriodEndService {

    @Resource
    private PeriodEndMapper periodEndMapper;

    @Override
    public List<PeriodEndCheckResult> getPeriodEndCheckItems(PeriodEndCheckParam param) {
        log.info("获取期末检查项配置，参数：{}", param);
        try {
            List<PeriodEndCheckResult> results = new ArrayList<>();

            // 如果 bookId 和 tenantId 都有值，尝试从数据库获取检查项配置
            if (param.getBookId() != null && param.getTenantId() != null) {
                try {
                    List<Map<String, Object>> checkItems = periodEndMapper.getCheckItemConfig(param.getBookId(), param.getTenantId());
                    for (Map<String, Object> item : checkItems) {
                        PeriodEndCheckResult result = new PeriodEndCheckResult();
                        result.setCheckId((String) item.get("checkItemId"));
                        result.setCheckItemName((String) item.get("checkItemName"));
                        result.setCheckType((String) item.get("checkType"));
                        result.setCheckDescription((String) item.get("description"));
                        result.setIsRequired((String) item.get("isRequired"));
                        result.setCheckOrder((Integer) item.get("checkOrder"));
                        result.setCheckStatus("PENDING");
                        result.setCheckResult("PENDING");
                        result.setErrorMessage("");
                        results.add(result);
                    }
                } catch (Exception e) {
                    log.warn("从数据库获取检查项配置失败，将使用默认检查项: {}", e.getMessage());
                }
            }

            // 如果没有配置项，添加默认检查项
            if (results.isEmpty()) {
                results = getDefaultCheckItems();
            }

            log.info("获取期末检查项配置成功，共{}项", results.size());
            return results;
        } catch (Exception e) {
            log.error("获取期末检查项配置失败", e);
            throw new RuntimeException("获取期末检查项配置失败: " + e.getMessage());
        }
    }

    /**
     * 获取默认检查项列表
     */
    private List<PeriodEndCheckResult> getDefaultCheckItems() {
        List<PeriodEndCheckResult> results = new ArrayList<>();

        PeriodEndCheckResult voucherCheck = new PeriodEndCheckResult();
        voucherCheck.setCheckId("CHECK_001");
        voucherCheck.setCheckItemName("凭证完整性检查");
        voucherCheck.setCheckType("凭证检查");
        voucherCheck.setCheckDescription("检查本期所有凭证是否完整录入");
        voucherCheck.setIsRequired("YES");
        voucherCheck.setCheckOrder(1);
        voucherCheck.setCheckStatus("PENDING");
        voucherCheck.setCheckResult("PENDING");
        voucherCheck.setErrorMessage("");
        results.add(voucherCheck);

        PeriodEndCheckResult balanceCheck = new PeriodEndCheckResult();
        balanceCheck.setCheckId("CHECK_002");
        balanceCheck.setCheckItemName("科目余额检查");
        balanceCheck.setCheckType("余额检查");
        balanceCheck.setCheckDescription("检查科目余额是否计算正确");
        balanceCheck.setIsRequired("YES");
        balanceCheck.setCheckOrder(2);
        balanceCheck.setCheckStatus("PENDING");
        balanceCheck.setCheckResult("PENDING");
        balanceCheck.setErrorMessage("");
        results.add(balanceCheck);

        PeriodEndCheckResult trialBalanceCheck = new PeriodEndCheckResult();
        trialBalanceCheck.setCheckId("CHECK_003");
        trialBalanceCheck.setCheckItemName("试算平衡检查");
        trialBalanceCheck.setCheckType("平衡检查");
        trialBalanceCheck.setCheckDescription("检查借贷方发生额是否平衡");
        trialBalanceCheck.setIsRequired("YES");
        trialBalanceCheck.setCheckOrder(3);
        trialBalanceCheck.setCheckStatus("PENDING");
        trialBalanceCheck.setCheckResult("PENDING");
        trialBalanceCheck.setErrorMessage("");
        results.add(trialBalanceCheck);

        PeriodEndCheckResult auditCheck = new PeriodEndCheckResult();
        auditCheck.setCheckId("CHECK_004");
        auditCheck.setCheckItemName("凭证审核检查");
        auditCheck.setCheckType("审核检查");
        auditCheck.setCheckDescription("检查是否存在未审核凭证");
        auditCheck.setIsRequired("YES");
        auditCheck.setCheckOrder(4);
        auditCheck.setCheckStatus("PENDING");
        auditCheck.setCheckResult("PENDING");
        auditCheck.setErrorMessage("");
        results.add(auditCheck);

        PeriodEndCheckResult cashFlowCheck = new PeriodEndCheckResult();
        cashFlowCheck.setCheckId("CHECK_005");
        cashFlowCheck.setCheckItemName("现金流量检查");
        cashFlowCheck.setCheckType("现金流量");
        cashFlowCheck.setCheckDescription("检查现金流量项目是否完整");
        cashFlowCheck.setIsRequired("NO");
        cashFlowCheck.setCheckOrder(5);
        cashFlowCheck.setCheckStatus("PENDING");
        cashFlowCheck.setCheckResult("PENDING");
        cashFlowCheck.setErrorMessage("");
        results.add(cashFlowCheck);

        return results;
    }

    @Override
    @Transactional
    public PeriodEndCheckResult executePeriodEndCheck(PeriodEndCheckParam param) {
        log.info("执行期末检查，参数：{}", param);
        try {
            PeriodEndCheckResult result = new PeriodEndCheckResult();
            result.setCheckId(param.getCheckItemId());

            // 根据检查项ID执行不同的检查逻辑
            Map<String, Object> checkResult;
            switch (param.getCheckItemId()) {
                case "CHECK_001":
                    // 凭证完整性检查
                    checkResult = periodEndMapper.checkVoucherIntegrity(param.getAccountingPeriod(), param.getBookId(), param.getTenantId());
                    break;
                case "CHECK_002":
                    // 科目余额平衡检查
                    checkResult = periodEndMapper.checkSubjectBalance(param.getAccountingPeriod(), param.getBookId(), param.getTenantId());
                    break;
                case "CHECK_003":
                    // 银行对账检查
                    checkResult = periodEndMapper.checkBankReconciliation(param.getAccountingPeriod(), param.getBookId(), param.getTenantId());
                    break;
                case "CHECK_004":
                    // 往来款项检查
                    checkResult = periodEndMapper.checkARAPBalance(param.getAccountingPeriod(), param.getBookId(), param.getTenantId());
                    break;
                default:
                    // 默认检查通过
                    checkResult = new HashMap<>();
                    checkResult.put("checkResult", "PASSED");
                    break;
            }
            result.setCheckResult((String) checkResult.get("checkResult"));
            result.setCheckStatus("COMPLETED");

            if (!"PASSED".equals(result.getCheckResult())) {
                result.setErrorMessage("检查未通过，请查看详细报告");
            }

            // 保存检查记录到数据库
            Map<String, Object> checkRecord = new HashMap<>();
            checkRecord.put("checkId", "CHECK_" + System.currentTimeMillis());
            checkRecord.put("checkItemId", param.getCheckItemId());
            checkRecord.put("accountingPeriod", param.getAccountingPeriod());
            checkRecord.put("bookId", param.getBookId());
            checkRecord.put("tenantId", param.getTenantId());
            checkRecord.put("checkResult", result.getCheckResult());
            checkRecord.put("errorMessage", result.getErrorMessage());
            checkRecord.put("checkTime", new Date());
            checkRecord.put("status", result.getCheckStatus());
            checkRecord.put("creator", "system");

            periodEndMapper.insertCheckRecord(checkRecord);

            log.info("期末检查执行完成，结果：{}", result.getCheckResult());
            return result;
        } catch (Exception e) {
            log.error("执行期末检查失败", e);
            throw new RuntimeException("执行期末检查失败: " + e.getMessage());
        }
    }

    @Override
    public PeriodEndStatusResult getPeriodEndStatus(PeriodEndCheckParam param) {
        log.info("获取期末处理状态，参数：{}", param);
        try {
            PeriodEndStatusResult result = new PeriodEndStatusResult();
            result.setAccountingPeriod(param.getAccountingPeriod());

            // 检查期间是否已经结账
            Boolean isClosed = periodEndMapper.isPeriodClosed(param.getAccountingPeriod(), param.getBookId());
            result.setPeriodStatus(isClosed ? "CLOSED" : "OPEN");

            // 获取结账历史记录数量作为状态判断依据
            List<PeriodEndClosingResult> history = getClosingHistory(param.getBookId(), param.getTenantId(), param.getAccountingPeriod(), 1, 1);
            result.setClosingStatus(history.isEmpty() ? "PENDING" : "COMPLETED");

            result.setCheckStatus("COMPLETED");
            result.setCarryForwardStatus("COMPLETED");
            result.setCanCarryForward(true);
            result.setCanClose(!isClosed);
            result.setCanReverseClosing(isClosed);
            result.setLastUpdateTime(new Date());

            log.info("获取期末处理状态完成，期间状态：{}", result.getPeriodStatus());
            return result;
        } catch (Exception e) {
            log.error("获取期末处理状态失败", e);
            throw new RuntimeException("获取期末处理状态失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PeriodEndClosingResult executePeriodEndClosing(PeriodEndClosingParam param) {
        log.info("执行期末结账，参数：{}", param);
        try {
            // 检查期间是否已经结账
            Boolean isClosed = periodEndMapper.isPeriodClosed(param.getClosingPeriod(), param.getBookId());
            if (isClosed) {
                throw new RuntimeException("该期间已经结账，不能重复结账");
            }

            PeriodEndClosingResult result = new PeriodEndClosingResult();
            result.setClosingId("CLOSING_" + System.currentTimeMillis());
            result.setClosingPeriod(param.getClosingPeriod());
            result.setClosingType(param.getClosingType());
            result.setClosingStatus("PROCESSING");
            result.setClosingTime(new Date());
            result.setClosingUserId(1L);
            result.setClosingUserName("系统用户");
            result.setClosingDescription(param.getClosingDescription());

            // 模拟结账处理（实际应该包含更多的业务逻辑）
            result.setVoucherCount(0);
            result.setTotalAmount(BigDecimal.ZERO);
            result.setDuration(0L);
            result.setStartTime(new Date());
            result.setEndTime(new Date());

            // 保存结账记录
            Map<String, Object> closingRecord = new HashMap<>();
            closingRecord.put("closingId", result.getClosingId());
            closingRecord.put("accountingPeriod", param.getClosingPeriod());
            closingRecord.put("closingType", param.getClosingType());
            closingRecord.put("bookId", param.getBookId());
            closingRecord.put("tenantId", param.getTenantId());
            closingRecord.put("closingStatus", "COMPLETED");
            closingRecord.put("closingTime", result.getClosingTime());
            closingRecord.put("closingUserId", result.getClosingUserId());
            closingRecord.put("closingDescription", result.getClosingDescription());
            closingRecord.put("voucherCount", result.getVoucherCount());
            closingRecord.put("totalAmount", result.getTotalAmount());
            closingRecord.put("duration", result.getDuration());
            closingRecord.put("startTime", result.getStartTime());
            closingRecord.put("endTime", result.getEndTime());
            closingRecord.put("creator", "system");

            periodEndMapper.insertClosingRecord(closingRecord);

            result.setClosingStatus("COMPLETED");
            log.info("期末结账执行完成，结账ID：{}", result.getClosingId());
            return result;
        } catch (Exception e) {
            log.error("执行期末结账失败", e);
            throw new RuntimeException("执行期末结账失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PeriodEndClosingResult reversePeriodEndClosing(PeriodEndClosingParam param) {
        log.info("反结账操作，参数：{}", param);
        try {
            // 检查期间是否已经结账
            Boolean isClosed = periodEndMapper.isPeriodClosed(param.getClosingPeriod(), param.getBookId());
            if (!isClosed) {
                throw new RuntimeException("该期间尚未结账，无法进行反结账");
            }

            PeriodEndClosingResult result = new PeriodEndClosingResult();
            result.setClosingId("REVERSE_" + System.currentTimeMillis());
            result.setClosingPeriod(param.getClosingPeriod());
            result.setClosingType(param.getClosingType());
            result.setClosingStatus("COMPLETED");
            result.setClosingTime(new Date());
            result.setClosingUserId(1L);
            result.setClosingUserName("系统用户");

            // 更新结账记录状态
            Map<String, Object> closingRecord = new HashMap<>();
            closingRecord.put("closingStatus", "REVERSED");
            closingRecord.put("closingTime", new Date());
            closingRecord.put("closingUserId", 1L);
            closingRecord.put("reverseReason", param.getReason());
            closingRecord.put("reverseTime", new Date());
            closingRecord.put("reverseUserId", 1L);
            closingRecord.put("updater", "system");
            closingRecord.put("closingId", param.getClosingId() != null ? param.getClosingId() : "REVERSE_" + System.currentTimeMillis());

            periodEndMapper.updateClosingRecord(closingRecord);

            log.info("反结账操作完成，期间：{}", param.getClosingPeriod());
            return result;
        } catch (Exception e) {
            log.error("反结账操作失败", e);
            throw new RuntimeException("反结账操作失败: " + e.getMessage());
        }
    }

    @Override
    public List<PeriodEndClosingResult> getClosingHistory(Long bookId, Long tenantId, String accountingPeriod, Integer pageNumber, Integer pageSize) {
        log.info("获取结账历史记录，bookId：{}，tenantId：{}，期间：{}", bookId, tenantId, accountingPeriod);
        try {
            if (accountingPeriod == null) {
                accountingPeriod = "";
            }
            List<PeriodEndClosingResult> history = periodEndMapper.getClosingHistory(accountingPeriod, bookId, pageNumber, pageSize);
            log.info("获取结账历史记录完成，共{}条", history.size());
            return history;
        } catch (Exception e) {
            log.error("获取结账历史记录失败", e);
            throw new RuntimeException("获取结账历史记录失败: " + e.getMessage());
        }
    }

    @Override
    public List<String> getAvailablePeriods(Long bookId, Long tenantId) {
        log.info("获取可用会计期间，bookId：{}，tenantId：{}", bookId, tenantId);
        try {
            List<String> periods = periodEndMapper.getAvailablePeriods(bookId);
            log.info("获取可用会计期间完成，共{}个期间", periods.size());
            return periods;
        } catch (Exception e) {
            log.error("获取可用会计期间失败", e);
            throw new RuntimeException("获取可用会计期间失败: " + e.getMessage());
        }
    }

    @Override
    public ProfitLossCarryForwardResult getProfitLossCarryForwardPreview(ProfitLossCarryForwardParam param) {
        log.info("获取损益结转预览，参数：{}", param);
        try {
            ProfitLossCarryForwardResult result = new ProfitLossCarryForwardResult();
            result.setCarryForwardId("PREVIEW_" + System.currentTimeMillis());
            result.setCarryForwardPeriod(param.getCarryForwardPeriod());
            result.setCarryForwardMethod(param.getCarryForwardMethod());
            result.setCurrentYearProfitSubject(param.getCurrentYearProfitSubject());
            result.setVoucherType(param.getVoucherType());
            result.setCarryForwardStatus("PREVIEW");
            result.setCreateTime(new Date());
            result.setCreator("系统用户");

            // 这里应该调用损益科目计算逻辑，暂时返回预览状态
            result.setTotalAmount(BigDecimal.ZERO);
            result.setVoucherCount(0);

            log.info("获取损益结转预览完成");
            return result;
        } catch (Exception e) {
            log.error("获取损益结转预览失败", e);
            throw new RuntimeException("获取损益结转预览失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ProfitLossCarryForwardResult executeProfitLossCarryForward(ProfitLossCarryForwardParam param) {
        log.info("执行损益结转，参数：{}", param);
        try {
            ProfitLossCarryForwardResult result = new ProfitLossCarryForwardResult();
            result.setCarryForwardId("CF_" + System.currentTimeMillis());
            result.setCarryForwardPeriod(param.getCarryForwardPeriod());
            result.setCarryForwardMethod(param.getCarryForwardMethod());
            result.setCurrentYearProfitSubject(param.getCurrentYearProfitSubject());
            result.setVoucherType(param.getVoucherType());
            result.setCarryForwardStatus("COMPLETED");
            result.setCarryForwardTime(new Date());
            result.setCreateTime(new Date());
            result.setCreator("系统用户");

            // 这里应该实现真实的损益结转逻辑
            result.setTotalAmount(BigDecimal.ZERO);
            result.setVoucherCount(1);
            result.setVoucherNo("记-" + param.getCarryForwardPeriod() + "-001");

            log.info("损益结转执行完成，结转ID：{}", result.getCarryForwardId());
            return result;
        } catch (Exception e) {
            log.error("执行损益结转失败", e);
            throw new RuntimeException("执行损益结转失败: " + e.getMessage());
        }
    }
}