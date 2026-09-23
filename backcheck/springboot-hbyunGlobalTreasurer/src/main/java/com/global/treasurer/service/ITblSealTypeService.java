package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblSealType;

import java.util.List;
import java.util.Map;

/**
 * 印鉴类型Service接口
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
public interface ITblSealTypeService extends IService<TblSealType> {

    /**
     * 根据条件分页查询印鉴类型
     *
     * @param params 查询参数
     * @return 印鉴类型分页数据
     */
    Map<String, Object> selectByPage(Map<String, Object> params);

    /**
     * 创建印鉴类型
     *
     * @param sealType 印鉴类型
     * @return 是否成功
     */
    boolean createSealType(TblSealType sealType);

    /**
     * 更新印鉴类型
     *
     * @param sealType 印鉴类型
     * @return 是否成功
     */
    boolean updateSealType(TblSealType sealType);

    /**
     * 删除印鉴类型
     *
     * @param id 印鉴类型ID
     * @return 是否成功
     */
    boolean deleteSealType(Long id);

    /**
     * 更新印鉴类型状态
     *
     * @param id 印鉴类型ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 获取启用的印鉴类型选项
     *
     * @return 印鉴类型列表
     */
    List<TblSealType> getActiveTypes();
}
