package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblSealType;

/**
 * 印鉴类型Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-25
 */
public interface TblSealTypeService extends IService<TblSealType> {

    /**
     * 分页查询印鉴类型
     */
    IPage<TblSealType> getSealTypePage(Integer page, Integer limit,
            String name, String sealLevel, String status);
}
