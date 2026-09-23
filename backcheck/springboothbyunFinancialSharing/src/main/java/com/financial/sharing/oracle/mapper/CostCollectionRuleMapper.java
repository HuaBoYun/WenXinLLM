package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.CostCollectionRuleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 成本归集规则Mapper接口
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
public interface CostCollectionRuleMapper extends BaseMapper<CostCollectionRuleEntity> {

    /**
     * 根据成本中心ID查询归集规则
     *
     * @param costCenterId 成本中心ID
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 规则列表
     */
    List<Map<String, Object>> selectRulesByCenterId(@Param("costCenterId") String costCenterId,
                                                     @Param("bookId") String bookId,
                                                     @Param("tenantId") String tenantId);

    /**
     * 查询所有启用的归集规则
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 规则列表
     */
    List<Map<String, Object>> selectEnabledRules(@Param("bookId") String bookId,
                                                  @Param("tenantId") String tenantId);

    /**
     * 批量更新规则状态
     *
     * @param ruleIds 规则ID列表
     * @param isEnabled 启用状态
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("ruleIds") List<String> ruleIds,
                          @Param("isEnabled") Integer isEnabled);

    /**
     * 检查规则名称是否存在
     *
     * @param ruleName 规则名称
     * @param ruleId 规则ID（更新时排除自己）
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 数量
     */
    int checkRuleNameExists(@Param("ruleName") String ruleName,
                           @Param("ruleId") String ruleId,
                           @Param("bookId") String bookId,
                           @Param("tenantId") String tenantId);
}

