package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.InvestmentProductDTO;
import com.global.treasurer.dto.InvestmentProductQueryDTO;
import com.global.treasurer.entity.TblInvestmentProduct;
import com.global.treasurer.mapper.InvestmentProductMapper;
import com.global.treasurer.service.InvestmentProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投资产品服务实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@Service
public class InvestmentProductServiceImpl implements InvestmentProductService {

    private static final Logger log = LoggerFactory.getLogger(InvestmentProductServiceImpl.class);

    @Resource
    private InvestmentProductMapper investmentProductMapper;

    @Override
    public PageInfo<TblInvestmentProduct> getProductList(InvestmentProductQueryDTO queryDTO) {
        try {
            // 设置分页参数
            PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

            // 构建查询条件
            Map<String, Object> params = new HashMap<>();
            if (StringUtils.hasText(queryDTO.getProductCode())) {
                params.put("productCode", queryDTO.getProductCode());
            }
            if (StringUtils.hasText(queryDTO.getProductName())) {
                params.put("productName", queryDTO.getProductName());
            }
            if (StringUtils.hasText(queryDTO.getProductType())) {
                params.put("productType", queryDTO.getProductType());
            }
            if (StringUtils.hasText(queryDTO.getIssuer())) {
                params.put("issuer", queryDTO.getIssuer());
            }
            if (StringUtils.hasText(queryDTO.getRiskLevel())) {
                params.put("riskLevel", queryDTO.getRiskLevel());
            }
            if (StringUtils.hasText(queryDTO.getProductStatus())) {
                params.put("productStatus", queryDTO.getProductStatus());
            }
            if (queryDTO.getMinReturnRate() != null) {
                params.put("minReturnRate", queryDTO.getMinReturnRate());
            }
            if (queryDTO.getMaxReturnRate() != null) {
                params.put("maxReturnRate", queryDTO.getMaxReturnRate());
            }
            if (queryDTO.getLaunchDateStart() != null) {
                params.put("launchDateStart", queryDTO.getLaunchDateStart());
            }
            if (queryDTO.getLaunchDateEnd() != null) {
                params.put("launchDateEnd", queryDTO.getLaunchDateEnd());
            }
            if (queryDTO.getMaturityDateStart() != null) {
                params.put("maturityDateStart", queryDTO.getMaturityDateStart());
            }
            if (queryDTO.getMaturityDateEnd() != null) {
                params.put("maturityDateEnd", queryDTO.getMaturityDateEnd());
            }
            if (StringUtils.hasText(queryDTO.getCurrencyCode())) {
                params.put("currencyCode", queryDTO.getCurrencyCode());
            }
            if (queryDTO.getCompanyId() != null) {
                params.put("companyId", queryDTO.getCompanyId());
            }

            // 执行查询
            List<TblInvestmentProduct> list = investmentProductMapper.selectProductList(params);

            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("查询投资产品列表失败", e);
            throw new RuntimeException("查询投资产品列表失败: " + e.getMessage());
        }
    }

    @Override
    public TblInvestmentProduct getProductById(Long productId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("产品ID不能为空");
            }

            TblInvestmentProduct product = investmentProductMapper.selectProductById(productId);
            if (product == null) {
                throw new RuntimeException("未找到产品信息");
            }

