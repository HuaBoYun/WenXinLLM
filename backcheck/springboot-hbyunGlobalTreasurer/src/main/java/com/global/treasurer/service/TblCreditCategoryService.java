package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblCreditCategory;

/**
 * 授信类别Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
public interface TblCreditCategoryService {

    /**
     * 分页查询授信类别
     *
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param categoryName 类别名称（模糊查询）
     * @param categoryCode 类别编码
     * @param status 状态
     * @return 分页结果
     */
    PageInfo<TblCreditCategory> getCreditCategoryPage(Integer pageNum, Integer pageSize,
                                                      String categoryName, String categoryCode, String status);

    /**
     * 根据ID查询授信类别
     *
     * @param id 类别ID
     * @return 授信类别实体
     */
    TblCreditCategory getById(Long id);

    /**
     * 新增授信类别
     *
     * @param category 授信类别实体
     * @return 是否成功
     */
    boolean save(TblCreditCategory category);

    /**
     * 更新授信类别
     *
     * @param category 授信类别实体
     * @return 是否成功
     */
    boolean updateById(TblCreditCategory category);

    /**
     * 删除授信类别
     *
     * @param id 类别ID
     * @return 是否成功
     */
    boolean removeById(Long id);
}
