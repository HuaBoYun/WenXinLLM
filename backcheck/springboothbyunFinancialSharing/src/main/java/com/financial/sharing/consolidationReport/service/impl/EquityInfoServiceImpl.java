package com.financial.sharing.consolidationReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.EquityInfoQueryParam;
import com.financial.sharing.consolidationReport.entity.TblEquityInfo;
import com.financial.sharing.consolidationReport.mapper.EquityInfoMapper;
import com.financial.sharing.consolidationReport.service.EquityInfoService;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 股权信息Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class EquityInfoServiceImpl implements EquityInfoService {

    @Autowired
    private EquityInfoMapper equityInfoMapper;

    @Override
    public List<TblEquityInfo> getEquityList(EquityInfoQueryParam param) {
        return equityInfoMapper.selectEquityList(param);
    }

    @Override
    public TblEquityInfo getEquityById(String equityId) {
        return equityInfoMapper.selectById(equityId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEquity(TblEquityInfo equity) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 检查股权关系是否已存在
        int count = equityInfoMapper.checkEquityExists(
            equity.getModelId(), 
            equity.getParentOrgId(), 
            equity.getSubsidiaryOrgId(), 
            null
        );
        if (count > 0) {
            throw new RuntimeException("该股权关系已存在");
        }

        // 设置默认值
        equity.setEquityId(UUID.randomUUID().toString().replace("-", ""));
        equity.setTenantId(tenantId);
        equity.setCreateUser(userId);
        equity.setCreateTime(now);
        equity.setUpdateUser(userId);
        equity.setUpdateTime(now);

        if (equity.getIsActive() == null || equity.getIsActive().isEmpty()) {
            equity.setIsActive("Y");
        }

        equityInfoMapper.insert(equity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEquity(TblEquityInfo equity) {
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 检查股权关系是否已存在(排除自己)
        int count = equityInfoMapper.checkEquityExists(
            equity.getModelId(), 
            equity.getParentOrgId(), 
            equity.getSubsidiaryOrgId(), 
            equity.getEquityId()
        );
        if (count > 0) {
            throw new RuntimeException("该股权关系已存在");
        }

        equity.setUpdateUser(userId);
        equity.setUpdateTime(now);

        equityInfoMapper.updateById(equity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEquity(String equityId) {
        equityInfoMapper.deleteById(equityId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEquityStatus(String equityId, String isActive) {
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        TblEquityInfo equity = new TblEquityInfo();
        equity.setEquityId(equityId);
        equity.setIsActive(isActive);
        equity.setUpdateUser(userId);
        equity.setUpdateTime(now);

        equityInfoMapper.updateById(equity);
    }

    @Override
    public List<TblEquityInfo> getEquityListByModelId(String modelId) {
        return equityInfoMapper.selectByModelId(modelId);
    }
}

