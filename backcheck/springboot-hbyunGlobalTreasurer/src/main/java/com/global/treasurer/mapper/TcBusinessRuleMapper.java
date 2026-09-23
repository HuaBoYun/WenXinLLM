package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcBusinessRule;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 财资公共模块 - 业务规则管理Mapper
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
public interface TcBusinessRuleMapper extends Mapper<TcBusinessRule> {

    /**
     * 根据规则编码查询
     * 
     * @param ruleCode 规则编码
     * @return 业务规则信息
     */
    TcBusinessRule selectByRuleCode(@Param("ruleCode") String ruleCode);

    /**
     * 根据条件查询业务规则列表
     * 
     * @param ruleCode 规则编码
     * @param ruleName 规则名称
     * @param ruleType 规则类型
     * @param ruleCategory 规则分类
     * @param businessDomain 业务域
     * @param isActive 是否激活
     * @param status 状态
     * @return 业务规则列表
     */
    List<TcBusinessRule> selectByCondition(@Param("ruleCode") String ruleCode,
                                           @Param("ruleName") String ruleName,
                                           @Param("ruleType") String ruleType,
                                           @Param("ruleCategory") String ruleCategory,
                                           @Param("businessDomain") String businessDomain,
                                           @Param("isActive") String isActive,
                                           @Param("status") String status);

    /**
     * 根据规则类型查询
     * 
     * @param ruleType 规则类型
     * @return 业务规则列表
     */
    List<TcBusinessRule> selectByRuleType(@Param("ruleType") String ruleType);

    /**
     * 根据业务域查询
     * 
     * @param businessDomain 业务域
     * @return 业务规则列表
     */
    List<TcBusinessRule> selectByBusinessDomain(@Param("businessDomain") String businessDomain);

    /**
     * 根据规则分类查询
     * 
     * @param ruleCategory 规则分类
     * @return 业务规则列表
     */
    List<TcBusinessRule> selectByRuleCategory(@Param("ruleCategory") String ruleCategory);

    /**
     * 查询激活的规则
     * 
     * @param ruleType 规则类型
     * @param businessDomain 业务域
     * @return 激活的业务规则列表
     */
    List<TcBusinessRule> selectActiveRules(@Param("ruleType") String ruleType,
                                           @Param("businessDomain") String businessDomain);

    /**
     * 根据执行顺序查询规则
     * 
     * @param ruleType 规则类型
     * @param businessDomain 业务域
     * @return 按执行顺序排序的业务规则列表
     */
    List<TcBusinessRule> selectByExecutionOrder(@Param("ruleType") String ruleType,
                                                @Param("businessDomain") String businessDomain);

    /**
     * 查询有效期内的规则
     * 
     * @param currentDate 当前日期
     * @param ruleType 规则类型
     * @param businessDomain 业务域
     * @return 有效期内的业务规则列表
     */
    List<TcBusinessRule> selectValidRules(@Param("currentDate") Date currentDate,
                                          @Param("ruleType") String ruleType,
                                          @Param("businessDomain") String businessDomain);

    /**
     * 查询所有规则类型
     * 
     * @return 规则类型列表
     */
    List<String> selectAllRuleTypes();

    /**
     * 查询所有规则分类
     * 
     * @return 规则分类列表
     */
    List<String> selectAllRuleCategories();

    /**
     * 查询所有业务域
     * 
     * @return 业务域列表
     */
    List<String> selectAllBusinessDomains();

    /**
     * 查询所有规则语法
     * 
     * @return 规则语法列表
     */
    List<String> selectAllRuleSyntax();

    /**
     * 批量插入业务规则
     * 
     * @param businessRules 业务规则列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<TcBusinessRule> businessRules);

    /**
     * 批量更新业务规则
     * 
     * @param businessRules 业务规则列表
     * @return 更新数量
     */
    int batchUpdate(@Param("list") List<TcBusinessRule> businessRules);

    /**
     * 更新规则状态
     * 
     * @param id 规则ID
     * @param status 状态
     * @param updateUser 更新人
     * @return 更新数量
     */
    int updateStatus(@Param("id") String id,
                     @Param("status") String status,
                     @Param("updateUser") String updateUser);

    /**
     * 更新规则激活状态
     * 
     * @param id 规则ID
     * @param isActive 是否激活
     * @param updateUser 更新人
     * @return 更新数量
     */
    int updateActiveStatus(@Param("id") String id,
                           @Param("isActive") String isActive,
                           @Param("updateUser") String updateUser);

    /**
     * 统计规则数据
     * 
     * @return 统计结果
     */
    List<Map<String, Object>> getStatistics();

    /**
     * 检查规则编码是否存在
     * 
     * @param ruleCode 规则编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    int checkRuleCodeExists(@Param("ruleCode") String ruleCode,
                            @Param("excludeId") String excludeId);

    /**
     * 根据规则类型和业务域统计规则数量
     * 
     * @param ruleType 规则类型
     * @param businessDomain 业务域
     * @return 规则数量
     */
    int countByTypeAndDomain(@Param("ruleType") String ruleType,
                             @Param("businessDomain") String businessDomain);

    /**
     * 查询即将过期的规则
     *
     * @param days 天数
     * @return 即将过期的规则列表
     */
    List<TcBusinessRule> selectExpiringRules(@Param("days") int days);

    // Service实现类中调用的方法别名
    default List<TcBusinessRule> getList(String ruleCode, String ruleName, String ruleType, String ruleCategory, String businessDomain, String isActive, String status) {
        return selectByCondition(ruleCode, ruleName, ruleType, ruleCategory, businessDomain, isActive, status);
    }

    default TcBusinessRule getByRuleCode(String ruleCode) {
        return selectByRuleCode(ruleCode);
    }

    default List<TcBusinessRule> getByRuleType(String ruleType) {
        return selectByRuleType(ruleType);
    }

    default List<TcBusinessRule> getByBusinessDomain(String businessDomain) {
        return selectByBusinessDomain(businessDomain);
    }

    default List<TcBusinessRule> getByRuleCategory(String ruleCategory) {
        return selectByRuleCategory(ruleCategory);
    }

    default List<TcBusinessRule> getActiveRules(String ruleType, String businessDomain) {
        return selectActiveRules(ruleType, businessDomain);
    }

    default List<TcBusinessRule> getByExecutionOrder(String ruleType, String businessDomain) {
        return selectByExecutionOrder(ruleType, businessDomain);
    }

    default List<TcBusinessRule> getValidRules(Date currentDate, String ruleType, String businessDomain) {
        return selectValidRules(currentDate, ruleType, businessDomain);
    }

    default List<String> getAllRuleTypes() {
        return selectAllRuleTypes();
    }

    default List<String> getAllRuleCategories() {
        return selectAllRuleCategories();
    }

    default List<String> getAllBusinessDomains() {
        return selectAllBusinessDomains();
    }

    default List<String> getAllRuleSyntax() {
        return selectAllRuleSyntax();
    }
}
