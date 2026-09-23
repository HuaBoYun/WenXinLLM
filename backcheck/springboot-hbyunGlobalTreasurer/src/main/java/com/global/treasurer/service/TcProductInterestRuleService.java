package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcProductInterestRule;
import com.global.treasurer.util.PageResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 产品利息规则表 Service接口
 * @author hbyun-admin
 * @date 2024-01-01
 */
public interface TcProductInterestRuleService {

    /**
     * 分页查询产品利息规则
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<TcProductInterestRule> selectPage(Integer pageNum, Integer pageSize, Map<String, Object> params);

    /**
     * 根据ID查询产品利息规则
     * @param id 主键ID
     * @return 产品利息规则
     */
    TcProductInterestRule selectById(String id);

    /**
     * 新增产品利息规则
     * @param rule 产品利息规则
     * @return 影响行数
     */
    int insert(TcProductInterestRule rule);

    /**
     * 修改产品利息规则
     * @param rule 产品利息规则
     * @return 影响行数
     */
    int update(TcProductInterestRule rule);

    /**
     * 删除产品利息规则
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(String id);

    /**
     * 批量删除产品利息规则
     * @param ids 主键ID数组
     * @return 影响行数
     */
    int deleteByIds(String[] ids);

    /**
     * 根据产品ID查询利息规则
     * @param productId 产品ID
     * @return 利息规则列表
     */
    List<TcProductInterestRule> selectByProductId(String productId);

    /**
     * 验证利息规则数据
     * @param rule 利息规则
     * @return 验证错误信息列表
     */
    List<String> validateInterestRule(TcProductInterestRule rule);

    /**
     * 获取利率类型选项
     * @return 选项列表
     */
    List<Map<String, Object>> getRateTypeOptions();

    /**
     * 获取计息算法选项
     * @return 选项列表
     */
    List<Map<String, Object>> getCalculationMethodOptions();

    /**
     * 获取结息规则选项
     * @return 选项列表
     */
    List<Map<String, Object>> getSettlementRuleOptions();

    /**
     * 复制利息规则
     * @param sourceProductId 源产品ID
     * @param targetProductId 目标产品ID
     * @return 复制的规则数量
     */
    int copyInterestRules(String sourceProductId, String targetProductId);

    /**
     * 批量更新利息规则状态
     * @param ids 主键ID数组
     * @param status 状态
     * @return 影响行数
     */
    int batchUpdateStatus(String[] ids, String status);

    /**
     * 获取利息规则统计信息
     * @param productId 产品ID（可选）
     * @return 统计信息
     */
    Map<String, Object> getInterestRuleStatistics(String productId);

    // Controller需要的方法

    /**
     * 根据ID查询产品利息规则详情
     */
    TcProductInterestRule getProductInterestRuleById(String id);

    /**
     * 新增产品利息规则
     */
    boolean createProductInterestRule(TcProductInterestRule tcProductInterestRule);

    /**
     * 修改产品利息规则
     */
    boolean updateProductInterestRule(TcProductInterestRule tcProductInterestRule);

    /**
     * 删除产品利息规则
     */
    boolean deleteProductInterestRule(String id);

    /**
     * 批量删除产品利息规则
     */
    boolean deleteProductInterestRules(List<String> idList);

    /**
     * 启用/禁用产品利息规则
     */
    boolean updateProductInterestRuleStatus(String id, String status);

    /**
     * 查询所有启用的产品利息规则
     */
    List<TcProductInterestRule> getEnabledProductInterestRules(Long orgId);

    /**
     * 根据产品ID查询利息规则
     */
    List<TcProductInterestRule> getProductInterestRuleByProductId(String productId, Long orgId);

    /**
     * 计算利息
     */
    BigDecimal calculateInterest(String ruleId, BigDecimal principal, String startDate, String endDate);

    /**
     * 保存规则模板
     */
    boolean saveRuleTemplate(String templateName, String ruleId, String userId);

    /**
     * 应用规则模板
     */
    boolean applyRuleTemplate(String templateId, String productId, String userId);
}