            return product;
        } catch (Exception e) {
            log.error("获取投资产品详情失败, productId: {}", productId, e);
            throw new RuntimeException("获取投资产品详情失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblInvestmentProduct saveProduct(InvestmentProductDTO dto) {
        try {
            TblInvestmentProduct product = new TblInvestmentProduct();
            BeanUtils.copyProperties(dto, product);

            if (dto.getProductId() != null) {
                // 更新
                TblInvestmentProduct existingProduct = investmentProductMapper.selectProductById(dto.getProductId());
                if (existingProduct == null) {
                    throw new RuntimeException("未找到要更新的产品信息");
                }

                product.setUpdatedBy(1L); // TODO: 从用户上下文获取
                product.setUpdatedByName("系统管理员"); // TODO: 从用户上下文获取
                product.setUpdatedTime(new Date());

                investmentProductMapper.updateById(product);
                log.info("更新投资产品成功, productId: {}", dto.getProductId());
            } else {
                // 新增
                product.setProductStatus("ACTIVE"); // 默认状态为活跃
                product.setDeleteFlag(0); // 默认未删除
                product.setCreatedBy(1L); // TODO: 从用户上下文获取
                product.setCreatedByName("系统管理员"); // TODO: 从用户上下文获取
                product.setCreatedTime(new Date());

                investmentProductMapper.insert(product);
                log.info("新增投资产品成功, productId: {}", product.getProductId());
            }

            return product;
        } catch (Exception e) {
            log.error("保存投资产品失败", e);
            throw new RuntimeException("保存投资产品失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long productId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("产品ID不能为空");
            }

            TblInvestmentProduct product = investmentProductMapper.selectProductById(productId);
            if (product == null) {
                throw new RuntimeException("未找到要删除的产品信息");
            }

            // 逻辑删除
            product.setDeleteFlag(1);
            product.setUpdatedBy(1L); // TODO: 从用户上下文获取
            product.setUpdatedByName("系统管理员"); // TODO: 从用户上下文获取
            product.setUpdatedTime(new Date());

            investmentProductMapper.updateById(product);
            log.info("删除投资产品成功, productId: {}", productId);
        } catch (Exception e) {
            log.error("删除投资产品失败, productId: {}", productId, e);
            throw new RuntimeException("删除投资产品失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteProducts(List<Long> productIds) {
        try {
            if (productIds == null || productIds.isEmpty()) {
                throw new IllegalArgumentException("产品ID列表不能为空");
            }

            investmentProductMapper.batchDeleteByIds(productIds);
            log.info("批量删除投资产品成功, count: {}", productIds.size());
        } catch (Exception e) {
            log.error("批量删除投资产品失败", e);
            throw new RuntimeException("批量删除投资产品失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void launchProduct(Long productId) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("产品ID不能为空");
            }

            TblInvestmentProduct product = investmentProductMapper.selectProductById(productId);
            if (product == null) {
                throw new RuntimeException("未找到产品信息");
            }

            // 更新产品状态为活跃
            investmentProductMapper.updateProductStatus(productId, "ACTIVE");

            // 清空下架原因
            product.setSuspendReason(null);
            product.setUpdatedBy(1L); // TODO: 从用户上下文获取
            product.setUpdatedByName("系统管理员"); // TODO: 从用户上下文获取
            product.setUpdatedTime(new Date());
            investmentProductMapper.updateById(product);

            log.info("上架投资产品成功, productId: {}", productId);
        } catch (Exception e) {
            log.error("上架投资产品失败, productId: {}", productId, e);
            throw new RuntimeException("上架投资产品失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void suspendProduct(Long productId, String suspendReason) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("产品ID不能为空");
            }

            TblInvestmentProduct product = investmentProductMapper.selectProductById(productId);
            if (product == null) {
                throw new RuntimeException("未找到产品信息");
            }

            // 更新产品状态为暂停
            investmentProductMapper.updateProductStatus(productId, "SUSPENDED");

            // 设置下架原因
            product.setSuspendReason(suspendReason);
            product.setUpdatedBy(1L); // TODO: 从用户上下文获取
            product.setUpdatedByName("系统管理员"); // TODO: 从用户上下文获取
            product.setUpdatedTime(new Date());
            investmentProductMapper.updateById(product);

            log.info("下架投资产品成功, productId: {}, reason: {}", productId, suspendReason);
        } catch (Exception e) {
            log.error("下架投资产品失败, productId: {}", productId, e);
            throw new RuntimeException("下架投资产品失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNetValue(Long productId, BigDecimal netValue) {
        try {
            if (productId == null) {
                throw new IllegalArgumentException("产品ID不能为空");
            }
            if (netValue == null || netValue.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("净值必须大于等于0");
            }

            TblInvestmentProduct product = investmentProductMapper.selectProductById(productId);
            if (product == null) {
                throw new RuntimeException("未找到产品信息");
            }

            // 更新净值
            investmentProductMapper.updateProductNetValue(productId, netValue);

            // 更新时间
            product.setUpdatedBy(1L); // TODO: 从用户上下文获取
            product.setUpdatedByName("系统管理员"); // TODO: 从用户上下文获取
            product.setUpdatedTime(new Date());
            investmentProductMapper.updateById(product);

            log.info("更新投资产品净值成功, productId: {}, netValue: {}", productId, netValue);
        } catch (Exception e) {
            log.error("更新投资产品净值失败, productId: {}, netValue: {}", productId, netValue, e);
            throw new RuntimeException("更新投资产品净值失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getProductStatistics() {
        try {
            Map<String, Object> statistics = investmentProductMapper.selectProductStatistics();
            if (statistics == null) {
                statistics = new HashMap<>();
            }
            return statistics;
        } catch (Exception e) {
            log.error("获取产品统计信息失败", e);
            throw new RuntimeException("获取产品统计信息失败: " + e.getMessage());
        }
    }

    @Override
    public List<TblInvestmentProduct> exportProducts(InvestmentProductQueryDTO queryDTO) {
        try {
            // 构建查询条件
            Map<String, Object> params = new HashMap<>();
            if (StringUtils.hasText(queryDTO.getProductCode())) {
                params.put("productCode", queryDTO.getProductCode());
            }
            if (StringUtils.hasText(queryDTO.getProductName())) {
                params.put("productName", queryDTO.getProductName());
            }
            if (StringUtils.hasText(queryDTO.getProductType())) {
                params.put("productType", queryDTO.getProductType());
            }
            if (StringUtils.hasText(queryDTO.getIssuer())) {
                params.put("issuer", queryDTO.getIssuer());
            }
            if (StringUtils.hasText(queryDTO.getRiskLevel())) {
                params.put("riskLevel", queryDTO.getRiskLevel());
            }
            if (StringUtils.hasText(queryDTO.getProductStatus())) {
                params.put("productStatus", queryDTO.getProductStatus());
            }
            if (queryDTO.getMinReturnRate() != null) {
                params.put("minReturnRate", queryDTO.getMinReturnRate());
            }
            if (queryDTO.getMaxReturnRate() != null) {
                params.put("maxReturnRate", queryDTO.getMaxReturnRate());
            }
            if (queryDTO.getLaunchDateStart() != null) {
                params.put("launchDateStart", queryDTO.getLaunchDateStart());
            }
            if (queryDTO.getLaunchDateEnd() != null) {
                params.put("launchDateEnd", queryDTO.getLaunchDateEnd());
            }
            if (queryDTO.getMaturityDateStart() != null) {
                params.put("maturityDateStart", queryDTO.getMaturityDateStart());
            }
            if (queryDTO.getMaturityDateEnd() != null) {
                params.put("maturityDateEnd", queryDTO.getMaturityDateEnd());
            }
            if (StringUtils.hasText(queryDTO.getCurrencyCode())) {
                params.put("currencyCode", queryDTO.getCurrencyCode());
            }
            if (queryDTO.getCompanyId() != null) {
                params.put("companyId", queryDTO.getCompanyId());
            }

            // 执行查询
            return investmentProductMapper.selectProductList(params);
        } catch (Exception e) {
            log.error("导出投资产品数据失败", e);
            throw new RuntimeException("导出投资产品数据失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getProductAnalysis() {
        try {
            Map<String, Object> analysis = new HashMap<>();

            // 构建查询条件获取所有产品
            Map<String, Object> params = new HashMap<>();
            List<TblInvestmentProduct> allProducts = investmentProductMapper.selectProductList(params);

            // 统计分析
            int totalProducts = allProducts.size();
            BigDecimal totalMinInvestment = BigDecimal.ZERO;
            BigDecimal avgReturnRate = BigDecimal.ZERO;
            int activeProducts = 0;

            for (TblInvestmentProduct product : allProducts) {
                if (product.getMinInvestmentAmount() != null) {
                    totalMinInvestment = totalMinInvestment.add(product.getMinInvestmentAmount());
                }
                if (product.getExpectedReturnRate() != null) {
                    avgReturnRate = avgReturnRate.add(product.getExpectedReturnRate());
                }
                if ("ACTIVE".equals(product.getProductStatus()) || "LISTED".equals(product.getProductStatus())) {
                    activeProducts++;
                }
            }

            // 计算平均收益率
            if (totalProducts > 0) {
                avgReturnRate = avgReturnRate.divide(new BigDecimal(totalProducts), 2, BigDecimal.ROUND_HALF_UP);
            }

            analysis.put("totalProducts", totalProducts);
            analysis.put("activeProducts", activeProducts);
            analysis.put("totalMinInvestment", totalMinInvestment);
            analysis.put("avgReturnRate", avgReturnRate);
            analysis.put("inactiveProducts", totalProducts - activeProducts);

            return analysis;
        } catch (Exception e) {
            log.error("获取产品分析数据失败", e);
            throw new RuntimeException("获取产品分析数据失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getProductTrend(Integer months) {
        try {
            // 返回模拟趋势数据
            List<Map<String, Object>> trendList = new java.util.ArrayList<>();

            for (int i = 0; i < months; i++) {
                Map<String, Object> trendData = new HashMap<>();
                trendData.put("month", "第" + (i + 1) + "月");
                trendData.put("investmentAmount", new java.util.Random().nextInt(1000000));
                trendData.put("returnValue", new java.util.Random().nextInt(100000));
                trendData.put("productCount", new java.util.Random().nextInt(50));
                trendList.add(trendData);
            }

            return trendList;
        } catch (Exception e) {
            log.error("获取产品趋势分析失败", e);
            throw new RuntimeException("获取产品趋势分析失败: " + e.getMessage());
        }
    }
}
