package com.financial.sharing.service;

import java.util.List;
import java.util.Map;

/**
 * 存货分类服务接口
 * 
 * @author system
 * @since 2026-01-26
 */
public interface InventoryCategoryService {

    /**
     * 获取存货分类树
     * 
     * @return 分类树列表
     */
    List<Map<String, Object>> getCategoryTree();

    /**
     * 根据ID查询分类详情
     * 
     * @param categoryId 分类ID
     * @return 分类详情
     */
    Map<String, Object> getCategoryById(Long categoryId);

    /**
     * 保存或更新存货分类
     * 
     * @param param 分类参数
     * @return 是否成功
     */
    boolean saveOrUpdateCategory(Map<String, Object> param);

    /**
     * 删除存货分类
     * 
     * @param categoryId 分类ID
     * @return 是否成功
     */
    boolean deleteCategory(Long categoryId);

    /**
     * 获取分类下的存货列表
     * 
     * @param categoryId 分类ID
     * @return 存货列表
     */
    List<Map<String, Object>> getInventoriesByCategory(Long categoryId);

    /**
     * 导出分类数据
     * 
     * @param param 查询参数
     * @return 导出文件路径
     */
    String exportCategory(Map<String, Object> param);
}

