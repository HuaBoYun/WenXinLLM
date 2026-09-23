package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblSealCombination;
import com.global.treasurer.mapper.TblSealCombinationMapper;
import com.global.treasurer.service.TblSealCombinationService;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 印鉴组合配置Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-25
 */
@Service
public class TblSealCombinationServiceImpl extends ServiceImpl<TblSealCombinationMapper, TblSealCombination> implements TblSealCombinationService {
    @Autowired
    private TblSealCombinationMapper tblSealCombinationMapper;

    /**
     * 分页查询印鉴组合
     */
    @Override
    public IPage<TblSealCombination> getSealCombinationPage(Integer page, Integer limit,
            String combinationCode, String combinationName, String combinationType,
            String businessType, String authorityLevel) {

        Page<TblSealCombination> pageParam = new Page<>(page, limit);
        QueryWrapper<TblSealCombination> queryWrapper = new QueryWrapper<>();

        // 动态条件构建
        if (StringUtils.hasText(combinationCode)) {
            queryWrapper.like("COMBINATION_CODE", combinationCode);
        }
        if (StringUtils.hasText(combinationName)) {
            queryWrapper.like("COMBINATION_NAME", combinationName);
        }
        if (StringUtils.hasText(combinationType)) {
            queryWrapper.eq("COMBINATION_TYPE", combinationType);
        }
        if (StringUtils.hasText(businessType)) {
            queryWrapper.eq("BUSINESS_TYPE", businessType);
        }
        if (StringUtils.hasText(authorityLevel)) {
            queryWrapper.eq("AUTHORITY_LEVEL", authorityLevel);
        }

        // 按创建时间倒序
        queryWrapper.orderByDesc("CREATE_TIME");

        return this.page(pageParam, queryWrapper);
    }
}
