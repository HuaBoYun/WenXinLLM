package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblInvestmentProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 投资产品Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface InvestmentProductMapper extends BaseMapper<TblInvestmentProduct> {

    /**
     * 分页查询投资产品列表
     *
     * @param params 查询参数
     * @return 投资产品列表
     */
    List<TblInvestmentProduct> selectProductList(Map<String, Object> params);

    /**
     * 根据ID查询投资产品详情
     *
     * @param productId 产品ID
     * @return 投资产品
     */
    TblInvestmentProduct selectProductById(@Param("productId") Long productId);

    /**
     * 根据产品代码查询
     *
     * @param productCode 产品代码
     * @return 投资产品
     */
    TblInvestmentProduct selectByProductCode(@Param("productCode") String productCode);

    /**
     * 统计投资产品数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countProductList(Map<String, Object> params);

    /**
     * 更新产品状态
     *
     * @param productId 产品ID
     * @param status 状态
     * @return 影响行数
     */
    int updateProductStatus(@Param("productId") Long productId, @Param("status") String status);

    /**
     * 更新产品净值
     *
     * @param productId 产品ID
     * @param netValue 净值
     * @return 影响行数
     */
    int updateProductNetValue(@Param("productId") Long productId, @Param("netValue") java.math.BigDecimal netValue);

    /**
     * 批量删除投资产品（逻辑删除）
     *
     * @param productIds 产品ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("productIds") List<Long> productIds);

    /**
     * 查询产品统计信息
     *
     * @return 统计数据
     */
    Map<String, Object> selectProductStatistics();
}

