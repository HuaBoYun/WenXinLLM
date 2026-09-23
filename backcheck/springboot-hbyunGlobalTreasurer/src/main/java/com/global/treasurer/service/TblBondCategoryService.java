package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblBondCategory;

/**
 * 债券类别Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
public interface TblBondCategoryService {

    /**
     * 分页查询债券类别
     */
    PageInfo<TblBondCategory> getBondCategoryPage(Integer pageNum, Integer pageSize,
                                                  String categoryName, String categoryCode, String status);
}
