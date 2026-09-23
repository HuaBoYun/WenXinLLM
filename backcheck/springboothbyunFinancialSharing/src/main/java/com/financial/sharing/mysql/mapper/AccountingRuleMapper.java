package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.mysql.entity.AccountingRuleEntity;
import com.financial.sharing.vo.param.AccountingRuleQueryParam;
import com.financial.sharing.vo.result.AccountingRuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 会计规则 Mapper接口 - MySQL版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Mapper
@Component("mysqlAccountingRuleMapper")
public interface AccountingRuleMapper extends BaseMapper<AccountingRuleEntity> {

    /**
     * 分页查询会计规则
     * 
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<AccountingRuleVO> selectAccountingRulePage(Page<AccountingRuleVO> page, @Param("param") AccountingRuleQueryParam param);

    /**
     * 根据规则编码查询会计规则
     * 
     * @param ruleCode 规则编码
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 会计规则
     */
    AccountingRuleEntity selectByRuleCode(@Param("ruleCode") String ruleCode,
                                         @Param("bookId") Long bookId,
                                         @Param("tenantId") Long tenantId,
                                         @Param("excludeId") Long excludeId);

    /**
     * 根据规则类型查询会计规则列表
     * 
     * @param ruleType 规则类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 会计规则列表
     */
    List<AccountingRuleVO> selectByRuleType(@Param("ruleType") Integer ruleType,
                                           @Param("bookId") Long bookId,
                                           @Param("tenantId") Long tenantId);

    /**
     * 根据事项类型查询会计规则列表
     * 
     * @param transactionType 事项类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 会计规则列表
     */
    List<AccountingRuleVO> selectByTransactionType(@Param("transactionType") String transactionType,
                                                  @Param("bookId") Long bookId,
                                                  @Param("tenantId") Long tenantId);

    /**
     * 根据启用状态查询会计规则列表
     * 
     * @param isEnabled 是否启用
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 会计规则列表
     */
    List<AccountingRuleVO> selectByEnabled(@Param("isEnabled") Integer isEnabled,
                                          @Param("bookId") Long bookId,
                                          @Param("tenantId") Long tenantId);

    /**
     * 批量删除会计规则
     * 
     * @param ids 规则ID列表
     * @param updater 更新人
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("updater") Long updater);

    /**
     * 批量更新启用状态
     * 
     * @param ids 规则ID列表
     * @param isEnabled 是否启用
     * @param updater 更新人
     * @return 更新数量
     */
    int batchUpdateEnabled(@Param("ids") List<Long> ids, 
                          @Param("isEnabled") Integer isEnabled, 
                          @Param("updater") Long updater);

    /**
     * 获取规则类型列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 规则类型列表
     */
    List<Integer> selectRuleTypes(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 获取事项类型列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 事项类型列表
     */
    List<String> selectTransactionTypes(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 统计规则数量按类型分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<AccountingRuleVO> countByRuleType(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 统计规则数量按事项类型分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<AccountingRuleVO> countByTransactionType(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);
}
