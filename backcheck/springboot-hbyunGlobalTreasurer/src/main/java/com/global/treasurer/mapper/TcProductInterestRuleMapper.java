package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcProductInterestRule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品利息规则Mapper接口
 * @author hbyun-admin
 * @date 2024-01-01
 */
@Repository
public interface TcProductInterestRuleMapper extends Mapper<TcProductInterestRule> {

    /**
     * 根据产品ID查询利息规则
     * @param productId 产品ID
     * @return 利息规则列表
     */
    List<TcProductInterestRule> selectByProductId(@Param("productId") String productId);

    /**
     * 根据产品ID删除利息规则
     * @param productId 产品ID
     * @return 删除数量
     */
    int deleteByProductId(@Param("productId") String productId);

    /**
     * 批量插入利息规则
     * @param rules 利息规则列表
     * @return 插入数量
     */
    int batchInsert(@Param("rules") List<TcProductInterestRule> rules);

    /**
     * 根据利率类型查询规则
     * @param interestRateType 利率类型
     * @return 利息规则列表
     */
    List<TcProductInterestRule> selectByInterestRateType(@Param("interestRateType") String interestRateType);

    /**
     * 根据计息方法查询规则
     * @param calculationMethod 计息方法
     * @return 利息规则列表
     */
    List<TcProductInterestRule> selectByCalculationMethod(@Param("calculationMethod") String calculationMethod);

    /**
     * 根据利率范围查询规则
     * @param minRate 最小利率
     * @param maxRate 最大利率
     * @return 利息规则列表
     */
    List<TcProductInterestRule> selectByRateRange(@Param("minRate") BigDecimal minRate, 
                                                  @Param("maxRate") BigDecimal maxRate);

    /**
     * 统计产品的利息规则数量
     * @param productId 产品ID
     * @return 规则数量
     */
    int countByProductId(@Param("productId") String productId);
}
