package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TcFinancialCategory;

import java.util.List;
import java.util.Map;

/**
 * 财务类别Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
public interface TcFinancialCategoryService extends IService<TcFinancialCategory> {

    /**
     * 分页查询财务类别列表
     * @param params 查询参数
     * @return 财务类别列表
     */
    List<TcFinancialCategory> selectPageList(Map<String, Object> params);

    /**
     * 根据ID查询财务类别详情
     * @param id 主键ID
     * @return 财务类别详情
     */
    TcFinancialCategory selectDetailById(Long id);

    /**
     * 保存财务类别
     * @param entity 财务类别实体
     * @return 是否成功
     */
    boolean saveCategory(TcFinancialCategory entity);

    /**
     * 更新财务类别
     * @param entity 财务类别实体
     * @return 是否成功
     */
    boolean updateCategory(TcFinancialCategory entity);

    /**
     * 删除财务类别
     * @param id 主键ID
     * @return 是否成功
     */
    boolean deleteCategory(Long id);
}

