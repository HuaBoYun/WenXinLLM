package com.global.treasurer.service;

import com.global.treasurer.entity.TcProductTermRule;
import com.global.treasurer.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 产品期限规则表 Service接口
 * @author hbyun-admin
 * @date 2024-01-01
 */
public interface TcProductTermRuleService {

    /**
     * 分页查询产品期限规则
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<TcProductTermRule> selectPage(Integer pageNum, Integer pageSize, Map<String, Object> params);

    /**
     * 根据ID查询产品期限规则
     * @param id 主键ID
     * @return 产品期限规则
     */
    TcProductTermRule selectById(String id);

    /**
     * 新增产品期限规则
     * @param rule 产品期限规则
     * @return 新增结果
     */
    int insert(TcProductTermRule rule);

    /**
     * 修改产品期限规则
     * @param rule 产品期限规则
     * @return 修改结果
     */
    int update(TcProductTermRule rule);

    /**
     * 删除产品期限规则
     * @param id 主键ID
     * @return 删除结果
     */
    int deleteById(String id);

    /**
     * 批量删除产品期限规则
     * @param ids 主键ID数组
     * @return 删除结果
     */
    int deleteByIds(String[] ids);

    /**
     * 根据产品ID查询期限规则
     * @param productId 产品ID
     * @return 期限规则列表
     */
    List<TcProductTermRule> selectByProductId(String productId);

    /**
     * 验证期限规则
     * @param rule 期限规则
     * @return 验证结果
     */
    List<String> validateTermRule(TcProductTermRule rule);

    /**
     * 获取期限类型选项
     * @return 期限类型选项
     */
    List<Map<String, Object>> getTermTypeOptions();

    /**
     * 获取期限计算方式选项
     * @return 期限计算方式选项
     */
    List<Map<String, Object>> getCalculationMethodOptions();

    /**
     * 获取提前终止规则选项
     * @return 提前终止规则选项
     */
    List<Map<String, Object>> getEarlyTerminationOptions();

    /**
     * 获取展期规则选项
     * @return 展期规则选项
     */
    List<Map<String, Object>> getExtensionRuleOptions();

    /**
     * 复制期限规则
     * @param sourceProductId 源产品ID
     * @param targetProductId 目标产品ID
     * @return 复制结果
     */
    int copyTermRules(String sourceProductId, String targetProductId);

    /**
     * 批量更新期限规则状态
     * @param ids 规则ID数组
     * @param status 状态
     * @return 更新结果
     */
    int batchUpdateStatus(String[] ids, String status);

    /**
     * 获取期限规则统计信息
     * @param productId 产品ID
     * @return 统计信息
     */
    Map<String, Object> getTermRuleStatistics(String productId);
}
