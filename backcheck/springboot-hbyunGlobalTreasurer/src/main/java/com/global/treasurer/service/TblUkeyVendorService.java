package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblUkeyVendor;
import com.global.treasurer.vo.param.TblUkeyVendorQueryParam;

import java.util.List;
import java.util.Map;

/**
 * Ukey厂商管理Service接口
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
public interface TblUkeyVendorService extends IService<TblUkeyVendor> {

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<TblUkeyVendor> selectPage(TblUkeyVendorQueryParam param);

    /**
     * 根据ID查询
     *
     * @param id 主键
     * @return 实体对象
     */
    TblUkeyVendor selectById(String id);

    /**
     * 查询列表
     *
     * @param param 查询参数
     * @return 列表结果
     */
    List<TblUkeyVendor> selectList(TblUkeyVendorQueryParam param);

    /**
     * 插入
     *
     * @param vendor 实体对象
     * @return 影响行数
     */
    int insert(TblUkeyVendor vendor);

    /**
     * 更新
     *
     * @param vendor 实体对象
     * @return 影响行数
     */
    int update(TblUkeyVendor vendor);

    /**
     * 删除
     *
     * @param id 主键
     * @return 影响行数
     */
    int delete(String id);

    /**
     * 获取统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 获取厂商类型选项
     *
     * @return 厂商类型选项列表
     */
    List<Map<String, String>> getVendorTypes();

    /**
     * 获取厂商等级选项
     *
     * @return 厂商等级选项列表
     */
    List<Map<String, String>> getVendorLevels();

    /**
     * 获取合作状态选项
     *
     * @return 合作状态选项列表
     */
    List<Map<String, String>> getCooperationStatuses();

    /**
     * 获取评价等级选项
     *
     * @return 评价等级选项列表
     */
    List<Map<String, String>> getEvaluationLevels();
}