package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.InventoryAlertRuleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 预警规则 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2026-01-26
 */
@Component("oracleInventoryAlertRuleMapper")
public interface InventoryAlertRuleMapper extends BaseMapper<InventoryAlertRuleEntity> {

    /**
     * 分页查询预警规则列表
     *
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<Map<String, Object>> selectRulePage(Page<Map<String, Object>> page, @Param("param") Map<String, Object> param);

    /**
     * 根据规则类型查询规则列表
     *
     * @param ruleType 规则类型
     * @param tenantId 租户ID
     * @return 规则列表
     */
    List<InventoryAlertRuleEntity> selectByRuleType(@Param("ruleType") String ruleType, @Param("tenantId") Long tenantId);

    /**
     * 根据分类ID查询规则列表
     *
     * @param categoryId 分类ID
     * @param tenantId 租户ID
     * @return 规则列表
     */
    List<InventoryAlertRuleEntity> selectByCategoryId(@Param("categoryId") Long categoryId, @Param("tenantId") Long tenantId);

    /**
     * 根据存货ID查询规则列表
     *
     * @param inventoryId 存货ID
     * @param tenantId 租户ID
     * @return 规则列表
     */
    List<InventoryAlertRuleEntity> selectByInventoryId(@Param("inventoryId") Long inventoryId, @Param("tenantId") Long tenantId);

    /**
     * 查询启用的规则列表
     *
     * @param tenantId 租户ID
     * @return 规则列表
     */
    List<InventoryAlertRuleEntity> selectEnabledRules(@Param("tenantId") Long tenantId);

    /**
     * 批量启用/禁用规则
     *
     * @param ruleIds 规则ID列表
     * @param status 状态
     * @param updaterId 更新人ID
     * @return 更新数量
     */
    int batchToggleStatus(@Param("ruleIds") List<Long> ruleIds,
                         @Param("status") Integer status,
                         @Param("updaterId") String updaterId);
}

