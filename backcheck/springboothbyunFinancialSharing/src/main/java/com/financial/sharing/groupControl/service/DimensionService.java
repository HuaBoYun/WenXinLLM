package com.financial.sharing.groupControl.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.groupControl.dto.DimensionQueryParam;
import com.financial.sharing.groupControl.entity.TblDimensionInfo;

import java.util.List;

/**
 * 维度信息Service接口
 * 
 * @author 华博云开发团队
 * @since 2026-01-30
 */
public interface DimensionService {

    /**
     * 分页查询维度列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    Page<TblDimensionInfo> getList(DimensionQueryParam param);

    /**
     * 根据ID查询维度详情
     * 
     * @param dimensionId 维度ID
     * @return 维度信息
     */
    TblDimensionInfo getDetail(String dimensionId);

    /**
     * 保存维度信息(新增或修改)
     * 
     * @param dimension 维度信息
     * @return 是否成功
     */
    boolean save(TblDimensionInfo dimension);

    /**
     * 删除维度
     * 
     * @param dimensionId 维度ID
     * @return 是否成功
     */
    boolean delete(String dimensionId);

    /**
     * 批量删除维度
     * 
     * @param dimensionIds 维度ID列表
     * @return 是否成功
     */
    boolean batchDelete(List<String> dimensionIds);

    /**
     * 检查维度编码是否存在
     * 
     * @param dimensionCode 维度编码
     * @param tenantId 租户ID
     * @param excludeId 排除的维度ID(修改时使用)
     * @return 是否存在
     */
    boolean checkCodeExists(String dimensionCode, String tenantId, String excludeId);

    /**
     * 启用/停用维度
     * 
     * @param dimensionId 维度ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(String dimensionId, String status);
}

