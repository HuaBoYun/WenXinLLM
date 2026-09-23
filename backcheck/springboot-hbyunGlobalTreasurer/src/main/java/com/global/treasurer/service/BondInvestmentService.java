package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BondInvestmentDTO;
import com.global.treasurer.dto.BondInvestmentQueryDTO;
import com.global.treasurer.entity.TblBondInvestment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 债券投资服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface BondInvestmentService {

    /**
     * 分页查询债券投资列表
     */
    PageInfo<TblBondInvestment> getInvestmentList(BondInvestmentQueryDTO queryDTO);

    /**
     * 根据ID查询债券投资详情
     */
    TblBondInvestment getInvestmentById(Long investmentId);

    /**
     * 保存债券投资（新增或更新）
     */
    TblBondInvestment saveInvestment(BondInvestmentDTO dto);

    /**
     * 删除债券投资
     */
    void deleteInvestment(Long investmentId);

    /**
     * 批量删除债券投资
     */
    void batchDeleteInvestments(List<Long> investmentIds);

    /**
     * 卖出债券
     */
    void sellBond(Long investmentId, BigDecimal sellPrice, Integer sellQuantity);

    /**
     * 更新市值
     */
    void updateMarketValue(Long investmentId, BigDecimal marketValue);

    /**
     * 获取投资统计信息
     */
    Map<String, Object> getInvestmentStatistics();

    /**
     * 获取即将到期的债券
     */
    List<TblBondInvestment> getMaturitySoon(Integer days);

    /**
     * 获取投资分析数据（按债券类型）
     */
    List<Map<String, Object>> getInvestmentAnalysisByType();

    /**
     * 获取投资分析数据（按信用评级）
     */
    List<Map<String, Object>> getInvestmentAnalysisByRating();

    /**
     * 导出投资数据
     */
    List<TblBondInvestment> exportInvestments(BondInvestmentQueryDTO queryDTO);
}

