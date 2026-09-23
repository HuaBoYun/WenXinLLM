package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblSealType;
import com.global.treasurer.mapper.TblSealTypeMapper;
import com.global.treasurer.service.TblSealTypeService;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 印鉴类型Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-25
 */
@Service
public class TblSealTypeServiceImpl extends ServiceImpl<TblSealTypeMapper, TblSealType> implements TblSealTypeService {
    @Autowired
    private TblSealTypeMapper tblSealTypeMapper;

    /**
     * 分页查询印鉴类型
     */
    @Override
    public IPage<TblSealType> getSealTypePage(Integer page, Integer limit,
            String name, String sealLevel, String status) {

        Page<TblSealType> pageParam = new Page<>(page, limit);
        QueryWrapper<TblSealType> queryWrapper = new QueryWrapper<>();

        // 动态条件构建
        if (StringUtils.hasText(name)) {
            queryWrapper.like("TYPE_NAME", name);
        }
        if (StringUtils.hasText(sealLevel)) {
            queryWrapper.eq("SEAL_LEVEL", sealLevel);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq("IS_ACTIVE", status);
        }

        // 按创建时间倒序
        queryWrapper.orderByDesc("CREATE_TIME");

        return this.page(pageParam, queryWrapper);
    }
}
