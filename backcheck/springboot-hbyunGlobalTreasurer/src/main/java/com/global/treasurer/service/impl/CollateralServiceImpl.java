package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.CollateralDTO;
import com.global.treasurer.dto.CollateralQueryDTO;
import com.global.treasurer.entity.TblCollateral;
import com.global.treasurer.mapper.CollateralMapper;
import com.global.treasurer.service.CollateralService;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CollateralServiceImpl implements CollateralService {
    @Autowired
    private CollateralMapper collateralMapper;

    @Override
    public PageInfo<TblCollateral> getCollateralList(CollateralQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("collateralType", queryDTO.getCollateralType());
        params.put("collateralStatus", queryDTO.getStatus());
        params.put("companyId", queryDTO.getCompanyId());
        params.put("currencyCode", queryDTO.getCurrencyCode());
        List<TblCollateral> list = collateralMapper.selectCollateralList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblCollateral getCollateralById(Long collateralId) {
        TblCollateral collateral = collateralMapper.selectCollateralById(collateralId);
        if (collateral == null) {
            throw new ServiceException(404, "担保物不存在");
        }
        return collateral;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblCollateral saveCollateral(CollateralDTO dto) {
        TblCollateral collateral = new TblCollateral();
        BeanUtils.copyProperties(dto, collateral);

        if (dto.getCollateralId() == null) {
            collateral.setCollateralNo("COL" + System.currentTimeMillis());
            collateral.setCollateralStatus("ACTIVE");
            collateral.setDeleteFlag(0);
            collateral.setCreatedTime(new Date());
            collateralMapper.insert(collateral);
        } else {
            collateral.setUpdatedTime(new Date());
            collateralMapper.updateById(collateral);
        }
        return collateral;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCollateral(Long collateralId) {
        TblCollateral collateral = getCollateralById(collateralId);
        if (!"ACTIVE".equals(collateral.getCollateralStatus())) {
            throw new ServiceException(400, "只能删除正常状态的担保物");
        }
        collateral.setDeleteFlag(1);
        collateral.setUpdatedTime(new Date());
        collateralMapper.updateById(collateral);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteCollaterals(List<Long> collateralIds) {
        collateralMapper.batchDeleteByIds(collateralIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mortgage(Long collateralId, BigDecimal amount) {
        TblCollateral collateral = getCollateralById(collateralId);
        collateral.setCollateralStatus("MORTGAGED");
        collateral.setUpdatedTime(new Date());
        collateralMapper.updateById(collateral);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pledge(Long collateralId, BigDecimal amount) {
        TblCollateral collateral = getCollateralById(collateralId);
        collateral.setCollateralStatus("PLEDGED");
        collateral.setUpdatedTime(new Date());
        collateralMapper.updateById(collateral);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void release(Long collateralId) {
        TblCollateral collateral = getCollateralById(collateralId);
        collateral.setCollateralStatus("ACTIVE");
        collateral.setUpdatedTime(new Date());
        collateralMapper.updateById(collateral);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void valuate(Long collateralId, BigDecimal value, String evaluationAgency) {
        TblCollateral collateral = getCollateralById(collateralId);
        collateral.setEvaluationValue(value);
        collateral.setEvaluationAgency(evaluationAgency);
        collateral.setEvaluationDate(new Date());
        collateral.setUpdatedTime(new Date());
        collateralMapper.updateById(collateral);
    }

    @Override
    public List<TblCollateral> getAvailableCollaterals(Long companyId) {
        return collateralMapper.selectAvailableCollaterals(companyId);
    }

    @Override
    public Map<String, Object> getCollateralSummary(Long companyId) {
        return collateralMapper.selectCollateralSummary(companyId);
    }
}

