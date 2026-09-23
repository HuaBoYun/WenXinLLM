package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.mysql.entity.RuleVoucherEntity;
import com.financial.sharing.vo.param.RuleVoucherQueryParam;
import com.financial.sharing.vo.result.RuleVoucherVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 规则凭证 Mapper接口 - MySQL版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Mapper
@Component("mysqlRuleVoucherMapper")
public interface RuleVoucherMapper extends BaseMapper<RuleVoucherEntity> {

    /**
     * 分页查询规则凭证
     * 
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<RuleVoucherVO> selectRuleVoucherPage(Page<RuleVoucherVO> page, @Param("param") RuleVoucherQueryParam param);

    /**
     * 根据规则编码查询规则凭证
     * 
     * @param ruleCode 规则编码
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 规则凭证
     */
    RuleVoucherEntity selectByRuleCode(@Param("ruleCode") String ruleCode,
                                      @Param("bookId") Long bookId,
                                      @Param("tenantId") Long tenantId,
                                      @Param("excludeId") Long excludeId);

    /**
     * 根据规则类型查询规则凭证列表
     * 
     * @param ruleType 规则类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 规则凭证列表
     */
    List<RuleVoucherVO> selectByRuleType(@Param("ruleType") Integer ruleType,
                                        @Param("bookId") Long bookId,
                                        @Param("tenantId") Long tenantId);

    /**
     * 根据执行期间查询规则凭证列表
     * 
     * @param executionPeriod 执行期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 规则凭证列表
     */
    List<RuleVoucherVO> selectByExecutionPeriod(@Param("executionPeriod") String executionPeriod,
                                               @Param("bookId") Long bookId,
                                               @Param("tenantId") Long tenantId);

    /**
     * 根据启用状态查询规则凭证列表
     * 
     * @param isEnabled 是否启用
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 规则凭证列表
     */
    List<RuleVoucherVO> selectByEnabled(@Param("isEnabled") Integer isEnabled,
                                       @Param("bookId") Long bookId,
                                       @Param("tenantId") Long tenantId);

    /**
     * 批量删除规则凭证
     * 
     * @param ids 规则凭证ID列表
     * @param updater 更新人
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("updater") Long updater);

    /**
     * 批量更新启用状态
     * 
     * @param ids 规则凭证ID列表
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
     * 获取执行期间列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 执行期间列表
     */
    List<String> selectExecutionPeriods(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 统计规则凭证数量按类型分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<RuleVoucherVO> countByRuleType(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 统计规则凭证数量按期间分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<RuleVoucherVO> countByExecutionPeriod(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);
}
