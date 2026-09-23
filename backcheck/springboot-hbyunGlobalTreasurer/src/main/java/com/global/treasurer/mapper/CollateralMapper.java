package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblCollateral;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 担保物Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface CollateralMapper extends BaseMapper<TblCollateral> {

    List<TblCollateral> selectCollateralList(Map<String, Object> params);

    TblCollateral selectCollateralById(@Param("collateralId") Long collateralId);

    int updateCollateralStatus(@Param("collateralId") Long collateralId, @Param("status") String status);

    int batchDeleteByIds(@Param("collateralIds") List<Long> collateralIds);

    Map<String, Object> selectCollateralSummary(@Param("companyId") Long companyId);

    List<TblCollateral> selectAvailableCollaterals(@Param("companyId") Long companyId);
}

