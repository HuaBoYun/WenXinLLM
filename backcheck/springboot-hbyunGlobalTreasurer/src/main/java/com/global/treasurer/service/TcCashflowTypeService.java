package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TcCashflowType;

import java.util.List;
import java.util.Map;

/**
 * 现金流类型Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
public interface TcCashflowTypeService extends IService<TcCashflowType> {

    /**
     * 分页查询现金流类型列表
     * @param params 查询参数
     * @return 现金流类型列表
     */
    List<TcCashflowType> selectPageList(Map<String, Object> params);

    /**
     * 根据ID查询现金流类型详情
     * @param id 主键ID
     * @return 现金流类型详情
     */
    TcCashflowType selectDetailById(Long id);

    /**
     * 保存现金流类型
     * @param entity 现金流类型实体
     * @return 是否成功
     */
    boolean saveCashflowType(TcCashflowType entity);

    /**
     * 更新现金流类型
     * @param entity 现金流类型实体
     * @return 是否成功
     */
    boolean updateCashflowType(TcCashflowType entity);

    /**
     * 删除现金流类型
     * @param id 主键ID
     * @return 是否成功
     */
    boolean deleteCashflowType(Long id);
}

