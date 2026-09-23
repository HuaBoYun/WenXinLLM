package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.InvestmentProductDTO;
import com.global.treasurer.dto.InvestmentProductQueryDTO;
import com.global.treasurer.entity.TblInvestmentProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 投资产品服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface InvestmentProductService {

    /**
     * 分页查询投资产品列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblInvestmentProduct> getProductList(InvestmentProductQueryDTO queryDTO);

    /**
     * 根据ID查询投资产品详情
     *
     * @param productId 产品ID
     * @return 投资产品
     */
    TblInvestmentProduct getProductById(Long productId);

    /**
     * 保存投资产品（新增或更新）
     *
     * @param dto 投资产品DTO
     * @return 保存后的投资产品
     */
    TblInvestmentProduct saveProduct(InvestmentProductDTO dto);

    /**
     * 删除投资产品
     *
     * @param productId 产品ID
     */
    void deleteProduct(Long productId);

    /**
     * 批量删除投资产品
     *
     * @param productIds 产品ID列表
     */
    void batchDeleteProducts(List<Long> productIds);

    /**
     * 上架产品
     *
     * @param productId 产品ID
     */
    void launchProduct(Long productId);

    /**
     * 下架产品
     *
     * @param productId 产品ID
     * @param suspendReason 下架原因
     */
    void suspendProduct(Long productId, String suspendReason);

    /**
     * 更新产品净值
     *
     * @param productId 产品ID
     * @param netValue 净值
     */
    void updateNetValue(Long productId, BigDecimal netValue);

    /**
     * 获取产品统计信息
     *
     * @return 统计数据
     */
    Map<String, Object> getProductStatistics();

    /**
     * 导出产品数据
     *
     * @param queryDTO 查询条件
     * @return 产品列表
     */
    List<TblInvestmentProduct> exportProducts(InvestmentProductQueryDTO queryDTO);

    /**
     * 获取产品分析数据
     *
     * @return 分析数据
     */
    Map<String, Object> getProductAnalysis();

    /**
     * 获取产品趋势分析
     *
     * @param months 月数
     * @return 趋势数据
     */
    List<Map<String, Object>> getProductTrend(Integer months);
}

