package com.global.treasurer.financialProductDefinition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.financialProductDefinition.entity.TblProductPrincipalRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品本金规则Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface TblProductPrincipalRuleMapper extends BaseMapper<TblProductPrincipalRule> {

    int checkCodeUnique(@Param("ruleCode") String ruleCode, @Param("excludeId") Long excludeId);

    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("isEnabled") Integer isEnabled, @Param("updateBy") String updateBy);

    List<TblProductPrincipalRule> selectByProductType(@Param("productType") String productType, @Param("orgId") Long orgId);

    int countUsage(@Param("ruleId") Long ruleId);
}

