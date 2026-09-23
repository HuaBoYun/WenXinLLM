package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.CollateralDTO;
import com.global.treasurer.dto.CollateralQueryDTO;
import com.global.treasurer.entity.TblCollateral;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 担保物服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface CollateralService {

    PageInfo<TblCollateral> getCollateralList(CollateralQueryDTO queryDTO);

    TblCollateral getCollateralById(Long collateralId);

    TblCollateral saveCollateral(CollateralDTO dto);

    void deleteCollateral(Long collateralId);

    void batchDeleteCollaterals(List<Long> collateralIds);

    void mortgage(Long collateralId, BigDecimal amount);

    void pledge(Long collateralId, BigDecimal amount);

    void release(Long collateralId);

    void valuate(Long collateralId, BigDecimal value, String valuationCompany);

    List<TblCollateral> getAvailableCollaterals(Long companyId);

    Map<String, Object> getCollateralSummary(Long companyId);
}

