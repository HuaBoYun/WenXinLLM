package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.RiskTypeDTO;
import com.global.treasurer.dto.RiskTypeQueryDTO;
import com.global.treasurer.entity.TblRiskType;
import com.global.treasurer.mapper.RiskTypeMapper;
import com.global.treasurer.service.IRiskTypeService;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 风险类型Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
@Service
public class RiskTypeServiceImpl extends ServiceImpl<RiskTypeMapper, TblRiskType> implements IRiskTypeService {
    @org.springframework.beans.factory.annotation.Autowired
    private com.hbfk.util.user.UserProvider userProvider;

    /**
     * 获取当前登录用户名
     */
    private String getLoginUserName() {
        try {
            com.hbfk.entity.TblStaffUtil user = userProvider.get();
            return user != null ? user.getUsername() : "system";
        } catch (Exception e) {
            return "system";
        }
    }

    @Override
    public PageInfo<TblRiskType> selectRiskTypeList(RiskTypeQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        LambdaQueryWrapper<TblRiskType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getRiskTypeCode()), TblRiskType::getRiskTypeCode, queryDTO.getRiskTypeCode())
                .like(StringUtils.hasText(queryDTO.getRiskTypeName()), TblRiskType::getRiskTypeName, queryDTO.getRiskTypeName())
                .eq(StringUtils.hasText(queryDTO.getRiskCategory()), TblRiskType::getRiskCategory, queryDTO.getRiskCategory())
                .eq(StringUtils.hasText(queryDTO.getImpactLevel()), TblRiskType::getImpactLevel, queryDTO.getImpactLevel())
                .eq(StringUtils.hasText(queryDTO.getProbabilityLevel()), TblRiskType::getProbabilityLevel, queryDTO.getProbabilityLevel())
                .eq(queryDTO.getIsEnabled() != null, TblRiskType::getIsEnabled, queryDTO.getIsEnabled())
                .eq(queryDTO.getOrgId() != null, TblRiskType::getOrgId, queryDTO.getOrgId())
                .orderByDesc(TblRiskType::getCreateTime);
        List<TblRiskType> list = this.list(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public TblRiskType selectRiskTypeById(Long typeId) {
        return this.getById(typeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRiskType insertRiskType(RiskTypeDTO dto) {
        TblRiskType entity = new TblRiskType();
        BeanUtils.copyProperties(dto, entity);
        entity.setCreateTime(new Date());
        entity.setCreateBy(getLoginUserName());
        entity.setIsEnabled(1);
        // 自动计算风险等级
        entity.setRiskLevel(calculateRiskLevel(dto.getImpactLevel(), dto.getProbabilityLevel()));
        this.save(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRiskType updateRiskType(RiskTypeDTO dto) {
        TblRiskType entity = this.getById(dto.getRiskTypeId());
        if (entity == null) {
            throw new RuntimeException("风险类型不存在");
        }
        BeanUtils.copyProperties(dto, entity);
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(getLoginUserName());
        entity.setRiskLevel(calculateRiskLevel(dto.getImpactLevel(), dto.getProbabilityLevel()));
        this.updateById(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRiskType(Long typeId) {
        return this.removeById(typeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleStatus(Long typeId, Integer status) {
        TblRiskType entity = this.getById(typeId);
        if (entity == null) {
            return false;
        }
        entity.setIsEnabled(status);
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(getLoginUserName());
        return this.updateById(entity);
    }

    @Override
    public Map<String, Object> getStatistics(Long orgId) {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<TblRiskType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(orgId != null, TblRiskType::getOrgId, orgId);
        
        List<TblRiskType> allTypes = this.list(wrapper);
        result.put("total", allTypes.size());
        result.put("enabled", allTypes.stream().filter(t -> t.getIsEnabled() == 1).count());
        result.put("disabled", allTypes.stream().filter(t -> t.getIsEnabled() == 0).count());
        
        // 按风险类别统计
        Map<String, Long> categoryCount = new HashMap<>();
        allTypes.forEach(t -> categoryCount.merge(t.getRiskCategory(), 1L, Long::sum));
        result.put("categoryDistribution", categoryCount);
        
        // 按风险等级统计
        Map<String, Long> levelCount = new HashMap<>();
        allTypes.forEach(t -> levelCount.merge(t.getRiskLevel(), 1L, Long::sum));
        result.put("levelDistribution", levelCount);
        
        return result;
    }

    /**
     * 计算风险等级
     */
    private String calculateRiskLevel(String impactLevel, String probabilityLevel) {
        int impactScore = getLevelScore(impactLevel);
        int probabilityScore = getLevelScore(probabilityLevel);
        int totalScore = impactScore * probabilityScore;
        if (totalScore >= 12) return "CRITICAL";
        if (totalScore >= 8) return "HIGH";
        if (totalScore >= 4) return "MEDIUM";
        return "LOW";
    }

    private int getLevelScore(String level) {
        if ("CRITICAL".equals(level)) return 4;
        if ("HIGH".equals(level)) return 3;
        if ("MEDIUM".equals(level)) return 2;
        return 1;
    }

    @Override
    public List<TblRiskType> selectRiskTypeExportList(RiskTypeQueryDTO queryDTO) {
        LambdaQueryWrapper<TblRiskType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getRiskTypeCode()), TblRiskType::getRiskTypeCode, queryDTO.getRiskTypeCode())
                .like(StringUtils.hasText(queryDTO.getRiskTypeName()), TblRiskType::getRiskTypeName, queryDTO.getRiskTypeName())
                .eq(StringUtils.hasText(queryDTO.getRiskCategory()), TblRiskType::getRiskCategory, queryDTO.getRiskCategory())
                .eq(StringUtils.hasText(queryDTO.getImpactLevel()), TblRiskType::getImpactLevel, queryDTO.getImpactLevel())
                .eq(StringUtils.hasText(queryDTO.getProbabilityLevel()), TblRiskType::getProbabilityLevel, queryDTO.getProbabilityLevel())
                .eq(queryDTO.getIsEnabled() != null, TblRiskType::getIsEnabled, queryDTO.getIsEnabled())
                .eq(queryDTO.getOrgId() != null, TblRiskType::getOrgId, queryDTO.getOrgId())
                .orderByDesc(TblRiskType::getCreateTime);
        return this.list(wrapper);
    }
}

