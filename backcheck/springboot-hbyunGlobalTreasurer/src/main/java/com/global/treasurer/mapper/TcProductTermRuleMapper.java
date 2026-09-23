package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcProductTermRule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 产品期限规则Mapper接口
 * @author hbyun-admin
 * @date 2024-01-01
 */
@Repository
public interface TcProductTermRuleMapper extends Mapper<TcProductTermRule> {

    /**
     * 根据产品ID查询期限规则
     * @param productId 产品ID
     * @return 期限规则列表
     */
    List<TcProductTermRule> selectByProductId(@Param("productId") String productId);

    /**
     * 根据产品ID删除期限规则
     * @param productId 产品ID
     * @return 删除数量
     */
    int deleteByProductId(@Param("productId") String productId);

    /**
     * 批量插入期限规则
     * @param rules 期限规则列表
     * @return 插入数量
     */
    int batchInsert(@Param("rules") List<TcProductTermRule> rules);

    /**
     * 根据期限类型查询规则
     * @param termType 期限类型
     * @return 期限规则列表
     */
    List<TcProductTermRule> selectByTermType(@Param("termType") String termType);

    /**
     * 根据期限范围查询规则
     * @param minTerm 最小期限
     * @param maxTerm 最大期限
     * @param termUnit 期限单位
     * @return 期限规则列表
     */
    List<TcProductTermRule> selectByTermRange(@Param("minTerm") Integer minTerm, 
                                              @Param("maxTerm") Integer maxTerm, 
                                              @Param("termUnit") String termUnit);

    /**
     * 统计产品的期限规则数量
     * @param productId 产品ID
     * @return 规则数量
     */
    int countByProductId(@Param("productId") String productId);
}
