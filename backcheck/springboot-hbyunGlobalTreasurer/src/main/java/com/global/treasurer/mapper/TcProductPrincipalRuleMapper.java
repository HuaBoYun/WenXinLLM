package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcProductPrincipalRule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 产品本金规则Mapper接口
 * @author hbyun-admin
 * @date 2024-01-01
 */
@Repository
public interface TcProductPrincipalRuleMapper extends Mapper<TcProductPrincipalRule> {

    /**
     * 根据产品ID查询本金规则
     * @param productId 产品ID
     * @return 本金规则列表
     */
    List<TcProductPrincipalRule> selectByProductId(@Param("productId") String productId);

    /**
     * 根据产品ID删除本金规则
     * @param productId 产品ID
     * @return 删除数量
     */
    int deleteByProductId(@Param("productId") String productId);

    /**
     * 批量插入本金规则
     * @param rules 本金规则列表
     * @return 插入数量
     */
    int batchInsert(@Param("rules") List<TcProductPrincipalRule> rules);

    /**
     * 根据本金类型查询规则
     * @param principalType 本金类型
     * @return 本金规则列表
     */
    List<TcProductPrincipalRule> selectByPrincipalType(@Param("principalType") String principalType);

    /**
     * 根据币种查询规则
     * @param currencyCode 币种代码
     * @return 本金规则列表
     */
    List<TcProductPrincipalRule> selectByCurrencyCode(@Param("currencyCode") String currencyCode);

    /**
     * 统计产品的本金规则数量
     * @param productId 产品ID
     * @return 规则数量
     */
    int countByProductId(@Param("productId") String productId);
}
