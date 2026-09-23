package com.financial.sharing.enterpriseReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.enterpriseReport.dto.IndicatorQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblIndicatorInfo;
import com.financial.sharing.enterpriseReport.mapper.IndicatorMapper;
import com.financial.sharing.enterpriseReport.service.IndicatorService;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 指标信息服务实现类
 * 
 * @author system
 * @since 2026-01-30
 */
@Slf4j
@Service
public class IndicatorServiceImpl implements IndicatorService {

    @Autowired
    private IndicatorMapper indicatorMapper;

    @Override
    public Page<TblIndicatorInfo> getPage(IndicatorQueryParam param) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        Page<TblIndicatorInfo> page = new Page<>(param.getPageNumber(), param.getPageSize());
        
        LambdaQueryWrapper<TblIndicatorInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblIndicatorInfo::getTenantId, tenantId);
        
        if (param.getIndicatorCode() != null && !param.getIndicatorCode().trim().isEmpty()) {
            wrapper.like(TblIndicatorInfo::getIndicatorCode, param.getIndicatorCode());
        }
        if (param.getIndicatorName() != null && !param.getIndicatorName().trim().isEmpty()) {
            wrapper.like(TblIndicatorInfo::getIndicatorName, param.getIndicatorName());
        }
        if (param.getIndicatorType() != null && !param.getIndicatorType().trim().isEmpty()) {
            wrapper.eq(TblIndicatorInfo::getIndicatorType, param.getIndicatorType());
        }
        if (param.getAggregateType() != null && !param.getAggregateType().trim().isEmpty()) {
            wrapper.eq(TblIndicatorInfo::getAggregateType, param.getAggregateType());
        }
        if (param.getStatus() != null && !param.getStatus().trim().isEmpty()) {
            wrapper.eq(TblIndicatorInfo::getStatus, param.getStatus());
        }
        
        wrapper.orderByDesc(TblIndicatorInfo::getCreateTime);
        
        return indicatorMapper.selectPage(page, wrapper);
    }

    @Override
    public TblIndicatorInfo getDetail(String indicatorId) {
        return indicatorMapper.selectById(indicatorId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(TblIndicatorInfo indicator) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();
        
        indicator.setTenantId(tenantId);
        
        if (indicator.getIndicatorId() == null || indicator.getIndicatorId().trim().isEmpty()) {
            // 新增
            indicator.setCreateUser(userId);
            indicator.setCreateTime(now);
            indicator.setUpdateUser(userId);
            indicator.setUpdateTime(now);
            
            // 检查编码是否存在
            int count = indicatorMapper.checkCodeExists(indicator.getIndicatorCode(), tenantId, "");
            if (count > 0) {
                throw new RuntimeException("指标编码已存在");
            }
            
            return indicatorMapper.insert(indicator) > 0;
        } else {
            // 修改
            indicator.setUpdateUser(userId);
            indicator.setUpdateTime(now);
            
            // 检查编码是否存在
            int count = indicatorMapper.checkCodeExists(indicator.getIndicatorCode(), tenantId, indicator.getIndicatorId());
            if (count > 0) {
                throw new RuntimeException("指标编码已存在");
            }
            
            return indicatorMapper.updateById(indicator) > 0;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String indicatorId) {
        return indicatorMapper.deleteById(indicatorId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<String> indicatorIds) {
        if (indicatorIds == null || indicatorIds.isEmpty()) {
            return false;
        }
        return indicatorMapper.deleteBatchIds(indicatorIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(String indicatorId, String status) {
        TblIndicatorInfo indicator = indicatorMapper.selectById(indicatorId);
        if (indicator == null) {
            throw new RuntimeException("指标不存在");
        }
        
        indicator.setStatus(status);
        indicator.setUpdateUser(UserUtils.getUser().getStaffid().toString());
        indicator.setUpdateTime(new Date());
        
        return indicatorMapper.updateById(indicator) > 0;
    }

    @Override
    public boolean checkCodeExists(String indicatorCode, String excludeId) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String exclude = excludeId == null ? "" : excludeId;
        int count = indicatorMapper.checkCodeExists(indicatorCode, tenantId, exclude);
        return count > 0;
    }
}

