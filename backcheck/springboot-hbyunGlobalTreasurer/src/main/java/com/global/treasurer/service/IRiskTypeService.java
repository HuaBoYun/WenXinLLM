package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.RiskTypeDTO;
import com.global.treasurer.dto.RiskTypeQueryDTO;
import com.global.treasurer.entity.TblRiskType;

import java.util.List;
import java.util.Map;

/**
 * 风险类型Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
public interface IRiskTypeService extends IService<TblRiskType> {

    /**
     * 分页查询风险类型
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblRiskType> selectRiskTypeList(RiskTypeQueryDTO queryDTO);

    /**
     * 根据ID查询风险类型
     *
     * @param typeId 类型ID
     * @return 风险类型
     */
    TblRiskType selectRiskTypeById(Long typeId);

    /**
     * 新增风险类型
     *
     * @param dto 风险类型DTO
     * @return 新增的风险类型
     */
    TblRiskType insertRiskType(RiskTypeDTO dto);

    /**
     * 更新风险类型
     *
     * @param dto 风险类型DTO
     * @return 更新后的风险类型
     */
    TblRiskType updateRiskType(RiskTypeDTO dto);

    /**
     * 删除风险类型
     *
     * @param typeId 类型ID
     * @return 是否成功
     */
    boolean deleteRiskType(Long typeId);

    /**
     * 切换状态
     *
     * @param typeId 类型ID
     * @param status 状态
     * @return 是否成功
     */
    boolean toggleStatus(Long typeId, Integer status);

    /**
     * 获取统计信息
     *
     * @param orgId 组织ID
     * @return 统计数据
     */
    Map<String, Object> getStatistics(Long orgId);

    /**
     * 查询导出数据（不分页）
     *
     * @param queryDTO 查询条件
     * @return 风险类型列表
     */
    List<TblRiskType> selectRiskTypeExportList(RiskTypeQueryDTO queryDTO);
}

