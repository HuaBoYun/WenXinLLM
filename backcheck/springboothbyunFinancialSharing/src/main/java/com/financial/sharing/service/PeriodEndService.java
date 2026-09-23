package com.financial.sharing.service;

import com.financial.sharing.vo.param.PeriodEndCheckParam;
import com.financial.sharing.vo.param.PeriodEndClosingParam;
import com.financial.sharing.vo.param.ProfitLossCarryForwardParam;
import com.financial.sharing.vo.result.PeriodEndCheckResult;
import com.financial.sharing.vo.result.PeriodEndClosingResult;
import com.financial.sharing.vo.result.PeriodEndStatusResult;
import com.financial.sharing.vo.result.ProfitLossCarryForwardResult;

import java.util.List;

/**
 * 期末处理服务接口
 *
 * @author system
 * @since 2024-12-08
 */
public interface PeriodEndService {

    /**
     * 获取期末处理检查项
     *
     * @param param 检查参数
     * @return 检查项列表
     */
    List<PeriodEndCheckResult> getPeriodEndCheckItems(PeriodEndCheckParam param);

    /**
     * 执行期末检查
     *
     * @param param 检查参数
     * @return 检查结果
     */
    PeriodEndCheckResult executePeriodEndCheck(PeriodEndCheckParam param);

    /**
     * 获取期末处理状态
     *
     * @param param 检查参数
     * @return 处理状态
     */
    PeriodEndStatusResult getPeriodEndStatus(PeriodEndCheckParam param);

    /**
     * 执行期末结账
     *
     * @param param 结账参数
     * @return 结账结果
     */
    PeriodEndClosingResult executePeriodEndClosing(PeriodEndClosingParam param);

    /**
     * 反向期末结账
     *
     * @param param 结账参数
     * @return 反结账结果
     */
    PeriodEndClosingResult reversePeriodEndClosing(PeriodEndClosingParam param);

    /**
     * 获取结账历史记录
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param accountingPeriod 会计期间
     * @param pageNumber 页码
     * @param pageSize 每页大小
     * @return 历史记录
     */
    List<PeriodEndClosingResult> getClosingHistory(Long bookId, Long tenantId, String accountingPeriod, Integer pageNumber, Integer pageSize);

    /**
     * 获取可用会计期间列表
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 期间列表
     */
    List<String> getAvailablePeriods(Long bookId, Long tenantId);

    /**
     * 获取损益结转预览
     *
     * @param param 结转参数
     * @return 预览结果
     */
    ProfitLossCarryForwardResult getProfitLossCarryForwardPreview(ProfitLossCarryForwardParam param);

    /**
     * 执行损益结转
     *
     * @param param 结转参数
     * @return 结转结果
     */
    ProfitLossCarryForwardResult executeProfitLossCarryForward(ProfitLossCarryForwardParam param);
}