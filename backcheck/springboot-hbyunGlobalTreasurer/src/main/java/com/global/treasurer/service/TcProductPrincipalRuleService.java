package com.global.treasurer.service;

import com.global.treasurer.entity.TcProductPrincipalRule;
import com.global.treasurer.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 产品本金规则Service接口
 * @author hbyun-admin
 * @date 2024-01-01
 */
public interface TcProductPrincipalRuleService {

    /**
     * 分页查询产品本金规则
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<TcProductPrincipalRule> selectByPage(int pageNum, int pageSize, Map<String, Object> params);

    /**
     * 根据ID查询产品本金规则
     * @param id 主键ID
     * @return 产品本金规则
     */
    TcProductPrincipalRule selectById(String id);

    /**
     * 根据产品ID查询本金规则
     * @param productId 产品ID
     * @return 本金规则列表
     */
    List<TcProductPrincipalRule> selectByProductId(String productId);

    /**
     * 新增产品本金规则
     * @param rule 产品本金规则
     * @return 操作结果
     */
    int insert(TcProductPrincipalRule rule);

    /**
     * 修改产品本金规则
     * @param rule 产品本金规则
     * @return 操作结果
     */
    int update(TcProductPrincipalRule rule);

    /**
     * 删除产品本金规则
     * @param id 主键ID
     * @return 操作结果
     */
    int deleteById(String id);

    /**
     * 批量删除产品本金规则
     * @param ids 主键ID数组
     * @return 操作结果
     */
    int deleteByIds(String[] ids);

    /**
     * 根据产品ID删除本金规则
     * @param productId 产品ID
     * @return 操作结果
     */
    int deleteByProductId(String productId);

    /**
     * 批量保存产品本金规则
     * @param productId 产品ID
     * @param rules 本金规则列表
     * @return 操作结果
     */
    int batchSave(String productId, List<TcProductPrincipalRule> rules);

    /**
     * 验证本金规则配置
     * @param rule 本金规则
     * @return 验证结果
     */
    Map<String, Object> validateRule(TcProductPrincipalRule rule);

    /**
     * 根据本金类型统计规则数量
     * @param principalType 本金类型
     * @return 规则数量
     */
    int countByPrincipalType(String principalType);

    /**
     * 获取本金类型选项
     * @return 本金类型选项列表
     */
    List<Map<String, Object>> getPrincipalTypeOptions();

    /**
     * 获取还本赎回方式选项
     * @return 还本赎回方式选项列表
     */
    List<Map<String, Object>> getRepaymentMethodOptions();

    /**
     * 复制产品本金规则
     * @param sourceProductId 源产品ID
     * @param targetProductId 目标产品ID
     * @return 操作结果
     */
    int copyRules(String sourceProductId, String targetProductId);
}
