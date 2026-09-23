package com.global.treasurer.financialProductDefinition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.financialProductDefinition.entity.TblProductRiskControl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品风控规则Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface TblProductRiskControlMapper extends BaseMapper<TblProductRiskControl> {

    int checkCodeUnique(@Param("riskControlCode") String riskControlCode, @Param("excludeId") Long excludeId);

    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("isEnabled") Integer isEnabled, @Param("updateBy") String updateBy);

    List<TblProductRiskControl> selectByProductType(@Param("productType") String productType, @Param("orgId") Long orgId);

    int countUsage(@Param("riskControlId") Long riskControlId);
}

