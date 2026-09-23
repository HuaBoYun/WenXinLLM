package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BankWealthInvestmentDTO;
import com.global.treasurer.dto.BankWealthInvestmentQueryDTO;
import com.global.treasurer.entity.TblBankWealthInvestment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 银行理财投资服务接口
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
public interface BankWealthInvestmentService {

    /**
     * 分页查询银行理财投资列表
     */
    PageInfo<TblBankWealthInvestment> getBankWealthInvestmentList(BankWealthInvestmentQueryDTO queryDTO);

    /**
     * 根据ID获取银行理财投资详情
     */
    TblBankWealthInvestment getBankWealthInvestmentById(Long investmentId);

    /**
     * 保存银行理财投资（新增或更新）
     */
    TblBankWealthInvestment saveBankWealthInvestment(BankWealthInvestmentDTO dto);

    /**
     * 删除银行理财投资
     */
    void deleteBankWealthInvestment(Long investmentId);

    /**
     * 批量删除银行理财投资
     */
    void batchDeleteBankWealthInvestments(List<Long> investmentIds);

    /**
     * 认购银行理财
     */
    void subscribeProduct(BankWealthInvestmentDTO dto);

    /**
     * 赎回银行理财
     */
    void redeemProduct(Long investmentId, BigDecimal redeemAmount, String redeemDate);

    /**
     * 更新估值
     */
    void updateValuation(Long investmentId, BigDecimal newValue);

    /**
     * 更新实际收益
     */
    void updateActualReturn(Long investmentId, BigDecimal actualReturn);

    /**
     * 获取统计信息
     */
    Map<String, Object> getBankWealthStatistics();
}
