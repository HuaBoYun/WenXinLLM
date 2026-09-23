package com.financial.sharing.groupControl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.groupControl.dto.DimensionQueryParam;
import com.financial.sharing.groupControl.entity.TblDimensionInfo;
import com.financial.sharing.groupControl.mapper.DimensionMapper;
import com.financial.sharing.groupControl.service.DimensionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 维度信息Service实现类
 * 
 * @author 华博云开发团队
 * @since 2026-01-30
 */
@Slf4j
@Service
public class DimensionServiceImpl implements DimensionService {

    @Autowired
    private DimensionMapper dimensionMapper;

    @Override
    public Page<TblDimensionInfo> getList(DimensionQueryParam param) {
        Page<TblDimensionInfo> page = new Page<>(param.getPageNumber(), param.getPageSize());
        
        LambdaQueryWrapper<TblDimensionInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(param.getTenantId()), 
                   TblDimensionInfo::getTenantId, param.getTenantId());
        wrapper.like(StringUtils.hasText(param.getDimensionCode()), 
                     TblDimensionInfo::getDimensionCode, param.getDimensionCode());
        wrapper.like(StringUtils.hasText(param.getDimensionName()), 
                     TblDimensionInfo::getDimensionName, param.getDimensionName());
        wrapper.eq(StringUtils.hasText(param.getDimensionType()), 
                   TblDimensionInfo::getDimensionType, param.getDimensionType());
        wrapper.eq(StringUtils.hasText(param.getDimensionCategory()), 
                   TblDimensionInfo::getDimensionCategory, param.getDimensionCategory());
        wrapper.eq(StringUtils.hasText(param.getStatus()), 
                   TblDimensionInfo::getStatus, param.getStatus());
        wrapper.orderByDesc(TblDimensionInfo::getCreateTime);
        
        return dimensionMapper.selectPage(page, wrapper);
    }

    @Override
    public TblDimensionInfo getDetail(String dimensionId) {
        if (!StringUtils.hasText(dimensionId)) {
            return null;
        }
        return dimensionMapper.selectById(dimensionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(TblDimensionInfo dimension) {
        if (dimension == null) {
            return false;
        }

        // 检查维度编码是否重复
        if (checkCodeExists(dimension.getDimensionCode(), dimension.getTenantId(), 
                           dimension.getDimensionId())) {
            throw new RuntimeException("维度编码已存在");
        }

        // 设置默认值
        if (!StringUtils.hasText(dimension.getStatus())) {
            dimension.setStatus("ACTIVE");
        }
        if (!StringUtils.hasText(dimension.getIsHierarchy())) {
            dimension.setIsHierarchy("N");
        }
        if (!StringUtils.hasText(dimension.getIsDefault())) {
            dimension.setIsDefault("N");
        }
        if (dimension.getMaxLevel() == null) {
            dimension.setMaxLevel(5);
        }

        if (StringUtils.hasText(dimension.getDimensionId())) {
            // 修改
            dimension.setUpdateTime(new Date());
            return dimensionMapper.updateById(dimension) > 0;
        } else {
            // 新增
            dimension.setCreateTime(new Date());
            dimension.setUpdateTime(new Date());
            return dimensionMapper.insert(dimension) > 0;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String dimensionId) {
        if (!StringUtils.hasText(dimensionId)) {
            return false;
        }
        return dimensionMapper.deleteById(dimensionId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<String> dimensionIds) {
        if (dimensionIds == null || dimensionIds.isEmpty()) {
            return false;
        }
        return dimensionMapper.deleteBatchIds(dimensionIds) > 0;
    }

    @Override
    public boolean checkCodeExists(String dimensionCode, String tenantId, String excludeId) {
        if (!StringUtils.hasText(dimensionCode) || !StringUtils.hasText(tenantId)) {
            return false;
        }

        LambdaQueryWrapper<TblDimensionInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblDimensionInfo::getDimensionCode, dimensionCode);
        wrapper.eq(TblDimensionInfo::getTenantId, tenantId);
        if (StringUtils.hasText(excludeId)) {
            wrapper.ne(TblDimensionInfo::getDimensionId, excludeId);
        }

        return dimensionMapper.selectCount(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(String dimensionId, String status) {
        if (!StringUtils.hasText(dimensionId) || !StringUtils.hasText(status)) {
            return false;
        }

        TblDimensionInfo dimension = new TblDimensionInfo();
        dimension.setDimensionId(dimensionId);
        dimension.setStatus(status);
        dimension.setUpdateTime(new Date());

        return dimensionMapper.updateById(dimension) > 0;
    }
}

